package org.by1337.blib.lang;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.bstats.json.JsonObjectBuilder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.PropertyKey;
import org.jetbrains.annotations.TestOnly;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.*;

public class Lang {

    private static final Map<String, String> emptyMap = new HashMap<>();
    private static Map<String, Map<String, String>> messages = new HashMap<>();


    public static void loadTranslations(Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "messages.json");
        if (!file.exists()) {
            plugin.saveResource("messages.json", true);
        }
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, Map<String, String>>>() {}.getType();
            messages = gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getMessage(String key) {
        return messages.getOrDefault(key, emptyMap).getOrDefault("message", "missing message: " + emptyMap);
    }

    @TestOnly
    @Deprecated(forRemoval = true)
    public static void loadIfNull() {

    }
}
