/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

/**
 * Objects of this class represent one specific version of a given browser.  Predefined
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

    /** Application code name for both Microsoft Internet Explorer and Netscape series */
    public static final String APP_CODE_NAME = "Mozilla";

    /** Application name for the Microsoft Internet Explorer series of browsers */
    public static final String INTERNET_EXPLORER = "Microsoft Internet Explorer";

    /** Application name the Netscape navigator series of browsers */
    public static final String NETSCAPE = "Netscape";

    /** United States English language identifier. */
    public static final String LANGUAGE_ENGLISH_US = "en-us";

    /** The X86 CPU class. */
    public static final String CPU_CLASS_X86 = "x86";

    /** The WIN32 platform. */
    public static final String PLATFORM_WIN32 = "Win32";

    /**
     * A fake browser that supports all the new features.  This constant is used whenever
     * you don't care which browser is being simulated.
     */
    public static final BrowserVersion FULL_FEATURED_BROWSER = new BrowserVersion(
        INTERNET_EXPLORER, "4.0 (compatible; MSIE 6.0b; Windows 98)",
        "Mozilla/4.0 (compatible; MSIE 6.0b; Windows 98)", "1.2", 6);

    /** Mozilla 1.0 */
    public static final BrowserVersion MOZILLA_1_0 = new BrowserVersion(
        NETSCAPE, "5.0 (Windows; en-US)",
        "Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.0.0) Gecko/20020530", "1.2", 6);

    /** Netscape 4.79 */
    public static final BrowserVersion NETSCAPE_4_7_9 = new BrowserVersion(
        NETSCAPE, "4.79 [en] (Windows NT 5.0; U)",
        "Mozilla/4.79 [en] (Windows NT 5.0; U)", "1.2", 4.79f);

    /** Netscape 6.2.3 */
    public static final BrowserVersion NETSCAPE_6_2_3 = new BrowserVersion(
        NETSCAPE, "5.0 (Windows; en-US)",
        "Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US;rv:0.9.4.1) Gecko/20020508 Netscape6/6.2.3",
        "1.2", 6);

    /** Internet explorer 6.0  */
    public static final BrowserVersion INTERNET_EXPLORER_6_0 = new BrowserVersion(
        INTERNET_EXPLORER, "4.0 (compatible; MSIE 6.0b; Windows 98)",
        "4.0 (compatible; MSIE 6.0; Windows 98)", "1.2", 6);

    private static BrowserVersion DefaultBrowserVersion_ = FULL_FEATURED_BROWSER;

    /**
     * Instantiate one.
     *
     * @param applicationName The name of the application
     * @param applicationVersion The version string of the application
     * @param userAgent The user agent string that will be sent to the server
     * @param javaScriptVersion The version of JavaScript
     * @param browserVersionNumeric The floating number version of the browser
     */
    public BrowserVersion( final String applicationName, final String applicationVersion,
        final String userAgent, final String javaScriptVersion, final float browserVersionNumeric) {

        applicationName_ = applicationName;
        setApplicationVersion(applicationVersion);
        userAgent_ = userAgent;
        setJavaScriptVersion(javaScriptVersion);
        browserVersionNumeric_ = browserVersionNumeric;
    }

    /**
     * Return the default version that is used whenever a specific version isn't specified.
     * @return The default version.
     */
    public static BrowserVersion getDefault() {
        return DefaultBrowserVersion_;
    }

    /**
     * Set the default version that is used whenever a specific version isn't specified.
     * @param newBrowserVersion The new default version.
     */
    public static void setDefault( final BrowserVersion newBrowserVersion ) {
        Assert.notNull("newBrowserVersion", newBrowserVersion);
        DefaultBrowserVersion_ = newBrowserVersion;
    }

    /**
     * Returns <tt>true</tt> if this <tt>BrowserVersion</tt> instance represents some
     * version of Microsoft Internet Explorer.
     * @return Whether or not this version is a version of IE.
     */
    public final boolean isIE() {
        return INTERNET_EXPLORER.equals( getApplicationName() );
    }

    /**
     * Returns <tt>true</tt> if this <tt>BrowserVersion</tt> instance represents some
     * version of a Netscape browser, including Mozilla and Firefox.
     * @return Whether or not this version is a version of a Netscape browser.
     */
    public final boolean isNetscape() {
        return NETSCAPE.equals( getApplicationName() );
    }

    /**
     * Return the application code name, for example "Mozilla".
     * Default value is {@link #APP_CODE_NAME} if not explicitly configured. 
     * @return The application code name.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/appcodename.asp">
     * MSDN documentation</a>
     */
    public String getApplicationCodeName() {
        return applicationCodeName_;
    }

    /**
     * Return the application minor version, for example "0".
     * Default value is "0" if not explicitly configured. 
     * @return The application minor version.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/appminorversion.asp">
     * MSDN documentation</a>
     */
    public String getApplicationMinorVersion() {
        return applicationMinorVersion_;
    }

    /**
     * Return the application name, for example "Microsoft Internet Explorer".
     * @return The application name.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/appname.asp">
     * MSDN documentation</a>
     */
    public String getApplicationName() {
        return applicationName_;
    }

    /**
     * Return the application version, for example "4.0 (compatible; MSIE 6.0b; Windows 98)".
     * @return The application version.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/appversion.asp">
     * MSDN documentation</a>
     */
    public String getApplicationVersion() {
        return applicationVersion_;
    }

    /**
     * Return the browser application language, for example "en-us".
     * Default value is {@link #LANGUAGE_ENGLISH_US} if not explicitly configured. 
     * @return The browser application language.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/browserlanguage.asp">
     * MSDN documentation</a>
     */
    public String getBrowserLanguage() {
        return browserLanguage_;
    }

    /**
     * Return the type of CPU in the machine, for example "x86".
     * Default value is {@link #CPU_CLASS_X86} if not explicitly configured. 
     * @return The type of CPU in the machine.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/cpuclass.asp">
     * MSDN documentation</a>
     */
    public String getCpuClass() {
        return cpuClass_;
    }

    /**
     * Return <tt>true</tt> if the browser is currently online.
     * Default value is <code>true</code> if not explicitly configured. 
     * @return <tt>true</tt> if the browser is currently online.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/online.asp">
     * MSDN documentation</a>
     */
    public boolean isOnLine() {
        return onLine_;
    }

    /**
     * Return the platform on which the application is running, for example "Win32".
     * Default value is {@link #PLATFORM_WIN32} if not explicitly configured. 
     * @return the platform on which the application is running.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/platform.asp">
     * MSDN documentation</a>
     */
    public String getPlatform() {
        return platform_;
    }

    /**
     * Return the system language, for example "en-us".
     * Default value is {@link #LANGUAGE_ENGLISH_US} if not explicitly configured. 
     * @return The system language.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/systemlanguage.asp">
     * MSDN documentation</a>
     */
    public String getSystemLanguage() {
        return systemLanguage_;
    }

    /**
     * Return the user agent string, for example "Mozilla/4.0 (compatible; MSIE 6.0b; Windows 98)".
     * @return The user agent string.
     */
    public String getUserAgent() {
        return userAgent_;
    }

    /**
     * Return the user language, for example "en-us".
     * Default value is {@link #LANGUAGE_ENGLISH_US} if not explicitly configured. 
     * @return The user language.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/userlanguage.asp">
     * MSDN documentation</a>
     */
    public String getUserLanguage() {
        return userLanguage_;
    }

    /**
     * Return the version of javascript used by the browser, for example "1.2".
     * @return the version of javascript used by the browser.
     */
    public String getJavaScriptVersion() {
        return javaScriptVersion_;
    }

    /**
     * @param applicationCodeName The applicationCodeName to set.
     */
    public void setApplicationCodeName(final String applicationCodeName) {
        applicationCodeName_ = applicationCodeName;
    }

    /**
     * @param applicationMinorVersion The applicationMinorVersion to set.
     */
    public void setApplicationMinorVersion(final String applicationMinorVersion) {
        applicationMinorVersion_ = applicationMinorVersion;
    }

    /**
     * @param applicationName The applicationName to set.
     */
    public void setApplicationName(final String applicationName) {
        applicationName_ = applicationName;
    }

    /**
     * @param applicationVersion The applicationVersion to set.
     */
    public void setApplicationVersion(final String applicationVersion) {
        applicationVersion_ = applicationVersion;
    }

    /**
     * @param browserLanguage The browserLanguage to set.
     */
    public void setBrowserLanguage(final String browserLanguage) {
        browserLanguage_ = browserLanguage;
    }

    /**
     * @param cpuClass The cpuClass to set.
     */
    public void setCpuClass(final String cpuClass) {
        cpuClass_ = cpuClass;
    }

    /**
     * @param javaScriptVersion The javaScriptVersion to set.
     */
    public void setJavaScriptVersion(final String javaScriptVersion) {
        javaScriptVersion_ = javaScriptVersion;
        javaScriptVersionNumeric_ = Float.parseFloat(javaScriptVersion);
    }

    /**
     * @param onLine The onLine to set.
     */
    public void setOnLine(final boolean onLine) {
        onLine_ = onLine;
    }

    /**
     * @param platform The platform to set.
     */
    public void setPlatform(final String platform) {
        platform_ = platform;
    }

    /**
     * @param systemLanguage The systemLanguage to set.
     */
    public void setSystemLanguage(final String systemLanguage) {
        systemLanguage_ = systemLanguage;
    }

    /**
     * @param userAgent The userAgent to set.
     */
    public void setUserAgent(final String userAgent) {
        userAgent_ = userAgent;
    }

    /**
     * @param userLanguage The userLanguage to set.
     */
    public void setUserLanguage(final String userLanguage) {
        userLanguage_ = userLanguage;
    }
    /**
     * @param browserVersion The browserVersion to set.
     */
    public void setBrowserVersion(final float browserVersion) {
        browserVersionNumeric_ = browserVersion;
    }
    /**
     * @return Returns the browserVersionNumeric.
     */
    public float getBrowserVersionNumeric() {
        return browserVersionNumeric_;
    }
    /**
     * @return Returns the javaScriptVersionNumeric.
     */
    public float getJavaScriptVersionNumeric() {
        return javaScriptVersionNumeric_;
    }
}
