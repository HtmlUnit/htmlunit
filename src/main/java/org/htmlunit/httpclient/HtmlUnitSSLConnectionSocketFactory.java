/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.httpclient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedTrustManager;

import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.htmlunit.WebClientOptions;

/**
 * Socket factory offering facilities for insecure SSL and for SOCKS proxy support.
 * This looks rather like a hack than like clean code but at the time of the writing it seems to
 * be the easiest way to provide SOCKS proxy support for HTTPS.
 *
 * @author Nicolas Belisle
 * @author Ahmed Ashour
 * @author Martin Huber
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public final class HtmlUnitSSLConnectionSocketFactory extends SSLConnectionSocketFactory {
    private static final String SSL3ONLY = "htmlunit.SSL3Only";

    private final boolean useInsecureSSL_;

    /**
     * Enables/Disables the exclusive usage of SSL3.
     * @param httpContext the http context
     * @param ssl3Only true or false
     */
    public static void setUseSSL3Only(final HttpContext httpContext, final boolean ssl3Only) {
        httpContext.setAttribute(SSL3ONLY, ssl3Only);
    }

    static boolean isUseSSL3Only(final HttpContext context) {
        return "TRUE".equalsIgnoreCase((String) context.getAttribute(SSL3ONLY));
    }

    /**
     * Factory method that builds a new SSLConnectionSocketFactory.
     * @param options the current WebClientOptions
     * @return the SSLConnectionSocketFactory
     */
    public static SSLConnectionSocketFactory buildSSLSocketFactory(final WebClientOptions options) {
        try {
            final String[] sslClientProtocols = options.getSSLClientProtocols();
            final String[] sslClientCipherSuites = options.getSSLClientCipherSuites();

            final boolean useInsecureSSL = options.isUseInsecureSSL();

            if (!useInsecureSSL) {
                final KeyStore keyStore = options.getSSLClientCertificateStore();
                final KeyStore trustStore = options.getSSLTrustStore();

                return new HtmlUnitSSLConnectionSocketFactory(keyStore,
                        keyStore == null ? null : options.getSSLClientCertificatePassword(),
                        trustStore, useInsecureSSL,
                        sslClientProtocols, sslClientCipherSuites);
            }

            // we need insecure SSL + SOCKS awareness
            String protocol = options.getSSLInsecureProtocol();
            if (protocol == null) {
                protocol = "SSL";
            }
            final SSLContext sslContext = SSLContext.getInstance(protocol);
            sslContext.init(getKeyManagers(options), new X509ExtendedTrustManager[] {new InsecureTrustManager()}, null);

            return new HtmlUnitSSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE,
                                            useInsecureSSL, sslClientProtocols, sslClientCipherSuites);
        }
        catch (final GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private HtmlUnitSSLConnectionSocketFactory(final SSLContext sslContext,
            final HostnameVerifier hostnameVerifier, final boolean useInsecureSSL,
            final String[] supportedProtocols, final String[] supportedCipherSuites) {
        super(sslContext, supportedProtocols, supportedCipherSuites, hostnameVerifier);
        useInsecureSSL_ = useInsecureSSL;
    }

    private HtmlUnitSSLConnectionSocketFactory(final KeyStore keystore, final char[] keystorePassword,
            final KeyStore truststore, final boolean useInsecureSSL,
            final String[] supportedProtocols, final String[] supportedCipherSuites)
        throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(SSLContexts.custom()
                .loadKeyMaterial(keystore, keystorePassword).loadTrustMaterial(truststore, null).build(),
                supportedProtocols, supportedCipherSuites,
                new DefaultHostnameVerifier());
        useInsecureSSL_ = useInsecureSSL;
    }

    private static void configureSocket(final SSLSocket sslSocket, final HttpContext context) {
        if (isUseSSL3Only(context)) {
            sslSocket.setEnabledProtocols(new String[]{"SSLv3"});
        }
    }

    /**
     * Connect via socket.
     * @param connectTimeout the timeout
     * @param socket the socket
     * @param host the host
     * @param remoteAddress the remote address
     * @param localAddress the local address
     * @param context the context
     * @return the created/connected socket
     * @throws IOException in case of problems
     */
    @Override
    public Socket connectSocket(
            final int connectTimeout,
            final Socket socket,
            final HttpHost host,
            final InetSocketAddress remoteAddress,
            final InetSocketAddress localAddress,
            final HttpContext context) throws IOException {
        final HttpHost socksProxy = SocksConnectionSocketFactory.getSocksProxy(context);
        if (socksProxy != null) {
            final Socket underlying = SocksConnectionSocketFactory.createSocketWithSocksProxy(socksProxy);
            underlying.setReuseAddress(true);

            final SocketAddress socksProxyAddress = new InetSocketAddress(socksProxy.getHostName(),
                    socksProxy.getPort());
            try {
                //underlying.setSoTimeout(soTimeout);
                underlying.connect(remoteAddress, connectTimeout);
            }
            catch (final SocketTimeoutException ex) {
                throw new ConnectTimeoutException("Connect to " + socksProxyAddress + " timed out");
            }

            final Socket sslSocket = getSSLSocketFactory().createSocket(underlying, remoteAddress.getHostName(),
                    remoteAddress.getPort(), true);
            configureSocket((SSLSocket) sslSocket, context);
            return sslSocket;
        }
        try {
            return super.connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, context);
        }
        catch (final IOException e) {
            if (useInsecureSSL_ && "handshake alert:  unrecognized_name".equals(e.getMessage())) {
                setEmptyHostname(host);

                return super.connectSocket(connectTimeout,
                        createSocket(context),
                        host, remoteAddress, localAddress, context);
            }
            throw e;
        }
    }

    private static void setEmptyHostname(final HttpHost host) {
        try {
            final Field field = HttpHost.class.getDeclaredField("hostname");
            field.setAccessible(true);
            field.set(host, "");
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private javax.net.ssl.SSLSocketFactory getSSLSocketFactory() {
        try {
            final Field field = SSLConnectionSocketFactory.class.getDeclaredField("socketfactory");
            field.setAccessible(true);
            return (javax.net.ssl.SSLSocketFactory) field.get(this);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyManager[] getKeyManagers(final WebClientOptions options) {
        if (options.getSSLClientCertificateStore() == null) {
            return null;
        }
        try {
            final KeyStore keyStore = options.getSSLClientCertificateStore();
            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                    KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, options.getSSLClientCertificatePassword());
            return keyManagerFactory.getKeyManagers();
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * A completely insecure (yet very easy to use) x509 trust manager. This manager trusts all servers
 * and all clients, regardless of credentials or lack thereof.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ronald Brill
 */
class InsecureTrustManager extends X509ExtendedTrustManager {
    private final Set<X509Certificate> acceptedIssuers_ = new HashSet<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        // Everyone is trusted!
        acceptedIssuers_.addAll(Arrays.asList(chain));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        // Everyone is trusted!
        acceptedIssuers_.addAll(Arrays.asList(chain));
    }

    @Override
    public void checkClientTrusted(final X509Certificate[] chain,
                    final String authType, final Socket socket) throws CertificateException {
        // Everyone is trusted!
        acceptedIssuers_.addAll(Arrays.asList(chain));
    }

    @Override
    public void checkClientTrusted(final X509Certificate[] chain,
                    final String authType, final SSLEngine sslEngine) throws CertificateException {
        // Everyone is trusted!
        acceptedIssuers_.addAll(Arrays.asList(chain));
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain,
                    final String authType, final Socket socket) throws CertificateException {
        // Everyone is trusted!
        acceptedIssuers_.addAll(Arrays.asList(chain));
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain,
                    final String authType, final SSLEngine sslEngine) throws CertificateException {
        // Everyone is trusted!
        acceptedIssuers_.addAll(Arrays.asList(chain));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // it seems to be OK for Java <= 6 to return an empty array but not for Java 7 (at least 1.7.0_04-b20):
        // requesting a URL with a valid certificate (working without WebClient.setUseInsecureSSL(true)) throws a
        //  javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
        // when the array returned here is empty
        if (acceptedIssuers_.isEmpty()) {
            return new X509Certificate[0];
        }
        return acceptedIssuers_.toArray(new X509Certificate[0]);
    }
}
