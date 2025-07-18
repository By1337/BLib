package org.by1337.blib.nms.V1_21_6.nbt;

import net.minecraft.nbt.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.nbt.impl.*;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.nms.V1_21_5.nbt.ParseCompoundTagV1215;
import org.by1337.blib.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NMSAccessor(forClazz = ParseCompoundTag.class, from = Version.V1_21_6, to = Version.V1_21_8)
public class ParseCompoundTagV1216 implements ParseCompoundTag {
    private final ParseCompoundTagV1215 nms = new ParseCompoundTagV1215();

    @Override
    public org.by1337.blib.nbt.impl.@NotNull CompoundTag copy(@NotNull ItemStack itemStack) {
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        var nmsTags = save(nmsItem);
        org.by1337.blib.nbt.impl.CompoundTag compoundTag = new org.by1337.blib.nbt.impl.CompoundTag();
        nms.copyAsApiType(nmsTags, compoundTag);
        return compoundTag;
    }

    private CompoundTag save(net.minecraft.world.item.ItemStack itemStack) {
        return (CompoundTag) net.minecraft.world.item.ItemStack.CODEC.encodeStart(
                MinecraftServer.getServer().registryAccess().createSerializationContext(NbtOps.INSTANCE),
                itemStack
        ).getOrThrow();
    }

    @Override
    public @NotNull ItemStack create(org.by1337.blib.nbt.impl.@NotNull CompoundTag compoundTag) {
        return nms.create(compoundTag);
    }

    @Override
    public CompletableFuture<org.by1337.blib.nbt.impl.@Nullable CompoundTag> readOfflinePlayerData(@NotNull UUID player) {
        return nms.readOfflinePlayerData(player);
    }

    @Override
    public @NotNull org.by1337.blib.nbt.impl.CompoundTag pdcToCompoundTag(@NotNull PersistentDataContainer persistentDataContainer) {
        return nms.pdcToCompoundTag(persistentDataContainer);
    }

    @Override
    public Object toNMS(@NotNull NBT nbt) {
        return nms.toNMS(nbt);
    }

    @Override
    public NBT fromNMS(@NotNull Object nmsObj) {
        return nms.fromNMS(nmsObj);
    }

    public void copyAsApiType(CompoundTag nms, org.by1337.blib.nbt.impl.CompoundTag compoundTag) {
        this.nms.copyAsApiType(nms, compoundTag);
    }

    public NBT convertFromNms(Tag tag) {
        return nms.convertFromNms(tag);
    }
}
