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

import static org.htmlunit.BrowserVersionFeatures.JS_REQUEST_HEADER_USER_AGENT_FORBIDDEN;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.htmlunit.BrowserVersion;
import org.htmlunit.HttpHeader;
import org.htmlunit.corejs.javascript.ClassDescriptor;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ES6Iterator;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.IteratorLikeIterable;
import org.htmlunit.corejs.javascript.ScriptRuntime;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
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
 * @see <a href="https://fetch.spec.whatwg.org/#headers-class">Fetch Standard - Headers class</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class Headers extends HtmlUnitScriptable {

    private static final String ITERATOR_TAG = "Headers Iterator";

    private static final String SET_COOKIE = "set-cookie";

    // https://fetch.spec.whatwg.org/#forbidden-request-header
    private static final String[] FORBIDDEN_REQUEST_HEADERS = {
        "accept-charset", "accept-encoding", "access-control-request-headers", "access-control-request-method",
        "connection", "content-length", "cookie", "cookie2", "date", "dnt", "expect", "host", "keep-alive",
        "origin", "referer", "set-cookie", "te", "trailer", "transfer-encoding", "upgrade", "via"
    };

    // https://fetch.spec.whatwg.org/#forbidden-request-header (the method override headers)
    private static final String[] METHOD_OVERRIDE_HEADERS = {
        "x-http-method", "x-http-method-override", "x-method-override"
    };

    // https://fetch.spec.whatwg.org/#forbidden-method
    private static final String[] FORBIDDEN_METHODS = {"connect", "trace", "track"};

    /**
     * The guard of a headers object; determines which changes are allowed.
     * @see <a href="https://fetch.spec.whatwg.org/#concept-headers-guard">Fetch Standard - headers guard</a>
     */
    public enum Guard {
        /** All changes are allowed. */
        NONE,
        /** The headers of a request; changes of forbidden request headers are ignored. */
        REQUEST,
        /**
         * The headers of a request in mode 'no-cors'; only the no-CORS-safelisted request headers
         * (with safe values) can be changed.
         */
        REQUEST_NO_CORS,
        /** The headers of a response; changes of forbidden response headers are ignored. */
        RESPONSE,
        /** No changes are allowed. */
        IMMUTABLE
    }

    // the header list; the names as given (the case is ignored for all operations)
    private final List<NameValuePair> headerList_ = new ArrayList<>();
    private Guard guard_ = Guard.NONE;

    /**
     * {@link ES6Iterator} implementation for the iterators of {@link Headers}.
     */
    public static final class HeadersIterator extends ES6Iterator {

        private static final ClassDescriptor DESCRIPTOR = ES6Iterator.makeDescriptor(ITERATOR_TAG, ITERATOR_TAG);

        enum Type { KEYS, VALUES, BOTH }

        private final Type type_;
        private final Headers headers_;
        private int index_;

        /**
         * Initializes the iterator prototype.
         *
         * @param cx the {@link Context}
         * @param scope the scope
         */
        public static void init(final Context cx, final TopLevel scope) {
            ES6Iterator.initialize(DESCRIPTOR, cx, scope, new HeadersIterator(), false, ITERATOR_TAG);
        }

        /**
         * Creates an empty instance (used for the prototype initialization).
         */
        public HeadersIterator() {
            super();
            headers_ = null;
            type_ = Type.BOTH;
        }

        /**
         * Creates an instance for the given headers; like the browsers, the iterator works on the
         * current state of the headers (the sorted and combined list is determined for every step).
         *
         * @param scope the scope
         * @param type the iteration type (keys, values, or both)
         * @param headers the headers
         */
        HeadersIterator(final VarScope scope, final Type type, final Headers headers) {
            super(scope, ITERATOR_TAG);
            headers_ = headers;
            type_ = type;
        }

        @Override
        public String getClassName() {
            return ITERATOR_TAG;
        }

        @Override
        protected boolean isDone(final Context cx, final VarScope scope) {
            return headers_ == null || index_ >= headers_.sortAndCombine().size();
        }

        @Override
        protected Object nextValue(final Context cx, final VarScope scope) {
            final NameValuePair e = headers_.sortAndCombine().get(index_);
            index_++;
            return switch (type_) {
                case KEYS -> e.getName();
                case VALUES -> e.getValue();
                case BOTH -> cx.newArray(scope, new Object[] {e.getName(), e.getValue()});
            };
        }
    }

    /**
     * Default constructor.
     */
    public Headers() {
        super();
    }

    /**
     * JavaScript constructor.
     *
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments (the optional initial headers: a {@link Headers} object,
     *        a sequence of name/value pairs or a record)
     * @param ctorObj the function object
     * @param inNewExpr whether invoked via {@code new}
     * @return the new {@code Headers} instance
     */
    @JsxConstructor
    public static Headers jsConstructor(final Context cx, final VarScope scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        if (!inNewExpr) {
            throw JavaScriptEngine.typeError("Failed to construct 'Headers': Please use the 'new' operator, "
                    + "this DOM object constructor cannot be called as a function.");
        }

        final Headers headers = new Headers();
        headers.setParentScope(getTopLevelScope(scope));
        headers.setPrototype(((FunctionObject) ctorObj).getClassPrototype());

        if (args.length > 0 && !JavaScriptEngine.isUndefined(args[0])) {
            headers.fill(args[0]);
        }
        return headers;
    }

    /**
     * Fills this object with the given headers.
     *
     * @param init the headers: a {@link Headers} object, a sequence of name/value pairs or a record
     * @see <a href="https://fetch.spec.whatwg.org/#concept-headers-fill">Fetch Standard - fill</a>
     */
    void fill(final Object init) {
        if (init instanceof Headers headers) {
            for (final NameValuePair header : headers.headerList_) {
                appendHeader(header.getName(), header.getValue());
            }
            return;
        }

        if (!(init instanceof Scriptable initScriptable)) {
            throw JavaScriptEngine.typeError("Failed to construct 'Headers': The provided value is not of type "
                    + "'(record<ByteString, ByteString> or sequence<sequence<ByteString>>)'.");
        }

        if (ScriptableObject.hasProperty(initScriptable, SymbolKey.ITERATOR)) {
            final Context cx = Context.getCurrentContext();
            JavaScriptEngine.iterate(cx, initScriptable.getParentScope(), initScriptable,
                    pair -> {
                        if (!(pair instanceof Scriptable pairScriptable)
                                || !ScriptableObject.hasProperty(pairScriptable, SymbolKey.ITERATOR)) {
                            throw JavaScriptEngine.typeError(
                                "Failed to construct 'Headers': The provided value cannot be converted to a sequence.");
                        }

                        final List<Object> items = new ArrayList<>();
                        try (IteratorLikeIterable pairItr = buildIteratorLikeIterable(cx, pairScriptable)) {
                            for (final Object item : pairItr) {
                                items.add(item);
                            }
                        }
                        if (items.size() != 2) {
                            throw JavaScriptEngine.typeError("Failed to construct 'Headers': Invalid value");
                        }
                        appendHeader(JavaScriptEngine.toString(items.get(0)), JavaScriptEngine.toString(items.get(1)));
                    });

            return;
        }

        // a record: the own enumerable properties
        for (final Object id : initScriptable.getIds()) {
            final String name = JavaScriptEngine.toString(id);
            final Object value = id instanceof Integer index
                    ? initScriptable.get(index, initScriptable)
                    : initScriptable.get(name, initScriptable);
            appendHeader(name, JavaScriptEngine.toString(value));
        }
    }

    private static IteratorLikeIterable buildIteratorLikeIterable(final Context cx, final Scriptable iterable) {
        final Object iterator = ScriptRuntime.callIterator(iterable, cx, iterable.getParentScope());
        return new IteratorLikeIterable(cx, iterable.getParentScope(), iterator);
    }

    /**
     * Appends a new value to an existing header, or adds the header if it does not exist.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments (name and value)
     * @param funObj the function
     */
    @JsxFunction
    public static void append(final Context cx, final VarScope scope, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        checkArguments("append", args, 2);
        ((Headers) thisObj).appendHeader(JavaScriptEngine.toString(args[0]), JavaScriptEngine.toString(args[1]));
    }

    /**
     * Sets the value of a header (replaces all existing values), or adds the header if it does not exist.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments (name and value)
     * @param funObj the function
     */
    @JsxFunction(functionName = "set")
    public static void jsSet(final Context cx, final VarScope scope, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        checkArguments("set", args, 2);
        ((Headers) thisObj).setHeader(JavaScriptEngine.toString(args[0]), JavaScriptEngine.toString(args[1]));
    }

    /**
     * Deletes a header.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments (name)
     * @param funObj the function
     */
    @JsxFunction(functionName = "delete")
    public static void jsDelete(final Context cx, final VarScope scope, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        checkArguments("delete", args, 1);
        ((Headers) thisObj).deleteHeader(JavaScriptEngine.toString(args[0]));
    }

    /**
     * Returns the combined values of a header.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments (name)
     * @param funObj the function
     * @return the values separated by ', ' or {@code null} if the header does not exist
     */
    @JsxFunction(functionName = "get")
    public static String jsGet(final Context cx, final VarScope scope, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        checkArguments("get", args, 1);
        return ((Headers) thisObj).getHeader(JavaScriptEngine.toString(args[0]));
    }

    /**
     * Returns whether a header exists.
     *
     * @param cx the current context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments (name)
     * @param funObj the function
     * @return whether the header exists
     */
    @JsxFunction(functionName = "has")
    public static boolean jsHas(final Context cx, final VarScope scope, final Scriptable thisObj,
            final Object[] args, final Function funObj) {
        checkArguments("has", args, 1);
        final String name = JavaScriptEngine.toString(args[0]);
        validateName(name);
        return ((Headers) thisObj).contains(name);
    }

    /**
     * Returns the values of all Set-Cookie headers.
     *
     * @return the values
     */
    @JsxFunction
    public Scriptable getSetCookie() {
        final List<Object> values = new ArrayList<>();
        for (final NameValuePair header : headerList_) {
            if (SET_COOKIE.equalsIgnoreCase(header.getName())) {
                values.add(header.getValue());
            }
        }
        return JavaScriptEngine.newArray(getParentScope(), values.toArray());
    }

    /**
     * Calls the callback for every header (sorted, combined; the names in lower case).
     *
     * @param callback the function to call with the value, the name and this object
     * @param thisArg the value to use as {@code this} for the callback
     */
    @JsxFunction
    public void forEach(final Object callback, final Object thisArg) {
        if (!(callback instanceof Function fun)) {
            throw JavaScriptEngine.typeError("Failed to execute 'forEach' on 'Headers': "
                    + "parameter 1 is not of type 'Function'.");
        }

        final Scriptable thisScriptable = thisArg instanceof Scriptable s ? s : null;
        // like the browsers, the list is determined again for every step
        for (int i = 0;; i++) {
            final List<NameValuePair> list = sortAndCombine();
            if (i >= list.size()) {
                break;
            }
            final NameValuePair header = list.get(i);
            fun.call(Context.getCurrentContext(), getParentScope(), thisScriptable,
                        new Object[] {header.getValue(), header.getName(), this});
        }
    }

    /**
     * Returns an iterator over all name/value pairs (sorted, combined; the names in lower case).
     *
     * @return the iterator
     */
    @JsxFunction
    @JsxSymbol(symbolName = "iterator")
    public ES6Iterator entries() {
        return new HeadersIterator(getParentScope(), HeadersIterator.Type.BOTH, this);
    }

    /**
     * Returns an iterator over all names (sorted, in lower case).
     *
     * @return the iterator
     */
    @JsxFunction
    public ES6Iterator keys() {
        return new HeadersIterator(getParentScope(), HeadersIterator.Type.KEYS, this);
    }

    /**
     * Returns an iterator over all values (sorted by the names, combined).
     *
     * @return the iterator
     */
    @JsxFunction
    public ES6Iterator values() {
        return new HeadersIterator(getParentScope(), HeadersIterator.Type.VALUES, this);
    }

    /**
     * Appends a header after normalizing and validating name and value.
     *
     * @param name the name
     * @param value the value
     * @see <a href="https://fetch.spec.whatwg.org/#concept-headers-append">Fetch Standard - append</a>
     */
    public void appendHeader(final String name, final String value) {
        final String normalized = normalizeValue(value);
        validateName(name);
        validateValue(normalized);
        if (isIgnoredByGuard(name, normalized)) {
            return;
        }
        if (guard_ == Guard.REQUEST_NO_CORS) {
            final String existing = getHeader(name);
            if (!isNoCorsSafelistedRequestHeader(name, existing == null ? normalized : existing + ", " + normalized)) {
                return;
            }
        }
        headerList_.add(new NameValuePair(name, normalized));
    }

    /**
     * Sets a header after normalizing and validating name and value.
     *
     * @param name the name
     * @param value the value
     * @see <a href="https://fetch.spec.whatwg.org/#dom-headers-set">Fetch Standard - set</a>
     */
    public void setHeader(final String name, final String value) {
        final String normalized = normalizeValue(value);
        validateName(name);
        validateValue(normalized);
        if (isIgnoredByGuard(name, normalized)) {
            return;
        }
        if (guard_ == Guard.REQUEST_NO_CORS && !isNoCorsSafelistedRequestHeader(name, normalized)) {
            return;
        }

        // replace the first one and remove all others
        boolean replaced = false;
        for (int i = 0; i < headerList_.size(); i++) {
            if (headerList_.get(i).getName().equalsIgnoreCase(name)) {
                if (replaced) {
                    headerList_.remove(i);
                    i--;
                }
                else {
                    headerList_.set(i, new NameValuePair(headerList_.get(i).getName(), normalized));
                    replaced = true;
                }
            }
        }
        if (!replaced) {
            headerList_.add(new NameValuePair(name, normalized));
        }
    }

    /**
     * Deletes all values of a header.
     *
     * @param name the name
     */
    public void deleteHeader(final String name) {
        validateName(name);
        if (isIgnoredByGuard(name, "")) {
            return;
        }
        if (guard_ == Guard.REQUEST_NO_CORS && !isNoCorsSafelistedRequestHeaderName(name)) {
            return;
        }
        headerList_.removeIf(header -> header.getName().equalsIgnoreCase(name));
    }

    /**
     * Returns the combined values of a header.
     *
     * @param name the name
     * @return the values separated by ', ' or {@code null} if the header does not exist
     */
    public String getHeader(final String name) {
        validateName(name);
        StringBuilder result = null;
        for (final NameValuePair header : headerList_) {
            if (header.getName().equalsIgnoreCase(name)) {
                if (result == null) {
                    result = new StringBuilder(header.getValue());
                }
                else {
                    result.append(", ").append(header.getValue());
                }
            }
        }
        return result == null ? null : result.toString();
    }

    private boolean contains(final String name) {
        for (final NameValuePair header : headerList_) {
            if (header.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the header list (the names as given).
     *
     * @return the header list
     */
    public List<NameValuePair> getHeaderList() {
        return Collections.unmodifiableList(headerList_);
    }

    /**
     * Returns the guard.
     *
     * @return the guard
     */
    public Guard getGuard() {
        return guard_;
    }

    /**
     * Sets the guard.
     *
     * @param guard the guard
     */
    public void setGuard(final Guard guard) {
        guard_ = guard;
    }

    /**
     * Returns the headers sorted by name, with the names in lower case and the values of a name combined;
     * the Set-Cookie headers are not combined.
     *
     * @return the sorted and combined list
     * @see <a href="https://fetch.spec.whatwg.org/#concept-header-list-sort-and-combine">Fetch Standard -
     *     sort and combine</a>
     */
    List<NameValuePair> sortAndCombine() {
        final TreeSet<String> names = new TreeSet<>();
        for (final NameValuePair header : headerList_) {
            names.add(header.getName().toLowerCase(Locale.ROOT));
        }

        final List<NameValuePair> result = new ArrayList<>();
        for (final String name : names) {
            if (SET_COOKIE.equals(name)) {
                for (final NameValuePair header : headerList_) {
                    if (SET_COOKIE.equalsIgnoreCase(header.getName())) {
                        result.add(new NameValuePair(name, header.getValue()));
                    }
                }
            }
            else {
                result.add(new NameValuePair(name, getHeader(name)));
            }
        }
        return result;
    }

    private boolean isIgnoredByGuard(final String name, final String value) {
        switch (guard_) {
            case IMMUTABLE:
                throw JavaScriptEngine.typeError("Headers are immutable");
            case REQUEST, REQUEST_NO_CORS:
                return isForbiddenRequestHeader(name, value, getBrowserVersion());
            case RESPONSE:
                return SET_COOKIE.equalsIgnoreCase(name) || "set-cookie2".equalsIgnoreCase(name);
            default:
                return false;
        }
    }

    private static boolean isNoCorsSafelistedRequestHeaderName(final String name) {
        final String lcName = name.toLowerCase(Locale.ROOT);
        return "accept".equals(lcName) || "accept-language".equals(lcName)
                || "content-language".equals(lcName) || "content-type".equals(lcName);
    }

    /**
     * Returns whether the header is a no-CORS-safelisted request header (name and value).
     *
     * @param name the name
     * @param value the value
     * @return whether the header is a no-CORS-safelisted request header
     * @see <a href="https://fetch.spec.whatwg.org/#no-cors-safelisted-request-header">Fetch Standard -
     *     no-CORS-safelisted request-header</a>
     */
    static boolean isNoCorsSafelistedRequestHeader(final String name, final String value) {
        if (!isNoCorsSafelistedRequestHeaderName(name)) {
            return false;
        }
        return isCorsSafelistedRequestHeader(name, value);
    }

    /**
     * Returns whether the header is a CORS-safelisted request header (name and value); requests
     * with other headers need a CORS preflight. In addition to the no-CORS-safelisted request headers
     * a Range header with a simple range (e.g. 'bytes=0-499') is safelisted.
     *
     * @param name the name
     * @param value the value
     * @return whether the header is a CORS-safelisted request header
     * @see <a href="https://fetch.spec.whatwg.org/#cors-safelisted-request-header">Fetch Standard -
     *     CORS-safelisted request-header</a>
     */
    static boolean isCorsSafelistedRequestHeader(final String name, final String value) {
        if (value.length() > 128) {
            return false;
        }

        final String lcName = name.toLowerCase(Locale.ROOT);
        if ("range".equals(lcName)) {
            return isSimpleRangeHeaderValue(value);
        }
        if (!isNoCorsSafelistedRequestHeaderName(lcName)) {
            return false;
        }

        if ("accept-language".equals(lcName) || "content-language".equals(lcName)) {
            for (int i = 0; i < value.length(); i++) {
                final char c = value.charAt(i);
                if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z'
                        || " *,-.;=".indexOf(c) > -1)) {
                    return false;
                }
            }
            return true;
        }

        // accept and content-type: no CORS-unsafe request-header bytes
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            if (c < 0x20 && c != '\t' || c == 0x7F || "\"():<>?@[\\]{}".indexOf(c) > -1) {
                return false;
            }
        }

        if ("content-type".equals(lcName)) {
            int end = value.indexOf(';');
            if (end < 0) {
                end = value.length();
            }
            final String essence = value.substring(0, end).trim().toLowerCase(Locale.ROOT);
            return "application/x-www-form-urlencoded".equals(essence)
                    || "multipart/form-data".equals(essence)
                    || "text/plain".equals(essence);
        }
        return true;
    }

    /**
     * Returns whether the value is a simple range header value: 'bytes=' (lower case) followed by a start
     * position, '-' and an optional end position (no whitespace, a single range, the start is required).
     *
     * @see <a href="https://fetch.spec.whatwg.org/#simple-range-header-value">Fetch Standard -
     *     simple range header value</a>
     */
    private static boolean isSimpleRangeHeaderValue(final String value) {
        // like the browsers, the unit is case-sensitive
        if (!value.startsWith("bytes=")) {
            return false;
        }
        final String range = value.substring(6);
        final int dash = range.indexOf('-');
        if (dash < 1 || !isDigits(range.substring(0, dash))) {
            return false;
        }
        final String end = range.substring(dash + 1);
        if (end.isEmpty()) {
            return true;
        }
        if (!isDigits(end)) {
            return false;
        }
        // the end has to be greater or equal than the start
        return new BigInteger(end).compareTo(new BigInteger(range.substring(0, dash))) >= 0;
    }

    private static boolean isDigits(final String value) {
        if (value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns whether the header is a forbidden request header; used by {@code fetch()} and
     * by {@code XMLHttpRequest}.
     * <p>
     * Forbidden are the names of the list of the Fetch standard, the names starting with
     * 'proxy-' or 'sec-' and the method override headers (X-HTTP-Method, X-HTTP-Method-Override,
     * X-Method-Override) if one of the values is a forbidden method (CONNECT, TRACE, TRACK).
     * </p>
     *
     * Like Chrome/Edge, the User-Agent header is forbidden also (feature
     * {@code JS_REQUEST_HEADER_USER_AGENT_FORBIDDEN}).
     *
     * @param name the name
     * @param value the value
     * @param browserVersion the browser version
     * @return whether the header is a forbidden request header
     * @see <a href="https://fetch.spec.whatwg.org/#forbidden-request-header">Fetch Standard -
     *     forbidden request-header</a>
     */
    public static boolean isForbiddenRequestHeader(final String name, final String value,
            final BrowserVersion browserVersion) {
        final String lcName = name.toLowerCase(Locale.ROOT);
        if (HttpHeader.USER_AGENT_LC.equals(lcName)
                && browserVersion.hasFeature(JS_REQUEST_HEADER_USER_AGENT_FORBIDDEN)) {
            return true;
        }
        for (final String forbidden : FORBIDDEN_REQUEST_HEADERS) {
            if (forbidden.equals(lcName)) {
                return true;
            }
        }
        if (lcName.startsWith("proxy-") || lcName.startsWith("sec-")) {
            return true;
        }

        for (final String override : METHOD_OVERRIDE_HEADERS) {
            if (override.equals(lcName)) {
                for (final String method : value.split(",")) {
                    final String lcMethod = normalizeValue(method).toLowerCase(Locale.ROOT);
                    for (final String forbiddenMethod : FORBIDDEN_METHODS) {
                        if (forbiddenMethod.equals(lcMethod)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    private static void checkArguments(final String function, final Object[] args, final int required) {
        if (args.length < required) {
            throw JavaScriptEngine.typeError("Failed to execute '" + function + "' on 'Headers': "
                    + required + " argument" + (required > 1 ? "s" : "") + " required, but only "
                    + args.length + " present.");
        }
    }

    /**
     * Removes the leading and trailing HTTP whitespace (tab, line feed, carriage return and space).
     */
    private static String normalizeValue(final String value) {
        int start = 0;
        int end = value.length();
        while (start < end && isHttpWhitespace(value.charAt(start))) {
            start++;
        }
        while (end > start && isHttpWhitespace(value.charAt(end - 1))) {
            end--;
        }
        return value.substring(start, end);
    }

    private static boolean isHttpWhitespace(final char c) {
        return c == '\t' || c == '\n' || c == '\r' || c == ' ';
    }

    /**
     * Validates the name: a non empty token (RFC 9110).
     */
    private static void validateName(final String name) {
        if (name.isEmpty()) {
            throw JavaScriptEngine.typeError("Invalid name");
        }
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            if (!isTokenChar(c)) {
                throw JavaScriptEngine.typeError("Invalid name");
            }
        }
    }

    private static boolean isTokenChar(final char c) {
        if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
            return true;
        }
        return "!#$%&'*+-.^_`|~".indexOf(c) >= 0;
    }

    /**
     * Validates the (normalized) value: no NUL, line feed or carriage return and only bytes (ByteString).
     */
    private static void validateValue(final String value) {
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            if (c == 0 || c == '\n' || c == '\r' || c > 0xFF) {
                throw JavaScriptEngine.typeError("Invalid value");
            }
        }
    }
}
