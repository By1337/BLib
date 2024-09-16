package org.by1337.blib.text;

/**
 * Utility class for formatting messages by replacing placeholders in the form of "{}" or "{index}"
 * with corresponding arguments provided in a varargs list. The placeholders are replaced in the order
 * of the arguments or by their specified index.
 */
public class MessageFormatter {

    /**
     * Formats a string by replacing placeholders in the form of "{}" or "{index}" with the corresponding
     * arguments provided. If the placeholder contains a numeric index, the argument at that index is used.
     * Otherwise, arguments are used in the order they appear.
     *
     * Placeholders without a matching argument are left as is. If there are more arguments than placeholders,
     * extra arguments are ignored. Handles nested braces by treating them as regular characters.
     *
     * @param source the string containing placeholders to be replaced
     * @param args the arguments to replace the placeholders with
     * @return the formatted string with placeholders replaced by the corresponding arguments
     */
    public static String apply(String source, Object... args) {
        if (args == null || args.length == 0 || source == null) return source;
        if (!source.contains("{")) return String.format(source, args);
        StringBuilder result = new StringBuilder();
        StringReader reader = new StringReader(source);

        int argPos = 0;
        boolean insideBraces = false;
        StringBuilder buffer = new StringBuilder();
        while (reader.hasNext()) {
            char c = reader.next();
            if (c == '{') {
                insideBraces = true;
                result.append(buffer);
                buffer.setLength(0);
                buffer.append(c);
            } else if (c == '}' && insideBraces) {
                int pos;
                if (buffer.length() > 1) {
                    String s = buffer.substring(1, buffer.length());
                    try {
                        pos = Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        pos = argPos;
                    }
                } else {
                    pos = argPos++;
                }
                if (pos < args.length) {
                    result.append(args[pos]);
                } else {
                    result.append(buffer);
                    result.append(c);
                }
                buffer.setLength(0);
                insideBraces = false;
            } else if (Character.isDigit(c) && insideBraces) {
                buffer.append(c);
            } else {
                result.append(buffer);
                buffer.setLength(0);
                result.append(c);
                insideBraces = false;
            }
        }
        result.append(buffer);
        return result.toString();
    }

    /**
     * A simple string reader to iterate over the characters in a string.
     * Provides methods for checking if there are more characters and retrieving the next character.
     */
    private static class StringReader {
        private final String string;
        private int pos;

        public StringReader(String string) {
            this.string = string;
        }

        public char next() {
            return string.charAt(pos++);
        }

        public boolean hasNext() {
            return pos < string.length();
        }

        public int getPos() {
            return pos;
        }
    }
}
