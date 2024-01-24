package org.by1337.blib.nms.v1_16_5.network.clientbound;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.*;
import org.by1337.blib.network.clientbound.entity.PacketAddEntity;
import org.by1337.blib.world.entity.PacketEntity;
import org.by1337.blib.nms.v1_16_5.network.PacketImpl165;
import org.by1337.blib.nms.v1_16_5.world.entity.PacketEntityImpl165;

import java.io.IOException;
import java.util.UUID;

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
    private EntityTypes<?> type;
    private int data;

    @Override
    protected Packet<?> create() {
        try {
            PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();
            packet.a(write(new PacketDataSerializer(Unpooled.buffer())));
            return packet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PacketAddEntityImpl165(PacketEntity packetEntity) {
        this.init((PacketEntityImpl165) packetEntity, 0);
    }

    public void init(int id, UUID uuid, double x, double y, double z, float pitch, float yaw, EntityTypes<?> types, int data, Vec3D var12) {
        this.id = id;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = MathHelper.d(pitch * 256.0f / 360.0f);
        this.yaw = MathHelper.d(yaw * 256.0f / 360.0f);
        this.type = types;
        this.data = data;
        this.xa = (int) (MathHelper.a(var12.x, -3.9, 3.9) * 8000.0);
        this.ya = (int) (MathHelper.a(var12.y, -3.9, 3.9) * 8000.0);
        this.za = (int) (MathHelper.a(var12.z, -3.9, 3.9) * 8000.0);
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
                new Vec3D(entity.getXa(), entity.getYa(), entity.getZa())
        );
    }

    private PacketDataSerializer write(PacketDataSerializer byteBuf) {
        byteBuf.d(this.id);
        byteBuf.a(this.uuid);
        byteBuf.d(IRegistry.ENTITY_TYPE.a(this.type));
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

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
    public int getXa() {
        return this.xa;
    }

    @Override
    public void setXa(int xa) {
        this.xa = xa;
    }

    @Override
    public int getYa() {
        return this.ya;
    }

    @Override
    public void setYa(int ya) {
        this.ya = ya;
    }

    @Override
    public int getZa() {
        return this.za;
    }

    @Override
    public void setZa(int za) {
        this.za = za;
    }

    @Override
    public int getPitch() {
        return this.pitch;
    }

    @Override
    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = MathHelper.d(pitch * 256.0f / 360.0f);
    }

    @Override
    public int getYaw() {
        return this.yaw;
    }

    @Override
    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = MathHelper.d(yaw * 256.0f / 360.0f);
    }

    public EntityTypes<?> getType() {
        return this.type;
    }

    public void setType(EntityTypes<?> type) {
        this.type = type;
    }

    @Override
    public int getData() {
        return this.data;
    }

    @Override
    public void setData(int data) {
        this.data = data;
    }

    @Override
    public <T> void setType(T type) {
        this.setType((EntityTypes<?>) type);
    }
}