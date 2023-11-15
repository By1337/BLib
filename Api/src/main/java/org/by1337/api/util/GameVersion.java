package org.by1337.api.util;

import java.util.Date;

/**
 * An interface representing a game version with various properties.
 */
public interface GameVersion {
    /**
     * Gets the unique identifier of the game version.
     *
     * @return The game version's ID.
     */
    String getId();

    /**
     * Gets the name of the game version.
     *
     * @return The game version's name.
     */
    String getName();

    /**
     * Gets the target or platform for which the version was released.
     *
     * @return The release target.
     */
    String getReleaseTarget();

    /**
     * Gets the version of the game's world data.
     *
     * @return The world version.
     */
    int getWorldVersion();

    /**
     * Gets the protocol version used for communication.
     *
     * @return The protocol version.
     */
    int getProtocolVersion();

    /**
     * Gets the date and time when this version was built.
     *
     * @return The build time as a Date object.
     */
    Date getBuildTime();

    /**
     * Checks whether the game version is considered stable (true for stable versions).
     *
     * @return True if the version is stable, false otherwise.
     */
    boolean isStable();
}
