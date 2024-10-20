package org.by1337.blib.math;


import org.by1337.blib.BLib;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.text.MessageFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathParser extends AbstractMathParser<Long>{
    private static final MathParser INSTANCE = new MathParser();
    public static final long TRUE = 1;
    public static final long FALSE = 0;

    public MathParser() {
        super(TRUE, FALSE, Long::parseLong);
    }

    /**
     * @deprecated {@link MathParser#mathSafe(String)}
     */
    @Deprecated
    public static String mathSave(String input) {
        return mathSafe(input, true);
    }

    public static String mathSafe(String input) {
        return mathSafe(input, true);
    }

    public static String mathSafe(String input, boolean replaceStrings) {
        try {
            return math(input, replaceStrings);
        } catch (ParseException e) {
            BLib.getApi().getMessage().error(e);
        }

        return input;
    }

    /**
     * @deprecated {@link MathParser#mathSafe(String, boolean)}
     */
    public static String mathSave(String input, boolean replaceStrings) {
        return mathSafe(input, replaceStrings);
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
            List<Lexeme> list = INSTANCE.parseExp(s);
            LexemeBuffer buffer = new LexemeBuffer(list);
            input = input.replace(matcher.group(0), String.valueOf(INSTANCE.analyze(buffer)));
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

    @Override
    protected int defaultTextParser(char c, int pos, String expText, List<Lexeme> lexemes) throws ParseException {
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
                throw new ParseException(MessageFormatter.apply(Lang.getMessage("unexpected-character"), c), pos);
            }
            pos++;
        }
        return pos;
    }

    @Override
    protected Long mul(Long v, Long v1) {
        return v * v1;
    }

    @Override
    protected Long div(Long v, Long v1) {
        return v / v1;
    }

    @Override
    protected Long modulus(Long v, Long v1) {
        return v % v1;
    }

    @Override
    protected Long add(Long v, Long v1) {
        return v + v1;
    }

    @Override
    protected Long sub(Long v, Long v1) {
        return v - v1;
    }

    @Override
    protected boolean equalTo(Long v, Long v1) {
        return Objects.equals(v, v1);
    }

    @Override
    protected boolean notEqualTo(Long v, Long v1) {
        return !equalTo(v, v1);
    }

    @Override
    protected boolean lessThan(Long v, Long v1) {
        return v < v1;
    }

    @Override
    protected boolean greaterThan(Long v, Long v1) {
        return v > v1;
    }

    @Override
    protected boolean greaterThanOrEqualTo(Long v, Long v1) {
        return v >= v1;
    }

    @Override
    protected boolean lessThanOrEqualTo(Long v, Long v1) {
        return v <= v1;
    }

    @Override
    protected Long opMinus(Long val) {
        return -val;
    }
}
