package org.by1337.blib.chat.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.by1337.blib.BLib;
import org.by1337.blib.text.ComponentToANSI;
import org.by1337.blib.text.LegacyConvertor;
import org.by1337.blib.text.LegacyFormattingConvertor;
import org.by1337.blib.translation.Translation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Message class is responsible for handling and sending formatted messages to both the console log and players.
 */
public class Message {
    @Deprecated(since = "1.0.7")
    private final Pattern hexPattern;
    private final Logger logger;
    @Deprecated(since = "1.0.7.1")
    private LogLevel logLevel = LogLevel.NONE;
    @Nullable
    private Translation translation;

    /**
     * Constructs a Message instance with a default color pattern.
     *
     * @param logger The logger to use for output.
     */
    public Message(@NotNull Logger logger) {
        this.logger = logger;
        hexPattern = Pattern.compile("&(#[a-f0-9]{6})", Pattern.CASE_INSENSITIVE);
    }

    public Message(@NotNull Logger logger, Reader translationReader) {
        this.logger = logger;
        hexPattern = Pattern.compile("&(#[a-f0-9]{6})", Pattern.CASE_INSENSITIVE);
        translation = Translation.fromJson(translationReader, this);
    }

    /**
     * Constructs a Message instance with a custom color pattern.
     *
     * @param hexPattern The custom color pattern to use.
     * @param logger     The logger to use for output.
     * @deprecated hexPattern is no longer used as there has been a transition to MiniMessages.
     * Support for old color formats still remains. Refer to {@link LegacyFormattingConvertor} to understand which color formats are supported.
     */
    @Deprecated(since = "1.0.7")
    public Message(@NotNull Pattern hexPattern, @NotNull Logger logger) {
        this.hexPattern = hexPattern;
        this.logger = logger;
    }

    @Nullable
    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(@Nullable Translation translation) {
        this.translation = translation;
    }

    /**
     * Sends a formatted message to a command sender.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg    The message string to be sent.
     */
    public void sendMsg(@NotNull CommandSender sender, @NotNull String msg) {
        OfflinePlayer player = sender instanceof OfflinePlayer o ? o : null;
        sender.sendMessage(componentBuilder(msg, player));
    }

    public void sendMsg(@NotNull CommandSender sender, @NotNull Component msg) {
        sender.sendMessage(translate(msg, sender instanceof OfflinePlayer offlinePlayer ? offlinePlayer : null));
    }

    public void sendMsg(@NotNull CommandSender sender, @NotNull TranslatableComponent msg, Object... objects) {
        sender.sendMessage(buildTranslatableAndTranslate(msg, sender instanceof OfflinePlayer offlinePlayer ? offlinePlayer : null, objects));
    }

    /**
     * Sends a formatted message to a command sender with variable replacements using format placeholders.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg    The message string to be sent with format placeholders.
     * @param format The objects used for formatting the message.
     */
    public void sendMsg(@NotNull CommandSender sender, @NotNull String msg, @NotNull Object... format) {
        OfflinePlayer player = sender instanceof OfflinePlayer o ? o : null;
        sender.sendMessage(componentBuilder(String.format(msg, format), player));
    }


    /**
     * Sends a raw message to a command sender using a ComponentBuilder.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg    The ComponentBuilder containing the raw message.
     */
    @Deprecated(since = "1.0.7", forRemoval = true)
    public void sendRawMsg(@NotNull CommandSender sender, @NotNull org.by1337.blib.chat.ComponentBuilder msg) {
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
    @Deprecated(since = "1.0.7.1")
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
    @Deprecated(since = "1.0.7.1")
    public void debug(@Nullable String msg, @NotNull LogLevel level, Object... objects) {
        if (level.getLvl() < logLevel.getLvl()) {
            logger.info(String.format("[DEBUG] " + msg, objects));
        }
    }

    @Deprecated(since = "1.0.7.1")
    public void logger(@NotNull String msg) {
        logger.log(Level.INFO, msg);
    }

    @Deprecated(since = "1.0.7.1")
    public void logger(@NotNull String msg, @NotNull Object... objects) {
        logger.log(Level.INFO, String.format(msg, objects));
    }


    //// error start
    public void error(@Nullable Throwable t) {
        logger.log(Level.SEVERE, "", t);
    }

    public void error(@NotNull String msg) {
        logger.log(Level.SEVERE, msg);
    }

    public void error(@NotNull String msg, @Nullable Throwable t) {
        logger.log(Level.SEVERE, msg, t);
    }

    public void error(@NotNull String msg, Object... objects) {
        logger.log(Level.SEVERE, String.format(msg, objects));
    }

    public void error(@NotNull String msg, @Nullable Throwable t, Object... objects) {
        logger.log(Level.SEVERE, String.format(msg, objects), t);
    }

    public void error(@NotNull Component msg) {
        logger.log(Level.SEVERE, ComponentToANSI.get().convert(translate(msg, null)));
    }

    public void error(@NotNull Component msg, @Nullable Throwable t) {
        logger.log(Level.SEVERE, ComponentToANSI.get().convert(translate(msg, null)), t);
    }

    public void error(@NotNull TranslatableComponent msg, @Nullable Throwable t, Object... objects) {
        error(buildTranslatableAndTranslate(msg, objects), t);
    }

    public void error(@NotNull TranslatableComponent msg, Object... objects) {
        error(buildTranslatableAndTranslate(msg, objects));
    }
    //// error end


    @NotNull
    public Component buildTranslatableAndTranslate(@NotNull TranslatableComponent msg, Object... objects) {
        return buildTranslatableAndTranslate(msg, null, objects);
    }

    public Component buildTranslatableAndTranslate(@NotNull TranslatableComponent msg, @Nullable OfflinePlayer player, Object... objects) {
        List<Component> components = new ArrayList<>();
        for (Object object : objects) {
            components.add(Component.text(String.valueOf(object)));
        }
        return translate(msg.args(components), player);
    }


    //// log start
    public void log(@Nullable Throwable t) {
        logger.log(Level.INFO, "", t);
    }

    public void log(@NotNull String msg) {
        logger.log(Level.INFO, msg);
    }

    public void log(@NotNull String msg, Object... objects) {
        logger.log(Level.INFO, String.format(msg, objects));
    }

    public void log(@NotNull String msg, @Nullable Throwable t) {
        logger.log(Level.INFO, msg, t);
    }

    public void log(@NotNull String msg, @Nullable Throwable t, Object... objects) {
        logger.log(Level.INFO, String.format(msg, objects), t);
    }

    public void log(@NotNull Component msg) {
        logger.log(Level.INFO, ComponentToANSI.get().convert(translate(msg, null)));
    }

    public void log(@NotNull Component msg, @Nullable Throwable t) {
        logger.log(Level.INFO, ComponentToANSI.get().convert(translate(msg, null)), t);
    }

    public void log(@NotNull TranslatableComponent msg, Object... objects) {
        log(buildTranslatableAndTranslate(msg, objects));
    }

    public void log(@NotNull TranslatableComponent msg, @Nullable Throwable t, Object... objects) {
        log(buildTranslatableAndTranslate(msg, objects), t);
    }
    //// log end

    //// warning start
    public void warning(@Nullable Throwable t) {
        logger.log(Level.WARNING, "", t);
    }

    public void warning(@NotNull String msg) {
        logger.log(Level.WARNING, msg);
    }

    public void warning(@NotNull String msg, Object... objects) {
        logger.log(Level.WARNING, String.format(msg, objects));
    }

    public void warning(@NotNull String msg, @Nullable Throwable t) {
        logger.log(Level.WARNING, msg, t);
    }

    public void warning(@NotNull String msg, @Nullable Throwable t, Object... objects) {
        logger.log(Level.WARNING, String.format(msg, objects), t);
    }

    public void warning(@NotNull Component msg) {
        logger.log(Level.WARNING, ComponentToANSI.get().convert(translate(msg, null)));
    }

    public void warning(@NotNull Component msg, @Nullable Throwable t) {
        logger.log(Level.WARNING, ComponentToANSI.get().convert(translate(msg, null)), t);
    }

    public void warning(@NotNull TranslatableComponent msg, Object... objects) {
        warning(buildTranslatableAndTranslate(msg, objects));
    }

    public void warning(@NotNull TranslatableComponent msg, @Nullable Throwable t, Object... objects) {
        warning(buildTranslatableAndTranslate(msg, objects), t);
    }
    //// warning end

    /**
     * Send a formatted message to all online players who are operators.
     *
     * @param msg The message to be sent to online operators.
     */
    public void sendAllOp(@NotNull String msg) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.isOp()) {
                sendMsg(pl, componentBuilder(msg));
            }
        }
    }

    public void sendAllOp(@NotNull Component msg) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.isOp()) {
                sendMsg(pl, msg);
            }
        }
    }

    /**
     * Send an action bar message to a player.
     *
     * @param pl  The player to send the action bar message to.
     * @param msg The message to be displayed in the action bar.
     */
    public void sendActionBar(@NotNull Player pl, @NotNull String msg) {
        pl.sendActionBar(componentBuilder(msg, pl));
    }

    public void sendActionBar(@NotNull Player pl, @NotNull Component msg) {
        pl.sendActionBar(translate(msg, pl));
    }

    /**
     * Send an action bar message to all online players.
     *
     * @param msg The message to be displayed in the action bar of all online players.
     */
    public void sendAllActionBar(@NotNull String msg) {
        for (Player pl : Bukkit.getOnlinePlayers())
            sendActionBar(pl, msg);
    }

    public void sendAllActionBar(@NotNull Component msg) {
        for (Player pl : Bukkit.getOnlinePlayers())
            sendActionBar(pl, msg);
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
        sendTitle(pl, componentBuilder(title), componentBuilder(subTitle), fadeIn, stay, fadeOut);
    }

    public void sendTitle(@NotNull Player pl, @NotNull Component title, @NotNull Component subTitle, int fadeIn, int stay, int fadeOut) {
        pl.showTitle(
                Title.title(
                        translate(title, pl),
                        translate(subTitle, pl),
                        Title.Times.of(
                                Ticks.duration(fadeIn),
                                Ticks.duration(stay),
                                Ticks.duration(fadeOut)
                        )
                )
        );
    }

    /**
     * Send a title message to a player with default fade-in, stay, and fade-out times.
     *
     * @param pl       The player to send the title message to.
     * @param title    The main title text.
     * @param subTitle The subtitle text.
     */
    public void sendTitle(@NotNull Player pl, @NotNull String title, @NotNull String subTitle) {
        sendTitle(pl, title, subTitle, 10, 70, 20);
    }

    public void sendTitle(@NotNull Player pl, @NotNull Component title, @NotNull Component subTitle) {
        sendTitle(pl, title, subTitle, 10, 70, 20);
    }

    /**
     * Send a title message to all online players with default fade-in, stay, and fade-out times.
     *
     * @param title    The main title text.
     * @param subTitle The subtitle text.
     */
    public void sendAllTitle(@NotNull String title, @NotNull String subTitle) {
        for (Player pl : Bukkit.getOnlinePlayers())
            sendTitle(pl, title, subTitle);
    }

    public void sendAllTitle(@NotNull Component title, @NotNull Component subTitle) {
        for (Player pl : Bukkit.getOnlinePlayers())
            sendTitle(pl, title, subTitle);
    }

    /**
     * Build a message with placeholders and hex color codes.
     *
     * @param msg The message to be formatted with placeholders and color codes.
     * @return The formatted message with placeholders and colors.
     */
    @Deprecated(since = "1.0.7")
    public String messageBuilder(@NotNull String msg) {
        return messageBuilder(msg, null);
    }

    public Component componentBuilder(@NotNull String msg) {
        return componentBuilder(msg, null);
    }

    public Component componentBuilder(@NotNull String msg, @Nullable OfflinePlayer player) {
        return translate(componentBuilderNoTranslate(msg, player), player);
    }

    public Component translate(Component component, @Nullable OfflinePlayer player) {
        if (translation != null) {
            if (player != null && player.isOnline()) {
                return translation.translate(component, player.getPlayer().locale(), player);
            }
            return translation.translate(component, null, player);
        }
        return component;
    }

    public Component componentBuilderNoTranslate(@NotNull String msg, @Nullable OfflinePlayer player) {
        msg = setPlaceholders(player, msg);
        msg = msg.replace("\\s", "\s").replace("\\n", "\n");
        return LegacyConvertor.convert0(msg);
    }


    public String messageBuilder(@NotNull String msg, @Nullable OfflinePlayer player) {
        msg = setPlaceholders(player, msg);
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

    public void sendAllMsg(@NotNull Component msg) {
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
    @Deprecated(since = "1.0.7")
    private @NotNull String hex(@NotNull String message) {
        Matcher m = hexPattern.matcher(message);
        while (m.find())
            message = message.replace(m.group(), ChatColor.of(m.group(1)).toString());
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getContent(Component component) {
        if (!(component instanceof net.kyori.adventure.text.TextComponent textComponent)) return "";
        StringBuilder sb = new StringBuilder();

        sb.append(textComponent.content());
        for (Component child : textComponent.children()) {
            sb.append(getContent(child));
        }
        return sb.toString();
    }

    @Deprecated(since = "1.0.7.1")
    public LogLevel getLogLevel() {
        return logLevel;
    }

    @Deprecated(since = "1.0.7.1")
    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }
}