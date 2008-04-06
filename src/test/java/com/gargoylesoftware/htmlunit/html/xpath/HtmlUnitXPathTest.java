/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.html.xpath;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for XPath evaluation on HtmlUnit DOM.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlUnitXPathTest extends WebTestCase {

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
        assertEquals(page.getDocumentHtmlElement(), page.getFirstByXPath("/html"));
        assertEquals(page.getDocumentHtmlElement().getFirstChild(), page.getFirstByXPath("/html/head"));
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
        final HtmlBody body = (HtmlBody) page.getFirstByXPath("/html/body");

        assertEquals((HtmlAnchor) page.getHtmlElementById("myLink"), (HtmlAnchor) body.getFirstByXPath("./a"));
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
        final List< ? > list = page.getByXPath("//*");

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

        assertEquals(3, ((Double) page.getFirstByXPath("count(//select[@name='select1']/option)")).intValue());

        page.getAnchors().get(0).click();
        assertEquals(4, ((Double) page.getFirstByXPath("count(//select[@name='select1']/option)")).intValue());
    }

    /**
     * Tests xpath where results are attributes.
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

        final List< ? > nameList = page.getByXPath("//img/@src");
        final List< ? > valueList = new ArrayList<Object>(nameList);

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
     * @throws Exception if the test fails
     */
    @Test
    public void optionText() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var expr = 'string(//option)';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    var value = result.stringValue;\n"
            + "    for (i=0; i < value.length; i++) {\n"
            + "      alert(value.charCodeAt(i));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <select name='test'><option value='1'>foo&nbsp;and&nbsp;foo</option></select>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"102", "111", "111", "160", "97", "110", "100", "160", "102", "111", "111"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
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
        final String value = (String) page.getFirstByXPath("string(//option)");
        final int[] expectedValues = {102, 111, 111, 160, 97, 110, 100, 160, 102, 111, 111};
        int index = 0;
        for (final int v : expectedValues) {
            if (value.codePointAt(index++) != v) {
                fail();
            }
        }
    }

    /**
     * Regression test for https://sf.net/tracker/index.php?func=detail&aid=1527799&group_id=47038&atid=448266.
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
            + "<div><a href='link.html' id='test'></div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);

        assertNull(page.getFirstByXPath("//div[@id='doesNotExist']"));

        assertNull(page.getFirstByXPath("id('doesNotExist')"));
    }
}
