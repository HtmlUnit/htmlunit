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
 * Tests for {@link DomComment}.
 *
 * @version $Revision$
 * @author Karel Kolman
 * @author Ahmed Ashour
 */
public class DomCommentTest extends WebTestCase {

    /**
     * Test the comment not visible when viewed by user.
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String content = "<html><body><!-- a comment --></body></html>";
        final HtmlPage page = loadPage(content);
        assertEquals("", page.asText());
    }

     /**
     * Test the comment correctness.
     * @throws Exception if the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String comment = "<!-- a comment -->";
        final String content = "<html><body><span id='foo'>" + comment + "</span></body></html>";
        final HtmlPage page = loadPage(content);
        final HtmlElement elt = page.getHtmlElementById("foo");
        final DomNode node = elt.getFirstChild();
        assertEquals(comment, node.asXml());
    }

     /**
     * Test comment and character data sibling correctness.
     * @throws Exception if the test fails
     */
    @Test
    public void textSibling() throws Exception {
        final String content = "<html><body id='body'><!-- c1 -->text<!-- c2 --></body></html>";
        final HtmlPage page = loadPage(content);
        final DomNode node = page.<HtmlElement>getHtmlElementById("body").getFirstChild();
        assertEquals(DomText.NODE_NAME, node.getNextSibling().getNodeName());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void setTextContent() throws Exception {
        final String html = "<html><body><span id='s'><!--abc--></span></body></html>";
        final HtmlPage page = loadPage(html);
        final DomComment comment = (DomComment) page.getElementById("s").getFirstChild();
        assertEquals("abc", comment.getTextContent());
        comment.setTextContent("xyz");
        assertEquals("xyz", comment.getTextContent());
    }

}
