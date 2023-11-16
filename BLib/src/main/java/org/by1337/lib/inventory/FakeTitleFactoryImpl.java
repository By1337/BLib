package org.by1337.lib.inventory;

import org.by1337.api.inventory.FakeTitle;
import org.by1337.api.inventory.FakeTitleFactory;
import org.by1337.api.util.Version;
import org.by1337.v1_16_5.inventory.FakeTitleV1_16_5;
import org.by1337.v1_17.inventory.FakeTitleV1_17;

public class FakeTitleFactoryImpl implements FakeTitleFactory {
    public FakeTitle get() {
        return switch (Version.VERSION) {
            case V1_16_5 -> new FakeTitleV1_16_5();
            case V1_17 -> new FakeTitleV1_17();
            case V1_17_1 -> null;
            case V1_18 -> null;
            case V1_18_1 -> null;
            case V1_18_2 -> null;
            case V1_19 -> null;
            case V1_19_1 -> null;
            case V1_19_2 -> null;
            case V1_19_3 -> null;
            case V1_19_4 -> null;
            case V1_20_1 -> null;
            case UNKNOWN -> null;
        };
    }
}

