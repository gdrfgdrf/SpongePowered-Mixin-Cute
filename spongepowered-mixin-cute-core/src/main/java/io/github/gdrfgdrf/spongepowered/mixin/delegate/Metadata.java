package io.github.gdrfgdrf.spongepowered.mixin.delegate;

import lombok.Getter;

import java.security.CodeSource;
import java.util.jar.Manifest;

/**
 * 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.KnotClassDelegate.Metadata
 * @author gdrfgdrf
 */
@Getter
public class Metadata {
    public static final Metadata EMPTY = new Metadata(null, null);

    private final Manifest manifest;
    private final CodeSource codeSource;

    public Metadata(Manifest manifest, CodeSource codeSource) {
        this.manifest = manifest;
        this.codeSource = codeSource;
    }
}
