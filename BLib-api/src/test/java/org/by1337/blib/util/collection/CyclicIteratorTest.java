package org.by1337.blib.util.collection;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class CyclicIteratorTest {

    @Test
    public void testIteration() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        CyclicIterator<Integer> iterator = new CyclicIterator<>(list);

        assertTrue(iterator.hasNext());
        assertEquals((Integer) 1, iterator.next());
        assertEquals((Integer) 2, iterator.next());
        assertEquals((Integer) 3, iterator.next());
        assertEquals((Integer) 1, iterator.next()); // Cycle back to the start
    }

    @Test
    public void testEmptyListNext() {
        List<Integer> list = new ArrayList<>();
        CyclicIterator<Integer> iterator = new CyclicIterator<>(list);

        assertThrows(IllegalStateException.class, iterator::next);
    }

    @Test
    public void testRemove() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        CyclicIterator<Integer> iterator = new CyclicIterator<>(list);

        assertEquals((Integer)1, iterator.next());
        iterator.remove();
        assertEquals(Arrays.asList(2, 3), list);

        assertEquals((Integer)2, iterator.next());
        iterator.remove();
        assertEquals(List.of(3), list);

        assertEquals((Integer)3, iterator.next());
        iterator.remove();
        assertEquals(new ArrayList<>(), list);
    }
    @Test
    public void testRemove2() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        CyclicIterator<Integer> iterator = new CyclicIterator<>(list);
        iterator.remove();
        assertEquals((Integer)2, iterator.next());
    }

    @Test
    public void testRemoveFromEmptyList() {
        List<Integer> list = new ArrayList<>();
        CyclicIterator<Integer> iterator = new CyclicIterator<>(list);

        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    public void testCyclicIteration() {
        List<Integer> list = Arrays.asList(1, 2);
        CyclicIterator<Integer> iterator = new CyclicIterator<>(list);

        assertEquals((Integer)1, iterator.next());
        assertEquals((Integer)2, iterator.next());
        assertEquals((Integer)1, iterator.next());
        assertEquals((Integer)2, iterator.next());
    }
}