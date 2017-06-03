/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;

import java.util.LinkedHashSet;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferViewBase;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code Set}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class Set extends SimpleScriptable {

    private static final String SET_ITERATOR_NAME = "Set Iterator";
    private static Iterator ITERATOR_PROTOTYPE_;
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
                        add(ScriptableObject.getProperty(array, i));
                    }
                }
                else if (iterable instanceof ArrayBufferViewBase) {
                    final ArrayBufferViewBase array = (ArrayBufferViewBase) iterable;
                    for (int i = 0; i < array.getLength(); i++) {
                        add(ScriptableObject.getProperty(array, i));
                    }
                }
                else if (iterable instanceof String) {
                    final String string = (String) iterable;
                    for (int i = 0; i < string.length(); i++) {
                        add(String.valueOf(string.charAt(i)));
                    }
                }
                else if (iterable instanceof Set) {
                    final Set set = (Set) iterable;
                    for (Object object : set.set_) {
                        add(object);
                    }
                }
                else if (iterable instanceof Map) {
                    final Iterator iterator = (Iterator) ((Map) iterable).entries();

                    SimpleScriptable object = (SimpleScriptable) iterator.next();
                    while (Undefined.instance != object.get("value", null)) {
                        add(object);
                        object = (SimpleScriptable) iterator.next();
                    }
                }
                else {
                    throw Context.reportRuntimeError("TypeError: object is not iterable ("
                                + iterable.getClass().getName() + ")");
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
        if (value == NOT_FOUND) {
            value = Undefined.instance;
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
            return ScriptableObject.getProperty(start, "values");
        }
        return super.get(name, start);
    }

    /**
     * Returns a new {@code Iterator} object that contains the values for each element in the Set object
     * in insertion order.
     * @return a new {@code Iterator} object
     */
    @JsxFunction({CHROME, FF, EDGE})
    public Object values() {
        final SimpleScriptable object = new Iterator(SET_ITERATOR_NAME, set_.iterator());
        object.setParentScope(getParentScope());
        setIteratorPrototype(object);
        return object;
    }

    private static void setIteratorPrototype(final Scriptable scriptable) {
        if (ITERATOR_PROTOTYPE_ == null) {
            ITERATOR_PROTOTYPE_ = new Iterator(SET_ITERATOR_NAME, null);
        }
        scriptable.setPrototype(ITERATOR_PROTOTYPE_);
    }

    /**
     * Executes a provided function once per each value in the {@link Set} object, in insertion order.
     * @param callback {@link Function} to execute for each element.
     * @param thisArg Value to use as this when executing callback (optional)
     */
    @JsxFunction
    public void forEach(final Function callback, final Object thisArg) {
        if (getBrowserVersion().hasFeature(JS_MAP_CONSTRUCTOR_ARGUMENT)) {
            final Scriptable thisArgument;
            if (thisArg instanceof Scriptable) {
                thisArgument = (Scriptable) thisArg;
            }
            else {
                thisArgument = getWindow();
            }
            for (final Object object : set_) {
                callback.call(Context.getCurrentContext(), getParentScope(), thisArgument,
                        new Object[] {object, object, this});
            }
        }
    }

}
