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
package com.gargoylesoftware.htmlunit.html.xpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.jaxen.BaseXPath;
import org.jaxen.Navigator;
import org.jaxen.XPath;

import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
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
     * Create an instance
     *
     * @param name The name of the test
     */
    public HtmlUnitXPathTest(final String name) {
        super(name);
    }

    /**
     * Test evaluation of some simple paths
     * @throws Exception if test fails
     */
    public void testSimplePath() throws Exception {
        final String content = "<html><head><title>Test page</title></head>\n"
            + "<body><a href='foo.html' id='myLink'>foo</a></body>\n"
            + "</html>";

        final HtmlPage page = loadPage(content);
        HtmlUnitXPath xpath = new HtmlUnitXPath("/html");
        assertEquals(page.getDocumentHtmlElement(), xpath.selectSingleNode(page));
        xpath = new HtmlUnitXPath("/html/head");
        assertEquals(page.getDocumentHtmlElement().getFirstDomChild(), xpath.selectSingleNode(page));
        xpath = new HtmlUnitXPath("/html/body/a");
        assertEquals(page.getHtmlElementById("myLink"), xpath.selectSingleNode(page));
        xpath = new HtmlUnitXPath("/html/head/title/text()");
        assertEquals("Test page", xpath.stringValueOf(page));
    }

    /**
     * Test evaluation relative from elements other than the whole page
     * @throws Exception if test fails
     */
    public void testXPathFromElement() throws Exception {
        final String content = "<html><head><title>Test page</title></head>\n"
            + "<body><a href='foo.html' id='myLink'>foo</a></body>\n"
            + "</html>";

        final HtmlPage page = loadPage(content);
        XPath xpath = new HtmlUnitXPath("/html/body");
        final HtmlBody body = (HtmlBody) xpath.selectSingleNode(page);

        final Navigator relativeNavigator = HtmlUnitXPath.buildSubtreeNavigator(body);
        xpath = new BaseXPath("/a", relativeNavigator);
        assertEquals(page.getHtmlElementById("myLink"), xpath.selectSingleNode(body));
    }

    /**
     * Test that the elements are in the right order (was a bug in Jaxen 1.0-FCS:
     * http://jira.codehaus.org/browse/JAXEN-55)
     * @throws Exception if test fails
     */
    public void testElementOrder() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "</script></head><body>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        final XPath xpath = new HtmlUnitXPath("//*");
        final List list = xpath.selectNodes(page);

        final String[] expected = {"html", "head", "title", "script", "body"};
        final Transformer tagReader = new Transformer() {
            public Object transform(final Object obj) {
                return ((DomNode) obj).getNodeName();
            }
        };
        CollectionUtils.transform(list, tagReader);
        assertEquals(expected, list);
    }

    /**
     * Test evaluation of paths after changed through javascript
     * @throws Exception if test fails
     */
    public void testWhenJSChangesPage() throws Exception {
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

        final HtmlUnitXPath xpath = new HtmlUnitXPath("count(//select[@name='select1']/option)");
        assertEquals(3, ((Double) xpath.evaluate(page)).intValue());

        final HtmlAnchor link = (HtmlAnchor) page.getAnchors().get(0);
        link.click();
        assertEquals(4, ((Double) xpath.evaluate(page)).intValue());

    }

    /**
     * Tests xpath where results are attributes.
     * @throws Exception if test fails
     */
    public void testListAttributesResult() throws Exception {
        final String content
            = "<html><body>\n"
            + "<img src='1.png'>\n"
            + "<img src='2.png'>\n"
            + "<img src='3.png'>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);

        final XPath xpath = new HtmlUnitXPath("//img/@src");
        final List nameList = xpath.selectNodes(page);
        final List valueList = new ArrayList(nameList);

        final String[] expectedName = {"src", "src", "src"};
        final Transformer nameReader = new Transformer() {
            public Object transform(final Object obj) {
                return ((DomNode) obj).getNodeName();
            }
        };
        CollectionUtils.transform(nameList, nameReader);
        assertEquals(expectedName, nameList);

        final String[] expectedValue = {"1.png", "2.png", "3.png"};
        final Transformer valueReader = new Transformer() {
            public Object transform(final Object obj) {
                return ((DomNode) obj).getNodeValue();
            }
        };
        CollectionUtils.transform(valueList, valueReader);
        assertEquals(expectedValue, valueList);
    }

    /**
     * Test if option/text() is cleaned like other text()
     * @throws Exception if test fails
     */
    public void testOptionText() throws Exception {
        final String content = "<html><head><title>Test page</title></head>\n"
            + "<body><form name='foo'>\n"
            + "<select name='test'><option value='1'>foo&nbsp;and&nbsp;foo</option></select>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(content);
        final HtmlUnitXPath xpath = new HtmlUnitXPath("string(//option)");
        assertEquals("foo and foo", xpath.selectSingleNode(page));
    }
    
    /**
     * Regression test for
     * https://sourceforge.net/tracker/index.php?func=detail&aid=1527799&group_id=47038&atid=448266
     * @throws Exception if test fails
     */
    public void testFollowingAxis() throws Exception {
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
        testXPath(page, "//*[contains(.,'a4')]/following::td[.='c']", new Object[] {td22});

        testXPath(page, "//body/following::*", new Object[] {});
        testXPath(page, "//html/following::*", new Object[] {});
        testXPath(page, "//table/following::*", new Object[] {});
        testXPath(page, "//td[@id='td11']/following::*", new Object[] {td12, tr2, td21, td22});
    }
    
    private void testXPath(final HtmlPage page, final String xpathExpr, final Object[] expectedNodes) throws Exception {
        final HtmlUnitXPath xpath = new HtmlUnitXPath(xpathExpr);
        assertEquals(Arrays.asList(expectedNodes), xpath.selectNodes(page));
    }
    
    /**
     * @throws Exception If test fails.
     */
    public void testID() throws Exception {
        final String content = "<html><head><title>foo</title></head>\n"
            + "<body>\n"
            + "<div><a href='link.html' id='test'></div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);

        final List xPathByAttributeCompare = page.getByXPath("//div[@id='doesNotExist']");
        assertTrue(xPathByAttributeCompare.isEmpty());

        final List xPathByID = page.getByXPath("id('doesNotExist')");
        assertTrue(xPathByID.isEmpty());
    }

}
