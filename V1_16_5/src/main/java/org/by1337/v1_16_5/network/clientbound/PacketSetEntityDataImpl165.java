package org.by1337.v1_16_5.network.clientbound;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import org.by1337.api.network.clientbound.entity.PacketSetEntityData;
import org.by1337.api.world.entity.PacketEntity;
import org.by1337.v1_16_5.network.PacketImpl165;
import org.by1337.v1_16_5.world.entity.PacketEntityImpl165;

import java.io.IOException;
import java.util.List;

public class PacketSetEntityDataImpl165 extends PacketImpl165 implements PacketSetEntityData {
    private int id;
    private List<DataWatcher.Item<?>> packedItems;

    public PacketSetEntityDataImpl165(PacketEntity packetEntity) {
        this(packetEntity, true);
    }

    public PacketSetEntityDataImpl165(PacketEntity packetEntity, boolean all) {
        this.init((PacketEntityImpl165) packetEntity, all);
    }

    private void init(PacketEntityImpl165 entity, boolean all) {
        this.id = entity.getId();
        if (all) {
            this.packedItems = entity.getEntityData().c();
            entity.getEntityData().e();
        } else {
            this.packedItems = entity.getEntityData().b();
        }
    }

    private PacketDataSerializer write(PacketDataSerializer buffer) {
        try {
            buffer.d(this.id);
            DataWatcher.a(this.packedItems, buffer);
            return buffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Packet<?> create() {
        try {
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
            packet.a(write(new PacketDataSerializer(Unpooled.buffer())));
            return packet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
