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
package com.gargoylesoftware.htmlunit;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Objects of this class represent one specific version of a given browser. Predefined
 * constants are provided for common browser versions.
 *
 * <p>You can change the constants by something like:
 * <pre id='htmlUnitCode'>
 *      String applicationName = "APPNAME";
 *      String applicationVersion = "APPVERSION";
 *      String userAgent = "USERAGENT";
 *      int browserVersionNumeric = NUMERIC;
 *
 *      BrowserVersion browser = new BrowserVersion(applicationName, applicationVersion, userAgent, browserVersionNumeric) {
 *          public boolean hasFeature(BrowserVersionFeatures property) {
 *
 *              // change features here
 *              return BrowserVersion.BROWSER.hasFeature(property);
 *          }
 *      };
 * </pre>
 * <script>
 *     var pre = document.getElementById('htmlUnitCode');
 *     pre.innerHTML = pre.innerHTML.replace('APPNAME', navigator.appName);
 *     pre.innerHTML = pre.innerHTML.replace('APPVERSION', navigator.appVersion);
 *     pre.innerHTML = pre.innerHTML.replace('USERAGENT', navigator.userAgent);
 *     var isMicrosoft = navigator.appName.indexOf('Microsoft') != -1;
 *     var numeric = isMicrosoft ? 9 : 14;
 *     pre.innerHTML = pre.innerHTML.replace('NUMERIC', numeric);
 *     var browser = isMicrosoft ? "INTERNET_EXPLORER_8" : "FIREFOX_10";
 *     pre.innerHTML = pre.innerHTML.replace('BROWSER', browser);
 * </script>
 * However, note that the constants are not enough to fully customize the browser,
 *   you also need to look into the browser 'features', and the
 *   <a href="http://htmlunit.svn.sourceforge.net/viewvc/htmlunit/trunk/htmlunit/src/main/resources/com/gargoylesoftware/htmlunit/javascript/configuration/JavaScriptConfiguration.xml?view=markup">JavaScriptConfiguration.xml</a>
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class BrowserVersion implements Serializable {

    private String applicationCodeName_ = "Mozilla";
    private String applicationMinorVersion_ = "0";
    private String applicationName_;
    private String applicationVersion_;
    private String browserLanguage_ = LANGUAGE_ENGLISH_US;
    private String cpuClass_ = CPU_CLASS_X86;
    private boolean onLine_ = true;
    private String platform_ = PLATFORM_WIN32;
    private String systemLanguage_ = LANGUAGE_ENGLISH_US;
    private String userAgent_;
    private String userLanguage_ = LANGUAGE_ENGLISH_US;
    private float browserVersionNumeric_;
    private final Set<PluginConfiguration> plugins_ = new HashSet<PluginConfiguration>();
    private final Set<BrowserVersionFeatures> features_ = EnumSet.noneOf(BrowserVersionFeatures.class);
    private final String nickname_;

    /**
     * Application name for the Internet Explorer series of browsers.
     */
    private static final String INTERNET_EXPLORER = "Microsoft Internet Explorer";

    /**
     * Application name the Netscape navigator series of browsers.
     */
    private static final String NETSCAPE = "Netscape";

    /**
     * United States English language identifier.
     */
    private static final String LANGUAGE_ENGLISH_US = "en-us";

    /**
     * The X86 CPU class.
     */
    private static final String CPU_CLASS_X86 = "x86";

    /**
     * The WIN32 platform.
     */
    private static final String PLATFORM_WIN32 = "Win32";

    /** Firefox 3.6. */
    public static final BrowserVersion FIREFOX_3_6 = new BrowserVersion(
        NETSCAPE, "5.0 (Windows; en-US)",
        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.28) Gecko/20120306 Firefox/3.6.28",
        (float) 3.6, "FF3.6", null);

    /** Firefox Warning: experimental!!! */
    public static final BrowserVersion FIREFOX_10 = new BrowserVersion(
        NETSCAPE, "5.0 (Windows)",
        "Mozilla/5.0 (" + getFFUserAgentVariable() + "; rv:10.0.6) Gecko/20100101 Firefox/10.0.6",
        (float) 10.0, "FF10", null);

    /** Internet Explorer 6. */
    public static final BrowserVersion INTERNET_EXPLORER_6 = new BrowserVersion(
        INTERNET_EXPLORER, "4.0 (compatible; MSIE 6.0b; Windows 98)",
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows 98)", 6, "IE6", null);

    /** Internet Explorer 7. */
    public static final BrowserVersion INTERNET_EXPLORER_7 = new BrowserVersion(
        INTERNET_EXPLORER, "4.0 (compatible; MSIE 7.0; Windows NT 5.1)",
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)", 7, "IE7", null);

    /** Internet Explorer 8. */
    public static final BrowserVersion INTERNET_EXPLORER_8 = new BrowserVersion(
        INTERNET_EXPLORER, "4.0 (compatible; MSIE 8.0; Windows NT 6.0)",
        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)", 8, "IE8", null);

    /** Chrome 16. Warning: highly experimental!!! */
    public static final BrowserVersion CHROME_16 = new BrowserVersion(
        "Netscape", "5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/535.7"
        + " (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/535.7"
        + " (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7",
        16, "Chrome16", null);

    /** The default browser version. */
    private static BrowserVersion DefaultBrowserVersion_ = INTERNET_EXPLORER_7;

    /** Register plugins for the browser versions. */
    static {
        INTERNET_EXPLORER_6.initDefaultFeatures();
        INTERNET_EXPLORER_7.initDefaultFeatures();
        INTERNET_EXPLORER_8.initDefaultFeatures();

        FIREFOX_3_6.initDefaultFeatures();
        FIREFOX_10.initDefaultFeatures();

        final PluginConfiguration flash = new PluginConfiguration("Shockwave Flash",
            "Shockwave Flash 9.0 r31", "libflashplayer.so");
        flash.getMimeTypes().add(new PluginConfiguration.MimeType("application/x-shockwave-flash",
            "Shockwave Flash", "swf"));
        FIREFOX_3_6.getPlugins().add(flash);
        FIREFOX_10.getPlugins().add(flash);

        CHROME_16.initDefaultFeatures();
        CHROME_16.setApplicationCodeName("Mozilla");
        CHROME_16.setPlatform("MacIntel");
        CHROME_16.setCpuClass(null);
        CHROME_16.setBrowserLanguage("undefined");
        // there are other issues with Chrome; a different productSub, etc.
    }

    /**
     * Instantiates one.
     *
     * @param applicationName the name of the application
     * @param applicationVersion the version string of the application
     * @param userAgent the user agent string that will be sent to the server
     * @param browserVersionNumeric the floating number version of the browser
     */
    public BrowserVersion(final String applicationName, final String applicationVersion,
        final String userAgent, final float browserVersionNumeric) {

        this(applicationName, applicationVersion, userAgent,
                browserVersionNumeric, applicationName + browserVersionNumeric, null);
    }

    /**
     * Instantiates one.
     *
     * @param applicationName the name of the application
     * @param applicationVersion the version string of the application
     * @param userAgent the user agent string that will be sent to the server
     * @param browserVersionNumeric the floating number version of the browser
     * @param features the browser features
     */
    public BrowserVersion(final String applicationName, final String applicationVersion,
        final String userAgent, final float browserVersionNumeric,
        final BrowserVersionFeatures[] features) {

        this(applicationName, applicationVersion, userAgent,
                browserVersionNumeric, applicationName + browserVersionNumeric, features);
    }

    /**
     * Creates a new browser version instance.
     *
     * @param applicationName the name of the application
     * @param applicationVersion the version string of the application
     * @param userAgent the user agent string that will be sent to the server
     * @param javaScriptVersion the version of JavaScript
     * @param browserVersionNumeric the floating number version of the browser
     * @param nickname the short name of the browser (like "FF3", "IE6", ...)
     * @param features the browser features
     */
    private BrowserVersion(final String applicationName, final String applicationVersion,
        final String userAgent, final float browserVersionNumeric,
        final String nickname, final BrowserVersionFeatures[] features) {

        applicationName_ = applicationName;
        setApplicationVersion(applicationVersion);
        userAgent_ = userAgent;
        browserVersionNumeric_ = browserVersionNumeric;
        nickname_ = nickname;
        if (features != null) {
            features_.addAll(Arrays.asList(features));
        }
    }

    private void initDefaultFeatures() {
        InputStream stream = null;
        final Properties props = new Properties();
        try {
            stream = getClass().getResourceAsStream("/com/gargoylesoftware/htmlunit/javascript/configuration/"
                    + nickname_ + ".properties");
            props.load(stream);
        }
        catch (final Exception e) {
            throw new RuntimeException("Configuration file not found for BrowserVersion: " + nickname_);
        }
        finally {
            IOUtils.closeQuietly(stream);
        }

        for (final Object key : props.keySet()) {
            try {
                features_.add(BrowserVersionFeatures.valueOf(key.toString()));
            }
            catch (final IllegalArgumentException iae) {
                throw new RuntimeException("Invalid entry '"
                        + key + "' found in configuration file for BrowserVersion: " + nickname_);
            }
        }
    }

    /**
     * Returns the default browser version that is used whenever a specific version isn't specified.
     * Defaults to {@link #INTERNET_EXPLORER_7}.
     * @return the default browser version
     */
    public static BrowserVersion getDefault() {
        return DefaultBrowserVersion_;
    }

    /**
     * Sets the default browser version that is used whenever a specific version isn't specified.
     * @param newBrowserVersion the new default browser version
     */
    public static void setDefault(final BrowserVersion newBrowserVersion) {
        WebAssert.notNull("newBrowserVersion", newBrowserVersion);
        DefaultBrowserVersion_ = newBrowserVersion;
    }

    /**
     * Returns <tt>true</tt> if this <tt>BrowserVersion</tt> instance represents some
     * version of Internet Explorer.
     * @return whether or not this version is a version of IE
     */
    public final boolean isIE() {
        return INTERNET_EXPLORER.equals(getApplicationName());
    }

    /**
     * Returns <tt>true</tt> if this <tt>BrowserVersion</tt> instance represents some
     * version of Google Chrome. Note that Google Chrome does not return 'Chrome'
     * in the application name, we have to look in the nickname.
     * @return whether or not this version is a version of a Chrome browser.
     */
    public final boolean isChrome() {
        return getNickname().startsWith("Chrome");
    }

    /**
     * Returns <tt>true</tt> if this <tt>BrowserVersion</tt> instance represents some
     * version of Firefox like {@link #FIREFOX_3_6} or {@link #FIREFOX_10}.
     * @return whether or not this version is a version of a Firefox browser
     */
    public final boolean isFirefox() {
        return !isChrome() && NETSCAPE.equals(getApplicationName());
    }

    /**
     * Returns the application code name, for example "Mozilla".
     * Default value is "Mozilla" if not explicitly configured.
     * @return the application code name
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533077.aspx">MSDN documentation</a>
     */
    public String getApplicationCodeName() {
        return applicationCodeName_;
    }

    /**
     * Returns the application minor version, for example "0".
     * Default value is "0" if not explicitly configured.
     * @return the application minor version
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533078.aspx">MSDN documentation</a>
     */
    public String getApplicationMinorVersion() {
        return applicationMinorVersion_;
    }

    /**
     * Returns the application name, for example "Microsoft Internet Explorer".
     * @return the application name
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533079.aspx">MSDN documentation</a>
     */
    public String getApplicationName() {
        return applicationName_;
    }

    /**
     * Returns the application version, for example "4.0 (compatible; MSIE 6.0b; Windows 98)".
     * @return the application version
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533080.aspx">MSDN documentation</a>
     */
    public String getApplicationVersion() {
        return applicationVersion_;
    }

    /**
     * Returns the browser application language, for example "en-us".
     * Default value is {@link #LANGUAGE_ENGLISH_US} if not explicitly configured.
     * @return the browser application language
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533542.aspx">MSDN documentation</a>
     */
    public String getBrowserLanguage() {
        return browserLanguage_;
    }

    /**
     * Returns the type of CPU in the machine, for example "x86".
     * Default value is {@link #CPU_CLASS_X86} if not explicitly configured.
     * @return the type of CPU in the machine
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533697.aspx">MSDN documentation</a>
     */
    public String getCpuClass() {
        return cpuClass_;
    }

    /**
     * Returns <tt>true</tt> if the browser is currently online.
     * Default value is <code>true</code> if not explicitly configured.
     * @return <tt>true</tt> if the browser is currently online
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534307.aspx">MSDN documentation</a>
     */
    public boolean isOnLine() {
        return onLine_;
    }

    /**
     * Returns the platform on which the application is running, for example "Win32".
     * Default value is {@link #PLATFORM_WIN32} if not explicitly configured.
     * @return the platform on which the application is running
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534340.aspx">MSDN documentation</a>
     */
    public String getPlatform() {
        return platform_;
    }

    /**
     * Returns the system language, for example "en-us".
     * Default value is {@link #LANGUAGE_ENGLISH_US} if not explicitly configured.
     * @return the system language
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534653.aspx">MSDN documentation</a>
     */
    public String getSystemLanguage() {
        return systemLanguage_;
    }

    /**
     * Returns the user agent string, for example "Mozilla/4.0 (compatible; MSIE 6.0b; Windows 98)".
     * @return the user agent string
     */
    public String getUserAgent() {
        return userAgent_;
    }

    /**
     * Returns the user language, for example "en-us".
     * Default value is {@link #LANGUAGE_ENGLISH_US} if not explicitly configured.
     * @return the user language
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534713.aspx">MSDN documentation</a>
     */
    public String getUserLanguage() {
        return userLanguage_;
    }

    /**
     * @param applicationCodeName the applicationCodeName to set
     */
    public void setApplicationCodeName(final String applicationCodeName) {
        applicationCodeName_ = applicationCodeName;
    }

    /**
     * @param applicationMinorVersion the applicationMinorVersion to set
     */
    public void setApplicationMinorVersion(final String applicationMinorVersion) {
        applicationMinorVersion_ = applicationMinorVersion;
    }

    /**
     * @param applicationName the applicationName to set
     */
    public void setApplicationName(final String applicationName) {
        applicationName_ = applicationName;
    }

    /**
     * @param applicationVersion the applicationVersion to set
     */
    public void setApplicationVersion(final String applicationVersion) {
        applicationVersion_ = applicationVersion;
    }

    /**
     * @param browserLanguage the browserLanguage to set
     */
    public void setBrowserLanguage(final String browserLanguage) {
        browserLanguage_ = browserLanguage;
    }

    /**
     * @param cpuClass the cpuClass to set
     */
    public void setCpuClass(final String cpuClass) {
        cpuClass_ = cpuClass;
    }

    /**
     * @param onLine the onLine to set
     */
    public void setOnLine(final boolean onLine) {
        onLine_ = onLine;
    }

    /**
     * @param platform the platform to set
     */
    public void setPlatform(final String platform) {
        platform_ = platform;
    }

    /**
     * @param systemLanguage the systemLanguage to set
     */
    public void setSystemLanguage(final String systemLanguage) {
        systemLanguage_ = systemLanguage;
    }

    /**
     * @param userAgent the userAgent to set
     */
    public void setUserAgent(final String userAgent) {
        userAgent_ = userAgent;
    }

    /**
     * @param userLanguage the userLanguage to set
     */
    public void setUserLanguage(final String userLanguage) {
        userLanguage_ = userLanguage;
    }

    /**
     * @param browserVersion the browserVersion to set
     */
    public void setBrowserVersion(final float browserVersion) {
        browserVersionNumeric_ = browserVersion;
    }

    /**
     * @return the browserVersionNumeric
     */
    public float getBrowserVersionNumeric() {
        return browserVersionNumeric_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Returns the available plugins. This makes only sense for Firefox as only this
     * browser makes this kind of information available via JavaScript.
     * @return the available plugins
     */
    public Set<PluginConfiguration> getPlugins() {
        return plugins_;
    }

    /**
     * Indicates if this instance has the given feature. Used for HtmlUnit internal processing.
     * @param property the property name
     * @return <code>false</code> if this browser doesn't have this feature
     */
    public boolean hasFeature(final BrowserVersionFeatures property) {
        return features_.contains(property);
    }

    /**
     * Returns the short name of the browser like "FF3", "IE7", ...
     * This is used in different tests to reference the browser to which it applies.
     * @return the short name (if any)
     */
    public String getNickname() {
        return nickname_;
    }

    /**
     * For Firefox only version, starting from 4.0, returns the variable part of the user-agent.
     * @see <a href="https://developer.mozilla.org/en-US/docs/Gecko_user_agent_string_reference">MDN documentation</a>
     */
    private static String getFFUserAgentVariable() {
        final String os = System.getProperty("os.name").toLowerCase();
        if ("windows 7".equals(os)) {
            return "Windows NT 6.1";
        }
        else if ("windows vista".equals(os)) {
            return "Windows NT 6.0";
        }
        else if ("windows 2003".equals(os)) {
            return "Windows NT 5.2";
        }
        else if ("windows xp".equals(os)) {
            return "Windows NT 5.1";
        }
        else if ("windows 2000".equals(os)) {
            return "Windows NT 5.0";
        }
        else if ("mac os x".equals(os)) {
            return "Macintosh; Intel Mac OS X" + System.getProperty("os.version");
        }
        else if ("linux".equals(os)) {
            return "X11; Linux i686";
        }
        return "Windows NT 6.1";
    }
}
