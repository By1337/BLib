package org.by1337.blib.nms.v1_16_5.block.replacer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.ReplaceTask;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.util.Version;

@NMSAccessor(forClazz = BlockReplacer.class, forVersions = Version.V1_16_5)
public class BlockReplacerV1165 implements BlockReplacer {
    @Override
    public Block replace(Vec3i pos0, BlockData data, ReplaceTask task, World world, int flag) {
        ServerLevel nmsWorld = ((CraftWorld) world).getHandle();
        BlockPos pos = toNMS(pos0);
        if (pos.getY() < 0 || pos.getY() > 255) return null;
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

