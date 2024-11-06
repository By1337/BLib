package org.by1337.blib.chat.placeholder.auto;

import org.by1337.blib.chat.placeholder.auto.annotations.CustomPlaceholderNameFactory;

import java.lang.reflect.Field;

/**
 * A factory interface for generating placeholder names for fields. Implementations of this
 * interface define the logic for transforming a field name into a placeholder name.
 *
 * Example:
 * <pre>
 * {@code
 * public class MyCustomNameFactory implements PlaceholderNameFactory {
 *     public String toName(Field field) {
 *         return "custom_" + field.getName();
 *     }
 * }
 * }
 * </pre>
 *
 * The generated placeholder name will be used in place of the default field name.
 *
 * @see CustomPlaceholderNameFactory
 */
public interface PlaceholderNameFactory {

    /**
     * Transforms the given field name into a placeholder name.
     *
     * @param field The field whose name should be converted.
     * @return The placeholder name for the field.
     */
    String toName(Field field);
}
