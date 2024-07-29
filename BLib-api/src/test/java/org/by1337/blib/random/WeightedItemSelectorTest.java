package org.by1337.blib.random;

import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class WeightedItemSelectorTest {

    @Test
    public void runTest() {
        List<WeightedItemImpl<String>> items = List.of(
                WeightedItemImpl.of(1, "A"),
                WeightedItemImpl.of(10, "B"),
                WeightedItemImpl.of(100, "C")
        );
        WeightedItemSelector<String> weightedItemSelector = new WeightedItemSelector<>(items, new Random(1337));

        int aCount = 0;
        int bCount = 0;
        int cCount = 0;
        int iterations = 10_000;

        for (int i = 0; i < iterations; i++) {
            String item = weightedItemSelector.getRandomItem();
            switch (item) {
                case "A":
                    aCount++;
                    break;
                case "B":
                    bCount++;
                    break;
                case "C":
                    cCount++;
                    break;
            }
        }
        assertTrue(((double) iterations / aCount) < 120 && ((double) iterations / aCount) > 70);
        assertTrue(((double) iterations / bCount) < 30 && ((double) iterations / bCount) > 5);
        assertTrue(((double) iterations / cCount) < 10 && ((double) iterations / cCount) > 0.1);
    }

}