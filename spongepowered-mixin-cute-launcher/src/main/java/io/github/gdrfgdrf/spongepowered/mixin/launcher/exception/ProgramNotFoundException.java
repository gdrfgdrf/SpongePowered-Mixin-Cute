package io.github.gdrfgdrf.spongepowered.mixin.launcher.exception;

/**
 * 无法找到 program.jar 时抛出
 * @author gdrfgdrf
 */
public class ProgramNotFoundException extends Exception {
    public ProgramNotFoundException(String message) {
        super(message);
    }
}
