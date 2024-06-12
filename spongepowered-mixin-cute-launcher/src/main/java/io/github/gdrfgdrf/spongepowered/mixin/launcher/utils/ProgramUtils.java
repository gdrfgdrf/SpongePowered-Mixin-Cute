package io.github.gdrfgdrf.spongepowered.mixin.launcher.utils;

import java.io.File;
import java.util.Objects;

/**
 * @author gdrfgdrf
 */
public class ProgramUtils {
    private ProgramUtils() {}

    public static boolean isStartupFromJar() {
        File file = new File(ProgramUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return file.isFile() && Objects.equals(FileUtils.getExtension(file), ".jar");
    }

}
