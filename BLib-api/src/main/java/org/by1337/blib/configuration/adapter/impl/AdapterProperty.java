package org.by1337.blib.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.adapter.ClassAdapter;
import org.by1337.blib.property.PropertyType;
import org.by1337.blib.property.property.Property;
import org.by1337.blib.property.property.PropertyList;

/**
 * A class adapter for serializing and deserializing Property objects.
 */
public class AdapterProperty<T extends Property<?>> implements ClassAdapter<T> {

    /**
     * Serialize a Property object to a ConfigurationSection in the specified YamlContext.
     *
     * @param obj     The Property object to be serialized.
     * @param context The YamlContext in which to serialize the object.
     * @return The serialized ConfigurationSection containing Property data.
     */
    @Override
    public ConfigurationSection serialize(T obj, YamlContext context) {
        if (obj instanceof PropertyList<?> propertyList) {
            context.set("item-type", propertyList.getInnerType());
        }
        context.set("type", obj.getType());
        context.set("value", obj.getValue());
        return context.getHandle();
    }

    /**
     * Deserialize a Property object from the specified YamlContext.
     *
     * @param context The YamlContext containing the Property data.
     * @return The deserialized Property object.
     */
    @Override
    public T deserialize(YamlContext context) {
        return deserialize0(context);
    }

    private <E> T deserialize0(YamlContext context) {
        PropertyType<E> propertyType = (PropertyType<E>) context.getAsPropertyType("type");
        if (propertyType != PropertyType.LIST) {
            return (T) propertyType.supplier().get(
                    (E) context.getAs("value", propertyType.innerClass(), null)
            );
        } else {
            Class<E> innerType = (Class<E>) context.getAsClass("item-type");
            return (T) new PropertyList<>(
                    context.getList("value", innerType, null),
                    innerType
            );
        }
    }

}
