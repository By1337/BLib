package org.by1337.blib.nms.V1_21_3.inventory;

import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.ItemLore;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.inventory.FastItemMutator;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FastItemMutatorV1_21_3 implements FastItemMutator {
    private static final Map<String, DataComponentType<?>> DATA_COMPONENTS;

    public ItemStack asBukkitMirror(@NotNull Object itemStack) {
        return CraftItemStack.asCraftMirror((net.minecraft.world.item.ItemStack) itemStack);
    }

    public Object asNMSCopyItemStack(@NotNull ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    public Object cloneNMSItemStack(@NotNull Object itemStack) {
        return ((net.minecraft.world.item.ItemStack) itemStack).copy();
    }

    public void setCount(int count, Object itemStack) {
        ((net.minecraft.world.item.ItemStack) itemStack).setCount(count);
    }

    public void setPopTime(int time, Object itemStack) {
        ((net.minecraft.world.item.ItemStack) itemStack).setPopTime(time);
    }

    public void remove(String key, Object itemStack) {
        var item = ((net.minecraft.world.item.ItemStack) itemStack);
        item.remove(getDataComponentType(key));
    }

    public void setInt(String key, int value, Object itemStack) {
        var item = ((net.minecraft.world.item.ItemStack) itemStack);
        item.set(getDataComponentType(key), value);
    }

    public Integer getInt(String key, Object itemStack) {
        var item = ((net.minecraft.world.item.ItemStack) itemStack);
        return item.get(getDataComponentType(key));
    }

    public void setComponent(String key, net.kyori.adventure.text.Component value, Object itemStack) {
        var item = ((net.minecraft.world.item.ItemStack) itemStack);
        item.set(getDataComponentType(key), PaperAdventure.asVanilla(value));
    }

    public net.kyori.adventure.text.@NotNull Component getComponent(String key, Object itemStack) {
        var item = ((net.minecraft.world.item.ItemStack) itemStack);
        return PaperAdventure.asAdventure((Component) item.get(getDataComponentType(key)));
    }

    public void setItemLore(String key, List<net.kyori.adventure.text.Component> value, Object itemStack) {
        var item = ((net.minecraft.world.item.ItemStack) itemStack);
        item.set(getDataComponentType(key), new ItemLore(PaperAdventure.asVanilla(value)));
    }

    public List<net.kyori.adventure.text.Component> getItemLore(String key, Object itemStack) {
        var item = ((net.minecraft.world.item.ItemStack) itemStack);
        ItemLore itemLore = item.get(getDataComponentType(key));
        if (itemLore == null) return null;
        return PaperAdventure.asAdventure(itemLore.lines());
    }

    public void setUnit(String key, Object itemStack) {
        var item = ((net.minecraft.world.item.ItemStack) itemStack);
        item.set(getDataComponentType(key), Unit.INSTANCE);
    }

    public boolean has(String key, Object itemStack) {
        var item = ((net.minecraft.world.item.ItemStack) itemStack);
        return item.has(getDataComponentType(key));
    }

    @SuppressWarnings("unchecked")
    private <T> DataComponentType<T> getDataComponentType(@NotNull String key) {
        return (DataComponentType<T>) Objects.requireNonNull(DATA_COMPONENTS.get(key), "Unknown data component " + key);
    }

    static {
        DATA_COMPONENTS = new HashMap<>();
        for (DataComponentType<?> dataComponentType : BuiltInRegistries.DATA_COMPONENT_TYPE) {
            var key = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(dataComponentType);
            DATA_COMPONENTS.put(key.getPath().toLowerCase(), dataComponentType);
            DATA_COMPONENTS.put(key.toString().toLowerCase(), dataComponentType);
        }
    }
}
