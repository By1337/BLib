package org.by1337.blib.core.factory;
/**
 * Represents a functional interface that supplies a result of type T
 * based on an input value of type E.
 *
 * @param <T> the type of result to be supplied
 * @param <E> the type of the input value
 */
@FunctionalInterface
public interface OneArgSupplier<T, E> {
    /**
     * Supplies a result of type T based on the provided input value of type E.
     *
     * @param val the input value used to produce the result
     * @return the result of type T
     */
    T create(E val);
}
