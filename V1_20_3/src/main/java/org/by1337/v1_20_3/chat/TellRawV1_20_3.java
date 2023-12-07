package org.by1337.v1_20_3.chat;

import com.google.gson.JsonParseException;
import net.minecraft.network.chat.Component;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.by1337.api.chat.TellRaw;

public class TellRawV1_20_3 implements TellRaw {
    public void tell(String raw, Player player) throws JsonParseException {
        Component component = Component.Serializer.fromJson(raw);
        ((CraftPlayer) player).getHandle().sendSystemMessage(component);
    }
}