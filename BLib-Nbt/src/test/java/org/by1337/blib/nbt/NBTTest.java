package org.by1337.blib.nbt;

import org.by1337.blib.nbt.impl.ByteNBT;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NBTTest {
    @Test
    public void asTest() {
        NBT byteNBT = ByteNBT.valueOf((byte) 2);

        assertNotNull(byteNBT.as(ByteNBT.class, null));
    }

    @Test
    public void uuidTest() {
        CompoundTag tag = new CompoundTag();
        UUID uuid = UUID.randomUUID();
        tag.putMojangUUID("uuid", uuid);
        assertEquals(uuid, tag.getAsMojangUUID("uuid"));
        assertEquals(uuid, tag.getAsUnknownUUID("uuid"));
        tag.putUUID("uuid", uuid);
        assertEquals(uuid, tag.getAsUUID("uuid"));
        assertEquals(uuid, tag.getAsUnknownUUID("uuid"));
    }

}