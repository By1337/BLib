package org.by1337.blib.nms.v1_16_5.world.entity;

import net.minecraft.core.Rotations;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.by1337.blib.world.BLocation;
import org.by1337.blib.world.entity.PacketArmorStand;

public class PacketArmorStandImp165 extends PacketLivingEntityImp165 implements PacketArmorStand {
    private static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(-10.0F, 0.0F, -10.0F);
    private static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(-15.0F, 0.0F, 10.0F);
    private static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(-1.0F, 0.0F, -1.0F);
    private static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(1.0F, 0.0F, 1.0F);
    public static final EntityDataAccessor<Byte> DATA_CLIENT_FLAGS = ArmorStand.DATA_CLIENT_FLAGS_;
    public static final EntityDataAccessor<Rotations> DATA_HEAD_POSE = ArmorStand.DATA_HEAD_POSE_;
    public static final EntityDataAccessor<Rotations> DATA_BODY_POSE = ArmorStand.DATA_BODY_POSE_;
    public static final EntityDataAccessor<Rotations> DATA_LEFT_ARM_POSE = ArmorStand.DATA_LEFT_ARM_POSE_;
    public static final EntityDataAccessor<Rotations> DATA_RIGHT_ARM_POSE = ArmorStand.DATA_RIGHT_ARM_POSE_;
    public static final EntityDataAccessor<Rotations> DATA_LEFT_LEG_POSE = ArmorStand.DATA_LEFT_LEG_POSE_;
    public static final EntityDataAccessor<Rotations> DATA_RIGHT_LEG_POSE = ArmorStand.DATA_RIGHT_LEG_POSE_;

    public PacketArmorStandImp165(BLocation location) {
        super(EntityType.ARMOR_STAND, location);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.register(DATA_CLIENT_FLAGS, (byte)0);
        this.entityData.register(DATA_HEAD_POSE, DEFAULT_HEAD_POSE);
        this.entityData.register(DATA_BODY_POSE, DEFAULT_BODY_POSE);
        this.entityData.register(DATA_LEFT_ARM_POSE, DEFAULT_LEFT_ARM_POSE);
        this.entityData.register(DATA_RIGHT_ARM_POSE, DEFAULT_RIGHT_ARM_POSE);
        this.entityData.register(DATA_LEFT_LEG_POSE, DEFAULT_LEFT_LEG_POSE);
        this.entityData.register(DATA_RIGHT_LEG_POSE, DEFAULT_RIGHT_LEG_POSE);
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
            param0 = (byte)(param0 | param1);
        } else {
            param0 = (byte)(param0 & ~param1);
        }

        return param0;
    }

    public void setHeadPose(float x, float y, float z) {
        this.setHeadPose(new Rotations(x, y, z));
    }

    public void setBodyPose(float x, float y, float z) {
        this.setBodyPose(new Rotations(x, y, z));
    }

    public void setLeftArmPose(float x, float y, float z) {
        this.setLeftArmPose(new Rotations(x, y, z));
    }

    public void setRightArmPose(float x, float y, float z) {
        this.setRightArmPose(new Rotations(x, y, z));
    }

    public void setLeftLegPose(float x, float y, float z) {
        this.setLeftLegPose(new Rotations(x, y, z));
    }

    public void setRightLegPose(float x, float y, float z) {
        this.setRightLegPose(new Rotations(x, y, z));
    }
}
