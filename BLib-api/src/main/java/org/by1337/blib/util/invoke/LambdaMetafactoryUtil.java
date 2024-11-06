package org.by1337.blib.util.invoke;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Utility class providing methods to dynamically create lambda expressions for field getters and setters
 * using Java's {@link LambdaMetafactory} and {@link MethodHandles}.
 * <p>
 * This class enables the creation of high-performance lambdas for field access (getter and setter)
 * through reflection, which is particularly useful for scenarios like serialization, deserialization, or
 * creating custom property accessors in libraries that require high efficiency and flexibility.
 *
 */
public class LambdaMetafactoryUtil {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    /**
     * Creates a {@link Function} representing the getter for the provided field.
     * <p>
     * This method uses {@link MethodHandles.Lookup#unreflectGetter(Field)} to obtain a {@link MethodHandle}
     * that represents the getter for the specified field. It then uses {@link LambdaMetafactory} to create
     * a lambda expression that invokes this getter. The resulting lambda can be used as a {@link Function}
     * to retrieve the value of the field from an object.
     * <p>
     * @param field The {@link Field} for which the getter lambda is created.
     * @param <T> The type of the object that contains the field.
     * @param <R> The type of the field value.
     * @return A {@link Function} that can be used to get the field value.
     * @throws Throwable If an error occurs while creating the MethodHandle or lambda.
     */
    @SuppressWarnings("unchecked")
    public static <T, R> Function<T, R> getterOf(Field field) throws Throwable {
        MethodHandle handle = LOOKUP.unreflectGetter(field);
        MethodType type = handle.type();
        return (Function<T, R>) LambdaMetafactory.metafactory(
                LOOKUP,
                "apply",
                MethodType.methodType(Function.class, MethodHandle.class),
                type.generic(),
                MethodHandles.exactInvoker(type),
                type
        ).getTarget().invokeExact(handle);
    }

    /**
     * Creates a {@link BiConsumer} representing the setter for the provided field.
     * <p>
     * This method uses {@link MethodHandles.Lookup#unreflectSetter(Field)} to obtain a {@link MethodHandle}
     * that represents the setter for the specified field. It then uses {@link LambdaMetafactory} to create
     * a lambda expression that invokes this setter. The resulting lambda can be used as a {@link BiConsumer}
     * to set the value of the field in an object.
     * <p>
     * @param field The {@link Field} for which the setter lambda is created.
     * @param <T> The type of the object that contains the field.
     * @param <R> The type of the field value.
     * @return A {@link BiConsumer} that can be used to set the field value.
     * @throws Throwable If an error occurs while creating the MethodHandle or lambda.
     */
    @SuppressWarnings("unchecked")
    public static <T, R> BiConsumer<T, R> setterOf(Field field) throws Throwable {
        MethodHandle handle = LOOKUP.unreflectSetter(field);
        MethodType type = handle.type();
        return (BiConsumer<T, R>) LambdaMetafactory.metafactory(
                LOOKUP,
                "accept",
                MethodType.methodType(BiConsumer.class, MethodHandle.class),
                type.generic().changeReturnType(void.class),
                MethodHandles.exactInvoker(type),
                type
        ).getTarget().invokeExact(handle);
    }
}
