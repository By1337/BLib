package org.by1337.blib.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.adapter.ClassAdapter;

public class MemorySectionAdapter implements ClassAdapter<MemorySection> {
    @Override
    public ConfigurationSection serialize(MemorySection obj, YamlContext ignore) {
        return obj;
    }

    @Override
    public MemorySection deserialize(YamlContext context) {
        return context.getHandle();
    }
}
