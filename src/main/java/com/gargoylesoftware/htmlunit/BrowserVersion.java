/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Objects of this class represent one specific version of a given browser. Predefined
 * constants are provided for common browser versions.
 *
 * If you wish to create a BrowserVersion for a browser that doesn't have a constant defined
 * but aren't sure what values to pass into the constructor then point your browser at
 * <a href="http://htmlunit.sourceforge.net/cgi-bin/browserVersion">
 * http://htmlunit.sourceforge.net/cgi-bin/browserVersion</a>
 * and the code will be generated for you.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class BrowserVersion implements Serializable {

    private static final long serialVersionUID = 594005988985654117L;

    private String applicationCodeName_ = APP_CODE_NAME;
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
    private String javaScriptVersion_;
    private float javaScriptVersionNumeric_;
    private float browserVersionNumeric_;
    private Set<PluginConfiguration> plugins_ = new HashSet<PluginConfiguration>();

    /** Application code name for both Microsoft Internet Explorer and Netscape series. */
    public static final String APP_CODE_NAME = "Mozilla";

    /** Application name for the Microsoft Internet Explorer series of browsers. */
    public static final String INTERNET_EXPLORER = "Microsoft Internet Explorer";

    /** Application name the Netscape navigator series of browsers. */
    public static final String NETSCAPE = "Netscape";

    /** United States English language identifier. */
    public static final String LANGUAGE_ENGLISH_US = "en-us";

    /** The X86 CPU class. */
    public static final String CPU_CLASS_X86 = "x86";

    /** The WIN32 platform. */
    public static final String PLATFORM_WIN32 = "Win32";

    /** Firefox 2. */
    public static final BrowserVersion FIREFOX_2 = new BrowserVersion(
        NETSCAPE, "5.0 (Windows; en-US)",
        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.4) Gecko/20070515 Firefox/2.0.0.4",
        "1.2", 6);

    static {
        final PluginConfiguration pluginFlash = new PluginConfiguration("Shockwave Flash",
            "Shockwave Flash 9.0 r31", "libflashplayer.so");
        pluginFlash.getMimeTypes().add(new PluginConfiguration.MimeType("application/x-shockwave-flash",
            "Shockwave Flash", "swf"));
        FIREFOX_2.getPlugins().add(pluginFlash);
    }

    /** Internet Explorer 6. */
    public static final BrowserVersion INTERNET_EXPLORER_6_0 = new BrowserVersion(
        INTERNET_EXPLORER, "4.0 (compatible; MSIE 6.0b; Windows 98)",
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows 98)", "1.2", 6);

    /** Internet Explorer 7. */
    public static final BrowserVersion INTERNET_EXPLORER_7_0 = new BrowserVersion(
        INTERNET_EXPLORER, "4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.1.4322)",
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.1.4322)", "1.2", 7);

    /** The default browser version. */
    private static BrowserVersion DefaultBrowserVersion_ = INTERNET_EXPLORER_6_0;

    /**
     * Instantiate one.
     *
     * @param applicationName the name of the application
     * @param applicationVersion the version string of the application
     * @param userAgent the user agent string that will be sent to the server
     * @param javaScriptVersion the version of JavaScript
     * @param browserVersionNumeric the floating number version of the browser
     */
    public BrowserVersion(final String applicationName, final String applicationVersion,
        final String userAgent, final String javaScriptVersion, final float browserVersionNumeric) {

        applicationName_ = applicationName;
        setApplicationVersion(applicationVersion);
        userAgent_ = userAgent;
        setJavaScriptVersion(javaScriptVersion);
        browserVersionNumeric_ = browserVersionNumeric;
    }

    /**
     * Returns the default browser version that is used whenever a specific version isn't specified.
     * Defaults to {@link #INTERNET_EXPLORER_6_0}.
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
     * version of Microsoft Internet Explorer.
     * @return whether or not this version is a version of IE
     */
    public final boolean isIE() {
        return INTERNET_EXPLORER.equals(getApplicationName());
    }

    /**
     * Returns <tt>true</tt> if this <tt>BrowserVersion</tt> instance represents some
     * version of a Netscape browser, including Mozilla and Firefox.
     * @return whether or not this version is a version of a Netscape browser
     */
    public final boolean isNetscape() {
        return NETSCAPE.equals(getApplicationName());
    }

    /**
     * Returns the application code name, for example "Mozilla".
     * Default value is {@link #APP_CODE_NAME} if not explicitly configured.
     * @return the application code name
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/appcodename.asp">
     * MSDN documentation</a>
     */
    public String getApplicationCodeName() {
        return applicationCodeName_;
    }

    /**
     * Returns the application minor version, for example "0".
     * Default value is "0" if not explicitly configured.
     * @return the application minor version
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/appminorversion.asp">
     * MSDN documentation</a>
     */
    public String getApplicationMinorVersion() {
        return applicationMinorVersion_;
    }

    /**
     * Returns the application name, for example "Microsoft Internet Explorer".
     * @return the application name
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/appname.asp">
     * MSDN documentation</a>
     */
    public String getApplicationName() {
        return applicationName_;
    }

    /**
     * Returns the application version, for example "4.0 (compatible; MSIE 6.0b; Windows 98)".
     * @return the application version
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/appversion.asp">
     * MSDN documentation</a>
     */
    public String getApplicationVersion() {
        return applicationVersion_;
    }

    /**
     * Returns the browser application language, for example "en-us".
     * Default value is {@link #LANGUAGE_ENGLISH_US} if not explicitly configured.
     * @return the browser application language
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/browserlanguage.asp">
     * MSDN documentation</a>
     */
    public String getBrowserLanguage() {
        return browserLanguage_;
    }

    /**
     * Returns the type of CPU in the machine, for example "x86".
     * Default value is {@link #CPU_CLASS_X86} if not explicitly configured.
     * @return the type of CPU in the machine
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/cpuclass.asp">
     * MSDN documentation</a>
     */
    public String getCpuClass() {
        return cpuClass_;
    }

    /**
     * Returns <tt>true</tt> if the browser is currently online.
     * Default value is <code>true</code> if not explicitly configured.
     * @return <tt>true</tt> if the browser is currently online
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/online.asp">
     * MSDN documentation</a>
     */
    public boolean isOnLine() {
        return onLine_;
    }

    /**
     * Returns the platform on which the application is running, for example "Win32".
     * Default value is {@link #PLATFORM_WIN32} if not explicitly configured.
     * @return the platform on which the application is running
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/platform.asp">
     * MSDN documentation</a>
     */
    public String getPlatform() {
        return platform_;
    }

    /**
     * Returns the system language, for example "en-us".
     * Default value is {@link #LANGUAGE_ENGLISH_US} if not explicitly configured.
     * @return the system language
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/systemlanguage.asp">
     * MSDN documentation</a>
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
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/userlanguage.asp">
     * MSDN documentation</a>
     */
    public String getUserLanguage() {
        return userLanguage_;
    }

    /**
     * Returns the version of JavaScript used by the browser, for example "1.2".
     * @return the version of JavaScript used by the browser
     */
    public String getJavaScriptVersion() {
        return javaScriptVersion_;
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
     * @param javaScriptVersion the javaScriptVersion to set
     */
    public void setJavaScriptVersion(final String javaScriptVersion) {
        javaScriptVersion_ = javaScriptVersion;
        javaScriptVersionNumeric_ = Float.parseFloat(javaScriptVersion);
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
     * @return the javaScriptVersionNumeric
     */
    public float getJavaScriptVersionNumeric() {
        return javaScriptVersionNumeric_;
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
}
