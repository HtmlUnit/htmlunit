/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Unit tests for {@link HtmlElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Denis N. Antonioli
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 */
@RunWith(BrowserRunner.class)
public class HtmlElementTest extends WebTestCase {

    /**
     * Test hasAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementHasAttributeWith() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        Assert.assertEquals("Element should have attribute", true, node.hasAttribute("id"));
    }

    /**
     * Test hasAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementHasAttributeNone() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        Assert.assertEquals("Element should not have attribute", false, node.hasAttribute("foo"));
    }

    /**
     * Test hasAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementHasAttributeNSWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        Assert.assertTrue("Element should have attribute", node.hasAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test hasAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementHasAttributeNSNone() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        Assert.assertFalse("Element should not have attribute", node.hasAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test getAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementGetAttributeWith() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        Assert.assertEquals("Element should have attribute", "tag", node.getAttribute("id"));
    }

    /**
     * Test getAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementGetAttributeNone() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        Assert.assertEquals("Element should not have attribute", "", node.getAttribute("foo"));
    }

    /**
     * Test getAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementGetAttributeNSWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        Assert.assertEquals("Element should have attribute", "bar", node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test getAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementGetAttributeNSNone() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        Assert.assertEquals("Element should not have attribute", "", node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test getNamespaceURI on an attribute that has a namespace.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementGetNamespaceURIWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("ns:foo".equals(attr.getName())) {
                Assert.assertEquals("Element should have a namespace URI", "http://foobar", attr.getNamespaceURI());
                return;
            }
        }
        Assert.fail("Attribute ns:foo not found.");
    }

    /**
     * Test getNamespaceURI on an attribute that has a namespace.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementGetNamespaceURINone() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("id".equals(attr.getName())) {
                Assert.assertEquals("Element should not have a namespace URI", null, attr.getNamespaceURI());
                return;
            }
        }
        Assert.fail("Attribute ns:foo not found.");
    }

    /**
     * Test getLocalName on an attribute that has a local name.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementGetLocalNameWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("ns:foo".equals(attr.getName())) {
                Assert.assertEquals("Element should have a local name", "foo", attr.getLocalName());
                return;
            }
        }
        Assert.fail("Attribute ns:foo not found.");
    }

    /**
     * Test getLocalName on an attribute that has a local name.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementGetLocalNameNone() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("id".equals(attr.getName())) {
                // This is not standard, but to change it now would break backwards compatibility.
                Assert.assertEquals("Element should not have a local name", "id", attr.getLocalName());
                return;
            }
        }
        Assert.fail("Attribute ns:foo not found.");
    }

    /**
     * Test getPrefix on an attribute that has a prefix.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementGetPrefixWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("ns:foo".equals(attr.getName())) {
                Assert.assertEquals("Element should have a prefix", "ns", attr.getPrefix());
                return;
            }
        }
        Assert.fail("Attribute ns:foo not found.");
    }

    /**
     * Test getPrefix on an attribute that has a prefix.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementGetPrefixNone() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("id".equals(attr.getName())) {
                Assert.assertEquals("Element should not have a prefix", null, attr.getPrefix());
                return;
            }
        }
        Assert.fail("Attribute ns:foo not found.");
    }

    /**
     * Test setPrefix on an attribute that has a prefix.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementSetPrefix() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("ns:foo".equals(attr.getName())) {
                attr.setPrefix("other");
                Assert.assertEquals("Element should have a changed prefix", "other", attr.getPrefix());
                Assert.assertEquals("setPrefix should change qualified name", "other:foo", attr.getName());
                return;
            }
        }
        Assert.fail("Attribute ns:foo not found.");
    }

    /**
     * Test setAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementSetAttributeWith() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        node.setAttribute("id", "other");
        Assert.assertEquals("Element should have attribute", "other", node.getAttribute("id"));
    }

    /**
     * Test setAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementSetAttributeNone() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        node.setAttribute("foo", "other");
        Assert.assertEquals("Element should have attribute", "other", node.getAttribute("foo"));
    }

    /**
     * Test setAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementSetAttributeNSWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        node.setAttributeNS("http://foobar", "ns:foo", "other");
        Assert.assertEquals("Element should have attribute", "other", node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test setAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementSetAttributeNSNone() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        node.setAttributeNS("http://foobar", "ns:foo", "other");
        Assert.assertEquals("Element should not have attribute", "other", node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test removeAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementRemoveAttributeWith() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        node.removeAttribute("id");
        Assert.assertEquals("Element should not have removed attribute", "", node.getAttribute("id"));
    }

    /**
     * Test removeAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementRemoveAttributeNone() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        node.removeAttribute("foo");
        Assert.assertEquals("Element should not have attribute", "", node.getAttribute("foo"));
    }

    /**
     * Test removeAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementRemoveAttributeNSWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        node.removeAttributeNS("http://foobar", "foo");
        Assert.assertEquals("Element should not have removed attribute", "",
            node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test removeAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void testElementRemoveAttributeNSNone() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getDocumentElement().getElementById("tag");
        node.removeAttributeNS("http://foobar", "foo");
        Assert.assertEquals("Element should not have attribute", "", node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * Verifies that cloned node attributes have the same initial values, but changes can be made
     * to the clone without affecting the original node, and that the id attribute is treated the
     * same as all the other attributes. See bug 1707726.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "false", "true", "a", "a", "b", "b", "b", "c" })
    public void testClonedNodeAttributes() throws Exception {
        final String html = "<html><body id='a' title='b'><script>\n"
            + "var x = document.body.cloneNode(true);\n"
            + "alert(document.body==x);\n"
            + "alert(document.getElementById('a')==document.body);\n"
            + "alert(document.body.id);\n"
            + "alert(x.id);\n"
            + "alert(document.body.title);\n"
            + "alert(x.title);\n"
            + "x.title='c';\n"
            + "alert(document.body.title);\n"
            + "alert(x.title);\n"
            + "</script></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetEnclosingForm() throws Exception {
        final String htmlContent =
            "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<table><tr><td><input type='text' id='foo'/></td></tr></table>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlInput input = form.getElementById("foo");
        assertSame(form, input.getEnclosingForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetEnclosing() throws Exception {
        final String htmlContent =
            "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<table id='table1'>\n"
            + "<tr id='tr1'><td id='td1'>foo</td></tr>\n"
            + "<tr id='tr2'><td id='td2'>foo</td></tr>\n"
            + "</table>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlElement td1 = page.getHtmlElementById("td1");
        assertEquals("tr1", td1.getEnclosingElement("tr").getId());
        assertEquals("tr1", td1.getEnclosingElement("TR").getId());
        assertEquals("table1", td1.getEnclosingElement("table").getId());
        assertEquals("form1", td1.getEnclosingElement("form").getId());

        final HtmlElement td2 = page.getHtmlElementById("td2");
        assertEquals("tr2", td2.getEnclosingElement("tr").getId());
        assertEquals("tr2", td2.getEnclosingElement("TR").getId());
        assertEquals("table1", td2.getEnclosingElement("table").getId());
        assertEquals("form1", td2.getEnclosingElement("form").getId());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText_WithComments() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<p id='p1'>foo<!--bar--></p>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement element = page.getHtmlElementById("p1");
        assertEquals("foo", element.asText());
    }

    /**
     * Tests constants.
     */
    @Test
    public void testConstants() {
        assertEquals("", HtmlElement.ATTRIBUTE_NOT_DEFINED);
        assertEquals("", HtmlElement.ATTRIBUTE_VALUE_EMPTY);
        assertTrue("Not the same object",
            HtmlElement.ATTRIBUTE_NOT_DEFINED != HtmlElement.ATTRIBUTE_VALUE_EMPTY);
    }

    static class HtmlAttributeChangeListenerTestImpl implements HtmlAttributeChangeListener {
        private final List<String> collectedValues_ = new ArrayList<String>();
        @Test
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            collectedValues_.add("attributeAdded: " + event.getHtmlElement().getTagName() + ','
                    + event.getName() + ',' + event.getValue());
        }
        @Test
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            collectedValues_.add("attributeRemoved: " + event.getHtmlElement().getTagName() + ','
                    + event.getName() + ',' + event.getValue());
        }

        @Test
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            collectedValues_.add("attributeReplaced: " + event.getHtmlElement().getTagName() + ','
                    + event.getName() + ',' + event.getValue());
        }
        List<String> getCollectedValues() {
            return collectedValues_;
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testHtmlAttributeChangeListener_AddAttribute() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    p1.setAttribute('title', 'myTitle');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody'>\n"
            + "<p id='p1'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";

        final String[] expectedValues =
        {"attributeAdded: p,title,myTitle",
            "attributeAdded: p,title,myTitle",
            "attributeAdded: p,title,myTitle"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlBody body = page.getHtmlElementById("myBody");
        final HtmlElement p1 = page.getHtmlElementById("p1");

        final HtmlAttributeChangeListenerTestImpl listenerImpl = new HtmlAttributeChangeListenerTestImpl();
        p1.addHtmlAttributeChangeListener(listenerImpl);
        body.addHtmlAttributeChangeListener(listenerImpl);
        page.addHtmlAttributeChangeListener(listenerImpl);
        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");

        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testHtmlAttributeChangeListener_ReplaceAttribute() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    p1.setAttribute('title', p1.getAttribute('title') + 'a');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody'>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";

        final String[] expectedValues =
        {"attributeReplaced: p,title,myTitle",
            "attributeReplaced: p,title,myTitle",
            "attributeReplaced: p,title,myTitle"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlBody body = page.getHtmlElementById("myBody");
        final HtmlElement p1 = page.getHtmlElementById("p1");
        final HtmlAttributeChangeListenerTestImpl listenerImpl = new HtmlAttributeChangeListenerTestImpl();
        page.addHtmlAttributeChangeListener(listenerImpl);
        body.addHtmlAttributeChangeListener(listenerImpl);
        p1.addHtmlAttributeChangeListener(listenerImpl);
        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");

        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
        assertEquals("myTitle" + 'a', p1.getAttribute("title"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testHtmlAttributeChangeListener_RemoveAttribute() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    p1.removeAttribute('title');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody'>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";

        final String[] expectedValues =
        {"attributeRemoved: p,title,myTitle",
            "attributeRemoved: p,title,myTitle",
            "attributeRemoved: p,title,myTitle"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlBody body = page.getHtmlElementById("myBody");
        final HtmlElement p1 = page.getHtmlElementById("p1");
        final HtmlAttributeChangeListenerTestImpl listenerImpl = new HtmlAttributeChangeListenerTestImpl();
        page.addHtmlAttributeChangeListener(listenerImpl);
        body.addHtmlAttributeChangeListener(listenerImpl);
        p1.addHtmlAttributeChangeListener(listenerImpl);
        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");

        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
        assertSame(HtmlElement.ATTRIBUTE_NOT_DEFINED, p1.getAttribute("title"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testHtmlAttributeChangeListener_RemoveListener() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    p1.setAttribute('title', p1.getAttribute('title') + 'a');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";

        final String[] expectedValues = {"attributeReplaced: p,title,myTitle"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement p1 = page.getHtmlElementById("p1");
        final HtmlAttributeChangeListenerTestImpl listenerImpl = new HtmlAttributeChangeListenerTestImpl();
        p1.addHtmlAttributeChangeListener(listenerImpl);
        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");

        myButton.click();
        p1.removeHtmlAttributeChangeListener(listenerImpl);
        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
        assertEquals("myTitle" + 'a' + 'a', p1.getAttribute("title"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testMouseOver() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function mouseOverMe() {\n"
            + "    document.getElementById('myTextarea').value+='mouseover-';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onmouseover='mouseOverMe()'>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlBody body = page.getHtmlElementById("myBody");
        body.mouseOver();
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals("mouseover-", textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testMouseMove() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function mouseMoveMe() {\n"
            + "    document.getElementById('myTextarea').value+='mousemove-';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onmousemove='mouseMoveMe()'>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlBody body = page.getHtmlElementById("myBody");
        body.mouseMove();
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals("mousemove-", textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testMouseOut() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function mouseOutMe() {\n"
            + "    document.getElementById('myTextarea').value+='mouseout-';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onmouseout='mouseOutMe()'>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlBody body = page.getHtmlElementById("myBody");
        body.mouseOut();
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals("mouseout-", textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testMouseDown() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function mouseDownMe(e) {\n"
            + "    document.getElementById('myTextarea').value+='mousedown-' + e.button;\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onmousedown='mouseDownMe(event)'>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final String expected = getBrowserVersion().isFirefox() ? "mousedown-0" : "mousedown-1";

        final HtmlPage page = loadPage(html);
        final HtmlBody body = page.getHtmlElementById("myBody");
        body.mouseDown();
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals(expected, textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testMouseUp() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function mouseUpMe() {\n"
            + "    document.getElementById('myTextarea').value+='mouseup-';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onmouseup='mouseUpMe()'>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlBody body = page.getHtmlElementById("myBody");
        body.mouseUp();
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals("mouseup-", textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testRightClick() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function divMouseEvent(e) {\n"
            + "    var textarea = document.getElementById('myTextarea');\n"
            + "    if (window.event)\n"
            + "      textarea.value += event.type + '-' + event.button + '-';\n"
            + "    else\n"
            + "      textarea.value += e.type + '-' + e.which + '-';\n"
            + "  }\n"
            + "  function loadFunction(e) {\n"
            + "     document.getElementById('myDiv').onmousedown   = divMouseEvent;\n"
            + "     document.getElementById('myDiv').onmouseup     = divMouseEvent;\n"
            + "     document.getElementById('myDiv').oncontextmenu = divMouseEvent;\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='loadFunction()'>\n"
            + "  <div id='myDiv'>Hello</div><br>\n"
            + "  <textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final String expected = getBrowserVersion().isFirefox() ? "mousedown-3-mouseup-3-contextmenu-3-"
            : "mousedown-2-mouseup-2-contextmenu-0-";

        final HtmlPage page = loadPage(html);
        final HtmlDivision div = page.getHtmlElementById("myDiv");
        div.rightClick();
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals(expected, textArea.getText());
    }

    /**
     * Test the mouse down, then mouse up.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testMouse_Down_Up() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function divMouseEvent(e) {\n"
            + "    var textarea = document.getElementById('myTextarea');\n"
            + "    if (window.event)\n"
            + "      textarea.value += event.type + '-' + event.button + '-';\n"
            + "    else\n"
            + "      textarea.value += e.type + '-' + e.which + '-';\n"
            + "  }\n"
            + "  function loadFunction(e) {\n"
            + "     document.getElementById('myDiv').onmousedown=divMouseEvent;\n"
            + "     document.getElementById('myDiv').onmouseup  =divMouseEvent;\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='loadFunction()'>\n"
            + "  <div id='myDiv'>Hello</div><br>\n"
            + "  <textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final String expected = "mousedown-1-mouseup-1-";

        final HtmlPage page = loadPage(html);
        final HtmlDivision div = page.getHtmlElementById("myDiv");
        div.mouseDown();
        div.mouseUp();
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals(expected, textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml_separateLineforEmptyElements() throws Exception {
        final String html = "<html><head><title>foo</title></head>\n"
            + "<body><table><tr><td></tr></table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        assertTrue(page.asXml().indexOf("/> ") == -1);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetElementsByTagName() throws Exception {
        final String html
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "<div>a</div> <div>b</div> <div>c</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlElement body = page.getBody();

        final NodeList inputs = body.getElementsByTagName("input");
        assertEquals(1, inputs.getLength());
        assertEquals("button", inputs.item(0).getAttributes().getNamedItem("type").getNodeValue());

        final NodeList divs = body.getElementsByTagName("div");
        assertEquals(3, divs.getLength());

        final HtmlDivision newDiv = new HtmlDivision(null, HtmlDivision.TAG_NAME, page, null);
        body.appendChild(newDiv);
        assertEquals(4, divs.getLength());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void type() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myInput').value);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <input id='myButton' type='button' onclick='test()'>\n"
            + "  <input id='myInput' onclick='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"Hello Cruel World"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, collectedAlerts);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        input.type("Hello Cruel World");
        assertEquals("Hello Cruel World", input.getValueAttribute());
        page.<HtmlButtonInput>getHtmlElementById("myButton").click();
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "value")
    public void onpropertychange() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    document.getElementById('input1').value = 'New Value';\n"
            + "  }\n"
            + "  function handler() {\n"
            + "    alert(event.propertyName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='input1' onpropertychange='handler()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeOnFocus() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form>\n"
            + "    <input type='text' id='textfield1' onfocus='alert(1)'>\n"
            + "</form>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"1"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, collectedAlerts);

        page.<HtmlElement>getHtmlElementById("textfield1").type('a');
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test attribute.text and attribute.xml added for XmlElement attributes
     * are undefined for HtmlElement.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "type", "undefined", "undefined" })
    public void testTextAndXmlUndefined() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "    <input type='text' id='textfield1' onfocus='alert(1)'>\n"
            + "    <script>\n"
            + "         var node = document.getElementById('textfield1');\n"
            + "         alert(node.attributes[0].nodeName);\n"
            + "         alert(node.attributes[0].text);\n"
            + "         alert(node.attributes[0].xml);\n"
            + "    </script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void asText() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "</head>\n"
            + "<body>Welcome\n"
            + "<div style='visibility:hidden'>to the big</div>\n"
            + "<div style='display:none'>\n"
            + "  <div style='display:block'><span style='visibility:visible'>world</span></div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        assertEquals("test" + LINE_SEPARATOR + "Welcome", page.asText());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void asTextOverridingVisibility() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "</head>\n"
            + "<body>Welcome\n"
            + "<p style='visibility:hidden'>hidden text\n"
            + "<FONT COLOR='#FF0000' style='visibility:visible'>to the world</FONT>\n"
            + "some more hidden text</p>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        assertEquals("test" + LINE_SEPARATOR + "Welcome" + LINE_SEPARATOR + "to the world", page.asText());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void asTextVisibilityCollapse() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "</head>\n"
            + "<body>Welcome\n"
            + "<p style='visibility:collapse'>hidden text\n"
            + "<FONT COLOR='#FF0000' style='visibility:visible'>to the world</FONT>\n"
            + "some more hidden text</p>\n"
            + "</body>\n"
            + "</html>";

        final String expected = getBrowserVersion().isFirefox()
            ? "test" + LINE_SEPARATOR + "Welcome" + LINE_SEPARATOR + "to the world"
            : "test" + LINE_SEPARATOR + "Welcome" + LINE_SEPARATOR + "hidden text to the world some more hidden text";

        final HtmlPage page = loadPage(html);
        assertEquals(expected, page.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getNodeName() throws Exception {
        final String html
            = "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:app='http://www.appcelerator.org'>\n"
            + "<script>\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<dIv id='dIv1'></dIv>\n"
            + "<app:dIv id='dIv2'></app:dIv>\n"
            + "<another:dIv id='dIv3'></another:dIv>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals("div", page.<HtmlElement>getHtmlElementById("dIv1").getNodeName());
        assertEquals("app:div", page.<HtmlElement>getHtmlElementById("dIv2").getNodeName());
        assertEquals("another:div", page.<HtmlElement>getHtmlElementById("dIv3").getNodeName());
        assertTrue(page.asXml().contains("<app:div "));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2" })
    public void getElementsByTagName() throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var form = document.getElementById('myForm');\n"
            + "    alert(form.getElementsByTagName('input').length);\n"
            + "    alert(document.body.getElementsByTagName('input').length);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form id='myForm'>\n"
            + "  <input type='button' name='button1' value='pushme'>\n"
            + "</form>\n"
            + "<input type='button' name='button2'>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals(1, page.getElementById("myForm").getElementsByTagName("input").getLength());
        assertEquals(2, page.getBody().getElementsByTagName("input").getLength());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "true" })
    public void duplicateId() throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var value = document.getElementById('duplicateID').innerHTML;\n"
            + "    alert(value.length > 10);\n"
            + "    document.getElementById('duplicateID').style.display = 'block';\n"
            + "    alert(value === document.getElementById('duplicateID').innerHTML);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <fieldset id='duplicateID'><span id='duplicateID'></span></fieldset>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String html = "<html><body><div id='d' a='b'></div></body></html>";
        HtmlPage page = loadPage(html);
        assertEquals("b", page.getElementById("d").getAttribute("a"));
        page = clone(page);
        assertEquals("b", page.getElementById("d").getAttribute("a"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = { "1", "1" })
    public void onpropertychange2() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    document.getElementById('input1').value = 'New Value';\n"
            + "  }\n"
            + "  function handler() {\n"
            + "    alert(1);\n"
            + "    document.getElementById('input1').dir='rtl';"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='input1' onpropertychange='handler()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Ensure that we don't escape when not needed.
     * @throws Exception on test failure
     */
    @Test
    public void asXml() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "</head>\n"
            + "<body>Welcome\n"
            + "<div id='div1' onclick=\"alert('hello')\">click me</div>\n"
            + "<div id='div2' onclick='alert(\"hello again\")'>click me again</div>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);

        final String htmlDiv1XML = "<div id=\"div1\" onclick=\"alert('hello')\">" + LINE_SEPARATOR
            + "  click me" + LINE_SEPARATOR
            + "</div>" + LINE_SEPARATOR;
        assertEquals(htmlDiv1XML, page.getElementById("div1").asXml());

        final String htmlDiv2XML = "<div id=\"div2\" onclick=\"alert(&quot;hello again&quot;)\">" + LINE_SEPARATOR
            + "  click me again" + LINE_SEPARATOR
            + "</div>" + LINE_SEPARATOR;
        assertEquals(htmlDiv2XML, page.getElementById("div2").asXml());
    }

}
