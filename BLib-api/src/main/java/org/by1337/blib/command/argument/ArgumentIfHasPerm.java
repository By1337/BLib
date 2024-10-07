package org.by1337.blib.command.argument;

import org.bukkit.command.CommandSender;
import org.by1337.blib.command.CommandSyntaxError;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ArgumentIfHasPerm<T extends CommandSender> extends Argument<T>{
    private final Argument<T> argument;
    private final String permission;

    public ArgumentIfHasPerm(String name, Argument<T> argument, String permission) {
        super(name);
        this.argument = argument;
        this.permission = permission;
    }

    public ArgumentIfHasPerm(String name, Supplier<List<String>> exx, Argument<T> argument, String permission) {
        super(name, exx);
        this.argument = argument;
        this.permission = permission;
    }

    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        if (sender.hasPermission(permission))
            return argument.process(sender, str);
        return null;
    }

    @Override
    public List<String> tabCompleter(T sender, String str) throws CommandSyntaxError {
        if (sender.hasPermission(permission))
            return argument.tabCompleter(sender, str);
        return Collections.emptyList();
    }
    public boolean allowAsync(){
        return true;
    }
}
