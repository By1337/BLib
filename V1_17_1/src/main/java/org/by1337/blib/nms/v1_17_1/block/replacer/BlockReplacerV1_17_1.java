package org.by1337.blib.nms.v1_17_1.block.replacer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.block.CapturedBlockState;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.by1337.blib.block.replacer.BlockReplaceFlags;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.ReplaceTask;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.util.Version;

import java.util.Objects;

@NMSAccessor(forClazz = BlockReplacer.class, forVersions = Version.V1_17_1)
public class BlockReplacerV1_17_1 implements BlockReplacer {
    @Override
    public Block replace(Vec3i pos0, BlockData data, ReplaceTask task, World world, int flag) {
        ServerLevel nmsWorld = ((CraftWorld) world).getHandle();
        BlockPos pos = toNMS(pos0);
        if (nmsWorld.isOutsideBuildHeight(pos.getY())) return null;
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
