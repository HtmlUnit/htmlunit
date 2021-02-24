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
package com.gargoylesoftware.htmlunit.javascript.host.css;

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
 * Tests encoding handling for {@link CSSStyleSheet}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class CSSStyleSheet3Test extends WebDriverTestCase {

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
        final TestCharset[] charsetResponseHeader = new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591};
        final TestCharset[] charsetResponseEncoding = new TestCharset[] {null, TestCharset.UTF8, TestCharset.ISO88591};
        final String[] bom = {null, BOM_UTF_8, BOM_UTF_16LE, BOM_UTF_16BE};

        for (final Object charsetHtml : charsetHtmlResponseHeader) {
            for (final Object responseHeader : charsetResponseHeader) {
                for (final Object responseEncoding : charsetResponseEncoding) {
                    for (final Object b : bom) {
                        list.add(new Object[] {charsetHtml, responseHeader, responseEncoding, b});
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
     * The charsetResponseHeader.
     */
    @Parameter(1)
    public TestCharset charsetResponseHeader_;

    /**
     * The charsetResponseEncoding.
     */
    @Parameter(2)
    public TestCharset charsetResponseEncoding_;

    /**
     * The bom.
     */
    @Parameter(3)
    public String bom_;

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""})
    @Default
    public void charset() throws Exception {
        charset(charsetHtmlResponseHeader_, charsetResponseHeader_, charsetResponseEncoding_, bom_);
    }

    private void charset(
            final TestCharset charsetHtmlResponse,
            final TestCharset charsetCssResponseHeader,
            final TestCharset charsetCssResponseEncoding,
            final String bom) throws Exception {

        // use always a different url to avoid caching effects
        final URL cssUrl = new URL(URL_SECOND, "" + System.currentTimeMillis() + ".js");

        final String html
            = "<html><head>\n"
            + "  <link rel='stylesheet' href='" + cssUrl + "'/>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var node = document.getElementById('c1');\n"
            + "    var style = window.getComputedStyle(node, ':before');\n"
            + "    log(style.content);\n"

            + "    node = document.getElementById('c2');\n"
            + "    style = window.getComputedStyle(node, ':before');\n"
            + "    log(style.content);\n"

            + "    node = document.getElementById('c3');\n"
            + "    style = window.getComputedStyle(node, ':before');\n"
            + "    log(style.content);\n"

            + "    node = document.getElementById('c4');\n"
            + "    style = window.getComputedStyle(node, ':before');\n"
            + "    log(style.content);\n"

            + "    node = document.getElementById('c5');\n"
            + "    style = window.getComputedStyle(node, ':before');\n"
            + "    log(style.content);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='c1' class='c1'>C1</div>\n"
            + "  <div id='c2' class='c2'>C2</div>\n"
            + "  <div id='c3' class='c3'>C3</div>\n"
            + "  <div id='c4' class='c4'>C4</div>\n"
            + "  <div id='c5' class='c5'>C5</div>\n"
            + "</body>\n"
            + "</html>";

        String cssContentType = MimeType.TEXT_CSS;
        if (charsetCssResponseHeader != null) {
            cssContentType = cssContentType + "; charset="
                                    + charsetCssResponseHeader.getCharset().name().toLowerCase();
        }
        final String css = ".c1::before { content: \"a\"}"
                + ".c2::before { content: \"ä\"}"
                + ".c3::before { content: \"أهلاً\"}"
                + ".c4::before { content: \"мир\"}"
                + ".c5::before { content: \"房间\"}";

        byte[] style = null;
        if (charsetCssResponseEncoding == null) {
            style = css.getBytes(UTF_8);
        }
        else {
            style = css.getBytes(charsetCssResponseEncoding.getCharset());
        }

        if (BOM_UTF_8.equals(bom)) {
            style = ArrayUtils.addAll(ByteOrderMark.UTF_8.getBytes(), css.getBytes(StandardCharsets.UTF_8));
        }
        else if (BOM_UTF_16BE.equals(bom)) {
            style = ArrayUtils.addAll(ByteOrderMark.UTF_16BE.getBytes(), css.getBytes(StandardCharsets.UTF_16BE));
        }
        else if (BOM_UTF_16LE.equals(bom)) {
            style = ArrayUtils.addAll(ByteOrderMark.UTF_16LE.getBytes(), css.getBytes(StandardCharsets.UTF_16LE));
        }
        getMockWebConnection().setResponse(cssUrl, style, 200, "OK", cssContentType, null);

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

            assertEquals(String.join("§", getExpectedAlerts()) + '§', driver.getTitle());
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
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void _ISO88591___() throws Exception {
        charset(TestCharset.ISO88591, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void _ISO88591__UTF8_() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"ä\"", "\"?????\"", "\"???\"", "\"??\""})
    public void _ISO88591__ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"�\"", "\"?????\"", "\"???\"", "\"??\""})
    public void _ISO88591_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void _ISO88591_ISO88591__() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void _ISO88591_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void _ISO88591_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void _ISO88591_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"ä\"", "\"?????\"", "\"???\"", "\"??\""})
    public void _ISO88591_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_ISO88591_ISO88591_BOMUTF16LE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_ISO88591_UTF8_BOMUTF16LE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_ISO88591__BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_ISO88591__BOMUTF16LE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_UTF8_UTF8_BOMUTF16LE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_UTF8__BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_UTF8__BOMUTF16LE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _ISO88591_UTF8_ISO88591_BOMUTF16LE() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"�\"", "\"?????\"", "\"???\"", "\"??\""})
    public void _UTF8__ISO88591_() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"ä\"", "\"?????\"", "\"???\"", "\"??\""})
    public void _UTF8_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void _UTF8_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void _UTF8_ISO88591__() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"�\"", "\"?????\"", "\"???\"", "\"??\""})
    public void _UTF8_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_UTF8_ISO88591_BOMUTF16LE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_UTF8_UTF8_BOMUTF16LE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_UTF8__BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_UTF8__BOMUTF16LE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, null, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void _UTF8_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void _UTF8_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"ä\"", "\"?????\"", "\"???\"", "\"??\""})
    public void __ISO88591_ISO88591_() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void __ISO88591_UTF8_() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void __ISO88591__() throws Exception {
        charset(null, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"�\"", "\"?????\"", "\"???\"", "\"??\""})
    public void __UTF8_ISO88591_() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __UTF8_ISO88591_BOMUTF16LE() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __UTF8_UTF8_BOMUTF16LE() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __UTF8__BOMUTF16BE() throws Exception {
        charset(null, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __UTF8__BOMUTF16LE() throws Exception {
        charset(null, TestCharset.UTF8, null, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"ä\"", "\"?????\"", "\"???\"", "\"??\""})
    public void ___ISO88591_() throws Exception {
        charset(null, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void ___UTF8_() throws Exception {
        charset(null, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void ____() throws Exception {
        charset(null, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void __ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void __ISO88591__BOMUTF8() throws Exception {
        charset(null, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"ä\"", "\"?????\"", "\"???\"", "\"??\""})
    public void _GB2312_ISO88591_ISO88591_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void _GB2312_ISO88591_UTF8_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented
    public void _GB2312_ISO88591__() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"�\"", "\"?????\"", "\"???\"", "\"??\""})
    public void _GB2312_UTF8_ISO88591_() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"�\"", "\"?????\"", "\"???\"", "\"??\""})
    public void _GB2312__ISO88591_() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"盲\"", "\"兀賴賱丕賸\"", "\"屑懈褉\"", "\"鎴块棿\""})
    @NotYetImplemented
    public void _GB2312__UTF8_() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.UTF8, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\"a\"", "\"盲\"", "\"兀賴賱丕賸\"", "\"屑懈褉\"", "\"鎴块棿\""})
    @NotYetImplemented
    public void _GB2312___() throws Exception {
        charset(TestCharset.GB2312, null, null, null);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void _GB2312_ISO88591_UTF8_BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void _GB2312_ISO88591__BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void _GB2312_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_ISO88591_ISO88591_BOMUTF16LE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_ISO88591_UTF8_BOMUTF16LE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_ISO88591__BOMUTF16BE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_ISO88591__BOMUTF16LE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void _ISO88591_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void _UTF8_ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_ISO88591_ISO88591_BOMUTF16LE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_ISO88591_UTF8_BOMUTF16LE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_ISO88591__BOMUTF16BE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _UTF8_ISO88591__BOMUTF16LE() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"\"a\"", "\"Ã¤\"", "\"Ø£Ù‡Ù„Ø§Ù‹\"", "\"Ð¼Ð¸Ñ€\"", "\"æˆ¿é—´\""})
    @NotYetImplemented(IE)
    public void __ISO88591_ISO88591_BOMUTF8() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_8);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __ISO88591_ISO88591_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __ISO88591_ISO88591_BOMUTF16LE() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __ISO88591_UTF8_BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __ISO88591_UTF8_BOMUTF16LE() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __ISO88591__BOMUTF16BE() throws Exception {
        charset(null, TestCharset.ISO88591, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void __ISO88591__BOMUTF16LE() throws Exception {
        charset(null, TestCharset.ISO88591, null, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_UTF8_ISO88591_BOMUTF16BE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_UTF8_ISO88591_BOMUTF16LE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_UTF8_UTF8_BOMUTF16BE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_UTF8_UTF8_BOMUTF16LE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.UTF8, BOM_UTF_16LE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_UTF8__BOMUTF16BE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, null, BOM_UTF_16BE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"\"a\"", "\"ä\"", "\"أهلاً\"", "\"мир\"", "\"房间\""},
            IE = {"none", "none", "none", "none", "none"})
    @NotYetImplemented(IE)
    public void _GB2312_UTF8__BOMUTF16LE() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, null, BOM_UTF_16LE);
    }
}
