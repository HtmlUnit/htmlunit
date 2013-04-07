/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Tests methods in {@link HttpWebConnection}.
 *
 * @version $Revision$
 * @author John J Murdoch
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnectionProxyTest extends WebServerTestCase {

    private Server proxyWebServer_;

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @Before
    public void setup() throws Exception {
        startWebServer("./src/test/resources/testfiles/noproxyroot/");

        proxyWebServer_ = createWebServer(PORT2, "./src/test/resources/testfiles/proxyroot/", null, null, null);
        proxyWebServer_.start();

        final WebClient webClient = getWebClient();

        final ProxyConfig proxy = new ProxyConfig(SOCKS_PROXY_HOST, PORT2);
        proxy.addHostsToProxyBypass("127.0.0.1");
        webClient.getOptions().setProxyConfig(proxy);
    }

    /**
     * Performs pre-test construction.
     * @throws Exception if an error occurs
     */
    @After
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
        URL resourceUrl = new URL("http://localhost:" + PORT2 + "/response.txt");
        TextPage page = getWebClient().getPage(UrlUtils.getUrlWithNewPort(resourceUrl, PORT2));
        Assert.assertEquals("proxy should have been used", "proxy-response", page.getContent().trim());

        resourceUrl = new URL("http://127.0.0.1:" + PORT + "/response.txt");
        page = getWebClient().getPage(resourceUrl);
        Assert.assertEquals("proxy should not be used", "no-proxy-response", page.getContent().trim());
    }
}
