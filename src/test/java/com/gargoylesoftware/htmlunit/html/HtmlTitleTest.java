/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlTitle}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlTitleTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLTitleElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head><title id='myId'>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(HtmlTitle.class.isInstance(page.getHtmlElementById("myId")));
    }

    /**
     * It is questionable to have the title in HtmlPage.asText() but if we have it, then
     * it should be followed by a new line.
     * @throws Exception if the test fails
     */
    @Test
    public void pageAsText() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<title>Dummy</title>\n"
            + "</head>\n"
            + "\n"
            + "<body>\n"
            + "Dummy page\n"
            + "</body>\n"
            + "</html>\n";

        final HtmlPage page = loadPage(html);
        final String expected = "Dummy" + LINE_SEPARATOR
            + "Dummy page";
        assertEquals(expected, page.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String html = "<html>\n"
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
        final String html = "<html>\n"
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
        final String html = "<html>\n"
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
     * Test for bug 3103101.
     * @throws Exception if the test fails
     */
    @Test
    public void asTextDontLoadCss() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<title>Title</title>\n"
            + "<link href='styles.css' type='text/css' rel='stylesheet'>"
            + "</head>\n"
            + "\n"
            + "<body>\n"
            + "Dummy page\n"
            + "</body>\n"
            + "</html>\n";

        final HtmlPage page = loadPage(html);
        assertEquals("Title", page.getTitleText());
        final MockWebConnection conn = (MockWebConnection) page.getWebClient().getWebConnection();
        assertEquals(1, conn.getRequestCount());
    }
}
