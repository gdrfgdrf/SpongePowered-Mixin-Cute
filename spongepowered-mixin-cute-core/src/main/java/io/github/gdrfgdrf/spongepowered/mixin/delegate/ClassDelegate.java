/*
 * Copyright 2024 SpongePowered Mixin Cute Project's Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.gdrfgdrf.spongepowered.mixin.delegate;

import io.github.gdrfgdrf.spongepowered.mixin.CuteMixin;
import io.github.gdrfgdrf.spongepowered.mixin.service.CuteMixinService;
import io.github.gdrfgdrf.spongepowered.mixin.loader.CuteClassLoader;
import io.github.gdrfgdrf.spongepowered.mixin.utils.ClassUtils;
import io.github.gdrfgdrf.spongepowered.mixin.utils.FileSystemUtils;
import io.github.gdrfgdrf.spongepowered.mixin.utils.ManifestUtils;
import io.github.gdrfgdrf.spongepowered.mixin.utils.UrlUtils;
import lombok.Getter;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Manifest;

/**
 * 类委托，参考了 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassDelegate
 * @author gdrfgdrf
 */
public class ClassDelegate {
    @Getter
    private IMixinTransformer mixinTransformer;
    private final CuteClassLoader classLoader;
    private final Set<String> parentSourcedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Map<String, Metadata> metadataCache = new ConcurrentHashMap<>();

    public ClassDelegate(CuteClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassDelegate#initializeTransformers
    public void initialize() {
        if (mixinTransformer != null) {
            return;
        }

        mixinTransformer = CuteMixinService.getInstance().getTransformer();
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassDelegate#tryLoadClass
    public Class<?> tryLoadClass(String name, boolean allowFromParent) {
        if (name.startsWith("java.")) {
            return null;
        }

        if (!allowFromParent && !parentSourcedClasses.isEmpty()) {
            int pos = name.length();

            while ((pos = name.lastIndexOf('$', pos - 1)) > 0) {
                if (parentSourcedClasses.contains(name.substring(0, pos))) {
                    allowFromParent = true;
                    break;
                }
            }
        }

        byte[] input = getPostMixinClassByteArray(name, allowFromParent);
        if (input == null) {
            return null;
        }

        if (allowFromParent) {
            parentSourcedClasses.add(name);
        }

        Metadata metadata = getMetadata(name, classLoader.getResource(ClassUtils.getClassFileName(name)));

        int pkgDelimiterPos = name.lastIndexOf('.');

        if (pkgDelimiterPos > 0) {
            // TODO: package definition stub
            String pkgString = name.substring(0, pkgDelimiterPos);

            if (classLoader.getPackage(pkgString) == null) {
                try {
                    classLoader.definePackage(pkgString, null, null, null, null, null, null, null);
                } catch (IllegalArgumentException e) { // presumably concurrent package definition
                    if (classLoader.getPackage(pkgString) == null) {
                        throw e; // still not defined?
                    }
                }
            }
        }

        return classLoader.defineClassFwd(name, input, 0, input.length, metadata.getCodeSource());
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassDelegate#getPreMixinClassByteArray
    public byte[] getPreMixinClassByteArray(String name, boolean allowFromParent) {
        // some of the transformers rely on dot notation
        name = name.replace('/', '.');

        if (mixinTransformer == null || !canTransformClass(name)) {
            try {
                return getRawClassByteArray(name, allowFromParent);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load class file for '" + name + "'!", e);
            }
        }

        byte[] input = CuteMixin.getInstance().getGameTransformer().transform(name);

        if (input == null) {
            try {
                input = getRawClassByteArray(name, allowFromParent);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load class file for '" + name + "'!", e);
            }
        }

        return input;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassDelegate#getPostMixinClassByteArray
    public byte[] getPostMixinClassByteArray(String name, boolean allowFromParent) {
        byte[] transformedClassArray = getPreMixinClassByteArray(name, allowFromParent);

        if (mixinTransformer == null || !canTransformClass(name)) {
            return transformedClassArray;
        }

        try {
            return getMixinTransformer().transformClassBytes(name, name, transformedClassArray);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassDelegate#getRawClassByteArray
    public byte[] getRawClassByteArray(String name, boolean allowFromParent) throws IOException {
        InputStream inputStream = classLoader.getResourceAsStream(ClassUtils.getClassFileName(name), allowFromParent);
        if (inputStream == null) {
            return null;
        }

        int a = inputStream.available();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(a < 32 ? 32768 : a);
        byte[] buffer = new byte[8192];
        int len;

        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }

        inputStream.close();
        return outputStream.toByteArray();
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassDelegate#getMetadata(java.lang.String, java.net.URL)
    private Metadata getMetadata(String name, URL resourceURL) {
        if (resourceURL == null) {
            return Metadata.EMPTY;
        }

        URL codeSourceUrl = null;

        try {
            codeSourceUrl = UrlUtils.getSource(ClassUtils.getClassFileName(name), resourceURL);
        } catch (Exception e) {
            System.err.println("Could not find code source for " + resourceURL + ": " + e.getMessage());
        }

        if (codeSourceUrl == null) {
            return Metadata.EMPTY;
        }

        return getMetadata(codeSourceUrl);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassDelegate#getMetadata(java.net.URL)
    @SuppressWarnings("all")
    private Metadata getMetadata(URL codeSourceUrl) {
        return metadataCache.computeIfAbsent(codeSourceUrl.toString(), (codeSourceStr) -> {
            Manifest manifest = null;
            CodeSource codeSource;
            Certificate[] certificates = null;

            try {
                Path path = Paths.get(codeSourceUrl.toURI());

                if (Files.isDirectory(path)) {
                    manifest = ManifestUtils.readManifest(path);
                } else {
                    URLConnection connection = new URL("jar:" + codeSourceStr + "!/").openConnection();

                    if (connection instanceof JarURLConnection) {
                        manifest = ((JarURLConnection) connection).getManifest();
                        certificates = ((JarURLConnection) connection).getCertificates();
                    }

                    if (manifest == null) {
                        try (FileSystemUtils.FileSystemDelegate jarFs = FileSystemUtils.getJarFileSystem(path, false)) {
                            manifest = ManifestUtils.readManifest(jarFs.get().getRootDirectories().iterator().next());
                        }
                    }

                    // TODO
                }
            } catch (IOException | FileSystemNotFoundException | URISyntaxException e) {
                e.printStackTrace();
            }

            codeSource = new CodeSource(codeSourceUrl, certificates);

            return new Metadata(manifest, codeSource);
        });
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassDelegate#canTransformClass
    private static boolean canTransformClass(String name) {
        name = name.replace('/', '.');
        // Blocking Fabric Loader classes is no longer necessary here as they don't exist on the modding class loader
        return /* !"net.fabricmc.api.EnvType".equals(name) && !name.startsWith("net.fabricmc.loader.") && */ !name.startsWith("org.apache.logging.log4j");
    }
}
