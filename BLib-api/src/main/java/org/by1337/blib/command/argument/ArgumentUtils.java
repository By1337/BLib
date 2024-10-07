package org.by1337.blib.command.argument;

import org.by1337.blib.command.StringReader;
import org.by1337.blib.nbt.NBTToString;

import java.util.ArrayList;
import java.util.List;

public class ArgumentUtils {
    public static List<String> quoteAndEscapeIfNeeded(final List<String> arguments) {
        var list = new ArrayList<>(arguments);
        list.replaceAll(s -> s.contains(" ") ? NBTToString.quoteAndEscape(s, false) : s);
        return list;
    }
    public static String readString(StringReader reader) {
        StringBuilder sb = new StringBuilder();
        char c = reader.next();
        final boolean isEscaped = c == '"' || c == '\'';
        final char quoteChar;
        if (!isEscaped) {
            reader.back();
            quoteChar = 0;
        } else {
            quoteChar = c;
        }
        char last = 0;
        while (reader.hasNext()) {
            c = reader.next();
            if (isEscaped && c == quoteChar) {
                if (last == '\\') {
                    sb.setLength(sb.length() - 1);
                    sb.append(quoteChar);
                } else {
                    if (reader.hasNext()) {
                        char next = reader.next();
                        if (next == ' ') {
                            reader.back();
                            return sb.toString();
                        } else {
                            reader.back();
                            sb.append(quoteChar);
                        }
                    }
                }
            } else if (!isEscaped && c == ' ') {
                reader.back();
                return sb.toString();
            } else {
                sb.append(c);
            }
            last = c;
        }
        return sb.toString();
    }
}
