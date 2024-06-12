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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * 主程序描述
 * @author gdrfgdrf
 */
@Data
@Setter(value = AccessLevel.PRIVATE)
public class ProgramDescription {
    /**
     * 主程序实现了 {@link io.github.gdrfgdrf.spongepowered.mixin.launcher.base.ProgramProvider} 的那个类的全限定名
     */
    @JsonProperty(value = "main-class")
    private String mainClass;
    /**
     * 若主程序支持插件，则需要定义运行时存储插件 Jar 文件的文件夹
     */
    @JsonProperty(value = "plugin-folder")
    private String pluginFolder;
    /**
     * 若主程序支持插件，则需要定于插件描述文件的名字，
     * 主程序的开发者应制定好标准，每个插件的资源文件夹里都必须拥有一个和该值同名的文件，
     * 获取到的文件将会被反序列化为 {@link PluginDescription}
     */
    @JsonProperty(value = "plugin-description-file-name")
    private String pluginDescriptionFileName;


}
