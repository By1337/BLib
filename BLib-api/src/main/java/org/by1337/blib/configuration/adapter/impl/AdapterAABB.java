package org.by1337.blib.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.adapter.ClassAdapter;
import org.by1337.blib.geom.AABB;
import org.by1337.blib.geom.Vec2d;

public class AdapterAABB implements ClassAdapter<AABB> {
    @Override
    public ConfigurationSection serialize(AABB obj, YamlContext context) {
        context.set("minX", obj.getMinX());
        context.set("minY", obj.getMinY());
        context.set("minZ", obj.getMinZ());
        context.set("maxX", obj.getMaxX());
        context.set("maxY", obj.getMaxY());
        context.set("maxZ", obj.getMaxZ());
        return context.getHandle();
    }

    @Override
    public AABB deserialize(YamlContext context) {
        return new AABB(
                context.getAsDouble("minX"),
                context.getAsDouble("minY"),
                context.getAsDouble("minZ"),
                context.getAsDouble("maxX"),
                context.getAsDouble("maxY"),
                context.getAsDouble("maxZ")
        );
    }
}
