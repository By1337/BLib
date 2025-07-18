package org.by1337.blib.nms.v1_16_5.inventory;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.by1337.blib.inventory.InventoryUtil;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.util.Version;
import org.by1337.blib.util.invoke.LambdaMetafactoryUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;

@NMSAccessor(forClazz = InventoryUtil.class, forVersions = Version.V1_16_5)
public class InventoryUtilV1165 implements InventoryUtil {
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

    public void setPacketItem(Inventory inventory, Player player, ItemStack itemStack, int index) {
        if (inventory instanceof PlayerInventory) {
            if (index < net.minecraft.world.entity.player.Inventory.getHotbarSize()) {
                index += 36;
            } else if (index > 39) {
                index += 5;
            } else if (index > 35) {
                index = 8 - (index - 36);
            }
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                new ClientboundContainerSetSlotPacket(
                        inventory instanceof PlayerInventory ? 0 :
                                ((AbstractContainerMenu) ((CraftInventory) inventory).getInventory()).windowId,
                        index,
                        CraftItemStack.asNMSCopy(itemStack)
                )
        );
    }

    public void setPacketItemInCursor(Player player, ItemStack itemStack) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                new ClientboundContainerSetSlotPacket(
                        -1,
                        -1,
                        CraftItemStack.asNMSCopy(itemStack)
                )
        );
    }


    public void silentSetItem(Inventory inventory, ItemStack itemStack, int slot) {
        if (inventory instanceof PlayerInventory) {
            CraftInventoryPlayer inv = (CraftInventoryPlayer) inventory;
            inv.getInventory().setItem(slot, CraftItemStack.asNMSCopy(itemStack));
        } else {
            AbstractContainerMenu menu = ((AbstractContainerMenu) ((CraftInventory) inventory).getInventory());
            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
            menu.slots.get(slot).set(nmsItem);
            menu.items.set(slot, nmsItem);
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


    @Override
    public void setItemStackWithoutCopy(Inventory to, ItemStack who, int index) {
        net.minecraft.world.item.ItemStack nms;
        if (who instanceof CraftItemStack craftItemStack) {
            nms = Objects.requireNonNullElse(craftItemStack.getHandle(), net.minecraft.world.item.ItemStack.EMPTY);
        } else {
            nms = CraftItemStack.asNMSCopy(who);
        }
        if (to instanceof CraftResultInventory cri) {
            if (index < cri.getIngredientsInventory().getSize()) {
                cri.getIngredientsInventory().setItem(index, nms);
            } else {
                cri.getResultInventory().setItem(index - cri.getIngredientsInventory().getSize(), nms);
            }
        } else if (to instanceof CraftInventoryCrafting cic) {
            if (index < cic.getResultInventory().getSize()) {
                cic.getResultInventory().setItem(index, nms);
            } else {
                cic.getMatrixInventory().setItem(index - cic.getResultInventory().getSize(), nms);
            }
        } else if (to instanceof CraftInventoryPlayer cip) {
            cip.getInventory().setItem(index, nms);
            if (cip.getHolder() != null) {
                ServerPlayer player = ((CraftPlayer) cip.getHolder()).getHandle();
                if (player.playerConnection != null) {
                    if (index < net.minecraft.world.entity.player.Inventory.getHotbarSize()) {
                        index += 36;
                    } else if (index > 39) {
                        index += 5;
                    } else if (index > 35) {
                        index = 8 - (index - 36);
                    }
                    player.playerConnection.sendPacket(new ClientboundContainerSetSlotPacket(player.defaultContainer.windowId, index, nms));
                }
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
