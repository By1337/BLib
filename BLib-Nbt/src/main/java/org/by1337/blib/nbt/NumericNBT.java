package org.by1337.blib.nbt;

public abstract class NumericNBT extends NBT {
    private final Number value;

    public NumericNBT(Number value) {
        this.value = value;
    }

    public int intValue() {
        return value.intValue();
    }

    public double doubleValue() {
        return value.doubleValue();
    }

    public byte byteValue() {
        return value.byteValue();
    }

    public long longValue() {
        return value.longValue();
    }

    public float floatValue() {
        return value.floatValue();
    }

    public short shortValue() {
        return value.shortValue();
    }
    public Number getAsNumber() {
        return value;
    }
}
