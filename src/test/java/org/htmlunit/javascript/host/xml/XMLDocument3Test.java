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

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.htmlunit.javascript.host.xml.XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION;
import static org.htmlunit.javascript.host.xml.XMLDocumentTest.LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION;
import static org.htmlunit.javascript.host.xml.XMLDocumentTest.callLoadXMLDocumentFromFile;
import static org.htmlunit.javascript.host.xml.XMLDocumentTest.callLoadXMLDocumentFromString;

import java.util.Collections;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link XMLDocument}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class XMLDocument3Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1610", "1575", "32", "1604", "1610", "1610", "1610", "1610", "1610", "1610", "1604"})
    public void load_Encoding() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var doc = " + callLoadXMLDocumentFromFile("'" + URL_SECOND + "'") + ";\n"
            + "    var value = doc.documentElement.firstChild.nodeValue;\n"
            + "    for (var i = 0; i < value.length; i++) {\n"
            + "      log(value.charCodeAt(i));\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_FILE_FUNCTION
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<something>\u064A\u0627 \u0644\u064A\u064A\u064A\u064A\u064A\u064A\u0644</something>";

        getMockWebConnection().setResponse(URL_SECOND, xml.getBytes("UTF-8"), 200, "OK",
                MimeType.TEXT_XML, Collections.emptyList());

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"230", "230"})
    public void parseIso88591Encoding() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(encoding) {\n"
            + "    var text=\"<?xml version='1.0' encoding='\" + encoding + \"'?><body>\u00e6</body>\";\n"
            + "    var doc=" + callLoadXMLDocumentFromString("text") + ";\n"
            + "    var value = doc.documentElement.firstChild.nodeValue;\n"
            + "    for (var i = 0; i < value.length; i++) {\n"
            + "      log(value.charCodeAt(i));\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head>\n"
            + "<body onload='test(\"ISO-8859-1\");test(\"UTF8\");'>\n"
            + "</body></html>";

        // javascript ignores the encoding defined in the xml, the xml is parsed as string
        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html; charset=ISO-8859-1", ISO_8859_1);
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1044", "1044"})
    public void parseUtf8Encoding() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(encoding) {\n"
            + "    var text=\"<?xml version='1.0' encoding='\" + encoding + \"'?><body>\u0414</body>\";\n"
            + "    var doc=" + callLoadXMLDocumentFromString("text") + ";\n"
            + "    var value = doc.documentElement.firstChild.nodeValue;\n"
            + "    for (var i = 0; i < value.length; i++) {\n"
            + "      log(value.charCodeAt(i));\n"
            + "    }\n"
            + "  }\n"
            + LOAD_XML_DOCUMENT_FROM_STRING_FUNCTION
            + "</script></head>\n"
            + "<body onload='test(\"UTF-8\");test(\"ISO-8859-1\");'>\n"
            + "</body></html>";

        // javascript ignores the encoding defined in the xml, the xml is parsed as string
        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html; charset=UTF-8", UTF_8);
        verifyTitle2(driver, getExpectedAlerts());
    }
}
