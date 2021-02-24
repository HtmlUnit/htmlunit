/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.net.URL;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for custom trust store.
 *
 * @author Carsten Steul
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnectionTruststoreTest extends SimpleWebTestCase {

    private static LocalTestServer LOCAL_SERVER_;

    /**
     * @throws Exception if an error occurs
     */
    @BeforeClass
    public static void setUp() throws Exception {
        final URL url = HttpWebConnectionTruststoreTest.class.getClassLoader().getResource("self-signed-cert.keystore");
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

        LOCAL_SERVER_ = new LocalTestServer(serverSSLContext);
        LOCAL_SERVER_.start();
    }

    private static KeyManagerFactory createKeyManagerFactory() throws NoSuchAlgorithmException {
        final String algorithm = KeyManagerFactory.getDefaultAlgorithm();
        try {
            return KeyManagerFactory.getInstance(algorithm);
        }
        catch (final NoSuchAlgorithmException e) {
            return KeyManagerFactory.getInstance("SunX509");
        }
    }

    private static TrustManagerFactory createTrustManagerFactory() throws NoSuchAlgorithmException {
        final String algorithm = TrustManagerFactory.getDefaultAlgorithm();
        try {
            return TrustManagerFactory.getInstance(algorithm);
        }
        catch (final NoSuchAlgorithmException e) {
            return TrustManagerFactory.getInstance("SunX509");
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @AfterClass
    public static void tearDown() throws Exception {
        if (LOCAL_SERVER_ != null) {
            LOCAL_SERVER_.shutDown();
        }
        LOCAL_SERVER_ = null;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selfSignedCertInTruststore() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getOptions().setSSLTrustStore(
                getClass().getClassLoader().getResource("self-signed-cert.keystore"),
                "nopassword", "jks");
        webClient.getPage("https://" + "localhost"
                + ':' + LOCAL_SERVER_.getServer().getLocalPort()
                + "/random/100");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test(expected = SSLHandshakeException.class)
    public void selfSignedCertNotInTruststore() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getPage("https://" + "localhost"
                + ':' + LOCAL_SERVER_.getServer().getLocalPort()
                + "/random/100");
    }
}
