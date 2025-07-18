package org.by1337.blib;

import org.bukkit.Bukkit;
import org.by1337.blib.command.CommandUtil;
import org.by1337.blib.factory.PacketEntityFactory;
import org.by1337.blib.factory.PacketFactory;
import org.by1337.blib.world.BLocation;
import org.by1337.blib.world.entity.PacketEntity;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class BLib {
    private static Api api;

    public static void setApi(@NotNull Api api) {
        if (BLib.api != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Api");
        }
        BLib.api = api;
    }

    @NotNull
    public static PacketEntityFactory getPacketEntityFactory() {
        return api.getPacketEntityFactory();
    }

    /**
     * Creates a PacketEntity instance of the specified entity class at the given location.
     *
     * @param location    The location at which the entity should be created.
     * @param entityClass The class of the entity to create.
     * @return A PacketEntity instance.
     * @throws IllegalStateException if the server version or entity class is not supported.
     */
    @NotNull
    public static <T extends PacketEntity> T createPacketEntity(BLocation location, Class<T> entityClass) {
        return getPacketEntityFactory().create(location, entityClass);
    }

    @NotNull
    public static Logger getLogger() {
        return api.getLogger();
    }

    @NotNull
    public static PacketFactory getPacketFactory() {
        return api.getPacketFactory();
    }

    public static @NotNull CommandUtil getCommandUtil() {
        return api.getCommandUtil();
    }

    public static void catchOp(@NotNull String identifier) {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Asynchronous " + identifier + "!");
        }
    }
    @NotNull
    public static Api getApi() {
        return api;
    }
}

