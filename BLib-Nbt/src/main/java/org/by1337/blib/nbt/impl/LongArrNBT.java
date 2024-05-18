package org.by1337.blib.nbt.impl;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;

import java.util.Arrays;

public class LongArrNBT extends NBT {
    private final long[] value;

    public LongArrNBT(long[] value) {
        this.value = value;
    }

    public long[] getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongArrNBT that = (LongArrNBT) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public NbtType getType() {
        return NbtType.LONG_ARR;
    }

    @Override
    public Object getAsObject() {
        return value;
    }
}
