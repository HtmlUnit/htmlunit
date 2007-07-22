/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode.DescendantElementsIterator;

/**
 *  Tests for DomNode
 *
 * @version $Revision$
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class DomNodeTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param name Name of the test
     */
    public DomNodeTest( final String name ) {
        super( name );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testRemoveAllChildren() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<p id='tag'><table>\n"
            + "<tr><td>row 1</td></tr>\n"
            + "<tr><td>row 2</td></tr>\n"
            + "</table></p></body></html>\n";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final DomNode node = page.getDocumentElement().getHtmlElementById("tag");
        node.removeAllChildren();
        assertEquals("Did not remove all nodes", null, node.getFirstChild());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testReplace() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div id='tag'/><br><div id='tag2'/></body></html>\n";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getDocumentElement().getHtmlElementById("tag");

        final DomNode previousSibling = node.getPreviousSibling();
        final DomNode nextSibling = node.getNextSibling();
        final DomNode parent = node.getParentNode();

        // position among parent's children
        final int position = readPositionAmongParentChildren(node);

        final DomNode newNode = new DomText(page, "test");
        node.replace(newNode);
        assertSame("previous sibling", previousSibling, newNode.getPreviousSibling());
        assertSame("next sibling", nextSibling, newNode.getNextSibling());
        assertSame("parent", parent, newNode.getParentNode());
        assertSame(newNode, previousSibling.getNextSibling());
        assertSame(newNode, nextSibling.getPreviousSibling());
        assertEquals(position, readPositionAmongParentChildren(newNode));

        final AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(null, "id", "id", null, "tag2"); // with the same id as the node to replace
        final DomNode node2 = page.getHtmlElementById("tag2");
        assertEquals("div", node2.getNodeName());
        
        final DomNode node3 = (HtmlSpan)HTMLParser.getFactory( HtmlSpan.TAG_NAME ).createElement(
                page, HtmlSpan.TAG_NAME, attributes);
        node2.replace(node3);
        assertEquals("span", page.getHtmlElementById("tag2").getTagName());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetNewNodeById() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div id='tag'/></body></html>\n";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getDocumentElement().getHtmlElementById("tag");

        final AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(null, "id", "id", null, "newElt");
        final DomNode newNode = (HtmlDivision)HTMLParser.getFactory( HtmlDivision.TAG_NAME ).createElement(
                page, HtmlDivision.TAG_NAME, attributes);
        try {
            page.getHtmlElementById("newElt");
            fail("Element should not exist yet");
        }
        catch (final ElementNotFoundException e) {
            // nothing to do, it's ok
        }

        node.replace(newNode);

        page.getHtmlElementById("newElt");
        try {
            page.getHtmlElementById("tag");
            fail("Element should not exist anymore");
        }
        catch (final ElementNotFoundException e) {
            // nothing to do, it's ok
        }

        newNode.insertBefore(node);
        page.getHtmlElementById("tag");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAppendChild() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div><div id='tag'/></div><br></body></html>\n";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getDocumentElement().getHtmlElementById("tag");

        final DomNode parent = node.getParentNode();

        // position among parent's children
        final int position = readPositionAmongParentChildren(node);

        final DomNode newNode = new DomText(page, "test");
        parent.appendChild(newNode);
        assertSame("new node previous sibling", node, newNode.getPreviousSibling());
        assertSame("new node next sibling", null, newNode.getNextSibling());
        assertSame("next sibling", newNode, node.getNextSibling());
        assertSame("parent", parent, newNode.getParentNode());
        assertEquals(position+1, readPositionAmongParentChildren(newNode));

        final DomNode newNode2 = new DomText(page, "test2");
        parent.appendChild(newNode2);
        page.getHtmlElementById("tag");
    }

    /**
     * @throws Exception if the test fails
     */
    public void testInsertBefore() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div id='tag'/><br></body></html>\n";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getDocumentElement().getHtmlElementById("tag");

        final DomNode previousSibling = node.getPreviousSibling();
        final DomNode nextSibling = node.getNextSibling();
        final DomNode parent = node.getParentNode();

        // position among parent's children
        final int position = readPositionAmongParentChildren(node);

        final DomNode newNode = new DomText(page, "test");
        node.insertBefore(newNode);
        assertSame("new node previous sibling", previousSibling, newNode.getPreviousSibling());
        assertSame("previous sibling", newNode, node.getPreviousSibling());
        assertSame("new node next sibling", node, newNode.getNextSibling());
        assertSame("next sibling", nextSibling, node.getNextSibling());
        assertSame("parent", parent, newNode.getParentNode());
        assertSame(newNode, previousSibling.getNextSibling());
        assertSame(node, nextSibling.getPreviousSibling());
        assertEquals(position, readPositionAmongParentChildren(newNode));
    }

    /**
     * Reads the position of the node among the children of its parent
     * @param node the node to look at
     * @return the position
     */
    private int readPositionAmongParentChildren(final DomNode node) {
        int i = 0;
        for (final Iterator iter = node.getParentNode().getChildIterator(); iter.hasNext();) {
            final DomNode child = (DomNode) iter.next();
            if (child == node) {
                return i;
            }
            ++i;
        }

        return -1;
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetByXPath() throws Exception {
        final String htmlContent
            = "<html><head><title>my title</title></head><body>"
            + "<p id='p1'><ul><li>foo 1</li><li>foo 2</li></li></p>"
            + "<div><span>bla</span></div>"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final List results = page.getByXPath("//title");
        assertEquals(1, results.size());
        final HtmlTitle title = (HtmlTitle) results.get(0);
        assertEquals("my title", title.asText());

        final HtmlHead head = (HtmlHead) title.getParentNode();
        assertEquals(results, head.getByXPath("//title"));
        assertEquals(results, head.getByXPath("/title"));
        assertEquals(results, head.getByXPath("title"));

        final HtmlParagraph p = (HtmlParagraph) page.getByXPath("//p").get(0);
        assertEquals(p, page.getHtmlElementById("p1"));
        final List lis = p.getByXPath("ul/li");
        assertEquals(2, lis.size());
        assertEquals(lis, page.getByXPath("//ul/li"));

        assertEquals(2, ((Number) p.getByXPath("count(//li)").get(0)).intValue());
    }

    /**
     * Verifies that {@link DomNode#getAllHtmlChildElements()} returns descendant elements in the correct order.
     * @throws Exception If an error occurs.
     */
    public void testGetAllHtmlChildElementsOrder() throws Exception {
        final String html =
            "<html><body id='0'>\n" +
            "<span id='I'><span id='I.1'><span id='I.1.a'/><span id='I.1.b'/><span id='I.1.c'/></span>" +
            "<span id='I.2'><span id='I.2.a'/></span></span>\n" +
            "<span id='II'/>\n" +
            "<span id='III'><span id='III.1'><span id='III.1.a'/></span></span>\n" +
            "</body></html>\n";
        final HtmlPage page = loadPage(html);
        final DescendantElementsIterator iterator = (DescendantElementsIterator)
            page.getDocumentElement().getAllHtmlChildElements();
        assertEquals("", iterator.nextElement().getId());
        assertEquals("0", iterator.nextElement().getId());
        assertEquals("I", iterator.nextElement().getId());
        assertEquals("I.1", iterator.nextElement().getId());
        assertEquals("I.1.a", iterator.nextElement().getId());
        assertEquals("I.1.b", iterator.nextElement().getId());
        assertEquals("I.1.c", iterator.nextElement().getId());
        assertEquals("I.2", iterator.nextElement().getId());
        assertEquals("I.2.a", iterator.nextElement().getId());
        assertEquals("II", iterator.nextElement().getId());
        assertEquals("III", iterator.nextElement().getId());
        assertEquals("III.1", iterator.nextElement().getId());
        assertEquals("III.1.a", iterator.nextElement().getId());
        assertFalse(iterator.hasNext());
    }

    static class DomChangeListenerTestImpl implements DomChangeListener {
        private final List collectedValues_ = new ArrayList();
        public void nodeAdded(final DomChangeEvent event) {
            collectedValues_.add("nodeAdded: " + event.getParentNode().getNodeName() + ',' +
                    event.getChangedNode().getNodeName());
        }
        public void nodeDeleted(final DomChangeEvent event) {
            collectedValues_.add("nodeDeleted: " + event.getParentNode().getNodeName() + ',' +
                    event.getChangedNode().getNodeName());
        }
        List getCollectedValues() {
            return collectedValues_;
        }
    }

    /**
     * @throws Exception if the test fails
     */
    public void testDomChangeListenerTestImpl_insertBefore() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    var div = document.createElement( 'DIV' );\n"
            + "    p1.insertBefore(div);"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";
        
        final String[] expectedValues = {"nodeAdded: p,div", "nodeAdded: p,div"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement p1 = page.getHtmlElementById("p1");
        final DomChangeListenerTestImpl listenerImpl = new DomChangeListenerTestImpl();
        p1.addDomChangeListener(listenerImpl);
        page.addDomChangeListener(listenerImpl);
        final HtmlButtonInput myButton = (HtmlButtonInput)page.getHtmlElementById("myButton");
        
        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testDomChangeListenerTestImpl_appendChild() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    var div = document.createElement( 'DIV' );\n"
            + "    p1.appendChild(div);"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";
        
        final String[] expectedValues = {"nodeAdded: p,div", "nodeAdded: p,div"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement p1 = page.getHtmlElementById("p1");
        final DomChangeListenerTestImpl listenerImpl = new DomChangeListenerTestImpl();
        p1.addDomChangeListener(listenerImpl);
        page.addDomChangeListener(listenerImpl);
        final HtmlButtonInput myButton = (HtmlButtonInput)page.getHtmlElementById("myButton");
        
        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testDomChangeListenerTestImpl_removeChild() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    div.removeChild(p1);"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div id='myDiv'><p id='p1' title='myTitle'></p></div>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";
        
        final String[] expectedValues = {"nodeDeleted: div,p", "nodeDeleted: div,p"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement p1 = page.getHtmlElementById("p1");
        final DomChangeListenerTestImpl listenerImpl = new DomChangeListenerTestImpl();
        p1.addDomChangeListener(listenerImpl);
        page.addDomChangeListener(listenerImpl);
        final HtmlButtonInput myButton = (HtmlButtonInput)page.getHtmlElementById("myButton");
        
        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }
}
