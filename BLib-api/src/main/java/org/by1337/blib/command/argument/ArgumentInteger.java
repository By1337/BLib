package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.lang.Lang;

import java.util.List;
import java.util.function.Supplier;

/**
 * Represents an integer argument for a command.
 */
public class ArgumentInteger<T> extends Argument<T> {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public ArgumentInteger(String name) {
        super(name);
    }

    public ArgumentInteger(String name, Supplier<List<String>> exx) {
        super(name, exx);
    }

    public ArgumentInteger(String name, List<String> exx) {
        super(name, () -> exx);
    }


    public ArgumentInteger(String name, int min) {
        super(name);
        this.min = min;
    }

    public ArgumentInteger(String name, Supplier<List<String>> exx, int min) {
        super(name, exx);
        this.min = min;
    }

    public ArgumentInteger(String name, List<String> exx, int min) {
        super(name, () -> exx);
        this.min = min;
    }


    public ArgumentInteger(String name, int min, int max) {
        super(name);
        this.min = min;
        this.max = max;
    }

    public ArgumentInteger(String name, Supplier<List<String>> exx, int min, int max) {
        super(name, exx);
        this.min = min;
        this.max = max;
    }

    public ArgumentInteger(String name, List<String> exx, int min, int max) {
        super(name, () -> exx);
        this.min = min;
        this.max = max;
    }

    @Override
    public List<String> tabCompleter(T sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return getExx();
        process(sender, str);
        return getExx();
    }

    /**
     * Processes the input string and returns an Integer representing the argument value within specified bounds.
     *
     * @param sender The sender of the command.
     * @param str    The input string to process.
     * @return An Integer representing the processed argument value.
     * @throws CommandSyntaxError If there's a syntax error in the argument processing or the value is out of bounds.
     */
    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return 0;
        try {
            int val = Integer.parseInt(str);

            if (val < min)
                throw new CommandSyntaxError(Lang.getMessage("number-too-big"), val, min);

            if (val > max)
                throw new CommandSyntaxError(Lang.getMessage("number-too-small"), val, max);

            return val;

        } catch (NumberFormatException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), str);
        }
    }
    public boolean allowAsync(){
        return true;
    }
}
