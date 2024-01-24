package org.by1337.blib.hologaram;

import org.bukkit.entity.Player;
import org.by1337.blib.BLib;
import org.by1337.blib.network.clientbound.entity.PacketAddEntity;
import org.by1337.blib.network.clientbound.entity.PacketSetEntityData;
import org.by1337.blib.network.clientbound.entity.PacketRemoveEntity;
import org.by1337.blib.network.clientbound.entity.TeleportEntityPacket;
import org.by1337.blib.world.BLocation;
import org.by1337.blib.world.entity.PacketArmorStand;
import org.jetbrains.annotations.NotNull;

/**
 * The HologramLine class represents an individual line of text within a hologram.
 */
public class HologramLine {
    private String textLine;
    private PacketArmorStand entity;
    private PacketAddEntity spawnPacket;
    private PacketSetEntityData dataPacket;
    private PacketRemoveEntity removePacket;
    private TeleportEntityPacket teleportPacket;

    /**
     * Constructs a new HologramLine instance with the specified location and text line.
     *
     * @param location The location where the hologram line is displayed.
     * @param line     The text content of the hologram line.
     */
    public HologramLine(@NotNull BLocation location, @NotNull String line) {
        this.textLine = line;

        entity = BLib.createPacketEntity(location, PacketArmorStand.class);
        entity.setCustomName(line);
        entity.setCustomNameVisible(true);
        entity.setSmall(true);
        entity.setInvisible(true);
        entity.setNoBasePlate(true);
        entity.setSilent(true);
        entity.setNoGravity(true);
        entity.setMarker(true);

        spawnPacket = PacketAddEntity.newInstance(entity);
        dataPacket = PacketSetEntityData.newInstance(entity);
        removePacket = PacketRemoveEntity.newInstance(entity);

    }

    /**
     * Spawns the hologram line for a specific player and updates its metadata.
     *
     * @param player The player for whom the hologram line should be spawned.
     */
    public void spawnFor(@NotNull Player player) {
        spawnPacket.send(player);
        updateMetaFor(player);
    }

    /**
     * Updates the metadata of the hologram line for a specific player.
     *
     * @param player The player for whom the metadata should be updated.
     */
    public void updateMetaFor(@NotNull Player player) {
        dataPacket.send(player);
    }

    /**
     * Removes the hologram line for a specific player.
     *
     * @param player The player for whom the hologram line should be removed.
     */
    public void removeFor(@NotNull Player player) {
        removePacket.send(player);
    }

    /**
     * Teleports the hologram line for a specific player.
     *
     * @param player The player for whom the hologram line should be teleported.
     */
    public void teleportFor(@NotNull Player player) {
        if (teleportPacket == null) {
            teleportPacket = TeleportEntityPacket.newInstance(entity);
        }
        teleportPacket.send(player);
    }

    /**
     * Sets the text content of the hologram line.
     *
     * @param textLine The new text content for the hologram line.
     */
    public void setText(@NotNull String textLine) {
        this.textLine = textLine;
        entity.setCustomName(textLine);
        dataPacket = PacketSetEntityData.newInstance(entity);
    }

    /**
     * Retrieves the text content of the hologram line.
     *
     * @return The text content of the hologram line.
     */
    @NotNull
    public String getText() {
        return textLine;
    }

    /**
     * Sets the location where the hologram line is displayed.
     *
     * @param location The new location for the hologram line.
     */
    public void setLocation(@NotNull BLocation location) {
        entity.setLocation(location);
        teleportPacket = TeleportEntityPacket.newInstance(entity);
        spawnPacket = PacketAddEntity.newInstance(entity);
    }
}
