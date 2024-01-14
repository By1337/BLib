package org.by1337.api.command.argument;

import org.bukkit.command.CommandSender;
import org.by1337.api.command.CommandSyntaxError;
import org.by1337.api.lang.Lang;

import java.util.List;
import java.util.function.Supplier;

public class ArgumentFloat<T> extends Argument<T> {
    private float min = Float.MIN_VALUE;
    private float max = Float.MAX_VALUE;


    public ArgumentFloat(String name) {
        super(name);
    }

    public ArgumentFloat(String name, Supplier<List<String>> exx) {
        super(name, exx);
    }

    public ArgumentFloat(String name, List<String> exx) {
        super(name, () -> exx);
    }


    public ArgumentFloat(String name, float min) {
        super(name);
        this.min = min;
    }

    public ArgumentFloat(String name, Supplier<List<String>> exx, float min) {
        super(name, exx);
        this.min = min;
    }

    public ArgumentFloat(String name, List<String> exx, float min) {
        super(name, () -> exx);
        this.min = min;
    }


    public ArgumentFloat(String name, float min, float max) {
        super(name);
        this.min = min;
        this.max = max;
    }

    public ArgumentFloat(String name, Supplier<List<String>> exx, float min, float max) {
        super(name, exx);
        this.min = min;
        this.max = max;
    }

    public ArgumentFloat(String name, List<String> exx, float min, float max) {
        super(name, () -> exx);
        this.min = min;
        this.max = max;
    }

    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return 0;
        try {
            float val = Float.parseFloat(str);
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
