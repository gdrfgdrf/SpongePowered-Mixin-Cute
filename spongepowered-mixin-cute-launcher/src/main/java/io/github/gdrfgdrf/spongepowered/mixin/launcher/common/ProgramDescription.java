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
