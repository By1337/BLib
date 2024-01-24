package org.by1337.blib.util;

import java.util.LinkedList;
import java.util.List;

public class CyclicList<T> extends LinkedList<T> {

    public int current = 0;

    public T getNext() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        current = (current+ 1) % size();
        return get(current);
    }

    public T getPrevious() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }

        current = (current - 1 + size()) % size();
        return get(current);
    }

    public T getCurrent() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return get(current);
    }

    public List<T> getAll(){
        return new LinkedList<>(this);
    }
}