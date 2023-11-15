package org.by1337.api.configuration.adapter.impl.primitive;

import org.by1337.api.configuration.adapter.PrimitiveAdapter;

/**
 * A primitive adapter for serializing and deserializing Float values.
 */
public class AdapterFloat implements PrimitiveAdapter<Float> {
    /**
     * Deserialize a Float value from a source object.
     *
     * @param src The source object containing the Float data, which can be a Number or a String.
     * @return The deserialized Float value.
     */
    @Override
    public Float deserialize(Object src) {
        if (src instanceof Number number) {
            return number.floatValue();
        }
        return Float.parseFloat(String.valueOf(src));
    }
}
