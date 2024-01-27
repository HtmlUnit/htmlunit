/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class OrderedFastHashMapTest {

    @Test
    public void ctr() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        assertEquals(0, m.size());
        assertEquals(0, m.entrySet().size());
        assertEquals(0, m.keys().size());
        assertEquals(0, m.values().size());

        assertNull(m.get("test"));
    }

    @Test
    public void simplePut() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("K", "V");

        assertEquals("V", m.get("K"));
        assertEquals("K", m.getKey(0));
        assertEquals("V", m.getValue(0));
        assertEquals("V", m.getFirst());
        assertEquals("V", m.getLast());

        assertEquals("K", m.getEntry(0).getKey());
        assertEquals("V", m.getEntry(0).getValue());
    }

    @Test
    public void allowAnEmptyStart() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>(0);
        assertNull(m.get("K"));
        assertNull(m.remove("K"));

        m.put("K", "V");

        assertEquals("V", m.get("K"));
        assertEquals("K", m.getKey(0));
        assertEquals("V", m.getValue(0));
        assertEquals("V", m.getFirst());
        assertEquals("V", m.getLast());

        assertEquals("K", m.getEntry(0).getKey());
        assertEquals("V", m.getEntry(0).getValue());
    }

    @Test
    public void simpleMultiPut() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("K", "VK");
        m.put("B", "VB");
        m.put("A", "VA");
        m.put("Z", "VZ");

        final BiConsumer<String, Integer> t = (k, index) -> {
            assertEquals("V" + k, m.get(k));
            assertEquals(k, m.getKey(index));
            assertEquals("V" + k, m.getValue(index));
        };
        t.accept("K", 0);
        t.accept("B", 1);
        t.accept("A", 2);
        t.accept("Z", 3);

        // adding something last
        m.put("0", "V0");
        m.put("z", "Vz");

        t.accept("K", 0);
        t.accept("B", 1);
        t.accept("A", 2);
        t.accept("Z", 3);
        t.accept("0", 4);
        t.accept("z", 5);
    }

    @Test
    public void putAgainDoesNotChangeOrder() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("K", "VK");
        m.put("B", "VB");
        m.put("A", "VA");
        m.put("Z", "VZ");

        m.put("B", "VB");
        m.put("K", "VK");
        m.put("Z", "VZ");
        m.put("A", "VA");

        final BiConsumer<String, Integer> t = (k, index) -> {
            assertEquals("V" + k, m.get(k));
            assertEquals(k, m.getKey(index));
            assertEquals("V" + k, m.getValue(index));
        };
        t.accept("K", 0);
        t.accept("B", 1);
        t.accept("A", 2);
        t.accept("Z", 3);
    }

    @Test
    public void getDoesNotChangeOrder() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("K", "VK");
        m.put("B", "VB");
        m.put("A", "VA");
        m.put("Z", "VZ");

        assertEquals("VZ", m.get("Z"));
        assertEquals("VA", m.get("A"));
        assertEquals("VB", m.get("B"));
        assertEquals("VZ", m.get("Z"));

        final BiConsumer<String, Integer> t = (k, index) -> {
            assertEquals("V" + k, m.get(k));
            assertEquals(k, m.getKey(index));
            assertEquals("V" + k, m.getValue(index));
        };
        t.accept("K", 0);
        t.accept("B", 1);
        t.accept("A", 2);
        t.accept("Z", 3);
    }

    @Test
    public void growSmall() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>(3);
        for (int i = 0; i < 10; i++) {
            m.add(String.valueOf(i), "V" + String.valueOf(i));
        }

        final BiConsumer<String, Integer> t = (k, index) -> {
            assertEquals("V" + k, m.get(k));
            assertEquals(k, m.getKey(index));
            assertEquals("V" + k, m.getValue(index));
        };
        for (int i = 0; i < 10; i++) {
            t.accept(String.valueOf(i), i);
        }
    }

    @Test
    public void growBig() {
        final List<String> keys = new ArrayList<>();
        final Random r = new Random();

        for (int i = 0; i < 200; i++) {
            final String key = RandomUtils.randomString(r, 3, 10);
            keys.add(key);
        }

        // ok, we got random keys, our values will be V + its key
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>(10);
        for (final String key : keys) {
            m.put(key, "V" + key);
        }

        final BiConsumer<String, Integer> t = (k, index) -> {
            assertEquals("V" + k, m.get(k));
            assertEquals(k, m.getKey(index));
            assertEquals("V" + k, m.getValue(index));
        };

        // ok, order will match when asking for keys
        for (int i = 0; i < 200; i++) {
            t.accept(keys.get(i), i);
        }
    }

    @Test
    public void keys() {
        final OrderedFastHashMap<String, Integer> f = new OrderedFastHashMap<>(3);
        f.put("aa", 1);
        f.put("bb", 2);
        f.put("cc", 3);
        f.put("dd", 4);
        f.put("ee", 5);

        final List<String> k = f.keys();
        assertEquals(5, k.size());
        assertTrue(k.contains("aa"));
        assertTrue(k.contains("bb"));
        assertTrue(k.contains("cc"));
        assertTrue(k.contains("dd"));
        assertTrue(k.contains("ee"));

        // remove from the middle
        assertEquals(Integer.valueOf(3), f.remove("cc"));
        f.remove("c");
        final List<String> k2 = f.keys();
        assertEquals(4, k2.size());
        assertTrue(k2.contains("aa"));
        assertTrue(k2.contains("bb"));
        assertTrue(k2.contains("dd"));
        assertTrue(k2.contains("ee"));

        // add to the end, remove unknown
        f.put("zz", 10);
        f.remove("c");
        final List<String> k3 = f.keys();
        assertEquals(5, k3.size());
        assertTrue(k3.contains("aa"));
        assertTrue(k3.contains("bb"));
        assertTrue(k3.contains("dd"));
        assertTrue(k3.contains("ee"));
        assertTrue(k3.contains("zz"));

        // ask for something unknown
        assertNull(f.get("unknown"));
    }

    @Test
    public void values() {
        final OrderedFastHashMap<String, Integer> f = new OrderedFastHashMap<>(3);
        f.put("aa", 1);
        f.put("bb", 2);
        f.put("cc", 3);
        f.put("dd", 4);
        f.put("ee", 5);

        // initial values
        final List<Integer> values = f.values();
        assertEquals(5, values.size());
        assertTrue(values.contains(1));
        assertTrue(values.contains(2));
        assertTrue(values.contains(3));
        assertTrue(values.contains(4));
        assertTrue(values.contains(5));

        // something removed
        assertEquals(Integer.valueOf(3), f.remove("cc"));
        // something unknnown removed
        f.remove("c");
        final List<Integer> v2 = f.values();
        assertEquals(4, v2.size());
        assertTrue(v2.contains(1));
        assertTrue(v2.contains(2));
        assertTrue(v2.contains(4));
        assertTrue(v2.contains(5));
    }

    @Test
    public void remove_simple_only_one() {
        final OrderedFastHashMap<String, String> m1 = new OrderedFastHashMap<>();
        // remove instantly
        m1.put("b", "value");
        assertEquals("value", m1.remove("b"));
        assertEquals(0, m1.size());
    }

    @Test
    public void remove_simple_first() {
        // remove first
        final OrderedFastHashMap<String, String> m1 = new OrderedFastHashMap<>();
        m1.put("b", "bvalue");
        m1.put("c", "cvalue");
        assertEquals("bvalue", m1.remove("b"));
        assertEquals("cvalue", m1.get("c"));
        assertEquals(1, m1.size());
    }

    @Test
    public void remove_simple_from_the_middle() {
        // remove the one in the middle
        final OrderedFastHashMap<String, String> m1 = new OrderedFastHashMap<>();
        m1.put("a", "avalue");
        m1.put("b", "bvalue");
        m1.put("c", "cvalue");
        assertEquals("bvalue", m1.remove("b"));
        assertEquals("avalue", m1.get("a"));
        assertEquals("cvalue", m1.get("c"));
        assertEquals(2, m1.size());
        assertEquals("a", m1.getKey(0));
        assertEquals("c", m1.getKey(1));
    }

    @Test
    public void remove_simple_unknown() {
        // remove unknown
        final OrderedFastHashMap<String, String> m1 = new OrderedFastHashMap<>();
        assertNull(m1.remove("a"));
        m1.put("b", "value");
        assertNull(m1.remove("a"));
    }

    @Test
    public void remove_complex() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>(3);
        m.put("b", "Vb1");
        m.put("a", "Va");
        m.put("d", "Vd1");
        m.put("c", "Vc");
        m.put("e", "Ve");

        m.remove("b");
        m.remove("d");

        final BiConsumer<String, Integer> t = (k, index) -> {
            assertEquals("V" + k, m.get(k));
            assertEquals(k, m.getKey(index));
            assertEquals("V" + k, m.getValue(index));
        };

        assertEquals(3, m.size());
        t.accept("a", 0);
        t.accept("c", 1);
        t.accept("e", 2);
        assertNull(m.get("d"));
        assertNull(m.get("b"));

        // remove again
        assertNull(m.remove("b"));
        assertNull(m.remove("d"));

        m.put("d", "Vd");
        m.put("b", "Vb");

        t.accept("a", 0);
        t.accept("c", 1);
        t.accept("e", 2);
        t.accept("d", 3);
        t.accept("b", 4);
    }

    @Test
    public void removeRandomlyToEmpty() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>(15);
        final Set<String> keys = new HashSet<>();
        final Random r = new Random();

        for (int i = 0; i < 1000; i++) {
            final String key = RandomUtils.randomString(r, 1, 10);
            keys.add(key); // just in case we have double keys generated
            m.put(key, "V" + key);
        }
        assertEquals(keys.size(), m.size());

        keys.forEach(key -> {
            assertEquals("V" + key, m.get(key));
            m.remove(key);
            assertNull(m.get(key));
        });
        assertEquals(0, m.size());
    }

    @Test
    public void removeTryingToCoverEdges_Last() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        for (int i = 0; i < 200; i++) {
            // we add two, but immediately remove the last again
            m.put(String.valueOf(i), "V" + String.valueOf(i));
            m.put(String.valueOf(i + 1), "any");
            assertEquals("any", m.remove(String.valueOf(i + 1)));
        }

        final BiConsumer<String, Integer> t = (k, index) -> {
            assertEquals("V" + k, m.get(k));
            assertEquals(k, m.getKey(index));
            assertEquals("V" + k, m.getValue(index));
        };

        assertEquals(200, m.size());
        for (int i = 0; i < 200; i++) {
            t.accept(String.valueOf(i), i);
        }
    }

    @Test
    public void removeTryingToCoverEdges_First() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        for (int i = 0; i < 4000; i = i + 2) {
            m.put(String.valueOf(i), "any");
            m.put(String.valueOf(i + 1), "V" + String.valueOf(i + 1));
            assertEquals("any", m.remove(String.valueOf(i)));
        }

        final BiConsumer<String, Integer> t = (k, index) -> {
            assertEquals("V" + k, m.get(k));
            assertEquals(k, m.getKey(index));
            assertEquals("V" + k, m.getValue(index));
        };

        assertEquals(2000, m.size());
        for (int i = 0; i < 4000; i = i + 2) {
            t.accept(String.valueOf(i + 1), i / 2);
        }
    }

    @Test
    public void removeTryingToCoverEdges_Middle() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        for (int i = 0; i < 3 * 1972; i = i + 3) {
            m.put(String.valueOf(i), "V" + String.valueOf(i));
            m.put(String.valueOf(i + 1), "any");
            m.put(String.valueOf(i + 2), "V" + String.valueOf(i + 2));
            assertEquals("any", m.remove(String.valueOf(i + 1)));
        }

        final BiConsumer<String, Integer> t = (k, index) -> {
            assertEquals("V" + k, m.get(k));
            assertEquals(k, m.getKey(index));
            assertEquals("V" + k, m.getValue(index));
        };

        assertEquals(2 * 1972, m.size());
        for (int i = 0, pos = 0; i < 3 * 1972; i = i + 3, pos = pos + 2) {
            t.accept(String.valueOf(i), pos);
            t.accept(String.valueOf(i + 2), pos + 1);
        }
    }

    @Test
    public void removeByIndex_first() {
        final OrderedFastHashMap<String, Integer> m = new OrderedFastHashMap<String, Integer>();
        m.put("a", 1);
        m.put("b", 2);
        m.put("c", 3);
        m.remove(0);

        assertEquals("b", m.getKey(0));
        assertEquals("c", m.getKey(1));
        assertEquals(2, m.size());
    }

    @Test
    public void removeByIndex_second() {
        final OrderedFastHashMap<String, Integer> m = new OrderedFastHashMap<String, Integer>();
        m.put("a", 1);
        m.put("b", 2);
        m.put("c", 3);
        m.remove(1);

        assertEquals("a", m.getKey(0));
        assertEquals("c", m.getKey(1));
        assertEquals(2, m.size());
    }

    @Test
    public void removeByIndex_last() {
        final OrderedFastHashMap<String, Integer> m = new OrderedFastHashMap<String, Integer>();
        m.put("a", 1);
        m.put("b", 2);
        m.put("c", 3);
        m.remove(2);

        assertEquals("a", m.getKey(0));
        assertEquals("b", m.getKey(1));
        assertEquals(2, m.size());
    }

    @Test
    public void removeByIndex_middle_to_empty() {
        final OrderedFastHashMap<String, Integer> m = new OrderedFastHashMap<String, Integer>();
        m.put("a", 1);
        m.put("b", 2);
        m.put("c", 3);
        m.remove(1);
        m.remove(1);
        m.remove(0);

        assertEquals(0, m.size());
    }

    @Test
    public void clear() {
        final OrderedFastHashMap<String, Integer> m = new OrderedFastHashMap<String, Integer>();
        m.put("a", 1);
        assertEquals(1, m.size());

        m.clear();
        assertEquals(0, m.size());
        assertEquals(0, m.keys().size());
        assertEquals(0, m.values().size());
        assertNull(m.get("a"));

        m.put("b", 2);
        assertEquals(1, m.size());
        m.put("a", 3);
        assertEquals(2, m.size());

        m.clear();
        assertEquals(0, m.size());
        assertEquals(0, m.keys().size());
        assertEquals(0, m.values().size());

        m.put("a", 1);
        m.put("b", 2);
        m.put("c", 3);
        m.put("c", 3);
        assertEquals(3, m.size());
        assertEquals(3, m.keys().size());
        assertEquals(3, m.values().size());

        assertEquals(Integer.valueOf(1), m.get("a"));
        assertEquals(Integer.valueOf(2), m.get("b"));
        assertEquals(Integer.valueOf(3), m.get("c"));
    }

    @Test
    public void removeLast() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        final Random r1 = new Random(144L);

        // empty is null
        assertNull(m.removeLast());

        for (int i = 0; i < 3 * 1972; i++) {
            final String key = RandomUtils.randomString(r1, 20);
            final String value = "V" + key;

            m.put(key, value);
            assertEquals(value, m.removeLast());
            m.put(key, value);
        }
        assertEquals(3 * 1972, m.size());

        final BiConsumer<String, Integer> t = (k, index) -> {
            assertEquals("V" + k, m.get(k));
            assertEquals(k, m.getKey(index));
            assertEquals("V" + k, m.getValue(index));
        };

        final Random r2 = new Random(144L);
        for (int i = 0; i < 3 * 1972; i++) {
            final String key = RandomUtils.randomString(r2, 20);
            t.accept(key, i);
        }

        final Random r3 = new Random(144L);
        for (int i = 0; i < 3 * 1972; i++) {
            final String key = RandomUtils.randomString(r3, 2, 10);
            m.removeLast();
        }
        assertEquals(0, m.size());
    }

    @Test
    public void removeFirst_Empty() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        assertNull(m.removeFirst());
        assertEquals(0, m.size());
    }

    @Test
    public void removeFirst_One() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("u", "Vu");
        assertEquals("Vu", m.removeFirst());
        assertEquals(0, m.size());
    }

    @Test
    public void removeFirst_Two() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("u", "Vu");
        m.put("a", "Va");
        assertEquals("Vu", m.removeFirst());
        assertEquals(1, m.size());
        assertEquals("a", m.getKey(0));
        assertEquals("Va", m.getValue(0));
    }

    @Test
    public void removeFirst_WithGrowth() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>(3);
        final Random r = new Random(98765244L);
        final List<String> keys = new ArrayList<>();

        final BiConsumer<String, Integer> t = (k, index) -> {
            assertEquals("V" + k, m.get(k));
            assertEquals(k, m.getKey(index));
            assertEquals("V" + k, m.getValue(index));
        };

        for (int i = 1; i < 1992; i++) {
            keys.clear();
            m.clear();

            // setup entries
            for (int e = 0; e < i; e++) {
                final String k = RandomUtils.randomString(r, 20);
                keys.add(k);
            }

            // add these all and remove first
            for (final String k : keys) {
                m.put(k, "V" + k);
            }
            // remove first
            final String rk = keys.remove(0);
            assertEquals("V" + rk, m.removeFirst());

            // add back
            m.put(rk, "V" + rk);
            keys.add(rk);

            // verify
            assertEquals(keys.size(), m.size());

            // order of things
            for (int e = 0; e < keys.size(); e++) {
                t.accept(keys.get(e), e);
            }
        }
    }

    @Test
    public void addFirst_to_empty() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.addFirst("a", "1");
        assertEquals(1, m.size());
        assertEquals("a", m.getKey(0));
        assertEquals("1", m.getValue(0));
    }


    @Test
    public void addFirst_twice() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.addFirst("a", "1");
        m.addFirst("b", "2");
        assertEquals(2, m.size());
        assertEquals("a", m.getKey(1));
        assertEquals("1", m.getValue(1));
        assertEquals("b", m.getKey(0));
        assertEquals("2", m.getValue(0));
    }

    @Test
    public void addFirst__second_to_normal_added() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.add("a", "1");
        m.addFirst("b", "2");
        assertEquals(2, m.size());
        assertEquals("a", m.getKey(1));
        assertEquals("1", m.getValue(1));
        assertEquals("b", m.getKey(0));
        assertEquals("2", m.getValue(0));
    }

    @Test
    public void addLast_to_empty() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.addLast("a", "1");
        assertEquals(1, m.size());
        assertEquals("a", m.getKey(0));
        assertEquals("1", m.getValue(0));
    }

    @Test
    public void addLast_twice() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.addLast("a", "1");
        m.addLast("b", "2");
        assertEquals(2, m.size());
        assertEquals("a", m.getKey(0));
        assertEquals("1", m.getValue(0));
        assertEquals("b", m.getKey(1));
        assertEquals("2", m.getValue(1));
    }

    @Test
    public void addLast_to_normally_added() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.add("a", "1");
        m.addLast("b", "2");
        assertEquals(2, m.size());
        assertEquals("a", m.getKey(0));
        assertEquals("1", m.getValue(0));
        assertEquals("b", m.getKey(1));
        assertEquals("2", m.getValue(1));
    }

    @Test
    public void containsKey_Empty() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        assertFalse(m.containsKey("akey"));
    }

    @Test
    public void containsKey_True_single() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("akey", "any");
        assertTrue(m.containsKey("akey"));
    }

    @Test
    public void containsKey_True_many() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("akey1", "any");
        m.put("akey2", "any");
        m.put("akey3", "any");
        m.put("akey4", "any");
        assertTrue(m.containsKey("akey2"));
        assertTrue(m.containsKey(new String("akey2")));
    }

    @Test
    public void containsKey_True_content_based() {
        // same hash and different content
        final OrderedFastHashMap<MockKey<String>, String> m = new OrderedFastHashMap<>();
        final MockKey<String> mockKey1 = new MockKey<>(10, "akey1");
        m.put(mockKey1, "any1");
        m.put(new MockKey<String>(10, "akey2"), "any2");
        m.put(new MockKey<String>(10, "akey3"), "any3");
        m.put(new MockKey<String>(10, "akey4"), "any4");
        assertTrue(m.containsKey(mockKey1));
        assertTrue(m.containsKey(new MockKey<>(10, "akey1")));
    }

    @Test
    public void containsKey_False_single() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("akey", "any");
        assertFalse(m.containsKey("akey1"));
    }

    @Test
    public void containsKey_False_many() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("akey1", "any");
        m.put("akey2", "any");
        m.put("akey3", "any");
        assertFalse(m.containsKey("akey"));
    }

    @Test
    public void containsKey_False_content_based() {
        // same hash but different content
        final OrderedFastHashMap<MockKey<String>, String> m = new OrderedFastHashMap<>();
        m.put(new MockKey<String>(10, "akey2"), "any2");
        m.put(new MockKey<String>(10, "akey3"), "any3");
        m.put(new MockKey<String>(10, "akey4"), "any4");
        assertFalse(m.containsKey(new MockKey<>(10, "akey")));

        // different hash, same content
        assertFalse(m.containsKey(new MockKey<>(114, "akey2")));
        assertFalse(m.containsKey(new MockKey<>(113, "akey3")));
        assertFalse(m.containsKey(new MockKey<>(112, "akey4")));
    }

    @Test
    public void containsValue_Empty() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        assertFalse(m.containsValue("avalue"));
    }

    @Test
    public void containsValue_True_single() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("akey3", "any");
        assertTrue(m.containsValue("any"));
    }

    @Test
    public void containsValue_True_content_based() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("akey1", "any1");
        m.put("akey2", "any2");
        m.put("akey3", "any3");
        assertTrue(m.containsValue("any1"));
        assertTrue(m.containsValue("any2"));
        assertTrue(m.containsValue("any3"));
        assertTrue(m.containsValue(new String("any1")));
        assertTrue(m.containsValue(new String("any2")));
        assertTrue(m.containsValue(new String("any3")));
    }

    @Test
    public void containsValue_False_single() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("akey3", "any");
        assertFalse(m.containsValue("asdf"));
    }

    @Test
    public void containsValue_False_many() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("akey1", "any1");
        m.put("akey2", "any2");
        m.put("akey3", "any3");
        // not mistaking the key as value
        assertFalse(m.containsValue("akey1"));
        assertFalse(m.containsValue("akey2"));
        assertFalse(m.containsValue("akey3"));

        // just some other values
        assertFalse(m.containsValue("any5"));
        assertFalse(m.containsValue("any6"));
        assertFalse(m.containsValue("any7"));
    }

    @Test
    public void isEmpty_empty() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        assertTrue(m.isEmpty());
    }

    @Test
    public void isEmpty_size_zero() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>(0);
        assertTrue(m.isEmpty());
    }

    @Test
    public void isEmpty_false() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("aaa", "a");
        assertFalse(m.isEmpty());
    }

    @Test
    public void isEmpty_after_clear() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("aaa", "a");
        m.clear();
        assertTrue(m.isEmpty());
    }

    @Test
    public void entry() {
        final Map<String, String> m = new OrderedFastHashMap<>();
        m.put("K1", "V1");
        m.put("K2", "V1");
        final Entry<String, String> e = m.entrySet().iterator().next();

        Iterator<Entry<String, String>> i = m.entrySet().iterator();
        final Entry<String, String> e1 = i.next(); // K1, V1
        final Entry<String, String> e2 = i.next(); // K2, V1

        m.put("K1", "V2");
        i = m.entrySet().iterator();
        final Entry<String, String> e3 = i.next(); // K1, V2

        // making sure we have all sub methods covered
        assertEquals("K1", e.getKey());
        assertEquals("V1", e.getValue());
        assertEquals(e.getKey().hashCode() ^ e.getValue().hashCode(), e.hashCode());
        assertEquals("K1=V1", e.toString());

        assertTrue(e.equals(e));

        assertTrue(e1.equals(e));
        assertTrue(e.equals(e1));

        assertFalse(e.equals(e2));
        assertFalse(e2.equals(e));

        assertFalse(e.equals(e2));
        assertFalse(e3.equals(e));

        assertFalse(e.equals("k"));

        assertThrows(UnsupportedOperationException.class, () -> e.setValue("foo"));
    }

    @Test
    public void entrySet_empty() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        final Set<Entry<String, String>> set = m.entrySet();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @Test
    public void entrySet_zero_size() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>(0);
        final Set<Entry<String, String>> set = m.entrySet();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @Test
    public void entrySet() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("K1", "V1");
        final Set<Entry<String, String>> set = m.entrySet();
        assertEquals(1, set.size());
        assertFalse(set.isEmpty());

        final Entry<String, String> e1 = set.iterator().next();
        assertEquals("K1", e1.getKey());
        assertEquals("V1", e1.getValue());

        final Map<String, String> map = new OrderedFastHashMap<>();
        map.put("K1", "V1");
        map.put("K2", "V1");
        final Iterator<Map.Entry<String, String>> i = map.entrySet().iterator();
        final Entry<String, String> e2 = i.next();
        final Entry<String, String> e3 = i.next();

        assertTrue(set.contains(e1));
        assertTrue(set.contains(e2));

        assertFalse(set.contains("a"));
        assertFalse(set.contains(e3));
    }

    @Test
    public void entrySet_large() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        IntStream.rangeClosed(0, 11).forEach(i -> m.put("K" + i, "V" + i));
        final Set<Entry<String, String>> set = m.entrySet();
        assertEquals(12, set.size());
        assertFalse(set.isEmpty());

        final Iterator<Entry<String, String>> iter = set.iterator();
        IntStream.rangeClosed(0, 11).forEach(i -> {
            assertTrue(iter.hasNext());
            final Entry<String, String> e = iter.next();
            assertEquals("K" + i, e.getKey());
            assertEquals("V" + i, e.getValue());
        });
        assertFalse(iter.hasNext());

        // overread
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void entrySet_toArray_empty() {
        final Map<String, String> m = new OrderedFastHashMap<>();
        final Set<Entry<String, String>> set = m.entrySet();
        assertEquals(0, set.toArray().length);
        assertEquals(0, set.toArray(new Map.Entry[0]).length);
        assertEquals(10, set.toArray(new String[10]).length);

        // unfilled despite length 10
        final String[] a = set.toArray(new String[10]);
        for (int i = 0; i < 10; i++) {
            assertNull(a[i]);
        }
    }

    @Test
    public void entrySet_toArray_zero_sized() {
        final Map<String, String> m = new OrderedFastHashMap<>(0);
        final Set<Entry<String, String>> set = m.entrySet();
        assertEquals(0, set.toArray().length);
        assertEquals(0, set.toArray(new Map.Entry[0]).length);
        assertEquals(10, set.toArray(new String[10]).length);

        // unfilled despite length 10
        final String[] a = set.toArray(new String[10]);
        for (int i = 0; i < 10; i++) {
            assertNull(a[i]);
        }
    }

    @Test
    public void entrySet_toArray_normal() {
        final Map<String, String> m = new OrderedFastHashMap<>();
        m.put("K1", "V1");
        final Set<Entry<String, String>> set = m.entrySet();
        assertEquals(1, set.toArray().length);
        assertEquals(1, set.toArray(new Map.Entry[0]).length);
        assertEquals(1, set.toArray(new Map.Entry[1]).length);

        assertNotNull(set.toArray()[0]);
        assertEquals("K1", ((Map.Entry<String, String>) set.toArray()[0]).getKey());
        assertEquals("V1", ((Map.Entry<String, String>) set.toArray()[0]).getValue());

        assertEquals("K1", set.toArray(new Map.Entry[0])[0].getKey());
        assertEquals("V1", set.toArray(new Map.Entry[0])[0].getValue());

        assertEquals("K1", set.toArray(new Map.Entry[1])[0].getKey());
        assertEquals("V1", set.toArray(new Map.Entry[1])[0].getValue());
    }

    @Test
    public void entrySet_toArray_get_same_back() {
        // ensure we get our array back when it is sufficiently sized
        final Map<String, String> m = new OrderedFastHashMap<>();
        m.put("K1", "V1");
        final Set<Entry<String, String>> set = m.entrySet();

        // right sized
        final Map.Entry[] array1 = new Map.Entry[1];
        assertSame(array1, set.toArray(array1));

        // oversized
        final Map.Entry[] array2 = new Map.Entry[10];
        assertSame(array2, set.toArray(array2));
    }

    @Test
    public void entrySet_toArray_large() {
        final Map<String, String> m = new OrderedFastHashMap<>();
        IntStream.rangeClosed(0, 44).forEach(i -> m.put("K" + i, "V" + i));
        final Object[] a1 = m.entrySet().toArray();
        final Map.Entry[] a2 = m.entrySet().toArray(new Map.Entry[45]);
        assertEquals(45, a1.length);
        assertEquals(45, a2.length);

        for (int i = 0; i <= 44; i++) {
            assertEquals("K" + i, ((Map.Entry<String, String>) a1[i]).getKey());
            assertEquals("V" + i, ((Map.Entry<String, String>) a1[i]).getValue());

            assertEquals("K" + i, a2[i].getKey());
            assertEquals("V" + i, a2[i].getValue());
        }
    }

    @Test
    public void keySet_empty() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        final Set<String> set = m.keySet();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @Test
    public void keySet_size_zero() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>(0);
        final Set<String> set = m.keySet();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @Test
    public void keySet_one() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("K1", "V1");
        final Set<String> set = m.keySet();
        assertEquals(1, set.size());
        assertFalse(set.isEmpty());

        final String e1 = set.iterator().next();
        assertEquals("K1", e1);
    }

    @Test
    public void keySet_large() {
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        IntStream.rangeClosed(0, 44).forEach(i -> m.put("K" + i, "V" + i));
        final Set<String> set = m.keySet();
        assertEquals(45, set.size());
        assertFalse(set.isEmpty());

        final Iterator<String> iter = set.iterator();
        IntStream.rangeClosed(0, 44).forEach(i -> {
            assertTrue(iter.hasNext());
            final String e = iter.next();
            assertEquals("K" + i, e);
            assertTrue(set.contains("K" + i));
        });
        assertFalse(iter.hasNext());

        // overread
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void keySet_toArray_empty() {
        final Map<String, String> m = new OrderedFastHashMap<>();
        final Set<String> set = m.keySet();
        assertEquals(0, set.toArray().length);
        assertEquals(0, set.toArray(new String[0]).length);
        assertEquals(10, set.toArray(new String[10]).length);

        // unfilled despite length 10
        final String[] a = set.toArray(new String[10]);
        for (int i = 0; i < 10; i++) {
            assertNull(a[i]);
        }
    }

    @Test
    public void keySet_toArray_one() {
        final Map<String, String> m = new OrderedFastHashMap<>();
        m.put("K1", "V1");
        final Set<String> set = m.keySet();
        assertEquals(1, set.toArray().length);
        assertEquals(1, set.toArray(new String[0]).length);
        assertEquals(1, set.toArray(new String[1]).length);

        assertEquals("K1", set.toArray()[0]);
        assertEquals("K1", set.toArray(new String[0])[0]);
        assertEquals("K1", set.toArray(new String[1])[0]);
    }

    @Test
    public void keySet_toArray_same_array() {
        // ensure we get our array back when it is sufficiently sized
        final Map<String, String> m = new OrderedFastHashMap<>();
        m.put("K1", "V1");
        final Set<String> set = m.keySet();

        // right sized
        final String[] array1 = new String[1];
        assertSame(array1, set.toArray(array1));

        // oversized
        final String[] array2 = new String[10];
        assertSame(array2, set.toArray(array2));
    }

    @Test
    public void keySet_toArray_many() {
        final Map<String, String> m = new OrderedFastHashMap<>();
        IntStream.rangeClosed(0, 44).forEach(i -> m.put("K" + i, "V" + i));
        final Object[] a1 = m.keySet().toArray();
        final String[] a2 = m.keySet().toArray(new String[45]);
        assertEquals(45, a1.length);
        assertEquals(45, a2.length);

        for (int i = 0; i <= 44; i++) {
            assertEquals("K" + i, a1[i]);
            assertEquals("K" + i, a2[i]);
        }
    }

    @Test
    public void putAll_cannot_add_self() {
        // we cannot add ourselves!
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
            m.putAll(m);
        });
        assertEquals("Cannot add myself", thrown.getMessage());
    }

    @Test
    public void putAll_empty() {
        // empty
        final Map<String, String> src = new HashMap<>();
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.putAll(src);
        assertEquals(0, m.size());
    }

    @Test
    public void putAll_target_empty() {
        // target empty
        final Map<String, String> src = new LinkedHashMap<>();
        IntStream.rangeClosed(0, 10).forEach(i -> src.put("K" + i, "V" + i));

        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.putAll(src);
        assertEquals(11, m.size());

        final Iterator<Entry<String, String>> iter = m.entrySet().iterator();
        IntStream.rangeClosed(0, 10).forEach(i -> {
            assertTrue(iter.hasNext());
            final Entry<String, String> e = iter.next();
            assertEquals("K" + i, e.getKey());
            assertEquals("V" + i, e.getValue());
        });
        assertFalse(iter.hasNext());
    }

    @Test
    public void putAll_source_empty() {
        // src empty
        final Map<String, String> src = new LinkedHashMap<>();
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("K1", "V1");

        m.putAll(src);
        assertEquals(1, m.size());

        final Iterator<Entry<String, String>> iter = m.entrySet().iterator();
        assertTrue(iter.hasNext());
        final Entry<String, String> e = iter.next();
        assertEquals("K1", e.getKey());
        assertEquals("V1", e.getValue());
        assertFalse(iter.hasNext());
    }

    @Test
    public void putAll_target_not_empty() {
        // target not empty
        final Map<String, String> src = new LinkedHashMap<>();
        IntStream.rangeClosed(0, 17).forEach(i -> src.put("SRCK" + i, "SRCV" + i));

        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        IntStream.rangeClosed(100, 111).forEach(i -> m.put("K" + i, "V" + i));

        m.putAll(src);
        assertEquals(12 + 18, m.size());

        final Iterator<Entry<String, String>> iter = m.entrySet().iterator();
        IntStream.rangeClosed(100, 111).forEach(i -> {
            assertTrue(iter.hasNext());
            final Entry<String, String> e = iter.next();
            assertEquals("K" + i, e.getKey());
            assertEquals("V" + i, e.getValue());
        });
        assertTrue(iter.hasNext());
        IntStream.rangeClosed(0, 17).forEach(i -> {
            assertTrue(iter.hasNext());
            final Entry<String, String> e = iter.next();
            assertEquals("SRCK" + i, e.getKey());
            assertEquals("SRCV" + i, e.getValue());
        });
        assertFalse(iter.hasNext());
    }

    @Test
    public void putAll_same_type_not_same_object() {
        // same type but not same map
        final OrderedFastHashMap<String, String> src = new OrderedFastHashMap<>();
        IntStream.rangeClosed(19, 99).forEach(i -> src.put("K" + i, "V" + i));

        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        IntStream.rangeClosed(0, 18).forEach(i -> m.put("K" + i, "V" + i));

        m.putAll(src);
        assertEquals(100, m.size());

        final Set<Entry<String, String>> set = m.entrySet();
        final Iterator<Entry<String, String>> iter = set.iterator();
        IntStream.rangeClosed(0, 99).forEach(i -> {
            assertTrue(iter.hasNext());
            final Entry<String, String> e = iter.next();
            assertEquals("K" + i, e.getKey());
            assertEquals("K" + i, e.getKey());
            assertEquals("V" + i, e.getValue());
        });
        assertFalse(iter.hasNext());
    }

    @Test
    public void putAll_to_another_map() {
        // I can be added to other Map.putAll
        final Map<String, String> src = new OrderedFastHashMap<>();
        final Map<String, String> m = new HashMap<>();
        m.putAll(src);
        assertEquals(0, m.size());
    }

    @Test
    public void collision() {
        final OrderedFastHashMap<MockKey<String>, String> f = new OrderedFastHashMap<MockKey<String>, String>(13);
        IntStream.range(0, 15).forEach(i -> {
            f.put(new MockKey<String>(12, "k" + i), "v" + i);
        });

        assertEquals(15, f.size());

        IntStream.range(0, 15).forEach(i -> {
            assertEquals("v" + i, f.get(new MockKey<String>(12, "k" + i)));
        });

        // round 2
        IntStream.range(0, 20).forEach(i -> {
            f.put(new MockKey<String>(12, "k" + i), "v" + i);
        });

        assertEquals(20, f.size());

        IntStream.range(0, 20).forEach(i -> {
            assertEquals("v" + i, f.get(new MockKey<String>(12, "k" + i)));
        });

        // round 3
        IntStream.range(0, 10).forEach(i -> {
            assertEquals("v" + i, f.remove(new MockKey<String>(12, "k" + i)));
        });
        IntStream.range(10, 20).forEach(i -> {
            assertEquals("v" + i, f.get(new MockKey<String>(12, "k" + i)));
        });
    }

    /**
     * Overflow initial size with collision keys. Some hash code for all keys.
     */
    @Test
    public void overflow() {
        final OrderedFastHashMap<MockKey<String>, Integer> m = new OrderedFastHashMap<>(5);
        final Map<MockKey<String>, Integer> data = IntStream.range(0, 152).mapToObj(Integer::valueOf)
                .collect(Collectors.toMap(i -> new MockKey<String>(1, "k" + i), i -> i));

        // add all
        data.forEach((k, v) -> m.put(k, v));

        // verify
        data.forEach((k, v) -> assertEquals(v, m.get(k)));
        assertEquals(152, m.size());
        assertEquals(152, m.keys().size());
        assertEquals(152, m.values().size());
    }

    /**
     * Try to test early growth and potential problems when growing. Based on
     * infinite loop observations.
     */
    @Test
    public void growFromSmall_InfiniteLoopIsssue() {
        for (int initSize = 0; initSize < 100; initSize++) {
            final OrderedFastHashMap<Integer, Integer> m = new OrderedFastHashMap<>(initSize);

            for (int i = 0; i < 300; i++) {
                // add one
                m.put(i, i);

                // ask for all
                for (int j = 0; j <= i; j++) {
                    assertEquals(Integer.valueOf(j), m.get(j));
                }

                // ask for something else
                for (int j = -1; j >= -100; j--) {
                    assertNull(m.get(j));
                }
            }
        }
    }

    /**
     * Try to hit all slots with bad hashcodes.
     */
    @Test
    public void hitEachSlot() {
        final OrderedFastHashMap<MockKey<String>, Integer> m = new OrderedFastHashMap<>(15);

        final Map<MockKey<String>, Integer> data = IntStream.range(0, 150).mapToObj(Integer::valueOf)
                .collect(Collectors.toMap(i -> new MockKey<String>(i, "k1" + i), i -> i));

        // add the same hash codes again but other keys
        data.putAll(IntStream.range(0, 150).mapToObj(Integer::valueOf)
                .collect(Collectors.toMap(i -> new MockKey<String>(i, "k2" + i), i -> i)));
        // add all
        data.forEach((k, v) -> m.put(k, v));
        // verify
        data.forEach((k, v) -> assertEquals(v, m.get(k)));
        assertEquals(300, m.size());
        assertEquals(300, m.keys().size());
        assertEquals(300, m.values().size());

        // remove all
        data.forEach((k, v) -> m.remove(k));
        // verify
        assertEquals(0, m.size());
        assertEquals(0, m.keys().size());
        assertEquals(0, m.values().size());

        // add all
        final List<MockKey<String>> keys = data.keySet().stream().collect(Collectors.toList());
        keys.stream().sorted().forEach(k -> m.put(k, data.get(k)));
        // put in different order
        Collections.shuffle(keys);
        keys.forEach(k -> m.put(k, data.get(k) + 42));

        // verify
        data.forEach((k, v) -> assertEquals(Integer.valueOf(v + 42), m.get(k)));
        assertEquals(300, m.size());
        assertEquals(300, m.keys().size());
        assertEquals(300, m.values().size());

        // remove in different order
        Collections.shuffle(keys);
        keys.forEach(k -> m.remove(k));

        // verify
        data.forEach((k, v) -> assertNull(m.get(k)));
        assertEquals(0, m.size());
        assertEquals(0, m.keys().size());
        assertEquals(0, m.values().size());
    }

    @Test
    public void reverse_empty() {
        // can reverse empty without exception
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.reverse();
    }

    @Test
    public void reverse_zero_sized() {
        // can reverse 0 sized map
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>(0);
        m.reverse();
    }

    @Test
    public void reverse_one() {
        // reverse one entry map
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("k0", "v0");
        m.reverse();
        assertEquals("k0", m.getEntry(0).getKey());
        assertEquals("v0", m.getEntry(0).getValue());
    }

    @Test
    public void reverse_two() {
        // reverse two entries map
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("k0", "v0");
        m.put("k1", "v1");
        m.reverse();
        assertEquals("k1", m.getEntry(0).getKey());
        assertEquals("v1", m.getEntry(0).getValue());
        assertEquals("k0", m.getEntry(1).getKey());
        assertEquals("v0", m.getEntry(1).getValue());
    }

    @Test
    public void reverse_three() {
        // reverse three entries map
        final OrderedFastHashMap<String, String> m = new OrderedFastHashMap<>();
        m.put("k0", "v0");
        m.put("k1", "v1");
        m.put("k2", "v2");
        m.reverse();
        assertEquals("k2", m.getEntry(0).getKey());
        assertEquals("v2", m.getEntry(0).getValue());
        assertEquals("k1", m.getEntry(1).getKey());
        assertEquals("v1", m.getEntry(1).getValue());
        assertEquals("k0", m.getEntry(2).getKey());
        assertEquals("v0", m.getEntry(2).getValue());
    }

    @Test
    public void reverse_many_odd() {
        // many entries, odd
        final OrderedFastHashMap<Integer, Integer> m = new OrderedFastHashMap<>(15);
        IntStream.range(0, 117).mapToObj(Integer::valueOf).forEach(i -> m.put(i, i));
        m.reverse();

        IntStream.range(0, 117).mapToObj(Integer::valueOf).forEach(i -> {
            final OrderedFastHashMap.Entry<Integer, Integer> e = m.getEntry(m.size() - i - 1);
            assertEquals(i, e.getKey());
            assertEquals(i, e.getValue());
        });
    }

    @Test
    public void reverse_many_even() {
        // many entries, even
        final OrderedFastHashMap<Integer, Integer> m = new OrderedFastHashMap<>(15);
        IntStream.range(0, 80).mapToObj(Integer::valueOf).forEach(i -> m.put(i, i));
        m.reverse();

        IntStream.range(0, 80).mapToObj(Integer::valueOf).forEach(i -> {
            final OrderedFastHashMap.Entry<Integer, Integer> e = m.getEntry(m.size() - i - 1);
            assertEquals(i, e.getKey());
            assertEquals(i, e.getValue());
        });
    }

    static class MockKey<T extends Comparable<T>> implements Comparable<MockKey<T>> {
        public final T key_;
        public final int hash_;

        MockKey(final int hash, final T key) {
            this.hash_ = hash;
            this.key_ = key;
        }

        @Override
        public int hashCode() {
            return hash_;
        }

        @Override
        public boolean equals(final Object o) {
            final MockKey<T> t = (MockKey<T>) o;
            return hash_ == o.hashCode() && key_.equals(t.key_);
        }

        @Override
        public String toString() {
            return "MockKey [key=" + key_ + ", hash=" + hash_ + "]";
        }

        @Override
        public int compareTo(final MockKey<T> o) {
            return o.key_.compareTo(this.key_);
        }

    }
}
