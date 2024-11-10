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
package org.htmlunit.javascript.host.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.FormEncodingType;
import org.htmlunit.WebRequest;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ES6Iterator;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
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

/**
 * A JavaScript object for {@code FormData}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Thorsten Wendelmuth
 */
@JsxClass
public class FormData extends HtmlUnitScriptable {

    /** Constant used to register the prototype in the context. */
    public static final String FORM_DATA_TAG = "FormData";

    private final List<NameValuePair> requestParameters_ = new ArrayList<>();

    /**
     * FormDate iterator support.
     */
    public static final class FormDataIterator extends ES6Iterator {
        enum Type { KEYS, VALUES, BOTH }

        private final Type type_;
        private final String className_;
        private final List<NameValuePair> nameValuePairList_;
        private int index_;

        /**
         * JS initializer.
         *
         * @param scope the scope
         * @param className the class name
         */
        public static void init(final ScriptableObject scope, final String className) {
            ES6Iterator.init(scope, false, new FormDataIterator(className), FORM_DATA_TAG);
        }

        /**
         * Ctor.
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
         * Ctor.
         *
         * @param scope the scope
         * @param className the class name
         * @param type the type
         * @param nameValuePairList the list of name value pairs
         */
        public FormDataIterator(final Scriptable scope, final String className, final Type type,
                final List<NameValuePair> nameValuePairList) {
            super(scope, FORM_DATA_TAG);
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
        protected boolean isDone(final Context cx, final Scriptable scope) {
            return index_ >= nameValuePairList_.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Object nextValue(final Context cx, final Scriptable scope) {
            if (isDone(cx, scope)) {
                return Context.getUndefinedValue();
            }

            final NameValuePair nextNameValuePair = nameValuePairList_.get(index_++);
            switch (type_) {
                case KEYS:
                    return nextNameValuePair.getName();
                case VALUES:
                    return nextNameValuePair.getValue();
                case BOTH:
                    return cx.newArray(scope, new Object[] {nextNameValuePair.getName(), nextNameValuePair.getValue()});
                default:
                    throw new AssertionError();
            }
        }
    }

    /**
     * Constructor.
     * @param formObj a form
     */
    @JsxConstructor
    public void jsConstructor(final Object formObj) {
        if (formObj instanceof HTMLFormElement) {
            final HTMLFormElement form = (HTMLFormElement) formObj;
            requestParameters_.addAll(form.getHtmlForm().getParameterListForSubmit(null));
        }
    }

    /**
     * Appends a new value onto an existing key inside a {@code FormData} object,
     * or adds the key if it does not already exist.
     * @param name the name of the field whose data is contained in {@code value}
     * @param value the field's value
     * @param filename the filename reported to the server (optional)
     */
    @JsxFunction
    public void append(final String name, final Object value, final Object filename) {
        if (value instanceof Blob) {
            final Blob blob = (Blob) value;
            String fileName = "blob";
            if (value instanceof File) {
                fileName = null;
            }
            if (filename instanceof String) {
                fileName = (String) filename;
            }
            requestParameters_.add(blob.getKeyDataPair(name, fileName));
            return;
        }
        requestParameters_.add(new NameValuePair(name, JavaScriptEngine.toString(value)));
    }

    /**
     * Removes the entry (if exists).
     * @param name the name of the field to remove
     */
    @JsxFunction(functionName = "delete")
    public void delete_js(final String name) {
        if (StringUtils.isEmpty(name)) {
            return;
        }

        requestParameters_.removeIf(pair -> name.equals(pair.getName()));
    }

    /**
     * @param name the name of the field to check
     * @return the first value found for the give name
     */
    @JsxFunction
    public String get(final String name) {
        if (StringUtils.isEmpty(name)) {
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
     * @param name the name of the field to check
     * @return the values found for the give name
     */
    @JsxFunction
    public Scriptable getAll(final String name) {
        if (StringUtils.isEmpty(name)) {
            return JavaScriptEngine.newArray(this, 0);
        }

        final List<Object> values = new ArrayList<>();
        for (final NameValuePair pair : requestParameters_) {
            if (name.equals(pair.getName())) {
                values.add(pair.getValue());
            }
        }

        final Object[] stringValues = values.toArray(new Object[0]);
        return JavaScriptEngine.newArray(this, stringValues);
    }

    /**
     * @param name the name of the field to check
     * @return true if the name exists
     */
    @JsxFunction
    public boolean has(final String name) {
        if (StringUtils.isEmpty(name)) {
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
     * Sets a new value for an existing key inside a {@code FormData} object,
     * or adds the key if it does not already exist.
     * @param name the name of the field whose data is contained in {@code value}
     * @param value the field's value
     * @param filename the filename reported to the server (optional)
     */
    @JsxFunction
    public void set(final String name, final Object value, final Object filename) {
        if (StringUtils.isEmpty(name)) {
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

        if (value instanceof Blob) {
            final Blob blob = (Blob) value;
            String fileName = "blob";
            if (value instanceof File) {
                fileName = null;
            }
            if (filename instanceof String) {
                fileName = (String) filename;
            }
            requestParameters_.add(pos, blob.getKeyDataPair(name, fileName));
        }
        else {
            requestParameters_.add(pos, new NameValuePair(name, JavaScriptEngine.toString(value)));
        }
    }

    /**
     * @return An Iterator that contains all the requestParameters name[0] and value[1]
     */
    @JsxFunction
    @JsxSymbol(symbolName = "iterator")
    public Scriptable entries() {
        return new FormDataIterator(this, "FormData Iterator", FormDataIterator.Type.BOTH, requestParameters_);
    }

    /**
     * Sets the specified request with the parameters in this {@code FormData}.
     * @param webRequest the web request to fill
     */
    public void fillRequest(final WebRequest webRequest) {
        webRequest.setEncodingType(FormEncodingType.MULTIPART);
        webRequest.setRequestParameters(requestParameters_);
    }

    /**
     * The FormData.forEach() method allows iteration through
     * all key/value pairs contained in this object via a callback function.
     * @param callback Function to execute on each key/value pairs
     */
    @JsxFunction
    public void forEach(final Object callback) {
        if (!(callback instanceof Function)) {
            throw JavaScriptEngine.typeError(
                    "Foreach callback '" + JavaScriptEngine.toString(callback) + "' is not a function");
        }

        final Function fun = (Function) callback;

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
     * The FormData.keys() method returns an iterator allowing to go through
     * all keys contained in this object. The keys are USVString objects.
     *
     * @return an iterator.
     */
    @JsxFunction
    public FormDataIterator keys() {
        return new FormDataIterator(getParentScope(),
                "FormData Iterator", FormDataIterator.Type.KEYS, requestParameters_);
    }

    /**
     * The URLSearchParams.values() method returns an iterator allowing to go through
     * all values contained in this object. The values are USVString objects.
     *
     * @return an iterator.
     */
    @JsxFunction
    public FormDataIterator values() {
        return new FormDataIterator(getParentScope(),
                "FormData Iterator", FormDataIterator.Type.VALUES, requestParameters_);
    }
}
