package org.by1337.v1_16_5.network;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.by1337.api.network.Packet;
import org.by1337.api.world.BLocation;
import org.jetbrains.annotations.NotNull;

public abstract class PacketImpl165 implements Packet {
    @Override
    public void send(@NotNull Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(this.create());
    }

    @Override
    public void sendAll() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    @Override
    public void sendNearby(@NotNull BLocation location, int radius) {
        location.getLocation()
                .getWorld()
                .getNearbyEntities(location.getLocation(), radius, radius, radius)
                .stream()
                .filter(entity -> entity instanceof CraftPlayer)
                .map(entity -> (Player) entity)
                .forEach(this::send);
    }

    protected abstract net.minecraft.server.v1_16_R3.Packet<?> create();
}
