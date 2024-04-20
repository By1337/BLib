package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandException;
import org.by1337.blib.command.CommandSyntaxError;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BiArgument<T> extends Argument<T> {
    private final Argument<T> first;
    private final Argument<T> second;

    public BiArgument(String name, Argument<T> first, Argument<T> second) {
        super(name);
        this.first = first;
        this.second = second;
    }

    public BiArgument(String name, Supplier<List<String>> exx, Argument<T> first, Argument<T> second) {
        super(name, exx);
        this.first = first;
        this.second = second;
    }

    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        try {
            return first.process(sender, str);
        } catch (CommandException e) {
            return second.process(sender, str);
        }
    }

    @Override
    public List<String> tabCompleter(T sender, String str) throws CommandSyntaxError {
        List<String> list = new ArrayList<>();
        CommandSyntaxError err;
        try {
            list.addAll(first.tabCompleter(sender, str));
            err = null;
        } catch (CommandSyntaxError error) {
            err = error;
        }
        try {
            list.addAll(second.tabCompleter(sender, str));
        } catch (CommandSyntaxError error) {
            if (err != null) {
                throw err;
            }
        }
        return list;
    }
}
