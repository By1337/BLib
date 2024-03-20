package org.by1337.blib.math;


import org.by1337.blib.BLib;
import org.by1337.blib.lang.Lang;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathParser {

    public static final int TRUE = 1;
    public static final int FALSE = 0;


    public static String mathSave(String input) {
        return mathSave(input, true);
    }

    public static String mathSave(String input, boolean replaceStrings) {
        try {
            return math(input, replaceStrings);
        } catch (ParseException e) {
            BLib.getApi().getMessage().error(e);
        }

        return input;
    }

    public static String math(String input) throws ParseException {
        return math(input, true);
    }

    public static String math(String input, boolean replaceStrings) throws ParseException {
        String pattern = "math\\[(.*?)]";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(input);

        while (matcher.find()) {
            String s = replaceStrings ? replaceStrings(matcher.group(1)) : matcher.group(1);
            s = s.replace(" ", "");
            List<Lexeme> list = parseExp(s);
            LexemeBuffer buffer = new LexemeBuffer(list);
            input = input.replace(matcher.group(0), String.valueOf(analyze(buffer)));
        }
        return input;
    }

    public static String replaceStrings(String s) {
        String step1 = s.replaceAll("\\btrue\\b", "1");
        String step2 = step1.replaceAll("\\bfalse\\b", "0");
        Pattern pattern = Pattern.compile("([^\\s=><!^%()&|+\\-*/\\d]+)");
        Matcher matcher = pattern.matcher(step2);
        StringBuilder resultBuffer = new StringBuilder();
        while (matcher.find()) {
            String replacement = String.valueOf(matcher.group().hashCode());
            matcher.appendReplacement(resultBuffer, replacement);
        }
        matcher.appendTail(resultBuffer);
        return resultBuffer.toString();
    }


    enum LexemeType {
        OPEN_BRACKET, // (
        CLOSE_BRACKET, // )
        NUMBER, // 0-9
        LOGICAL_AND, // &&
        LOGICAL_OR, // ||
        LOGICAL_NOT, // !
        EQUAL_TO, // ==
        NOT_EQUAL_TO, // !=
        OP_MINUS, // -
        LESS_THAN, // <
        GREATER_THAN, // >
        GREATER_THAN_OR_EQUAL_TO, // >=
        LESS_THAN_OR_EQUAL_TO, // <=
        OP_PLUS, // +
        OP_MUL, // *
        OP_DIV, // /
        MODULUS, // %
        EOF; // end exp
    }

    public static int analyze(LexemeBuffer lexemes) throws ParseException {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return 0;
        } else {
            lexemes.back();
            return logical(lexemes);
        }
    }

    public static int multdiv(LexemeBuffer buffer) throws ParseException {
        int value = factor(buffer);
        while (true) {
            Lexeme lexeme = buffer.next();
            switch (lexeme.type) {
                case OP_MUL -> value *= factor(buffer);
                case OP_DIV -> value /= factor(buffer);
                case MODULUS -> value %= factor(buffer);
                case OP_PLUS, OP_MINUS, CLOSE_BRACKET, EOF, LOGICAL_AND, LOGICAL_OR, GREATER_THAN,
                        EQUAL_TO, NOT_EQUAL_TO, LESS_THAN,
                        GREATER_THAN_OR_EQUAL_TO, LESS_THAN_OR_EQUAL_TO -> {
                    buffer.back();
                    return value;
                }
                default ->
                        throw new ParseException(String.format(Lang.getMessage("unexpected-token"), lexeme.type), buffer.pos);
            }
        }

    }

    public static int plusMinus(LexemeBuffer buffer) throws ParseException {
        int value = multdiv(buffer);
        while (true) {
            Lexeme lexeme = buffer.next();
            switch (lexeme.type) {
                case OP_PLUS -> {
                    int val1 = multdiv(buffer);
                    value += val1;
                }
                case OP_MINUS -> {
                    int val1 = multdiv(buffer);
                    value -= val1;
                }
                case CLOSE_BRACKET, EOF, LOGICAL_AND, LOGICAL_OR, GREATER_THAN,
                        EQUAL_TO, NOT_EQUAL_TO, LESS_THAN,
                        GREATER_THAN_OR_EQUAL_TO, LESS_THAN_OR_EQUAL_TO -> {
                    buffer.back();
                    return value;
                }
                default ->
                        throw new ParseException(String.format(Lang.getMessage("unexpected-token"), lexeme.type), buffer.pos);
            }
        }
    }

    public static int logical(LexemeBuffer buffer) throws ParseException {
        int value = plusMinus(buffer);
        while (true) {
            Lexeme lexeme = buffer.next();
            switch (lexeme.type) {
                case EQUAL_TO -> {
                    //  buffer.back();
                    int val1 = plusMinus(buffer);
                    value = value == val1 ? TRUE : FALSE;
                }
                case LOGICAL_AND -> {
                    // buffer.back();
                    int val1 = logical(buffer);
                    value = value == TRUE && val1 == TRUE ? TRUE : FALSE;
                }
                case LOGICAL_OR -> {
                    // buffer.back();
                    int val1 = logical(buffer);
                    value = value == TRUE || val1 == TRUE ? TRUE : FALSE;
                }
                case NOT_EQUAL_TO -> {
                    //  buffer.back();
                    int val1 = plusMinus(buffer);
                    value = value != val1 ? TRUE : FALSE;
                }
                case LESS_THAN -> {
                    //  buffer.back();
                    int val1 = plusMinus(buffer);
                    value = value < val1 ? TRUE : FALSE;
                }
                case GREATER_THAN -> {
                    //  buffer.back();
                    int val1 = plusMinus(buffer);
                    value = value > val1 ? TRUE : FALSE;
                }
                case GREATER_THAN_OR_EQUAL_TO -> {
                    //  buffer.back();
                    int val1 = plusMinus(buffer);
                    value = value >= val1 ? TRUE : FALSE;
                }
                case LESS_THAN_OR_EQUAL_TO -> {
                    //   buffer.back();
                    int val1 = plusMinus(buffer);
                    value = value <= val1 ? TRUE : FALSE;
                }
                case EOF, CLOSE_BRACKET, OP_PLUS, OP_MINUS, OP_MUL, OP_DIV, MODULUS -> {
                    buffer.back();
                    return value;
                }
                default ->
                        throw new ParseException(String.format(Lang.getMessage("unexpected-token"), lexeme.type), buffer.pos);
            }
        }
    }

    public static int factor(LexemeBuffer buffer) throws ParseException {
        Lexeme lexeme = buffer.next();
        switch (lexeme.type) {
            case NUMBER -> {
                return Integer.parseInt(lexeme.value);
            }
            case LOGICAL_NOT -> {
                return factor(buffer) == FALSE ? TRUE : FALSE;
            }

            case OP_MINUS -> {
                return -factor(buffer);
            }
            case OPEN_BRACKET -> {
                int value = logical(buffer);
                lexeme = buffer.next();
                if (lexeme.type != LexemeType.CLOSE_BRACKET) {

                    throw new ParseException(String.format(Lang.getMessage("unexpected-token"), lexeme.type), buffer.pos);
                }
                return value;
            }
            default ->
                    throw new ParseException(String.format(Lang.getMessage("unexpected-token"), lexeme.type), buffer.pos);
        }
    }

    private static List<Lexeme> parseExp(String expText) throws ParseException {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int pos = 0;
        while (pos < expText.length()) {
            char c = expText.charAt(pos);
            switch (c) {
                case '%' -> {
                    lexemes.add(new Lexeme(LexemeType.MODULUS, c));
                    pos++;
                }
                case '/' -> {
                    lexemes.add(new Lexeme(LexemeType.OP_DIV, c));
                    pos++;
                }
                case '*' -> {
                    lexemes.add(new Lexeme(LexemeType.OP_MUL, c));
                    pos++;
                }
                case '+' -> {
                    lexemes.add(new Lexeme(LexemeType.OP_PLUS, c));
                    pos++;
                }
                case '>' -> {
                    if (pos + 1 < expText.length()) {
                        if (expText.charAt(pos + 1) == '=') {
                            lexemes.add(new Lexeme(LexemeType.GREATER_THAN_OR_EQUAL_TO, ">="));
                            pos += 2;
                            continue;
                        }
                    }
                    lexemes.add(new Lexeme(LexemeType.GREATER_THAN, '>'));
                    pos++;
                }
                case '<' -> {
                    if (pos + 1 < expText.length()) {
                        if (expText.charAt(pos + 1) == '=') {
                            lexemes.add(new Lexeme(LexemeType.LESS_THAN_OR_EQUAL_TO, "<="));
                            pos += 2;
                            continue;
                        }
                    }
                    lexemes.add(new Lexeme(LexemeType.LESS_THAN, '<'));
                    pos++;

                }
                case '=' -> {
                    if (pos + 1 < expText.length()) {
                        if (expText.charAt(pos + 1) == '=') {
                            lexemes.add(new Lexeme(LexemeType.EQUAL_TO, "=="));
                            pos += 2;
                            continue;
                        }
                    }
                    throw new ParseException(String.format(Lang.getMessage("expected"), '='), pos);
                }
                case '!' -> {
                    if (pos + 1 < expText.length()) {
                        if (expText.charAt(pos + 1) == '=') {
                            lexemes.add(new Lexeme(LexemeType.NOT_EQUAL_TO, "!="));
                            pos += 2;
                            continue;
                        }
                    }
                    lexemes.add(new Lexeme(LexemeType.LOGICAL_NOT, '!'));
                    pos++;
                }
                case '(' -> {
                    lexemes.add(new Lexeme(LexemeType.OPEN_BRACKET, c));
                    pos++;
                }
                case ')' -> {
                    lexemes.add(new Lexeme(LexemeType.CLOSE_BRACKET, c));
                    pos++;
                }
                case '&' -> {
                    if (pos + 1 < expText.length()) {
                        if (expText.charAt(pos + 1) == '&') {
                            lexemes.add(new Lexeme(LexemeType.LOGICAL_AND, "&&"));
                            pos += 2;
                            continue;
                        }
                    }
                    throw new ParseException(String.format(Lang.getMessage("expected"), '&'), pos);
                }
                case '|' -> {
                    if (pos + 1 < expText.length()) {
                        if (expText.charAt(pos + 1) == '|') {
                            lexemes.add(new Lexeme(LexemeType.LOGICAL_OR, "||"));
                            pos += 2;
                            continue;
                        }
                    }
                    throw new ParseException(String.format(Lang.getMessage("expected"), '|'), pos);
                }
                case '-' -> {
                    lexemes.add(new Lexeme(LexemeType.OP_MINUS, c));
                    pos++;
                }
                default -> {
                    if (c <= '9' && c >= '0') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            pos++;
                            if (pos >= expText.length()) {
                                break;
                            }
                            c = expText.charAt(pos);
                        } while (c <= '9' && c >= '0');
                        lexemes.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
                    } else {
                        if (c != ' ') {
                            throw new ParseException(String.format(Lang.getMessage("unexpected-character"), c), pos);
                        }
                        pos++;
                    }
                }

            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
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
    }
}
