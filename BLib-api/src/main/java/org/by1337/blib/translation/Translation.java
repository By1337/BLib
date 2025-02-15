package org.by1337.blib.translation;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.OfflinePlayer;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.text.MessageFormatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

public class Translation {
    @Nullable
    private final Locale useLocale;
    private final Locale defaultLocale;
    private final Map<Locale, Map<String, String>> messages;
    private final Map<String, Map<String, String>> byLanguage;
    private final Message message;

    public Translation(@Nullable Locale useLocale, Locale defaultLocale, Map<Locale, Map<String, String>> messages, Message message) {
        this.useLocale = useLocale;
        this.defaultLocale = defaultLocale;
        this.messages = messages;
        this.message = message;
        byLanguage = new HashMap<>();
        messages.forEach((k, v) -> {
            byLanguage.put(k.getLanguage(), v);
        });
    }

    public void saveDefaults(Reader reader) {
        Gson gson = new Gson();
        saveDefaults(gson.fromJson(reader, JsonObject.class));
    }

    public void saveDefaults(JsonObject jsonObject) {
        JsonObject object = jsonObject.getAsJsonObject("messages");
        for (Map.Entry<String, JsonElement> key : object.entrySet()) {
            JsonObject lang = key.getValue().getAsJsonObject();
            for (Map.Entry<String, JsonElement> locale : lang.entrySet()) {
                String[] arr = locale.getKey().split("_");
                String text;
                var textRaw = locale.getValue();
                if (textRaw.isJsonPrimitive()) {
                    text = textRaw.getAsString();
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (JsonElement jsonElement : textRaw.getAsJsonArray()) {
                        sb.append(jsonElement.getAsString()).append("\n");
                    }
                    if (!sb.isEmpty()) {
                        sb.setLength(sb.length() - 1);
                    }
                    text = sb.toString();
                }
                putTranslationIfNotExist(
                        new Locale(arr[0], arr[1]),
                        key.getKey(),
                        text
                );
            }
        }
    }

    public void putTranslationIfNotExist(@NotNull Locale locale, @NotNull String key, @NotNull String message) {
        if (!messages.getOrDefault(locale, Collections.emptyMap()).containsKey(key)) {
            putTranslation(locale, key, message);
        }
    }

    public void putTranslation(@NotNull Locale locale, @NotNull String key, @NotNull String message) {
        messages.computeIfAbsent(locale, k -> new HashMap<>()).put(key, message);
        byLanguage.put(locale.getLanguage(), messages.get(locale));
    }

    public static Translation fromJson(Reader reader, Message message) {
        return createGson(message).fromJson(reader, Translation.class);
    }

    public static Gson createGson(Message message) {
        return new GsonBuilder()
                .registerTypeAdapter(Translation.class, new TranslationAdapter(message))
                .create();
    }

    @Nullable
    public String translate(String s, @Nullable Locale locale) {
        return translateByLocale(s, useLocale == null ? locale : useLocale);
    }

    @Nullable
    public String translate(String s) {
        return translateByLocale(s, useLocale);
    }

    @Nullable
    public String translateByLocale(String s, @Nullable Locale locale) {
        Map<String, String> map = null;
        if (locale != null) {
            map = messages.get(locale);
            if (map == null) {
                map = byLanguage.get(locale.getLanguage());
            }
        }
        if (map == null) {
            map = messages.get(defaultLocale);
        }
        return map == null ? s : map.get(s);
    }

    @NotNull
    public Component translate(@NotNull Component component) {
        return translate(component, null, null);
    }

    @NotNull
    public Component translate(@NotNull Component component, @Nullable Locale locale, @Nullable OfflinePlayer player) {
        Component component0;
        if (component instanceof TranslatableComponent translatable) {
            String key = translatable.key();
            String translation = translate(key, locale);
            if (translation != null) {
                Object[] args = translatable.args().stream().map(message::getContent).toArray();
                if (args.length != 0) {
                    component0 = message.componentBuilderNoTranslate(MessageFormatter.apply(translation, args), player);
                } else {
                    component0 = message.componentBuilderNoTranslate(translation, player);
                }
            } else {
                component0 = component;
            }
        } else {
            component0 = component;
        }
        List<Component> children = new ArrayList<>(component0.children());
        children.replaceAll(c -> translate(c, locale, player));
        return component0.children(children);
    }

    static class TranslationAdapter implements JsonDeserializer<Translation> {

        private final Message message;

        public TranslationAdapter(Message message) {
            this.message = message;
        }

        @Override
        public Translation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String rawUseLocale = jsonObject.getAsJsonPrimitive("use-locale").getAsString();
            String rawDefaultLocale = jsonObject.getAsJsonPrimitive("default-locale").getAsString();

            JsonObject messagesObject = jsonObject.getAsJsonObject("messages");
            Map<String, Map<String, String>> rawMessages = new HashMap<>();

            for (Map.Entry<String, JsonElement> entry : messagesObject.entrySet()) {
                String key = entry.getKey();
                JsonObject innerObject = entry.getValue().getAsJsonObject();
                Map<String, String> innerMap = new HashMap<>();

                for (Map.Entry<String, JsonElement> innerEntry : innerObject.entrySet()) {
                    String text;
                    var textRaw = innerEntry.getValue();
                    if (textRaw.isJsonPrimitive()) {
                        text = textRaw.getAsString();
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (JsonElement element : textRaw.getAsJsonArray()) {
                            sb.append(element.getAsString()).append("\n");
                        }
                        if (!sb.isEmpty()) {
                            sb.setLength(sb.length() - 1);
                        }
                        text = sb.toString();
                    }

                    innerMap.put(innerEntry.getKey(), text);
                }

                rawMessages.put(key, innerMap);
            }



            Locale useLocale;
            if (!rawUseLocale.equals("auto-detect")) {
                String[] arr = rawUseLocale.split("_");
                useLocale = new Locale(arr[0], arr[1]);
            } else {
                useLocale = null;
            }
            String[] arr = rawDefaultLocale.split("_");
            Locale defaultLocale = new Locale(arr[0], arr[1]);

            Map<Locale, Map<String, String>> messages = new HashMap<>();

            rawMessages.forEach((k, v) -> {
                v.forEach((locale, msg) -> {
                    String[] arr0 = locale.split("_");
                    var l = new Locale(arr0[0], arr0[1]);
                    var map = messages.getOrDefault(l, new HashMap<>());
                    map.put(k, msg);
                    messages.put(l, map);
                });
            });

            return new Translation(useLocale, defaultLocale, messages, message);
        }
    }

    public Locale getUseLocale() {
        return useLocale;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public Map<Locale, Map<String, String>> getMessages() {
        return messages;
    }

    public Map<String, Map<String, String>> getByLanguage() {
        return byLanguage;
    }

    public Message getMessage() {
        return message;
    }
}
