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

import org.htmlunit.SimpleWebTestCase;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlDivision}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class HtmlDivisionTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asNormalizedText() throws Exception {
        String expected = "hello\nworld";
        testAsNormalizedText(expected, "<div>hello</div>world");
        testAsNormalizedText(expected, "<div>hello<br/></div>world");

        expected = "hello\n\nworld";
        testAsNormalizedText(expected, "<div>hello<br/><br/></div>world");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asTextContiguousBlocks() throws Exception {
        final String expected = "hello\nworld";
        testAsNormalizedText(expected, "<div><table><tr><td>hello</td></tr><tr><td>world<br/></td></tr></table></div>");
        testAsNormalizedText(expected, "<div>hello</div><div>world</div>");
        testAsNormalizedText(expected, "<div>hello</div><div><div>world</div></div>");
        testAsNormalizedText(expected, "<div><table><tr><td>hello</td></tr><tr><td>world<br/></td></tr></table></div>");
    }

    private void testAsNormalizedText(final String expected, final String htmlSnippet) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + htmlSnippet
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals(expected, page.asNormalizedText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asTextDiv() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<div id='foo'>\n \n hello </div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals("hello", page.asNormalizedText());
        final HtmlDivision div = page.getHtmlElementById("foo");
        assertEquals("hello", div.asNormalizedText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void css() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<div style='display:inline'>1</div><div style='display:inline'>2</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals("12", page.getBody().asNormalizedText());
    }
}
