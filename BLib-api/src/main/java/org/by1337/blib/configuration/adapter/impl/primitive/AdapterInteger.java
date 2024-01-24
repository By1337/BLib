package org.by1337.blib.configuration.adapter.impl.primitive;

import org.by1337.blib.configuration.adapter.PrimitiveAdapter;

/**
 * A primitive adapter for serializing and deserializing Integer values.
 */
public class AdapterInteger implements PrimitiveAdapter<Integer> {

    /**
     * Deserialize an Integer value from a source object.
     *
     * @param src The source object containing the Integer data, which can be a Number or a String.
     * @return The deserialized Integer value.
     */
    @Override
    public Integer deserialize(Object src) {
        if (src instanceof Number number){
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(src));
    }
}
