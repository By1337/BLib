package org.by1337.blib.math;

import org.by1337.blib.lang.Lang;
import org.by1337.blib.text.MessageFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractMathParser<T> {
    public final T TRUE;
    public final T FALSE;
    private final Function<String,T> numberParser;

    protected AbstractMathParser(T aTrue, T aFalse, Function<String, T> numberParser) {
        TRUE = aTrue;
        FALSE = aFalse;
        this.numberParser = numberParser;
    }


    protected T analyze(LexemeBuffer lexemes) throws ParseException {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return null;
        } else {
            lexemes.back();
            return logical(lexemes);
        }
    }
    protected abstract T mul(T v, T v1);
    protected abstract T div(T v, T v1);
    protected abstract T modulus(T v, T v1);

    protected T multdiv(LexemeBuffer buffer) throws ParseException {
        T value = factor(buffer);
        while (true) {
            Lexeme lexeme = buffer.next();
            switch (lexeme.type) {
                case OP_MUL -> value = mul(value, factor(buffer));
                case OP_DIV -> value = div(value, factor(buffer));
                case MODULUS -> value = modulus(value, factor(buffer));
                case OP_PLUS, OP_MINUS, CLOSE_BRACKET, EOF, LOGICAL_AND, LOGICAL_OR, GREATER_THAN,
                     EQUAL_TO, NOT_EQUAL_TO, LESS_THAN,
                     GREATER_THAN_OR_EQUAL_TO, LESS_THAN_OR_EQUAL_TO -> {
                    buffer.back();
                    return value;
                }
                default ->
                        throw new ParseException(MessageFormatter.apply(Lang.getMessage("unexpected-token"), lexeme.type), buffer.pos);
            }
        }

    }
    protected abstract T add(T v, T v1);
    protected abstract T sub(T v, T v1);

    protected T plusMinus(LexemeBuffer buffer) throws ParseException {
        T value = multdiv(buffer);
        while (true) {
            Lexeme lexeme = buffer.next();
            switch (lexeme.type) {
                case OP_PLUS -> {
                    T val1 = multdiv(buffer);
                    value = add(value, val1);
                }
                case OP_MINUS -> {
                    T val1 = multdiv(buffer);
                    value = sub(value, val1);
                }
                case CLOSE_BRACKET, EOF, LOGICAL_AND, LOGICAL_OR, GREATER_THAN,
                     EQUAL_TO, NOT_EQUAL_TO, LESS_THAN,
                     GREATER_THAN_OR_EQUAL_TO, LESS_THAN_OR_EQUAL_TO -> {
                    buffer.back();
                    return value;
                }
                default ->
                        throw new ParseException(MessageFormatter.apply(Lang.getMessage("unexpected-token"), lexeme.type), buffer.pos);
            }
        }
    }
    protected abstract boolean equalTo(T v, T v1);
    protected abstract boolean notEqualTo(T v, T v1);
    protected abstract boolean lessThan(T v, T v1);
    protected abstract boolean greaterThan(T v, T v1);
    protected abstract boolean greaterThanOrEqualTo(T v, T v1);
    protected abstract boolean lessThanOrEqualTo(T v, T v1);

    protected T logical(LexemeBuffer buffer) throws ParseException {
        T value = plusMinus(buffer);
        while (true) {
            Lexeme lexeme = buffer.next();
            switch (lexeme.type) {
                case EQUAL_TO -> {
                    T val1 = plusMinus(buffer);
                    value = equalTo(value, val1) ? TRUE : FALSE;
                }
                case LOGICAL_AND -> {
                    T val1 = logical(buffer);
                    value = value == TRUE && val1 == TRUE ? TRUE : FALSE;
                }
                case LOGICAL_OR -> {
                    T val1 = logical(buffer);
                    value = value == TRUE || val1 == TRUE ? TRUE : FALSE;
                }
                case NOT_EQUAL_TO -> {
                    T val1 = plusMinus(buffer);
                    value = notEqualTo(value, val1) ? TRUE : FALSE;
                }
                case LESS_THAN -> {
                    T val1 = plusMinus(buffer);
                    value = lessThan(value, val1) ? TRUE : FALSE;
                }
                case GREATER_THAN -> {
                    T val1 = plusMinus(buffer);
                    value = greaterThan(value, val1) ? TRUE : FALSE;
                }
                case GREATER_THAN_OR_EQUAL_TO -> {
                    T val1 = plusMinus(buffer);
                    value = greaterThanOrEqualTo(value, val1) ? TRUE : FALSE;
                }
                case LESS_THAN_OR_EQUAL_TO -> {
                    T val1 = plusMinus(buffer);
                    value = lessThanOrEqualTo(value, val1) ? TRUE : FALSE;
                }
                case EOF, CLOSE_BRACKET, OP_PLUS, OP_MINUS, OP_MUL, OP_DIV, MODULUS -> {
                    buffer.back();
                    return value;
                }
                default ->
                        throw new ParseException(MessageFormatter.apply(Lang.getMessage("unexpected-token"), lexeme.type), buffer.pos);
            }
        }
    }
    protected abstract T opMinus(T val);
    protected T factor(LexemeBuffer buffer) throws ParseException {
        Lexeme lexeme = buffer.next();
        switch (lexeme.type) {
            case NUMBER -> {
                return numberParser.apply(lexeme.value);
            }
            case LOGICAL_NOT -> {
                return factor(buffer) == FALSE ? TRUE : FALSE;
            }

            case OP_MINUS -> {
                return opMinus(factor(buffer));
            }
            case OPEN_BRACKET -> {
                T value = logical(buffer);
                lexeme = buffer.next();
                if (lexeme.type != LexemeType.CLOSE_BRACKET) {

                    throw new ParseException(MessageFormatter.apply(Lang.getMessage("unexpected-token"), lexeme.type), buffer.pos);
                }
                return value;
            }
            default ->
                    throw new ParseException(MessageFormatter.apply(Lang.getMessage("unexpected-token"), lexeme.type), buffer.pos);
        }
    }


    protected List<Lexeme> parseExp(String expText) throws ParseException {
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
                    throw new ParseException(MessageFormatter.apply(Lang.getMessage("expected"), '='), pos);
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
                    throw new ParseException(MessageFormatter.apply(Lang.getMessage("expected"), '&'), pos);
                }
                case '|' -> {
                    if (pos + 1 < expText.length()) {
                        if (expText.charAt(pos + 1) == '|') {
                            lexemes.add(new Lexeme(LexemeType.LOGICAL_OR, "||"));
                            pos += 2;
                            continue;
                        }
                    }
                    throw new ParseException(MessageFormatter.apply(Lang.getMessage("expected"), '|'), pos);
                }
                case '-' -> {
                    lexemes.add(new Lexeme(LexemeType.OP_MINUS, c));
                    pos++;
                }
                default -> {
                    pos = defaultTextParser(c, pos, expText, lexemes);
                }

            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
    }
    protected abstract int defaultTextParser(char c, int pos, String expText, List<Lexeme> lexemes) throws ParseException;
    protected enum LexemeType {
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

    protected static class Lexeme {
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

    protected static class LexemeBuffer {
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

        private int getPos() {
            return pos;
        }
    }
}
