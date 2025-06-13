package org.by1337.blib.inventory;

import org.bukkit.inventory.ItemStack;
import org.by1337.blib.nbt.NBT;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LegacyFastItemMutator {
    String DISPLAY = "display";
    String LORE = "Lore";
    String NAME = "Name";

    ItemStack asBukkitMirror(@NotNull Object itemStack);
    Object asNMSCopyItemStack(@NotNull ItemStack itemStack);
    Object cloneNMSItemStack(@NotNull Object itemStack);
    @Nullable NBT getNBT(@NotNull Object nmsItem, String key);
    void setNBT(@NotNull Object nmsItem, @NotNull String key, @Nullable NBT nbt);
    void setCount(int count, Object itemStack);
    void setDamage(int count, Object itemStack);

}
