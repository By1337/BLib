package org.by1337.blib.nms.v1_20_4.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.*;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.nbt.impl.*;

import java.util.Map;

public class ParseCompoundTagV204 implements ParseCompoundTag {
    @Override
    public org.by1337.blib.nbt.impl.CompoundTag copy(ItemStack itemStack) {
        var nmsItem = CraftItemStack.asNMSCopy(itemStack);
        var nmsTags = new CompoundTag();
        nmsItem.save(nmsTags);
        org.by1337.blib.nbt.impl.CompoundTag compoundTag = new org.by1337.blib.nbt.impl.CompoundTag();
        copyAsApiType(nmsTags, compoundTag);
        return compoundTag;
    }

    @Override
    public ItemStack create(org.by1337.blib.nbt.impl.CompoundTag compoundTag) {
        var nms = new CompoundTag();
        copyAsNms(compoundTag, nms);
        return CraftItemStack.asBukkitCopy(
                net.minecraft.world.item.ItemStack.of(nms)
        );
    }

    private void copyAsNms(org.by1337.blib.nbt.impl.CompoundTag compoundTag, CompoundTag nms) {
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
