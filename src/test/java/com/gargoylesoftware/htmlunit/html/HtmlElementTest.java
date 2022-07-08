/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link HtmlElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Denis N. Antonioli
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlElementTest extends SimpleWebTestCase {

    /**
     * Test hasAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void hasAttributeWith() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        assertTrue("Element should have attribute", node.hasAttribute("id"));
    }

    /**
     * Test hasAttribute() on an element with the attribute but without a value.
     * @throws Exception if the test fails
     */
    @Test
    public void hasAttributeWithMissingValue() throws Exception {
        final String html = "<html><head></head><body id='tag' attrib>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        assertTrue("Element should have attribute", node.hasAttribute("attrib"));
    }

    /**
     * Test hasAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void hasAttributeNone() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        assertFalse("Element should not have attribute", node.hasAttribute("foo"));
    }

    /**
     * Test hasAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void hasAttributeNSWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        assertTrue("Element should have attribute", node.hasAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test hasAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void hasAttributeNSNone() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        assertFalse("Element should not have attribute", node.hasAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test getAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeWith() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        assertEquals("Element should have attribute", "tag", node.getId());
    }

    /**
     * Test getAttribute() on an element with the attribute but without a value.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeWithMissingValue() throws Exception {
        final String html = "<html><head></head><body id='tag' attrib>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        assertEquals("", node.getAttribute("attrib"));
        assertTrue(DomElement.ATTRIBUTE_VALUE_EMPTY == node.getAttribute("attrib"));
    }

    /**
     * Test getAttribute() on an element with the attribute but without a value.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeWithEmptyValue() throws Exception {
        final String html = "<html><head></head><body id='tag' attrib=''>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        assertEquals("", node.getAttribute("attrib"));
        assertTrue(DomElement.ATTRIBUTE_VALUE_EMPTY == node.getAttribute("attrib"));
    }

    /**
     * Test getAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeNone() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        assertEquals("Element should not have attribute", "", node.getAttribute("foo"));
    }

    /**
     * Test getAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeNSWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        assertEquals("Element should have attribute", "bar", node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test getAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeNSNone() throws Exception {
        final String html = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        assertEquals("Element should not have attribute", "", node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test getNamespaceURI on an attribute that has a namespace.
     * @throws Exception if the test fails
     */
    @Test
    public void getNamespaceURIWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("ns:foo".equals(attr.getName())) {
                assertEquals("Element should have a namespace URI", "http://foobar", attr.getNamespaceURI());
                return;
            }
        }
        fail("Attribute ns:foo not found.");
    }

    /**
     * Test getNamespaceURI on an attribute that has a namespace.
     * @throws Exception if the test fails
     */
    @Test
    public void getNamespaceURINone() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("id".equals(attr.getName())) {
                assertEquals("Element should not have a namespace URI", null, attr.getNamespaceURI());
                return;
            }
        }
        fail("Attribute ns:foo not found.");
    }

    /**
     * Test getLocalName on an attribute that has a local name.
     * @throws Exception if the test fails
     */
    @Test
    public void getLocalNameWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("ns:foo".equals(attr.getName())) {
                assertEquals("Element should have a local name", "foo", attr.getLocalName());
                return;
            }
        }
        fail("Attribute ns:foo not found.");
    }

    /**
     * Test getLocalName on an attribute that has a local name.
     * @throws Exception if the test fails
     */
    @Test
    public void getLocalNameNone() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("id".equals(attr.getName())) {
                // This is not standard, but to change it now would break backwards compatibility.
                assertEquals("Element should not have a local name", "id", attr.getLocalName());
                return;
            }
        }
        fail("Attribute ns:foo not found.");
    }

    /**
     * Test getPrefix on an attribute that has a prefix.
     * @throws Exception if the test fails
     */
    @Test
    public void getPrefixWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("ns:foo".equals(attr.getName())) {
                assertEquals("Element should have a prefix", "ns", attr.getPrefix());
                return;
            }
        }
        fail("Attribute ns:foo not found.");
    }

    /**
     * Test getPrefix on an attribute that has a prefix.
     * @throws Exception if the test fails
     */
    @Test
    public void getPrefixNone() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("id".equals(attr.getName())) {
                assertEquals("Element should not have a prefix", null, attr.getPrefix());
                return;
            }
        }
        fail("Attribute ns:foo not found.");
    }

    /**
     * Test setPrefix on an attribute that has a prefix.
     * @throws Exception if the test fails
     */
    @Test
    public void setPrefix() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        for (final DomAttr attr : node.getAttributesMap().values()) {
            if ("ns:foo".equals(attr.getName())) {
                attr.setPrefix("other");
                assertEquals("Element should have a changed prefix", "other", attr.getPrefix());
                assertEquals("setPrefix should change qualified name", "other:foo", attr.getName());
                return;
            }
        }
        fail("Attribute ns:foo not found.");
    }

    /**
     * Test setAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttributeWith() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        node.setAttribute("id", "other");
        assertEquals("Element should have attribute", "other", node.getId());
    }

    /**
     * Test setAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttributeNone() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        node.setAttribute("foo", "other");
        assertEquals("Element should have attribute", "other", node.getAttribute("foo"));
    }

    /**
     * Test setAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttributeNSWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        node.setAttributeNS("http://foobar", "ns:foo", "other");
        assertEquals("Element should have attribute", "other", node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test setAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttributeNSNone() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        node.setAttributeNS("http://foobar", "ns:foo", "other");
        assertEquals("Element should not have attribute", "other", node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test removeAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void removeAttributeWith() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        node.removeAttribute("id");
        assertEquals("Element should not have removed attribute", "", node.getId());
    }

    /**
     * Test removeAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void removeAttributeNone() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        node.removeAttribute("foo");
        assertEquals("Element should not have attribute", "", node.getAttribute("foo"));
    }

    /**
     * Test removeAttribute() on an element with the attribute.
     * @throws Exception if the test fails
     */
    @Test
    public void removeAttributeNSWith() throws Exception {
        final String html
            = "<html><head></head><body xmlns:ns='http://foobar' id='tag' ns:foo='bar'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        node.removeAttributeNS("http://foobar", "foo");
        assertEquals("Element should not have removed attribute", "",
            node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * Test removeAttribute() on an element without the attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void removeAttributeNSNone() throws Exception {
        final String html
            = "<html><head></head><body id='tag'>text</body></html>";
        final HtmlPage page = loadPage(html);

        final HtmlElement node = page.getHtmlElementById("tag");
        node.removeAttributeNS("http://foobar", "foo");
        assertEquals("Element should not have attribute", "", node.getAttributeNS("http://foobar", "foo"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getEnclosingForm() throws Exception {
        final String htmlContent =
            "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<table><tr><td><input type='text' id='foo'/></td></tr></table>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlInput input = page.getHtmlElementById("foo");
        assertSame(form, input.getEnclosingForm());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getEnclosing() throws Exception {
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
    public void asNormalizedTextWithComments() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<p id='p1'>foo<!--bar--></p>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement element = page.getHtmlElementById("p1");
        assertEquals("foo", element.asNormalizedText());
    }

    /**
     * Tests constants.
     */
    @Test
    public void constants() {
        assertEquals("", DomElement.ATTRIBUTE_NOT_DEFINED);
        assertEquals("", DomElement.ATTRIBUTE_VALUE_EMPTY);
        assertTrue("Not the same object",
                DomElement.ATTRIBUTE_NOT_DEFINED != DomElement.ATTRIBUTE_VALUE_EMPTY);
    }

    static class HtmlAttributeChangeListenerTestImpl implements HtmlAttributeChangeListener {
        private final List<String> collectedValues_ = new ArrayList<>();
        @Override
        @Test
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            collectedValues_.add("attributeAdded: " + event.getHtmlElement().getTagName() + ','
                    + event.getName() + ',' + event.getValue());
        }
        @Override
        @Test
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            collectedValues_.add("attributeRemoved: " + event.getHtmlElement().getTagName() + ','
                    + event.getName() + ',' + event.getValue());
        }

        @Override
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
    public void htmlAttributeChangeListener_AddAttribute() throws Exception {
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
    public void htmlAttributeChangeListener_ReplaceAttribute() throws Exception {
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
    public void htmlAttributeChangeListener_RemoveAttribute() throws Exception {
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
        assertSame(DomElement.ATTRIBUTE_NOT_DEFINED, p1.getAttribute("title"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void htmlAttributeChangeListener_RemoveListener() throws Exception {
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
    public void mouseOver() throws Exception {
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
    public void mouseMove() throws Exception {
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
    public void mouseOut() throws Exception {
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
    @Alerts("mousedown-0")
    public void mouseDown() throws Exception {
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

        final HtmlPage page = loadPage(html);
        final HtmlBody body = page.getHtmlElementById("myBody");
        body.mouseDown();
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals(getExpectedAlerts()[0], textArea.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void mouseUp() throws Exception {
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
    @Alerts("mousedown-2-mouseup-2-contextmenu-2-")
    public void rightClick() throws Exception {
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
            + "    document.getElementById('myDiv').onmousedown   = divMouseEvent;\n"
            + "    document.getElementById('myDiv').onmouseup     = divMouseEvent;\n"
            + "    document.getElementById('myDiv').oncontextmenu = divMouseEvent;\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='loadFunction()'>\n"
            + "  <div id='myDiv'>Hello</div><br>\n"
            + "  <textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlDivision div = page.getHtmlElementById("myDiv");
        div.rightClick();
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals(getExpectedAlerts()[0], textArea.getText());
    }

    /**
     * Test the mouse down, then mouse up.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("mousedown-0-mouseup-0-")
    public void mouse_Down_Up() throws Exception {
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
            + "    document.getElementById('myDiv').onmousedown = divMouseEvent;\n"
            + "    document.getElementById('myDiv').onmouseup   = divMouseEvent;\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='loadFunction()'>\n"
            + "  <div id='myDiv'>Hello</div><br>\n"
            + "  <textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlDivision div = page.getHtmlElementById("myDiv");
        div.mouseDown();
        div.mouseUp();
        final HtmlTextArea textArea = page.getHtmlElementById("myTextarea");
        assertEquals(getExpectedAlerts()[0], textArea.getText());
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
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(html, collectedAlerts);
        final HtmlTextInput input = page.getHtmlElementById("myInput");
        input.type("Hello Cruel World");
        assertEquals("Hello Cruel World", input.getValueAttribute());
        assertEquals("Hello Cruel World", input.getValue());
        page.getHtmlElementById("myButton").click();
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeOnFocus() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form>\n"
            + "  <input type='text' id='textfield1' onfocus='alert(1)'>\n"
            + "</form>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"1"};
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(html, collectedAlerts);

        page.getHtmlElementById("textfield1").type('a');
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void asNormalizedText() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "</head>\n"
            + "<body>Welcome\n"
            + "<div style='visibility:hidden'>to the big</div>\n"
            + "<div style='display:none'>\n"
            + "  <div style='display:block'><span style='visibility:visible'>world</span></div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        assertEquals("test\nWelcome", page.asNormalizedText());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void asNormalizedTextOverridingVisibility() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "</head>\n"
            + "<body>Welcome\n"
            + "<p style='visibility:hidden'>hidden text\n"
            + "<FONT COLOR='#FF0000' style='visibility:visible'>to the world</FONT>\n"
            + "some more hidden text</p>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);
        assertEquals("test\nWelcome\nto the world", page.asNormalizedText());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void asNormalizedTextVisibilityCollapse() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "</head>\n"
            + "<body>Welcome\n"
            + "<p style='visibility:collapse'>hidden text\n"
            + "<font color='#FF0000' style='visibility:visible'>to the world</font>\n"
            + "some more hidden text</p>\n"
            + "</body>\n"
            + "</html>";
        final String expected = "test\nWelcome\nto the world";

        final HtmlPage page = loadPage(html);
        assertEquals(expected, page.asNormalizedText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getNodeName() throws Exception {
        final String html
            = "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:app='http://www.appcelerator.org'>\n"
            + "<head>\n"
            + "<script>\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<dIv id='dIv1'></dIv>\n"
            + "<app:dIv id='dIv2'></app:dIv>\n"
            + "<another:dIv id='dIv3'></another:dIv>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals("div", page.getHtmlElementById("dIv1").getNodeName());
        assertEquals("app:div", page.getHtmlElementById("dIv2").getNodeName());
        assertEquals("another:div", page.getHtmlElementById("dIv3").getNodeName());
        assertTrue(page.asXml().contains("<app:div "));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2"})
    public void getElementsByTagName() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
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
     * @throws Exception if an error occurs
     */
    @Test
    public void getElementsByTagName2() throws Exception {
        final String html
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "<div>a</div> <div>b</div> <div>c</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlElement body = page.getBody();

        NodeList inputs = body.getElementsByTagName("input");
        assertEquals(1, inputs.getLength());
        assertEquals("button", inputs.item(0).getAttributes().getNamedItem("type").getNodeValue());

        final NodeList divs = body.getElementsByTagName("div");
        assertEquals(3, divs.getLength());

        final HtmlDivision newDiv = new HtmlDivision(HtmlDivision.TAG_NAME, page, null);
        body.appendChild(newDiv);
        assertEquals(4, divs.getLength());

        // case sensitive
        inputs = page.getElementsByTagName("inPUT");
        assertEquals(1, inputs.getLength());

        // empty
        inputs = page.getElementsByTagName("");
        assertEquals(0, inputs.getLength());

        // null
        inputs = page.getElementsByTagName(null);
        assertEquals(0, inputs.getLength());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByAttribute() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='myForm'>\n"
            + "  <input type='button' name='buttonName' value='pushme'>\n"
            + "  <select id='selectId' multiple>\n"
            + "    <option value='option1' id='option1' selected>Option1</option>\n"
            + "    <option value='option2' id='option2' selected='selected'>Option2</option>\n"
            + "  </select>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final HtmlElement form = page.getHtmlElementById("myForm");

        List<HtmlElement> elements = form.getElementsByAttribute("input", "value", "pushme");
        assertEquals(1, elements.size());
        assertEquals("<input type=\"button\" name=\"buttonName\" value=\"pushme\"/>",
            elements.get(0).asXml().replaceAll("\\r|\\n", ""));

        // ignore case
        elements = form.getElementsByAttribute("iNPuT", "value", "pushme");
        assertEquals(1, elements.size());
        assertEquals("<input type=\"button\" name=\"buttonName\" value=\"pushme\"/>",
                elements.get(0).asXml().replaceAll("\\r|\\n", ""));

        // attribute value is case sensitive
        elements = form.getElementsByAttribute("input", "value", "pushMe");
        assertTrue(elements.isEmpty());

        // selected='selected'
        elements = form.getElementsByAttribute("option", "selected", "selected");
        assertEquals(1, elements.size());
        assertEquals("<option value=\"option2\" id=\"option2\" selected=\"selected\">  Option2</option>",
                elements.get(0).asXml().replaceAll("\\r|\\n", ""));

        // selected
        elements = form.getElementsByAttribute("option", "selected", "");
        assertEquals(1, elements.size());
        assertEquals("<option value=\"option1\" id=\"option1\" selected=\"\">  Option1</option>",
                elements.get(0).asXml().replaceAll("\\r|\\n", ""));
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
     * Ensure that we don't escape when not needed.
     * @throws Exception on test failure
     */
    @Test
    public void asXml() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "</head>\n"
            + "<body>Welcome\n"
            + "<div id='div1' onclick=\"alert('hello')\">click me</div>\n"
            + "<div id='div2' onclick='alert(\"hello again\")'>click me again</div>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(html);

        final String htmlDiv1XML = "<div id=\"div1\" onclick=\"alert('hello')\">\r\n  click me"
                + "\r\n</div>\r\n";
        assertEquals(htmlDiv1XML, page.getElementById("div1").asXml());

        final String htmlDiv2XML = "<div id=\"div2\" onclick=\"alert(&quot;hello again&quot;)\">\r\n  click me again"
                + "\r\n</div>\r\n";
        assertEquals(htmlDiv2XML, page.getElementById("div2").asXml());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void isDisplayed() throws Exception {
        final String html = "<html><head>\n"
            + "</head>\n"
            + "</body>\n"
            + "<div id='d1'>hello</div>\n"
            + "<div id='d2' hidden>world</div>\n"
            + "</body></html>";

        getWebClient().getOptions().setJavaScriptEnabled(false);
        final HtmlPage page = loadPage(html);
        assertTrue(page.getElementById("d1").isDisplayed());
        assertEquals(Boolean.parseBoolean(getExpectedAlerts()[0]), page.getElementById("d2").isDisplayed());
    }
}
