package org.by1337.api.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.bukkit.Bukkit;
import org.by1337.api.lang.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    UNKNOWN("unknown"),
    V1_16_5("1.16.5"),
    V1_17("1.17"),
    V1_17_1("1.17.1"),
    V1_18("1.18"),
    V1_18_1("1.18.1"),
    V1_18_2("1.18.2"),
    V1_19("1.19"),
    V1_19_1("1.19.1"),
    V1_19_2("1.19.2"),
    V1_19_3("1.19.3"),
    V1_19_4("1.19.4"),
    V1_20_1("1.20.1"),
    V1_20_2("1.20.2"),
    V1_20_3("1.20.3");

    @NotNull
    private final String ver;

    @Nullable
    private static GameVersion gameVersion;

    /**
     * The current server version.
     */
    public static final Version VERSION;

    Version(@NotNull String version) {
        this.ver = version;
    }

    static {
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

    private static Version getVersion(GameVersion gameVersion) throws UnsupportedVersionException {
        for (Version version : Version.values()) {
            if (version.getVer().equals(gameVersion.getName())) {
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
        if (ver.equals("16.5")) return V1_16_5;
        if (ver.equals("17.0")) return V1_17;
        if (ver.equals("17.1")) return V1_17_1;
        if (ver.equals("18.0")) return V1_18;
        if (ver.equals("18.1")) return V1_18_1;
        if (ver.equals("18.2")) return V1_18_2;
        if (ver.equals("19.0")) return V1_19;
        if (ver.equals("19.1")) return V1_19_1;
        if (ver.equals("19.2")) return V1_19_2;
        if (ver.equals("19.3")) return V1_19_3;
        if (ver.equals("19.4")) return V1_19_4;
        if (ver.equals("20.1")) return V1_20_1;
        if (ver.equals("20.2")) return V1_20_2;
        if (ver.equals("20.3")) return V1_20_3;
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

    /**
     * An exception class to represent unsupported server versions.
     */
    public static class UnsupportedVersionException extends Exception {
        public UnsupportedVersionException(String message) {
            super(message);
        }

        public UnsupportedVersionException(String message, Object... objects) {
            super(String.format(message, objects));
        }
    }
}
