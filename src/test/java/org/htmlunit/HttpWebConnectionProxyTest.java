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

import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.htmlunit.util.UrlUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests methods in {@link HttpWebConnection}.
 *
 * @author John J Murdoch
 * @author Ronald Brill
 */
public class HttpWebConnectionProxyTest extends WebServerTestCase {

    private Server proxyWebServer_;

    /**
     * Performs pre-test construction.
     * @throws Exception if an error occurs
     */
    @BeforeEach
    public void setup() throws Exception {
        // we have to stop all servers running already to free the port
        WebDriverTestCase.stopWebServers();
        startWebServer("src/test/resources/testfiles/noproxyroot/");

        proxyWebServer_ = createWebServer(PORT_PROXY_SERVER, "src/test/resources/testfiles/proxyroot/", null, null);

        final WebClient webClient = getWebClient();

        final ProxyConfig proxy = new ProxyConfig(SOCKS_PROXY_HOST, PORT_PROXY_SERVER, null);
        proxy.addHostsToProxyBypass("127.0.0.1");
        webClient.getOptions().setProxyConfig(proxy);
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @Override
    @AfterEach
    public void tearDown() throws Exception {
        if (proxyWebServer_ != null) {
            proxyWebServer_.stop();
            proxyWebServer_.destroy();
        }
        proxyWebServer_ = null;

        super.tearDown();
    }

    /**
     * See http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d5e299
     *
     * Non client params should not be placed on <code>HttpClient</code>. They
     * should be carried by the request itself.
     * @throws Exception if the test fails
     */
    @Test
    public void proxySettingsAreNotUsedForSubsequentRequestToNonProxyHosts() throws Exception {
        URL resourceUrl = new URL("http://localhost:" + PORT_PROXY_SERVER + "/response.txt");
        TextPage page = getWebClient().getPage(UrlUtils.getUrlWithNewPort(resourceUrl, PORT_PROXY_SERVER));
        assertEquals("proxy should have been used", "proxy-response", page.getContent().trim());

        resourceUrl = new URL("http://127.0.0.1:" + PORT + "/response.txt");
        page = getWebClient().getPage(resourceUrl);
        assertEquals("proxy should not be used", "no-proxy-response", page.getContent().trim());
    }
}
