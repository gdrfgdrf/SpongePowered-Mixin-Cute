package io.github.gdrfgdrf.spongepowered.mixin.launcher.utils;

import lombok.Cleanup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

/**
 * @author gdrfgdrf
 */
public class ClassUtils {
    private ClassUtils() {}

    public static void saveResource(String name, File target) throws IOException {
        saveResource(name, target, ClassUtils.class.getClassLoader());
    }

    public static void saveResource(String name, File target, ClassLoader classLoader) throws IOException {
        if (target.exists()) {
            return;
        }
        @Cleanup
        InputStream resourceAsStream = getResourceAsStream(name, classLoader);
        Files.copy(resourceAsStream, target.toPath());
    }

    public static URL getResource(String name) {
        return getResource(name, ClassUtils.class.getClassLoader());
    }

    public static URL getResource(String name, ClassLoader classLoader) {
        return classLoader.getResource(name);
    }

    @SuppressWarnings("unused")
    public static InputStream getResourceAsStream(String name) {
        return getResourceAsStream(name, FileUtils.class.getClassLoader());
    }

    public static InputStream getResourceAsStream(String name, ClassLoader classLoader) {
        return classLoader.getResourceAsStream(name);
    }
}
