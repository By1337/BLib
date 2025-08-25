package org.by1337.blib.math;

import org.by1337.blib.BLib;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.text.MessageFormatter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoubleMathParser extends AbstractMathParser<Double> {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private static final DoubleMathParser INSTANCE = new DoubleMathParser();
    private static final double TRUE = 1;
    private static final double FALSE = 0;

    protected DoubleMathParser() {
        super(TRUE, FALSE, Double::parseDouble);
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

    public static String mathSafeReplacePlaceholders(String input) {
        return mathSafeReplacePlaceholders(input, true);
    }

    public static String mathSafeReplacePlaceholders(String input, boolean replaceStrings) {
        try {
            return mathReplacePlaceholders(input, replaceStrings);
        } catch (ParseException e) {
            BLib.getApi().getMessage().error(e);
        }
        return input;
    }

    public static String mathReplacePlaceholders(String input, boolean replaceStrings) throws ParseException {
        String pattern = "math\\[(.*?)]";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(input);
        while (matcher.find()) {
            String group = matcher.group(1);
            input = input.replace(matcher.group(0), math(group, replaceStrings));
        }
        return input;
    }

    public static String math(String input) throws ParseException {
        return math(input, false);
    }
    public static String math(String input, boolean replaceStrings) throws ParseException {
        String s = replaceStrings ? replaceStrings(input) : input;
        s = s.replace(" ", "");
        List<Lexeme> list = INSTANCE.parseExp(s);
        LexemeBuffer buffer = new LexemeBuffer(list);
        return input.replace(input, DECIMAL_FORMAT.format(INSTANCE.analyze(buffer)));
    }

    public static String replaceStrings(String s) {
        String step1 = s.replaceAll("\\btrue\\b", "1");
        String step2 = step1.replaceAll("\\bfalse\\b", "0");
        Pattern pattern = Pattern.compile("([^\\s=><!^%()&|+\\-*/.E\\d]+)");
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
    protected Double mul(Double v, Double v1) {
        return v * v1;
    }

    @Override
    protected Double div(Double v, Double v1) {
        return v / v1;
    }

    @Override
    protected Double modulus(Double v, Double v1) {
        return v % v1;
    }

    @Override
    protected Double add(Double v, Double v1) {
        return v + v1;
    }

    @Override
    protected Double sub(Double v, Double v1) {
        return v - v1;
    }

    @Override
    protected boolean equalTo(Double v, Double v1) {
        return Objects.equals(v, v1);
    }

    @Override
    protected boolean notEqualTo(Double v, Double v1) {
        return !equalTo(v, v1);
    }

    @Override
    protected boolean lessThan(Double v, Double v1) {
        return v < v1;
    }

    @Override
    protected boolean greaterThan(Double v, Double v1) {
        return v > v1;
    }

    @Override
    protected boolean greaterThanOrEqualTo(Double v, Double v1) {
        return v >= v1;
    }

    @Override
    protected boolean lessThanOrEqualTo(Double v, Double v1) {
        return v <= v1;
    }

    @Override
    protected Double opMinus(Double val) {
        return -val;
    }

    @Override
    protected int defaultTextParser(char c, int pos, String expText, List<Lexeme> lexemes) throws ParseException {
        if ((c <= '9' && c >= '0') || c == '.' || c == 'E') {
            StringBuilder sb = new StringBuilder();
            do {
                sb.append(c);
                pos++;
                if (pos >= expText.length()) {
                    break;
                }
                c = expText.charAt(pos);
            } while ((c <= '9' && c >= '0') || c == '.' || c == 'E');
            lexemes.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
        } else {
            if (c != ' ') {
                throw new ParseException(MessageFormatter.apply(Lang.getMessage("unexpected-character"), c), pos);
            }
            pos++;
        }
        return pos;
    }
}
