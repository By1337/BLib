package org.by1337.blib.block.replacer;

import org.bukkit.block.Block;
import org.by1337.blib.block.replacer.type.ReplaceBlock;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ReplaceTask {
    @Nullable Pair<Vec3i, ReplaceBlock> next();

    boolean isEnd();

    int getFlag();

    int getUpdateLimit();

    @Nullable
    Predicate<Block> getFilter();

    @Nullable
    Consumer<Block> getBlockBreakCallBack();

    int getMaxReplacesInTick();

    default @Nullable BlockReplacer getCustomBlockReplacer() {
        return null;
    }
}
