package org.by1337.blib.util.collection;

import org.by1337.blib.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A synchronized hash map with automatic removal of unused values.
 * When the map is no longer needed, make sure to call {@link ExpiringSynchronizedMap#shutdown}
 * to avoid memory leaks.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class ExpiringSynchronizedMap<K, V> implements Map<K, V> {
    private final Object lock = new Object();
    private final Map<K, TimedValue> source;
    private final ScheduledExecutorService scheduler;
    private final long expiryDuration;
    private BiConsumer<K, V> onExpired;

    /**
     * Constructs an instance of ExpiringSynchronizedMap with the specified expiry duration.
     *
     * @param expiryDuration the duration after which an entry should be removed if not accessed
     * @param timeUnit       the time unit of the expiry duration
     */
    public ExpiringSynchronizedMap(long expiryDuration, TimeUnit timeUnit) {
        this(expiryDuration, timeUnit, HashMap::new);
    }

    @SuppressWarnings("unchecked")
    public ExpiringSynchronizedMap(long expiryDuration, TimeUnit timeUnit, Supplier<Map<K, ?>> mapSupplier) {
        this.expiryDuration = timeUnit.toMillis(expiryDuration);
        scheduler = Executors.newScheduledThreadPool(1);
        var map = mapSupplier.get();
        if (!map.isEmpty()) throw new IllegalArgumentException("map must be empty");
        source = (Map<K, TimedValue>) map;
    }

    /**
     * Removes the expired entry from the map and triggers the onExpired consumer if set.
     *
     * @param value the TimedValue instance representing the expired entry
     */
    private void expired(TimedValue value) {
        remove(value.key);
        if (onExpired != null) {
            onExpired.accept(value.key, value.value);
        }
    }

    /**
     * Sets a consumer to be called when an entry expires.
     *
     * @param consumer a BiConsumer that accepts the key and value of the expired entry
     */
    public void onExpired(BiConsumer<K, V> consumer) {
        onExpired = consumer;
    }

    /**
     * Shuts down the scheduler service to avoid memory leaks.
     * This should be called when the map is no longer needed.
     */
    public void shutdown() {
        scheduler.shutdown();
    }

    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     *
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        synchronized (lock) {
            return source.size();
        }
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        synchronized (lock) {
            return source.isEmpty();
        }
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified
     * key.  More formally, returns {@code true} if and only if
     * this map contains a mapping for a key {@code k} such that
     * {@code Objects.equals(key, k)}.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified
     * key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map ({@linkplain Collection##optional-restrictions optional})
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys ({@linkplain Collection##optional-restrictions optional})
     */
    @Override
    public boolean containsKey(Object key) {
        synchronized (lock) {
            return source.containsKey(key);
        }
    }

    /**
     * Returns {@code true} if this map maps one or more keys to the
     * specified value.  More formally, returns {@code true} if and only if
     * this map contains at least one mapping to a value {@code v} such that
     * {@code Objects.equals(value, v)}.  This operation
     * will probably require time linear in the map size for most
     * implementations of the {@code Map} interface.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the
     * specified value
     * @throws ClassCastException   if the value is of an inappropriate type for
     *                              this map ({@linkplain Collection##optional-restrictions optional})
     * @throws NullPointerException if the specified value is null and this
     *                              map does not permit null values ({@linkplain Collection##optional-restrictions optional})
     */
    @Override
    public boolean containsValue(Object value) {
        synchronized (lock) {
            for (TimedValue v : source.values()) {
                if (Objects.equals(value, v.value))
                    return true;
            }
            return false;
        }
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that
     * {@code Objects.equals(key, k)},
     * then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     * {@code null} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map ({@linkplain Collection##optional-restrictions optional})
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys ({@linkplain Collection##optional-restrictions optional})
     */
    @Override
    public V get(Object key) {
        TimedValue value;
        synchronized (lock) {
            value = source.get(key);
        }
        if (value == null) return null;
        value.updateLastAccessTime();
        value.updateRemoveTask();
        return value.value;
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * {@code m} is said to contain a mapping for a key {@code k} if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * {@code true}.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with {@code key},
     * if the implementation supports {@code null} values.)
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     * @throws NullPointerException          if the specified key or value is null
     *                                       and this map does not permit null keys or values
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     */
    @Nullable
    @Override
    public V put(K key, V value) {
        TimedValue timedValue = new TimedValue(key, value);
        TimedValue old;
        synchronized (lock) {
            old = source.put(key, timedValue);
        }
        if (old != null) {
            old.close();
            return old.value;
        }
        return null;
    }

    /**
     * Removes the mapping for a key from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from key {@code k} to value {@code v} such that
     * {@code Objects.equals(key, k)}, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     *
     * <p>Returns the value to which this map previously associated the key,
     * or {@code null} if the map contained no mapping for the key.
     *
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contained no mapping for the key; it's also possible that the map
     * explicitly mapped the key to {@code null}.
     *
     * <p>The map will not contain a mapping for the specified key once the
     * call returns.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}.
     * @throws UnsupportedOperationException if the {@code remove} operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the key is of an inappropriate type for
     *                                       this map ({@linkplain Collection##optional-restrictions optional})
     * @throws NullPointerException          if the specified key is null and this
     *                                       map does not permit null keys ({@linkplain Collection##optional-restrictions optional})
     */
    @Override
    public V remove(Object key) {
        TimedValue timedValue;
        synchronized (lock) {
            timedValue = source.remove(key);
        }
        if (timedValue != null) {
            timedValue.close();
            return timedValue.value;
        }
        return null;
    }

    /**
     * Copies all of the mappings from the specified map to this map
     * (optional operation).  The effect of this call is equivalent to that
     * of calling {@link #put(Object, Object) put(k, v)} on this map once
     * for each mapping from key {@code k} to value {@code v} in the
     * specified map.  The behavior of this operation is undefined if the specified map
     * is modified while the operation is in progress. If the specified map has a defined
     * <a href="SequencedCollection.html#encounter">encounter order</a>,
     * processing of its mappings generally occurs in that order.
     *
     * @param m mappings to be stored in this map
     * @throws UnsupportedOperationException if the {@code putAll} operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of a key or value in the
     *                                       specified map prevents it from being stored in this map
     * @throws NullPointerException          if the specified map is null, or if
     *                                       this map does not permit null keys or values, and the
     *                                       specified map contains null keys or values
     * @throws IllegalArgumentException      if some property of a key or value in
     *                                       the specified map prevents it from being stored in this map
     */
    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *                                       is not supported by this map
     */
    @Override
    public void clear() {
        synchronized (lock) {
            source.values().forEach(TimedValue::close);
            source.clear();
        }
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * {@code Iterator.remove}, {@code Set.remove},
     * {@code removeAll}, {@code retainAll}, and {@code clear}
     * operations.  It does not support the {@code add} or {@code addAll}
     * operations.
     *
     * @return a set view of the keys contained in this map
     */
    @NotNull
    @Override
    public Set<K> keySet() {
        synchronized (lock) {
            return source.keySet();
        }
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own {@code remove} operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Collection.remove}, {@code removeAll},
     * {@code retainAll} and {@code clear} operations.  It does not
     * support the {@code add} or {@code addAll} operations.
     *
     * @return a collection view of the values contained in this map
     */
    @NotNull
    @Override
    public Collection<V> values() {
        synchronized (lock) {
            List<V> list = new ArrayList<>(size());
            for (TimedValue value : source.values()) {
                list.add(value.value);
            }
            return Collections.unmodifiableCollection(list);
        }
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation, or through the
     * {@code setValue} operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Set.remove}, {@code removeAll}, {@code retainAll} and
     * {@code clear} operations.  It does not support the
     * {@code add} or {@code addAll} operations.
     *
     * @return a set view of the mappings contained in this map
     */
    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        synchronized (lock) {
            Set<Entry<K, V>> set = new HashSet<>();
            for (Entry<K, TimedValue> entry : source.entrySet()) {
                set.add(Pair.of(entry.getKey(), entry.getValue().value));
            }
            return Collections.unmodifiableSet(set);
        }
    }

    /**
     * Performs the given action for each entry in this map until all entries
     * have been processed or the action throws an exception.   Unless
     * otherwise specified by the implementing class, actions are performed in
     * the order of entry set iteration (if an iteration order is specified.)
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @param action The action to be performed for each entry
     * @throws NullPointerException            if the specified action is null
     * @throws ConcurrentModificationException if an entry is found to be
     *                                         removed during iteration
     * @implSpec The default implementation is equivalent to, for this {@code map}:
     * <pre> {@code
     * for (Map.Entry<K, V> entry : map.entrySet())
     *     action.accept(entry.getKey(), entry.getValue());
     * }</pre>
     * <p>
     * The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        synchronized (lock) {
            for (Entry<K, TimedValue> entry : source.entrySet()) {
                action.accept(entry.getKey(), entry.getValue().value);
            }
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map ({@linkplain Collection##optional-restrictions optional})
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys ({@linkplain Collection##optional-restrictions optional})
     * @implSpec The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    /**
     * Replaces each entry's value with the result of invoking the given
     * function on that entry until all entries have been processed or the
     * function throws an exception.  Exceptions thrown by the function are
     * relayed to the caller.
     *
     * @param function the function to apply to each entry
     * @throws UnsupportedOperationException   if the {@code set} operation
     *                                         is not supported by this map's entry set iterator.
     * @throws ClassCastException              if the class of a replacement value
     *                                         prevents it from being stored in this map
     *                                         ({@linkplain Collection##optional-restrictions optional})
     * @throws NullPointerException            if the specified function is null, or if a
     *                                         replacement value is null and this map does not permit null values
     *                                         ({@linkplain Collection##optional-restrictions optional})
     * @throws IllegalArgumentException        if some property of a replacement value
     *                                         prevents it from being stored in this map
     *                                         ({@linkplain Collection##optional-restrictions optional})
     * @throws ConcurrentModificationException if an entry is found to be
     *                                         removed during iteration
     * @implSpec <p>The default implementation is equivalent to, for this {@code map}:
     * <pre> {@code
     * for (Map.Entry<K, V> entry : map.entrySet())
     *     entry.setValue(function.apply(entry.getKey(), entry.getValue()));
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        for (Map.Entry<K, TimedValue> entry : source.entrySet()) {
            K k;
            TimedValue v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }

            // ise thrown from function is not a cme.
            V result = function.apply(k, v.value);
            v.close();
            try {
                entry.setValue(new TimedValue(k, result));
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
        }
    }

    /**
     * If the specified key is not already associated with a value (or is mapped
     * to {@code null}) associates it with the given value and returns
     * {@code null}, else returns the current value.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or
     * {@code null} if there was no mapping for the key.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with the key,
     * if the implementation supports null values.)
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws ClassCastException            if the key or value is of an inappropriate
     *                                       type for this map ({@linkplain Collection##optional-restrictions optional})
     * @throws NullPointerException          if the specified key or value is null,
     *                                       and this map does not permit null keys or values
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @implSpec The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * V v = map.get(key);
     * if (v == null)
     *     v = map.put(key, value);
     *
     * return v;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    @Nullable
    @Override
    public V putIfAbsent(K key, V value) {
        return Map.super.putIfAbsent(key, value);
    }

    /**
     * Removes the entry for the specified key only if it is currently
     * mapped to the specified value.
     *
     * @param key   key with which the specified value is associated
     * @param value value expected to be associated with the specified key
     * @return {@code true} if the value was removed
     * @throws UnsupportedOperationException if the {@code remove} operation
     *                                       is not supported by this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws ClassCastException            if the key or value is of an inappropriate
     *                                       type for this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws NullPointerException          if the specified key or value is null,
     *                                       and this map does not permit null keys or values
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @implSpec The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
     *     map.remove(key);
     *     return true;
     * } else
     *     return false;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    @Override
    public boolean remove(Object key, Object value) {
        return Map.super.remove(key, value);
    }

    /**
     * Replaces the entry for the specified key only if currently
     * mapped to the specified value.
     *
     * @param key      key with which the specified value is associated
     * @param oldValue value expected to be associated with the specified key
     * @param newValue value to be associated with the specified key
     * @return {@code true} if the value was replaced
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map ({@linkplain Collection##optional-restrictions optional})
     * @throws ClassCastException            if the class of a specified key or value
     *                                       prevents it from being stored in this map
     * @throws NullPointerException          if a specified key or newValue is null,
     *                                       and this map does not permit null keys or values
     * @throws NullPointerException          if oldValue is null and this map does not
     *                                       permit null values ({@linkplain Collection##optional-restrictions optional})
     * @throws IllegalArgumentException      if some property of a specified key
     *                                       or value prevents it from being stored in this map
     * @implSpec The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key) && Objects.equals(map.get(key), oldValue)) {
     *     map.put(key, newValue);
     *     return true;
     * } else
     *     return false;
     * }</pre>
     * <p>
     * The default implementation does not throw NullPointerException
     * for maps that do not support null values if oldValue is null unless
     * newValue is also null.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return Map.super.replace(key, oldValue, newValue);
    }

    /**
     * Replaces the entry for the specified key only if it is
     * currently mapped to some value.
     *
     * @param key   key with which the specified value is associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or
     * {@code null} if there was no mapping for the key.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with the key,
     * if the implementation supports null values.)
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws NullPointerException          if the specified key or value is null,
     *                                       and this map does not permit null keys or values
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     * @implSpec The default implementation is equivalent to, for this {@code map}:
     *
     * <pre> {@code
     * if (map.containsKey(key)) {
     *     return map.put(key, value);
     * } else
     *     return null;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    @Nullable
    @Override
    public V replace(K key, V value) {
        return Map.super.replace(key, value);
    }

    /**
     * If the specified key is not already associated with a value (or is mapped
     * to {@code null}), attempts to compute its value using the given mapping
     * function and enters it into this map unless {@code null}.
     *
     * <p>If the mapping function returns {@code null}, no mapping is recorded.
     * If the mapping function itself throws an (unchecked) exception, the
     * exception is rethrown, and no mapping is recorded.  The most
     * common usage is to construct a new object serving as an initial
     * mapped value or memoized result, as in:
     *
     * <pre> {@code
     * map.computeIfAbsent(key, k -> new Value(f(k)));
     * }</pre>
     *
     * <p>Or to implement a multi-value map, {@code Map<K,Collection<V>>},
     * supporting multiple values per key:
     *
     * <pre> {@code
     * map.computeIfAbsent(key, k -> new HashSet<V>()).add(v);
     * }</pre>
     *
     * <p>The mapping function should not modify this map during computation.
     *
     * @param key             key with which the specified value is to be associated
     * @param mappingFunction the mapping function to compute a value
     * @return the current (existing or computed) value associated with
     * the specified key, or null if the computed value is null
     * @throws NullPointerException          if the specified key is null and
     *                                       this map does not support null keys, or the mappingFunction
     *                                       is null
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @implSpec The default implementation is equivalent to the following steps for this
     * {@code map}, then returning the current value or {@code null} if now
     * absent:
     *
     * <pre> {@code
     * if (map.get(key) == null) {
     *     V newValue = mappingFunction.apply(key);
     *     if (newValue != null)
     *         map.put(key, newValue);
     * }
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about detecting if the
     * mapping function modifies this map during computation and, if
     * appropriate, reporting an error. Non-concurrent implementations should
     * override this method and, on a best-effort basis, throw a
     * {@code ConcurrentModificationException} if it is detected that the
     * mapping function modifies this map during computation. Concurrent
     * implementations should override this method and, on a best-effort basis,
     * throw an {@code IllegalStateException} if it is detected that the
     * mapping function modifies this map during computation and as a result
     * computation would never complete.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties. In particular, all implementations of
     * subinterface {@link ConcurrentMap} must document
     * whether the mapping function is applied once atomically only if the value
     * is not present.
     * @since 1.8
     */
    @Override
    public V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mappingFunction) {
        return Map.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * If the value for the specified key is present and non-null, attempts to
     * compute a new mapping given the key and its current mapped value.
     *
     * <p>If the remapping function returns {@code null}, the mapping is removed.
     * If the remapping function itself throws an (unchecked) exception, the
     * exception is rethrown, and the current mapping is left unchanged.
     *
     * <p>The remapping function should not modify this map during computation.
     *
     * @param key               key with which the specified value is to be associated
     * @param remappingFunction the remapping function to compute a value
     * @return the new value associated with the specified key, or null if none
     * @throws NullPointerException          if the specified key is null and
     *                                       this map does not support null keys, or the
     *                                       remappingFunction is null
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @implSpec The default implementation is equivalent to performing the following
     * steps for this {@code map}, then returning the current value or
     * {@code null} if now absent:
     *
     * <pre> {@code
     * if (map.get(key) != null) {
     *     V oldValue = map.get(key);
     *     V newValue = remappingFunction.apply(key, oldValue);
     *     if (newValue != null)
     *         map.put(key, newValue);
     *     else
     *         map.remove(key);
     * }
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about detecting if the
     * remapping function modifies this map during computation and, if
     * appropriate, reporting an error. Non-concurrent implementations should
     * override this method and, on a best-effort basis, throw a
     * {@code ConcurrentModificationException} if it is detected that the
     * remapping function modifies this map during computation. Concurrent
     * implementations should override this method and, on a best-effort basis,
     * throw an {@code IllegalStateException} if it is detected that the
     * remapping function modifies this map during computation and as a result
     * computation would never complete.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties. In particular, all implementations of
     * subinterface {@link ConcurrentMap} must document
     * whether the remapping function is applied once atomically only if the
     * value is not present.
     * @since 1.8
     */
    @Override
    public V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Map.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * Attempts to compute a mapping for the specified key and its current
     * mapped value (or {@code null} if there is no current mapping). For
     * example, to either create or append a {@code String} msg to a value
     * mapping:
     *
     * <pre> {@code
     * map.compute(key, (k, v) -> (v == null) ? msg : v.concat(msg))}</pre>
     * (Method {@link #merge merge()} is often simpler to use for such purposes.)
     *
     * <p>If the remapping function returns {@code null}, the mapping is removed
     * (or remains absent if initially absent).  If the remapping function
     * itself throws an (unchecked) exception, the exception is rethrown, and
     * the current mapping is left unchanged.
     *
     * <p>The remapping function should not modify this map during computation.
     *
     * @param key               key with which the specified value is to be associated
     * @param remappingFunction the remapping function to compute a value
     * @return the new value associated with the specified key, or null if none
     * @throws NullPointerException          if the specified key is null and
     *                                       this map does not support null keys, or the
     *                                       remappingFunction is null
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @implSpec The default implementation is equivalent to performing the following
     * steps for this {@code map}:
     *
     * <pre> {@code
     * V oldValue = map.get(key);
     * V newValue = remappingFunction.apply(key, oldValue);
     * if (newValue != null) {
     *     map.put(key, newValue);
     * } else if (oldValue != null || map.containsKey(key)) {
     *     map.remove(key);
     * }
     * return newValue;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about detecting if the
     * remapping function modifies this map during computation and, if
     * appropriate, reporting an error. Non-concurrent implementations should
     * override this method and, on a best-effort basis, throw a
     * {@code ConcurrentModificationException} if it is detected that the
     * remapping function modifies this map during computation. Concurrent
     * implementations should override this method and, on a best-effort basis,
     * throw an {@code IllegalStateException} if it is detected that the
     * remapping function modifies this map during computation and as a result
     * computation would never complete.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties. In particular, all implementations of
     * subinterface {@link ConcurrentMap} must document
     * whether the remapping function is applied once atomically only if the
     * value is not present.
     * @since 1.8
     */
    @Override
    public V compute(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Map.super.compute(key, remappingFunction);
    }

    /**
     * If the specified key is not already associated with a value or is
     * associated with null, associates it with the given non-null value.
     * Otherwise, replaces the associated value with the results of the given
     * remapping function, or removes if the result is {@code null}. This
     * method may be of use when combining multiple mapped values for a key.
     * For example, to either create or append a {@code String msg} to a
     * value mapping:
     *
     * <pre> {@code
     * map.merge(key, msg, String::concat)
     * }</pre>
     *
     * <p>If the remapping function returns {@code null}, the mapping is removed.
     * If the remapping function itself throws an (unchecked) exception, the
     * exception is rethrown, and the current mapping is left unchanged.
     *
     * <p>The remapping function should not modify this map during computation.
     *
     * @param key               key with which the resulting value is to be associated
     * @param value             the non-null value to be merged with the existing value
     *                          associated with the key or, if no existing value or a null value
     *                          is associated with the key, to be associated with the key
     * @param remappingFunction the remapping function to recompute a value if
     *                          present
     * @return the new value associated with the specified key, or null if no
     * value is associated with the key
     * @throws UnsupportedOperationException if the {@code put} operation
     *                                       is not supported by this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     *                                       ({@linkplain Collection##optional-restrictions optional})
     * @throws NullPointerException          if the specified key is null and this map
     *                                       does not support null keys or the value or remappingFunction is
     *                                       null
     * @implSpec The default implementation is equivalent to performing the following
     * steps for this {@code map}, then returning the current value or
     * {@code null} if absent:
     *
     * <pre> {@code
     * V oldValue = map.get(key);
     * V newValue = (oldValue == null) ? value :
     *              remappingFunction.apply(oldValue, value);
     * if (newValue == null)
     *     map.remove(key);
     * else
     *     map.put(key, newValue);
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about detecting if the
     * remapping function modifies this map during computation and, if
     * appropriate, reporting an error. Non-concurrent implementations should
     * override this method and, on a best-effort basis, throw a
     * {@code ConcurrentModificationException} if it is detected that the
     * remapping function modifies this map during computation. Concurrent
     * implementations should override this method and, on a best-effort basis,
     * throw an {@code IllegalStateException} if it is detected that the
     * remapping function modifies this map during computation and as a result
     * computation would never complete.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties. In particular, all implementations of
     * subinterface {@link ConcurrentMap} must document
     * whether the remapping function is applied once atomically only if the
     * value is not present.
     * @since 1.8
     */
    @Override
    public V merge(K key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return Map.super.merge(key, value, remappingFunction);
    }


    private class TimedValue {
        private final K key;
        private V value;
        private long lastAccessTime;
        private ScheduledFuture<?> task;

        TimedValue(K key, V value) {
            this.key = key;
            this.value = value;
            updateLastAccessTime();
            updateRemoveTask();
        }

        void updateLastAccessTime() {
            synchronized (this) {
                this.lastAccessTime = System.currentTimeMillis();
            }
        }

        void updateRemoveTask() {
            synchronized (this) {
                if (task != null) {
                    task.cancel(false);
                }
                task = scheduler.schedule(this::expired, expiryDuration, TimeUnit.MILLISECONDS);
            }
        }

        void expired() {
            synchronized (this) {
                if (System.currentTimeMillis() - lastAccessTime > expiryDuration) {
                    ExpiringSynchronizedMap.this.expired(this);
                }
            }
        }

        void close() {
            synchronized (this) {
                if (task != null) {
                    task.cancel(false);
                }
                task = null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimedValue that = (TimedValue) o;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
