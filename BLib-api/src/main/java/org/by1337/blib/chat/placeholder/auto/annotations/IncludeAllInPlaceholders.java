package org.by1337.blib.chat.placeholder.auto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class to indicate that all **non-static** fields in the class
 * should automatically be included in the placeholder generation process.
 * This annotation works in conjunction with {@link AutoPlaceholderGeneration}.
 * When a class is annotated with {@link IncludeAllInPlaceholders}, all fields
 * in the class are automatically treated as if they were individually annotated
 * with {@link IncludeInPlaceholders}, meaning that placeholders will be generated
 * for every field in the class, without requiring explicit annotation of each field.
 *
 * This is useful when you want all fields in the class to be included in placeholder
 * generation by default, rather than specifying each field individually.
 *
 * @see AutoPlaceholderGeneration
 * @see IncludeInPlaceholders
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IncludeAllInPlaceholders {
}
