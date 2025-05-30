package org.by1337.blib.command.argument;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.lang.Lang;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Represents a player argument for a command.
 */
public class ArgumentPlayer<T extends CommandSender> extends Argument<T> {
    private static final Pattern UUID_REGEX =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private List<String> exx;

    /**
     * Constructs an ArgumentPlayer with the specified name, including predefined examples.
     *
     * @param name The name of the argument.
     */
    public ArgumentPlayer(String name) {
        super(name);
        exx = new ArrayList<>();
    }

    /**
     * Constructs an ArgumentPlayer with the specified name and custom examples.
     *
     * @param name The name of the argument.
     * @param exx  A list of example values for the argument.
     */
    public ArgumentPlayer(String name, List<String> exx) {
        super(name);
        this.exx = exx;
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
    public Object process(T sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return null;
        Player player = getPlayer(sender, str);
        if (player == null)
            throw new CommandSyntaxError(Lang.getMessage("unknown-player"), str);
        return player;
    }

    private Player getPlayer(T sender, String str) {
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
            return Bukkit.getPlayerExact(str);
        }
    }

    @Override
    public List<String> tabCompleter(T sender, String str) {
        Player player = sender instanceof Player ? (Player) sender : null;
        var list = Bukkit.getOnlinePlayers().stream().filter(pl -> player == null || player.canSee(pl)).map(Player::getName).collect(Collectors.toList());
        list.addAll(exx);
        if (str.isEmpty())
            return list;
        String input = str.toLowerCase(Locale.ENGLISH);
        return list.stream().filter(
                s -> s.toLowerCase(Locale.ENGLISH).startsWith(input)
        ).toList();
    }
    public boolean allowAsync(){
        return true;
    }
}
