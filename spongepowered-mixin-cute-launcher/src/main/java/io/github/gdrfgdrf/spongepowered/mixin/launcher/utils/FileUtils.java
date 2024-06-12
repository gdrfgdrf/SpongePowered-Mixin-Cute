package io.github.gdrfgdrf.spongepowered.mixin.launcher.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author gdrfgdrf
 */
@SuppressWarnings("unused")
public class FileUtils {
    private FileUtils() {}

    public static void mkdirs(String name) {
        File file = new File(name);
        mkdirs(file);
    }

    @SuppressWarnings("all")
    public static void mkdirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static InputStream readJarResource(File file, String fileName) throws IOException {
        return readJarResource(file, fileName, true);
    }

    public static InputStream readJarResource(File file, String fileName, boolean autoClose) throws IOException {
        if (!autoClose) {
            JarFile jarFile = new JarFile(file);
            return readJarResource(jarFile, fileName);
        }
        try (JarFile jarFile = new JarFile(file)) {
            return readJarResource(jarFile, fileName);
        }
    }

    public static InputStream readJarResource(JarFile jarFile, String fileName) throws IOException {
        JarEntry jarEntry = jarFile.getJarEntry(fileName);
        return jarFile.getInputStream(jarEntry);
    }

    public static String getExtension(File file) {
        if (!file.isFile()) {
            return null;
        }
        return file.getName().substring(
                file.getName().lastIndexOf(".")
        );
    }

    public static File[] getFiles(File folder, FileFilter fileFilter) {
        if (!folder.isDirectory()) {
            return null;
        }
        if (fileFilter == null) {
            return folder.listFiles();
        }
        return folder.listFiles(fileFilter);
    }

    public static boolean isFolderEmpty(File folder) {
        assert folder != null;
        assert folder.isDirectory();

        File[] files = folder.listFiles();
        assert files != null;

        return files.length == 0;
    }
}