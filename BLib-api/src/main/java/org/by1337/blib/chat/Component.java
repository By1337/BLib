package org.by1337.blib.chat;


import com.google.gson.internal.LinkedTreeMap;
import org.by1337.blib.chat.hover.HoverEvent;
import org.by1337.blib.chat.hover.HoverEventContentsString;

import java.awt.*;

@Deprecated(since = "1.0.7", forRemoval = true)
public class Component {
    private boolean bold = false;
    private boolean italic = false;
    private boolean strikethrough = false;
    private boolean underlined = false;
    private boolean obfuscated = false;
    private ChatColor color = new ChatColor(Color.WHITE);
    private String text = "";
    private HoverEvent hoverEvent = null;
    private ClickEvent clickEvent = null;

    public Component() {
    }

    public Component(String text) {
        this.text = text;
    }

    public Component(boolean bold, boolean italic, boolean strikethrough, boolean underlined, boolean obfuscated, ChatColor color, String text, HoverEvent hoverEvent, ClickEvent clickEvent) {
        this.bold = bold;
        this.italic = italic;
        this.strikethrough = strikethrough;
        this.underlined = underlined;
        this.obfuscated = obfuscated;
        this.color = color;
        this.text = text;
        this.hoverEvent = hoverEvent;
        this.clickEvent = clickEvent;
    }

    /**
     * Converts the Component object to its JSON string representation.
     *
     * @return A JSON string representing the Component.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("{")
                .append(String.format("\"text\":\"%s\"", text));

        if (bold)
            sb.append(",").append("\"bold\":true");
        if (italic)
            sb.append(",").append("\"italic\":true");
        if (strikethrough)
            sb.append(",").append("\"strikethrough\":true");
        if (underlined)
            sb.append(",").append("\"underlined\":true");
        if (obfuscated)
            sb.append(",").append("\"obfuscated\":true");
        sb.append(String.format(",\"color\":\"%s\"", color.toHex()));
        if (hoverEvent != null)
            sb.append(",").append(hoverEvent);
        if (clickEvent != null)
            sb.append(",").append(clickEvent);
        sb.append("}");

        return sb.toString();
    }

    /**
     * Parses a LinkedTreeMap object into a Component.
     *
     * @param linkedTreeMap The LinkedTreeMap to parse into a Component.
     * @return A Component object parsed from the LinkedTreeMap.
     */
    public static Component parse(LinkedTreeMap<Object, Object> linkedTreeMap) {
        boolean bold;
        boolean italic;
        boolean strikethrough;
        boolean underlined;
        boolean obfuscated;
        ChatColor color;
        String text;
        HoverEvent hoverEvent = null;
        ClickEvent clickEvent = null;

        text = (String) linkedTreeMap.get("text");

        bold = (boolean) linkedTreeMap.getOrDefault("bold", false);
        italic = (boolean) linkedTreeMap.getOrDefault("italic", false);
        strikethrough = (boolean) linkedTreeMap.getOrDefault("strikethrough", false);
        underlined = (boolean) linkedTreeMap.getOrDefault("underlined", false);
        obfuscated = (boolean) linkedTreeMap.getOrDefault("obfuscated", false);
        color = ChatColor.fromHex((String) linkedTreeMap.getOrDefault("color", new ChatColor(Color.WHITE).toHex()));
        if (linkedTreeMap.containsKey("hoverEvent")) {
            hoverEvent = new HoverEvent(HoverEventContentsString.parse((LinkedTreeMap<Object, Object>) linkedTreeMap.get("hoverEvent")));
        }
        if (linkedTreeMap.containsKey("clickEvent")) {
            clickEvent = ClickEvent.parse((LinkedTreeMap<Object, Object>) linkedTreeMap.get("clickEvent"));
        }
        return new Component(bold, italic, strikethrough, underlined, obfuscated, color, text, hoverEvent, clickEvent);
    }

    /**
     * Converts the Component to a source code representation for building.
     *
     * @return A source code representation of the Component.
     */
    public String toSource() {
        return String.format(".addComponent(new Component(ChatColor.fromHex(\"%s\"), \"%s\")%s%s%s%s%s%s%s)",
                color.toHex(),
                text,
                bold ? ".bold(true)" : "",
                italic ? ".italic(true)" : "",
                strikethrough ? ".strikethrough(true)" : "",
                underlined ? ".underlined(true)" : "",
                obfuscated ? ".obfuscated(true)" : "",
                clickEvent == null ? "" : clickEvent.toSource(),
                hoverEvent == null ? "" : hoverEvent.toSource()
        );
    }

    public Component(ChatColor color, String text) {
        this.color = color;
        this.text = text;
    }

    public Component bold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public Component italic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public Component strikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    public Component underlined(boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    public Component obfuscated(boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    public Component color(ChatColor color) {
        this.color = color;
        return this;
    }

    public Component text(String text) {
        this.text = text;
        return this;
    }

    public Component hoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        return this;
    }

    public Component clickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }
}
