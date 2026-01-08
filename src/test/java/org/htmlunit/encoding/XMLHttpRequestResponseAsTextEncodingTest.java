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
package org.htmlunit.encoding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.htmlunit.javascript.host.xml.XMLHttpRequest;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link XMLHttpRequest} encoding.
 *
 * @author Ronald Brill
 */
public class XMLHttpRequestResponseAsTextEncodingTest extends AbstractXMLHttpRequestEncodingTest {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        final List<Arguments> list = new ArrayList<>();

        final String[] xmlEncodingHeaders = {"", "utf8"};
        final TestCharset[] charsetHtmlResponseHeaders =
                new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.WINDOWS1250, TestCharset.GB2312};
        final TestMimeType[] mimeTypeXmls = {TestMimeType.EMPTY, TestMimeType.XML, TestMimeType.PLAIN};
        final TestCharset[] charsetXmlResponseHeaders =
                new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.WINDOWS1250, TestCharset.GB2312};
        final String[] boms = {null, BOM_UTF_8, BOM_UTF_16LE, BOM_UTF_16BE};

        for (final Object xmlEncodingHeader : xmlEncodingHeaders) {
            for (final Object charsetHtmlResponseHeader : charsetHtmlResponseHeaders) {
                for (final Object mimeTypeXml : mimeTypeXmls) {
                    for (final Object charsetXmlResponseHeader : charsetXmlResponseHeaders) {
                        for (final Object bom : boms) {
                            list.add(Arguments.of(xmlEncodingHeader,
                                                   charsetHtmlResponseHeader,
                                                   mimeTypeXml,
                                                   charsetXmlResponseHeader,
                                                   bom));
                        }
                    }
                }
            }
        }

        return list;
    }

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @ParameterizedTest(name = "_{0}_{1}_{2}_{3}_{4}", quoteTextArguments = false)
    @MethodSource("data")
    void responseText(
           final String xmlEncodingHeader,
            final TestCharset charsetHtmlResponseHeader,
            final TestMimeType mimeTypeXml,
            final TestCharset charsetXmlResponseHeader,
            final String bom) throws Exception {
        responseText("GET", xmlEncodingHeader, charsetHtmlResponseHeader, mimeTypeXml, charsetXmlResponseHeader, bom);
        responseText("POST", xmlEncodingHeader, charsetHtmlResponseHeader, mimeTypeXml, charsetXmlResponseHeader, bom);
    }

    private void responseText(
            final String httpMethod,
            final String xmlEncodingHeader,
            final TestCharset charsetHtmlResponseHeader,
            final TestMimeType mimeTypeXml,
            final TestCharset charsetXmlResponseHeader,
            final String bom) throws Exception {

        String xmlEnc = xmlEncodingHeader;
        if ("utf8".equals(xmlEnc)) {
            xmlEnc = "encoding=\"utf-8\"";
        }

        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION

            + "      function unicodeEscape(str) {\n"
            + "        let result = '', index = 0, charCode, escape;\n"
            + "        while (!isNaN(charCode = str.charCodeAt(index++))) {\n"
            + "          escape = charCode.toString(16);\n"
            + "          result += '\\\\u' + ('0000' + escape).slice(-4);\n"
            + "        }\n"
            + "        return result;\n"
            + "      }\n"

            + "      function test() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.onreadystatechange = () => {\n"
            + "          if (request.readyState === 4) {\n"
            + "            let txt = request.response;\n"
            + "            if (txt == null) { log('null'); return; }\n"
            + "            log(unicodeEscape(txt));\n"
            + "          }\n"
            + "        }\n"

            + "        request.open('" + httpMethod + "', '" + URL_SECOND + "', true);\n"
            + "        request.send('');\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        final String xml = "<?xml version=\"1.0\" " + xmlEnc + "?>"
                + "<htmlunit>"
                + "<c1>a</c1>"
                + "<c2>\u008A\u009A\u00E4\u00A9</c2>"
                + "<c3>\u0623\u0647\u0644\u0627\u064B</c3>"
                + "<c4>\u043C\u0438\u0440</c4>"
                + "<c5>\u623F\u95F4</c5>"
                + "</htmlunit>";

        String[] expected = getExpectedAlerts();
        if (expected == null || expected.length == 0) {
            expected = new String[] {
                escape("<?xml version=\"1.0\" " + xmlEnc + "?>")
                + "\\u003c\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e\\u003c\\u0063\\u0031\\u003e\\u0061\\u003c\\u002f\\u0063\\u0031\\u003e\\u003c\\u0063\\u0032\\u003e\\u0160\\u0161\\u00e4\\u00a9\\u003c\\u002f\\u0063\\u0032\\u003e\\u003c\\u0063\\u0033\\u003e\\u003f\\u003f\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0033\\u003e\\u003c\\u0063\\u0034\\u003e\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0034\\u003e\\u003c\\u0063\\u0035\\u003e\\u003f\\u003f\\u003c\\u002f\\u0063\\u0035\\u003e\\u003c\\u002f\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e"};

            if (TestCharset.UTF8.equals(charsetXmlResponseHeader) || bom != null) {
                expected = new String[] {
                    escape("<?xml version=\"1.0\" " + xmlEnc + "?>")
                    + "\\u003c\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e\\u003c\\u0063\\u0031\\u003e\\u0061\\u003c\\u002f\\u0063\\u0031\\u003e\\u003c\\u0063\\u0032\\u003e\\u008a\\u009a\\u00e4\\u00a9\\u003c\\u002f\\u0063\\u0032\\u003e\\u003c\\u0063\\u0033\\u003e\\u0623\\u0647\\u0644\\u0627\\u064b\\u003c\\u002f\\u0063\\u0033\\u003e\\u003c\\u0063\\u0034\\u003e\\u043c\\u0438\\u0440\\u003c\\u002f\\u0063\\u0034\\u003e\\u003c\\u0063\\u0035\\u003e\\u623f\\u95f4\\u003c\\u002f\\u0063\\u0035\\u003e\\u003c\\u002f\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e"};
            }
            else if (TestMimeType.EMPTY.equals(mimeTypeXml)) {
                if (TestCharset.GB2312.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {
                        escape("<?xml version=\"1.0\" " + xmlEnc + "?>")
                        + "\\u003c\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e\\u003c\\u0063\\u0031\\u003e\\u0061\\u003c\\u002f\\u0063\\u0031\\u003e\\u003c\\u0063\\u0032\\u003e\\u003f\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0032\\u003e\\u003c\\u0063\\u0033\\u003e\\u003f\\u003f\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0033\\u003e\\u003c\\u0063\\u0034\\u003e\\ufffd\\u07a7\\u06a7\\ufffd\\u003c\\u002f\\u0063\\u0034\\u003e\\u003c\\u0063\\u0035\\u003e\\ufffd\\ufffd\\ufffd\\ufffd\\u003c\\u002f\\u0063\\u0035\\u003e\\u003c\\u002f\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e"};
                }
                else if (TestCharset.WINDOWS1250.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {
                        escape("<?xml version=\"1.0\" " + xmlEnc + "?>")
                        + "\\u003c\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e\\u003c\\u0063\\u0031\\u003e\\u0061\\u003c\\u002f\\u0063\\u0031\\u003e\\u003c\\u0063\\u0032\\u003e\\u003f\\u003f\\ufffd\\u003c\\u002f\\u0063\\u0032\\u003e\\u003c\\u0063\\u0033\\u003e\\u003f\\u003f\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0033\\u003e\\u003c\\u0063\\u0034\\u003e\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0034\\u003e\\u003c\\u0063\\u0035\\u003e\\u003f\\u003f\\u003c\\u002f\\u0063\\u0035\\u003e\\u003c\\u002f\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e"};
                }
                else if (null == charsetXmlResponseHeader || TestCharset.ISO88591.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {
                        escape("<?xml version=\"1.0\" " + xmlEnc + "?>")
                        + "\\u003c\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e\\u003c\\u0063\\u0031\\u003e\\u0061\\u003c\\u002f\\u0063\\u0031\\u003e\\u003c\\u0063\\u0032\\u003e\\ufffd\\ufffd\\ufffd\\u003c\\u002f\\u0063\\u0032\\u003e\\u003c\\u0063\\u0033\\u003e\\u003f\\u003f\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0033\\u003e\\u003c\\u0063\\u0034\\u003e\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0034\\u003e\\u003c\\u0063\\u0035\\u003e\\u003f\\u003f\\u003c\\u002f\\u0063\\u0035\\u003e\\u003c\\u002f\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e"};
                }
            }
            else if (TestMimeType.PLAIN.equals(mimeTypeXml) || TestMimeType.XML.equals(mimeTypeXml)) {
                if (TestCharset.GB2312.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {
                        escape("<?xml version=\"1.0\" " + xmlEnc + "?>")
                        + "\\u003c\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e\\u003c\\u0063\\u0031\\u003e\\u0061\\u003c\\u002f\\u0063\\u0031\\u003e\\u003c\\u0063\\u0032\\u003e\\u003f\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0032\\u003e\\u003c\\u0063\\u0033\\u003e\\u003f\\u003f\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0033\\u003e\\u003c\\u0063\\u0034\\u003e\\u043c\\u0438\\u0440\\u003c\\u002f\\u0063\\u0034\\u003e\\u003c\\u0063\\u0035\\u003e\\u623f\\u95f4\\u003c\\u002f\\u0063\\u0035\\u003e\\u003c\\u002f\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e"};
                }
                else if (TestCharset.WINDOWS1250.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {
                        escape("<?xml version=\"1.0\" " + xmlEnc + "?>")
                        + "\\u003c\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e\\u003c\\u0063\\u0031\\u003e\\u0061\\u003c\\u002f\\u0063\\u0031\\u003e\\u003c\\u0063\\u0032\\u003e\\u003f\\u003f\\u00e4\\u00a9\\u003c\\u002f\\u0063\\u0032\\u003e\\u003c\\u0063\\u0033\\u003e\\u003f\\u003f\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0033\\u003e\\u003c\\u0063\\u0034\\u003e\\u003f\\u003f\\u003f\\u003c\\u002f\\u0063\\u0034\\u003e\\u003c\\u0063\\u0035\\u003e\\u003f\\u003f\\u003c\\u002f\\u0063\\u0035\\u003e\\u003c\\u002f\\u0068\\u0074\\u006d\\u006c\\u0075\\u006e\\u0069\\u0074\\u003e"};
                }
            }
        }

        setupXmlResponse(xml, bom, mimeTypeXml, charsetXmlResponseHeader);
        final WebDriver driver = loadPage2(html, URL_FIRST, MimeType.TEXT_HTML,
                charsetHtmlResponseHeader == null ? null : charsetHtmlResponseHeader.getCharset());

        verifyTextArea2(driver, expected);
    }
}
