package org.by1337.api.command.argument;

import org.bukkit.command.CommandSender;
import org.by1337.api.BLib;
import org.by1337.api.command.CommandSyntaxError;
import org.by1337.api.lang.Lang;
import org.by1337.api.match.BMatch;

import java.text.ParseException;
import java.util.List;
import java.util.function.Supplier;

public class ArgumentIntegerAllowedMatch extends ArgumentInteger {

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
    public List<String> tabCompleter(CommandSender sender, String str) throws CommandSyntaxError {
        try {
            String str1 = BMatch.match(String.format("match[%s]", str));
            super.process(sender, str1);
            return List.of(str1);
        } catch (ParseException | NumberFormatException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), str);
        }
    }

    @Override
    public Object process(CommandSender sender, String str) throws CommandSyntaxError {
        try {
            return super.process(sender, BMatch.match(String.format("match[%s]", str)));
        } catch (ParseException | NumberFormatException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), str);
        }
    }
}
