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
import java.net.URL;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.localserver.LocalTestServer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

/**
 * Simple insecure HTTPS server.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class InsecureHttpsServer {

    private LocalTestServer localServer_;
    private final String html_;

    /**
     * Create a server delivering nothing.
     */
    public InsecureHttpsServer() {
        html_ = null;
    }

    /**
     * Create a server delivering the provided HTML content.
     * @param html the HTML content to deliver
     */
    public InsecureHttpsServer(final String html) {
        html_ = html;
    }

    private KeyManagerFactory createKeyManagerFactory() throws NoSuchAlgorithmException {
        final String algorithm = KeyManagerFactory.getDefaultAlgorithm();
        try {
            return KeyManagerFactory.getInstance(algorithm);
        }
        catch (final NoSuchAlgorithmException e) {
            return KeyManagerFactory.getInstance("SunX509");
        }
    }

    private TrustManagerFactory createTrustManagerFactory() throws NoSuchAlgorithmException {
        final String algorithm = TrustManagerFactory.getDefaultAlgorithm();
        try {
            return TrustManagerFactory.getInstance(algorithm);
        }
        catch (final NoSuchAlgorithmException e) {
            return TrustManagerFactory.getInstance("SunX509");
        }
    }

    /**
     * Starts the server.
     * @throws Exception in case of exception
     */
    public void start() throws Exception {
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

        localServer_ = new LocalTestServer(serverSSLContext);
        localServer_.registerDefaultHandlers();

        if (html_ != null) {
            final HttpRequestHandler handler = new HttpRequestHandler() {
                @Override
                public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context)
                    throws HttpException, IOException {
                    response.setEntity(new StringEntity(html_, ContentType.TEXT_HTML));
                }
            };
            localServer_.register("*", handler);
        }

        localServer_.start();
    }

    /**
     * Stops the server.
     * @throws Exception in case of exception
     */
    public void stop() throws Exception {
        localServer_.stop();
    }

    /**
     * Gets the host name on which the server is listening.
     * @return the host name
     */
    public String getHostName() {
        return localServer_.getServiceAddress().getHostName();
    }

    /**
     * Gets the host port on which the server is listening.
     * @return the host port
     */
    public int getPort() {
        return localServer_.getServiceAddress().getPort();
    }
}
