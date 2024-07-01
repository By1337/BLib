package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.lang.Lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArgumentFlagList<T> extends ArgumentStrings<T> {

    private final List<String> flags;

    public ArgumentFlagList(String name, List<String> flags) {
        super(name);
        this.flags = flags;
    }

    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return null;
        List<String> inputFlags = List.of(str.split(" "));
        if (inputFlags.isEmpty()) return null;
        List<String> result = new ArrayList<>();
        for (String inputFlag : inputFlags) {
            if (!flags.contains(inputFlag)) {
                if (!flags.contains(str)) {
                    if (flags.size() > 10)
                        throw new CommandSyntaxError(Lang.getMessage("constant-not-found-more"), str, flags.subList(0, 5), flags.size() - 10);
                    throw new CommandSyntaxError(Lang.getMessage("constant-not-found"), str, flags);
                }
            }
            if (!result.contains(inputFlag)) {
                result.add(inputFlag);
            }
        }
        return result;
    }

    @Override
    public List<String> tabCompleter(T sender, String str) throws CommandSyntaxError {
        List<String> result = new ArrayList<>(flags);
        if (str.isEmpty())
            return result;
        List<String> inputFlags = List.of(str.split(" "));
        if (inputFlags.isEmpty()) return result;
        result.removeAll(inputFlags);
        var list = result.stream().filter(s -> s.startsWith(inputFlags.get(inputFlags.size() - 1))).toList();
        if (list.isEmpty()) return result;
        return list;
    }
}
