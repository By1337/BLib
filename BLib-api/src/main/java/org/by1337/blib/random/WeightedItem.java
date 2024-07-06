package org.by1337.blib.random;

/**
 * Represents an item with an associated weight.
 *
 * @param <T> the type of the value
 */
public interface WeightedItem<T> {
    /**
     * Gets the value of the item.
     *
     * @return the value of the item
     */
    T value();

    /**
     * Gets the weight of the item.
     *
     * @return the weight of the item
     */
    double weight();
}