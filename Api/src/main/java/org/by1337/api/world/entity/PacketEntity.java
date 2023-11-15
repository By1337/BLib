package org.by1337.api.world.entity;

import org.by1337.api.world.BLocation;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PacketEntity {
    UUID uuid();
    BLocation getLocation();
    int getId();
    float getXa();
    float getYa();
    float getZa();
    void setPose(BPose pose);
    BPose getPose();
    boolean isSilent();
    void setSilent(boolean silent);
    boolean isNoGravity();
    void setNoGravity(boolean noGravity);
    int getAirSupply();
    void setAirSupply(int airSupply);
    void setCustomName(@Nullable String customName);
    @Nullable
    String getCustomName();
    boolean hasCustomName();
    void setCustomNameVisible(boolean customNameVisible);
    boolean isCustomNameVisible();
    void setShiftKeyDown(boolean shiftKeyDown);
    void setSprinting(boolean sprinting);
    void setSwimming(boolean swimming);
    void setGlowing(boolean glowing);
    void setInvisible(boolean invisible);
    <T> T getType();
    <T> T getEntityData();
    void setLocation(BLocation location);

}
