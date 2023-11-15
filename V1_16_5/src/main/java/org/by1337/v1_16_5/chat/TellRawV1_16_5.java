package org.by1337.v1_16_5.chat;

import com.google.gson.JsonParseException;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.by1337.api.chat.TellRaw;

public class TellRawV1_16_5 implements TellRaw {
    public void tell(String raw, Player player) throws JsonParseException {
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a(raw);
        ((CraftPlayer) player).getHandle().sendMessage(iChatBaseComponent, player.getUniqueId());

        //net.minecraft.network.chat.Component  net.minecraft.network.chat.Component.Serializer.fromJson
    }

}
