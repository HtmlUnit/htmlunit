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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.lang3.ArrayUtils;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.javascript.host.xml.XMLHttpRequest;
import org.htmlunit.util.MimeType;

/**
 * Base class for for several {@link XMLHttpRequest} encoding tests.
 *
 * @author Ronald Brill
 */
public abstract class AbstractXMLHttpRequestEncodingTest extends WebDriverTestCase {

    /** "BOMUTF16LE". */
    protected static final String BOM_UTF_16LE = "BOMUTF16LE";
    /** "BOMUTF16BE". */
    protected static final String BOM_UTF_16BE = "BOMUTF16BE";
    /** "BOMUTF8". */
    protected static final String BOM_UTF_8 = "BOMUTF8";

    /**
     * Enum for data driven tests.
     */
    public enum TestCharset {
        /** utf 8. */
        UTF8("UTF8", UTF_8),
        /** iso 8859 1. */
        ISO88591("ISO88591", ISO_8859_1),
        /** windows-1250. */
        WINDOWS1250("WINDOWS1250", Charset.forName("windows-1250")),
        /** gb 2312. */
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

        /**
         * @return the {@link Charset}
         */
        public Charset getCharset() {
            return charset_;
        }
    }

    /**
     * Enum for data driven tests.
     */
    public enum TestMimeType {
        /** empty. */
        EMPTY("EMPTY", ""),
        /** xml. */
        XML("XML", MimeType.TEXT_XML),
        /** plain. */
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

        /**
         * @return the mime type
         */
        public String getMimeType() {
            return mimeType_;
        }
    }

    protected void setupXmlResponse(final String xml, final String bom, final TestMimeType mimeTypeXml,
            final TestCharset charsetXmlResponseHeader) {
        if (BOM_UTF_8.equals(bom)) {
            final byte[] xmlBytes =
                    ArrayUtils.addAll(ByteOrderMark.UTF_8.getBytes(), xml.getBytes(StandardCharsets.UTF_8));
            getMockWebConnection().setResponse(URL_SECOND, xmlBytes, 200, "OK", mimeTypeXml.getMimeType(), null);
            return;
        }
        if (BOM_UTF_16BE.equals(bom)) {
            final byte[] xmlBytes =
                    ArrayUtils.addAll(ByteOrderMark.UTF_16BE.getBytes(), xml.getBytes(StandardCharsets.UTF_16BE));
            getMockWebConnection().setResponse(URL_SECOND, xmlBytes, 200, "OK", mimeTypeXml.getMimeType(), null);
            return;
        }
        if (BOM_UTF_16LE.equals(bom)) {
            final byte[] xmlBytes =
                    ArrayUtils.addAll(ByteOrderMark.UTF_16LE.getBytes(), xml.getBytes(StandardCharsets.UTF_16LE));
            getMockWebConnection().setResponse(URL_SECOND, xmlBytes, 200, "OK", mimeTypeXml.getMimeType(), null);
            return;
        }
        getMockWebConnection().setResponse(URL_SECOND, xml, mimeTypeXml.getMimeType(),
                charsetXmlResponseHeader == null ? null : charsetXmlResponseHeader.getCharset());
    }

    protected static String escape(final String str) {
        final StringBuilder res = new StringBuilder();
        for (final char c : str.toCharArray()) {
            res.append("\\u").append(String.format("%04X", (int) c).toLowerCase(Locale.ROOT));
        }
        return res.toString();
    }
}
