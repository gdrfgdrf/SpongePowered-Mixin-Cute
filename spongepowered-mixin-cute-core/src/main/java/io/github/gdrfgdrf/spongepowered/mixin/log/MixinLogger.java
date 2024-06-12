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

package io.github.gdrfgdrf.spongepowered.mixin.log;

import io.github.gdrfgdrf.spongepowered.mixin.CuteMixin;
import lombok.extern.slf4j.Slf4j;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.logging.Level;
import org.spongepowered.asm.logging.LoggerAdapterAbstract;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 来自 fabric 的 net.fabricmc.loader.impl.launch.knot.MixinLogger
 * @author gdrfgdrf
 */
@Slf4j
public class MixinLogger extends LoggerAdapterAbstract {
    private static final Map<String, ILogger> LOGGER_MAP = new ConcurrentHashMap<>();
    private static final Map<Level, org.slf4j.event.Level> LEVEL_MAP = createLevelMap();

    public static ILogger get(String name) {
        return LOGGER_MAP.computeIfAbsent(name, MixinLogger::new);
    }

    MixinLogger(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "CuteMixin Logger";
    }

    @Override
    public void catching(Level level, Throwable t) {
        log(level, "Catching ".concat(t.toString()), t);
    }

    @Override
    public void log(Level level, String message, Object... params) {
        if (!CuteMixin.getInstance().isLoggingEnabled()) {
            return;
        }
        org.slf4j.event.Level slf4jLevel = translateLevel(level);

        switch (slf4jLevel) {
            case ERROR -> log.error(message, params);
            case WARN -> log.warn(message, params);
            case INFO -> log.info(message, params);
            case DEBUG -> log.debug(message, params);
            case TRACE -> log.trace(message, params);
        }
    }

    @Override
    public void log(Level level, String message, Throwable t) {
        switch (level) {
            case ERROR -> log.error(message, t);
            case WARN -> log.warn(message, t);
            case INFO -> log.info(message, t);
            case DEBUG -> log.debug(message, t);
            case TRACE -> log.trace(message, t);
        }
    }

    @Override
    public <T extends Throwable> T throwing(T t) {
        log(Level.ERROR, "Throwing ".concat(t.toString()), t);

        return t;
    }

    private static org.slf4j.event.Level translateLevel(Level level) {
        return LEVEL_MAP.getOrDefault(level, org.slf4j.event.Level.INFO);
    }

    private static Map<Level, org.slf4j.event.Level> createLevelMap() {
        Map<Level, org.slf4j.event.Level> ret = new EnumMap<>(Level.class);

        ret.put(Level.FATAL, org.slf4j.event.Level.ERROR);
        ret.put(Level.ERROR, org.slf4j.event.Level.ERROR);
        ret.put(Level.WARN, org.slf4j.event.Level.WARN);
        ret.put(Level.INFO, org.slf4j.event.Level.INFO);
        ret.put(Level.DEBUG, org.slf4j.event.Level.DEBUG);
        ret.put(Level.TRACE, org.slf4j.event.Level.TRACE);

        return ret;
    }
}
