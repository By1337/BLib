package org.by1337.blib.block.replacer.type.impl;

import org.bukkit.Material;
import org.by1337.blib.block.custom.CustomBlock;
import org.by1337.blib.block.replacer.type.ReplaceBlock;

public class CustomBlockReplace implements ReplaceBlock {
    public final CustomBlock customBlock;
    public final int flag;

    public CustomBlockReplace(CustomBlock customBlock) {
        this.customBlock = customBlock;
        flag = -1;
    }

    public CustomBlockReplace(CustomBlock customBlock, int flag) {
        this.customBlock = customBlock;
        this.flag = flag;
    }

    @Override
    public Material getType() {
        return customBlock.createBlockData().getMaterial();
    }
    @Override
    public ReplaceBlock withPlaceFlag(int flag0) {
        return new CustomBlockReplace(customBlock, flag0);
    }
    @Override
    public int getFlag() {
        return flag;
    }
}
