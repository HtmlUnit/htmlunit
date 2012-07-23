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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link XPathResult}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class XPathResultTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "4", "1", "3" })
    public void resultType() throws Exception {
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
            + "    var parser = new DOMParser();\n"
            + "    var doc = parser.parseFromString(text, 'text/xml');\n"
            + "    var expressions = ['//div', 'count(//div)', 'count(//div) = 2'];\n"
            + "    for (var i=0; i<expressions.length; ++i) {\n"
            + "      var expression = expressions[i];\n"
            + "      var result = doc.evaluate(expression, doc.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "      alert(result.resultType);\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "7", "id1", "id2" })
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "9", "id1" })
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "id1", "id2" })
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts("7")
    public void notOr() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var expression = \".//*[@id='level1']/*[not(preceding-sibling::* or following-sibling::*)]\";\n"
            + "    var result = document.evaluate(expression, document, null, "
            + "XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);\n"
            + "    alert(result.resultType);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
