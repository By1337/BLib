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
    public String toString() {
        return arrToString();
    }

    public String arrToString(){
        StringBuilder sb = new StringBuilder("[L;");

        for (Long b : value) {
            sb.append(b).append("L,");
        }
        if (value.length != 0)
            sb.setLength(sb.length() - 1);

        return sb.append("]").toString();
    }

    @Override
    public String toStringBeautifier(int lvl) {
        String space = " ".repeat(lvl + 4);
        StringBuilder sb = new StringBuilder("[ L;\n");
        for (long b : value) {
            sb.append(space).append(b).append("L,\n");
        }
        if (value.length != 0)
            sb.setLength(sb.length() - 2);
        else {
            sb.setLength(sb.length() - 1);
        }
        return sb.append("\n").append(" ".repeat(lvl)).append("]").toString();
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
