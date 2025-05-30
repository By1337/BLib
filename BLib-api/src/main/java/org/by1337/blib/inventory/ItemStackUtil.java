package org.by1337.blib.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.nbt.NBT;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ApiStatus.Experimental
public interface ItemStackUtil {
    void setItemStackWithoutCopy(Inventory to, ItemStack who, int index);
    ItemStack asBukkitMirror(@NotNull Object itemStack);
    Object asNMSCopyItemStack(@NotNull ItemStack itemStack);
    Object copyNMSItemStack(@NotNull Object itemStack);
    void setDisplayName(Component component, Object itemStack);
    void setDisplayName(String json, Object itemStack);
    void setLore(List<Component> jsons, Object itemStack);
    void setNBTToTag(String key, NBT nbt, Object itemStack);
    void setLoreJson(List<String> jsons, Object itemStack);
    void setCount(int count, Object itemStack);
    void setDamage(int count, Object itemStack);
    boolean isJsonSupport();
    @Nullable Component getDisplayName(Object itemStack);
    @Nullable String getJsonDisplayName(Object itemStack);
    List<Component> getLore(Object itemStack);
    List<String> getJsonLore(Object itemStack);
}
