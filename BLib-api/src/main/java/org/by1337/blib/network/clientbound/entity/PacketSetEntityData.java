package org.by1337.blib.network.clientbound.entity;

import org.by1337.blib.BLib;
import org.by1337.blib.network.Packet;
import org.by1337.blib.world.entity.PacketEntity;
@Deprecated(forRemoval = true)
public interface PacketSetEntityData extends Packet {
    static PacketSetEntityData newInstance(PacketEntity entity) {
        return BLib.getPacketFactory().createPacketSetEntityData(entity);
    }
    int getId();
    void setId(int id);
}
