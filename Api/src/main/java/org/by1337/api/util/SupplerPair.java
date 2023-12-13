package org.by1337.api.util;

import java.util.function.Supplier;

public class SupplerPair<L, R> extends Pair<L, R> {
    private final Supplier<L> left;
    private final Supplier<R> right;

    public SupplerPair(Supplier<L> left1, Supplier<R> right1) {
        super(null, null);
        this.left = left1;
        this.right = right1;
    }

    public SupplerPair() {
        super(null, null);
        left = null;
        right = null;
    }

    @Override
    public L getLeft() {
        return left == null ? null : left.get();
    }

    @Override
    public R getRight() {
        return right == null ? null : right.get();
    }
}
