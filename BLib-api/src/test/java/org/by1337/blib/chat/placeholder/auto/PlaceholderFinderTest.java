package org.by1337.blib.chat.placeholder.auto;

import org.by1337.blib.chat.placeholder.Placeholder;
import org.by1337.blib.chat.placeholder.auto.annotations.AutoPlaceholderGeneration;
import org.by1337.blib.chat.placeholder.auto.annotations.ExcludeFromPlaceholders;
import org.by1337.blib.chat.placeholder.auto.annotations.IncludeAllInPlaceholders;
import org.by1337.blib.chat.placeholder.auto.annotations.PlaceholderName;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceholderFinderTest {

    @org.junit.Test
    public void test() {
        Test test = new Test();
        assertEquals("name", test.replace("{name}"));
        assertEquals("camelCase", test.replace("{camel_case}"));
        assertEquals("{ignore}", test.replace("{ignore}"));
        assertEquals("customName", test.replace("{name_custom}"));
    }


    @AutoPlaceholderGeneration
    @IncludeAllInPlaceholders
    public static class Test extends Placeholder {
        private String name = "name";
        private String camelCase = "camelCase";
        @ExcludeFromPlaceholders
        private String ignore = "ignore";
        @PlaceholderName(name = "name_custom")
        private String customName = "customName";
    }
}