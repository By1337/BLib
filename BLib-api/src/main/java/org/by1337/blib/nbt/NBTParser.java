package org.by1337.blib.nbt;

import org.by1337.blib.nbt.impl.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class NBTParser {

    private static CompoundTag parseAsCompoundTag(File file) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return parseAsCompoundTag(new String(encoded, StandardCharsets.UTF_8));
    }

    public static CompoundTag parseAsCompoundTag(String raw) {
        List<Lexeme> list = parseExp(raw);

        LexemeBuffer buffer = new LexemeBuffer(list);

        return parseCompoundTag(buffer);
    }


    private static CompoundTag parseCompoundTag(LexemeBuffer buffer) {
        if (buffer.next().type != LexemeType.STRUCT_OPEN) {
            throw new ParseException("должен начинаться с {");
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
                        compoundTag.putTag(name, parseNBT(buffer));
                    }
                }
                case STRUCT_OPEN -> {
                    if (name == null) {
                        throw new ParseException("missing tag name! " + lexeme);
                    }
                    buffer.back();
                    compoundTag.putTag(name, parseCompoundTag(buffer));
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
                    compoundTag.putTag(name, parseList(buffer));
                }
                case STRUCT_CLOSE -> {
                    break main;
                }
                case NUMBER -> {
                    buffer.back();
                    if (name == null) {
                        throw new ParseException("missing tag name! " + lexeme);
                    }
                    compoundTag.putTag(name, parseNBT(buffer));
                }
                default -> {
                    throw new ParseException("неожиданный токен! " + lexeme);
                }
            }
        }

        return compoundTag;
    }

    public static NBT parseNBT(String raw) {
        return parseNBT(new LexemeBuffer(parseExp(raw)));
    }

    private static NBT parseNBT(LexemeBuffer buffer) {
        Lexeme lexeme = buffer.next();
        LexemeType type = lexeme.type;
        if (type == LexemeType.LIST_OPEN){
            buffer.back();
            return parseList(buffer);
        }
        if (type == LexemeType.STRUCT_OPEN){
            buffer.back();
            return parseCompoundTag(buffer);
        }
        String val = lexeme.value;
        boolean isByte = false;
        if (type == LexemeType.STRING && (val.equals("true") || val.equals("false"))) {
            type = LexemeType.NUMBER;
            val = val.equals("true") ? "1" : "0";
            isByte = true;
        }
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
            if (isByte) {
                return ByteNBT.valueOf(Byte.parseByte(val));
            }
            return IntNBT.valueOf(Integer.parseInt(val));
        } else if (type == LexemeType.STRING) {
            return new StringNBT(val);
        }
        throw new ParseException("current lexeme is not number! ", lexeme);
    }

    public static NBT parseList(String raw) {
        return parseList(new LexemeBuffer(parseExp(raw)));
    }

    private static NBT parseList(LexemeBuffer buffer) {
        if (buffer.next().type != LexemeType.LIST_OPEN) {
            throw new ParseException("Ожидался [ " + buffer);
        }
        List<NBT> list = new ArrayList<>();
        LexemeType type = null;
        main:
        while (true) {
            Lexeme lexeme = buffer.next();
            switch (lexeme.type) {
                case LIST_OPEN -> {
                    buffer.back();
                    list.add(parseList(buffer));
                }
                case STRUCT_OPEN -> {
                    buffer.back();
                    list.add(parseCompoundTag(buffer));
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
                    type = lexeme.type;
                    Lexeme next = buffer.next();
                    if (next.type != LexemeType.ARR_TYPE_SEPARATOR) {
                        throw new ParseException("неожиданный токен! " + lexeme);
                    }
                }
                default -> {
                    buffer.back();
                    list.add(parseNBT(buffer));
                }
            }
        }

        if (list.isEmpty() && type == null)
            return new ListNBT(list);

        if (!list.isEmpty()) {
            final Class<?> clazz = list.get(0).getClass();
            for (NBT nbt : list) {
                if (nbt.getClass() != clazz) {
                    throw new ParseException("Элементы в списке не одного класса!" + list);
                }
            }
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
        return new ListNBT(list);
    }

    private static List<Lexeme> parseExp(String expText) throws ParseException {
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
                                } else {
                                    throw new ParseException("Ожидался '%s' или '%s', а не '%s', at", "\\", isSingleQuote ? "\\'" : "\"", expText.charAt(pos + 1), pos + 1);
                                }
                            } else {
                                throw new ParseException("Строка неожиданно заканчивается на символе '%s' на позиции '%s'.", c, pos);
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
                        throw new ParseException("Строка неожиданно заканчивается на символе '%s' на позиции '%s'.", c, pos);
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
                            throw new ParseException("specified type of digit even though there is no digit! At position %s", pos);
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
                            throw new ParseException("specified type of digit even though there is no digit! At position %s", pos);
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
                            throw new ParseException("specified type of digit even though there is no digit! At position %s", pos);
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
                            throw new ParseException("specified type of digit even though there is no digit! At position %s", pos);
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
                            throw new ParseException("specified type of digit even though there is no digit! At position %s", pos);
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
                            throw new ParseException("specified type of digit even though there is no digit! At position %s", pos);
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
                    } else if (Character.isDigit(c) || (c == '.' && lastIsDigit)) {
                        if (!text.isEmpty()) {
                            if (!text.toString().equals("-")) {
                                throw new ParseException(text + " pos= " + pos);
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


    private enum LexemeType {
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


    private static class LexemeBuffer {
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

    private static class Lexeme {
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
