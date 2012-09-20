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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests for elements inside {@link HtmlNoScript}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlNoScript2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml_jsEnabled() throws Exception {
        final String html
            = "<html><body>\n"
            + "<noscript><div>hello</noscript>"
            + "</body></html>";

        final String expectedFF = "<body>\n"
            + "  <noscript>\n"
            + "    &lt;div&gt;hello\n"
            + "  </noscript>\n"
            + "</body>\n";
        final String expectedIE = "<body>\n"
            + "  <noscript/>\n"
            + "</body>\n";

        final String expected = getBrowserVersion().isFirefox() ? expectedFF : expectedIE;
        final HtmlPage page = loadPage(html);
        assertEquals(expected, page.getBody().asXml().replaceAll("\\r", ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml_jsDisabled() throws Exception {
        final String html = "<html><body>\n"
            + "<noscript><div>hello</noscript>"
            + "</body></html>";

        final String expected = "<body>\n"
            + "  <noscript>\n"
            + "    <div>\n"
            + "      hello\n"
            + "    </div>\n"
            + "  </noscript>\n"
            + "</body>\n";

        final WebClient client = getWebClient();
        client.getOptions().setJavaScriptEnabled(false);

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(expected, page.getBody().asXml().replaceAll("\\r", ""));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText_jsEnabled() throws Exception {
        final String htmlContent
            = "<html><body>\n"
            + "<noscript>hello</noscript>"
            + "</body></html>";

        final String expected = "";
        final HtmlPage page = loadPage(htmlContent);
        assertEquals(expected, page.getBody().asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText_jsDisabled() throws Exception {
        final String html = "<html><body>\n"
            + "<noscript>hello</noscript>"
            + "</body></html>";

        final String expected = "hello";

        final WebClient client = getWebClient();
        client.getOptions().setJavaScriptEnabled(false);

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(expected, page.getBody().asText().replaceAll("\\r", ""));
    }
}
