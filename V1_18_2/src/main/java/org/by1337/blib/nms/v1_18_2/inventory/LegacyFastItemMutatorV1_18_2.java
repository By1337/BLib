package org.by1337.blib.nms.v1_18_2.inventory;

import net.minecraft.nbt.Tag;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.inventory.ItemStackSerialize;
import org.by1337.blib.inventory.LegacyFastItemMutator;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.ParseCompoundTag;

import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.nms.v1_18_2.nbt.ParseCompoundTagV182;
import org.by1337.blib.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NMSAccessor(forClazz = LegacyFastItemMutator.class, forVersions = Version.V1_18_2)
public class LegacyFastItemMutatorV1_18_2 implements LegacyFastItemMutator {
    private static final ParseCompoundTag PDC_UTIL = new ParseCompoundTagV182();

    public ItemStack asBukkitMirror(@NotNull Object itemStack) {
        return CraftItemStack.asCraftMirror((net.minecraft.world.item.ItemStack) itemStack);
    }

    public Object asNMSCopyItemStack(@NotNull ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    public Object cloneNMSItemStack(@NotNull Object itemStack) {
        return ((net.minecraft.world.item.ItemStack) itemStack).copy();
    }

    public @Nullable NBT getNBT(@NotNull Object nmsItem, String key) {
        net.minecraft.world.item.ItemStack item = (net.minecraft.world.item.ItemStack) nmsItem;
        var tag = item.getOrCreateTag();
        var nms = tag.get(key);
        return nms == null ? null : PDC_UTIL.fromNMS(nms);
    }

    public void setNBT(@NotNull Object nmsItem, @NotNull String key, @Nullable NBT nbt) {
        net.minecraft.world.item.ItemStack item = (net.minecraft.world.item.ItemStack) nmsItem;
        var tag = item.getOrCreateTag();
        if (nbt == null) {
            tag.remove(key);
        } else {
            tag.put(key, (Tag) PDC_UTIL.toNMS(nbt));
        }
    }

    public void setCount(int count, Object itemStack) {
        ((net.minecraft.world.item.ItemStack) itemStack).setCount(count);
    }

    public void setDamage(int count, Object itemStack) {
        ((net.minecraft.world.item.ItemStack) itemStack).setDamageValue(count);
    }
}
