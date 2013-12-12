/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.PluginConfiguration;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Geolocation;

/**
 * A JavaScript object for a Navigator.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535867.aspx">MSDN documentation</a>
 */
@JsxClass
public final class Navigator extends SimpleScriptable {

    private PluginArray plugins_;
    private MimeTypeArray mimeTypes_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Navigator() { }

    /**
     * Returns the property "appCodeName".
     * @return the property "appCodeName"
     */
    @JsxGetter
    public String getAppCodeName() {
        return getBrowserVersion().getApplicationCodeName();
    }

    /**
     * Returns the property "appMinorVersion".
     * @return the property "appMinorVersion"
     */
    @JsxGetter(@WebBrowser(IE))
    public String getAppMinorVersion() {
        return getBrowserVersion().getApplicationMinorVersion();
    }

    /**
     * Returns the property "appName".
     * @return the property "appName"
     */
    @JsxGetter
    public String getAppName() {
        return getBrowserVersion().getApplicationName();
    }

    /**
     * Returns the property "appVersion".
     * @return the property "appVersion"
     */
    @JsxGetter
    public String getAppVersion() {
        return getBrowserVersion().getApplicationVersion();
    }

    /**
     * Returns the language of the browser (for IE).
     * @return the language
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBrowserLanguage() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Returns the language of the browser (for Mozilla).
     * @return the language
     */
    @JsxGetter(@WebBrowser(FF))
    public String getLanguage() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Returns the property "cookieEnabled".
     * @return the property "cookieEnabled"
     */
    @JsxGetter
    public boolean getCookieEnabled() {
        return getWindow().getWebWindow().getWebClient().getCookieManager().isCookiesEnabled();
    }

    /**
     * Returns the property "cpuClass".
     * @return the property "cpuClass"
     */
    @JsxGetter(@WebBrowser(IE))
    public String getCpuClass() {
        return getBrowserVersion().getCpuClass();
    }

    /**
     * Returns the property "onLine".
     * @return the property "onLine"
     */
    @JsxGetter
    public boolean getOnLine() {
        return getBrowserVersion().isOnLine();
    }

    /**
     * Returns the property "platform".
     * @return the property "platform"
     */
    @JsxGetter
    public String getPlatform() {
        return getBrowserVersion().getPlatform();
    }

    /**
     * Returns the property "product".
     * @return the property "product"
     */
    @JsxGetter(@WebBrowser(FF))
    public String getProduct() {
        return "Gecko";
    }

    /**
     * Returns the build number of the current browser.
     * @see <a href="https://developer.mozilla.org/en/navigator.productSub">Mozilla Doc</a>
     * @return false
     */
    @JsxGetter(@WebBrowser(FF))
    public String getProductSub() {
        return "20100215";
    }

    /**
     * Returns the property "systemLanguage".
     * @return the property "systemLanguage"
     */
    @JsxGetter(@WebBrowser(IE))
    public String getSystemLanguage() {
        return getBrowserVersion().getSystemLanguage();
    }

    /**
     * Returns the property "userAgent".
     * @return the property "userAgent"
     */
    @JsxGetter
    public String getUserAgent() {
        return getBrowserVersion().getUserAgent();
    }

    /**
     * Returns the property "userLanguage".
     * @return the property "userLanguage"
     */
    @JsxGetter(@WebBrowser(IE))
    public String getUserLanguage() {
        return getBrowserVersion().getUserLanguage();
    }

    /**
     * Returns an empty array because HtmlUnit does not support embedded objects.
     * @return an empty array
     */
    @JsxGetter
    public Object getPlugins() {
        initPlugins();
        return plugins_;
    }

    private void initPlugins() {
        if (plugins_ != null) {
            return;
        }
        plugins_ = new PluginArray();
        plugins_.setParentScope(this);
        plugins_.setPrototype(getPrototype(PluginArray.class));

        mimeTypes_ = new MimeTypeArray();
        mimeTypes_.setParentScope(this);
        mimeTypes_.setPrototype(getPrototype(MimeTypeArray.class));

        for (final PluginConfiguration pluginConfig : getBrowserVersion().getPlugins()) {
            final Plugin plugin = new Plugin(pluginConfig.getName(), pluginConfig.getDescription(),
                pluginConfig.getFilename());
            plugin.setParentScope(this);
            plugin.setPrototype(getPrototype(Plugin.class));
            plugins_.add(plugin);

            for (final PluginConfiguration.MimeType mimeTypeConfig : pluginConfig.getMimeTypes()) {
                final MimeType mimeType = new MimeType(mimeTypeConfig.getType(), mimeTypeConfig.getDescription(),
                    mimeTypeConfig.getSuffixes(), plugin);
                mimeType.setParentScope(this);
                mimeType.setPrototype(getPrototype(MimeType.class));
                mimeTypes_.add(mimeType);
                plugin.add(mimeType);
            }
        }
    }

    /**
     * Returns an empty array because HtmlUnit does not support embedded objects.
     * @return an empty array
     */
    @JsxGetter
    public Object getMimeTypes() {
        initPlugins();
        return mimeTypes_;
    }

    /**
     * Indicates if Java is enabled.
     * @return true/false (see {@link com.gargoylesoftware.htmlunit.WebClientOptions#isAppletEnabled()}
     */
    @JsxFunction
    public boolean javaEnabled() {
        return getWindow().getWebWindow().getWebClient().getOptions().isAppletEnabled();
    }

    /**
     * Returns <tt>false</tt> always as data tainting support is not enabled in HtmlUnit.
     * @return false
     */
    @JsxFunction
    public boolean taintEnabled() {
        return false;
    }

    /**
     * Returns the geolocation.
     * @return the geolocation
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public Geolocation getGeolocation() {
        final Geolocation geolocation = new Geolocation();
        geolocation.setPrototype(getPrototype(geolocation.getClass()));
        geolocation.setParentScope(getParentScope());
        return geolocation;
    }

    /**
     * Returns the buildID.
     * @return the buildID
     */
    @JsxGetter(@WebBrowser(FF))
    public String getBuildID() {
        return getBrowserVersion().getBuildId();
    }

    /**
     * Returns the vendor.
     * @return the vendor
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public String getVendor() {
        final BrowserVersion browser = getBrowserVersion();
        if (browser.getNickname().startsWith("FF")) {
            return "";
        }
        return "Google Inc.";
    }

    /**
     * Returns the vendorSub.
     * @return the vendorSub
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public String getVendorSub() {
        return "";
    }

    /**
     * Returns the doNotTrack.
     * @return the doNotTrack
     */
    @JsxGetter(@WebBrowser(value = FF, minVersion = 10))
    public String getDoNotTrack() {
        if (getWindow().getWebWindow().getWebClient().getOptions().isDoNotTrackEnabled()) {
            return "yes";
        }
        return "unspecified";
    }

    /**
     * Returns the msDoNotTrack.
     * @return the msDoNotTrack
     */
    public String getMsDoNotTrack() {
        if (getWindow().getWebWindow().getWebClient().getOptions().isDoNotTrackEnabled()) {
            return "1";
        }
        return "0";
    }

    /**
     * Returns the oscpu.
     * @return the oscpu
     */
    @JsxGetter(@WebBrowser(FF))
    public String getOscpu() {
        return "Windows NT 6.1";
    }
}
