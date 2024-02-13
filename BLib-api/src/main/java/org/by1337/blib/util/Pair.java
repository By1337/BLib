package org.by1337.blib.util;

import java.util.Map;

public class Pair<L, R> implements Map.Entry<L, R> {

    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }
    public static <L, R> Pair<L, R> of(L left, R right){
        return new Pair<>(left, right);
    }
    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public final L getKey() {
        return getLeft();
    }

    @Override
    public R getValue() {
        return getRight();
    }

    @Override
    public R setValue(R value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "(" + getLeft() + ',' + getRight() + ')';
    }

    public String toString(final String format) {
        return String.format(format, getLeft(), getRight());
    }
}
