package org.by1337.blib.nbt.impl;

import org.by1337.blib.nbt.NbtType;
import org.by1337.blib.nbt.NumericNBT;

public class ShortNBT extends NumericNBT {

    private final short value;

    private ShortNBT(short value) {
        super(value);
        this.value = value;
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
    public ShortNBT copy() {
        return this;
    }

    static class Cache {
        private static final int HIGH = 1024;
        private static final int LOW = -128;
        static final ShortNBT[] cache = new ShortNBT[HIGH - LOW + 1];

        private Cache() {
        }

        static {
            for (int i = 0; i < cache.length; ++i) {
                cache[i] = new ShortNBT((short) (LOW + i));
            }

        }
    }
}
