package org.by1337.blib.nms.v1_16_5.network.clientbound;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.network.clientbound.entity.PacketSetEquipment;
import org.by1337.blib.world.entity.BEquipmentSlot;
import org.by1337.blib.nms.v1_16_5.network.PacketImpl165;

import java.util.ArrayList;
import java.util.Map;

public class PacketSetEquipmentImpl165 extends PacketImpl165 implements PacketSetEquipment {
    private int entityId;
    private Map<BEquipmentSlot, ItemStack> slots;

    public PacketSetEquipmentImpl165(int entityId, Map<BEquipmentSlot, ItemStack> slots) {
        this.entityId = entityId;
        this.slots = slots;
    }

    @Override
    protected Packet<?> create() {
        java.util.List<com.mojang.datafixers.util.Pair<net.minecraft.server.v1_16_R3.EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> list = new ArrayList<>();
        for (Map.Entry<BEquipmentSlot, ItemStack> entry : slots.entrySet()) {
            list.add(new Pair<>(
                            switch (entry.getKey()) {
                                case MAINHAND -> EnumItemSlot.MAINHAND;
                                case OFFHAND -> EnumItemSlot.OFFHAND;
                                case FEET -> EnumItemSlot.FEET;
                                case LEGS -> EnumItemSlot.LEGS;
                                case CHEST -> EnumItemSlot.CHEST;
                                case HEAD -> EnumItemSlot.HEAD;
                            },
                            CraftItemStack.asNMSCopy(entry.getValue())
                    )
            );
        }
        return new PacketPlayOutEntityEquipment(entityId, list);
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public Map<BEquipmentSlot, ItemStack> getSlots() {
        return slots;
    }

    public void setSlots(Map<BEquipmentSlot, ItemStack> slots) {
        this.slots = slots;
    }
}
