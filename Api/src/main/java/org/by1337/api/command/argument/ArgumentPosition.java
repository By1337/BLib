package org.by1337.api.command.argument;

import org.bukkit.command.CommandSender;
import org.by1337.api.command.CommandSyntaxError;
import org.by1337.api.lang.Lang;

import java.util.List;
import java.util.function.Supplier;

/**
 * Represents a position argument for a command, which can be a numeric value or a relative position indicator.
 */
public class ArgumentPosition extends Argument {
    protected final ArgumentPositionType type;

    /**
     * Constructs an ArgumentPosition with the specified name and position type.
     *
     * @param name The name of the argument.
     * @param type The type of position argument (X, Y, or Z).
     */
    public ArgumentPosition(String name, ArgumentPositionType type) {
        super(name);
        this.type = type;
    }

    /**
     * Constructs an ArgumentPosition with the specified name, custom examples, and position type.
     *
     * @param name The name of the argument.
     * @param exx  A list of example values for the argument.
     * @param type The type of position argument (X, Y, or Z).
     */
    public ArgumentPosition(String name, List<String> exx, ArgumentPositionType type) {
        super(name, () -> exx);
        this.type = type;
    }

    public ArgumentPosition(String name, Supplier<List<String>> exx, ArgumentPositionType type) {
        super(name, exx);
        this.type = type;
    }

    /**
     * Processes the input string and returns a Double representing the argument value, which can be a numeric value or a relative position indicator (~).
     *
     * @param sender The sender of the command.
     * @param str    The input string to process.
     * @return A Double representing the processed argument value.
     * @throws CommandSyntaxError If there's a syntax error in the argument processing or the value is not a valid number.
     */
    @Override
    public Object process(CommandSender sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return null;
        try {
            if (str.equals("~")) {
                return switch (type) {
                    case X -> PositionUtil.getXIfPlayer(sender).doubleValue();
                    case Y -> PositionUtil.getYIfPlayer(sender).doubleValue();
                    case Z -> PositionUtil.getZIfPlayer(sender).doubleValue();
                };
            }
            return Double.parseDouble(str);

        } catch (NumberFormatException e) {
            throw new CommandSyntaxError(Lang.getMessage("nan"), str);
        }
    }

    /**
     * Enum representing the type of position argument (X, Y, or Z).
     */
    public enum ArgumentPositionType {
        X,
        Y,
        Z
    }
}
