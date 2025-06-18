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
 * Tests for {@link HtmlParagraph}.
 *
 * @author Marc Guillemot
 */
public class HtmlParagraphTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml_emptyTag() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<p id='foo'></p>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlElement element = page.getHtmlElementById("foo");
        assertTrue(element.asXml().contains("</p>"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asNormalizedText_getTextContent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<p id='p1'></p>\n"
                + "<p id='p2'>abc</p>\n"
                + "<p id='p3'>$24.43</p>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(html);

        HtmlParagraph paragraph = page.getHtmlElementById("p1");
        assertEquals("", paragraph.asNormalizedText());
        assertEquals("", paragraph.getTextContent());

        paragraph = page.getHtmlElementById("p2");
        assertEquals("abc", paragraph.asNormalizedText());
        assertEquals("abc", paragraph.getTextContent());

        paragraph = page.getHtmlElementById("p3");
        assertEquals("$24.43", paragraph.asNormalizedText());
        assertEquals("$24.43", paragraph.getTextContent());
    }
}
