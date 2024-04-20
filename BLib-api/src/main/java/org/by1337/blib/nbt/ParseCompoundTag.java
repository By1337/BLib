package org.by1337.blib.nbt;

import org.bukkit.inventory.ItemStack;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ParseCompoundTag {
    CompoundTag copy(ItemStack itemStack);

    ItemStack create(CompoundTag compoundTag);

    CompletableFuture<@Nullable CompoundTag> readOfflinePlayerData(UUID player);
}
