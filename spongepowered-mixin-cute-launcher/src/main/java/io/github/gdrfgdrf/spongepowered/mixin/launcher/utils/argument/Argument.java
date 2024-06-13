package io.github.gdrfgdrf.spongepowered.mixin.launcher.utils.argument;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @author gdrfgdrf
 */
@Data
@Setter(value = AccessLevel.PACKAGE)
public class Argument {
    private String key;
    private String value;
}
