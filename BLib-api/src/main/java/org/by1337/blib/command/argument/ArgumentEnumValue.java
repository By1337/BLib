package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandSyntaxError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ArgumentEnumValue<T> extends ArgumentSetList<T> {
    private final Class<? extends Enum<?>> anEnum;
    private final Map<String, Enum<?>> values;

    public <E extends Enum<E>> ArgumentEnumValue(String name, Class<E> anEnum, Predicate<E> filter) {
        super(name, Arrays.stream(anEnum.getEnumConstants()).filter(filter).map(Enum::name).collect(Collectors.toList()));
        this.anEnum = anEnum;
        values = Arrays.stream(anEnum.getEnumConstants()).filter(filter).collect(Collectors.toMap(Enum::name, Function.identity()));
    }

    public ArgumentEnumValue(String name, Class<? extends Enum<?>> anEnum) {
        super(name, Arrays.stream(anEnum.getEnumConstants()).map(Enum::name).collect(Collectors.toList()));
        this.anEnum = anEnum;
        values = Arrays.stream(anEnum.getEnumConstants()).collect(Collectors.toMap(Enum::name, Function.identity()));
    }

    public ArgumentEnumValue(String name, List<String> exx, Class<? extends Enum<?>> anEnum) {
        super(name, exx, Arrays.stream(anEnum.getEnumConstants()).map(Enum::name).collect(Collectors.toList()));
        this.anEnum = anEnum;
        values = Arrays.stream(anEnum.getEnumConstants()).collect(Collectors.toMap(Enum::name, Function.identity()));
    }

    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        String val = (String) super.process(sender, str);
        if (val == null || val.isEmpty()) return null;
        return values.get(val);
    }

    public boolean allowAsync(){
        return true;
    }
}
