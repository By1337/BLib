/**
 * Interface representing hover event contents.
 */
package org.by1337.blib.chat.hover;
@Deprecated(forRemoval = true)
public interface HoverEventContents {
    String toString();
    HoverEventType getType();
    String toSource();
}
