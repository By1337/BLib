package org.by1337.blib.nbt.impl;

import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    public NbtType getType() {
        return NbtType.LONG_ARR;
    }

    @Override
    public Object getAsObject() {
        return value;
    }

    @Override
    public LongArrNBT copy() {
        return new LongArrNBT(value.clone());
    }

    public Spliterator<Long> spliterator() {
        return new SpliteratorImp();
    }
    public Stream<Long> stream(){
        return StreamSupport.stream(spliterator(), false);
    }

    private class SpliteratorImp implements Spliterator<Long> {
        private int index = 0;

        @Override
        public boolean tryAdvance(Consumer<? super Long> action) {
            if (index < value.length) {
                action.accept(value[index++]);
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<Long> trySplit() {
            int currentSize = value.length - index;
            if (currentSize < 2) {
                return null;
            }
            int splitSize = currentSize / 2;
            long[] splitArray = Arrays.copyOfRange(value, index, index + splitSize);
            index += splitSize;
            return new LongArrNBT(splitArray).spliterator();
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
