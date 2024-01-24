package org.by1337.blib.configuration.adapter;

import org.by1337.blib.configuration.YamlContext;

/**
 * An interface for defining adapters that can serialize and deserialize objects of a specific type.
 *
 * @param <T> The type of object to be serialized and deserialized.
 * @param <E> The target format or representation to serialize the object to.
 */
public interface Adapter<T, E> {
    /**
     * Serialize an object of type T to a target format or representation of type E.
     *
     * @param obj     The object to be serialized.
     * @param context A context for customizing the serialization process (may be null).
     * @return The serialized object in the target format.
     */
    E serialize(T obj, YamlContext context);

    /**
     * Deserialize an object of type T from a context, typically containing serialized data.
     *
     * @param context A context containing data to deserialize the object from.
     * @return The deserialized object of type T.
     */
    T deserialize(YamlContext context);

}
