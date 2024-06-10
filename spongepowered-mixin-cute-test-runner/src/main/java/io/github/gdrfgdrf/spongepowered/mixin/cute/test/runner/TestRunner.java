package io.github.gdrfgdrf.spongepowered.mixin.cute.test.runner;

import io.github.gdrfgdrf.spongepowered.mixin.CuteMixin;
import io.github.gdrfgdrf.spongepowered.mixin.loader.CuteClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author gdrfgdrf
 */
public class TestRunner {
    private static TestRunner INSTANCE;

    private TestRunner() {}

    public static TestRunner getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestRunner();
        }
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        TestRunner.getInstance().run();
    }

    public void run() throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException
    {
        CuteMixin.getInstance().initialize();

        CuteClassLoader classLoader = CuteMixin.getInstance().getClassLoader();
        Class<?> clazz =
                classLoader.loadClass("io.github.gdrfgdrf.spongepowered.mixin.cute.test.plugin.TestPlugin");
        Method run = clazz.getDeclaredMethod("run");
        run.invoke(null);
    }
}
