package org.by1337.lib.chat;

import org.bukkit.entity.Player;
import org.by1337.api.util.Version;
import org.by1337.v1_16_5.chat.TellRawV1_16_5;
import org.by1337.v1_17.chat.TellRawV1_17;
import org.by1337.v1_17_1.chat.TellRawV1_17_1;
import org.by1337.v1_18.chat.TellRawV1_18;
import org.by1337.v1_18_1.chat.TellRawV1_18_1;
import org.by1337.v1_18_2.chat.TellRawV1_18_2;
import org.by1337.v1_19.chat.TellRawV1_19;
import org.by1337.v1_19_1.chat.TellRawV1_19_1;
import org.by1337.v1_19_2.chat.TellRawV1_19_2;
import org.by1337.v1_19_3.chat.TellRawV1_19_3;
import org.by1337.v1_19_4.chat.TellRawV1_19_4;
import org.by1337.v1_20_1.chat.TellRawV1_20_1;
import org.by1337.v1_20_2.chat.TellRawV1_20_2;
import org.by1337.v1_20_3.chat.TellRawV1_20_3;
import org.jetbrains.annotations.NotNull;

public class TellRaw {
    public static void tell(@NotNull String s, @NotNull Player player) {
        switch (Version.VERSION) {
            case V1_16_5 -> new TellRawV1_16_5().tell(s, player);
            case V1_17 -> new TellRawV1_17().tell(s, player);
            case V1_17_1 -> new TellRawV1_17_1().tell(s, player);
            case V1_18 -> new TellRawV1_18().tell(s, player);
            case V1_18_1 -> new TellRawV1_18_1().tell(s, player);
            case V1_18_2 -> new TellRawV1_18_2().tell(s, player);
            case V1_19 -> new TellRawV1_19().tell(s, player);
            case V1_19_1 -> new TellRawV1_19_1().tell(s, player);
            case V1_19_2 -> new TellRawV1_19_2().tell(s, player);
            case V1_19_3 -> new TellRawV1_19_3().tell(s, player);
            case V1_19_4 -> new TellRawV1_19_4().tell(s, player);
            case V1_20_1 -> new TellRawV1_20_1().tell(s, player);
            case V1_20_2 -> new TellRawV1_20_2().tell(s, player);
            case V1_20_3 -> new TellRawV1_20_3().tell(s, player);
            default -> throw new IllegalStateException("Unsupported version");
        }
    }

}
