package org.by1337.blib.nms.v1_16_5.network.clientbound;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataItem;
import org.by1337.blib.network.clientbound.entity.PacketSetEntityData;
import org.by1337.blib.nms.v1_16_5.network.PacketImpl165;
import org.by1337.blib.nms.v1_16_5.world.entity.PacketEntityImpl165;
import org.by1337.blib.world.entity.PacketEntity;

public class PacketSetEntityDataImpl165 extends PacketImpl165 implements PacketSetEntityData {
    private int id;
    private List<DataItem<?>> packedItems;

    public PacketSetEntityDataImpl165(PacketEntity packetEntity) {
        this(packetEntity, true);
    }

    public PacketSetEntityDataImpl165(PacketEntity packetEntity, boolean all) {
        this.init((PacketEntityImpl165)packetEntity, all);
    }

    private void init(PacketEntityImpl165 entity, boolean all) {
        this.id = entity.getId();
        if (all) {
            this.packedItems = entity.getEntityData().getAll();
            entity.getEntityData().clearDirty();
        } else {
            this.packedItems = entity.getEntityData().packDirty();
        }
    }

    private FriendlyByteBuf write(FriendlyByteBuf buffer) {
        try {
            buffer.writeVarInt(this.id);
            SynchedEntityData.pack(this.packedItems, buffer);
            return buffer;
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    @Override
    protected Packet<?> create() {
        try {
            ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket();
            packet.read(this.write(new FriendlyByteBuf(Unpooled.buffer())));
            return packet;
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
