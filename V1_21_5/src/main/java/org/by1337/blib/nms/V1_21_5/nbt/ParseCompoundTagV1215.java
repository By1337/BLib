package org.by1337.blib.nms.V1_21_5.nbt;

import net.minecraft.nbt.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.nbt.impl.*;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.nms.V1_20_6.nbt.ParseCompoundTagV1206;
import org.by1337.blib.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@NMSAccessor(forClazz = ParseCompoundTag.class, forVersions = {Version.V1_21_5})
public class ParseCompoundTagV1215 implements ParseCompoundTag {
    private final ParseCompoundTagV1206 nms = new ParseCompoundTagV1206();

    @Override
    public org.by1337.blib.nbt.impl.@NotNull CompoundTag copy(@NotNull ItemStack itemStack) {
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        var nmsTags = (CompoundTag) nmsItem.save(MinecraftServer.getServer().registryAccess());
        org.by1337.blib.nbt.impl.CompoundTag compoundTag = new org.by1337.blib.nbt.impl.CompoundTag();
        copyAsApiType(nmsTags, compoundTag);
        return compoundTag;
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
        if (persistentDataContainer instanceof CraftPersistentDataContainer pdc) {
            return (org.by1337.blib.nbt.impl.CompoundTag) convertFromNms(pdc.toTagCompound());
        }
        return new org.by1337.blib.nbt.impl.CompoundTag();
    }

    @Override
    public Object toNMS(@NotNull NBT nbt) {
        return nms.toNMS(nbt);
    }

    @Override
    public NBT fromNMS(@NotNull Object nmsObj) {
        return convertFromNms((Tag) nmsObj);
    }

    public void copyAsNms(org.by1337.blib.nbt.impl.CompoundTag compoundTag, CompoundTag nms) {
        for (Map.Entry<String, NBT> entry : compoundTag.entrySet()) {
            NBT nbt = entry.getValue();
            String key = entry.getKey();
            nms.put(key, convert(nbt));
        }
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


    public void copyAsApiType(CompoundTag nms, org.by1337.blib.nbt.impl.CompoundTag compoundTag) {
        for (String key : nms.keySet()) {
            var tag = nms.get(key);
            compoundTag.putTag(key, convertFromNms(tag));
        }
    }

    public NBT convertFromNms(Tag tag) {
        if (tag instanceof ByteArrayTag byteTags) {
            return new ByteArrNBT(byteTags.getAsByteArray());
        } else if (tag instanceof IntArrayTag intTags) {
            return new IntArrNBT(intTags.getAsIntArray());
        } else if (tag instanceof LongArrayTag longTags) {
            return new LongArrNBT(longTags.getAsLongArray());
        } else if (tag instanceof ByteTag byteTag) {
            return ByteNBT.valueOf(byteTag.byteValue());
        } else if (tag instanceof CompoundTag nmsTags) {
            org.by1337.blib.nbt.impl.CompoundTag compoundTag1 = new org.by1337.blib.nbt.impl.CompoundTag();
            copyAsApiType(nmsTags, compoundTag1);
            return compoundTag1;
        } else if (tag instanceof DoubleTag doubleTag) {
            return new DoubleNBT(doubleTag.doubleValue());
        } else if (tag instanceof FloatTag floatTag) {
            return new FloatNBT(floatTag.floatValue());
        } else if (tag instanceof IntTag intTag) {
            return IntNBT.valueOf(intTag.intValue());
        } else if (tag instanceof LongTag longTag) {
            return LongNBT.valueOf(longTag.longValue());
        } else if (tag instanceof ShortTag shortTag) {
            return ShortNBT.valueOf(shortTag.shortValue());
        } else if (tag instanceof StringTag stringTag) {
            return new StringNBT(stringTag.value());
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
