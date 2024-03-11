package org.by1337.blib.nbt;

import org.bukkit.inventory.ItemStack;
import org.by1337.blib.nbt.impl.CompoundTag;

public interface ParseCompoundTag {
    CompoundTag copy(ItemStack itemStack);
    ItemStack create(CompoundTag compoundTag);
}
