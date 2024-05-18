package org.by1337.blib.nbt.impl;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;
import java.util.Arrays;

public class IntArrNBT extends NBT {
    private final int[] value;

    public IntArrNBT(int[] value) {
        this.value = value;
    }

    public int[] getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntArrNBT intArrNBT = (IntArrNBT) o;
        return Arrays.equals(value, intArrNBT.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
    @Override
    public NbtType getType() {
        return NbtType.INT_ARR;
    }

    @Override
    public Object getAsObject() {
        return value;
    }
}
