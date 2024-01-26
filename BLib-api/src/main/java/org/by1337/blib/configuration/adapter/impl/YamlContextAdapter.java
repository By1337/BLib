package org.by1337.blib.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.adapter.ClassAdapter;

public class YamlContextAdapter implements ClassAdapter<YamlContext> {
    @Override
    public ConfigurationSection serialize(YamlContext obj, YamlContext ignore) {
        return obj.getHandle();
    }

    @Override
    public YamlContext deserialize(YamlContext context) {
        return context;
    }
}
