package org.by1337.blib.nbt.impl;

import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    public ByteArrNBT copy() {
        return new ByteArrNBT(value.clone());
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

    public Spliterator<Byte> spliterator() {
        return new SpliteratorImp();
    }
    public Stream<Byte> stream(){
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    private class SpliteratorImp implements Spliterator<Byte> {
        private int index = 0;

        @Override
        public boolean tryAdvance(Consumer<? super Byte> action) {
            if (index < value.length) {
                action.accept(value[index++]);
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<Byte> trySplit() {
            int currentSize = value.length - index;
            if (currentSize < 2) {
                return null;
            }
            int splitSize = currentSize / 2;
            byte[] splitArray = Arrays.copyOfRange(value, index, index + splitSize);
            index += splitSize;
            return new ByteArrNBT(splitArray).spliterator();
        }

        @Override
        public long estimateSize() {
            return value.length - index;
        }

        @Override
        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }
}
