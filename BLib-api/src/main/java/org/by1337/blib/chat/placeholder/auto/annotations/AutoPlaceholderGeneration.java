package org.by1337.blib.chat.placeholder.auto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the placeholders for specific fields in the annotated class
 * should be generated automatically. This annotation should be placed on a class
 * to enable automatic placeholder generation, based on the fields annotated with
 * {@link IncludeInPlaceholders} or {@link IncludeAllInPlaceholders}.
 *
 * When this annotation is applied to a class, the system will automatically generate
 * placeholders for fields that are explicitly annotated with {@link IncludeInPlaceholders}.
 * If {@link IncludeAllInPlaceholders} is used on the class, all non-static fields in the class
 * will be included in the placeholder generation automatically.
 *
 * This annotation works in conjunction with other annotations like:
 * - {@link IncludeInPlaceholders}: to include specific fields in the placeholder generation.
 * - {@link ExcludeFromPlaceholders}: to exclude specific fields from the placeholder generation.
 * - {@link PlaceholderName}: to specify custom placeholder names for fields.
 *
 * @see IncludeInPlaceholders
 * @see IncludeAllInPlaceholders
 * @see ExcludeFromPlaceholders
 * @see PlaceholderName
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoPlaceholderGeneration {
}
