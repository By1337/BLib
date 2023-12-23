package org.by1337.v1_16_5.world.entity;

import net.minecraft.server.v1_16_R3.*;
import org.by1337.api.world.BLocation;
import org.by1337.api.world.entity.BInteractionHand;
import org.by1337.api.world.entity.PacketLivingEntity;

import java.lang.reflect.Field;
import java.util.Optional;

public abstract class PacketLivingEntityImp165 extends PacketEntityImpl165 implements PacketLivingEntity {
    protected static final DataWatcherObject<Byte> DATA_LIVING_ENTITY_FLAGS;
    private static final DataWatcherObject<Float> DATA_HEALTH_ID;
    private static final DataWatcherObject<Integer> DATA_EFFECT_COLOR_ID;
    private static final DataWatcherObject<Boolean> DATA_EFFECT_AMBIENCE_ID;
    private static final DataWatcherObject<Integer> DATA_ARROW_COUNT_ID;
    private static final DataWatcherObject<Integer> DATA_STINGER_COUNT_ID;
    private static final DataWatcherObject<Optional<BlockPosition>> SLEEPING_POS_ID;

    public PacketLivingEntityImp165(EntityTypes<?> param0, BLocation location) {
        super(param0, location);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.register(DATA_LIVING_ENTITY_FLAGS, (byte) 0);
        this.entityData.register(DATA_EFFECT_COLOR_ID, 0);
        this.entityData.register(DATA_EFFECT_AMBIENCE_ID, false);
        this.entityData.register(DATA_ARROW_COUNT_ID, 0);
        this.entityData.register(DATA_STINGER_COUNT_ID, 0);
        this.entityData.register(DATA_HEALTH_ID, 1.0F);
        this.entityData.register(SLEEPING_POS_ID, Optional.empty());
    }

    @Override
    public float getHealth() {
        return this.entityData.get(DATA_HEALTH_ID);
    }

    @Override
    public final int getArrowCount() {
        return this.entityData.get(DATA_ARROW_COUNT_ID);
    }

    @Override
    public final void setArrowCount(int param0) {
        this.entityData.set(DATA_ARROW_COUNT_ID, param0);
    }

    @Override
    public final int getStingerCount() {
        return this.entityData.get(DATA_STINGER_COUNT_ID);
    }

    @Override
    public final void setStingerCount(int param0) {
        this.entityData.set(DATA_STINGER_COUNT_ID, param0);
    }

    @Override
    public boolean isAutoSpinAttack() {
        return (this.entityData.get(DATA_LIVING_ENTITY_FLAGS) & 4) != 0;
    }

    @Override
    public boolean isUsingItem() {
        return (this.entityData.get(DATA_LIVING_ENTITY_FLAGS) & 1) > 0;
    }

    public EnumHand getUsedItemHand_NMS() {
        return (this.entityData.get(DATA_LIVING_ENTITY_FLAGS) & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }

    protected void setLivingEntityFlag(int param0, boolean param1) {
        int var3 = this.entityData.get(DATA_LIVING_ENTITY_FLAGS);
        if (param1) {
            var3 |= param0;
        } else {
            var3 &= ~param0;
        }

        this.entityData.set(DATA_LIVING_ENTITY_FLAGS, (byte) var3);
    }

    @Override
    public void stopUsingItem() {
        this.setLivingEntityFlag(1, false);
    }

    @Override
    public void startAutoSpinAttack(int param0) {
        this.setLivingEntityFlag(4, true);
    }

    public Optional<BlockPosition> getSleepingPos_NMS() {
        return this.entityData.get(SLEEPING_POS_ID);
    }

    public void setSleepingPos(BlockPosition param0) {
        this.entityData.set(SLEEPING_POS_ID, Optional.of(param0));
    }

    @Override
    public void clearSleepingPos() {
        this.entityData.set(SLEEPING_POS_ID, Optional.empty());
    }

    @Override
    public BInteractionHand getUsedItemHand() {
        return switch (this.getUsedItemHand_NMS()) {
            case MAIN_HAND -> BInteractionHand.MAIN_HAND;
            case OFF_HAND -> BInteractionHand.OFF_HAND;
        };
    }

    @Override
    public Optional<org.by1337.api.world.BlockPosition> getSleepingPos() {
        Optional<BlockPosition> opt = this.getSleepingPos_NMS();
        if (opt.isEmpty()) {
            return Optional.empty();
        } else {
            BlockPosition pos = opt.get();
            return Optional.of(new org.by1337.api.world.BlockPosition(pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    @Override
    public void setSleepingPos(org.by1337.api.world.BlockPosition param0) {
        this.setSleepingPos(new BlockPosition(param0.getX(), param0.getY(), param0.getZ()));
    }

    static {
        try {
            Field field = EntityLiving.class.getDeclaredField("ag");
            field.setAccessible(true);
            DATA_LIVING_ENTITY_FLAGS = (DataWatcherObject<Byte>) field.get(null);

            field = EntityLiving.class.getDeclaredField("HEALTH");
            field.setAccessible(true);
            DATA_HEALTH_ID = (DataWatcherObject<Float>) field.get(null);

            field = EntityLiving.class.getDeclaredField("f");
            field.setAccessible(true);
            DATA_EFFECT_COLOR_ID = (DataWatcherObject<Integer>) field.get(null);

            field = EntityLiving.class.getDeclaredField("g");
            field.setAccessible(true);
            DATA_EFFECT_AMBIENCE_ID = (DataWatcherObject<Boolean>) field.get(null);

            field = EntityLiving.class.getDeclaredField("ARROWS_IN_BODY");
            field.setAccessible(true);
            DATA_ARROW_COUNT_ID = (DataWatcherObject<Integer>) field.get(null);

            field = EntityLiving.class.getDeclaredField("bi");
            field.setAccessible(true);
            DATA_STINGER_COUNT_ID = (DataWatcherObject<Integer>) field.get(null);

            field = EntityLiving.class.getDeclaredField("bj");
            field.setAccessible(true);
            SLEEPING_POS_ID = (DataWatcherObject<Optional<BlockPosition>>) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException var1) {
            throw new RuntimeException(var1);
        }


//        DATA_LIVING_ENTITY_FLAGS = new DataWatcherObject<>(7, DataWatcherRegistry.a);
//        DATA_HEALTH_ID = new DataWatcherObject<>(8, DataWatcherRegistry.c);
//        DATA_EFFECT_COLOR_ID = new DataWatcherObject<>(9, DataWatcherRegistry.b);
//        DATA_EFFECT_AMBIENCE_ID = new DataWatcherObject<>(10, DataWatcherRegistry.i);
//        DATA_ARROW_COUNT_ID = new DataWatcherObject<>(11, DataWatcherRegistry.b);
//        DATA_STINGER_COUNT_ID = new DataWatcherObject<>(12, DataWatcherRegistry.b);
//        SLEEPING_POS_ID = new DataWatcherObject<>(13, DataWatcherRegistry.m);


/*        System.out.println(PacketLivingEntityImp165.class.getName() + " ============== ");

        System.out.println("DATA_LIVING_ENTITY_FLAGS = " + DATA_LIVING_ENTITY_FLAGS.a());
        System.out.println("DATA_HEALTH_ID = " + DATA_HEALTH_ID.a());
        System.out.println("DATA_EFFECT_COLOR_ID = " + DATA_EFFECT_COLOR_ID.a());
        System.out.println("DATA_EFFECT_AMBIENCE_ID = " + DATA_EFFECT_AMBIENCE_ID.a());
        System.out.println("DATA_ARROW_COUNT_ID = " + DATA_ARROW_COUNT_ID.a());
        System.out.println("DATA_STINGER_COUNT_ID = " + DATA_STINGER_COUNT_ID.a());
        System.out.println("SLEEPING_POS_ID = " + SLEEPING_POS_ID.a());*/

    }
}
