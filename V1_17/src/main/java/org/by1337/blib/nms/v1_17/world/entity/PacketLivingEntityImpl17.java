package org.by1337.blib.nms.v1_17.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.by1337.blib.world.BLocation;
import org.by1337.blib.world.BlockPosition;
import org.by1337.blib.world.entity.BInteractionHand;
import org.by1337.blib.world.entity.PacketLivingEntity;

import java.lang.reflect.Field;
import java.util.Optional;

public abstract class PacketLivingEntityImpl17 extends PacketEntityImpl17 implements PacketLivingEntity {

    protected static final EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS;
    private static final EntityDataAccessor<Float> DATA_HEALTH_ID;
    private static final EntityDataAccessor<Integer> DATA_EFFECT_COLOR_ID;
    private static final EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID;
    private static final EntityDataAccessor<Integer> DATA_ARROW_COUNT_ID;
    private static final EntityDataAccessor<Integer> DATA_STINGER_COUNT_ID;
    private static final EntityDataAccessor<Optional<BlockPos>> SLEEPING_POS_ID;

    public PacketLivingEntityImpl17(EntityType<?> param0, BLocation location) {
        super(param0, location);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_LIVING_ENTITY_FLAGS, (byte) 0);
        this.entityData.define(DATA_EFFECT_COLOR_ID, 0);
        this.entityData.define(DATA_EFFECT_AMBIENCE_ID, false);
        this.entityData.define(DATA_ARROW_COUNT_ID, 0);
        this.entityData.define(DATA_STINGER_COUNT_ID, 0);
        this.entityData.define(DATA_HEALTH_ID, 1.0F);
        this.entityData.define(SLEEPING_POS_ID, Optional.empty());
    }

    public float getHealth() {
        return this.entityData.get(DATA_HEALTH_ID);
    }

    public final int getArrowCount() {
        return this.entityData.get(DATA_ARROW_COUNT_ID);
    }

    public final void setArrowCount(int param0) {
        this.entityData.set(DATA_ARROW_COUNT_ID, param0);
    }

    public final int getStingerCount() {
        return this.entityData.get(DATA_STINGER_COUNT_ID);
    }

    public final void setStingerCount(int param0) {
        this.entityData.set(DATA_STINGER_COUNT_ID, param0);
    }

    public boolean isAutoSpinAttack() {
        return (this.entityData.get(DATA_LIVING_ENTITY_FLAGS) & 4) != 0;
    }

    public boolean isUsingItem() {
        return (this.entityData.get(DATA_LIVING_ENTITY_FLAGS) & 1) > 0;
    }

    public InteractionHand getUsedItemHand_NMS() {
        return (this.entityData.get(DATA_LIVING_ENTITY_FLAGS) & 2) > 0 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
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


    public void stopUsingItem() {
        this.setLivingEntityFlag(1, false);
    }

    public void startAutoSpinAttack(int param0) {
        this.setLivingEntityFlag(4, true);
    }

    public Optional<BlockPos> getSleepingPos_NMS() {
        return this.entityData.get(SLEEPING_POS_ID);
    }

    public void setSleepingPos(BlockPos param0) {
        this.entityData.set(SLEEPING_POS_ID, Optional.of(param0));
    }

    public void clearSleepingPos() {
        this.entityData.set(SLEEPING_POS_ID, Optional.empty());
    }

    @Override
    public BInteractionHand getUsedItemHand() {
        return switch (getUsedItemHand_NMS()) {
            case MAIN_HAND -> BInteractionHand.MAIN_HAND;
            case OFF_HAND -> BInteractionHand.OFF_HAND;
        };
    }

    @Override
    public Optional<BlockPosition> getSleepingPos() {
        Optional<BlockPos> opt = getSleepingPos_NMS();
        if (opt.isEmpty()) return Optional.empty();
        BlockPos pos = opt.get();
        return Optional.of(new BlockPosition(pos.getX(), pos.getY(), pos.getZ()));
    }

    @Override
    public void setSleepingPos(BlockPosition param0) {
        setSleepingPos(new BlockPos(param0.getX(), param0.getY(), param0.getZ()));
    }

    static {
        try {
            Field field = LivingEntity.class.getDeclaredField("aB");
            field.setAccessible(true);
            DATA_LIVING_ENTITY_FLAGS = (EntityDataAccessor<Byte>) field.get(null);

            field = LivingEntity.class.getDeclaredField("bJ");
            field.setAccessible(true);
            DATA_HEALTH_ID = (EntityDataAccessor<Float>) field.get(null);

            field = LivingEntity.class.getDeclaredField("bK");
            field.setAccessible(true);
            DATA_EFFECT_COLOR_ID = (EntityDataAccessor<Integer>) field.get(null);

            field = LivingEntity.class.getDeclaredField("bL");
            field.setAccessible(true);
            DATA_EFFECT_AMBIENCE_ID = (EntityDataAccessor<Boolean>) field.get(null);

            field = LivingEntity.class.getDeclaredField("bM");
            field.setAccessible(true);
            DATA_ARROW_COUNT_ID = (EntityDataAccessor<Integer>) field.get(null);

            field = LivingEntity.class.getDeclaredField("bN");
            field.setAccessible(true);
            DATA_STINGER_COUNT_ID = (EntityDataAccessor<Integer>) field.get(null);

            field = LivingEntity.class.getDeclaredField("bO");
            field.setAccessible(true);
            SLEEPING_POS_ID = (EntityDataAccessor<Optional<BlockPos>>) field.get(null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
