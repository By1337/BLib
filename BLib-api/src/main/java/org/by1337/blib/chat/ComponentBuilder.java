/**
 * A utility class for building and manipulating components for a chat.
 */
package org.by1337.blib.chat;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
@Deprecated(since = "1.0.7", forRemoval = true)
public class ComponentBuilder {
    private final List<Component> list = new ArrayList<>();

    /**
     * Adds a component to the builder.
     *
     * @param component The component to add.
     * @return This ComponentBuilder instance for method chaining.
     */
    public ComponentBuilder component(Component component) {
        list.add(component);
        return this;
    }

    /**
     * Converts the components in the builder to their source code representation.
     *
     * @return A string containing the source code representation of the components.
     */
    public String toSource() {
        StringBuilder sb = new StringBuilder("new ComponentBuilder()\n");
        for (Component component : list) {
            sb.append(component.toSource().replaceAll("\n", "\\\\n")).append("\n");
        }
        sb.setLength(sb.length() - 1);
        sb.append(";");
        return sb.toString();
    }

    /**
     * Parses a JSON string into components and adds them to the builder.
     *
     * @param json The JSON string to parse.
     * @return This ComponentBuilder instance for method chaining.
     */
    public ComponentBuilder parse(String json) {
        Gson gson = new Gson();
        Object[] jsonArray = gson.fromJson(json, Object[].class);
        for (Object obj : jsonArray) {
            if (obj instanceof LinkedTreeMap linkedTreeMap) {
                component(Component.parse(linkedTreeMap));
            }
        }
        return this;
    }

    /**
     * Builds a JSON representation of the components in the builder.
     *
     * @return A JSON string representing the components.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("[\"\",");

        for (Component component : list) {
            sb.append(component).append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
}
