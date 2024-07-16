package org.by1337.blib.block.custom;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.by1337.blib.block.custom.data.CustomBlockData;
import org.by1337.blib.util.SpacedNameKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class CustomBlock {
    /**
     * Returns the ID of the custom block.
     *
     * @return the ID of the custom block.
     */
    public abstract SpacedNameKey getId();

    /**
     * Creates the BlockData for the custom block.
     *
     * @return the BlockData for the custom block.
     */
    public abstract BlockData createBlockData();

    /**
     * Called when the block is placed using BlockData. Use this method to set additional parameters if necessary.
     *
     * @param world the world where the block is placed.
     * @param x     the x-coordinate of the block.
     * @param y     the y-coordinate of the block.
     * @param z     the z-coordinate of the block.
     * @return
     */
    public abstract @NotNull CustomBlockData doPlace(World world, int x, int y, int z);

    /**
     * Checks if a player can break this block.
     *
     * @param player          the player trying to break the block.
     * @param customBlockData the data associated with the custom block.
     * @return the status indicating whether the block can be broken.
     */
    public abstract Status canBreak(Player player, CustomBlockData customBlockData);

    /**
     * Called when a player attempts to place the custom block.
     *
     * @param event the block place event.
     * @return the operation status and custom block data.
     */
    public abstract OperationStatus<CustomBlockData> onPlace(BlockPlaceEvent event);

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
    public abstract OperationStatus<CustomBlockData> place(World world, int x, int y, int z, @Nullable Player player);

    /**
     * Called whenever the block is removed from the world. No additional actions are required.
     *
     * @param data the data associated with the custom block.
     */
    public abstract void remove(CustomBlockData data);

    /**
     * Called when a player attempts to break the block. Note that canBreak might not be called first.
     *
     * @param event the block break event.
     * @param data  the data associated with the custom block.
     * @return the status indicating the result of the break attempt.
     */
    public abstract Status onBreak(BlockBreakEvent event, CustomBlockData data);

    /**
     * Returns the custom block as an item.
     *
     * @return the item representing the custom block.
     */
    public abstract ItemStack getItem();

    /**
     * Default implementation to add NBT tag to the custom item.
     *
     * @param itemStack the item stack to add NBT tag to.
     * @return the item stack with NBT tag added.
     */
    protected ItemStack addNBT(ItemStack itemStack) {
        itemStack.editMeta(m -> {
            m.getPersistentDataContainer().set(new NamespacedKey(getPlugin(), getId().getSpace().getName() + "_" + getId().getName().getName()), PersistentDataType.BYTE, (byte) 1);
        });
        return itemStack;
    }

    /**
     * Default implementation to check if an item stack is a custom item. Can be overridden.
     *
     * @param itemStack the item stack to check.
     * @return true if the item stack is a custom item, false otherwise.
     */
    public boolean isIt(@Nullable ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(getPlugin(), getId().getSpace().getName() + "_" + getId().getName().getName()), PersistentDataType.BYTE);
    }

    /**
     * Returns the drops for the custom block.
     *
     * @param data the data associated with the custom block.
     * @return the list of item stacks dropped by the custom block.
     */
    public abstract List<ItemStack> getDrop(CustomBlockData data);

    /**
     * Called when a player interacts with the placed custom block.
     *
     * @param event the player interact event.
     * @param data  the data associated with the custom block.
     */
    public abstract void onClick(PlayerInteractEvent event, CustomBlockData data);

    /**
     * Called when the block attempts to explode for any reason.
     *
     * @param data the data associated with the custom block.
     * @return the status indicating the result of the explosion attempt.
     */
    public abstract Status explode(CustomBlockData data);

    /**
     * Called when the block changes due to an entity.
     *
     * @param event the entity change block event.
     * @param data  the data associated with the custom block.
     * @return the status indicating the result of the block change.
     */
    public abstract Status onChange(EntityChangeBlockEvent event, CustomBlockData data);

    /**
     * Returns the piston move reaction for the custom block.
     *
     * @return the piston move reaction.
     */
    public abstract PistonMoveReaction getPistonMoveReaction();

    /**
     * Called when the block is moved by a piston. It is prohibited to cancel the event here. No additional logic is required.
     *
     * @param event the block piston extend event.
     */
    public abstract void onMove(BlockPistonExtendEvent event, Location moveTo);

    /**
     * Returns the plugin instance.
     *
     * @return the plugin instance.
     */
    private Plugin getPlugin() {
        if (this.getClass().getClassLoader() instanceof PluginClassLoader pluginClassLoader) {
            return pluginClassLoader.getPlugin();
        }
        throw new IllegalStateException("Failed to get plugin!");
    }

    public static class OperationStatus<T> {
        public static OperationStatus<?> DENY = new OperationStatus<>(Status.DENY, null);
        private final Status status;
        private final @Nullable T result;

        /**
         * Constructor for OperationStatus.
         *
         * @param status the status of the operation.
         * @param result the result of the operation.
         */
        public OperationStatus(Status status, @Nullable T result) {
            this.status = status;
            this.result = result;
        }

        /**
         * Returns the status of the operation.
         *
         * @return the status of the operation.
         */
        public Status getStatus() {
            return status;
        }

        /**
         * Returns the result of the operation.
         *
         * @return the result of the operation.
         */
        public T getResult() {
            return result;
        }
        public static <T> OperationStatus<T> deny(){
            return (OperationStatus<T>) DENY;
        }
    }

    public enum Status {
        ALLOW(true),
        DENY(false),
        DONE(true),
        CANCELED(false);

        private final boolean done;

        /**
         * Constructor for Status enum.
         *
         * @param done indicates if the operation is done.
         */
        Status(boolean done) {
            this.done = done;
        }

        /**
         * Indicates if the operation is done.
         *
         * @return true if the operation is done, false otherwise.
         */
        public boolean isDone() {
            return done;
        }

        /**
         * Indicates if the operation is allowed.
         *
         * @return true if the operation is allowed, false otherwise.
         */
        public boolean isAllowed() {
            return done;
        }

        /**
         * Indicates if the operation is canceled.
         *
         * @return true if the operation is canceled, false otherwise.
         */
        public boolean isCanceled() {
            return !done;
        }

        /**
         * Indicates if the operation is denied.
         *
         * @return true if the operation is denied, false otherwise.
         */
        public boolean isDeny() {
            return !done;
        }
    }
}