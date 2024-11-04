package org.by1337.blib.util;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.bukkit.plugin.Plugin;
import org.by1337.blib.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * This class provides methods to load YAML configuration files and ensure
 * that required files are present in the plugin's data folder.
 */
public class ResourceUtil {

    /**
     * Loads a YAML configuration file from the specified path. If the file does
     * not exist, it will be created by saving the resource from the plugin's
     * jar file.
     *
     * @param path   the path to the configuration file relative to the plugin's data folder
     * @param plugin the plugin instance used to access the data folder
     * @return a {@link YamlConfig} instance representing the loaded configuration
     * @throws RuntimeException if an error occurs while loading the configuration
     */
    @NotNull
    public static YamlConfig load(@NotNull String path, @NotNull Plugin plugin) {
        return tryRun(() -> new YamlConfig(saveIfNotExist(path, plugin)));
    }

    /**
     * Saves a resource file to the plugin's data folder if it does not already exist.
     * The path is adjusted to ensure it is formatted correctly for the file system.
     *
     * @param path   the path to the resource file relative to the plugin's jar
     * @param plugin the plugin instance used to access the data folder
     * @return a {@link File} instance representing the saved or existing file
     */
    @NotNull
    @CanIgnoreReturnValue
    public static File saveIfNotExist(@NotNull String path, @NotNull Plugin plugin) {
        path = path.replace('\\', '/');
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        var f = new File(plugin.getDataFolder(), path);
        if (!f.exists()) {
            plugin.saveResource(path, false);
        }
        return f;
    }

    /**
     * Executes a {@link ThrowingRunnable} and handles any thrown exceptions.
     * If an exception is thrown, it is wrapped in a {@link RuntimeException}.
     *
     * @param runnable the runnable to execute
     * @param <T>     the return type of the runnable
     * @return the result of the runnable's execution
     * @throws RuntimeException if an exception is thrown during execution
     */
    public static <T> T tryRun(@NotNull ThrowingRunnable<T> runnable) {
        try {
            return runnable.run();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Functional interface for a runnable that can throw exceptions.
     *
     * @param <T> the return type of the run method
     */
    @FunctionalInterface
    public interface ThrowingRunnable<T> {
        @NotNull T run() throws Throwable;
    }
}
