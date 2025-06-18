/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.xml;

import java.net.URL;

import org.htmlunit.MockWebConnection;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XSLTProcessor}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class XSLTProcessorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<head></head><body> <h2>My CD Collection</h2> "
                + "<ul><li>Empire Burlesque (Bob Dylan)</li></ul> </body></html>",
            FF = "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<body><h2>My CD Collection</h2>"
                + "<ul><li>Empire Burlesque (Bob Dylan)</li></ul></body></html>",
            FF_ESR = "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<body><h2>My CD Collection</h2>"
                + "<ul><li>Empire Burlesque (Bob Dylan)</li></ul></body></html>")
    @HtmlUnitNYI(CHROME = "<html><body><h2>My CD Collection</h2>"
                + "<ul><li>Empire Burlesque (Bob Dylan)</li></ul></body></html>",
            EDGE = "<html><body><h2>My CD Collection</h2>"
                + "<ul><li>Empire Burlesque (Bob Dylan)</li></ul></body></html>",
            FF = "<html><body><h2>My CD Collection</h2>"
                + "<ul><li>Empire Burlesque (Bob Dylan)</li></ul></body></html>",
            FF_ESR = "<html><body><h2>My CD Collection</h2>"
                + "<ul><li>Empire Burlesque (Bob Dylan)</li></ul></body></html>")
    public void transformToDocument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  function  loadXMLDocument(url) {\n"
            + "    var xhttp = new XMLHttpRequest();\n"
            + "    xhttp.open('GET', url, false);\n"
            + "    xhttp.send();\n"
            + "    return xhttp.responseXML;\n"
            + "  }"

            + "  function test() {\n"
            + "    try {\n"
            + "      var xmlDoc = loadXMLDocument('" + URL_SECOND + "1');\n"
            + "      var xslDoc = loadXMLDocument('" + URL_SECOND + "2');\n"

            + "      var processor = new XSLTProcessor();\n"
            + "      processor.importStylesheet(xslDoc);\n"
            + "      var newDocument = processor.transformToDocument(xmlDoc);\n"
            + "      log(new XMLSerializer().serializeToString(newDocument.documentElement));\n"
            + "    } catch(e) { logEx(e); }\n"
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
            + "      <ul>\n"
            + "      <xsl:for-each select=\"catalog/cd\">\n"
            + "        <li><xsl:value-of select='title'/> (<xsl:value-of select='artist'/>)</li>\n"
            + "      </xsl:for-each>\n"
            + "      </ul>\n"
            + "    </body>\n"
            + "  </html>\n"
            + "  </xsl:template>\n"
            + "</xsl:stylesheet>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(URL_SECOND, "1"), xml, MimeType.TEXT_XML);
        conn.setResponse(new URL(URL_SECOND, "2"), xsl, MimeType.TEXT_XML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("*bar*")
    public void transformToFragment() throws Exception {
        final String xsl
            = "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
            + "<xsl:template match=\"/\">*<xsl:value-of select=\"foo\" />*</xsl:template>"
            + "</xsl:stylesheet>";

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  function test() {\n"
            + "    try {\n"
            + "      var xmlDoc = new DOMParser().parseFromString('<foo>bar</foo>', 'application/xml');\n"
            + "      var xslDoc = new DOMParser().parseFromString('" + xsl + "', 'application/xml');\n"

            + "      var processor = new XSLTProcessor();\n"
            + "      processor.importStylesheet(xslDoc);\n"
            + "      var newFragment = processor.transformToFragment(xmlDoc, document);\n"
            + "      log(new XMLSerializer().serializeToString(newFragment));\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"

            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function", "function", "function", "function",
             "undefined", "undefined", "undefined", "undefined"})
    public void methods() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      if (XSLTProcessor) {\n"
            + "        var processor = new XSLTProcessor();\n"
            + "        log(typeof processor.importStylesheet);\n"
            + "        log(typeof processor.transformToDocument);\n"
            + "        log(typeof processor.transformToFragment);\n"
            + "        log(typeof processor.setParameter);\n"
            + "        log(typeof processor.getParameter);\n"
            + "        log(typeof processor.input);\n"
            + "        log(typeof processor.ouput);\n"
            + "        log(typeof processor.addParameter);\n"
            + "        log(typeof processor.transform);\n"
            + "      } else {\n"
            + "        log('XSLTProcessor not available');\n"
            + "      }\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function XSLTProcessor() { [native code] }",
             "[object XSLTProcessor]"})
    public void type() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(typeof XSLTProcessor);\n"
            + "      log(XSLTProcessor);\n"
            + "      log(new XSLTProcessor());\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function XSLTProcessor() { [native code] }", "NaN", "true", "Yes", "Yes"})
    public void browserDetection() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(String(XSLTProcessor));\n"
            + "    } catch(e) { log('exception str'); }\n"
            + "    try {\n"
            + "      log(Number(XSLTProcessor));\n"
            + "    } catch(e) { log('exception numb'); }\n"
            + "    try {\n"
            + "      log(Boolean(XSLTProcessor));\n"
            + "    } catch(e) { log('exception bool'); }\n"
            + "    try {\n"
            + "      log(XSLTProcessor ? 'Yes' : 'No');\n"
            + "    } catch(e) { log('exception ?'); }\n"
            + "    try {\n"
            + "      if (XSLTProcessor) { log('Yes') } else { log('No') }\n"
            + "    } catch(e) { log('exception if'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"preparation done", "null"},
            FF = {"preparation done", "exception "},
            FF_ESR = {"preparation done", "exception "})
    @HtmlUnitNYI(CHROME = {"preparation done", "exception InternalError"},
            EDGE = {"preparation done", "exception InternalError"},
            FF = {"preparation done", "exception InternalError"},
            FF_ESR = {"preparation done", "exception InternalError"})
    public void testSecurity() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  function  loadXMLDocument(url) {\n"
            + "    var xhttp = new XMLHttpRequest();\n"
            + "    xhttp.open('GET', url, false);\n"
            + "    xhttp.send();\n"
            + "    return xhttp.responseXML;\n"
            + "  }"

            + "  function test() {\n"
            + "    try {\n"
            + "      var xmlDoc = loadXMLDocument('" + URL_SECOND + "1');\n"
            + "      var xslDoc = loadXMLDocument('" + URL_SECOND + "2');\n"

            + "      var processor = new XSLTProcessor();\n"
            + "      processor.importStylesheet(xslDoc);\n"
            + "      log('preparation done');\n"
            + "      var newDocument = processor.transformToDocument(xmlDoc);\n"
            + "      log(newDocument);\n"
            + "    } catch(e) { log('exception ' + e.name); }\n"
            + "  }\n"
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

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(URL_SECOND, "1"), xml, MimeType.TEXT_XML);
        conn.setResponse(new URL(URL_SECOND, "2"), xsl, MimeType.TEXT_XML);

        loadPageVerifyTitle2(html);
    }
}
