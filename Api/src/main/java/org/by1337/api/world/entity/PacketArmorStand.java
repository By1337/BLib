package org.by1337.api.world.entity;

public interface PacketArmorStand extends PacketLivingEntity {
    void setSmall(boolean small);
    boolean isSmall();
    void setShowArms(boolean showArms);
    boolean isShowArms();
    void setNoBasePlate(boolean noBasePlate);
    boolean isNoBasePlate();
    void setMarker(boolean marker);
    boolean isMarker();
    void setHeadPose(float x, float y, float z);
    void setBodyPose(float x, float y, float z);
    void setLeftArmPose(float x, float y, float z);
    void setRightArmPose(float x, float y, float z);
    void setLeftLegPose(float x, float y, float z);
    void setRightLegPose(float x, float y, float z);

}
