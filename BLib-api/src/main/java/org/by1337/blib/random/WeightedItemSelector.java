package org.by1337.blib.random;

import java.util.List;
import java.util.Random;

/**
 * Class for selecting a random item based on their weights.
 *
 * @param <T> the type of the item value
 */
public class WeightedItemSelector<T> {
    private final List<? extends WeightedItem<T>> items;
    private final Random random;
    private final double totalWeight;

    /**
     * Constructs a WeightedItemSelector with the specified list of items.
     *
     * @param items the list of weighted items
     */
    public WeightedItemSelector(List<? extends WeightedItem<T>> items) {
        this(items, new Random());
    }

    /**
     * Constructs a WeightedItemSelector with the specified list of items and a random number generator.
     *
     * @param items  the list of weighted items
     * @param random the random number generator
     */
    public WeightedItemSelector(List<? extends WeightedItem<T>> items, Random random) {
        this.items = List.copyOf(items);
        this.random = random;
        this.totalWeight = this.items.stream().mapToDouble(WeightedItem::weight).sum();
    }

    /**
     * Selects a random item based on their weights.
     *
     * @return a randomly selected item
     * @throws IllegalStateException if there are no items to select from
     */
    public T getRandomItem() {
        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0;

        for (WeightedItem<T> item : items) {
            cumulativeWeight += item.weight();
            if (randomValue < cumulativeWeight) {
                return item.value();
            }
        }

        throw new IllegalStateException("No items to select from.");
    }
}