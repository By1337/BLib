package org.by1337.blib.nms.v1_16_5.world.entity;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.by1337.blib.world.BLocation;
import org.by1337.blib.world.entity.BPose;
import org.by1337.blib.world.entity.PacketEntity;
import org.jetbrains.annotations.Nullable;

public abstract class PacketEntityImpl165 implements PacketEntity {
    protected static final EntityDataAccessor<Byte> DATA_SHARED_FLAGS_ID = new EntityDataAccessor(0, EntityDataSerializers.BYTE_);
    private static final EntityDataAccessor<Integer> DATA_AIR_SUPPLY_ID = new EntityDataAccessor(1, EntityDataSerializers.INT_);
    private static final EntityDataAccessor<Boolean> DATA_CUSTOM_NAME_VISIBLE = new EntityDataAccessor(3, EntityDataSerializers.BOOLEAN_);
    private static final EntityDataAccessor<Boolean> DATA_SILENT = new EntityDataAccessor(4, EntityDataSerializers.BOOLEAN_);
    private static final EntityDataAccessor<Boolean> DATA_NO_GRAVITY = new EntityDataAccessor(5, EntityDataSerializers.BOOLEAN_);
    protected static final EntityDataAccessor<Pose> DATA_POSE = new EntityDataAccessor(6, EntityDataSerializers.POSE_);
    private static final EntityDataAccessor<Optional<Component>> DATA_CUSTOM_NAME = new EntityDataAccessor(2, EntityDataSerializers.OPTIONAL_COMPONENT_);
    protected final SynchedEntityData entityData;
    protected static final Random random = new Random();
    private final EntityType<?> type;
    private BLocation location;
    private final int id;
    private float xa = 0.0F;
    private float ya = 0.0F;
    private float za = 0.0F;
    private final UUID uuid;

    public PacketEntityImpl165(EntityType<?> param0, BLocation location) {
        this.location = location;
        this.type = param0;
        this.id = random.nextInt();
        this.uuid = UUID.randomUUID();
        this.entityData = new SynchedEntityDataCustom();
        this.entityData.register(DATA_SHARED_FLAGS_ID, (byte)0);
        this.entityData.register(DATA_AIR_SUPPLY_ID, 300);
        this.entityData.register(DATA_CUSTOM_NAME_VISIBLE, false);
        this.entityData.register(DATA_CUSTOM_NAME, Optional.empty());
        this.entityData.register(DATA_SILENT, false);
        this.entityData.register(DATA_NO_GRAVITY, false);
        this.entityData.register(DATA_POSE, Pose.STANDING);
        this.defineSynchedData();
    }

    public UUID uuid() {
        return this.uuid;
    }

    public EntityType<?> getType() {
        return this.type;
    }

    public BLocation getLocation() {
        return this.location;
    }

    public int getId() {
        return this.id;
    }

    public float getXa() {
        return this.xa;
    }

    public float getYa() {
        return this.ya;
    }

    public float getZa() {
        return this.za;
    }

    protected abstract void defineSynchedData();

    public SynchedEntityData getEntityData() {
        return this.entityData;
    }

    public void setPose(Pose param0) {
        this.entityData.set(DATA_POSE, param0);
    }

    public Pose getPose_NMS() {
        return (Pose)this.entityData.get(DATA_POSE);
    }

    public BPose getPose() {
        return switch(this.getPose_NMS()) {
            case STANDING -> BPose.STANDING;
            case FALL_FLYING -> BPose.FALL_FLYING;
            case SLEEPING -> BPose.SLEEPING;
            case SWIMMING -> BPose.SWIMMING;
            case SPIN_ATTACK -> BPose.SPIN_ATTACK;
            case CROUCHING -> BPose.CROUCHING;
            case DYING -> BPose.DYING;
            default -> throw new IncompatibleClassChangeError();
        };
    }

    public boolean isSilent() {
        return this.entityData.get(DATA_SILENT);
    }

    public void setSilent(boolean silent) {
        this.entityData.set(DATA_SILENT, silent);
    }

    public boolean isNoGravity() {
        return this.entityData.get(DATA_NO_GRAVITY);
    }

    public void setNoGravity(boolean noGravity) {
        this.entityData.set(DATA_NO_GRAVITY, noGravity);
    }

    protected boolean getSharedFlag(int param0) {
        return (this.entityData.get(DATA_SHARED_FLAGS_ID) & 1 << param0) != 0;
    }

    protected void setSharedFlag(int param0, boolean param1) {
        byte var3 = this.entityData.get(DATA_SHARED_FLAGS_ID);
        if (param1) {
            this.entityData.set(DATA_SHARED_FLAGS_ID, (byte)(var3 | 1 << param0));
        } else {
            this.entityData.set(DATA_SHARED_FLAGS_ID, (byte)(var3 & ~(1 << param0)));
        }
    }

    public int getAirSupply() {
        return this.entityData.get(DATA_AIR_SUPPLY_ID);
    }

    public void setAirSupply(int airSupply) {
        this.entityData.set(DATA_AIR_SUPPLY_ID, airSupply);
    }

    public void setCustomName(@Nullable Component customName) {
        this.entityData.set(DATA_CUSTOM_NAME, Optional.ofNullable(customName));
    }

    @Nullable
    public Component getCustomName_NMS() {
        return (Component)((Optional)this.entityData.get(DATA_CUSTOM_NAME)).orElse(null);
    }

    public void setPose(BPose pose) {
        this.setPose(switch(pose) {
            case STANDING -> Pose.STANDING;
            case FALL_FLYING -> Pose.FALL_FLYING;
            case SLEEPING -> Pose.SLEEPING;
            case SWIMMING -> Pose.SWIMMING;
            case SPIN_ATTACK -> Pose.SPIN_ATTACK;
            case CROUCHING -> Pose.CROUCHING;
            case LONG_JUMPING -> throw new IllegalArgumentException("не поддерживается в версии 1.16.5");
            case DYING -> Pose.DYING;
            default -> throw new IncompatibleClassChangeError();
        });
    }

    public void setCustomName(@Nullable String customName) {
        this.setCustomName(CraftChatMessage.fromStringOrNull(customName));
    }

    @Nullable
    public String getCustomName() {
        return this.getCustomName_NMS().getString();
    }

    public boolean hasCustomName() {
        return ((Optional)this.entityData.get(DATA_CUSTOM_NAME)).isPresent();
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        this.entityData.set(DATA_CUSTOM_NAME_VISIBLE, customNameVisible);
    }

    public boolean isCustomNameVisible() {
        return this.entityData.get(DATA_CUSTOM_NAME_VISIBLE);
    }

    public void setShiftKeyDown(boolean shiftKeyDown) {
        this.setSharedFlag(1, shiftKeyDown);
    }

    public void setSprinting(boolean sprinting) {
        this.setSharedFlag(3, sprinting);
    }

    public void setSwimming(boolean swimming) {
        this.setSharedFlag(4, swimming);
    }

    public void setGlowing(boolean glowing) {
        this.setSharedFlag(6, glowing);
    }

    public void setInvisible(boolean invisible) {
        this.setSharedFlag(5, invisible);
    }

    public void setLocation(BLocation location) {
        this.location = location;
    }
}
