package org.by1337.api.network.clientbound.entity;

import org.bukkit.inventory.ItemStack;
import org.by1337.api.BLib;
import org.by1337.api.world.entity.BEquipmentSlot;
import org.by1337.api.world.entity.PacketEntity;

import java.util.Map;

public interface PacketSetEquipment {

    static PacketSetEquipment newInstance(int entityId, Map<BEquipmentSlot, ItemStack> slots) {
        return BLib.getPacketFactory().createPacketSetEquipment(entityId, slots);
    }

    int getEntityId();

    void setEntityId(int entityId);

    Map<BEquipmentSlot, ItemStack> getSlots();

    void setSlots(Map<BEquipmentSlot, ItemStack> slots);

}
