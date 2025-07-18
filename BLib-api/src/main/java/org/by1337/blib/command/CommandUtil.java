package org.by1337.blib.command;

import org.bukkit.entity.Player;
import org.by1337.blib.world.BLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Deprecated(forRemoval = true)
@SuppressWarnings({"removal"})
public interface CommandUtil {
    CommandUtil INSTANCE = new CommandUtil() {};
    default void summon(@NotNull String entityType, @NotNull BLocation location, @Nullable String nbt) {
        SummonCommand.INSTANCE.spawn(entityType, location, nbt);
    }

    default void tellRaw(@NotNull String message, @NotNull Player player) {
        player.sendRawMessage(message);
    }
}
