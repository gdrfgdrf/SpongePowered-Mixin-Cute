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
