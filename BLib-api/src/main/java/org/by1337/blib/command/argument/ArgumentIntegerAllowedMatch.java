package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.match.BMatch;

import java.text.ParseException;
import java.util.List;
import java.util.function.Supplier;

public class ArgumentIntegerAllowedMatch<T> extends ArgumentInteger<T> {

    public ArgumentIntegerAllowedMatch(String name) {
        super(name);
    }

    public ArgumentIntegerAllowedMatch(String name, Supplier<List<String>> exx) {
        super(name, exx);
    }

    public ArgumentIntegerAllowedMatch(String name, List<String> exx) {
        super(name, exx);
    }

    public ArgumentIntegerAllowedMatch(String name, int min) {
        super(name, min);
    }

    public ArgumentIntegerAllowedMatch(String name, Supplier<List<String>> exx, int min) {
        super(name, exx, min);
    }

    public ArgumentIntegerAllowedMatch(String name, List<String> exx, int min) {
        super(name, exx, min);
    }

    public ArgumentIntegerAllowedMatch(String name, int min, int max) {
        super(name, min, max);
    }

    public ArgumentIntegerAllowedMatch(String name, Supplier<List<String>> exx, int min, int max) {
        super(name, exx, min, max);
    }

    public ArgumentIntegerAllowedMatch(String name, List<String> exx, int min, int max) {
        super(name, exx, min, max);
    }

    @Override
    public List<String> tabCompleter(T sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return getExx();
        try {
            String str1 = BMatch.match(String.format("match[%s]", str));
            super.process(sender, str1);
            return List.of(str1);
        } catch (ParseException | NumberFormatException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), str);
        }
    }

    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        try {
            return super.process(sender, BMatch.match(String.format("match[%s]", str)));
        } catch (ParseException | NumberFormatException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), str);
        }
    }
}
