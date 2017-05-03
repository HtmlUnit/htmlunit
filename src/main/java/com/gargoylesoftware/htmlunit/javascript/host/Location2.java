/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event2;
import com.gargoylesoftware.htmlunit.javascript.host.event.HashChangeEvent2;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

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
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535866.aspx">MSDN Documentation</a>
 */
@ScriptClass
public class Location2 extends SimpleScriptObject {

    private static final Log LOG = LogFactory.getLog(Location.class);
    private static final String UNKNOWN = "null";

    /**
     * The window which owns this location object.
     */
    private Window2 window_;

    /**
     * The current hash; we cache it locally because we don't want to modify the page's URL and
     * force a page reload each time this changes.
     */
    private String hash_;

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static Location2 constructor(final boolean newObj, final Object self) {
        final Location2 host = new Location2();
        ScriptUtils.initialize(host);
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Initializes the object.
     *
     * @param window the window that this location belongs to
     */
    public void initialize(final Window2 window) {
        window_ = window;
        if (window_ != null && window_.getWebWindow().getEnclosedPage() != null) {
            setHash(window_.getWebWindow().getEnclosedPage().getUrl().getRef());
        }
    }

    /**
     * Returns the location URL.
     * @return the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533867.aspx">MSDN Documentation</a>
     */
    @Getter
    public String getHref() {
        final Page page = window_.getWebWindow().getEnclosedPage();
        if (page == null) {
            return UNKNOWN;
        }
        try {
            URL url = page.getUrl();
            final boolean encodeHash = window_.getWebWindow().getWebClient()
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
            LOG.error(e.getMessage(), e);
            return page.getUrl().toExternalForm();
        }
    }

    /**
     * Sets the location URL to an entirely new value.
     * @param newLocation the new location URL
     * @throws IOException if loading the specified location fails
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533867.aspx">MSDN Documentation</a>
     */
    @Setter
    public void setHref(final String newLocation) throws IOException {
        setHref(newLocation, false, null);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Sets the location URL to an entirely new value.
     *
     * @param newLocation the new location URL
     * @param justHistoryAPIPushState indicates if change is caused by using HTML5 HistoryAPI
     * @param state the state object passed down if justHistoryAPIPushState is true
     * @throws IOException if loading the specified location fails
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533867.aspx">MSDN Documentation</a>
     */
    public void setHref(final String newLocation, final boolean justHistoryAPIPushState, final Object state)
            throws IOException {
        final HtmlPage page = (HtmlPage) Global.instance().<Window2>getWindow().getWebWindow().getEnclosedPage();
        if (newLocation.startsWith(JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
            final String script = newLocation.substring(11);
            page.executeJavaScript(script, "new location value", 1);
            return;
        }
        try {
            URL url = page.getFullyQualifiedUrl(newLocation);
            // fix for empty url
            if (StringUtils.isEmpty(newLocation)) {
                final boolean dropFilename = page.getWebClient().getBrowserVersion().
                        hasFeature(ANCHOR_EMPTY_HREF_NO_FILENAME);
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

            final WebRequest request = new WebRequest(url);
            request.setAdditionalHeader("Referer", page.getUrl().toExternalForm());

            final WebWindow webWindow = window_.getWebWindow();
            webWindow.getWebClient().download(webWindow, "", request, true, false, "JS set location");
            if (justHistoryAPIPushState) {
                webWindow.getWebClient().loadDownloadedResponses();
            }
        }
        catch (final MalformedURLException e) {
            LOG.error("setHref('" + newLocation + "') got MalformedURLException", e);
            throw e;
        }
    }

    /**
     * Returns the hash portion of the location URL (the portion following the '#').
     * @return the hash portion of the location URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533775.aspx">MSDN Documentation</a>
     */
    @Getter
    public String getHash() {
        final boolean decodeHash = getBrowserVersion().hasFeature(JS_LOCATION_HASH_IS_DECODED);
        String hash = hash_;

        if (hash_ != null && (decodeHash || hash_.equals(getUrl().getRef()))) {
            hash = decodeHash(hash);
        }

        if (StringUtils.isEmpty(hash)) {
            if (getBrowserVersion().hasFeature(JS_LOCATION_HASH_RETURNS_HASH_FOR_EMPTY_DEFINED)
                    && getHref().endsWith("#")) {
                return "#";
            }
        }
        else if (getBrowserVersion().hasFeature(JS_LOCATION_HASH_HASH_IS_ENCODED)) {
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
    @Setter
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
        if (hash != null) {
            if (!hash.isEmpty() && ('#' == hash.charAt(0))) {
                hash = hash.substring(1);
            }
        }
        final boolean hasChanged = hash != null && !hash.equals(hash_);
        hash_ = hash;
        final String newURL = getHref();

        if (hasChanged) {
            final Event2 event;
            if (getBrowserVersion().hasFeature(EVENT_TYPE_HASHCHANGEEVENT)) {
                event = new HashChangeEvent2(getWindow(), Event2.TYPE_HASH_CHANGE, oldURL, newURL);
            }
            else {
                event = new Event2(getWindow(), Event2.TYPE_HASH_CHANGE);
                event.initEvent(Event.TYPE_HASH_CHANGE, false, false);
            }
            getWindow().executeEventLocally(event);
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
    @Getter
    public String getHostname() {
        return getUrl().getHost();
    }

    /**
     * Returns this location's current URL.
     * @return this location's current URL
     */
    private URL getUrl() {
        return window_.getWebWindow().getEnclosedPage().getUrl();
    }

    /**
     * Loads the new HTML document corresponding to the specified URL.
     * @param url the location of the new HTML document to load
     * @throws IOException if loading the specified location fails
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536342.aspx">MSDN Documentation</a>
     */
    @Function
    public void assign(final String url) throws IOException {
        setHref(url);
    }

    /**
     * Reloads the window using the specified URL via a postponed action.
     * @param url the new URL to use to reload the window
     * @throws IOException if loading the specified location fails
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536712.aspx">MSDN Documentation</a>
     */
    @Function
    public void replace(final String url) throws IOException {
        window_.getWebWindow().getHistory().removeCurrent();
        setHref(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> typeHint) {
        if (typeHint == null || typeHint == String.class) {
            return getHref();
        }
        return super.getDefaultValue(typeHint);
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(Location2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("Location",
                    staticHandle("constructor", Location2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("Location");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("Location");
        }
    }
}
