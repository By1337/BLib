package org.by1337.blib.text;

import dev.by1337.core.util.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;

public interface LegacyConvertor {
    LegacyConvertor INSTANCE = MiniMessage::deserialize;

    static LegacyConvertor get() {
        return INSTANCE;
    }

    static Component convert0(String legacy) {
        return INSTANCE.convert(legacy);
    }

    Component convert(String legacy);
}
