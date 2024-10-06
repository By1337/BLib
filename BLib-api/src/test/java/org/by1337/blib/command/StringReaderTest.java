package org.by1337.blib.command;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringReaderTest {


    @Test
    public void testHasNextTrue() {
        StringReader reader = new StringReader("hello");
        assertTrue(reader.hasNext());
    }

    @Test
    public void testHasNextFalse() {
        StringReader reader = new StringReader("");
        assertFalse(reader.hasNext());
    }

    @Test
    public void testNext() {
        StringReader reader = new StringReader("hello");
        assertEquals('h', reader.next());
        assertEquals('e', reader.next());
    }

    @Test
    public void testPeek() {
        StringReader reader = new StringReader("hello");
        assertEquals('h', reader.peek());
        reader.next();
        assertEquals('e', reader.peek());
    }

    @Test
    public void testSkip() {
        StringReader reader = new StringReader("hello");
        reader.skip(2);
        assertEquals('l', reader.peek());
        assertEquals(2, reader.getCursor());
    }

    @Test
    public void testBack() {
        StringReader reader = new StringReader("hello");
        reader.next(); // 'h'
        reader.next(); // 'e'
        assertEquals('h', reader.back());
        assertEquals(0, reader.getCursor());
    }

    @Test
    public void testBack2() {
        StringReader reader = new StringReader("hello");
        reader.next(); // 'h'
        assertEquals('h', reader.peekPrevious());
        assertEquals('h', reader.back());
        assertEquals(0, reader.getCursor());
    }

    @Test
    public void testGetString() {
        StringReader reader = new StringReader("hello");
        assertEquals("hello", reader.getString());
    }

    @Test
    public void testGetCursor() {
        StringReader reader = new StringReader("hello");
        reader.next();
        reader.next();
        assertEquals(2, reader.getCursor());
    }

    @Test()
    public void testNextThrowsExceptionAtEnd() {
        StringReader reader = new StringReader("h");
        reader.next();
        assertThrows(IndexOutOfBoundsException.class, reader::next);
    }

    @Test
    public void testBackThrowsExceptionAtStart() {
        StringReader reader = new StringReader("hello");
        assertThrows(IndexOutOfBoundsException.class, reader::back);
    }
}