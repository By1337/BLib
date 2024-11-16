package org.by1337.blib.util;

import blib.com.mojang.serialization.Codec;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.bukkit.Bukkit;
import org.by1337.blib.configuration.serialization.DefaultCodecs;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.text.MessageFormatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An enumeration representing different server versions.
 */
public enum Version {
    UNKNOWN("unknown", 0, 0),
    V1_14("1.14", 477, 1952),
    V1_14_1("1.14.1", 480, 1957),
    V1_14_2("1.14.2", 485, 1963),
    V1_14_3("1.14.3", 490, 1968),
    V1_14_4("1.14.4", 498, 1976),
    V1_15("1.15", 573, 2225),
    V1_15_1("1.15.1", 575, 2227),
    V1_15_2("1.15.2", 578, 2230),
    V1_16("1.16", 735, 2566),
    V1_16_1("1.16.1", 736, 2567),
    V1_16_2("1.16.2", 751, 2578),
    V1_16_3("1.16.3", 753, 2580),
    V1_16_4("1.16.4", 754, 2584),
    V1_16_5("1.16.5", 754, 2586),
    V1_17("1.17", 755, 2724),
    V1_17_1("1.17.1", 756, 2730),
    V1_18("1.18", 757, 2860),
    V1_18_1("1.18.1", 757, 2865),
    V1_18_2("1.18.2", 758, 2975),
    V1_19("1.19", 759, 3105),
    V1_19_1("1.19.1", 760, 3117),
    V1_19_2("1.19.2", 760, 3120),
    V1_19_3("1.19.3", 761, 3218),
    V1_19_4("1.19.4", 762, 3337),
    V1_20("1.20", 763, 3463),
    V1_20_1("1.20.1", 763, 3465),
    V1_20_2("1.20.2", 764, 3578),
    V1_20_3("1.20.3", 765, 3698),
    V1_20_4("1.20.4", 765, 3700),
    V1_20_5("1.20.5", 766, 3837),
    V1_20_6("1.20.6", 766, 3839),
    V1_21("1.21", 767, 3953),
    V1_21_1("1.21.1", 767, 3955),
    V1_21_2("1.21.2", 768, 4080),
    V1_21_3("1.21.3", 768, 4082);
    public static final Codec<Version> CODEC = DefaultCodecs.createEnumCodec(Version.class);
    private static final Logger LOGGER = LoggerFactory.getLogger("BLib#Version");

    @NotNull
    private final String ver;
    private final int protocolVersion;
    private final int worldVersion;

    @Nullable
    private static GameVersion gameVersion;

    /**
     * The current server version.
     */
    public static final Version VERSION;

    Version(@NotNull String version, int protocolVersion, int worldVersion) {
        this.ver = version;
        this.protocolVersion = protocolVersion;
        this.worldVersion = worldVersion;
    }

    static {
        String version = System.getProperty("blib.server.version");
        if (version != null) {
            LOGGER.warn("Server version {} is set via property blib.server.version", version);
            if (version.contains(".")){
                try {
                    VERSION = getVersion(version);
                } catch (UnsupportedVersionException e) {
                    throw new RuntimeException(e);
                }
            }else {
                VERSION = valueOf(version);
            }
        } else {
            Version detectedVer;
            try (InputStream stream = Bukkit.getServer().getClass().getResourceAsStream("/version.json")) {
                if (stream == null) {
                    Bukkit.getLogger().log(Level.WARNING, "[BLib] Missing version information!");
                    throw new FileNotFoundException();
                } else {
                    try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                        Gson gson = new Gson();
                        JsonReader jsonReader = new JsonReader(reader);
                        gameVersion = new BGameVersion(gson.getAdapter(JsonObject.class).read(jsonReader));
                    }
                    detectedVer = getVersion(gameVersion);
                }
            } catch (IOException | UnsupportedVersionException e) {
                try {
                    detectedVer = getVersion(Bukkit.getVersion(), Bukkit.getBukkitVersion(), Bukkit.getServer().getClass().getPackage().getName());
                } catch (UnsupportedVersionException ex) {
                    throw new RuntimeException(new UnsupportedVersionException("Cannot be detected server version! " + "Version info: Bukkit.getVersion()='" + Bukkit.getVersion() +
                            "', Bukkit.getBukkitVersion()='" + Bukkit.getBukkitVersion() +
                            "', Bukkit.getServer().getClass().getPackage().getName()='" + Bukkit.getServer().getClass().getPackage().getName() + "'" + ", gameVersion='" + gameVersion + "'"));
                }
            }
            VERSION = detectedVer;
        }
    }

    private static Version getVersion(GameVersion gameVersion) throws UnsupportedVersionException {
        return getVersion(gameVersion.getName());
    }
    private static Version getVersion(String gameVersion) throws UnsupportedVersionException {
        for (Version version : Version.values()) {
            if (version.getVer().equals(gameVersion)) {
                return version;
            }
        }
        throw new UnsupportedVersionException(Lang.getMessage("unsupported-version"), gameVersion.toString());
    }

    /**
     * Gets the {@link Version} based on the server, Bukkit, and server package versions.
     *
     * @param version       The server version.
     * @param bukkitVersion The Bukkit version.
     * @param serverPackage The server package version.
     * @return The corresponding {@link Version}.
     * @throws UnsupportedVersionException If the server version is not supported.
     */
    public static Version getVersion(String version, String bukkitVersion, String serverPackage) throws UnsupportedVersionException {
        String ver = detectServerVersion(version, bukkitVersion, serverPackage);
        for (Version value : values()) {
            if (ver.endsWith(".0") && value.ver.split("\\.").length == 2) {
                if (ver.equals(value.ver.substring(2) + ".0")) {
                    return value;
                }
            } else if (ver.equals(value.ver.substring(2))) {
                return value;
            }
        }
        throw new UnsupportedVersionException(Lang.getMessage("unsupported-version"), version);
    }

    /**
     * Detects the server version based on the input parameters.
     *
     * @param version       The server version.
     * @param bukkitVersion The Bukkit version.
     * @param serverPackage The server package version.
     * @return The detected server version as a string.
     */
    private static String detectServerVersion(String version, String bukkitVersion, String serverPackage) {
        int majorVersion;
        int minorVersion;
        try {
            Pattern versionPattern = Pattern.compile("\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?\\)");
            Matcher matcher = versionPattern.matcher(version);

            if (matcher.find()) {
                MatchResult matchResult = matcher.toMatchResult();
                majorVersion = Integer.parseInt(matchResult.group(2), 10);
                if (matchResult.groupCount() >= 3) {
                    minorVersion = Integer.parseInt(matchResult.group(3), 10);
                    return majorVersion + "." + minorVersion;
                }
            } else {
                throw new IllegalStateException();
            }

        } catch (Exception e) {
            try {
                String[] split = bukkitVersion.split("-")[0].split("\\.");
                majorVersion = Integer.parseInt(split[1]);
                if (split.length == 3) {
                    minorVersion = Integer.parseInt(split[2]);
                    return majorVersion + "." + minorVersion;
                }
            } catch (Exception e2) {
                try {
                    String[] split = serverPackage.split("\\.")[3].split("_");
                    majorVersion = Integer.parseInt(split[1]);
                    return majorVersion + ".0";
                } catch (Exception e3) {
                    return "UNKNOWN";
                }
            }
        }
        return majorVersion + ".0";
    }

    @Nullable
    public static GameVersion getGameVersion() {
        return gameVersion;
    }

    public @NotNull String getVer() {
        return this.ver;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public int getWorldVersion() {
        return worldVersion;
    }

    public boolean newerThan(Version version){
        return ordinal() > version.ordinal();
    }
    public boolean newerThanOrEqual(Version version){
        return ordinal() >= version.ordinal();
    }
    public boolean olderThan(Version version){
        return ordinal() < version.ordinal();
    }
    public boolean olderThanOrEqual(Version version){
        return ordinal() <= version.ordinal();
    }

    /**
     * An exception class to represent unsupported server versions.
     */
    public static class UnsupportedVersionException extends Exception {
        public UnsupportedVersionException(String message) {
            super(message);
        }

        public UnsupportedVersionException(String message, Object... objects) {
            super(MessageFormatter.apply(message, objects));
        }
    }
}
