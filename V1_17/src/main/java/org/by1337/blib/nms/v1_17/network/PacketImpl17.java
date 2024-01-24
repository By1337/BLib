package org.by1337.blib.nms.v1_17.network;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.by1337.blib.network.Packet;
import org.by1337.blib.world.BLocation;
import org.jetbrains.annotations.NotNull;

public abstract class PacketImpl17 implements Packet {
    @Override
    public void send(@NotNull Player player) {
        ((CraftPlayer) player).getHandle().connection.send(create());
    }

    @Override
    public void sendAll() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    @Override
    public void sendNearby(@NotNull BLocation location, int radius) {
        location.getLocation().getWorld().getNearbyEntities(location.getLocation(), radius, radius, radius).stream().filter(entity -> entity instanceof CraftPlayer).map(entity -> (Player) entity).forEach(this::send);
    }

    protected abstract net.minecraft.network.protocol.Packet<?> create();
}
