package io.github.gdrfgdrf.spongepowered.mixin.cute.test.runner;

/**
 * @author gdrfgdrf
 */
public class TestClass {
    public TestClass() {}

    public void print() {
        System.out.println(getString());
    }

    public String getString() {
        return "Hello World, I'm not hacked";
    }

}
