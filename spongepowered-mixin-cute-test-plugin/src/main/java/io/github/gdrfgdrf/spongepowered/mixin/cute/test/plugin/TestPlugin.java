package io.github.gdrfgdrf.spongepowered.mixin.cute.test.plugin;

import io.github.gdrfgdrf.spongepowered.mixin.CuteMixin;
import io.github.gdrfgdrf.spongepowered.mixin.cute.test.runner.TestClass;

/**
 * @author gdrfgdrf
 */
@SuppressWarnings("unused")
public class TestPlugin {

    public static void run() {
        CuteMixin.getInstance().load("testrunner.mixins.json");
        TestClass.print();
    }

}
