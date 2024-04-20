package org.by1337.blib.hook.papi;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PlaceholderExecutor {
    @Nullable
    String run(@Nullable Player player, @NotNull String[] args);
}
