package org.by1337.blib.configuration.adapter.impl.primitive;

import org.by1337.blib.configuration.adapter.PrimitiveAdapter;
import org.by1337.blib.util.OldEnumFixer;

import java.util.Locale;
import java.util.Map;

public class AdapterOldEnum<T> implements PrimitiveAdapter<T> {
    private final Class<T> clazz;
    private final Map<String, T> nameToValue;
    private final Map<T, String> valueToName;


    public AdapterOldEnum(Class<T> clazz) {
        this.clazz = clazz;
        nameToValue = OldEnumFixer.getNameToValueMap(clazz);
        valueToName = OldEnumFixer.getValueToNameMap(clazz);
    }

    @Override
    public Object serialize(T obj) {
        return valueToName.getOrDefault(obj, obj.toString());
    }

    @Override
    public T deserialize(Object src) {
        String s = String.valueOf(src);
        return nameToValue.getOrDefault(s, nameToValue.get(s.toUpperCase(Locale.ENGLISH)));
    }
}
