package org.by1337.blib.chat.placeholder.auto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated field should not be included in the placeholder generation
 * for the class. Fields annotated with this will be excluded from the placeholder processing
 * even if the class has {@link IncludeAllInPlaceholders}.
 *
 * @see IncludeAllInPlaceholders
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludeFromPlaceholders {
}
