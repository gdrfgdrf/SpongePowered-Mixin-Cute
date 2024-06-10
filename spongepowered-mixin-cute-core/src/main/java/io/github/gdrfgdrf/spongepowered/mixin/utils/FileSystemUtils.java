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
