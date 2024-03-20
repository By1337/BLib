package org.by1337.blib.nms.v1_16_5.inventory;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.by1337.blib.inventory.FakeTitle;

import java.util.ArrayList;
import java.util.List;

public class FakeTitleV1_16_5 implements FakeTitle {
    @Override
    @Deprecated(since = "1.0.7")
    public void send(Inventory inventory, String newTitle) {
        send(inventory, CraftChatMessage.fromStringOrNull(newTitle));
    }

    public void send(Inventory inventory, IChatBaseComponent newTitle) {
        List<HumanEntity> list = new ArrayList<>(inventory.getViewers());
        for (HumanEntity humanEntity : list) {
            if (humanEntity instanceof CraftPlayer craftPlayer) {
                EntityPlayer entityPlayer = craftPlayer.getHandle();
                PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId,
                        entityPlayer.activeContainer.getType(),
                        newTitle);
                entityPlayer.playerConnection.sendPacket(packet);
                entityPlayer.updateInventory(entityPlayer.activeContainer);
            }
        }
    }

    @Override
    public void send(Inventory inventory, net.kyori.adventure.text.Component newTitle) {
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a(GsonComponentSerializer.gson().serialize(newTitle));
        send(inventory, iChatBaseComponent);
    }
}
