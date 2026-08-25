/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.html.impl;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.DomDocumentFragment;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

/**
 * Tests for SimpleRange.
 *
 * @author Ronald Brill
 */
public class SimpleRangeTest extends SimpleWebTestCase {

    /**
     * @throws Exception if test fails
     */
    @Test
    public void toStringOneNode() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head><title>Test page</title></head>\n"
            + "<body>\n"
            + "  <input type='text' id='myInput' value='abcd'>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(content);
        final DomNode node = page.getElementById("myInput");

        // select all
        SimpleRange range = new SimpleRange(node, 0, node, 4);
        assertEquals("abcd", range.toString());

        // select part
        range = new SimpleRange(node, 1, node, 3);
        assertEquals("bc", range.toString());

        // wrong start offset
        range = new SimpleRange(node, 7, node, 3);
        assertEquals("", range.toString());

        // wrong end offset
        range = new SimpleRange(node, 0, node, 11);
        assertEquals("abcd", range.toString());
    }

    @Test
    public void selectNodeVsSelectNodeContents() throws Exception {
        final String html = "<html><body><div id='parent'><p id='child'>text</p></div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode parent = page.getElementById("parent");
        final DomNode child = page.getElementById("child");

        // selectNode should set parent container as start/end container
        final SimpleRange rangeSelectNode = new SimpleRange();
        rangeSelectNode.selectNode(child);
        assertEquals(parent, rangeSelectNode.getStartContainer());
        assertEquals(parent, rangeSelectNode.getEndContainer());
        assertEquals(0, rangeSelectNode.getStartOffset());
        assertEquals(1, rangeSelectNode.getEndOffset());

        // selectNodeContents should set child as start/end container
        final SimpleRange rangeSelectContents = new SimpleRange();
        rangeSelectContents.selectNodeContents(child);
        assertEquals(child, rangeSelectContents.getStartContainer());
        assertEquals(child, rangeSelectContents.getEndContainer());
        assertEquals(0, rangeSelectContents.getStartOffset());
        assertEquals(1, rangeSelectContents.getEndOffset()); // 1 child node inside <p>
    }

    @Test
    public void cloneContentsSameContainerNotAncestor() throws Exception {
        final String html = "<html><body><div id='root'><p id='p1'>Hello World</p></div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode p1 = page.getElementById("p1");

        // Range where startContainer == endContainer, but neither is the root ancestor
        final SimpleRange range = new SimpleRange(p1, 0, p1, 1);

        // Before fix: threw IllegalStateException("Unable to find end node clone.")
        final DomDocumentFragment fragment = range.cloneContents();
        assertNotNull(fragment);
        assertEquals("<p id=\"p1\">Hello World</p>", fragment.asXml().trim());
    }

    @Test
    public void deleteContentsSameTextNode() throws Exception {
        final String html = "<html><body><div id='div'>Hello World</div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode div = page.getElementById("div");
        final DomNode textNode = div.getFirstChild();

        // Range selecting "llo W" (index 2 to 7) within the same text node
        final SimpleRange range = new SimpleRange(textNode, 2, textNode, 7);
        range.deleteContents();

        // Expect "He" + "orld" = "Heorld"
        assertEquals("Heorld", textNode.getTextContent());
    }

    @Test
    public void constructorInvalidOffsetNormalizer() throws Exception {
        final String html = "<html><body><div id='div'>Sample</div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode div = page.getElementById("div");

        // Start offset (5) > End offset (2) on the same node
        final SimpleRange range = new SimpleRange(div, 5, div, 2);

        // Constructor should adjust endOffset to match startOffset
        assertEquals(5, range.getStartOffset());
        assertEquals(5, range.getEndOffset());
        assertTrue(range.isCollapsed());
    }

    @Test
    public void collapseToStartAndEnd() throws Exception {
        final String html = "<html><body><div id='d1'>A</div><div id='d2'>B</div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode d1 = page.getElementById("d1");
        final DomNode d2 = page.getElementById("d2");

        final SimpleRange range = new SimpleRange(d1, 0, d2, 1);
        assertFalse(range.isCollapsed());

        // Collapse to start
        range.collapse(true);
        assertTrue(range.isCollapsed());
        assertEquals(d1, range.getStartContainer());
        assertEquals(d1, range.getEndContainer());

        // Reset and collapse to end
        range.setEnd(d2, 1);
        range.collapse(false);
        assertTrue(range.isCollapsed());
        assertEquals(d2, range.getStartContainer());
        assertEquals(d2, range.getEndContainer());
    }

    @Test
    public void getCommonAncestorContainer() throws Exception {
        final String html = "<html><body><div id='ancestor'>"
                + "<div id='branch1'><span id='s1'>A</span></div>"
                + "<div id='branch2'><span id='s2'>B</span></div>"
                + "</div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode ancestor = page.getElementById("ancestor");
        final DomNode s1 = page.getElementById("s1");
        final DomNode s2 = page.getElementById("s2");

        final SimpleRange range = new SimpleRange(s1, 0, s2, 0);
        assertEquals(ancestor, range.getCommonAncestorContainer());
    }

    @Test
    public void insertNodeInsideTextNode() throws Exception {
        final String html = "<html><body><div id='div'>HelloWorld</div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode div = page.getElementById("div");
        final DomNode textNode = div.getFirstChild();

        final SimpleRange range = new SimpleRange(textNode, 5, textNode, 5);
        final DomNode span = page.createElement("span");
        span.setTextContent("xyz");

        range.insertNode(span);

        assertEquals("<div id=\"div\">Hello<span>xyz</span>World</div>", div.asXml());
    }

    @Test
    public void containedNodes() throws Exception {
        final String html = "<html><body><div id='root'><p id='p1'>1</p><p id='p2'>2</p><p id='p3'>3</p></div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode root = page.getElementById("root");
        final DomNode p1 = page.getElementById("p1");
        final DomNode p3 = page.getElementById("p3");

        // Range spanning from p1 to p3
        final SimpleRange range = new SimpleRange(root, 0, root, 3);
        final List<DomNode> nodes = range.containedNodes();

        assertTrue(nodes.contains(p1));
        assertTrue(nodes.contains(page.getElementById("p2")));
        assertTrue(nodes.contains(p3));
    }

    @Test
    public void equalsAndHashCode() throws Exception {
        final String html = "<html><body><div id='div'>Test</div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode div = page.getElementById("div");

        final SimpleRange r1 = new SimpleRange(div, 0, div, 2);
        final SimpleRange r2 = new SimpleRange(div, 0, div, 2);
        final SimpleRange r3 = new SimpleRange(div, 0, div, 4);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertNotEquals(r1, r3);
        assertNotEquals(r1, "String Object");
    }
}
