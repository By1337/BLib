package org.by1337.blib.nms.v1_16_5.inventory;

import java.util.ArrayList;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.by1337.blib.inventory.FakeTitle;

public class FakeTitleV1_16_5 implements FakeTitle {
    @Deprecated(
            since = "1.0.7"
    )
    public void send(Inventory inventory, String newTitle) {
        this.send(inventory, CraftChatMessage.fromStringOrNull(newTitle));
    }

    public void send(Inventory inventory, Component newTitle) {
        for(HumanEntity humanEntity : new ArrayList<>(inventory.getViewers())) {
            if (humanEntity instanceof CraftPlayer craftPlayer) {
                ServerPlayer entityPlayer = craftPlayer.getHandle();
                ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(
                        entityPlayer.activeContainer.windowId, entityPlayer.activeContainer.getType(), newTitle
                );
                entityPlayer.playerConnection.sendPacket(packet);
                entityPlayer.updateInventory(entityPlayer.activeContainer);
            }
        }
    }

    public void send(Inventory inventory, net.kyori.adventure.text.Component newTitle) {
        this.send(inventory, PaperAdventure.asVanilla(newTitle));
    }
}
