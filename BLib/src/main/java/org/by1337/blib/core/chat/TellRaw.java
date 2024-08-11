package org.by1337.blib.core.chat;

import org.bukkit.entity.Player;
import org.by1337.blib.nms.V1_20_6.chat.TellRawV1_20_6;
import org.by1337.blib.nms.V1_21.chat.TellRawV1_21;
import org.by1337.blib.nms.V1_21.chat.TellRawV1_21_1;
import org.by1337.blib.util.Version;
import org.by1337.blib.nms.v1_16_5.chat.TellRawV1_16_5;
import org.by1337.blib.nms.v1_17_1.chat.TellRawV1_17_1;
import org.by1337.blib.nms.v1_18_2.chat.TellRawV1_18_2;
import org.by1337.blib.nms.v1_19_4.chat.TellRawV1_19_4;
import org.by1337.blib.nms.v1_20_1.chat.TellRawV1_20_1;
import org.by1337.blib.nms.v1_20_2.chat.TellRawV1_20_2;
import org.by1337.blib.nms.v1_20_4.chat.TellRawV1_20_4;
import org.jetbrains.annotations.NotNull;

public class TellRaw {
    public static void tell(@NotNull String s, @NotNull Player player) {
        switch (Version.VERSION) {
            case V1_16_5 -> new TellRawV1_16_5().tell(s, player);
            case V1_17_1 -> new TellRawV1_17_1().tell(s, player);
            case V1_18_2 -> new TellRawV1_18_2().tell(s, player);
            case V1_19_4 -> new TellRawV1_19_4().tell(s, player);
            case V1_20_1 -> new TellRawV1_20_1().tell(s, player);
            case V1_20_2 -> new TellRawV1_20_2().tell(s, player);
            case V1_20_4, V1_20_3 -> new TellRawV1_20_4().tell(s, player);
            case V1_20_5, V1_20_6 -> new TellRawV1_20_6().tell(s, player);
            case V1_21 -> new TellRawV1_21().tell(s, player);
            case V1_21_1 -> new TellRawV1_21_1().tell(s, player);
            default -> throw new IllegalStateException("Unsupported version! use 1.16.5, 1.17.1, 1.18.2, 1.19.4, 1.20.x, 1.21.x. Version: '" + Version.VERSION.getVer() + "', GameVersion: '" + Version.getGameVersion() + "'");
        }
    }

}
