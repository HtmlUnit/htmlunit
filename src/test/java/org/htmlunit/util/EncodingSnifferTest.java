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
package org.htmlunit.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.htmlunit.util.EncodingSniffer.extractEncodingFromContentType;
import static org.htmlunit.util.EncodingSniffer.sniffEncodingFromCssDeclaration;
import static org.htmlunit.util.EncodingSniffer.sniffEncodingFromMetaTag;
import static org.htmlunit.util.EncodingSniffer.sniffEncodingFromXmlDeclaration;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.htmlunit.HttpHeader;
import org.junit.Test;

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

}
