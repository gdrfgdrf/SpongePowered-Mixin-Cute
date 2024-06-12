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

package io.github.gdrfgdrf.spongepowered.mixin.launcher.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 插件描述，若主程序支持插件，该类将被使用
 * @author gdrfgdrf
 */
@Data
public class PluginDescription {
    /**
     * 插件的所有 Mixin 配置文件的文件名，这些文件必须在插件资源文件夹下
     */
    private final List<String> mixins = new ArrayList<>();
}
