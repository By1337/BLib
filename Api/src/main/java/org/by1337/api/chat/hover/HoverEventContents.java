/**
 * Interface representing hover event contents.
 */
package org.by1337.api.chat.hover;

public interface HoverEventContents {
    String toString();
    HoverEventType getType();
    String toSource();
}
