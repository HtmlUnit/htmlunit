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
package org.htmlunit.util;

import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_16LE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.htmlunit.util.EncodingSniffer.contentTypeEndsWith;
import static org.htmlunit.util.EncodingSniffer.extractEncodingFromContentType;
import static org.htmlunit.util.EncodingSniffer.sniffEncodingFromCssDeclaration;
import static org.htmlunit.util.EncodingSniffer.sniffEncodingFromMetaTag;
import static org.htmlunit.util.EncodingSniffer.sniffEncodingFromUnicodeBom;
import static org.htmlunit.util.EncodingSniffer.sniffEncodingFromXmlDeclaration;
import static org.htmlunit.util.EncodingSniffer.toCharset;
import static org.htmlunit.util.EncodingSniffer.translateEncodingLabel;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.htmlunit.HttpHeader;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link EncodingSniffer}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
public class EncodingSnifferTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromHttpHeaders() throws Exception {
        header(null, null, null);
        header(null, "foo", "bar");
        header(null, HttpHeader.CONTENT_TYPE, "blah");

        header(null, HttpHeader.CONTENT_TYPE, "text/html;charset=blah");
        header(UTF_8, HttpHeader.CONTENT_TYPE, "text/html;charset=utf-8");
        header(UTF_8, HttpHeader.CONTENT_TYPE, "text/html;charset=utf-8;");

        header(UTF_8, HttpHeader.CONTENT_TYPE, "text/xml;charset=UTF-8");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromHttpHeadersNoContentType() throws Exception {
        header(null, HttpHeader.CONTENT_TYPE, "charset=blah");
        header(UTF_8, HttpHeader.CONTENT_TYPE, "charset=utf-8");
        header(UTF_8, HttpHeader.CONTENT_TYPE, ";charset=utf-8;");
    }

    private static void header(final Charset expectedEncoding, final String headerName, final String headerValue) {
        assertSame(expectedEncoding, extractEncodingFromContentType(headerValue));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromMetaTag() throws Exception {
        meta(null, "");
        meta(null, "foo");
        meta(null, "<!--");
        meta(null, " <!-- blah");
        meta(null, " <!-- blah --> ");
        meta(null, "<");
        meta(null, "</");
        meta(null, "<meta/>");
        meta(null, "<meta />");
        meta(null, "<meta blah />");
        meta(null, "<meta");
        meta(null, "<meta ");
        meta(null, "<meta blah");
        meta(null, "<meta blah  ");
        meta(null, "<meta a='b'");
        meta(null, "<meta a='b' c=d e=\"f\"/>");
        meta(null, "<meta a='b' c=d e=\"f\" content='text/html; charset=blah' />");
        meta(UTF_8, "<meta a='b' c=d e=\"f\" content='text/html; charset=utf-8' />");
        meta(UTF_8, "abc <meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>");
        meta(UTF_8, "abc <meta http-equiv='Content-Type' content='text/html; CHARSET=UTF-8'/>");
        meta(UTF_8, "abc <meta http-equiv='Content-Type' content='text/html; chArsEt=UtF-8'/>");
        meta(UTF_8, "<meta a='b' c=d e=\"f\" CONTENT='text/html; CHARSET=utf-8' />");
    }

    private static void meta(final Charset expectedEncoding, final String content) throws Exception {
        assertSame(expectedEncoding, sniffEncodingFromMetaTag(new ByteArrayInputStream(content.getBytes())));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromXmlDeclaration() throws Exception {
        xmlDeclaration(null, "");
        xmlDeclaration(null, "foo");
        xmlDeclaration(null, "<?");
        xmlDeclaration(null, "<?xml");
        xmlDeclaration(null, "<?xml ");
        xmlDeclaration(null, "<?xml encoding");
        xmlDeclaration(null, "<?xml encoding=");
        xmlDeclaration(null, "<?xml encoding='utf-8");
        xmlDeclaration(null, "<?xml encoding='utf-8'");
        xmlDeclaration(null, "<?xml encoding='blah'?>");
        xmlDeclaration(UTF_8, "<?xml encoding='utf-8'?>");
        xmlDeclaration(null, "<?xml encoding=\"utf-8");
        xmlDeclaration(null, "<?xml encoding=\"utf-8\"");
        xmlDeclaration(UTF_8, "<?xml encoding=\"utf-8\"?>");
    }

    private static void xmlDeclaration(final Charset expectedEncoding, final String content) throws Exception {
        assertSame(expectedEncoding, sniffEncodingFromXmlDeclaration(new ByteArrayInputStream(content.getBytes())));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromCssDeclaration() throws Exception {
        cssDeclaration(null, "");
        cssDeclaration(null, "foo");
        cssDeclaration(null, "@charset");
        cssDeclaration(null, "@charset \"utf-8");
        cssDeclaration(null, "@charset \"utf-8\"");
        cssDeclaration(null, "@charset\"utf-8\";");
        cssDeclaration(null, "@charset 'utf-8';");
        cssDeclaration(UTF_8, "@charset \"utf-8\";");
        cssDeclaration(null, " @charset \"utf-8\";");
        cssDeclaration(null, "@charset \"blah\";");
    }

    private static void cssDeclaration(final Charset expectedEncoding, final String content) throws Exception {
        assertSame(expectedEncoding, sniffEncodingFromCssDeclaration(new ByteArrayInputStream(content.getBytes())));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromContentType() throws Exception {
        contentType(null, null);
        contentType(null, "");
        contentType(null, " \t \n ");
        contentType(null, "foo");
        contentType(null, MimeType.TEXT_HTML);
        contentType(null, "\n text/html \t");
        contentType(null, "\n text/html ; char \t");
        contentType(null, "\n text/html ; charset \t");
        contentType(null, "\n text/html ; charset=");
        contentType(null, "\n text/html ; charset= \t");
        contentType(null, "\n text/html ; charset =");
        contentType(null, "\n text/html ; charset = \n");
        contentType(null, "\n text/html ; charset=blah");
        contentType(UTF_8, "\n text/html ; charset=utf-8");
        contentType(UTF_8, "\n text/html ; charset=utf-8;");
        contentType(UTF_8, "\n text/html ; charset = \n utf-8 ");
        contentType(UTF_8, "\n text/html ; charset = \n utf-8 ; ");
        contentType(null, "\n text/html ; charset = \n'");
        contentType(null, "\n text/html ; charset = \n' ");
        contentType(null, "\n text/html ; charset = \n' utf-8");
        contentType(UTF_8, "\n text/html ; charset = \n'utf-8'");
        contentType(null, "\n text/html ; charset = \n\"");
        contentType(null, "\n text/html ; charset = \n\" ");
        contentType(null, "\n text/html ; charset = \n\" utf-8");
        contentType(UTF_8, "\n text/html ; charset = \n\"utf-8\"");
    }

    private static void contentType(final Charset expectedEncoding, final String contentType) {
        assertSame(expectedEncoding, extractEncodingFromContentType(contentType));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromUnicodeBom_utf8() throws Exception {
        final byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'x'};
        assertSame(UTF_8, sniffEncodingFromUnicodeBom(bom));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromUnicodeBom_utf16be() throws Exception {
        final byte[] bom = {(byte) 0xFE, (byte) 0xFF, 'x'};
        assertSame(UTF_16BE, sniffEncodingFromUnicodeBom(bom));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromUnicodeBom_utf16le() throws Exception {
        final byte[] bom = {(byte) 0xFF, (byte) 0xFE, 'x'};
        assertSame(UTF_16LE, sniffEncodingFromUnicodeBom(bom));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromUnicodeBom_noBom_returnsNull() throws Exception {
        final byte[] plain = {'<', 'h', 't', 'm', 'l'};
        assertNull(sniffEncodingFromUnicodeBom(plain));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromUnicodeBom_nullInput_returnsNull() throws Exception {
        assertNull(sniffEncodingFromUnicodeBom(null));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromUnicodeBom_emptyInput_returnsNull() throws Exception {
        assertNull(sniffEncodingFromUnicodeBom(new byte[0]));
    }

    /**
     * A buffer shorter than a full BOM must not throw, and must report no
     * match.
     * @throws Exception if an error occurs
     */
    @Test
    public void fromUnicodeBom_tooShortForBom_returnsNull() throws Exception {
        final byte[] tooShort = {(byte) 0xEF, (byte) 0xBB}; // UTF-8 BOM is 3 bytes
        assertNull(sniffEncodingFromUnicodeBom(tooShort));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentTypeEndsWith_matches() throws Exception {
        final List<NameValuePair> headers = Collections.singletonList(
                new NameValuePair(HttpHeader.CONTENT_TYPE, "application/xhtml+xml"));

        assertTrue(contentTypeEndsWith(headers, "+xml"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentTypeEndsWith_ignoresCharsetParameter() throws Exception {
        final List<NameValuePair> headers = Collections.singletonList(
                new NameValuePair(HttpHeader.CONTENT_TYPE, "application/xhtml+xml; charset=utf-8"));

        assertTrue(contentTypeEndsWith(headers, "+xml"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentTypeEndsWith_noMatch_returnsFalse() throws Exception {
        final List<NameValuePair> headers = Collections.singletonList(
                new NameValuePair(HttpHeader.CONTENT_TYPE, "text/html"));

        assertFalse(contentTypeEndsWith(headers, "+xml"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentTypeEndsWith_noContentTypeHeader_returnsFalse() throws Exception {
        final List<NameValuePair> headers = Collections.singletonList(
                new NameValuePair("X-Other", "application/xhtml+xml"));

        assertFalse(contentTypeEndsWith(headers, "+xml"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentTypeEndsWith_caseInsensitive() throws Exception {
        final List<NameValuePair> headers = Collections.singletonList(
                new NameValuePair(HttpHeader.CONTENT_TYPE, "APPLICATION/XHTML+XML"));

        assertTrue(contentTypeEndsWith(headers, "+xml"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromMetaTag_charsetXUserDefined_mapsToWindows1252() throws Exception {
        meta(Charset.forName("windows-1252"), "<meta charset='x-user-defined'/>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromMetaTag_contentXUserDefined_mapsToWindows1252() throws Exception {
        meta(Charset.forName("windows-1252"),
                "<meta http-equiv='Content-Type' content='text/html; charset=x-user-defined'/>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromMetaTag_charsetUtf16be_remappedToUtf8() throws Exception {
        meta(UTF_8, "<meta charset='utf-16be'/>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromMetaTag_charsetUtf16le_remappedToUtf8() throws Exception {
        meta(UTF_8, "<meta charset='utf-16le'/>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromXmlDeclaration_encodingWordWithNoQuoteAtAll_doesNotThrow() throws Exception {
        xmlDeclaration(null, "<?xml version=\"1.0\" encoding standalone=\"no\"?>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromXmlDeclaration_encodingAsSubstringNoQuote_doesNotThrow() throws Exception {
        xmlDeclaration(null, "<?xml version=\"1.0\" fooencodingbar?>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromXmlDeclaration_openingQuoteButNoClosingQuote_doesNotThrow() throws Exception {
        xmlDeclaration(null, "<?xml version=\"1.0\" encoding=\"utf-8?>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void toCharset_validLabel() throws Exception {
        assertSame(UTF_8, toCharset("utf-8"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void toCharset_unknownLabel_returnsNull() throws Exception {
        assertNull(toCharset("this-is-not-a-real-charset"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void toCharset_nullOrEmpty_returnsNull() throws Exception {
        assertNull(toCharset(null));
        assertNull(toCharset(""));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void translateEncodingLabel_knownAlias() throws Exception {
        // "latin1" is a common alias that should normalize to a real charset name
        assertTrue(translateEncodingLabel("latin1") != null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void translateEncodingLabel_unknownLabel_returnsNull() throws Exception {
        assertNull(translateEncodingLabel("this-is-not-a-real-charset"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void translateEncodingLabel_nullOrEmpty_returnsNull() throws Exception {
        assertNull(translateEncodingLabel(null));
        assertNull(translateEncodingLabel(""));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void fromCssDeclaration_noClosingQuote_returnsNull() throws Exception {
        cssDeclaration(null, "@charset \"utf-8");
    }
}
