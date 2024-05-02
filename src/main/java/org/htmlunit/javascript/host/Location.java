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

import static org.htmlunit.BrowserVersionFeatures.JS_LOCATION_RELOAD_REFERRER;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.BrowserVersion;
import org.htmlunit.Page;
import org.htmlunit.WebRequest;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.HashChangeEvent;
import org.htmlunit.protocol.javascript.JavaScriptURLConnection;
import org.htmlunit.util.UrlUtils;

/**
 * A JavaScript object for {@code Location}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Michael Ottati
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Daniel Gredler
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Adam Afeltowicz
 * @author Atsushi Nakagawa
 * @author Kanoko Yamamoto
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535866.aspx">MSDN Documentation</a>
 */
@JsxClass
public class Location extends HtmlUnitScriptable {

    private static final Log LOG = LogFactory.getLog(Location.class);
    private static final String UNKNOWN = "null";

    /**
     * The window which owns this location object.
     */
    private Window window_;

    private static final Method methodAssign;
    private static final Method methodReload;
    private static final Method methodReplace;
    private static final Method methodToString;

    private static final Method getterHash;
    private static final Method setterHash;

    private static final Method getterHost;
    private static final Method setterHost;

    private static final Method getterHostname;
    private static final Method setterHostname;

    private static final Method getterHref;
    private static final Method setterHref;

    private static final Method getterOrigin;

    private static final Method getterPathname;
    private static final Method setterPathname;

    private static final Method getterPort;
    private static final Method setterPort;

    private static final Method getterProtocol;
    private static final Method setterProtocol;

    private static final Method getterSearch;
    private static final Method setterSearch;

    static {
        try {
            methodAssign = Location.class.getDeclaredMethod("assign", String.class);
            methodReload = Location.class.getDeclaredMethod("reload", boolean.class);
            methodReplace = Location.class.getDeclaredMethod("replace", String.class);
            methodToString = Location.class.getDeclaredMethod("jsToString");

            getterHash = Location.class.getDeclaredMethod("getHash");
            setterHash = Location.class.getDeclaredMethod("setHash", String.class);

            getterHost = Location.class.getDeclaredMethod("getHost");
            setterHost = Location.class.getDeclaredMethod("setHost", String.class);

            getterHostname = Location.class.getDeclaredMethod("getHostname");
            setterHostname = Location.class.getDeclaredMethod("setHostname", String.class);

            getterHref = Location.class.getDeclaredMethod("getHref");
            setterHref = Location.class.getDeclaredMethod("setHref", String.class);

            getterOrigin = Location.class.getDeclaredMethod("getOrigin");

            getterPathname = Location.class.getDeclaredMethod("getPathname");
            setterPathname = Location.class.getDeclaredMethod("setPathname", String.class);

            getterPort = Location.class.getDeclaredMethod("getPort");
            setterPort = Location.class.getDeclaredMethod("setPort", String.class);

            getterProtocol = Location.class.getDeclaredMethod("getProtocol");
            setterProtocol = Location.class.getDeclaredMethod("setProtocol", String.class);

            getterSearch = Location.class.getDeclaredMethod("getSearch");
            setterSearch = Location.class.getDeclaredMethod("setSearch", String.class);
        }
        catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The current hash; we cache it locally because we don't want to modify the page's URL and
     * force a page reload each time this changes.
     */
    private String hash_;

    /**
     * Creates an instance.
     */
    public Location() {
    }

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public void jsConstructor() {
        final int attributes = ScriptableObject.PERMANENT | ScriptableObject.READONLY;

        FunctionObject functionObject = new FunctionObject(methodAssign.getName(), methodAssign, this);
        defineProperty(methodAssign.getName(), functionObject, attributes);

        functionObject = new FunctionObject(methodReload.getName(), methodReload, this);
        defineProperty(methodReload.getName(), functionObject, attributes);

        functionObject = new FunctionObject(methodReplace.getName(), methodReplace, this);
        defineProperty(methodReplace.getName(), functionObject, attributes);

        functionObject = new FunctionObject(methodToString.getName(), methodToString, this);
        defineProperty("toString", functionObject, attributes);

        defineProperty("hash", null, getterHash, setterHash, attributes);
        defineProperty("host", null, getterHost, setterHost, attributes);
        defineProperty("hostname", null, getterHostname, setterHostname, attributes);
        defineProperty("href", null, getterHref, setterHref, attributes);
        defineProperty("origin", null, getterOrigin, null, attributes);
        defineProperty("pathname", null, getterPathname, setterPathname, attributes);
        defineProperty("port", null, getterPort, setterPort, attributes);
        defineProperty("protocol", null, getterProtocol, setterProtocol, attributes);
        defineProperty("search", null, getterSearch, setterSearch, attributes);
    }

    /**
     * Initializes this Location.
     *
     * @param window the window that this location belongs to
     * @param page the page that will become the enclosing page
     */
    public void initialize(final Window window, final Page page) {
        window_ = window;
        if (window_ != null && page != null) {
            setHash(null, page.getUrl().getRef());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (getPrototype() != null
                && window_ != null
                && (hint == null || String.class.equals(hint))) {
            return getHref();
        }
        return super.getDefaultValue(hint);
    }

    /**
     * Loads the new HTML document corresponding to the specified URL.
     * @param url the location of the new HTML document to load
     * @throws IOException if loading the specified location fails
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536342.aspx">MSDN Documentation</a>
     */
    public void assign(final String url) throws IOException {
        setHref(url);
    }

    /**
     * Reloads the current page, possibly forcing retrieval from the server even if
     * the browser cache contains the latest version of the document.
     * @param force if {@code true}, force reload from server; otherwise, may reload from cache
     * @throws IOException if there is a problem reloading the page
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536342.aspx">MSDN Documentation</a>
     */
    public void reload(final boolean force) throws IOException {
        final WebWindow webWindow = window_.getWebWindow();
        final HtmlPage htmlPage = (HtmlPage) webWindow.getEnclosedPage();
        final WebRequest request = htmlPage.getWebResponse().getWebRequest();

        if (webWindow.getWebClient().getBrowserVersion().hasFeature(JS_LOCATION_RELOAD_REFERRER)) {
            request.setRefererlHeader(htmlPage.getUrl());
        }

        webWindow.getWebClient().download(webWindow, "", request, false, false, false, "JS location.reload");
    }

    /**
     * Reloads the window using the specified URL via a postponed action.
     * @param url the new URL to use to reload the window
     * @throws IOException if loading the specified location fails
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536712.aspx">MSDN Documentation</a>
     */
    public void replace(final String url) throws IOException {
        window_.getWebWindow().getHistory().removeCurrent();
        setHref(url);
    }

    /**
     * Returns the location URL.
     * @return the location URL
     */
    public String jsToString() {
        if (window_ != null) {
            return getHref();
        }
        return "";
    }

    /**
     * Returns the location URL.
     * @return the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533867.aspx">MSDN Documentation</a>
     */
    public String getHref() {
        final WebWindow webWindow = window_.getWebWindow();
        final Page page = webWindow.getEnclosedPage();
        if (page == null) {
            return UNKNOWN;
        }
        try {
            URL url = page.getUrl();
            final String hash = getHash(true);
            if (hash != null) {
                url = UrlUtils.getUrlWithNewRef(url, hash);
            }
            String s = url.toExternalForm();
            if (s.startsWith("file:/") && !s.startsWith("file:///")) {
                // Java (sometimes?) returns file URLs with a single slash; however, browsers return
                // three slashes. See http://www.cyanwerks.com/file-url-formats.html for more info.
                s = "file:///" + s.substring("file:/".length());
            }
            return s;
        }
        catch (final MalformedURLException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(e.getMessage(), e);
            }
            return page.getUrl().toExternalForm();
        }
    }

    /**
     * Sets the location URL to an entirely new value.
     * @param newLocation the new location URL
     * @throws IOException if loading the specified location fails
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533867.aspx">MSDN Documentation</a>
     */
    public void setHref(final String newLocation) throws IOException {
        WebWindow webWindow = getWindow(getStartingScope()).getWebWindow();
        final HtmlPage page = (HtmlPage) webWindow.getEnclosedPage();
        if (newLocation.startsWith(JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
            final String script = newLocation.substring(11);
            page.executeJavaScript(script, "new location value", 1);
            return;
        }
        try {
            final BrowserVersion browserVersion = webWindow.getWebClient().getBrowserVersion();

            URL url = page.getFullyQualifiedUrl(newLocation);
            // fix for empty url
            if (StringUtils.isEmpty(newLocation)) {
                url = UrlUtils.getUrlWithNewRef(url, null);
            }

            final WebRequest request = new WebRequest(url,
                        browserVersion.getHtmlAcceptHeader(), browserVersion.getAcceptEncodingHeader());
            request.setRefererlHeader(page.getUrl());

            webWindow = window_.getWebWindow();
            webWindow.getWebClient().download(webWindow, "", request, true, false, false, "JS set location");
        }
        catch (final MalformedURLException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("setHref('" + newLocation + "') got MalformedURLException", e);
            }
            throw e;
        }
    }

    /**
     * Returns the search portion of the location URL (the portion following the '?').
     * @return the search portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534620.aspx">MSDN Documentation</a>
     */
    public String getSearch() {
        final String search = getUrl().getQuery();
        if (search == null) {
            return "";
        }
        return "?" + search;
    }

    /**
     * Sets the search portion of the location URL (the portion following the '?').
     * @param search the new search portion of the location URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534620.aspx">MSDN Documentation</a>
     */
    public void setSearch(final String search) throws Exception {
        setUrl(UrlUtils.getUrlWithNewQuery(getUrl(), search));
    }

    /**
     * Returns the hash portion of the location URL (the portion following the '#').
     * @return the hash portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533775.aspx">MSDN Documentation</a>
     */
    public String getHash() {
        String hash = hash_;

        if (hash_ != null) {
            hash = decodeHash(hash);
        }

        if (StringUtils.isEmpty(hash)) {
            // nothing to do
        }
        else {
            return "#" + UrlUtils.encodeHash(hash);
        }

        return "";
    }

    private String getHash(final boolean encoded) {
        if (hash_ == null || hash_.isEmpty()) {
            return null;
        }
        if (encoded) {
            return UrlUtils.encodeHash(hash_);
        }
        return hash_;
    }

    /**
     * Sets the hash portion of the location URL (the portion following the '#').
     *
     * @param hash the new hash portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533775.aspx">MSDN Documentation</a>
     */
    public void setHash(final String hash) {
        // IMPORTANT: This method must not call setUrl(), because
        // we must not hit the server just to change the hash!
        setHash(getHref(), hash, true);
    }

    /**
     * Sets the hash portion of the location URL (the portion following the '#').
     *
     * @param oldURL the old URL
     * @param hash the new hash portion of the location URL
     */
    public void setHash(final String oldURL, final String hash) {
        setHash(oldURL, hash, true);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Sets the hash portion of the location URL (the portion following the '#').
     *
     * @param oldURL the old URL
     * @param hash the new hash portion of the location URL
     * @param triggerHashChanged option to disable event triggering
     */
    public void setHash(final String oldURL, String hash, final boolean triggerHashChanged) {
        // IMPORTANT: This method must not call setUrl(), because
        // we must not hit the server just to change the hash!
        if (hash != null && !hash.isEmpty() && hash.charAt(0) == '#') {
            hash = hash.substring(1);
        }
        final boolean hasChanged = hash != null && !hash.equals(hash_);
        hash_ = hash;

        if (triggerHashChanged && hasChanged) {
            final Window w = getWindow();
            final Event event = new HashChangeEvent(w, Event.TYPE_HASH_CHANGE, oldURL, getHref());
            w.executeEventLocally(event);
        }
    }

    private static String decodeHash(final String hash) {
        if (hash.indexOf('%') == -1) {
            return hash;
        }
        return UrlUtils.decode(hash);
    }

    /**
     * Returns the hostname portion of the location URL.
     * @return the hostname portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533785.aspx">MSDN Documentation</a>
     */
    public String getHostname() {
        return getUrl().getHost();
    }

    /**
     * Sets the hostname portion of the location URL.
     * @param hostname the new hostname portion of the location URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533785.aspx">MSDN Documentation</a>
     */
    public void setHostname(final String hostname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
    }

    /**
     * Returns the host portion of the location URL (the '[hostname]:[port]' portion).
     * @return the host portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533784.aspx">MSDN Documentation</a>
     */
    public String getHost() {
        final URL url = getUrl();
        final int port = url.getPort();
        final String host = url.getHost();

        if (port == -1) {
            return host;
        }
        return host + ":" + port;
    }

    /**
     * Sets the host portion of the location URL (the '[hostname]:[port]' portion).
     * @param host the new host portion of the location URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533784.aspx">MSDN Documentation</a>
     */
    public void setHost(final String host) throws Exception {
        final String hostname;
        final int port;
        final int index = host.indexOf(':');
        if (index == -1) {
            hostname = host;
            port = -1;
        }
        else {
            hostname = host.substring(0, index);
            port = Integer.parseInt(host.substring(index + 1));
        }
        final URL url = UrlUtils.getUrlWithNewHostAndPort(getUrl(), hostname, port);
        setUrl(url);
    }

    /**
     * Returns the pathname portion of the location URL.
     * @return the pathname portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534332.aspx">MSDN Documentation</a>
     */
    public String getPathname() {
        if (UrlUtils.URL_ABOUT_BLANK == getUrl()) {
            return "blank";
        }
        return getUrl().getPath();
    }

    /**
     * Sets the pathname portion of the location URL.
     * @param pathname the new pathname portion of the location URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534332.aspx">MSDN Documentation</a>
     */
    public void setPathname(final String pathname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPath(getUrl(), pathname));
    }

    /**
     * Returns the port portion of the location URL.
     * @return the port portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534342.aspx">MSDN Documentation</a>
     */
    public String getPort() {
        final int port = getUrl().getPort();
        if (port == -1) {
            return "";
        }
        return Integer.toString(port);
    }

    /**
     * Sets the port portion of the location URL.
     * @param port the new port portion of the location URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534342.aspx">MSDN Documentation</a>
     */
    public void setPort(final String port) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPort(getUrl(), Integer.parseInt(port)));
    }

    /**
     * Returns the protocol portion of the location URL, including the trailing ':'.
     * @return the protocol portion of the location URL, including the trailing ':'
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534353.aspx">MSDN Documentation</a>
     */
    public String getProtocol() {
        return getUrl().getProtocol() + ":";
    }

    /**
     * Sets the protocol portion of the location URL.
     * @param protocol the new protocol portion of the location URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534353.aspx">MSDN Documentation</a>
     */
    public void setProtocol(final String protocol) throws Exception {
        setUrl(UrlUtils.getUrlWithNewProtocol(getUrl(), protocol));
    }

    /**
     * Returns this location's current URL.
     * @return this location's current URL
     */
    private URL getUrl() {
        return window_.getWebWindow().getEnclosedPage().getUrl();
    }

    /**
     * Sets this location's URL, triggering a server hit and loading the resultant document
     * into this location's window.
     * @param url This location's new URL
     * @throws IOException if there is a problem loading the new location
     */
    private void setUrl(final URL url) throws IOException {
        final WebWindow webWindow = window_.getWebWindow();
        final BrowserVersion browserVersion = webWindow.getWebClient().getBrowserVersion();

        final WebRequest webRequest = new WebRequest(url,
                browserVersion.getHtmlAcceptHeader(), browserVersion.getAcceptEncodingHeader());
        webRequest.setRefererlHeader(getUrl());

        webWindow.getWebClient().getPage(webWindow, webRequest);
    }

    /**
     * Returns the {@code origin} property.
     * @return the {@code origin} property
     */
    public String getOrigin() {
        return getUrl().getProtocol() + "://" + getHost();
    }
}
