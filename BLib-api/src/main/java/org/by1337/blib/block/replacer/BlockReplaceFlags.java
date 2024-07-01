package org.by1337.blib.block.replacer;

/**
 * A class containing constants that define various flags for block replacement.
 */
public class BlockReplaceFlags {
    /**
     * Updates comparators
     */
    public static final int UPDATE_NEIGHBORS = 1 << 0; // 1

    /**
     * Flag to update clients.
     * When set, the change will be sent to clients to update the visual state of the block.
     */
    public static final int UPDATE_CLIENTS = 1 << 1; // 2

    /**
     * Flag to perform invisible updates.
     * When set, the update will be processed without visual changes on the client side.
     */
    public static final int UPDATE_INVISIBLE = 1 << 2; // 4

    /**
     * Flag to perform immediate updates.
     * When set, the change will be processed immediately, without any delay or asynchronous operations.
     */
    public static final int UPDATE_IMMEDIATE = 1 << 3; // 8

    /**
     * Disable physical calculations
     */
    public static final int UPDATE_KNOWN_SHAPE = 1 << 4; // 16

    /**
     * Flag to suppress item drops.
     * When set, indirectly destroyed blocks will not drop loot.
     * For example, if a block under a flower is broken, the flower would normally drop;
     * this flag will suppress such behavior.
     */
    public static final int UPDATE_SUPPRESS_DROPS = 1 << 5; // 32

    /**
     * Flag to update blocks moved by pistons.
     * Indicates that the block is being moved by a piston and should be handled accordingly.
     */
    public static final int UPDATE_MOVE_BY_PISTON = 1 << 6; // 64

    /**
     * Flag to suppress light updates.
     * When set, the block change will not trigger light updates in the world.
     */
    public static final int UPDATE_SUPPRESS_LIGHT = 1 << 7; // 128

    /**
     * Flag to indicate no updates.
     * Equivalent to `UPDATE_INVISIBLE`, used when no updates should be performed.
     */
    public static final int UPDATE_NONE = UPDATE_INVISIBLE;

    /**
     * Flag to perform all standard updates.
     * Combines `UPDATE_CLIENTS` and `UPDATE_NEIGHBORS` to update both clients and neighboring blocks.
     */
    public static final int UPDATE_ALL = UPDATE_CLIENTS + UPDATE_NEIGHBORS;

    /**
     * Flag to perform all updates immediately.
     * Combines `UPDATE_ALL` with `UPDATE_IMMEDIATE` to update clients and neighboring blocks immediately.
     */
    public static final int UPDATE_ALL_IMMEDIATE = UPDATE_ALL + UPDATE_IMMEDIATE;

    /**
     * Special Bukkit flag that disables physical updates for the placed block.
     * For example, placing sand in the air would normally cause it to fall;
     * this flag suppresses such behavior.
     */
    public static final int DONT_PLACE = 1 << 10; // 1024
    /**
     * Standard set of flags for placing a block without calculating physical operations.
     */
    public static final int NO_PHYSICS = UPDATE_CLIENTS + UPDATE_KNOWN_SHAPE + DONT_PLACE; // 1042

    /**
     * The limit for updates.
     * Defines the maximum number of updates that can be processed in a single tick to avoid performance issues.
     */
    public static final int UPDATE_LIMIT = 512;
}
