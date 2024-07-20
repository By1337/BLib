package org.by1337.blib.block.replacer.type.impl;

import org.bukkit.Material;
import org.by1337.blib.block.custom.CustomBlock;
import org.by1337.blib.block.replacer.type.ReplaceBlock;

public class CustomBlockReplace implements ReplaceBlock {
    public final CustomBlock customBlock;

    public CustomBlockReplace(CustomBlock customBlock) {
        this.customBlock = customBlock;
    }

    @Override
    public Material getType() {
        return customBlock.createBlockData().getMaterial();
    }
}
