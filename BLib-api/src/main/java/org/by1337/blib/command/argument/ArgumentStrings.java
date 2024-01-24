package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandSyntaxError;

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
    public Object process(T sender, String str) throws CommandSyntaxError {
        return str;
    }
}
