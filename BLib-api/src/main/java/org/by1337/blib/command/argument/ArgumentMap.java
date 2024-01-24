package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandSyntaxError;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Represents a specialized map for managing command arguments and their values.
 *
 * @param <KEY> The type of keys in the map.
 * @param <VAL> The type of values in the map.
 */
public class ArgumentMap<KEY, VAL> extends HashMap<KEY, VAL> {
    /**
     * Retrieves the value associated with the specified key, throwing a CommandSyntaxError if the value is null.
     *
     * @param key The key to retrieve the value for.
     * @return The value associated with the key.
     * @throws CommandSyntaxError If the value is null.
     */
    public VAL getOrThrow(@NotNull KEY key) throws CommandSyntaxError {
        VAL v = getOrDefault(key, null);
        CommandSyntaxError.ifNull(v);
        return v;
    }

    /**
     * Retrieves the value associated with the specified key, throwing a CommandSyntaxError with the specified message if the value is null.
     *
     * @param key     The key to retrieve the value for.
     * @param message The message to include in the CommandSyntaxError if the value is null.
     * @return The value associated with the key.
     * @throws CommandSyntaxError If the value is null, with the specified message.
     */
    public VAL getOrThrow(@NotNull KEY key, @NotNull String message) throws CommandSyntaxError {
        VAL v = getOrDefault(key, null);
        CommandSyntaxError.ifNull(v, message);
        return v;
    }

    /**
     * Retrieves the value associated with the specified key, throwing a CommandSyntaxError with the specified message and additional objects if the value is null.
     *
     * @param key      The key to retrieve the value for.
     * @param message  The message to include in the CommandSyntaxError if the value is null.
     * @param objects  Additional objects to include in the CommandSyntaxError message.
     * @return The value associated with the key.
     * @throws CommandSyntaxError If the value is null, with the specified message and additional objects.
     */
    public VAL getOrThrow(@NotNull KEY key, @NotNull String message, Object... objects) throws CommandSyntaxError {
        VAL v = getOrDefault(key, null);
        CommandSyntaxError.ifNull(v, message, objects);
        return v;
    }

    /**
     * Checks if the value associated with the specified key is not null, throwing a CommandSyntaxError if it is not null.
     *
     * @param key The key to check for a null value.
     * @throws CommandSyntaxError If the value is not null.
     */
    public void isNull(@NotNull KEY key) throws CommandSyntaxError {
        CommandSyntaxError.ifNotNull(getOrDefault(key, null));
    }

    /**
     * Checks if the value associated with the specified key is not null, throwing a CommandSyntaxError with the specified message if it is not null.
     *
     * @param key     The key to check for a null value.
     * @param message The message to include in the CommandSyntaxError if the value is not null.
     * @throws CommandSyntaxError If the value is not null, with the specified message.
     */
    public void isNull(@NotNull KEY key, @NotNull String message) throws CommandSyntaxError {
        CommandSyntaxError.ifNotNull(getOrDefault(key, null), message);
    }

    /**
     * Checks if the value associated with the specified key is not null, throwing a CommandSyntaxError with the specified message and additional objects if it is not null.
     *
     * @param key      The key to check for a null value.
     * @param message  The message to include in the CommandSyntaxError if the value is not null.
     * @param objects  Additional objects to include in the CommandSyntaxError message.
     * @throws CommandSyntaxError If the value is not null, with the specified message and additional objects.
     */
    public void isNull(@NotNull KEY key, @NotNull String message, Object... objects) throws CommandSyntaxError {
        CommandSyntaxError.ifNotNull(getOrDefault(key, null), message, objects);
    }
}
