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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase2;

/**
 * Tests for {@link XPathResult}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XPathResultTest extends WebTestCase2 {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void resultType() throws Exception {
        resultType("//div", "4");
        resultType("count(//div)", "1");
        resultType("count(//div)=2", "3");
    }

    private void resultType(final String expression, final String expectedAlert) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "    text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "    text += '  <xsl:template match=\"/\">\\n';\n"
            + "    text += '  <html>\\n';\n"
            + "    text += '    <body>\\n';\n"
            + "    text += '      <div/>\\n';\n"
            + "    text += '      <div/>\\n';\n"
            + "    text += '    </body>\\n';\n"
            + "    text += '  </html>\\n';\n"
            + "    text += '  </xsl:template>\\n';\n"
            + "    text += '</xsl:stylesheet>';\n"
            + "    var parser=new DOMParser();\n"
            + "    var doc=parser.parseFromString(text,'text/xml');\n"
            + "    var result = doc.evaluate('" + expression + "', doc.documentElement, "
            + "null, XPathResult.ANY_TYPE, null);\n"
            + "    alert(result.resultType);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(new String[] {expectedAlert}, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void snapshotType() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "    text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "    text += '  <xsl:template match=\"/\">\\n';\n"
            + "    text += '  <html>\\n';\n"
            + "    text += '    <body>\\n';\n"
            + "    text += '      <div id=\\'id1\\'/>\\n';\n"
            + "    text += '      <div id=\\'id2\\'/>\\n';\n"
            + "    text += '    </body>\\n';\n"
            + "    text += '  </html>\\n';\n"
            + "    text += '  </xsl:template>\\n';\n"
            + "    text += '</xsl:stylesheet>';\n"
            + "    var parser=new DOMParser();\n"
            + "    var doc=parser.parseFromString(text,'text/xml');\n"
            + "    var result = doc.evaluate('//div', doc.documentElement, null,"
            + " XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);\n"
            + "    alert(result.resultType);\n"
            + "    for (var i=0; i < result.snapshotLength; i++) {\n"
            + "      alert(result.snapshotItem(i).getAttribute('id'));\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"7", "id1", "id2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void singleNodeValue() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "    text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "    text += '  <xsl:template match=\"/\">\\n';\n"
            + "    text += '  <html>\\n';\n"
            + "    text += '    <body>\\n';\n"
            + "    text += '      <div id=\\'id1\\'/>\\n';\n"
            + "    text += '      <div id=\\'id2\\'/>\\n';\n"
            + "    text += '    </body>\\n';\n"
            + "    text += '  </html>\\n';\n"
            + "    text += '  </xsl:template>\\n';\n"
            + "    text += '</xsl:stylesheet>';\n"
            + "    var parser=new DOMParser();\n"
            + "    var doc=parser.parseFromString(text,'text/xml');\n"
            + "    var result = doc.evaluate('//div', doc.documentElement, null,"
            + " XPathResult.FIRST_ORDERED_NODE_TYPE, null);\n"
            + "    alert(result.resultType);\n"
            + "    alert(result.singleNodeValue.getAttribute('id'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"9", "id1"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void iterateNext() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "    text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "    text += '  <xsl:template match=\"/\">\\n';\n"
            + "    text += '  <html>\\n';\n"
            + "    text += '    <body>\\n';\n"
            + "    text += '      <div id=\"id1\"/>\\n';\n"
            + "    text += '      <div id=\"id2\"/>\\n';\n"
            + "    text += '    </body>\\n';\n"
            + "    text += '  </html>\\n';\n"
            + "    text += '  </xsl:template>\\n';\n"
            + "    text += '</xsl:stylesheet>';\n"
            + "    var parser=new DOMParser();\n"
            + "    var doc=parser.parseFromString(text,'text/xml');\n"
            + "    var result = doc.evaluate('" + "//div" + "', doc.documentElement, "
            + "null, XPathResult.ANY_TYPE, null);\n"
            + "    \n"
            + "    var thisNode = result.iterateNext();\n"
            + "    while (thisNode) {\n"
            + "      alert(thisNode.getAttribute('id'));\n"
            + "      thisNode = result.iterateNext();\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"id1", "id2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void notOr() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var expression = \".//*[@id='level1']/*[not(preceding-sibling::* or following-sibling::*)]\";\n"
            + "    var result = document.evaluate(expression, document, null, "
            + "XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);\n"
            + "    alert(result.resultType);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"7"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests that evaluating an expression that starts with slash in a sub-element, evalutes relative
     * to the root element, not to the specific sub-element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void evaluate_slash() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "    text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "    text += '  <xsl:template match=\"/\">\\n';\n"
            + "    text += '  <html>\\n';\n"
            + "    text += '    <body>\\n';\n"
            + "    text += '      <div id=\"id1\"><a/></div>\\n';\n"
            + "    text += '      <div id=\"id2\"><a/></div>\\n';\n"
            + "    text += '    </body>\\n';\n"
            + "    text += '  </html>\\n';\n"
            + "    text += '  </xsl:template>\\n';\n"
            + "    text += '</xsl:stylesheet>';\n"
            + "    var parser=new DOMParser();\n"
            + "    var doc=parser.parseFromString(text,'text/xml');\n"
            + "    var result = doc.evaluate(\"" + "//div[@id='id1']" + "\", doc.documentElement, "
            + "null, XPathResult.ANY_TYPE, null);\n"
            + "    \n"
            + "    var divNode = result.iterateNext();\n"
            + "    alert(divNode.getAttribute('id'));\n"
            + "    var total = doc.evaluate(\"" + "count(//a)" + "\", doc.documentElement, "
            + "null, XPathResult.NUMBER_TYPE, null);\n"
            + "    alert(total.numberValue);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"id1", "2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
