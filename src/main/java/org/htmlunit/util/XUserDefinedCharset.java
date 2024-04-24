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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

/**
 * Simple implementation of the 'x-user-defined' {@link Charset}.
 * @author Ronald Brill
 *
 */
public final class XUserDefinedCharset extends Charset {

    /** The single instance. */
    public static final String NAME = "x-user-defined";

    /** The single instance. */
    public static final XUserDefinedCharset INSTANCE = new XUserDefinedCharset();

    private XUserDefinedCharset() {
        super(NAME, new String[0]);
    }

    @Override
    public boolean contains(final Charset charset) {
        return StandardCharsets.US_ASCII.equals(charset);
    }

    @Override
    public CharsetDecoder newDecoder() {
        return new XUserDefinedDecoder();
    }

    @Override
    public CharsetEncoder newEncoder() {
        // not implemented for now
        return null;
    }

    private static final class XUserDefinedDecoder extends CharsetDecoder {
        XUserDefinedDecoder() {
            super(XUserDefinedCharset.INSTANCE, 1, 1);
        }

        @Override
        protected CoderResult decodeLoop(final ByteBuffer in, final CharBuffer out) {
            while (true) {
                if (!in.hasRemaining()) {
                    return CoderResult.UNDERFLOW;
                }
                if (!out.hasRemaining()) {
                    return CoderResult.OVERFLOW;
                }
                final byte b = in.get();
                if (b >= 0) {
                    out.append((char) b);
                }
                else {
                    out.append((char) (0xF700 + (b & 0xFF)));
                }
            }
        }
    }
}
