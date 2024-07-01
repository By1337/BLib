package org.by1337.blib.core.factory;

import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.impl.BukkitBlockReplacer;
import org.by1337.blib.nms.v1_16_5.block.replacer.BlockReplacerV165;
import org.by1337.blib.util.Version;

public class BlockReplacerFactory {
    public static BlockReplacer create() {
        return switch (Version.VERSION) {
            case V1_16_5 -> new BlockReplacerV165();
            default -> new BukkitBlockReplacer();
        };
    }
}
