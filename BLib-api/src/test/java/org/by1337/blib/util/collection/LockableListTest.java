package org.by1337.blib.util.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LockableListTest {
    private LockableList<Integer> lockableList;

    public LockableListTest() {
        List<Integer> source = new ArrayList<>();
        lockableList = new LockableList<>(source);
    }

    @Test
    public void iteratorTest() {
        LockableList<String> list = new LockableList<>();
        list.add("str");
        list.add("str1");
        list.add("str2");
        list.lock();
        var iterator = list.iterator();

        while (iterator.hasNext()) {
            var s = iterator.next();
            if (s.equals("str1")) {
                iterator.remove();
            }
            if (s.equals("str2")) {
                list.add("str5");
                list.add("str6");
            }
        }
        assertEquals(list.size(), 3);
        list.unlock();
        assertEquals(list.size(), 4);
        assertEquals(list.get(0), "str");
        assertEquals(list.get(1), "str2");
        assertEquals(list.get(2), "str5");
        assertEquals(list.get(3), "str6");
    }

    @Test
    public void testAddAndSize() {
        lockableList.add(1);
        lockableList.add(2);
        assertEquals(2, lockableList.size());
    }

    @Test
    public void testRemove() {
        lockableList.add(1);
        lockableList.add(2);
        assertTrue(lockableList.remove(1));
        assertEquals(1, lockableList.size());
    }

    @Test
    public void testLockAndUnlock() {
        lockableList.add(1);
        lockableList.lock();
        lockableList.add(2);
        assertEquals(1, lockableList.size());
        lockableList.unlock();
        assertEquals(2, lockableList.size());
    }

    @Test
    public void testClear() {
        lockableList.add(1);
        lockableList.add(2);
        lockableList.clear();
        assertTrue(lockableList.isEmpty());
    }

    @Test
    public void testIterator() {
        lockableList.add(1);
        lockableList.add(2);
        int sum = 0;
        for (Integer i : lockableList) {
            sum += i;
        }
        assertEquals(3, sum);
    }

    @Test
    public void testToArray() {
        lockableList.add(1);
        lockableList.add(2);
        Object[] array = lockableList.toArray();
        assertEquals(1, array[0]);
        assertEquals(2, array[1]);
    }

    @Test
    public void testIndexOf() {
        lockableList.add(1);
        lockableList.add(2);
        assertEquals(0, lockableList.indexOf(1));
        assertEquals(1, lockableList.indexOf(2));
    }

    @Test
    public void testLastIndexOf() {
        lockableList.add(1);
        lockableList.add(2);
        lockableList.add(1);
        assertEquals(2, lockableList.lastIndexOf(1));
    }

}