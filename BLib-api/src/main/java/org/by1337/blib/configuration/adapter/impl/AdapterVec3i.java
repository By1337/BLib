package org.by1337.blib.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.adapter.ClassAdapter;
import org.by1337.blib.geom.Vec3i;

/**
 * A class adapter for serializing and deserializing Vec3i objects.
 */
public class AdapterVec3i implements ClassAdapter<Vec3i> {
    /**
     * Serialize a Vec3i object to a ConfigurationSection in the specified YamlContext.
     *
     * @param obj     The Vec3i object to be serialized.
     * @param context The YamlContext in which to serialize the object.
     * @return The serialized ConfigurationSection containing Vec3i data.
     */
    @Override
    public ConfigurationSection serialize(Vec3i obj, YamlContext context) {
        context.set("x", obj.getX());
        context.set("y", obj.getY());
        context.set("z", obj.getZ());
        return context.getHandle();
    }

    /**
     * Deserialize a Vec3i object from the specified YamlContext.
     *
     * @param context The YamlContext containing the Vec3i data.
     * @return The deserialized Vec3i object.
     */
    @Override
    public Vec3i deserialize(YamlContext context) {
        return new Vec3i(
                context.getAsInteger("x"),
                context.getAsInteger("y"),
                context.getAsInteger("z")
        );
    }
}
