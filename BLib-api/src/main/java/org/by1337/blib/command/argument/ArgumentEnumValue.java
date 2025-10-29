package org.by1337.blib.command.argument;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Sound;
import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.StringReader;
import org.by1337.blib.util.OldEnumFixer;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ArgumentEnumValue<T> extends ArgumentSetList<T> {
    private final Map<String, ?> values;
    private final ArgumentSound<T> sound;

    public <E extends Enum<E>> ArgumentEnumValue(String name, Class<E> anEnum, Predicate<E> filter) {
        super(name, process(anEnum, filter));
        if (anEnum == Sound.class) {
            values = ArgumentSound.LOOKUP_BY_NAME;
            sound = new ArgumentSound<>(name);
        } else {
            values = nameToValueMap(anEnum);
            sound = null;
        }
    }

    public ArgumentEnumValue(String name, Class<? extends Enum<?>> anEnum) {
        super(name, process(anEnum, null));
        if (anEnum == Sound.class) {
            values = ArgumentSound.LOOKUP_BY_NAME;
            sound = new ArgumentSound<>(name);
        } else {
            values = nameToValueMap(anEnum);
            sound = null;
        }
    }

    public ArgumentEnumValue(String name, List<String> exx, Class<? extends Enum<?>> anEnum) {
        super(name, exx, process(anEnum, null));
        if (anEnum == Sound.class) {
            values = ArgumentSound.LOOKUP_BY_NAME;
            sound = new ArgumentSound<>(name);
        } else {
            values = nameToValueMap(anEnum);
            sound = null;
        }
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

    @Override
    public void process(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        if (sound != null) {
            sound.process(sender, reader, argumentMap);
        } else {
            super.process(sender, reader, argumentMap);
        }

    }

    @Override
    public void tabCompleter(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap, SuggestionsBuilder builder) throws CommandSyntaxError {
        if (sound != null) {
            sound.tabCompleter(sender, reader, argumentMap, builder);
        } else {
            super.tabCompleter(sender, reader, argumentMap, builder);
        }
    }

    public boolean allowAsync() {
        return true;
    }
}
