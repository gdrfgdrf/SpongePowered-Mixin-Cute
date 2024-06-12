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
