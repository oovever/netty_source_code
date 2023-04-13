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

import io.netty.util.collection.IntObjectMap.PrimitiveEntry;

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
 * Tests for {@link IntObjectHashMap}.
 */
public class IntObjectHashMapTest {

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

    private IntObjectHashMap<Value> map;

    @BeforeEach
    public void setup() {
        map = new IntObjectHashMap<Value>();
    }

    @Test
    public void iteratorRemoveShouldNotNPE() {
        map = new IntObjectHashMap<Value>(4, 1);
        map.put((Integer)(int) 0, new Value("A"));
        map.put((Integer)(int) 1, new Value("B"));
        map.put((Integer)(int) 4, new Value("C"));
        map.remove((Integer)(int) 1);
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
        int key = 1;
        assertNull(map.put(key, v));
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(v));
        assertEquals(v, map.get(key));
    }

    @Test
    public void putNewMappingShouldSucceed_mapApi() {
        Value v = new Value("v");
        Integer key = (Integer)(int) 1;
        assertNull(map.put(key, v));
        assertEquals(1, map.size());
        assertTrue(map.containsKey(key));
        assertTrue(map.containsValue(v));
        assertEquals(v, map.get(key));
    }

    @Test
    public void putShouldReplaceValue() {
        Value v1 = new Value("v1");
        int key = 1;
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
        Integer key = (Integer)(int) 1;
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
        for (int key = 0; key < (int) 255; ++key) {
            Value v = new Value(Integer.toString(key));
            assertNull(map.put(key, v));
            assertEquals(key + 1, map.size());
            assertTrue(map.containsKey(key));
            assertTrue(map.containsValue(v));
            assertEquals(v, map.get(key));
        }
    }

    @Test
    public void putShouldGrowMap_mapApi() {
        for (int key = 0; key < (int) 255; ++key) {
            Integer okey = (Integer) key;
            Value v = new Value(Integer.toString(key));
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
        map.put((int) -3, v);
        assertEquals(1, map.size());
        assertEquals(v, map.get((int) -3));
    }

    @Test
    public void negativeKeyShouldSucceed_mapApi() {
        Value v = new Value("v");
        map.put((Integer)(int) -3, v);
        assertEquals(1, map.size());
        assertEquals(v, map.get((Integer)(int) -3));
    }

    @Test
    public void removeMissingValueShouldReturnNull() {
        assertNull(map.remove((int) 1));
        assertEquals(0, map.size());
    }

    @Test
    public void removeMissingValueShouldReturnNull_mapApi() {
        assertNull(map.remove((Integer)(int) 1));
        assertEquals(0, map.size());
    }

    @Test
    public void removeShouldReturnPreviousValue() {
        Value v = new Value("v");
        int key = 1;
        map.put(key, v);
        assertSame(v, map.remove(key));
    }

    @Test
    public void removeShouldReturnPreviousValue_mapApi() {
        Value v = new Value("v");
        Integer key = (Integer)(int) 1;
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
        for (int i = 0; i < 10; ++i) {
            map.put(i, new Value(Integer.toString(i)));
            // Now mark it as REMOVED so that size won't cause the rehash.
            map.remove(i);
            assertEquals(0, map.size());
        }

        // Now add an entry to force the rehash since no FREE slots are available in the map.
        Value v = new Value("v");
        int key = 1;
        map.put(key, v);
        assertEquals(1, map.size());
        assertSame(v, map.get(key));
    }

    @Test
    public void noFreeSlotsShouldRehash_mapApi() {
        for (int i = 0; i < 10; ++i) {
            map.put(i, new Value(Integer.toString(i)));
            // Now mark it as REMOVED so that size won't cause the rehash.
            map.remove((Integer) i);
            assertEquals(0, map.size());
        }

        // Now add an entry to force the rehash since no FREE slots are available in the map.
        Value v = new Value("v");
        Integer key = (Integer)(int) 1;
        map.put(key, v);
        assertEquals(1, map.size());
        assertSame(v, map.get(key));
    }

    @Test
    public void putAllShouldSucceed() {
        IntObjectHashMap<Value> other = new IntObjectHashMap<Value>();

        int k1 = 1;
        int k2 = 2;
        int k3 = 3;
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
        IntObjectHashMap<Value> other = new IntObjectHashMap<Value>();

        Integer k1 = (Integer)(int) 1;
        Integer k2 = (Integer)(int) 2;
        Integer k3 = (Integer)(int) 3;
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
        Map<Integer, Value> other = new HashMap<Integer, Value>();

        Integer k1 = (Integer)(int) 1;
        Integer k2 = (Integer)(int) 2;
        Integer k3 = (Integer)(int) 3;
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
        map.put((int) 1, v1);
        map.put((int) 2, v2);
        map.put((int) 3, v3);
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void containsValueShouldFindNull() {
        map.put((int) 1, new Value("v1"));
        map.put((int) 2, null);
        map.put((int) 3, new Value("v2"));
        assertTrue(map.containsValue(null));
    }

    @Test
    public void containsValueShouldFindNull_mapApi() {
        map.put((Integer)(int) 1, new Value("v1"));
        map.put((Integer)(int) 2, null);
        map.put((Integer)(int) 3, new Value("v2"));
        assertTrue(map.containsValue(null));
    }

    @Test
    public void containsValueShouldFindInstance() {
        Value v = new Value("v1");
        map.put((int) 1, new Value("v2"));
        map.put((int) 2, new Value("v3"));
        map.put((int) 3, v);
        assertTrue(map.containsValue(v));
    }

    @Test
    public void containsValueShouldFindInstance_mapApi() {
        Value v = new Value("v1");
        map.put((Integer)(int) 1, new Value("v2"));
        map.put((Integer)(int) 2, new Value("v3"));
        map.put((Integer)(int) 3, v);
        assertTrue(map.containsValue(v));
    }

    @Test
    public void containsValueShouldFindEquivalentValue() {
        map.put((int) 1, new Value("v1"));
        map.put((int) 2, new Value("v2"));
        map.put((int) 3, new Value("v3"));
        assertTrue(map.containsValue(new Value("v2")));
    }

    @Test
    public void containsValueShouldFindEquivalentValue_mapApi() {
        map.put((Integer)(int) 1, new Value("v1"));
        map.put((Integer)(int) 2, new Value("v2"));
        map.put((Integer)(int) 3, new Value("v3"));
        assertTrue(map.containsValue(new Value("v2")));
    }

    @Test
    public void containsValueNotFindMissingValue() {
        map.put((int) 1, new Value("v1"));
        map.put((int) 2, new Value("v2"));
        map.put((int) 3, new Value("v3"));
        assertFalse(map.containsValue(new Value("v4")));
    }

    @Test
    public void containsValueNotFindMissingValue_mapApi() {
        map.put((Integer)(int) 1, new Value("v1"));
        map.put((Integer)(int) 2, new Value("v2"));
        map.put((Integer)(int) 3, new Value("v3"));
        assertFalse(map.containsValue(new Value("v4")));
    }

    @Test
    public void iteratorShouldTraverseEntries() {
        int k1 = 1;
        int k2 = 2;
        int k3 = 3;
        int k4 = 4;
        map.put(k1, new Value("v1"));
        map.put(k2, new Value("v2"));
        map.put(k3, new Value("v3"));

        // Add and then immediately remove another entry.
        map.put(k4, new Value("v4"));
        map.remove(k4);

        Set<Integer> found = new HashSet<Integer>();
        for (IntObjectMap.Entry<Integer, Value> entry : map.entrySet()) {
            assertTrue(found.add(entry.getKey()));
        }
        assertEquals(3, found.size());
        assertTrue(found.contains(k1));
        assertTrue(found.contains(k2));
        assertTrue(found.contains(k3));
    }

    @Test
    public void keysShouldBeReturned() {
        int k1 = 1;
        int k2 = 2;
        int k3 = 3;
        int k4 = 4;
        map.put(k1, new Value("v1"));
        map.put(k2, new Value("v2"));
        map.put(k3, new Value("v3"));

        // Add and then immediately remove another entry.
        map.put(k4, new Value("v4"));
        map.remove(k4);

        Set<Integer> keys = map.keySet();
        assertEquals(3, keys.size());

        Set<Integer> expected = new HashSet<Integer>();
        expected.add(k1);
        expected.add(k2);
        expected.add(k3);

        Set<Integer> found = new HashSet<Integer>();
        for (int key : keys) {
            assertTrue(found.add(key));
        }
        assertEquals(expected, found);
    }

    @Test
    public void valuesShouldBeReturned() {
        int k1 = 1;
        int k2 = 2;
        int k3 = 3;
        int k4 = 4;
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
                IntObjectHashMap<String> map = new IntObjectHashMap<String>(sz);
                for (int i = 0; i < 100; ++i) {
                    map.put((int)(i * mod), "");
                }
            }
        }
    }

    @Test
    public void mapShouldSupportHashingConflicts_mapApi() {
        for (int mod = 0; mod < 10; ++mod) {
            for (int sz = 1; sz <= 101; sz += 2) {
                IntObjectHashMap<String> map = new IntObjectHashMap<String>(sz);
                for (int i = 0; i < 100; ++i) {
                    map.put((Integer)(int)(i * mod), "");
                }
            }
        }
    }

    @Test
    public void hashcodeEqualsTest() {
        IntObjectHashMap<Integer> map1 = new IntObjectHashMap<Integer>();
        IntObjectHashMap<Integer> map2 = new IntObjectHashMap<Integer>();
        Random rnd = new Random(0);
        while (map1.size() < 100) {
            int key = (int) rnd.nextInt(100);
            map1.put(key, Integer.valueOf(key));
            map2.put(key, Integer.valueOf(key));
        }
        assertEquals(map1.hashCode(), map2.hashCode());
        assertEquals(map1, map2);
        // Remove one "middle" element, maps should now be non-equals.
        Set<Integer> keys = map1.keySet();
        Integer removed = null;
        Iterator<Integer> iter = keys.iterator();
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
        map2.put((int) 100, (Integer)(int) 100);
        assertFalse(map1.equals(map2));
        // Rebuild map2 with elements in a different order, again the maps should be equal.
        // (These tests with same elements in different order also show that the hashCode
        // function does not depend on the internal ordering of entries.)
        map2.clear();
        for (Integer key : map1.keySet()) {
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
        IntObjectHashMap<Integer> map = new IntObjectHashMap<Integer>(startTableSize);
        // Reference map which implementation we trust to be correct, will mirror all operations.
        HashMap<Integer, Integer> goodMap = new HashMap<Integer, Integer>();

        // Add initial population.
        for (int i = 0; i < baseSize / 4; ++i) {
            int key = (int) rnd.nextInt(baseSize);
            assertEquals(goodMap.put(key, Integer.valueOf(key)), map.put(key, Integer.valueOf(key)));
            // 50% elements are multiple of a divisor of startTableSize => more conflicts.
            key = (int) (rnd.nextInt(baseSize) * 17);
            assertEquals(goodMap.put(key, Integer.valueOf(key)), map.put(key, Integer.valueOf(key)));
        }

        // Now do some mixed adds and removes for further fuzzing
        // Rehash will happen here, but only once, and the final size will be closer to max.
        for (int i = 0; i < baseSize * 1000; ++i) {
            int key = (int) rnd.nextInt(baseSize);
            if (rnd.nextDouble() >= 0.2) {
                assertEquals(goodMap.put(key, Integer.valueOf(key)), map.put(key, Integer.valueOf(key)));
            } else {
                assertEquals(goodMap.remove(key), map.remove(key));
            }
        }

        // Final batch of fuzzing, only searches and removes.
        int removeSize = map.size() / 2;
        while (removeSize > 0) {
            int key = (int) rnd.nextInt(baseSize);
            boolean found = goodMap.containsKey(key);
            assertEquals(found, map.containsKey(key));
            assertEquals(goodMap.remove(key), map.remove(key));
            if (found) {
                --removeSize;
            }
        }

        // Now gotta write some code to compare the final maps, as equals() won't work.
        assertEquals(goodMap.size(), map.size());
        Integer[] goodKeys = goodMap.keySet().toArray(new Integer[goodMap.size()]);
        Arrays.sort(goodKeys);
        Integer[] keys = map.keySet().toArray(new Integer[map.size()]);
        Arrays.sort(keys);
        for (int i = 0; i < goodKeys.length; ++i) {
            assertEquals(goodKeys[i], keys[i]);
        }

        // Finally drain the map.
        for (int key : keys) {
            assertEquals(goodMap.remove(key), map.remove(key));
        }
        assertTrue(map.isEmpty());
    }

    @Test
    public void valuesIteratorRemove() {
        Value v1 = new Value("v1");
        Value v2 = new Value("v2");
        Value v3 = new Value("v3");
        map.put((int) 1, v1);
        map.put((int) 2, v2);
        map.put((int) 3, v3);

        Iterator<Value> it = map.values().iterator();

        assertSame(v1, it.next());
        assertSame(v2, it.next());
        it.remove();

        assertSame(v3, it.next());
        assertFalse(it.hasNext());

        assertEquals(2, map.size());
        assertSame(v1, map.get((int) 1));
        assertNull(map.get((int) 2));
        assertSame(v3, map.get((int) 3));

        it = map.values().iterator();

        assertSame(v1, it.next());
        assertSame(v3, it.next());
        assertFalse(it.hasNext());
    }
}
