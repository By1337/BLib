package org.by1337.blib.util.invoke;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.by1337.blib.util.invoke.LambdaMetafactoryUtil.*;
import static org.junit.jupiter.api.Assertions.*;

public class LambdaMetafactoryUtilTest {


    @Test
    public void run() throws Throwable {
        TestClass testClass = new TestClass();
        setterOf(getField("string")).accept(testClass, "string");
        assertEquals("string", testClass.string);

        setterOf(getField("integer")).accept(testClass, 0x255);
        assertEquals(0x255, testClass.integer);

        setterOf(getField("booleanV")).accept(testClass, true);
        assertTrue(testClass.booleanV);

        setterOf(getField("charV")).accept(testClass, 'a');
        assertEquals('a', testClass.charV);

        setterOf(getField("byteV")).accept(testClass, 0x7F);
        assertEquals(0x7F, testClass.byteV);

        setterOf(getField("shortV")).accept(testClass, 0x255);
        assertEquals(0x255, testClass.shortV);

        setterOf(getField("intV")).accept(testClass, 0x255);
        assertEquals(0x255, testClass.intV);

        setterOf(getField("longV")).accept(testClass, 0x255);
        assertEquals(0x255, testClass.longV);

        setterOf(getField("floatV")).accept(testClass, 10F);
        assertEquals(10F, testClass.floatV);

        setterOf(getField("doubleV")).accept(testClass, 10D);
        assertEquals(10D, testClass.doubleV);

    }

    private Field getField(String name){
        try {
            Field field = TestClass.class.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static class TestClass {
        String string = "str";
        Integer integer = 100;
        boolean booleanV = false;
        char charV = '\0';
        byte byteV = 0;
        short shortV = 0;
        int intV = 0;
        long longV = 0;
        float floatV = 0;
        double doubleV = 0;
    }
}