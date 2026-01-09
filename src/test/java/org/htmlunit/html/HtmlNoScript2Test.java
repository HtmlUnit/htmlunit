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
package org.htmlunit.html;

import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for elements inside {@link HtmlNoScript}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class HtmlNoScript2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<body>\r\n"
                + "  <noscript>&lt;div&gt;hello</noscript>\r\n"
                + "</body>")
    public void asXml_jsEnabled() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<noscript><div>hello</noscript>"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals(getExpectedAlerts()[0], page.getBody().asXml());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml_jsDisabled() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<noscript><div>hello</noscript>"
            + "</body></html>";

        final String expected = "<body>\r\n"
            + "  <noscript>\r\n"
            + "    <div>hello</div>\r\n"
            + "  </noscript>\r\n"
            + "</body>";

        final WebClient client = getWebClient();
        client.getOptions().setJavaScriptEnabled(false);

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(expected, page.getBody().asXml());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asNormalizedTextjsEnabled() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html><body>\n"
            + "<noscript>hello</noscript>"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        assertEquals("", page.getBody().asNormalizedText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asNormalizedText_jsDisabled() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<noscript>hello</noscript>"
            + "</body></html>";

        final String expected = "hello";

        final WebClient client = getWebClient();
        client.getOptions().setJavaScriptEnabled(false);

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(expected, page.getBody().asNormalizedText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isEmptyXmlTagExpanded() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<noscript></noscript>"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertTrue(page.asXml().contains("</noscript>"));
    }
}
