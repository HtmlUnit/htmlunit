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
 * Tests for {@link HtmlStyle}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlStyleTest extends SimpleWebTestCase {

    /**
     * Verifies that asNormalizedText() returns "checked" or "unchecked" according to the state of the checkbox.
     * @throws Exception if the test fails
     */
    @Test
    public void asNormalizedText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <title>foo</title>\n"
            + "  <style type='text/css' id='testStyle'>\n"
            + "    img { border: 0px }\n"
            + "  </style>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);

        final DomNode node = page.getHtmlElementById("testStyle");
        assertEquals("style", node.getNodeName());
        assertEquals("", node.asNormalizedText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asNormalizedText_getTextContent_insideDiv() throws Exception {
        final String html = DOCTYPE_HTML
            +   "<html>\n"
            + "<head></head>\n"
            + "<body>"
            + "<div id='tester'>"
                + "<style>h6.add-class {color: green;}</style>"
                + "Text content"
            + "</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final DomNode node = page.getHtmlElementById("tester");

        assertEquals("Text content", node.asNormalizedText());
        assertEquals("h6.add-class {color: green;}Text content", node.getTextContent());
    }

    /**
     * See <a href="http://sourceforge.net/support/tracker.php?aid=2802096">Bug 2802096</a>.
     * @throws Exception if the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title>\n"
            + "<style type='text/css'></style>\n"
            + "<style type='text/css'><!-- \n"
            + "body > p { color: red }\n"
            + "--></style>\n"
            + "</head><body>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final String xml = page.asXml();
        assertTrue("Style node not expanded in: " + xml, xml.contains("</style>"));

        final String xmlWithoutSpace = xml.replaceAll("\\s", "");
        assertTrue(xml, xmlWithoutSpace.contains("<styletype=\"text/css\"><!--body>p{color:red}--></style>"));
    }
}
