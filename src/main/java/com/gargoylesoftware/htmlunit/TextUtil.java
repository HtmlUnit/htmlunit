/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Utility methods relating to text.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public final class TextUtil {

    /**
     * Default encoding used.
     */
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    
    /** Private constructor to prevent instantiation. */
    private TextUtil() { }

    /**
     * Returns true if the string starts with the specified prefix, irrespective of case.
     * @param stringToCheck The string to check
     * @param prefix The prefix
     * @return true if the string starts with the prefix.
     */
    public static boolean startsWithIgnoreCase(final String stringToCheck, final String prefix) {
        WebAssert.notNull("stringToCheck", stringToCheck);
        WebAssert.notNull("prefix", prefix);

        if (prefix.length() == 0) {
            throw new IllegalArgumentException("Prefix may not be empty");
        }

        final int prefixLength = prefix.length();
        if (stringToCheck.length() < prefixLength) {
            return false;
        }
        else {
            return stringToCheck.substring(0, prefixLength).toLowerCase().equals(prefix.toLowerCase());
        }
    }

    /**
     * Convert a string into an input stream.
     * @param content The string
     * @return The resulting input stream.
     */
    public static InputStream toInputStream(final String content) {
        try {
            return toInputStream(content, DEFAULT_CHARSET);
        }
        catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(
                DEFAULT_CHARSET + " is an unsupported encoding!  You may have a corrupted installation of java.");
        }
    }

    /**
     * Convert a string into an input stream.
     * @param content The string
     * @param encoding The encoding to use when converting the string to a stream.
     * @return The resulting input stream.
     * @throws UnsupportedEncodingException If the encoding is not supported.
     */
    public static InputStream toInputStream(
            final String content,
            final String encoding)
        throws
            UnsupportedEncodingException {

        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(content.length() * 2);
            final OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, encoding);
            writer.write(content);
            writer.flush();

            final byte[] byteArray = byteArrayOutputStream.toByteArray();
            return new ByteArrayInputStream(byteArray);
        }
        catch (final UnsupportedEncodingException e) {
            throw e;
        }
        catch (final IOException e) {
            // Theoretically impossible since all the "IO" is in memory but it's a
            // checked exception so we have to catch it.
            e.printStackTrace();
            throw new IllegalStateException("Exception when converting a string to an input stream: " + e);
        }
    }

    /**
     * Convert a string into a byte array using a default encoding {@link #DEFAULT_CHARSET}.
     *
     * @param content The string to convert, assumed to be {@link #DEFAULT_CHARSET} encoded
     * @return The String as a byte[]. If the default encoding is not supported an empty byte[] will be returned.
     */
    public static byte[] stringToByteArray(final String content) {
        return stringToByteArray(content, DEFAULT_CHARSET);
    }

    /**
     * Convert a string into a byte array using the given encoding.
     *
     * @param charset The name of a supported charset
     * @param content The string to convert
     *
     * @return The String as a byte[]. If the specified encoding is not supported an empty byte[] will be returned.
     */
    public static byte[] stringToByteArray(final String content, final String charset) {
        byte[] contentBytes;
        try {
            contentBytes = content.getBytes(charset);
        }
        catch (final UnsupportedEncodingException e) {
            contentBytes = new byte[0];
        }
        return contentBytes;
    }
}
