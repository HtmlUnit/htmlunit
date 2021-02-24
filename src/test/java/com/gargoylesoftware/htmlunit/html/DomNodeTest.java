/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode.DescendantElementsIterator;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Tests for {@link DomNode}.
 *
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DomNodeTest extends SimpleWebTestCase {

    /**
     * Test hasAttributes() on an element with attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void elementHasAttributesWith() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");
        assertTrue("Element should have attribute", node.hasAttributes());
    }

    /**
     * Test hasAttributes() on an element with no attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void elementHasAttributesNone() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");
        final DomNode parent = node.getParentNode();
        assertFalse("Element should not have attribute", parent.hasAttributes());
    }

    /**
     * Test hasAttributes on a node that is not defined to have attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void nonElementHasAttributes() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");
        final DomNode child = node.getFirstChild();
        assertFalse("Text should not have attribute", child.hasAttributes());
    }

    /**
     * Test getPrefix on a node that is not defined to have a prefix.
     * @throws Exception if the test fails
     */
    @Test
    public void nonElementGetPrefix() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");
        final DomNode child = node.getFirstChild();
        assertEquals("Text should not have a prefix", null, child.getPrefix());
    }

    /**
     * Test getNamespaceURI on a node that is not defined to have a namespace.
     * @throws Exception if the test fails
     */
    @Test
    public void nonElementGetNamespaceURI() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");
        final DomNode child = node.getFirstChild();
        assertEquals("Text should not have a prefix", null, child.getNamespaceURI());
    }

    /**
     * Test getLocalName on a node that is not defined to have a local name.
     * @throws Exception if the test fails
     */
    @Test
    public void nonElementGetLocalName() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");
        final DomNode child = node.getFirstChild();
        assertEquals("Text should not have a prefix", null, child.getLocalName());
    }

    /**
     * Test setPrefix on a node that is not defined to have a prefix.
     * @throws Exception if the test fails
     */
    @Test
    public void nonElementSetPrefix() throws Exception {
        final String content = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");
        final DomNode child = node.getFirstChild();
        child.setPrefix("bar"); // This does nothing.
        assertEquals("Text should not have a prefix", null, child.getPrefix());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void removeAllChildren() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<p id='tag'><table>\n"
            + "<tr><td>row 1</td></tr>\n"
            + "<tr><td>row 2</td></tr>\n"
            + "</table></p></body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");
        node.removeAllChildren();
        assertEquals("Did not remove all nodes", null, node.getFirstChild());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void replace() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div id='tag'></div><br><div id='tag2'/></body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");

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

        final DomNode node3 = page.getWebClient().getPageCreator().getHtmlParser()
                                .getFactory(HtmlSpan.TAG_NAME)
                                .createElement(page, HtmlSpan.TAG_NAME, attributes);
        node2.replace(node3);
        assertEquals("span", page.getHtmlElementById("tag2").getTagName());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getNewNodeById() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div id='tag'/></body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");

        final AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(null, "id", "id", null, "newElt");
        final DomNode newNode = page.getWebClient().getPageCreator().getHtmlParser()
                                    .getFactory(HtmlDivision.TAG_NAME)
                                    .createElement(page, HtmlDivision.TAG_NAME, attributes);
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
    @Test
    public void appendChild() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div><div id='tag'></div></div><br></body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");

        final DomNode parent = node.getParentNode();

        // position among parent's children
        final int position = readPositionAmongParentChildren(node);

        final DomNode newNode = new DomText(page, "test");
        parent.appendChild(newNode);
        assertSame("new node previous sibling", node, newNode.getPreviousSibling());
        assertSame("new node next sibling", null, newNode.getNextSibling());
        assertSame("next sibling", newNode, node.getNextSibling());
        assertSame("parent", parent, newNode.getParentNode());
        assertEquals(position + 1, readPositionAmongParentChildren(newNode));

        final DomNode newNode2 = new DomText(page, "test2");
        parent.appendChild(newNode2);
        page.getHtmlElementById("tag");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void insertBefore() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div id='tag'></div><br></body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode node = page.getElementById("tag");

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
    private static int readPositionAmongParentChildren(final DomNode node) {
        int i = 0;
        for (final DomNode child : node.getParentNode().getChildren()) {
            if (child == node) {
                return i;
            }
            i++;
        }

        return -1;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getByXPath() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "  <head>\n"
            + "    <title>my title</title>\n"
            + "  </head>"
            + "  <body>\n"
            + "    <div id='d1'>\n"
            + "      <ul>\n"
            + "        <li>foo 1</li>\n"
            + "        <li>foo 2</li>\n"
            + "      </ul>\n"
            + "    </div>\n"
            + "    <div><span>bla</span></div>\n"
            + "</body>\n"
            + "</html>";
        final HtmlPage page = loadPage(htmlContent);

        final List<?> results = page.getByXPath("//title");
        assertEquals(1, results.size());
        final HtmlTitle title = (HtmlTitle) results.get(0);
        assertEquals("my title", title.asText());

        final HtmlHead head = (HtmlHead) title.getParentNode();
        assertEquals(results, head.getByXPath("//title"));
        assertEquals(results, head.getByXPath("./title"));
        assertTrue(head.getByXPath("/title").isEmpty());
        assertEquals(results, head.getByXPath("title"));

        final HtmlElement div = page.getFirstByXPath("//div");
        assertSame(div, page.getHtmlElementById("d1"));
        final List<?> lis = div.getByXPath("ul/li");
        assertEquals(2, lis.size());
        assertEquals(lis, page.getByXPath("//ul/li"));

        assertEquals(2, div.<Number>getFirstByXPath("count(//li)").intValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getByXPathSelectedNode() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "  <head>\n"
            + "    <title>my title</title>\n"
            + "  </head>"
            + "  <body>\n"
            + "    <h1 id='outer_h1'>HtmlUnit</h1>\n"
            + "    <div id='d1'>\n"
            + "      <h1 id='h1'>HtmlUnit</h1>\n"
            + "    </div>\n"
            + "</body>\n"
            + "</html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlDivision divNode = (HtmlDivision) page.getElementById("d1");

        assertEquals(page.getElementById("h1"), divNode.getByXPath(".//h1").get(0));
        assertEquals(page.getElementById("outer_h1"), divNode.getByXPath("//h1").get(0));
    }

    /**
     * Regression test for bug #1149: xmlns value has to be trimmed.
     * @throws Exception if the test fails
     */
    @Test
    public void getByXPath_trim_namespace() throws Exception {
        final String html = "<html xmlns=' http://www.w3.org/1999/xhtml'>\n"
            + "<body>\n"
            + "<div><span>bla</span></div>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);

        final List<?> results = page.getByXPath("//div");
        assertEquals(1, results.size());
    }

    /**
     * Make sure display: none has no effect for xpath expressions.
     * @throws Exception if the test fails
     */
    @Test
    public void getFirstByXPathDisplayNone() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "  <head>\n"
            + "    <title>my title</title>\n"
            + "  </head>"
            + "  <body>\n"
            + "    <div id='d1'><span style='display: none;'>bla</span></div>\n"
            + "</body>\n"
            + "</html>";
        final HtmlPage page = loadPage(htmlContent);

        HtmlElement span = page.getFirstByXPath("//div/span");
        assertEquals("<span style=\"display: none;\">\r\n  bla\r\n</span>\r\n", span.asXml());
        assertFalse(span.isDisplayed());

        span = page.getFirstByXPath("//span[text()=\"bla\"]");
        assertEquals("<span style=\"display: none;\">\r\n  bla\r\n</span>\r\n", span.asXml());
        assertFalse(span.isDisplayed());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getFirstByXPath() throws Exception {
        final String htmlContent
            = "<html><head><title>my title</title></head><body>\n"
            + "<div id='d1'><ul><li>foo 1</li><li>foo 2</li></ul></div>\n"
            + "<div><span>bla</span></div>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTitle title = page.getFirstByXPath("//title");
        assertEquals("my title", title.asText());

        final HtmlHead head = (HtmlHead) title.getParentNode();
        assertSame(title, head.getFirstByXPath("//title"));
        assertSame(title, head.getFirstByXPath("./title"));
        assertNull(head.getFirstByXPath("/title"));
        assertSame(title, head.getFirstByXPath("title"));

        final HtmlElement div = page.getFirstByXPath("//div");
        assertSame(div, page.getHtmlElementById("d1"));
        final HtmlListItem listItem = (HtmlListItem) div.getFirstByXPath("ul/li");
        assertSame(listItem, page.getFirstByXPath("//ul/li"));

        assertEquals(2, ((Number) div.getFirstByXPath("count(//li)")).intValue());
    }

    /**
     * Verifies that {@link DomNode#getHtmlElementDescendants()} returns descendant elements in the correct order.
     * @throws Exception if an error occurs
     */
    @Test
    public void getHtmlElementDescendantsOrder() throws Exception {
        final String html = "<html><body id='0'>\n"
            + "<span id='I'><span id='I.1'><span id='I.1.a'/><span id='I.1.b'/><span id='I.1.c'/></span>\n"
            + "<span id='I.2'><span id='I.2.a'/></span></span>\n"
            + "<span id='II'/>\n"
            + "<span id='III'><span id='III.1'><span id='III.1.a'/></span></span>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final DescendantElementsIterator<HtmlElement> iterator = (DescendantElementsIterator<HtmlElement>)
            page.getDocumentElement().getHtmlElementDescendants().iterator();
        assertEquals("", iterator.nextNode().getId());
        assertEquals("0", iterator.nextNode().getId());
        assertEquals("I", iterator.nextNode().getId());
        assertEquals("I.1", iterator.nextNode().getId());
        assertEquals("I.1.a", iterator.nextNode().getId());
        assertEquals("I.1.b", iterator.nextNode().getId());
        assertEquals("I.1.c", iterator.nextNode().getId());
        assertEquals("I.2", iterator.nextNode().getId());
        assertEquals("I.2.a", iterator.nextNode().getId());
        assertEquals("II", iterator.nextNode().getId());
        assertEquals("III", iterator.nextNode().getId());
        assertEquals("III.1", iterator.nextNode().getId());
        assertEquals("III.1.a", iterator.nextNode().getId());
        assertFalse(iterator.hasNext());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void getDescendants_remove() throws Exception {
        final String html =
              "<html><body id='body'>\n"
            + "<div id='a'>a<div id='b'>b</div>a<div id='c'>c</div>a</div><div id='d'>d</div>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        assertEquals("abacad", page.asText().replaceAll("\\s", ""));
        final DescendantElementsIterator<HtmlElement> iterator = (DescendantElementsIterator<HtmlElement>)
            page.getDocumentElement().getHtmlElementDescendants().iterator();
        assertEquals("", iterator.nextNode().getId());
        assertEquals("body", iterator.nextNode().getId());
        assertEquals("a", iterator.nextNode().getId());
        iterator.remove();
        assertEquals("d", iterator.nextNode().getId());
        assertFalse(iterator.hasNext());
        assertEquals("d", page.asText().replaceAll("\\s", ""));
    }

    static class DomChangeListenerTestImpl implements DomChangeListener {
        private final List<String> collectedValues_ = new ArrayList<>();
        @Override
        public void nodeAdded(final DomChangeEvent event) {
            collectedValues_.add("nodeAdded: " + event.getParentNode().getNodeName() + ','
                    + event.getChangedNode().getNodeName());
        }
        @Override
        public void nodeDeleted(final DomChangeEvent event) {
            collectedValues_.add("nodeDeleted: " + event.getParentNode().getNodeName() + ','
                    + event.getChangedNode().getNodeName());
        }
        List<String> getCollectedValues() {
            return collectedValues_;
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domChangeListenerTestImpl_insertBefore() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    var div = document.createElement('DIV');\n"
            + "    p1.insertBefore(div, null);\n"
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
        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");

        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domChangeListenerTestImpl_appendChild() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    var div = document.createElement('DIV');\n"
            + "    p1.appendChild(div);\n"
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
        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");

        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"nodeDeleted: div,p", "nodeDeleted: div,p"})
    public void domChangeListenerTestImpl_removeChild() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    div.removeChild(p1);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div id='myDiv'><p id='p1' title='myTitle'></p></div>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement p1 = page.getHtmlElementById("p1");
        final DomChangeListenerTestImpl listenerImpl = new DomChangeListenerTestImpl();
        p1.addDomChangeListener(listenerImpl);
        page.addDomChangeListener(listenerImpl);
        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");

        myButton.click();
        assertEquals(getExpectedAlerts(), listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domChangeListenerRegisterNewListener() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    var div = document.createElement('DIV');\n"
            + "    p1.appendChild(div);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final List<String> l = new ArrayList<>();
        final DomChangeListener listener2 = new DomChangeListenerTestImpl() {
            @Override
            public void nodeAdded(final DomChangeEvent event) {
                l.add("in listener 2");
            }
        };
        final DomChangeListener listener1 = new DomChangeListenerTestImpl() {
            @Override
            public void nodeAdded(final DomChangeEvent event) {
                l.add("in listener 1");
                page.addDomChangeListener(listener2);
            }
        };

        page.addDomChangeListener(listener1);

        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");
        myButton.click();

        final String[] expectedValues = {"in listener 1"};
        assertEquals(expectedValues, l);
        l.clear();

        myButton.click();
        final String[] expectedValues2 = {"in listener 1", "in listener 2"};
        assertEquals(expectedValues2, l);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getByXPath_XML() throws Exception {
        final String xml
            = "<books>\n"
            + "  <book>\n"
            + "    <title>Immortality</title>\n"
            + "    <author>John Smith</author>\n"
            + "  </book>\n"
            + "</books>";

        getMockWebConnection().setResponse(URL_FIRST, xml, MimeType.TEXT_XML);
        final WebClient client = getWebClientWithMockWebConnection();
        final XmlPage page = (XmlPage) client.getPage(URL_FIRST);

        final List<?> results = page.getByXPath("//title");
        assertEquals(1, results.size());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void ownerDocument() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document == document.body.ownerDocument);\n"
            + "      alert(document == document.getElementById('foo').ownerDocument);\n"
            + "      alert(document == document.body.firstChild.ownerDocument);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>bla\n"
            + "<div id='foo'>bla</div>\n"
            + "</body>\n"
            + "</html>";

        final String[] expectedAlerts = {"true", "true", "true"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getCanonicalXPath() throws Exception {
        final String content = "<html><head></head><body><div id='div1'/><div id='div2'/></body></html>";
        final HtmlPage page = loadPage(content);
        for (final HtmlElement element : page.getHtmlElementDescendants()) {
            final List<?> foundElements = page.getByXPath(element.getCanonicalXPath());
            assertEquals(1, foundElements.size());
            assertSame(element, foundElements.get(0));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getChildNodes_remove() throws Exception {
        final String content = "<html><body id='b'><div id='d1'></div><div id='d2'></div></body></html>";
        final HtmlPage page = loadPage(content);
        final DomNodeList<DomNode> children = page.getElementById("b").getChildNodes();
        assertEquals(2, children.getLength());
        page.getElementById("d1").remove();
        assertEquals(1, children.getLength());
        page.getElementById("d2").remove();
        assertEquals(0, children.getLength());
        assertNull(children.get(0));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String html = "<html><head></head><body></body></html>";
        final DomChangeListenerTestImpl listener = new DomChangeListenerTestImpl();
        HtmlPage page = loadPage(html);
        page.addDomChangeListener(listener);
        page = clone(page);
        page.removeDomChangeListener(listener);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + "#d2 { display: none; }\n"
            + "#d3 { visibility: hidden; }\n"
            + "#d4 { display: block !important; }\n"
            + "</style>\n"
            + "<div id='d1'>hello</div>\n"
            + "<div id='d2'>world</div>\n"
            + "<div id='d3'>again</div>\n"
            + "<div id='d4' style='display: none' >important</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertTrue(page.getElementById("d1").isDisplayed());
        assertFalse(page.getElementById("d2").isDisplayed());
        assertFalse(page.getElementById("d3").isDisplayed());
        assertTrue(page.getElementById("d4").isDisplayed());

        getWebClient().getOptions().setCssEnabled(false);
        assertTrue(page.getElementById("d1").isDisplayed());
        assertTrue(page.getElementById("d2").isDisplayed());
        assertTrue(page.getElementById("d3").isDisplayed());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isDisplayedMouseOver() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + "#d2:hover { display: none; }\n"
            + "#d3:hover { visibility: hidden; }\n"
            + "</style>\n"
            + "<div id='d1'>hello</div>\n"
            + "<div id='d2'>world</div>\n"
            + "<div id='d3'>again</div>\n"
            + "<div id='d4' style='display: none' >important</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertTrue(page.getElementById("d1").isDisplayed());

        HtmlElement elem = page.getHtmlElementById("d2");
        assertTrue(elem.isDisplayed());
        elem.mouseOver();
        assertFalse(elem.isDisplayed());
        elem.mouseOut();
        assertTrue(elem.isDisplayed());

        elem = page.getHtmlElementById("d3");
        assertTrue(elem.isDisplayed());
        elem.mouseOver();
        assertFalse(elem.isDisplayed());
        elem.mouseOut();
        assertTrue(elem.isDisplayed());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isDisplayedMouseOverParent() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + "#d1:hover { display: none; }\n"
            + "#d2:hover { visibility: hidden; }\n"
            + "</style>\n"
            + "<div id='d1'><div id='d1-1'>hello</div></div>\n"
            + "<div id='d2'><div id='d2-1'>world</div></div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        HtmlElement elem = page.getHtmlElementById("d1");
        assertTrue(elem.isDisplayed());
        page.getHtmlElementById("d1-1").mouseOver();
        assertFalse(elem.isDisplayed());
        page.getHtmlElementById("d1-1").mouseOut();
        assertTrue(elem.isDisplayed());

        elem = page.getHtmlElementById("d2");
        assertTrue(elem.isDisplayed());
        page.getHtmlElementById("d2-1").mouseOver();
        assertFalse(elem.isDisplayed());
        page.getHtmlElementById("d2-1").mouseOut();
        assertTrue(elem.isDisplayed());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isDisplayedMousePath() throws Exception {
        final String html = "<html><head>\n"
            + "<style>\n"
            + "#d1:hover #d2 { display: none; }\n"
            + "</style>\n"
            + "<div id='d1'>hello<div id='d2'>world</div></div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlElement elem1 = page.getHtmlElementById("d1");
        final HtmlElement elem2 = page.getHtmlElementById("d2");

        assertTrue(elem1.isDisplayed());
        assertTrue(elem2.isDisplayed());

        elem1.mouseOver();
        assertTrue(elem1.isDisplayed());
        assertFalse(elem2.isDisplayed());

        elem1.mouseOut();
        assertTrue(elem1.isDisplayed());
        assertTrue(elem2.isDisplayed());
    }
}
