/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License, version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.netty.util.collection;

import io.netty.util.collection.ShortObjectMap.PrimitiveEntry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests for {@link ShortObjectHashMap}.
 */
public class ShortObjectHashMapTest {

    private static class Value {
        private final String name;

        Value(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (name == null ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Value other = (Value) obj;
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            return true;
        }
    }

    private ShortObjectHashMap<Value> map;

    @BeforeEach
    public void setup() {
        map = new ShortObjectHashMap<Value>();
    }

    @Test
    public void iteratorRemoveShouldNotNPE() {
        map = new ShortObjectHashMap<Value>(4, 1);
        map.put((Short)(short) 0, new Value("A"));
        map.put((Short)(short) 1, new Value("B"));
        map.put((Short)(short) 4, new Value("C"));
        map.remove((Short)(short) 1);
        Iterator<PrimitiveEntry<Value>> itr = map.entries().iterator();
        while (itr.hasNext()) {
            PrimitiveEntry<Value> entry = itr.next();
            assertNotNull(entry.key());
            assertNotNull(entry.value());
            itr.remove();
        }
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void putNewMappingShouldSucceed() {
        Value v = new Value("v");
        short key = 1;
        assertNull(map.put(key, v));
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(v));
        assertEquals(v, map.get(key));
    }

    @Test
    public void putNewMappingShouldSucceed_mapApi() {
        Value v = new Value("v");
        Short key = (Short)(short) 1;
        assertNull(map.put(key, v));
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(v));
        assertEquals(v, map.get(key));
    }

    @Test
    public void putShouldReplaceValue() {
        Value v1 = new Value("v1");
        short key = 1;
        assertNull(map.put(key, v1));

        // Replace the value.
        Value v2 = new Value("v2");
        assertSame(v1, map.put(key, v2));

        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(v2));
        assertEquals(v2, map.get(key));
    }

    @Test
    public void putShouldReplaceValue_mapApi() {
        Value v1 = new Value("v1");
        Short key = (Short)(short) 1;
        assertNull(map.put(key, v1));

        // Replace the value.
        Value v2 = new Value("v2");
        assertSame(v1, map.put(key, v2));

        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(v2));
        assertEquals(v2, map.get(key));
    }

    @Test
    public void putShouldGrowMap() {
        for (short key = 0; key < (short) 255; ++key) {
            Value v = new Value(Short.toString(key));
            assertNull(map.put(key, v));
            assertEquals(key + 1, map.size());
            assertTrue(map.containsKey(key));
            assertTrue(map.containsValue(v));
            assertEquals(v, map.get(key));
        }
    }

    @Test
    public void putShouldGrowMap_mapApi() {
        for (short key = 0; key < (short) 255; ++key) {
            Short okey = (Short) key;
            Value v = new Value(Short.toString(key));
            assertNull(map.put(okey, v));
            assertEquals(key + 1, map.size());
            assertTrue(map.containsKey(okey));
            assertTrue(map.containsValue(v));
            assertEquals(v, map.get(okey));
        }
    }

    @Test
    public void negativeKeyShouldSucceed() {
        Value v = new Value("v");
        map.put((short) -3, v);
        assertEquals(1, map.size());
        assertEquals(v, map.get((short) -3));
    }

    @Test
    public void negativeKeyShouldSucceed_mapApi() {
        Value v = new Value("v");
        map.put((Short)(short) -3, v);
        assertEquals(1, map.size());
        assertEquals(v, map.get((Short)(short) -3));
    }

    @Test
    public void removeMissingValueShouldReturnNull() {
        assertNull(map.remove((short) 1));
        assertEquals(0, map.size());
    }

    @Test
    public void removeMissingValueShouldReturnNull_mapApi() {
        assertNull(map.remove((Short)(short) 1));
        assertEquals(0, map.size());
    }

    @Test
    public void removeShouldReturnPreviousValue() {
        Value v = new Value("v");
        short key = 1;
        map.put(key, v);
        assertSame(v, map.remove(key));
    }

    @Test
    public void removeShouldReturnPreviousValue_mapApi() {
        Value v = new Value("v");
        Short key = (Short)(short) 1;
        map.put(key, v);
        assertSame(v, map.remove(key));
    }

    /**
     * This test is a bit internal-centric. We're just forcing a rehash to occur based on no longer
     * having any FREE slots available. We do this by adding and then removing several keys up to
     * the capacity, so that no rehash is done. We then add one more, which will cause the rehash
     * due to a lack of free slots and verify that everything is still behaving properly
     */
    @Test
    public void noFreeSlotsShouldRehash() {
        for (short i = 0; i < 10; ++i) {
            map.put(i, new Value(Short.toString(i)));
            // Now mark it as REMOVED so that size won't cause the rehash.
            map.remove(i);
            assertEquals(0, map.size());
        }

        // Now add an entry to force the rehash since no FREE slots are available in the map.
        Value v = new Value("v");
        short key = 1;
        map.put(key, v);
        assertEquals(1, map.size());
        assertSame(v, map.get(key));
    }

    @Test
    public void noFreeSlotsShouldRehash_mapApi() {
        for (short i = 0; i < 10; ++i) {
            map.put(i, new Value(Short.toString(i)));
            // Now mark it as REMOVED so that size won't cause the rehash.
            map.remove((Short) i);
            assertEquals(0, map.size());
        }

        // Now add an entry to force the rehash since no FREE slots are available in the map.
        Value v = new Value("v");
        Short key = (Short)(short) 1;
        map.put(key, v);
        assertEquals(1, map.size());
        assertSame(v, map.get(key));
    }

    @Test
    public void putAllShouldSucceed() {
        ShortObjectHashMap<Value> other = new ShortObjectHashMap<Value>();

        short k1 = 1;
        short k2 = 2;
        short k3 = 3;
        Value v1 = new Value("v1");
        Value v2 = new Value("v2");
        Value v3 = new Value("v3");
        other.put(k1, v1);
        other.put(k2, v2);
        other.put(k3, v3);

        map.putAll(other);
        assertEquals(3, map.size());
        assertSame(v1, map.get(k1));
        assertSame(v2, map.get(k2));
        assertSame(v3, map.get(k3));
    }

    @Test
    public void putAllShouldSucceed_mapApi() {
        ShortObjectHashMap<Value> other = new ShortObjectHashMap<Value>();

        Short k1 = (Short)(short) 1;
        Short k2 = (Short)(short) 2;
        Short k3 = (Short)(short) 3;
        Value v1 = new Value("v1");
        Value v2 = new Value("v2");
        Value v3 = new Value("v3");
        other.put(k1, v1);
        other.put(k2, v2);
        other.put(k3, v3);

        map.putAll(other);
        assertEquals(3, map.size());
        assertSame(v1, map.get(k1));
        assertSame(v2, map.get(k2));
        assertSame(v3, map.get(k3));
    }

    @Test
    public void putAllWithJavaMapShouldSucceed_mapApi() {
        Map<Short, Value> other = new HashMap<Short, Value>();

        Short k1 = (Short)(short) 1;
        Short k2 = (Short)(short) 2;
        Short k3 = (Short)(short) 3;
        Value v1 = new Value("v1");
        Value v2 = new Value("v2");
        Value v3 = new Value("v3");
        other.put(k1, v1);
        other.put(k2, v2);
        other.put(k3, v3);

        map.putAll(other);
        assertEquals(3, map.size());
        assertSame(v1, map.get(k1));
        assertSame(v2, map.get(k2));
        assertSame(v3, map.get(k3));
    }

    @Test
    public void clearShouldSucceed() {
        Value v1 = new Value("v1");
        Value v2 = new Value("v2");
        Value v3 = new Value("v3");
        map.put((short) 1, v1);
        map.put((short) 2, v2);
        map.put((short) 3, v3);
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void containsValueShouldFindNull() {
        map.put((short) 1, new Value("v1"));
        map.put((short) 2, null);
        map.put((short) 3, new Value("v2"));
        assertTrue(map.containsValue(null));
    }

    @Test
    public void containsValueShouldFindNull_mapApi() {
        map.put((Short)(short) 1, new Value("v1"));
        map.put((Short)(short) 2, null);
        map.put((Short)(short) 3, new Value("v2"));
        assertTrue(map.containsValue(null));
    }

    @Test
    public void containsValueShouldFindInstance() {
        Value v = new Value("v1");
        map.put((short) 1, new Value("v2"));
        map.put((short) 2, new Value("v3"));
        map.put((short) 3, v);
        assertTrue(map.containsValue(v));
    }

    @Test
    public void containsValueShouldFindInstance_mapApi() {
        Value v = new Value("v1");
        map.put((Short)(short) 1, new Value("v2"));
        map.put((Short)(short) 2, new Value("v3"));
        map.put((Short)(short) 3, v);
        assertTrue(map.containsValue(v));
    }

    @Test
    public void containsValueShouldFindEquivalentValue() {
        map.put((short) 1, new Value("v1"));
        map.put((short) 2, new Value("v2"));
        map.put((short) 3, new Value("v3"));
        assertTrue(map.containsValue(new Value("v2")));
    }

    @Test
    public void containsValueShouldFindEquivalentValue_mapApi() {
        map.put((Short)(short) 1, new Value("v1"));
        map.put((Short)(short) 2, new Value("v2"));
        map.put((Short)(short) 3, new Value("v3"));
        assertTrue(map.containsValue(new Value("v2")));
    }

    @Test
    public void containsValueNotFindMissingValue() {
        map.put((short) 1, new Value("v1"));
        map.put((short) 2, new Value("v2"));
        map.put((short) 3, new Value("v3"));
        assertFalse(map.containsValue(new Value("v4")));
    }

    @Test
    public void containsValueNotFindMissingValue_mapApi() {
        map.put((Short)(short) 1, new Value("v1"));
        map.put((Short)(short) 2, new Value("v2"));
        map.put((Short)(short) 3, new Value("v3"));
        assertFalse(map.containsValue(new Value("v4")));
    }

    @Test
    public void iteratorShouldTraverseEntries() {
        short k1 = 1;
        short k2 = 2;
        short k3 = 3;
        short k4 = 4;
        map.put(k1, new Value("v1"));
        map.put(k2, new Value("v2"));
        map.put(k3, new Value("v3"));

        // Add and then immediately remove another entry.
        map.put(k4, new Value("v4"));
        map.remove(k4);

        Set<Short> found = new HashSet<Short>();
        for (ShortObjectMap.Entry<Short, Value> entry : map.entrySet()) {
            assertTrue(found.add(entry.getKey()));
        }
        assertEquals(3, found.size());
        assertTrue(found.contains(k1));
        assertTrue(found.contains(k2));
        assertTrue(found.contains(k3));
    }

    @Test
    public void keysShouldBeReturned() {
        short k1 = 1;
        short k2 = 2;
        short k3 = 3;
        short k4 = 4;
        map.put(k1, new Value("v1"));
        map.put(k2, new Value("v2"));
        map.put(k3, new Value("v3"));

        // Add and then immediately remove another entry.
        map.put(k4, new Value("v4"));
        map.remove(k4);

        Set<Short> keys = map.keySet();
        assertEquals(3, keys.size());

        Set<Short> expected = new HashSet<Short>();
        expected.add(k1);
        expected.add(k2);
        expected.add(k3);

        Set<Short> found = new HashSet<Short>();
        for (short key : keys) {
            assertTrue(found.add(key));
        }
        assertEquals(expected, found);
    }

    @Test
    public void valuesShouldBeReturned() {
        short k1 = 1;
        short k2 = 2;
        short k3 = 3;
        short k4 = 4;
        Value v1 = new Value("v1");
        Value v2 = new Value("v2");
        Value v3 = new Value("v3");
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);

        // Add and then immediately remove another entry.
        map.put(k4, new Value("v4"));
        map.remove(k4);

        // Ensure values() return all values.
        Set<Value> expected = new HashSet<Value>();
        expected.add(v1);
        expected.add(v2);
        expected.add(v3);

        Set<Value> actual = new HashSet<Value>(map.values());
        assertEquals(expected, actual);
    }

    @Test
    public void mapShouldSupportHashingConflicts() {
        for (int mod = 0; mod < 10; ++mod) {
            for (int sz = 1; sz <= 101; sz += 2) {
                ShortObjectHashMap<String> map = new ShortObjectHashMap<String>(sz);
                for (int i = 0; i < 100; ++i) {
                    map.put((short)(i * mod), "");
                }
            }
        }
    }

    @Test
    public void mapShouldSupportHashingConflicts_mapApi() {
        for (int mod = 0; mod < 10; ++mod) {
            for (int sz = 1; sz <= 101; sz += 2) {
                ShortObjectHashMap<String> map = new ShortObjectHashMap<String>(sz);
                for (int i = 0; i < 100; ++i) {
                    map.put((Short)(short)(i * mod), "");
                }
            }
        }
    }

    @Test
    public void hashcodeEqualsTest() {
        ShortObjectHashMap<Short> map1 = new ShortObjectHashMap<Short>();
        ShortObjectHashMap<Short> map2 = new ShortObjectHashMap<Short>();
        Random rnd = new Random(0);
        while (map1.size() < 100) {
            short key = (short) rnd.nextInt(100);
            map1.put(key, Short.valueOf(key));
            map2.put(key, Short.valueOf(key));
        }
        assertEquals(map1.hashCode(), map2.hashCode());
        assertEquals(map1, map2);
        // Remove one "middle" element, maps should now be non-equals.
        Set<Short> keys = map1.keySet();
        Short removed = null;
        Iterator<Short> iter = keys.iterator();
        for (int ix = 0; iter.hasNext() && ix < 50; ++ix) {
            removed = iter.next();
        }
        map2.remove(removed);
        assertFalse(map1.equals(map2));
        // Put it back; will likely be in a different position, but maps will be equal again.
        map2.put(removed, removed);
        assertEquals(map1, map2);
        assertEquals(map1.hashCode(), map2.hashCode());
        // Make map2 have one extra element, will be non-equal.
        map2.put((short) 100, (Short)(short) 100);
        assertFalse(map1.equals(map2));
        // Rebuild map2 with elements in a different order, again the maps should be equal.
        // (These tests with same elements in different order also show that the hashCode
        // function does not depend on the internal ordering of entries.)
        map2.clear();
        for (Short key : map1.keySet()) {
            map2.put(key, key);
        }
        assertEquals(map1.hashCode(), map2.hashCode());
        assertEquals(map1, map2);
    }

    @Test
    public void fuzzTest() {
        // This test is so extremely internals-dependent that I'm not even trying to
        // minimize that. Any internal changes will not fail the test (so it's not flaky per se)
        // but will possibly make it less effective (not test interesting scenarios anymore).

        // The RNG algorithm is specified and stable, so this will cause the same exact dataset
        // to be used in every run and every JVM implementation.
        Random rnd = new Random(0);

        int baseSize = 1000;
        // Empirically-determined size to expand the capacity exactly once, and before
        // the step that creates the long conflict chain. We need to test rehash(),
        // but also control when rehash happens because it cleans up the REMOVED entries.
        // This size is also chosen so after the single rehash, the map will be densely
        // populated, getting close to a second rehash but not triggering it.
        int startTableSize = 1105;
        ShortObjectHashMap<Short> map = new ShortObjectHashMap<Short>(startTableSize);
        // Reference map which implementation we trust to be correct, will mirror all operations.
        HashMap<Short, Short> goodMap = new HashMap<Short, Short>();

        // Add initial population.
        for (int i = 0; i < baseSize / 4; ++i) {
            short key = (short) rnd.nextInt(baseSize);
            assertEquals(goodMap.put(key, Short.valueOf(key)), map.put(key, Short.valueOf(key)));
            // 50% elements are multiple of a divisor of startTableSize => more conflicts.
            key = (short) (rnd.nextInt(baseSize) * 17);
            assertEquals(goodMap.put(key, Short.valueOf(key)), map.put(key, Short.valueOf(key)));
        }

        // Now do some mixed adds and removes for further fuzzing
        // Rehash will happen here, but only once, and the final size will be closer to max.
        for (int i = 0; i < baseSize * 1000; ++i) {
            short key = (short) rnd.nextInt(baseSize);
            if (rnd.nextDouble() >= 0.2) {
                assertEquals(goodMap.put(key, Short.valueOf(key)), map.put(key, Short.valueOf(key)));
            } else {
                assertEquals(goodMap.remove(key), map.remove(key));
            }
        }

        // Final batch of fuzzing, only searches and removes.
        int removeSize = map.size() / 2;
        while (removeSize > 0) {
            short key = (short) rnd.nextInt(baseSize);
            boolean found = goodMap.containsKey(key);
            assertEquals(found, map.containsKey(key));
            assertEquals(goodMap.remove(key), map.remove(key));
            if (found) {
                --removeSize;
            }
        }

        // Now gotta write some code to compare the final maps, as equals() won't work.
        assertEquals(goodMap.size(), map.size());
        Short[] goodKeys = goodMap.keySet().toArray(new Short[goodMap.size()]);
        Arrays.sort(goodKeys);
        Short[] keys = map.keySet().toArray(new Short[map.size()]);
        Arrays.sort(keys);
        for (int i = 0; i < goodKeys.length; ++i) {
            assertEquals(goodKeys[i], keys[i]);
        }

        // Finally drain the map.
        for (short key : keys) {
            assertEquals(goodMap.remove(key), map.remove(key));
        }
        assertTrue(map.isEmpty());
    }

    @Test
    public void valuesIteratorRemove() {
        Value v1 = new Value("v1");
        Value v2 = new Value("v2");
        Value v3 = new Value("v3");
        map.put((short) 1, v1);
        map.put((short) 2, v2);
        map.put((short) 3, v3);

        Iterator<Value> it = map.values().iterator();

        assertSame(v1, it.next());
        assertSame(v2, it.next());
        it.remove();

        assertSame(v3, it.next());
        assertFalse(it.hasNext());

        assertEquals(2, map.size());
        assertSame(v1, map.get((short) 1));
        assertNull(map.get((short) 2));
        assertSame(v3, map.get((short) 3));

        it = map.values().iterator();

        assertSame(v1, it.next());
        assertSame(v3, it.next());
        assertFalse(it.hasNext());
    }
}
