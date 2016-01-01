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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.util.Collections;
import java.util.WeakHashMap;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code WeakSet}.
 *
 * @author Ahmed Ashour
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 38), @WebBrowser(EDGE) })
public class WeakSet extends SimpleScriptable {

    private transient java.util.Set<Object> set_ = Collections.newSetFromMap(new WeakHashMap<Object, Boolean>());

    /**
     * Creates an instance.
     */
    public WeakSet() {
    }

    /**
     * Creates an instance.
     * @param iterable an Array or other iterable object
     */
    @JsxConstructor
    public WeakSet(final Object iterable) {
        if (iterable != Undefined.instance) {
            if (iterable instanceof NativeArray) {
                final NativeArray array = (NativeArray) iterable;
                for (int i = 0; i < array.getLength(); i++) {
                    add(array.get(i));
                }
            }
            else {
                throw Context.reportRuntimeError("TypeError: object is not iterable");
            }
        }
    }

    /**
     * Adds the specified value.
     * @param value the value
     * @return the Set object.
     */
    @JsxFunction
    public WeakSet add(Object value) {
        if (value instanceof Delegator) {
            value = ((Delegator) value).getDelegee();
        }
        if (!(value instanceof ScriptableObject)) {
            throw Context.reportRuntimeError("TypeError: key is not an object");
        }
        set_.add(value);
        return this;
    }

    /**
     * Removes all elements.
     */
    @JsxFunction
    public void clear() {
        set_.clear();
    }

    /**
     * Removed the specified element.
     * @param key the key
     * @return whether the element has been successfully removed
     */
    @JsxFunction
    public boolean delete(final Object key) {
        return set_.remove(key);
    }

    /**
     * Returns whether the specified element exists or not.
     * @param value the value
     * @return whether the element exists or not
     */
    @JsxFunction
    public boolean has(final Object value) {
        return set_.contains(value);
    }

}
