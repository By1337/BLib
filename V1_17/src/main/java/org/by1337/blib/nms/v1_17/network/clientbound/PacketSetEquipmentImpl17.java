package org.by1337.blib.nms.v1_17.network.clientbound;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.network.clientbound.entity.PacketSetEquipment;
import org.by1337.blib.world.entity.BEquipmentSlot;
import org.by1337.blib.nms.v1_17.network.PacketImpl17;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Map;

public class PacketSetEquipmentImpl17 extends PacketImpl17 implements PacketSetEquipment {

    private int entityId;
    private Map<BEquipmentSlot, ItemStack> slots;

    public PacketSetEquipmentImpl17(int entityId, Map<BEquipmentSlot, ItemStack> slots) {
        this.entityId = entityId;
        this.slots = slots;
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public Map<BEquipmentSlot, ItemStack> getSlots() {
        return slots;
    }

    @Override
    public void setSlots(Map<BEquipmentSlot, ItemStack> slots) {
        this.slots = slots;
    }

    @Override
    protected Packet<?> create() {
        List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList<>();

        for (Map.Entry<BEquipmentSlot, ItemStack> entry : slots.entrySet()) {
            list.add(new Pair<>(
                            switch (entry.getKey()) {
                                case MAINHAND -> EquipmentSlot.MAINHAND;
                                case OFFHAND -> EquipmentSlot.OFFHAND;
                                case FEET -> EquipmentSlot.FEET;
                                case LEGS -> EquipmentSlot.LEGS;
                                case CHEST -> EquipmentSlot.CHEST;
                                case HEAD -> EquipmentSlot.HEAD;
                            },
                            CraftItemStack.asNMSCopy(entry.getValue())
                    )
            );
        }
        return new ClientboundSetEquipmentPacket(entityId, list);
    }
}
