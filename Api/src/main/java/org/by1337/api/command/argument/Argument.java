package org.by1337.api.command.argument;

import org.bukkit.command.CommandSender;
import org.by1337.api.command.CommandSyntaxError;
import org.by1337.api.command.requires.Requires;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Abstract class representing a command argument.
 */
public abstract class Argument {
    protected final String name;
    private Supplier<List<String>> exx;
    protected Requires requires;

    /**
     * Constructs an Argument with the specified name and no examples.
     *
     * @param name The name of the argument.
     */
    public Argument(String name) {
        this.name = name;
        this.exx = ArrayList::new;
    }


    public Argument(String name, Supplier<List<String>> exx) {
        this.name = name;
        this.exx = exx;
    }

    /**
     * Processes the input string and returns an object representing the argument value.
     *
     * @param sender The sender of the command.
     * @param str    The input string to process.
     * @return An object representing the processed argument value.
     * @throws CommandSyntaxError If there's a syntax error in the argument processing.
     */
    public abstract Object process(CommandSender sender, String str) throws CommandSyntaxError;

    public List<String> tabCompleter(CommandSender sender, String str) throws CommandSyntaxError {
        process(sender, str);
        return new ArrayList<>();
    }

    public Argument requires(Requires requires) {
        this.requires = requires;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getExx() {
        return this.exx.get();
    }

    public Requires getRequires() {
        return this.requires;
    }

    public void setExx(Supplier<List<String>> exx) {
        this.exx = exx;
    }
}
