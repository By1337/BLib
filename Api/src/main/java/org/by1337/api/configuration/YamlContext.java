package org.by1337.api.configuration;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.by1337.api.configuration.adapter.AdapterRegistry;
import org.by1337.api.world.BLocation;
import org.by1337.api.property.PropertyType;
import org.by1337.api.util.NameKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A class for providing convenient access to values within a MemorySection, using AdapterRegistry for type conversion.
 */
public class YamlContext {
    private final MemorySection section;

    /**
     * Constructs a YamlContext using the provided MemorySection.
     *
     * @param section The MemorySection containing YAML data to be accessed.
     */
    public YamlContext(MemorySection section) {
        this.section = section;
    }

    public Byte getAsByte(String path) {
        return AdapterRegistry.getAs(section.get(path), Byte.class);
    }

    /**
     * Get the value at the specified path as a Byte, with optional default value.
     *
     * @param path The path to the value within the MemorySection.
     * @param def  The default value to be returned if the path does not exist or the value cannot be converted to a Byte.
     * @return The value as a Byte, or the default value if the path does not exist or conversion fails.
     */
    public Byte getAsByte(String path, Byte def) {
        if (!section.contains(path)) return def;
        return getAsByte(path);
    }

    public Double getAsDouble(String path) {
        return AdapterRegistry.getAs(section.get(path), Double.class);
    }

    public Double getAsDouble(String path, Double def) {
        if (!section.contains(path)) return def;
        return getAsDouble(path);
    }

    public Float getAsFloat(String path) {
        return AdapterRegistry.getAs(section.get(path), Float.class);
    }

    public Float getAsFloat(String path, Float def) {
        if (!section.contains(path)) return def;
        return getAsFloat(path);
    }


    public Integer getAsInteger(String path) {
        return AdapterRegistry.getAs(section.get(path), Integer.class);
    }

    public Integer getAsInteger(String path, Integer def) {
        if (!section.contains(path)) return def;
        return getAsInteger(path);
    }


    public Long getAsLong(String path) {
        return AdapterRegistry.getAs(section.get(path), Long.class);
    }

    public Long getAsLong(String path, Long def) {
        if (!section.contains(path)) return def;
        return getAsLong(path);
    }


    public Boolean getAsBoolean(String path) {
        return AdapterRegistry.getAs(section.get(path), Boolean.class);
    }

    public Boolean getAsBoolean(String path, Boolean def) {
        if (!section.contains(path)) return def;
        return getAsBoolean(path);
    }

    public Class<?> getAsClass(String path) {
        return AdapterRegistry.getAs(section.get(path), Class.class);
    }

    public Class<?> getAsClass(String path, Class<?> def) {
        if (!section.contains(path)) return def;
        return getAsClass(path);
    }

    public Short getAsShort(String path) {
        return AdapterRegistry.getAs(section.get(path), Short.class);
    }

    public Short getAsShort(String path, Short def) {
        if (!section.contains(path)) return def;
        return getAsShort(path);
    }

    public NameKey getAsNameKey(String path) {
        return AdapterRegistry.getAs(section.get(path), NameKey.class);
    }

    public NameKey getAsNameKey(String path, NameKey def) {
        if (!section.contains(path)) return def;
        return getAsNameKey(path);
    }

    public Material getAsMaterial(String path) {
        return AdapterRegistry.getAs(section.get(path), Material.class);
    }

    public Material getAsMaterial(String path, Material def) {
        if (!section.contains(path)) return def;
        return getAsMaterial(path);
    }

    public Particle getAsParticle(String path) {
        return AdapterRegistry.getAs(section.get(path), Particle.class);
    }

    public Particle getAsParticle(String path, Particle def) {
        if (!section.contains(path)) return def;
        return getAsParticle(path);
    }


    public PropertyType<?> getAsPropertyType(String path) {
        return AdapterRegistry.getAs(section.get(path), PropertyType.class);
    }

    public PropertyType<?> getAsPropertyType(String path, PropertyType<?> def) {
        if (!section.contains(path)) return def;
        return getAsPropertyType(path);
    }

    public <T> T getAs(String path, Class<T> clazz) {
        return AdapterRegistry.getAs(section.get(path), clazz);
    }

    public <T> T getAs(String path, Class<T> clazz, T def) {
        if (!section.contains(path)) {
            return def;
        }
        return getAs(path, clazz);
    }

    public String getAsString(String path) {
        return section.getString(path);
    }

    public String getAsString(String path, String def) {
        if (!section.contains(path)) return def;
        return getAsString(path);
    }


    public ItemStack getAsItemStack(String path) {
        return AdapterRegistry.getAs((MemorySection) section.get(path), ItemStack.class);
    }

    public ItemStack getAsItemStack(String path, ItemStack def) {
        if (!section.contains(path)) return def;
        return getAsItemStack(path);
    }


    public BLocation getAsBLocation(String path) {
        return AdapterRegistry.getAs((MemorySection) section.get(path), BLocation.class);
    }

    public BLocation getAsBLocation(String path, BLocation def) {
        if (!section.contains(path)) return def;
        return getAsBLocation(path);
    }

    /**
     * Sets a value in the underlying MemorySection using the provided path and object, serializing the object if necessary.
     *
     * @param path The path to the value within the MemorySection.
     * @param obj  The object to be stored in the MemorySection.
     */
    public void set(@NotNull String path, @Nullable Object obj) {
        if (obj == null) {
            section.set(path, null);
        } else {
            section.set(path, serialize(obj));
        }
    }

    /**
     * Serialize an object for storage in a MemorySection, handling arrays, lists, maps, and other types.
     *
     * @param obj The object to be serialized.
     * @return The serialized object, suitable for storage in a MemorySection.
     */
    public static Object serialize(@NotNull Object obj) {
        if (obj instanceof Object[] array) {
            List<Object> out = new ArrayList<>();
            for (Object o : array) {
                out.add(serialize(o));
            }
            return out;
        } else if (obj instanceof Set<?> set) {
            List<Object> out = new ArrayList<>();
            for (Object o : set) {
                out.add(serialize(o));
            }
            return out;
        } else if (obj instanceof List<?> list) {
            List<Object> out = new ArrayList<>();
            for (Object o : list) {
                out.add(serialize(o));
            }
            return out;
        } else if (obj instanceof Map<?, ?> map) {
            Map<Object, Object> map1 = new HashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                map1.put(
                        serialize(entry.getKey()), // сериализация ключа для примитивных типов, например NameKey
                        serialize(entry.getValue())
                );
            }
            return map1;
        } else {
            return AdapterRegistry.serialize(obj);
        }
    }

    /**
     * Get a list of values at the specified path, converted to the specified value type, with a default value.
     *
     * @param path      The path to the list within the MemorySection.
     * @param valueType The class type for the list elements.
     * @param def       The default list to be returned if the path does not exist.
     * @return The list of values, or the default list if the path does not exist.
     */
    public <T> List<T> getList(String path, Class<T> valueType, List<T> def) {
        if (!section.contains(path)) return def;
        return getList(path, valueType);
    }


    /**
     * Get a list of values at the specified path, converted to the specified value type.
     *
     * @param path      The path to the list within the MemorySection.
     * @param valueType The class type for the list elements.
     * @return The list of values.
     */
    public <T> List<T> getList(String path, Class<T> valueType) {
        List<?> raw = section.getList(path);
        List<T> out = new ArrayList<>();
        for (Object o : raw) {
            out.add(AdapterRegistry.getAs(o, valueType));
        }
        return out;
    }

    /**
     * Get a map of values at the specified path, converted to the specified value type, with a default value.
     *
     * @param path      The path to the map within the MemorySection.
     * @param valueType The class type for the map values.
     * @param def       The default map to be returned if the path does not exist.
     * @return The map of values, or the default map if the path does not exist.
     */
    public <V> Map<String, V> getMap(String path, Class<V> valueType, Map<String, V> def) {
        if (!section.contains(path)) return def;
        return getMap(path, valueType);
    }

    /**
     * Get a map of values at the specified path, converted to the specified value type.
     *
     * @param path      The path to the map within the MemorySection.
     * @param valueType The class type for the map values.
     * @return The map of values.
     */
    public <V> Map<String, V> getMap(String path, Class<V> valueType) {
        Map<String, ?> map = getMemorySection(section.get(path)).getValues(false);
        Map<String, V> out = new HashMap<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            out.put(entry.getKey(), AdapterRegistry.getAs(entry.getValue(), valueType));
        }
        return out;
    }

    /**
     * Get a map of values at the specified path, with both key and value types specified, and a default value.
     *
     * @param path      The path to the map within the MemorySection.
     * @param valueType The class type for the map values.
     * @param keyType   The class type for the map keys.
     * @param def       The default map to be returned if the path does not exist.
     * @return The map of values, or the default map if the path does not exist.
     */
    public <K, V> Map<K, V> getMap(String path, Class<V> valueType, Class<K> keyType, Map<K, V> def) {
        if (!section.contains(path)) return def;
        return getMap(path, valueType, keyType);
    }

    /**
     * Get a map of values at the specified path, with both key and value types specified.
     *
     * @param path      The path to the map within the MemorySection.
     * @param valueType The class type for the map values.
     * @param keyType   The class type for the map keys.
     * @return The map of values.
     */
    public <K, V> Map<K, V> getMap(String path, Class<V> valueType, Class<K> keyType) {
        Map<String, ?> map = getMemorySection(section.get(path)).getValues(false);
        Map<K, V> out = new HashMap<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            out.put(AdapterRegistry.getAs(entry.getKey(), keyType), AdapterRegistry.getAs(entry.getValue(), valueType));
        }
        return out;
    }

    /**
     * Get a MemorySection from an object, converting maps to sections if necessary.
     *
     * @param o The object to be converted to a MemorySection.
     * @return A MemorySection containing the data from the object.
     */
    public static MemorySection getMemorySection(Object o) {
        if (o instanceof Map<?, ?> map) {
            return (MemorySection) convertMapsToSections(map, new YamlConfiguration());
        }
        return (MemorySection) o;
    }

    /**
     * Convert a map into a ConfigurationSection.
     *
     * @param input   The map to be converted into a ConfigurationSection.
     * @param section The ConfigurationSection to store the converted data.
     * @return The ConfigurationSection containing the converted data.
     */
    public static ConfigurationSection convertMapsToSections(@NotNull Map<?, ?> input, @NotNull ConfigurationSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();

            if (value instanceof Map) {
                convertMapsToSections((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
        return section;
    }


    /**
     * Get the underlying MemorySection.
     *
     * @return The MemorySection that this YamlContext is based on.
     */
    public MemorySection getHandle() {
        return section;
    }
}
