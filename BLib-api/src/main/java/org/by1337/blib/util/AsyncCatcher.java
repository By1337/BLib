package org.by1337.blib.util;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * An interface for catching asynchronous operations (async tasks) in the application.
 */
@Deprecated
public interface AsyncCatcher {
    /**
     * Handles catching an asynchronous operation with the given identifier.
     *
     * @param identifier A unique identifier or description of the asynchronous operation.
     */
    default void catchOp(@NotNull String identifier) {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Asynchronous " + identifier + "!");
        }
    }
}
