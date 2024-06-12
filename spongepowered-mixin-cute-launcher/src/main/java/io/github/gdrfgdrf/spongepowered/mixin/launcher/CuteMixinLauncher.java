package io.github.gdrfgdrf.spongepowered.mixin.launcher;

import io.github.gdrfgdrf.spongepowered.mixin.CuteMixin;
import io.github.gdrfgdrf.spongepowered.mixin.launcher.base.ProgramProvider;
import io.github.gdrfgdrf.spongepowered.mixin.launcher.common.Constants;
import io.github.gdrfgdrf.spongepowered.mixin.launcher.common.PluginDescription;
import io.github.gdrfgdrf.spongepowered.mixin.launcher.common.ProgramDescription;
import io.github.gdrfgdrf.spongepowered.mixin.launcher.exception.WrongProgramProviderException;
import io.github.gdrfgdrf.spongepowered.mixin.launcher.utils.*;
import io.github.gdrfgdrf.spongepowered.mixin.launcher.utils.jackson.JacksonUtils;
import io.github.gdrfgdrf.spongepowered.mixin.loader.CuteClassLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author gdrfgdrf
 */
public class CuteMixinLauncher {
    private static CuteMixinLauncher INSTANCE;

    private CuteMixinLauncher() {}

    public static CuteMixinLauncher getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CuteMixinLauncher();
        }
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        CuteMixinLauncher.getInstance().run(args);
    }

    private void run(String[] args) throws
            IOException,
            WrongProgramProviderException,
            ClassNotFoundException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException
    {
        File mainProgram = prepareMainProgram();
        JarFile mainProgramJarFile = new JarFile(mainProgram);

        ProgramDescription programDescription = prepareProgramDescription(mainProgramJarFile);
        File[] plugins = preparePluginFolder(programDescription.getPluginFolder());

        CuteMixin.getInstance().initialize();

        if (plugins != null) {
            Arrays.stream(plugins).forEach(plugin -> {
                try {
                    CuteMixin.getInstance().getClassLoader().addURL(plugin.toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            });

            PluginDescription[] pluginDescriptions = preparePluginDescriptions(
                    plugins,
                    programDescription.getPluginDescriptionFileName()
            );

            Set<List<String>> collect = Arrays.stream(pluginDescriptions)
                    .map(PluginDescription::getMixins)
                    .collect(Collectors.toSet());

            collect.forEach(mixins -> mixins.forEach(mixin -> CuteMixin.getInstance().load(mixin)));
        }

        CuteMixin.getInstance().finishInitializing();

        CuteMixin.getInstance().getClassLoader().addURL(mainProgram.toURI().toURL());

        CuteClassLoader classLoader = CuteMixin.getInstance().getClassLoader();
        String mainClassStr = programDescription.getMainClass();

        Class<? extends ProgramProvider> mainClass = prepareMainClass(mainClassStr, classLoader);
        launch(mainClass, args);
    }

    private void launch(Class<? extends ProgramProvider> mainClass, String[] args) throws
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException
    {
        Object programProvider = mainClass.getDeclaredConstructor().newInstance();
        Method main = mainClass.getDeclaredMethod("main", String[].class);

        main.invoke(programProvider, (Object) args);
    }

    private File prepareMainProgram() throws IOException {
        boolean startupFromJar = ProgramUtils.isStartupFromJar();
        if (startupFromJar) {
            FileUtils.mkdirs(Constants.LIB_FOLDER);

            File target = new File(Constants.LIB_FOLDER + Constants.MAIN_PROGRAM_PATH);
            ClassUtils.saveResource(
                    Constants.MAIN_PROGRAM_PATH,
                    target
            );

            return target;
        }

        return new File(ClassUtils.getResource(Constants.MAIN_PROGRAM_PATH).getFile());
    }

    private ProgramDescription prepareProgramDescription(JarFile jarFile) throws IOException {
        InputStream inputStream = FileUtils.readJarResource(jarFile, Constants.PROGRAM_DESCRIPTION_FILE_NAME);
        return JacksonUtils.readInputStream(inputStream, ProgramDescription.class);
    }

    private File[] preparePluginFolder(String pluginFolder) {
        if (StringUtils.isBlank(pluginFolder)) {
            return null;
        }

        File folder = new File(pluginFolder);
        if (!folder.exists() || !folder.isDirectory() || FileUtils.isFolderEmpty(folder)) {
            return null;
        }

        File[] plugins = FileUtils.getFiles(folder, file ->
                !Objects.equals(FileUtils.getExtension(file), "jar"));
        if (plugins == null || plugins.length == 0) {
            return null;
        }

        return plugins;
    }

    private PluginDescription preparePlugin(File plugin, String pluginDescriptionFileName) throws IOException {
        InputStream inputStream = FileUtils.readJarResource(plugin, pluginDescriptionFileName, false);
        return JacksonUtils.readInputStream(inputStream, PluginDescription.class);
    }

    private PluginDescription[] preparePluginDescriptions(File[] plugins, String pluginDescriptionFileName) {
        return Arrays.stream(plugins)
                .map(plugin -> {
                    try {
                        return preparePlugin(plugin, pluginDescriptionFileName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(PluginDescription[]::new);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends ProgramProvider> prepareMainClass(
            String mainClassStr,
            ClassLoader classLoader
    ) throws ClassNotFoundException, WrongProgramProviderException {
        Class<?> mainClass = classLoader.loadClass(mainClassStr);
        Class<?>[] interfaces = mainClass.getInterfaces();
        if (!ArrayUtils.containsClass(interfaces, ProgramProvider.class)) {
            throw new WrongProgramProviderException(
                    mainClass + " does not implement ProgramProvider"
            );
        }

        return (Class<? extends ProgramProvider>) mainClass;
    }


}