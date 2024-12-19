package org.by1337.blib.util.collection;

import org.by1337.blib.text.MessageFormatter;
import org.by1337.blib.util.NameKey;
import org.by1337.blib.util.SpacedNameKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

/**
 * A registry that allows storage and retrieval of values using
 * {@link SpacedNameKey} as well as {@link NameKey}.
 *
 * @param <T> the type of values stored in the registry
 */
public class SpacedNameRegistry<T> {
    private static final Set<SpacedNameKey> EMPTY_SET = new HashSet<>();
    private final Map<SpacedNameKey, T> lookupBySpacedName;
    private final Map<NameKey, Set<SpacedNameKey>> lookupSpacedNameByName;
    private final Map<NameKey, Set<SpacedNameKey>> lookupSpacedNameBySpace;

    /**
     * Constructs a new {@link SpacedNameRegistry} using a default
     * {@link HashMap} for storage.
     */
    public SpacedNameRegistry() {
        this(HashMap::new);
    }

    /**
     * Constructs a new {@link SpacedNameRegistry} using a provided
     * {@link Supplier} to create the underlying maps.
     *
     * @param mapCreator a supplier that creates a new map instance
     */
    @SuppressWarnings("unchecked")
    public SpacedNameRegistry(Supplier<Map<?, T>> mapCreator) {
        lookupBySpacedName = (Map<SpacedNameKey, T>) mapCreator.get();
        lookupSpacedNameByName = (Map<NameKey, Set<SpacedNameKey>>) mapCreator.get();
        lookupSpacedNameBySpace = (Map<NameKey, Set<SpacedNameKey>>) mapCreator.get();
    }

    /**
     * Puts a value associated with the specified {@link SpacedNameKey}.
     * If the value is {@code null}, it will remove the key from the registry.
     *
     * @param key   the {@link SpacedNameKey} to associate with the value
     * @param value the value to be stored, or {@code null} to remove the key
     * @return the previous value associated with the key, or {@code null} if there was none
     */
    @Nullable
    public T put(SpacedNameKey key, @Nullable T value) {
        if (value == null){
           return remove(key);
        }
        var old = lookupBySpacedName.put(key, value);
        lookupSpacedNameByName.computeIfAbsent(key.getName(), k -> new HashSet<>()).add(key);
        lookupSpacedNameBySpace.computeIfAbsent(key.getSpace(), k -> new HashSet<>()).add(key);
        return old;
    }

    public boolean has(SpacedNameKey key) {
        return lookupBySpacedName.containsKey(key);
    }

    /**
     * Removes the value associated with the specified {@link SpacedNameKey}.
     *
     * @param key the {@link SpacedNameKey} to remove
     * @return the previous value associated with the key, or {@code null} if there was none
     */
    @Nullable
    public T remove(SpacedNameKey key) {
        var old = lookupBySpacedName.remove(key);
        lookupSpacedNameByName.getOrDefault(key.getName(), EMPTY_SET).remove(key);
        lookupSpacedNameBySpace.getOrDefault(key.getSpace(), EMPTY_SET).remove(key);
        return old;
    }

    /**
     * Removes all values associated with the specified {@link NameKey}.
     *
     * @param key the {@link NameKey} to remove values for
     * @return a collection of removed values
     */
    public Collection<T> removeAllWithName(NameKey key) {
        return new ArrayList<>(removeAll(Set.copyOf(lookupSpacedNameByName.getOrDefault(key, Collections.emptySet()))));
    }

    /**
     * Removes all values associated with the specified {@link NameKey} for space.
     *
     * @param key the {@link NameKey} to remove values for
     * @return a collection of removed values
     */
    public Collection<T> removeAllWithSpace(NameKey key) {
        return new ArrayList<>(removeAll(Set.copyOf(lookupSpacedNameBySpace.getOrDefault(key, Collections.emptySet()))));
    }

    /**
     * Removes all values associated with the specified collection of
     * {@link SpacedNameKey}.
     *
     * @param keyList a collection of {@link SpacedNameKey} to remove
     * @return a collection of removed values
     */
    public Collection<T> removeAll(Collection<SpacedNameKey> keyList) {
        List<T> result = new ArrayList<>();
        for (SpacedNameKey spacedNameKey : keyList) {
            var t = remove(spacedNameKey);
            if (t != null) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Retrieves the value associated with the specified {@link SpacedNameKey}.
     *
     * @param key the {@link SpacedNameKey} to look up
     * @return the associated value, or {@code null} if there is none
     */
    @Nullable
    public T get(SpacedNameKey key) {
        return lookupBySpacedName.get(key);
    }

    /**
     * Finds the value associated with the specified {@link SpacedNameKey}.
     * If not found, it looks up by {@link NameKey} in the registry.
     *
     * @param key the {@link SpacedNameKey} to find
     * @return the associated value, or {@code null} if there is none
     */
    @Nullable
    public T find(SpacedNameKey key) {
        T t = get(key);
        return t == null ? lookupByName(key.getName()) : t;
    }

    /**
     * Looks up a value using the specified {@link NameKey}.
     *
     * @param key the {@link NameKey} to look up
     * @return the associated value, or {@code null} if there is none
     * @throws IllegalArgumentException if the result is ambiguous
     */
    @Nullable
    public T lookupByName(NameKey key) {
        return find(lookupSpacedNameByName.get(key), key);
    }

    /**
     * Looks up a value using the specified {@link NameKey} for space.
     *
     * @param key the {@link NameKey} to look up
     * @return the associated value, or {@code null} if there is none
     * @throws IllegalArgumentException if the result is ambiguous
     */
    @Nullable
    public T lookupBySpace(NameKey key) {
        return find(lookupSpacedNameBySpace.get(key), key);
    }

    /**
     * Finds a value from a set of {@link SpacedNameKey}.
     *
     * @param set the set of {@link SpacedNameKey} to search
     * @param key the original {@link NameKey} used for lookup
     * @return the associated value, or {@code null} if there is none
     * @throws IllegalArgumentException if the result is ambiguous
     */
    private T find(@Nullable Set<SpacedNameKey> set, NameKey key) {
        if (set == null || set.isEmpty()) return null;
        if (set.size() == 1) return get(set.iterator().next());
        throw new IllegalArgumentException(MessageFormatter.apply("Not an unambiguous result for the name {}! All possible values of {}", key.getName(), set));
    }

    /**
     * Retrieves all keys associated with the specified {@link NameKey} for space.
     *
     * @param key the {@link NameKey} to look up
     * @return a set of associated {@link SpacedNameKey}
     */
    @NotNull
    public Set<SpacedNameKey> getAllKeysBySpace(NameKey key) {
        return Set.copyOf(Objects.requireNonNullElse(lookupSpacedNameBySpace.get(key), Collections.emptySet()));
    }

    /**
     * Retrieves all keys associated with the specified {@link NameKey}.
     *
     * @param key the {@link NameKey} to look up
     * @return a set of associated {@link SpacedNameKey}
     */
    @NotNull
    public Set<SpacedNameKey> getAllKeysByName(NameKey key) {
        return Set.copyOf(Objects.requireNonNullElse(lookupSpacedNameByName.get(key), Collections.emptySet()));
    }

    /**
     * Returns an unmodifiable collection of all values in the registry.
     *
     * @return a collection of values
     */
    @NotNull
    public Collection<T> values() {
        return Collections.unmodifiableCollection(lookupBySpacedName.values());
    }

    @NotNull
    public Collection<SpacedNameKey> keySet(){
        return Collections.unmodifiableCollection(lookupBySpacedName.keySet());
    }
    public int size(){
        return lookupBySpacedName.size();
    }
}
