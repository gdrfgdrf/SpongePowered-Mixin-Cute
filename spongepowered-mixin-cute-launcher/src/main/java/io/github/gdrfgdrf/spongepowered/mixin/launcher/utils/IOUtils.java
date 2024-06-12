package io.github.gdrfgdrf.spongepowered.mixin.launcher.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author gdrfgdrf
 */
@SuppressWarnings("unused")
public class IOUtils {
    private IOUtils() {}

    public static String read(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = getReader(inputStream);
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        bufferedReader.close();

        return stringBuilder.toString();
    }

    public static BufferedReader getReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

}
