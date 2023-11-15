package org.by1337.api.configuration.adapter.impl.primitive;

import org.by1337.api.configuration.adapter.PrimitiveAdapter;

/**
 * A primitive adapter for serializing and deserializing Double values.
 */
public class AdapterDouble implements PrimitiveAdapter<Double> {

    /**
     * Deserialize a Double value from a source object.
     *
     * @param src The source object containing the Double data, which can be a Number or a String.
     * @return The deserialized Double value.
     */
    @Override
    public Double deserialize(Object src) {
        if (src instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(String.valueOf(src));
    }
}
