package org.by1337.blib.text;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageFormatterTest {
    @Test
    public void testBasicReplacement() {
        String result = MessageFormatter.apply("Test string with {} and {}", 1, 2);
        assertEquals("Test string with 1 and 2", result);
    }

    @Test
    public void testMultiplePlaceholders() {
        String result = MessageFormatter.apply("Numbers: {} {} {}", 10, 20, 30);
        assertEquals("Numbers: 10 20 30", result);
    }

    @Test
    public void testPlaceholdersWithIndices() {
        String result = MessageFormatter.apply("This is {2} and {0} and {1}", "first", "second", "third");
        assertEquals("This is third and first and second", result);
    }

    @Test
    public void testEmptyPlaceholders() {
        String result = MessageFormatter.apply("Empty placeholders: {} {} {}", "one", "two");
        assertEquals("Empty placeholders: one two {}", result);
    }

    @Test
    public void testNoPlaceholders() {
        String result = MessageFormatter.apply("No placeholders here.");
        assertEquals("No placeholders here.", result);
    }

    @Test
    public void testUnmatchedBraces() {
        String result = MessageFormatter.apply("Unmatched {braces} and {}.", "placeholder");
        assertEquals("Unmatched {braces} and placeholder.", result);
    }

    @Test
    public void testDigitInPlaceholder() {
        String result = MessageFormatter.apply("Replace {0} and keep {1}", "zero", "one");
        assertEquals("Replace zero and keep one", result);
    }

    @Test
    public void testMixedIndexedAndEmptyPlaceholders() {
        String result = MessageFormatter.apply("Here is {2}, then {} and {}.", "first", "second", "third");
        assertEquals("Here is third, then first and second.", result);
    }

    @Test
    public void testExtraArgumentsIgnored() {
        String result = MessageFormatter.apply("Test {} only", "first", "extra");
        assertEquals("Test first only", result);
    }

    @Test
    public void testNestedBracesIgnored() {
        String result = MessageFormatter.apply("Nested braces {{0}}", "first");
        assertEquals("Nested braces {first}", result);
    }
    
    @Test
    public void testLegacy() {
        String result = MessageFormatter.apply("Numbers: %s %s %s", 10, 20, 30);
        assertEquals("Numbers: 10 20 30", result);
    }
}