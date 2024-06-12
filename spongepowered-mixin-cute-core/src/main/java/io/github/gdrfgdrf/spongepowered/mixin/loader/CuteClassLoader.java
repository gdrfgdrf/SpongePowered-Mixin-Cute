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

package io.github.gdrfgdrf.spongepowered.mixin.loader;

import io.github.gdrfgdrf.spongepowered.mixin.delegate.ClassDelegate;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.util.Enumeration;
import java.util.Objects;

/**
 * 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader
 * @author gdrfgdrf
 */
public class CuteClassLoader extends SecureClassLoader {
    private final DynamicURLClassLoader urlClassLoader;
    private final ClassLoader originalLoader;
    @Getter
    private final ClassDelegate delegate;

    public CuteClassLoader(ClassLoader originalLoader) {
        super(new DynamicURLClassLoader(new URL[0]));
        this.urlClassLoader = (DynamicURLClassLoader) getParent();
        this.originalLoader = originalLoader;
        this.delegate = new ClassDelegate(this);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader#getResource
    @Override
    public URL getResource(String name) {
        Objects.requireNonNull(name);

        URL url = urlClassLoader.getResource(name);

        if (url == null) {
            url = originalLoader.getResource(name);
        }

        return url;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader#findResource
    @Override
    public URL findResource(String name) {
        Objects.requireNonNull(name);

        return urlClassLoader.findResource(name);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader#getResourceAsStream(java.lang.String)
    @Override
    public InputStream getResourceAsStream(String name) {
        Objects.requireNonNull(name);

        InputStream inputStream = urlClassLoader.getResourceAsStream(name);

        if (inputStream == null) {
            inputStream = originalLoader.getResourceAsStream(name);
        }

        return inputStream;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader#getResources
    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        Objects.requireNonNull(name);

        Enumeration<URL> first = urlClassLoader.getResources(name);
        Enumeration<URL> second = originalLoader.getResources(name);

        return new Enumeration<>() {
            Enumeration<URL> current = first;

            @Override
            public boolean hasMoreElements() {
                if (current == null) {
                    return false;
                }
                if (current.hasMoreElements()) {
                    return true;
                }

                return current == first && second.hasMoreElements();
            }

            @Override
            public URL nextElement() {
                if (current == null) {
                    return null;
                }

                if (!current.hasMoreElements()) {
                    if (current == first) {
                        current = second;
                    } else {
                        current = null;
                        return null;
                    }
                }

                return current.nextElement();
            }
        };
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader#isClassLoaded
    public boolean isClassLoaded(String name) {
        synchronized (getClassLoadingLock(name)) {
            return findLoadedClass(name) != null;
        }
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader#loadClass
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);

            if (c == null) {
                c = delegate.tryLoadClass(name, true);

                if (c == null) {
                    c = originalLoader.loadClass(name);
                }
            }

            if (resolve) {
                resolveClass(c);
            }

            return c;
        }
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader#findClass
    @Override
    protected Class<?> findClass(String name) {
        return delegate.tryLoadClass(name, true);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader#getResourceAsStream(java.lang.String, boolean)
    public InputStream getResourceAsStream(String classFile, boolean allowFromParent) throws IOException {
        InputStream inputStream = urlClassLoader.getResourceAsStream(classFile);

        if (inputStream == null && allowFromParent) {
            inputStream = originalLoader.getResourceAsStream(classFile);
        }

        return inputStream;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader#getPackage
    @Override
    public Package getPackage(String name) {
        return super.getPackage(name);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader#definePackage
    @Override
    public Package definePackage(String name, String specTitle, String specVersion, String specVendor,
                                 String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
        return super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader#defineClassFwd
    public Class<?> defineClassFwd(String name, byte[] b, int off, int len, CodeSource cs) {
        return super.defineClass(name, b, off, len, cs);
    }

    public void addURL(URL url) {
        urlClassLoader.addURL(url);
    }

    static {
        registerAsParallelCapable();
    }
}
