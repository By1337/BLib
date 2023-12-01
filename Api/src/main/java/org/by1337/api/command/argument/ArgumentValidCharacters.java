package org.by1337.api.command.argument;

import org.bukkit.command.CommandSender;
import org.by1337.api.chat.util.InvalidCharacters;
import org.by1337.api.command.CommandSyntaxError;
import org.by1337.api.lang.Lang;

import java.util.List;
import java.util.function.Supplier;

/**
 * Represents an argument that validates input strings for valid characters.
 */
public class ArgumentValidCharacters<T> extends Argument<T> {
    /**
     * Constructs an ArgumentValidCharacters with the specified name.
     *
     * @param name The name of the argument.
     */
    public ArgumentValidCharacters(String name) {
        super(name);
    }

    /**
     * Constructs an ArgumentValidCharacters with the specified name and custom examples.
     *
     * @param name The name of the argument.
     * @param exx  A list of example values for the argument.
     */
    public ArgumentValidCharacters(String name, List<String> exx) {
        super(name, () -> exx);
    }

    public ArgumentValidCharacters(String name, Supplier<List<String>> exx) {
        super(name, exx);
    }

    /**
     * Processes the input string and checks if it contains any invalid characters. Throws a CommandSyntaxError if invalid characters are found.
     *
     * @param sender The sender of the command.
     * @param str    The input string to process.
     * @return The input string if it contains valid characters.
     * @throws CommandSyntaxError If the input string contains invalid characters.
     */
    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        String invalidChars = InvalidCharacters.getInvalidCharacters(str);
        if (!invalidChars.isEmpty())
            throw new CommandSyntaxError(Lang.getMessage("invalid-characters"), invalidChars);
        return str;
    }
}
