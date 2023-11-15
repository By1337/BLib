package org.by1337.api.network.clientbound.entity;

import org.by1337.api.BLib;
import org.by1337.api.network.Packet;
import org.by1337.api.world.entity.PacketEntity;

public interface PacketSetEntityData extends Packet {
    static PacketSetEntityData newInstance(PacketEntity entity) {
        return BLib.getPacketFactory().createPacketSetEntityData(entity);
    }
    int getId();
    void setId(int id);
}
