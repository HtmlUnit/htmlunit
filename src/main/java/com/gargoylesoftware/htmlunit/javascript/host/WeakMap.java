/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WEAKMAP_CONSTRUCTOR_ARGUMENT;

import java.util.WeakHashMap;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code WeakMap}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class WeakMap extends SimpleScriptable {

    private transient java.util.Map<Object, Object> map_ = new WeakHashMap<>();

    /**
     * Creates an instance.
     */
    public WeakMap() {
    }

    /**
     * Creates an instance.
     * @param iterable an Array or other iterable object
     */
    @JsxConstructor
    public WeakMap(final Object iterable) {
        if (iterable != Undefined.instance) {
            final Window window = (Window) ScriptRuntime.getTopCallScope(Context.getCurrentContext());
            if (window.getBrowserVersion().hasFeature(JS_WEAKMAP_CONSTRUCTOR_ARGUMENT)) {
                if (iterable instanceof NativeArray) {
                    final NativeArray array = (NativeArray) iterable;
                    for (int i = 0; i < array.getLength(); i++) {
                        final Object entryObject = array.get(i);
                        if (entryObject instanceof NativeArray) {
                            final Object[] entry = ((NativeArray) entryObject).toArray();
                            if (entry.length > 0) {
                                final Object key = entry[0];
                                final Object value = entry.length > 1 ? entry[1] : null;
                                set(key, value);
                            }
                        }
                        else {
                            throw Context.reportRuntimeError("TypeError: object is not iterable");
                        }
                    }
                }
                else {
                    throw Context.reportRuntimeError("TypeError: object is not iterable");
                }
            }
        }
    }

    /**
     * Returns the value of the given key.
     * @param key the key
     * @return the value
     */
    @Override
    @JsxFunction
    public Object get(final Object key) {
        Object o = map_.get(key);
        if (o == null) {
            o = Undefined.instance;
        }
        return o;
    }

    /**
     * Adds the specified pair.
     * @param key the key
     * @param value the value
     * @return the Map object.
     */
    @JsxFunction
    public WeakMap set(Object key, final Object value) {
        if (key instanceof Delegator) {
            key = ((Delegator) key).getDelegee();
        }
        if (!(key instanceof ScriptableObject)) {
            throw Context.reportRuntimeError("TypeError: key is not an object");
        }
        map_.put(key, value);
        return this;
    }

    /**
     * Removes all elements.
     */
    @JsxFunction
    public void clear() {
        map_.clear();
    }

    /**
     * Removed the specified element.
     * @param key the key
     * @return whether the element has been successfully removed
     */
    @JsxFunction
    public boolean delete(final Object key) {
        return map_.remove(key) != null;
    }

    /**
     * Returns whether an element with the specified key exists or not.
     * @param key the key
     * @return whether the element exists or not
     */
    @JsxFunction
    public boolean has(final Object key) {
        return map_.remove(key) != null;
    }

}
