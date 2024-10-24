package org.by1337.blib.command.argument;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.StringReader;
import org.by1337.blib.lang.Lang;

import javax.print.DocFlavor;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ArgumentBoolean<T> extends Argument<T> {
    private static final List<String> TRUE_FALSE = List.of("true", "false");
    private static final Supplier<List<String>> TRUE_FALSE_SUPPLIER = () -> TRUE_FALSE;

    public ArgumentBoolean(String name) {
        super(name, TRUE_FALSE_SUPPLIER);
    }

    @Deprecated
    public ArgumentBoolean(String name, List<String> items) {
        super(name, TRUE_FALSE_SUPPLIER);
    }

    @Deprecated
    public ArgumentBoolean(String name, Supplier<List<String>> exx, List<String> items) {
        super(name, TRUE_FALSE_SUPPLIER);
    }

    @Deprecated
    public ArgumentBoolean(String name, Supplier<List<String>> exx) {
        super(name, TRUE_FALSE_SUPPLIER);
    }

    @Deprecated
    public ArgumentBoolean(String name, Supplier<List<String>> exx, Supplier<List<String>> items) {
        super(name, TRUE_FALSE_SUPPLIER);
    }

    @Override
    public void process(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        if (!reader.hasNext()) return;
        String str = ArgumentUtils.readString(reader);
        switch (str) {
            case "true" -> argumentMap.put(name, true);
            case "false" -> argumentMap.put(name, false);
            default -> throw new CommandSyntaxError(Lang.getMessage("expected-2"), true, false);
        }
    }

    @Override
    public void tabCompleter(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap, SuggestionsBuilder builder) throws CommandSyntaxError {
        if (!reader.hasNext()) {
            builder.suggest("true");
            builder.suggest("false");
            return;
        }
        String str = ArgumentUtils.readString(reader);
        switch (str) {
            case "true" -> argumentMap.put(name, true);
            case "false" -> argumentMap.put(name, false);
            default -> {
                for (String string : TRUE_FALSE) {
                    if (string.startsWith(str)) {
                        builder.suggest(string);
                        return;
                    }
                }
                throw new CommandSyntaxError(Lang.getMessage("expected-2"), true, false);
            }
        }
    }

    public boolean allowAsync(){
        return true;
    }
}
