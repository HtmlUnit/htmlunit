/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.lang3.ArrayUtils;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebTestCase;
import org.htmlunit.html.HtmlScript;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.MiniServer;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * Tests encoding handling for {@link HtmlScript}.
 *
 * @author Ronald Brill
 */
public class ScriptEncodingTest extends WebDriverTestCase {

    private static final String BOM_UTF_16LE = "BOMUTF16LE";
    private static final String BOM_UTF_16BE = "BOMUTF16BE";
    private static final String BOM_UTF_8 = "BOMUTF8";

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
    public static Collection<Arguments> data() throws Exception {
        final List<Arguments> list = new ArrayList<>();

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
                            list.add(Arguments.of(charsetHtml, attribute, responseHeader, responseEncoding, b, false));
                            list.add(Arguments.of(charsetHtml, attribute, responseHeader, responseEncoding, b, true));
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
    @ParameterizedTest(name = "_{0}_{1}_{2}_{3}_{4}_{5}", quoteTextArguments = false)
    @MethodSource("data")
    @Alerts({"a", "ä", "أهلاً", "мир", "房间"})
    void charset(
            final TestCharset charsetHtmlResponse,
            final TestCharset charsetAttribute,
            final TestCharset charsetResponseHeader,
            final TestCharset charsetResponseEncoding,
            final String bom,
            final boolean gzip) throws Exception {

        // use always a different url to avoid caching effects
        final URL scriptUrl = new URL(URL_SECOND, System.currentTimeMillis() + ".js");

        String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "  <script>var logMsg = ''; function log(msg) { logMsg += msg + '\\xA7\\xA7'; }</script>\n"
            + "  <script type='text/javascript'>window.onerror=function(msg) { log(msg); }</script>"
            + "  <script src='" + scriptUrl + "'";
        if (charsetAttribute != null) {
            html = html + " charset='" + charsetAttribute.getCharset().name().toLowerCase() + "'";
        }
        html = html + "></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";

        String scriptContentType = MimeType.TEXT_JAVASCRIPT;
        if (charsetResponseHeader != null) {
            scriptContentType = scriptContentType + "; charset="
                                    + charsetResponseHeader.getCharset().name().toLowerCase();
        }
        final String js = "log('a'); log('ä'); log('أهلاً'); log('мир'); log('房间');";

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
        if (gzip) {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final GZIPOutputStream gout = new GZIPOutputStream(bos);
            gout.write(script);
            gout.finish();

            final List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair("Content-Encoding", "gzip"));
            getMockWebConnection().setResponse(scriptUrl, bos.toByteArray(), 200, "OK", scriptContentType, headers);
        }
        else {
            getMockWebConnection().setResponse(scriptUrl, script, 200, "OK", scriptContentType, null);
        }

        String htmlContentType = MimeType.TEXT_HTML;
        Charset htmlResponseCharset = ISO_8859_1;
        if (charsetHtmlResponse != null) {
            htmlContentType = htmlContentType + "; charset=" + charsetHtmlResponse.getCharset().name();
            htmlResponseCharset = charsetHtmlResponse.getCharset();
        }

        getMockWebConnection().setResponse(URL_FIRST,
                html.getBytes(htmlResponseCharset), 200, "OK", htmlContentType, null);

        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedAlerts = getExpectedAlerts();



        try (MiniServer miniServer1 = new MiniServer(PORT, getMockWebConnection())) {
            miniServer1.start();

            try {
                final WebDriver driver = getWebDriver();
                driver.get(WebTestCase.URL_FIRST.toExternalForm());

                final String logMsg = (String) ((JavascriptExecutor) driver).executeScript("return logMsg;");
                final String[] actualAlerts = logMsg.split("§§");

                if (actualAlerts.length == 1) {
                    assertEquals(1, actualAlerts.length);

                    final String msg = actualAlerts[0];
                    assertEquals(expectedAlerts[0], "Invalid token");
                    assertTrue(msg, msg.contains("Invalid or unexpected token")
                                    || msg.contains("illegal character")
                                    || msg.contains("Ungültiges Zeichen"));
                }
                else {
                    assertArrayEquals(getExpectedAlerts(), actualAlerts);
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
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_null_null_null_null_false() throws Exception {
        charset(TestCharset.ISO88591, null, null, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_null_null_null_null_true() throws Exception {
        charset(TestCharset.ISO88591, null, null, null, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_null_null_UTF8_null_false() throws Exception {
        charset(TestCharset.ISO88591, null, null, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_null_null_UTF8_null_true() throws Exception {
        charset(TestCharset.ISO88591, null, null, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _ISO88591_null_null_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, null, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _ISO88591_null_null_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, null, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _ISO88591_null_UTF8_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _ISO88591_null_UTF8_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_null_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_null_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_null_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_null_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _ISO88591_null_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _ISO88591_null_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _ISO88591_UTF8_null_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _ISO88591_UTF8_null_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _ISO88591_UTF8_UTF8_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _ISO88591_UTF8_UTF8_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_UTF8_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_UTF8_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_UTF8_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_UTF8_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _ISO88591_UTF8_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _ISO88591_UTF8_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_ISO88591_null_null_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_ISO88591_null_null_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, null, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_ISO88591_null_UTF8_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_ISO88591_null_UTF8_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _ISO88591_ISO88591_null_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _ISO88591_ISO88591_null_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _ISO88591_ISO88591_UTF8_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _ISO88591_ISO88591_UTF8_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_ISO88591_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_ISO88591_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_ISO88591_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _ISO88591_ISO88591_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _ISO88591_ISO88591_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _ISO88591_ISO88591_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _UTF8_ISO88591_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _UTF8_ISO88591_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_ISO88591_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_ISO88591_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_ISO88591_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_ISO88591_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _UTF8_ISO88591_UTF8_ISO88591_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _UTF8_ISO88591_UTF8_ISO88591_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _UTF8_ISO88591_null_ISO88591_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _UTF8_ISO88591_null_ISO88591_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_ISO88591_null_UTF8_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_ISO88591_null_UTF8_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_ISO88591_null_null_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_ISO88591_null_null_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, null, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _UTF8_UTF8_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _UTF8_UTF8_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_UTF8_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_UTF8_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_UTF8_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_UTF8_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _UTF8_UTF8_UTF8_ISO88591_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _UTF8_UTF8_UTF8_ISO88591_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _UTF8_UTF8_null_ISO88591_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _UTF8_UTF8_null_ISO88591_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.UTF8, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _UTF8_null_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _UTF8_null_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_null_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_null_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_null_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _UTF8_null_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _UTF8_null_UTF8_ISO88591_null_false() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _UTF8_null_UTF8_ISO88591_null_true() throws Exception {
        charset(TestCharset.UTF8, null, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _UTF8_null_null_ISO88591_null_false() throws Exception {
        charset(TestCharset.UTF8, null, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _UTF8_null_null_ISO88591_null_true() throws Exception {
        charset(TestCharset.UTF8, null, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _null_ISO88591_ISO88591_ISO88591_null_false() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _null_ISO88591_ISO88591_ISO88591_null_true() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_ISO88591_ISO88591_UTF8_null_false() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_ISO88591_ISO88591_UTF8_null_true() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_ISO88591_ISO88591_null_null_false() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_ISO88591_ISO88591_null_null_true() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _null_ISO88591_UTF8_ISO88591_null_false() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _null_ISO88591_UTF8_ISO88591_null_true() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _null_ISO88591_null_ISO88591_null_false() throws Exception {
        charset(null, TestCharset.ISO88591, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _null_ISO88591_null_ISO88591_null_true() throws Exception {
        charset(null, TestCharset.ISO88591, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_ISO88591_null_UTF8_null_false() throws Exception {
        charset(null, TestCharset.ISO88591, null, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_ISO88591_null_UTF8_null_true() throws Exception {
        charset(null, TestCharset.ISO88591, null, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_ISO88591_null_null_null_false() throws Exception {
        charset(null, TestCharset.ISO88591, null, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_ISO88591_null_null_null_true() throws Exception {
        charset(null, TestCharset.ISO88591, null, null, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _null_UTF8_ISO88591_ISO88591_null_false() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _null_UTF8_ISO88591_ISO88591_null_true() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_UTF8_ISO88591_UTF8_null_false() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_UTF8_ISO88591_UTF8_null_true() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_UTF8_ISO88591_null_null_false() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_UTF8_ISO88591_null_null_true() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _null_UTF8_UTF8_ISO88591_null_false() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _null_UTF8_UTF8_ISO88591_null_true() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _null_UTF8_null_ISO88591_null_false() throws Exception {
        charset(null, TestCharset.UTF8, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _null_UTF8_null_ISO88591_null_true() throws Exception {
        charset(null, TestCharset.UTF8, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _null_null_ISO88591_ISO88591_null_false() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _null_null_ISO88591_ISO88591_null_true() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_null_ISO88591_UTF8_null_false() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_null_ISO88591_UTF8_null_true() throws Exception {
        charset(null, null, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_null_ISO88591_null_null_false() throws Exception {
        charset(null, null, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _null_null_ISO88591_null_null_true() throws Exception {
        charset(null, null, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _null_null_UTF8_ISO88591_null_false() throws Exception {
        charset(null, null, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _null_null_UTF8_ISO88591_null_true() throws Exception {
        charset(null, null, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _null_null_null_ISO88591_null_false() throws Exception {
        charset(null, null, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _null_null_null_ISO88591_null_true() throws Exception {
        charset(null, null, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    @HtmlUnitNYI(CHROME = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            EDGE = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            FF = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            FF_ESR = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"})
    void _null_null_null_UTF8_null_false() throws Exception {
        charset(null, null, null, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    @HtmlUnitNYI(CHROME = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            EDGE = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            FF = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            FF_ESR = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"})
    void _null_null_null_UTF8_null_true() throws Exception {
        charset(null, null, null, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    @HtmlUnitNYI(CHROME = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            EDGE = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            FF = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            FF_ESR = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"})
    void _null_null_null_null_null_false() throws Exception {
        charset(null, null, null, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    @HtmlUnitNYI(CHROME = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            EDGE = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            FF = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"},
            FF_ESR = {"a", "Ã¤", "Ø£ÙÙØ§Ù", "Ð¼Ð¸Ñ", "æ¿é´"})
    void _null_null_null_null_null_true() throws Exception {
        charset(null, null, null, null, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _GB2312_ISO88591_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _GB2312_ISO88591_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_ISO88591_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_ISO88591_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_ISO88591_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_ISO88591_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _GB2312_ISO88591_UTF8_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _GB2312_ISO88591_UTF8_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _GB2312_ISO88591_null_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _GB2312_ISO88591_null_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_ISO88591_null_UTF8_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_ISO88591_null_UTF8_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_ISO88591_null_null_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_ISO88591_null_null_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, null, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _GB2312_UTF8_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _GB2312_UTF8_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_UTF8_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_UTF8_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_UTF8_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_UTF8_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _GB2312_UTF8_UTF8_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _GB2312_UTF8_UTF8_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _GB2312_UTF8_null_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _GB2312_UTF8_null_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _GB2312_null_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "ä", "?????", "???", "??"})
    void _GB2312_null_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_null_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_null_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_null_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, null, null, false);
    }

    @Alerts({"a", "Ã¤", "Ø£Ù‡Ù„Ø§Ù‹", "Ð¼Ð¸Ñ€", "æˆ¿é—´"})
    void _GB2312_null_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, null, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _GB2312_null_UTF8_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _GB2312_null_UTF8_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _GB2312_null_null_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, null, null, TestCharset.ISO88591, null, false);
    }

    @Alerts({"a", "�", "?????", "???", "??"})
    void _GB2312_null_null_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, null, null, TestCharset.ISO88591, null, true);
    }

    @Alerts({"a", "盲", "兀賴賱丕賸", "屑懈褉", "鎴块棿"})
    void _GB2312_null_null_UTF8_null_false() throws Exception {
        charset(TestCharset.GB2312, null, null, TestCharset.UTF8, null, false);
    }

    @Alerts({"a", "盲", "兀賴賱丕賸", "屑懈褉", "鎴块棿"})
    void _GB2312_null_null_UTF8_null_true() throws Exception {
        charset(TestCharset.GB2312, null, null, TestCharset.UTF8, null, true);
    }

    @Alerts({"a", "盲", "兀賴賱丕賸", "屑懈褉", "鎴块棿"})
    void _GB2312_null_null_null_null_false() throws Exception {
        charset(TestCharset.GB2312, null, null, null, null, false);
    }

    @Alerts({"a", "盲", "兀賴賱丕賸", "屑懈褉", "鎴块棿"})
    void _GB2312_null_null_null_null_true() throws Exception {
        charset(TestCharset.GB2312, null, null, null, null, true);
    }
}
