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

package io.github.gdrfgdrf.spongepowered.mixin.launcher.utils;

import io.github.gdrfgdrf.spongepowered.mixin.launcher.exception.ResourceNotFoundException;
import lombok.Cleanup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

/**
 * @author gdrfgdrf
 */
public class ClassUtils {
    private ClassUtils() {}

    public static void saveResource(String name, File target) throws IOException, ResourceNotFoundException {
        saveResource(name, target, ClassUtils.class.getClassLoader());
    }

    public static void saveResource(String name, File target, ClassLoader classLoader) throws
            IOException,
            ResourceNotFoundException
    {
        if (target.exists()) {
            return;
        }
        @Cleanup
        InputStream resourceAsStream = getResourceAsStream(name, classLoader);
        AssertUtils.expression(
                resourceAsStream != null,
                new ResourceNotFoundException("Unable to get resource file " + name + " from jar")
        );

        Files.copy(resourceAsStream, target.toPath());
    }

    public static URL getResource(String name) {
        return getResource(name, ClassUtils.class.getClassLoader());
    }

    public static URL getResource(String name, ClassLoader classLoader) {
        return classLoader.getResource(name);
    }

    @SuppressWarnings("unused")
    public static InputStream getResourceAsStream(String name) {
        return getResourceAsStream(name, FileUtils.class.getClassLoader());
    }

    public static InputStream getResourceAsStream(String name, ClassLoader classLoader) {
        return classLoader.getResourceAsStream(name);
    }
}
