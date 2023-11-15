package org.by1337.api.configuration.adapter.impl.primitive;

import org.by1337.api.configuration.adapter.AdapterRegistry;
import org.by1337.api.configuration.adapter.PrimitiveAdapter;
import org.by1337.api.property.PropertyType;
import org.by1337.api.util.NameKey;

/**
 * A primitive adapter for serializing and deserializing PropertyType objects.
 */
public class AdapterPropertyType implements PrimitiveAdapter<PropertyType<?>> {
    /**
     * Serialize a PropertyType object to its name.
     *
     * @param obj The PropertyType object to be serialized.
     * @return The name of the PropertyType.
     */
    @Override
    public Object serialize(PropertyType<?> obj) {
        return AdapterRegistry.serialize(obj.getName());
    }

    /**
     * Deserialize a PropertyType object from its name.
     *
     * @param src The name of the PropertyType to be deserialized.
     * @return The deserialized PropertyType object.
     */
    @Override
    public PropertyType<?> deserialize(Object src) {
        // Deserialize the name from the source and retrieve the corresponding PropertyType.
        return PropertyType.getByKey(AdapterRegistry.getAs(src, NameKey.class));
    }
}
