package org.by1337.blib.nms.v1_16_5.network.clientbound;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.network.clientbound.entity.PacketSetEquipment;
import org.by1337.blib.nms.v1_16_5.network.PacketImpl165;
import org.by1337.blib.world.entity.BEquipmentSlot;

public class PacketSetEquipmentImpl165 extends PacketImpl165 implements PacketSetEquipment {
    private int entityId;
    private Map<BEquipmentSlot, ItemStack> slots;

    public PacketSetEquipmentImpl165(int entityId, Map<BEquipmentSlot, ItemStack> slots) {
        this.entityId = entityId;
        this.slots = slots;
    }

    @Override
    protected Packet<?> create() {
        List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList();

        for(Entry<BEquipmentSlot, ItemStack> entry : this.slots.entrySet()) {
            list.add(new Pair<>(switch(entry.getKey()) {
                case MAINHAND -> EquipmentSlot.MAINHAND;
                case OFFHAND -> EquipmentSlot.OFFHAND;
                case FEET -> EquipmentSlot.FEET;
                case LEGS -> EquipmentSlot.LEGS;
                case CHEST -> EquipmentSlot.CHEST;
                case HEAD -> EquipmentSlot.HEAD;
                default -> throw new IncompatibleClassChangeError();
            }, CraftItemStack.asNMSCopy(entry.getValue())));
        }

        return new ClientboundSetEquipmentPacket(this.entityId, list);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public Map<BEquipmentSlot, ItemStack> getSlots() {
        return this.slots;
    }

    public void setSlots(Map<BEquipmentSlot, ItemStack> slots) {
        this.slots = slots;
    }
}
