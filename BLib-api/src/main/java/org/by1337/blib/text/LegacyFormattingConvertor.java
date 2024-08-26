package org.by1337.blib.text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegacyFormattingConvertor {
    private static final Pattern hexPattern = Pattern.compile("[a-f0-9]{6}", Pattern.CASE_INSENSITIVE);

    public static String convert(String str) {
        LexemeBuffer buffer = new LexemeBuffer(parseExp(str.replace("&", "§")));
        return convert(buffer).replace("§", "&");
    }

    private static String convert(LexemeBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        while (buffer.hasNext()) {
            sb.append(parseText(buffer));
        }
        return sb.toString();
    }

    private static String parseText(LexemeBuffer buffer) {
        if (!buffer.hasNext()) return "";
        Lexeme lexeme = buffer.next();
        switch (lexeme.type) {
            case HEX_COLOR -> {
                return "<color:#" + lexeme.value + ">" + parseText(buffer) + "</color>";
            }
            case TEXT -> {
                var next = buffer.next();
                if (next.type.isColor) {
                    buffer.back();
                    return lexeme.value;
                } else {
                    buffer.back();
                    return lexeme.value + parseText(buffer);
                }
            }
            case EOF -> {
                return "";
            }
            default -> {
                return "<" + lexeme.type.miniMessagesSyntax + ">"
                       + parseText(buffer) + "</" + lexeme.type.miniMessagesSyntax + ">";
            }
        }
    }

    enum LexemeType {
        COLOR_BLACK(false, '0', "black", true), // §0 -> <black>
        COLOR_DARK_BLUE(false, '1', "dark_blue", true), // §1 -> <dark_blue>
        COLOR_DARK_GREEN(false, '2', "dark_green", true), // §2 -> <dark_green>
        COLOR_DARK_AQUA(false, '3', "dark_aqua", true), // §3 -> <dark_aqua>
        COLOR_DARK_RED(false, '4', "dark_red", true), // §4 -> <dark_red>
        COLOR_DARK_PURPLE(false, '5', "dark_purple", true), // §5 -> <dark_purple>
        COLOR_GOLD(false, '6', "gold", true), // §6 -> <gold>
        COLOR_GRAY(false, '7', "gray", true), // §7 -> <gray>
        COLOR_DARK_GRAY(false, '8', "dark_gray", true), // §8 -> <dark_gray>
        COLOR_BLUE(false, '9', "blue", true), // §9 -> <blue>
        COLOR_GREEN(false, 'a', "green", true), // §a -> <green>
        COLOR_AQUA(false, 'b', "aqua", true), // §b -> <aqua>
        COLOR_RED(false, 'c', "red", true), // §c -> <red>
        COLOR_LIGHT_PURPLE(false, 'd', "light_purple", true), // §d -> <light_purple>
        COLOR_YELLOW(false, 'e', "yellow", true), // §e -> <yellow>
        COLOR_WHITE(false, 'f', "white", true), // §f -> <white>
        HEX_COLOR(true), // §#123456 -> <color:#123456>
        RESET(false, 'r', "reset", true), // §r <reset>
        BOLD(true, 'l', "b", false), // §l <b> format
        UNDERLINE(true, 'n', "u", false), // §n <u> format
        STRIKETHROUGH(true, 'm', "st", false), // §m <st>
        ITALIC(true, 'o', "i", false), // §o <i> format
        OBFUSCATED(true, 'k', "obf", false), // §k <obf> format
        TEXT(false),
        EOF(false);
        final boolean isFormat;
        final char code;
        final String miniMessagesSyntax;
        final boolean isColor;

        LexemeType(boolean isFormat, char code, String miniMessagesSyntax, boolean isColor) {
            this.isFormat = isFormat;
            this.code = code;
            this.miniMessagesSyntax = miniMessagesSyntax;
            this.isColor = isColor;
        }

        LexemeType(boolean isColor) {
            this.isColor = isColor;
            this.miniMessagesSyntax = "";
            isFormat = false;
            code = '\n';
        }

        static LexemeType byColor(char color) {
            for (LexemeType value : values()) {
                if (value.code == color) {
                    return value;
                }
            }
            return null;
        }
    }

    private static List<Lexeme> parseExp(String expText) {
        BufferedLexemeAdder buffer = new BufferedLexemeAdder();
        int pos = 0;

        StringBuilder text = new StringBuilder();

        while (pos < expText.length()) {
            char c = expText.charAt(pos);
            if (c == '§') {
                pos++;
                if (pos >= expText.length()) {
                    text.append(c);
                    continue;
                }
                char code = expText.charAt(pos);
                LexemeType type = LexemeType.byColor(code);

                if (type != null) {
                    if (!text.isEmpty()) {
                        buffer.add(new Lexeme(LexemeType.TEXT, text.toString()));
                        text = new StringBuilder();
                    }

                    buffer.add(new Lexeme(type, "§" + code));
                    pos++;
                } else if (code == '#') {
                    if (pos + 6 >= expText.length()) {
                        text.append(c);
                    } else {
                        pos++;
                        String hexColor = expText.substring(pos, pos + 6);
                        if (isHex(hexColor)) {
                            if (!text.isEmpty()) {
                                buffer.add(new Lexeme(LexemeType.TEXT, text.toString()));
                                text = new StringBuilder();
                            }
                            buffer.add(new Lexeme(LexemeType.HEX_COLOR, hexColor));
                            pos += 6;
                        } else {
                            text.append(c);
                            pos--;
                        }
                    }
                } else if (code == 'x') {
                    if (pos + 12 >= expText.length()) {
                        text.append(c);
                    } else {
                        pos++;
                        String hexColor = expText.substring(pos, pos + 12).replace("§", "");
                        if (isHex(hexColor)) {
                            if (!text.isEmpty()) {
                                buffer.add(new Lexeme(LexemeType.TEXT, text.toString()));
                                text = new StringBuilder();
                            }
                            buffer.add(new Lexeme(LexemeType.HEX_COLOR, hexColor));
                            pos += 12;
                        } else {
                            text.append(c);
                            pos--;
                        }
                    }
                } else {
                    text.append(c);
                    pos++;
                }
            } else {
                text.append(c);
                pos++;
            }
        }
        if (!text.isEmpty()) {
            buffer.add(new Lexeme(LexemeType.TEXT, text.toString()));
        }
        buffer.add(new Lexeme(LexemeType.EOF, ' '));
        return buffer.lexemes;
    }

    private static boolean isHex(String hex) {
        Matcher m = hexPattern.matcher(hex);
        return m.find();
    }

    static class Lexeme {
        LexemeType type;
        String value;

        public Lexeme(LexemeType type, String value) {
            this.type = type;
            this.value = value;
        }

        public Lexeme(LexemeType type, Character value) {
            this.type = type;
            this.value = String.valueOf(value);
        }

        @Override
        public String toString() {
            return "Lexeme{" +
                   "type=" + type +
                   ", value='" + value + '\'' +
                   '}';
        }
    }

    static class BufferedLexemeAdder {
        final List<Lexeme> lexemes = new ArrayList<>();
        final List<Lexeme> buffer = new ArrayList<>();

        void add(Lexeme lexeme) {
            if (lexeme.type == LexemeType.TEXT) {
                if (lexeme.value.isBlank()) {
                    if (!buffer.isEmpty()) {
                        if (buffer.get(buffer.size() - 1).type.isColor) {
                            buffer.clear();
                            buffer.add(new Lexeme(LexemeType.RESET, ""));
                        }
                    }
                }
                lexemes.addAll(buffer);
                lexemes.add(lexeme);
                buffer.clear();
            } else if (lexeme.type.isColor) {
                buffer.clear();
                buffer.add(lexeme);
            } else if (lexeme.type.isFormat) {
                buffer.add(lexeme);
            } else {
                lexemes.add(lexeme);
            }
        }
    }

    static class LexemeBuffer {
        private int pos;

        public List<Lexeme> lexemes;

        public LexemeBuffer(List<Lexeme> lexemes) {
            this.lexemes = lexemes;
        }

        public Lexeme next() {
            return lexemes.get(pos++);
        }

        public boolean hasNext() {
            return lexemes.size() > pos;
        }

        public LexemeBuffer back() {
            pos--;
            return this;
        }

        public Lexeme current() {
            return lexemes.get(pos);
        }

        public LexemeBuffer add() {
            pos++;
            return this;
        }


        public int getPos() {
            return pos;
        }

        @Override
        public String toString() {
            return "LexemeBuffer{" +
                   "pos=" + pos +
                   ", lexemes=" + lexemes +
                   '}';
        }
    }

    private static String reportMessage(String text, int pos) {
        final int len = text.length();
        int start = Math.max(pos - 10, 0);
        int end = Math.min(pos + 10, len);
        System.out.println("start=" + start + " end=" + end);

        String startText = text.substring(start, pos);

        return startText +
               text.substring(pos, end) +
               "\n" + " ".repeat(startText.length()) + "^";
    }

    public static class LegacyTextParseException extends IllegalArgumentException {

        public LegacyTextParseException() {
            super();
        }

        public LegacyTextParseException(String s, Object... objects) {
            super(String.format(s, objects));
        }

        public LegacyTextParseException(String s) {
            super(s);
        }

        public LegacyTextParseException(String message, Throwable cause) {
            super(message, cause);
        }

        public LegacyTextParseException(Throwable cause) {
            super(cause);
        }
    }

}
