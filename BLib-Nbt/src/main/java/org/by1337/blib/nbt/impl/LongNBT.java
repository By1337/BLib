package org.by1337.blib.nbt.impl;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;

import java.util.Objects;

public class LongNBT extends NBT {

    private final long value;

    private LongNBT(long value) {
        this.value = value;
    }

    @Override
    public NbtType getType() {
        return NbtType.LONG;
    }

    public static LongNBT valueOf(long l) {
        return l >= -128L && l <= 1024L ? Cache.cache[(int) l + 128] : new LongNBT(l);
    }

    public long getValue() {
        return value;
    }
    @Override
    public Object getAsObject() {
        return value;
    }

    @Override
    public LongNBT copy() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongNBT longNBT = (LongNBT) o;
        return value == longNBT.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    static class Cache {
        private static final int HIGH = 1024;
        private static final int LOW = -128;
        static final LongNBT[] cache = new LongNBT[HIGH - LOW + 1];

        private Cache() {
        }

        static {
            for(int i = 0; i < cache.length; ++i) {
                cache[i] = new LongNBT(LOW + i);
            }

        }
    }
}
