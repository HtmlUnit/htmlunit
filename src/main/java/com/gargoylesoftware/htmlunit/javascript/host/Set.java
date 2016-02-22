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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_MAP_CONSTRUCTOR_ARGUMENT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.util.LinkedHashSet;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code Set}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class Set extends SimpleScriptable {

    private java.util.Set<Object> set_ = new LinkedHashSet<>();

    /**
     * Creates an instance.
     */
    public Set() {
    }

    /**
     * Creates an instance.
     * @param iterable an Array or other iterable object
     */
    @JsxConstructor
    public Set(final Object iterable) {
        if (iterable != Undefined.instance) {
            final Window window = (Window) ScriptRuntime.getTopCallScope(Context.getCurrentContext());
            if (window.getBrowserVersion().hasFeature(JS_MAP_CONSTRUCTOR_ARGUMENT)) {
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
    }

    /**
     * Returns the size.
     * @return the size
     */
    @JsxGetter
    public int getSize() {
        return set_.size();
    }

    /**
     * Adds the specified value.
     * @param value the value
     * @return the Set object.
     */
    @JsxFunction
    public Set add(Object value) {
        if (value instanceof Delegator) {
            value = ((Delegator) value).getDelegee();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        // A hack to handle Rhino not supporting "get(Object object, Scriptable start)"
        if (name.equals(Symbol.ITERATOR_STRING)) {
            return super.get("values", start);
        }
        return super.get(name, start);
    }

    /**
     * Returns a new {@code Iterator} object that contains the values for each element in the Set object
     * in insertion order.
     * @return a new {@code Iterator} object
     */
    @JsxFunction({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public Object values() {
        final SimpleScriptable object = new Iterator("Set Iterator", set_.iterator());
        object.setParentScope(getParentScope());
        return object;
    }
}
