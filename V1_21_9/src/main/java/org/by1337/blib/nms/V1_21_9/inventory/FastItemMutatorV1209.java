package org.by1337.blib.nms.V1_21_9.inventory;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.ItemLore;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.inventory.FastItemMutator;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.nms.V1_20_6.inventory.FastItemMutatorV1206;
import org.by1337.blib.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@NMSAccessor(forClazz = FastItemMutator.class, from = Version.V1_21_9)
public class FastItemMutatorV1209 implements FastItemMutator {
    private final FastItemMutatorV1206 nms = new FastItemMutatorV1206();

    @Override
    public List<net.kyori.adventure.text.Component> getItemLore(String key, Object itemStack) {
        var item = ((net.minecraft.world.item.ItemStack) itemStack);
        ItemLore itemLore = item.get(nms.getDataComponentType(key));
        if (itemLore == null) return null;
        return PaperAdventure.asAdventure(itemLore.lines());
    }


    @Override
    public boolean has(String key, Object itemStack) {
        return nms.has(key, itemStack);
    }

    @Override
    public void setUnit(String key, Object itemStack) {
        nms.setUnit(key, itemStack);
    }

    @Override
    public void setItemLore(String key, List<Component> value, Object itemStack) {
        nms.setItemLore(key, value, itemStack);
    }

    @Override
    public @NotNull Component getComponent(String key, Object itemStack) {
        return nms.getComponent(key, itemStack);
    }

    @Override
    public void setComponent(String key, Component value, Object itemStack) {
        nms.setComponent(key, value, itemStack);
    }

    @Override
    public @Nullable Integer getInt(String key, Object itemStack) {
        return nms.getInt(key, itemStack);
    }

    @Override
    public void setInt(String key, int value, Object itemStack) {
        nms.setInt(key, value, itemStack);
    }

    @Override
    public void remove(String key, Object itemStack) {
        nms.remove(key, itemStack);
    }

    @Override
    public void setPopTime(int time, Object itemStack) {
        nms.setPopTime(time, itemStack);
    }

    @Override
    public void setCount(int count, Object itemStack) {
        nms.setCount(count, itemStack);
    }

    @Override
    public Object cloneNMSItemStack(@NotNull Object itemStack) {
        return nms.cloneNMSItemStack(itemStack);
    }

    @Override
    public Object asNMSCopyItemStack(@NotNull ItemStack itemStack) {
        return nms.asNMSCopyItemStack(itemStack);
    }

    @Override
    public ItemStack asBukkitMirror(@NotNull Object itemStack) {
        return nms.asBukkitMirror(itemStack);
    }
}
