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

import static org.eclipse.jetty.http.HttpVersion.HTTP_1_1;
import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;

import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for SOCKS proxy support.
 * This test {@link org.junit.Assume assumes} a SOCKS proxy to be available on port
 * {@link WebTestCase#SOCKS_PROXY_PORT} of {@link WebTestCase#SOCKS_PROXY_HOST}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class SocksProxyTest extends WebServerTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void http() throws Exception {
        assumeSocksProxyInUse();
        doHttpTest(getWebClientWithSocksProxy());
    }

    /**
     * Ensure that an error occurs if no SOCKS proxy runs on the configured port.
     * @throws Exception if an error occurs
     */
    @Test(expected = SocketException.class)
    public void httpWithBadProxyPortShouldFail() throws Exception {
        doHttpTest(getWebClientWithWrongSocksProxy());
    }

    private void doHttpTest(final WebClient client) throws Exception, IOException, MalformedURLException {
        final URL http = new URL("http://localhost:" + PORT + "/");
        final HtmlPage page = client.getPage(http);
        assertEquals("hello", page.getTitleText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void https() throws Exception {
        assumeSocksProxyInUse();
        doHttpsTest(getWebClientWithSocksProxy());
    }

    /**
     * If the SOCKS proxy port is wrong, an exception should occur otherwise
     * it shows that the proxy isn't used.
     * @throws Exception if an error occurs
     */
    @Test(expected = SocketException.class)
    public void httpsWithBadProxyPortShouldFail() throws Exception {
        doHttpsTest(getWebClientWithWrongSocksProxy());
    }

    private void doHttpsTest(final WebClient webClient) throws Exception {
        webClient.getOptions().setUseInsecureSSL(true);

        final URL https = new URL("https://localhost:" + PORT2 + "/");
        loadPage("<div>test</div>", https);
    }

    private static void assumeSocksProxyInUse() {
        try {
            try (Socket socket = new Socket(SOCKS_PROXY_HOST, SOCKS_PROXY_PORT)) {
                // nothing
            }
        }
        catch (final IOException e) {
            assumeTrue("Socks proxy is not available", false);
        }
    }

    private WebClient getWebClientWithWrongSocksProxy() {
        final WebClient client = getWebClient();
        client.getOptions().setProxyConfig(new ProxyConfig(SOCKS_PROXY_HOST, SOCKS_PROXY_PORT + 1, null, true));
        return client;
    }

    private WebClient getWebClientWithSocksProxy() {
        final WebClient client = getWebClient();
        client.getOptions().setTimeout(10_000);
        client.getOptions().setProxyConfig(new ProxyConfig(SOCKS_PROXY_HOST, SOCKS_PROXY_PORT, null, true));
        return client;
    }


    @Override
    protected boolean isHttps() {
        return true;
    }

    @Override
    public SslConnectionFactory getSslConnectionFactory() {
        final URL url = HttpWebConnectionInsecureSSLWithClientCertificateTest.class
                .getClassLoader().getResource("insecureSSL.pfx");

        final SslContextFactory contextFactory = new Server.Server();
        contextFactory.setKeyStorePath(url.toExternalForm());
        contextFactory.setKeyStorePassword("nopassword");
        return new SslConnectionFactory(contextFactory, HTTP_1_1.toString());
    }
}
