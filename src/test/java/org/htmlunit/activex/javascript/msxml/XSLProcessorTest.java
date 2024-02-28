/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.activex.javascript.msxml;

import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.ACTIVEX_CHECK;
import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;
import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.callLoadXMLDOMDocumentFromURL;
import static org.htmlunit.activex.javascript.msxml.MSXMLTestHelper.createTestHTML;

import java.net.URL;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link XSLProcessor}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XSLProcessorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX",
            IE = {"undefined", "undefined", "undefined", "undefined", "undefined",
                  "undefined", "undefined", "unknown", "unknown"})
    public void methods() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            // document must be free threaded
            + "    var xslDoc = new ActiveXObject('Msxml2.FreeThreadedDOMDocument.3.0');\n"
            + "    xslDoc.async = false;\n"
            + "    xslDoc.load('" + URL_SECOND + "');\n"
            + "    \n"
            + "    var xslt = new ActiveXObject('Msxml2.XSLTemplate.3.0');\n"
            + "    xslt.stylesheet = xslDoc;\n"
            + "    var xslProc = xslt.createProcessor();\n"
            + "    try {\n"
            + "      log(typeof xslProc.importStylesheet);\n"
            + "      log(typeof xslProc.transformToDocument);\n"
            + "      log(typeof xslProc.transformToFragment);\n"
            + "      log(typeof xslProc.setParameter);\n"
            + "      log(typeof xslProc.getParameter);\n"
            + "      log(typeof xslProc.input);\n"
            + "      log(typeof xslProc.ouput);\n"
            + "      log(typeof xslProc.addParameter);\n"
            + "      log(typeof xslProc.transform);\n"
            + "    } catch (e) {log(e)}\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xsl
            = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
            + "  <xsl:template match=\"/\">\n"
            + "  </xsl:template>\n"
            + "</xsl:stylesheet>";

        getMockWebConnection().setResponse(URL_SECOND, xsl, MimeType.TEXT_XML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no ActiveX")
    public void transform() throws Exception {
        final URL urlThird = new URL(URL_FIRST, "third/");

        final String html = ""
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    var xmlDoc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "'") + ";\n"
            + "    \n"
            // document must be free threaded
            + "    var xslDoc = new ActiveXObject('Msxml2.FreeThreadedDOMDocument.3.0');\n"
            + "    xslDoc.async = false;\n"
            + "    xslDoc.load('" + urlThird + "');\n"
            + "    \n"
            + "    var xslt = new ActiveXObject('Msxml2.XSLTemplate.3.0');\n"
            + "    xslt.stylesheet = xslDoc;\n"
            + "    var xslProc = xslt.createProcessor();\n"
            + "    xslProc.input = xmlDoc;\n"
            + "    xslProc.transform();\n"
            + "    var s = xslProc.output.replace(/\\r?\\n/g, '');\n"
            + "    log(s.length);\n"
            + "    xslProc.input = xmlDoc.documentElement;\n"
            + "    xslProc.transform();\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION;

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
            + "      <ul>\n"
            + "      <xsl:for-each select=\"catalog/cd\">\n"
            + "        <li><xsl:value-of select='title'/> (<xsl:value-of select='artist'/>)</li>\n"
            + "      </xsl:for-each>\n"
            + "      </ul>\n"
            + "    </body>\n"
            + "  </html>\n"
            + "  </xsl:template>\n"
            + "</xsl:stylesheet>";

        getMockWebConnection().setResponse(URL_SECOND, xml, MimeType.TEXT_XML);
        getMockWebConnection().setResponse(urlThird, xsl, MimeType.TEXT_XML);

        loadPageVerifyTitle2(createTestHTML(html));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no ActiveX")
    public void testSecurity() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + ACTIVEX_CHECK
            + "    try {"
            + "      var xmlDoc = " + callLoadXMLDOMDocumentFromURL("'" + URL_SECOND + "1'") + ";\n"
            + "      var xslDoc = new ActiveXObject('Msxml2.FreeThreadedDOMDocument.3.0');\n"
            + "      xslDoc.async = false;\n"
            + "      xslDoc.load('" + URL_SECOND + "2');\n"
            + "      var xslt = new ActiveXObject('Msxml2.XSLTemplate.3.0');\n"
            + "      xslt.stylesheet = xslDoc;\n"
            + "      var xslProc = xslt.createProcessor();\n"
            + "      xslProc.input = xmlDoc;\n"
            + "      log('preparation done');\n"
            + "      xslProc.transform();\n"
            + "      log(newxslProc.output);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + LOAD_XMLDOMDOCUMENT_FROM_URL_FUNCTION
            + "</script></head>"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final String xml
            = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
            + "<s></s>";

        final String xsl
            = " <xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" "
                    + "xmlns:rt=\"http://xml.apache.org/xalan/java/java.lang.Runtime\" "
                    + "xmlns:ob=\"http://xml.apache.org/xalan/java/java.lang.Object\">\r\n"
            + "    <xsl:template match='/'>\n"
            + "      <xsl:variable name='rtobject' select='rt:getRuntime()'/>\n"
            + "      <xsl:variable name=\"rtString\" select=\"ob:toString($rtobject)\"/>\n"
            + "      <xsl:value-of select=\"$rtString\"/>\n"
            + "    </xsl:template>\r\n"
            + "  </xsl:stylesheet>";

        getMockWebConnection().setResponse(new URL(URL_SECOND, "1"), xml, MimeType.TEXT_XML);
        getMockWebConnection().setResponse(new URL(URL_SECOND, "2"), xsl, MimeType.TEXT_XML);

        loadPageVerifyTitle2(html);
    }
}
