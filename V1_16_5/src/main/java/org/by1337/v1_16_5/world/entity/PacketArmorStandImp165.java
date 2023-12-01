package org.by1337.v1_16_5.world.entity;

import net.minecraft.server.v1_16_R3.*;
import org.by1337.api.world.BLocation;
import org.by1337.api.world.entity.PacketArmorStand;

public class PacketArmorStandImp165 extends PacketLivingEntityImp165 implements PacketArmorStand {

    private static final Vector3f DEFAULT_HEAD_POSE = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f DEFAULT_BODY_POSE = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f DEFAULT_LEFT_ARM_POSE = new Vector3f(-10.0F, 0.0F, -10.0F);
    private static final Vector3f DEFAULT_RIGHT_ARM_POSE = new Vector3f(-15.0F, 0.0F, 10.0F);
    private static final Vector3f DEFAULT_LEFT_LEG_POSE = new Vector3f(-1.0F, 0.0F, -1.0F);
    private static final Vector3f DEFAULT_RIGHT_LEG_POSE = new Vector3f(1.0F, 0.0F, 1.0F);
    public static final DataWatcherObject<Byte> DATA_CLIENT_FLAGS;
    public static final DataWatcherObject<Vector3f> DATA_HEAD_POSE;
    public static final DataWatcherObject<Vector3f> DATA_BODY_POSE;
    public static final DataWatcherObject<Vector3f> DATA_LEFT_ARM_POSE;
    public static final DataWatcherObject<Vector3f> DATA_RIGHT_ARM_POSE;
    public static final DataWatcherObject<Vector3f> DATA_LEFT_LEG_POSE;
    public static final DataWatcherObject<Vector3f> DATA_RIGHT_LEG_POSE;
    public PacketArmorStandImp165(BLocation location) {
        super(EntityTypes.ARMOR_STAND, location);
    }

    static {
        DATA_CLIENT_FLAGS = EntityArmorStand.b;
        DATA_HEAD_POSE = EntityArmorStand.c;
        DATA_BODY_POSE = EntityArmorStand.d;
        DATA_LEFT_ARM_POSE = EntityArmorStand.e;
        DATA_RIGHT_ARM_POSE = EntityArmorStand.f;
        DATA_LEFT_LEG_POSE = EntityArmorStand.g;
        DATA_RIGHT_LEG_POSE = EntityArmorStand.bh;



//        DATA_CLIENT_FLAGS = new DataWatcherObject<>(14, DataWatcherRegistry.a);
//        DATA_HEAD_POSE = new DataWatcherObject<>(15, DataWatcherRegistry.k);
//        DATA_BODY_POSE = new DataWatcherObject<>(16, DataWatcherRegistry.k);
//        DATA_LEFT_ARM_POSE = new DataWatcherObject<>(17, DataWatcherRegistry.k);
//        DATA_RIGHT_ARM_POSE = new DataWatcherObject<>(18, DataWatcherRegistry.k);
//        DATA_LEFT_LEG_POSE = new DataWatcherObject<>(19, DataWatcherRegistry.k);
//        DATA_RIGHT_LEG_POSE = new DataWatcherObject<>(20, DataWatcherRegistry.k);

        System.out.println(PacketArmorStandImp165.class.getName() + " ============== ");

        System.out.println("DATA_CLIENT_FLAGS = " + DATA_CLIENT_FLAGS.a());
        System.out.println("DATA_HEAD_POSE = " + DATA_HEAD_POSE.a());
        System.out.println("DATA_BODY_POSE = " + DATA_BODY_POSE.a());
        System.out.println("DATA_LEFT_ARM_POSE = " + DATA_LEFT_ARM_POSE.a());
        System.out.println("DATA_RIGHT_ARM_POSE = " + DATA_RIGHT_ARM_POSE.a());
        System.out.println("DATA_LEFT_LEG_POSE = " + DATA_LEFT_LEG_POSE.a());
        System.out.println("DATA_RIGHT_LEG_POSE = " + DATA_RIGHT_LEG_POSE.a());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.register(DATA_CLIENT_FLAGS, (byte) 0);
        this.entityData.register(DATA_HEAD_POSE, DEFAULT_HEAD_POSE);
        this.entityData.register(DATA_BODY_POSE, DEFAULT_BODY_POSE);
        this.entityData.register(DATA_LEFT_ARM_POSE, DEFAULT_LEFT_ARM_POSE);
        this.entityData.register(DATA_RIGHT_ARM_POSE, DEFAULT_RIGHT_ARM_POSE);
        this.entityData.register(DATA_LEFT_LEG_POSE, DEFAULT_LEFT_LEG_POSE);
        this.entityData.register(DATA_RIGHT_LEG_POSE, DEFAULT_RIGHT_LEG_POSE);
    }

    @Override
    public void setSmall(boolean small) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 1, small));
    }

    @Override
    public boolean isSmall() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 1) != 0;
    }

    @Override
    public void setShowArms(boolean showArms) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 4, showArms));
    }

    @Override
    public boolean isShowArms() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 4) != 0;
    }

    @Override
    public void setNoBasePlate(boolean noBasePlate) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 8, noBasePlate));
    }

    @Override
    public boolean isNoBasePlate() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 8) != 0;
    }

    @Override
    public void setMarker(boolean marker) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 16, marker));
    }

    @Override
    public boolean isMarker() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 16) != 0;
    }

    public void setHeadPose(Vector3f param0) {
        this.entityData.set(DATA_HEAD_POSE, param0);
    }

    public void setBodyPose(Vector3f param0) {
        this.entityData.set(DATA_BODY_POSE, param0);
    }

    public void setLeftArmPose(Vector3f param0) {
        this.entityData.set(DATA_LEFT_ARM_POSE, param0);
    }

    public void setRightArmPose(Vector3f param0) {
        this.entityData.set(DATA_RIGHT_ARM_POSE, param0);
    }

    public void setLeftLegPose(Vector3f param0) {
        this.entityData.set(DATA_LEFT_LEG_POSE, param0);
    }

    public void setRightLegPose(Vector3f param0) {
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
        this.setHeadPose(new Vector3f(x, y, z));
    }

    @Override
    public void setBodyPose(float x, float y, float z) {
        this.setBodyPose(new Vector3f(x, y, z));
    }

    @Override
    public void setLeftArmPose(float x, float y, float z) {
        this.setLeftArmPose(new Vector3f(x, y, z));
    }

    @Override
    public void setRightArmPose(float x, float y, float z) {
        this.setRightArmPose(new Vector3f(x, y, z));
    }

    @Override
    public void setLeftLegPose(float x, float y, float z) {
        this.setLeftLegPose(new Vector3f(x, y, z));
    }

    @Override
    public void setRightLegPose(float x, float y, float z) {
        this.setRightLegPose(new Vector3f(x, y, z));
    }
}
