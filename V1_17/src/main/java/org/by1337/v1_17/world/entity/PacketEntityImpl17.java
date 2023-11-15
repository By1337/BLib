package org.by1337.v1_17.world.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.by1337.api.world.BLocation;
import org.by1337.api.world.entity.BPose;
import org.by1337.api.world.entity.PacketEntity;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public abstract class PacketEntityImpl17 implements PacketEntity {
    protected static final EntityDataAccessor<Byte> DATA_SHARED_FLAGS_ID;
    private static final EntityDataAccessor<Integer> DATA_AIR_SUPPLY_ID;
    private static final EntityDataAccessor<Boolean> DATA_CUSTOM_NAME_VISIBLE;
    private static final EntityDataAccessor<Boolean> DATA_SILENT;
    private static final EntityDataAccessor<Boolean> DATA_NO_GRAVITY;
    protected static final EntityDataAccessor<Pose> DATA_POSE;
    private static final EntityDataAccessor<Optional<Component>> DATA_CUSTOM_NAME;

    protected final SynchedEntityData entityData;

    protected static final Random random = new Random();
    private final EntityType<?> type;

    private BLocation location;

    private final int id;

    private float xa = 0;
    private float ya = 0;
    private float za = 0;

    private final UUID uuid;

    public PacketEntityImpl17(EntityType<?> param0, BLocation location) {
        this.location = location;
        this.type = param0;
        id = random.nextInt();
        uuid = UUID.randomUUID();

        this.entityData = new SynchedEntityDataCustom();

        this.entityData.define(DATA_SHARED_FLAGS_ID, (byte) 0);
        this.entityData.define(DATA_AIR_SUPPLY_ID, 300);
        this.entityData.define(DATA_CUSTOM_NAME_VISIBLE, false);
        this.entityData.define(DATA_CUSTOM_NAME, Optional.empty());
        this.entityData.define(DATA_SILENT, false);
        this.entityData.define(DATA_NO_GRAVITY, false);
        this.entityData.define(DATA_POSE, Pose.STANDING);
        this.defineSynchedData();
    }

    public UUID uuid() {
        return uuid;
    }

    public EntityType<?> getType() {
        return type;
    }

    public BLocation getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public float getXa() {
        return xa;
    }

    public float getYa() {
        return ya;
    }

    public float getZa() {
        return za;
    }

    protected abstract void defineSynchedData();

    public SynchedEntityData getEntityData() {
        return this.entityData;
    }

    public void setPose(Pose param0) {
        this.entityData.set(DATA_POSE, param0);
    }

    public Pose getPose_NMS() {
        return this.entityData.get(DATA_POSE);
    }

    @Override
    public BPose getPose() {
        return switch (getPose_NMS()) {
            case STANDING -> BPose.STANDING;
            case FALL_FLYING -> BPose.FALL_FLYING;
            case SLEEPING -> BPose.SLEEPING;
            case SWIMMING -> BPose.SWIMMING;
            case SPIN_ATTACK -> BPose.SPIN_ATTACK;
            case CROUCHING -> BPose.CROUCHING;
            case LONG_JUMPING -> BPose.LONG_JUMPING;
            case DYING -> BPose.DYING;
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
            this.entityData.set(DATA_SHARED_FLAGS_ID, (byte) (var3 | 1 << param0));
        } else {
            this.entityData.set(DATA_SHARED_FLAGS_ID, (byte) (var3 & ~(1 << param0)));
        }
    }

    public int getAirSupply() {
        return this.entityData.get(DATA_AIR_SUPPLY_ID);
    }

    public void setAirSupply(int airSupply) {
        this.entityData.set(DATA_AIR_SUPPLY_ID, airSupply);
    }

    public void setCustomName(@Nullable Component param0) {
        this.entityData.set(DATA_CUSTOM_NAME, Optional.ofNullable(param0));
    }

    @Nullable
    public Component getCustomName_NMS() {
        return this.entityData.get(DATA_CUSTOM_NAME).orElse(null);
    }

    @Override
    public void setPose(BPose pose) {
        setPose(switch (pose) {
            case STANDING -> Pose.STANDING;
            case FALL_FLYING -> Pose.FALL_FLYING;
            case SLEEPING -> Pose.SLEEPING;
            case SWIMMING -> Pose.SWIMMING;
            case SPIN_ATTACK -> Pose.SPIN_ATTACK;
            case CROUCHING -> Pose.CROUCHING;
            case LONG_JUMPING -> Pose.LONG_JUMPING;
            case DYING -> Pose.DYING;
        });
    }

    @Override
    public void setCustomName(@Nullable String customName) {
        setCustomName(CraftChatMessage.fromStringOrNull(customName));
    }

    @Override
    public @Nullable String getCustomName() {
        return getCustomName_NMS().getString();
    }

    public boolean hasCustomName() {
        return this.entityData.get(DATA_CUSTOM_NAME).isPresent();
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

    static {
        try {
            Field field = Entity.class.getDeclaredField("Z"); //DATA_SHARED_FLAGS_ID
            field.setAccessible(true);
            DATA_SHARED_FLAGS_ID = (EntityDataAccessor<Byte>) field.get(null);

            field = Entity.class.getDeclaredField("aI"); //DATA_AIR_SUPPLY_ID
            field.setAccessible(true);
            DATA_AIR_SUPPLY_ID = (EntityDataAccessor<Integer>) field.get(null);

            field = Entity.class.getDeclaredField("aK"); // DATA_CUSTOM_NAME_VISIBLE
            field.setAccessible(true);
            DATA_CUSTOM_NAME_VISIBLE = (EntityDataAccessor<Boolean>) field.get(null);

            field = Entity.class.getDeclaredField("aL"); // DATA_SILENT
            field.setAccessible(true);
            DATA_SILENT = (EntityDataAccessor<Boolean>) field.get(null);

            field = Entity.class.getDeclaredField("aM"); //DATA_NO_GRAVITY
            field.setAccessible(true);
            DATA_NO_GRAVITY = (EntityDataAccessor<Boolean>) field.get(null);

            field = Entity.class.getDeclaredField("ad"); //DATA_POSE
            field.setAccessible(true);
            DATA_POSE = (EntityDataAccessor<Pose>) field.get(null);

            field = Entity.class.getDeclaredField("aJ"); //DATA_CUSTOM_NAME
            field.setAccessible(true);
            DATA_CUSTOM_NAME = (EntityDataAccessor<Optional<Component>>) field.get(null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
