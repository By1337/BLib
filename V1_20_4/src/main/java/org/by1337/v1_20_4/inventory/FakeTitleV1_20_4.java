package org.by1337.v1_20_4.inventory;


import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;

import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.by1337.api.inventory.FakeTitle;

import java.util.ArrayList;
import java.util.List;

public class FakeTitleV1_20_4 implements FakeTitle {
    @Override
    public void send(Inventory inventory, String newTitle) {
        List<HumanEntity> list = new ArrayList<>(inventory.getViewers());
        for (HumanEntity humanEntity : list) {
            if (humanEntity instanceof CraftPlayer craftPlayer) {
                ServerPlayer serverPlayer = craftPlayer.getHandle();
                ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(serverPlayer.containerMenu.containerId, serverPlayer.containerMenu.getType(), CraftChatMessage.fromStringOrNull(newTitle));
                serverPlayer.connection.send(packet);
                craftPlayer.updateInventory();
              //  serverPlayer.connection.send(new ClientboundContainerSetContentPacket(serverPlayer.containerMenu.containerId, serverPlayer.containerMenu.getItems()));
            }
        }
    }
}
