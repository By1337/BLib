/**
 * Represents hover event contents as a string.
 */
package org.by1337.api.chat.hover;

import com.google.gson.internal.LinkedTreeMap;

public class HoverEventContentsString implements HoverEventContents{
    private String contents;

    /**
     * Constructs HoverEventContentsString with the specified contents.
     *
     * @param contents The contents of the hover event.
     */
    public HoverEventContentsString(String contents) {
        this.contents = contents;
    }

    public HoverEventContentsString setContents(String contents) {
        this.contents = contents;
        return this;
    }

    @Override
    public String toString() {
        return String.format("\"contents\": \"%s\"", contents);
    }

    @Override
    public HoverEventType getType() {
        return HoverEventType.SHOW_TEXT;
    }
    public String toSource(){
        return String.format("new HoverEventContentsString(\"%s\")", contents);
    }
    public static HoverEventContentsString parse(LinkedTreeMap<Object, Object> linkedTreeMap){
        return new HoverEventContentsString((String) linkedTreeMap.get("contents"));
    }
}
