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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * Simple and efficient linked map or better ordered map implementation to
 * replace the default linked list which is heavy.
 *
 * This map does not support null and it is not thread-safe. It implements the
 * map interface but only for compatibility reason in the sense of replacing a
 * regular map. Iterator and streaming methods are either not implemented or
 * less efficient.
 *
 * It goes the extra mile to avoid the overhead of wrapper objects.
 *
 * Because you typically know what you do, we run minimal index checks only and
 * rely on the default exceptions by Java. Why should we do things twice?
 *
 * Important Note: This is meant for small maps because to save on memory
 * allocation and churn, we are not keeping a wrapper for a reference from the
 * map to the list, only from the list to the map. Hence when you remove a key,
 * we have to iterate the entire list. Mostly, half of it most likely, but still
 * expensive enough. When you have something small like 10 to 20 entries, this
 * won't matter that much especially when a remove might be a rare event.
 *
 * This is based on FashHashMap from XLT which is based on a version from:
 * https://github.com/mikvor/hashmapTest/blob/master/src/main/java/map/objobj/ObjObjMap.java
 * No concrete license specified at the source. The project is public domain.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 *
 * @author Ren&eacute; Schwietzke
 */
public class OrderedFastHashMap<K, V> implements Map<K, V>, Serializable {
    // our placeholders in the map
    private static Object FREE_KEY_ = null;
    private static Object REMOVED_KEY_ = new Object();

    // Fill factor, must be between (0 and 1)
    private static final double FILLFACTOR_ = 0.7d;

    // The map with the key value pairs */
    private Object[] mapData_;

    // We will resize a map once it reaches this size
    private int mapThreshold_;

    // Current map size
    private int mapSize_;

    // the list to impose order, the list refers to the key and value
    // position in the map, hence needs an update every time the
    // map sees an update (in regards to positions).
    private int[] orderedList_;

    // the size of the orderedList, in case we proactivly sized
    // it larger
    private int orderedListSize_;

    /**
     * Default constructor which create an ordered map with default size.
     */
    public OrderedFastHashMap() {
        this(8);
    }

    /**
     * Custom constructor to get a map with a custom size and fill factor. We are
     * not spending time on range checks, rather use a default if things are wrong.
     *
     * @param size the size to use, must 0 or positive, negative values default to 0
     */
    public OrderedFastHashMap(final int size) {
        if (size > 0) {
            final int capacity = arraySize(size, FILLFACTOR_);

            this.mapData_ = new Object[capacity << 1];
            this.mapThreshold_ = (int) (capacity * FILLFACTOR_);

            this.orderedList_ = new int[capacity];
        }
        else {
            this.mapData_ = new Object[0];
            this.mapThreshold_ = 0;

            this.orderedList_ = new int[0];
        }
    }

    /**
     * Get a value for a key, any key type is permitted due to
     * the nature of the Map interface.
     *
     * @param key the key
     * @return the value or null, if the key does not exist
     */
    @Override
    public V get(final Object key) {
        final int length = this.mapData_.length;

        // nothing in it
        if (length == 0) {
            return null;
        }

        int ptr = (key.hashCode() & ((length >> 1) - 1)) << 1;
        Object k = mapData_[ptr];

        if (k == FREE_KEY_) {
            return null; // end of chain already
        }

        // we checked FREE
        if (k.hashCode() == key.hashCode() && k.equals(key)) {
            return (V) this.mapData_[ptr + 1];
        }

        // we have not found it, search longer
        final int originalPtr = ptr;
        while (true) {
            ptr = (ptr + 2) & (length - 1); // that's next index

            // if we searched the entire array, we can stop
            if (originalPtr == ptr) {
                return null;
            }

            k = this.mapData_[ptr];

            if (k == FREE_KEY_) {
                return null;
            }

            if (k != REMOVED_KEY_) {
                if (k.hashCode() == key.hashCode() && k.equals(key)) {
                    return (V) this.mapData_[ptr + 1];
                }
            }
        }
    }

    /**
     * Adds a key and value to the internal position structure
     *
     * @param key the key
     * @param value the value to store
     * @param listPosition defines where to add the new key/value pair
     *
     * @return the old value or null if they key was not known before
     */
    private V put(final K key, final V value, final Position listPosition) {
        if (mapSize_ >= mapThreshold_) {
            rehash(this.mapData_.length == 0 ? 4 : this.mapData_.length << 1);
        }

        int ptr = getStartIndex(key) << 1;
        Object k = mapData_[ptr];

        if (k == FREE_KEY_) {
            // end of chain already
            mapData_[ptr] = key;
            mapData_[ptr + 1] = value;

            // ok, remember position, it is a new entry
            orderedListAdd(listPosition, ptr);

            mapSize_++;

            return null;
        }
        else if (k.equals(key)) {
            // we check FREE and REMOVED prior to this call
            final Object ret = mapData_[ptr + 1];
            mapData_[ptr + 1] = value;

            /// existing entry, no need to update the position

            return (V) ret;
        }

        int firstRemoved = -1;
        if (k == REMOVED_KEY_) {
            firstRemoved = ptr; // we may find a key later
        }

        while (true) {
            ptr = (ptr + 2) & (this.mapData_.length - 1); // that's next index calculation
            k = mapData_[ptr];

            if (k == FREE_KEY_) {
                if (firstRemoved != -1) {
                    ptr = firstRemoved;
                }
                mapData_[ptr] = key;
                mapData_[ptr + 1] = value;

                // ok, remember position, it is a new entry
                orderedListAdd(listPosition, ptr);

                mapSize_++;

                return null;
            }
            else if (k.equals(key)) {
                final Object ret = mapData_[ptr + 1];
                mapData_[ptr + 1] = value;

                // same key, different value, this does not change the order

                return (V) ret;
            }
            else if (k == REMOVED_KEY_) {
                if (firstRemoved == -1) {
                    firstRemoved = ptr;
                }
            }
        }
    }

    /**
     * Remove a key from the map. Returns the stored value or
     * null of the key is not known.
     *
     * @param key the key to remove
     * @return the stored value or null if the key does not exist
     */
    @Override
    public V remove(final Object key) {
        final int length = this.mapData_.length;
        // it is empty
        if (length == 0) {
            return null;
        }

        int ptr = getStartIndex(key) << 1;
        Object k = this.mapData_[ptr];

        if (k == FREE_KEY_) {
            return null; // end of chain already
        }
        else if (k.equals(key)) {
            // we check FREE and REMOVED prior to this call
            this.mapSize_--;

            if (this.mapData_[(ptr + 2) & (length - 1)] == FREE_KEY_) {
                this.mapData_[ptr] = FREE_KEY_;
            }
            else {
                this.mapData_[ptr] = REMOVED_KEY_;
            }

            final V ret = (V) this.mapData_[ptr + 1];
            this.mapData_[ptr + 1] = null;

            // take this out of the list
            orderedListRemove(ptr);

            return ret;
        }

        while (true) {
            ptr = (ptr + 2) & (length - 1); // that's next index calculation
            k = this.mapData_[ptr];

            if (k == FREE_KEY_) {
                return null;
            }
            else if (k.equals(key)) {
                this.mapSize_--;
                if (this.mapData_[(ptr + 2) & (length - 1)] == FREE_KEY_) {
                    this.mapData_[ptr] = FREE_KEY_;
                }
                else {
                    this.mapData_[ptr] = REMOVED_KEY_;
                }

                final V ret = (V) this.mapData_[ptr + 1];
                this.mapData_[ptr + 1] = null;

                // take this out of the list
                orderedListRemove(ptr);

                return ret;
            }
        }
    }

    /**
     * Returns the size of the map, effectively the number of entries.
     *
     * @return the size of the map
     */
    public int size() {
        return mapSize_;
    }

    /**
     * Rehash the map.
     *
     * @param newCapacity the new size of the map
     */
    private void rehash(final int newCapacity) {
        this.mapThreshold_ = (int) (newCapacity / 2 * FILLFACTOR_);

        final Object[] oldData = this.mapData_;

        this.mapData_ = new Object[newCapacity];

        // we just have to grow it and not touch it at all after that,
        // just use it as source for the new map via the old
        final int[] oldOrderedList = this.orderedList_;
        final int oldOrderedListSize = this.orderedListSize_;
        this.orderedList_ = new int[newCapacity];

        this.mapSize_ = 0;
        this.orderedListSize_ = 0;

        // we use our ordered list as source and the old
        // array as reference
        // we basically rebuild the map and the ordering
        // from scratch
        for (int i = 0; i < oldOrderedListSize; i++) {
            final int pos = oldOrderedList[i];

            // get us the old data
            final K key = (K) oldData[pos];
            final V value = (V) oldData[pos + 1];

            // write the old to the new map without updating
            // the positioning
            put(key, value, Position.LAST);
        }
    }

    /**
     * Returns a list of all keys in order of addition.
     * This is an expensive operation, because we get a static
     * list back that is not backed by the implementation. Changes
     * to the returned list are not reflected in the map.
     *
     * @return a list of keys as inserted into the map
     */
    public List<K> keys() {
        final List<K> result = new ArrayList<>(this.orderedListSize_);

        for (int i = 0; i < this.orderedListSize_; i++) {
            final int pos = this.orderedList_[i];
            final Object o = this.mapData_[pos];
            result.add((K) o);
        }

        return result;
    }

    /**
     * Returns a list of all values ordered by when the key was
     * added. This is an expensive operation, because we get a static
     * list back that is not backed by the implementation. Changes
     * to the returned list are not reflected in the map.
     *
     * @return a list of values
     */
    public List<V> values() {
        final List<V> result = new ArrayList<>(this.orderedListSize_);

        for (int i = 0; i < this.orderedListSize_; i++) {
            final int pos = this.orderedList_[i];
            final Object o = this.mapData_[pos + 1];
            result.add((V) o);
        }

        return result;
    }

    /**
     * Clears the map, reuses the data structure by clearing it out. It won't shrink
     * the underlying arrays!
     */
    public void clear() {
        this.mapSize_ = 0;
        this.orderedListSize_ = 0;
        Arrays.fill(this.mapData_, FREE_KEY_);
        // Arrays.fill(this.orderedList, 0);
    }

    /**
     * Get us the start index from where we search or insert into the map
     *
     * @param key the key to calculate the position for
     * @return the start position
     */
    private int getStartIndex(final Object key) {
        // key is not null here
        return key.hashCode() & ((this.mapData_.length >> 1) - 1);
    }

    /**
     * Return the least power of two greater than or equal to the specified value.
     *
     * <p>
     * Note that this function will return 1 when the argument is 0.
     *
     * @param x a long integer smaller than or equal to 2<sup>62</sup>.
     * @return the least power of two greater than or equal to the specified value.
     */
    private static long nextPowerOfTwo(final long x) {
        if (x == 0) {
            return 1;
        }

        long r = x - 1;
        r |= r >> 1;
        r |= r >> 2;
        r |= r >> 4;
        r |= r >> 8;
        r |= r >> 16;

        return (r | r >> 32) + 1;
    }

    /**
     * Returns the least power of two smaller than or equal to 2<sup>30</sup> and
     * larger than or equal to <code>Math.ceil( expected / f )</code>.
     *
     * @param expected the expected number of elements in a hash table.
     * @param f        the load factor.
     * @return the minimum possible size for a backing array.
     * @throws IllegalArgumentException if the necessary size is larger than
     *                                  2<sup>30</sup>.
     */
    private static int arraySize(final int expected, final double f) {
        final long s = Math.max(2, nextPowerOfTwo((long) Math.ceil(expected / f)));

        if (s > (1 << 30)) {
            throw new IllegalArgumentException(
                    "Too large (" + expected + " expected elements with load factor " + f + ")");
        }
        return (int) s;
    }

    /**
     * Returns an entry consisting of key and value at a given position.
     * This position relates to the ordered key list that maintain the
     * addition order for this map.
     *
     * @param index the position to fetch
     * @return an entry of key and value
     * @throws IndexOutOfBoundsException when the ask for the position is invalid
     */
    public Entry<K, V> getEntry(final int index) {
        if (index < 0 || index >= this.orderedListSize_) {
            throw new IndexOutOfBoundsException(String.format("Index: %s, Size: %s", index, this.orderedListSize_));
        }

        final int pos = this.orderedList_[index];
        return new Entry(this.mapData_[pos], this.mapData_[pos + 1]);
    }

    /**
     * Returns the key at a certain position of the ordered list that
     * keeps the addition order of this map.
     *
     * @param index the position to fetch
     * @return the key at this position
     * @throws IndexOutOfBoundsException when the ask for the position is invalid
     */
    public K getKey(final int index) {
        if (index < 0 || index >= this.orderedListSize_) {
            throw new IndexOutOfBoundsException(String.format("Index: %s, Size: %s", index, this.orderedListSize_));
        }

        final int pos = this.orderedList_[index];
        return (K) this.mapData_[pos];
    }

    /**
     * Returns the value at a certain position of the ordered list that
     * keeps the addition order of this map.
     *
     * @param index the position to fetch
     * @return the value at this position
     * @throws IndexOutOfBoundsException when the ask for the position is invalid
     */
    public V getValue(final int index) {
        if (index < 0 || index >= this.orderedListSize_) {
            throw new IndexOutOfBoundsException(String.format("Index: %s, Size: %s", index, this.orderedListSize_));
        }

        final int pos = this.orderedList_[index];
        return (V) this.mapData_[pos + 1];
    }

    /**
     * Removes a key and value from this map based on the position
     * in the backing list, rather by key as usual.
     *
     * @param index the position to remove the data from
     * @return the value stored
     * @throws IndexOutOfBoundsException when the ask for the position is invalid
     */
    public V remove(final int index) {
        if (index < 0 || index >= this.orderedListSize_) {
            throw new IndexOutOfBoundsException(String.format("Index: %s, Size: %s", index, this.orderedListSize_));
        }

        final int pos = this.orderedList_[index];
        final K key = (K) this.mapData_[pos];

        return remove(key);
    }

    @Override
    public V put(final K key, final V value) {
        return this.put(key, value, Position.LAST);
    }

    public V addFirst(final K key, final V value) {
        return this.put(key, value, Position.FIRST);
    }

    public V add(final K key, final V value) {
        return this.put(key, value, Position.LAST);
    }

    public V addLast(final K key, final V value) {
        return this.put(key, value, Position.LAST);
    }

    public V getFirst() {
        return getValue(0);
    }

    public V getLast() {
        return getValue(this.orderedListSize_ - 1);
    }

    public V removeFirst() {
        if (this.orderedListSize_ > 0) {
            final int pos = this.orderedList_[0];
            final K key = (K) this.mapData_[pos];
            return remove(key);
        }
        else {
            return null;
        }
    }

    public V removeLast() {
        if (this.orderedListSize_ > 0) {
            final int pos = this.orderedList_[this.orderedListSize_ - 1];
            final K key = (K) this.mapData_[pos];
            return remove(key);
        }
        else {
            return null;
        }
    }

    /**
     * Checks of a key is in the map.
     *
     * @param key the key to check
     * @return true of the key is in the map, false otherwise
     */
    @Override
    public boolean containsKey(final Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(final Object value) {
        // that is expensive, we have to iterate everything
        for (int i = 0; i < this.orderedListSize_; i++) {
            final int pos = this.orderedList_[i] + 1;
            final Object v = this.mapData_[pos];

            // do we match?
            if (v == value || v.equals(value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.mapSize_ == 0;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final Set<Map.Entry<K, V>> set = new OrderedEntrySet<>(this);
        return set;
    }

    @Override
    public Set<K> keySet() {
        final Set<K> set = new OrderedKeySet<>(this);
        return set;
    }

    /**
     * Just reverses the ordering of the map as created so far.
     */
    public void reverse() {
        // In-place reversal
        final int to = this.orderedListSize_ / 2;

        for (int i = 0; i < to; i++) {
            // Swapping the elements
            final int j = this.orderedList_[i];
            this.orderedList_[i] = this.orderedList_[this.orderedListSize_ - i - 1];
            this.orderedList_[this.orderedListSize_ - i - 1] = j;
        }
    }

    /**
     * This set does not support any modifications through its interface. All such
     * methods will throw {@link UnsupportedOperationException}.
     */
    static class OrderedEntrySet<K, V> implements Set<Map.Entry<K, V>> {
        private final OrderedFastHashMap<K, V> backingMap_;

        OrderedEntrySet(final OrderedFastHashMap<K, V> backingMap) {
            this.backingMap_ = backingMap;
        }

        @Override
        public int size() {
            return this.backingMap_.size();
        }

        @Override
        public boolean isEmpty() {
            return this.backingMap_.isEmpty();
        }

        @Override
        public boolean contains(final Object o) {
            if (o instanceof Map.Entry) {
                final Map.Entry ose = (Map.Entry) o;
                final Object k = ose.getKey();
                final Object v = ose.getValue();

                final Object value = this.backingMap_.get(k);
                if (value != null) {
                    return v.equals(value);
                }
            }

            return false;
        }

        @Override
        public Object[] toArray() {
            final Object[] array = new Object[this.backingMap_.orderedListSize_];
            return toArray(array);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(final T[] a) {
            final T[] array;
            if (a.length >= this.backingMap_.orderedListSize_) {
                array = a;
            }
            else {
                array = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(),
                        this.backingMap_.orderedListSize_);
            }

            for (int i = 0; i < this.backingMap_.orderedListSize_; i++) {
                array[i] = (T) this.backingMap_.getEntry(i);
            }

            return (T[]) array;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new OrderedEntryIterator();
        }

        @Override
        public boolean add(final Map.Entry<K, V> e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(final Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(final Collection<? extends Map.Entry<K, V>> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        class OrderedEntryIterator implements Iterator<Map.Entry<K, V>> {
            private int pos_ = 0;

            @Override
            public boolean hasNext() {
                return pos_ < backingMap_.orderedListSize_;
            }

            @Override
            public Map.Entry<K, V> next() {
                if (pos_ < backingMap_.orderedListSize_) {
                    return backingMap_.getEntry(pos_++);
                }
                throw new NoSuchElementException();
            }
        }
    }

    static class OrderedKeySet<K, V> implements Set<K> {
        private final OrderedFastHashMap<K, V> backingMap_;

        OrderedKeySet(final OrderedFastHashMap<K, V> backingMap) {
            this.backingMap_ = backingMap;
        }

        @Override
        public int size() {
            return this.backingMap_.size();
        }

        @Override
        public boolean isEmpty() {
            return this.backingMap_.isEmpty();
        }

        @Override
        public boolean contains(final Object o) {
            return this.backingMap_.containsKey(o);
        }

        @Override
        public Object[] toArray() {
            final Object[] array = new Object[this.backingMap_.orderedListSize_];
            return toArray(array);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(final T[] a) {
            final T[] array;

            if (a.length >= this.backingMap_.orderedListSize_) {
                array = a;
            }
            else {
                array = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(),
                        this.backingMap_.orderedListSize_);
            }

            for (int i = 0; i < this.backingMap_.orderedListSize_; i++) {
                array[i] = (T) this.backingMap_.getKey(i);
            }

            return (T[]) array;
        }

        @Override
        public Iterator<K> iterator() {
            return new OrderedKeyIterator();
        }

        class OrderedKeyIterator implements Iterator<K> {
            private int pos_ = 0;

            @Override
            public boolean hasNext() {
                return this.pos_ < backingMap_.orderedListSize_;
            }

            @Override
            public K next() {
                if (this.pos_ < backingMap_.orderedListSize_) {
                    return backingMap_.getKey(this.pos_++);
                }
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean add(final K e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(final Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> src) {
        if (src == this) {
            throw new IllegalArgumentException("Cannot add myself");
        }

        for (final Map.Entry<? extends K, ? extends V> entry : src.entrySet()) {
            put(entry.getKey(), entry.getValue(), Position.LAST);
        }
    }

    private void orderedListAdd(final Position listPosition, final int position) {
        // the list should still have room, otherwise the map was
        // grown already and the ordering list with it
        if (listPosition == Position.FIRST) {
            System.arraycopy(this.orderedList_, 0, this.orderedList_, 1, this.orderedList_.length - 1);
            this.orderedList_[0] = position;
            this.orderedListSize_++;
        }
        else if (listPosition == Position.LAST) {
            this.orderedList_[this.orderedListSize_] = position;
            this.orderedListSize_++;
        }
        else {
            // if none, we are rebuilding the map and don't have to do a thing
        }
    }

    private void orderedListRemove(final int position) {
        // find the positional information
        int i = 0;
        for ( ; i < this.orderedListSize_; i++) {
            if (this.orderedList_[i] == position) {
                this.orderedList_[i] = -1;
                if (i < this.orderedListSize_) {
                    // not the last element, compact
                    System.arraycopy(this.orderedList_, i + 1, this.orderedList_, i, this.orderedListSize_ - i);
                }
                this.orderedListSize_--;

                return;
            }
        }

        if (i == this.orderedListSize_) {
            throw new IllegalArgumentException(String.format("Position %s was not in order list", position));
        }
    }

    @Override
    public String toString() {
        final int maxLen = 10;

        return String.format(
                "mapData=%s, mapFillFactor=%s, mapThreshold=%s, mapSize=%s,%norderedList=%s, orderedListSize=%s",
                mapData_ != null ? Arrays.asList(mapData_).subList(0, Math.min(mapData_.length, maxLen)) : null,
                        FILLFACTOR_, mapThreshold_, mapSize_,
                        orderedList_ != null
                        ? Arrays.toString(Arrays.copyOf(orderedList_, Math.min(orderedList_.length, maxLen)))
                                : null,
                                orderedListSize_);
    }

    /**
     * Helper for identifying if we need to position our new entry differently.
     */
    private enum Position {
        NONE, FIRST, LAST
    }

    /**
     * Well, we need that to satisfy the map implementation concept.
     *
     * @param <K> the key
     * @param <V> the value
     */
    static class Entry<K, V> implements Map.Entry<K, V> {
        private final K key_;
        private final V value_;

        Entry(final K key, final V value) {
            this.key_ = key;
            this.value_ = value;
        }

        @Override
        public K getKey() {
            return key_;
        }

        @Override
        public V getValue() {
            return value_;
        }

        @Override
        public V setValue(final V value) {
            throw new UnsupportedOperationException("This map does not support write-through via an entry");
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key_) ^ Objects.hashCode(value_);
        }

        @Override
        public String toString() {
            return key_ + "=" + value_;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }

            if (o instanceof Map.Entry) {
                final Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;

                if (Objects.equals(key_, e.getKey()) && Objects.equals(value_, e.getValue())) {
                    return true;
                }
            }

            return false;
        }

    }
}
