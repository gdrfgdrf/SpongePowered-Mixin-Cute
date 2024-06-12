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
