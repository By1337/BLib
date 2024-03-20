/**
 * Represents a click event with a type and value.
 */
package org.by1337.blib.chat;

import com.google.gson.internal.LinkedTreeMap;

@Deprecated(forRemoval = true)
public class ClickEvent {
    private ClickEventType type;
    private String value;

    /**
     * Constructs a ClickEvent with the specified type and value.
     *
     * @param type  The type of the click event.
     * @param value The value associated with the click event.
     */
    public ClickEvent(ClickEventType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
       return "\"clickEvent\":{" + "\"action\":\"" + type.name().toLowerCase() + "\"," + "\"value\":\"" + value + "\"}";
    }

    public ClickEvent setType(ClickEventType type) {
        this.type = type;
        return this;
    }

    public ClickEvent setValue(String value) {
        this.value = value;
        return this;
    }
    public String toSource(){
        return String.format(".clickEvent(new ClickEvent(ClickEventType.%s, \"%s\"))", type.name().toUpperCase(), value);
    }
    public static ClickEvent parse(LinkedTreeMap<?, ?> linkedTreeMap){
        return new ClickEvent(
                ClickEventType.valueOf(((String) linkedTreeMap.get("action")).toUpperCase()),
                (String) linkedTreeMap.get("value")
        );
    }
}
