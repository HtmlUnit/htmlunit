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
package org.htmlunit.html;

import org.htmlunit.MockWebConnection;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlTitle}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HtmlTitleTest extends SimpleWebTestCase {

    /**
     * It is questionable to have the title in HtmlPage.asText() but if we have it, then
     * it should be followed by a new line.
     * @throws Exception if the test fails
     */
    @Test
    public void pageAsNormalizedText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<title>Dummy</title>\n"
            + "</head>\n"
            + "\n"
            + "<body>\n"
            + "Dummy page\n"
            + "</body>\n"
            + "</html>\n";

        final HtmlPage page = loadPage(html);
        final String expected = "Dummy\nDummy page";
        assertEquals(expected, page.asNormalizedText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getTitleText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<title>Title\nText     Test</title>\n"
            + "</head>\n"
            + "\n"
            + "<body>\n"
            + "Dummy page\n"
            + "</body>\n"
            + "</html>\n";

        final HtmlPage page = loadPage(html);
        assertEquals("Title Text Test", page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asTextEmptyTitle() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<title></title>\n"
            + "</head>\n"
            + "\n"
            + "<body>\n"
            + "Dummy page\n"
            + "</body>\n"
            + "</html>\n";

        final HtmlPage page = loadPage(html);
        assertEquals("", page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asTextContainingTags() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<title>My<br>Title<p>Text</p></title>\n"
            + "</head>\n"
            + "\n"
            + "<body>\n"
            + "Dummy page\n"
            + "</body>\n"
            + "</html>\n";

        final HtmlPage page = loadPage(html);
        assertEquals("My<br>Title<p>Text</p>", page.getTitleText());
    }

    /**
     * Test for Bug #1199.
     * @throws Exception if the test fails
     */
    @Test
    public void getTitleTextDontLoadCss() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<title>Title</title>\n"
            + "<link href='styles.css' type='text/css' rel='stylesheet'>\n"
            + "</head>\n"
            + "\n"
            + "<body>\n"
            + "Dummy page\n"
            + "</body>\n"
            + "</html>\n";

        final HtmlPage page = loadPage(html);
        final MockWebConnection conn = (MockWebConnection) page.getWebClient().getWebConnection();
        assertEquals(2, conn.getRequestCount());

        assertEquals("Title", page.getTitleText());
        assertEquals(2, conn.getRequestCount());
    }

    /**
     * As of WebDriver 2.25.0, this can't be run with FirefoxDriver as following exception occurs:
     * org.openqa.selenium.WebDriverException: waiting for doc.body failed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void titleAfterDeleteDocumentElement() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    document.documentElement.parentNode.removeChild(document.documentElement);\n"
            + "    alert(document.title);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        page.asXml(); // ensure that no NPE occurs here now
    }
}
