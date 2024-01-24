package org.by1337.blib.nms.v1_17.network.clientbound;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundRemoveEntityPacket;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.by1337.blib.network.clientbound.entity.PacketRemoveEntity;
import org.by1337.blib.world.entity.PacketEntity;
import org.by1337.blib.nms.v1_17.network.PacketImpl17;
import org.jetbrains.annotations.NotNull;

public class PacketRemoveEntityImpl17 extends PacketImpl17 implements PacketRemoveEntity {
    private int[] ids;

    public PacketRemoveEntityImpl17(int... ids) {
        this.ids = ids;
    }

    public PacketRemoveEntityImpl17(PacketEntity entity) {
        ids = new int[]{entity.getId()};
    }

    @Override
    protected Packet<?> create() {
        throw new IllegalStateException("it is nop!");
    }

    @Override
    public void send(@NotNull Player player) {
        for (int id : ids) {
            ((CraftPlayer) player).getHandle().connection.send(new ClientboundRemoveEntityPacket(id));
        }

    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }
}
