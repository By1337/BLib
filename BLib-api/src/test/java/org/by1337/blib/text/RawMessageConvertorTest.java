package org.by1337.blib.text;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RawMessageConvertorTest {


    @Test
    public void testBasicColorConversion() {
        String miniMsg = "<color:#0dfb00>HEX text </color><green>Green</green><red>red &#</red>";
        String expectedLegacy = "&#0dfb00HEX text &aGreen&cred &#";
        assertEquals(expectedLegacy, toLegacy(toRaw(miniMsg)));
    }

    @Test
    public void testSimpleText() {
        String miniMsg = "Simple text without color";
        String expectedLegacy = "Simple text without color";
        assertEquals(expectedLegacy, toLegacy(toRaw(miniMsg)));
    }

    @Test
    public void testMultipleColors() {
        String miniMsg = "<green>Green text </green><red>Red text </red><blue>Blue text</blue>";
        String expectedLegacy = "&aGreen text &cRed text &9Blue text";
        assertEquals(expectedLegacy, toLegacy(toRaw(miniMsg)));
    }

    @Test
    public void testEmptyString() {
        String miniMsg = "";
        String expectedLegacy = "";
        assertEquals(expectedLegacy, toLegacy(toRaw(miniMsg)));
    }

    @Test
    public void testSpecialCharacters() {
        String miniMsg = "<gold>Special chars: !@#$%^&*()_\n+</gold>";
        String expectedLegacy = "&6Special chars: !@#$%^&*()_\n+";
        assertEquals(expectedLegacy, toLegacy(toRaw(miniMsg)));
    }

    @Test
    public void testNestedColors() {
        String miniMsg = "<green>This is <red>nested</red> text";
        String expectedLegacy = "&aThis is &cnested text";
        assertEquals(expectedLegacy, toLegacy(toRaw(miniMsg)));
    }

    private String toLegacy(String raw) {
        return RawMessageConvertor.convertToLegacy(raw);
    }

    private String toRaw(String miniMsg) {
        return GsonComponentSerializer.gson().serialize(MiniMessage.miniMessage().deserialize(miniMsg));
    }
}