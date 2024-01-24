package org.by1337.blib.command;

import org.bukkit.entity.Player;
import org.by1337.blib.world.BLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandUtil {
    void summon(@NotNull String entityType, @NotNull BLocation location, @Nullable String nbt);

    void tellRaw(@NotNull String message, @NotNull Player player);
}
