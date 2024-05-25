package org.by1337.blib.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.adapter.ClassAdapter;
import org.by1337.blib.geom.Vec3d;

/**
 * A class adapter for serializing and deserializing Vec3d objects.
 */
public class AdapterVec3d implements ClassAdapter<Vec3d> {
    /**
     * Serialize a Vec3d object to a ConfigurationSection in the specified YamlContext.
     *
     * @param obj     The Vec3d object to be serialized.
     * @param context The YamlContext in which to serialize the object.
     * @return The serialized ConfigurationSection containing Vec3d data.
     */
    @Override
    public ConfigurationSection serialize(Vec3d obj, YamlContext context) {
        context.set("x", obj.getX());
        context.set("y", obj.getY());
        context.set("z", obj.getZ());
        return context.getHandle();
    }

    /**
     * Deserialize a Vec3d object from the specified YamlContext.
     *
     * @param context The YamlContext containing the Vec3d data.
     * @return The deserialized Vec3d object.
     */
    @Override
    public Vec3d deserialize(YamlContext context) {
        return new Vec3d(
                context.getAsDouble("x"),
                context.getAsDouble("y"),
                context.getAsDouble("z")
        );
    }
}
