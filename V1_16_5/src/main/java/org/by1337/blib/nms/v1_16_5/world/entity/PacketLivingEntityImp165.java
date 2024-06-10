package org.by1337.blib.nms.v1_16_5.world.entity;

import java.lang.reflect.Field;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.by1337.blib.world.BLocation;
import org.by1337.blib.world.BlockPosition;
import org.by1337.blib.world.entity.BInteractionHand;
import org.by1337.blib.world.entity.PacketLivingEntity;

public abstract class PacketLivingEntityImp165 extends PacketEntityImpl165 implements PacketLivingEntity {
    protected static final EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS;
    private static final EntityDataAccessor<Float> DATA_HEALTH_ID;
    private static final EntityDataAccessor<Integer> DATA_EFFECT_COLOR_ID;
    private static final EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID;
    private static final EntityDataAccessor<Integer> DATA_ARROW_COUNT_ID;
    private static final EntityDataAccessor<Integer> DATA_STINGER_COUNT_ID;
    private static final EntityDataAccessor<Optional<BlockPos>> SLEEPING_POS_ID;

    public PacketLivingEntityImp165(EntityType<?> param0, BLocation location) {
        super(param0, location);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.register(DATA_LIVING_ENTITY_FLAGS, (byte)0);
        this.entityData.register(DATA_EFFECT_COLOR_ID, 0);
        this.entityData.register(DATA_EFFECT_AMBIENCE_ID, false);
        this.entityData.register(DATA_ARROW_COUNT_ID, 0);
        this.entityData.register(DATA_STINGER_COUNT_ID, 0);
        this.entityData.register(DATA_HEALTH_ID, 1.0F);
        this.entityData.register(SLEEPING_POS_ID, Optional.empty());
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

        this.entityData.set(DATA_LIVING_ENTITY_FLAGS, (byte)var3);
    }

    public void stopUsingItem() {
        this.setLivingEntityFlag(1, false);
    }

    public void startAutoSpinAttack(int param0) {
        this.setLivingEntityFlag(4, true);
    }

    public Optional<BlockPos> getSleepingPos_NMS() {
        return (Optional<BlockPos>)this.entityData.get(SLEEPING_POS_ID);
    }

    public void setSleepingPos(BlockPos param0) {
        this.entityData.set(SLEEPING_POS_ID, Optional.of(param0));
    }

    public void clearSleepingPos() {
        this.entityData.set(SLEEPING_POS_ID, Optional.empty());
    }

    public BInteractionHand getUsedItemHand() {
        return switch(this.getUsedItemHand_NMS()) {
            case MAIN_HAND -> BInteractionHand.MAIN_HAND;
            case OFF_HAND -> BInteractionHand.OFF_HAND;
            default -> throw new IncompatibleClassChangeError();
        };
    }

    public Optional<BlockPosition> getSleepingPos() {
        Optional<BlockPos> opt = this.getSleepingPos_NMS();
        if (opt.isEmpty()) {
            return Optional.empty();
        } else {
            BlockPos pos = (BlockPos)opt.get();
            return Optional.of(new BlockPosition(pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    public void setSleepingPos(BlockPosition param0) {
        this.setSleepingPos(new BlockPos(param0.getX(), param0.getY(), param0.getZ()));
    }

    static {
        try {
            Field field = LivingEntity.class.getDeclaredField("ag");
            field.setAccessible(true);
            DATA_LIVING_ENTITY_FLAGS = (EntityDataAccessor)field.get(null);
            field = LivingEntity.class.getDeclaredField("HEALTH");
            field.setAccessible(true);
            DATA_HEALTH_ID = (EntityDataAccessor)field.get(null);
            field = LivingEntity.class.getDeclaredField("f");
            field.setAccessible(true);
            DATA_EFFECT_COLOR_ID = (EntityDataAccessor)field.get(null);
            field = LivingEntity.class.getDeclaredField("g");
            field.setAccessible(true);
            DATA_EFFECT_AMBIENCE_ID = (EntityDataAccessor)field.get(null);
            field = LivingEntity.class.getDeclaredField("ARROWS_IN_BODY");
            field.setAccessible(true);
            DATA_ARROW_COUNT_ID = (EntityDataAccessor)field.get(null);
            field = LivingEntity.class.getDeclaredField("bi");
            field.setAccessible(true);
            DATA_STINGER_COUNT_ID = (EntityDataAccessor)field.get(null);
            field = LivingEntity.class.getDeclaredField("bj");
            field.setAccessible(true);
            SLEEPING_POS_ID = (EntityDataAccessor)field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException var11) {
            throw new RuntimeException(var11);
        }
    }
}
