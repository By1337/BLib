package org.by1337.blib.nms.v1_16_5.inventory;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.by1337.blib.inventory.InventoryUtil;
import org.by1337.blib.util.invoke.LambdaMetafactoryUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class InventoryUtilV1_16_5 implements InventoryUtil {
    private static final BiConsumer<ServerPlayer, Integer> CONTAINER_UPDATE_DELAY;

    @Override
    public void sendFakeTitle(Inventory inventory, Component newTitle) {
        for (HumanEntity humanEntity : new ArrayList<>(inventory.getViewers())) {
            if (humanEntity instanceof CraftPlayer craftPlayer) {
                ServerPlayer entityPlayer = craftPlayer.getHandle();
                ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(
                        entityPlayer.activeContainer.windowId, entityPlayer.activeContainer.getType(), PaperAdventure.asVanilla(newTitle)
                );
                entityPlayer.playerConnection.sendPacket(packet);
                entityPlayer.updateInventory(entityPlayer.activeContainer);
            }
        }
    }

    @Override
    public void flushInv(Player player) {
        ((CraftPlayer) player).getHandle().activeContainer.broadcastChanges();
    }

    @Override
    public void disableAutoFlush(Player player) {
        CONTAINER_UPDATE_DELAY.accept(((CraftPlayer) player).getHandle(), Integer.MAX_VALUE);
    }

    @Override
    public void enableAutoFlush(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        CONTAINER_UPDATE_DELAY.accept(craftPlayer.getHandle(), craftPlayer.getHandle().world.paperConfig.containerUpdateTickRate);
    }

    static {
        try {
            Field field = ServerPlayer.class.getDeclaredField("containerUpdateDelay");
            field.setAccessible(true);
            CONTAINER_UPDATE_DELAY = LambdaMetafactoryUtil.setterOf(field);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
