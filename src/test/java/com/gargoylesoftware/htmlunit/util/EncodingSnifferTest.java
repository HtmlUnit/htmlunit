/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
    public void fromHttpHeaders() throws Exception {
        header(null, null, null);
        header(null, "foo", "bar");
        header(null, "Content-Type", "blah");
        header(null, "Content-Type", "text/html;charset=blah");
        header("UTF-8", "Content-Type", "text/html;charset=utf-8");
        header("UTF-8", "Content-Type", "text/html;charset=utf-8;");
    }

    private void header(final String expectedEncoding, final String headerName, final String headerValue) {
        final NameValuePair header = new NameValuePair(headerName, headerValue);
        assertEquals(expectedEncoding, sniffEncodingFromHttpHeaders(singletonList(header)));
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
        meta("UTF-8", "<meta a='b' c=d e=\"f\" content='text/html; charset=utf-8' />");
        meta("UTF-8", "abc <meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>");
        meta("UTF-8", "abc <meta http-equiv='Content-Type' content='text/html; CHARSET=UTF-8'/>");
        meta("UTF-8", "abc <meta http-equiv='Content-Type' content='text/html; chArsEt=UtF-8'/>");
    }

    private void meta(final String expectedEncoding, final String content) {
        assertEquals(expectedEncoding, sniffEncodingFromMetaTag(content.getBytes()));
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
        xmlDeclaration("utf-8", "<?xml encoding='utf-8'?>");
        xmlDeclaration(null, "<?xml encoding=\"utf-8");
        xmlDeclaration(null, "<?xml encoding=\"utf-8\"");
        xmlDeclaration("utf-8", "<?xml encoding=\"utf-8\"?>");
    }

    private void xmlDeclaration(final String expectedEncoding, final String content) {
        assertEquals(expectedEncoding, sniffEncodingFromXmlDeclaration(content.getBytes()));
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
        contentType(null, "text/html");
        contentType(null, "\n text/html \t");
        contentType(null, "\n text/html ; char \t");
        contentType(null, "\n text/html ; charset \t");
        contentType(null, "\n text/html ; charset=");
        contentType(null, "\n text/html ; charset= \t");
        contentType(null, "\n text/html ; charset =");
        contentType(null, "\n text/html ; charset = \n");
        contentType(null, "\n text/html ; charset=blah");
        contentType("utf-8", "\n text/html ; charset=utf-8");
        contentType("utf-8", "\n text/html ; charset=utf-8;");
        contentType("utf-8", "\n text/html ; charset = \n utf-8 ");
        contentType("utf-8", "\n text/html ; charset = \n utf-8 ; ");
        contentType(null, "\n text/html ; charset = \n'");
        contentType(null, "\n text/html ; charset = \n' ");
        contentType(null, "\n text/html ; charset = \n' utf-8");
        contentType("utf-8", "\n text/html ; charset = \n'utf-8'");
        contentType(null, "\n text/html ; charset = \n\"");
        contentType(null, "\n text/html ; charset = \n\" ");
        contentType(null, "\n text/html ; charset = \n\" utf-8");
        contentType("utf-8", "\n text/html ; charset = \n\"utf-8\"");
    }

    private void contentType(final String expectedEncoding, final String contentType) {
        assertEquals(expectedEncoding, extractEncodingFromContentType(contentType));
    }

}
