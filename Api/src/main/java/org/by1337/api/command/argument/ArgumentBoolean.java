package org.by1337.api.command.argument;

import org.bukkit.command.CommandSender;
import org.by1337.api.command.CommandSyntaxError;

import java.util.List;
import java.util.function.Supplier;

public class ArgumentBoolean<T> extends ArgumentSetList<T> {


    public ArgumentBoolean(String name, List<String> items) {
        super(name, items);
    }

    public ArgumentBoolean(String name, Supplier<List<String>> exx, List<String> items) {
        super(name, exx, items);
    }

    public ArgumentBoolean(String name) {
        super(name, List.of("true", "false"));
    }

    public ArgumentBoolean(String name, Supplier<List<String>> exx) {
        super(name, exx, List.of("true", "false"));
    }

    public ArgumentBoolean(String name, Supplier<List<String>> exx, Supplier<List<String>> items) {
        super(name, exx, items);
    }

    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        return "true".equals(super.process(sender, str));
    }
}
