package org.by1337.blib.block.replacer.type.impl;

import org.bukkit.Material;
import org.by1337.blib.block.replacer.type.ReplaceBlock;

public class MaterialBlock implements ReplaceBlock {
    public static final MaterialBlock AIR = new MaterialBlock(Material.AIR);
    public final Material material;
    public final int flag;

    public MaterialBlock(Material material) {
        this.material = material;
        flag = -1;
    }

    public MaterialBlock(Material material, int flag) {
        this.material = material;
        this.flag = flag;
    }

    @Override
    public Material getType() {
        return material;
    }
    @Override
    public ReplaceBlock withPlaceFlag(int flag0) {
        return new MaterialBlock(material, flag0);
    }
    @Override
    public int getFlag() {
        return flag;
    }
}
