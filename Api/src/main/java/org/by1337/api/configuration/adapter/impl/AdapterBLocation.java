package org.by1337.api.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.by1337.api.configuration.YamlContext;
import org.by1337.api.world.BLocation;
import org.by1337.api.configuration.adapter.ClassAdapter;

/**
 * A class adapter for serializing and deserializing BLocation objects.
 */
public class AdapterBLocation implements ClassAdapter<BLocation> {
    /**
     * Serialize a BLocation object to a ConfigurationSection in the specified YamlContext.
     *
     * @param obj     The BLocation object to be serialized.
     * @param context The YamlContext in which to serialize the object.
     * @return The serialized ConfigurationSection containing BLocation data.
     */
    @Override
    public ConfigurationSection serialize(BLocation obj, YamlContext context) {
        context.set("world", obj.getWorldName());
        context.set("x", obj.getX());
        context.set("y", obj.getY());
        context.set("z", obj.getZ());
        context.set("yaw", obj.getYaw());
        context.set("pitch", obj.getPitch());
        return context.getHandle();
    }

    /**
     * Deserialize a BLocation object from the specified YamlContext.
     *
     * @param context The YamlContext containing the BLocation data.
     * @return The deserialized BLocation object.
     */
    @Override
    public BLocation deserialize(YamlContext context) {
        String world = context.getAsString("world");
        double x = context.getAsDouble("x");
        double y = context.getAsDouble("y");
        double z = context.getAsDouble("z");
        float yaw = context.getAsFloat("yaw");
        float pitch = context.getAsFloat("pitch");
        return new BLocation(x, y, z, yaw, pitch, world);
    }
}
