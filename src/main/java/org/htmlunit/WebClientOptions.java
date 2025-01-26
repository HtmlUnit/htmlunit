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
package org.htmlunit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.FileUtils;

/**
 * Represents options of a {@link WebClient}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Madis Pärn
 * @author Ronald Brill
 */
public class WebClientOptions implements Serializable {

    /** 1920. */
    private static final int DEFAULT_SCRREN_WIDTH = 1920;
    /** 1080. */
    private static final int DEFAULT_SCRREN_HEIGHT = 1080;

    private boolean javaScriptEnabled_ = true;
    private boolean cssEnabled_ = true;
    private boolean printContentOnFailingStatusCode_ = true;
    private boolean throwExceptionOnFailingStatusCode_ = true;
    private boolean throwExceptionOnScriptError_ = true;
    private boolean popupBlockerEnabled_;
    private boolean isRedirectEnabled_ = true;
    private File tempFileDirectory_;

    private transient KeyStore sslClientCertificateStore_;
    private char[] sslClientCertificatePassword_;
    private transient KeyStore sslTrustStore_;
    private String[] sslClientProtocols_;
    private String[] sslClientCipherSuites_;

    private transient SSLContext sslContext_;
    private boolean useInsecureSSL_; // default is secure SSL
    private String sslInsecureProtocol_;

    private boolean doNotTrackEnabled_;
    private String homePage_ = "https://www.htmlunit.org/";
    private ProxyConfig proxyConfig_;
    private int timeout_ = 90_000; // like Firefox 16 default's value for network.http.connection-timeout
    private long connectionTimeToLive_ = -1; // HttpClient default

    private boolean fileProtocolForXMLHttpRequestsAllowed_;

    private int maxInMemory_ = 500 * 1024;
    private int historySizeLimit_ = 50;
    private int historyPageCacheLimit_ = Integer.MAX_VALUE;
    private InetAddress localAddress_;
    private boolean downloadImages_;
    private int screenWidth_ = DEFAULT_SCRREN_WIDTH;
    private int screenHeight_ = DEFAULT_SCRREN_HEIGHT;

    private boolean geolocationEnabled_;
    private Geolocation geolocation_;

    private boolean webSocketEnabled_ = true;
    private int webSocketMaxTextMessageSize_ = -1;
    private int webSocketMaxTextMessageBufferSize_ = -1;
    private int webSocketMaxBinaryMessageSize_ = -1;
    private int webSocketMaxBinaryMessageBufferSize_ = -1;

    private boolean isFetchPolyfillEnabled_;

    /**
     * Sets the SSLContext; if this is set it is used and some other settings are ignored
     * (protocol, keyStore, keyStorePassword, trustStore, sslClientCertificateStore, sslClientCertificatePassword).
     * <p>This property is transient (because SSLContext is not serializable)
     * @param sslContext the SSLContext, {@code null} to use for default value
     */
    public void setSSLContext(final SSLContext sslContext) {
        sslContext_ = sslContext;
    }

    /**
     * Gets the SSLContext; if this is set this is used and some other settings are ignored
     * (protocol, keyStore, keyStorePassword, trustStore, sslClientCertificateStore, sslClientCertificatePassword).
     * <p>This property is transient (because SSLContext is not serializable)
     * @return the SSLContext
     */
    public SSLContext getSSLContext() {
        return sslContext_;
    }

    /**
     * If set to {@code true}, the client will accept connections to any host, regardless of
     * whether they have valid certificates or not. This is especially useful when you are trying to
     * connect to a server with expired or corrupt certificates.
     * @param useInsecureSSL whether or not to use insecure SSL
     */
    public void setUseInsecureSSL(final boolean useInsecureSSL) {
        useInsecureSSL_ = useInsecureSSL;
    }

    /**
     * Indicates if insecure SSL should be used.
     * @return {@code true} if insecure SSL should be used. Default is {@code false}.
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
     * Returns the directory to be used for storing the response content in
     * a temporary file see {@link #getMaxInMemory()}.
     * @return the directory to be used for storing temp files or null to use the system default
     */
    public File getTempFileDirectory() {
        return tempFileDirectory_;
    }

    /**
     * Sets the directory to be used for storing the response content in
     * a temporary file see {@link #setMaxInMemory(int)}.
     * If the given directory does not exist, this creates it.
     *
     * @param tempFileDirectory the directory to be used or null to use the system default
     * @throws IOException in case of error
     */
    public void setTempFileDirectory(final File tempFileDirectory) throws IOException {
        if (tempFileDirectory != null) {
            if (tempFileDirectory.exists() && !tempFileDirectory.isDirectory()) {
                throw new IllegalArgumentException("The provided file '" + tempFileDirectory
                        + "' points to an already existing file");
            }

            if (!tempFileDirectory.exists()) {
                FileUtils.forceMkdir(tempFileDirectory);
            }
        }
        tempFileDirectory_ = tempFileDirectory;
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
     * Sets the SSL client certificate {@link KeyStore} to use.
     * <p>
     * If the web server requires Renegotiation, you have to set system property
     * "sun.security.ssl.allowUnsafeRenegotiation" to true, as hinted in
     * <a href="http://www.oracle.com/technetwork/java/javase/documentation/tlsreadme2-176330.html">
     * TLS Renegotiation Issue</a>.
     * <p>
     * In some cases the impl seems to pick old certificates from the {@link KeyStore}. To avoid
     * that, wrap your {@link KeyStore} inside your own {@link KeyStore} impl and filter out outdated
     * certificates.
     * <p>This property is transient (because KeyStore is not serializable)
     *
     * @param keyStore {@link KeyStore} to use
     * @param keyStorePassword the keystore password
     */
    public void setSSLClientCertificateKeyStore(final KeyStore keyStore, final char[] keyStorePassword) {
        sslClientCertificateStore_ = keyStore;
        sslClientCertificatePassword_ = keyStorePassword;
    }

    /**
     * Sets the SSL client certificate to use.
     * The needed parameters are used to construct a {@link java.security.KeyStore}.
     * <p>
     * If the web server requires Renegotiation, you have to set system property
     * "sun.security.ssl.allowUnsafeRenegotiation" to true, as hinted in
     * <a href="http://www.oracle.com/technetwork/java/javase/documentation/tlsreadme2-176330.html">
     * TLS Renegotiation Issue</a>.
     * <p>This property is transient (because KeyStore is not serializable)
     *
     * @param keyStoreUrl the URL which locates the certificate {@link KeyStore}
     * @param keyStorePassword the certificate {@link KeyStore} password
     * @param keyStoreType the type of certificate {@link KeyStore}, usually {@code jks} or {@code pkcs12}
     *
     */
    public void setSSLClientCertificateKeyStore(final URL keyStoreUrl, final String keyStorePassword,
            final String keyStoreType) {
        try (InputStream is = keyStoreUrl.openStream()) {
            sslClientCertificateStore_ = getKeyStore(is, keyStorePassword, keyStoreType);
            sslClientCertificatePassword_ = keyStorePassword == null ? null : keyStorePassword.toCharArray();
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the SSL client certificate {@link KeyStore} to use. The parameters are used to
     * construct the {@link KeyStore}.
     * <p>
     * If the web server requires Renegotiation, you have to set system property
     * "sun.security.ssl.allowUnsafeRenegotiation" to true, as hinted in
     * <a href="http://www.oracle.com/technetwork/java/javase/documentation/tlsreadme2-176330.html">
     * TLS Renegotiation Issue</a>.
     * <p>
     * In some cases the impl seems to pick old certificates from the {@link KeyStore}. To avoid
     * that, wrap your {@link KeyStore} inside your own {@link KeyStore} impl and filter out outdated
     * certificates. Provide the {@link KeyStore} to the options instead of the input stream.
     *
     * @param keyStoreInputStream the input stream which represents the {@link KeyStore} holding the certificates
     * @param keyStorePassword the {@link KeyStore} password
     * @param keyStoreType the type of {@link KeyStore}, usually {@code jks} or {@code pkcs12}
     */
    public void setSSLClientCertificateKeyStore(final InputStream keyStoreInputStream,
            final String keyStorePassword, final String keyStoreType) {
        try {
            setSSLClientCertificateKeyStore(
                    getKeyStore(keyStoreInputStream, keyStorePassword, keyStoreType),
                    keyStorePassword.toCharArray());
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the SSLClientCertificateStore.
     * <p>This property is transient (because KeyStore is not serializable)
     *
     * @return the KeyStore for use on SSL connections
     */
    public KeyStore getSSLClientCertificateStore() {
        return sslClientCertificateStore_;
    }

    /**
     * Gets the SSLClientCertificatePassword.
     * @return the password
     */
    public char[] getSSLClientCertificatePassword() {
        return sslClientCertificatePassword_;
    }

    /**
     * Gets the protocol versions enabled for use on SSL connections.
     * @return the protocol versions enabled for use on SSL connections
     * @see #setSSLClientProtocols(String...)
     */
    public String[] getSSLClientProtocols() {
        return sslClientProtocols_;
    }

    /**
     * Sets the protocol versions enabled for use on SSL connections,
     * {@code null} to use default ones.
     *
     * @param sslClientProtocols the protocol versions
     * @see javax.net.ssl.SSLSocket#setEnabledProtocols(String[])
     * @see #getSSLClientProtocols()
     */
    public void setSSLClientProtocols(final String... sslClientProtocols) {
        sslClientProtocols_ = sslClientProtocols;
    }

    /**
     * Gets the cipher suites enabled for use on SSL connections.
     * @return the cipher suites enabled for use on SSL connections
     * @see #setSSLClientCipherSuites(String...)
     */
    public String[] getSSLClientCipherSuites() {
        return sslClientCipherSuites_;
    }

    /**
     * Sets the cipher suites enabled for use on SSL connections,
     * {@code null} to use default ones.
     *
     * @param sslClientCipherSuites the cipher suites
     * @see javax.net.ssl.SSLSocket#setEnabledCipherSuites(String[])
     * @see #getSSLClientCipherSuites()
     */
    public void setSSLClientCipherSuites(final String... sslClientCipherSuites) {
        sslClientCipherSuites_ = sslClientCipherSuites;
    }

    /**
     * Enables/disables JavaScript support. By default, this property is enabled.
     *
     * @param enabled {@code true} to enable JavaScript support
     */
    public void setJavaScriptEnabled(final boolean enabled) {
        javaScriptEnabled_ = enabled;
    }

    /**
     * Returns {@code true} if JavaScript is enabled and the script engine was loaded successfully.
     *
     * @return {@code true} if JavaScript is enabled
     */
    public boolean isJavaScriptEnabled() {
        return javaScriptEnabled_;
    }

    /**
     * Enables/disables CSS support. By default, this property is enabled.
     * If disabled HtmlUnit will not download the linked css files and also
     * not triggered the associated onload/onerror events.
     *
     * @param enabled {@code true} to enable CSS support
     */
    public void setCssEnabled(final boolean enabled) {
        cssEnabled_ = enabled;
    }

    /**
     * Returns {@code true} if CSS is enabled.
     *
     * @return {@code true} if CSS is enabled
     */
    public boolean isCssEnabled() {
        return cssEnabled_;
    }

    /**
     * Enable/disable the popup window blocker. By default, the popup blocker is disabled, and popup
     * windows are allowed. When set to {@code true}, <code>window.open()</code> has no effect and
     * returns {@code null}.
     *
     * @param enabled {@code true} to enable the popup window blocker
     */
    public void setPopupBlockerEnabled(final boolean enabled) {
        popupBlockerEnabled_ = enabled;
    }

    /**
     * Returns {@code true} if the popup window blocker is enabled.
     *
     * @return {@code true} if the popup window blocker is enabled
     */
    public boolean isPopupBlockerEnabled() {
        return popupBlockerEnabled_;
    }

    /**
     * Enables/disables "Do Not Track" support. By default, this property is disabled.
     *
     * @param enabled {@code true} to enable "Do Not Track" support
     */
    public void setDoNotTrackEnabled(final boolean enabled) {
        doNotTrackEnabled_ = enabled;
    }

    /**
     * Returns {@code true} if "Do Not Track" is enabled.
     *
     * @return {@code true} if "Do Not Track" is enabled
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
     * Returns {@code true} if the content of the resulting document will be printed to
     * the console in the event of a failing response code.
     *
     * @return {@code true} if the content of the resulting document will be printed to
     *         the console in the event of a failing response code
     * @see #setPrintContentOnFailingStatusCode
     */
    public boolean isPrintContentOnFailingStatusCode() {
        return printContentOnFailingStatusCode_;
    }

    /**
     * Specify whether or not an exception will be thrown in the event of a
     * failing status code. Successful status codes are in the range 200-299.
     * The default is true.
     *
     * @param enabled {@code true} to enable this feature
     */
    public void setThrowExceptionOnFailingStatusCode(final boolean enabled) {
        throwExceptionOnFailingStatusCode_ = enabled;
    }

    /**
     * Returns {@code true} if an exception will be thrown in the event of a failing response code.
     * @return {@code true} if an exception will be thrown in the event of a failing response code
     * @see #setThrowExceptionOnFailingStatusCode
     */
    public boolean isThrowExceptionOnFailingStatusCode() {
        return throwExceptionOnFailingStatusCode_;
    }

    /**
     * Indicates if an exception should be thrown when a script execution fails
     * (the default) or if it should be caught and just logged to allow page
     * execution to continue.
     * @return {@code true} if an exception is thrown on script error (the default)
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
     * The default timeout is 90 seconds.
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

    /**
     * Gets the connTimeToLive value for the HttpClient connection pool.
     *
     * @return the timeout value in milliseconds
     */
    public long getConnectionTimeToLive() {
        return connectionTimeToLive_;
    }

    /**
     * Sets the connTimeToLive of the HttpClient connection pool.
     * Use this if you are working with web pages behind a DNS based load balancer.
     * Set to -1 (default) for disabling this timeout.
     *
     * @param connectionTimeToLive the value of the timeout in milliseconds
     */
    public void setConnectionTimeToLive(final long connectionTimeToLive) {
        connectionTimeToLive_ = connectionTimeToLive;
    }

    /**
     * Sets the SSL protocol, used only when {@link #setUseInsecureSSL(boolean)} is set to {@code true}.
     * @param sslInsecureProtocol the SSL protocol for insecure SSL connections,
     *      {@code null} to use for default value
     */
    public void setSSLInsecureProtocol(final String sslInsecureProtocol) {
        sslInsecureProtocol_ = sslInsecureProtocol;
    }

    /**
     * Gets the SSL protocol, to be used only when {@link #setUseInsecureSSL(boolean)} is set to {@code true}.
     * @return the SSL protocol for insecure SSL connections
     */
    public String getSSLInsecureProtocol() {
        return sslInsecureProtocol_;
    }

    /**
     * Sets the SSL server certificate trust store. All server certificates will be validated against
     * this trust store.
     * <p>This property is transient (because KeyStore is not serializable)
     * <p>The needed parameters are used to construct a {@link java.security.KeyStore}.
     *
     * @param sslTrustStoreUrl the URL which locates the trust store
     * @param sslTrustStorePassword the trust store password
     * @param sslTrustStoreType the type of trust store, usually {@code jks} or {@code pkcs12}
     */
    public void setSSLTrustStore(final URL sslTrustStoreUrl, final String sslTrustStorePassword,
            final String sslTrustStoreType) {
        try (InputStream is = sslTrustStoreUrl.openStream()) {
            sslTrustStore_ = getKeyStore(is, sslTrustStorePassword, sslTrustStoreType);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    void setSSLTrustStore(final KeyStore keyStore) {
        sslTrustStore_ = keyStore;
    }

    /**
     * Gets the SSL TrustStore.
     * <p>This property is transient (because KeyStore is not serializable)
     * @return the SSL TrustStore for insecure SSL connections
     */
    public KeyStore getSSLTrustStore() {
        return sslTrustStore_;
    }

    private static KeyStore getKeyStore(final InputStream inputStream, final String keystorePassword,
            final String keystoreType)
                    throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
        if (inputStream == null) {
            return null;
        }

        final KeyStore keyStore = KeyStore.getInstance(keystoreType);
        final char[] passwordChars = keystorePassword == null ? null : keystorePassword.toCharArray();
        keyStore.load(inputStream, passwordChars);
        return keyStore;
    }

    /**
     * Returns the maximum bytes to have in memory, after which the content is saved to a temporary file.
     * Default is 500 * 1024.
     * @return the maximum bytes in memory
     */
    public int getMaxInMemory() {
        return maxInMemory_;
    }

    /**
     * Sets the maximum bytes to have in memory, after which the content is saved to a temporary file.
     * Set this to zero or -1 to deactivate the saving at all.
     * @param maxInMemory maximum bytes in memory
     */
    public void setMaxInMemory(final int maxInMemory) {
        maxInMemory_ = maxInMemory;
    }

    /**
     * Returns the maximum number of {@link Page pages} kept in {@link WebWindow#getHistory()}.
     * @return the maximum number of pages in history
     */
    public int getHistorySizeLimit() {
        return historySizeLimit_;
    }

    /**
     * Sets the History size limit. HtmlUnit uses SoftReferences&lt;Page&gt; for
     * storing the pages that are part of the history. If you like to fine tune this
     * you can use {@link #setHistoryPageCacheLimit(int)} to limit the number of page references
     * stored by the history.
     * @param historySizeLimit maximum number of pages in history
     */
    public void setHistorySizeLimit(final int historySizeLimit) {
        historySizeLimit_ = historySizeLimit;
    }

    /**
     * Returns the maximum number of {@link Page pages} to cache in history.
     * @return the maximum number of pages to cache in history
     */
    public int getHistoryPageCacheLimit() {
        return historyPageCacheLimit_;
    }

    /**
     * Sets the maximum number of {@link Page pages} to cache in history.
     * If this value is smaller than the {{@link #getHistorySizeLimit()} than
     * HtmlUnit will only use soft references for the first historyPageCacheLimit
     * entries in the history. For older entries only the url is saved; the page
     * will be (re)retrieved on demand.
     * @param historyPageCacheLimit maximum number of pages to cache in history
     * default is Integer.MAX_VALUE; negative values are having the same effect
     * as setting this to zero.
     */
    public void setHistoryPageCacheLimit(final int historyPageCacheLimit) {
        historyPageCacheLimit_ = historyPageCacheLimit;
    }

    /**
     * Returns local address to be used for request execution.
     * <p>
     * On machines with multiple network interfaces, this parameter can be used to select the network interface
     * from which the connection originates.
     * <p>
     * Default: {@code null}
     *
     * @return the local address
     */
    public InetAddress getLocalAddress() {
        return localAddress_;
    }

    /**
     * Sets the local address to be used for request execution.
     * <p>
     * On machines with multiple network interfaces, this parameter can be used to select the network interface
     * from which the connection originates.
     *
     * @param localAddress the local address
     */
    public void setLocalAddress(final InetAddress localAddress) {
        localAddress_ = localAddress;
    }

    /**
     * Sets whether to automatically download images by default, or not.
     * @param downloadImages whether to automatically download images by default, or not
     */
    public void setDownloadImages(final boolean downloadImages) {
        downloadImages_ = downloadImages;
    }

    /**
     * Returns whether to automatically download images by default, or not.
     * @return whether to automatically download images by default, or not.
     */
    public boolean isDownloadImages() {
        return downloadImages_;
    }

    /**
     * Sets the screen width.
     *
     * @param screenWidth the screen width
     */
    public void setScreenWidth(final int screenWidth) {
        screenWidth_ = screenWidth;
    }

    /**
     * Returns the screen width.
     *
     * @return the screen width
     */
    public int getScreenWidth() {
        return screenWidth_;
    }

    /**
     * Sets the screen height.
     *
     * @param screenHeight the screen height
     */
    public void setScreenHeight(final int screenHeight) {
        screenHeight_ = screenHeight;
    }

    /**
     * Returns the screen height.
     *
     * @return the screen height
     */
    public int getScreenHeight() {
        return screenHeight_;
    }

    /**
     * Enables/disables WebSocket support. By default, this property is enabled.
     *
     * @param enabled {@code true} to enable WebSocket support
     */
    public void setWebSocketEnabled(final boolean enabled) {
        webSocketEnabled_ = enabled;
    }

    /**
     * Returns {@code true} if WebSockets are enabled.
     *
     * @return {@code true} if WebSockets are enabled
     */
    public boolean isWebSocketEnabled() {
        return webSocketEnabled_;
    }

    /**
     * @return the WebSocket maxTextMessageSize
     */
    public int getWebSocketMaxTextMessageSize() {
        return webSocketMaxTextMessageSize_;
    }

    /**
     * Sets the WebSocket maxTextMessageSize.
     *
     * @param webSocketMaxTextMessageSize the new value
     */
    public void setWebSocketMaxTextMessageSize(final int webSocketMaxTextMessageSize) {
        webSocketMaxTextMessageSize_ = webSocketMaxTextMessageSize;
    }

    /**
     * @return the WebSocket maxTextMessageBufferSize
     */
    public int getWebSocketMaxTextMessageBufferSize() {
        return webSocketMaxTextMessageBufferSize_;
    }

    /**
     * Sets the WebSocket maxTextMessageBufferSize.
     *
     * @param webSocketMaxTextMessageBufferSize the new value
     */
    public void setWebSocketMaxTextMessageBufferSize(final int webSocketMaxTextMessageBufferSize) {
        webSocketMaxTextMessageBufferSize_ = webSocketMaxTextMessageBufferSize;
    }

    /**
     * @return the WebSocket maxTextMessageSize
     */
    public int getWebSocketMaxBinaryMessageSize() {
        return webSocketMaxBinaryMessageSize_;
    }

    /**
     * Sets the WebSocket maxBinaryMessageSize.
     *
     * @param webSocketMaxBinaryMessageSize the new value
     */
    public void setWebSocketMaxBinaryMessageSize(final int webSocketMaxBinaryMessageSize) {
        webSocketMaxBinaryMessageSize_ = webSocketMaxBinaryMessageSize;
    }

    /**
     * @return the WebSocket maxBinaryMessageBufferSize
     */
    public int getWebSocketMaxBinaryMessageBufferSize() {
        return webSocketMaxBinaryMessageBufferSize_;
    }

    /**
     * Sets the WebSocket maxBinaryMessageBufferSize.
     *
     * @param webSocketMaxBinaryMessageBufferSize the new value
     */
    public void setWebSocketMaxBinaryMessageBufferSize(final int webSocketMaxBinaryMessageBufferSize) {
        webSocketMaxBinaryMessageBufferSize_ = webSocketMaxBinaryMessageBufferSize;
    }

    /**
     * Sets whether or not fetch polyfill should be used.
     * @param enabled true to enable fetch polyfill
     */
    public void setFetchPolyfillEnabled(final boolean enabled) {
        isFetchPolyfillEnabled_ = enabled;
    }

    /**
     * @return true if the fetch api polyfill is enabled
     */
    public boolean isFetchPolyfillEnabled() {
        return isFetchPolyfillEnabled_;
    }

    /**
     * Enables/disables Geolocation support. By default, this property is disabled.
     *
     * @param enabled {@code true} to enable Geolocation support
     */
    public void setGeolocationEnabled(final boolean enabled) {
        geolocationEnabled_ = enabled;
    }

    /**
     * @return {@code true} if Geolocation is enabled
     */
    public boolean isGeolocationEnabled() {
        return geolocationEnabled_;
    }

    /**
     * @return the {@link Geolocation}
     */
    public Geolocation getGeolocation() {
        return geolocation_;
    }

    /**
     * Sets the {@link Geolocation} to be used.
     * @param geolocation the new location or null
     */
    public void setGeolocation(final Geolocation geolocation) {
        geolocation_ = geolocation;
    }

    public static class Geolocation implements Serializable {
        private final double accuracy_;
        private final double latitude_;
        private final double longitude_;
        private final Double altitude_;
        private final Double altitudeAccuracy_;
        private final Double heading_;
        private final Double speed_;

        /**
         * Ctor.
         *
         * @param accuracy the accuracy
         * @param latitude the latitude
         * @param longitude the longitude
         * @param altitude the altitude or null
         * @param altitudeAccuracy the altitudeAccuracy or null
         * @param heading the heading or null
         * @param speed the speed or null
         */
        public Geolocation(
                final double latitude,
                final double longitude,
                final double accuracy,
                final Double altitude,
                final Double altitudeAccuracy,
                final Double heading,
                final Double speed) {
            latitude_ = latitude;
            longitude_ = longitude;
            accuracy_ = accuracy;
            altitude_ = altitude;
            altitudeAccuracy_ = altitudeAccuracy;
            heading_ = heading;
            speed_ = speed;
        }

        /**
         * @return the accuracy
         */
        public double getAccuracy() {
            return accuracy_;
        }

        /**
         * @return the latitude
         */
        public double getLatitude() {
            return latitude_;
        }

        /**
         * @return the longitude
         */
        public double getLongitude() {
            return longitude_;
        }

        /**
         * @return the longitude
         */
        public Double getAltitude() {
            return altitude_;
        }

        /**
         * @return the altitudeAccuracy
         */
        public Double getAltitudeAccuracy() {
            return altitudeAccuracy_;
        }

        /**
         * @return the heading
         */
        public Double getHeading() {
            return heading_;
        }

        /**
         * @return the speed
         */
        public Double getSpeed() {
            return speed_;
        }
    }

    /**
     * If set to {@code true}, the client will accept XMLHttpRequests to URL's
     * using the 'file' protocol. Allowing this introduces security problems and is
     * therefore not allowed by current browsers. But some browsers have special settings
     * to open this door; therefore we have this option.
     * @param fileProtocolForXMLHttpRequestsAllowed whether or not allow (local) file access
     */
    public void setFileProtocolForXMLHttpRequestsAllowed(final boolean fileProtocolForXMLHttpRequestsAllowed) {
        fileProtocolForXMLHttpRequestsAllowed_ = fileProtocolForXMLHttpRequestsAllowed;
    }

    /**
     * Indicates if the client will accept XMLHttpRequests to URL's
     * using the 'file' protocol.
     * @return {@code true} if access to local files is allowed.
     */
    public boolean isFileProtocolForXMLHttpRequestsAllowed() {
        return fileProtocolForXMLHttpRequestsAllowed_;
    }
}
