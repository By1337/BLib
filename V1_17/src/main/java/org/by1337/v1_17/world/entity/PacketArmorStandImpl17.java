package org.by1337.v1_17.world.entity;

import net.minecraft.core.Rotations;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.by1337.api.world.BLocation;
import org.by1337.api.world.entity.PacketArmorStand;

public class PacketArmorStandImpl17 extends PacketLivingEntityImpl17 implements PacketArmorStand {
    private static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(-10.0F, 0.0F, -10.0F);
    private static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(-15.0F, 0.0F, 10.0F);
    private static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(-1.0F, 0.0F, -1.0F);
    private static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(1.0F, 0.0F, 1.0F);

    public static final EntityDataAccessor<Byte> DATA_CLIENT_FLAGS;
    public static final EntityDataAccessor<Rotations> DATA_HEAD_POSE;
    public static final EntityDataAccessor<Rotations> DATA_BODY_POSE;
    public static final EntityDataAccessor<Rotations> DATA_LEFT_ARM_POSE;
    public static final EntityDataAccessor<Rotations> DATA_RIGHT_ARM_POSE;
    public static final EntityDataAccessor<Rotations> DATA_LEFT_LEG_POSE;
    public static final EntityDataAccessor<Rotations> DATA_RIGHT_LEG_POSE;


    static {
        DATA_CLIENT_FLAGS = ArmorStand.DATA_CLIENT_FLAGS;
        DATA_HEAD_POSE = ArmorStand.DATA_HEAD_POSE;
        DATA_BODY_POSE = ArmorStand.DATA_BODY_POSE;
        DATA_LEFT_ARM_POSE = ArmorStand.DATA_LEFT_ARM_POSE;
        DATA_RIGHT_ARM_POSE = ArmorStand.DATA_RIGHT_ARM_POSE;
        DATA_LEFT_LEG_POSE = ArmorStand.DATA_LEFT_LEG_POSE;
        DATA_RIGHT_LEG_POSE = ArmorStand.DATA_RIGHT_LEG_POSE;
    }

    public PacketArmorStandImpl17(BLocation location) {
        super(EntityType.ARMOR_STAND, location);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CLIENT_FLAGS, (byte) 0);
        this.entityData.define(DATA_HEAD_POSE, DEFAULT_HEAD_POSE);
        this.entityData.define(DATA_BODY_POSE, DEFAULT_BODY_POSE);
        this.entityData.define(DATA_LEFT_ARM_POSE, DEFAULT_LEFT_ARM_POSE);
        this.entityData.define(DATA_RIGHT_ARM_POSE, DEFAULT_RIGHT_ARM_POSE);
        this.entityData.define(DATA_LEFT_LEG_POSE, DEFAULT_LEFT_LEG_POSE);
        this.entityData.define(DATA_RIGHT_LEG_POSE, DEFAULT_RIGHT_LEG_POSE);
    }

    public void setSmall(boolean small) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 1, small));
    }

    public boolean isSmall() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 1) != 0;
    }

    public void setShowArms(boolean showArms) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 4, showArms));
    }

    public boolean isShowArms() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 4) != 0;
    }

    public void setNoBasePlate(boolean noBasePlate) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 8, noBasePlate));
    }

    public boolean isNoBasePlate() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 8) != 0;
    }

    public void setMarker(boolean marker) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 16, marker));
    }

    public boolean isMarker() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 16) != 0;
    }

    public void setHeadPose(Rotations param0) {
        this.entityData.set(DATA_HEAD_POSE, param0);
    }

    public void setBodyPose(Rotations param0) {
        this.entityData.set(DATA_BODY_POSE, param0);
    }

    public void setLeftArmPose(Rotations param0) {
        this.entityData.set(DATA_LEFT_ARM_POSE, param0);
    }

    public void setRightArmPose(Rotations param0) {
        this.entityData.set(DATA_RIGHT_ARM_POSE, param0);
    }

    public void setLeftLegPose(Rotations param0) {
        this.entityData.set(DATA_LEFT_LEG_POSE, param0);
    }

    public void setRightLegPose(Rotations param0) {
        this.entityData.set(DATA_RIGHT_LEG_POSE, param0);
    }

    private byte setBit(byte param0, int param1, boolean param2) {
        if (param2) {
            param0 = (byte) (param0 | param1);
        } else {
            param0 = (byte) (param0 & ~param1);
        }

        return param0;
    }

    @Override
    public void setHeadPose(float x, float y, float z) {
        setHeadPose(new Rotations(x, y, z));
    }

    @Override
    public void setBodyPose(float x, float y, float z) {
        setBodyPose(new Rotations(x, y, z));
    }

    @Override
    public void setLeftArmPose(float x, float y, float z) {
        setLeftArmPose(new Rotations(x, y, z));
    }

    @Override
    public void setRightArmPose(float x, float y, float z) {
        setRightArmPose(new Rotations(x, y, z));
    }

    @Override
    public void setLeftLegPose(float x, float y, float z) {
        setLeftLegPose(new Rotations(x, y, z));
    }

    @Override
    public void setRightLegPose(float x, float y, float z) {
        setRightLegPose(new Rotations(x, y, z));
    }
}
