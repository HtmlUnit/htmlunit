/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;


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
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class BrowserVersion {
    private final String applicationName_;
    private final String applicationVersion_;
    private final String userAgent_;
    private final String javaScriptVersion_;

    /** Constant representing the Microsoft Internet Explorer series of browsers */
    public static final String INTERNET_EXPLORER = "Microsoft Internet Explorer";

    /** Constant representing the Netscape navigator series of browsers */
    public static final String NETSCAPE = "Netscape";

    /**
     * A fake browser that supports all the new features.  This constant is used whenever
     * you don't care which browser is being simulated.
     */
    public static final BrowserVersion FULL_FEATURED_BROWSER = new BrowserVersion(
        INTERNET_EXPLORER, "4.0 (compatible; MSIE 6.0b; Windows 98)",
        "Mozilla/4.0 (compatible; MSIE 6.0b; Windows 98)", "1.2");

    /** Mozilla 1.0 */
    public static final BrowserVersion MOZILLA_1_0 = new BrowserVersion(
        NETSCAPE, "5.0 (Windows; en-US)",
        "Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.0.0) Gecko/20020530", "1.2");

    /** Netscape 4.79 */
    public static final BrowserVersion NETSCAPE_4_7_9 = new BrowserVersion(
        NETSCAPE, "4.79 [en] (Windows NT 5.0; U)",
        "Mozilla/4.79 [en] (Windows NT 5.0; U)", "1.2");

    /** Netscape 6.2.3 */
    public static final BrowserVersion NETSCAPE_6_2_3 = new BrowserVersion(
        NETSCAPE, "5.0 (Windows; en-US)",
        "Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US;rv:0.9.4.1) Gecko/20020508 Netscape6/6.2.3",
        "1.2" );

    /** Internet explorer 6.0  */
    public static final BrowserVersion INTERNET_EXPLORER_6_0 = new BrowserVersion(
        INTERNET_EXPLORER, "4.0 (compatible; MSIE 6.0b; Windows 98)",
        "4.0 (compatible; MSIE 6.0; Windows 98)", "1.2");

    private static BrowserVersion DefaultBrowserVersion_ = FULL_FEATURED_BROWSER;


    /**
     * Instantiate one.
     *
     * @param applicationName The name of the application
     * @param applicationVersion The version string of the application
     * @param userAgent The user agent string that will be sent to the server
     * @param javaScriptVersion The version of JavaScript
     */
    public BrowserVersion( final String applicationName, final String applicationVersion,
        final String userAgent, final String javaScriptVersion ) {

        applicationName_ = applicationName;
        applicationVersion_ = applicationVersion;
        userAgent_ = userAgent;
        javaScriptVersion_ = javaScriptVersion;
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
     * Return the application name.  IE "Microsoft Internet Explorer"
     * @return The application name
     */
    public String getApplicationName() {
        return applicationName_;
    }


    /**
     * Return the user agent.  IE "Mozilla/4.0 (compatible; MSIE 6.0b; Windows 98)"
     * @return The user agent
     */
    public String getUserAgent() {
        return userAgent_;
    }


    /**
     * Return the application version.  IE "4.0 (compatible; MSIE 6.0b; Windows 98)"
     * @return The application version
     */
    public String getApplicationVersion() {
        return applicationVersion_;
    }


    /**
     * Return the version of javascript.  ie "1.2"
     * @return The javascript version
     */
    public String getJavaScriptVersion() {
        return javaScriptVersion_;
    }
}
