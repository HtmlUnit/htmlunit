/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static org.eclipse.jetty.http.HttpVersion.HTTP_1_1;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.SSLHandshakeException;

import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for insecure SSL, with client certificate.
 *
 * @author Martin Huber
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HttpWebConnectionInsecureSSLWithClientCertificateTest extends WebServerTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void insecureSSL_clientCertificates_fail() throws Exception {
        final URL https = new URL("https://localhost:" + PORT2 + "/");

        Assertions.assertThrows(SSLHandshakeException.class,
                    () -> loadPage("<div>test</div>", https));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void insecureSSL_clientCertificates() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getOptions().setSSLClientCertificateKeyStore(
                getClass().getClassLoader().getResource("insecureSSL.pfx"),
                "nopassword", "PKCS12");
        webClient.getOptions().setUseInsecureSSL(true);

        final URL https = new URL("https://localhost:" + PORT2 + "/");
        loadPage("<div>test</div>", https);
    }

    /**
     * Test if a certificate/keystore can be load from an input stream.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void insecureSSL_clientCertificatesInputStream_fail() throws Exception {
        final URL https = new URL("https://localhost:" + PORT2 + "/");

        Assertions.assertThrows(SSLHandshakeException.class,
                () -> loadPage("<div>test</div>", https));
    }

    /**
     * Test if a certificate/keystore can be load from an input stream.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void insecureSSL_clientCertificatesInputStream() throws Exception {
        final WebClient webClient = getWebClient();
        try (InputStream certificateInputStream = getClass().getClassLoader()
                .getResourceAsStream("insecureSSL.pfx")) {
            final byte[] certificateBytes = new byte[4096];
            certificateInputStream.read(certificateBytes);

            try (InputStream is = new ByteArrayInputStream(certificateBytes)) {
                webClient.getOptions().setSSLClientCertificateKeyStore(is, "nopassword", "PKCS12");
                webClient.getOptions().setUseInsecureSSL(true);

                final URL https = new URL("https://localhost:" + PORT2 + "/");
                loadPage("<div>test</div>", https);
            }
        }
    }

    @Override
    public SslConnectionFactory getSslConnectionFactory() {
        final URL url = HttpWebConnectionInsecureSSLWithClientCertificateTest.class
                .getClassLoader().getResource("insecureSSL.pfx");

        final Server contextFactory = new Server();
        contextFactory.setKeyStorePath(url.toExternalForm());
        contextFactory.setKeyStorePassword("nopassword");
        contextFactory.setEndpointIdentificationAlgorithm(null);
        return new SslConnectionFactory(contextFactory, HTTP_1_1.toString());
    }
}
