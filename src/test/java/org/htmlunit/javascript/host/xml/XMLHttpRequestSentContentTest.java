/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.xml;

import java.net.URL;

import org.htmlunit.MiniServer;
import org.htmlunit.MockWebConnection;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the content sent to the server.
 *
 * @author Ronald Brill
 *
 */
public final class XMLHttpRequestSentContentTest extends WebDriverTestCase {

    /**
     * Shuts down all browsers and resets the {@link MiniServer}.
     * @throws Exception in case of error
     */
    @Before
    public void before() throws Exception {
        // Chrome seems to cache preflight results
        shutDownAll();
        MiniServer.resetDropRequests();
    }

    /**
     * Resets the {@link MiniServer}.
     * @throws Exception in case of error
     */
    @After
    public void after() throws Exception {
        MiniServer.resetDropRequests();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getShouldNotSendBody() throws Exception {
        testSendBody("GET", false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void postShouldSendBody() throws Exception {
        testSendBody("POST", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void putShouldSendBody() throws Exception {
        testSendBody("PUT", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void patchShouldSendBody() throws Exception {
        testSendBody("PATCH", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void deleteShouldSendBody() throws Exception {
        testSendBody("DELETE", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void optionsShouldSendBody() throws Exception {
        testSendBody("OPTIONS", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headShouldNotSendBody() throws Exception {
        testSendBody("HEAD", false);
    }

    private void testSendBody(final String method, final boolean bodyIncluded) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + "  function test() {\n"
                + "    var xhr = new XMLHttpRequest();\n"
                + "    xhr.open('" + method + "', 'second.html?a=x', false);\n"
                + "    let body = new URLSearchParams();\n"
                + "    body.append('x', 'body');\n"
                + "    xhr.send(body);\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'></body></html>";

        final MockWebConnection mockWebConnection = getMockWebConnection();
        mockWebConnection.setResponse(WebTestCase.URL_FIRST, html);
        mockWebConnection.setDefaultResponse("");

        try (MiniServer miniServer = new MiniServer(PORT, mockWebConnection)) {
            miniServer.start();

            final WebDriver driver = getWebDriver();
            driver.get(WebTestCase.URL_FIRST.toExternalForm());

            assertEquals(2, mockWebConnection.getRequestCount());
            assertEquals(new URL(WebTestCase.URL_FIRST, "second.html?a=x"),
                                mockWebConnection.getLastWebRequest().getUrl());

            if (bodyIncluded) {
                assertTrue(miniServer.getLastRequest(), miniServer.getLastRequest().contains("\nx=body"));
            }
            else {
                assertTrue(miniServer.getLastRequest(), !miniServer.getLastRequest().contains("\nx=body"));
            }
        }
    }
}
