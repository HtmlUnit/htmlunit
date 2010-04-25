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
package com.gargoylesoftware.htmlunit.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link FalsifyingWebConnection}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class FalsifyingWebConnectionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void blockSomeRequests() throws Exception {
        final WebClient webClient = getWebClient();

        final String html = "<html><head>\n"
            + "<script src='http://www.google-analytics.com/ga.js'></script>\n"
            + "<script src='myJs.js'></script>\n"
            + "</head><body>\n"
            + "hello world!"
            + "<body></html>";

        final MockWebConnection mockConnection = new MockWebConnection();
        mockConnection.setResponse(URL_FIRST, html);
        mockConnection.setResponse(new URL(URL_FIRST.toExternalForm() + "myJs.js"), "alert('hello');");
        webClient.setWebConnection(mockConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        // create a WebConnection that filters google-analytics scripts
        // c'tor configures connection on the web client
        new FalsifyingWebConnection(webClient) {
            @Override
            public WebResponse getResponse(final WebRequest settings) throws IOException {
                if ("www.google-analytics.com".equals(settings.getUrl().getHost())) {
                    return createWebResponse(settings, "", "application/javascript"); // -> empty script
                }
                return super.getResponse(settings);
            }
        };

        webClient.getPage(URL_FIRST);

        assertEquals(2, mockConnection.getRequestCount());
        final String[] expectedAlerts = {"hello"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void simulateHttpError() throws Exception {
        final WebClient webClient = getWebClient();

        final String html = "<html><head>\n"
            + "<script src='myJs.js'></script>\n"
            + "</head><body>\n"
            + "hello world!"
            + "<body></html>";

        final MockWebConnection mockConnection = new MockWebConnection();
        mockConnection.setResponse(URL_FIRST, html);
        mockConnection.setResponse(new URL(URL_FIRST.toExternalForm() + "myJs.js"), "alert('hello');");
        webClient.setWebConnection(mockConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        // first test this "site" when everything is ok
        webClient.getPage(URL_FIRST);
        final String[] expectedAlerts = {"hello"};
        assertEquals(expectedAlerts, collectedAlerts);

        // now simulate some server problems

        // create a WebConnection that filters google-analytics scripts
        // c'tor configures connection on the web client
        new FalsifyingWebConnection(webClient) {
            @Override
            public WebResponse getResponse(final WebRequest settings) throws IOException {
                if (settings.getUrl().getPath().endsWith(".js")) {
                    return createWebResponse(settings, "", "text/html", 500, "Application Error");
                }
                return super.getResponse(settings);
            }
        };

        try {
            webClient.getPage(URL_FIRST);
            Assert.fail("HTTP Exception expected!");
        }
        catch (final FailingHttpStatusCodeException e) {
            // that's fine
        }
    }
}
