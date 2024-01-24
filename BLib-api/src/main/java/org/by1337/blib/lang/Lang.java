package org.by1337.blib.lang;


import lombok.Getter;
import org.jetbrains.annotations.PropertyKey;
import org.jetbrains.annotations.TestOnly;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Lang {
    public static final List<String> LANGUAGES = List.of(
            "en_US",
            "ru_RU"
    );
    @Getter
    @PropertyKey(resourceBundle = "messages")
    private static ResourceBundle messages;
    public static void loadTranslations(Locale locale) {
        messages = ResourceBundle.getBundle("messages", locale);
    }

    public static void loadTranslations(String locale) { // format <language>_<country>
        String[] lang = locale.split("_");
        messages = ResourceBundle.getBundle("messages", new Locale(lang[0], lang[1]));
    }

    public static String getMessage(String key) {
        return messages.getString(key);
    }
    @TestOnly
    public static void loadIfNull(){
        if (messages == null){
            loadTranslations(new Locale("en", "US"));
        }
    }
}
