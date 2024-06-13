package io.github.gdrfgdrf.spongepowered.mixin.launcher.exception;

/**
 * 当描述文件中有错误的属性时抛出
 * @author gdrfgdrf
 */
public class InvalidPropertyException extends Exception {
    public InvalidPropertyException(String message) {
        super(message);
    }
}
