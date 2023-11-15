package org.by1337.api.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.util.Vector;
import org.by1337.api.configuration.YamlContext;
import org.by1337.api.configuration.adapter.ClassAdapter;
/**
 * A class adapter for serializing and deserializing Vector objects.
 */
public class AdapterVector implements ClassAdapter<Vector> {
    /**
     * Serialize a Vector object to a ConfigurationSection in the specified YamlContext.
     *
     * @param obj     The Vector object to be serialized.
     * @param context The YamlContext in which to serialize the object.
     * @return The serialized ConfigurationSection containing Vector data.
     */
    @Override
    public ConfigurationSection serialize(Vector obj, YamlContext context) {
        context.set("x", obj.getX());
        context.set("y", obj.getY());
        context.set("z", obj.getZ());
        return context.getHandle();
    }

    /**
     * Deserialize a Vector object from the specified YamlContext.
     *
     * @param context The YamlContext containing the Vector data.
     * @return The deserialized Vector object.
     */
    @Override
    public Vector deserialize(YamlContext context) {
        return new Vector(
                context.getAsDouble("x"),
                context.getAsDouble("y"),
                context.getAsDouble("z")
        );
    }
}
