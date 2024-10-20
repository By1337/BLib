package org.by1337.blib.math;

import org.junit.Test;

import static org.by1337.blib.math.DoubleMathParser.mathSafe;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DoubleMathParserTest {

    @Test
    public void run() {
        assertTrue(Double.parseDouble(mathSafe("10.15 + 0.05")) - 10.2 < 0.01);
        assertEquals(Double.parseDouble(mathSafe("10.5 * 1000")), 10500D);
        assertTrue(Double.parseDouble(mathSafe("(5 + 3) * 2")) - 16 < 0.01);
        assertTrue(Double.parseDouble(mathSafe("10 / 2 + 5")) - 10 < 0.01);
        assertEquals(Double.parseDouble(mathSafe("3 * (4 + 2)")), 18D);
        assertTrue(Double.parseDouble(mathSafe("10 - 4 + 2 * 3")) - 12 < 0.01);
        assertTrue(Double.parseDouble(mathSafe("5 % 2")) - 1 < 0.01);
        assertTrue(Double.parseDouble(mathSafe("5 > 3 && 2 < 4 ? 1 : 0")) - 1 < 0.01);
        assertEquals(1, Double.parseDouble(mathSafe("!(true && false)")));
        assertEquals(1_250_000D, Double.parseDouble(mathSafe("1.25*1000*1000")));
    }
}