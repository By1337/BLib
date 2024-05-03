package org.by1337.blib.nms.V1_20_6.chat;

import com.google.gson.JsonParseException;
import net.minecraft.network.chat.Component;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.by1337.blib.chat.TellRaw;

public class TellRawV1_20_6 implements TellRaw {
    public void tell(String raw, Player player) throws JsonParseException {
        Component component = CraftChatMessage.fromJSON(raw);
        ((CraftPlayer) player).getHandle().sendSystemMessage(component);
    }
}