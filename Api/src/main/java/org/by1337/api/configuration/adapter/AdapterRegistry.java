package org.by1337.api.configuration.adapter;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.by1337.api.configuration.adapter.impl.*;
import org.by1337.api.configuration.adapter.impl.primitive.*;
import org.by1337.api.property.property.*;
import org.by1337.api.world.BLocation;
import org.by1337.api.world.BlockPosition;
import org.by1337.api.world.Vector2D;
import org.by1337.api.configuration.YamlContext;
import org.by1337.api.property.PropertyType;
import org.by1337.api.util.NameKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.UUID;

/**
 * A registry for managing various adapters used for serialization and deserialization.
 */
public class AdapterRegistry {
    private static final HashMap<Class<?>, ClassAdapter<?>> adapters;
    private static final HashMap<Class<?>, PrimitiveAdapter<?>> primitiveAdapters;

    /**
     * Registers a new adapter in the registry.
     *
     * @param adapterClass The class of the adapter to be registered.
     * @param adapter      The adapter instance to be registered.
     * @throws AdapterAlreadyExistsException If the adapter with the same class already exists in the registry.
     */
    @CanIgnoreReturnValue
    public static <T> boolean registerAdapter(Class<T> adapterClass, ClassAdapter<? extends T> adapter) throws AdapterAlreadyExistsException {
        if (hasAdapter(adapterClass)) {
            //throw new AdapterAlreadyExistsException("Adapter with class " + adapterClass.getName() + " already exists in the registry.");
            return false;
        }
        adapters.put(adapterClass, adapter);
        return true;
    }

    /**
     * Unregisters an adapter from the registry based on its class.
     *
     * @param adapterClass The class of the adapter to be unregistered.
     * @throws AdapterNotFoundException If the adapter with the specified class is not found in the registry.
     */
    @CanIgnoreReturnValue
    public static <T> boolean unregisterAdapter(Class<T> adapterClass) throws AdapterNotFoundException {
        if (!hasAdapter(adapterClass)) {
            //throw new AdapterNotFoundException("Adapter with class " + adapterClass.getName() + " not found in the registry.");
            return false;
        }
        adapters.remove(adapterClass);
        return true;
    }

    /**
     * Registers a new primitive adapter in the registry.
     *
     * @param adapterClass The class of the primitive adapter to be registered.
     * @param adapter      The primitive adapter instance to be registered.
     * @throws AdapterAlreadyExistsException If a primitive adapter with the same class already exists in the registry.
     */
    @CanIgnoreReturnValue
    public static <T> boolean registerPrimitiveAdapter(Class<T> adapterClass, PrimitiveAdapter<? extends T> adapter) throws AdapterAlreadyExistsException {
        if (hasPrimitiveAdapter(adapterClass)) {
            //throw new AdapterAlreadyExistsException("Adapter with class " + adapterClass.getName() + " already exists in the registry.");
            return false;
        }
        primitiveAdapters.put(adapterClass, adapter);
        return true;
    }

    /**
     * Unregisters a primitive adapter from the registry based on its class.
     *
     * @param adapterClass The class of the primitive adapter to be unregistered.
     * @throws AdapterNotFoundException If the primitive adapter with the specified class is not found in the registry.
     */
    @CanIgnoreReturnValue
    public static <T> boolean unregisterPrimitiveAdapter(Class<T> adapterClass) throws AdapterNotFoundException {
        if (!hasPrimitiveAdapter(adapterClass)) {
            //throw new AdapterNotFoundException("Primitive adapter with class " + adapterClass.getName() + " not found in the registry.");
            return false;
        }
        primitiveAdapters.remove(adapterClass);
        return true;
    }

    /**
     * Checks if an adapter with the specified class exists in the registry.
     *
     * @param adapterClass The class of the adapter to check for.
     * @return True if the adapter exists in the registry, false otherwise.
     */
    public static boolean hasAdapter(Class<?> adapterClass) {
        return adapters.containsKey(adapterClass);
    }

    /**
     * Checks if a primitive adapter with the specified class exists in the registry.
     *
     * @param adapterClass The class of the primitive adapter to check for.
     * @return True if the primitive adapter exists in the registry, false otherwise.
     */
    public static boolean hasPrimitiveAdapter(Class<?> adapterClass) {
        return primitiveAdapters.containsKey(adapterClass);
    }


    /**
     * Deserialize an object of a specific class from a MemorySection using the appropriate class adapter.
     *
     * @param memorySection The MemorySection to deserialize from.
     * @param clazz         The class to deserialize to.
     * @param <T>           The type of the object to deserialize.
     * @return The deserialized object.
     * @throws AdapterAlreadyExistsException If the class adapter for the specified class doesn't exist in the registry.
     */
    private static <T> T getAs(MemorySection memorySection, Class<T> clazz) {
        if (!hasAdapter(clazz)) {
            throw new AdapterAlreadyExistsException("The adapter with class " + clazz.getName() + " does not exist in the registry.");
        }
        ClassAdapter<T> adapter = (ClassAdapter<T>) adapters.get(clazz);
        return adapter.deserialize(new YamlContext(memorySection));
    }


    /**
     * Deserialize a primitive object of a specific class from a source object using the appropriate primitive adapter.
     *
     * @param src   The source object to deserialize from.
     * @param clazz The class to deserialize to.
     * @param <T>   The type of the primitive object to deserialize.
     * @return The deserialized primitive object.
     * @throws AdapterAlreadyExistsException If the primitive adapter for the specified class doesn't exist in the registry.
     */
    private static <T> T getPrimitiveAs(Object src, Class<T> clazz) {
        if (!hasPrimitiveAdapter(clazz)) {
            throw new AdapterAlreadyExistsException("The adapter with class " + clazz.getName() + " does not exist in the registry.");
        }
        PrimitiveAdapter<T> adapter = (PrimitiveAdapter<T>) primitiveAdapters.get(clazz);

        return adapter.deserialize(src);

    }

    /**
     * Deserialize an object of a specific class from a source object, which can be either a MemorySection or a primitive type.
     *
     * @param src   The source object to deserialize from.
     * @param clazz The class to deserialize to.
     * @param <T>   The type of the object to deserialize.
     * @return The deserialized object.
     * @throws IllegalStateException If no suitable adapter is found for the specified class.
     */
    @Contract("null, _ -> null")
    public static <T> T getAs(@Nullable Object src, @NotNull Class<T> clazz) {
        if (src == null) return null;
        if (clazz.isAssignableFrom(src.getClass())) {
            return clazz.cast(src);
        }
        if (!hasPrimitiveAdapter(clazz)) {
            if (!hasAdapter(clazz)) {
                throw new IllegalStateException();
            } else {
                MemorySection section = YamlContext.getMemorySection(src);
                return getAs(section, clazz);
            }
        } else {
            return getPrimitiveAs(src, clazz);
        }
    }


    /**
     * Serialize an object of a specific class using the appropriate adapter.
     *
     * @param src The source object to serialize.
     * @param <T> The type of the object to serialize.
     * @return The serialized object.
     */
    public static <T> Object serialize(@NotNull T src) {
        if (!hasPrimitiveAdapter(src.getClass())) {
            if (!hasAdapter(src.getClass())) {
                return src;
            } else {
                ClassAdapter<T> classAdapter = (ClassAdapter<T>) adapters.get(src.getClass());
                return classAdapter.serialize(src, new YamlContext(new YamlConfiguration()));
            }
        } else {
            PrimitiveAdapter<T> adapter = (PrimitiveAdapter<T>) primitiveAdapters.get(src.getClass());
            return adapter.serialize(src);
        }
    }

    /**
     * Custom exception class for cases where an adapter with the same class already exists in the registry.
     */
    public static class AdapterAlreadyExistsException extends IllegalArgumentException {
        /**
         * Constructs an AdapterAlreadyExistsException with the specified detail message.
         *
         * @param message The detail message explaining the reason for the exception.
         */
        public AdapterAlreadyExistsException(String message) {
            super(message);
        }
    }

    /**
     * Custom exception class for cases where an adapter with the specified class is not found in the registry.
     */
    public static class AdapterNotFoundException extends IllegalArgumentException {
        /**
         * Constructs an AdapterNotFoundException with the specified detail message.
         *
         * @param message The detail message explaining the reason for the exception.
         */
        public AdapterNotFoundException(String message) {
            super(message);
        }
    }


    static {
        adapters = new HashMap<>();
        primitiveAdapters = new HashMap<>();

        registerPrimitiveAdapter(Byte.class, new AdapterByte());
        registerPrimitiveAdapter(Double.class, new AdapterDouble());
        registerPrimitiveAdapter(Float.class, new AdapterFloat());
        registerPrimitiveAdapter(Integer.class, new AdapterInteger());
        registerPrimitiveAdapter(Long.class, new AdapterLong());
        registerPrimitiveAdapter(Short.class, new AdapterShort());
        registerPrimitiveAdapter(NameKey.class, new AdapterNameKey());
        registerPrimitiveAdapter(Boolean.class, new AdapterBoolean());
        registerPrimitiveAdapter(Color.class, new AdapterColor());

        registerPrimitiveAdapter(PropertyType.class, new AdapterPropertyType());

        registerPrimitiveAdapter(String.class, String::valueOf);
        registerPrimitiveAdapter(Object.class, obj -> obj);
        registerPrimitiveAdapter(Class.class, new AdapterClass());
        registerPrimitiveAdapter(UUID.class, new AdapterUUID());
        registerPrimitiveAdapter(Biome.class, new AdapterEnum<>(Biome.class));
        registerPrimitiveAdapter(Material.class, new AdapterEnum<>(Material.class));
        registerPrimitiveAdapter(Particle.class, new AdapterEnum<>(Particle.class));
        registerPrimitiveAdapter(ItemFlag.class, new AdapterEnum<>(ItemFlag.class));

        registerAdapter(ItemStack.class, new AdapterItemStack());

        registerAdapter(BLocation.class, new AdapterBLocation());
        registerAdapter(Vector.class, new AdapterVector());
        registerAdapter(BlockPosition.class, new AdapterBlockPosition());
        registerAdapter(Vector2D.class, new AdapterVector2D());
        registerAdapter(PropertyColor.class, new AdapterProperty<>());
        registerAdapter(PropertyBoolean.class, new AdapterProperty<>());
        registerAdapter(PropertyDouble.class, new AdapterProperty<>());
        registerAdapter(PropertyInteger.class, new AdapterProperty<>());
        registerAdapter(PropertyList.class, new AdapterProperty<>());
        registerAdapter(PropertyLocation.class, new AdapterProperty<>());
        registerAdapter(PropertyLong.class, new AdapterProperty<>());
        registerAdapter(PropertyMaterial.class, new AdapterProperty<>());
        registerAdapter(PropertyParticle.class, new AdapterProperty<>());
        registerAdapter(PropertyString.class, new AdapterProperty<>());
        registerAdapter(PropertyVector.class, new AdapterProperty<>());
        registerAdapter(Property.class, new AdapterProperty<>());
    }
}
