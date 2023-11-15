package org.by1337.v1_18_1.chat;

import com.google.gson.JsonParseException;
import net.minecraft.network.chat.Component;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.by1337.api.chat.TellRaw;

public class TellRawV1_18_1 implements TellRaw {
    public void tell(String raw, Player player) throws JsonParseException {
        Component component = Component.Serializer.fromJson(raw);
        ((CraftPlayer) player).getHandle().sendMessage(component, player.getUniqueId());
    }

}