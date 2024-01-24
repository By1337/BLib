package org.by1337.blib.world.entity;

import org.by1337.blib.world.BlockPosition;

import java.util.Optional;

public interface PacketLivingEntity extends PacketEntity{
    float getHealth();
    int getArrowCount();
    void setArrowCount(int param0);
    int getStingerCount();
    void setStingerCount(int param0);
    boolean isAutoSpinAttack();
    boolean isUsingItem();
    BInteractionHand getUsedItemHand();
    void stopUsingItem();
    void startAutoSpinAttack(int param0);
    Optional<BlockPosition> getSleepingPos();
    void setSleepingPos(BlockPosition param0);
    void clearSleepingPos();



}
