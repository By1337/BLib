package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.util.OldEnumFixer;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ArgumentEnumValue<T> extends ArgumentSetList<T> {
    private final Map<String, Object> values;

    public <E extends Enum<E>> ArgumentEnumValue(String name, Class<E> anEnum, Predicate<E> filter) {
        super(name, process(anEnum, filter));
        values = nameToValueMap(anEnum);
    }

    public ArgumentEnumValue(String name, Class<? extends Enum<?>> anEnum) {
        super(name, process(anEnum, null));
        values = nameToValueMap(anEnum);
    }

    public ArgumentEnumValue(String name, List<String> exx, Class<? extends Enum<?>> anEnum) {
        super(name, exx, process(anEnum, null));
        values = nameToValueMap(anEnum);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> nameToValueMap(Class<?> anEnum) {
        if (OldEnumFixer.isOldEnum(anEnum)) {
            return (Map<String, Object>) OldEnumFixer.getNameToValueMap(anEnum);
        }
        return Arrays.stream(anEnum.getEnumConstants()).collect(Collectors.toMap(v -> ((Enum<?>) v).name(), Function.identity()));
    }

    private static <E> List<String> process(Class<E> anEnum, @Nullable Predicate<E> filter) {
        final E[] values;
        final boolean isOldEnum = OldEnumFixer.isOldEnum(anEnum);
        if (isOldEnum) {
            values = OldEnumFixer.values(anEnum);
        } else {
            values = anEnum.getEnumConstants();
        }
        return Arrays.stream(values).filter(v -> filter == null || filter.test(v)).map(v -> {
            if (isOldEnum) return OldEnumFixer.name(v);
            return ((Enum<?>) v).name();
        }).collect(Collectors.toList());
    }

    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        String val = (String) super.process(sender, str);
        if (val == null || val.isEmpty()) return null;
        return values.get(val);
    }

    public boolean allowAsync() {
        return true;
    }
}
