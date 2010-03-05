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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for elements inside {@link HtmlNoScript}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlNoScriptTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void testGetElementById() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('second'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "    <input type='text' id='first' name='textfield'/>\n"
            + "    <noscript>\n"
            + "    <input type='text' id='second' name='button'/>\n"
            + "    </noscript>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void testChildNodes() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var noscript = document.getElementById('myDiv' ).childNodes.item(0);\n"
            + "    alert(noscript.childNodes.length);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "    <div id='myDiv'><noscript>\n"
            + "        <input type='text' name='button'/>\n"
            + "      </noscript></div>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void testJavaScript() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  alert(1);\n"
            + "</script>\n"
            + "<noscript>\n"
            + "  <script>\n"
            + "    alert(2);\n"
            + "  </script>\n"
            + "</noscript>\n"
            + "</head><body>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testFormValues() throws Exception {
        final String html
            = "<html><body>\n"
            + "<form name='item' method='post'>\n"
            + "  <noscript>\n"
            + "    <input type=hidden name='__webpage_no_js__' value='1'>\n"
            + "  </noscript>\n"
            + "  <input type=hidden name='myParam' value='myValue'>\n"
            + "  <input type='submit'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage firstPage = loadPage(html);
        final HtmlForm form = firstPage.getForms().get(0);
        final HtmlPage secondPage = (HtmlPage) form.submit((SubmittableElement) null);

        final MockWebConnection mockWebConnection = getMockConnection(secondPage);
        assertEquals(1, mockWebConnection.getLastParameters().size());
        assertTrue(secondPage.asXml().indexOf("__webpage_no_js__") > -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml_jsEnabled() throws Exception {
        final String html
            = "<html><body>\n"
            + "<noscript><div>hello</noscript>"
            + "</body></html>";

        final String expected = "<body>\n"
            + "  <noscript>\n"
            + "    &lt;div&gt;hello\n"
            + "  </noscript>\n"
            + "</body>\n";
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
        client.setJavaScriptEnabled(false);

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
        client.setJavaScriptEnabled(false);

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(expected, page.getBody().asText().replaceAll("\\r", ""));
    }
}
