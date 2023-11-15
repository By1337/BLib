package org.by1337.lib.factory;

import org.bukkit.Bukkit;
import org.by1337.api.factory.PacketEntityFactory;
import org.by1337.api.world.BLocation;
import org.by1337.api.world.entity.PacketArmorStand;
import org.by1337.api.world.entity.PacketEntity;
import org.by1337.api.util.Version;

import org.by1337.v1_16_5.world.entity.PacketArmorStandImp165;
import org.by1337.v1_17.world.entity.PacketArmorStandImpl17;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory class for creating PacketEntity instances based on the server version and entity class.
 */
public class BPacketEntityFactory implements PacketEntityFactory {
    private static final Map<Class<? extends PacketEntity>, OneArgSupplier<? extends PacketEntity, BLocation>> entityMap;


    /**
     * Registers a specific implementation of PacketEntity for a given server version and entity class.
     *
     * @param entityClass The entity class for which the implementation is registered.
     * @param supplier    A supplier that creates instances of the specified entity class.
     */
    public static void registerEntity(Class<? extends PacketEntity> entityClass, OneArgSupplier<? extends PacketEntity, BLocation> supplier) {
        entityMap.put(entityClass, supplier);
    }

    /**
     * Creates a PacketEntity instance of the specified entity class at the given location.
     *
     * @param location    The location at which the entity should be created.
     * @param entityClass The class of the entity to create.
     * @return A PacketEntity instance.
     * @throws IllegalStateException if the server version or entity class is not supported.
     */
    public <T extends PacketEntity> T create(BLocation location, Class<T> entityClass) {
        OneArgSupplier<? extends PacketEntity, BLocation> supplier = entityMap.getOrDefault(entityClass, null);
        if (supplier == null) {
            throw new IllegalStateException("Unsupported entity");
        }
        return (T) supplier.create(location);
    }

    static {
        entityMap = new HashMap<>();
        if (Version.VERSION == Version.V1_16_5) {
            BPacketEntityFactory.registerEntity(PacketArmorStand.class, PacketArmorStandImp165::new);
        } else if (Version.VERSION == Version.V1_17) {
            BPacketEntityFactory.registerEntity(PacketArmorStand.class, PacketArmorStandImpl17::new);
        }
    }
}
