package org.by1337.blib.util;

import org.by1337.blib.test.util.SetServer;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VersionTest {
    @Test
    public void testGetVersion() throws Version.UnsupportedVersionException {
        SetServer.setServer();
        assertEquals(Version.getVersion("super server (MC: 1.19)", "1.19-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_19_R1"), Version.V1_19);
        assertEquals(Version.getVersion("super server (MC: 1.19.2)", "1.19.2-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_19_R1"), Version.V1_19_2);
        assertEquals(Version.getVersion("super server (MC: 1.16.5)", "1.16.5-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_16_R3"), Version.V1_16_5);
        assertEquals(Version.getVersion("super server (MC: 1.17)", "1.17-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_17_R3"), Version.V1_17);
        assertEquals(Version.getVersion("super server (MC: 1.18)", "1.18-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_18_R0"), Version.V1_18);
        assertEquals(Version.getVersion("super server (MC: ?.??)", "1.18-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_???"), Version.V1_18);
        assertEquals(Version.getVersion("super server (MC: 1.18)", "?.??-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_18_R0"), Version.V1_18);
        assertEquals(Version.getVersion("super server (MC: ?.??)", "?.??-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_18_R0"), Version.V1_18);
        assertEquals(Version.getVersion("super server (MC: 1.20)", "1.20-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_20_R1"), Version.V1_20);
        assertEquals(Version.getVersion("super server (MC: 1.20.5)", "1.20-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_20_R1"), Version.V1_20_5);
        assertEquals(Version.getVersion("super server (MC: 1.21)", "1.21-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_21_R1"), Version.V1_21);
    }

    @Test(expected = Version.UnsupportedVersionException.class)
    public void testUnsupportedVersion() throws Version.UnsupportedVersionException {
        SetServer.setServer();
        Version.getVersion("unknown server (MC: 1.21)", "1.21-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_21_R1");
        Version.getVersion("unknown server (MC: 1.16.4)", "1.16-R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v1_16.4");
        Version.getVersion("unknown server (MC: ?.??)", "?.??R0.1-SNAPSHOT", "org.bukkit.craftbukkit.v????");
    }
}
