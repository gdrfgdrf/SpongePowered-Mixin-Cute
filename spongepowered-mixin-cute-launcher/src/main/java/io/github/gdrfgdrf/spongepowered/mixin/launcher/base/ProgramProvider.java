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

package io.github.gdrfgdrf.spongepowered.mixin.launcher.base;

/**
 * 程序提供器，
 * 该类需要程序主类实现
 * @author gdrfgdrf
 */
public interface ProgramProvider {
    /**
     * 用来替代原本的 public static void main(String[] args) 方法，当 Mixin 系统加载完成时将会调用
     * 但是，为了测试，原本的 public static void main(String[] args) 方法建议保留，直到对外公布时才去除，
     * 其内容和该方法的实现一致
     * <p>
     * 测试时 IDE 将会调用原本的 public static void main(String[] args) 方法，
     * 运行时将会调用该方法
     *
     * @param args
	 *        运行时提供的参数
     * @author gdrfgdrf
     */
    void start(String[] args);
}
