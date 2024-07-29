package org.by1337.blib.core.inventory;

import org.by1337.blib.inventory.ItemStackSerialize;
import org.by1337.blib.nms.V1_20_6.inventory.ItemStackSerializeV1_20_6;
import org.by1337.blib.nms.V1_21.inventory.ItemStackSerializeV1_21;
import org.by1337.blib.util.Version;
import org.by1337.blib.nms.v1_16_5.inventory.ItemStackSerializeV1_16_5;
import org.by1337.blib.nms.v1_17_1.inventory.ItemStackSerializeV1_17_1;
import org.by1337.blib.nms.v1_18_2.inventory.ItemStackSerializeV1_18_2;
import org.by1337.blib.nms.v1_19_4.inventory.ItemStackSerializeV1_19_4;
import org.by1337.blib.nms.v1_20_1.inventory.ItemStackSerializeV1_20_1;
import org.by1337.blib.nms.v1_20_2.inventory.ItemStackSerializeV1_20_2;
import org.by1337.blib.nms.v1_20_4.inventory.ItemStackSerializeV1_20_4;

public class ItemStackSerializeFactory {
    public static ItemStackSerialize create() {
        return switch (Version.VERSION) {
            case V1_16_5 -> new ItemStackSerializeV1_16_5();
            case V1_17_1 -> new ItemStackSerializeV1_17_1();
            case V1_18_2 -> new ItemStackSerializeV1_18_2();
            case V1_19_4 -> new ItemStackSerializeV1_19_4();
            case V1_20_1 -> new ItemStackSerializeV1_20_1();
            case V1_20_2 -> new ItemStackSerializeV1_20_2();
            case V1_20_4, V1_20_3 -> new ItemStackSerializeV1_20_4();
            case V1_20_5, V1_20_6 -> new ItemStackSerializeV1_20_6();
            case V1_21 -> new ItemStackSerializeV1_21();
            default ->
                    throw new IllegalStateException("Unsupported version! use 1.16.5, 1.17.1, 1.18.2, 1.19.4, 1.20.(1|2|3|4|5|6)");
        };
    }
}
