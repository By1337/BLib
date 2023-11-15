package org.by1337.api.factory;

import org.by1337.api.world.BLocation;
import org.by1337.api.world.entity.PacketEntity;

public interface PacketEntityFactory {
    <T extends PacketEntity> T create(BLocation location, Class<T> entityClass);
}
