package org.by1337.blib.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.ansi.ColorLevel;

public class JsonComponentToANSI {
    public static String convert(String json){
        Component component = GsonComponentSerializer.gson().deserialize(json);
        return ANSIComponentSerializer.ansi().serialize(component);
    }
    public static String buildAndConvert(String legacy){
        return ANSIComponentSerializer.ansi().serialize(LegacyAndMiniMessageConverter.convertToComponent(legacy));
    }
}
