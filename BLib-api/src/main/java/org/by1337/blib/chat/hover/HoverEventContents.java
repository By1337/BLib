/**
 * Interface representing hover event contents.
 */
package org.by1337.blib.chat.hover;
@Deprecated(since = "1.0.7", forRemoval = true)
public interface HoverEventContents {
    String toString();
    HoverEventType getType();
    String toSource();
}
