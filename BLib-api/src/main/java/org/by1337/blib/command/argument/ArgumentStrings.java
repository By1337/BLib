package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.StringReader;

import java.util.List;
import java.util.function.Supplier;

public class ArgumentStrings<T> extends Argument<T> {
    public ArgumentStrings(String name) {
        super(name);
    }

    public ArgumentStrings(String name, List<String> exx) {
        super(name, () -> exx);
    }

    public ArgumentStrings(String name, Supplier<List<String>> exx) {
        super(name, exx);
    }

    @Override
    public void process(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        argumentMap.put(name, process(sender, reader.readAll()));
    }

    @Override
    public List<String> tabCompleter(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        return tabCompleter(sender, reader.readAll());
    }

    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        return str;
    }
    public boolean allowAsync(){
        return true;
    }
}
