package org.by1337.blib.test.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The org.by1337.blib.test.util.SetServer class is responsible for initializing a mock Bukkit Server
 * for testing purposes. It ensures that only one instance of the mock server
 * is created.
 */
public class SetServer {

    private static SetServer instance = null;

    /**
     * Initializes the mock Bukkit Server if it has not been initialized before.
     * This method should be called to ensure that the server is set up for testing.
     */
    public static void setServer() {
        if (instance == null)
            instance = new SetServer();
    }

    private SetServer() {
        Server mockedServer = mock(Server.class);

        when(mockedServer.getLogger()).thenReturn(java.util.logging.Logger.getLogger("Minecraft"));
        when(mockedServer.getName()).thenReturn("Mock Server");
        when(mockedServer.getVersion()).thenReturn("1.16.5" + " (MC: 1.16.5)");
        when(mockedServer.getBukkitVersion()).thenReturn("1.16.5");
        when(mockedServer.isPrimaryThread()).thenReturn(true);

        Bukkit.setServer(mockedServer);
    }
}
