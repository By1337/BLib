package org.by1337.blib.nbt;

import net.minecraft.nbt.*;
import net.minecraft.nbt.CompoundTag;
import org.by1337.blib.nbt.impl.*;
import org.junit.Assert;

public class NbtUtil {
    public static NBT convertFromNms(Tag tag) {
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

    private static void copyAsApiType(CompoundTag nms, org.by1337.blib.nbt.impl.CompoundTag compoundTag) {
        for (String key : nms.getAllKeys()) {
            var tag = nms.get(key);
            compoundTag.putTag(key, convertFromNms(tag));
        }
    }

    public static void assertEqualsTags(NBT nbt1, NBT nbt) {
        if (nbt1 instanceof org.by1337.blib.nbt.impl.CompoundTag compoundTag) {
            for (String key : compoundTag.getTags().keySet()) {
                assertEqualsTags(
                        compoundTag.get(key),
                        ((org.by1337.blib.nbt.impl.CompoundTag) nbt).get(key)
                );
            }
        } else if (nbt1 instanceof ListNBT listTag) {
            var iterator = listTag.iterator();
            var iterator1 = ((ListNBT) nbt).iterator();
            while (iterator.hasNext()) {
                var nmsTag = iterator.next();
                var tag = iterator1.next();

                assertEqualsTags(
                        nmsTag,
                        tag
                );
            }
        } else {
            Assert.assertEquals(
                    nbt1.toString(),
                    nbt.toString()
            );
        }
    }
}
