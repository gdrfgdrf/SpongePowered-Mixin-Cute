package io.github.gdrfgdrf.spongepowered.mixin.utils;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author gdrfgdrf
 */
public class UrlUtils {
    // 来自 fabric 的 net.fabricmc.loader.impl.util.UrlUtil#getSource
    public static URL getSource(String filename, URL resourceURL) throws Exception {
        URL codeSourceURL;

        URLConnection connection = resourceURL.openConnection();

        if (connection instanceof JarURLConnection) {
            codeSourceURL = ((JarURLConnection) connection).getJarFileURL();
        } else {
            String path = resourceURL.getPath();

            if (path.endsWith(filename)) {
                codeSourceURL = new URL(resourceURL.getProtocol(), resourceURL.getHost(), resourceURL.getPort(), path.substring(0, path.length() - filename.length()));
            } else {
                throw new RuntimeException("Could not figure out code source for file '" + filename + "' and URL '" + resourceURL + "'!");
            }
        }

        return codeSourceURL;
    }
}
