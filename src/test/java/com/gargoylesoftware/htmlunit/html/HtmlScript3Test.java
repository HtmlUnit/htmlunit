/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner.Default;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests encoding handling for {@link HtmlScript}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class HtmlScript3Test extends WebDriverTestCase {

    private static final String BOM_UTF_16BE = "BOMUTF16BE";
    private static final String BOM_UTF_8 = "BOMUTF8";

    private enum TestCharset {
        UTF8("UTF8", UTF_8),
        ISO88591("ISO88591", ISO_8859_1);

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

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        final List<Object[]> list = new ArrayList<>();

        final TestCharset[] charsetHtmlResponseHeader =
                new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591};
        final TestCharset[] charsetAttribute = new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591};
        final TestCharset[] charsetResponseHeader = new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591};
        final TestCharset[] charsetResponseEncoding = new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591};
        final String[] bom = new String[] {null, BOM_UTF_8, BOM_UTF_16BE};

        for (final Object charsetHtml : charsetHtmlResponseHeader) {
            for (final Object attribute : charsetAttribute) {
                for (final Object responseHeader : charsetResponseHeader) {
                    for (final Object responseEncoding : charsetResponseEncoding) {
                        for (final Object b : bom) {
                            list.add(new Object[] {charsetHtml, attribute, responseHeader, responseEncoding, b});
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * The charsetHtmlResponseHeader.
     */
    @Parameter
    public TestCharset charsetHtmlResponseHeader_;

    /**
     * The charsetAttribute.
     */
    @Parameter(1)
    public TestCharset charsetAttribute_;

    /**
     * The charsetResponseHeader.
     */
    @Parameter(2)
    public TestCharset charsetResponseHeader_;

    /**
     * The charsetResponseEncoding.
     */
    @Parameter(3)
    public TestCharset charsetResponseEncoding_;

    /**
     * The bom.
     */
    @Parameter(4)
    public String bom_;

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("أهلاً")
    @Default
    public void charset() throws Exception {
        charset(charsetHtmlResponseHeader_, charsetAttribute_, charsetResponseHeader_, charsetResponseEncoding_, bom_);
    }

    private void charset(
            final TestCharset charsetHtmlResponse,
            final TestCharset charsetAttribute,
            final TestCharset charsetResponseHeader,
            final TestCharset charsetResponseEncoding,
            final String bom) throws Exception {

        // use always a different url to avoid caching effects
        final URL cssUrl = new URL(URL_SECOND, "" + System.currentTimeMillis() + ".js");

        String html
            = "<html><head>\n"
            + "  <script src='" + cssUrl + "'";
        if (charsetAttribute != null) {
            html = html + " charset='" + charsetAttribute.getCharset().name().toLowerCase() + "'";
        }
        html = html + "></script>\n"
            + "</head>\n"
            + "<body></body>\n"
            + "</html>";

        String scriptContentType = "application/javascript";
        if (charsetResponseHeader != null) {
            scriptContentType = scriptContentType + "; charset="
                                    + charsetResponseHeader.getCharset().name().toLowerCase();
        }
        final String js = "alert('أهلاً');";

        byte[] script = null;
        if (charsetResponseEncoding == null) {
            script = js.getBytes(UTF_8);
        }
        else {
            script = js.getBytes(charsetResponseEncoding.getCharset());
        }

        if (BOM_UTF_8.equals(bom)) {
            script = ArrayUtils.addAll(ByteOrderMark.UTF_8.getBytes(), script);
        }
        else if (BOM_UTF_16BE.equals(bom)) {
            script = ArrayUtils.addAll(ByteOrderMark.UTF_16BE.getBytes(), script);
        }
        getMockWebConnection().setResponse(cssUrl, script, 200, "OK", scriptContentType, null);

        String htmlContentType = "text/html";
        if (charsetHtmlResponse != null) {
            htmlContentType = htmlContentType + "; charset=" + charsetHtmlResponse.getCharset().name();
        }

        Charset htmlResponseCharset = ISO_8859_1;
        if (charsetHtmlResponse != null) {
            htmlResponseCharset = charsetHtmlResponse.getCharset();
        }

        try {
            expandExpectedAlertsVariables(URL_FIRST);
            final String[] expectedAlerts = getExpectedAlerts();

            final WebDriver driver = loadPage2(html, URL_FIRST,
                                        htmlContentType, htmlResponseCharset, null);

            verifyAlerts(DEFAULT_WAIT_TIME, driver, expectedAlerts);
        }
        catch (final WebDriverException e) {
            if (!e.getCause().getMessage().contains("illegal character")
                && !e.getCause().getMessage().contains("is not defined.")) {
                throw e;
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591____() throws Exception {
        charset(TestCharset.ISO88591, null, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591____BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, null, null, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591___UTF8_() throws Exception {
        charset(TestCharset.ISO88591, null, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591___UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, null, null, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591___ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, null, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591___ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, null, null, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591___ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, null, null, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591__UTF8__BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591__UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591__UTF8_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591__UTF8_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591__UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591__ISO88591__() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591__ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591__ISO88591__BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, null, null, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591__ISO88591_UTF8_() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591__ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591__ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591__ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591__ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "?????")
    public void _ISO88591__ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591_UTF8___BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591_UTF8__UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_UTF8__ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_UTF8__ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591_UTF8__ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "أهلاً")
    public void _ISO88591_UTF8_UTF8__BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "أهلاً")
    public void _ISO88591_UTF8_UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_UTF8_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_UTF8_UTF8_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "?????")
    public void _ISO88591_UTF8_UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_UTF8_ISO88591__() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_UTF8_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591_UTF8_ISO88591__BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_UTF8_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_UTF8_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_UTF8_ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_UTF8_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_UTF8_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "?????")
    public void _ISO88591_UTF8_ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_ISO88591___() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591_ISO88591___BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_ISO88591__UTF8_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591_ISO88591__UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_ISO88591__ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_ISO88591__ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591_ISO88591__ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "أهلاً")
    public void _ISO88591_ISO88591_UTF8__BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "أهلاً")
    public void _ISO88591_ISO88591_UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_ISO88591_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_ISO88591_UTF8_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "?????")
    public void _ISO88591_ISO88591_UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_ISO88591_ISO88591__() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_ISO88591_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _ISO88591_ISO88591_ISO88591__BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_ISO88591_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_ISO88591_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "Ø£Ù‡Ù„Ø§Ù‹")
    public void _ISO88591_ISO88591_ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_ISO88591_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _ISO88591_ISO88591_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {},
            IE = "?????")
    public void _ISO88591_ISO88591_ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_ISO88591_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_ISO88591_ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_ISO88591_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8_ISO88591_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_ISO88591_ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8_ISO88591_ISO88591__() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_ISO88591_ISO88591__BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_ISO88591_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_ISO88591_UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_ISO88591_UTF8_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_ISO88591_UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_ISO88591_UTF8__BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_ISO88591__ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_ISO88591__ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_ISO88591__ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8_ISO88591__UTF8_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_ISO88591__UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8_ISO88591___() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_ISO88591___BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_UTF8_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_UTF8_ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_UTF8_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8_UTF8_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_UTF8_ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8_UTF8_ISO88591__() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_UTF8_ISO88591__BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_UTF8_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_UTF8_UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_UTF8_UTF8_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_UTF8_UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_UTF8_UTF8__BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_UTF8__ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_UTF8__ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, null, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8_UTF8__ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, null, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_UTF8__UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, null, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8_UTF8___BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, null, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8__ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8__ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8__ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8__ISO88591_UTF8_() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8__ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8__ISO88591__() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8__ISO88591__BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8__UTF8_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8__UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8__UTF8_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8__UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8__UTF8__BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8___ISO88591_() throws Exception {
        charset(TestCharset.UTF8, null, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8___ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, null, null, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void _UTF8___ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, null, null, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8___UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, null, null, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _UTF8____BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, null, null, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8_ISO88591_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8_ISO88591_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8_UTF8_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8_UTF8_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8__ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _UTF8__ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __ISO88591_ISO88591_ISO88591_() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __ISO88591_ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __ISO88591_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void __ISO88591_ISO88591_UTF8_() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __ISO88591_ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void __ISO88591_ISO88591__() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __ISO88591_ISO88591__BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __ISO88591_UTF8_ISO88591_() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __ISO88591_UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __ISO88591_UTF8_ISO88591_BOMUTF8() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __ISO88591_UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __ISO88591_UTF8__BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __ISO88591__ISO88591_() throws Exception {
        charset(null, TestCharset.ISO88591, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __ISO88591__ISO88591_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, null, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __ISO88591__ISO88591_BOMUTF8() throws Exception {
        charset(null, TestCharset.ISO88591, null, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void __ISO88591__UTF8_() throws Exception {
        charset(null, TestCharset.ISO88591, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __ISO88591__UTF8_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, null, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void __ISO88591___() throws Exception {
        charset(null, TestCharset.ISO88591, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __ISO88591___BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, null, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __UTF8_ISO88591_ISO88591_() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __UTF8_ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __UTF8_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void __UTF8_ISO88591_UTF8_() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __UTF8_ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void __UTF8_ISO88591__() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __UTF8_ISO88591__BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __UTF8_UTF8_ISO88591_() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __UTF8_UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __UTF8_UTF8_ISO88591_BOMUTF8() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __UTF8_UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __UTF8_UTF8__BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __UTF8__ISO88591_() throws Exception {
        charset(null, TestCharset.UTF8, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __UTF8__ISO88591_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, null, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void __UTF8__ISO88591_BOMUTF8() throws Exception {
        charset(null, TestCharset.UTF8, null, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __UTF8__UTF8_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, null, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void __UTF8___BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, null, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void ___ISO88591_ISO88591_() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void ___ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void ___ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void ___ISO88591_UTF8_() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void ___ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void ___ISO88591__() throws Exception {
        charset(null, null, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void ___ISO88591__BOMUTF16BE() throws Exception {
        charset(null, null, TestCharset.ISO88591, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void ___UTF8_ISO88591_() throws Exception {
        charset(null, null, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void ___UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(null, null, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void ___UTF8_ISO88591_BOMUTF8() throws Exception {
        charset(null, null, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void ___UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(null, null, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void ___UTF8__BOMUTF16BE() throws Exception {
        charset(null, null, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void ____ISO88591_() throws Exception {
        charset(null, null, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void ____ISO88591_BOMUTF16BE() throws Exception {
        charset(null, null, null, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void ____ISO88591_BOMUTF8() throws Exception {
        charset(null, null, null, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void ____UTF8_() throws Exception {
        charset(null, null, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void ____UTF8_BOMUTF16BE() throws Exception {
        charset(null, null, null, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void _____() throws Exception {
        charset(null, null, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _____BOMUTF16BE() throws Exception {
        charset(null, null, null, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void __ISO88591_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void __ISO88591_ISO88591__BOMUTF8() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void __UTF8_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void __UTF8_ISO88591__BOMUTF8() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void ___ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "أهلاً",
            IE = "\u00D8\u00A3\u00D9\u2021\u00D9\u201E\u00D8\u00A7\u00D9\u2039")
    public void ___ISO88591__BOMUTF8() throws Exception {
        charset(null, null, TestCharset.ISO88591, null, BOM_UTF_8);
    }
}
