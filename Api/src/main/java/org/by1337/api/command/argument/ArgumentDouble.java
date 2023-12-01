package org.by1337.api.command.argument;

import org.bukkit.command.CommandSender;
import org.by1337.api.command.CommandSyntaxError;
import org.by1337.api.lang.Lang;

import java.util.List;
import java.util.function.Supplier;

public class ArgumentDouble<T> extends Argument<T> {
    private double min = Double.MIN_VALUE;
    private double max = Double.MAX_VALUE;


    public ArgumentDouble(String name) {
        super(name);
    }

    public ArgumentDouble(String name, Supplier<List<String>> exx) {
        super(name, exx);
    }

    public ArgumentDouble(String name, List<String> exx) {
        super(name, () -> exx);
    }


    public ArgumentDouble(String name, double min) {
        super(name);
        this.min = min;
    }

    public ArgumentDouble(String name, Supplier<List<String>> exx, double min) {
        super(name, exx);
        this.min = min;
    }

    public ArgumentDouble(String name, List<String> exx, double min) {
        super(name, () -> exx);
        this.min = min;
    }


    public ArgumentDouble(String name, double min, double max) {
        super(name);
        this.min = min;
        this.max = max;
    }

    public ArgumentDouble(String name, Supplier<List<String>> exx, double min, double max) {
        super(name, exx);
        this.min = min;
        this.max = max;
    }

    public ArgumentDouble(String name, List<String> exx, double min, double max) {
        super(name, () -> exx);
        this.min = min;
        this.max = max;
    }


    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return 0;
        try {
            double val = Double.parseDouble(str);

            if (val < min)
                throw new CommandSyntaxError(Lang.getMessage("number-too-big"), val, min);

            if (val > max)
                throw new CommandSyntaxError(Lang.getMessage("number-too-small"), val, max);

            return val;

        } catch (NumberFormatException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), str);
        }
    }
}