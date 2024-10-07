package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.math.MathParser;

import java.text.ParseException;
import java.util.List;
import java.util.function.Supplier;

public class ArgumentIntegerAllowedMath<T> extends ArgumentInteger<T> {

    public ArgumentIntegerAllowedMath(String name) {
        super(name);
    }

    public ArgumentIntegerAllowedMath(String name, Supplier<List<String>> exx) {
        super(name, exx);
    }

    public ArgumentIntegerAllowedMath(String name, List<String> exx) {
        super(name, exx);
    }

    public ArgumentIntegerAllowedMath(String name, int min) {
        super(name, min);
    }

    public ArgumentIntegerAllowedMath(String name, Supplier<List<String>> exx, int min) {
        super(name, exx, min);
    }

    public ArgumentIntegerAllowedMath(String name, List<String> exx, int min) {
        super(name, exx, min);
    }

    public ArgumentIntegerAllowedMath(String name, int min, int max) {
        super(name, min, max);
    }

    public ArgumentIntegerAllowedMath(String name, Supplier<List<String>> exx, int min, int max) {
        super(name, exx, min, max);
    }

    public ArgumentIntegerAllowedMath(String name, List<String> exx, int min, int max) {
        super(name, exx, min, max);
    }

    @Override
    public List<String> tabCompleter(T sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return getExx();
        try {
            String str1 = MathParser.math(String.format("math[%s]", str
                    .replace("_", "")
                    .replace("k", "000")
                    .replace("K", "000")
                    .replace("к", "000")
                    .replace("К", "000")
            ), false);
            super.process(sender, str1);
            return List.of(str1);
        } catch (ParseException | NumberFormatException | ArithmeticException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), str);
        }
    }

    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        try {
            return super.process(sender, MathParser.math(String.format("math[%s]", str
                    .replace("_", "")
                    .replace("k", "000")
                    .replace("K", "000")
                    .replace("к", "000")
                    .replace("К", "000")
            ), false));
        } catch (ParseException | NumberFormatException | ArithmeticException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), str);
        }
    }
    public boolean allowAsync(){
        return true;
    }
}
