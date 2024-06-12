/*
 * Copyright 2024 SpongePowered Mixin Cute Project's Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
