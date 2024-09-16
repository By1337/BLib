package org.by1337.blib.math;

import org.junit.Test;

import static org.by1337.blib.math.MathParser.mathSave;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathParserTest {
    @Test
    public void run() {
        assertEquals(mathSave("math[5 + 5 * 5 == 30 && 7 + 7 * 7 == 56 && (25 + 25 % 50 == 1 || 35 + 35 == 70)]"), "1");
        assertEquals(mathSave("math[(10 == 10) && (63 == 63) && (63 == 63) && (63 == 63)]"), "1");
        assertEquals(mathSave("math[5 + 5 * 5 == 30 && 7 + 7 * 7 == 56]"), "1");
        assertEquals(mathSave("math[10 == 10 && 63 == 63 && 63 == 63 && 63 == 63]"), "1");
        assertEquals(mathSave("math[10 == 10 && ? == ? && ? == ? && ? == ?]"), "1");
        assertEquals(mathSave("math[((true || false) && !(false && true)) || (true && (false || true))]"), "1");
        assertEquals(mathSave("math[true && true]"), "1");
        assertEquals(mathSave("math[true && false]"), "0");
        assertEquals(mathSave("math[false && true]"), "0");
        assertEquals(mathSave("math[false && false]"), "0");
        assertEquals(mathSave("math[true || true]"), "1");
        assertEquals(mathSave("math[true || false]"), "1");
        assertEquals(mathSave("math[false || true]"), "1");
        assertEquals(mathSave("math[false || false]"), "0");
        assertEquals(mathSave("math[!true]"), "0");
        assertEquals(mathSave("math[!false]"), "1");
        assertEquals(mathSave("math[true && (false || true)]"), "1");
        assertEquals(mathSave("math[false || (true && true)]"), "1");
        assertEquals(mathSave("math[!(true && false)]"), "1");
        assertEquals(mathSave("math[(true || false) && !(false && false)]"), "1");
        assertEquals(mathSave("math[true && (false && false)]"), "0");
        assertEquals(mathSave("math[10 > 9]"), "1");
        assertEquals(mathSave("math[10 % 5 == 0]"), "1");
        assertEquals(mathSave("math[10 > 10]"), "0");
        assertEquals(mathSave("math[10 >= 10]"), "1");
        assertEquals(mathSave("math[9 <= 10]"), "1");
        assertEquals(mathSave("math[9 != 10]"), "1");
        assertEquals(mathSave("math[1 && 0]"), "0");
        assertEquals(mathSave("math[(10 > 9) && (10 < 11)]"), "1");
        assertEquals(mathSave("math[(10 < 9) || (10 < 11)]"), "1");
        assertEquals(mathSave("math[1 && 1]"), "1");
        assertEquals(mathSave("math[1 && (1 || 0)]"), "1");
        assertEquals(mathSave("math[!0 && !0]"), "1");
        assertEquals(mathSave("math[1 && (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || 0)))))))))))))]"), "1");
        assertEquals(mathSave("math[0 && 0]"), "0");
        assertEquals(mathSave("math[0 && 0 && 0]"), "0");
        assertEquals(mathSave("math[1 && 1 && 1]"), "1");
        assertEquals(mathSave("math[10 == 10 && 1 && 1]"), "1");
        assertEquals(mathSave("math[9 < 10 && 1 && 1 && 90 < 100]"), "1");
        assertEquals(mathSave("math[10 < 9]"), "0");
        assertEquals(mathSave("math[-10 + 10]"), "0");
        assertEquals(mathSave("math[10 + 9]"), "19");
        assertEquals(mathSave("math[10 == 9]"), "0");
        assertEquals(mathSave("math[10 == 10]"), "1");
        assertEquals(mathSave("math[10 + 10 + 10 - 5]"), "25");
        assertEquals(mathSave("math[10 - 1 >= 9]"), "1");
        assertEquals(mathSave("math[-10+-10]"), "-20");
        assertEquals(mathSave("math[-10+-10+-20-10]"), "-50");
        assertEquals(mathSave("math[99 + 999 + 2]"), "1100");
        assertEquals(mathSave("math[777 + 9 + 643 + 87 / 123 * 7]"), "1429");
        assertEquals(mathSave("math[4327 + 654- 123 +-435 * 34 / 34]"), "4423");
        assertEquals(mathSave("math[34527563 + 9783455]"), "44311018");
        assertEquals(mathSave("math[819 + 340 - 831 - 18 + 676 / 723 / 112 - 982 * 692 + 524]"), "-678710");
        assertEquals(mathSave("math[492 / 526 * 712 * 802 * 455 + 249 / 350 - 941 / 225 / 972 / 821 * 281 - 504 + 535 - 821 + 921 * 395 / 836 * 613 / 638]"), "-373");
        assertEquals(mathSave("math[2539+489*6527*4391-2191/7721/4763-1276/6618*6018+1070-198-2166-3130+3901/2845-575/8472*8364+1079]"), "14014767068");
        assertEquals(mathSave("math[34527563 + 9783455] math[34527563 + 9783455]"), "44311018 44311018");
        assertEquals(mathSave("math[461168601842738790 * 2]"), "922337203685477580");
        assertEquals(mathSave("math[461168601842738790 + 461168601842738790]"), "922337203685477580");
    }
}
