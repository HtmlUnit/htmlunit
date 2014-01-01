/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * The JavaScript object that represents a Storage.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@JsxClass
public class Storage extends SimpleScriptable {

    private static List<String> RESERVED_NAMES_ = Arrays.asList("clear", "key", "getItem", "length", "removeItem",
        "setItem");

    private final Map<String, String> store_;

    /**
     * Public no-arg constructor only for the prototype.
     */
    @Deprecated
    public Storage() {
        store_ = null;
    }

    /**
     * Constructor.
     * @param window the parent scope
     * @param store the storage itself
     */
    public Storage(final Window window, final Map<String, String> store) {
        store_ = store;
        setParentScope(window);
        setPrototype(window.getPrototype(Storage.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (store_ == null || RESERVED_NAMES_.contains(name)) {
            super.put(name, start, value);
            return;
        }
        setItem(name, Context.toString(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if (store_ == null || RESERVED_NAMES_.contains(name)) {
            return super.get(name, start);
        }
        return getItem(name);
    }

    /**
     * Returns the length property.
     * @return the length property
     */
    @JsxGetter
    public int getLength() {
        return getMap().size();
    }

    /**
     * Removes the specified key.
     * @param key the item key
     */
    @JsxFunction
    public void removeItem(final String key) {
        getMap().remove(key);
    }

    /**
     * Returns the key of the specified index.
     * @param index the index
     * @return the key
     */
    @JsxFunction
    public String key(final int index) {
        int counter = 0;
        for (final String key : getMap().keySet()) {
            if (counter++ == index) {
                return key;
            }
        }
        return null;
    }

    private Map<String, String> getMap() {
        return store_;
    }

    /**
     * Returns the value of the specified key.
     * @param key the item key
     * @return the value
     */
    @JsxFunction
    public Object getItem(final String key) {
        return getMap().get(key);
    }

    /**
     * Sets the item value.
     * @param key the item key
     * @param data the value
     */
    @JsxFunction
    public void setItem(final String key, final String data) {
        getMap().put(key, data);
    }

    /**
     * Clears all items.
     */
    @JsxFunction
    public void clear() {
        getMap().clear();
    }
}
