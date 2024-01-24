package org.by1337.blib.nms.v1_16_5.network.factory;

import org.bukkit.inventory.ItemStack;
import org.by1337.blib.factory.PacketFactory;
import org.by1337.blib.network.clientbound.entity.*;
import org.by1337.blib.world.entity.BEquipmentSlot;
import org.by1337.blib.world.entity.PacketEntity;
import org.by1337.blib.nms.v1_16_5.network.clientbound.*;


import java.util.Map;

public class PacketFactoryImpl165 implements PacketFactory {
    @Override
    public PacketAddEntity createPacketAddEntity(PacketEntity packetEntity) {
        return new PacketAddEntityImpl165(packetEntity);
    }

    @Override
    public PacketRemoveEntity createPacketRemoveEntity(PacketEntity entity) {
        return new PacketRemoveEntityImpl165(entity);
    }

    @Override
    public PacketRemoveEntity createPacketRemoveEntity(int... ids) {
        return new PacketRemoveEntityImpl165(ids);
    }

    @Override
    public PacketSetEntityData createPacketSetEntityData(PacketEntity packetEntity) {
        return new PacketSetEntityDataImpl165(packetEntity);
    }

    @Override
    public TeleportEntityPacket createTeleportEntityPacket(PacketEntity entity) {
        return new TeleportEntityPacketImp165(entity);
    }
    @Override
    public TeleportEntityPacket createTeleportEntityPacket(int id, double x, double y, double z, float pitch, float yaw, boolean onGround) {
        return new TeleportEntityPacketImp165(id, x, y, z, pitch, yaw, onGround);
    }

    @Override
    public PacketSetEquipment createPacketSetEquipment(int entityId, Map<BEquipmentSlot, ItemStack> slots) {
        return new PacketSetEquipmentImpl165(entityId, slots);
    }
}
