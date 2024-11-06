package org.by1337.blib.chat.placeholder.auto.annotations;

import org.by1337.blib.chat.placeholder.auto.PlaceholderNameFactory;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a custom factory class to generate placeholder names for fields
 * in the annotated class. The factory class should implement the {@link PlaceholderNameFactory}
 * interface and define the custom logic for generating placeholder names.
 *
 * Example:
 * <pre>
 * {@code
 * @CustomPlaceholderNameFactory(MyCustomNameFactory.class)
 * public class SomeClass { ... }
 * }
 * </pre>
 *
 * @see PlaceholderNameFactory
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPlaceholderNameFactory {
   /**
    * The custom {@link PlaceholderNameFactory} class used for generating placeholder names.
    *
    * @return The class implementing {@link PlaceholderNameFactory}.
    */
   @NotNull Class<? extends PlaceholderNameFactory> value();
}
