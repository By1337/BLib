package org.by1337.blib.core.command;

import org.bukkit.entity.Player;
import org.by1337.blib.command.CommandUtil;
import org.by1337.blib.command.SummonCommand;
import org.by1337.blib.core.nms.NmsFactory;
import org.by1337.blib.world.BLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class BCommandUtil implements CommandUtil {
    private final SummonCommand summonCommand = NmsFactory.get().get(SummonCommand.class);

    @Override
    public void summon(@NotNull String entityType, @NotNull BLocation location, @Nullable String nbt) {
        summonCommand.spawn(entityType, location, nbt);
    }

    @Override
    public void tellRaw(@NotNull String message, @NotNull Player player) {
        player.sendRawMessage(message);
    }
}
