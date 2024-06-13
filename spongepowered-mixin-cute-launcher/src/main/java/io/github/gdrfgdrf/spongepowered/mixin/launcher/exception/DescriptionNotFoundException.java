package io.github.gdrfgdrf.spongepowered.mixin.launcher.exception;

/**
 * 当无法读取到描述文件时抛出
 * @author gdrfgdrf
 */
public class DescriptionNotFoundException extends Exception {
    public DescriptionNotFoundException(String message) {
        super(message);
    }
}
