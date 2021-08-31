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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;

import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;

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

    private final URIBuilder url;

    /**
     * Constructs a new instance.
     */
    public URLSearchParams() {
        try {
            this.url = new URIBuilder("http://dummy.com");
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unexpected");
        }
    }

    /**
     * Constructs a new instance.
     * @param params the params string
     */
    @JsxConstructor
    public URLSearchParams(final Object params) {
        this();
        // TODO: Pass in a sequence
        // new URLSearchParams([["foo", 1],["bar", 2]]);

        // TODO: Pass in a record
        // new URLSearchParams({"foo" : 1 , "bar" : 2});

        if (params == null || Undefined.isUndefined(params)) {
            return;
        }

        List<NameValuePair> nameValuePairs = splitQuery(Context.toString(params));
        url.setParameters(nameValuePairs);
    }

    public URLSearchParams(final URIBuilder url){
        this.url = url;
    }

    private List<NameValuePair> splitQuery(String params) {
        List<NameValuePair> result= new ArrayList<>();
        params = StringUtils.stripStart(params, "?");
        if (StringUtils.isEmpty(params)) {
            return result;
        }

        // TODO: encoding
        final String[] parts = StringUtils.split(params, '&');
        for (final String part : parts) {
            result.add(splitQueryParameter(part));
        }
        return result;
    }

    private static com.gargoylesoftware.htmlunit.util.NameValuePair splitQueryParameter(final String singleParam) {
        final int idx = singleParam.indexOf('=');
        if (idx > -1) {
            final String key = singleParam.substring(0, idx);
            String value = null;
            if (idx < singleParam.length()) {
                value = singleParam.substring(idx + 1);
            }
            return new com.gargoylesoftware.htmlunit.util.NameValuePair(key, value);
        }
        final String key = singleParam;
        final String value = "";
        return new com.gargoylesoftware.htmlunit.util.NameValuePair(key, value);
    }

    private List<org.apache.http.NameValuePair> getParams() {
        return url.getQueryParams();
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
        url.addParameter(name, value);
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
        List<NameValuePair> params = url.getQueryParams();
        final Iterator<NameValuePair> iter = params.iterator();
        while (iter.hasNext()) {
            final NameValuePair entry = iter.next();
            if (entry.getName().equals(name)) {
                iter.remove();
            }
        }
        url.setParameters(params);
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
        for (final NameValuePair param : getParams()) {
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
        final List<String> result = new ArrayList<>(getParams().size());
        for (final NameValuePair param : getParams()) {
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
        List<NameValuePair> params = url.getQueryParams();
        boolean found = false;
        final ListIterator<NameValuePair> iter = params.listIterator();
        while (iter.hasNext()) {
            final NameValuePair entry = iter.next();
            if (entry.getName().equals(name)) {
                if (!found) {
                    iter.set(new com.gargoylesoftware.htmlunit.util.NameValuePair(name, value));
                    found = true;
                }
                else {
                    iter.remove();
                }
            }
        }

        if (found) {
            url.setParameters(params);
        } else {
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
        for (final NameValuePair param : getParams()) {
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
                    "Iterator", NativeParamsIterator.Type.BOTH, getParams().iterator());
        }

        return new NativeParamsIterator(getParentScope(),
                "URLSearchParams Iterator", NativeParamsIterator.Type.BOTH, getParams().iterator());
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
                    "Iterator", NativeParamsIterator.Type.KEYS, getParams().iterator());
        }

        return new NativeParamsIterator(getParentScope(),
                "URLSearchParams Iterator", NativeParamsIterator.Type.KEYS, getParams().iterator());
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
                    "Iterator", NativeParamsIterator.Type.VALUES, getParams().iterator());
        }

        return new NativeParamsIterator(getParentScope(),
                "URLSearchParams Iterator", NativeParamsIterator.Type.VALUES, getParams().iterator());
    }

    /**
     * Returns the text of the Range.
     * @return the text
     */
    @JsxFunction(functionName = "toString")
    public String jsToString() {
        List<NameValuePair> params = getParams().stream()
                .map(p -> {
                    if (p.getValue() != null) {
                        return p;
                    } else {
                        return new com.gargoylesoftware.htmlunit.util.NameValuePair(p.getName(), "");
                    }
                }).collect(Collectors.toList());
        return URLEncodedUtils.format(params, "UTF-8");
    }

    /**
     * Calls for instance for implicit conversion to string.
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#getDefaultValue(java.lang.Class)
     * @param hint the type hint
     * @return the default value
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        return jsToString();
    }

    /**
     * Sets the specified request with the parameters in this {@code FormData}.
     * @param webRequest the web request to fill
     */
    public void fillRequest(final WebRequest webRequest) {
        webRequest.setRequestBody(null);
        webRequest.setEncodingType(FormEncodingType.URL_ENCODED);

        if (getParams().size() > 0) {
            final List<com.gargoylesoftware.htmlunit.util.NameValuePair> params = new ArrayList<>();
            for (final NameValuePair entry : getParams()) {
                params.add(new com.gargoylesoftware.htmlunit.util.NameValuePair(entry.getName(), entry.getValue()));
            }
            webRequest.setRequestParameters(params);
        }
    }
}
