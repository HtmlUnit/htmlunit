/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_URL_SEARCH_PARMS_ITERATOR_SIMPLE_NAME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;

import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ES6Iterator;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code URLSearchParams}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Ween Jiann
 */
@JsxClass({CHROME, EDGE, FF, FF78})
public class URLSearchParams extends SimpleScriptable {

    /** Constant used to register the prototype in the context. */
    public static final String URL_SEARCH_PARMS_TAG = "URLSearchParams";

    public static final class NativeParamsIterator extends ES6Iterator {
        private Type type_;
        private String className_;
        private transient Iterator<NameValuePair> iterator_ = Collections.emptyIterator();
        enum Type { KEYS, VALUES, BOTH }

        public static void init(final ScriptableObject scope, final String className) {
            ES6Iterator.init(scope, false, new NativeParamsIterator(className), URL_SEARCH_PARMS_TAG);
        }

        public NativeParamsIterator(final String className) {
            iterator_ = Collections.emptyIterator();
            type_ = Type.BOTH;
            className_ = className;
        }

        public NativeParamsIterator(final Scriptable scope, final String className, final Type type,
                                        final Iterator<NameValuePair> iterator) {
            super(scope, URL_SEARCH_PARMS_TAG);
            iterator_ = iterator;
            type_ = type;
            className_ = className;
        }

        @Override
        public String getClassName() {
            return className_;
        }

        @Override
        protected boolean isDone(final Context cx, final Scriptable scope) {
            return !iterator_.hasNext();
        }

        @Override
        protected Object nextValue(final Context cx, final Scriptable scope) {
            final NameValuePair e = iterator_.next();
            switch (type_) {
                case KEYS:
                    return e.getName();
                case VALUES:
                    return e.getValue();
                case BOTH:
                    return cx.newArray(scope, new Object[] {e.getName(), e.getValue()});
                default:
                    throw new AssertionError();
            }
        }
    }

    private final List<NameValuePair> params_ = new LinkedList<>();

    /**
     * Constructs a new instance.
     */
    public URLSearchParams() {
    }

    /**
     * Constructs a new instance.
     * @param params the params string
     */
    @JsxConstructor
    public URLSearchParams(final Object params) {
        // TODO: Pass in a sequence
        // new URLSearchParams([["foo", 1],["bar", 2]]);

        // TODO: Pass in a record
        // new URLSearchParams({"foo" : 1 , "bar" : 2});

        if (params == null || Undefined.isUndefined(params)) {
            return;
        }

        splitQuery(Context.toString(params));
    }

    private void splitQuery(String params) {
        params = StringUtils.stripStart(params, "?");
        if (StringUtils.isEmpty(params)) {
            return;
        }

        // TODO: encoding
        final String[] parts = StringUtils.split(params, '&');
        for (final String part : parts) {
            params_.add(splitQueryParameter(part));
        }
    }

    private static NameValuePair splitQueryParameter(final String singleParam) {
        final int idx = singleParam.indexOf('=');
        if (idx > -1) {
            final String key = singleParam.substring(0, idx);
            String value = null;
            if (idx < singleParam.length()) {
                value = singleParam.substring(idx + 1);
            }
            return new NameValuePair(key, value);
        }
        final String key = singleParam;
        final String value = "";
        return new NameValuePair(key, value);
    }

    /**
     * The append() method of the URLSearchParams interface appends a specified
     * key/value pair as a new search parameter.
     *
     * @param name  The name of the parameter to append.
     * @param value The value of the parameter to append.
     */
    @JsxFunction
    public void append(final String name, final String value) {
        params_.add(new NameValuePair(name, value));
    }

    /**
     * The delete() method of the URLSearchParams interface deletes the given search
     * parameter and its associated value, from the list of all search parameters.
     *
     * @param name The name of the parameter to be deleted.
     */
    @JsxFunction
    @Override
    public void delete(final String name) {
        final Iterator<NameValuePair> iter = params_.iterator();
        while (iter.hasNext()) {
            final NameValuePair entry = iter.next();
            if (entry.getName().equals(name)) {
                iter.remove();
            }
        }
    }

    /**
     * The get() method of the URLSearchParams interface returns the
     * first value associated to the given search parameter.
     *
     * @param name The name of the parameter to return.
     * @return An array of USVStrings.
     */
    @JsxFunction
    public String get(final String name) {
        for (final NameValuePair param : params_) {
            if (param.getName().equals(name)) {
                return param.getValue();
            }
        }
        return null;
    }

    /**
     * The getAll() method of the URLSearchParams interface returns all the values
     * associated with a given search parameter as an array.
     *
     * @param name The name of the parameter to return.
     * @return An array of USVStrings.
     */
    @JsxFunction
    public Scriptable getAll(final String name) {
        final List<String> result = new LinkedList<>();
        for (final NameValuePair param : params_) {
            if (param.getName().equals(name)) {
                result.add(param.getValue());
            }
        }

        return Context.getCurrentContext().newArray(getWindow(this), result.toArray());
    }

    /**
     * The set() method of the URLSearchParams interface sets the value associated with a
     * given search parameter to the given value. If there were several matching values,
     * this method deletes the others. If the search parameter doesn't exist, this method
     * creates it.
     *
     * @param name  The name of the parameter to set.
     * @param value The value of the parameter to set.
     */
    @JsxFunction
    public void set(final String name, final String value) {
        boolean change = true;
        final ListIterator<NameValuePair> iter = params_.listIterator();
        while (iter.hasNext()) {
            final NameValuePair entry = iter.next();
            if (entry.getName().equals(name)) {
                if (change) {
                    iter.set(new NameValuePair(name, value));
                    change = false;
                }
                else {
                    iter.remove();
                }
            }
        }

        if (change) {
            append(name, value);
        }
    }

    /**
     * The has() method of the URLSearchParams interface returns a Boolean that
     * indicates whether a parameter with the specified name exists.
     *
     * @param name The name of the parameter to find.
     * @return A Boolean.
     */
    @JsxFunction
    public boolean has(final String name) {
        for (final NameValuePair param : params_) {
            if (param.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The URLSearchParams.entries() method returns an iterator allowing to go through
     * all key/value pairs contained in this object. The key and value of each pair
     * are USVString objects.
     *
     * @return an iterator.
     */
    @JsxFunction
    public Object entries() {
        if (getBrowserVersion().hasFeature(JS_URL_SEARCH_PARMS_ITERATOR_SIMPLE_NAME)) {
            return new NativeParamsIterator(getParentScope(),
                    "Iterator", NativeParamsIterator.Type.BOTH, params_.iterator());
        }

        return new NativeParamsIterator(getParentScope(),
                "URLSearchParams Iterator", NativeParamsIterator.Type.BOTH, params_.iterator());
    }

    /**
     * The URLSearchParams.keys() method returns an iterator allowing to go through
     * all keys contained in this object. The keys are USVString objects.
     *
     * @return an iterator.
     */
    @JsxFunction
    public Object keys() {
        if (getBrowserVersion().hasFeature(JS_URL_SEARCH_PARMS_ITERATOR_SIMPLE_NAME)) {
            return new NativeParamsIterator(getParentScope(),
                    "Iterator", NativeParamsIterator.Type.KEYS, params_.iterator());
        }

        return new NativeParamsIterator(getParentScope(),
                "URLSearchParams Iterator", NativeParamsIterator.Type.KEYS, params_.iterator());
    }

    /**
     * The URLSearchParams.values() method returns an iterator allowing to go through
     * all values contained in this object. The values are USVString objects.
     *
     * @return an iterator.
     */
    @JsxFunction
    public Object values() {
        if (getBrowserVersion().hasFeature(JS_URL_SEARCH_PARMS_ITERATOR_SIMPLE_NAME)) {
            return new NativeParamsIterator(getParentScope(),
                    "Iterator", NativeParamsIterator.Type.VALUES, params_.iterator());
        }

        return new NativeParamsIterator(getParentScope(),
                "URLSearchParams Iterator", NativeParamsIterator.Type.VALUES, params_.iterator());
    }

    /**
     * Calls for instance for implicit conversion to string.
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#getDefaultValue(java.lang.Class)
     * @param hint the type hint
     * @return the default value
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        return URLEncodedUtils.format(NameValuePair.toHttpClient(params_), "UTF-8");
    }

    /**
     * Sets the specified request with the parameters in this {@code FormData}.
     * @param webRequest the web request to fill
     */
    public void fillRequest(final WebRequest webRequest) {
        webRequest.setRequestBody(null);
        webRequest.setEncodingType(FormEncodingType.URL_ENCODED);

        if (params_.size() > 0) {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (final NameValuePair entry : params_) {
                params.add(new NameValuePair(entry.getName(), entry.getValue()));
            }
            webRequest.setRequestParameters(params);
        }
    }
}
