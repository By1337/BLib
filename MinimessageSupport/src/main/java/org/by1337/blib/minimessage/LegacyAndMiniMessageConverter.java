package org.by1337.blib.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.by1337.blib.text.LegacyFormattingConvertor;

public class LegacyAndMiniMessageConverter {
    public static String convertToGson(String legacy){
        String str = LegacyFormattingConvertor.convert(legacy);
        final Component component = MiniMessage.miniMessage().deserialize(str);
        return GsonComponentSerializer.gson().serialize(component);
    }
}
