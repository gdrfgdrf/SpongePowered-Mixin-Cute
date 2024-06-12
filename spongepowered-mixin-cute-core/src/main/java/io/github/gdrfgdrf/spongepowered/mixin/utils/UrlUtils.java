/*
 * Copyright 2024 SpongePowered Mixin Cute Project's Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
