/**
 * Interface representing hover event contents.
 */
package org.by1337.blib.chat.hover;

public interface HoverEventContents {
    String toString();
    HoverEventType getType();
    String toSource();
}
