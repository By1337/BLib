package org.by1337.blib.factory;

import org.by1337.blib.world.BLocation;
import org.by1337.blib.world.entity.PacketEntity;
@Deprecated(forRemoval = true)
public interface PacketEntityFactory {
    <T extends PacketEntity> T create(BLocation location, Class<T> entityClass);
}
