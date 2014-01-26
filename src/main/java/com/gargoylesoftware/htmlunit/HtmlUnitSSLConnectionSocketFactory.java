/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.protocol.HttpContext;

/**
 * Socket factory offering facilities for insecure SSL and for SOCKS proxy support.
 * This looks rather like a hack than like clean code but at the time of the writing it seems to
 * be the easiest way to provide SOCKS proxy support for HTTPS.
 *
 * @version $Revision$
 * @author Nicolas Belisle
 * @author Ahmed Ashour
 * @author Martin Huber
 * @author Marc Guillemot
 * @author Ronald Brill
 */
final class HtmlUnitSSLConnectionSocketFactory extends SSLConnectionSocketFactory {
    private static final String SSL3ONLY = "htmlunit.SSL3Only";

    static void setUseSSL3Only(final HttpContext parameters, final boolean ssl3Only) {
        parameters.setAttribute(SSL3ONLY, ssl3Only);
    }

    static boolean isUseSSL3Only(final HttpContext context) {
        return "TRUE".equalsIgnoreCase((String) context.getAttribute(SSL3ONLY));
    }

    public static SSLConnectionSocketFactory buildSSLSocketFactory(final WebClientOptions options) {
        try {
            if (!options.isUseInsecureSSL()) {
                if (options.getSSLClientCertificateUrl() == null) {
                    return new HtmlUnitSSLConnectionSocketFactory((KeyStore) null, null); // only SOCKS awareness
                }
                // SOCKS + keystore
                return new HtmlUnitSSLConnectionSocketFactory(getKeyStore(options),
                        options.getSSLClientCertificatePassword());
            }

            // we need insecure SSL + SOCKS awareness
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(getKeyManagers(options), new TrustManager[]{new InsecureTrustManager2()}, null);

            final SSLConnectionSocketFactory factory = new HtmlUnitSSLConnectionSocketFactory(sslContext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return factory;
        }
        catch (final GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private HtmlUnitSSLConnectionSocketFactory(final SSLContext sslContext,
            final X509HostnameVerifier hostnameVerifier) {
        super(sslContext, hostnameVerifier);
    }

    private HtmlUnitSSLConnectionSocketFactory(final KeyStore keystore, final String keystorePassword)
        throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(SSLContexts.custom()
                .loadKeyMaterial(keystore, keystorePassword != null ? keystorePassword.toCharArray() : null)
                .build(),
                BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }

    private void configureSocket(final SSLSocket sslSocket, final HttpContext context) {
        if (isUseSSL3Only(context)) {
            sslSocket.setEnabledProtocols(new String[]{"SSLv3"});
        }
    }

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

            // TODO: commented out for HttpClient 4.3
            // final int soTimeout = HttpConnectionParams.getSoTimeout(params);

            final SocketAddress socksProxyAddress = new InetSocketAddress(socksProxy.getHostName(),
                    socksProxy.getPort());
            try {
                //underlying.setSoTimeout(soTimeout);
                underlying.connect(remoteAddress, connectTimeout);
            }
            catch (final SocketTimeoutException ex) {
                throw new ConnectTimeoutException("Connect to " + socksProxyAddress + " timed out");
            }

            final Socket sslSocket = getSSLSocketFactory().createSocket(underlying, socksProxy.getHostName(),
                    socksProxy.getPort(), true);
            configureSocket((SSLSocket) sslSocket, context);
            return sslSocket;
        }
        return super.connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, context);
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
        if (options.getSSLClientCertificateUrl() == null) {
            return null;
        }
        try {
            final String password = options.getSSLClientCertificatePassword();
            final char[] passwordChars = password != null ? password.toCharArray() : null;

            final KeyStore keyStore = getKeyStore(options);
            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, passwordChars);
            return keyManagerFactory.getKeyManagers();
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyStore getKeyStore(final WebClientOptions options) {
        try {
            final KeyStore keyStore = KeyStore.getInstance(options.getSSLClientCertificateType());
            final String password = options.getSSLClientCertificatePassword();
            final char[] passwordChars = password != null ? password.toCharArray() : null;
            keyStore.load(options.getSSLClientCertificateUrl().openStream(), passwordChars);
            return keyStore;
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
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
class InsecureTrustManager2 implements X509TrustManager {
    private final Set<X509Certificate> acceptedIssuers_ = new HashSet<X509Certificate>();

    /**
     * {@inheritDoc}
     */
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        // Everyone is trusted!
        acceptedIssuers_.addAll(Arrays.asList(chain));
    }

    /**
     * {@inheritDoc}
     */
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        // Everyone is trusted!
        acceptedIssuers_.addAll(Arrays.asList(chain));
    }

    /**
     * {@inheritDoc}
     */
    public X509Certificate[] getAcceptedIssuers() {
        // it seems to be OK for Java <= 6 to return an empty array but not for Java 7 (at least 1.7.0_04-b20):
        // requesting an URL with a valid certificate (working without WebClient.setUseInsecureSSL(true)) throws a
        //  javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
        // when the array returned here is empty
        if (acceptedIssuers_.isEmpty()) {
            return new X509Certificate[0];
        }
        return acceptedIssuers_.toArray(new X509Certificate[acceptedIssuers_.size()]);
    }
}
