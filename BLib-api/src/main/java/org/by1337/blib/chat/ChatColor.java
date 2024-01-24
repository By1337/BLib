package org.by1337.blib.chat;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Represents a chat color with the option to convert to/from hexadecimal values.
 */
public class ChatColor {
    private Color color;

    /**
     * Constructs a ChatColor from a given Color object.
     *
     * @param color The Color object representing the chat color.
     */
    public ChatColor(@NotNull Color color) {
        this.color = color;
    }

    /**
     * Constructs a ChatColor from a hexadecimal color code.
     *
     * @param hex The hexadecimal color code (e.g., "#RRGGBB").
     */
    public ChatColor(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        int red = Integer.parseInt(hex.substring(0, 2), 16);
        int green = Integer.parseInt(hex.substring(2, 4), 16);
        int blue = Integer.parseInt(hex.substring(4, 6), 16);
        color = new Color(red, green, blue);
    }

    /**
     * Converts the ChatColor to its hexadecimal representation.
     *
     * @return The hexadecimal color code.
     */
    public String toHex() {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return String.format("#%02X%02X%02X", red, green, blue).toLowerCase();
    }

    @NotNull
    public Color getColor() {
        return color;
    }

    public org.bukkit.Color toBukkitColor() {
        return org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static String toHex(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return String.format("#%02X%02X%02X", red, green, blue).toLowerCase();
    }

    public static String toHex(org.bukkit.Color color) {
        return toHex(new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue()
        ));
    }

    public static ChatColor fromHex(String hex) {
        return switch (hex) {
            case "black" -> new ChatColor("#000000");
            case "red" -> new ChatColor("#FF5555");
            case "dark_blue" -> new ChatColor("#0000AA");
            case "light_purple" -> new ChatColor("#FF55FF");
            case "dark_green" -> new ChatColor("#00AA00");
            case "yellow" -> new ChatColor("#FFFF55");
            case "dark_aqua" -> new ChatColor("#00AAAA");
            case "white" -> new ChatColor("#FFFFFF");
            case "dark_red" -> new ChatColor("#AA0000");
            case "dark_purple" -> new ChatColor("#AA00AA");
            case "gold" -> new ChatColor("#FFAA00");
            case "gray" -> new ChatColor("#AAAAAA");
            case "dark_gray" -> new ChatColor("#555555");
            case "blue" -> new ChatColor("#5555FF");
            case "green" -> new ChatColor("#55FF55");
            case "aqua" -> new ChatColor("#55FFFF");
            default -> new ChatColor(hex);
        };
    }
}
