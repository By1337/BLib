package org.by1337.blib.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.adapter.ClassAdapter;
import org.by1337.blib.geom.Vec2i;
import org.by1337.blib.geom.Vec3i;
public class AdapterVec2i implements ClassAdapter<Vec2i> {
    @Override
    public ConfigurationSection serialize(Vec2i obj, YamlContext context) {
        context.set("x", obj.getX());
        context.set("y", obj.getY());
        return context.getHandle();
    }

    @Override
    public Vec2i deserialize(YamlContext context) {
        return new Vec2i(
                context.getAsInteger("x"),
                context.getAsInteger("y")
        );
    }
}
