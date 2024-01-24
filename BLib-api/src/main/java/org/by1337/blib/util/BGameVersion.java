package org.by1337.blib.util;

import com.google.gson.JsonObject;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * A class representing a specific game version with various properties.
 */
public class BGameVersion implements GameVersion {

    private final String id; // The unique identifier of the game version (e.g., "1.16.5").
    private final String name; // The name of the game version (e.g., "1.16.5").
    private final boolean stable; // Indicates whether the game version is considered stable (true for stable versions).
    private final int worldVersion; // The version of the game's world data (e.g., 2586).
    private final int protocolVersion; // The protocol version used for communication (e.g., 754).
    private final Date buildTime; // The date and time when this version was built (e.g., "2021-01-14T16:03:41+00:00").
    private final String releaseTarget; // The target or platform for which the version was released (e.g., "1.16.5").

    /**
     * Constructs a BGameVersion object from a JSON representation.
     *
     * @param jsonObject The JSON object containing game version information.
     */
    public BGameVersion(JsonObject jsonObject) {
        this.id = jsonObject.get("id").getAsString();
        this.name = jsonObject.get("name").getAsString();
        if (jsonObject.has("release_target"))
            this.releaseTarget = jsonObject.get("release_target").getAsString();
        else releaseTarget = null;
        this.stable = jsonObject.get("stable").getAsBoolean();
        this.worldVersion = jsonObject.get("world_version").getAsInt();
        this.protocolVersion = jsonObject.get("protocol_version").getAsInt();
        this.buildTime = Date.from(ZonedDateTime.parse(jsonObject.get("build_time").getAsString()).toInstant());
    }

    /**
     * Gets the unique identifier of the game version (e.g., "1.16.5").
     *
     * @return The game version's ID.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the game version (e.g., "1.16.5").
     *
     * @return The game version's name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Checks whether the game version is considered stable (true for stable versions).
     *
     * @return True if the version is stable, false otherwise.
     */
    @Override
    public boolean isStable() {
        return stable;
    }

    /**
     * Gets the version of the game's world data (e.g., 2586).
     *
     * @return The world version.
     */
    @Override
    public int getWorldVersion() {
        return worldVersion;
    }

    /**
     * Gets the protocol version used for communication (e.g., 754).
     *
     * @return The protocol version.
     */
    @Override
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Gets the date and time when this version was built (e.g., "2021-01-14T16:03:41+00:00").
     *
     * @return The build time as a Date object.
     */
    @Override
    public Date getBuildTime() {
        return buildTime;
    }

    /**
     * Gets the target or platform for which the version was released (e.g., "1.16.5").
     *
     * @return The release target.
     */
    @Override
    public String getReleaseTarget() {
        return releaseTarget;
    }

    @Override
    public String toString() {
        return "BGameVersion{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", stable=" + stable +
                ", worldVersion=" + worldVersion +
                ", protocolVersion=" + protocolVersion +
                ", buildTime=" + buildTime +
                ", releaseTarget='" + releaseTarget + '\'' +
                '}';
    }
}
