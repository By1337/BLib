package org.by1337.blib.util.collection;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpiringSynchronizedMapTest {
    private ExpiringSynchronizedMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new ExpiringSynchronizedMap<>(50, TimeUnit.MILLISECONDS);
    }

    @AfterEach
    void tearDown() {
        map.shutdown();
    }

    @Test
    void testPutAndGet() {
        map.put("key1", "value1");
        assertEquals("value1", map.get("key1"));
    }

    @Test
    void testExpiration() throws InterruptedException {
        map.put("key1", "value1");
        Thread.sleep(70);
        assertNull(map.get("key1"));
    }

    @Test
    void testUpdateLastAccessTime() throws InterruptedException {
        map.put("key1", "value1");
        Thread.sleep(20);
        assertEquals("value1", map.get("key1"));
        Thread.sleep(20);
        assertEquals("value1", map.get("key1"));
        Thread.sleep(20);
        assertEquals("value1", map.get("key1"));
        Thread.sleep(70);
        assertNull(map.get("key1"));
    }

    @Test
    void testOnExpired() throws InterruptedException {
        AtomicReference<String> expiredKey = new AtomicReference<>();
        AtomicReference<String> expiredValue = new AtomicReference<>();
        map.onExpired((key, value) -> {
            expiredKey.set(key);
            expiredValue.set(value);
        });

        map.put("key1", "value1");
        Thread.sleep(70);
        assertNull(map.get("key1"));
        assertEquals("key1", expiredKey.get());
        assertEquals("value1", expiredValue.get());
    }

    @Test
    void testOnExpired2() throws InterruptedException {
        AtomicReference<String> expiredKey = new AtomicReference<>();
        AtomicReference<String> expiredValue = new AtomicReference<>();
        map.onExpired((key, value) -> {
            expiredKey.set(key);
            expiredValue.set(value);
        });

        map.put("key1", "value1");
        Thread.sleep(20);
        map.remove("key1");
        Thread.sleep(70);
        assertNull(map.get("key1"));
        assertNull(expiredKey.get());
        assertNull(expiredValue.get());

    }

    @Test
    void testRemove() {
        map.put("key1", "value1");
        assertEquals("value1", map.remove("key1"));
        assertNull(map.get("key1"));
    }

    @Test
    void testClear() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.clear();
        assertNull(map.get("key1"));
        assertNull(map.get("key2"));
    }

    @Test
    void testSize() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals(2, map.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(map.isEmpty());
        map.put("key1", "value1");
        assertFalse(map.isEmpty());
    }

    @Test
    void testContainsKey() {
        map.put("key1", "value1");
        assertTrue(map.containsKey("key1"));
        assertFalse(map.containsKey("key2"));
    }

    @Test
    void testContainsValue() {
        map.put("key1", "value1");
        assertTrue(map.containsValue("value1"));
        assertFalse(map.containsValue("value2"));
    }
}