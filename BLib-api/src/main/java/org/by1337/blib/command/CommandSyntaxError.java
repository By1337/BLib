package org.by1337.blib.command;

import org.by1337.blib.text.MessageFormatter;

/**
 * Represents an exception thrown for command syntax errors.
 */
public class CommandSyntaxError extends CommandException {

    /**
     * Constructs a CommandSyntaxError with the specified error message.
     *
     * @param message The error message.
     */
    public CommandSyntaxError(String message) {
        super(message);
    }

    /**
     * Constructs a CommandSyntaxError with a formatted error message and arguments.
     *
     * @param message  The formatted error message.
     * @param objects  The arguments to be formatted into the message.
     */
    public CommandSyntaxError(String message, Object... objects) {
        super(MessageFormatter.apply(message, objects));
    }

    /**
     * Constructs a CommandSyntaxError with no specific error message.
     */
    public CommandSyntaxError() {
    }

    /**
     * Throws a CommandSyntaxError if the provided object is null.
     *
     * @param obj The object to check for null.
     * @param <T> The type of the object.
     * @return The non-null object if it passes the check.
     * @throws CommandSyntaxError If the object is null.
     */
    public static <T> T ifNull(T obj) throws CommandSyntaxError {
        if (obj == null)
            throw new CommandSyntaxError();
        return obj;
    }

    /**
     * Throws a CommandSyntaxError with a specified message if the provided object is null.
     *
     * @param obj     The object to check for null.
     * @param message The error message to use if the object is null.
     * @param <T>     The type of the object.
     * @return The non-null object if it passes the check.
     * @throws CommandSyntaxError If the object is null.
     */
    public static <T> T ifNull(T obj, String message) throws CommandSyntaxError {
        if (obj == null)
            throw new CommandSyntaxError(message);
        return obj;
    }

    /**
     * Throws a CommandSyntaxError with a formatted error message if the provided object is null.
     *
     * @param obj     The object to check for null.
     * @param message The formatted error message to use if the object is null.
     * @param objects The arguments to be formatted into the message.
     * @param <T>     The type of the object.
     * @return The non-null object if it passes the check.
     * @throws CommandSyntaxError If the object is null.
     */
    public static <T> T ifNull(T obj, String message, Object... objects) throws CommandSyntaxError {
        if (obj == null)
            throw new CommandSyntaxError(message, objects);
        return obj;
    }

    /**
     * Throws a CommandSyntaxError if the provided object is not null.
     *
     * @param obj The object to check for not null.
     * @param <T> The type of the object.
     * @throws CommandSyntaxError If the object is not null.
     */
    public static <T> void ifNotNull(T obj) throws CommandSyntaxError {
        if (obj != null)
            throw new CommandSyntaxError();
    }

    /**
     * Throws a CommandSyntaxError with a specified message if the provided object is not null.
     *
     * @param obj     The object to check for not null.
     * @param message The error message to use if the object is not null.
     * @param <T>     The type of the object.
     * @throws CommandSyntaxError If the object is not null.
     */
    public static <T> void ifNotNull(T obj, String message) throws CommandSyntaxError {
        if (obj != null)
            throw new CommandSyntaxError(message);
    }

    /**
     * Throws a CommandSyntaxError with a formatted error message if the provided object is not null.
     *
     * @param obj     The object to check for not null.
     * @param message The formatted error message to use if the object is not null.
     * @param objects The arguments to be formatted into the message.
     * @param <T>     The type of the object.
     * @throws CommandSyntaxError If the object is not null.
     */
    public static <T> void ifNotNull(T obj, String message, Object... objects) throws CommandSyntaxError {
        if (obj != null)
            throw new CommandSyntaxError(message, objects);
    }
}
