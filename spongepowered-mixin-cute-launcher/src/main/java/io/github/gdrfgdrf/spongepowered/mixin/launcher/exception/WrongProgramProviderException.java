package io.github.gdrfgdrf.spongepowered.mixin.launcher.exception;

/**
 * 当主程序的主类没有实现 {@link io.github.gdrfgdrf.spongepowered.mixin.launcher.base.ProgramProvider} 时抛出
 * @author gdrfgdrf
 */
public class WrongProgramProviderException extends Exception {
    public WrongProgramProviderException(String message) {
        super(message);
    }
}
