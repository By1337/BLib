package org.by1337.blib.util;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.gson.Gson;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.Plugin;
import org.by1337.blib.configuration.YamlConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * This class provides methods to load YAML configuration files and ensure
 * that required files are present in the plugin's data folder.
 */
public class ResourceUtil {
    private static final Gson GSON = new Gson();

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
     * Loads a JSON configuration file from the specified path and deserializes it
     * into an object of the specified type. This version uses a {@link Type} to
     * allow for generic type handling.
     *
     * @param path    the path to the JSON configuration file relative to the plugin's data folder
     * @param plugin  the plugin instance used to access the data folder
     * @param typeOfT the type of the object to deserialize
     * @param <T>     the type of the object to return
     * @return the deserialized object
     */
    @SuppressWarnings("unchecked")
    public static <T> T loadJson(@NotNull String path, @NotNull Plugin plugin, Type typeOfT) {
        return (T) loadJson(path, plugin, TypeToken.get(typeOfT));
    }

    /**
     * Loads a JSON configuration file from the specified path and deserializes it
     * into an object of the specified class.
     *
     * @param path     the path to the JSON configuration file relative to the plugin's data folder
     * @param plugin   the plugin instance used to access the data folder
     * @param classOfT the class of the object to deserialize
     * @param <T>      the type of the object to return
     * @return the deserialized object
     */
    public static <T> T loadJson(@NotNull String path, @NotNull Plugin plugin, Class<T> classOfT) {
        T object = loadJson(path, plugin, TypeToken.get(classOfT));
        return Primitives.wrap(classOfT).cast(object);
    }

    /**
     * Loads a JSON configuration file from the specified path and deserializes it
     * into an object of the specified type using a {@link TypeToken}.
     *
     * @param path    the path to the JSON configuration file relative to the plugin's data folder
     * @param plugin  the plugin instance used to access the data folder
     * @param typeOfT the {@link TypeToken} representing the type of the object to deserialize
     * @param <T>     the type of the object to return
     * @return the deserialized object
     * @throws RuntimeException if an error occurs while loading or deserializing the JSON
     */
    public static <T> T loadJson(@NotNull String path, @NotNull Plugin plugin, TypeToken<T> typeOfT) {
        return tryRun(() -> {
            try (InputStreamReader in = new InputStreamReader(new FileInputStream(saveIfNotExist(path, plugin)))) {
                return GSON.fromJson(in, typeOfT);
            }
        });
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
     * @param <T>      the return type of the runnable
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
