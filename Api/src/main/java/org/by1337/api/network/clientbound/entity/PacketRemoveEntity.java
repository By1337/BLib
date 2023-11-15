package org.by1337.api.network.clientbound.entity;

import org.by1337.api.BLib;
import org.by1337.api.network.Packet;
import org.by1337.api.world.entity.PacketEntity;

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
