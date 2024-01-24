package org.by1337.blib.chat;

import com.google.gson.JsonParseException;
import org.bukkit.entity.Player;

/**
 * An interface for sending raw JSON text to a player using the TellRaw command.
 */
public interface TellRaw {
    /**
     * Sends a raw JSON text message to the specified player.
     *
     * @param raw    The raw JSON text to send.
     * @param player The player to whom the message should be sent.
     * @throws JsonParseException If there's an error parsing the JSON text.
     */
    void tell(String raw, Player player) throws JsonParseException;
}
