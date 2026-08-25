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
import org.htmlunit.html.DomText;
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

    /**
     * @throws Exception if test fails
     */
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
        assertEquals(1, rangeSelectContents.getEndOffset());
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void cloneContentsSameContainerNotAncestor() throws Exception {
        final String html = "<html><body><div id='root'><p id='p1'>Hello World</p></div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode p1 = page.getElementById("p1");

        // Offsets 0 to 1 select child #0 of <p> (the text node "Hello World")
        final SimpleRange range = new SimpleRange(p1, 0, p1, 1);

        final DomDocumentFragment fragment = range.cloneContents();
        assertNotNull(fragment);

        assertEquals("Hello World", fragment.asXml().trim());
    }

    /**
     * @throws Exception if test fails
     */
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

    /**
     * Verifies that deleteContents() correctly clears endContainer_ text
     * when endOffset_ equals the total length of the text node.
     * @throws Exception if the test fails
     */
    @Test
    public void deleteContents_endOffsetEqualsTextLength() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'><span id='s1'>Hello</span><span id='s2'>World</span></div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode s1text = page.getElementById("s1").getFirstChild(); // "Hello"
        final DomNode s2text = page.getElementById("s2").getFirstChild(); // "World" (length = 5)

        // Select from offset 2 in s1text ("llo") up to offset 5 in s2text (all of "World")
        final SimpleRange range = new SimpleRange(s1text, 2, s2text, 5);
        range.deleteContents();

        assertEquals("He", s1text.getTextContent());
        assertEquals("", s2text.getTextContent());
    }

    /**
     * Verifies that deleteContents() preserves the endContainer element
     * when endOffset equals its child count and it has no next sibling.
     * @throws Exception if the test fails
     */
    @Test
    public void deleteContents_endOffsetEqualsChildCountLastChild() throws Exception {
        final String html = "<html><body>"
                + "<div id='root'>"
                + "  <div id='d1'><span id='s1'>1</span></div>"
                + "  <div id='d2'><span id='s2'>2</span></div>"
                + "</div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode d1 = page.getElementById("d1");
        final DomNode d2 = page.getElementById("d2");

        // Range from start of d1 (offset 0) to end of d2 (offset 1)
        final SimpleRange range = new SimpleRange(d1, 0, d2, 1);
        range.deleteContents();

        // Children inside d1 and d2 must be deleted
        assertNull(page.getElementById("s1"));
        assertNull(page.getElementById("s2"));

        // Boundary containers MUST remain in the DOM tree
        assertNotNull(page.getElementById("d1"));
        assertNotNull(page.getElementById("d2"));
    }

    /**
     * Verifies that containedNodes() collects nodes when endOffset
     * equals the child count of a container that has no next sibling.
     * @throws Exception if the test fails
     */
    @Test
    public void containedNodes_endOffsetEqualsChildCountLastChild() throws Exception {
        final String html = "<html><body>"
                + "<div id='root'>"
                + "  <div id='d1'><span id='s1'>1</span></div>"
                + "  <div id='d2'><span id='s2'>2</span></div>"
                + "</div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode d1 = page.getElementById("d1");
        final DomNode d2 = page.getElementById("d2");
        final DomNode s1 = page.getElementById("s1");
        final DomNode s2 = page.getElementById("s2");

        // Range selecting from d1 offset 0 to d2 offset 1 (d2.getNextSibling() == null)
        final SimpleRange range = new SimpleRange(d1, 0, d2, 1);
        final List<DomNode> nodes = range.containedNodes();

        assertTrue(nodes.contains(s1));
        assertTrue(nodes.contains(s2));
    }

    /**
     * @throws Exception if test fails
     */
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

    /**
     * @throws Exception if test fails
     */
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

    /**
     * @throws Exception if test fails
     */
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

    /**
     * @throws Exception if test fails
     */
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

    /**
     * Verifies that insertNode() inside a DomText correctly splits the text node
     * into two clean DomText siblings using DOM splitText.
     * @throws Exception if the test fails
     */
    @Test
    public void insertNode_splitsDomTextCorrectly() throws Exception {
        final String html = "<html><body><div id='div'>HelloWorld</div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode div = page.getElementById("div");
        final DomText textNode = (DomText) div.getFirstChild();

        final SimpleRange range = new SimpleRange(textNode, 5, textNode, 5);
        final DomNode span = page.createElement("span");
        span.setTextContent("123");

        range.insertNode(span);

        // Verify parent contains 3 children: DomText("Hello"), Span("123"), DomText("World")
        assertEquals(3, div.getChildNodes().getLength());
        assertEquals("Hello", div.getChildNodes().item(0).getTextContent());
        assertEquals("span", div.getChildNodes().item(1).getNodeName());
        assertEquals("World", div.getChildNodes().item(2).getTextContent());

        assertEquals("<div id=\"div\">Hello<span>123</span>World</div>", div.asXml());
    }

    /**
     * Verifies that insertNode() at the end boundary of a text node (offset == length)
     * does not duplicate text in the DOM tree.
     * @throws Exception if the test fails
     */
    @Test
    public void insertNode_atTextNodeBoundary_doesNotDuplicateText() throws Exception {
        final String html = "<html><body><div id='div'>Hello</div></body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode div = page.getElementById("div");
        final DomText textNode = (DomText) div.getFirstChild();

        // Text length is 5 ("Hello"); insertion offset is 5 (at the very end of the text node)
        final SimpleRange range = new SimpleRange(textNode, 5, textNode, 5);
        final DomNode span = page.createElement("span");
        span.setTextContent("123");

        range.insertNode(span);

        assertEquals("<div id=\"div\">Hello<span>123</span></div>", div.asXml().trim());
    }

    /**
     * @throws Exception if test fails
     */
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

    /**
     * @throws Exception if test fails
     */
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

    /**
     * Deleting across two DIFFERENT text
     * nodes -- start node tail and end node head must both be trimmed, end
     * first so that startOffset_ still points into the correct position
     * in the start node when it's processed second.
     * @throws Exception if the test fails
     */
    @Test
    public void deleteContents_acrossTwoDifferentTextNodes() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'><span id='s1'>Hello</span><span id='s2'>World</span></div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode s1text = page.getElementById("s1").getFirstChild(); // "Hello"
        final DomNode s2text = page.getElementById("s2").getFirstChild(); // "World"

        // select "llo" from s1 and "Wo" from s2 → "He" should remain in s1,
        // "rld" should remain in s2
        final SimpleRange range = new SimpleRange(s1text, 2, s2text, 2);
        range.deleteContents();

        assertEquals("He", s1text.getTextContent());
        assertEquals("rld", s2text.getTextContent());
    }

    /**
     * Deleting a range that spans whole element nodes (not just text
     * offsets).
     * @throws Exception if the test fails
     */
    @Test
    public void deleteContents_acrossElementNodes() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'><span id='a'>A</span><span id='b'>B</span><span id='c'>C</span></div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode div = page.getElementById("d");

        // select from start of span A to end of span C (all three spans)
        final SimpleRange range = new SimpleRange(div, 0, div, 3);
        range.deleteContents();

        assertEquals(0, div.getChildNodes().getLength());
    }

    /**
     * Deleting a range that starts mid-text in the first node and spans
     * into a following element -- the containing element of the deleted
     * nodes must be updated consistently.
     * @throws Exception if the test fails
     */
    @Test
    public void deleteContents_textNodeIntoElement() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'>Hello <span id='s'>World</span></div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode div = page.getElementById("d");
        final DomNode textNode = div.getFirstChild(); // "Hello "
        final DomNode span = page.getElementById("s");

        // range: from offset 5 in "Hello " up to the end of the span
        final SimpleRange range = new SimpleRange(textNode, 5, div, 2);
        range.deleteContents();

        // "Hello" should remain, span should be gone
        assertTrue(div.getTextContent().startsWith("Hello"));
        assertNull(page.getElementById("s"));
    }

    /**
     * Cloning a range that spans two sibling text nodes -- the returned
     * fragment must contain both partial texts without modifying the
     * originals.
     * @throws Exception if the test fails
     */
    @Test
    public void cloneContents_acrossTwoTextNodes() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'><span id='s1'>Hello</span><span id='s2'>World</span></div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode s1text = page.getElementById("s1").getFirstChild();
        final DomNode s2text = page.getElementById("s2").getFirstChild();

        final SimpleRange range = new SimpleRange(s1text, 2, s2text, 3);
        final DomDocumentFragment fragment = range.cloneContents();

        // original nodes must be UNCHANGED
        assertEquals("Hello", s1text.getTextContent());
        assertEquals("World", s2text.getTextContent());

        // fragment must contain the selected text
        assertNotNull(fragment);
        assertTrue(fragment.getTextContent().contains("llo"));
        assertTrue(fragment.getTextContent().contains("Wor"));
    }

    /**
     * Cloning must produce an INDEPENDENT copy -- mutating the original
     * after cloning must not affect the clone.
     * @throws Exception if the test fails
     */
    @Test
    public void cloneContents_isIndependentOfOriginal() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'><span id='s'>Hello</span></div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode span = page.getElementById("s");
        final DomNode textNode = span.getFirstChild();

        final SimpleRange range = new SimpleRange(textNode, 0, textNode, 5);
        final DomDocumentFragment fragment = range.cloneContents();
        final String fragmentTextBefore = fragment.getTextContent();

        // mutate the original AFTER cloning
        textNode.setTextContent("Changed");

        // clone must be unaffected
        assertEquals(fragmentTextBefore, fragment.getTextContent());
    }

    /**
     * Cloning a collapsed range (start == end) must return an empty
     * fragment, not throw.
     * @throws Exception if the test fails
     */
    @Test
    public void cloneContents_collapsedRange_returnsEmptyFragment() throws Exception {
        final String html = "<html><body><div id='d'>Text</div></body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode div = page.getElementById("d");
        final SimpleRange range = new SimpleRange(div, 0);

        final DomDocumentFragment fragment = range.cloneContents();
        assertNotNull(fragment);
        assertEquals("", fragment.getTextContent());
    }

    /**
     * Method extractContents() must return the selected content AND remove it
     * from the original document.
     * @throws Exception if the test fails
     */
    @Test
    public void extractContents_removesContentFromOriginalAndReturnsIt() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'>Hello World</div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode div = page.getElementById("d");
        final DomNode textNode = div.getFirstChild();

        final SimpleRange range = new SimpleRange(textNode, 6, textNode, 11); // "World"
        final DomDocumentFragment extracted = range.extractContents();

        // original must be shortened
        assertEquals("Hello ", div.getTextContent());

        // extracted fragment must contain removed text
        assertNotNull(extracted);
        assertTrue(extracted.getTextContent().contains("World"));
    }

    /**
     * Method extractContents() on a range spanning elements must remove all
     * selected nodes from the tree.
     * @throws Exception if the test fails
     */
    @Test
    public void extractContents_acrossElementNodes() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'><span id='a'>A</span><span id='b'>B</span><span id='c'>C</span></div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode div = page.getElementById("d");

        // select all three spans
        final SimpleRange range = new SimpleRange(div, 0, div, 3);
        final DomDocumentFragment fragment = range.extractContents();

        assertEquals(0, div.getChildNodes().getLength());

        // all three spans must be in the fragment
        assertTrue(fragment.getTextContent().contains("A"));
        assertTrue(fragment.getTextContent().contains("B"));
        assertTrue(fragment.getTextContent().contains("C"));
    }

    /**
     * Method surroundContents() must wrap the selected content inside the given
     * new parent node and insert the new parent in place of the selection.
     * @throws Exception if the test fails
     */
    @Test
    public void surroundContents_wrapsSelectedContent() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'>Hello World</div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode div = page.getElementById("d");
        final DomNode textNode = div.getFirstChild();

        final SimpleRange range = new SimpleRange(textNode, 6, textNode, 11); // "World"

        final org.htmlunit.html.HtmlSpan wrapper =
                (org.htmlunit.html.HtmlSpan) page.createElement("span");
        range.surroundContents(wrapper);

        // the wrapper must now be inside the div
        assertTrue(div.getTextContent().contains("World"));
        assertTrue(wrapper.getTextContent().contains("World"));
        assertSame(div, wrapper.getParentNode());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void cloneContents_elementContainer_returnsChildrenNotContainer() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'><span id='s1'>A</span><span id='s2'>B</span></div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode div = page.getElementById("d");

        // Select child 0 (<span id='s1'>A</span>)
        final SimpleRange range = new SimpleRange(div, 0, div, 1);
        final DomDocumentFragment fragment = range.cloneContents();

        assertNotNull(fragment);
        assertEquals(1, fragment.getChildNodes().getLength());
        assertEquals("<span id=\"s1\">A</span>", fragment.asXml().trim());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void cloneContents_collapsedInElement_returnsEmptyFragment() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'><span id='s1'>A</span><span id='s2'>B</span></div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode div = page.getElementById("d");

        // Collapsed at index 1 inside <div>
        final SimpleRange range = new SimpleRange(div, 1, div, 1);
        final DomDocumentFragment fragment = range.cloneContents();

        assertNotNull(fragment);
        assertEquals(0, fragment.getChildNodes().getLength());
        assertEquals("", fragment.asXml().trim());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void cloneContents_reversedBoundaries_handlesNodeMatching() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'><p id='p1'>First</p><p id='p2'>Second</p></div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);
        final DomNode p1 = page.getElementById("p1");
        final DomNode p2 = page.getElementById("p2");

        // Start is p2 (after p1 in DOM order), End is p1
        final SimpleRange range = new SimpleRange(p2, 0, p1, 1);

        // Should not throw IllegalStateException("Unable to find start node clone.")
        final DomDocumentFragment fragment = range.cloneContents();
        assertNotNull(fragment);
    }

    /**
     * Method cloneRange() must produce an equal but independent copy -- mutating
     * one must not affect the other.
     * @throws Exception if the test fails
     */
    @Test
    public void cloneRange_producesIndependentEqualCopy() throws Exception {
        final String html = "<html><body>"
                + "<div id='d1'>A</div><div id='d2'>B</div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode d1 = page.getElementById("d1");
        final DomNode d2 = page.getElementById("d2");

        final SimpleRange original = new SimpleRange(d1, 0, d2, 1);
        final SimpleRange clone = original.cloneRange();

        assertEquals(original, clone);
        assertNotSame(original, clone);

        // mutating the clone must not affect the original
        clone.collapse(true);
        assertFalse(original.isCollapsed());
    }

    /**
     * Method toString() must return the concatenated text of all nodes in the
     * range, not just the first one.
     * @throws Exception if the test fails
     */
    @Test
    public void toString_acrossMultipleNodes() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'><span>Hello</span> <span>World</span></div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode div = page.getElementById("d");
        final SimpleRange range = new SimpleRange(div, 0, div, 3);

        final String text = range.toString();
        assertTrue(text.contains("Hello"));
        assertTrue(text.contains("World"));
    }

    /**
     * Method toString() on a collapsed range must return the empty string.
     * @throws Exception if the test fails
     */
    @Test
    public void toString_collapsedRange_returnsEmpty() throws Exception {
        final String html = "<html><body><div id='d'>Text</div></body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode div = page.getElementById("d");
        final SimpleRange range = new SimpleRange(div, 0);

        assertEquals("", range.toString());
    }

    /**
     * Deleting before an offset deeper than 2 -- exercises the
     * i--/offset-- loop more than once to confirm correctness across
     * multiple iterations.
     * @throws Exception if the test fails
     */
    @Test
    public void deleteContents_deletesCorrectNumberOfLeadingChildren() throws Exception {
        final String html = "<html><body>"
                + "<div id='d'>"
                + "<span id='a'>A</span>"
                + "<span id='b'>B</span>"
                + "<span id='c'>C</span>"
                + "<span id='d2'>D</span>"
                + "<span id='e'>E</span>"
                + "</div></body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode div = page.getElementById("d");

        // range: select from child index 2 to end (selecting C,D,E)
        final SimpleRange range = new SimpleRange(div, 2, div, 5);
        range.deleteContents();

        // only A and B should remain
        assertEquals(2, div.getChildNodes().getLength());
        assertEquals("A", div.getChildNodes().item(0).getTextContent());
        assertEquals("B", div.getChildNodes().item(1).getTextContent());
    }

    /**
     * Method setEnd() must update end container and offset independently of the
     * start boundary.
     * @throws Exception if the test fails
     */
    @Test
    public void setEnd_updatesEndBoundaryIndependently() throws Exception {
        final String html = "<html><body>"
                + "<div id='d1'>A</div><div id='d2'>B</div>"
                + "</body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode d1 = page.getElementById("d1");
        final DomNode d2 = page.getElementById("d2");

        final SimpleRange range = new SimpleRange(d1, 0, d1, 1);
        range.setEnd(d2, 1);

        assertSame(d1, range.getStartContainer());
        assertEquals(0, range.getStartOffset());
        assertSame(d2, range.getEndContainer());
        assertEquals(1, range.getEndOffset());
        assertFalse(range.isCollapsed());
    }

    /**
     * When start and end are the SAME node, the common ancestor is that
     * node itself (or its parent, depending on the range type) -- confirms
     * the simple identity case.
     * @throws Exception if the test fails
     */
    @Test
    public void getCommonAncestorContainer_sameNode() throws Exception {
        final String html = "<html><body><div id='d'>Text</div></body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode div = page.getElementById("d");
        final DomNode textNode = div.getFirstChild();

        final SimpleRange range = new SimpleRange(textNode, 0, textNode, 4);
        assertSame(textNode, range.getCommonAncestorContainer());
    }

    /**
     * When start and end have NO common ancestor (neither is attached to the
     * same document tree), getCommonAncestorContainer() must return null,
     * not throw.
     * @throws Exception if the test fails
     */
    @Test
    public void getCommonAncestorContainer_noCommonAncestor_returnsNull() throws Exception {
        final String html1 = "<html><body><div id='d1'>A</div></body></html>";
        final String html2 = "<html><body><div id='d2'>B</div></body></html>";

        final HtmlPage page1 = loadPage(html1);
        final HtmlPage page2 = loadPage(html2);

        final DomNode d1 = page1.getElementById("d1");
        final DomNode d2 = page2.getElementById("d2");

        final SimpleRange range = new SimpleRange(d1, 0, d2, 1);
        assertNull(range.getCommonAncestorContainer());
    }
}
