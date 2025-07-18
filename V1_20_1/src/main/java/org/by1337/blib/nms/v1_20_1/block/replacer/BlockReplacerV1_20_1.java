package org.by1337.blib.nms.v1_20_1.block.replacer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.ReplaceTask;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.util.Version;

@NMSAccessor(forClazz = BlockReplacer.class, forVersions = Version.V1_20_1)
public class BlockReplacerV1_20_1 implements BlockReplacer {
    @Override
    public Block replace(Vec3i pos0, BlockData data, ReplaceTask task, World world, int flag) {
        ServerLevel nmsWorld = ((CraftWorld) world).getHandle();
        BlockPos pos = toNMS(pos0);
        if (nmsWorld.isOutsideBuildHeight(pos)) return null;
        if (nmsWorld.captureTreeGeneration) return null;

        BlockState state = ((CraftBlockData) data).getState();
        if (nmsWorld.setBlock(pos, state, flag, task.getUpdateLimit())) {
            return new CraftBlock(nmsWorld, pos);
        }
        return null;
    }

    public BlockPos toNMS(Vec3i pos) {
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }
}
