package org.by1337.blib.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.adapter.ClassAdapter;
import org.by1337.blib.geom.Vec2d;
import org.by1337.blib.geom.Vec2i;

public class AdapterVec2d implements ClassAdapter<Vec2d> {
    @Override
    public ConfigurationSection serialize(Vec2d obj, YamlContext context) {
        context.set("x", obj.getX());
        context.set("y", obj.getY());
        return context.getHandle();
    }

    @Override
    public Vec2d deserialize(YamlContext context) {
        return new Vec2d(
                context.getAsDouble("x"),
                context.getAsDouble("y")
        );
    }
}
