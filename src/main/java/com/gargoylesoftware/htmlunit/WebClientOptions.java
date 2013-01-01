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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.net.URL;

/**
 * Represents options of a {@link WebClient}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class WebClientOptions implements Serializable {

    private boolean javaScriptEnabled_ = true;
    private boolean cssEnabled_ = true;
    private boolean printContentOnFailingStatusCode_ = true;
    private boolean throwExceptionOnFailingStatusCode_ = true;
    private boolean throwExceptionOnScriptError_ = true;
    private boolean appletEnabled_;
    private boolean popupBlockerEnabled_;
    private boolean isRedirectEnabled_ = true;
    private URL sslClientCertificateUrl_;
    private String sslClientCertificatePassword_;
    private String sslClientCertificateType_;
    private boolean geolocationEnabled_;
    private boolean doNotTrackEnabled_;
    private boolean activeXNative_;
    private String homePage_ = "http://htmlunit.sf.net/";
    private ProxyConfig proxyConfig_;
    private int timeout_ = 90000; // like Firefox 16 default's value for network.http.connection-timeout

    private boolean useInsecureSSL_ = false; // default is secure SSL

    /**
     * If set to <code>true</code>, the client will accept connections to any host, regardless of
     * whether they have valid certificates or not. This is especially useful when you are trying to
     * connect to a server with expired or corrupt certificates.
     * @param useInsecureSSL whether or not to use insecure SSL
     */
    public void setUseInsecureSSL(final boolean useInsecureSSL) {
        useInsecureSSL_ = useInsecureSSL;
    }

    /**
     * Indicates if insecure SSL should be used.
     * @return <code>true</code> if insecure SSL should be used. Default is <code>false</code>.
     */
    public boolean isUseInsecureSSL() {
        return useInsecureSSL_;
    }

    /**
     * Sets whether or not redirections will be followed automatically on receipt of a redirect
     * status code from the server.
     * @param enabled true to enable automatic redirection
     */
    public void setRedirectEnabled(final boolean enabled) {
        isRedirectEnabled_ = enabled;
    }

    /**
     * Returns whether or not redirections will be followed automatically on receipt of
     * a redirect status code from the server.
     * @return true if automatic redirection is enabled
     */
    public boolean isRedirectEnabled() {
        return isRedirectEnabled_;
    }

    /**
     * Sets the SSL client certificate to use.
     * The needed parameters are used to construct a {@link java.security.KeyStore}.
     *
     * If the web server requires Renegotiation, you have to set sytem property
     * "sun.security.ssl.allowUnsafeRenegotiation" to true, as hinted in
     * <a href="http://www.oracle.com/technetwork/java/javase/documentation/tlsreadme2-176330.html">
     * TLS Renegotiation Issue</a>.
     *
     * @param certificateUrl the URL which locates the certificate
     * @param certificatePassword the certificate password
     * @param certificateType the type of certificate, usually "jks" or "pkcs12".
     */
    public void setSSLClientCertificate(final URL certificateUrl, final String certificatePassword,
            final String certificateType) {
        sslClientCertificateUrl_ = certificateUrl;
        sslClientCertificatePassword_ = certificatePassword;
        sslClientCertificateType_ = certificateType;
    }

    URL getSSLClientCertificateUrl() {
        return sslClientCertificateUrl_;
    }

    String getSSLClientCertificatePassword() {
        return sslClientCertificatePassword_;
    }

    String getSSLClientCertificateType() {
        return sslClientCertificateType_;
    }

    /**
     * Enables/disables JavaScript support. By default, this property is enabled.
     *
     * @param enabled <tt>true</tt> to enable JavaScript support
     */
    public void setJavaScriptEnabled(final boolean enabled) {
        javaScriptEnabled_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if JavaScript is enabled and the script engine was loaded successfully.
     *
     * @return <tt>true</tt> if JavaScript is enabled
     */
    public boolean isJavaScriptEnabled() {
        return javaScriptEnabled_;
    }

    /**
     * Enables/disables CSS support. By default, this property is enabled.
     *
     * @param enabled <tt>true</tt> to enable CSS support
     */
    public void setCssEnabled(final boolean enabled) {
        cssEnabled_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if CSS is enabled.
     *
     * @return <tt>true</tt> if CSS is enabled
     */
    public boolean isCssEnabled() {
        return cssEnabled_;
    }

    /**
     * Enables/disables Applet support. By default, this property is disabled.<br/>
     * <p>
     * Note: as of HtmlUnit-2.4, Applet support is experimental and minimal
     * </p>
     * @param enabled <tt>true</tt> to enable Applet support
     * @since HtmlUnit-2.4
     */
    public void setAppletEnabled(final boolean enabled) {
        appletEnabled_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if Applet are enabled.
     *
     * @return <tt>true</tt> if Applet is enabled
     */
    public boolean isAppletEnabled() {
        return appletEnabled_;
    }

    /**
     * Enable/disable the popup window blocker. By default, the popup blocker is disabled, and popup
     * windows are allowed. When set to <tt>true</tt>, <tt>window.open()</tt> has no effect and
     * returns <tt>null</tt>.
     *
     * @param enabled <tt>true</tt> to enable the popup window blocker
     */
    public void setPopupBlockerEnabled(final boolean enabled) {
        popupBlockerEnabled_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if the popup window blocker is enabled.
     *
     * @return <tt>true</tt> if the popup window blocker is enabled
     */
    public boolean isPopupBlockerEnabled() {
        return popupBlockerEnabled_;
    }

    /**
     * Enables/disables Geolocation support. By default, this property is disabled.
     *
     * @param enabled <tt>true</tt> to enable Geolocation support
     */
    public void setGeolocationEnabled(final boolean enabled) {
        geolocationEnabled_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if Geolocation is enabled.
     *
     * @return <tt>true</tt> if Geolocation is enabled
     */
    public boolean isGeolocationEnabled() {
        return geolocationEnabled_;
    }

    /**
     * Enables/disables "Do Not Track" support. By default, this property is disabled.
     *
     * @param enabled <tt>true</tt> to enable "Do Not Track" support
     */
    public void setDoNotTrackEnabled(final boolean enabled) {
        doNotTrackEnabled_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if "Do Not Track" is enabled.
     *
     * @return <tt>true</tt> if "Do Not Track" is enabled
     */
    public boolean isDoNotTrackEnabled() {
        return doNotTrackEnabled_;
    }

    /**
     * Specify whether or not the content of the resulting document will be
     * printed to the console in the event of a failing response code.
     * Successful response codes are in the range 200-299. The default is true.
     *
     * @param enabled True to enable this feature
     */
    public void setPrintContentOnFailingStatusCode(final boolean enabled) {
        printContentOnFailingStatusCode_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if the content of the resulting document will be printed to
     * the console in the event of a failing response code.
     *
     * @return <tt>true</tt> if the content of the resulting document will be printed to
     *         the console in the event of a failing response code
     * @see #setPrintContentOnFailingStatusCode
     */
    public boolean getPrintContentOnFailingStatusCode() {
        return printContentOnFailingStatusCode_;
    }

    /**
     * Specify whether or not an exception will be thrown in the event of a
     * failing status code. Successful status codes are in the range 200-299.
     * The default is true.
     *
     * @param enabled <tt>true</tt> to enable this feature
     */
    public void setThrowExceptionOnFailingStatusCode(final boolean enabled) {
        throwExceptionOnFailingStatusCode_ = enabled;
    }

    /**
     * Returns <tt>true</tt> if an exception will be thrown in the event of a failing response code.
     * @return <tt>true</tt> if an exception will be thrown in the event of a failing response code
     * @see #setThrowExceptionOnFailingStatusCode
     */
    public boolean isThrowExceptionOnFailingStatusCode() {
        return throwExceptionOnFailingStatusCode_;
    }

    /**
     * Indicates if an exception should be thrown when a script execution fails
     * (the default) or if it should be caught and just logged to allow page
     * execution to continue.
     * @return <code>true</code> if an exception is thrown on script error (the default)
     */
    public boolean isThrowExceptionOnScriptError() {
        return throwExceptionOnScriptError_;
    }

    /**
     * Changes the behavior of this webclient when a script error occurs.
     * @param enabled indicates if exception should be thrown or not
     */
    public void setThrowExceptionOnScriptError(final boolean enabled) {
        throwExceptionOnScriptError_ = enabled;
    }

    /**
     * Sets whether to allow native ActiveX or no. Default value is false.
     * Beware that you should never allow running native ActiveX components unless you fully trust
     * the JavaScript code, as it is not controlled by the Java Virtual Machine.
     *
     * @param allow whether to allow or no
     */
    public void setActiveXNative(final boolean allow) {
        activeXNative_ = allow;
    }

    /**
     * Returns whether native ActiveX components are allowed or no.
     * @return whether native ActiveX components are allowed or no
     */
    public boolean isActiveXNative() {
        return activeXNative_;
    }

    /**
     * Returns the client's current homepage.
     * @return the client's current homepage
     */
    public String getHomePage() {
        return homePage_;
    }

    /**
     * Sets the client's homepage.
     * @param homePage the new homepage URL
     */
    public void setHomePage(final String homePage) {
        homePage_ = homePage;
    }

    /**
     * Returns the proxy configuration for this client.
     * @return the proxy configuration for this client
     */
    public ProxyConfig getProxyConfig() {
        return proxyConfig_;
    }

    /**
     * Sets the proxy configuration for this client.
     * @param proxyConfig the proxy configuration for this client
     */
    public void setProxyConfig(final ProxyConfig proxyConfig) {
        WebAssert.notNull("proxyConfig", proxyConfig);
        proxyConfig_ = proxyConfig;
    }

    /**
     * Gets the timeout value for the {@link WebConnection}.
     * The default timeout is 90 seconds (it was 0 up to HtmlUnit-2.11).
     * @return the timeout value in milliseconds
     * @see WebClientOptions#setTimeout(int)
     */
    public int getTimeout() {
        return timeout_;
    }

    /**
     * <p>Sets the timeout of the {@link WebConnection}. Set to zero for an infinite wait.</p>
     *
     * <p>Note: The timeout is used twice. The first is for making the socket connection, the second is
     * for data retrieval. If the time is critical you must allow for twice the time specified here.</p>
     *
     * @param timeout the value of the timeout in milliseconds
     */
    public void setTimeout(final int timeout) {
        timeout_ = timeout;
    }
}
