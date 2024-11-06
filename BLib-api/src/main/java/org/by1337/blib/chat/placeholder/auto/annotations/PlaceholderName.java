package org.by1337.blib.chat.placeholder.auto.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a custom name for the placeholder generated for the annotated field.
 * The value provided in this annotation will be used as the placeholder name
 * instead of the default field name.
 *
 * Example:
 * <pre>
 * {@code
 * @PlaceholderName("custom_placeholder")
 * private String fieldName;
 * }
 * </pre>
 *
 * @see AutoPlaceholderGeneration
 * @see IncludeInPlaceholders
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlaceholderName {
    /**
     * The custom placeholder name for the annotated field.
     *
     * @return The name to use for the placeholder.
     */
    @NotNull String name();
}
