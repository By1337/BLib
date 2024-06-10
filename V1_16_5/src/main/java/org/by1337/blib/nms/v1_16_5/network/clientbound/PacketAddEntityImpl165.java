package org.by1337.blib.nms.v1_16_5.network.clientbound;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.by1337.blib.network.clientbound.entity.PacketAddEntity;
import org.by1337.blib.nms.v1_16_5.network.PacketImpl165;
import org.by1337.blib.nms.v1_16_5.world.entity.PacketEntityImpl165;
import org.by1337.blib.world.entity.PacketEntity;

public class PacketAddEntityImpl165 extends PacketImpl165 implements PacketAddEntity {
    private int id;
    private UUID uuid;
    private double x;
    private double y;
    private double z;
    private int xa;
    private int ya;
    private int za;
    private int pitch;
    private int yaw;
    private EntityType<?> type;
    private int data;

    @Override
    protected Packet<?> create() {
        try {
            ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket();
            packet.read_(this.write(new FriendlyByteBuf(Unpooled.buffer())));
            return packet;
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public PacketAddEntityImpl165(PacketEntity packetEntity) {
        this.init((PacketEntityImpl165)packetEntity, 0);
    }

    public void init(int id, UUID uuid, double x, double y, double z, float pitch, float yaw, EntityType<?> types, int data, Vec3 var12) {
        this.id = id;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = Mth.floor_(pitch * 256.0F / 360.0F);
        this.yaw = Mth.floor_(yaw * 256.0F / 360.0F);
        this.type = types;
        this.data = data;
        this.xa = (int)(Mth.clamp_(var12.x, -3.9, 3.9) * 8000.0);
        this.ya = (int)(Mth.clamp_(var12.y, -3.9, 3.9) * 8000.0);
        this.za = (int)(Mth.clamp_(var12.z, -3.9, 3.9) * 8000.0);
    }

    private void init(PacketEntityImpl165 entity, int data) {
        this.init(
                entity.getId(),
                entity.uuid(),
                entity.getLocation().getX(),
                entity.getLocation().getY(),
                entity.getLocation().getZ(),
                entity.getLocation().getPitch(),
                entity.getLocation().getYaw(),
                entity.getType(),
                data,
                new Vec3((double)entity.getXa(), (double)entity.getYa(), (double)entity.getZa())
        );
    }

    private FriendlyByteBuf write(FriendlyByteBuf byteBuf) {
        byteBuf.writeVarInt_(this.id);
        byteBuf.writeUUID_(this.uuid);
        byteBuf.writeVarInt_(Registry.ENTITY_TYPE.getId_(this.type));
        byteBuf.writeDouble(this.x);
        byteBuf.writeDouble(this.y);
        byteBuf.writeDouble(this.z);
        byteBuf.writeByte(this.pitch);
        byteBuf.writeByte(this.yaw);
        byteBuf.writeInt(this.data);
        byteBuf.writeShort(this.xa);
        byteBuf.writeShort(this.ya);
        byteBuf.writeShort(this.za);
        return byteBuf;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public int getXa() {
        return this.xa;
    }

    public void setXa(int xa) {
        this.xa = xa;
    }

    public int getYa() {
        return this.ya;
    }

    public void setYa(int ya) {
        this.ya = ya;
    }

    public int getZa() {
        return this.za;
    }

    public void setZa(int za) {
        this.za = za;
    }

    public int getPitch() {
        return this.pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = Mth.floor_(pitch * 256.0F / 360.0F);
    }

    public int getYaw() {
        return this.yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = Mth.floor_(yaw * 256.0F / 360.0F);
    }

    public EntityType<?> getType() {
        return this.type;
    }

    public void setType(EntityType<?> type) {
        this.type = type;
    }

    public int getData() {
        return this.data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public <T> void setType(T type) {
        this.setType((EntityType<?>)type);
    }
}
