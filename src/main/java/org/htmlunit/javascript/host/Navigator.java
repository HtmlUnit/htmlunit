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
package org.htmlunit.javascript.host;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.htmlunit.BrowserVersionFeatures.JS_NAVIGATOR_DO_NOT_TRACK_UNSPECIFIED;
import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.FormEncodingType;
import org.htmlunit.HttpHeader;
import org.htmlunit.HttpMethod;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.WebRequest.HttpHint;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBufferView;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.file.Blob;
import org.htmlunit.javascript.host.geo.Geolocation;
import org.htmlunit.javascript.host.media.MediaDevices;
import org.htmlunit.javascript.host.network.NetworkInformation;
// TODO verify actual package - not visible from XMLHttpRequest.java's import list,
// which suggests it may already share a package with XMLHttpRequest rather than
// living here; adjust this import (and the FormData branch below) accordingly.
import org.htmlunit.javascript.host.xml.FormData;
import org.htmlunit.util.StringUtils;
import org.htmlunit.util.UrlUtils;

/**
 * JavaScript host object for {@code Navigator}.
 *
 * @author Mike Bowler
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Navigator">MDN Documentation</a>
 */
@JsxClass
public class Navigator extends HtmlUnitScriptable {

    private static final Log LOG = LogFactory.getLog(Navigator.class);

    private PluginArray plugins_;
    private MimeTypeArray mimeTypes_;
    private MediaDevices mediaDevices_;

    /**
     * Creates an instance of this object.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Returns the {@code appCodeName} property.
     *
     * @return the {@code appCodeName} property
     */
    @JsxGetter
    public String getAppCodeName() {
        return getBrowserVersion().getApplicationCodeName();
    }

    /**
     * Returns the {@code appName} property.
     *
     * @return the {@code appName} property
     */
    @JsxGetter
    public String getAppName() {
        return getBrowserVersion().getApplicationName();
    }

    /**
     * Returns the {@code appVersion} property.
     *
     * @return the {@code appVersion} property
     */
    @JsxGetter
    public String getAppVersion() {
        return getBrowserVersion().getApplicationVersion();
    }

    /**
     * Returns the language of the browser.
     *
     * @return the browser language
     */
    @JsxGetter
    public String getLanguage() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Returns the preferred languages of the browser as an array.
     *
     * @return the languages array
     */
    @JsxGetter
    public Scriptable getLanguages() {
        final String acceptLang = getBrowserVersion().getAcceptLanguageHeader();
        if (StringUtils.isEmptyOrNull(acceptLang)) {
            return JavaScriptEngine.newArray(getParentScope(), 0);
        }

        final ArrayList<String> res = new ArrayList<>();
        final String[] parts = StringUtils.splitAtComma(acceptLang);
        for (final String part : parts) {
            if (!StringUtils.isEmptyOrNull(part)) {
                final String lang = StringUtils.substringBefore(part, ";").trim();
                if (!StringUtils.isEmptyOrNull(part)) {
                    res.add(lang);
                }
            }
        }

        return JavaScriptEngine.newArray(getParentScope(), res.toArray());
    }

    /**
     * Returns the {@code cookieEnabled} property.
     *
     * @return the {@code cookieEnabled} property
     */
    @JsxGetter
    public boolean isCookieEnabled() {
        return getWindow().getWebWindow().getWebClient().getCookieManager().isCookiesEnabled();
    }

    /**
     * Returns the {@code onLine} property.
     *
     * @return the {@code onLine} property
     */
    @JsxGetter
    public boolean isOnLine() {
        return getBrowserVersion().isOnLine();
    }

    /**
     * Returns the {@code platform} property.
     *
     * @return the {@code platform} property
     */
    @JsxGetter
    public String getPlatform() {
        return getBrowserVersion().getPlatform();
    }

    /**
     * Returns the {@code product} property.
     *
     * @return the {@code product} property
     */
    @JsxGetter
    public String getProduct() {
        return "Gecko";
    }

    /**
     * Returns the build number of the current browser.
     *
     * @return the {@code productSub} property
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Navigator/productSub">MDN Documentation</a>
     */
    @JsxGetter
    public String getProductSub() {
        return getBrowserVersion().getProductSub();
    }

    /**
     * Returns the {@code userAgent} property.
     *
     * @return the {@code userAgent} property
     */
    @JsxGetter
    public String getUserAgent() {
        return getBrowserVersion().getUserAgent();
    }

    /**
     * Returns the list of browser plugins.
     *
     * @return the {@code plugins} array
     */
    @JsxGetter
    public PluginArray getPlugins() {
        initPluginsAndMimeTypes();
        return plugins_;
    }

    private void initPluginsAndMimeTypes() {
        // https://developer.mozilla.org/en-US/docs/Web/API/Navigator/plugins
        // Recent versions of the specification hard-code the returned list.
        // If inline viewing of PDF files is supported the property lists five standard plugins.
        // If inline PDF viewing is not supported then an empty list is returned.
        if (plugins_ != null) {
            return;
        }
        plugins_ = new PluginArray();
        plugins_.setParentScope(getParentScope());
        plugins_.setPrototype(getPrototype(PluginArray.class));

        Plugin plugin = new Plugin("PDF Viewer", "Portable Document Format", "internal-pdf-viewer");
        plugin.setParentScope(getParentScope());
        plugin.setPrototype(getPrototype(Plugin.class));

        // https://developer.mozilla.org/en-US/docs/Web/API/Navigator/mimeTypes
        // Recent versions of the specification hard-code the returned set of MIME types.
        // If PDF files can be displayed inline then application/pdf and text/pdf are listed,
        // otherwise an empty list is returned.
        mimeTypes_ = new MimeTypeArray();
        mimeTypes_.setParentScope(getParentScope());
        mimeTypes_.setPrototype(getPrototype(MimeTypeArray.class));

        final MimeType mimeTypeAppPdf = new MimeType("application/pdf", "Portable Document Format", "pdf", plugin);
        mimeTypeAppPdf.setParentScope(getParentScope());
        mimeTypeAppPdf.setPrototype(getPrototype(MimeType.class));
        mimeTypes_.add(mimeTypeAppPdf);

        final MimeType mimeTypeTxtPdf = new MimeType("text/pdf", "Portable Document Format", "pdf", plugin);
        mimeTypeTxtPdf.setParentScope(getParentScope());
        mimeTypeTxtPdf.setPrototype(getPrototype(MimeType.class));
        mimeTypes_.add(mimeTypeTxtPdf);

        plugin.add(mimeTypeAppPdf);
        plugin.add(mimeTypeTxtPdf);
        plugins_.add(plugin);

        // all the others
        plugin = new Plugin("Chrome PDF Viewer", "Portable Document Format", "internal-pdf-viewer");
        plugin.setParentScope(getParentScope());
        plugin.setPrototype(getPrototype(Plugin.class));
        plugin.add(mimeTypeAppPdf);
        plugin.add(mimeTypeTxtPdf);
        plugins_.add(plugin);

        plugin = new Plugin("Chromium PDF Viewer", "Portable Document Format", "internal-pdf-viewer");
        plugin.setParentScope(getParentScope());
        plugin.setPrototype(getPrototype(Plugin.class));
        plugin.add(mimeTypeAppPdf);
        plugin.add(mimeTypeTxtPdf);
        plugins_.add(plugin);

        plugin = new Plugin("Microsoft Edge PDF Viewer", "Portable Document Format", "internal-pdf-viewer");
        plugin.setParentScope(getParentScope());
        plugin.setPrototype(getPrototype(Plugin.class));
        plugin.add(mimeTypeAppPdf);
        plugin.add(mimeTypeTxtPdf);
        plugins_.add(plugin);

        plugin = new Plugin("WebKit built-in PDF", "Portable Document Format", "internal-pdf-viewer");
        plugin.setParentScope(getParentScope());
        plugin.setPrototype(getPrototype(Plugin.class));
        plugin.add(mimeTypeAppPdf);
        plugin.add(mimeTypeTxtPdf);
        plugins_.add(plugin);
    }

    /**
     * Returns the {@code mimeTypes} property.
     *
     * @return the {@code mimeTypes} property
     */
    @JsxGetter
    public MimeTypeArray getMimeTypes() {
        initPluginsAndMimeTypes();
        return mimeTypes_;
    }

    /**
     * Returns whether Java is enabled. Always returns {@code false}.
     *
     * @return {@code false}
     */
    @JsxFunction
    public boolean javaEnabled() {
        return false;
    }

    /**
     * Returns {@code false} as data tainting support is not enabled in HtmlUnit.
     *
     * @return {@code false}
     */
    @JsxFunction({FF, FF_ESR})
    public boolean taintEnabled() {
        return false;
    }

    /**
     * Returns the {@code geolocation} property.
     *
     * @return the {@code geolocation} property
     */
    @JsxGetter
    public Geolocation getGeolocation() {
        final Geolocation geolocation = new Geolocation();
        geolocation.setParentScope(getParentScope());
        geolocation.setPrototype(getPrototype(geolocation.getClass()));
        return geolocation;
    }

    /**
     * Returns whether the browser supports inline display of PDF files when navigating to them.
     *
     * @return {@code true} if inline PDF viewing is supported
     */
    @JsxGetter
    public boolean isPdfViewerEnabled() {
        return true;
    }

    /**
     * Returns the {@code buildID} property.
     *
     * @return the {@code buildID} property
     */
    @JsxGetter({FF, FF_ESR})
    public String getBuildID() {
        return getBrowserVersion().getBuildId();
    }

    /**
     * Returns the {@code vendor} property.
     *
     * @return the {@code vendor} property
     */
    @JsxGetter
    public String getVendor() {
        return getBrowserVersion().getVendor();
    }

    /**
     * Returns the {@code vendorSub} property.
     *
     * @return the {@code vendorSub} property
     */
    @JsxGetter
    public String getVendorSub() {
        return "";
    }

    /**
     * Returns the {@code doNotTrack} property.
     *
     * @return the {@code doNotTrack} property
     */
    @JsxGetter
    public Object getDoNotTrack() {
        final WebClient client = getWindow().getWebWindow().getWebClient();
        if (client.getOptions().isDoNotTrackEnabled()) {
            return 1;
        }
        if (client.getBrowserVersion().hasFeature(JS_NAVIGATOR_DO_NOT_TRACK_UNSPECIFIED)) {
            return "unspecified";
        }
        return null;
    }

    /**
     * Returns the {@code oscpu} property.
     *
     * @return the {@code oscpu} property
     */
    @JsxGetter({FF, FF_ESR})
    public String getOscpu() {
        return "Windows NT 6.1";
    }

    /**
     * Returns the {@code connection} property.
     *
     * @return the {@code connection} property
     */
    @JsxGetter({CHROME, EDGE})
    public NetworkInformation getConnection() {
        final NetworkInformation networkInformation = new NetworkInformation();
        networkInformation.setPrototype(getPrototype(networkInformation.getClass()));
        networkInformation.setParentScope(getParentScope());
        return networkInformation;
    }

    /**
     * Returns the {@code mediaDevices} property.
     *
     * @return the {@code mediaDevices} property
     */
    @JsxGetter
    public MediaDevices getMediaDevices() {
        if (mediaDevices_ == null) {
            mediaDevices_ = new MediaDevices();
            mediaDevices_.setPrototype(getPrototype(mediaDevices_.getClass()));
            mediaDevices_.setParentScope(getParentScope());
        }
        return mediaDevices_;
    }

    /**
     * The {@code sendBeacon()} method.
     * <p>
     * Note: real browsers queue the beacon and return immediately, continuing to
     * attempt delivery even after the page unloads. This implementation instead sends
     * the request synchronously (mirroring how {@code HyperlinkElementSupport} already
     * handles hyperlink-auditing/{@code ping} requests and swallows any network error,
     * since there is no caller left to report it to by the time
     * a real browser would encounter it either.
     * </p>
     *
     * @param url the URL to send the data to
     * @param data the data to send; USVString, {@link Blob}, {@link URLSearchParams},
     *             {@code FormData}, and ArrayBufferView are supported, per spec
     * @return {@code true} if the browser successfully queued the data for transfer,
     *         {@code false} otherwise (e.g. the URL could not be resolved)
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Navigator/sendBeacon">
     *      MDN Documentation</a>
     */
    @JsxFunction
    public boolean sendBeacon(final String url, final Object data) {
        final Window window = getWindow();
        final WebWindow webWindow = window.getWebWindow();
        final HtmlPage page = (HtmlPage) webWindow.getEnclosedPage();
        final URL pageUrl = page.getUrl();

        final URL targetUrl;
        try {
            targetUrl = page.getFullyQualifiedUrl(url);
        }
        catch (final MalformedURLException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("sendBeacon(): invalid url '" + url + "'", e);
            }
            return false;
        }

        final WebRequest request = new WebRequest(targetUrl, HttpMethod.POST);

        // a beacon is always a POST, so per Fetch's "Origin header" rules this is
        // added unconditionally, same as HyperlinkElementSupport's ping request
        try {
            request.setRefererHeader(pageUrl);
            request.setAdditionalHeader(HttpHeader.ORIGIN,
                    UrlUtils.getUrlWithProtocolAndAuthority(pageUrl).toExternalForm());
        }
        catch (final MalformedURLException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("sendBeacon(): invalid origin url '" + pageUrl + "'", e);
            }
        }

        // Sec-Fetch-* support (https://www.w3.org/TR/fetch-metadata/): a beacon has no
        // destination and is always no-cors, matching hyperlink-auditing (ping) requests.
        request.setFetchDestination(WebRequest.FetchDestination.EMPTY);
        request.setFetchModeOverride(WebRequest.FetchMode.NO_CORS);
        request.setRequestingUrl(pageUrl);

        fillRequestBody(request, data);

        final WebClient webClient = webWindow.getWebClient();
        try {
            webClient.loadWebResponse(request);
        }
        catch (final IOException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("sendBeacon(): request failed", e);
            }
            // per spec this still returns true - queuing succeeded even if delivery didn't
        }
        return true;
    }

    /**
     * Fills in the request body for {@link #sendBeacon(String, Object)}, dispatching on
     * the data's type the same way {@code XMLHttpRequest#prepareRequestContent} does for
     * the body types the Beacon spec actually allows (unlike XHR, this excludes
     * {@code Document} types).
     *
     * @param request the request to fill in
     * @param data the data passed to {@code sendBeacon()}, possibly {@code null}/undefined
     */
    private static void fillRequestBody(final WebRequest request, final Object data) {
        if (data == null || JavaScriptEngine.isUndefined(data)) {
            return;
        }

        if (data instanceof FormData formData) {
            formData.fillRequest(request);
        }
        else if (data instanceof URLSearchParams params) {
            params.fillRequest(request);
            request.setCharset(UTF_8);
            request.addHint(HttpHint.IncludeCharsetInContentTypeHeader);
        }
        else if (data instanceof Blob blob) {
            blob.fillRequest(request);
        }
        else if (data instanceof NativeArrayBufferView view) {
            request.setRequestBody(new String(view.getBuffer().getBuffer(), UTF_8));
            request.setEncodingType(null);
        }
        else {
            final String body = JavaScriptEngine.toString(data);
            if (!body.isEmpty()) {
                request.setRequestBody(body);
                request.setEncodingType(FormEncodingType.TEXT_PLAIN);
                request.setCharset(UTF_8);
                request.addHint(HttpHint.IncludeCharsetInContentTypeHeader);
            }
        }
    }
}
