/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

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
    @Alerts(FF = { "97", "[object Element]" },
            IE = "exception",
            CHROME = "exception")
    @NotYetImplemented(FF)
    public void test() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var xmlDoc = createXmlDocument();\n"
            + "      xmlDoc.async = false;\n"
            + "      xmlDoc.load('" + URL_SECOND + "2');\n"

            + "      var xslDoc;\n"
            + "      xslDoc = createXmlDocument();\n"
            + "      xslDoc.async = false;\n"
            + "      xslDoc.load('" + URL_SECOND + "2');\n"

            + "      var processor = new XSLTProcessor();\n"
            + "      processor.importStylesheet(xslDoc);\n"
            + "      var newDocument = processor.transformToDocument(xmlDoc);\n"
            + "      alert(new XMLSerializer().serializeToString(newDocument.documentElement).length);\n"
            + "      newDocument = processor.transformToDocument(xmlDoc.documentElement);\n"
            + "      alert(newDocument.documentElement);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"

            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument) {\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    }\n"
            + "    alert('createDocument not available');\n"
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
        conn.setResponse(new URL(URL_SECOND, "1"), xml, "text/xml");
        conn.setResponse(new URL(URL_SECOND, "2"), xsl, "text/xml");

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function", "function", "function", "function", "function",
            "undefined", "undefined", "undefined", "undefined" },
            IE = "exception")
    public void methods() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      if (XSLTProcessor) {\n"
            + "        var processor = new XSLTProcessor();\n"
            + "        alert(typeof processor.importStylesheet);\n"
            + "        alert(typeof processor.transformToDocument);\n"
            + "        alert(typeof processor.transformToFragment);\n"
            + "        alert(typeof processor.setParameter);\n"
            + "        alert(typeof processor.getParameter);\n"
            + "        alert(typeof processor.input);\n"
            + "        alert(typeof processor.ouput);\n"
            + "        alert(typeof processor.addParameter);\n"
            + "        alert(typeof processor.transform);\n"
            + "      } else {\n"
            + "        alert('XSLTProcessor not available');\n"
            + "      }\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "function", "function XSLTProcessor() {\n    [native code]\n}",
                "[object XSLTProcessor]" },
            CHROME = { "function", "function XSLTProcessor() { [native code] }",
                "[object XSLTProcessor]" },
            IE = { "undefined", "exception" })
    public void type() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(typeof XSLTProcessor);"
            + "      alert(XSLTProcessor);"
            + "      alert(new XSLTProcessor());"
            + "    } catch (e) {alert('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object XSLTProcessor]", "NaN", "true", "Yes", "Yes" },
            CHROME = {"function XSLTProcessor() { [native code] }", "NaN", "true", "Yes", "Yes" },
            FF = {"function XSLTProcessor() {\n    [native code]\n}", "NaN", "true", "Yes", "Yes" },
            IE = {"exception str", "exception numb", "exception bool", "exception ?", "exception if" })
    public void browserDetection() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(String(XSLTProcessor));\n"
            + "    } catch(e) { alert('exception str'); }\n"
            + "    try {\n"
            + "      alert(Number(XSLTProcessor));\n"
            + "    } catch(e) { alert('exception numb'); }\n"
            + "    try {\n"
            + "      alert(Boolean(XSLTProcessor));\n"
            + "    } catch(e) { alert('exception bool'); }\n"
            + "    try {\n"
            + "      alert(XSLTProcessor ? 'Yes' : 'No');\n"
            + "    } catch(e) { alert('exception ?'); }\n"
            + "    try {\n"
            + "      if (XSLTProcessor) { alert('Yes') } else { alert('No') }\n"
            + "    } catch(e) { alert('exception if'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
