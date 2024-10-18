package org.by1337.blib.command.argument;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.StringReader;
import org.by1337.blib.lang.Lang;

import java.util.ArrayList;
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
    public void process(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        if (!reader.hasNext()) return;
        String str = ArgumentUtils.readString(reader);
        try {
            double val = Double.parseDouble(str);

            if (val < min)
                throw new CommandSyntaxError(Lang.getMessage("number-too-big"), val, min);

            if (val > max)
                throw new CommandSyntaxError(Lang.getMessage("number-too-small"), val, max);

            argumentMap.put(name, val);
        } catch (NumberFormatException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), str);
        }
    }

    @Override
    public void tabCompleter(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap, SuggestionsBuilder builder) throws CommandSyntaxError {
        if (!reader.hasNext()){
            applyExx(builder);
            return;
        }
        process(sender, reader, argumentMap);
        applyExx(builder);
    }
    public boolean allowAsync(){
        return true;
    }
}