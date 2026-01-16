package org.by1337.blib.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.by1337.blib.BLib;

/// @deprecated use {@link InventoryUtil}
@Deprecated(since = "1.4.1")
public interface FakeTitle {
    FakeTitle INSTANCE = InventoryUtil.INSTANCE::sendFakeTitle;

    @Deprecated(since = "1.0.7")
    default void send(Inventory inventory, String newTitle) {
        send(inventory, BLib.getApi().getMessage().componentBuilder(newTitle));
    }

    /// @deprecated use {@link InventoryUtil#sendFakeTitle(Inventory, Component)}
    void send(Inventory inventory, Component newTitle);
}
