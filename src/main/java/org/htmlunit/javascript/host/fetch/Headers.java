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
package org.htmlunit.javascript.host.fetch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ES6Iterator;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.IteratorLikeIterable;
import org.htmlunit.corejs.javascript.NativeObject;
import org.htmlunit.corejs.javascript.ScriptRuntime;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.SymbolKey;
import org.htmlunit.corejs.javascript.TopLevel;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.util.NameValuePair;

/**
 * A JavaScript object for {@code Headers}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class Headers extends HtmlUnitScriptable {

    /** Constant used to register the prototype in the context. */
    public static final String HEADERS_TAG = "Headers";

    private final List<NameValuePair> headers_ = new ArrayList<>();

    /**
     * {@link ES6Iterator} implementation for js support.
     */
    public static final class NativeHeadersIterator extends ES6Iterator {
        enum Type { KEYS, VALUES, BOTH }

        private final Type type_;
        private final String className_;
        private final transient Iterator<NameValuePair> iterator_;

        /**
         * Init.
         * @param scope the scope
         * @param className the class name
         */
        public static void init(final TopLevel scope, final String className) {
            ES6Iterator.init(scope, false, new NativeHeadersIterator(className), HEADERS_TAG);
        }

        /**
         * Ctor.
         * @param className the class name
         */
        public NativeHeadersIterator(final String className) {
            super();
            iterator_ = Collections.emptyIterator();
            type_ = Type.BOTH;
            className_ = className;
        }

        /**
         * Ctor.
         * @param scope the scope
         * @param className the class name
         * @param type the type
         * @param iterator the backing iterator
         */
        public NativeHeadersIterator(final VarScope scope, final String className, final Type type,
                final Iterator<NameValuePair> iterator) {
            super(scope, HEADERS_TAG);
            iterator_ = iterator;
            type_ = type;
            className_ = className;
        }

        @Override
        public String getClassName() {
            return className_;
        }

        @Override
        protected boolean isDone(final Context cx, final VarScope scope) {
            return !iterator_.hasNext();
        }

        @Override
        protected Object nextValue(final Context cx, final VarScope scope) {
            final NameValuePair e = iterator_.next();
            return switch (type_) {
                case KEYS -> e.getName();
                case VALUES -> e.getValue();
                case BOTH -> cx.newArray(scope, new Object[] {e.getName(), e.getValue()});
            };
        }
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor(final Object init) {
        if (init == null || JavaScriptEngine.isUndefined(init)) {
            return;
        }

        if (init instanceof Headers headers) {
            headers_.addAll(headers.headers_);
            return;
        }

        if (init instanceof NativeObject object) {
            for (final Map.Entry<Object, Object> entry : object.entrySet()) {
                append(JavaScriptEngine.toString(entry.getKey()), JavaScriptEngine.toString(entry.getValue()));
            }
            return;
        }

        if (init instanceof Scriptable scriptable && hasProperty(scriptable, SymbolKey.ITERATOR)) {
            final Context cx = Context.getCurrentContext();
            try (IteratorLikeIterable itr = buildIteratorLikeIterable(cx, scriptable)) {
                for (final Object nameValue : itr) {
                    if (!(nameValue instanceof Scriptable pair)) {
                        throw JavaScriptEngine.typeError("The provided value cannot be converted to a sequence.");
                    }
                    if (!hasProperty(pair, SymbolKey.ITERATOR)) {
                        throw JavaScriptEngine.typeError("The object must have a callable @@iterator property.");
                    }

                    try (IteratorLikeIterable pairItr = buildIteratorLikeIterable(cx, pair)) {
                        final Iterator<Object> iterator = pairItr.iterator();
                        final Object name = iterator.hasNext() ? iterator.next() : NOT_FOUND;
                        final Object value = iterator.hasNext() ? iterator.next() : NOT_FOUND;
                        if (name == NOT_FOUND || value == NOT_FOUND || iterator.hasNext()) {
                            throw JavaScriptEngine.typeError("Sequence initializer must only contain pair elements.");
                        }
                        append(JavaScriptEngine.toString(name), JavaScriptEngine.toString(value));
                    }
                }
            }
            return;
        }

        throw JavaScriptEngine.typeError("Failed to construct 'Headers': Invalid initializer");
    }

    /**
     * Appends a header to this Headers object.
     * @param name the header name
     * @param value the header value
     */
    @JsxFunction
    public void append(final String name, final String value) {
        headers_.add(new NameValuePair(normalizeName(name), JavaScriptEngine.toString(value)));
    }

    /**
     * Deletes all matching header values.
     * @param name the header name
     */
    @JsxFunction(functionName = "delete")
    @Override
    public void delete(final String name) {
        final String normalizedName = normalizeName(name);
        headers_.removeIf(entry -> normalizedName.equals(entry.getName()));
    }

    /**
     * Returns the first value for the header name.
     * @param name the header name
     * @return the value or null
     */
    @JsxFunction
    public String get(final String name) {
        final String normalizedName = normalizeName(name);
        String result = null;
        for (final NameValuePair entry : headers_) {
            if (normalizedName.equals(entry.getName())) {
                if (result == null) {
                    result = entry.getValue();
                }
                else {
                    result += ", " + entry.getValue();
                }
            }
        }
        return result;
    }

    /**
     * Returns all set-cookie header values.
     * @return the set-cookie values
     */
    @JsxFunction
    public Scriptable getSetCookie() {
        final List<String> result = new ArrayList<>();
        for (final NameValuePair entry : headers_) {
            if ("set-cookie".equals(entry.getName())) {
                result.add(entry.getValue());
            }
        }
        return JavaScriptEngine.newArray(getParentScope(), result.toArray());
    }

    /**
     * Returns if this Headers contains the given name.
     * @param name the header name
     * @return true if this Headers contains the given name
     */
    @JsxFunction
    public boolean has(final String name) {
        final String normalizedName = normalizeName(name);
        for (final NameValuePair entry : headers_) {
            if (normalizedName.equals(entry.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets a header value.
     * @param name the header name
     * @param value the value
     */
    @JsxFunction
    public void set(final String name, final String value) {
        final String normalizedName = normalizeName(name);
        delete(normalizedName);
        headers_.add(new NameValuePair(normalizedName, JavaScriptEngine.toString(value)));
    }

    /**
     * Allows iteration through all key/value pairs.
     * @param callback function to execute on each key/value pair
     */
    @JsxFunction
    public void forEach(final Object callback) {
        if (!(callback instanceof Function fun)) {
            throw JavaScriptEngine.typeError(
                    "Foreach callback '" + JavaScriptEngine.toString(callback) + "' is not a function");
        }

        final List<NameValuePair> entries = entriesList();
        for (final NameValuePair entry : entries) {
            fun.call(Context.getCurrentContext(), getParentScope(), this,
                    new Object[] {entry.getValue(), entry.getName(), this});
        }
    }

    /**
     * Returns an iterator of key/value pairs.
     * @return the iterator
     */
    @JsxFunction
    @JsxSymbol(symbolName = "iterator")
    public ES6Iterator entries() {
        return new NativeHeadersIterator(getParentScope(),
                "Headers Iterator", NativeHeadersIterator.Type.BOTH, entriesList().iterator());
    }

    /**
     * Returns an iterator of header keys.
     * @return the iterator
     */
    @JsxFunction
    public ES6Iterator keys() {
        return new NativeHeadersIterator(getParentScope(),
                "Headers Iterator", NativeHeadersIterator.Type.KEYS, entriesList().iterator());
    }

    /**
     * Returns an iterator of header values.
     * @return the iterator
     */
    @JsxFunction
    public ES6Iterator values() {
        return new NativeHeadersIterator(getParentScope(),
                "Headers Iterator", NativeHeadersIterator.Type.VALUES, entriesList().iterator());
    }

    /**
     * @return all request header entries for this object
     */
    List<NameValuePair> entriesList() {
        final Map<String, String> merged = new LinkedHashMap<>();
        for (final NameValuePair entry : headers_) {
            final String previous = merged.get(entry.getName());
            if (previous == null) {
                merged.put(entry.getName(), entry.getValue());
            }
            else {
                merged.put(entry.getName(), previous + ", " + entry.getValue());
            }
        }

        final List<NameValuePair> result = new ArrayList<>(merged.size());
        for (final Map.Entry<String, String> entry : merged.entrySet()) {
            result.add(new NameValuePair(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    void copyFrom(final Headers headers) {
        headers_.clear();
        headers_.addAll(headers.headers_);
    }

    void appendRaw(final String name, final String value) {
        headers_.add(new NameValuePair(normalizeName(name), value));
    }

    private static IteratorLikeIterable buildIteratorLikeIterable(final Context cx, final Scriptable iterable) {
        final Object iterator = ScriptRuntime.callIterator(iterable, cx, iterable.getParentScope());
        return new IteratorLikeIterable(cx, iterable.getParentScope(), iterator);
    }

    private static String normalizeName(final String name) {
        return JavaScriptEngine.toString(name).toLowerCase(Locale.ROOT);
    }
}
