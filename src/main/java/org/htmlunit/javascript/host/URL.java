/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import static org.htmlunit.BrowserVersionFeatures.JS_ANCHOR_HOSTNAME_IGNORE_BLANK;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxConstructorAlias;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.configuration.JsxStaticFunction;
import org.htmlunit.javascript.host.file.Blob;
import org.htmlunit.javascript.host.file.File;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.UrlUtils;

/**
 * A JavaScript object for {@code URL}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author cd alexndr
 * @author Lai Quang Duong
 */
@JsxClass
public class URL extends HtmlUnitScriptable {

    private java.net.URL url_;

    /**
     * Creates an instance.
     * @param url a string representing an absolute or relative URL.
     *        If url is a relative URL, base is required, and will be used
     *        as the base URL. If url is an absolute URL, a given base will be ignored.
     * @param base a string representing the base URL to use in case url
     *        is a relative URL. If not specified, it defaults to ''.
     */
    @JsxConstructor
    @JsxConstructorAlias(alias = "webkitURL")
    public void jsConstructor(final String url, final Object base) {
        String baseStr = null;
        if (!JavaScriptEngine.isUndefined(base)) {
            baseStr = JavaScriptEngine.toString(base);
        }

        try {
            if (org.htmlunit.util.StringUtils.isBlank(baseStr)) {
                url_ = UrlUtils.toUrlUnsafe(url);
            }
            else {
                final java.net.URL baseUrl = UrlUtils.toUrlUnsafe(baseStr);
                url_ = UrlUtils.toUrlUnsafe(UrlUtils.resolveUrl(baseUrl, url));
            }
            url_ = UrlUtils.removeRedundantPort(url_);
        }
        catch (final MalformedURLException e) {
            throw JavaScriptEngine.typeError(e.toString());
        }
    }

    /**
     * The URL.createObjectURL() static method creates a DOMString containing a URL
     * representing the object given in parameter.
     * The URL lifetime is tied to the document in the window on which it was created.
     * The new object URL represents the specified File object or Blob object.
     *
     * @param fileOrBlob Is a File object or a Blob object to create a object URL for.
     * @return the url
     */
    @JsxStaticFunction
    public static String createObjectURL(final Object fileOrBlob) {
        if (fileOrBlob instanceof File) {
            final File file = (File) fileOrBlob;
            return getWindow(file).getDocument().generateBlobUrl(file);
        }

        if (fileOrBlob instanceof Blob) {
            final Blob blob = (Blob) fileOrBlob;
            return getWindow(blob).getDocument().generateBlobUrl(blob);
        }

        return null;
    }

    /**
     * @param objectURL String representing the object URL that was
     *          created by calling URL.createObjectURL().
     */
    @JsxStaticFunction
    public static void revokeObjectURL(final Scriptable objectURL) {
        getWindow(objectURL).getDocument().revokeBlobUrl(Context.toString(objectURL));
    }

    /**
     * @return hash property of the URL containing a '#' followed by the fragment identifier of the URL.
     */
    @JsxGetter
    public String getHash() {
        if (url_ == null) {
            return null;
        }
        final String ref = url_.getRef();
        return ref == null ? "" : "#" + ref;
    }

    /**
     * Sets the {@code hash} property.
     * @param fragment the {@code hash} property
     */
    @JsxSetter
    public void setHash(final String fragment) throws MalformedURLException {
        if (url_ == null) {
            return;
        }
        url_ = UrlUtils.getUrlWithNewRef(url_, org.htmlunit.util.StringUtils.isEmptyOrNull(fragment) ? null : fragment);
    }

    /**
     * @return the host, that is the hostname, and then, if the port of the URL is nonempty,
     *         a ':', followed by the port of the URL.
     */
    @JsxGetter
    public String getHost() {
        if (url_ == null) {
            return null;
        }
        final int port = url_.getPort();
        return url_.getHost() + (port > 0 ? ":" + port : "");
    }

    /**
     * Sets the {@code host} property.
     * @param host the {@code host} property
     */
    @JsxSetter
    public void setHost(final String host) throws MalformedURLException {
        if (url_ == null) {
            return;
        }

        String newHost = StringUtils.substringBefore(host, ':');
        if (org.htmlunit.util.StringUtils.isEmptyOrNull(newHost)) {
            return;
        }

        try {
            int ip = Integer.parseInt(newHost);
            final StringBuilder ipString = new StringBuilder();
            ipString.insert(0, ip % 256);
            ipString.insert(0, '.');

            ip = ip / 256;
            ipString.insert(0, ip % 256);
            ipString.insert(0, '.');

            ip = ip / 256;
            ipString.insert(0, ip % 256);
            ipString.insert(0, '.');
            ip = ip / 256;
            ipString.insert(0, ip % 256);

            newHost = ipString.toString();
        }
        catch (final Exception expected) {
            // back to string
        }

        url_ = UrlUtils.getUrlWithNewHost(url_, newHost);

        final String newPort = StringUtils.substringAfter(host, ':');
        if (org.htmlunit.util.StringUtils.isNotBlank(newHost)) {
            try {
                url_ = UrlUtils.getUrlWithNewHostAndPort(url_, newHost, Integer.parseInt(newPort));
            }
            catch (final Exception expected) {
                // back to string
            }
        }
        else {
            url_ = UrlUtils.getUrlWithNewHost(url_, newHost);
        }

        url_ = UrlUtils.removeRedundantPort(url_);
    }

    /**
     * @return the host, that is the hostname, and then, if the port of the URL is nonempty,
     *         a ':', followed by the port of the URL.
     */
    @JsxGetter
    public String getHostname() {
        if (url_ == null) {
            return null;
        }

        return UrlUtils.encodeAnchor(url_.getHost());
    }

    /**
     * Sets the {@code hostname} property.
     * @param hostname the {@code hostname} property
     */
    @JsxSetter
    public void setHostname(final String hostname) throws MalformedURLException {
        if (getBrowserVersion().hasFeature(JS_ANCHOR_HOSTNAME_IGNORE_BLANK)) {
            if (!org.htmlunit.util.StringUtils.isBlank(hostname)) {
                url_ = UrlUtils.getUrlWithNewHost(url_, hostname);
            }
        }
        else if (!org.htmlunit.util.StringUtils.isEmptyOrNull(hostname)) {
            url_ = UrlUtils.getUrlWithNewHost(url_, hostname);
        }
    }

    /**
     * @return whole URL
     */
    @JsxGetter
    public String getHref() {
        if (url_ == null) {
            return null;
        }

        return jsToString();
    }

    /**
     * Sets the {@code href} property.
     * @param href the {@code href} property
     */
    @JsxSetter
    public void setHref(final String href) throws MalformedURLException {
        if (url_ == null) {
            return;
        }

        url_ = UrlUtils.toUrlUnsafe(href);
        url_ = UrlUtils.removeRedundantPort(url_);
    }

    /**
     * @return the origin
     */
    @JsxGetter
    public Object getOrigin() {
        if (url_ == null) {
            return null;
        }

        if (url_.getPort() < 0 || url_.getPort() == url_.getDefaultPort()) {
            return url_.getProtocol() + "://" + url_.getHost();
        }

        return url_.getProtocol() + "://" + url_.getHost() + ':' + url_.getPort();
    }

    /**
     * @return a URLSearchParams object allowing access to the GET decoded query arguments contained in the URL.
     */
    @JsxGetter
    public URLSearchParams getSearchParams() {
        if (url_ == null) {
            return null;
        }

        final URLSearchParams searchParams = new URLSearchParams(this);
        searchParams.setParentScope(getParentScope());
        searchParams.setPrototype(getPrototype(searchParams.getClass()));
        return searchParams;
    }

    /**
     * @return the password specified before the domain name.
     */
    @JsxGetter
    public String getPassword() {
        if (url_ == null) {
            return null;
        }

        final String userInfo = url_.getUserInfo();
        final int idx = userInfo == null ? -1 : userInfo.indexOf(':');
        return idx == -1 ? "" : userInfo.substring(idx + 1);
    }

    /**
     * Sets the {@code password} property.
     * @param password the {@code password} property
     */
    @JsxSetter
    public void setPassword(final String password) throws MalformedURLException {
        if (url_ == null) {
            return;
        }

        url_ = UrlUtils.getUrlWithNewUserPassword(url_, password.isEmpty() ? null : password);
    }

    /**
     * @return a URLSearchParams object allowing access to the GET decoded query arguments contained in the URL.
     */
    @JsxGetter
    public String getPathname() {
        if (url_ == null) {
            return null;
        }

        final String path = url_.getPath();
        return path.isEmpty() ? "/" : path;
    }

    /**
     * Sets the {@code path} property.
     * @param path the {@code path} property
     */
    @JsxSetter
    public void setPathname(final String path) throws MalformedURLException {
        if (url_ == null) {
            return;
        }

        url_ = UrlUtils.getUrlWithNewPath(url_, path.startsWith("/") ? path : "/" + path);
    }

    /**
     * @return the port number of the URL. If the URL does not contain an explicit port number,
     *         it will be set to ''
     */
    @JsxGetter
    public String getPort() {
        if (url_ == null) {
            return null;
        }

        final int port = url_.getPort();
        return port == -1 ? "" : Integer.toString(port);
    }

    /**
     * Sets the {@code port} property.
     * @param port the {@code port} property
     */
    @JsxSetter
    public void setPort(final String port) throws MalformedURLException {
        if (url_ == null) {
            return;
        }
        final int portInt = port.isEmpty() ? -1 : Integer.parseInt(port);
        url_ = UrlUtils.getUrlWithNewPort(url_, portInt);
        url_ = UrlUtils.removeRedundantPort(url_);
    }

    /**
     * @return the protocol scheme of the URL, including the final ':'.
     */
    @JsxGetter
    public String getProtocol() {
        if (url_ == null) {
            return null;
        }
        final String protocol = url_.getProtocol();
        return protocol.isEmpty() ? "" : (protocol + ":");
    }

    /**
     * Sets the {@code protocol} property.
     * @param protocol the {@code protocol} property
     */
    @JsxSetter
    public void setProtocol(final String protocol) throws MalformedURLException {
        if (url_ == null || protocol.isEmpty()) {
            return;
        }

        final String bareProtocol = StringUtils.substringBefore(protocol, ":").trim();
        if (!UrlUtils.isValidScheme(bareProtocol)) {
            return;
        }
        if (!UrlUtils.isSpecialScheme(bareProtocol)) {
            return;
        }

        try {
            url_ = UrlUtils.getUrlWithNewProtocol(url_, bareProtocol);
            url_ = UrlUtils.removeRedundantPort(url_);
        }
        catch (final MalformedURLException ignored) {
            // ignore
        }
    }

    /**
     * @return the query string containing a '?' followed by the parameters of the URL
     */
    @JsxGetter
    public String getSearch() {
        if (url_ == null) {
            return null;
        }
        final String search = url_.getQuery();
        return search == null ? "" : "?" + search;
    }

    /**
     * Sets the {@code search} property.
     * @param search the {@code search} property
     */
    @JsxSetter
    public void setSearch(final String search) throws MalformedURLException {
        if (url_ == null) {
            return;
        }

        String query;
        if (search == null
                || org.htmlunit.util.StringUtils.equalsChar('?', search)
                || org.htmlunit.util.StringUtils.isEmptyString(search)) {
            query = null;
        }
        else {
            if (search.charAt(0) == '?') {
                query = search.substring(1);
            }
            else {
                query = search;
            }
            query = UrlUtils.encodeQuery(query);
        }

        url_ = UrlUtils.getUrlWithNewQuery(url_, query);
    }

    /**
     * Sets the {@code search} property based on {@link NameValuePair}'s.
     * @param nameValuePairs the pairs
     * @throws MalformedURLException in case of error
     */
    public void setSearch(final List<NameValuePair> nameValuePairs) throws MalformedURLException {
        final StringBuilder newSearch = new StringBuilder();
        for (final NameValuePair nameValuePair : nameValuePairs) {
            if (newSearch.length() > 0) {
                newSearch.append('&');
            }
            newSearch
                .append(UrlUtils.encodeQueryPart(nameValuePair.getName()))
                .append('=')
                .append(UrlUtils.encodeQueryPart(nameValuePair.getValue()));
        }

        url_ = UrlUtils.getUrlWithNewQuery(url_, newSearch.toString());
    }

    /**
     * @return the username specified before the domain name.
     */
    @JsxGetter
    public String getUsername() {
        if (url_ == null) {
            return null;
        }

        final String userInfo = url_.getUserInfo();
        if (userInfo == null) {
            return "";
        }

        return StringUtils.substringBefore(userInfo, ':');
    }

    /**
     * Sets the {@code username} property.
     * @param username the {@code username} property
     */
    @JsxSetter
    public void setUsername(final String username) throws MalformedURLException {
        if (url_ == null) {
            return;
        }
        url_ = UrlUtils.getUrlWithNewUserName(url_, username.isEmpty() ? null : username);
    }

    /**
     * Calls for instance for implicit conversion to string.
     * @see org.htmlunit.javascript.HtmlUnitScriptable#getDefaultValue(java.lang.Class)
     * @param hint the type hint
     * @return the default value
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (url_ == null) {
            return super.getDefaultValue(hint);
        }

        if (org.htmlunit.util.StringUtils.isEmptyOrNull(url_.getPath())) {
            return url_.toExternalForm() + "/";
        }
        return url_.toExternalForm();
    }

    /**
     * @return a serialized version of the URL,
     *         although in practice it seems to have the same effect as URL.toString().
     */
    @JsxFunction
    public String toJSON() {
        return jsToString();
    }

    /**
     * Returns the text of the URL.
     * @return the text
     */
    @JsxFunction(functionName = "toString")
    public String jsToString() {
        if (org.htmlunit.util.StringUtils.isEmptyOrNull(url_.getPath())) {
            try {
                return UrlUtils.getUrlWithNewPath(url_, "/").toExternalForm();
            }
            catch (final MalformedURLException e) {
                return url_.toExternalForm();
            }
        }
        return url_.toExternalForm();
    }
}
