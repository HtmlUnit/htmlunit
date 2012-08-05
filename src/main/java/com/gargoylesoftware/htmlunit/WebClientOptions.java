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

import java.io.Serializable;
import java.net.URL;
import java.security.GeneralSecurityException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;

/**
 * Represents options of a {@link WebClient}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class WebClientOptions implements Serializable {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(WebClientOptions.class);

    private WebClient webClient_;
    private boolean javaScriptEnabled_ = true;
    private boolean cssEnabled_ = true;
    private boolean appletEnabled_;
    private boolean popupBlockerEnabled_;
    private boolean isRedirectEnabled_ = true;
    private URL sslClientCertificateUrl_;
    private String sslClientCertificatePassword_;
    private String sslClientCertificateType_;
    private boolean geolocationEnabled_;
    private boolean doNotTrackEnabled_;

    /**
     * Creates an instance.
     * @param webClient the webclient to which this options object belong
     */
    WebClientOptions(final WebClient webClient) {
        webClient_ = webClient;
    }

    /**
     * If set to <code>true</code>, the client will accept connections to any host, regardless of
     * whether they have valid certificates or not. This is especially useful when you are trying to
     * connect to a server with expired or corrupt certificates.
     * <p>
     * This method works only if {@link #getWebConnection()} returns an {@link HttpWebConnection}
     * (which is the default) or a {@link WebConnectionWrapper} wrapping an {@link HttpWebConnection}.
     * </p>
     * @param useInsecureSSL whether or not to use insecure SSL
     * @throws GeneralSecurityException if a security error occurs
     */
    public void setUseInsecureSSL(final boolean useInsecureSSL) throws GeneralSecurityException {
        //FIXME Depends on the implementation.
        WebConnection webConnection = webClient_.getWebConnection();
        while (webConnection instanceof WebConnectionWrapper) {
            webConnection = ((WebConnectionWrapper) webConnection).getWrappedWebConnection();
        }

        if (webConnection instanceof HttpWebConnection) {
            ((HttpWebConnection) webConnection).setUseInsecureSSL(useInsecureSSL);
        }
        else {
            LOG.warn("Can't configure useInsecureSSL on " + webConnection);
        }
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
}
