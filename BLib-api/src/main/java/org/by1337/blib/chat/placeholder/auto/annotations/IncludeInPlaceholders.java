package org.by1337.blib.chat.placeholder.auto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated field should be included in the placeholder generation for
 * the class, even if the class is using {@link AutoPlaceholderGeneration} and not all fields
 * are explicitly marked for inclusion. This can be used when only specific fields should
 * be included in the placeholder generation.
 *
 * @see AutoPlaceholderGeneration
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IncludeInPlaceholders {
}
