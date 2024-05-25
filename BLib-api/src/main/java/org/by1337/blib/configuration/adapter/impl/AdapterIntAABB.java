package org.by1337.blib.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.adapter.ClassAdapter;
import org.by1337.blib.geom.AABB;
import org.by1337.blib.geom.IntAABB;

public class AdapterIntAABB implements ClassAdapter<IntAABB> {
    @Override
    public ConfigurationSection serialize(IntAABB obj, YamlContext context) {
        context.set("minX", obj.getMinX());
        context.set("minY", obj.getMinY());
        context.set("minZ", obj.getMinZ());
        context.set("maxX", obj.getMaxX());
        context.set("maxY", obj.getMaxY());
        context.set("maxZ", obj.getMaxZ());
        return context.getHandle();
    }

    @Override
    public IntAABB deserialize(YamlContext context) {
        return new IntAABB(
                context.getAsInteger("minX"),
                context.getAsInteger("minY"),
                context.getAsInteger("minZ"),
                context.getAsInteger("maxX"),
                context.getAsInteger("maxY"),
                context.getAsInteger("maxZ")
        );
    }
}
