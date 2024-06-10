package io.github.gdrfgdrf.spongepowered.mixin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.Manifest;

/**
 * @author gdrfgdrf
 */
public class ManifestUtils {
    // 来自 fabric 的 net.fabricmc.loader.impl.util.ManifestUtil#readManifest(java.nio.file.Path)
    public static Manifest readManifest(Path basePath) throws IOException {
        Path path = basePath.resolve("META-INF").resolve("MANIFEST.MF");
        if (!Files.exists(path)) {
            return null;
        }

        try (InputStream stream = Files.newInputStream(path)) {
            return new Manifest(stream);
        }
    }
}
