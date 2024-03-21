package org.by1337.blib.nms.v1_19_4.inventory;


import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;

import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.by1337.blib.inventory.FakeTitle;

import java.util.ArrayList;
import java.util.List;

public class FakeTitleV1_19_4 implements FakeTitle {
    @Override
    @Deprecated(since = "1.0.7")
    public void send(Inventory inventory, String newTitle) {
        send(inventory, CraftChatMessage.fromStringOrNull(newTitle));
    }

    public void send(Inventory inventory, Component newTitle) {
        List<HumanEntity> list = new ArrayList<>(inventory.getViewers());
        for (HumanEntity humanEntity : list) {
            if (humanEntity instanceof CraftPlayer craftPlayer) {
                ServerPlayer serverPlayer = craftPlayer.getHandle();
                ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(serverPlayer.containerMenu.containerId, serverPlayer.containerMenu.getType(), newTitle);
                serverPlayer.connection.send(packet);
                craftPlayer.updateInventory();
            }
        }
    }

    @Override
    public void send(Inventory inventory, net.kyori.adventure.text.Component newTitle) {
        send(inventory, Component.Serializer.fromJson(GsonComponentSerializer.gson().serialize(newTitle)));
    }
}
