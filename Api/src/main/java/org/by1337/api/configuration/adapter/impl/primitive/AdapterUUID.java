package org.by1337.api.configuration.adapter.impl.primitive;

import org.by1337.api.configuration.adapter.PrimitiveAdapter;

import java.util.UUID;

public class AdapterUUID implements PrimitiveAdapter<UUID> {
    @Override
    public Object serialize(UUID obj) {
        return obj.toString();
    }

    @Override
    public UUID deserialize(Object src) {
        return UUID.fromString(String.valueOf(src));
    }
}
