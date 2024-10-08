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
            if (first.checkRequires(sender)) {
                return first.process(sender, str);
            }
        } catch (CommandException e) {
        }
        if (second.checkRequires(sender)) {
            return second.process(sender, str);
        }
        return null;
    }

    @Override
    public List<String> tabCompleter(T sender, String str) throws CommandSyntaxError {
        List<String> list = new ArrayList<>();
        CommandSyntaxError err;
        try {
            if (first.checkRequires(sender)) {
                list.addAll(first.tabCompleter(sender, str));
            }
            err = null;
        } catch (CommandSyntaxError error) {
            err = error;
        }
        try {
            if (second.checkRequires(sender)) {
                list.addAll(second.tabCompleter(sender, str));
            }
        } catch (CommandSyntaxError error) {
            if (err != null) {
                throw err;
            }
        }
        return list;
    }
    public boolean allowAsync(){
        return first.allowAsync() && second.allowAsync();
    }
}
