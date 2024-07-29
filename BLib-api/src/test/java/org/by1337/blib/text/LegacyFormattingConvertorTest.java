package org.by1337.blib.text;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyFormattingConvertorTest {

    @Test
    public void test() {
        assertEquals(LegacyFormattingConvertor.convert("&#0dfb00HEX text &aGreen&cred &#&#0dfb00"), "<color:#0dfb00>HEX text </color><green>Green</green><red>red &#</red>");
        assertEquals(LegacyFormattingConvertor.convert("&#0dfb00hex&#0dfb00hex&#nohex_"), "<color:#0dfb00>hex</color><color:#0dfb00>hex&#nohex_</color>");
        assertEquals(LegacyFormattingConvertor.convert("&x&0&d&f&b&0&0HEX text &aGreen&cred &#&x&0&d&f&b&0&0"), "<color:#0dfb00>HEX text </color><green>Green</green><red>red &#</red>");
        assertEquals(LegacyFormattingConvertor.convert("&c&l&n&m&f12 &a3&c"), "<white>12 </white><green>3</green>");
        assertEquals(LegacyFormattingConvertor.convert("&k&l&m5 &k&l&m&c5"), "<obf><b><st>5 </st></b></obf><red>5</red>");
        assertEquals(LegacyFormattingConvertor.convert("&#00ff00[&ka&#00ff00, &ka&#00ff00, &ka&#00ff00]"), "<color:#00ff00>[<obf>a</obf></color><color:#00ff00>, <obf>a</obf></color><color:#00ff00>, <obf>a</obf></color><color:#00ff00>]</color>");
    }

}