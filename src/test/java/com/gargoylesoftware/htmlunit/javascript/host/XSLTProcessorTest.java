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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link XSLTProcessor}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XSLTProcessorTest extends WebTestCase {

    /**
     * Creates a new test instance.
     * @param name The name of the new test instance.
     */
    public XSLTProcessorTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void test() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String[] expectedAlertsIE = {"241"};
        test(BrowserVersion.INTERNET_EXPLORER_7_0, expectedAlertsIE);
        final String[] expectedAlertsFF = {"226"};
        test(BrowserVersion.FIREFOX_2, expectedAlertsFF);
    }
    
    private void test(final BrowserVersion browserVersion, final String[] expectedAlerts) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var xmlDoc = createXmlDocument();\n"
            + "    xmlDoc.async = false;\n"
            + "    xmlDoc.load('" + URL_SECOND + "');\n"
            + "    \n"
            + "    var xslDoc;\n"
            + "    if (window.ActiveXObject)\n"
            + "      xslDoc = new ActiveXObject('Msxml2.FreeThreadedDOMDocument.3.0');\n"
            + "    else\n"
            + "      xslDoc = createXmlDocument();\n"
            + "    xslDoc.async = false;\n"
            + "    xslDoc.load('" + URL_THIRD + "');\n"
            + "    \n"
            + "    if (window.ActiveXObject) {\n"
            + "      var xslt = new ActiveXObject('Msxml2.XSLTemplate.3.0');\n"
            + "      xslt.stylesheet = xslDoc;\n"
            + "      var xslProc = xslt.createProcessor();\n"
            + "      xslProc.input = xmlDoc;\n"
            + "      xslProc.transform();\n"
            + "      alert(xslProc.output.length);\n"
            + "    } else {\n"
            + "      var processor = new XSLTProcessor();\n"
            + "      processor.importStylesheet(xslDoc);\n"
            + "      var newDocument = processor.transformToDocument(xmlDoc);\n"
            + "      alert(new XMLSerializer().serializeToString(newDocument.documentElement).length);\n"
            + "    }\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
 
        final String xml
            = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
            + "<catalog>\n"
            + "  <cd>\n"
            + "    <title>Empire Burlesque</title>\n"
            + "    <artist>Bob Dylan</artist>\n"
            + "    <country>USA</country>\n"
            + "    <company>Columbia</company>\n"
            + "    <price>10.90</price>\n"
            + "    <year>1985</year>\n"
            + "  </cd>\n"
            + "</catalog>";

        final String xsl
            = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
            + "  <xsl:template match=\"/\">\n"
            + "  <html>\n"
            + "    <body>\n"
            + "      <h2>My CD Collection</h2>\n"
            + "      <table border='1'>\n"
            + "        <tr bgcolor='#9acd32'>\n"
            + "          <th align='left'>Title</th>\n"
            + "          <th align='left'>Artist</th>\n"
            + "        </tr>\n"
            + "      <xsl:for-each select=\"catalog/cd\">\n"
            + "        <tr>\n"
            + "          <td><xsl:value-of select='title'/></td>\n"
            + "          <td><xsl:value-of select='artist'/></td>\n"
            + "          </tr>\n"
            + "      </xsl:for-each>\n"
            + "      </table>\n"
            + "    </body>\n"
            + "  </html>\n"
            + "  </xsl:template>\n"
            + "</xsl:stylesheet>";
        
        final List<String> collectedAlerts = new ArrayList<String>();
        final WebClient client = new WebClient(browserVersion);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, xml, "text/xml");
        conn.setResponse(URL_THIRD, xsl, "text/xml");
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
