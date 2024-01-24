package org.by1337.blib.core.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.by1337.blib.core.BLib;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Config {
    private File file;
    private FileConfiguration fileConfiguration;

    public String lang;
    public void load(){
        file = new File(BLib.getInstance().getDataFolder() + "/config.yml");
        if (!file.exists()){
            BLib.getInstance().saveResource("config.yml", true);
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);

        lang = fileConfiguration.getString("lang");
    }

    public void save(){
        fileConfiguration.set("lang", lang);

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            BLib.getInstance().getLogger().log(Level.SEVERE, e.getLocalizedMessage());
        }
    }
}
