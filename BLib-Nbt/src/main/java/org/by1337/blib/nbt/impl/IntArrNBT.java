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
    public String toStringBeautifier(int lvl) {
        String space = " ".repeat(lvl + 4);
        StringBuilder sb = new StringBuilder("[ I;\n");
        for (int b : value) {
            sb.append(space).append(b).append(",\n");
        }
        if (value.length != 0)
            sb.setLength(sb.length() - 2);
        else {
            sb.setLength(sb.length() - 1);
        }
        return sb.append("\n").append(" ".repeat(lvl)).append("]").toString();
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
    public String toString() {
        return arrToString();
    }

    public String arrToString(){
        StringBuilder sb = new StringBuilder("[I;");

        for (Integer b : value) {
            sb.append(b).append(",");
        }
        if (value.length != 0)
            sb.setLength(sb.length() - 1);

        return sb.append("]").toString();
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
