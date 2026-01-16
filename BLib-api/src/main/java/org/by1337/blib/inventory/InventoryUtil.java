package org.by1337.blib.inventory;

import dev.by1337.core.BCore;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface InventoryUtil {
    InventoryUtil INSTANCE = new InventoryUtil() {
        dev.by1337.core.bridge.inventory.InventoryUtil bridge = BCore.getInventoryUtil();

        @Override
        public void sendFakeTitle(Inventory inventory, Component newTitle) {
            bridge.sendFakeTitle(inventory, newTitle);
        }

        @Override
        public void flushInv(Player player) {
            bridge.flushInv(player);
        }

        @Override
        public void disableAutoFlush(Player player) {
            bridge.disableAutoFlush(player);
        }

        @Override
        public void enableAutoFlush(Player player) {
            bridge.enableAutoFlush(player);
        }

        @Override
        public void setItemStackWithoutCopy(Inventory to, ItemStack who, int index) {
            bridge.setItemStackWithoutCopy(to, who, index);
        }
    };
    void sendFakeTitle(Inventory inventory, Component newTitle);
    void flushInv(Player player);
    void disableAutoFlush(Player player);
    void enableAutoFlush(Player player);
    void setItemStackWithoutCopy(Inventory to, ItemStack who, int index);
}
