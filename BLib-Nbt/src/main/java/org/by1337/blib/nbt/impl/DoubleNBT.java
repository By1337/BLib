package org.by1337.blib.nbt.impl;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;

import java.util.Objects;

public class DoubleNBT extends NBT {

    private final double value;

    public DoubleNBT(double value) {
        this.value = value;
    }

    @Override
    public NbtType getType() {
        return NbtType.DOUBLE;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleNBT doubleNBT = (DoubleNBT) o;
        return Double.compare(value, doubleNBT.value) == 0;
    }

    @Override
    public Object getAsObject() {
        return value;
    }
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
