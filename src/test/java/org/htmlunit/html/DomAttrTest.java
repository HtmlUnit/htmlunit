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
 * Tests for {@link DomAttr}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class DomAttrTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getCanonicalXPath() throws Exception {
        final String html = DOCTYPE_HTML + "<html id='foo'><body></body></html>";
        final HtmlPage page = loadPage(html);
        final DomAttr attr = page.getHtmlElementById("foo").getAttributeNode("id");

        assertEquals("/html/@id", attr.getCanonicalXPath());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void textContent() throws Exception {
        final String html = DOCTYPE_HTML + "<html id='foo'><body></body></html>";
        final HtmlPage page = loadPage(html);
        final DomAttr attr = page.getDocumentElement().getAttributeNode("id");

        assertEquals("foo", attr.getTextContent());
        attr.setTextContent("hello");
        assertEquals("hello", attr.getTextContent());

        assertEquals(page.getDocumentElement(), page.getHtmlElementById("hello"));
    }
}
