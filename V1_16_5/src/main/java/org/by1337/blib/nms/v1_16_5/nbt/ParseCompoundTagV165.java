package org.by1337.blib.nms.v1_16_5.nbt;

import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.nbt.impl.ByteArrNBT;
import org.by1337.blib.nbt.impl.ByteNBT;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.by1337.blib.nbt.impl.DoubleNBT;
import org.by1337.blib.nbt.impl.FloatNBT;
import org.by1337.blib.nbt.impl.IntArrNBT;
import org.by1337.blib.nbt.impl.IntNBT;
import org.by1337.blib.nbt.impl.ListNBT;
import org.by1337.blib.nbt.impl.LongArrNBT;
import org.by1337.blib.nbt.impl.LongNBT;
import org.by1337.blib.nbt.impl.ShortNBT;
import org.by1337.blib.nbt.impl.StringNBT;
import org.jetbrains.annotations.Nullable;

public class ParseCompoundTagV165 implements ParseCompoundTag {
    private final PlayerDataStorage worldNBTStorage;

    public ParseCompoundTagV165() {
        this.worldNBTStorage = ((CraftServer)Bukkit.getServer()).getServer().worldNBTStorage;
    }

    public CompoundTag copy(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        net.minecraft.nbt.CompoundTag nmsTags = new net.minecraft.nbt.CompoundTag();
        nmsItem.save(nmsTags);
        CompoundTag compoundTag = new CompoundTag();
        this.copyAsApiType(nmsTags, compoundTag);
        return compoundTag;
    }

    public ItemStack create(CompoundTag compoundTag) {
        net.minecraft.nbt.CompoundTag nms = new net.minecraft.nbt.CompoundTag();
        this.copyAsNms(compoundTag, nms);
        return CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.of_(nms));
    }

    public CompletableFuture<CompoundTag> readOfflinePlayerData(UUID player) {
        return CompletableFuture.supplyAsync(
                () -> this.applyIfNotNull(this.worldNBTStorage.getPlayerData(player.toString()), nbt -> (CompoundTag)this.convertFromNms(nbt))
        );
    }

    @Nullable
    private <T, R> R applyIfNotNull(@Nullable T raw, Function<T, R> function) {
        return (R)(raw == null ? null : function.apply(raw));
    }

    private void copyAsNms(CompoundTag compoundTag, net.minecraft.nbt.CompoundTag nms) {
        for(Entry<String, NBT> entry : compoundTag.entrySet()) {
            NBT nbt = (NBT)entry.getValue();
            String key = (String)entry.getKey();
            nms.set(key, this.convert(nbt));
        }
    }

    private Tag convert(NBT nbt) {
        if (nbt instanceof ByteArrNBT) {
            return new ByteArrayTag(((ByteArrNBT)nbt).getValue());
        } else if (nbt instanceof IntArrNBT) {
            return new IntArrayTag(((IntArrNBT)nbt).getValue());
        } else if (nbt instanceof LongArrNBT) {
            return new LongArrayTag(((LongArrNBT)nbt).getValue());
        } else if (nbt instanceof ByteNBT) {
            return ByteTag.valueOf_(((ByteNBT)nbt).getValue());
        } else if (nbt instanceof DoubleNBT) {
            return DoubleTag.valueOf_(((DoubleNBT)nbt).getValue());
        } else if (nbt instanceof FloatNBT) {
            return FloatTag.valueOf_(((FloatNBT)nbt).getValue());
        } else if (nbt instanceof IntNBT) {
            return IntTag.valueOf_(((IntNBT)nbt).getValue());
        } else if (nbt instanceof LongNBT) {
            return LongTag.valueOf_(((LongNBT)nbt).getValue());
        } else if (nbt instanceof ShortNBT) {
            return ShortTag.valueOf_(((ShortNBT)nbt).getValue());
        } else if (nbt instanceof StringNBT) {
            return StringTag.valueOf_(((StringNBT)nbt).getValue());
        } else if (nbt instanceof CompoundTag) {
            net.minecraft.nbt.CompoundTag subNms = new net.minecraft.nbt.CompoundTag();
            this.copyAsNms((CompoundTag)nbt, subNms);
            return subNms;
        } else if (!(nbt instanceof ListNBT)) {
            throw new UnsupportedOperationException("Unsupported tag type: " + nbt.getClass().getSimpleName());
        } else {
            ListNBT listNBT = (ListNBT)nbt;
            ListTag listTag = new ListTag();

            for(NBT element : listNBT.getList()) {
                listTag.add(this.convert(element));
            }

            return listTag;
        }
    }

    private void copyAsApiType(net.minecraft.nbt.CompoundTag nms, CompoundTag compoundTag) {
        for(String key : nms.getKeys()) {
            Tag tag = nms.get(key);
            compoundTag.putTag(key, this.convertFromNms(tag));
        }
    }

    private NBT convertFromNms(Tag tag) {
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
            this.copyAsApiType(nmsTags, compoundTag1);
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
            ListTag listTag = (ListTag)tag;
            ListNBT listNBT = new ListNBT();

            for(Tag value : listTag) {
                listNBT.add(this.convertFromNms(value));
            }

            return listNBT;
        }
    }
}
