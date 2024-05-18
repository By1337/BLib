package org.by1337.blib.nbt.impl;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;
import java.util.Objects;

public class ByteNBT extends NBT{
    private final byte value;
    private ByteNBT(byte value) {
        this.value = value;
    }

    @Override
    public NbtType getType() {
        return NbtType.BYTE;
    }

    public byte getValue() {
        return value;
    }

    public static ByteNBT valueOf(byte b) {
        return Cache.cache[128 + b];
    }

    @Override
    public Object getAsObject() {
        return value;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteNBT byteNBT = (ByteNBT) o;
        return value == byteNBT.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    static class Cache {
        static final ByteNBT[] cache = new ByteNBT[256];

        private Cache() {
        }

        static {
            for(int i = 0; i < cache.length; ++i) {
                cache[i] = new ByteNBT((byte)(i - 128));
            }

        }
    }
}
