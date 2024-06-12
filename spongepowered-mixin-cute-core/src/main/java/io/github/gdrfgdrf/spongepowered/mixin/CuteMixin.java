package io.github.gdrfgdrf.spongepowered.mixin;

import io.github.gdrfgdrf.spongepowered.mixin.loader.CuteClassLoader;
import io.github.gdrfgdrf.spongepowered.mixin.transformer.GameTransformer;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gdrfgdrf
 */
@Getter
public class CuteMixin {
    private static CuteMixin INSTANCE;

    private final Map<String, Object> properties = new HashMap<>();

    private CuteClassLoader classLoader;
    @Setter
    private GameTransformer gameTransformer;
    @Setter
    private boolean loggingEnabled = false;
    @Setter
    private MixinEnvironment.Side side = MixinEnvironment.Side.CLIENT;

    private CuteMixin() {
        this.gameTransformer = new GameTransformer();
    }

    public static CuteMixin getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CuteMixin();
        }
        return INSTANCE;
    }

    public void initialize() {
        initialize(CuteMixin.class.getClassLoader());
    }

    public void initialize(ClassLoader originalClassLoader) {
        classLoader = new CuteClassLoader(originalClassLoader);
        Thread.currentThread().setContextClassLoader(classLoader);

        MixinBootstrap.init();
    }

    public void finishInitializing() {
        finishMixinBootstrapping();
        classLoader.getDelegate().initialize();
    }

    /**
     * 加载一个 Mixin 配置文件
     * @param fileName
	 *        Mixin 配置文件
     * @author gdrfgdrf
     */
    public void load(String fileName) {
        Mixins.addConfiguration(fileName);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.Knot#isClassLoaded
    public boolean isClassLoaded(String name) {
        return classLoader.isClassLoaded(name);
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.Knot#getResourceAsStream
    public InputStream getResourceAsStream(String name) {
        try {
            return classLoader.getResourceAsStream(name, false);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file '" + name + "'!", e);
        }
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.Knot#getClassByteArray
    public byte[] getClassByteArray(String name, boolean runTransformers) throws IOException {
        if (runTransformers) {
            return classLoader.getDelegate().getPreMixinClassByteArray(name, true);
        } else {
            return classLoader.getDelegate().getRawClassByteArray(name, true);
        }
    }

    // 来自 fabric 的 net.fabricmc.loader.impl.launch.FabricLauncherBase#finishMixinBootstrapping
    private static void finishMixinBootstrapping() {
        try {
            Method m = MixinEnvironment.class.getDeclaredMethod("gotoPhase", MixinEnvironment.Phase.class);
            m.setAccessible(true);
            m.invoke(null, MixinEnvironment.Phase.INIT);
            m.invoke(null, MixinEnvironment.Phase.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
