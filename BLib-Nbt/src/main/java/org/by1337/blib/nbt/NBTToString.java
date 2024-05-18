package org.by1337.blib.nbt;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.by1337.blib.nbt.impl.*;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;

public class NBTToString {

    public static String toString(NBT nbt, NBTToStringStyle style) {
        return appendNBT(nbt, style, new StringBuilder(), 0).toString();
    }

    @CanIgnoreReturnValue
    public static StringBuilder appendNBT(NBT nbt, NBTToStringStyle style, StringBuilder sb, int lvl) {
        if (nbt instanceof ByteArrNBT byteArrNBT) {
            appendArray(new ArrayIterator(byteArrNBT.getValue()), lvl, style, sb, "B", "B");
        } else if (nbt instanceof IntArrNBT intArrNBT) {
            appendArray(new ArrayIterator(intArrNBT.getValue()), lvl, style, sb, "I", "");
        } else if (nbt instanceof LongArrNBT longArrNBT) {
            appendArray(new ArrayIterator(longArrNBT.getValue()), lvl, style, sb, "L", "L");
        } else if (nbt instanceof ByteNBT byteNBT) {
            appendNumber(byteNBT.getValue(), style, sb, "b");
        } else if (nbt instanceof DoubleNBT doubleNBT) {
            appendNumber(doubleNBT.getValue(), style, sb, "d");
        } else if (nbt instanceof FloatNBT floatNBT) {
            appendNumber(floatNBT.getValue(), style, sb, "f");
        } else if (nbt instanceof IntNBT intNBT) {
            appendNumber(intNBT.getValue(), style, sb, "");
        } else if (nbt instanceof LongNBT longNBT) {
            appendNumber(longNBT.getValue(), style, sb, "L");
        } else if (nbt instanceof ShortNBT shortNBT) {
            appendNumber(shortNBT.getValue(), style, sb, "s");
        } else if (nbt instanceof StringNBT stringNBT) {
            sb.append(quoteAndEscape(stringNBT.getValue()));
        } else if (nbt instanceof CompoundTag compoundTag) {
            String line = style.isCompact() ? "" : "\n";
            String space = " ".repeat(style.isCompact() ? 0 : lvl + 4);
            sb.append("{").append(line);
            for (Map.Entry<String, NBT> entry : compoundTag.getTags().entrySet()) {
                sb.append(space);
                if (style.isJson()) {
                    sb.append(quoteAndEscape(entry.getKey()));
                } else {
                    sb.append(quoteAndEscapeIfNeed(entry.getKey()));
                }
                sb.append(":");
                if (!style.isCompact()) {
                    sb.append(" ");
                }
                appendNBT(entry.getValue(), style, sb, lvl + 4);
                sb.append(",").append(line);
            }
            if (!compoundTag.getTags().isEmpty()) {
                sb.setLength(sb.length() - (1 + line.length()));
            }
            sb.append(line).append(" ".repeat(style.isCompact() ? 0 : lvl)).append("}");
        } else if (nbt instanceof ListNBT listNBT) {
            var iterator = listNBT.iterator();
            appendArray(new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public String next() {
                    return appendNBT(iterator.next(), style, new StringBuilder(), lvl + 4).toString();
                }
            }, lvl, style, sb, "", "");
        }
        return sb;
    }

    @CanIgnoreReturnValue
    public static StringBuilder appendNumber(Number number, NBTToStringStyle style, StringBuilder sb, String nbtSymbol) {
        sb.append(number);
        if (!style.isJson()) {
            sb.append(nbtSymbol);
        }
        return sb;
    }

    @CanIgnoreReturnValue
    private static StringBuilder appendArray(Iterator<String> iterator, int lvl, NBTToStringStyle style, StringBuilder sb, String arraySymbol, String typeSymbol) {
        String space = " ".repeat(style.isCompact() ? 0 : lvl + 4);
        String line = style.isCompact() ? "" : "\n";
        sb.append("[").append(line);
        if (!style.isJson() && !arraySymbol.isEmpty()) {
            sb.append(space).append(arraySymbol).append(";").append(line);
        }
        boolean isEmpty = !iterator.hasNext();
        while (iterator.hasNext()) {
            sb.append(space).append(iterator.next());
            if (!style.isJson()) {
                sb.append(typeSymbol);
            }
            sb.append(",").append(line);
        }
        if (!isEmpty) {
            sb.setLength(sb.length() - (1 + line.length()));
            sb.append(line);
        }
        sb.append(" ".repeat(style.isCompact() ? 0 : lvl)).append("]");
        return sb;
    }


    public static String quoteAndEscapeIfNeed(String name) {
        return name.contains("'") ||
                name.contains("\"") ||
                name.contains("\\") ||
                name.contains("{") ||
                name.contains("}") ||
                name.contains(":") ||
                name.contains("[") ||
                name.contains("]") ||
                name.contains(";") ||
                name.contains(" ") ||
                name.contains(",") ? quoteAndEscape(name) : name;
    }

    public static String quoteAndEscape(String raw) {
        StringBuilder var1 = new StringBuilder(" ");
        int var2 = 0;

        for (int var3 = 0; var3 < raw.length(); ++var3) {
            char var4 = raw.charAt(var3);
            if (var4 == '\\') {
                var1.append('\\');
            } else if (var4 == '"' || var4 == '\'') {
                if (var2 == 0) {
                    var2 = var4 == '"' ? 39 : 34;
                }

                if (var2 == var4) {
                    var1.append('\\');
                }
            }

            var1.append(var4);
        }

        if (var2 == 0) {
            var2 = 34;
        }

        var1.setCharAt(0, (char) var2);
        var1.append((char) var2);
        return var1.toString();
    }

    private static class ArrayIterator implements Iterator<String> {
        private final Object array;
        private int pos;

        public ArrayIterator(Object array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return Array.getLength(array) > pos;
        }

        @Override
        public String next() {
            return String.valueOf(Array.get(array, pos++));
        }
    }
}
