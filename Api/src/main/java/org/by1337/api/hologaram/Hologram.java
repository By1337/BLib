package org.by1337.api.hologaram;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.by1337.api.world.BLocation;
import org.by1337.api.util.CyclicList;
import org.by1337.api.chat.Placeholderable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Hologram is a class that creates and manages holographic displays in the world.
 * It allows for the presentation of dynamic text lines, updating them for players within a specified radius.
 */
public class Hologram {
    private final Setting setting;
    private final World world;
    private final Location bukkitLocation;
    private BukkitTask task;
    private final CyclicList<List<String>> cyclicList;
    private final Placeholderable placeholderable;
    private final Plugin plugin;

    /**
     * Constructs a new Hologram instance.
     *
     * @param setting         The configuration settings for the hologram.
     * @param location        The location of the hologram.
     * @param placeholderable The placeholder provider for replacing placeholders in text lines.
     * @param plugin          The plugin associated with this hologram.
     */
    public Hologram(@NotNull Setting setting, @NotNull BLocation location, @NotNull Placeholderable placeholderable, @NotNull Plugin plugin) {
        this.setting = setting;
        this.plugin = plugin;
        bukkitLocation = location.getLocation().add(setting.offsets);
        world = location.getLocation().getWorld();
        cyclicList = setting.lines;
        this.placeholderable = placeholderable;
    }

    /**
     * Constructs a new Hologram instance using a Location.
     *
     * @param setting         The configuration settings for the hologram.
     * @param location        The location of the hologram.
     * @param placeholderable The placeholder provider for replacing placeholders in text lines.
     * @param plugin          The plugin associated with this hologram.
     */
    public Hologram(@NotNull Setting setting, @NotNull Location location, @NotNull Placeholderable placeholderable, @NotNull Plugin plugin) {
        this.setting = setting;
        this.plugin = plugin;
        bukkitLocation = location.add(setting.offsets);
        world = location.getWorld();
        cyclicList = setting.lines;
        this.placeholderable = placeholderable;
    }

    /**
     * Starts the hologram, periodically updating the text lines.
     */
    public void start() {
        if (task != null) {
            throw new IllegalStateException("The hologram is already running!");
        }
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::run, 0, setting.timeUpdate);
    }

    /**
     * Stops the hologram and removes it from view.
     */
    public void stop() {
        if (task == null) {
            throw new IllegalStateException("The hologram has not yet running!");
        }
        task.cancel();
        task = null;
        lines.forEach(line -> viewers.forEach(line::removeFor));
        lines.clear();
        viewers.clear();
    }

    private List<Player> viewers = new ArrayList<>();
    private final LinkedList<HologramLine> lines = new LinkedList<>();

    /**
     * The `run` method is responsible for updating the hologram's display. It performs the following steps:
     * <p>
     * 1. Identifies the players who are within a specified radius of the hologram's location.
     * 2. Retrieves the next set of text lines to display from the cyclic list.
     * 3. Iterates over the text lines and corresponding hologram lines.
     * - If a hologram line already exists, it checks if the text has changed. If it has changed,
     * it updates the hologram line's text and either spawns or updates it for the players.
     * - If no hologram line exists for a text line, a new hologram line is created and spawned for the players.
     * 4. Iterates through the existing hologram lines and removes those that are no longer needed (text lines removed).
     * 5. Removes hologram lines for players who are no longer within the radius.
     * 6. Updates the list of current viewers to reflect the players within the radius.
     */
    private void run() {
        List<Player> actualViewers = new ArrayList<>();
        int sq = setting.radius * setting.radius;
        for (Player player : world.getPlayers()) {
            if (player.getLocation().distanceSquared(bukkitLocation) <= sq) {
                actualViewers.add(player);
            }
        }

        List<String> textLines = cyclicList.getNext();
        Iterator<HologramLine> iterator = new ArrayList<>(lines).iterator();

        for (int i = 0; i < textLines.size(); i++) {
            String textLine = placeholderable.replace(textLines.get(i));
            if (iterator.hasNext()) {
                HologramLine line = iterator.next();
                if (!line.getText().equals(textLine)) {
                    line.setText(textLine);
                    actualViewers.forEach(player -> {
                        if (!viewers.contains(player)) {
                            line.spawnFor(player);
                        } else {
                            line.updateMetaFor(player);
                        }
                    });
                } else {
                    actualViewers.forEach(player -> {
                        if (!viewers.contains(player)) {
                            line.spawnFor(player);
                        }
                    });
                }

            } else {
                HologramLine line = new HologramLine(new BLocation(bukkitLocation.clone().add(0, -(setting.lineSpacing * i), 0)), textLine);
                actualViewers.forEach(line::spawnFor);
                lines.add(line);
            }
        }
        while (iterator.hasNext()) {
            HologramLine line = iterator.next();
            actualViewers.forEach(line::removeFor);
            lines.remove(line);
        }
        viewers.stream()
                .filter(player -> !player.isOnline() || !world.equals(player.getLocation().getWorld()) || !actualViewers.contains(player))
                .forEach(player -> lines.forEach(holo -> holo.removeFor(player)));

        viewers = actualViewers;
    }

    /**
     * Represents the configuration settings for a hologram.
     */
    public static class Setting {
        private int timeUpdate = 20;
        private double lineSpacing = 0.3;
        private CyclicList<List<String>> lines;
        private int radius;
        private Vector offsets;

        /**
         * Constructs a new Setting instance.
         *
         * @param timeUpdate  The interval for updating the hologram text lines.
         * @param lineSpacing The vertical spacing between lines.
         * @param lines       The cyclic list of text lines to display.
         * @param radius      The viewing radius for players.
         * @param offsets     The vector for adjusting the hologram's position.
         */
        public Setting(int timeUpdate, double lineSpacing, @NotNull CyclicList<List<String>> lines, int radius, @NotNull Vector offsets) {
            this.timeUpdate = timeUpdate;
            this.lineSpacing = lineSpacing;
            this.lines = lines;
            this.radius = radius;
            this.offsets = offsets;
        }

        /**
         * Saves the settings to a YamlConfiguration.
         *
         * @return A YamlConfiguration containing the settings.
         */
        @NotNull
        public YamlConfiguration save() {
            YamlConfiguration configuration = new YamlConfiguration();

            configuration.set("time-update", timeUpdate);
            configuration.set("line-spacing", lineSpacing);
            configuration.set("radius", radius);

            configuration.set("offsets.x", offsets.getX());
            configuration.set("offsets.y", offsets.getY());
            configuration.set("offsets.z", offsets.getZ());

            int x = 0;
            for (List<String> lines : lines.getAll()) {
                configuration.set(String.format("lines.%s", "line-" + x), lines);
                x++;
            }
            return configuration;
        }

        /**
         * Loads the settings from a YamlConfiguration.
         *
         * @param section The configuration section to load settings from.
         * @return A Setting instance with loaded settings.
         */
        @NotNull
        public static Setting load(@NotNull ConfigurationSection section) {
            int timeUpdate = section.getInt("time-update");
            double lineSpacing = ((Number) section.get("line-spacing")).doubleValue();
            int radius = section.getInt("radius");

            Vector offsets = new Vector(
                    ((Number) section.get("offsets.x")).doubleValue(),
                    ((Number) section.get("offsets.y")).doubleValue(),
                    ((Number) section.get("offsets.z")).doubleValue()
            );

            CyclicList<List<String>> lines = new CyclicList<>();

            for (String key : section.getConfigurationSection("lines").getKeys(false)) {
                lines.add(section.getStringList(String.format("lines.%s", key)));
            }

            return new Setting(timeUpdate, lineSpacing, lines, radius, offsets);
        }
    }
}
