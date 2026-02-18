package org.by1337.blib.nbt;

import org.by1337.blib.nbt.impl.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class NBTParser {

    public static CompoundTag parseJson(String json) {
        return parseAsCompoundTag(json, new NBTParserContext().setAllowMultipleTypeInList(true).setDoNotConvertListToArray(true));
    }

    private static CompoundTag parseAsCompoundTag(File file) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return parseAsCompoundTag(new String(encoded, StandardCharsets.UTF_8));
    }


    public static CompoundTag parseAsCompoundTag(String raw) {
        return parseAsCompoundTag(raw, NBTParserContext.empty());
    }

    public static CompoundTag parseAsCompoundTag(String raw, NBTParserContext context) {
        List<Lexeme> list = parseExp(raw);

        LexemeBuffer buffer = new LexemeBuffer(list);

        return parseCompoundTag(buffer, context);
    }


    private static CompoundTag parseCompoundTag(LexemeBuffer buffer, NBTParserContext context) {
        if (buffer.next().type != LexemeType.STRUCT_OPEN) {
            throw new ParseException("should start with %s", "{");
        }
        CompoundTag compoundTag = new CompoundTag();
        String name = null;

        main:
        while (true) {
            Lexeme lexeme = buffer.next();
            switch (lexeme.type) {
                case STRING -> {
                    if (name == null)
                        name = lexeme.value;
                    else {
                        buffer.back();
                        compoundTag.putTag(name, parseNBT(buffer, context));
                    }
                }
                case STRUCT_OPEN -> {
                    if (name == null) {
                        throw new ParseException("missing tag name! " + lexeme);
                    }
                    buffer.back();
                    compoundTag.putTag(name, parseCompoundTag(buffer, context));
                }
                case ELEMENT_SEPARATOR -> {
                    name = null;
                }
                case NAME_VALUE_SEPARATOR -> {

                }
                case LIST_OPEN -> {
                    if (name == null) {
                        throw new ParseException("missing tag name! " + lexeme);
                    }
                    buffer.back();
                    compoundTag.putTag(name, parseList(buffer, context));
                }
                case STRUCT_CLOSE -> {
                    break main;
                }
                case NUMBER -> {
                    buffer.back();
                    if (name == null) {
                        throw new ParseException("missing tag name! " + lexeme);
                    }
                    compoundTag.putTag(name, parseNBT(buffer, context));
                }
                default -> {
                    throw new ParseException("Unexpected token: %s", lexeme);
                }
            }
        }

        return compoundTag;
    }

    public static NBT parseNBT(String raw) {
        return parseNBT(raw, NBTParserContext.empty());
    }

    public static NBT parseNBT(String raw, NBTParserContext context) {
        return parseNBT(new LexemeBuffer(parseExp(raw)), context);
    }

    static NBT parseNBT(LexemeBuffer buffer, NBTParserContext context) {
        Lexeme lexeme = buffer.next();
        LexemeType type = lexeme.type;
        if (type == LexemeType.LIST_OPEN) {
            buffer.back();
            return parseList(buffer, context);
        }
        if (type == LexemeType.STRUCT_OPEN) {
            buffer.back();
            return parseCompoundTag(buffer, context);
        }
        String val = lexeme.value;
        if (type == LexemeType.NUMBER) {
            Lexeme next = buffer.next();
            switch (next.type) {
                case TYPE_INT -> {
                    return IntNBT.valueOf(Integer.parseInt(val));
                }
                case TYPE_BYTE -> {
                    return ByteNBT.valueOf(Byte.parseByte(val));
                }
                case TYPE_DOUBLE -> {
                    return new DoubleNBT(Double.parseDouble(val));
                }
                case TYPE_LONG -> {
                    return LongNBT.valueOf(Long.parseLong(val));
                }
                case TYPE_SHORT -> {
                    return ShortNBT.valueOf(Short.parseShort(val));
                }
                case TYPE_FLOAT -> {
                    return new FloatNBT(Float.parseFloat(val));
                }
                default -> {
                    buffer.back();
                }
            }
            if (val.contains(".")) {
                return new DoubleNBT(Double.parseDouble(val));
            }
            try {
                return IntNBT.valueOf(Integer.parseInt(val));
            } catch (NumberFormatException ignore) {
                try {
                    return LongNBT.valueOf(Long.parseLong(val));
                } catch (NumberFormatException ignore2) {
                    return new StringNBT(val);
                }
            }
        } else if (type == LexemeType.STRING) {
            return new StringNBT(val);
        }
        throw new ParseException("current lexeme is not number! ", lexeme);
    }

    public static NBT parseList(String raw) {
        return parseList(raw, NBTParserContext.empty());
    }

    public static NBT parseList(String raw, NBTParserContext context) {
        return parseList(new LexemeBuffer(parseExp(raw)), context);
    }

    private static NBT parseList(LexemeBuffer buffer, NBTParserContext context) {
        if (buffer.next().type != LexemeType.LIST_OPEN) {
            throw new ParseException("Unexpected character: %s", buffer);
        }
        List<NBT> list = new ArrayList<>();
        LexemeType type = null;
        boolean oneType = true;
        main:
        while (true) {
            Lexeme lexeme = buffer.next();
            switch (lexeme.type) {
                case LIST_OPEN -> {
                    buffer.back();
                    list.add(parseList(buffer, context));
                }
                case STRUCT_OPEN -> {
                    buffer.back();
                    list.add(parseCompoundTag(buffer, context));
                }
                case LIST_CLOSE -> {
                    break main;
                }
                case ELEMENT_SEPARATOR -> {
                    // ignore - не хотелось бы
                }
                case TYPE_INT,
                        TYPE_BYTE,
                        TYPE_DOUBLE,
                        TYPE_LONG,
                        TYPE_SHORT,
                        TYPE_FLOAT -> {
                    if (type !=  lexeme.type && oneType){
                        oneType = false;
                    }
                    type = lexeme.type;
                    Lexeme next = buffer.next();
                    if (next.type != LexemeType.ARR_TYPE_SEPARATOR) {
                        throw new ParseException("Unexpected token: %s", lexeme);
                    }
                }
                default -> {
                    buffer.back();
                    list.add(parseNBT(buffer, context));
                }
            }
        }
        if (!oneType){
            type = null;
        }

        if (list.isEmpty() && type == null) {
            ListNBT result = new ListNBT(new ArrayList<>(list.size()), context.isAllowMultipleTypeInList());
            list.forEach(result::addAndUnwrap);
            return result;
        }


        if (!list.isEmpty() && !context.isAllowMultipleTypeInList()) {
            final Class<?> clazz = list.get(0).getClass();
            for (NBT nbt : list) {
                if (nbt.getClass() != clazz) {
                    throw new ParseException("The list is non-homogeneous; its elements do not share the same class. [%s]", list);
                }
            }
        }
        if (context.isDoNotConvertListToArray()) {
            ListNBT result = new ListNBT(new ArrayList<>(list.size()), context.isAllowMultipleTypeInList());
            list.forEach(result::addAndUnwrap);
            return result;
        }

        if (type == LexemeType.TYPE_BYTE) {
            byte[] arr = new byte[list.size()];
            int i = 0;
            for (NBT nbt : list) {
                arr[i] = ((ByteNBT) nbt).getValue();
                i++;
            }
            return new ByteArrNBT(arr);
        } else if (type == LexemeType.TYPE_LONG) {
            long[] arr = new long[list.size()];
            int i = 0;
            for (NBT nbt : list) {
                arr[i] = ((LongNBT) nbt).getValue();
                i++;
            }
            return new LongArrNBT(arr);
        } else if (type == LexemeType.TYPE_INT) {
            int[] arr = new int[list.size()];
            int i = 0;
            for (NBT nbt : list) {
                arr[i] = ((IntNBT) nbt).getValue();
                i++;
            }
            return new IntArrNBT(arr);
        }
        ListNBT result = new ListNBT(new ArrayList<>(list.size()), context.isAllowMultipleTypeInList());
        list.forEach(result::addAndUnwrap);
        return result;
    }

    static List<Lexeme> parseExp(String expText) throws ParseException {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int pos = 0;
        boolean textExpected = false;
        boolean isSingleQuote = false;
        StringBuilder text = new StringBuilder();
        StringBuilder digit = new StringBuilder();
        boolean lastIsDigit = false;
        char last = ' ';
        main:
        while (pos < expText.length()) {
            lastIsDigit = lastIsDigit ? (last == '.' || Character.isDigit(last)) : Character.isDigit(last);
            char c = expText.charAt(pos);
            if (textExpected) {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    switch (c) {
                        case '\\' -> {
                            if (pos + 1 < expText.length()) {
                                char next = expText.charAt(pos + 1);
                                if (next == '\\') {
                                    sb.append('\\');
                                    pos += 2;

                                } else if (!isSingleQuote && next == '"') {
                                    sb.append('"');
                                    pos += 2;
                                } else if (next == '\'') {
                                    sb.append("'");
                                    pos += 2;
                                } else if (next == 'u' && pos + 5 < expText.length()) {
                                    String unicode = expText.substring(pos + 2, pos + 6);
                                    try {
                                        char unicodeChar = (char) Integer.parseInt(unicode, 16);
                                        sb.append(unicodeChar);
                                        pos += 6;
                                    } catch (NumberFormatException e) {
                                        throw new ParseException("Invalid Unicode escape sequence at position %s\n%s", pos, ParserExceptionReport.report(expText, pos));
                                    }
                                } else if (next == 'n') {
                                    sb.append("\n");
                                    pos += 2;
                                } else if (next == 'r') {
                                    sb.append("\r");
                                    pos += 2;
                                } else if (next == 't') {
                                    sb.append("\t");
                                    pos += 2;
                                } else if (next == 's') {
                                    sb.append(" ");
                                    pos += 2;
                                } else if (next == 'f') {
                                    sb.append("\f");
                                    pos += 2;
                                } else if (next == 'b') {
                                    sb.append("\b");
                                    pos += 2;
                                } else {
                                    throw new ParseException("Expect '%s' or '%s', а не '%s', at %s\n%s", "\\", isSingleQuote ? "\\'" : "\"", expText.charAt(pos + 1), pos + 1, ParserExceptionReport.report(expText, pos));
                                }
                            } else {
                                throw new ParseException("The string ends unexpectedly with the character '%s' at position '%s'.\n%s", c, pos, ParserExceptionReport.report(expText, pos));
                            }
                        }
                        case '"' -> {
                            if (isSingleQuote) {
                                sb.append('"');
                                pos++;
                                break;
                            }
                            textExpected = false;
                            lexemes.add(new Lexeme(LexemeType.STRING, sb.toString()));
                            pos++;
                            continue main;
                        }
                        case '\'' -> {
                            if (!isSingleQuote) {
                                sb.append("'");
                                pos++;
                                break;
                            }
                            textExpected = false;
                            lexemes.add(new Lexeme(LexemeType.STRING, sb.toString()));
                            pos++;
                            continue main;
                        }

                        default -> {
                            sb.append(c);
                            pos++;
                        }

                    }
                    if (pos < expText.length()) {
                        c = expText.charAt(pos);
                    } else {
                        throw new ParseException("The string ends unexpectedly with the character '%s' at position '%s'.\n%s", c, pos, ParserExceptionReport.report(expText, pos));
                    }
                }
            }
            switch (c) {
                case '{' -> {
                    if (!text.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.STRING, text.toString()));
                        text = new StringBuilder();
                    }
                    if (!digit.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                    }
                    lexemes.add(new Lexeme(LexemeType.STRUCT_OPEN, c));
                    pos++;
                }
                case '}' -> {
                    if (!text.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.STRING, text.toString()));
                        text = new StringBuilder();
                    }
                    if (!digit.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                    }
                    lexemes.add(new Lexeme(LexemeType.STRUCT_CLOSE, c));
                    pos++;
                }
                case ',' -> {
                    if (!text.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.STRING, text.toString()));
                        text = new StringBuilder();
                    }
                    if (!digit.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                    }
                    lexemes.add(new Lexeme(LexemeType.ELEMENT_SEPARATOR, c));
                    pos++;
                }
                case ':' -> {
                    if (!text.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.STRING, text.toString()));
                        text = new StringBuilder();
                    }
                    if (!digit.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                    }
                    lexemes.add(new Lexeme(LexemeType.NAME_VALUE_SEPARATOR, c));
                    pos++;
                }
                case '[' -> {
                    if (!text.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.STRING, text.toString()));
                        text = new StringBuilder();
                    }
                    if (!digit.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                    }
                    lexemes.add(new Lexeme(LexemeType.LIST_OPEN, c));
                    pos++;
                }
                case ']' -> {
                    if (!text.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.STRING, text.toString()));
                        text = new StringBuilder();
                    }
                    if (!digit.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                    }
                    lexemes.add(new Lexeme(LexemeType.LIST_CLOSE, c));
                    pos++;
                }
                case 'I', 'i' -> {
                    if (last == '[') {
                        lexemes.add(new Lexeme(LexemeType.TYPE_INT, c));
                        pos++;
                        break;
                    }
                    if (lastIsDigit) {
                        if (digit.isEmpty())
                            throw new ParseException("specified type of digit even though there is no digit! At position %s\n%s", pos, ParserExceptionReport.report(expText, pos));
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                        lexemes.add(new Lexeme(LexemeType.TYPE_INT, c));
                    } else
                        text.append(c);
                    pos++;
                }
                case 'B', 'b' -> {
                    if (last == '[') {
                        lexemes.add(new Lexeme(LexemeType.TYPE_BYTE, c));
                        pos++;
                        break;
                    }
                    if (lastIsDigit) {
                        if (digit.isEmpty())
                            throw new ParseException("specified type of digit even though there is no digit! At position %s\n%s", pos, ParserExceptionReport.report(expText, pos));
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                        lexemes.add(new Lexeme(LexemeType.TYPE_BYTE, c));
                    } else
                        text.append(c);
                    pos++;
                }
                case 'd', 'D' -> {
                    if (lastIsDigit) {
                        if (digit.isEmpty())
                            throw new ParseException("specified type of digit even though there is no digit! At position %s\n%s", pos, ParserExceptionReport.report(expText, pos));
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                        lexemes.add(new Lexeme(LexemeType.TYPE_DOUBLE, c));
                    } else
                        text.append(c);
                    pos++;
                }
                case 'L', 'l' -> {
                    if (last == '[') {
                        lexemes.add(new Lexeme(LexemeType.TYPE_LONG, c));
                        pos++;
                        break;
                    }
                    if (lastIsDigit) {
                        if (digit.isEmpty())
                            throw new ParseException("specified type of digit even though there is no digit! At position %s\n%s", pos, ParserExceptionReport.report(expText, pos));
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                        lexemes.add(new Lexeme(LexemeType.TYPE_LONG, c));
                    } else
                        text.append(c);
                    pos++;
                }
                case 's', 'S' -> {
                    if (lastIsDigit) {
                        if (digit.isEmpty())
                            throw new ParseException("specified type of digit even though there is no digit! At position %s\n%s", pos, ParserExceptionReport.report(expText, pos));
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                        lexemes.add(new Lexeme(LexemeType.TYPE_SHORT, c));
                    } else
                        text.append(c);
                    pos++;
                }
                case 'f', 'F' -> {
                    if (lastIsDigit) {
                        if (digit.isEmpty())
                            throw new ParseException("specified type of digit even though there is no digit! At position %s\n%s", pos, ParserExceptionReport.report(expText, pos));
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                        lexemes.add(new Lexeme(LexemeType.TYPE_FLOAT, c));
                    } else
                        text.append(c);
                    pos++;
                }
                case ';' -> {
                    if (!text.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.STRING, text.toString()));
                        text = new StringBuilder();
                    }
                    if (!digit.isEmpty()) {
                        lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
                        digit = new StringBuilder();
                    }
                    lexemes.add(new Lexeme(LexemeType.ARR_TYPE_SEPARATOR, c));
                    pos++;
                }
                case '"' -> {
                    textExpected = true;
                    isSingleQuote = false;
                    pos++;
                }
                case '\'' -> {
                    textExpected = true;
                    isSingleQuote = true;
                    pos++;
                }
                case ' ', '\n', '\t', '\r', '\f', '\b' -> {
                    pos++;
                    continue main;
                }
                default -> {
                    if (!text.isEmpty() && (!Character.isDigit(c) || !text.toString().equals("-"))) {
                        text.append(c);
                    } else if (Character.isDigit(c) || (c == '.' && lastIsDigit) || (c == 'E' && lastIsDigit)) {
                        if (!text.isEmpty()) {
                            if (!text.toString().equals("-")) {
                                throw new ParseException(text + " pos= " + pos + "\n" + ParserExceptionReport.report(expText, pos));
                            }
                            digit.append("-");
                            text = new StringBuilder();
                        }
                        digit.append(c);
                    } else {
                        text.append(c);
                    }
                    pos++;
                }
            }
            last = c;
        }
        if (!text.isEmpty()) {
            lexemes.add(new Lexeme(LexemeType.STRING, text.toString()));
            text = new StringBuilder();
        }
        if (!digit.isEmpty()) {
            lexemes.add(new Lexeme(LexemeType.NUMBER, digit.toString()));
            digit = new StringBuilder();
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
    }


    enum LexemeType {
        STRUCT_OPEN, // {
        STRUCT_CLOSE, // }
        ELEMENT_SEPARATOR, // ,
        NAME_VALUE_SEPARATOR, // :
        LIST_OPEN, // [
        LIST_CLOSE, // ]
        TYPE_INT, // I
        TYPE_BYTE, // B, b
        TYPE_DOUBLE, // d
        TYPE_LONG, // L
        TYPE_SHORT, // s
        TYPE_FLOAT, // f
        STRING,
        NUMBER,
        ARR_TYPE_SEPARATOR, // ;
        EOF; // end exp
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

        public LexemeBuffer back() {
            pos--;
            return this;
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

    public static class ParserExceptionReport {
        public static String report(String input, int position) {
            StringBuilder sb = new StringBuilder();

            int len = input.length();

            int start = Math.max(0, position - 100);
            int end = Math.min(len, position + 100);

            sb.append("Parsing Error Report:\n");
            String s = input.substring(start, end);
            String replaced = s.replace("\n", "\\n");
            sb.append(replaced);
            sb.append("\n");
            sb.append(" ".repeat(Math.max(0, position - start + (replaced.length() - s.length()) - 2))).append("^^^");

            return sb.toString();
        }
    }

    public static class ParseException extends IllegalArgumentException {
        public ParseException() {
        }

        public ParseException(String s) {
            super(s);
        }

        public ParseException(String s, Object... objects) {
            super(String.format(s, objects));
        }
    }

}
