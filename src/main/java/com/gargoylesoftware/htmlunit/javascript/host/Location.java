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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.ANCHOR_EMPTY_HREF_NO_FILENAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_TYPE_HASHCHANGEEVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_LOCATION_HASH_HASH_IS_ENCODED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_LOCATION_HASH_IS_DECODED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_LOCATION_HASH_RETURNS_HASH_FOR_EMPTY_DEFINED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_LOCATION_HREF_HASH_IS_ENCODED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_LOCATION_RELOAD_REFERRER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.URL_ABOUT_BLANK_HAS_BLANK_PATH;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.HashChangeEvent;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

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
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535866.aspx">MSDN Documentation</a>
 */
@JsxClass
public class Location extends SimpleScriptable {

    private static final Log LOG = LogFactory.getLog(Location.class);
    private static final String UNKNOWN = "null";

    /**
     * The window which owns this location object.
     */
    private Window window_;

    /**
     * The current hash; we cache it locally because we don't want to modify the page's URL and
     * force a page reload each time this changes.
     */
    private String hash_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public Location() {
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
    @JsxFunction
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
    @JsxFunction
    public void reload(final boolean force) throws IOException {
        final WebWindow webWindow = window_.getWebWindow();
        final HtmlPage htmlPage = (HtmlPage) webWindow.getEnclosedPage();
        final WebRequest request = htmlPage.getWebResponse().getWebRequest();

        if (webWindow.getWebClient().getBrowserVersion().hasFeature(JS_LOCATION_RELOAD_REFERRER)) {
            request.setRefererlHeader(htmlPage.getUrl());
        }

        webWindow.getWebClient().download(webWindow, "", request, true, false, false, "JS location.reload");
    }

    /**
     * Reloads the window using the specified URL via a postponed action.
     * @param url the new URL to use to reload the window
     * @throws IOException if loading the specified location fails
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536712.aspx">MSDN Documentation</a>
     */
    @JsxFunction
    public void replace(final String url) throws IOException {
        window_.getWebWindow().getHistory().removeCurrent();
        setHref(url);
    }

    /**
     * Returns the location URL.
     * @return the location URL
     */
    @JsxFunction(functionName = "toString")
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
    @JsxGetter
    public String getHref() {
        final WebWindow webWindow = window_.getWebWindow();
        final Page page = webWindow.getEnclosedPage();
        if (page == null) {
            return UNKNOWN;
        }
        try {
            URL url = page.getUrl();
            final boolean encodeHash = webWindow.getWebClient()
                    .getBrowserVersion().hasFeature(JS_LOCATION_HREF_HASH_IS_ENCODED);
            final String hash = getHash(encodeHash);
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
    @JsxSetter
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
                final boolean dropFilename = browserVersion.hasFeature(ANCHOR_EMPTY_HREF_NO_FILENAME);
                if (dropFilename) {
                    String path = url.getPath();
                    path = path.substring(0, path.lastIndexOf('/') + 1);
                    url = UrlUtils.getUrlWithNewPath(url, path);
                    url = UrlUtils.getUrlWithNewRef(url, null);
                }
                else {
                    url = UrlUtils.getUrlWithNewRef(url, null);
                }
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
    @JsxGetter
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
    @JsxSetter
    public void setSearch(final String search) throws Exception {
        setUrl(UrlUtils.getUrlWithNewQuery(getUrl(), search));
    }

    /**
     * Returns the hash portion of the location URL (the portion following the '#').
     * @return the hash portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533775.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getHash() {
        final BrowserVersion browserVersion = getBrowserVersion();
        final boolean decodeHash = browserVersion.hasFeature(JS_LOCATION_HASH_IS_DECODED);
        String hash = hash_;

        if (hash_ != null && (decodeHash || hash_.equals(getUrl().getRef()))) {
            hash = decodeHash(hash);
        }

        if (StringUtils.isEmpty(hash)) {
            if (browserVersion.hasFeature(JS_LOCATION_HASH_RETURNS_HASH_FOR_EMPTY_DEFINED)
                    && getHref().endsWith("#")) {
                return "#";
            }
        }
        else if (browserVersion.hasFeature(JS_LOCATION_HASH_HASH_IS_ENCODED)) {
            return "#" + UrlUtils.encodeHash(hash);
        }
        else {
            return "#" + hash;
        }

        return "";
    }

    private String getHash(final boolean encoded) {
        if (hash_ == null || hash_.isEmpty()) {
            return null;
        }
        if (encoded) {
            return UrlUtils.encodeAnchor(hash_);
        }
        return hash_;
    }

    /**
     * Sets the hash portion of the location URL (the portion following the '#').
     *
     * @param hash the new hash portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533775.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setHash(final String hash) {
        // IMPORTANT: This method must not call setUrl(), because
        // we must not hit the server just to change the hash!
        setHash(getHref(), hash);
    }

    /**
     * Sets the hash portion of the location URL (the portion following the '#').
     *
     * @param oldURL the old URL
     * @param hash the new hash portion of the location URL
     */
    public void setHash(final String oldURL, String hash) {
        // IMPORTANT: This method must not call setUrl(), because
        // we must not hit the server just to change the hash!
        if (hash != null && !hash.isEmpty() && hash.charAt(0) == '#') {
            hash = hash.substring(1);
        }
        final boolean hasChanged = hash != null && !hash.equals(hash_);
        hash_ = hash;

        if (hasChanged) {
            final Window w = getWindow();
            final Event event;
            if (getBrowserVersion().hasFeature(EVENT_TYPE_HASHCHANGEEVENT)) {
                event = new HashChangeEvent(w, Event.TYPE_HASH_CHANGE, oldURL, getHref());
            }
            else {
                event = new Event(w, Event.TYPE_HASH_CHANGE);
                event.initEvent(Event.TYPE_HASH_CHANGE, false, false);
            }
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
    @JsxGetter
    public String getHostname() {
        return getUrl().getHost();
    }

    /**
     * Sets the hostname portion of the location URL.
     * @param hostname the new hostname portion of the location URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533785.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setHostname(final String hostname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
    }

    /**
     * Returns the host portion of the location URL (the '[hostname]:[port]' portion).
     * @return the host portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533784.aspx">MSDN Documentation</a>
     */
    @JsxGetter
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
    @JsxSetter
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
    @JsxGetter
    public String getPathname() {
        if (UrlUtils.URL_ABOUT_BLANK == getUrl()) {
            if (getBrowserVersion().hasFeature(URL_ABOUT_BLANK_HAS_BLANK_PATH)) {
                return "blank";
            }
            return "/blank";
        }
        return getUrl().getPath();
    }

    /**
     * Sets the pathname portion of the location URL.
     * @param pathname the new pathname portion of the location URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534332.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setPathname(final String pathname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPath(getUrl(), pathname));
    }

    /**
     * Returns the port portion of the location URL.
     * @return the port portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534342.aspx">MSDN Documentation</a>
     */
    @JsxGetter
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
    @JsxSetter
    public void setPort(final String port) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPort(getUrl(), Integer.parseInt(port)));
    }

    /**
     * Returns the protocol portion of the location URL, including the trailing ':'.
     * @return the protocol portion of the location URL, including the trailing ':'
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534353.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getProtocol() {
        return getUrl().getProtocol() + ":";
    }

    /**
     * Sets the protocol portion of the location URL.
     * @param protocol the new protocol portion of the location URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534353.aspx">MSDN Documentation</a>
     */
    @JsxSetter
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
    @JsxGetter
    public String getOrigin() {
        return getUrl().getProtocol() + "://" + getHost();
    }

}
