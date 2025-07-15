package org.by1337.blib.nms.v1_17_1.inventory;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.command.SummonCommand;
import org.by1337.blib.inventory.InventoryUtil;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.util.Version;
import org.by1337.blib.util.invoke.LambdaMetafactoryUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@NMSAccessor(forClazz = InventoryUtil.class, forVersions = Version.V1_17_1)
public class InventoryUtilV1_17_1 implements InventoryUtil {
    private static final BiConsumer<ServerPlayer, Integer> CONTAINER_UPDATE_DELAY;

    @Override
    public void sendFakeTitle(Inventory inventory, Component newTitle) {
        List<HumanEntity> list = new ArrayList<>(inventory.getViewers());
        for (HumanEntity humanEntity : list) {
            if (humanEntity instanceof CraftPlayer craftPlayer) {
                ServerPlayer serverPlayer = craftPlayer.getHandle();
                ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(
                        serverPlayer.containerMenu.containerId,
                        serverPlayer.containerMenu.getType(),
                        PaperAdventure.asVanilla(newTitle)
                );
                serverPlayer.connection.send(packet);
                craftPlayer.updateInventory();
            }
        }
    }

    @Override
    public void flushInv(Player player) {
        ((CraftPlayer) player).getHandle().containerMenu.broadcastChanges();
    }

    @Override
    public void disableAutoFlush(Player player) {
        CONTAINER_UPDATE_DELAY.accept(((CraftPlayer) player).getHandle(), Integer.MAX_VALUE);
    }

    @Override
    public void enableAutoFlush(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        CONTAINER_UPDATE_DELAY.accept(craftPlayer.getHandle(), craftPlayer.getHandle().level.paperConfig.containerUpdateTickRate);
    }

    @Override
    public void setItemStackWithoutCopy(Inventory to, ItemStack who, int index) {
        net.minecraft.world.item.ItemStack nms;
        if (who instanceof CraftItemStack craftItemStack) {
            nms = Objects.requireNonNullElse(craftItemStack.handle, net.minecraft.world.item.ItemStack.EMPTY);
        } else {
            nms = CraftItemStack.asNMSCopy(who);
        }
        if (to instanceof CraftResultInventory cri) {
            if (index < cri.getIngredientsInventory().getContainerSize()) {
                cri.getIngredientsInventory().setItem(index, nms);
            } else {
                cri.getResultInventory().setItem(index - cri.getIngredientsInventory().getContainerSize(), nms);
            }
        } else if (to instanceof CraftInventoryCrafting cic) {
            if (index < cic.getResultInventory().getContainerSize()) {
                cic.getResultInventory().setItem(index, nms);
            } else {
                cic.getMatrixInventory().setItem(index - cic.getResultInventory().getContainerSize(), nms);
            }
        } else if (to instanceof CraftInventoryPlayer cip) {
            cip.getInventory().setItem(index, nms);
            if (cip.getHolder() != null) {
                ServerPlayer player = ((CraftPlayer) cip.getHolder()).getHandle();
                if (index < net.minecraft.world.entity.player.Inventory.getSelectionSize()) {
                    index += 36;
                } else if (index > 39) {
                    index += 5;
                } else if (index > 35) {
                    index = 8 - (index - 36);
                }
                player.connection.send(new ClientboundContainerSetSlotPacket(player.inventoryMenu.containerId, player.inventoryMenu.incrementStateId(), index, nms));
            }
        } else if (to instanceof CraftInventory ci) {
            ci.getInventory().setItem(index, nms);
        } else {
            throw new UnsupportedOperationException("Unknown inventory impl " + to.getClass());
        }
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
