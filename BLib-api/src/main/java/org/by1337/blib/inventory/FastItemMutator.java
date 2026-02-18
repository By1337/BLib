package org.by1337.blib.inventory;

import io.papermc.paper.inventory.ItemRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public interface FastItemMutator {
    FastItemMutator INSTANCE = new FastItemMutator() {
        @Override
        public ItemStack asBukkitMirror(@NotNull Object itemStack) {
            return (ItemStack) itemStack;
        }

        @Override
        public Object asNMSCopyItemStack(@NotNull ItemStack itemStack) {
            return itemStack;
        }

        @Override
        public Object cloneNMSItemStack(@NotNull Object itemStack) {
            return ((ItemStack)itemStack).clone();
        }

        @Override
        public void setCount(int count, Object itemStack) {
            ((ItemStack)itemStack).setAmount(count);
        }

        @Override
        public void setPopTime(int time, Object itemStack) {

        }

        @Override
        public void remove(String key, Object itemStack) {

        }

        @Override
        public void setInt(String key, int value, Object itemStack) {

        }

        @Override
        public void setComponent(String key, Component value, Object itemStack) {
            ((ItemStack)itemStack).editMeta(m -> m.displayName(value));
        }

        @Override
        public void setItemLore(String key, List<Component> value, Object itemStack) {
            ((ItemStack)itemStack).editMeta(m -> m.lore(value));
        }

        @Override
        public void setUnit(String key, Object itemStack) {

        }

        @Override
        public @Nullable Integer getInt(String key, Object itemStack) {
            return 0;
        }

        @Override
        public @NotNull Component getComponent(String key, Object itemStack) {
            return Objects.requireNonNullElse(((ItemStack)itemStack).getItemMeta().displayName(), Component.empty());
        }

        @Override
        public @Nullable List<Component> getItemLore(String key, Object itemStack) {
            return ((ItemStack)itemStack).getItemMeta().lore();
        }

        @Override
        public boolean has(String key, Object itemStack) {
            return false;
        }
    };
    // Int
    String MAX_STACK_SIZE = "max_stack_size";
    String MAX_DAMAGE = "max_damage";
    String DAMAGE = "damage";
    String REPAIR_COST = "repair_cost";
    String OMINOUS_BOTTLE_AMPLIFIER = "ominous_bottle_amplifier";
    // Component
    String CUSTOM_NAME = "custom_name";
    String ITEM_NAME = "item_name";
    // ItemLore
    String LORE = "lore";
    // Unit
    String HIDE_ADDITIONAL_TOOLTIP = "hide_additional_tooltip";
    String HIDE_TOOLTIP = "hide_tooltip";
    String CREATIVE_SLOT_LOCK = "creative_slot_lock";
    String INTANGIBLE_PROJECTILE = "intangible_projectile";
    String FIRE_RESISTANT = "fire_resistant";

    ItemStack asBukkitMirror(@NotNull Object itemStack);

    Object asNMSCopyItemStack(@NotNull ItemStack itemStack);

    Object cloneNMSItemStack(@NotNull Object itemStack);

    void setCount(int count, Object itemStack);

    void setPopTime(int time, Object itemStack);

    void remove(String key, Object itemStack);

    void setInt(String key, int value, Object itemStack);

    void setComponent(String key, net.kyori.adventure.text.Component value, Object itemStack);

    void setItemLore(String key, List<Component> value, Object itemStack);

    void setUnit(String key, Object itemStack);

    @Nullable Integer getInt(String key, Object itemStack);

    @NotNull net.kyori.adventure.text.Component getComponent(String key, Object itemStack);

    @Nullable List<net.kyori.adventure.text.Component> getItemLore(String key, Object itemStack);

    boolean has(String key, Object itemStack);
}
