package org.by1337.blib.nms.v1_16_5.network.clientbound;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import org.by1337.blib.network.clientbound.entity.TeleportEntityPacket;
import org.by1337.blib.nms.v1_16_5.network.PacketImpl165;
import org.by1337.blib.world.entity.PacketEntity;

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
        this.pitch = (byte)((int)(entity.getLocation().getPitch() * 256.0F / 360.0F));
        this.yaw = (byte)((int)(entity.getLocation().getYaw() * 256.0F / 360.0F));
        this.onGround = true;
    }

    public TeleportEntityPacketImp165(int id, double x, double y, double z, float pitch, float yaw, boolean onGround) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = (byte)((int)(pitch * 256.0F / 360.0F));
        this.yaw = (byte)((int)(yaw * 256.0F / 360.0F));
        this.onGround = onGround;
    }

    @Override
    protected Packet<?> create() {
        try {
            ClientboundTeleportEntityPacket packet = new ClientboundTeleportEntityPacket();
            packet.read(this.write(new FriendlyByteBuf(Unpooled.buffer())));
            return packet;
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
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
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public byte getPitch() {
        return this.pitch;
    }

    public void setPitch(byte pitch) {
        this.pitch = pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = (byte)((int)(pitch * 256.0F / 360.0F));
    }

    public void setYaw(float yaw) {
        this.yaw = (byte)((int)(yaw * 256.0F / 360.0F));
    }

    public byte getYaw() {
        return this.yaw;
    }

    public void setYaw(byte yaw) {
        this.yaw = yaw;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
