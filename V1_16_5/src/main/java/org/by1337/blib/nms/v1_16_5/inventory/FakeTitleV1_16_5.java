package org.by1337.blib.nms.v1_16_5.inventory;

import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityPlayer;
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
    public void send(Inventory inventory, String newTitle) {
        List<HumanEntity> list = new ArrayList<>(inventory.getViewers());
        for (HumanEntity humanEntity : list) {
            if (humanEntity instanceof CraftPlayer craftPlayer) {
                EntityPlayer entityPlayer = craftPlayer.getHandle();
                PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(entityPlayer.activeContainer.windowId, entityPlayer.activeContainer.getType(), CraftChatMessage.fromStringOrNull(newTitle));
                entityPlayer.playerConnection.sendPacket(packet);
                entityPlayer.updateInventory(entityPlayer.activeContainer);
            }
        }
    }
}
