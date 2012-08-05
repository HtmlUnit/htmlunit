/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.PluginConfiguration;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
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
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535867.aspx">MSDN documentation</a>
 */
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
    public String jsxGet_appCodeName() {
        return getBrowserVersion().getApplicationCodeName();
    }

    /**
     * Returns the property "appMinorVersion".
     * @return the property "appMinorVersion"
     */
    public String jsxGet_appMinorVersion() {
        return getBrowserVersion().getApplicationMinorVersion();
    }

    /**
     * Returns the property "appName".
     * @return the property "appName"
     */
    public String jsxGet_appName() {
        return getBrowserVersion().getApplicationName();
    }

    /**
     * Returns the property "appVersion".
     * @return the property "appVersion"
     */
    public String jsxGet_appVersion() {
        return getBrowserVersion().getApplicationVersion();
    }

    /**
     * Returns the language of the browser (for IE).
     * @return the language
     */
    public String jsxGet_browserLanguage() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Returns the language of the browser (for Mozilla).
     * @return the language
     */
    public String jsxGet_language() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Returns the property "cookieEnabled".
     * @return the property "cookieEnabled"
     */
    public boolean jsxGet_cookieEnabled() {
        return getWindow().getWebWindow().getWebClient().getCookieManager().isCookiesEnabled();
    }

    /**
     * Returns the property "cpuClass".
     * @return the property "cpuClass"
     */
    public String jsxGet_cpuClass() {
        return getBrowserVersion().getCpuClass();
    }

    /**
     * Returns the property "onLine".
     * @return the property "onLine"
     */
    public boolean jsxGet_onLine() {
        return getBrowserVersion().isOnLine();
    }

    /**
     * Returns the property "platform".
     * @return the property "platform"
     */
    public String jsxGet_platform() {
        return getBrowserVersion().getPlatform();
    }

    /**
     * Returns the property "product".
     * @return the property "product"
     */
    public String jsxGet_product() {
        return "Gecko";
    }

    /**
     * Returns the build number of the current browser.
     * @see <a href="https://developer.mozilla.org/en/navigator.productSub">Mozilla Doc</a>
     * @return false
     */
    public String jsxGet_productSub() {
        return "20100215";
    }

    /**
     * Returns the property "systemLanguage".
     * @return the property "systemLanguage"
     */
    public String jsxGet_systemLanguage() {
        return getBrowserVersion().getSystemLanguage();
    }

    /**
     * Returns the property "userAgent".
     * @return the property "userAgent"
     */
    public String jsxGet_userAgent() {
        return getBrowserVersion().getUserAgent();
    }

    /**
     * Returns the property "userLanguage".
     * @return the property "userLanguage"
     */
    public String jsxGet_userLanguage() {
        return getBrowserVersion().getUserLanguage();
    }

    /**
     * Returns an empty array because HtmlUnit does not support embedded objects.
     * @return an empty array
     */
    public Object jsxGet_plugins() {
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
    public Object jsxGet_mimeTypes() {
        initPlugins();
        return mimeTypes_;
    }

    /**
     * Indicates if Java is enabled.
     * @return true/false (see {@link com.gargoylesoftware.htmlunit.WebClientOptions#isAppletEnabled()}
     */
    public boolean jsxFunction_javaEnabled() {
        return getWindow().getWebWindow().getWebClient().getOptions().isAppletEnabled();
    }

    /**
     * Returns <tt>false</tt> always as data tainting support is not enabled in HtmlUnit.
     * @return false
     */
    public boolean jsxFunction_taintEnabled() {
        return false;
    }

    /**
     * Returns the geolocation.
     * @return the geolocation
     */
    public Geolocation jsxGet_geolocation() {
        final Geolocation geolocation = new Geolocation();
        geolocation.setPrototype(getPrototype(geolocation.getClass()));
        geolocation.setParentScope(getParentScope());
        return geolocation;
    }

    /**
     * Returns the buildID.
     * @return the buildID
     */
    public String jsxGet_buildID() {
        final BrowserVersion browser = getBrowserVersion();
        if ("FF3.6".equals(browser.getNickname())) {
            return "20120306064154";
        }
        return "20120713134347";
    }

    /**
     * Returns the vendor.
     * @return the vendor
     */
    public String jsxGet_vendor() {
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
    public String jsxGet_vendorSub() {
        return "";
    }

    /**
     * Returns the doNotTrack.
     * @return the doNotTrack
     */
    public String jsxGet_doNotTrack() {
        if (getWindow().getWebWindow().getWebClient().getOptions().isDoNotTrackEnabled()) {
            return "yes";
        }
        return "unspecified";
    }

    /**
     * Returns the msDoNotTrack.
     * @return the msDoNotTrack
     */
    public String jsxGet_msDoNotTrack() {
        if (getWindow().getWebWindow().getWebClient().getOptions().isDoNotTrackEnabled()) {
            return "1";
        }
        return "0";
    }
}
