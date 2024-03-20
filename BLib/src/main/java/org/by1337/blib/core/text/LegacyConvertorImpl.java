package org.by1337.blib.core.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.by1337.blib.minimessage.LegacyAndMiniMessageConverter;
import org.by1337.blib.text.LegacyConvertor;

public class LegacyConvertorImpl implements LegacyConvertor {
    @Override
    public Component convert(String legacy) {
        String raw = LegacyAndMiniMessageConverter.convertToGson(legacy);
        return GsonComponentSerializer.gson().deserialize(raw);
    }

}
