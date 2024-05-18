package org.by1337.blib.nbt.impl;

import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;

import java.util.Arrays;

public class ByteArrNBT extends NBT {
    private final byte[] value;

    public ByteArrNBT(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public Object getAsObject() {
        return value;
    }

    @Override
    public NbtType getType() {
        return NbtType.BYTE_ARR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteArrNBT that = (ByteArrNBT) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
