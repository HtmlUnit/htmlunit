/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;

import com.gargoylesoftware.htmlunit.ssl.InsecureTrustManager;

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

    static void setUseInsecureSSL(final AbstractHttpClient httpClient,
            final boolean useInsecureSSL) throws GeneralSecurityException {
        if (useInsecureSSL) {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[] {new InsecureTrustManager()}, null);
            final SocketFactory factory = new SSLSocketFactory(sslContext);
            final Scheme https = new Scheme("https", factory, 443);

            final SchemeRegistry schemeRegistry = httpClient.getConnectionManager().getSchemeRegistry();
            schemeRegistry.register(https);
        }
        else {
            final SchemeRegistry schemeRegistry = httpClient.getConnectionManager().getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        }
    }
}
