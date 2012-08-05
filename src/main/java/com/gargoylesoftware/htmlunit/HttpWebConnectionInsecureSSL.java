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

import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.params.HttpParams;

/**
 * Ideally should be part of {@link HttpWebConnection},
 * but Google App Engine does not support {@link SSLContext}.
 *
 * @version $Revision$
 * @author Nicolas Belisle
 * @author Ahmed Ashour
 * @author Martin Huber
 */
final class HttpWebConnectionInsecureSSL {

    private static final Log LOG = LogFactory.getLog(HttpWebConnectionInsecureSSL.class);

    private HttpWebConnectionInsecureSSL() {
    }

    /**
     * Sets Insecure SSL.
     *
     * @param webClient      the webClient
     * @param httpClient     the client
     * @param useInsecureSSL whether to use insecure SSL or not
     * @param ssl3Only       whether to allow only SSLv3
     * @throws GeneralSecurityException if an error occurs
     */
    static void setUseInsecureSSL(final WebClient webClient, final AbstractHttpClient httpClient,
                          final boolean useInsecureSSL, final boolean ssl3Only) throws GeneralSecurityException {
        if (useInsecureSSL) {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(getKeyManagers(webClient), new TrustManager[]{new InsecureTrustManager()}, null);
            final SSLSocketFactory factory = new SSLSocketFactory(sslContext, new AllowAllHostnameVerifier()) {
                @Override
                public Socket createSocket(final HttpParams params) throws IOException {
                    final SSLSocket sslSocket = (SSLSocket) super.createSocket(params);
                    if (ssl3Only) {
                        sslSocket.setEnabledProtocols(new String[]{"SSLv3"});
                    }
                    return sslSocket;
                }
            };
            final Scheme https = new Scheme("https", 443, factory);

            final SchemeRegistry schemeRegistry = httpClient.getConnectionManager().getSchemeRegistry();
            schemeRegistry.register(https);
        }
        else {
            final SchemeRegistry schemeRegistry = httpClient.getConnectionManager().getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", 443, HttpWebConnection.getSSLSocketFactory(webClient)));
        }
    }

    private static KeyManager[] getKeyManagers(final WebClient webClient) {
        final WebClientOptions options = webClient.getOptions();
        if (options.getSSLClientCertificateUrl() == null) {
            return null;
        }
        try {
            final KeyStore keyStore = KeyStore.getInstance(options.getSSLClientCertificateType());
            final String password = options.getSSLClientCertificatePassword();
            final char[] passwordChars = password != null ? password.toCharArray() : null;
            keyStore.load(options.getSSLClientCertificateUrl().openStream(), passwordChars);

            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, passwordChars);
            return keyManagerFactory.getKeyManagers();
        }
        catch (final Exception e) {
            LOG.error(e);
            return null;
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
class InsecureTrustManager implements X509TrustManager {
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
