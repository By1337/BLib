package org.by1337.api.factory;

import org.bukkit.inventory.ItemStack;
import org.by1337.api.network.clientbound.entity.*;
import org.by1337.api.world.entity.BEquipmentSlot;
import org.by1337.api.world.entity.PacketEntity;

import java.util.Map;

public interface PacketFactory {
    PacketAddEntity createPacketAddEntity(PacketEntity packetEntity);

    PacketRemoveEntity createPacketRemoveEntity(PacketEntity entity);

    PacketRemoveEntity createPacketRemoveEntity(int... ids);

    PacketSetEntityData createPacketSetEntityData(PacketEntity packetEntity);

    TeleportEntityPacket createTeleportEntityPacket(PacketEntity entity);

    TeleportEntityPacket createTeleportEntityPacket(int id, double x, double y, double z, float pitch, float yaw, boolean onGround);

    PacketSetEquipment createPacketSetEquipment(int entityId, Map<BEquipmentSlot, ItemStack> slots);
}
