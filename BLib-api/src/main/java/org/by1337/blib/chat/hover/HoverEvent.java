package org.by1337.blib.chat.hover;

/**
 * Represents a hover event with contents.
 */
@Deprecated(since = "1.0.7", forRemoval = true)
public class HoverEvent {
    private HoverEventContents contents;

    /**
     * Constructs a HoverEvent with the specified contents.
     *
     * @param contents The contents of the hover event.
     */
    public HoverEvent(HoverEventContents contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "\"hoverEvent\":{" + "\"action\":\"" + contents.getType().name().toLowerCase() + "\"," + contents + "}";
    }
    public String toSource(){
        return String.format(".hoverEvent(new HoverEvent(%s))", contents.toSource());
    }

    public HoverEvent setContents(HoverEventContents contents) {
        this.contents = contents;
        return this;
    }
}
