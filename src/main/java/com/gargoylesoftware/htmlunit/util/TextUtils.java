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
package com.gargoylesoftware.htmlunit.util;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Utility methods relating to text.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Brad Clarke
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class TextUtils {

    /** Private constructor to prevent instantiation. */
    private TextUtils() {
    }

    /**
     * Convert a string into an input stream.
     * @param content the string
     * @return the resulting input stream
     */
    public static InputStream toInputStream(final String content) {
        return toInputStream(content, ISO_8859_1);
    }

    /**
     * Convert a string into an input stream.
     * @param content the string
     * @param charset the encoding to use when converting the string to a stream
     * @return the resulting input stream
     */
    public static InputStream toInputStream(
            final String content,
            final Charset charset) {

        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(content.length() * 2);
            final OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, charset);
            writer.write(content);
            writer.flush();

            final byte[] byteArray = byteArrayOutputStream.toByteArray();
            return new ByteArrayInputStream(byteArray);
        }
        catch (final IOException e) {
            // Theoretically impossible since all the "IO" is in memory but it's a
            // checked exception so we have to catch it.
            throw new IllegalStateException("Exception when converting a string to an input stream: '" + e + "'", e);
        }
    }

    /**
     * Converts a string into a byte array using the specified encoding.
     *
     * @param charset the charset
     * @param content the string to convert
     * @return the String as a byte[]; if the specified encoding is not supported an empty byte[] will be returned
     */
    public static byte[] stringToByteArray(final String content, final Charset charset) {
        if (content ==  null || content.isEmpty() || charset == null) {
            return new byte[0];
        }

        return content.getBytes(charset);
    }
}
