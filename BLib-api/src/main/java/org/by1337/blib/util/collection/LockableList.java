package org.by1337.blib.util.collection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * LockableList provides a thread-safe wrapper around a List implementation,
 * protecting against ConcurrentModificationException by queuing modification actions
 * while the list is locked and applying them once the list is unlocked.
 * This class implements the Iterable interface to allow for iteration over its elements.
 *
 * <p>LockableList ensures thread safety only when the underlying source list
 * is itself thread-safe. If the source list is not thread-safe, concurrent
 * modifications may still lead to data inconsistency or other threading issues.
 *
 * @param <E> the type of elements in the list
 */
public class LockableList<E> implements Iterable<E> {
    private final List<E> source;
    private final Queue<Action> actions;
    private boolean locked;

    /**
     * Constructs a LockableList with the specified source list.
     *
     * @param source the source List to wrap
     */
    public LockableList(List<E> source) {
        this.source = source;
        actions = new LinkedBlockingQueue<>();
    }

    /**
     * Constructs a LockableList with an internal ArrayList as the source list.
     */
    public LockableList() {
        source = new ArrayList<>();
        actions = new LinkedBlockingQueue<>();
    }

    /**
     * Constructs a LockableList with the specified source list and action queue.
     * Used internally for creating thread-safe instances.
     *
     * @param source  the source List to wrap
     * @param actions the queue to store modification actions
     */
    private LockableList(List<E> source, Queue<Action> actions) {
        this.source = source;
        this.actions = actions;
    }

    /**
     * Creates a thread-safe LockableList with the specified source list.
     *
     * @param <E>    the type of elements in the list
     * @param source the source List to wrap
     * @return a thread-safe LockableList instance
     */
    public static <E> LockableList<E> createThreadSaveList(List<E> source) {
        return new LockableList<>(source, new ConcurrentLinkedQueue<>());
    }

    /**
     * Creates a thread-safe LockableList with an internal CopyOnWriteArrayList as the source list.
     *
     * @param <E> the type of elements in the list
     * @return a thread-safe LockableList instance
     */
    public static <E> LockableList<E> createThreadSaveList() {
        return new LockableList<>(new CopyOnWriteArrayList<>(), new ConcurrentLinkedQueue<>());
    }

    /**
     * Locks the list, preventing modifications.
     */
    public void lock() {
        locked = true;
    }

    /**
     * Unlocks the list, applying any pending modification actions.
     * Clears the action queue and applies each action to the source list.
     */
    @SuppressWarnings("unchecked")
    public void unlock() {
        locked = false;
        Action action;
        while ((action = actions.poll()) != null) {
            switch (action.type) {
                case ADD -> source.add((E) action.object);
                case CLEAR -> source.clear();
                case REMOVE -> source.remove(action.object);
            }
        }
    }

    public int size() {
        return source.size();
    }

    public boolean isEmpty() {
        return source.isEmpty();
    }

    public boolean contains(Object o) {
        return source.contains(o);
    }

    @NotNull
    public Iterator<E> iterator() {
        return new Itr();
    }

    @NotNull
    public Object @NotNull [] toArray() {
        return source.toArray();
    }

    @NotNull
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return source.toArray(a);
    }

    public boolean add(E e) {
        if (locked) {
            return actions.offer(new Action(ActionType.ADD, e));
        }
        return source.add(e);
    }

    public boolean remove(Object o) {
        if (locked) {
            actions.offer(new Action(ActionType.REMOVE, o));
            return true;
        }
        return source.remove(o);
    }


    public void clear() {
        if (locked) {
            actions.offer(new Action(ActionType.CLEAR, null));
        } else {
            source.clear();
        }
    }

    public E get(int index) {
        return source.get(index);
    }

    public int indexOf(Object o) {
        return source.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return source.lastIndexOf(o);
    }

    private class Itr implements Iterator<E> {
        Iterator<E> sourceIter = source.iterator();
        E last;

        Itr() {
        }

        public boolean hasNext() {
            return sourceIter.hasNext();
        }

        @SuppressWarnings("unchecked")
        public E next() {
            last = sourceIter.next();
            return last;
        }

        public void remove() {
            if (locked) {
                actions.offer(new Action(ActionType.REMOVE, last));
            } else {
                sourceIter.remove();
            }
        }

        public void forEachRemaining(Consumer<? super E> action) {
            sourceIter.forEachRemaining(action);
        }
    }

    private enum ActionType {
        ADD,
        REMOVE,
        CLEAR,
    }

    private static class Action {
        final ActionType type;
        final Object object;

        public Action(ActionType type, Object object) {
            this.type = type;
            this.object = object;
        }
    }
}
