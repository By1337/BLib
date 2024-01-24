package org.by1337.blib.property;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.by1337.blib.property.property.*;
import org.by1337.blib.world.BLocation;
import org.by1337.blib.util.NameKey;
import org.by1337.blib.util.Named;
import org.by1337.blib.property.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

/**
 * A class that represents the type of a property along with its key and associated properties.
 *
 * @param <T> The generic type of the property.
 */
public record PropertyType<T>(@NotNull NameKey key, @NotNull Class<? extends T> innerClass,
                              @NotNull PropertySuppler<? extends Property<T>, T> supplier) implements Named {
    private static final HashMap<NameKey, PropertyType<?>> byKey = new HashMap<>();

    // Predefined PropertyType instances for common data types
    public static final PropertyType<Vector> VECTOR = register(new PropertyType<>(new NameKey("vector"), Vector.class, PropertyVector::new));
    public static final PropertyType<String> STRING = register(new PropertyType<>(new NameKey("string"), String.class, PropertyString::new));
    public static final PropertyType<Particle> PARTICLE = register(new PropertyType<>(new NameKey("particle"), Particle.class, PropertyParticle::new));
    public static final PropertyType<Long> LONG = register(new PropertyType<>(new NameKey("long"), Long.class, PropertyLong::new));
    public static final PropertyType<Integer> INTEGER = register(new PropertyType<>(new NameKey("integer"), Integer.class, PropertyInteger::new));
    public static final PropertyType<Double> DOUBLE = register(new PropertyType<>(new NameKey("double"), Double.class, PropertyDouble::new));
    public static final PropertyType<Color> COLOR = register(new PropertyType<>(new NameKey("color"), Color.class, PropertyColor::new));
    public static final PropertyType<Boolean> BOOLEAN = register(new PropertyType<>(new NameKey("boolean"), Boolean.class, PropertyBoolean::new));
    public static final PropertyType<List> LIST = register(new PropertyType<>(new NameKey("list"), List.class, value -> {
        throw new IllegalStateException();
    }));
    public static final PropertyType<BLocation> LOCATION = register(new PropertyType<>(new NameKey("location"), BLocation.class, PropertyLocation::new));
    public static final PropertyType<Material> MATERIAL = register(new PropertyType<>(new NameKey("material"), Material.class, PropertyMaterial::new));


    /**
     * Get a PropertyType instance by its key.
     *
     * @param key The key associated with the PropertyType.
     * @return The corresponding PropertyType, or null if not found.
     */
    @Nullable
    public static PropertyType<?> getByKey(NameKey key) {
        return byKey.getOrDefault(key, null);
    }

    /**
     * Register a PropertyType and associate it with a key.
     *
     * @param type The PropertyType to register.
     * @param <T>  The type of the PropertyType.
     * @return The registered PropertyType.
     */
    public static <T extends PropertyType<?>> T register(T type) {
        if (byKey.containsKey(type.key())) {
            throw new IllegalArgumentException("Cannot set already-set type: " + type.getName().getName());
        }
        byKey.put(type.getName(), type);
        return type;
    }

    /**
     * Check if a specific PropertyType is registered.
     *
     * @param propertyType The PropertyType to check.
     * @return true if the PropertyType is registered, false otherwise.
     */
    public static boolean has(PropertyType<?> propertyType) {
        return byKey.containsKey(propertyType.key);
    }

    /**
     * Unregister a PropertyType.
     *
     * @param propertyType The PropertyType to unregister.
     */
    public static void unregister(PropertyType<?> propertyType) {
        byKey.remove(propertyType.key);
    }

    @Override
    public @NotNull NameKey getName() {
        return key;
    }

    /**
     * An interface for supplying properties of a specific type.
     *
     * @param <T> The type of the Property.
     * @param <E> The type of the property's value.
     */
    public interface PropertySuppler<T extends Property<?>, E> {
        T get(E value);
    }
}