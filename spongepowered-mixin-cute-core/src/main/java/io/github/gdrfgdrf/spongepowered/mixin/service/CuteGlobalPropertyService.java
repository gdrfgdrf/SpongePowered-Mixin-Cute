package io.github.gdrfgdrf.spongepowered.mixin.service;

import io.github.gdrfgdrf.spongepowered.mixin.CuteMixin;
import io.github.gdrfgdrf.spongepowered.mixin.property.MixinStringPropertyKey;
import org.spongepowered.asm.service.IGlobalPropertyService;
import org.spongepowered.asm.service.IPropertyKey;

/**
 * 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.FabricGlobalPropertyService
 * @author gdrfgdrf
 */
public class CuteGlobalPropertyService implements IGlobalPropertyService {
    @Override
    public IPropertyKey resolveKey(String name) {
        return new MixinStringPropertyKey(name);
    }

    private String keyString(IPropertyKey key) {
        return ((MixinStringPropertyKey) key).key;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProperty(IPropertyKey key) {
        return (T) CuteMixin.getInstance().getProperties().get(keyString(key));
    }

    @Override
    public void setProperty(IPropertyKey key, Object value) {
        CuteMixin.getInstance().getProperties().put(keyString(key), value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProperty(IPropertyKey key, T defaultValue) {
        return (T) CuteMixin.getInstance().getProperties().getOrDefault(keyString(key), defaultValue);
    }

    @Override
    public String getPropertyString(IPropertyKey key, String defaultValue) {
        Object o = CuteMixin.getInstance().getProperties().get(keyString(key));
        return o != null ? o.toString() : defaultValue;
    }
}
