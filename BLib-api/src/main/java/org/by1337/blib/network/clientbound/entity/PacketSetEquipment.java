package org.by1337.blib.network.clientbound.entity;

import org.bukkit.inventory.ItemStack;
import org.by1337.blib.BLib;
import org.by1337.blib.network.Packet;
import org.by1337.blib.world.entity.BEquipmentSlot;

import java.util.Map;
@Deprecated(forRemoval = true)
public interface PacketSetEquipment extends Packet {

    static PacketSetEquipment newInstance(int entityId, Map<BEquipmentSlot, ItemStack> slots) {
        return BLib.getPacketFactory().createPacketSetEquipment(entityId, slots);
    }

    int getEntityId();

    void setEntityId(int entityId);

    Map<BEquipmentSlot, ItemStack> getSlots();

    void setSlots(Map<BEquipmentSlot, ItemStack> slots);

}
