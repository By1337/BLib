package org.by1337.api.command.argument;

import org.bukkit.command.CommandSender;
import org.by1337.api.command.CommandSyntaxError;

import java.util.List;
import java.util.function.Supplier;

public class ArgumentString extends Argument {
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
    public Object process(CommandSender sender, String str) throws CommandSyntaxError {
        return str;
    }
}
