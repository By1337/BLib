package org.by1337.blib.network.clientbound.entity;

import org.by1337.blib.BLib;
import org.by1337.blib.network.Packet;
import org.by1337.blib.world.entity.PacketEntity;

public interface PacketRemoveEntity extends Packet {
    static PacketRemoveEntity newInstance(PacketEntity entity) {
        return BLib.getPacketFactory().createPacketRemoveEntity(entity);
    }
    static PacketRemoveEntity newInstance(int... ids) {
        return BLib.getPacketFactory().createPacketRemoveEntity(ids);
    }

    int[] getIds();

    void setIds(int[] ids);

}
