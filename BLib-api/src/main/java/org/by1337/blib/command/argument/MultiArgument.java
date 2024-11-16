package org.by1337.blib.command.argument;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.by1337.blib.command.CommandException;
import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.StringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiArgument<T> extends Argument<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger("BLib#MultiArgument");

    private final Argument<T>[] arguments;

    public MultiArgument(String name, Argument<T>... arguments) {
        super(name);
        this.arguments = arguments;
        for (Argument<T> argument : arguments) {
            if (!argument.name.equals(name)){
                LOGGER.warn("Argument {} has a name {} different from MultiArgument {}", argument, argument.name, name);
            }
        }
    }

    @Override
    public void process(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        int start = reader.getCursor();
        for (Argument<T> argument : arguments) {
            try {
                if (argument.checkRequires(sender)) {
                    argument.process(sender, reader, argumentMap);
                    return;
                }
            } catch (CommandException ignore) {
                reader.setCursor(start);
            }
        }
    }
    @Override
    public void tabCompleter(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap, SuggestionsBuilder builder) throws CommandSyntaxError {
        int start = reader.getCursor();

        for (Argument<T> argument : arguments) {
            try {
                if (argument.checkRequires(sender)) {
                    SuggestionsBuilder builder1 = builder.createOffset(builder.getStart());
                    argument.tabCompleter(sender, reader, argumentMap, builder1);
                    builder.add(builder1);
                    return;
                }
            } catch (CommandException ignore) {
            }
        }
        reader.setCursor(start);
    }
}
