/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests encoding handling for {@link HtmlScript}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class HtmlScript3Test extends WebDriverTestCase {

    private static final String BOM_UTF_16LE = "BOMUTF16LE";
    private static final String BOM_UTF_16BE = "BOMUTF16BE";
    private static final String BOM_UTF_8 = "BOMUTF8";

    private static int ServerRestartCount_ = 0;

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

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        final List<Object[]> list = new ArrayList<>();

        final TestCharset[] charsetHtmlResponseHeader =
                new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.GB2312};
        final TestCharset[] charsetAttribute = new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591};
        final TestCharset[] charsetResponseHeader = new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591};
        final TestCharset[] charsetResponseEncoding = new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591};
        final String[] bom = {null, BOM_UTF_8, BOM_UTF_16LE, BOM_UTF_16BE};

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
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
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
        final URL scriptUrl = new URL(URL_SECOND, "" + System.currentTimeMillis() + ".js");

        String html
            = "<html><head>\n"
            + "  <script type='text/javascript'>window.onerror=function(msg) { alert(msg); }</script>"
            + "  <script src='" + scriptUrl + "'";
        if (charsetAttribute != null) {
            html = html + " charset='" + charsetAttribute.getCharset().name().toLowerCase() + "'";
        }
        html = html + "></script>\n"
            + "</head>\n"
            + "<body></body>\n"
            + "</html>";

        String scriptContentType = MimeType.APPLICATION_JAVASCRIPT;
        if (charsetResponseHeader != null) {
            scriptContentType = scriptContentType + "; charset="
                                    + charsetResponseHeader.getCharset().name().toLowerCase();
        }
        final String js = "alert('a'); alert('ä'); alert('أهلاً'); alert('мир'); alert('房间');";

        byte[] script = null;
        if (charsetResponseEncoding == null) {
            script = js.getBytes(UTF_8);
        }
        else {
            script = js.getBytes(charsetResponseEncoding.getCharset());
        }

        if (BOM_UTF_8.equals(bom)) {
            script = ArrayUtils.addAll(ByteOrderMark.UTF_8.getBytes(), js.getBytes(StandardCharsets.UTF_8));
        }
        else if (BOM_UTF_16BE.equals(bom)) {
            script = ArrayUtils.addAll(ByteOrderMark.UTF_16BE.getBytes(), js.getBytes(StandardCharsets.UTF_16BE));
        }
        else if (BOM_UTF_16LE.equals(bom)) {
            script = ArrayUtils.addAll(ByteOrderMark.UTF_16LE.getBytes(), js.getBytes(StandardCharsets.UTF_16LE));
        }
        getMockWebConnection().setResponse(scriptUrl, script, 200, "OK", scriptContentType, null);

        String htmlContentType = MimeType.TEXT_HTML;
        if (charsetHtmlResponse != null) {
            htmlContentType = htmlContentType + "; charset=" + charsetHtmlResponse.getCharset().name();
        }

        Charset htmlResponseCharset = ISO_8859_1;
        if (charsetHtmlResponse != null) {
            htmlResponseCharset = charsetHtmlResponse.getCharset();
        }

        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedAlerts = getExpectedAlerts();
        try {
            ServerRestartCount_++;
            if (ServerRestartCount_ == 200) {
                stopWebServers();
                ServerRestartCount_ = 0;
            }
            final WebDriver driver = loadPage2(html, URL_FIRST, htmlContentType, htmlResponseCharset, null);

            if (expectedAlerts.length == 1) {
                final List<String> actualAlerts = getCollectedAlerts(DEFAULT_WAIT_TIME, driver, expectedAlerts.length);
                assertEquals(1, actualAlerts.size());

                final String msg = actualAlerts.get(0);
                assertEquals(expectedAlerts[0], "Invalid token");
                assertTrue(msg, msg.contains("Invalid or unexpected token")
                                || msg.contains("illegal character")
                                || msg.contains("Ungültiges Zeichen"));
            }
            else {
                verifyAlerts(DEFAULT_WAIT_TIME, driver, expectedAlerts);
            }
        }
        catch (final WebDriverException e) {
            if (!e.getCause().getMessage().contains("illegal character")
                && !e.getCause().getMessage().contains("is not defined.")) {
                throw e;
            }

            assertTrue(expectedAlerts.length == 1);
            final String msg = e.getCause().getMessage();
            assertTrue(msg, msg.contains(expectedAlerts[0]));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591____() throws Exception {
        charset(TestCharset.ISO88591, null, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591___UTF8_() throws Exception {
        charset(TestCharset.ISO88591, null, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _ISO88591___ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, null, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _ISO88591__UTF8_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591__ISO88591__() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591__ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591__ISO88591_UTF8_() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591__ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _ISO88591__ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _ISO88591__ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _ISO88591_UTF8__ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _ISO88591_UTF8_UTF8__BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _ISO88591_UTF8_UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _ISO88591_UTF8_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _ISO88591_UTF8_UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_UTF8_ISO88591__() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_UTF8_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_UTF8_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_UTF8_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _ISO88591_UTF8_ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _ISO88591_UTF8_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _ISO88591_UTF8_ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_ISO88591___() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_ISO88591__UTF8_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _ISO88591_ISO88591__ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _ISO88591_ISO88591_UTF8__BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _ISO88591_ISO88591_UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _ISO88591_ISO88591_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _ISO88591_ISO88591_UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_ISO88591_ISO88591__() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_ISO88591_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_ISO88591_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_ISO88591_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _ISO88591_ISO88591_ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _ISO88591_ISO88591_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _ISO88591_ISO88591_ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _UTF8_ISO88591_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_ISO88591_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_ISO88591_ISO88591__() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _UTF8_ISO88591_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _UTF8_ISO88591__ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_ISO88591__UTF8_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_ISO88591___() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _UTF8_UTF8_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_UTF8_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_UTF8_ISO88591__() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _UTF8_UTF8_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _UTF8_UTF8__ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    public void _UTF8_UTF8___BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, null, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _UTF8__ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8__ISO88591_UTF8_() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8__ISO88591__() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _UTF8__UTF8_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _UTF8___ISO88591_() throws Exception {
        charset(TestCharset.UTF8, null, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_ISO88591_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_ISO88591_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_UTF8_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_UTF8_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8__ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8__ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void __ISO88591_ISO88591_ISO88591_() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __ISO88591_ISO88591_UTF8_() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __ISO88591_ISO88591__() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void __ISO88591_UTF8_ISO88591_() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void __ISO88591__ISO88591_() throws Exception {
        charset(null, TestCharset.ISO88591, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __ISO88591__UTF8_() throws Exception {
        charset(null, TestCharset.ISO88591, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __ISO88591___() throws Exception {
        charset(null, TestCharset.ISO88591, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void __UTF8_ISO88591_ISO88591_() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __UTF8_ISO88591_UTF8_() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __UTF8_ISO88591__() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void __UTF8_UTF8_ISO88591_() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void __UTF8__ISO88591_() throws Exception {
        charset(null, TestCharset.UTF8, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void ___ISO88591_ISO88591_() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void ___ISO88591_UTF8_() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void ___ISO88591__() throws Exception {
        charset(null, null, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void ___UTF8_ISO88591_() throws Exception {
        charset(null, null, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void ____ISO88591_() throws Exception {
        charset(null, null, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void ____UTF8_() throws Exception {
        charset(null, null, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _____() throws Exception {
        charset(null, null, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __ISO88591_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __ISO88591_ISO88591__BOMUTF8() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __UTF8_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __UTF8_ISO88591__BOMUTF8() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void ___ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void ___ISO88591__BOMUTF8() throws Exception {
        charset(null, null, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _GB2312_ISO88591_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_ISO88591_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_ISO88591_ISO88591__() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _GB2312_ISO88591_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _GB2312_ISO88591__ISO88591_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_ISO88591__UTF8_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_ISO88591___() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _GB2312_UTF8_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_UTF8_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_UTF8_ISO88591__() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _GB2312_UTF8_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _GB2312_UTF8__ISO88591_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "?????", "???", "??"})
    public void _GB2312__ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312__ISO88591_UTF8_() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312__ISO88591__() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _GB2312__UTF8_ISO88591_() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "�", "?????", "???", "??"})
    public void _GB2312___ISO88591_() throws Exception {
        charset(TestCharset.GB2312, null, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "盲", "兀賴賱丕賸", "屑懈褉", "鎴块棿"})
    @NotYetImplemented
    public void _GB2312___UTF8_() throws Exception {
        charset(TestCharset.GB2312, null, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "盲", "兀賴賱丕賸", "屑懈褉", "鎴块棿"})
    @NotYetImplemented
    public void _GB2312____() throws Exception {
        charset(TestCharset.GB2312, null, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_ISO88591_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_ISO88591_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_UTF8_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_UTF8_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312__ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312__ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_ISO88591_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312_UTF8_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _GB2312__ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_ISO88591_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591_UTF8_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _ISO88591__ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_ISO88591_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8_UTF8_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void _UTF8__ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __ISO88591_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void __UTF8_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"a", "ä", "أهلاً", "мир", "房间"},
            IE = {"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    public void ___ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    @NotYetImplemented(IE)
    public void _GB2312____BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, null, null, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    @NotYetImplemented(IE)
    public void _GB2312___UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, null, null, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    @NotYetImplemented(IE)
    public void _GB2312___ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, null, null, TestCharset.ISO88591, BOM_UTF_8);
    }
}
