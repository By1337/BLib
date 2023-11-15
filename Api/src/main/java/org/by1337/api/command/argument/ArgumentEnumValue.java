package org.by1337.api.command.argument;

import org.bukkit.command.CommandSender;
import org.by1337.api.command.CommandSyntaxError;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArgumentEnumValue extends ArgumentSetList {
    private final Class<? extends Enum<?>> anEnum;

    public ArgumentEnumValue(String name, Class<? extends Enum<?>> anEnum) {
        super(name, Arrays.stream(anEnum.getEnumConstants()).map(Enum::name).collect(Collectors.toList()));
        this.anEnum = anEnum;
    }

    public ArgumentEnumValue(String name, List<String> exx, Class<? extends Enum<?>> anEnum) {
        super(name, exx, Arrays.stream(anEnum.getEnumConstants()).map(Enum::name).collect(Collectors.toList()));
        this.anEnum = anEnum;
    }

    @Override
    public Object process(CommandSender sender, String str) throws CommandSyntaxError {
        String val = (String) super.process(sender, str);
        if (val == null || val.isEmpty()) return null;
        return Arrays.stream(anEnum.getEnumConstants()).filter(constant -> constant.name().equals(val)).findFirst().orElse(null);
    }
}
