package org.by1337.api.command.argument;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.by1337.api.command.CommandSyntaxError;
import org.by1337.api.lang.Lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Represents a player argument for a command.
 */
public class ArgumentPlayer extends Argument {
    private static final Pattern UUID_REGEX =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    /**
     * Constructs an ArgumentPlayer with the specified name, including predefined examples.
     *
     * @param name The name of the argument.
     */
    public ArgumentPlayer(String name) {
        super(name);
        List<String> exx = new ArrayList<>();
        exx.add("@p");
        exx.add("@r");
        exx.add("@s");
        exx.addAll(List.of(Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new)));
        super.setExx(() -> exx);
    }

    /**
     * Constructs an ArgumentPlayer with the specified name and custom examples.
     *
     * @param name The name of the argument.
     * @param exx  A list of example values for the argument.
     */
    public ArgumentPlayer(String name, List<String> exx) {
        super(name);
        List<String> exxL = new ArrayList<>(exx);
        exxL.add("@p");
        exxL.add("@r");
        exxL.add("@s");
        exxL.addAll(List.of(Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new)));
        super.setExx(() -> exxL);
    }

    public ArgumentPlayer(String name, Supplier<List<String>> exx) {
        super(name, exx);
    }

    /**
     * Processes the input string and returns a Player representing the argument value.
     *
     * @param sender The sender of the command.
     * @param str    The input string to process.
     * @return A Player representing the processed argument value.
     * @throws CommandSyntaxError If there's a syntax error in the argument processing or the player is unknown.
     */
    @Override
    public Object process(CommandSender sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return null;
        Player player = getPlayer(sender, str);
        if (player == null)
            throw new CommandSyntaxError(Lang.getMessage("unknown-player"), str);
        return player;
    }

    private Player getPlayer(CommandSender sender, String str) {
        if (str.equals("@p")) {
            return PositionUtil.getWorldIfPlayer(sender).getNearbyEntities(PositionUtil.getLocationIfPlayer(sender), 1, 1, 1).stream().findFirst().orElse(null) instanceof Player player ? player : null;
        }
        if (str.equals("@r")) {
            return Bukkit.getOnlinePlayers().stream().toList().get(new Random().nextInt(Bukkit.getOnlinePlayers().size()));
        }
        if (str.equals("@s")) {
            return sender instanceof Player player ? player : null;
        }
        if (UUID_REGEX.matcher(str).matches()) {
            return Bukkit.getPlayer(UUID.fromString(str));
        } else {
            return Bukkit.getPlayer(str);
        }
    }
}
