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
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.ServletContentWrapper;

/**
 * Tests for SOCKS proxy support.
 * This test expects a SOCKS proxy to be available on port
 * {@link AbstractWebTestCase#SOCKS_PROXY_PORT} of {@link AbstractWebTestCase#SOCKS_PROXY_HOST}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class SocksProxyTest extends WebServerTestCase {

    private InsecureHttpsServer localServer_;

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void http() throws Exception {
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
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/test", SocksProxyTestServlet.class);
        startWebServer("./", new String[0], servlets);

        final HtmlPage page = client.getPage("http://localhost:" + PORT + "/test");
        assertEquals("hello", page.getTitleText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void https() throws Exception {
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
        localServer_ = new InsecureHttpsServer(SocksProxyTestServlet.HTML);
        localServer_.start();

        webClient.getOptions().setUseInsecureSSL(true);

        final String url = "https://" + localServer_.getHostName() + ":" + localServer_.getPort();
        final HtmlPage page = webClient.getPage(url);
        assertEquals("hello", page.getTitleText());
    }

    private WebClient getWebClientWithWrongSocksProxy() {
        final WebClient client = getWebClient();
        client.getOptions().setProxyConfig(new ProxyConfig(SOCKS_PROXY_HOST, SOCKS_PROXY_PORT + 1, true));
        return client;
    }

    private WebClient getWebClientWithSocksProxy() {
        final WebClient client = getWebClient();
        client.getOptions().setProxyConfig(new ProxyConfig(SOCKS_PROXY_HOST, SOCKS_PROXY_PORT, true));
        return client;
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

        super.tearDown();
    }

    /**
     * Helper class to deliver content.
     */
    public static class SocksProxyTestServlet extends ServletContentWrapper {
        private static final String HTML = "<html><head><title>hello</title></head></html>";

        /**
         * Constructor.
         */
        public SocksProxyTestServlet() {
            super(HTML);
        }
    }

}
