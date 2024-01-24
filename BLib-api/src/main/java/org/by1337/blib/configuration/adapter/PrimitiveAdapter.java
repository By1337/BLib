package org.by1337.blib.configuration.adapter;

/**
 * An interface for defining primitive adapters that can serialize and deserialize objects of a specific primitive type.
 *
 * @param <T> The primitive type of object to be serialized and deserialized.
 */
public interface PrimitiveAdapter<T> {
    /**
     * Serialize a primitive object of type T to a target format or representation.
     *
     * @param obj The primitive object to be serialized.
     * @return The serialized primitive object in the target format.
     */
    default Object serialize(T obj) {
        return obj;
    }

    /**
     * Deserialize a primitive object of type T from a source object.
     *
     * @param src The source object containing the primitive data.
     * @return The deserialized primitive object of type T.
     */
    T deserialize(Object src);

}
