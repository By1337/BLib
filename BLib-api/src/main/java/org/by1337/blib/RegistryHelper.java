package org.by1337.blib;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class RegistryHelper {
    public static final Registry<RegistryHelper.Holder<PotionEffectType>> MOB_EFFECT = BLib.getApi().getRegistryCreator().createPotionType();


    public static <T extends Keyed> Registry<Holder<T>> of(Iterator<T> iterator) {
        Map<NamespacedKey, Holder<T>> hashMap = new HashMap<>();
        while (iterator.hasNext()) {
            T item = iterator.next();
            hashMap.put(item.getKey(), new Holder<>(item));
        }
        return create(hashMap);
    }

    public static <T, R> Registry<Holder<T>> of(Iterator<R> iterator, Function<R, T> map, Function<R, NamespacedKey> toKey) {
        Map<NamespacedKey, Holder<T>> hashMap = new HashMap<>();
        while (iterator.hasNext()) {
            R v = iterator.next();
            T val = map.apply(v);
            NamespacedKey key = toKey.apply(v);
            hashMap.put(toKey.apply(v), new Holder<>(val, key));
        }
        return create(hashMap);
    }

    private static <T> Registry<Holder<T>> create(Map<NamespacedKey, Holder<T>> hashMap) {
        return new Registry<>() {
            final Map<NamespacedKey, Holder<T>> source = Collections.unmodifiableMap(hashMap);

            @Override
            public @NotNull Iterator<Holder<T>> iterator() {
                return source.values().iterator();
            }

            @Override
            public @Nullable Holder<T> get(@NotNull NamespacedKey key) {
                return source.get(key);
            }
        };
    }

    public static class Holder<T> implements Keyed {
        private final T value;
        private final NamespacedKey key;

        public Holder(T value, NamespacedKey key) {
            this.value = value;
            this.key = key;
        }

        public Holder(T value) {
            this.value = value;
            if (value instanceof Keyed keyed) {
                key = keyed.getKey();
            } else {
                throw new IllegalArgumentException(value + " must be Keyed");
            }
        }

        public T value() {
            return value;
        }

        @Override
        @NotNull
        public NamespacedKey getKey() {
            return key;
        }
    }
}
