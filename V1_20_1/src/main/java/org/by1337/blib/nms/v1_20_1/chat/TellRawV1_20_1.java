package org.by1337.blib.nms.v1_20_1.chat;

import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.by1337.blib.chat.TellRaw;

public class TellRawV1_20_1 implements TellRaw {
    public void tell(String raw, Player player) throws JsonParseException {
        Component component = Component.Serializer.fromJson(raw);
        ((CraftPlayer) player).getHandle().sendSystemMessage(component);
    }
    public void test(){

    }
}