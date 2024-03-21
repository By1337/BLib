package org.by1337.blib.text;

import net.kyori.adventure.text.Component;
import org.by1337.blib.BLib;

public interface LegacyConvertor {
    static LegacyConvertor get() {
        return BLib.getApi().getLegacyConvertor();
    }

    static Component convert0(String legacy) {
        return get().convert(legacy);
    }

    Component convert(String legacy);
}
