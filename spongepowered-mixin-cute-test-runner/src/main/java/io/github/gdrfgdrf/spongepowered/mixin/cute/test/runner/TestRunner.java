package io.github.gdrfgdrf.spongepowered.mixin.cute.test.runner;

import io.github.gdrfgdrf.spongepowered.mixin.launcher.base.ProgramProvider;

/**
 * @author gdrfgdrf
 */
public class TestRunner implements ProgramProvider {
    @Override
    @SuppressWarnings("all")
    public void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        new TestClass().print();
    }
}
