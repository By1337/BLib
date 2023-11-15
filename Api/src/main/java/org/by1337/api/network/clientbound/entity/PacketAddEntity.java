package org.by1337.api.network.clientbound.entity;

import org.by1337.api.BLib;
import org.by1337.api.network.Packet;
import org.by1337.api.world.entity.PacketEntity;

import java.util.UUID;

public interface PacketAddEntity extends Packet {
    static PacketAddEntity newInstance(PacketEntity entity) {
        return BLib.getPacketFactory().createPacketAddEntity(entity);
    }

    int getId();

    void setId(int id);

    UUID getUuid();

    void setUuid(UUID uuid);

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    double getZ();

    void setZ(double z);

    int getXa();

    void setXa(int xa);

    int getYa();

    void setYa(int ya);

    int getZa();

    void setZa(int za);

    int getPitch();

    void setPitch(int pitch);

    void setPitch(float pitch);

    int getYaw();

    void setYaw(int yaw);

    void setYaw(float yaw);

    int getData();

    void setData(int data);

    <T> T getType();

    <T> void setType(T type);

}
