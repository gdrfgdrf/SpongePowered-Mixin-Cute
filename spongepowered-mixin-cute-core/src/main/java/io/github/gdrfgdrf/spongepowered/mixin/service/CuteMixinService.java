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

package io.github.gdrfgdrf.spongepowered.mixin.service;

import io.github.gdrfgdrf.spongepowered.mixin.CuteMixin;
import io.github.gdrfgdrf.spongepowered.mixin.log.MixinLogger;
import lombok.Getter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.launch.platform.container.ContainerHandleURI;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.IMixinTransformerFactory;
import org.spongepowered.asm.service.*;
import org.spongepowered.asm.util.ReEntranceLock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

/**
 * Mixin 服务
 * @author gdrfgdrf
 */
public class CuteMixinService implements IMixinService, IClassProvider, IClassBytecodeProvider, ITransformerProvider, IClassTracker {
    private static CuteMixinService INSTANCE;

    private final ReEntranceLock lock;

    public CuteMixinService() {
        INSTANCE = this;
        lock = new ReEntranceLock(1);
    }

    @Getter
    private IMixinTransformer transformer;

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getClassNode(java.lang.String)
    @Override
    public ClassNode getClassNode(String name) throws ClassNotFoundException, IOException {
        return getClassNode(name, true);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getClassNode(java.lang.String, boolean)
    @Override
    public ClassNode getClassNode(String name, boolean runTransformers) throws ClassNotFoundException, IOException {
        ClassReader reader = new ClassReader(getClassBytes(name, runTransformers));
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        return node;
    }

    @Override
    public URL[] getClassPath() {
        return new URL[0];
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#findClass(java.lang.String)
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return CuteMixin.getInstance().getClassLoader().loadClass(name);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#findClass(java.lang.String, boolean)
    @Override
    public Class<?> findClass(String name, boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, CuteMixin.getInstance().getClassLoader());
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#findAgentClass
    @Override
    public Class<?> findAgentClass(String name, boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, CuteMixin.class.getClassLoader());
    }

    @Override
    public void registerInvalidClass(String className) {

    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#isClassLoaded
    @Override
    public boolean isClassLoaded(String className) {
        return CuteMixin.getInstance().isClassLoaded(className);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getClassRestrictions
    @Override
    public String getClassRestrictions(String className) {
        return "";
    }

    @Override
    public String getName() {
        return "CuteMixin/ReferenceToFabric";
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#isValid
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void prepare() {

    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getInitialPhase
    @Override
    public MixinEnvironment.Phase getInitialPhase() {
        return MixinEnvironment.Phase.PREINIT;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#offer
    @Override
    public void offer(IMixinInternal internal) {
        if (internal instanceof IMixinTransformerFactory) {
            transformer = ((IMixinTransformerFactory) internal).createTransformer();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void beginPhase() {

    }

    @Override
    public void checkEnv(Object bootSource) {

    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getReEntranceLock
    @Override
    public ReEntranceLock getReEntranceLock() {
        return lock;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getClassProvider
    @Override
    public IClassProvider getClassProvider() {
        return this;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getBytecodeProvider
    @Override
    public IClassBytecodeProvider getBytecodeProvider() {
        return this;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getTransformerProvider
    @Override
    public ITransformerProvider getTransformerProvider() {
        return this;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getClassTracker
    @Override
    public IClassTracker getClassTracker() {
        return this;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getAuditTrail
    @Override
    public IMixinAuditTrail getAuditTrail() {
        return null;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getPlatformAgents
    @Override
    public Collection<String> getPlatformAgents() {
        return Collections.singletonList("org.spongepowered.asm.launch.platform.MixinPlatformAgentDefault");
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getPrimaryContainer
    @Override
    public IContainerHandle getPrimaryContainer() {
        try {
            return new ContainerHandleURI(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getMixinContainers
    @Override
    public Collection<IContainerHandle> getMixinContainers() {
        return Collections.emptyList();
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getResourceAsStream
    @Override
    public InputStream getResourceAsStream(String name) {
        return CuteMixin.getInstance().getResourceAsStream(name);
    }

    @Override
    public String getSideName() {
        return "CuteMixinDefaultSideName";
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getMinCompatibilityLevel
    @Override
    public MixinEnvironment.CompatibilityLevel getMinCompatibilityLevel() {
        return MixinEnvironment.CompatibilityLevel.JAVA_8;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getMaxCompatibilityLevel
    @Override
    public MixinEnvironment.CompatibilityLevel getMaxCompatibilityLevel() {
        return MixinEnvironment.CompatibilityLevel.JAVA_17;
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getLogger
    @Override
    public ILogger getLogger(String name) {
        return MixinLogger.get(name);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getTransformers
    @Override
    public Collection<ITransformer> getTransformers() {
        return Collections.emptyList();
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getDelegatedTransformers
    @Override
    public Collection<ITransformer> getDelegatedTransformers() {
        return Collections.emptyList();
    }

    @Override
    public void addTransformerExclusion(String name) {

    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinServiceKnot#getClassBytes(java.lang.String, boolean)
    public byte[] getClassBytes(String name, boolean runTransformers) throws ClassNotFoundException, IOException {
        byte[] classBytes = CuteMixin.getInstance().getClassByteArray(name, runTransformers);

        if (classBytes != null) {
            return classBytes;
        } else {
            throw new ClassNotFoundException(name);
        }
    }

    public static CuteMixinService getInstance() {
        return INSTANCE;
    }
}
