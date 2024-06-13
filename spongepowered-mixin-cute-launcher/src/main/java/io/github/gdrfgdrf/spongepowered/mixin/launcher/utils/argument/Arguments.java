package io.github.gdrfgdrf.spongepowered.mixin.launcher.utils.argument;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author gdrfgdrf
 */
public class Arguments {
    private final Map<String, Argument> argumentMap = new HashMap<>();

    private Arguments() {}

    void put(Argument argument) {
        argumentMap.put(argument.getKey(), argument);
    }

    public Argument get(String key) {
        return argumentMap.get(key);
    }

    public Argument getOrDefault(String key, Argument defaultArgument) {
        return argumentMap.getOrDefault(key, defaultArgument);
    }

    public boolean contains(String key) {
        return argumentMap.containsKey(key);
    }

    public int length() {
        return argumentMap.size();
    }

    public static Arguments of(Stream<Argument> argumentStream) {
        Arguments arguments = new Arguments();
        argumentStream.forEach(arguments::put);

        return arguments;
    }

}
