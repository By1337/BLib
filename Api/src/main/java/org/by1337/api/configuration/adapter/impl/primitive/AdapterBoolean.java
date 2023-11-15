package org.by1337.api.configuration.adapter.impl.primitive;

import org.by1337.api.configuration.adapter.PrimitiveAdapter;

/**
 * A primitive adapter for serializing and deserializing Boolean values.
 */
public class AdapterBoolean implements PrimitiveAdapter<Boolean> {
    /**
     * Deserialize a Boolean value from a source object.
     *
     * @param src The source object containing the Boolean data, which can be a Boolean or a String.
     * @return The deserialized Boolean value.
     */
    @Override
    public Boolean deserialize(Object src) {
        if (src instanceof Boolean b) {
            // If the source object is already a Boolean, return it.
            return b;
        }
        // If the source object is a String, parse it to a Boolean.
        return Boolean.parseBoolean(String.valueOf(src));
    }
}
