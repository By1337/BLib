package org.by1337.blib.command.argument;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.StringReader;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.math.DoubleMathParser;
import org.by1337.blib.util.number.NumberFormatter;

import java.text.ParseException;
import java.util.List;
import java.util.function.Supplier;

public class ArgumentFormattedDouble<T> extends Argument<T> {
    public ArgumentFormattedDouble(String name) {
        super(name);
    }

    public ArgumentFormattedDouble(String name, Supplier<List<String>> exx) {
        super(name, exx);
    }

    public ArgumentFormattedDouble(String name, List<String> exx) {
        super(name, () -> exx);
    }


    @Override
    public void process(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        if (!reader.hasNext()) return;
        String input = ArgumentUtils.readString(reader);
        try {
            String s = DoubleMathParser.math(
                    replaceK(input)
                            .replace("_", "")
                            .replace(",", "")
            );
            argumentMap.put(name, Double.parseDouble(s));
        } catch (ParseException | NumberFormatException | ArithmeticException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), input);
        }
    }

    @Override
    public void tabCompleter(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap, SuggestionsBuilder builder) throws CommandSyntaxError {
        if (!reader.hasNext()) {
            addSuggestions(builder, getExx());
            return;
        }
        String input = ArgumentUtils.readString(reader);
        try {
            String s = DoubleMathParser.math(
                    replaceK(input)
                            .replace("_", "")
                            .replace(",", "")
            );
            Double d = Double.parseDouble(s);
            builder.suggest(NumberFormatter.formatNumberK(d));
            builder.suggest(NumberFormatter.formatNumberWithThousandsSeparator(d, ".", ","));
        } catch (ParseException | NumberFormatException | ArithmeticException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), input);
        }
    }

    private String replaceK(String input) {
        String[] parts = input.split("\\.");
        if (parts.length == 2) {
            return parts[0] + "." + parts[1]
                    .replace("K", "*1000")
                    .replace("k", "*1000")
                    .replace("к", "*1000")
                    .replace("К", "*1000");
        } else {
            return input
                    .replace("K", "*1000")
                    .replace("k", "*1000")
                    .replace("к", "*1000")
                    .replace("К", "*1000");
        }
    }

    @Override
    public boolean allowAsync() {
        return true;
    }
}
