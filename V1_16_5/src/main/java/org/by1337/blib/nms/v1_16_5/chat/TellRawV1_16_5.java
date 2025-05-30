package org.by1337.blib.nms.v1_16_5.chat;

import com.google.gson.JsonParseException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.by1337.blib.RegistryHelper;
import org.by1337.blib.chat.TellRaw;

public class TellRawV1_16_5 implements TellRaw {
    public void tell(String raw, Player player) throws JsonParseException {
        Component iChatBaseComponent = Serializer.fromJson(raw);
        ((CraftPlayer)player).getHandle().sendMessage(iChatBaseComponent, player.getUniqueId());
    }
}
