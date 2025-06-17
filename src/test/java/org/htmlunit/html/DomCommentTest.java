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
 * Tests for {@link DomComment}.
 *
 * @author Karel Kolman
 * @author Ahmed Ashour
 * @author Philip Graf
 * @author Ronald Brill
 */
public class DomCommentTest extends SimpleWebTestCase {

    /**
     * Test the comment not visible when viewed by user.
     * @throws Exception if the test fails
     */
    @Test
    public void asNormalizedText() throws Exception {
        final String content = DOCTYPE_HTML + "<html><body><!-- a comment --></body></html>";
        final HtmlPage page = loadPage(content);
        assertEquals("", page.asNormalizedText());
    }

     /**
      * Test the comment correctness.
      * @throws Exception if the test fails
      */
    @Test
    public void asXml() throws Exception {
        final String comment = "<!-- a comment -->";
        final String content = DOCTYPE_HTML + "<html><body><span id='foo'>" + comment + "</span></body></html>";
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
        final String content = DOCTYPE_HTML + "<html><body id='body'><!-- c1 -->text<!-- c2 --></body></html>";
        final HtmlPage page = loadPage(content);
        final DomNode node = page.getHtmlElementById("body").getFirstChild();
        assertEquals(DomText.NODE_NAME, node.getNextSibling().getNodeName());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void setTextContent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><span id='s'><!--abc--></span></body></html>";
        final HtmlPage page = loadPage(html);
        final DomComment comment = (DomComment) page.getElementById("s").getFirstChild();
        assertEquals("abc", comment.getTextContent());
        comment.setTextContent("xyz");
        assertEquals("xyz", comment.getTextContent());
    }

    /**
     * Tests if {@code getCanonicalXPath()} returns the correct XPath for a
     * comment node.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void getCanonicalXPath_withoutCommentSiblings() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><span id='s'><!--abc--></span></body></html>";
        final HtmlPage page = loadPage(html);
        final DomComment comment = (DomComment) page.getElementById("s").getFirstChild();
        assertEquals("/html/body/span/comment()", comment.getCanonicalXPath());
        assertEquals(comment, page.getFirstByXPath(comment.getCanonicalXPath()));
    }

    /**
     * Tests if {@code getCanonicalXPath()} returns the correct XPath for a
     * comment node with other comment node siblings.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void getCanonicalXPath_withCommentSiblings() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><span id='s'><!--abc--><br/><!--def--></span></body></html>";
        final HtmlPage page = loadPage(html);

        final DomComment comment1 = (DomComment) page.getElementById("s").getFirstChild();
        assertEquals("abc", comment1.getData());
        assertEquals("/html/body/span/comment()[1]", comment1.getCanonicalXPath());
        assertEquals(comment1, page.getFirstByXPath(comment1.getCanonicalXPath()));

        final DomComment comment2 = (DomComment) page.getElementById("s").getChildNodes().get(2);
        assertEquals("def", comment2.getData());
        assertEquals("/html/body/span/comment()[2]", comment2.getCanonicalXPath());
        assertEquals(comment2, page.getFirstByXPath(comment2.getCanonicalXPath()));
    }

}
