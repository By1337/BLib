package org.by1337.blib.chat.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.by1337.blib.BLib;
import org.by1337.blib.chat.ComponentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Message class is responsible for handling and sending formatted messages to both the console log and players.
 */
public class Message {
    private final Pattern hexPattern;
    private final Logger logger;
    private LogLevel logLevel = LogLevel.NONE;

    /**
     * Constructs a Message instance with a default color pattern.
     *
     * @param logger The logger to use for output.
     */
    public Message(@NotNull Logger logger) {
        this.logger = logger;
        hexPattern = Pattern.compile("&(#[a-f0-9]{6})", Pattern.CASE_INSENSITIVE);
    }

    /**
     * Constructs a Message instance with a custom color pattern.
     *
     * @param hexPattern The custom color pattern to use.
     * @param logger     The logger to use for output.
     */
    public Message(@NotNull Pattern hexPattern, @NotNull Logger logger) {
        this.hexPattern = hexPattern;
        this.logger = logger;
    }

    /**
     * Logs a severe error message with the associated Throwable.
     *
     * @param msg The error message to log.
     * @param t   The Throwable associated with the error.
     */
    public void error(String msg, Throwable t) {
        logger.log(Level.SEVERE, msg, t);
    }

    /**
     * Logs a severe error message with the associated Throwable and additional objects.
     *
     * @param msg     The error message to log, which may contain placeholders for objects.
     * @param t       The Throwable associated with the error.
     * @param objects Additional objects to be included in the log message.
     */
    public void error(String msg, Throwable t, Object... objects) {
        logger.log(Level.SEVERE, String.format(msg, objects), t);
    }

    /**
     * Logs a severe error message with the associated Throwable and an empty message.
     *
     * @param t The Throwable associated with the error.
     */
    public void error(Throwable t) {
        logger.log(Level.SEVERE, "", t);
    }

    /**
     * Sends a formatted message to a command sender.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg    The message string to be sent.
     */
    public void sendMsg(@NotNull CommandSender sender, @NotNull String msg) {
        msg = setPlaceholders(sender instanceof OfflinePlayer ? (OfflinePlayer) sender : null, msg);
        sender.sendMessage(messageBuilder(msg));
    }

    /**
     * Sends a raw message to a command sender using a ComponentBuilder.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg    The ComponentBuilder containing the raw message.
     */
    public void sendRawMsg(@NotNull CommandSender sender, @NotNull ComponentBuilder msg) {
        if (sender instanceof Player player)
            BLib.getCommandUtil().tellRaw(msg.build(), player);
        else
            logger(msg.build());
    }

    /**
     * Sends a raw message to a command sender using a plain string.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg    The raw message string to be sent.
     */
    public void sendRawMsg(@NotNull CommandSender sender, @NotNull String msg) {
        if (sender instanceof Player player)
            BLib.getCommandUtil().tellRaw(msg, player);
        else
            logger(msg);
    }

    /**
     * Sends a formatted message to a command sender with variable replacements using format placeholders.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg    The message string to be sent with format placeholders.
     * @param format The objects used for formatting the message.
     */
    public void sendMsg(@NotNull CommandSender sender, @NotNull String msg, @NotNull Object... format) {
        msg = setPlaceholders(sender instanceof OfflinePlayer ? (OfflinePlayer) sender : null, msg);
        sender.sendMessage(messageBuilder(String.format(msg, format)));
    }

    /**
     * Send a debug message to the logger.
     *
     * @param msg The debug message to be logged.
     */
    public void debug(@Nullable String msg) {
        logger.info("[DEBUG] " + msg);
    }

    /**
     * Send a debug message to the logger with the specified message and log level control.
     *
     * @param msg   The debug message to be logged.
     * @param level The log level control to determine if the message should be logged.
     */
    public void debug(@Nullable String msg, @NotNull LogLevel level) {
        if (level.getLvl() < logLevel.getLvl()) {
            logger.info("[DEBUG] " + msg);
        }
    }

    /**
     * Send a debug message to the logger with the specified message, log level control, and additional objects.
     *
     * @param msg     The debug message to be logged, which may contain placeholders for objects.
     * @param level   The log level control to determine if the message should be logged.
     * @param objects Additional objects to be included in the log message.
     */
    public void debug(@Nullable String msg, @NotNull LogLevel level, Object... objects) {
        if (level.getLvl() < logLevel.getLvl()) {
            logger.info(String.format("[DEBUG] " + msg, objects));
        }
    }

    /**
     * Log a message to the logger with formatting.
     *
     * @param msg The message to be logged.
     */
    public void logger(@NotNull String msg) {
        logger.log(Level.INFO, messageBuilder(msg));
    }


    /**
     * Log a message to the logger with formatting and additional objects.
     *
     * @param msg     The message format string.
     * @param objects Objects to format the message.
     */
    public void logger(@NotNull String msg, @NotNull Object... objects) {
        logger.log(Level.INFO, String.format(messageBuilder(msg), objects));
    }

    /**
     * Log an error message to the logger.
     *
     * @param msg The error message to be logged.
     */
    public void error(@NotNull String msg) {
        logger.log(Level.SEVERE, messageBuilder(msg));
    }

    /**
     * Log an error message to the logger with formatting.
     *
     * @param msg     The error message format string.
     * @param objects Objects to format the error message.
     */
    public void error(@NotNull String msg, @NotNull Object... objects) {
        logger.log(Level.SEVERE, messageBuilder(String.format(msg, objects)));
    }

    /**
     * Send a formatted message to all online players who are operators.
     *
     * @param msg The message to be sent to online operators.
     */
    public void sendAllOp(@NotNull String msg) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.isOp()) {
                sendMsg(pl, messageBuilder(msg));
            }
        }
    }

    /**
     * Log a warning message to the logger.
     *
     * @param msg The warning message to be logged.
     */
    public void warning(@NotNull String msg) {
        logger.log(Level.WARNING, messageBuilder(msg));
    }

    public void warning(@NotNull String msg, Object... objects) {
        logger.log(Level.WARNING, messageBuilder(String.format(msg, objects)));
    }

    /**
     * Send an action bar message to a player.
     *
     * @param pl  The player to send the action bar message to.
     * @param msg The message to be displayed in the action bar.
     */
    public void sendActionBar(@NotNull Player pl, @NotNull String msg) {
        pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messageBuilder(msg)));
    }

    /**
     * Send an action bar message to all online players.
     *
     * @param msg The message to be displayed in the action bar of all online players.
     */
    public void sendAllActionBar(@NotNull String msg) {
        for (Player pl : Bukkit.getOnlinePlayers())
            pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messageBuilder(msg)));
    }

    /**
     * Send a title message to a player.
     *
     * @param pl       The player to send the title message to.
     * @param title    The main title text.
     * @param subTitle The subtitle text.
     * @param fadeIn   Time in ticks for the title to fade in.
     * @param stay     Time in ticks for the title to stay on screen.
     * @param fadeOut  Time in ticks for the title to fade out.
     */
    public void sendTitle(@NotNull Player pl, @NotNull String title, @NotNull String subTitle, int fadeIn, int stay, int fadeOut) {
        pl.sendTitle(messageBuilder(title), messageBuilder(subTitle), fadeIn, stay, fadeOut);
    }

    /**
     * Send a title message to a player with default fade-in, stay, and fade-out times.
     *
     * @param pl       The player to send the title message to.
     * @param title    The main title text.
     * @param subTitle The subtitle text.
     */
    public void sendTitle(@NotNull Player pl, @NotNull String title, @NotNull String subTitle) {
        pl.sendTitle(messageBuilder(title), messageBuilder(subTitle), 10, 20, 10);
    }

    /**
     * Send a title message to all online players with default fade-in, stay, and fade-out times.
     *
     * @param title    The main title text.
     * @param subTitle The subtitle text.
     */
    public void sendAllTitle(@NotNull String title, @NotNull String subTitle) {
        for (Player pl : Bukkit.getOnlinePlayers())
            pl.sendTitle(messageBuilder(title), messageBuilder(subTitle), 20, 30, 20);
    }

    /**
     * Build a message with placeholders and hex color codes.
     *
     * @param msg The message to be formatted with placeholders and color codes.
     * @return The formatted message with placeholders and colors.
     */
    public String messageBuilder(@NotNull String msg) {
        return messageBuilder(msg, null);
    }

    public String messageBuilder(@NotNull String msg, @Nullable OfflinePlayer player) {
        msg = setPlaceholders(player, msg);
        msg = msg.replace("\\s", "\s")
             //   .replace("\\r", "\r")
            //    .replace("\\f", "\f")
              //  .replace("\\t", "\t")
              //  .replace("\\b", "\b")
                .replace("\\n", "\n");
        return hex(msg);
    }

    /**
     * Send a formatted message to all online players.
     *
     * @param msg The message to be sent to all online players.
     */
    public void sendAllMsg(@NotNull String msg) {
        for (Player pl : Bukkit.getOnlinePlayers())
            sendMsg(pl, msg);

    }

    /**
     * Set placeholders in a message using PlaceholderAPI if available.
     *
     * @param player The player for whom the placeholders should be replaced.
     * @param string The message string containing placeholders.
     * @return The message with replaced placeholders.
     */
    public @NotNull String setPlaceholders(@Nullable OfflinePlayer player, @NotNull String string) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            try {
                return PlaceholderAPI.setPlaceholders(player, string);
            } catch (Exception ignore) {
            }
        }
        return string;
    }

    /**
     * Play a sound to a player.
     *
     * @param pl    The player to play the sound to.
     * @param sound The name of the sound to play.
     */
    public void sendSound(@NotNull Player pl, @NotNull String sound) {
        try {
            pl.playSound(pl.getLocation(), Sound.valueOf(sound), 1, 1);
        } catch (IllegalArgumentException e) {
            error(e.getLocalizedMessage());
        }
    }

    /**
     * Play a sound to a player.
     *
     * @param pl    The player to play the sound to.
     * @param sound The Sound enum value to play.
     */
    public void sendSound(@NotNull Player pl, @NotNull Sound sound) {
        pl.playSound(pl.getLocation(), sound, 1, 1);
    }

    /**
     * Play a sound to a player with custom volume and pitch.
     *
     * @param pl     The player to play the sound to.
     * @param sound  The Sound enum value to play.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    public void sendSound(@NotNull Player pl, @NotNull Sound sound, float volume, float pitch) {
        pl.playSound(pl.getLocation(), sound, volume, pitch);
    }

    /**
     * Play a sound to all online players.
     *
     * @param sound The name of the sound to play.
     */
    public void sendAllSound(@NotNull String sound) {
        try {
            for (Player pl : Bukkit.getOnlinePlayers())
                pl.playSound(pl.getLocation(), Sound.valueOf(sound), 1, 1);
        } catch (IllegalArgumentException e) {
            error(e.getLocalizedMessage());
        }
    }

    /**
     * Play a sound to all online players.
     *
     * @param sound The Sound enum value to play.
     */
    public void sendAllSound(@NotNull Sound sound) {
        for (Player pl : Bukkit.getOnlinePlayers())
            pl.playSound(pl.getLocation(), sound, 1, 1);
    }

    /**
     * Play a sound to all online players with custom volume and pitch.
     *
     * @param sound  The Sound enum value to play.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    public void sendAllSound(@NotNull Sound sound, float volume, float pitch) {
        for (Player pl : Bukkit.getOnlinePlayers())
            pl.playSound(pl.getLocation(), sound, volume, pitch);
    }

    /**
     * Translate hex color codes in a message to ChatColor.
     *
     * @param message The message to be processed for hex color codes.
     * @return The message with hex color codes replaced by ChatColor.
     */
    private @NotNull String hex(@NotNull String message) {
        Matcher m = hexPattern.matcher(message);
        while (m.find())
            message = message.replace(m.group(), ChatColor.of(m.group(1)).toString());
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }
}