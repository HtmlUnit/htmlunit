/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * <p>Tests for XPath evaluation on HtmlUnit DOM.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class HtmlUnitXPathTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlUnitXPathTest( final String name ) {
        super( name );
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
        assertEquals(page.getDocumentElement(), xpath.selectSingleNode(page));
        xpath = new HtmlUnitXPath("/html/head");
        assertEquals(page.getDocumentElement().getFirstChild(), xpath.selectSingleNode(page));
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
     * Test that the elements are in the right order
     * @throws Exception if test fails
     */
    public void testElementOrder() throws Exception {
        if (notYetImplemented()) {
            // due to bug http://jira.codehaus.org/browse/JAXEN-55
            return;
        }

        final String content
            = "<html><head><title>First</title><script>"
            + "</script></head><body>"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        final XPath xpath = new HtmlUnitXPath("//*");
        final List list = xpath.selectNodes(page);

        final List expected = Arrays.asList(new String[] {"html", "head", "title", "script", "body"});
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
            = "<html><head><title>foo</title><script>"
            + "function addOption() {\n"
            + "    var options = document.form1.select1.options;\n"
            + "    var index = options.length;\n"
            + "    options[index] = new Option('Four','value4');\n"
            + "}</script>\n"
            + "</head>\n"
            + "<body>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <select name='select1'>"
            + "        <option name='option1' value='value1'>One</option>"
            + "        <option name='option2' value='value2' selected>Two</option>"
            + "        <option name='option3' value='value3'>Three</option>"
            + "    </select>"
            + "</form>"
            + "<a href='javascript:addOption()'>add option</a>"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        assertEquals("foo", page.getTitleText());
        
        final HtmlUnitXPath xpath = new HtmlUnitXPath("count(//select[@name='select1']/option)");
        assertEquals(3, ((Double) xpath.evaluate(page)).intValue());
        
        final HtmlAnchor link = (HtmlAnchor) page.getAnchors().get(0);
        link.click();
        assertEquals(4, ((Double) xpath.evaluate(page)).intValue());

    }
}
