package org.by1337.api.configuration.adapter.impl.primitive;

import org.by1337.api.configuration.adapter.PrimitiveAdapter;

/**
 * A primitive adapter for serializing and deserializing Short values.
 */
public class AdapterShort implements PrimitiveAdapter<Short> {
    /**
     * Deserialize a Short value from a source object.
     *
     * @param src The source object containing the Short data, which can be a Number or a String.
     * @return The deserialized Short value.
     */
    @Override
    public Short deserialize(Object src) {
        if (src instanceof Number number) {
            // If the source object is already a Number, return its short value.
            return number.shortValue();
        }
        // If the source object is a String, parse it to a Short.
        return Short.parseShort(String.valueOf(src));
    }
}
