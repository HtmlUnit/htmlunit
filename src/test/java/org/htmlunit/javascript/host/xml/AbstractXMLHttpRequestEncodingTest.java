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
package org.htmlunit.javascript.host.xml;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.charset.Charset;

import org.htmlunit.WebDriverTestCase;
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

    public enum TestCharset {
        /** utf 8. */
        UTF8("UTF8", UTF_8),
        /** iso 8859 1. */
        ISO88591("ISO88591", ISO_8859_1),
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

        public Charset getCharset() {
            return charset_;
        }
    }

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

        public String getMimeType() {
            return mimeType_;
        }
    }
}
