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
package org.htmlunit.javascript.host;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.FormEncodingType;
import org.htmlunit.WebRequest;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ES6Iterator;
import org.htmlunit.corejs.javascript.EcmaError;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.IteratorLikeIterable;
import org.htmlunit.corejs.javascript.NativeObject;
import org.htmlunit.corejs.javascript.ScriptRuntime;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.SymbolKey;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.UrlUtils;

/**
 * A JavaScript object for {@code URLSearchParams}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Ween Jiann
 * @author cd alexndr
 * @author Lai Quang Duong
 */
@JsxClass
public class URLSearchParams extends HtmlUnitScriptable {

    private static final Log LOG = LogFactory.getLog(URLSearchParams.class);

    /** Constant used to register the prototype in the context. */
    public static final String URL_SEARCH_PARMS_TAG = "URLSearchParams";

    private URL url_;

    public static final class NativeParamsIterator extends ES6Iterator {
        enum Type { KEYS, VALUES, BOTH }

        private final Type type_;
        private final String className_;
        private final transient Iterator<NameValuePair> iterator_;

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

    /**
     * Constructs a new instance.
     */
    public URLSearchParams() {
    }

    /**
     * Constructs a new instance for the given js url.
     * @param url the base url
     */
    URLSearchParams(final URL url) {
        url_ = url;
    }

    /**
     * Constructs a new instance.
     * @param params the params string
     */
    @JsxConstructor
    public void jsConstructor(final Object params) {
        url_ = new URL();
        url_.jsConstructor("http://www.htmlunit.org", "");

        if (params == null || JavaScriptEngine.isUndefined(params)) {
            return;
        }

        try {
            url_.setSearch(resolveParams(params));
        }
        catch (final EcmaError e) {
            throw JavaScriptEngine.typeError("Failed to construct 'URLSearchParams': " + e.getErrorMessage());
        }
        catch (final MalformedURLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /*
     * Implementation follows https://url.spec.whatwg.org/#urlsearchparams-initialize
     */
    private static List<NameValuePair> resolveParams(final Object params) {
        // if params is a sequence
        if (params instanceof Scriptable && ScriptableObject.hasProperty((Scriptable) params, SymbolKey.ITERATOR)) {

            final Context cx = Context.getCurrentContext();
            final Scriptable paramsScriptable = (Scriptable) params;

            final List<NameValuePair> nameValuePairs = new ArrayList<>();

            try (IteratorLikeIterable itr = buildIteratorLikeIterable(cx, paramsScriptable)) {
                for (final Object nameValue : itr) {
                    if (!(nameValue instanceof Scriptable)) {
                        throw JavaScriptEngine.typeError("The provided value cannot be converted to a sequence.");
                    }
                    if (!ScriptableObject.hasProperty((Scriptable) nameValue, SymbolKey.ITERATOR)) {
                        throw JavaScriptEngine.typeError("The object must have a callable @@iterator property.");
                    }

                    try (IteratorLikeIterable nameValueItr = buildIteratorLikeIterable(cx, (Scriptable) nameValue)) {

                        final Iterator<Object> nameValueIterator = nameValueItr.iterator();
                        final Object name =
                                nameValueIterator.hasNext() ? nameValueIterator.next() : Scriptable.NOT_FOUND;
                        final Object value =
                                nameValueIterator.hasNext() ? nameValueIterator.next() : Scriptable.NOT_FOUND;

                        if (name == Scriptable.NOT_FOUND
                                || value == Scriptable.NOT_FOUND
                                || nameValueIterator.hasNext()) {
                            throw JavaScriptEngine.typeError("Sequence initializer must only contain pair elements.");
                        }

                        nameValuePairs.add(new NameValuePair(
                                JavaScriptEngine.toString(name),
                                JavaScriptEngine.toString(value)));
                    }
                }
            }

            return nameValuePairs;
        }

        // if params is a record
        if (params instanceof NativeObject) {
            final List<NameValuePair> nameValuePairs = new ArrayList<>();
            for (final Map.Entry<Object, Object> keyValuePair : ((NativeObject) params).entrySet()) {
                nameValuePairs.add(
                        new NameValuePair(
                                JavaScriptEngine.toString(keyValuePair.getKey()),
                                JavaScriptEngine.toString(keyValuePair.getValue())));
            }
            return nameValuePairs;
        }

        // otherwise handle it as string
        return splitQuery(JavaScriptEngine.toString(params));
    }

    private List<NameValuePair> splitQuery() {
        return splitQuery(url_.getSearch());
    }

    private static List<NameValuePair> splitQuery(String params) {
        final List<NameValuePair> splitted = new ArrayList<>();

        params = StringUtils.stripStart(params, "?");
        if (StringUtils.isEmpty(params)) {
            return splitted;
        }

        final String[] parts = StringUtils.split(params, '&');
        for (final String part : parts) {
            final NameValuePair pair = splitQueryParameter(part);
            splitted.add(new NameValuePair(UrlUtils.decode(pair.getName()), UrlUtils.decode(pair.getValue())));
        }
        return splitted;
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
        final String value = "";
        return new NameValuePair(singleParam, value);
    }

    private static IteratorLikeIterable buildIteratorLikeIterable(final Context cx, final Scriptable iterable) {
        final Object iterator = ScriptRuntime.callIterator(iterable, cx, iterable.getParentScope());
        return new IteratorLikeIterable(cx, iterable.getParentScope(), iterator);
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
        final String search = url_.getSearch();

        final List<NameValuePair> pairs;
        if (search == null || search.isEmpty()) {
            pairs = new ArrayList<>(1);
        }
        else {
            pairs = splitQuery(search);
        }

        pairs.add(new NameValuePair(name, value));
        try {
            url_.setSearch(pairs);
        }
        catch (final MalformedURLException e) {
            LOG.error(e.getMessage(), e);
        }
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
        final List<NameValuePair> splitted = splitQuery();
        splitted.removeIf(entry -> entry.getName().equals(name));

        if (splitted.size() == 0) {
            try {
                url_.setSearch((String) null);
            }
            catch (final MalformedURLException e) {
                LOG.error(e.getMessage(), e);
            }
            return;
        }

        try {
            url_.setSearch(splitted);
        }
        catch (final MalformedURLException e) {
            LOG.error(e.getMessage(), e);
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
        final List<NameValuePair> splitted = splitQuery();
        for (final NameValuePair param : splitted) {
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
        final List<NameValuePair> splitted = splitQuery();
        final List<String> result = new ArrayList<>(splitted.size());
        for (final NameValuePair param : splitted) {
            if (param.getName().equals(name)) {
                result.add(param.getValue());
            }
        }

        return JavaScriptEngine.newArray(getWindow(this), result.toArray());
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
        final List<NameValuePair> splitted = splitQuery();

        boolean change = true;
        final ListIterator<NameValuePair> iter = splitted.listIterator();
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
            splitted.add(new NameValuePair(name, value));
        }

        try {
            url_.setSearch(splitted);
        }
        catch (final MalformedURLException e) {
            LOG.error(e.getMessage(), e);
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
        final List<NameValuePair> splitted = splitQuery();

        for (final NameValuePair param : splitted) {
            if (param.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The URLSearchParams.forEach() method allows iteration through
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

        String currentSearch = null;
        List<NameValuePair> params = null;
        // This must be indexes instead of iterator() for correct behavior when of list changes while iterating
        for (int i = 0;; i++) {
            final String search = url_.getSearch();
            if (!search.equals(currentSearch)) {
                params = splitQuery(search);
                currentSearch = search;
            }
            if (i >= params.size()) {
                break;
            }

            final NameValuePair param = params.get(i);
            fun.call(Context.getCurrentContext(), getParentScope(), this,
                        new Object[] {param.getValue(), param.getName(), this});
        }
    }

    /**
     * The URLSearchParams.entries() method returns an iterator allowing to go through
     * all key/value pairs contained in this object. The key and value of each pair
     * are USVString objects.
     *
     * @return an iterator.
     */
    @JsxFunction
    @JsxSymbol(value = {CHROME, EDGE, FF, FF_ESR}, symbolName = "iterator")
    public Object entries() {
        final List<NameValuePair> splitted = splitQuery();

        return new NativeParamsIterator(getParentScope(),
                "URLSearchParams Iterator", NativeParamsIterator.Type.BOTH, splitted.iterator());
    }

    /**
     * The URLSearchParams.keys() method returns an iterator allowing to go through
     * all keys contained in this object. The keys are USVString objects.
     *
     * @return an iterator.
     */
    @JsxFunction
    public Object keys() {
        final List<NameValuePair> splitted = splitQuery();

        return new NativeParamsIterator(getParentScope(),
                "URLSearchParams Iterator", NativeParamsIterator.Type.KEYS, splitted.iterator());
    }

    /**
     * The URLSearchParams.values() method returns an iterator allowing to go through
     * all values contained in this object. The values are USVString objects.
     *
     * @return an iterator.
     */
    @JsxFunction
    public Object values() {
        final List<NameValuePair> splitted = splitQuery();

        return new NativeParamsIterator(getParentScope(),
                "URLSearchParams Iterator", NativeParamsIterator.Type.VALUES, splitted.iterator());
    }

    /**
     * @return the total number of search parameter entries
     */
    @JsxGetter
    public int getSize() {
        final List<NameValuePair> splitted = splitQuery();
        return splitted.size();
    }

    /**
     * @return the text of the URLSearchParams
     */
    @JsxFunction(functionName = "toString")
    public String jsToString() {
        final StringBuilder newSearch = new StringBuilder();
        for (final NameValuePair nameValuePair : splitQuery(url_.getSearch())) {
            if (newSearch.length() > 0) {
                newSearch.append('&');
            }
            newSearch
                .append(UrlUtils.encodeQueryPart(nameValuePair.getName()))
                .append('=')
                .append(UrlUtils.encodeQueryPart(nameValuePair.getValue()));
        }

        return newSearch.toString();
    }

    /**
     * Calls for instance for implicit conversion to string.
     * @see org.htmlunit.javascript.HtmlUnitScriptable#getDefaultValue(java.lang.Class)
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

        final List<NameValuePair> splitted = splitQuery();
        if (splitted.size() > 0) {
            final List<NameValuePair> params = new ArrayList<>();
            for (final NameValuePair entry : splitted) {
                params.add(new NameValuePair(entry.getName(), entry.getValue()));
            }
            webRequest.setRequestParameters(params);
        }
    }
}
