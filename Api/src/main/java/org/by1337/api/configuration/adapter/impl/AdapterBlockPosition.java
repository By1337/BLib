package org.by1337.api.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;

import org.by1337.api.configuration.YamlContext;
import org.by1337.api.world.BlockPosition;
import org.by1337.api.configuration.adapter.ClassAdapter;
/**
 * A class adapter for serializing and deserializing BlockPosition objects.
 */
public class AdapterBlockPosition implements ClassAdapter<BlockPosition> {
    /**
     * Serialize a BlockPosition object to a ConfigurationSection in the specified YamlContext.
     *
     * @param obj     The BlockPosition object to be serialized.
     * @param context The YamlContext in which to serialize the object.
     * @return The serialized ConfigurationSection containing BlockPosition data.
     */
    @Override
    public ConfigurationSection serialize(BlockPosition obj, YamlContext context) {
        context.set("x", obj.getX());
        context.set("y", obj.getY());
        context.set("z", obj.getZ());
        return context.getHandle();
    }
    /**
     * Deserialize a BlockPosition object from the specified YamlContext.
     *
     * @param context The YamlContext containing the BlockPosition data.
     * @return The deserialized BlockPosition object.
     */
    @Override
    public BlockPosition deserialize(YamlContext context) {
        return new BlockPosition(
                context.getAsInteger("x"),
                context.getAsInteger("y"),
                context.getAsInteger("z")
        );
    }
}