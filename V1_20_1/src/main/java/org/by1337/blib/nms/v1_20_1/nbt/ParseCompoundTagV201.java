package org.by1337.blib.nms.v1_20_1.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.*;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.nbt.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ParseCompoundTagV201 implements ParseCompoundTag {
    @Override
    public org.by1337.blib.nbt.impl.@NotNull CompoundTag copy(@NotNull ItemStack itemStack) {
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        var nmsTags = new CompoundTag();
        nmsItem.save(nmsTags);
        org.by1337.blib.nbt.impl.CompoundTag compoundTag = new org.by1337.blib.nbt.impl.CompoundTag();
        copyAsApiType(nmsTags, compoundTag);
        return compoundTag;
    }

    @Override
    public @NotNull ItemStack create(org.by1337.blib.nbt.impl.@NotNull CompoundTag compoundTag) {
        var nms = new CompoundTag();
        copyAsNms(compoundTag, nms);
        return CraftItemStack.asCraftMirror(
                net.minecraft.world.item.ItemStack.of(nms)
        );
    }

    @Override
    public Object toNMS(@NotNull NBT nbt) {
        return convert(nbt);
    }
    @Override
    public NBT fromNMS(@NotNull Object nmsObj) {
        return convertFromNms((Tag) nmsObj);
    }
    @Override
    public @NotNull org.by1337.blib.nbt.impl.CompoundTag pdcToCompoundTag(@NotNull PersistentDataContainer persistentDataContainer) {
        if (persistentDataContainer instanceof CraftPersistentDataContainer pdc){
            return (org.by1337.blib.nbt.impl.CompoundTag) convertFromNms(pdc.toTagCompound());
        }
        return new org.by1337.blib.nbt.impl.CompoundTag();
    }
    private void copyAsNms(org.by1337.blib.nbt.impl.CompoundTag compoundTag, CompoundTag nms) {
        for (Map.Entry<String, NBT> entry : compoundTag.entrySet()) {
            NBT nbt = entry.getValue();
            String key = entry.getKey();
            nms.put(key, convert(nbt));
        }
    }

    private final PlayerDataStorage worldNBTStorage = ((CraftServer) Bukkit.getServer()).getServer().playerDataStorage;
    @Override
    public CompletableFuture<org.by1337.blib.nbt.impl.CompoundTag> readOfflinePlayerData(@NotNull UUID player) {
        return CompletableFuture.supplyAsync(() ->
                applyIfNotNull(worldNBTStorage.getPlayerData(player.toString()), nbt -> (org.by1337.blib.nbt.impl.CompoundTag) convertFromNms(nbt))
        );
    }
    @Nullable
    private <T, R> R applyIfNotNull(@Nullable T raw, Function<T, R> function) {
        if (raw == null) return null;
        return function.apply(raw);
    }
    private Tag convert(NBT nbt) {
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
        } else if (nbt instanceof org.by1337.blib.nbt.impl.CompoundTag) {
            CompoundTag subNms = new CompoundTag();
            copyAsNms((org.by1337.blib.nbt.impl.CompoundTag) nbt, subNms);
            return subNms;
        } else if (nbt instanceof ListNBT listNBT) {
            ListTag listTag = new ListTag();
            for (NBT element : listNBT.getList()) {
                listTag.add(convert(element));
            }
            return listTag;
        } else {
            throw new UnsupportedOperationException("Unsupported tag type: " + nbt.getClass().getSimpleName());
        }
    }


    private void copyAsApiType(CompoundTag nms, org.by1337.blib.nbt.impl.CompoundTag compoundTag) {
        for (String key : nms.getAllKeys()) {
            var tag = nms.get(key);
            compoundTag.putTag(key, convertFromNms(tag));
        }
    }

    private NBT convertFromNms(Tag tag) {
        if (tag instanceof ByteArrayTag byteTags) {
            return new ByteArrNBT(byteTags.getAsByteArray());
        } else if (tag instanceof IntArrayTag intTags) {
            return new IntArrNBT(intTags.getAsIntArray());
        } else if (tag instanceof LongArrayTag longTags) {
            return new LongArrNBT(longTags.getAsLongArray());
        } else if (tag instanceof ByteTag byteTag) {
            return ByteNBT.valueOf(byteTag.getAsByte());
        } else if (tag instanceof CompoundTag nmsTags) {
            org.by1337.blib.nbt.impl.CompoundTag compoundTag1 = new org.by1337.blib.nbt.impl.CompoundTag();
            copyAsApiType(nmsTags, compoundTag1);
            return compoundTag1;
        } else if (tag instanceof DoubleTag doubleTag) {
            return new DoubleNBT(doubleTag.getAsDouble());
        } else if (tag instanceof FloatTag floatTag) {
            return new FloatNBT(floatTag.getAsFloat());
        } else if (tag instanceof IntTag intTag) {
            return IntNBT.valueOf(intTag.getAsInt());
        } else if (tag instanceof LongTag longTag) {
            return LongNBT.valueOf(longTag.getAsLong());
        } else if (tag instanceof ShortTag shortTag) {
            return ShortNBT.valueOf(shortTag.getAsShort());
        } else if (tag instanceof StringTag stringTag) {
            return new StringNBT(stringTag.getAsString());
        } else if (tag instanceof ListTag listTag) {
            ListNBT listNBT = new ListNBT();
            for (Tag value : listTag) {
                listNBT.add(convertFromNms(value));
            }
            return listNBT;
        } else {
            throw new UnsupportedOperationException("Unsupported tag type: " + tag.getClass().getSimpleName());
        }
    }

}
