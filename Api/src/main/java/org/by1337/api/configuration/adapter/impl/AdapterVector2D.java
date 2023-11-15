package org.by1337.api.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.by1337.api.configuration.YamlContext;
import org.by1337.api.world.Vector2D;
import org.by1337.api.configuration.adapter.ClassAdapter;

/**
 * A class adapter for serializing and deserializing Vector2D objects.
 */
public class AdapterVector2D implements ClassAdapter<Vector2D> {
    /**
     * Serialize a Vector2D object to a ConfigurationSection in the specified YamlContext.
     *
     * @param obj     The Vector2D object to be serialized.
     * @param context The YamlContext in which to serialize the object.
     * @return The serialized ConfigurationSection containing Vector2D data.
     */
    @Override
    public ConfigurationSection serialize(Vector2D obj, YamlContext context) {
        context.set("x", obj.getX());
        context.set("z", obj.getZ());
        return context.getHandle();
    }

    /**
     * Deserialize a Vector2D object from the specified YamlContext.
     *
     * @param context The YamlContext containing the Vector2D data.
     * @return The deserialized Vector2D object.
     */
    @Override
    public Vector2D deserialize(YamlContext context) {
        return new Vector2D(
                context.getAsDouble("x"),
                context.getAsDouble("z")
        );
    }
}
