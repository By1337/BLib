package org.by1337.blib.block.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.BLib;
import org.by1337.blib.block.CustomBlock;
import org.by1337.blib.block.data.CustomBlockData;
import org.by1337.blib.util.SpacedNameKey;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomBlockExample extends CustomBlock {
    /**
     * Returns the ID of the custom block.
     *
     * @return the ID of the custom block.
     */
    @Override
    public SpacedNameKey getId() {
        return new SpacedNameKey("blib", "example");
    }

    /**
     * Creates the BlockData for the custom block.
     *
     * @return the BlockData for the custom block.
     */
    @Override
    public BlockData createBlockData() {
        BlockData blockData = Material.PINK_SHULKER_BOX.createBlockData();
        ((Rotatable) blockData).setRotation(BlockFace.UP);
        return blockData;
    }

    /**
     * Called when the block is placed using BlockData. Use this method to set additional parameters if necessary.
     *
     * @param world the world where the block is placed.
     * @param x     the x-coordinate of the block.
     * @param y     the y-coordinate of the block.
     * @param z     the z-coordinate of the block.
     */
    @Override
    public void onPlace(World world, int x, int y, int z) {
        BLib.getApi().getMessage().log("CustomBlockExample#onPlace");
    }

    /**
     * Checks if a player can break this block.
     *
     * @param player          the player trying to break the block.
     * @param customBlockData the data associated with the custom block.
     * @return the status indicating whether the block can be broken.
     */
    @Override
    public Status canBreak(Player player, CustomBlockData customBlockData) {
        BLib.getApi().getMessage().log("CustomBlockExample#canBreak");
        return Status.ALLOW;
    }

    /**
     * Called when a player attempts to place the custom block.
     *
     * @param event the block place event.
     * @return the operation status and custom block data.
     */
    @Override
    public OperationStatus<CustomBlockData> onPlace(BlockPlaceEvent event) {
        BLib.getApi().getMessage().log("CustomBlockExample#onPlace");
        return new OperationStatus<>(Status.ALLOW, CustomBlockData.create(this, event.getBlockPlaced().getLocation()));
    }

    /**
     * Called when the custom block needs to be placed programmatically.
     *
     * @param world  the world where the block is placed.
     * @param x      the x-coordinate of the block.
     * @param y      the y-coordinate of the block.
     * @param z      the z-coordinate of the block.
     * @param player the player placing the block, may be null.
     * @return the operation status and custom block data.
     */
    @Override
    public OperationStatus<CustomBlockData> place(World world, int x, int y, int z, @Nullable Player player) {
        BLib.getApi().getMessage().log("CustomBlockExample#place");
        world.getBlockAt(x, y, z).setBlockData(createBlockData());
        return new OperationStatus<>(Status.ALLOW, CustomBlockData.create(this, world.getName(), x, y, z));
    }

    /**
     * Called whenever the block is removed from the world. No additional actions are required.
     *
     * @param data the data associated with the custom block.
     */
    @Override
    public void remove(CustomBlockData data) {
        BLib.getApi().getMessage().log("CustomBlockExample#remove");
    }

    /**
     * Called when a player attempts to break the block. Note that canBreak might not be called first.
     *
     * @param event the block break event.
     * @param data  the data associated with the custom block.
     * @return the status indicating the result of the break attempt.
     */
    @Override
    public Status onBreak(BlockBreakEvent event, CustomBlockData data) {
        BLib.getApi().getMessage().log("CustomBlockExample#remove");
        return Status.ALLOW;
    }

    /**
     * Returns the custom block as an item.
     *
     * @return the item representing the custom block.
     */
    @Override
    public ItemStack getItem() {
        BLib.getApi().getMessage().log("CustomBlockExample#getItem");
        ItemStack itemStack = new ItemStack(Material.PINK_SHULKER_BOX);
        itemStack.editMeta(m -> {
            m.displayName(BLib.getApi().getMessage().componentBuilder("&cCustom shulker box"));
        });
        return addNBT(itemStack);
    }

    /**
     * Returns the drops for the custom block.
     *
     * @param data the data associated with the custom block.
     * @return the list of item stacks dropped by the custom block.
     */
    @Override
    public List<ItemStack> getDrop(CustomBlockData data) {
        BLib.getApi().getMessage().log("CustomBlockExample#getDrop");
        return List.of(getItem());
    }

    /**
     * Called when a player interacts with the placed custom block.
     *
     * @param event the player interact event.
     * @param data  the data associated with the custom block.
     */
    @Override
    public void onClick(PlayerInteractEvent event, CustomBlockData data) {
        BLib.getApi().getMessage().log("CustomBlockExample#onClick");
    }

    /**
     * Called when the block attempts to explode for any reason.
     *
     * @param data the data associated with the custom block.
     * @return the status indicating the result of the explosion attempt.
     */
    @Override
    public Status explode(CustomBlockData data) {
        BLib.getApi().getMessage().log("CustomBlockExample#explode");
        return Status.DENY;
    }

    /**
     * Called when the block changes due to an entity.
     *
     * @param event the entity change block event.
     * @param data  the data associated with the custom block.
     * @return the status indicating the result of the block change.
     */
    @Override
    public Status onChange(EntityChangeBlockEvent event, CustomBlockData data) {
        BLib.getApi().getMessage().log("CustomBlockExample#onChange");
        return Status.DENY;
    }

    /**
     * Returns the piston move reaction for the custom block.
     *
     * @return the piston move reaction.
     */
    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        BLib.getApi().getMessage().log("CustomBlockExample#getPistonMoveReaction");
        return PistonMoveReaction.BLOCK;
    }

    /**
     * Called when the block is moved by a piston. It is prohibited to cancel the event here. No additional logic is required.
     *
     * @param event the block piston extend event.
     */
    @Override
    public void onMove(BlockPistonExtendEvent event, Location moveTo) {
        BLib.getApi().getMessage().log("CustomBlockExample#onMove");
    }
}
