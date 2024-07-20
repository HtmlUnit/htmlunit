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

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.htmlunit.WebDriverTestCase;
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
public class XMLHttpRequestResponseAsTextEncodingTest extends WebDriverTestCase {

    private enum TestCharset {
        UTF8("UTF8", UTF_8),
        ISO88591("ISO88591", ISO_8859_1),
        GB2312("GB2312", Charset.forName("GB2312"));

        private final String label_;
        private final Charset charset_;

        TestCharset(final String label, final Charset charset) {
            label_ = label;
            charset_ = charset;
        }

        @Override
        public String toString() {
            return label_;
        }

        public Charset getCharset() {
            return charset_;
        }
    }

    private enum TestMimeType {
        EMPTY("EMPTY", ""),
        XML("XML", MimeType.TEXT_XML),
        PLAIN("PLAIN", MimeType.TEXT_PLAIN);

        private final String label_;
        private final String mimeType_;

        TestMimeType(final String label, final String mimeType) {
            label_ = label;
            mimeType_ = mimeType;
        }

        @Override
        public String toString() {
            return label_;
        }

        public String getMimeType() {
            return mimeType_;
        }
    }

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

        for (final Object xmlEncodingHeader : xmlEncodingHeaders) {
            for (final Object charsetHtmlResponseHeader : charsetHtmlResponseHeaders) {
                for (final Object mimeTypeXml : mimeTypeXmls) {
                    for (final Object charsetXmlResponseHeader : charsetXmlResponseHeaders) {
                        list.add(new Object[] {xmlEncodingHeader,
                                               charsetHtmlResponseHeader,
                                               mimeTypeXml,
                                               charsetXmlResponseHeader});
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
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Default
    public void responseText() throws Exception {
        responseText(xmlEncodingHeader_, charsetHtmlResponseHeader_, mimeTypeXml_, charsetXmlResponseHeader_);
    }

    private void responseText(
           final String xmlEncodingHeader,
            final TestCharset charsetHtmlResponseHeader,
            final TestMimeType mimeTypeXml,
            final TestCharset charsetXmlResponseHeader) throws Exception {
        responseText("GET", xmlEncodingHeader, charsetHtmlResponseHeader, mimeTypeXml, charsetXmlResponseHeader);
        responseText("POST", xmlEncodingHeader, charsetHtmlResponseHeader, mimeTypeXml, charsetXmlResponseHeader);
    }

    private void responseText(
            final String httpMethod,
            final String xmlEncodingHeader,
            final TestCharset charsetHtmlResponseHeader,
            final TestMimeType mimeTypeXml,
            final TestCharset charsetXmlResponseHeader) throws Exception {

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
            + "        request.onreadystatechange = () => {\n"
            + "          if (request.readyState === 4) {\n"
            + "            let txt = request.response;\n"
            + "            if (txt == null) { log('null'); return; }\n"
            + "            log(txt);\n"
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
                + "<c2>\u00E4</c2>"
                + "<c3>\u0623\u0647\u0644\u0627\u064B</c3>"
                + "<c4>\u043C\u0438\u0440</c4>"
                + "<c5>\u623F\u95F4</c5>"
                + "</htmlunit>";

        String[] expected = getExpectedAlerts();
        if (expected == null || expected.length == 0) {
            expected = new String[] {
                "<?xml version=\"1.0\" "
                    + xmlEnc
                    + "?><htmlunit><c1>a</c1><c2>ä</c2><c3>?????</c3><c4>???</c4><c5>??</c5></htmlunit>"};

            if (TestCharset.UTF8.equals(charsetXmlResponseHeader)) {
                expected = new String[] {
                    "<?xml version=\"1.0\" "
                        + xmlEnc
                        + "?><htmlunit><c1>a</c1><c2>ä</c2><c3>أهلاً</c3><c4>мир</c4><c5>房间</c5></htmlunit>"};
            }
            else if (TestMimeType.EMPTY.equals(mimeTypeXml)) {
                if (TestCharset.GB2312.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {
                        "<?xml version=\"1.0\" "
                            + xmlEnc
                            + "?><htmlunit><c1>a</c1><c2>?</c2><c3>?????</c3><c4>�ާڧ�</c4><c5>����</c5></htmlunit>"};
                }
                else if (null == charsetXmlResponseHeader || TestCharset.ISO88591.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {
                        "<?xml version=\"1.0\" "
                            + xmlEnc
                            + "?><htmlunit><c1>a</c1><c2>�</c2><c3>?????</c3><c4>???</c4><c5>??</c5></htmlunit>"};
                }
            }
            else if (TestMimeType.PLAIN.equals(mimeTypeXml) || TestMimeType.XML.equals(mimeTypeXml)) {
                if (TestCharset.GB2312.equals(charsetXmlResponseHeader)) {
                    expected = new String[] {
                        "<?xml version=\"1.0\" "
                            + xmlEnc
                            + "?><htmlunit><c1>a</c1><c2>?</c2><c3>?????</c3><c4>мир</c4><c5>房间</c5></htmlunit>"};
                }
            }
        }

        getMockWebConnection().setResponse(URL_SECOND, xml, mimeTypeXml.getMimeType(),
                charsetXmlResponseHeader == null ? null : charsetXmlResponseHeader.getCharset());

        final WebDriver driver = loadPage2(html, URL_FIRST, MimeType.TEXT_HTML,
                charsetHtmlResponseHeader == null ? null : charsetHtmlResponseHeader.getCharset());

        verifyTextArea2(driver, expected);
    }
}
