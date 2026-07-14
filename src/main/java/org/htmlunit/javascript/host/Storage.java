/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import static org.htmlunit.BrowserVersionFeatures.JS_STORAGE_PRESERVED_INCLUDED;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.w3c.dom.DOMException;

/**
 * JavaScript host object for {@code Storage}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Kanoko Yamamoto
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Storage">MDN Documentation</a>
 */
@JsxClass
public class Storage extends HtmlUnitScriptable {

    private static final HashSet<String> RESERVED_NAMES_ = new HashSet<>(Arrays.asList(
        "clear", "key", "getItem", "length", "removeItem",
        "setItem", "constructor", "toString", "toLocaleString", "valueOf", "hasOwnProperty", "propertyIsEnumerable",
        "isPrototypeOf", "__defineGetter__", "__defineSetter__", "__lookupGetter__", "__lookupSetter__"));

    private static final long STORE_SIZE_KIMIT = 5_200_000;

    private final Map<String, String> store_;
    private long storeSize_;

    /**
     * Default constructor for prototype instantiation only.
     */
    public Storage() {
        super();
        store_ = null;
    }

    /**
     * Creates an instance of this object.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Creates a new {@code Storage} instance backed by the given store.
     *
     * @param window the parent scope
     * @param store the backing map for this storage
     */
    public Storage(final Window window, final Map<String, String> store) {
        super();
        store_ = store;
        storeSize_ = 0L;
        setParentScope(getTopLevelScope(window.getParentScope()));
        setPrototype(window.getPrototype(Storage.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        final boolean isReserved = RESERVED_NAMES_.contains(name);
        if (store_ == null || isReserved) {
            super.put(name, start, value);
        }
        if (store_ != null && (!isReserved || getBrowserVersion().hasFeature(JS_STORAGE_PRESERVED_INCLUDED))) {
            setItem(name, JavaScriptEngine.toString(value));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if (store_ == null || RESERVED_NAMES_.contains(name)) {
            return super.get(name, start);
        }
        final Object value = getItem(name);
        if (value != null) {
            return value;
        }
        return super.get(name, start);
    }

    /**
     * Returns the number of items in the storage.
     *
     * @return the number of items
     */
    @JsxGetter
    public int getLength() {
        return store_.size();
    }

    /**
     * Removes the item with the specified key.
     *
     * @param key the key of the item to remove
     */
    @JsxFunction
    public void removeItem(final String key) {
        final String removed = store_.remove(key);
        if (removed != null) {
            storeSize_ -= removed.length();
        }
    }

    /**
     * Returns the key at the specified index.
     *
     * @param index the index of the key to retrieve
     * @return the key at the given index, or {@code null} if out of range
     */
    @JsxFunction
    public String key(final int index) {
        if (index >= 0) {
            int counter = 0;
            for (final String key : store_.keySet()) {
                if (counter == index) {
                    return key;
                }
                counter++;
            }
        }
        return null;
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param key the key of the item to retrieve
     * @return the value for the given key, or {@code null} if not found
     */
    @JsxFunction
    public Object getItem(final String key) {
        return store_.get(key);
    }

    /**
     * Sets the value for the given key.
     *
     * @param key the key of the item to set
     * @param data the new value
     */
    @JsxFunction
    public void setItem(final String key, final String data) {
        final String existingData = store_.get(key);
        final long storeSize = storeSize_ + data.length() - (existingData != null ? existingData.length() : 0);
        if (storeSize > STORE_SIZE_KIMIT) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(
                    new DOMException((short) 22, "QuotaExceededError: Failed to execute 'setItem' on 'Storage': "
                            + "Setting the value of '" + key + "' exceeded the quota."));
        }
        storeSize_ = storeSize;
        store_.put(key, data);
    }

    /**
     * Removes all items from this storage.
     */
    @JsxFunction
    public void clear() {
        store_.clear();
        storeSize_ = 0;
    }
}
