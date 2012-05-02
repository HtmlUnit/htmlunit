/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Map implementation delegating all calls to the wrapped <tt>Map</tt> instance. This
 * class is <tt>Serializable</tt>, but serialization will fail if the wrapped map is not
 * itself <tt>Serializable</tt>.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @param <K> the type of the map key
 * @param <V> the type of the value
 */
public class MapWrapper<K, V> implements Map<K, V>, Serializable {

    private Map<K, V> wrappedMap_;

    /**
     * Simple constructor. Needed to allow subclasses to be serializable.
     */
    protected MapWrapper() {
        // Empty.
    }

    /**
     * Initializes the wrapper with its wrapped map.
     * @param map the map to wrap.
     */
    public MapWrapper(final Map<K, V> map) {
        wrappedMap_ = map;
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        wrappedMap_.clear();
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(final Object key) {
        return wrappedMap_.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(final Object value) {
        return wrappedMap_.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    public Set<Entry<K, V>> entrySet() {
        return wrappedMap_.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        return wrappedMap_.equals(o);
    }

    /**
     * {@inheritDoc}
     */
    public V get(final Object key) {
        return wrappedMap_.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return wrappedMap_.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return wrappedMap_.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public Set<K> keySet() {
        return wrappedMap_.keySet();
    }

    /**
     * {@inheritDoc}
     */
    public V put(final K key, final V value) {
        return wrappedMap_.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public void putAll(final Map<? extends K, ? extends V> t) {
        wrappedMap_.putAll(t);
    }

    /**
     * {@inheritDoc}
     */
    public V remove(final Object key) {
        return wrappedMap_.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return wrappedMap_.size();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<V> values() {
        return wrappedMap_.values();
    }
}
