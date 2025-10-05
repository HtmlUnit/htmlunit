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

import static org.htmlunit.BrowserVersionFeatures.JS_NAVIGATOR_DO_NOT_TRACK_UNSPECIFIED;
import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.util.ArrayList;

import org.htmlunit.WebClient;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.geo.Geolocation;
import org.htmlunit.javascript.host.media.MediaDevices;
import org.htmlunit.javascript.host.network.NetworkInformation;
import org.htmlunit.util.StringUtils;

/**
 * A JavaScript object for {@code Navigator}.
 *
 * @author Mike Bowler
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535867.aspx">MSDN documentation</a>
 */
@JsxClass
public class Navigator extends HtmlUnitScriptable {

    private PluginArray plugins_;
    private MimeTypeArray mimeTypes_;
    private MediaDevices mediaDevices_;

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Returns the {@code appCodeName} property.
     * @return the {@code appCodeName} property
     */
    @JsxGetter
    public String getAppCodeName() {
        return getBrowserVersion().getApplicationCodeName();
    }

    /**
     * Returns the {@code appName} property.
     * @return the {@code appName} property
     */
    @JsxGetter
    public String getAppName() {
        return getBrowserVersion().getApplicationName();
    }

    /**
     * Returns the {@code appVersion} property.
     * @return the {@code appVersion} property
     */
    @JsxGetter
    public String getAppVersion() {
        return getBrowserVersion().getApplicationVersion();
    }

    /**
     * Returns the language of the browser.
     * @return the language
     */
    @JsxGetter
    public String getLanguage() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Returns the language of the browser.
     * @return the language
     */
    @JsxGetter
    public Scriptable getLanguages() {
        final String acceptLang = getBrowserVersion().getAcceptLanguageHeader();
        if (StringUtils.isEmptyOrNull(acceptLang)) {
            return JavaScriptEngine.newArray(this, 0);
        }

        final ArrayList<String> res = new ArrayList<>();
        final String[] parts = StringUtils.splitAtComma(acceptLang);
        for (final String part : parts) {
            if (!StringUtils.isEmptyOrNull(part)) {
                final String lang = org.apache.commons.lang3.StringUtils.substringBefore(part, ";").trim();
                if (!StringUtils.isEmptyOrNull(part)) {
                    res.add(lang);
                }
            }
        }

        return JavaScriptEngine.newArray(this, res.toArray());
    }

    /**
     * Returns the {@code cookieEnabled} property.
     * @return the {@code cookieEnabled} property
     */
    @JsxGetter
    public boolean isCookieEnabled() {
        return getWindow().getWebWindow().getWebClient().getCookieManager().isCookiesEnabled();
    }

    /**
     * Returns the {@code onLine} property.
     * @return the {@code onLine} property
     */
    @JsxGetter
    public boolean isOnLine() {
        return getBrowserVersion().isOnLine();
    }

    /**
     * Returns the {@code platform} property.
     * @return the {@code platform} property
     */
    @JsxGetter
    public String getPlatform() {
        return getBrowserVersion().getPlatform();
    }

    /**
     * Returns the {@code product} property.
     * @return the {@code product} property
     */
    @JsxGetter
    public String getProduct() {
        return "Gecko";
    }

    /**
     * Returns the build number of the current browser.
     * @see <a href="https://developer.mozilla.org/en/navigator.productSub">Mozilla Doc</a>
     * @return false
     */
    @JsxGetter
    public String getProductSub() {
        return getBrowserVersion().getProductSub();
    }

    /**
     * Returns the property {@code userAgent}.
     * @return the property {@code userAgent}
     */
    @JsxGetter
    public String getUserAgent() {
        return getBrowserVersion().getUserAgent();
    }

    /**
     * Returns an empty array because HtmlUnit does not support embedded objects.
     * @return an empty array
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
        plugins_.setParentScope(this);
        plugins_.setPrototype(getPrototype(PluginArray.class));

        Plugin plugin = new Plugin("PDF Viewer", "Portable Document Format", "internal-pdf-viewer");
        plugin.setParentScope(this);
        plugin.setPrototype(getPrototype(Plugin.class));

        // https://developer.mozilla.org/en-US/docs/Web/API/Navigator/mimeTypes
        // Recent versions of the specification hard-code the returned set of MIME types.
        // If PDF files can be displayed inline then application/pdf and text/pdf are listed,
        // otherwise an empty list is returned.
        mimeTypes_ = new MimeTypeArray();
        mimeTypes_.setParentScope(this);
        mimeTypes_.setPrototype(getPrototype(MimeTypeArray.class));

        final MimeType mimeTypeAppPdf = new MimeType("application/pdf", "Portable Document Format", "pdf", plugin);
        mimeTypeAppPdf.setParentScope(this);
        mimeTypeAppPdf.setPrototype(getPrototype(MimeType.class));
        mimeTypes_.add(mimeTypeAppPdf);

        final MimeType mimeTypeTxtPdf = new MimeType("text/pdf", "Portable Document Format", "pdf", plugin);
        mimeTypeTxtPdf.setParentScope(this);
        mimeTypeTxtPdf.setPrototype(getPrototype(MimeType.class));
        mimeTypes_.add(mimeTypeTxtPdf);

        plugin.add(mimeTypeAppPdf);
        plugin.add(mimeTypeTxtPdf);
        plugins_.add(plugin);

        // all the others
        plugin = new Plugin("Chrome PDF Viewer", "Portable Document Format", "internal-pdf-viewer");
        plugin.setParentScope(this);
        plugin.setPrototype(getPrototype(Plugin.class));
        plugin.add(mimeTypeAppPdf);
        plugin.add(mimeTypeTxtPdf);
        plugins_.add(plugin);

        plugin = new Plugin("Chromium PDF Viewer", "Portable Document Format", "internal-pdf-viewer");
        plugin.setParentScope(this);
        plugin.setPrototype(getPrototype(Plugin.class));
        plugin.add(mimeTypeAppPdf);
        plugin.add(mimeTypeTxtPdf);
        plugins_.add(plugin);

        plugin = new Plugin("Microsoft Edge PDF Viewer", "Portable Document Format", "internal-pdf-viewer");
        plugin.setParentScope(this);
        plugin.setPrototype(getPrototype(Plugin.class));
        plugin.add(mimeTypeAppPdf);
        plugin.add(mimeTypeTxtPdf);
        plugins_.add(plugin);

        plugin = new Plugin("WebKit built-in PDF", "Portable Document Format", "internal-pdf-viewer");
        plugin.setParentScope(this);
        plugin.setPrototype(getPrototype(Plugin.class));
        plugin.add(mimeTypeAppPdf);
        plugin.add(mimeTypeTxtPdf);
        plugins_.add(plugin);
    }

    /**
     * Returns the {@code mimeTypes} property.
     * @return the {@code mimeTypes} property
     */
    @JsxGetter
    public MimeTypeArray getMimeTypes() {
        initPluginsAndMimeTypes();
        return mimeTypes_;
    }

    /**
     * Indicates if Java is enabled.
     * @return false
     */
    @JsxFunction
    public boolean javaEnabled() {
        return false;
    }

    /**
     * Returns {@code false} always as data tainting support is not enabled in HtmlUnit.
     * @return false
     */
    @JsxFunction({FF, FF_ESR})
    public boolean taintEnabled() {
        return false;
    }

    /**
     * Returns the {@code geolocation} property.
     * @return the {@code geolocation} property
     */
    @JsxGetter
    public Geolocation getGeolocation() {
        final Geolocation geolocation = new Geolocation();
        geolocation.setPrototype(getPrototype(geolocation.getClass()));
        geolocation.setParentScope(getParentScope());
        return geolocation;
    }

    /**
     * @return true whether the browser supports inline display
     *         of PDF files when navigating to them
     */
    @JsxGetter
    public boolean isPdfViewerEnabled() {
        return true;
    }

    /**
     * Returns the {@code buildID} property.
     * @return the {@code buildID} property
     */
    @JsxGetter({FF, FF_ESR})
    public String getBuildID() {
        return getBrowserVersion().getBuildId();
    }

    /**
     * Returns the {@code vendor} property.
     * @return the {@code vendor} property
     */
    @JsxGetter
    public String getVendor() {
        return getBrowserVersion().getVendor();
    }

    /**
     * Returns the {@code vendorSub} property.
     * @return the {@code vendorSub} property
     */
    @JsxGetter
    public String getVendorSub() {
        return "";
    }

    /**
     * Returns the {@code doNotTrack} property.
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
     * @return the {@code oscpu} property
     */
    @JsxGetter({FF, FF_ESR})
    public String getOscpu() {
        return "Windows NT 6.1";
    }

    /**
     * Returns the {@code connection} property.
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
     * Returns the {@code mimeTypes} property.
     * @return the {@code mimeTypes} property
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
}
