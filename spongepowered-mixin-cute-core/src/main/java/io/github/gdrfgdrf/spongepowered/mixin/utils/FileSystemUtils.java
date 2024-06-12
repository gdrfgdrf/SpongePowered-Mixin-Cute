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

package io.github.gdrfgdrf.spongepowered.mixin.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Map;
import java.util.zip.ZipError;

/**
 * 来自 fabric 的 net.fabricmc.loader.impl.util.FileSystemUtil
 * @author gdrfgdrf
 */
public class FileSystemUtils {
    public static class FileSystemDelegate implements AutoCloseable {
        private final FileSystem fileSystem;
        private final boolean owner;

        public FileSystemDelegate(FileSystem fileSystem, boolean owner) {
            this.fileSystem = fileSystem;
            this.owner = owner;
        }

        public FileSystem get() {
            return fileSystem;
        }

        @Override
        public void close() throws IOException {
            if (owner) {
                fileSystem.close();
            }
        }
    }

    private FileSystemUtils() { }

    private static final Map<String, String> jfsArgsCreate = Collections.singletonMap("create", "true");
    private static final Map<String, String> jfsArgsEmpty = Collections.emptyMap();

    public static FileSystemDelegate getJarFileSystem(Path path, boolean create) throws IOException {
        return getJarFileSystem(path.toUri(), create);
    }

    public static FileSystemDelegate getJarFileSystem(URI uri, boolean create) throws IOException {
        URI jarUri;

        try {
            jarUri = new URI("jar:" + uri.getScheme(), uri.getHost(), uri.getPath(), uri.getFragment());
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }

        boolean opened = false;
        FileSystem ret;

        try {
            ret = FileSystems.getFileSystem(jarUri);
        } catch (FileSystemNotFoundException ignore) {
            try {
                ret = FileSystems.newFileSystem(jarUri, create ? jfsArgsCreate : jfsArgsEmpty);
                opened = true;
            } catch (FileSystemAlreadyExistsException ignore2) {
                ret = FileSystems.getFileSystem(jarUri);
            } catch (IOException | ZipError e) {
                throw new IOException("Error accessing "+uri+": "+e, e);
            }
        }

        return new FileSystemDelegate(ret, opened);
    }
}
