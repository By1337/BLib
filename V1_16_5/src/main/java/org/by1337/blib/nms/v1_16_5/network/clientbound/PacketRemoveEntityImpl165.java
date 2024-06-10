package org.by1337.blib.nms.v1_16_5.network.clientbound;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import org.by1337.blib.network.clientbound.entity.PacketRemoveEntity;
import org.by1337.blib.nms.v1_16_5.network.PacketImpl165;
import org.by1337.blib.world.entity.PacketEntity;

public class PacketRemoveEntityImpl165 extends PacketImpl165 implements PacketRemoveEntity {
    private int[] ids;

    public PacketRemoveEntityImpl165(int... ids) {
        this.ids = ids;
    }

    public PacketRemoveEntityImpl165(PacketEntity entity) {
        this.ids = new int[]{entity.getId()};
    }

    @Override
    protected Packet<?> create() {
        return new ClientboundRemoveEntitiesPacket(this.ids);
    }

    public int[] getIds() {
        return this.ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }
}
