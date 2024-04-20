package org.by1337.blib.nbt.impl;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;

import java.util.Objects;

public class FloatNBT extends NBT {

    private final float value;

    public FloatNBT(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value + "f";
    }
    @Override
    public NbtType getType() {
        return NbtType.FLOAT;
    }
    @Override
    public String toStringBeautifier(int lvl) {
        return toString();
    }

    public float getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatNBT floatNBT = (FloatNBT) o;
        return Float.compare(value, floatNBT.value) == 0;
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
