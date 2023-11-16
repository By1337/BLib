package org.by1337.lib.inventory;

import org.by1337.api.inventory.ItemStackSerialize;
import org.by1337.api.util.Version;
import org.by1337.v1_16_5.inventory.ItemStackSerializeV1_16_5;
import org.by1337.v1_17.inventory.ItemStackSerializeV1_17;
import org.by1337.v1_17_1.inventory.ItemStackSerializeV1_17_1;
import org.by1337.v1_18.inventory.ItemStackSerializeV1_18;
import org.by1337.v1_18_1.inventory.ItemStackSerializeV1_18_1;
import org.by1337.v1_18_2.inventory.ItemStackSerializeV1_18_2;
import org.by1337.v1_19.inventory.ItemStackSerializeV1_19;
import org.by1337.v1_19_1.inventory.ItemStackSerializeV1_19_1;
import org.by1337.v1_19_2.inventory.ItemStackSerializeV1_19_2;
import org.by1337.v1_19_3.inventory.ItemStackSerializeV1_19_3;
import org.by1337.v1_19_4.inventory.ItemStackSerializeV1_19_4;
import org.by1337.v1_20_1.inventory.ItemStackSerializeV1_20_1;

public class ItemStackSerializeFactory {
    public static ItemStackSerialize create() {
        if (Version.VERSION == Version.V1_16_5) {
            return new ItemStackSerializeV1_16_5();
        } else if (Version.VERSION == Version.V1_17) {
            return new ItemStackSerializeV1_17();
        } else if (Version.VERSION == Version.V1_17_1) {
            return new ItemStackSerializeV1_17_1();
        } else if (Version.VERSION == Version.V1_18) {
            return new ItemStackSerializeV1_18();
        } else if (Version.VERSION == Version.V1_18_1) {
            return new ItemStackSerializeV1_18_1();
        } else if (Version.VERSION == Version.V1_18_2) {
            return new ItemStackSerializeV1_18_2();
        } else if (Version.VERSION == Version.V1_19) {
            return new ItemStackSerializeV1_19();
        } else if (Version.VERSION == Version.V1_19_1) {
            return new ItemStackSerializeV1_19_1();
        } else if (Version.VERSION == Version.V1_19_2) {
            return new ItemStackSerializeV1_19_2();
        } else if (Version.VERSION == Version.V1_19_3) {
            return new ItemStackSerializeV1_19_3();
        } else if (Version.VERSION == Version.V1_19_4) {
            return new ItemStackSerializeV1_19_4();
        } else if (Version.VERSION == Version.V1_20_1) {
            return new ItemStackSerializeV1_20_1();
        } else{
            throw new IllegalStateException("Unsupported version");
        }

    }
}
