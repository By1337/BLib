package org.by1337.api.configuration.adapter.impl.primitive;

import org.by1337.api.configuration.adapter.PrimitiveAdapter;

/**
 * A generic adapter for serializing and deserializing Enum objects.
 *
 * @param <T> The type of Enum to be serialized and deserialized.
 */
public class AdapterEnum<T extends Enum<T>> implements PrimitiveAdapter<T> {
    private final Class<T> clazz;

    /**
     * Creates a new instance of the AdapterEnum.
     *
     * @param clazz The Enum class type this adapter is designed for.
     */
    public AdapterEnum(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Serializes an Enum object to its name as a String.
     *
     * @param obj The Enum object to serialize.
     * @return The name of the Enum as a String.
     */
    @Override
    public Object serialize(T obj) {
        return obj.name();
    }

    /**
     * Deserializes an Enum by its name as a String.
     *
     * @param src The name of the Enum as a String.
     * @return The deserialized Enum object.
     */
    @Override
    public T deserialize(Object src) {
        return Enum.valueOf(clazz, String.valueOf(src));
    }
}
