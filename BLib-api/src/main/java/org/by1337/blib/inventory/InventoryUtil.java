package org.by1337.blib.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface InventoryUtil {
    void sendFakeTitle(Inventory inventory, Component newTitle);
    void flushInv(Player player);
    void disableAutoFlush(Player player);
    void enableAutoFlush(Player player);
    void setItemStackWithoutCopy(Inventory to, ItemStack who, int index);
}
