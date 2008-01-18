/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * A JavaScript object for a Location.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Michael Ottati
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Daniel Gredler
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/obj_location.asp">
 * MSDN Documentation</a>
 */
public class Location extends SimpleScriptable {

    private static final long serialVersionUID = -2907220432378132233L;
    private static final String UNKNOWN = "null";
    private Window window_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Location() {
        // Empty.
    }

    /**
     * Initializes the object.
     * @param window The window that this location belongs to.
     */
    public void initialize(final Window window) {
        window_ = window;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object getDefaultValue(final Class hint) {
        if (hint == null || String.class.equals(hint)) {
            return jsxGet_href();
        }
        else {
            return super.getDefaultValue(hint);
        }
    }

    /**
     * Returns the string value of the location, which is the full URL string.
     * @return The full URL string.
     */
    public String toString() {
        if (window_ != null) {
            return jsxGet_href();
        }
        else {
            return "[Uninitialized]";
        }
    }

    /**
     * Loads the new HTML document corresponding to the specified URL.
     * @param url The location of the new HTML document to load.
     * @throws IOException If loading the specified location fails.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/assign.asp">
     * MSDN Documentation</a>
     */
    public void jsxFunction_assign(final String url) throws IOException {
        jsxSet_href(url);
    }

    /**
     * Reloads the current page, possibly forcing retrieval from the server even if
     * the browser cache contains the latest version of the document.
     * @param force If <tt>true</tt>, force reload from server; otherwise, may reload from cache.
     * @throws IOException When there is a problem reloading the page.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/reload.asp">
     * MSDN Documentation</a>
     */
    public void jsxFunction_reload(final boolean force) throws IOException {
        final String url = jsxGet_href();
        if (UNKNOWN.equals(url)) {
            getLog().error("Unable to reload location: current URL is unknown.");
        }
        else {
            jsxSet_href(url);
        }
    }

    /**
     * Reloads the window using the specified URL.
     * @param url The new URL to use to reload the window.
     * @throws IOException When there is a problem loading the new page.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/replace.asp">
     * MSDN Documentation</a>
     */
    public void jsxFunction_replace(final String url) throws IOException {
        jsxSet_href(url);
    }

    /**
     * Returns the location URL.
     * @return the location URL.
     */
    public String jsxFunction_toString() {
        return jsxGet_href();
    }

    /**
     * Returns the location URL.
     * @return The location URL.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/href_3.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_href() {
        final Page page = window_.getWebWindow().getEnclosedPage();
        if (page == null) {
            return UNKNOWN;
        }
        else {
            return page.getWebResponse().getUrl().toExternalForm();
        }
    }

    /**
     * Set the location URL to an entirely new value.
     * @param newLocation The new location URL.
     * @throws IOException If loading the specified location fails.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/href_3.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_href(final String newLocation) throws IOException {
        // url should be resolved from the page in which the js is executed
        // cf test FrameTest#testLocation
        final HtmlPage page = (HtmlPage) getWindow(getStartingScope()).getWebWindow().getEnclosedPage();

        if (newLocation.startsWith("javascript:")) {
            final String script = newLocation.substring(11);
            page.executeJavaScriptIfPossible(script, "new location value", 1);
        }
        else {
            try {
                final URL url = page.getFullyQualifiedUrl(newLocation);

                final WebWindow webWindow = getWindow().getWebWindow();
                webWindow.getWebClient().getPage(webWindow, new WebRequestSettings(url));
            }
            catch (final MalformedURLException e) {
                getLog().error("jsxSet_location(\"" + newLocation + "\") Got MalformedURLException", e);
                throw e;
            }
            catch (final IOException e) {
                getLog().error("jsxSet_location(\"" + newLocation + "\") Got IOException", e);
                throw e;
            }
        }
    }

    /**
     * Returns the search portion of the location URL (the portion following the '?').
     * @return The search portion of the location URL.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/search.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_search() {
        final String search = getUrl().getQuery();
        if (search == null) {
            return "";
        }
        else {
            return "?" + search;
        }
    }

    /**
     * Sets the search portion of the location URL (the portion following the '?').
     * @param search The new search portion of the location URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/search.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_search(final String search) throws Exception {
        setUrl(UrlUtils.getUrlWithNewQuery(getUrl(), search));
    }

    /**
     * Returns the hash portion of the location URL (the portion following the '#').
     * @return The hash portion of the location URL.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/hash.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_hash() {
        final String hash = getUrl().getRef();
        if (hash == null) {
            return "";
        }
        else {
            return hash;
        }
    }

    /**
     * Sets the hash portion of the location URL (the portion following the '#').
     * @param hash The new hash portion of the location URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/hash.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_hash(final String hash) throws Exception {
        setUrl(UrlUtils.getUrlWithNewRef(getUrl(), hash));
    }

    /**
     * Returns the hostname portion of the location URL.
     * @return The hostname portion of the location URL.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/hostname.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_hostname() {
        return getUrl().getHost();
    }

    /**
     * Sets the hostname portion of the location URL.
     * @param hostname The new hostname portion of the location URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/hostname.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_hostname(final String hostname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
    }

    /**
     * Returns the host portion of the location URL (the '[hostname]:[port]' portion).
     * @return The host portion of the location URL.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/host.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_host() {
        final URL url = getUrl();
        final int port = url.getPort();
        final String host = url.getHost();

        if (port == -1) {
            return host;
        }
        else {
            return host + ":" + port;
        }
    }

    /**
     * Sets the host portion of the location URL (the '[hostname]:[port]' portion).
     * @param host The new host portion of the location URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/host.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_host(final String host) throws Exception {
        final String hostname;
        final int port;
        final int index = host.indexOf(':');
        if (index != -1) {
            hostname = host.substring(0, index);
            port = Integer.parseInt(host.substring(index + 1));
        }
        else {
            hostname = host;
            port = -1;
        }
        final URL url1 = UrlUtils.getUrlWithNewHost(getUrl(), hostname);
        final URL url2 = UrlUtils.getUrlWithNewPort(url1, port);
        setUrl(url2);
    }

    /**
     * Returns the pathname portion of the location URL.
     * @return The pathname portion of the location URL.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/pathname.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_pathname() {
        return getUrl().getPath();
    }

    /**
     * Sets the pathname portion of the location URL.
     * @param pathname The new pathname portion of the location URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/pathname.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_pathname(final String pathname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPath(getUrl(), pathname));
    }

    /**
     * Returns the port portion of the location URL.
     * @return The port portion of the location URL.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/port.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_port() {
        final int port = getUrl().getPort();
        if (port == -1) {
            return "";
        }
        else {
            return String.valueOf(port);
        }
    }

    /**
     * Sets the port portion of the location URL.
     * @param port The new port portion of the location URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/port.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_port(final String port) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPort(getUrl(), Integer.parseInt(port)));
    }

    /**
     * Returns the protocol portion of the location URL, including the trailing ':'.
     * @return The protocol portion of the location URL, including the trailing ':'.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/protocol.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_protocol() {
        return getUrl().getProtocol() + ":";
    }

    /**
     * Sets the protocol portion of the location URL.
     * @param protocol The new protocol portion of the location URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/protocol.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_protocol(final String protocol) throws Exception {
        setUrl(UrlUtils.getUrlWithNewProtocol(getUrl(), protocol));
    }

    /**
     * Returns this location's current URL.
     * @return This location's current URL.
     */
    private URL getUrl() {
        return window_.getWebWindow().getEnclosedPage().getWebResponse().getUrl();
    }

    /**
     * Sets this location's URL, triggering a server hit and loading the resultant document
     * into this location's window.
     * @param url This location's new URL.
     * @throws IOException If there is a problem loading the new location.
     */
    private void setUrl(final URL url) throws IOException {
        window_.getWebWindow().getWebClient().getPage(window_.getWebWindow(), new WebRequestSettings(url));
    }

}
