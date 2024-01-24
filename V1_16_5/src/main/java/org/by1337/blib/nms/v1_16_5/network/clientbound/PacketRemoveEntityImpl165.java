package org.by1337.blib.nms.v1_16_5.network.clientbound;

import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.network.clientbound.entity.PacketRemoveEntity;
import org.by1337.blib.util.Version;
import org.by1337.blib.world.entity.PacketEntity;
import org.by1337.blib.nms.v1_16_5.network.PacketImpl165;

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
        return new PacketPlayOutEntityDestroy(this.ids);
    }

    @Override
    public int[] getIds() {
        return this.ids;
    }

    @Override
    public void setIds(int[] ids) {
        this.ids = ids;
    }
}
