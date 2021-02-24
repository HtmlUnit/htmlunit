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
package com.gargoylesoftware.htmlunit.html.parser;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.html.HtmlTableColumnGroup;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;

/**
 * Test class for {@link HTMLParser}.
 *
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLParserTest extends SimpleWebTestCase {

    /**
     * Tests the new HTMLParser on a simple HTML string.
     * @throws Exception failure
     */
    @Test
    public void simpleHTMLString() throws Exception {
        final WebClient webClient = getWebClient();
        final WebResponse webResponse = new StringWebResponse(
            "<html><head><title>TITLE</title></head><body><div>TEST</div></body></html>", URL_FIRST);

        final HtmlPage page = new HtmlPage(webResponse, webClient.getCurrentWindow());
        webClient.getCurrentWindow().setEnclosedPage(page);

        webClient.getPageCreator().getHtmlParser().parse(webResponse, page, false);

        final String stringVal = page.<HtmlDivision>getFirstByXPath("//div").getFirstChild().getNodeValue();
        assertEquals("TEST", stringVal);

        final HtmlElement node = (HtmlElement) page.getFirstByXPath("//*[./text() = 'TEST']");
        assertEquals(node.getTagName(), HtmlDivision.TAG_NAME);
    }

    /**
     * Works since NekoHtml 0.9.5.
     * @exception Exception If the test fails
     */
    @Test
    public void badTagInHead() throws Exception {
        final String htmlContent = "<html>\n" + "<head><foo/>\n<title>foo\n</head>\n"
                + "<body>\nfoo\n</body>\n</html>";

        final HtmlPage page = loadPageWithAlerts(htmlContent);
        assertEquals("foo", page.getTitleText());
    }

    /**
     * Regression test for bug #766: parse failure when parsing page with UTF-8 BOM (byte order mark).
     * The HTML file used is from NekoHTML's bug number #54.
     * @throws Exception if an error occurs
     */
    @Test
    public void bomUtf8() throws Exception {
        final String resource = "bom-utf8.html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);

        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(url);
        assertEquals("Welcome to Suffolk Coastal District Council online", page.getTitleText());
    }

    /**
     * This HTML was causing an EmptyStackException to be thrown.
     * @throws Exception if an error occurs
     */
    @Test
    public void emptyStack() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='document.getElementById(\"s\").innerHTML = "
            + "    \"<h1><span><span></span></span><span><span></span></span></h1>\";'>\n"
            + "    <div>\n"
            + "      <div>\n"
            + "        <table>\n"
            + "          <tbody>\n"
            + "            <tr>\n"
            + "              <td>\n"
            + "                <table>\n"
            + "                  <tbody>\n"
            + "                    <tr>\n"
            + "                      <td>\n"
            + "                        <div>\n"
            + "                          <div>\n"
            + "                            <h1>\n"
            + "                              <span id='s'>blah</span>\n"
            + "                            </h1>\n"
            + "                          </div>\n"
            + "                        </div>\n"
            + "                      </td>\n"
            + "                    </tr>\n"
            + "                  </tbody>\n"
            + "                </table>\n"
            + "              </td>\n"
            + "            </tr>\n"
            + "          </tbody>\n"
            + "        </table>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </body>\n"
            + "</html>";
        final HtmlPage page = loadPageWithAlerts(html);
        assertNotNull(page);
    }

    /**
     * @throws Exception failure
     */
    @Test
    public void tableWithoutColgroup() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <table><col width='7'/><col width='1'/><tbody><tr><td>seven</td><td>One</td></tr></tbody></table>\n"
            + "</body></html>";

        final WebClient webClient = getWebClient();
        final WebResponse webResponse = new StringWebResponse(html, URL_FIRST);

        final XHtmlPage page = new XHtmlPage(webResponse, webClient.getCurrentWindow());
        webClient.getCurrentWindow().setEnclosedPage(page);

        webClient.getPageCreator().getHtmlParser().parse(webResponse, page, true);

        final DomElement col = page.getElementsByTagName("col").get(0);
        assertEquals(col.getParentNode().getNodeName(), HtmlTableColumnGroup.TAG_NAME);
    }
}
