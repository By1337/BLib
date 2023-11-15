package org.by1337.api.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface FakeTitle {
    void send(Inventory inventory, String newTitle);
}
