package org.by1337.v1_16_5.network.clientbound;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
import org.by1337.api.network.clientbound.entity.TeleportEntityPacket;
import org.by1337.api.world.entity.PacketEntity;
import org.by1337.v1_16_5.network.PacketImpl165;

import java.io.IOException;

public class TeleportEntityPacketImp165 extends PacketImpl165 implements TeleportEntityPacket {
    private int id;
    private double x;
    private double y;
    private double z;
    private byte pitch;
    private byte yaw;
    private boolean onGround;

    public TeleportEntityPacketImp165(PacketEntity entity) {
        this.id = entity.getId();
        this.x = entity.getLocation().getX();
        this.y = entity.getLocation().getY();
        this.z = entity.getLocation().getZ();
        this.pitch = (byte) ((int) (entity.getLocation().getPitch() * 256.0F / 360.0F));
        this.yaw = (byte) ((int) (entity.getLocation().getYaw() * 256.0F / 360.0F));
        this.onGround = true;
    }

    public TeleportEntityPacketImp165(int id, double x, double y, double z, float pitch, float yaw, boolean onGround) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = (byte) ((int) (pitch * 256.0F / 360.0F));
        this.yaw = (byte) ((int) (yaw * 256.0F / 360.0F));;
        this.onGround = onGround;
    }

    @Override
    protected Packet<?> create() {
        try {
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
            packet.a(this.write(new PacketDataSerializer(Unpooled.buffer())));
            return packet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PacketDataSerializer write(PacketDataSerializer byteBuf) {
        byteBuf.d(this.id);
        byteBuf.writeDouble(this.x);
        byteBuf.writeDouble(this.y);
        byteBuf.writeDouble(this.z);
        byteBuf.writeByte(this.pitch);
        byteBuf.writeByte(this.yaw);
        byteBuf.writeBoolean(this.onGround);
        return byteBuf;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getZ() {
        return this.z;
    }

    @Override
    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public byte getPitch() {
        return this.pitch;
    }

    @Override
    public void setPitch(byte pitch) {
        this.pitch = pitch;
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = (byte) ((int) (pitch * 256.0F / 360.0F));
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = (byte) ((int) (yaw * 256.0F / 360.0F));
    }

    @Override
    public byte getYaw() {
        return this.yaw;
    }

    @Override
    public void setYaw(byte yaw) {
        this.yaw = yaw;
    }

    @Override
    public boolean isOnGround() {
        return this.onGround;
    }

    @Override
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
