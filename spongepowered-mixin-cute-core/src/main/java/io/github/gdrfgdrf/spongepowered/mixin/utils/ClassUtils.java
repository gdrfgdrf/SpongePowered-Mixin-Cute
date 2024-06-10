package io.github.gdrfgdrf.spongepowered.mixin.utils;

/**
 * @author gdrfgdrf
 */
public class ClassUtils {
    public static String getClassFileName(String className) {
        return className.replace('.', '/').concat(".class");
    }
}
