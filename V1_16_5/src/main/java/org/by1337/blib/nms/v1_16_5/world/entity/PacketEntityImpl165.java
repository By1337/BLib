package org.by1337.blib.nms.v1_16_5.world.entity;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.by1337.blib.world.BLocation;
import org.by1337.blib.world.entity.BPose;
import org.by1337.blib.world.entity.PacketEntity;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public abstract class PacketEntityImpl165 implements PacketEntity {
    protected static final DataWatcherObject<Byte> DATA_SHARED_FLAGS_ID;
    private static final DataWatcherObject<Integer> DATA_AIR_SUPPLY_ID;
    private static final DataWatcherObject<Boolean> DATA_CUSTOM_NAME_VISIBLE;
    private static final DataWatcherObject<Boolean> DATA_SILENT;
    private static final DataWatcherObject<Boolean> DATA_NO_GRAVITY;
    protected static final DataWatcherObject<EntityPose> DATA_POSE;
    private static final DataWatcherObject<Optional<IChatBaseComponent>> DATA_CUSTOM_NAME;
    protected final DataWatcher entityData;
    protected static final Random random;
    private final EntityTypes<?> type;
    private BLocation location;
    private final int id;
    private float xa = 0.0f;
    private float ya = 0.0f;
    private float za = 0.0f;
    private final UUID uuid;

    public PacketEntityImpl165(EntityTypes<?> param0, BLocation location) {
        this.location = location;
        this.type = param0;
        this.id = random.nextInt();
        this.uuid = UUID.randomUUID();
        this.entityData = new SynchedEntityDataCustom();
        this.entityData.register(DATA_SHARED_FLAGS_ID, (byte) 0);
        this.entityData.register(DATA_AIR_SUPPLY_ID, 300);
        this.entityData.register(DATA_CUSTOM_NAME_VISIBLE, false);
        this.entityData.register(DATA_CUSTOM_NAME, Optional.empty());
        this.entityData.register(DATA_SILENT, false);
        this.entityData.register(DATA_NO_GRAVITY, false);
        this.entityData.register(DATA_POSE, EntityPose.STANDING);
        this.defineSynchedData();
    }

    @Override
    public UUID uuid() {
        return this.uuid;
    }

    public EntityTypes<?> getType() {
        return this.type;
    }

    @Override
    public BLocation getLocation() {
        return this.location;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public float getXa() {
        return this.xa;
    }

    @Override
    public float getYa() {
        return this.ya;
    }

    @Override
    public float getZa() {
        return this.za;
    }

    protected abstract void defineSynchedData();

    public DataWatcher getEntityData() {
        return this.entityData;
    }

    public void setPose(EntityPose param0) {
        this.entityData.set(DATA_POSE, param0);
    }

    public EntityPose getPose_NMS() {
        return (EntityPose) this.entityData.get(DATA_POSE);
    }

    @Override
    public BPose getPose() {
        return switch (this.getPose_NMS()) {
            case STANDING -> BPose.STANDING;
            case FALL_FLYING -> BPose.FALL_FLYING;
            case SLEEPING -> BPose.SLEEPING;
            case SWIMMING -> BPose.SWIMMING;
            case SPIN_ATTACK -> BPose.SPIN_ATTACK;
            case CROUCHING -> BPose.CROUCHING;
            case DYING -> BPose.DYING;
        };
    }

    @Override
    public boolean isSilent() {
        return this.entityData.get(DATA_SILENT);
    }

    @Override
    public void setSilent(boolean silent) {
        this.entityData.set(DATA_SILENT, silent);
    }

    @Override
    public boolean isNoGravity() {
        return this.entityData.get(DATA_NO_GRAVITY);
    }

    @Override
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

    @Override
    public int getAirSupply() {
        return this.entityData.get(DATA_AIR_SUPPLY_ID);
    }

    @Override
    public void setAirSupply(int airSupply) {
        this.entityData.set(DATA_AIR_SUPPLY_ID, airSupply);
    }

    public void setCustomName(@Nullable IChatBaseComponent customName) {
        this.entityData.set(DATA_CUSTOM_NAME, Optional.ofNullable(customName));
    }

    @Nullable
    public IChatBaseComponent getCustomName_NMS() {
        return this.entityData.get(DATA_CUSTOM_NAME).orElse(null);
    }

    @Override
    public void setPose(BPose pose) {
        this.setPose(switch (pose) {
            case STANDING -> EntityPose.STANDING;
            case FALL_FLYING -> EntityPose.FALL_FLYING;
            case SLEEPING -> EntityPose.SLEEPING;
            case SWIMMING -> EntityPose.SWIMMING;
            case SPIN_ATTACK -> EntityPose.SPIN_ATTACK;
            case CROUCHING -> EntityPose.CROUCHING;
            case LONG_JUMPING -> throw new IllegalArgumentException("не поддерживается в версии 1.16.5");
            case DYING -> EntityPose.DYING;
        });
    }

    @Override
    public void setCustomName(@Nullable String customName) {
        this.setCustomName(CraftChatMessage.fromStringOrNull(customName));
    }

    @Override
    @Nullable
    public String getCustomName() {
        return this.getCustomName_NMS().getString();
    }

    @Override
    public boolean hasCustomName() {
        return (this.entityData.get(DATA_CUSTOM_NAME)).isPresent();
    }

    @Override
    public void setCustomNameVisible(boolean customNameVisible) {
        this.entityData.set(DATA_CUSTOM_NAME_VISIBLE, customNameVisible);
    }

    @Override
    public boolean isCustomNameVisible() {
        return (Boolean) this.entityData.get(DATA_CUSTOM_NAME_VISIBLE);
    }

    @Override
    public void setShiftKeyDown(boolean shiftKeyDown) {
        this.setSharedFlag(1, shiftKeyDown);
    }

    @Override
    public void setSprinting(boolean sprinting) {
        this.setSharedFlag(3, sprinting);
    }

    @Override
    public void setSwimming(boolean swimming) {
        this.setSharedFlag(4, swimming);
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.setSharedFlag(6, glowing);
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.setSharedFlag(5, invisible);
    }

    @Override
    public void setLocation(BLocation location) {
        this.location = location;
    }

    static {
        random = new Random();
        try {
            Field field = Entity.class.getDeclaredField("S");
            field.setAccessible(true);
            DATA_SHARED_FLAGS_ID = (DataWatcherObject<Byte>) field.get(null);

            field = Entity.class.getDeclaredField("AIR_TICKS");
            field.setAccessible(true);
            DATA_AIR_SUPPLY_ID = (DataWatcherObject<Integer>) field.get(null);

            field = Entity.class.getDeclaredField("ar");
            field.setAccessible(true);
            DATA_CUSTOM_NAME_VISIBLE = (DataWatcherObject<Boolean>) field.get(null);

            field = Entity.class.getDeclaredField("as");
            field.setAccessible(true);
            DATA_SILENT = (DataWatcherObject<Boolean>) field.get(null);

            field = Entity.class.getDeclaredField("at");
            field.setAccessible(true);
            DATA_NO_GRAVITY = (DataWatcherObject<Boolean>) field.get(null);

            field = Entity.class.getDeclaredField("POSE");
            field.setAccessible(true);
            DATA_POSE = (DataWatcherObject<EntityPose>) field.get(null);

            field = Entity.class.getDeclaredField("aq");
            field.setAccessible(true);
            DATA_CUSTOM_NAME = (DataWatcherObject<Optional<IChatBaseComponent>>) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

//        DATA_SHARED_FLAGS_ID = new DataWatcherObject<>(0, DataWatcherRegistry.a);
//        DATA_AIR_SUPPLY_ID = new DataWatcherObject<>(1, DataWatcherRegistry.b);
//        DATA_CUSTOM_NAME = new DataWatcherObject<>(2, DataWatcherRegistry.f);
//        DATA_CUSTOM_NAME_VISIBLE = new DataWatcherObject<>(3, DataWatcherRegistry.i);
//        DATA_SILENT = new DataWatcherObject<>(4, DataWatcherRegistry.i);
//        DATA_NO_GRAVITY = new DataWatcherObject<>(5, DataWatcherRegistry.i);
//        DATA_POSE = new DataWatcherObject<>(6, DataWatcherRegistry.s);


//        System.out.println(PacketEntityImpl165.class.getName() + " ============== ");
//
//        System.out.println("DATA_SHARED_FLAGS_ID = " + DATA_SHARED_FLAGS_ID.a());
//        System.out.println("DATA_AIR_SUPPLY_ID = " + DATA_AIR_SUPPLY_ID.a());
//        System.out.println("DATA_CUSTOM_NAME_VISIBLE = " + DATA_CUSTOM_NAME_VISIBLE.a());
//        System.out.println("DATA_SILENT = " + DATA_SILENT.a());
//        System.out.println("DATA_NO_GRAVITY = " + DATA_NO_GRAVITY.a());
//        System.out.println("DATA_POSE = " + DATA_POSE.a());
//        System.out.println("DATA_CUSTOM_NAME = " + DATA_CUSTOM_NAME.a());
    }
}
