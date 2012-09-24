/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.protocol.data;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;

/**
 * Helper to work with data URLs.
 * @see <a href="http://www.ietf.org/rfc/rfc2397.txt">RFC2397</a>
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DataUrlDecoder {
    private static final String DEFAULT_CHARSET = "US-ASCII";
    private static final String DEFAULT_MEDIA_TYPE = "text/plain";
    private final String mediaType_;
    private final String charset_;
    private byte[] content_;

    /**
     * C'tor.
     * @param data the data
     * @param mediaType the media type
     * @param charset the charset
     */
    protected DataUrlDecoder(final byte[] data, final String mediaType, final String charset) {
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
        final String beforeData =  url.substring("data:".length(), comma);
        final String mediaType = extractMediaType(beforeData);
        final String charset = extractCharset(beforeData);

        final boolean base64 = beforeData.endsWith(";base64");
        byte[] data = url.substring(comma + 1).getBytes(charset);
        if (base64) {
            data = Base64.decodeBase64(URLCodec.decodeUrl(data));
        }
        else {
            data = URLCodec.decodeUrl(data);
        }

        return new DataUrlDecoder(data, mediaType, charset);
    }

    private static String extractCharset(final String beforeData) {
        // TODO
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
        return charset_;
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
}
