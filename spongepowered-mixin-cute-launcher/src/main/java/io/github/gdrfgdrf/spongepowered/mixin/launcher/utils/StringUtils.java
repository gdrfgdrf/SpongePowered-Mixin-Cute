package io.github.gdrfgdrf.spongepowered.mixin.launcher.utils;

/**
 * @author gdrfgdrf
 */
public class StringUtils {
    private StringUtils() {}

    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static boolean isBlank(CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
