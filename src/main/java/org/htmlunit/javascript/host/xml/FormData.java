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
package org.htmlunit.javascript.host.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.htmlunit.FormEncodingType;
import org.htmlunit.WebRequest;
import org.htmlunit.corejs.javascript.ClassDescriptor;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ES6Iterator;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.TopLevel;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.javascript.host.file.Blob;
import org.htmlunit.javascript.host.file.File;
import org.htmlunit.javascript.host.html.HTMLFormElement;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.StringUtils;

/**
 * JavaScript host object for {@code FormData}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Thorsten Wendelmuth
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/FormData">MDN Documentation</a>
 */
@JsxClass
public class FormData extends HtmlUnitScriptable {

    /** Constant used to register the prototype in the context. */
    private static final String FORM_DATA_ITERATOR_TAG = "FormData Iterator";

    private final List<NameValuePair> requestParameters_ = new ArrayList<>();

    /**
     * Iterator support for {@code FormData}.
     */
    public static final class FormDataIterator extends ES6Iterator {
        private static final ClassDescriptor DESCRIPTOR =
                ES6Iterator.makeDescriptor(FORM_DATA_ITERATOR_TAG, FORM_DATA_ITERATOR_TAG);

        enum Type { KEYS, VALUES, BOTH }

        private final Type type_;
        private final String className_;
        private final List<NameValuePair> nameValuePairList_;
        private int index_;

        /**
         * Initializes the iterator prototype.
         *
         * @param cx the {@link Context}
         * @param scope the scope
         * @param className the class name
         */
        public static void init(final Context cx, final TopLevel scope, final String className) {
            ES6Iterator.initialize(
                    DESCRIPTOR, cx, scope, new FormDataIterator(className), false, FORM_DATA_ITERATOR_TAG);
        }

        /**
         * Creates an instance with an empty list (used for prototype initialization).
         *
         * @param className the class name
         */
        public FormDataIterator(final String className) {
            super();

            type_ = Type.BOTH;
            index_ = 0;
            nameValuePairList_ = Collections.emptyList();
            className_ = className;
        }

        /**
         * Creates an instance for iterating over the given list of name-value pairs.
         *
         * @param scope the scope
         * @param className the class name
         * @param type the iteration type (keys, values, or both)
         * @param nameValuePairList the list of name-value pairs to iterate
         */
        public FormDataIterator(final VarScope scope, final String className, final Type type,
                final List<NameValuePair> nameValuePairList) {
            super(scope, className);
            type_ = type;
            index_ = 0;
            nameValuePairList_ = nameValuePairList;
            className_ = className;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getClassName() {
            return className_;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected boolean isDone(final Context cx, final VarScope scope) {
            return index_ >= nameValuePairList_.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Object nextValue(final Context cx, final VarScope scope) {
            if (isDone(cx, scope)) {
                return Context.getUndefinedValue();
            }

            final NameValuePair nextNameValuePair = nameValuePairList_.get(index_++);
            return switch (type_) {
                case KEYS -> nextNameValuePair.getName();
                case VALUES -> nextNameValuePair.getValue();
                case BOTH ->
                    cx.newArray(scope, new Object[]{nextNameValuePair.getName(), nextNameValuePair.getValue()});
            };
        }
    }

    /**
     * Creates an instance of this object, optionally pre-populated from the given form.
     *
     * @param formObj an {@link HTMLFormElement} to initialize the data from, or {@code undefined}
     */
    @JsxConstructor
    public void jsConstructor(final Object formObj) {
        if (formObj instanceof HTMLFormElement form) {
            requestParameters_.addAll(form.getHtmlForm().getParameterListForSubmit(null));
        }
    }

    /**
     * Appends a new value for an existing key, or adds the key if it does not already exist.
     *
     * @param name the name of the field
     * @param value the field's value
     * @param filename the filename reported to the server (optional)
     */
    @JsxFunction
    public void append(final String name, final Object value, final Object filename) {
        if (value instanceof Blob blob) {
            String fileName = "blob";
            if (value instanceof File) {
                fileName = null;
            }
            if (filename instanceof String string) {
                fileName = string;
            }
            requestParameters_.add(blob.getKeyDataPair(name, fileName));
            return;
        }
        requestParameters_.add(new NameValuePair(name, JavaScriptEngine.toString(value)));
    }

    /**
     * Removes the entry with the given name, if it exists.
     *
     * @param name the name of the field to remove
     */
    @JsxFunction(functionName = "delete")
    public void delete_js(final String name) {
        if (StringUtils.isEmptyOrNull(name)) {
            return;
        }

        requestParameters_.removeIf(pair -> name.equals(pair.getName()));
    }

    /**
     * Returns the first value associated with the given name.
     *
     * @param name the name of the field to retrieve
     * @return the first value found, or {@code null} if not found
     */
    @JsxFunction
    public String get(final String name) {
        if (StringUtils.isEmptyOrNull(name)) {
            return null;
        }

        for (final NameValuePair pair : requestParameters_) {
            if (name.equals(pair.getName())) {
                return pair.getValue();
            }
        }
        return null;
    }

    /**
     * Returns all values associated with the given name.
     *
     * @param name the name of the field to retrieve
     * @return an array of all values found for the given name
     */
    @JsxFunction
    public Scriptable getAll(final String name) {
        if (StringUtils.isEmptyOrNull(name)) {
            return JavaScriptEngine.newArray(getParentScope(), 0);
        }

        final List<Object> values = new ArrayList<>();
        for (final NameValuePair pair : requestParameters_) {
            if (name.equals(pair.getName())) {
                values.add(pair.getValue());
            }
        }

        final Object[] stringValues = values.toArray(new Object[0]);
        return JavaScriptEngine.newArray(getParentScope(), stringValues);
    }

    /**
     * Returns whether an entry with the given name exists.
     *
     * @param name the name of the field to check
     * @return {@code true} if the name exists, {@code false} otherwise
     */
    @JsxFunction
    public boolean has(final String name) {
        if (StringUtils.isEmptyOrNull(name)) {
            return false;
        }

        for (final NameValuePair pair : requestParameters_) {
            if (name.equals(pair.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets a new value for an existing key, replacing all existing values for that key,
     * or adds the key if it does not already exist.
     *
     * @param name the name of the field
     * @param value the field's value
     * @param filename the filename reported to the server (optional)
     */
    @JsxFunction
    public void set(final String name, final Object value, final Object filename) {
        if (StringUtils.isEmptyOrNull(name)) {
            return;
        }

        int pos = -1;

        final Iterator<NameValuePair> iter = requestParameters_.iterator();
        int idx = 0;
        while (iter.hasNext()) {
            final NameValuePair pair = iter.next();
            if (name.equals(pair.getName())) {
                iter.remove();
                if (pos < 0) {
                    pos = idx;
                }
            }
            idx++;
        }

        if (pos < 0) {
            pos = requestParameters_.size();
        }

        if (value instanceof Blob blob) {
            String fileName = "blob";
            if (value instanceof File) {
                fileName = null;
            }
            if (filename instanceof String string) {
                fileName = string;
            }
            requestParameters_.add(pos, blob.getKeyDataPair(name, fileName));
        }
        else {
            requestParameters_.add(pos, new NameValuePair(name, JavaScriptEngine.toString(value)));
        }
    }

    /**
     * Returns an iterator over all name/value pairs contained in this {@code FormData}.
     *
     * @return an iterator of {@code [name, value]} arrays
     */
    @JsxFunction
    @JsxSymbol(symbolName = "iterator")
    public Scriptable entries() {
        return new FormDataIterator(getParentScope(),
                FORM_DATA_ITERATOR_TAG, FormDataIterator.Type.BOTH, requestParameters_);
    }

    /**
     * Populates the given {@link WebRequest} with the parameters from this {@code FormData}.
     *
     * @param webRequest the web request to fill
     */
    public void fillRequest(final WebRequest webRequest) {
        webRequest.setEncodingType(FormEncodingType.MULTIPART);
        webRequest.setRequestParameters(requestParameters_);
    }

    /**
     * Iterates over all key/value pairs in this {@code FormData}, calling the given callback for each.
     *
     * @param callback the function to execute for each key/value pair
     */
    @JsxFunction
    public void forEach(final Object callback) {
        if (!(callback instanceof Function fun)) {
            throw JavaScriptEngine.typeError(
                    "Foreach callback '" + JavaScriptEngine.toString(callback) + "' is not a function");
        }

        // This must be indexes instead of iterator() for correct behavior when of list changes while iterating
        for (int i = 0;; i++) {
            if (i >= requestParameters_.size()) {
                break;
            }

            final NameValuePair param = requestParameters_.get(i);
            fun.call(Context.getCurrentContext(), getParentScope(), this,
                        new Object[] {param.getValue(), param.getName(), this});
        }
    }

    /**
     * Returns an iterator over all keys in this {@code FormData}.
     *
     * @return an iterator of key strings
     */
    @JsxFunction
    public FormDataIterator keys() {
        return new FormDataIterator(getParentScope(),
                FORM_DATA_ITERATOR_TAG, FormDataIterator.Type.KEYS, requestParameters_);
    }

    /**
     * Returns an iterator over all values in this {@code FormData}.
     *
     * @return an iterator of value strings
     */
    @JsxFunction
    public FormDataIterator values() {
        return new FormDataIterator(getParentScope(),
                FORM_DATA_ITERATOR_TAG, FormDataIterator.Type.VALUES, requestParameters_);
    }
}
