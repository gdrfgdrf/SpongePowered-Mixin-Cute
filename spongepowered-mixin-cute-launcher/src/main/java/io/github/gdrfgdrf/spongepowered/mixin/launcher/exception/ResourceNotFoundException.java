package io.github.gdrfgdrf.spongepowered.mixin.launcher.exception;

/**
 * 无法从 jar 中获取到资源文件时抛出
 * @author gdrfgdrf
 */
public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
