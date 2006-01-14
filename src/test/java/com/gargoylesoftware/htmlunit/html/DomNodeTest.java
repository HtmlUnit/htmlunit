/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 *  Tests for DomNode
 *
 * @version  $Revision$
 * @author Chris Erskine
 */
public class DomNodeTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name Name of the test
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

        final Map attributes = new HashMap();
        attributes.put("id", "tag2"); // with the same id as the node to replace
        final DomNode node2 = page.getHtmlElementById("tag2");
        assertEquals("div", node2.getNodeName());
        final DomNode node3 = new HtmlSpan(page, attributes);
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

        final Map attributes = new HashMap();
        attributes.put("id", "newElt");
        final DomNode newNode = new HtmlDivision(page, attributes);
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
     * Reads the position of the node amongs the children of its parent
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
}
