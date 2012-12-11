/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests for {@link DomText}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Rodney Gitzel
 * @author Sudhan Moghe
 * @author Philip Graf
 */
@RunWith(BrowserRunner.class)
public class DomTextTest extends SimpleWebTestCase {

    /**
     * Test the clean up of &amp;nbsp; in strings.
     * @throws Exception if the test fails
     */
    @Test
    public void asText_nbsp() throws Exception {
        testPlainText("a b&nbsp;c  d &nbsp;e",  "a b c d  e");
        testPlainText("a b&nbsp;c  d &nbsp; e", "a b c d   e");
        testPlainText("&nbsp;a&nbsp;", " a ");
        testPlainText("&nbsp; a&nbsp;", "  a ");
        testPlainText("&nbsp;a &nbsp;", " a  ");
    }

    /**
     * Test font formats, as per bug #1731042.
     * See http://sourceforge.net/tracker/index.php?func=detail&aid=1731042&group_id=47038&atid=448266.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void asText_fontFormat() throws Exception {
        testAsText("a <b>b</b> c",  "a b c");
        testAsText("a <b>b</b>c",   "a bc");
        testAsText("a<b>b</b> c",   "ab c");
        testAsText("a<b>b</b>c",    "abc");

        // italics and teletype should work the same way
        testAsText("a <i>b</i> c",  "a b c");
        testAsText("a <i>b</i>c",   "a bc");
        testAsText("a<i>b</i> c",   "ab c");
        testAsText("a<i>b</i>c",    "abc");

        testAsText("a <tt>b</tt> c",  "a b c");
        testAsText("a <tt>b</tt>c",   "a bc");
        testAsText("a<tt>b</tt> c",   "ab c");
        testAsText("a<tt>b</tt>c",    "abc");

        testAsText("a <font>b</font> c",  "a b c");
        testAsText("a<font>b</font> c",   "ab c");
        testAsText("a <font>b</font>c",   "a bc");
        testAsText("a<font>b</font>c",    "abc");

        testAsText("a <span>b</span> c",  "a b c");
        testAsText("a<span>b</span> c",   "ab c");
        testAsText("a <span>b</span>c",   "a bc");
        testAsText("a<span>b</span>c",    "abc");

        testAsText("a<b><font><i>b</i></font></b>c",  "abc");
        testAsText("a<b><font> <i>b</i></font></b>c", "a bc");
    }

    /**
     * This test once tested regression for bug #1731042 but the expectations have been changed
     * as asText() should now use new lines when appropriate.
     * @throws Exception if the test fails
     */
    @Test
    public void asText_regression() throws Exception {
        String expected = "a" + LINE_SEPARATOR + "b" + LINE_SEPARATOR + "c";
        testAsText("a<ul><li>b</ul>c", expected);
        testAsText("a<p>b<br>c", expected);
        testAsText("a<table><tr><td>b</td></tr></table>c", expected);
        testAsText("a<div>b</div>c", expected);

        expected = "a" + LINE_SEPARATOR + "b" + LINE_SEPARATOR + "b" + LINE_SEPARATOR + "c";
        testAsText("a<table><tr><td> b </td></tr>\n<tr><td> b </td></tr></table>c", expected);
    }

    /**
     * Checks the HtmlTable* objects themselves.
     * @throws Exception if the test fails
     */
    @Test
    public void asText_table_elements() throws Exception {
        final String html = "<table id='table'><tr id='row'><td id='cell'> b </td></tr>\n</table>\n";
        final String content = "<html><body><span id='foo'>" + html + "</span></body></html>";

        final HtmlPage page = loadPage(content);

        assertEquals("b", page.getHtmlElementById("cell").asText());
        assertEquals("b", page.getHtmlElementById("row").asText());
        assertEquals("b", page.getHtmlElementById("table").asText());
    }

    private void testPlainText(final String html, final String expectedText) throws Exception {
        final String content = "<html><body><span id='foo'>" + html + "</span></body></html>";

        final HtmlPage page = loadPage(content);
        assertEquals(expectedText, page.asText());

        final HtmlElement elt = page.getHtmlElementById("foo");
        assertEquals(expectedText, elt.asText());

        final DomNode node = elt.getFirstChild();
        assertEquals(expectedText, node.asText());
    }

    private void testAsText(final String html, final String expectedText) throws Exception {
        final String content = "<html><body><span id='foo'>" + html + "</span></body></html>";

        final HtmlPage page = loadPage(content);
        final HtmlElement elt = page.getHtmlElementById("foo");
        assertEquals(expectedText, elt.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String unicodeString = "\u064A\u0627 \u0644\u064A\u064A\u0644";
        final String html = "<html>\n"
            + "<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head>\n"
            + "<body><span id='foo'>" + unicodeString + "</span></body></html>";

        final int[] expectedValues = {1610, 1575, 32, 1604, 1610, 1610, 1604};

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setDefaultResponse(TextUtil.stringToByteArray(html, "UTF-8"), 200, "OK", "text/html");
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(getDefaultUrl());
        final String xml = page.getHtmlElementById("foo").getFirstChild().asXml().trim();
        assertEquals(expectedValues.length, xml.length());
        int index = 0;
        for (final int expectedValue : expectedValues) {
            assertEquals(expectedValue, xml.codePointAt(index++));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void splitText() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<br><div id='tag'></div><br></body></html>";
        final HtmlPage page = loadPage(html);

        final DomNode divNode = page.getElementById("tag");

        final DomText node = new DomText(page, "test split");
        divNode.insertBefore(node);

        final DomNode previousSibling = node.getPreviousSibling();
        final DomNode nextSibling = node.getNextSibling();
        final DomNode parent = node.getParentNode();

        // position among parent's children
        final int position = readPositionAmongParentChildren(node);

        final DomText newNode = node.splitText(5);

        assertSame("new node previous sibling", node, newNode.getPreviousSibling());
        assertSame("previous sibling", previousSibling, node.getPreviousSibling());
        assertSame("new node next sibling", nextSibling, newNode.getNextSibling());
        assertSame("next sibling", newNode, node.getNextSibling());
        assertSame("parent", parent, newNode.getParentNode());
        assertSame(node, previousSibling.getNextSibling());
        assertSame(newNode, nextSibling.getPreviousSibling());
        assertEquals(position + 1, readPositionAmongParentChildren(newNode));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void splitLastDomText() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<br><div id='tag'></div><br></body></html>";
        final HtmlPage page = loadPage(content);

        final DomNode divNode = page.getElementById("tag");

        final DomText firstNode = new DomText(page, "test split");
        divNode.appendChild(firstNode);

        assertNull(firstNode.getPreviousSibling());

        final DomText secondNode = firstNode.splitText(5);

        final DomText thirdNode = new DomText(page, "test split");
        divNode.appendChild(thirdNode);

        assertSame(secondNode, firstNode.getNextSibling());
        assertNull(firstNode.getPreviousSibling());
        assertSame(firstNode, secondNode.getPreviousSibling());
        assertSame(thirdNode, secondNode.getNextSibling());
        assertSame(secondNode, thirdNode.getPreviousSibling());
        assertNull(thirdNode.getNextSibling());
        assertSame(divNode, secondNode.getParentNode());
        assertSame(divNode, thirdNode.getParentNode());
        assertEquals(0, readPositionAmongParentChildren(firstNode));
        assertEquals(1, readPositionAmongParentChildren(secondNode));
        assertEquals(2, readPositionAmongParentChildren(thirdNode));
    }

    /**
     * Reads the position of the node among the children of its parent
     * @param node the node to look at
     * @return the position
     */
    private int readPositionAmongParentChildren(final DomNode node) {
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
    public void splitText2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    div.appendChild(document.createElement('a'));\n"
            + "    var text = document.createTextNode('123456');\n"
            + "    div.appendChild(text);\n"
            + "    div.appendChild(document.createElement('hr'));\n"
            + "    alert(div.childNodes.length);\n"
            + "    text.splitText(3);\n"
            + "    alert(div.childNodes.length);\n"
            + "    alert(div.childNodes.item(2).nodeValue);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"3", "4", "456"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void setTextContent() throws Exception {
        final String html = "<html><body><span id='s'>abc</span></body></html>";
        final HtmlPage page = loadPage(html);
        final DomText text = (DomText) page.getElementById("s").getFirstChild();
        assertEquals("abc", text.getTextContent());
        text.setTextContent("xyz");
        assertEquals("xyz", text.getTextContent());
        assertEquals("xyz", page.asText());
    }

    /**
     * Tests if {@code getCanonicalXPath()} returns the correct XPath for a text
     * node without other text node siblings.
     * @throws Exception if an error occurs
     */
    @Test
    public void getCanonicalXPath_withoutTextSiblings() throws Exception {
        final String html = "<html><body><span id='s'>abc</span></body></html>";
        final HtmlPage page = loadPage(html);
        final DomText text = (DomText) page.getElementById("s").getFirstChild();
        assertEquals("/html/body/span/text()", text.getCanonicalXPath());
        assertEquals(text, page.getFirstByXPath(text.getCanonicalXPath()));
    }

    /**
     * Tests if {@code getCanonicalXPath()} returns the correct XPath for a text
     * node with other text node siblings.
     * @throws Exception if an error occurs
     */
    @Test
    public void getCanonicalXPath_withTextSiblings() throws Exception {
        final String html = "<html><body><span id='s'>abc<br/>def</span></body></html>";
        final HtmlPage page = loadPage(html);

        final DomText text1 = (DomText) page.getElementById("s").getFirstChild();
        assertEquals("abc", text1.getData());
        assertEquals("/html/body/span/text()[1]", text1.getCanonicalXPath());
        assertEquals(text1, page.getFirstByXPath(text1.getCanonicalXPath()));

        final DomText text2 = (DomText) page.getElementById("s").getChildNodes().get(2);
        assertEquals("def", text2.getData());
        assertEquals("/html/body/span/text()[2]", text2.getCanonicalXPath());
        assertEquals(text2, page.getFirstByXPath(text2.getCanonicalXPath()));
    }

}
