package org.by1337.blib.nbt.impl;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;

public class ShortNBT extends NBT {

    private final short value;

    private ShortNBT(short value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value + "s";
    }
    @Override
    public NbtType getType() {
        return NbtType.SHORT;
    }

    public static ShortNBT valueOf(short i) {
        return i >= -128 && i <= 1024 ? Cache.cache[i + 128] : new ShortNBT(i);
    }

    public short getValue() {
        return value;
    }
    @Override
    public Object getAsObject() {
        return value;
    }

    @Override
    public String toStringBeautifier(int lvl) {
        return toString();
    }
    static class Cache {
        private static final int HIGH = 1024;
        private static final int LOW = -128;
        static final ShortNBT[] cache = new ShortNBT[HIGH - LOW + 1];

        private Cache() {
        }

        static {
            for(int i = 0; i < cache.length; ++i) {
                cache[i] = new ShortNBT((short)(LOW + i));
            }

        }
    }
}
