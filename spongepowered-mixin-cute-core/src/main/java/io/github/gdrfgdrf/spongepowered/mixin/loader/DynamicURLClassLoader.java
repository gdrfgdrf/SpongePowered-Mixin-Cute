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

package io.github.gdrfgdrf.spongepowered.mixin.loader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassLoader.DynamicURLClassLoader
 * @author gdrfgdrf
 */
public class DynamicURLClassLoader extends URLClassLoader {
    public DynamicURLClassLoader(URL[] urls) {
        super(urls, new DummyClassLoader());
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    static {
        registerAsParallelCapable();
    }
}
