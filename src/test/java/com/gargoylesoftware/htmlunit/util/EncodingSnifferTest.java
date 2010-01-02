/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.util;

import static com.gargoylesoftware.htmlunit.util.EncodingSniffer.extractEncodingFromContentType;
import static com.gargoylesoftware.htmlunit.util.EncodingSniffer.sniffEncodingFromHttpHeaders;
import static com.gargoylesoftware.htmlunit.util.EncodingSniffer.sniffEncodingFromMetaTag;
import static com.gargoylesoftware.htmlunit.util.EncodingSniffer.sniffEncodingFromXmlDeclaration;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link EncodingSniffer}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class EncodingSnifferTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testSniffEncodingFromHttpHeaders() throws Exception {
        testHeader(null, null, null);
        testHeader(null, "foo", "bar");
        testHeader(null, "Content-Type", "blah");
        testHeader(null, "Content-Type", "text/html;charset=blah");
        testHeader("utf-8", "Content-Type", "text/html;charset=utf-8");
        testHeader("utf-8", "Content-Type", "text/html;charset=utf-8;");
    }

    private void testHeader(final String expectedEncoding, final String headerName, final String headerValue) {
        final NameValuePair header = new NameValuePair(headerName, headerValue);
        assertEquals(expectedEncoding, sniffEncodingFromHttpHeaders(singletonList(header)));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testSniffEncodingFromMetaTag() throws Exception {
        testMeta(null, "");
        testMeta(null, "foo");
        testMeta(null, "<!--");
        testMeta(null, " <!-- blah");
        testMeta(null, " <!-- blah --> ");
        testMeta(null, "<");
        testMeta(null, "</");
        testMeta(null, "<meta/>");
        testMeta(null, "<meta />");
        testMeta(null, "<meta blah />");
        testMeta(null, "<meta");
        testMeta(null, "<meta ");
        testMeta(null, "<meta blah");
        testMeta(null, "<meta blah  ");
        testMeta(null, "<meta a='b'");
        testMeta(null, "<meta a='b' c=d e=\"f\"/>");
        testMeta(null, "<meta a='b' c=d e=\"f\" content='text/html; charset=blah' />");
        testMeta("utf-8", "<meta a='b' c=d e=\"f\" content='text/html; charset=utf-8' />");
        testMeta("utf-8", "abc <meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>");
        testMeta("utf-8", "abc <meta http-equiv='Content-Type' content='text/html; CHARSET=UTF-8'/>");
        testMeta("utf-8", "abc <meta http-equiv='Content-Type' content='text/html; chArsEt=UtF-8'/>");
    }

    private void testMeta(final String expectedEncoding, final String content) {
        assertEquals(expectedEncoding, sniffEncodingFromMetaTag(content.getBytes()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testSniffEncodingFromXmlDeclaration() throws Exception {
        testXmlDeclaration(null, "");
        testXmlDeclaration(null, "foo");
        testXmlDeclaration(null, "<?");
        testXmlDeclaration(null, "<?xml");
        testXmlDeclaration(null, "<?xml ");
        testXmlDeclaration(null, "<?xml encoding");
        testXmlDeclaration(null, "<?xml encoding=");
        testXmlDeclaration(null, "<?xml encoding='utf-8");
        testXmlDeclaration(null, "<?xml encoding='utf-8'");
        testXmlDeclaration(null, "<?xml encoding='blah'?>");
        testXmlDeclaration("utf-8", "<?xml encoding='utf-8'?>");
        testXmlDeclaration(null, "<?xml encoding=\"utf-8");
        testXmlDeclaration(null, "<?xml encoding=\"utf-8\"");
        testXmlDeclaration("utf-8", "<?xml encoding=\"utf-8\"?>");
    }

    private void testXmlDeclaration(final String expectedEncoding, final String content) {
        assertEquals(expectedEncoding, sniffEncodingFromXmlDeclaration(content.getBytes()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testExtractEncodingFromContentType() throws Exception {
        testContentType(null, null);
        testContentType(null, "");
        testContentType(null, " \t \n ");
        testContentType(null, "foo");
        testContentType(null, "text/html");
        testContentType(null, "\n text/html \t");
        testContentType(null, "\n text/html ; char \t");
        testContentType(null, "\n text/html ; charset \t");
        testContentType(null, "\n text/html ; charset=");
        testContentType(null, "\n text/html ; charset= \t");
        testContentType(null, "\n text/html ; charset =");
        testContentType(null, "\n text/html ; charset = \n");
        testContentType(null, "\n text/html ; charset=blah");
        testContentType("utf-8", "\n text/html ; charset=utf-8");
        testContentType("utf-8", "\n text/html ; charset=utf-8;");
        testContentType("utf-8", "\n text/html ; charset = \n utf-8 ");
        testContentType("utf-8", "\n text/html ; charset = \n utf-8 ; ");
        testContentType(null, "\n text/html ; charset = \n'");
        testContentType(null, "\n text/html ; charset = \n' ");
        testContentType(null, "\n text/html ; charset = \n' utf-8");
        testContentType("utf-8", "\n text/html ; charset = \n'utf-8'");
        testContentType(null, "\n text/html ; charset = \n\"");
        testContentType(null, "\n text/html ; charset = \n\" ");
        testContentType(null, "\n text/html ; charset = \n\" utf-8");
        testContentType("utf-8", "\n text/html ; charset = \n\"utf-8\"");
    }

    private void testContentType(final String expectedEncoding, final String contentType) {
        assertEquals(expectedEncoding, extractEncodingFromContentType(contentType));
    }

}
