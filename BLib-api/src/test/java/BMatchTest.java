import org.by1337.blib.lang.Lang;
import org.by1337.blib.match.BMatch;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

public class BMatchTest {
    @Test
    public void run() throws ParseException {
        Lang.loadIfNull();

        Assert.assertEquals(BMatch.matchSave("match[5 + 5 * 5 == 30 && 7 + 7 * 7 == 56 && (25 + 25 % 50 == 1 || 35 + 35 == 70)]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[(10 == 10) && (63 == 63) && (63 == 63) && (63 == 63)]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[5 + 5 * 5 == 30 && 7 + 7 * 7 == 56]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[10 == 10 && 63 == 63 && 63 == 63 && 63 == 63]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[10 == 10 && ? == ? && ? == ? && ? == ?]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[((true || false) && !(false && true)) || (true && (false || true))]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[true && true]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[true && false]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[false && true]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[false && false]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[true || true]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[true || false]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[false || true]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[false || false]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[!true]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[!false]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[true && (false || true)]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[false || (true && true)]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[!(true && false)]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[(true || false) && !(false && false)]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[true && (false && false)]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[10 > 9]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[10 % 5 == 0]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[10 > 10]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[10 >= 10]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[9 <= 10]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[9 != 10]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[1 && 0]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[(10 > 9) && (10 < 11)]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[(10 < 9) || (10 < 11)]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[1 && 1]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[1 && (1 || 0)]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[!0 && !0]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[1 && (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || (1 || 0)))))))))))))]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[0 && 0]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[0 && 0 && 0]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[1 && 1 && 1]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[10 == 10 && 1 && 1]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[9 < 10 && 1 && 1 && 90 < 100]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[10 < 9]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[-10 + 10]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[10 + 9]"), "19");
        Assert.assertEquals(BMatch.matchSave("match[10 == 9]"), "0");
        Assert.assertEquals(BMatch.matchSave("match[10 == 10]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[10 + 10 + 10 - 5]"), "25");
        Assert.assertEquals(BMatch.matchSave("match[10 - 1 >= 9]"), "1");
        Assert.assertEquals(BMatch.matchSave("match[-10+-10]"), "-20");
        Assert.assertEquals(BMatch.matchSave("match[-10+-10+-20-10]"), "-50");
        Assert.assertEquals(BMatch.matchSave("match[99 + 999 + 2]"), "1100");
        Assert.assertEquals(BMatch.matchSave("match[777 + 9 + 643 + 87 / 123 * 7]"), "1429");
        Assert.assertEquals(BMatch.matchSave("match[4327 + 654- 123 +-435 * 34 / 34]"), "4423");
        Assert.assertEquals(BMatch.matchSave("match[34527563 + 9783455]"), "44311018");
        Assert.assertEquals(BMatch.matchSave("match[819 + 340 - 831 - 18 + 676 / 723 / 112 - 982 * 692 + 524]"), "-678710");
        Assert.assertEquals(BMatch.matchSave("match[492 / 526 * 712 * 802 * 455 + 249 / 350 - 941 / 225 / 972 / 821 * 281 - 504 + 535 - 821 + 921 * 395 / 836 * 613 / 638]"), "-373");
        Assert.assertEquals(BMatch.matchSave("match[2539+489*6527*4391-2191/7721/4763-1276/6618*6018+1070-198-2166-3130+3901/2845-575/8472*8364+1079]"), "1129865180");
        Assert.assertEquals(BMatch.matchSave("match[34527563 + 9783455] match[34527563 + 9783455]"), "44311018 44311018");

    }
}
