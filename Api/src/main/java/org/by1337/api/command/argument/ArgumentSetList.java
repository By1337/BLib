package org.by1337.api.command.argument;

import org.bukkit.command.CommandSender;
import org.by1337.api.command.CommandSyntaxError;
import org.by1337.api.lang.Lang;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Represents an argument that accepts values from a predefined set of options.
 */
public class ArgumentSetList extends Argument {
    public final Supplier<List<String>> items;

    /**
     * Constructs an ArgumentSetList with the specified name and a list of allowed items.
     *
     * @param name  The name of the argument.
     * @param items A list of allowed values for the argument.
     */
    public ArgumentSetList(String name, List<String> items) {
        super(name, () -> items);
        this.items = () -> items;
    }


    public ArgumentSetList(String name, Supplier<List<String>> exx, List<String> items) {
        super(name, exx);
        this.items = () -> items;
    }

    public ArgumentSetList(String name, List<String> exx, List<String> items) {
        super(name, () -> exx);
        this.items = () -> items;
    }

    public ArgumentSetList(String name, Supplier<List<String>> items) {
        super(name);
        this.items = items;
    }

    public ArgumentSetList(String name, Supplier<List<String>> exx, Supplier<List<String>> items) {
        super(name, exx);
        this.items = items;
    }

    /**
     * Processes the input string and returns the value if it's in the predefined set of options.
     *
     * @param sender The sender of the command.
     * @param str    The input string to process.
     * @return The processed argument value if it's a valid option.
     * @throws CommandSyntaxError If the input string is not a valid option.
     */
    @Override
    public Object process(CommandSender sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return null;
        List<String> list = items.get();
        if (!list.contains(str)) {
            if (list.size() > 10)
                throw new CommandSyntaxError(Lang.getMessage("constant-not-found-more"), str, list.subList(0, 5), list.size() - 10);
            throw new CommandSyntaxError(Lang.getMessage("constant-not-found"), str, items.get());
        }
        return str;
    }
}
