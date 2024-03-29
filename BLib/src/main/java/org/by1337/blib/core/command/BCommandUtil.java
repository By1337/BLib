package org.by1337.blib.core.command;

import org.bukkit.entity.Player;
import org.by1337.blib.command.CommandUtil;
import org.by1337.blib.world.BLocation;
import org.by1337.blib.core.SummonCommand;
import org.by1337.blib.core.chat.TellRaw;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BCommandUtil implements CommandUtil {
    @Override
    public void summon(@NotNull String entityType, @NotNull BLocation location, @Nullable String nbt) {
        SummonCommand.execute(entityType, location, nbt);
    }

    @Override
    public void tellRaw(@NotNull String message, @NotNull Player player) {
        TellRaw.tell(message, player);
    }
}
