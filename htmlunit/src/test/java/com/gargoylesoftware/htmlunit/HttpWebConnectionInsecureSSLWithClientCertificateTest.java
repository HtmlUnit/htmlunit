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

import java.net.URL;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for insecure SSL, with client certificate.
 *
 * @version $Revision$
 * @author Martin Huber
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnectionInsecureSSLWithClientCertificateTest extends WebTestCase {

    private LocalTestServerWithClientCerts localServer_;

    /**
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        final URL url = getClass().getClassLoader().getResource("insecureSSL.keystore");
        final KeyStore keystore = KeyStore.getInstance("jks");
        final char[] pwd = "nopassword".toCharArray();
        keystore.load(url.openStream(), pwd);

        final TrustManagerFactory trustManagerFactory = createTrustManagerFactory();
        trustManagerFactory.init(keystore);
        final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        final KeyManagerFactory keyManagerFactory = createKeyManagerFactory();
        keyManagerFactory.init(keystore, pwd);
        final KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

        final SSLContext serverSSLContext = SSLContext.getInstance("TLS");
        serverSSLContext.init(keyManagers, trustManagers, null);

        localServer_ = new LocalTestServerWithClientCerts(serverSSLContext);
        localServer_.registerDefaultHandlers();

        localServer_.start();
    }

    private KeyManagerFactory createKeyManagerFactory() throws NoSuchAlgorithmException {
        final String algorithm = KeyManagerFactory.getDefaultAlgorithm();
        try {
            return KeyManagerFactory.getInstance(algorithm);
        }
        catch (final NoSuchAlgorithmException e) {
            return KeyManagerFactory.getInstance("SunX");
        }
    }

    private TrustManagerFactory createTrustManagerFactory() throws NoSuchAlgorithmException {
        final String algorithm = TrustManagerFactory.getDefaultAlgorithm();
        try {
            return TrustManagerFactory.getInstance(algorithm);
        }
        catch (final NoSuchAlgorithmException e) {
            return TrustManagerFactory.getInstance("SunX");
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        if (localServer_ != null) {
            localServer_.stop();
        }
        localServer_ = null;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void insecureSSL_clientCertificates() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.setSSLClientCertificate(getClass().getClassLoader().getResource("insecureSSL.keystore"),
                "nopassword", "jks");
        webClient.setUseInsecureSSL(true);
        webClient.getPage("https://" + localServer_.getServiceAddress().getHostName()
                + ':' + localServer_.getServiceAddress().getPort()
                + "/random/100");
    }
}
