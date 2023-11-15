package org.by1337.api.configuration.adapter.impl.primitive;

import org.by1337.api.configuration.adapter.PrimitiveAdapter;
/**
 * A primitive adapter for serializing and deserializing Byte values.
 */
public class AdapterByte implements PrimitiveAdapter<Byte> {

    /**
     * Deserialize a Byte value from a source object.
     *
     * @param src The source object containing the Byte data, which can be a Number or a String.
     * @return The deserialized Byte value.
     */
    @Override
    public Byte deserialize(Object src) {
        if (src instanceof Number number) {
            return number.byteValue();
        }
        return Byte.parseByte(String.valueOf(src));
    }

}
