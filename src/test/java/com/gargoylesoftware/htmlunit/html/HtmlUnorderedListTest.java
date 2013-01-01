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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlUnorderedList}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlUnorderedListTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String html = "<html><head>\n"
            + "</head><body>\n"
            + "  <ul id='foo'>"
            + "  <li>first item</li>\n"
            + "  <li>second item</li>\n"
            + "something without li node\n"
            + "  <li>third item</li>\n"
            + "  </ul>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlElement node = page.getHtmlElementById("foo");
        final String expectedText = "first item" + LINE_SEPARATOR
            + "second item" + LINE_SEPARATOR
            + "something without li node" + LINE_SEPARATOR
            + "third item";

        assertEquals(expectedText, node.asText());
        assertEquals(expectedText, page.asText());
    }

    /**
     * Browsers ignore closing information in a self closing UL tag.
     * @throws Exception if the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<ul id='myNode'></ul>\n"
            + "foo\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(content);
        final HtmlElement element = page.getHtmlElementById("myNode");

        assertEquals("<ul id=\"myNode\">" + LINE_SEPARATOR + "</ul>" + LINE_SEPARATOR, element.asXml());
        assertTrue(page.asXml().contains("</ul>"));
    }
}
