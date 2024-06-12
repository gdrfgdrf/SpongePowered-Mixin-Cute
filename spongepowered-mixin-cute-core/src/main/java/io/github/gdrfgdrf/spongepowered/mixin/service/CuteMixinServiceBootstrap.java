package io.github.gdrfgdrf.spongepowered.mixin.service;

import org.spongepowered.asm.service.IMixinServiceBootstrap;

/**
 * @author gdrfgdrf
 */
public class CuteMixinServiceBootstrap implements IMixinServiceBootstrap {
    @Override
    public String getName() {
        return "CuteMixin/ReferenceToFabric";
    }

    @Override
    public String getServiceClassName() {
        return "io.github.gdrfgdrf.spongepowered.mixin.service.CuteMixinService";
    }

    @Override
    public void bootstrap() {

    }
}
