package org.by1337.blib.nms.v1_16_5.nbt;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.nbt.impl.*;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ParseCompoundTagV165
        implements ParseCompoundTag {

    public CompoundTag copy(ItemStack itemStack) {
        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsTags = new NBTTagCompound();
        nmsItem.save(nmsTags);
        CompoundTag compoundTag = new CompoundTag();
        this.copyAsApiType(nmsTags, compoundTag);
        return compoundTag;
    }

    public ItemStack create(CompoundTag compoundTag) {
        NBTTagCompound nms = new NBTTagCompound();
        this.copyAsNms(compoundTag, nms);
        return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_16_R3.ItemStack.a(nms));
    }

    private final WorldNBTStorage worldNBTStorage = ((CraftServer) Bukkit.getServer()).getServer().worldNBTStorage;
    @Override
    public CompletableFuture<CompoundTag> readOfflinePlayerData(UUID player) {
        return CompletableFuture.supplyAsync(() ->
                applyIfNotNull(worldNBTStorage.getPlayerData(player.toString()), nbt -> (CompoundTag) convertFromNms(nbt))
        );
    }
    @Nullable
    private <T, R> R applyIfNotNull(@Nullable T raw, Function<T, R> function) {
        if (raw == null) return null;
        return function.apply(raw);
    }

    private void copyAsNms(CompoundTag compoundTag, NBTTagCompound nms) {
        for (var entry : compoundTag.entrySet()) {
            NBT nbt = entry.getValue();
            String key = entry.getKey();
            nms.set(key, this.convert(nbt));
        }
    }

    private NBTBase convert(NBT nbt) {
        if (nbt instanceof ByteArrNBT) {
            return new NBTTagByteArray(((ByteArrNBT) nbt).getValue());
        }
        if (nbt instanceof IntArrNBT) {
            return new NBTTagIntArray(((IntArrNBT) nbt).getValue());
        }
        if (nbt instanceof LongArrNBT) {
            return new NBTTagLongArray(((LongArrNBT) nbt).getValue());
        }
        if (nbt instanceof ByteNBT) {
            return NBTTagByte.a(((ByteNBT) nbt).getValue());
        }
        if (nbt instanceof DoubleNBT) {
            return NBTTagDouble.a(((DoubleNBT) nbt).getValue());
        }
        if (nbt instanceof FloatNBT) {
            return NBTTagFloat.a(((FloatNBT) nbt).getValue());
        }
        if (nbt instanceof IntNBT) {
            return NBTTagInt.a(((IntNBT) nbt).getValue());
        }
        if (nbt instanceof LongNBT) {
            return NBTTagLong.a(((LongNBT) nbt).getValue());
        }
        if (nbt instanceof ShortNBT) {
            return NBTTagShort.a(((ShortNBT) nbt).getValue());
        }
        if (nbt instanceof StringNBT) {
            return NBTTagString.a(((StringNBT) nbt).getValue());
        }
        if (nbt instanceof CompoundTag) {
            NBTTagCompound subNms = new NBTTagCompound();
            this.copyAsNms((CompoundTag) nbt, subNms);
            return subNms;
        }
        if (nbt instanceof ListNBT listNBT) {
            NBTTagList listTag = new NBTTagList();
            for (NBT element : listNBT.getList()) {
                listTag.add(this.convert(element));
            }
            return listTag;
        }
        throw new UnsupportedOperationException("Unsupported tag type: " + nbt.getClass().getSimpleName());
    }

    private void copyAsApiType(NBTTagCompound nms, CompoundTag compoundTag) {
        for (String key : nms.getKeys()) {
            NBTBase tag = nms.get(key);
            compoundTag.putTag(key, this.convertFromNms(tag));
        }
    }

    private NBT convertFromNms(NBTBase tag) {
        CompoundTag compoundTag1;
        if (tag instanceof NBTTagByteArray byteTags) {
            return new ByteArrNBT(byteTags.getBytes());
        }
        if (tag instanceof NBTTagIntArray intTags) {
            return new IntArrNBT(intTags.getInts());
        }
        if (tag instanceof NBTTagLongArray longTags) {
            return new LongArrNBT(longTags.getLongs());
        }
        if (tag instanceof NBTTagByte byteTag) {
            return ByteNBT.valueOf(byteTag.asByte());
        }
        if (tag instanceof NBTTagCompound nmsTags) {
            compoundTag1 = new CompoundTag();
            this.copyAsApiType(nmsTags, compoundTag1);
            return compoundTag1;
        }
        if (tag instanceof NBTTagDouble doubleTag) {
            return new DoubleNBT(doubleTag.asDouble());
        }
        if (tag instanceof NBTTagFloat floatTag) {
            return new FloatNBT(floatTag.asFloat());
        }
        if (tag instanceof NBTTagInt intTag) {
            return IntNBT.valueOf(intTag.asInt());
        }
        if (tag instanceof NBTTagLong longTag) {
            return LongNBT.valueOf(longTag.asLong());
        }
        if (tag instanceof NBTTagShort shortTag) {
            return ShortNBT.valueOf(shortTag.asShort());
        }
        if (tag instanceof NBTTagString stringTag) {
            return new StringNBT(stringTag.asString());
        }
        if (tag instanceof NBTTagList listTag) {
            ListNBT listNBT = new ListNBT();
            for (NBTBase value : listTag) {
                listNBT.add(this.convertFromNms(value));
            }
            return listNBT;
        }
        throw new UnsupportedOperationException("Unsupported tag type: " + tag.getClass().getSimpleName());
    }
}