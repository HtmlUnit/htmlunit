/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link XSLTProcessor}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XSLTProcessorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void test() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var xmlDoc = createXmlDocument();\n"
            + "      xmlDoc.async = false;\n"
            + "      xmlDoc.load('" + URL_SECOND + "1');\n"

            + "      var xslDoc;\n"
            + "      xslDoc = createXmlDocument();\n"
            + "      xslDoc.async = false;\n"
            + "      xslDoc.load('" + URL_SECOND + "2');\n"

            + "      var processor = new XSLTProcessor();\n"
            + "      processor.importStylesheet(xslDoc);\n"
            + "      var newDocument = processor.transformToDocument(xmlDoc);\n"
            + "      log(new XMLSerializer().serializeToString(newDocument.documentElement).length);\n"
            + "      newDocument = processor.transformToDocument(xmlDoc.documentElement);\n"
            + "      log(newDocument.documentElement);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"

            + "  function createXmlDocument() {\n"
            + "    return document.implementation.createDocument('', '', null);\n"
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
    @Alerts(DEFAULT = {"function", "function", "function", "function", "function",
                       "undefined", "undefined", "undefined", "undefined"},
            IE = "exception")
    public void methods() throws Exception {
        final String html = "<html><head>\n"
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
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "function XSLTProcessor() { [native code] }",
                       "[object XSLTProcessor]"},
            IE = {"undefined", "exception"})
    public void type() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(typeof XSLTProcessor);\n"
            + "      log(XSLTProcessor);\n"
            + "      log(new XSLTProcessor());\n"
            + "    } catch (e) {log('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function XSLTProcessor() { [native code] }", "NaN", "true", "Yes", "Yes"},
            IE = {"exception str", "exception numb", "exception bool", "exception ?", "exception if"})
    public void browserDetection() throws Exception {
        final String html = "<html>\n"
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
}
