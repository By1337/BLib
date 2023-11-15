package org.by1337.v1_17.network.clientbound;

import io.netty.buffer.Unpooled;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.by1337.api.network.clientbound.entity.PacketAddEntity;
import org.by1337.api.world.entity.PacketEntity;
import org.by1337.v1_17.network.PacketImpl17;
import org.by1337.v1_17.world.entity.PacketEntityImpl17;

import java.util.UUID;

public class PacketAddEntityImpl17 extends PacketImpl17 implements PacketAddEntity {
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
        return new ClientboundAddEntityPacket(write(new FriendlyByteBuf(Unpooled.buffer())));
    }

    public PacketAddEntityImpl17(PacketEntity entity) {
        init((PacketEntityImpl17) entity, 0);

    }

    private void init(int var0, UUID var1, double var2, double var4, double var6, float pitch, float yaw, EntityType<?> var10, int data, Vec3 var12) {
        this.id = var0;
        this.uuid = var1;
        this.x = var2;
        this.y = var4;
        this.z = var6;
        this.pitch = (byte) Mth.floor(pitch * 256.0f / 360.0f);
        this.yaw = (byte) Mth.floor(yaw * 256.0f / 360.0f);
        this.type = var10;
        this.data = data;
        this.xa = (int) (Mth.clamp(var12.x, -3.9, 3.9) * 8000.0);
        this.ya = (int) (Mth.clamp(var12.y, -3.9, 3.9) * 8000.0);
        this.za = (int) (Mth.clamp(var12.z, -3.9, 3.9) * 8000.0);
    }

    private void init(PacketEntityImpl17 entity, int data) {
        init(
                entity.getId(),
                entity.uuid(),
                entity.getLocation().getX(),
                entity.getLocation().getY(),
                entity.getLocation().getZ(),
                entity.getLocation().getPitch(),
                entity.getLocation().getYaw(),
                entity.getType(),
                data,
                new Vec3(entity.getXa(), entity.getYa(), entity.getZa()));
    }

    private FriendlyByteBuf write(FriendlyByteBuf byteBuf) {
        byteBuf.writeVarInt(this.id);
        byteBuf.writeUUID(this.uuid);
        byteBuf.writeVarInt(Registry.ENTITY_TYPE.getId(this.type));
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
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public int getXa() {
        return xa;
    }

    public void setXa(int xa) {
        this.xa = xa;
    }

    public int getYa() {
        return ya;
    }

    public void setYa(int ya) {
        this.ya = ya;
    }

    public int getZa() {
        return za;
    }

    public void setZa(int za) {
        this.za = za;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = (byte) Mth.floor(pitch * 256.0f / 360.0f);
    }

    public int getYaw() {
        return yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = (byte) Mth.floor(yaw * 256.0f / 360.0f);
    }

    public EntityType<?> getType() {
        return type;
    }

    public void setType(EntityType<?> type) {
        this.type = type;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public <T> void setType(T type) {
        setType((EntityType<?>) type);
    }
}
