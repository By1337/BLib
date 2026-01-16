package org.by1337.blib.text;

import io.papermc.lib.PaperLib;
import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import org.by1337.blib.BLib;

public interface ComponentToANSI {
    ComponentToANSI INSTANCE = new ComponentToANSI() {
        //todo?
        @Override
        public String convert(Component component) {
            return PaperComponents.plainSerializer().serialize(component);
        }

        @Override
        public String buildAndConvert(String legacy) {
            return legacy;
        }
    };
    static ComponentToANSI get() {
        return INSTANCE;
    }
    String convert(Component component);

    String buildAndConvert(String legacy);

}
