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
package com.gargoylesoftware.htmlunit.protocol.data;

import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Helper to work with data URLs.
 * @see <a href="http://www.ietf.org/rfc/rfc2397.txt">RFC2397</a>
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Carsten Steul
 */
public class DataUrlDecoder {
    private static final Charset DEFAULT_CHARSET = US_ASCII;
    private static final String DEFAULT_MEDIA_TYPE = MimeType.TEXT_PLAIN;
    private final String mediaType_;
    private final Charset charset_;
    private byte[] content_;

    /**
     * C'tor.
     * @param data the data
     * @param mediaType the media type
     * @param charset the charset
     */
    protected DataUrlDecoder(final byte[] data, final String mediaType, final Charset charset) {
        content_ = data;
        mediaType_ = mediaType;
        charset_ = charset;
    }

    /**
     * Decodes a data URL providing simple access to the information contained by the URL.
     * @param url the URL to decode
     * @return the {@link DataUrlDecoder} holding decoded information
     * @throws UnsupportedEncodingException if the encoding specified by the data URL is invalid or not
     * available on the JVM
     * @throws DecoderException if decoding didn't success
     */
    public static DataUrlDecoder decode(final URL url) throws UnsupportedEncodingException, DecoderException {
        return decodeDataURL(url.toExternalForm());
    }

    /**
     * Decodes a data URL providing simple access to the information contained by the URL.
     * @param url the string representation of the URL to decode
     * @return the {@link DataUrlDecoder} holding decoded information
     * @throws UnsupportedEncodingException if the encoding specified by the data URL is invalid or not
     * available on the JVM
     * @throws DecoderException if decoding didn't success
     */
    public static DataUrlDecoder decodeDataURL(final String url) throws UnsupportedEncodingException,
            DecoderException {
        if (!url.startsWith("data")) {
            throw new IllegalArgumentException("Not a data url: " + url);
        }
        final int comma = url.indexOf(',');
        String beforeData = url.substring("data:".length(), comma);

        final boolean base64 = beforeData.endsWith(";base64");
        if (base64) {
            beforeData = beforeData.substring(0, beforeData.length() - 7);
        }
        final String mediaType = extractMediaType(beforeData);
        final Charset charset = extractCharset(beforeData);

        byte[] data = url.substring(comma + 1).getBytes(charset);
        if (base64) {
            data = Base64.decodeBase64(decodeUrl(data));
        }
        else {
            data = URLCodec.decodeUrl(data);
        }

        return new DataUrlDecoder(data, mediaType, charset);
    }

    private static Charset extractCharset(final String beforeData) {
        if (beforeData.contains(";")) {
            String charsetName = StringUtils.substringAfter(beforeData, ";");
            charsetName = charsetName.trim();
            if (charsetName.startsWith("charset=")) {
                charsetName = charsetName.substring(8);
            }
            try {
                return Charset.forName(charsetName);
            }
            catch (final UnsupportedCharsetException | IllegalCharsetNameException e) {
                return DEFAULT_CHARSET;
            }
        }
        return DEFAULT_CHARSET;
    }

    private static String extractMediaType(final String beforeData) {
        if (beforeData.contains("/")) {
            if (beforeData.contains(";")) {
                return StringUtils.substringBefore(beforeData, ";");
            }
            return beforeData;
        }
        return DEFAULT_MEDIA_TYPE;
    }

    /**
     * Gets the media type information contained in the data URL.
     * @return "text/plain" if the URL didn't contain any media type information
     */
    public String getMediaType() {
        return mediaType_;
    }

    /**
     * Gets the charset information specified in the data URL.
     * @return "US-ASCII" if the URL didn't contain any charset information
     */
    public String getCharset() {
        return charset_.name();
    }

    /**
     * Gets the bytes contained in the data URL.
     * @return the content
     */
    public byte[] getBytes() {
        return content_;
    }

    /**
     * Gets the text content of the data URL. This makes sense only for data URL that
     * represents some text.
     * @return the text content
     * @throws UnsupportedEncodingException if decoding failed using the specified charset
     */
    public String getDataAsString() throws UnsupportedEncodingException {
        return new String(content_, charset_);
    }

    // adapted from apache commons codec
    private static byte[] decodeUrl(final byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; i++) {
            final int b = bytes[i];
            if (b == '%') {
                try {
                    final int u = digit16(bytes[++i]);
                    final int l = digit16(bytes[++i]);
                    buffer.write((char) ((u << 4) + l));
                }
                catch (final ArrayIndexOutOfBoundsException e) {
                    throw new DecoderException("Invalid URL encoding: ", e);
                }
            }
            else {
                buffer.write(b);
            }
        }
        return buffer.toByteArray();
    }

    private static int digit16(final byte b) throws DecoderException {
        final int i = Character.digit((char) b, 16);
        if (i == -1) {
            throw new DecoderException("Invalid URL encoding: not a valid digit (radix 16): " + b);
        }
        return i;
    }
}
