package org.by1337.blib.nms.v1_17.network.clientbound;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import org.by1337.blib.network.clientbound.entity.PacketSetEntityData;
import org.by1337.blib.world.entity.PacketEntity;
import org.by1337.blib.nms.v1_17.network.PacketImpl17;
import org.by1337.blib.nms.v1_17.world.entity.PacketEntityImpl17;

import java.util.List;

public class PacketSetEntityDataImpl17 extends PacketImpl17 implements PacketSetEntityData {
    private int id;
    private List<SynchedEntityData.DataItem<?>> packedItems;

    public PacketSetEntityDataImpl17(PacketEntity packetEntity) {
        this(packetEntity, true);
    }

    public PacketSetEntityDataImpl17(PacketEntity entity, boolean all) {
        init((PacketEntityImpl17) entity, all);
    }

    private void init(PacketEntityImpl17 entity, boolean all) {
        this.id = entity.getId();
        if (all) {
            this.packedItems = entity.getEntityData().getAll();
            entity.getEntityData().clearDirty();
        } else {
            this.packedItems = entity.getEntityData().packDirty();
        }
    }

    private FriendlyByteBuf write(FriendlyByteBuf var0) {
        var0.writeVarInt(this.id);
        SynchedEntityData.pack(this.packedItems, var0);
        return var0;
    }

    @Override
    protected Packet<?> create() {
        return new ClientboundSetEntityDataPacket(write(new FriendlyByteBuf(Unpooled.buffer())));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
