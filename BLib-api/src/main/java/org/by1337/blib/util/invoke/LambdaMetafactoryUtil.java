package org.by1337.blib.util.invoke;

import java.lang.invoke.*;
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
     *
     * @param field The {@link Field} for which the getter lambda is created.
     * @param <T>   The type of the object that contains the field.
     * @param <R>   The type of the field value.
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
     *
     * @param field The {@link Field} for which the setter lambda is created.
     * @param <T>   The type of the object that contains the field.
     * @param <R>   The type of the field value.
     * @return A {@link BiConsumer} that can be used to set the field value.
     * @throws Throwable If an error occurs while creating the MethodHandle or lambda.
     */
    public static <T, R> BiConsumer<T, R> setterOf(Field field) throws Throwable {
        MethodHandle handle = LOOKUP.unreflectSetter(field);
        MethodType type = handle.type();
        Class<?> interfaceType = getSetterType(field);
        CallSite callSite = LambdaMetafactory.metafactory(
                LOOKUP,
                interfaceType == BiConsumer.class ? "accept" : "accept0",
                MethodType.methodType(interfaceType, MethodHandle.class),
                interfaceType == BiConsumer.class ?
                        type.generic().changeReturnType(void.class) :
                        type.changeParameterType(0, Object.class),
                MethodHandles.exactInvoker(type),
                type
        );
        return castSetter(callSite, field, handle);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T, R> BiConsumer<T, R> castSetter(CallSite callSite, Field field, MethodHandle handle) throws Throwable {
        if (field.getType() == boolean.class) {
            return (BiConsumer<T, R>) (BooleanSetter) callSite.getTarget().invokeExact(handle);
        } else if (field.getType() == char.class) {
            return (BiConsumer<T, R>) (CharSetter) callSite.getTarget().invokeExact(handle);
        } else if (field.getType() == byte.class) {
            return (BiConsumer<T, R>) (ByteSetter) callSite.getTarget().invokeExact(handle);
        } else if (field.getType() == short.class) {
            return (BiConsumer<T, R>) (ShortSetter) callSite.getTarget().invokeExact(handle);
        } else if (field.getType() == int.class) {
            return (BiConsumer<T, R>) (IntSetter) callSite.getTarget().invokeExact(handle);
        } else if (field.getType() == long.class) {
            return (BiConsumer<T, R>) (LongSetter) callSite.getTarget().invokeExact(handle);
        } else if (field.getType() == float.class) {
            return (BiConsumer<T, R>) (FloatSetter) callSite.getTarget().invokeExact(handle);
        } else if (field.getType() == double.class) {
            return (BiConsumer<T, R>) (DoubleSetter) callSite.getTarget().invokeExact(handle);
        } else {
            return (BiConsumer<T, R>) callSite.getTarget().invokeExact(handle);
        }
    }

    private static Class<?> getSetterType(Field field) {
        if (field.getType() == boolean.class) {
            return BooleanSetter.class;
        } else if (field.getType() == char.class) {
            return CharSetter.class;
        } else if (field.getType() == byte.class) {
            return ByteSetter.class;
        } else if (field.getType() == short.class) {
            return ShortSetter.class;
        } else if (field.getType() == int.class) {
            return IntSetter.class;
        } else if (field.getType() == long.class) {
            return LongSetter.class;
        } else if (field.getType() == float.class) {
            return FloatSetter.class;
        } else if (field.getType() == double.class) {
            return DoubleSetter.class;
        } else {
            return BiConsumer.class;
        }
    }

    public interface BooleanSetter<T, R extends Boolean> extends BiConsumer<T, R> {
        @Override
        default void accept(T t, R r) {
            accept0(t, r);
        }

        void accept0(Object o, boolean u);
    }

    public interface CharSetter<T, R extends Character> extends BiConsumer<T, R> {
        @Override
        default void accept(T t, R r) {
            accept0(t, r);
        }

        void accept0(Object o, char u);
    }

    public interface ByteSetter<T, R extends Number> extends BiConsumer<T, R> {
        @Override
        default void accept(T t, R r) {
            accept0(t, r.byteValue());
        }

        void accept0(Object o, byte u);
    }

    public interface ShortSetter<T, R extends Number> extends BiConsumer<T, R> {
        @Override
        default void accept(T t, R r) {
            accept0(t, r.shortValue());
        }

        void accept0(Object o, short u);
    }

    public interface IntSetter<T, R extends Number> extends BiConsumer<T, R> {
        @Override
        default void accept(T t, R r) {
            accept0(t, r.intValue());
        }

        void accept0(Object o, int u);
    }

    public interface LongSetter<T, R extends Number> extends BiConsumer<T, R> {
        @Override
        default void accept(T t, R r) {
            accept0(t, r.longValue());
        }

        void accept0(Object o, long u);
    }

    public interface FloatSetter<T, R extends Number> extends BiConsumer<T, R> {
        @Override
        default void accept(T t, R r) {
            accept0(t, r.floatValue());
        }

        void accept0(Object o, float u);
    }

    public interface DoubleSetter<T, R extends Number> extends BiConsumer<T, R> {
        @Override
        default void accept(T t, R r) {
            accept0(t, r.doubleValue());
        }

        void accept0(Object o, double u);
    }
}
