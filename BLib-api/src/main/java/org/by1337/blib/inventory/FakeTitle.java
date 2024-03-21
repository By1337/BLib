package org.by1337.blib.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface FakeTitle {
    @Deprecated(since = "1.0.7")
    void send(Inventory inventory, String newTitle);
    void send(Inventory inventory, Component newTitle);
}
