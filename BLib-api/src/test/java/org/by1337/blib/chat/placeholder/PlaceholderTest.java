package org.by1337.blib.chat.placeholder;


import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
public class PlaceholderTest {
    @Test
    public void testReplace() {
        Placeholder test = new Placeholder();
        test.registerPlaceholder("{test}", () -> "123");
        test.registerPlaceholder("{test1}", () -> "321");
        test.registerPlaceholder("{test2}", () -> "456");
        test.registerPlaceholder("{qwerty}", () -> "456456456456456456456");
        test.registerPlaceholder("{placeholder_123_123}", () -> "a");
        assertEquals("message 456456456456456456456456456456456456456456123test, a321123456 bla bla bla 123", test.replace("message {qwerty}{qwerty}{test}test, {placeholder_123_123}{test1}{test}{test2} bla bla bla {test}"));
        assertEquals("123123123123123123123123", test.replace("{test}{test}{test}{test}{test}{test}{test}{test}"));
    }
}