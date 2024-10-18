package org.by1337.blib.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Represents a custom location in the game world.
 */
public class BLocation {
    private double x;
    private double y;
    private double z;
    private float yaw = 0;
    private float pitch = 0;
    private String worldName;

    /**
     * Constructs a new BLocation with specified coordinates, yaw, pitch, and world name.
     *
     * @param x         The X-coordinate of the location.
     * @param y         The Y-coordinate of the location.
     * @param z         The Z-coordinate of the location.
     * @param yaw       The yaw angle (horizontal rotation) of the location.
     * @param pitch     The pitch angle (vertical rotation) of the location.
     * @param worldName The name of the world where the location exists.
     */
    public BLocation(double x, double y, double z, float yaw, float pitch, String worldName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.worldName = worldName;
    }

    /**
     * Constructs a new BLocation with specified coordinates and world name.
     *
     * @param x         The X-coordinate of the location.
     * @param y         The Y-coordinate of the location.
     * @param z         The Z-coordinate of the location.
     * @param worldName The name of the world where the location exists.
     */
    public BLocation(double x, double y, double z, String worldName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldName = worldName;
    }

    /**
     * Constructs a BLocation from a Bukkit Location object.
     *
     * @param location The Bukkit Location to convert to a BLocation.
     */
    public BLocation(Location location) {
        this(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), location.getWorld().getName());
    }

    /**
     * Converts the BLocation to a Bukkit Location.
     *
     * @return The Bukkit Location representing this BLocation.
     */
    public Location getLocation() {
        return new Location(
                Bukkit.getWorld(worldName),
                x,
                y,
                z,
                yaw,
                pitch
        );
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String toString() {
        return "BLocation(x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", yaw=" + this.getYaw() + ", pitch=" + this.getPitch() + ", worldName=" + this.getWorldName() + ")";
    }
}
