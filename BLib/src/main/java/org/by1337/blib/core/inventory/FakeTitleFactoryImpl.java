package org.by1337.blib.core.inventory;

import org.by1337.blib.inventory.FakeTitle;
import org.by1337.blib.inventory.FakeTitleFactory;
import org.by1337.blib.nms.V1_21.inventory.FakeTitleV1_21;
import org.by1337.blib.nms.V1_21.inventory.FakeTitleV1_21_1;
import org.by1337.blib.util.Version;
import org.by1337.blib.nms.v1_16_5.inventory.FakeTitleV1_16_5;
import org.by1337.blib.nms.v1_17_1.inventory.FakeTitleV1_17_1;
import org.by1337.blib.nms.v1_18_2.inventory.FakeTitleV1_18_2;
import org.by1337.blib.nms.v1_19_4.inventory.FakeTitleV1_19_4;
import org.by1337.blib.nms.v1_20_1.inventory.FakeTitleV1_20_1;
import org.by1337.blib.nms.v1_20_2.inventory.FakeTitleV1_20_2;
import org.by1337.blib.nms.v1_20_4.inventory.FakeTitleV1_20_4;
import org.by1337.blib.nms.V1_20_6.inventory.FakeTitleV1_20_6;

public class FakeTitleFactoryImpl implements FakeTitleFactory {
    private final FakeTitle fakeTitle = switch (Version.VERSION) {
        case V1_16_5 -> new FakeTitleV1_16_5();
        case V1_17_1 -> new FakeTitleV1_17_1();
        case V1_18_2 -> new FakeTitleV1_18_2();
        case V1_19_4 -> new FakeTitleV1_19_4();
        case V1_20_1 -> new FakeTitleV1_20_1();
        case V1_20_2 -> new FakeTitleV1_20_2();
        case V1_20_4, V1_20_3 -> new FakeTitleV1_20_4();
        case V1_20_5, V1_20_6 -> new FakeTitleV1_20_6();
        case V1_21 -> new FakeTitleV1_21();
        case V1_21_1 -> new FakeTitleV1_21_1();
        default -> throw new IllegalStateException("Unsupported version! use 1.16.5, 1.17.1, 1.18.2, 1.19.4, 1.20.x, 1.21.x. Version: '" + Version.VERSION.getVer() + "', GameVersion: '" + Version.getGameVersion() + "'");
    };

    public FakeTitle get() {
        return fakeTitle;
    }
}

