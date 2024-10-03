package org.by1337.blib.util.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class IdentityHashSet<E> implements Set<E> {
    private final IdentityHashMap<E, Object> map;
    private static final Object PRESENT = new Object();

    public IdentityHashSet() {
        map = new IdentityHashMap<>();
    }

    public IdentityHashSet(Collection<? extends E> c) {
        map = new IdentityHashMap<>(Math.max(c.size(), 12));
        addAll(c);
    }

    public IdentityHashSet(int initialCapacity) {
        map = new IdentityHashMap<>(initialCapacity);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public @NotNull Object[] toArray() {
        return map.keySet().toArray();
    }

    @Override
    public @NotNull <T1> T1[] toArray(@NotNull T1[] a) {
        return map.keySet().toArray(a);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == null;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return map.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return map.keySet().retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return map.keySet().removeAll(c);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
