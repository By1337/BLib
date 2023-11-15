package org.by1337.v1_17.network.clientbound;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.by1337.api.network.clientbound.entity.TeleportEntityPacket;
import org.by1337.api.world.entity.PacketEntity;
import org.by1337.v1_17.network.PacketImpl17;

public class TeleportEntityPacketImpl17 extends PacketImpl17 implements TeleportEntityPacket {
    private int id;
    private double x;
    private double y;
    private double z;
    private byte pitch;
    private byte yaw;
    private boolean onGround;

    public TeleportEntityPacketImpl17(PacketEntity entity) {
        this.id = entity.getId();
        this.x = entity.getLocation().getX();
        this.y = entity.getLocation().getY();
        this.z = entity.getLocation().getZ();
        this.pitch = (byte) (entity.getLocation().getPitch() * 256.0f / 360.0f);
        this.yaw = (byte) (entity.getLocation().getYaw() * 256.0f / 360.0f);
        this.onGround = true;
    }

    public TeleportEntityPacketImpl17(int id, double x, double y, double z, float pitch, float yaw, boolean onGround) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = (byte) (pitch * 256.0f / 360.0f);
        this.yaw = (byte) (yaw * 256.0f / 360.0f);
        this.onGround = onGround;
    }

    @Override
    protected Packet<?> create() {
        return new ClientboundTeleportEntityPacket(write(new FriendlyByteBuf(Unpooled.buffer())));
    }


    public FriendlyByteBuf write(FriendlyByteBuf byteBuf) {
        byteBuf.writeVarInt(this.id);
        byteBuf.writeDouble(this.x);
        byteBuf.writeDouble(this.y);
        byteBuf.writeDouble(this.z);
        byteBuf.writeByte(this.pitch);
        byteBuf.writeByte(this.yaw);
        byteBuf.writeBoolean(this.onGround);
        return byteBuf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public byte getPitch() {
        return pitch;
    }

    public void setPitch(byte pitch) {
        this.pitch = pitch;
    }

    public byte getYaw() {
        return yaw;
    }

    public void setYaw(byte yaw) {
        this.yaw = yaw;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = (byte) ((int) (pitch * 256.0F / 360.0F));
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = (byte) ((int) (yaw * 256.0F / 360.0F));
    }
}
