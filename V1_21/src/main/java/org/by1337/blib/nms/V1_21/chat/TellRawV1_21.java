package org.by1337.blib.nms.V1_21.chat;

import com.google.gson.JsonParseException;
import net.minecraft.network.chat.Component;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.by1337.blib.chat.TellRaw;

public class TellRawV1_21 implements TellRaw {
    public void tell(String raw, Player player) throws JsonParseException {
        Component component = CraftChatMessage.fromJSON(raw);
        ((CraftPlayer) player).getHandle().sendSystemMessage(component);
    }
}