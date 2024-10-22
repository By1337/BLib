package org.by1337.blib.nbt.impl;

import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    @Override
    public IntArrNBT copy() {
        return new IntArrNBT(value.clone());
    }

    public Spliterator<Integer> spliterator() {
        return new SpliteratorImp();
    }
    public Stream<Integer> stream(){
        return StreamSupport.stream(spliterator(), false);
    }

    private class SpliteratorImp implements Spliterator<Integer> {
        private int index = 0;

        @Override
        public boolean tryAdvance(Consumer<? super Integer> action) {
            if (index < value.length) {
                action.accept(value[index++]);
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<Integer> trySplit() {
            int currentSize = value.length - index;
            if (currentSize < 2) {
                return null;
            }
            int splitSize = currentSize / 2;
            int[] splitArray = Arrays.copyOfRange(value, index, index + splitSize);
            index += splitSize;
            return new IntArrNBT(splitArray).spliterator();
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
