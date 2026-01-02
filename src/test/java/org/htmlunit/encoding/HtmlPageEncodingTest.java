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

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

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
import org.htmlunit.MiniServer;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * Tests encoding handling for html pages.
 *
 * @author Ronald Brill
 */
public class HtmlPageEncodingTest extends WebDriverTestCase {

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

        final TestCharset[] charsetResponseHeader = new TestCharset[]
            {null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.GB2312};
        final TestCharset[] charsetResponseEncoding = new TestCharset[]
            {null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.GB2312};
        final TestCharset[] charsetMeta = new TestCharset[]
            {null, TestCharset.UTF8, TestCharset.ISO88591, TestCharset.GB2312};
        final String[] bom = {null, BOM_UTF_8, BOM_UTF_16LE, BOM_UTF_16BE};

        for (final Object responseHeader : charsetResponseHeader) {
            for (final Object responseEncoding : charsetResponseEncoding) {
                for (final Object meta : charsetMeta) {
                    for (final Object b : bom) {
                        list.add(Arguments.of(responseHeader, responseEncoding, meta, b, false));
                        list.add(Arguments.of(responseHeader, responseEncoding, meta, b, true));
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
    @ParameterizedTest(name = "_{0}_{1}_{2}_{3}_{4}")
    @MethodSource("data")
    @Alerts("a \u00E4 \u0623\u0647\u0644\u0627\u064B \u043C\u0438\u0440 \u623F\u95F4")
    void charset(
            final TestCharset charsetResponseHeader,
            final TestCharset charsetResponseEncoding,
            final TestCharset charsetMeta,
            final String bom,
            final boolean gzip) throws Exception {

        // use always a different url to avoid caching effects
        final URL htmlUrl = new URL(URL_FIRST, System.currentTimeMillis() + ".html");


        String meta = "";
        if (charsetMeta != null) {
            meta = "<meta charset='" + charsetMeta.getCharset().name().toLowerCase() + "' />\n";
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + meta
            + "  <title>a ä أهلاً мир 房间</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";

        String contentType = MimeType.TEXT_HTML;
        if (charsetResponseHeader != null) {
            contentType = contentType + "; charset=" + charsetResponseHeader.getCharset().name().toLowerCase();
        }

        byte[] htmlBytes = null;
        if (charsetResponseEncoding == null) {
            htmlBytes = html.getBytes(UTF_8);
        }
        else {
            htmlBytes = html.getBytes(charsetResponseEncoding.getCharset());
        }

        if (BOM_UTF_8.equals(bom)) {
            htmlBytes = ArrayUtils.addAll(ByteOrderMark.UTF_8.getBytes(), html.getBytes(StandardCharsets.UTF_8));
        }
        else if (BOM_UTF_16BE.equals(bom)) {
            htmlBytes = ArrayUtils.addAll(ByteOrderMark.UTF_16BE.getBytes(), html.getBytes(StandardCharsets.UTF_16BE));
        }
        else if (BOM_UTF_16LE.equals(bom)) {
            htmlBytes = ArrayUtils.addAll(ByteOrderMark.UTF_16LE.getBytes(), html.getBytes(StandardCharsets.UTF_16LE));
        }

        if (gzip) {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final GZIPOutputStream gout = new GZIPOutputStream(bos);
            gout.write(htmlBytes);
            gout.finish();

            final List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair("Content-Encoding", "gzip"));
            getMockWebConnection().setResponse(htmlUrl, bos.toByteArray(), 200, "OK", contentType, headers);
        }
        else {
            getMockWebConnection().setResponse(htmlUrl, htmlBytes, 200, "OK", contentType, null);
        }

        try (MiniServer miniServer1 = new MiniServer(PORT, getMockWebConnection())) {
            miniServer1.start();

            try {
                final WebDriver driver = getWebDriver();
                driver.get(htmlUrl.toExternalForm());

                assertEquals(getExpectedAlerts()[0], driver.getTitle());
            }
            catch (final WebDriverException e) {
                if (!e.getCause().getMessage().contains("illegal character")
                    && !e.getCause().getMessage().contains("is not defined.")) {
                    throw e;
                }

                final String msg = e.getCause().getMessage();
                assertTrue(msg, msg.contains(getExpectedAlerts()[0]));
            }
        }
    }

    @Alerts("a ? ????? \u043C\u0438\u0440 \u623F\u95F4")
    void _GB2312_GB2312_GB2312_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.GB2312, TestCharset.GB2312, null, false);
    }

    @Alerts("a ? ????? \u043C\u0438\u0440 \u623F\u95F4")
    void _GB2312_GB2312_GB2312_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.GB2312, TestCharset.GB2312, null, true);
    }

    @Alerts("a ? ????? \u043C\u0438\u0440 \u623F\u95F4")
    void _GB2312_GB2312_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.GB2312, TestCharset.ISO88591, null, false);
    }

    @Alerts("a ? ????? \u043C\u0438\u0440 \u623F\u95F4")
    void _GB2312_GB2312_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.GB2312, TestCharset.ISO88591, null, true);
    }

    @Alerts("a ? ????? \u043C\u0438\u0440 \u623F\u95F4")
    void _GB2312_GB2312_UTF8_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.GB2312, TestCharset.UTF8, null, false);
    }

    @Alerts("a ? ????? \u043C\u0438\u0440 \u623F\u95F4")
    void _GB2312_GB2312_UTF8_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.GB2312, TestCharset.UTF8, null, true);
    }

    @Alerts("a ? ????? \u043C\u0438\u0440 \u623F\u95F4")
    void _GB2312_GB2312_null_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.GB2312, null, null, false);
    }

    @Alerts("a ? ????? \u043C\u0438\u0440 \u623F\u95F4")
    void _GB2312_GB2312_null_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.GB2312, null, null, true);
    }

    @Alerts("a � ????? ??? ??")
    void _GB2312_ISO88591_GB2312_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.GB2312, null, false);
    }

    @Alerts("a � ????? ??? ??")
    void _GB2312_ISO88591_GB2312_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.GB2312, null, true);
    }

    @Alerts("a � ????? ??? ??")
    void _GB2312_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts("a � ????? ??? ??")
    void _GB2312_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts("a � ????? ??? ??")
    void _GB2312_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts("a � ????? ??? ??")
    void _GB2312_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts("a � ????? ??? ??")
    void _GB2312_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, null, false);
    }

    @Alerts("a � ????? ??? ??")
    void _GB2312_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.ISO88591, null, null, true);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_UTF8_GB2312_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.GB2312, null, false);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_UTF8_GB2312_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.GB2312, null, true);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_UTF8_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_UTF8_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_UTF8_UTF8_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.UTF8, null, false);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_UTF8_UTF8_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, TestCharset.UTF8, null, true);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_UTF8_null_null_false() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, null, null, false);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_UTF8_null_null_true() throws Exception {
        charset(TestCharset.GB2312, TestCharset.UTF8, null, null, true);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_null_GB2312_null_false() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.GB2312, null, false);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_null_GB2312_null_true() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.GB2312, null, true);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_null_ISO88591_null_false() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, null, false);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_null_ISO88591_null_true() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.ISO88591, null, true);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_null_UTF8_null_false() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.UTF8, null, false);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_null_UTF8_null_true() throws Exception {
        charset(TestCharset.GB2312, null, TestCharset.UTF8, null, true);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_null_null_null_false() throws Exception {
        charset(TestCharset.GB2312, null, null, null, false);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _GB2312_null_null_null_true() throws Exception {
        charset(TestCharset.GB2312, null, null, null, true);
    }

    @Alerts("a ? ????? §Þ§Ú§â ·¿¼ä")
    void _ISO88591_GB2312_GB2312_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.GB2312, TestCharset.GB2312, null, false);
    }

    @Alerts("a ? ????? §Þ§Ú§â ·¿¼ä")
    void _ISO88591_GB2312_GB2312_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.GB2312, TestCharset.GB2312, null, true);
    }

    @Alerts("a ? ????? §Þ§Ú§â ·¿¼ä")
    void _ISO88591_GB2312_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.GB2312, TestCharset.ISO88591, null, false);
    }

    @Alerts("a ? ????? §Þ§Ú§â ·¿¼ä")
    void _ISO88591_GB2312_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.GB2312, TestCharset.ISO88591, null, true);
    }

    @Alerts("a ? ????? §Þ§Ú§â ·¿¼ä")
    void _ISO88591_GB2312_UTF8_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.GB2312, TestCharset.UTF8, null, false);
    }

    @Alerts("a ? ????? §Þ§Ú§â ·¿¼ä")
    void _ISO88591_GB2312_UTF8_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.GB2312, TestCharset.UTF8, null, true);
    }

    @Alerts("a ? ????? §Þ§Ú§â ·¿¼ä")
    void _ISO88591_GB2312_null_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.GB2312, null, null, false);
    }

    @Alerts("a ? ????? §Þ§Ú§â ·¿¼ä")
    void _ISO88591_GB2312_null_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.GB2312, null, null, true);
    }

    @Alerts("a ä ????? ??? ??")
    void _ISO88591_ISO88591_GB2312_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.GB2312, null, false);
    }

    @Alerts("a ä ????? ??? ??")
    void _ISO88591_ISO88591_GB2312_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.GB2312, null, true);
    }

    @Alerts("a ä ????? ??? ??")
    void _ISO88591_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts("a ä ????? ??? ??")
    void _ISO88591_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts("a ä ????? ??? ??")
    void _ISO88591_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts("a ä ????? ??? ??")
    void _ISO88591_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts("a ä ????? ??? ??")
    void _ISO88591_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, null, false);
    }

    @Alerts("a ä ????? ??? ??")
    void _ISO88591_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.ISO88591, null, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_UTF8_GB2312_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.GB2312, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_UTF8_GB2312_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.GB2312, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_UTF8_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_UTF8_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_UTF8_UTF8_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_UTF8_UTF8_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, TestCharset.UTF8, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_UTF8_null_null_false() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_UTF8_null_null_true() throws Exception {
        charset(TestCharset.ISO88591, TestCharset.UTF8, null, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_null_GB2312_null_false() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.GB2312, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_null_GB2312_null_true() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.GB2312, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_null_ISO88591_null_false() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_null_ISO88591_null_true() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.ISO88591, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_null_UTF8_null_false() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.UTF8, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_null_UTF8_null_true() throws Exception {
        charset(TestCharset.ISO88591, null, TestCharset.UTF8, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_null_null_null_false() throws Exception {
        charset(TestCharset.ISO88591, null, null, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _ISO88591_null_null_null_true() throws Exception {
        charset(TestCharset.ISO88591, null, null, null, true);
    }

    @Alerts("a ? ????? �ާڧ� ����")
    void _UTF8_GB2312_GB2312_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.GB2312, TestCharset.GB2312, null, false);
    }

    @Alerts("a ? ????? �ާڧ� ����")
    void _UTF8_GB2312_GB2312_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.GB2312, TestCharset.GB2312, null, true);
    }

    @Alerts("a ? ????? �ާڧ� ����")
    void _UTF8_GB2312_ISO88591_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.GB2312, TestCharset.ISO88591, null, false);
    }

    @Alerts("a ? ????? �ާڧ� ����")
    void _UTF8_GB2312_ISO88591_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.GB2312, TestCharset.ISO88591, null, true);
    }

    @Alerts("a ? ????? �ާڧ� ����")
    void _UTF8_GB2312_UTF8_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.GB2312, TestCharset.UTF8, null, false);
    }

    @Alerts("a ? ????? �ާڧ� ����")
    void _UTF8_GB2312_UTF8_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.GB2312, TestCharset.UTF8, null, true);
    }

    @Alerts("a ? ????? �ާڧ� ����")
    void _UTF8_GB2312_null_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.GB2312, null, null, false);
    }

    @Alerts("a ? ????? �ާڧ� ����")
    void _UTF8_GB2312_null_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.GB2312, null, null, true);
    }

    @Alerts("a � ????? ??? ??")
    void _UTF8_ISO88591_GB2312_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.GB2312, null, false);
    }

    @Alerts("a � ????? ??? ??")
    void _UTF8_ISO88591_GB2312_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.GB2312, null, true);
    }

    @Alerts("a � ????? ??? ??")
    void _UTF8_ISO88591_ISO88591_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts("a � ????? ??? ??")
    void _UTF8_ISO88591_ISO88591_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts("a � ????? ??? ??")
    void _UTF8_ISO88591_UTF8_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts("a � ????? ??? ??")
    void _UTF8_ISO88591_UTF8_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts("a � ????? ??? ??")
    void _UTF8_ISO88591_null_null_false() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, null, false);
    }

    @Alerts("a � ????? ??? ??")
    void _UTF8_ISO88591_null_null_true() throws Exception {
        charset(TestCharset.UTF8, TestCharset.ISO88591, null, null, true);
    }

    @Alerts("a ? ????? мир 房间")
    void _null_GB2312_GB2312_null_false() throws Exception {
        charset(null, TestCharset.GB2312, TestCharset.GB2312, null, false);
    }

    @Alerts("a ? ????? мир 房间")
    void _null_GB2312_GB2312_null_true() throws Exception {
        charset(null, TestCharset.GB2312, TestCharset.GB2312, null, true);
    }

    @Alerts("a ? ????? §Þ§Ú§â ·¿¼ä")
    void _null_GB2312_ISO88591_null_false() throws Exception {
        charset(null, TestCharset.GB2312, TestCharset.ISO88591, null, false);
    }

    @Alerts("a ? ????? §Þ§Ú§â ·¿¼ä")
    void _null_GB2312_ISO88591_null_true() throws Exception {
        charset(null, TestCharset.GB2312, TestCharset.ISO88591, null, true);
    }

    @Alerts("a ? ????? �ާڧ� ����")
    void _null_GB2312_UTF8_null_false() throws Exception {
        charset(null, TestCharset.GB2312, TestCharset.UTF8, null, false);
    }

    @Alerts("a ? ????? �ާڧ� ����")
    void _null_GB2312_UTF8_null_true() throws Exception {
        charset(null, TestCharset.GB2312, TestCharset.UTF8, null, true);
    }

    @Alerts(DEFAULT = "a ? ????? §Þ§Ú§â ·¿¼ä",
            FF = "a ? ????? 技我把 滇潔",
            FF_ESR = "a ? ????? 技我把 滇潔")
    @HtmlUnitNYI(FF = "a ? ????? §Þ§Ú§â ·¿¼ä",
            FF_ESR = "a ? ????? §Þ§Ú§â ·¿¼ä")
    void _null_GB2312_null_null_false() throws Exception {
        charset(null, TestCharset.GB2312, null, null, false);
    }

    @Alerts(DEFAULT = "a ? ????? §Þ§Ú§â ·¿¼ä",
            FF = "a ? ????? 技我把 滇潔",
            FF_ESR = "a ? ????? 技我把 滇潔")
    @HtmlUnitNYI(FF = "a ? ????? §Þ§Ú§â ·¿¼ä",
            FF_ESR = "a ? ????? §Þ§Ú§â ·¿¼ä")
    void _null_GB2312_null_null_true() throws Exception {
        charset(null, TestCharset.GB2312, null, null, true);
    }

    @Alerts("a � ????? ??? ??")
    void _null_ISO88591_GB2312_null_false() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.GB2312, null, false);
    }

    @Alerts("a � ????? ??? ??")
    void _null_ISO88591_GB2312_null_true() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.GB2312, null, true);
    }

    @Alerts("a ä ????? ??? ??")
    void _null_ISO88591_ISO88591_null_false() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, null, false);
    }

    @Alerts("a ä ????? ??? ??")
    void _null_ISO88591_ISO88591_null_true() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.ISO88591, null, true);
    }

    @Alerts("a � ????? ??? ??")
    void _null_ISO88591_UTF8_null_false() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, null, false);
    }

    @Alerts("a � ????? ??? ??")
    void _null_ISO88591_UTF8_null_true() throws Exception {
        charset(null, TestCharset.ISO88591, TestCharset.UTF8, null, true);
    }

    @Alerts("a ä ????? ??? ??")
    void _null_ISO88591_null_null_false() throws Exception {
        charset(null, TestCharset.ISO88591, null, null, false);
    }

    @Alerts("a ä ????? ??? ??")
    void _null_ISO88591_null_null_true() throws Exception {
        charset(null, TestCharset.ISO88591, null, null, true);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _null_UTF8_GB2312_null_true() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.GB2312, null, true);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _null_UTF8_GB2312_null_false() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.GB2312, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _null_UTF8_ISO88591_null_true() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _null_UTF8_ISO88591_null_false() throws Exception {
        charset(null, TestCharset.UTF8, TestCharset.ISO88591, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _null_UTF8_null_null_true() throws Exception {
        charset(null, TestCharset.UTF8, null, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _null_UTF8_null_null_false() throws Exception {
        charset(null, TestCharset.UTF8, null, null, false);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _null_null_GB2312_null_false() throws Exception {
        charset(null, null, TestCharset.GB2312, null, false);
    }

    @Alerts("a 盲 兀賴賱丕賸 屑懈褉 鎴块棿")
    void _null_null_GB2312_null_true() throws Exception {
        charset(null, null, TestCharset.GB2312, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _null_null_ISO88591_null_false() throws Exception {
        charset(null, null, TestCharset.ISO88591, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _null_null_ISO88591_null_true() throws Exception {
        charset(null, null, TestCharset.ISO88591, null, true);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _null_null_null_null_false() throws Exception {
        charset(null, null, null, null, false);
    }

    @Alerts("a Ã¤ Ø£Ù‡Ù„Ø§Ù‹ Ð¼Ð¸Ñ€ æˆ¿é—´")
    void _null_null_null_null_true() throws Exception {
        charset(null, null, null, null, true);
    }
}
