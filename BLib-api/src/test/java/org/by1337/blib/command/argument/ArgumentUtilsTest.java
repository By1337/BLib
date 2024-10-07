package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.StringReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArgumentUtilsTest {


    @Test
    public void test() throws CommandSyntaxError {
        StringReader reader = new StringReader("'string string'");
        assertEquals("string string", ArgumentUtils.readString(reader));
    }

    @Test
    public void test2() throws CommandSyntaxError {
        StringReader reader = new StringReader("\"string string\"");
        assertEquals("string string", ArgumentUtils.readString(reader));
    }

    @Test
    public void test3() throws CommandSyntaxError {
        StringReader reader = new StringReader("string string");
        assertEquals("string", ArgumentUtils.readString(reader));
    }
    @Test
    public void test4() throws CommandSyntaxError {
        StringReader reader = new StringReader("\"string' 'string\"");
        assertEquals("string' 'string", ArgumentUtils.readString(reader));
    }

    @Test
    public void test5() throws CommandSyntaxError {
        StringReader reader = new StringReader("\"string\\\" string\"");
        assertEquals("string\" string", ArgumentUtils.readString(reader));
    }

    @Test
    public void test6() throws CommandSyntaxError {
        StringReader reader = new StringReader("\"string\\\\ string\"");
        assertEquals("string\\\\ string", ArgumentUtils.readString(reader));
    }
    @Test
    public void test7() throws CommandSyntaxError {
        StringReader reader = new StringReader("\"string string\"123\"");
        assertEquals("string string\"123", ArgumentUtils.readString(reader));
    }
}