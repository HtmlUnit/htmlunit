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
package org.htmlunit.javascript.host.xml;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.lang3.ArrayUtils;
import org.htmlunit.junit.BrowserParameterizedRunner;
import org.htmlunit.junit.BrowserParameterizedRunner.Default;
import org.htmlunit.util.MimeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link XMLHttpRequest} encoding.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class XMLHttpRequestResponseXMLEncodingTest extends AbstractXMLHttpRequestEncodingTest {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        final List<Object[]> list = new ArrayList<>();

        final String[] xmlEncodingHeaders = {"", "utf8"};
        final TestCharset[] charsetHtmlResponseHeaders =
                new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.GB2312};
        final TestMimeType[] mimeTypeXmls = {TestMimeType.EMPTY, TestMimeType.XML, TestMimeType.PLAIN};
        final TestCharset[] charsetXmlResponseHeaders =
                new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.GB2312};
        final String[] boms = {null, BOM_UTF_8, BOM_UTF_16LE, BOM_UTF_16BE};

        for (final Object xmlEncodingHeader : xmlEncodingHeaders) {
            for (final Object charsetHtmlResponseHeader : charsetHtmlResponseHeaders) {
                for (final Object mimeTypeXml : mimeTypeXmls) {
                    for (final Object charsetXmlResponseHeader : charsetXmlResponseHeaders) {
                        for (final Object bom : boms) {
                            list.add(new Object[] {xmlEncodingHeader,
                                                   charsetHtmlResponseHeader,
                                                   mimeTypeXml,
                                                   charsetXmlResponseHeader,
                                                   bom});
                        }
                    }
                }
            }
        }

        return list;
    }

    /**
     * The xmlEncodingHeader.
     */
    @Parameter
    public String xmlEncodingHeader_;

    /**
     * The charsetHtmlResponseHeader.
     */
    @Parameter(1)
    public TestCharset charsetHtmlResponseHeader_;

    /**
     * The charsetHtmlResponseHeader.
     */
    @Parameter(2)
    public TestMimeType mimeTypeXml_;

    /**
     * The charsetXmlResponseHeader.
     */
    @Parameter(3)
    public TestCharset charsetXmlResponseHeader_;

    /**
     * The charsetXmlResponseHeader.
     */
    @Parameter(4)
    public String bom_;

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Default
    public void responseText() throws Exception {
        responseText(xmlEncodingHeader_, charsetHtmlResponseHeader_, mimeTypeXml_, charsetXmlResponseHeader_, bom_);
    }

    private void responseText(
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

        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var request = new XMLHttpRequest();\n"
            + "        request.open('" + httpMethod + "', '" + URL_SECOND + "', false);\n"
            + "        request.send('');\n"
            + "        let xml = request.responseXML;\n"
            + "        if (xml== null) { log('null'); return; }\n"

            + "        log(xml.getElementsByTagName('c1')[0].childNodes[0].nodeValue);\n"
            + "        log(xml.getElementsByTagName('c2')[0].childNodes[0].nodeValue);\n"
            + "        log(xml.getElementsByTagName('c3')[0].childNodes[0].nodeValue);\n"
            + "        log(xml.getElementsByTagName('c4')[0].childNodes[0].nodeValue);\n"
            + "        log(xml.getElementsByTagName('c5')[0].childNodes[0].nodeValue);\n"
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
                + "<c2>\u00E4</c2>"
                + "<c3>\u0623\u0647\u0644\u0627\u064B</c3>"
                + "<c4>\u043C\u0438\u0440</c4>"
                + "<c5>\u623F\u95F4</c5>"
                + "</htmlunit>";

        String[] expected = getExpectedAlerts();
        if (expected == null || expected.length == 0) {
            expected = new String[] {"a", "�", "?????", "???", "??"};

            if (TestMimeType.PLAIN.equals(mimeTypeXml)) {
                expected = new String[] {"null"};
            }
            else if (TestCharset.UTF8.equals(charsetXmlResponseHeader) || bom != null) {
                expected = new String[] {"a", "ä", "أهلاً", "мир", "房间"};
            }
            else if (TestMimeType.EMPTY.equals(mimeTypeXml)) {
                /* real FF - ignored for the moment
                if (getBrowserVersion().isFirefox()
                        && !TestCharset.UTF8.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {"null"};
                }
                else */
                if (TestCharset.GB2312.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {"a", "?", "?????", "�ާڧ�", "����"};
                }
            }
            else if (TestMimeType.XML.equals(mimeTypeXml)) {
                if (TestCharset.GB2312.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {"a", "?", "?????", "мир", "房间"};
                }
                else if (null == charsetXmlResponseHeader
                        || TestCharset.ISO88591.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {"a", "ä", "?????", "???", "??"};
                }
            }
            else {
                if (TestCharset.GB2312.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {"a", "?", "?????", "мир", "房间"};
                }
                else if (TestCharset.ISO88591.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {"a", "ä", "?????", "???", "??"};
                }
            }
        }

        if (BOM_UTF_8.equals(bom)) {
            final byte[] xmlBytes =
                    ArrayUtils.addAll(ByteOrderMark.UTF_8.getBytes(), xml.getBytes(StandardCharsets.UTF_8));
            getMockWebConnection().setResponse(URL_SECOND, xmlBytes, 200, "OK", mimeTypeXml.getMimeType(), null);
        }
        else if (BOM_UTF_16BE.equals(bom)) {
            final byte[] xmlBytes =
                    ArrayUtils.addAll(ByteOrderMark.UTF_16BE.getBytes(), xml.getBytes(StandardCharsets.UTF_16BE));
            getMockWebConnection().setResponse(URL_SECOND, xmlBytes, 200, "OK", mimeTypeXml.getMimeType(), null);
        }
        else if (BOM_UTF_16LE.equals(bom)) {
            final byte[] xmlBytes =
                    ArrayUtils.addAll(ByteOrderMark.UTF_16LE.getBytes(), xml.getBytes(StandardCharsets.UTF_16LE));
            getMockWebConnection().setResponse(URL_SECOND, xmlBytes, 200, "OK", mimeTypeXml.getMimeType(), null);
        }
        else {
            getMockWebConnection().setResponse(URL_SECOND, xml, mimeTypeXml.getMimeType(),
                    charsetXmlResponseHeader == null ? null : charsetXmlResponseHeader.getCharset());
        }

        final WebDriver driver = loadPage2(html, URL_FIRST, MimeType.TEXT_HTML,
                charsetHtmlResponseHeader == null ? null : charsetHtmlResponseHeader.getCharset());

        verifyTextArea2(driver, expected);
    }
}
