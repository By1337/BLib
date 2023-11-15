package org.by1337.v1_19_4.chat;

import com.google.gson.JsonParseException;
import net.minecraft.network.chat.Component;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.by1337.api.chat.TellRaw;

public class TellRawV1_19_4 implements TellRaw {
    public void tell(String raw, Player player) throws JsonParseException {
        Component component = Component.Serializer.fromJson(raw);
        ((CraftPlayer) player).getHandle().sendSystemMessage(component);
    }
}