package org.by1337.blib.util.collection;

import java.util.Iterator;
import java.util.List;

public class CyclicIterator<T> implements Iterator<T> {
    private final List<T> list;
    private int current = 0;

    public CyclicIterator(List<T> list) {
        this.list = list;
    }

    public boolean hasNext() {
        return !list.isEmpty();
    }

    public T next() {
        if (list.isEmpty()) {
            throw new IllegalStateException("List is empty");
        } else {
            this.current = (current) % list.size();
            return list.get(current++);
        }
    }

    public void remove() {
        if (list.isEmpty()) {
            throw new IllegalStateException("Cannot remove from empty list");
        }
        current = Math.max(--current % list.size(), 0);
        list.remove(current);
        if (list.size() >= current) {
            current = 0;
        }
    }
}