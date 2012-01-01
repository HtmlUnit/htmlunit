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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
 */
final class HttpWebConnectionInsecureSSL {

    private HttpWebConnectionInsecureSSL() { }

    /**
     * Sets Insecure SSL.
     * @param httpClient the client
     * @param useInsecureSSL whether to use insecure SSL or not
     * @param ssl3Only whether to allow only SSLv3
     * @throws GeneralSecurityException if an error occurs
     */
    static void setUseInsecureSSL(final AbstractHttpClient httpClient,
            final boolean useInsecureSSL, final boolean ssl3Only) throws GeneralSecurityException {
        if (useInsecureSSL) {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[] {new InsecureTrustManager()}, null);
            final SSLSocketFactory factory = new SSLSocketFactory(sslContext, new AllowAllHostnameVerifier()) {
                @Override
                public Socket createSocket(final HttpParams params) throws IOException {
                    final SSLSocket sslSocket = (SSLSocket) super.createSocket(params);
                    if (ssl3Only) {
                        sslSocket.setEnabledProtocols(new String[] {"SSLv3"});
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
            schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        }
    }
}

/**
 * A completely insecure (yet very easy to use) x509 trust manager. This manager trusts all servers
 * and all clients, regardless of credentials or lack thereof.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
class InsecureTrustManager implements X509TrustManager {

    /**
     * {@inheritDoc}
     */
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        // Everyone is trusted!
    }

    /**
     * {@inheritDoc}
     */
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        // Everyone is trusted!
    }

    /**
     * {@inheritDoc}
     */
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
