package org.by1337.blib.network;

import org.bukkit.entity.Player;
import org.by1337.blib.world.BLocation;
import org.jetbrains.annotations.NotNull;

public interface Packet {
    void send(@NotNull Player player);
    void sendAll();
    void sendNearby(@NotNull BLocation location, int radius);
}
