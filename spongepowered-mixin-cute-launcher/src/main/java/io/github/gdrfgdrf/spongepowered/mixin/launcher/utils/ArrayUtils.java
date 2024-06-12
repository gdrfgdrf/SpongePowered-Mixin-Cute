package io.github.gdrfgdrf.spongepowered.mixin.launcher.utils;

/**
 * @author gdrfgdrf
 */
public class ArrayUtils {
    private ArrayUtils() {}

    public static boolean containsClass(Class<?>[] arr, Class<?> targetValue) {
        for (Class<?> clazz : arr) {
            if (targetValue.getName().equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }

}
