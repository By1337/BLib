package org.by1337.blib.nms.v1_16_5.nbt;

import net.minecraft.nbt.*;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.by1337.blib.nbt.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ParseCompoundTagV165 implements ParseCompoundTag {
    private final PlayerDataStorage worldNBTStorage;

    public ParseCompoundTagV165() {
        this.worldNBTStorage = ((CraftServer) Bukkit.getServer()).getServer().worldNBTStorage;
    }

    public @NotNull CompoundTag copy(@NotNull ItemStack itemStack) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        net.minecraft.nbt.CompoundTag nmsTags = new net.minecraft.nbt.CompoundTag();
        nmsItem.save(nmsTags);
        CompoundTag compoundTag = new CompoundTag();
        this.copyAsApiType(nmsTags, compoundTag);
        return compoundTag;
    }

    @Override
    public @NotNull CompoundTag pdcToCompoundTag(@NotNull PersistentDataContainer persistentDataContainer) {
        if (persistentDataContainer instanceof CraftPersistentDataContainer pdc) {
            return (CompoundTag) convertFromNms(pdc.toTagCompound());
        }
        return new CompoundTag();
    }

    public @NotNull ItemStack create(@NotNull CompoundTag compoundTag) {
        net.minecraft.nbt.CompoundTag nms = new net.minecraft.nbt.CompoundTag();
        this.copyAsNms(compoundTag, nms);
        return CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.of(nms));
    }

    public CompletableFuture<CompoundTag> readOfflinePlayerData(@NotNull UUID player) {
        return CompletableFuture.supplyAsync(
                () -> this.applyIfNotNull(this.worldNBTStorage.getPlayerData(player.toString()), nbt -> (CompoundTag) this.convertFromNms(nbt))
        );
    }

    @Nullable
    private <T, R> R applyIfNotNull(@Nullable T raw, Function<T, R> function) {
        return (raw == null ? null : function.apply(raw));
    }

    @Override
    public Object toNMS(@NotNull NBT nbt) {
        return convert(nbt);
    }

    @Override
    public NBT fromNMS(@NotNull Object nmsObj) {
        return convertFromNms((Tag) nmsObj);
    }

    private static void copyAsNms(CompoundTag compoundTag, net.minecraft.nbt.CompoundTag nms) {
        for (Entry<String, NBT> entry : compoundTag.entrySet()) {
            NBT nbt = (NBT) entry.getValue();
            String key = (String) entry.getKey();
            nms.set(key, convert(nbt));
        }
    }

    public static Tag convert(NBT nbt) {
        if (nbt instanceof ByteArrNBT) {
            return new ByteArrayTag(((ByteArrNBT) nbt).getValue());
        } else if (nbt instanceof IntArrNBT) {
            return new IntArrayTag(((IntArrNBT) nbt).getValue());
        } else if (nbt instanceof LongArrNBT) {
            return new LongArrayTag(((LongArrNBT) nbt).getValue());
        } else if (nbt instanceof ByteNBT) {
            return ByteTag.valueOf(((ByteNBT) nbt).getValue());
        } else if (nbt instanceof DoubleNBT) {
            return DoubleTag.valueOf(((DoubleNBT) nbt).getValue());
        } else if (nbt instanceof FloatNBT) {
            return FloatTag.valueOf(((FloatNBT) nbt).getValue());
        } else if (nbt instanceof IntNBT) {
            return IntTag.valueOf(((IntNBT) nbt).getValue());
        } else if (nbt instanceof LongNBT) {
            return LongTag.valueOf(((LongNBT) nbt).getValue());
        } else if (nbt instanceof ShortNBT) {
            return ShortTag.valueOf(((ShortNBT) nbt).getValue());
        } else if (nbt instanceof StringNBT) {
            return StringTag.valueOf(((StringNBT) nbt).getValue());
        } else if (nbt instanceof CompoundTag) {
            net.minecraft.nbt.CompoundTag subNms = new net.minecraft.nbt.CompoundTag();
            copyAsNms((CompoundTag) nbt, subNms);
            return subNms;
        } else if (!(nbt instanceof ListNBT)) {
            throw new UnsupportedOperationException("Unsupported tag type: " + nbt.getClass().getSimpleName());
        } else {
            ListNBT listNBT = (ListNBT) nbt;
            ListTag listTag = new ListTag();

            for (NBT element : listNBT.getList()) {
                listTag.add(convert(element));
            }

            return listTag;
        }
    }

    private static void copyAsApiType(net.minecraft.nbt.CompoundTag nms, CompoundTag compoundTag) {
        for (String key : nms.getKeys()) {
            Tag tag = nms.get(key);
            compoundTag.putTag(key, convertFromNms(tag));
        }
    }

    private static NBT convertFromNms(Tag tag) {
        if (tag instanceof ByteArrayTag byteTags) {
            return new ByteArrNBT(byteTags.getBytes());
        } else if (tag instanceof IntArrayTag intTags) {
            return new IntArrNBT(intTags.getInts());
        } else if (tag instanceof LongArrayTag longTags) {
            return new LongArrNBT(longTags.getLongs());
        } else if (tag instanceof ByteTag byteTag) {
            return ByteNBT.valueOf(byteTag.asByte());
        } else if (tag instanceof net.minecraft.nbt.CompoundTag nmsTags) {
            CompoundTag compoundTag1 = new CompoundTag();
            copyAsApiType(nmsTags, compoundTag1);
            return compoundTag1;
        } else if (tag instanceof DoubleTag doubleTag) {
            return new DoubleNBT(doubleTag.asDouble());
        } else if (tag instanceof FloatTag floatTag) {
            return new FloatNBT(floatTag.asFloat());
        } else if (tag instanceof IntTag intTag) {
            return IntNBT.valueOf(intTag.asInt());
        } else if (tag instanceof LongTag longTag) {
            return LongNBT.valueOf(longTag.asLong());
        } else if (tag instanceof ShortTag shortTag) {
            return ShortNBT.valueOf(shortTag.asShort());
        } else if (tag instanceof StringTag stringTag) {
            return new StringNBT(stringTag.asString());
        } else if (!(tag instanceof ListTag)) {
            throw new UnsupportedOperationException("Unsupported tag type: " + tag.getClass().getSimpleName());
        } else {
            ListTag listTag = (ListTag) tag;
            ListNBT listNBT = new ListNBT();

            for (Tag value : listTag) {
                listNBT.add(convertFromNms(value));
            }

            return listNBT;
        }
    }
}
