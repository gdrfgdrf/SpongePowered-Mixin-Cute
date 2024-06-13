package io.github.gdrfgdrf.spongepowered.mixin.launcher.utils;

/**
 * @author gdrfgdrf
 */
public class AssertUtils {
    private AssertUtils() {}

    public static <T extends Throwable> void expression(boolean expression, T throwable) throws T {
        if (!expression) {
            throw throwable;
        }
    }
}
