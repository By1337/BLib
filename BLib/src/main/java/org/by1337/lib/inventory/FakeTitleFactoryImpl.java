package org.by1337.lib.inventory;

import org.by1337.api.inventory.FakeTitle;
import org.by1337.api.inventory.FakeTitleFactory;
import org.by1337.api.util.Version;
import org.by1337.v1_16_5.inventory.FakeTitleV1_16_5;
import org.by1337.v1_17.inventory.FakeTitleV1_17;
import org.by1337.v1_17_1.inventory.FakeTitleV1_17_1;
import org.by1337.v1_18.inventory.FakeTitleV1_18;
import org.by1337.v1_18_1.inventory.FakeTitleV1_18_1;
import org.by1337.v1_18_2.inventory.FakeTitleV1_18_2;
import org.by1337.v1_19.inventory.FakeTitleV1_19;
import org.by1337.v1_19_1.inventory.FakeTitleV1_19_1;
import org.by1337.v1_19_2.inventory.FakeTitleV1_19_2;
import org.by1337.v1_19_3.inventory.FakeTitleV1_19_3;
import org.by1337.v1_19_4.inventory.FakeTitleV1_19_4;
import org.by1337.v1_20_1.inventory.FakeTitleV1_20_1;
import org.by1337.v1_20_2.inventory.FakeTitleV1_20_2;
import org.by1337.v1_20_3.inventory.FakeTitleV1_20_3;

public class FakeTitleFactoryImpl implements FakeTitleFactory {
    public FakeTitle get() {
        return switch (Version.VERSION) {
            case V1_16_5 -> new FakeTitleV1_16_5();
            case V1_17 -> new FakeTitleV1_17();
            case V1_17_1 -> new FakeTitleV1_17_1();
            case V1_18 -> new FakeTitleV1_18();
            case V1_18_1 -> new FakeTitleV1_18_1();
            case V1_18_2 -> new FakeTitleV1_18_2();
            case V1_19 -> new FakeTitleV1_19();
            case V1_19_1 -> new FakeTitleV1_19_1();
            case V1_19_2 -> new FakeTitleV1_19_2();
            case V1_19_3 -> new FakeTitleV1_19_3();
            case V1_19_4 -> new FakeTitleV1_19_4();
            case V1_20_1 -> new FakeTitleV1_20_1();
            case V1_20_2 -> new FakeTitleV1_20_2();
            case V1_20_3 -> new FakeTitleV1_20_3();
            default -> null;
        };
    }
}

