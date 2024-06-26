package org.by1337.blib.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * A functional interface representing an executor for processing a placeholder's value.
 */
@FunctionalInterface
@Deprecated(forRemoval = true, since = "1.0.7.1-beta")
public interface PlaceholderExecutor {
    /**
     * Executes the placeholder for the specified player.
     *
     * @param player The player for whom the placeholder is executed. Can be null.
     * @return The processed placeholder value, or null if no value is available.
     */
    @Nullable
    String run(@Nullable Player player);
}
