package io.github.gdrfgdrf.spongepowered.mixin.launcher.utils.argument;

import java.util.Arrays;

/**
 * @author gdrfgdrf
 */
public class ArgumentParser {
    private static ArgumentParser INSTANCE;

    private ArgumentParser() {}

    public static ArgumentParser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ArgumentParser();
        }
        return INSTANCE;
    }

    public Arguments parse(String[] args) {
        String collect = String.join(" ", args);
        int firstBarre = collect.indexOf("-");
        if (firstBarre == -1) {
            return null;
        }
        collect = collect.substring(firstBarre + 1);

        String[] split = collect.split(" -");

        return Arguments.of(Arrays.stream(split)
                .map(str -> {
                    Argument argument = new Argument();

                    int firstSpace = str.indexOf(" ");
                    if (firstSpace == -1) {
                        firstSpace = str.length();
                    }

                    String key = str.substring(0, firstSpace);
                    String value = null;

                    if (firstSpace + 1 <= str.length()) {
                        value = str.substring(firstSpace + 1);
                    }

                    argument.setKey(key);
                    argument.setValue(value);

                    return argument;
                }));
    }
}
