package org.by1337.blib.command.argument;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.StringReader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ArgumentString<T> extends Argument<T> {
    public ArgumentString(String name) {
        super(name);
    }

    public ArgumentString(String name, List<String> exx) {
        super(name, () -> exx);
    }

    public ArgumentString(String name, Supplier<List<String>> exx) {
        super(name, exx);
    }

    @Override
    public void tabCompleter(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap, SuggestionsBuilder builder) throws CommandSyntaxError {
        if (!reader.hasNext()) {
            addSuggestions(builder, ArgumentUtils.quoteAndEscapeIfNeeded(getExx()));
            return;
        }
        String str = ArgumentUtils.readString(reader);
        var list = new ArrayList<>(getExx());
        list.removeIf(s -> !s.startsWith(str));
        list.add(str);
        argumentMap.put(name, str);
        addSuggestions(builder, ArgumentUtils.quoteAndEscapeIfNeeded(list));
    }

    @Override
    public void process(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        if (!reader.hasNext()) return;
        var str = ArgumentUtils.readString(reader);
        if (str.isBlank()) return;
        argumentMap.put(name, str);
    }

    public boolean allowAsync() {
        return true;
    }
}
