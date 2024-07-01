package org.by1337.blib.block.replacer.type.impl;

import org.bukkit.Material;
import org.by1337.blib.block.replacer.type.ReplaceBlock;

public class MaterialBlock implements ReplaceBlock {
    public final Material material;

    public MaterialBlock(Material material) {
        this.material = material;
    }
}
