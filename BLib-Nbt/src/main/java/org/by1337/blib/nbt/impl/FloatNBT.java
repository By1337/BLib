package org.by1337.blib.nbt.impl;

import org.by1337.blib.nbt.NbtType;
import org.by1337.blib.nbt.NumericNBT;

import java.util.Objects;

public class FloatNBT extends NumericNBT {

    private final float value;

    public FloatNBT(float value) {
        super(value);
        this.value = value;
    }

    @Override
    public NbtType getType() {
        return NbtType.FLOAT;
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
    public FloatNBT copy() {
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
