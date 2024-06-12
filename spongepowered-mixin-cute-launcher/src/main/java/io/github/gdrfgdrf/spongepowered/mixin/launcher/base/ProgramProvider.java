package io.github.gdrfgdrf.spongepowered.mixin.launcher.base;

/**
 * 程序提供器，
 * 该类需要程序主类实现
 * @author gdrfgdrf
 */
public interface ProgramProvider {
    /**
     * 用来替代原本的 public static void main(String[] args) 方法，当 Mixin 系统加载完成时将会调用
     * @param args
	 *        运行时提供的参数
     * @author gdrfgdrf
     */
    void main(String[] args);
}
