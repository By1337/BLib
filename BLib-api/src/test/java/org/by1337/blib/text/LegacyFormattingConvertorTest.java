package org.by1337.blib.text;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class LegacyFormattingConvertorTest {

    @Test
    public void test1() {
        String raw = "&#0dfb00HEX text &aGreen&cred &#&#0dfb00";
        Assert.assertEquals(LegacyFormattingConvertor.convert(raw), "<color:#0dfb00>HEX text </color><green>Green</green><red>red &#</red><color:#0dfb00></color>");
        Assert.assertEquals(LegacyFormattingConvertor.convert("&#0dfb00hex&#0dfb00hex&#nohex_"), "<color:#0dfb00>hex</color><color:#0dfb00>hex&#nohex_</color>");
    }

}