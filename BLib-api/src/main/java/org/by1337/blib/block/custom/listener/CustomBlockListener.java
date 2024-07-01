package org.by1337.blib.block.custom.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.block.custom.CustomBlock;
import org.by1337.blib.block.custom.data.CustomBlockData;
import org.by1337.blib.block.custom.registry.BlockRegistry;
import org.by1337.blib.block.custom.registry.WorldRegistry;
import org.by1337.blib.nbt.impl.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class CustomBlockListener implements Listener {
    private final WorldRegistry<CustomBlockData> worldRegistry;

    public CustomBlockListener(WorldRegistry<CustomBlockData> worldRegistry) {
        this.worldRegistry = worldRegistry;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        CustomBlockData data = worldRegistry.get(event.getClickedBlock().getLocation());
        if (data == null) return;
        CustomBlock block = BlockRegistry.get().getCustomBlock(data.getId());
        block.onClick(event, data);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        CustomBlockData data = worldRegistry.get(event.getBlock().getLocation());
        if (data == null) return;
        CustomBlock block = BlockRegistry.get().getCustomBlock(data.getId());
        block.onBreak(event, data);
        if (event.isCancelled()) return;
        block.remove(data);
        worldRegistry.remove(event.getBlock().getLocation());
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
        for (ItemStack itemStack : block.getDrop(data)) {
            if (itemStack == null || itemStack.getType().isAir()) continue;
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        var item = event.getItemInHand();
        for (CustomBlock value : BlockRegistry.get().getAll()) {
            if (value.isIt(item)) {
                var status = value.onPlace(event);
                if (status.getStatus().isCanceled()) {
                    event.setCancelled(true);
                    return;
                }
                worldRegistry.add(event.getBlockPlaced().getLocation(), status.getResult());
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPistonMove(BlockPistonExtendEvent event) {
        List<CustomBlockData> customBlocks = new ArrayList<>();
        for (Block bukkitBlock : event.getBlocks()) {
            CustomBlockData data = worldRegistry.get(bukkitBlock.getLocation());
            if (data == null) continue;
            customBlocks.add(data);
        }
        for (CustomBlockData data : customBlocks) {
            CustomBlock customBlock = BlockRegistry.get().getCustomBlock(data.getId());
            if (customBlock.getPistonMoveReaction() == PistonMoveReaction.BLOCK || customBlock.getPistonMoveReaction() == PistonMoveReaction.IGNORE) {
                event.setCancelled(true);
                return;
            }
            Block block = event.getBlock().getWorld().getBlockAt(data.getBlockX(), data.getBlockY(), data.getBlockZ());

            if (customBlock.getPistonMoveReaction() == PistonMoveReaction.BREAK || block.getPistonMoveReaction() == PistonMoveReaction.BREAK) {
                customBlock.remove(data);
                worldRegistry.remove(data.getWorld(), data.getBlockX(), data.getBlockY(), data.getBlockZ());
                block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0, 0.5), 30, block.getBlockData());
                block.setType(Material.AIR);
                for (ItemStack itemStack : customBlock.getDrop(data)) {
                    if (itemStack == null || itemStack.getType().isAir()) continue;
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);
                }
                return;
            } else {
                Location location = new Location(Bukkit.getWorld(data.getWorld()), data.getBlockX(), data.getBlockY(), data.getBlockZ());
                worldRegistry.remove(location);
                location.add(event.getDirection().getDirection());
                customBlock.onMove(event, location);
                if (event.isCancelled()) {
                    BlockRegistry.getPlugin(customBlock.getClass()).getLogger().severe("At this moment it is forbidden to cancel the event!");
                    event.setCancelled(false);
                }
                CompoundTag compoundTag = data.getData().getAsCompoundTag("info");
                compoundTag.putInt("x", location.getBlockX());
                compoundTag.putInt("y", location.getBlockY());
                compoundTag.putInt("z", location.getBlockZ());
                worldRegistry.add(location, data);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onExplode(EntityExplodeEvent event) {
        explode(event.blockList());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onExplode(BlockExplodeEvent event) {
        explode(event.blockList());
    }

    private void explode(List<Block> blockList) {
        blockList.removeIf(block -> {
            CustomBlockData data = worldRegistry.get(block.getLocation());
            if (data == null) return false;
            CustomBlock customBlock = BlockRegistry.get().getCustomBlock(data.getId());
            if (customBlock.explode(data).isAllowed()) {
                customBlock.remove(data);
                worldRegistry.remove(block.getLocation());
                for (ItemStack itemStack : customBlock.getDrop(data)) {
                    if (itemStack == null || itemStack.getType().isAir()) continue;
                    block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
                }
                block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0, 0.5), 30, block.getBlockData());
                block.setType(Material.AIR);
            }
            return true;
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChange(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        CustomBlockData data = worldRegistry.get(block.getLocation());
        if (data == null) return;
        CustomBlock customBlock = BlockRegistry.get().getCustomBlock(data.getId());
        customBlock.onChange(event, data);
        if (!event.isCancelled()) {
            customBlock.remove(data);
            event.setCancelled(true);
            worldRegistry.remove(block.getLocation());
            for (ItemStack itemStack : customBlock.getDrop(data)) {
                if (itemStack == null || itemStack.getType().isAir()) continue;
                block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
            }
            block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0, 0.5), 30, block.getBlockData());
            block.setType(Material.AIR);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void pluginUnload(PluginDisableEvent event) {
        BlockRegistry.get().unregisterAll(event.getPlugin());
    }
}

