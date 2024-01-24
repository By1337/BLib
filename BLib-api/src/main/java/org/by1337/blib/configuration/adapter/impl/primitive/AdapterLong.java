package org.by1337.blib.configuration.adapter.impl.primitive;

import org.by1337.blib.configuration.adapter.PrimitiveAdapter;

/**
 * A primitive adapter for serializing and deserializing Long values.
 */
public class AdapterLong implements PrimitiveAdapter<Long> {
    /**
     * Deserialize a Long value from a source object.
     *
     * @param src The source object containing the Long data, which can be a Number or a String.
     * @return The deserialized Long value.
     */
    @Override
    public Long deserialize(Object src) {
        if (src instanceof Number number) {
            // If the source object is already a Number, return its long value.
            return number.longValue();
        }
        // If the source object is a String, parse it to a Long.
        return Long.parseLong(String.valueOf(src));
    }
}
