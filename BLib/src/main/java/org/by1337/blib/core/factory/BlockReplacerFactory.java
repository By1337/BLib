package org.by1337.blib.core.factory;

import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.impl.BukkitBlockReplacer;
import org.by1337.blib.nms.V1_20_6.block.replacer.BlockReplacerV1_20_6;
import org.by1337.blib.nms.V1_21.block.replacer.BlockReplacerV1_21;
import org.by1337.blib.nms.v1_16_5.block.replacer.BlockReplacerV165;
import org.by1337.blib.nms.v1_17_1.block.replacer.BlockReplacerV1_17_1;
import org.by1337.blib.nms.v1_18_2.block.replacer.BlockReplacerV1_18_2;
import org.by1337.blib.nms.v1_19_4.block.replacer.BlockReplacerV1_19_4;
import org.by1337.blib.nms.v1_20_1.block.replacer.BlockReplacerV1_20_1;
import org.by1337.blib.nms.v1_20_2.block.replacer.BlockReplacerV1_20_2;
import org.by1337.blib.nms.v1_20_4.block.replacer.BlockReplacerV1_20_4;
import org.by1337.blib.util.Version;

public class BlockReplacerFactory {
    public static BlockReplacer create() {
        return switch (Version.VERSION) {
            case V1_16_5 -> new BlockReplacerV165();
            case V1_17_1 -> new BlockReplacerV1_17_1();
            case V1_18_2 -> new BlockReplacerV1_18_2();
            case V1_19_4 -> new BlockReplacerV1_19_4();
            case V1_20_1 -> new BlockReplacerV1_20_1();
            case V1_20_2, V1_20_3 -> new BlockReplacerV1_20_2();
            case V1_20_4, V1_20_5 -> new BlockReplacerV1_20_4();
            case V1_20_6 -> new BlockReplacerV1_20_6();
            case V1_21 -> new BlockReplacerV1_21();
            default -> new BukkitBlockReplacer();
        };
    }
}
