package org.by1337.api.configuration.adapter.impl.primitive;

import org.by1337.api.configuration.adapter.PrimitiveAdapter;
import org.by1337.api.util.NameKey;

/**
 * A primitive adapter for serializing and deserializing NameKey objects.
 */
public class AdapterNameKey implements PrimitiveAdapter<NameKey> {
    /**
     * Serialize a NameKey object to its name.
     *
     * @param obj The NameKey object to be serialized.
     * @return The name of the NameKey.
     */
    @Override
    public Object serialize(NameKey obj) {
        return obj.getName();
    }

    /**
     * Deserialize a NameKey object from its name.
     *
     * @param src The name of the NameKey to be deserialized.
     * @return The deserialized NameKey object.
     */
    @Override
    public NameKey deserialize(Object src) {
        return new NameKey(String.valueOf(src));
    }
}
