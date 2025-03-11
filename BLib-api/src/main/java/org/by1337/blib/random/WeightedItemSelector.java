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
        if (items.isEmpty()) {
            throw new IllegalArgumentException("items is empty");
        }
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

    /**
     * Selects a random item based on their weights with an exponent adjustment.
     * <p>
     * The exponent parameter allows tweaking the probability distribution:
     * <ul>
     *   <li>If exponent &lt; 1, lower-weighted items become more likely to be chosen.</li>
     *   <li>If exponent = 1, standard weighted random selection is used.</li>
     *   <li>If exponent &gt; 1, higher-weighted items become even more dominant.</li>
     * </ul>
     *
     * @param exponent the exponent to adjust weight distribution
     * @return a randomly selected item
     * @throws IllegalStateException if there are no items to select from
     */

    public T getRandomItem(double exponent) {
        double totalAdjustedWeight = 0;
        double[] array = new double[items.size()];

        int x = 0;
        for (WeightedItem<T> item : items) {
            double adjustedWeight = Math.pow(item.weight(), exponent);
            array[x++] = adjustedWeight;
            totalAdjustedWeight += adjustedWeight;
        }

        double randomValue = random.nextDouble() * totalAdjustedWeight;
        double cumulativeWeight = 0;

        for (int i = 0; i < items.size(); i++) {
            cumulativeWeight += array[i];
            if (randomValue < cumulativeWeight) {
                return items.get(i).value();
            }
        }

        throw new IllegalStateException("No items to select from.");
    }

}