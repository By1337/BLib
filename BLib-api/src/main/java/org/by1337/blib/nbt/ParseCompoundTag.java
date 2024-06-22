package org.by1337.blib.nbt;

import org.bukkit.inventory.ItemStack;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ParseCompoundTag {
    @NotNull CompoundTag copy(@NotNull ItemStack itemStack);

    @NotNull ItemStack create(@NotNull CompoundTag compoundTag);

    CompletableFuture<@Nullable CompoundTag> readOfflinePlayerData(@NotNull UUID player);
}
