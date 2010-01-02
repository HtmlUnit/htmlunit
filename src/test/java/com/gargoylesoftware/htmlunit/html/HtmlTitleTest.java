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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlTitle}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HtmlTitleTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSimpleScriptable() throws Exception {
        final String html = "<html><head><title id='myId'>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object HTMLTitleElement]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertTrue(HtmlTitle.class.isInstance(page.getHtmlElementById("myId")));
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * It is questionable to have the title in HtmlPage.asText() but if we have it, then
     * it should be followed by a new line.
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
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
}
