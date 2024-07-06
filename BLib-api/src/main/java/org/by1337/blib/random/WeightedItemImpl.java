package org.by1337.blib.random;

/**
 * Implementation of the WeightedItem interface.
 *
 * @param <T> the type of the value
 */
public record WeightedItemImpl<T>(double weight, T value) implements WeightedItem<T> {
    /**
     * Creates a new instance of WeightedItemImpl with the specified weight and value.
     *
     * @param weight the weight of the item
     * @param value  the value of the item
     * @param <T>    the type of the value
     * @return a new instance of WeightedItemImpl
     */
    public static <T> WeightedItemImpl<T> of(double weight, T value) {
        return new WeightedItemImpl<>(weight, value);
    }
}
