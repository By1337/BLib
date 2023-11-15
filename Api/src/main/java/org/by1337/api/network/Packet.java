package org.by1337.api.network;

import org.bukkit.entity.Player;
import org.by1337.api.world.BLocation;
import org.jetbrains.annotations.NotNull;

public interface Packet {
    void send(@NotNull Player player);
    void sendAll();
    void sendNearby(@NotNull BLocation location, int radius);
}
