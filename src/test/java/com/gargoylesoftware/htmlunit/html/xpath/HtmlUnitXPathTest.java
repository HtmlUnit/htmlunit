/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.xpath;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for XPath evaluation on HtmlUnit DOM.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlUnitXPathTest extends SimpleWebTestCase {

    /**
     * Test evaluation of some simple paths.
     * @throws Exception if test fails
     */
    @Test
    public void simplePath() throws Exception {
        final String content = "<html><head><title>Test page</title></head>\n"
            + "<body><a href='foo.html' id='myLink'>foo</a></body>\n"
            + "</html>";

        final HtmlPage page = loadPage(content);
        assertEquals(page.getDocumentElement(), page.getFirstByXPath("/html"));
        assertEquals(page.getDocumentElement().getFirstChild(), page.getFirstByXPath("/html/head"));
        assertEquals(page.getHtmlElementById("myLink"), page.getFirstByXPath("/html/body/a"));
        assertEquals("Test page", ((DomText) page.getFirstByXPath("/html/head/title/text()")).getNodeValue());
    }

    /**
     * Test evaluation relative from elements other than the whole page.
     * @throws Exception if test fails
     */
    @Test
    public void xpathFromElement() throws Exception {
        final String content = "<html><head><title>Test page</title></head>\n"
            + "<body><a href='foo.html' id='myLink'>foo</a></body>\n"
            + "</html>";

        final HtmlPage page = loadPage(content);
        final HtmlBody body = page.getFirstByXPath("/html/body");

        assertEquals(page.getHtmlElementById("myLink"), body.getFirstByXPath("./a"));
    }

    /**
     * Test that the elements are in the right order.
     * @throws Exception if test fails
     */
    @Test
    @SuppressWarnings("unchecked")
    public void elementOrder() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "</script></head><body>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        final List<?> list = page.getByXPath("//*");

        final String[] expected = {"html", "head", "title", "script", "body"};
        final List<String> actualNames = new ArrayList<String>();
        for (final DomNode node : (List<DomNode>) list) {
            actualNames.add(node.getNodeName());
        }
        assertEquals(expected, actualNames);
    }

    /**
     * Test evaluation of paths after they're changed through JavaScript.
     * @throws Exception if test fails
     */
    @Test
    public void whenJSChangesPage() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function addOption() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    var index = options.length;\n"
            + "    options[index] = new Option('Four','value4');\n"
            + "}</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option name='option1' value='value1'>One</option>\n"
            + "        <option name='option2' value='value2' selected>Two</option>\n"
            + "        <option name='option3' value='value3'>Three</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "<a href='javascript:addOption()'>add option</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        assertEquals("foo", page.getTitleText());

        assertEquals(3, page.<Number>getFirstByXPath("count(//select[@name='select1']/option)").intValue());

        page.getAnchors().get(0).click();
        assertEquals(4, page.<Number>getFirstByXPath("count(//select[@name='select1']/option)").intValue());
    }

    /**
     * Tests XPath where results are attributes.
     * @throws Exception if test fails
     */
    @Test
    @SuppressWarnings("unchecked")
    public void listAttributesResult() throws Exception {
        final String content
            = "<html><body>\n"
            + "<img src='1.png'>\n"
            + "<img src='2.png'>\n"
            + "<img src='3.png'>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);

        final List<?> nameList = page.getByXPath("//img/@src");
        final List<?> valueList = new ArrayList<Object>(nameList);

        final String[] expectedNames = {"src", "src", "src"};

        final List<String> collectedNames = new ArrayList<String>();
        for (final DomNode node : (List<DomNode>) nameList) {
            collectedNames.add(node.getNodeName());
        }
        assertEquals(expectedNames, collectedNames);

        final String[] expectedValues = {"1.png", "2.png", "3.png"};
        final List<String> collectedValues = new ArrayList<String>();
        for (final DomNode node : (List<DomNode>) valueList) {
            collectedValues.add(node.getNodeValue());
        }
        assertEquals(expectedValues, collectedValues);
    }

    /**
     * Test if option/text() is cleaned like other text().
     * @throws Exception if test fails
     */
    @Test
    public void optionText_getFirstByXPath() throws Exception {
        final String content = "<html><head><title>Test page</title></head>\n"
            + "<body><form name='foo'>\n"
            + "<select name='test'><option value='1'>foo&nbsp;and&nbsp;foo</option></select>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(content);
        final String value = page.getFirstByXPath("string(//option)");
        final int[] expectedValues = {102, 111, 111, 160, 97, 110, 100, 160, 102, 111, 111};
        int index = 0;
        for (final int v : expectedValues) {
            if (value.codePointAt(index++) != v) {
                fail();
            }
        }
    }

    /**
     * Regression test for http://sourceforge.net/p/htmlunit/bugs/365/.
     * @throws Exception if test fails
     */
    @Test
    public void followingAxis() throws Exception {
        final String content = "<html><title>XPath tests</title><body>\n"
            + "<table id='table1'>\n"
            + "<tr id='tr1'>\n"
            + "<td id='td11'>a3</td>\n"
            + "<td id='td12'>c</td>\n"
            + "</tr>\n"
            + "<tr id='tr2'>\n"
            + "<td id='td21'>a4</td>\n"
            + "<td id='td22'>c</td>\n"
            + "</tr>\n"
            + "</table>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        final HtmlElement td12 = page.getHtmlElementById("td12");
        final HtmlElement tr2 = page.getHtmlElementById("tr2");
        final HtmlElement td21 = page.getHtmlElementById("td21");
        final HtmlElement td22 = page.getHtmlElementById("td22");
        xpath(page, "//*[contains(.,'a4')]/following::td[.='c']", new Object[] {td22});

        xpath(page, "//body/following::*", new Object[] {});
        xpath(page, "//html/following::*", new Object[] {});
        xpath(page, "//table/following::*", new Object[] {});
        xpath(page, "//td[@id='td11']/following::*", new Object[] {td12, tr2, td21, td22});
    }

    private void xpath(final HtmlPage page, final String xpathExpr, final Object[] expectedNodes) throws Exception {
        assertEquals(Arrays.asList(expectedNodes), page.getByXPath(xpathExpr));
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void id() throws Exception {
        final String content = "<html><head><title>foo</title></head>\n"
            + "<body>\n"
            + "<div>\n"
            + "  <a href='link.html' id='test'>\n"
            + "</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);

        final HtmlAnchor anchor = page.getHtmlElementById("test");
        Assert.assertSame(anchor, page.getFirstByXPath("//a[@id='test']"));
        Assert.assertSame(anchor, page.getFirstByXPath("//*[@id='test']"));

        assertNull(page.getFirstByXPath("//div[@id='doesNotExist']"));
        assertNull(page.getFirstByXPath("id('doesNotExist')"));
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void changingAttributes() throws Exception {
        final String content = "<html><head><title>foo</title></head>\n"
            + "<body>\n"
            + "<div id='testDiv' title='foo'></div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        final HtmlDivision div = page.getHtmlElementById("testDiv");

        Assert.assertSame(div, page.getFirstByXPath("//*[@title = 'foo']"));
        assertNull(page.getFirstByXPath("//*[@class = 'design']"));

        div.setAttribute("class", "design");
        Assert.assertSame(div, page.getFirstByXPath("//*[@class = 'design']"));
    }
}
