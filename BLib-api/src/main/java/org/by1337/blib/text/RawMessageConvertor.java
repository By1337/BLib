package org.by1337.blib.text;

import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NBTParser;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.by1337.blib.nbt.impl.ListNBT;

public class RawMessageConvertor {

    public static String convertToLegacy(String rawMessage) {
        return convertToLegacy(rawMessage, false);
    }

    public static String convertToLegacy(String rawMessage, boolean noColors) {
        NBT tag = NBTParser.parseNBT(rawMessage);
        return toLegacy(tag, new StringBuilder(), noColors).toString();
    }

    private static StringBuilder toLegacy(NBT rawNBT, StringBuilder sb, boolean noColors) {
        if (rawNBT instanceof CompoundTag raw) {
            if (raw.has("text")) {
                String text = raw.getAsString("text");
                if (!text.isEmpty()) {
                    if (!noColors) {
                        if (raw.has("color")) {
                            String color = raw.getAsString("color");
                            for (StringFormatters value : StringFormatters.values()) {
                                if (value.isColor && value.miniMessagesSyntax.equals(color)) {
                                    sb.append("&").append(value.code);
                                    color = null;
                                    break;
                                }
                            }
                            if (color != null) {
                                sb.append("&").append(color);
                            }
                        }

                        for (StringFormatters value : StringFormatters.values()) {
                            if (value.isFormat && raw.has(value.miniMessagesSyntax)) {
                                String flag = raw.getAsString(value.miniMessagesSyntax);
                                if (flag.equals("true")) {
                                    sb.append("&").append(value.code);
                                }
                            }
                        }
                    }
                    sb.append(text);
                }
            }
            if (raw.has("extra")) {
                toLegacy(raw.get("extra"), sb, noColors);
            }
        } else if (rawNBT instanceof ListNBT listNBT) {
            for (NBT nbt : listNBT) {
                toLegacy(nbt, sb, noColors);
            }
        }
        return sb;
    }

    private enum StringFormatters {
        COLOR_BLACK(false, '0', "black", true),
        COLOR_DARK_BLUE(false, '1', "dark_blue", true),
        COLOR_DARK_GREEN(false, '2', "dark_green", true),
        COLOR_DARK_AQUA(false, '3', "dark_aqua", true),
        COLOR_DARK_RED(false, '4', "dark_red", true),
        COLOR_DARK_PURPLE(false, '5', "dark_purple", true),
        COLOR_GOLD(false, '6', "gold", true),
        COLOR_GRAY(false, '7', "gray", true),
        COLOR_DARK_GRAY(false, '8', "dark_gray", true),
        COLOR_BLUE(false, '9', "blue", true),
        COLOR_GREEN(false, 'a', "green", true),
        COLOR_AQUA(false, 'b', "aqua", true),
        COLOR_RED(false, 'c', "red", true),
        COLOR_LIGHT_PURPLE(false, 'd', "light_purple", true),
        COLOR_YELLOW(false, 'e', "yellow", true),
        COLOR_WHITE(false, 'f', "white", true),
        HEX_COLOR(true),
        RESET(false, 'r', "reset", true),
        BOLD(true, 'l', "bold", false),
        UNDERLINE(true, 'n', "underlined", false),
        STRIKETHROUGH(true, 'm', "strikethrough", false),
        ITALIC(true, 'o', "italic", false),
        OBFUSCATED(true, 'k', "obfuscated", false);
        public final boolean isFormat;
        public final char code;
        public final String miniMessagesSyntax;
        public final boolean isColor;

        StringFormatters(boolean isFormat, char code, String miniMessagesSyntax, boolean isColor) {
            this.isFormat = isFormat;
            this.code = code;
            this.miniMessagesSyntax = miniMessagesSyntax;
            this.isColor = isColor;
        }

        StringFormatters(boolean isColor) {
            this.isColor = isColor;
            this.miniMessagesSyntax = "";
            isFormat = false;
            code = '\n';
        }
    }
}
