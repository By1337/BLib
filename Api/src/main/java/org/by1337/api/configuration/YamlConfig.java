package org.by1337.api.configuration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.by1337.api.BLib;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * A class for managing YAML configuration files using YamlContext.
 */
public class YamlConfig {
    private final File file;
    private YamlContext context;

    /**
     * Constructs a YamlConfig object for the specified file, loading its contents into a YamlContext.
     *
     * @param file The file to be managed as a YAML configuration.
     * @throws IOException                 If there's an issue reading or loading the file.
     * @throws InvalidConfigurationException If the file's contents are not a valid YAML configuration.
     */
    public YamlConfig(@NotNull File file) throws IOException, InvalidConfigurationException {
        this.file = file;
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(file);
        context = new YamlContext(yamlConfiguration);
    }

    /**
     * Saves the YAML configuration to the specified file.
     *
     * @param file The file to save the YAML configuration to.
     * @throws IOException If there's an issue writing to the file.
     */
    public void save(@NotNull File file) throws IOException {
        ((YamlConfiguration) context.getHandle()).save(file);
    }

    /**
     * Saves the YAML configuration to the specified file.
     *
     * @throws IOException If there's an issue writing to the file.
     */
    public void save() throws IOException {
        ((YamlConfiguration) context.getHandle()).save(file);
    }

    /**
     * Tries to save the YAML configuration to the original file, handling any potential IO exceptions.
     */
    public void trySave() {
        try {
            save(file);
        } catch (IOException e) {
            BLib.getApi().getMessage().error(e);
        }
    }

    /**
     * Get the file associated with this YAML configuration.
     *
     * @return The file associated with this configuration.
     */
    @NotNull
    public File getFile() {
        return file;
    }

    /**
     * Get the YamlContext containing the YAML configuration.
     *
     * @return The YamlContext containing the YAML configuration data.
     */
    @NotNull
    public YamlContext getContext() {
        return context;
    }

    /**
     * Set the YamlContext to use a different YamlConfiguration.
     *
     * @param configuration The new YamlConfiguration to be used in the YamlContext.
     */
    public void setYamlConfiguration(@NotNull YamlConfiguration configuration) {
        this.context = new YamlContext(configuration);
    }
}
