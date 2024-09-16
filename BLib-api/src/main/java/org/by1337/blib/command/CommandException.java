package org.by1337.blib.command;

import org.by1337.blib.text.MessageFormatter;

public class CommandException extends Exception{

    /**
     * Constructs a CommandSyntaxError with the specified error message.
     *
     * @param message The error message.
     */
    public CommandException(String message) {
        super(message);
    }

    /**
     * Constructs a CommandSyntaxError with a formatted error message and arguments.
     *
     * @param message  The formatted error message.
     * @param objects  The arguments to be formatted into the message.
     */
    public CommandException(String message, Object... objects) {
        super(MessageFormatter.apply(message, objects));
    }

    /**
     * Constructs a CommandSyntaxError with no specific error message.
     */
    public CommandException() {
    }
}

