package io.github.gdrfgdrf.spongepowered.mixin.cute.test.plugin.mixin;

import io.github.gdrfgdrf.spongepowered.mixin.cute.test.runner.TestClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author gdrfgdrf
 */
@Mixin(TestClass.class)
public class TestClassMixin {
    @Inject(method = "getString*", at = @At("HEAD"), cancellable = true)
    public void injectGetString(CallbackInfoReturnable<String> callbackInfo) {
        callbackInfo.setReturnValue("Goodbye World!, I'm hacked");
    }
}
