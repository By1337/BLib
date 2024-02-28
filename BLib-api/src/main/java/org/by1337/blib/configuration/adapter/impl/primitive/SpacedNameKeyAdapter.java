package org.by1337.blib.configuration.adapter.impl.primitive;

import org.by1337.blib.configuration.adapter.PrimitiveAdapter;
import org.by1337.blib.util.SpacedNameKey;

public class SpacedNameKeyAdapter implements PrimitiveAdapter<SpacedNameKey> {
    @Override
    public Object serialize(SpacedNameKey obj) {
        return obj.getSpace().getName() + ":" + obj.getName().getName();
    }

    @Override
    public SpacedNameKey deserialize(Object src) {
        return new SpacedNameKey(String.valueOf(src));
    }
}
