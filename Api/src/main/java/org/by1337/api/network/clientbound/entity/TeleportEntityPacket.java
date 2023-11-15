package org.by1337.api.network.clientbound.entity;

import org.by1337.api.BLib;
import org.by1337.api.network.Packet;
import org.by1337.api.world.entity.PacketEntity;

public interface TeleportEntityPacket extends Packet {
    static TeleportEntityPacket newInstance(PacketEntity entity) {
        return BLib.getPacketFactory().createTeleportEntityPacket(entity);
    }

    static TeleportEntityPacket newInstance(int id, double x, double y, double z, float pitch, float yaw, boolean onGround) {
        return BLib.getPacketFactory().createTeleportEntityPacket(id, x, y, z, pitch, yaw, onGround);
    }

    int getId();

    void setId(int id);

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    double getZ();

    void setZ(double z);

    byte getPitch();

    void setPitch(byte pitch);

    void setPitch(float pitch);

    byte getYaw();

    void setYaw(byte yaw);

    void setYaw(float yaw);

    boolean isOnGround();

    void setOnGround(boolean onGround);
}
