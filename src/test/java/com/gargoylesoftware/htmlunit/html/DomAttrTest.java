/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DomAttr}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DomAttrTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getCanonicalXPath() throws Exception {
        final String html = "<html id='foo'><body></body></html>";
        final HtmlPage page = loadPage(html);
        final DomAttr attr = page.<HtmlElement>getHtmlElementById("foo").getAttributeNode("id");

        assertEquals("/html/@id", attr.getCanonicalXPath());
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    public void textContent() throws Exception {
        final String html = "<html id='foo'><body></body></html>";
        final HtmlPage page = loadPage(html);
        final DomAttr attr = page.getDocumentElement().getAttributeNode("id");

        assertEquals("foo", attr.getTextContent());
        attr.setTextContent("hello");
        assertEquals("hello", attr.getTextContent());

        assertEquals(page.getDocumentElement(), page.getHtmlElementById("hello"));
    }
}
