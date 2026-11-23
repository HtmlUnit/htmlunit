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
package org.htmlunit.protocol.data;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.htmlunit.protocol.data.DataURLConnection.DATA_PREFIX;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.htmlunit.util.MimeType;
import org.htmlunit.util.StringUtils;
import org.htmlunit.util.UrlUtils;

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
    /** The MIME type of a data url without (valid) MIME type. */
    private static final String DEFAULT_CONTENT_TYPE = "text/plain;charset=US-ASCII";

    private final String mediaType_;
    private final Charset charset_;
    private final byte[] content_;
    private String contentType_;
    private String beforeData_;

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
     */
    public static DataUrlDecoder decode(final URL url) throws UnsupportedEncodingException {
        return decodeDataURL(url.toExternalForm());
    }

    /**
     * Decodes a data URL providing simple access to the information contained by the URL.
     * @param url the string representation of the URL to decode
     * @return the {@link DataUrlDecoder} holding decoded information
     * @throws UnsupportedEncodingException if the encoding specified by the data URL is invalid or not
     *         available on the JVM
     */
    public static DataUrlDecoder decodeDataURL(final String url) throws UnsupportedEncodingException {
        if (!url.startsWith(DATA_PREFIX)) {
            throw new UnsupportedEncodingException("Invalid data url: '" + url + "' (wrong prefix)");
        }
        final int comma = url.indexOf(',');
        if (comma < 0) {
            throw new UnsupportedEncodingException("Invalid data url: '" + url + "' (no data)");
        }

        String beforeData = url.substring(DATA_PREFIX.length(), comma);
        // ';base64' at the end (case-insensitive, spaces allowed)
        final int lastSemicolon = beforeData.lastIndexOf(';');
        final boolean base64 = lastSemicolon > -1
                && "base64".equalsIgnoreCase(beforeData.substring(lastSemicolon + 1).strip());
        if (base64) {
            beforeData = beforeData.substring(0, lastSemicolon);
        }
        final String mediaType = extractMediaType(beforeData);
        final Charset charset = extractCharset(beforeData);

        try {
            byte[] data = url.substring(comma + 1).getBytes(charset);
            data = UrlUtils.decodeDataUrl(data, base64);
            if (base64) {
                data = Base64.getDecoder().decode(data);
            }
            final DataUrlDecoder decoder = new DataUrlDecoder(data, mediaType, charset);
            decoder.beforeData_ = url.substring(DATA_PREFIX.length(), comma);
            decoder.contentType_ = toContentType(decoder.beforeData_, false);
            return decoder;
        }
        catch (final IllegalArgumentException e) {
            final UnsupportedEncodingException ex =
                    new UnsupportedEncodingException("Invalid data url: '" + url + "' (data decoding failed)");
            ex.initCause(e);
            throw ex;
        }
    }

    /**
     * Returns the content type like the fetch standard does it for the MIME type part of a data url.
     *
     * @param beforeData the part of the url between 'data:' and the comma
     * @param standardSerialization whether to serialize like the MIME Sniffing Standard
     *        (see {@link #parseAndSerializeMimeType(String, boolean)}); like Firefox all whitespace
     *        is removed from base64 data urls (e.g. 'a="b c";base64' is 'a=bc')
     * @return the serialized MIME type
     * @see <a href="https://fetch.spec.whatwg.org/#data-url-processor">Fetch Standard - data: URL processor</a>
     */
    static String toContentType(final String beforeData, final boolean standardSerialization) {
        String mimeType = beforeData.strip();

        // remove ';base64' (case-insensitive, spaces allowed before 'base64')
        final int semicolon = mimeType.lastIndexOf(';');
        if (semicolon > -1 && "base64".equalsIgnoreCase(mimeType.substring(semicolon + 1).stripLeading())) {
            mimeType = mimeType.substring(0, semicolon);

            // Firefox removes all whitespace from base64 data urls (also from the MIME type)
            if (standardSerialization) {
                final StringBuilder withoutWhitespace = new StringBuilder(mimeType.length());
                for (int i = 0; i < mimeType.length(); i++) {
                    final char c = mimeType.charAt(i);
                    if (!isHttpWhitespace(c)) {
                        withoutWhitespace.append(c);
                    }
                }
                mimeType = withoutWhitespace.toString();
            }
        }

        if (mimeType.startsWith(";")) {
            mimeType = MimeType.TEXT_PLAIN + mimeType;
        }

        final String serialized = parseAndSerializeMimeType(mimeType, standardSerialization);
        if (serialized == null) {
            return DEFAULT_CONTENT_TYPE;
        }
        return serialized;
    }

    /**
     * Parses the MIME type and serializes it again like Chrome does it for data urls
     * (see {@link #parseAndSerializeMimeType(String, boolean)}).
     *
     * @param input the MIME type
     * @return the serialized MIME type or {@code null} if the input is not a valid MIME type
     */
    static String parseAndSerializeMimeType(final String input) {
        return parseAndSerializeMimeType(input, false);
    }

    /**
     * Parses the MIME type and serializes it again like the browsers do it for data urls;
     * type and subtype are lower case, invalid parameters are ignored.
     * <ul>
     * <li>With standard serialization (like Firefox) the parameters are parsed and serialized like the
     * MIME Sniffing Standard does it: the parameter names are lower case, only the first parameter of a
     * name is used, values are quoted if needed.</li>
     * <li>Otherwise (like Chrome) the parameters are split at every ';' (also inside quotes), names and
     * values are kept as they are (no quotes added), duplicates are kept, the first charset parameter is
     * moved to the end.</li>
     * </ul>
     *
     * @param input the MIME type
     * @param standardSerialization whether to serialize like the MIME Sniffing Standard
     * @return the serialized MIME type or {@code null} if the input is not a valid MIME type
     * @see <a href="https://mimesniff.spec.whatwg.org/#parse-a-mime-type">MIME Sniffing Standard
     *      - parse a MIME type</a>
     */
    static String parseAndSerializeMimeType(final String input, final boolean standardSerialization) {
        final String mimeType = stripHttpWhitespace(input);

        final int slash = mimeType.indexOf('/');
        if (slash < 1) {
            return null;
        }
        final String type = mimeType.substring(0, slash);
        if (!isToken(type)) {
            return null;
        }

        int position = mimeType.indexOf(';', slash + 1);
        if (position < 0) {
            position = mimeType.length();
        }
        final String subtype = stripHttpWhitespace(mimeType.substring(slash + 1, position));
        if (subtype.isEmpty() || !isToken(subtype)) {
            return null;
        }

        final StringBuilder result = new StringBuilder()
                .append(type.toLowerCase(Locale.ROOT))
                .append('/')
                .append(subtype.toLowerCase(Locale.ROOT));

        if (!standardSerialization) {
            appendParametersLikeChrome(result, mimeType.substring(position));
            return result.toString();
        }

        final Set<String> names = new HashSet<>();
        final int length = mimeType.length();
        while (position < length) {
            // skip the ';' and the leading whitespace
            position++;
            while (position < length && isHttpWhitespace(mimeType.charAt(position))) {
                position++;
            }

            // the name
            final int nameStart = position;
            while (position < length && mimeType.charAt(position) != ';' && mimeType.charAt(position) != '=') {
                position++;
            }
            final String name = mimeType.substring(nameStart, position).toLowerCase(Locale.ROOT);
            if (position >= length) {
                break;
            }
            if (mimeType.charAt(position) == ';') {
                continue;
            }
            // skip the '='
            position++;

            // the value
            final String value;
            if (position < length && mimeType.charAt(position) == '"') {
                final StringBuilder quoted = new StringBuilder();
                position++;
                while (position < length && mimeType.charAt(position) != '"') {
                    char c = mimeType.charAt(position);
                    if (c == '\\' && position + 1 < length) {
                        position++;
                        c = mimeType.charAt(position);
                    }
                    quoted.append(c);
                    position++;
                }
                value = quoted.toString();
                // ignore the rest up to the next ';'
                while (position < length && mimeType.charAt(position) != ';') {
                    position++;
                }
            }
            else {
                final int valueStart = position;
                while (position < length && mimeType.charAt(position) != ';') {
                    position++;
                }
                value = stripHttpWhitespace(mimeType.substring(valueStart, position));
                if (value.isEmpty()) {
                    continue;
                }
            }

            if (!name.isEmpty() && isToken(name) && isQuotedStringContent(value)) {
                if (names.add(name)) {
                    result.append(';').append(name).append('=').append(serializeParameterValue(value));
                }
            }
        }
        return result.toString();
    }

    /**
     * Appends the parameters like Chrome does it for data urls: split at every ';', parameters without
     * '=' or with whitespace around the '=' are ignored, names and values are kept (a quoted value ends
     * at the next not escaped quote or at the end of the parameter), the first charset parameter is moved
     * to the end (without quotes).
     *
     * @param result the serialized type and subtype
     * @param parameters the parameter part of the MIME type (starting with ';' or empty)
     */
    private static void appendParametersLikeChrome(final StringBuilder result, final String parameters) {
        String charset = null;
        for (final String part : parameters.split(";", -1)) {
            final String parameter = stripHttpWhitespace(part);
            final int equals = parameter.indexOf('=');
            if (equals < 1) {
                continue;
            }
            final String name = parameter.substring(0, equals);
            // whitespace around the '=' makes the parameter invalid ('a = b')
            if (isHttpWhitespace(name.charAt(name.length() - 1))
                    || equals + 1 < parameter.length() && isHttpWhitespace(parameter.charAt(equals + 1))) {
                continue;
            }
            String value = parameter.substring(equals + 1);
            if (!isQuotedStringContent(value)) {
                continue;
            }
            String unquoted = value;
            if (value.startsWith("\"")) {
                final StringBuilder content = new StringBuilder();
                int i = 1;
                while (i < value.length() && value.charAt(i) != '"') {
                    if (value.charAt(i) == '\\' && i + 1 < value.length()) {
                        i++;
                    }
                    content.append(value.charAt(i));
                    i++;
                }
                unquoted = content.toString();
                // the raw value (with escapes) up to the closing quote
                value = i < value.length() ? value.substring(0, i + 1) : value + '"';
            }

            if ("charset".equalsIgnoreCase(name)) {
                if (charset == null && !unquoted.isEmpty()) {
                    charset = unquoted;
                }
                continue;
            }
            result.append(';').append(name).append('=').append(value);
        }

        if (charset != null) {
            result.append(";charset=").append(charset);
        }
    }

    private static String serializeParameterValue(final String value) {
        if (!value.isEmpty() && isToken(value)) {
            return value;
        }
        return '"' + value.replace("\\", "\\\\").replace("\"", "\\\"") + '"';
    }

    private static boolean isHttpWhitespace(final char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private static String stripHttpWhitespace(final String value) {
        int start = 0;
        int end = value.length();
        while (start < end && isHttpWhitespace(value.charAt(start))) {
            start++;
        }
        while (end > start && isHttpWhitespace(value.charAt(end - 1))) {
            end--;
        }
        return value.substring(start, end);
    }

    private static boolean isToken(final String value) {
        if (value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9'
                    || "!#$%&'*+-.^_`|~".indexOf(c) > -1)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isQuotedStringContent(final String value) {
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            if (c != '\t' && (c < 0x20 || c == 0x7F || c > 0xFF)) {
                return false;
            }
        }
        return true;
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
     * Gets the content type of the data URL, serialized like the browsers do it
     * (e.g. 'text/plain;charset=utf-8' for 'data:text/plain;charset=utf-8,...').
     * @return the content type; "text/plain;charset=US-ASCII" if the URL didn't contain a valid MIME type
     */
    public String getContentType() {
        if (contentType_ == null) {
            return mediaType_ + ";charset=" + charset_.name();
        }
        return contentType_;
    }

    /**
     * Gets the content type of the data URL, serialized like the browsers do it.
     * @param standardSerialization whether to serialize like the MIME Sniffing Standard (like Firefox;
     *        duplicate parameters are ignored) or like Chrome (see {@link #getContentType()})
     * @return the content type; "text/plain;charset=US-ASCII" if the URL didn't contain a valid MIME type
     */
    public String getContentType(final boolean standardSerialization) {
        if (!standardSerialization || beforeData_ == null) {
            return getContentType();
        }
        return toContentType(beforeData_, true);
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
}
