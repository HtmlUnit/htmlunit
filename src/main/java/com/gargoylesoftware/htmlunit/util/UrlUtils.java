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

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Locale;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;

import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.protocol.AnyHandler;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;

/**
 * URL utilities class that makes it easy to create new URLs based off of old URLs
 * without having to assemble or parse them yourself.
 *
 * @author Daniel Gredler
 * @author Martin Tamme
 * @author Sudhan Moghe
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Joerg Werner
 * @author Hartmut Arlt
 */
public final class UrlUtils {

    /** "about". */
    public static final String ABOUT = "about";
    /** "about:". */
    public static final String ABOUT_SCHEME = ABOUT + ":";
    /** "about:blank". */
    public static final String ABOUT_BLANK = ABOUT_SCHEME + "blank";
    /** URL for "about:blank". */
    public static final URL URL_ABOUT_BLANK;

    private static final URLStreamHandler JS_HANDLER;
    private static final URLStreamHandler ABOUT_HANDLER;
    private static final URLStreamHandler DATA_HANDLER;

    private static final BitSet PATH_ALLOWED_CHARS = new BitSet(256);
    private static final BitSet QUERY_ALLOWED_CHARS = new BitSet(256);
    private static final BitSet ANCHOR_ALLOWED_CHARS = new BitSet(256);
    private static final BitSet HASH_ALLOWED_CHARS = new BitSet(256);

    /**
     * URI allowed char initialization; based on HttpClient 3.1's URI bit sets.
     */
    static {
        // make sure the handlers are available first (before calling toUrlSafe())
        JS_HANDLER = new com.gargoylesoftware.htmlunit.protocol.javascript.Handler();
        ABOUT_HANDLER = new com.gargoylesoftware.htmlunit.protocol.about.Handler();
        DATA_HANDLER = new com.gargoylesoftware.htmlunit.protocol.data.Handler();

        try {
            URL_ABOUT_BLANK = new URL(null, ABOUT_BLANK, ABOUT_HANDLER);
        }
        catch (final MalformedURLException e) {
            // should never happen
            throw new RuntimeException(e);
        }

        final BitSet reserved = new BitSet(256);
        reserved.set(';');
        reserved.set('/');
        reserved.set('?');
        reserved.set(':');
        reserved.set('@');
        reserved.set('&');
        reserved.set('=');
        reserved.set('+');
        reserved.set('$');
        reserved.set(',');

        final BitSet mark = new BitSet(256);
        mark.set('-');
        mark.set('_');
        mark.set('.');
        mark.set('!');
        mark.set('~');
        mark.set('*');
        mark.set('\'');
        mark.set('(');
        mark.set(')');

        final BitSet alpha = new BitSet(256);
        for (int i = 'a'; i <= 'z'; i++) {
            alpha.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            alpha.set(i);
        }

        final BitSet digit = new BitSet(256);
        for (int i = '0'; i <= '9'; i++) {
            digit.set(i);
        }

        final BitSet alphanumeric = new BitSet(256);
        alphanumeric.or(alpha);
        alphanumeric.or(digit);

        final BitSet unreserved = new BitSet(256);
        unreserved.or(alphanumeric);
        unreserved.or(mark);

        final BitSet hex = new BitSet(256);
        hex.or(digit);
        for (int i = 'a'; i <= 'f'; i++) {
            hex.set(i);
        }
        for (int i = 'A'; i <= 'F'; i++) {
            hex.set(i);
        }

        final BitSet escaped = new BitSet(256);
        escaped.set('%');
        escaped.or(hex);

        final BitSet uric = new BitSet(256);
        uric.or(reserved);
        uric.or(unreserved);
        uric.or(escaped);

        final BitSet pchar = new BitSet(256);
        pchar.or(unreserved);
        pchar.or(escaped);
        pchar.set(':');
        pchar.set('@');
        pchar.set('&');
        pchar.set('=');
        pchar.set('+');
        pchar.set('$');
        pchar.set(',');

        final BitSet param = pchar;

        final BitSet segment = new BitSet(256);
        segment.or(pchar);
        segment.set(';');
        segment.or(param);

        final BitSet pathSegments = new BitSet(256);
        pathSegments.set('/');
        pathSegments.or(segment);

        final BitSet absPath = new BitSet(256);
        absPath.set('/');
        absPath.or(pathSegments);

        final BitSet allowedAbsPath = new BitSet(256);
        allowedAbsPath.or(absPath);

        final BitSet allowedFragment = new BitSet(256);
        allowedFragment.or(uric);
//        allowedFragment.clear('%');

        final BitSet allowedQuery = new BitSet(256);
        allowedQuery.or(uric);

        final BitSet allowedHash = new BitSet(256);
        allowedHash.or(uric);
        allowedHash.clear('%');

        PATH_ALLOWED_CHARS.or(allowedAbsPath);
        QUERY_ALLOWED_CHARS.or(allowedQuery);
        ANCHOR_ALLOWED_CHARS.or(allowedFragment);
        HASH_ALLOWED_CHARS.or(allowedHash);
    }

    /**
     * Disallow instantiation of this class.
     */
    private UrlUtils() {
        // Empty.
    }

    /**
     * <p>Constructs a URL instance based on the specified URL string, taking into account the fact that the
     * specified URL string may represent an <tt>"about:..."</tt> URL, a <tt>"javascript:..."</tt> URL, or
     * a <tt>data:...</tt> URL.</p>
     *
     * <p>The caller should be sure that URL strings passed to this method will parse correctly as URLs, as
     * this method never expects to have to handle {@link MalformedURLException}s.</p>
     *
     * @param url the URL string to convert into a URL instance
     * @return the constructed URL instance
     */
    public static URL toUrlSafe(final String url) {
        try {
            return toUrlUnsafe(url);
        }
        catch (final MalformedURLException e) {
            // Should never happen.
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Constructs a URL instance based on the specified URL string, taking into account the fact that the
     * specified URL string may represent an <tt>"about:..."</tt> URL, a <tt>"javascript:..."</tt> URL, or
     * a <tt>data:...</tt> URL.</p>
     *
     * <p>Unlike {@link #toUrlSafe(String)}, the caller need not be sure that URL strings passed to this
     * method will parse correctly as URLs.</p>
     *
     * @param url the URL string to convert into a URL instance
     * @return the constructed URL instance
     * @throws MalformedURLException if the URL string cannot be converted to a URL instance
     */
    public static URL toUrlUnsafe(final String url) throws MalformedURLException {
        WebAssert.notNull("url", url);

        final String protocol = org.apache.commons.lang3.StringUtils.substringBefore(url, ":").toLowerCase(Locale.ROOT);

        if (protocol.isEmpty() || UrlUtils.isNormalUrlProtocol(protocol)) {
            final URL response = new URL(url);
            if (response.getProtocol().startsWith("http")
                    && org.apache.commons.lang3.StringUtils.isEmpty(response.getHost())) {
                throw new MalformedURLException("Missing host name in url: " + url);
            }
            return response;
        }

        if (JavaScriptURLConnection.JAVASCRIPT_PREFIX.equals(protocol + ":")) {
            return new URL(null, url, JS_HANDLER);
        }

        if (ABOUT.equals(protocol)) {
            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(ABOUT_BLANK, url)) {
                return URL_ABOUT_BLANK;
            }
            return new URL(null, url, ABOUT_HANDLER);
        }

        if ("data".equals(protocol)) {
            return new URL(null, url, DATA_HANDLER);
        }

        return new URL(null, url, AnyHandler.INSTANCE);
    }

    /**
     * <p>Encodes illegal characters in the specified URL's path, query string and anchor according to the URL
     * encoding rules observed in real browsers.</p>
     *
     * <p>For example, this method changes <tt>"http://first/?a=b c"</tt> to <tt>"http://first/?a=b%20c"</tt>.</p>
     *
     * @param url the URL to encode
     * @param minimalQueryEncoding whether or not to perform minimal query encoding, like IE does
     * @param charset the charset
     * @return the encoded URL
     */
    public static URL encodeUrl(final URL url, final boolean minimalQueryEncoding, final Charset charset) {
        if (!isNormalUrlProtocol(url.getProtocol())) {
            return url; // javascript:, about:, data: and anything not supported like foo:
        }

        try {
            String path = url.getPath();
            if (path != null) {
                path = encode(path, PATH_ALLOWED_CHARS, UTF_8);
            }
            String query = url.getQuery();
            if (query != null) {
                if (minimalQueryEncoding) {
                    query = org.apache.commons.lang3.StringUtils.replace(query, " ", "%20");
                }
                else {
                    query = encode(query, QUERY_ALLOWED_CHARS, charset);
                }
            }
            String anchor = url.getRef();
            if (anchor != null) {
                anchor = encode(anchor, ANCHOR_ALLOWED_CHARS, UTF_8);
            }
            return createNewUrl(url.getProtocol(), url.getUserInfo(), url.getHost(),
                                url.getPort(), path, anchor, query);
        }
        catch (final MalformedURLException e) {
            // Impossible... I think.
            throw new RuntimeException(e);
        }
    }

    /**
     * Encodes and escapes the specified URI anchor string.
     *
     * @param anchor the anchor string to encode and escape
     * @return the encoded and escaped anchor string
     */
    public static String encodeAnchor(final String anchor) {
        if (anchor == null) {
            return null;
        }
        return encode(anchor, ANCHOR_ALLOWED_CHARS, UTF_8);
    }

    /**
     * Encodes and escapes the specified URI hash string.
     *
     * @param hash the anchor string to encode and escape
     * @return the encoded and escaped hash string
     */
    public static String encodeHash(final String hash) {
        if (hash == null) {
            return null;
        }
        return encode(hash, HASH_ALLOWED_CHARS, UTF_8);
    }

    /**
     * Unescapes and decodes the specified string.
     *
     * @param escaped the string to be unescaped and decoded
     * @return the unescaped and decoded string
     */
    public static String decode(final String escaped) {
        try {
            final byte[] bytes = escaped.getBytes(US_ASCII);
            final byte[] bytes2 = URLCodec.decodeUrl(bytes);
            return new String(bytes2, UTF_8);
        }
        catch (final DecoderException e) {
            // Should never happen.
            throw new RuntimeException(e);
        }
    }

    /**
     * Escapes and encodes the specified string. Based on HttpClient 3.1's <tt>URIUtil.encode()</tt> method.
     *
     * @param unescaped the string to encode
     * @param allowed allowed characters that shouldn't be escaped
     * @param charset the charset to use
     * @return the escaped string
     */
    private static String encode(final String unescaped, final BitSet allowed, final Charset charset) {
        final byte[] bytes = unescaped.getBytes(charset);
        final byte[] bytes2 = URLCodec.encodeUrl(allowed, bytes);
        return encodePercentSign(bytes2);
    }

    /**
     * Encodes every occurrence of the escape character '%' in the given input
     * string that is not followed by two hexadecimal characters.
     * @param str the input string
     * @return the given input string where every occurrence of <code>%</code> in
     * invalid escape sequences has been replace by <code>%25</code>
     */
    private static String encodePercentSign(final byte[] input) {
        if (input == null) {
            return null;
        }

        final StringBuilder result = new StringBuilder(new String(input, US_ASCII));
        int state = -0;
        int offset = 0;
        for (int i = 0; i < input.length; i++) {
            final byte b = input[i];
            if (state == 0 && b == '%') {
                state = 1;
            }
            else if (state == 1 || state == 2) {
                if (('0' <= b && b <= '9')
                        || ('A' <= b && b <= 'F')
                        || ('a' <= b && b <= 'f')) {
                    state++;
                    if (state == 3) {
                        state = 0;
                    }
                }
                else {
                    final int st = i - state + offset;
                    result.replace(st, st + 1, "%25");
                    offset = offset + 2;
                    state = b == '%' ? 1 : 0;
                }
            }
        }
        if (state == 1 || state == 2) {
            final int st = input.length - state + offset;
            result.replace(st, st + 1, "%25");
        }
        return result.toString();
    }

    /**
     * Creates and returns a new URL using only the protocol and authority from the given one.
     * @param u the URL on which to base the returned URL
     * @return a new URL using only the protocol and authority from the given one
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithoutPathRefQuery(final URL u) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getAuthority(), null, null, null);
    }

    /**
     * Creates and returns a new URL using only the protocol, authority and path
     * from the given one.
     * @param u the URL on which to base the returned URL
     * @return a new URL using only the protocol and authority from the given one
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithoutRef(final URL u) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getAuthority(), u.getPath(), null, u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified protocol.
     * @param u the URL on which to base the returned URL
     * @param newProtocol the new protocol to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified protocol
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewProtocol(final URL u, final String newProtocol) throws MalformedURLException {
        return createNewUrl(newProtocol, u.getAuthority(), u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified host.
     * @param u the URL on which to base the returned URL
     * @param newHost the new host to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified host
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewHost(final URL u, final String newHost)
        throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getUserInfo(), newHost,
                            u.getPort(), u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified host.
     * @param u the URL on which to base the returned URL
     * @param newHost the new host to use in the returned URL
     * @param newPort the new port to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified host
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewHostAndPort(final URL u, final String newHost, final int newPort)
        throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getUserInfo(), newHost, newPort, u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified port.
     * @param u the URL on which to base the returned URL
     * @param newPort the new port to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified port
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewPort(final URL u, final int newPort) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getUserInfo(), u.getHost(),
                            newPort, u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified path.
     * @param u the URL on which to base the returned URL
     * @param newPath the new path to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified path
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewPath(final URL u, final String newPath) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getAuthority(), newPath, u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified reference.
     * @param u the URL on which to base the returned URL
     * @param newRef the new reference to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified reference
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewRef(final URL u, final String newRef) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getAuthority(), u.getPath(), newRef, u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL, except using the specified query string.
     * @param u the URL on which to base the returned URL
     * @param newQuery the new query string to use in the returned URL
     * @return a new URL identical to the specified URL, except using the specified query string
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewQuery(final URL u, final String newQuery) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getAuthority(), u.getPath(), u.getRef(), newQuery);
    }

    /**
     * Creates and returns a new URL identical to the specified URL, ignoring path, protocol and query.
     * @param u the URL on which to base the returned URL
     * @return a new URL identical to the specified URL, ignoring path, protocol and query
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithProtocolAndAuthority(final URL u) throws MalformedURLException {
        return createNewUrl(u.getProtocol(), u.getAuthority(), null, null, null);
    }

    /**
     * Creates and returns a new URL identical to the specified URL but with a changed user name.
     * @param u the URL on which to base the returned URL
     * @param newUserName the new user name
     * @return a new URL identical to the specified URL; only user name updated
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewUserName(final URL u, final String newUserName) throws MalformedURLException {
        String newUserInfo = newUserName;
        final String userInfo = u.getUserInfo();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userInfo)) {
            final int colonIdx = userInfo.indexOf(':');
            if (colonIdx > -1) {
                newUserInfo = newUserName + userInfo.substring(colonIdx);
            }
        }
        return createNewUrl(u.getProtocol(), newUserInfo,
                u.getHost(), u.getPort(), u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates and returns a new URL identical to the specified URL but with a changed user password.
     * @param u the URL on which to base the returned URL
     * @param newUserPassword the new user password
     * @return a new URL identical to the specified URL; only user name updated
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    public static URL getUrlWithNewUserPassword(final URL u, final String newUserPassword)
            throws MalformedURLException {
        String newUserInfo = ':' + newUserPassword;
        final String userInfo = u.getUserInfo();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userInfo)) {
            final int colonIdx = userInfo.indexOf(':');
            if (colonIdx > -1) {
                newUserInfo = userInfo.substring(0, colonIdx + 1) + newUserPassword;
            }
            else {
                newUserInfo = userInfo + newUserInfo;
            }
        }
        return createNewUrl(u.getProtocol(), newUserInfo,
                u.getHost(), u.getPort(), u.getPath(), u.getRef(), u.getQuery());
    }

    /**
     * Creates a new URL based on the specified fragments.
     * @param protocol the protocol to use (may not be {@code null})
     * @param userInfo the user info to use (may be {@code null})
     * @param host the host to use (may not be {@code null})
     * @param port the port to use (may be <tt>-1</tt> if no port is specified)
     * @param path the path to use (may be {@code null} and may omit the initial <tt>'/'</tt>)
     * @param ref the reference to use (may be {@code null} and must not include the <tt>'#'</tt>)
     * @param query the query to use (may be {@code null} and must not include the <tt>'?'</tt>)
     * @return a new URL based on the specified fragments
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    private static URL createNewUrl(final String protocol, final String userInfo, final String host, final int port,
            final String path, final String ref, final String query) throws MalformedURLException {
        final StringBuilder s = new StringBuilder();
        s.append(protocol).append("://");
        if (userInfo != null) {
            s.append(userInfo).append('@');
        }
        s.append(host);
        if (port != -1) {
            s.append(':').append(port);
        }
        if (path != null && !path.isEmpty()) {
            if ('/' != path.charAt(0)) {
                s.append('/');
            }
            s.append(path);
        }
        if (query != null) {
            s.append('?').append(query);
        }
        if (ref != null) {
            if (ref.isEmpty() || ref.charAt(0) != '#') {
                s.append('#');
            }
            s.append(ref);
        }

        return new URL(s.toString());
    }

    /**
     * Creates a new URL based on the specified fragments.
     * @param protocol the protocol to use (may not be {@code null})
     * @param authority the authority to use (may not be {@code null})
     * @param path the path to use (may be {@code null} and may omit the initial <tt>'/'</tt>)
     * @param ref the reference to use (may be {@code null} and must not include the <tt>'#'</tt>)
     * @param query the query to use (may be {@code null} and must not include the <tt>'?'</tt>)
     * @return a new URL based on the specified fragments
     * @throws MalformedURLException if there is a problem creating the new URL
     */
    private static URL createNewUrl(final String protocol, final String authority,
            final String path, final String ref, final String query) throws MalformedURLException {

        // pre-compute length of StringBuilder
        int len = protocol.length() + 1;
        if (authority != null && !authority.isEmpty()) {
            len += 2 + authority.length();
        }
        if (path != null) {
            len += path.length();
        }
        if (query != null) {
            len += 1 + query.length();
        }
        if (ref != null) {
            len += 1 + ref.length();
        }

        final StringBuilder s = new StringBuilder(len);
        s.append(protocol).append(':');
        if (authority != null && !authority.isEmpty()) {
            s.append("//");
            s.append(authority);
        }
        if (path != null) {
            s.append(path);
        }
        if (query != null) {
            s.append('?');
            s.append(query);
        }
        if (ref != null) {
            if (ref.isEmpty() || ref.charAt(0) != '#') {
                s.append('#');
            }
            s.append(ref);
        }

        return toUrlSafe(s.toString());
    }

    /**
     * Resolves a given relative URL against a base URL. See
     * <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a>
     * Section 4 for more details.
     *
     * @param baseUrl     The base URL in which to resolve the specification.
     * @param relativeUrl The relative URL to resolve against the base URL.
     * @return the resolved specification.
     */
    public static String resolveUrl(final String baseUrl, final String relativeUrl) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("Base URL must not be null");
        }
        if (relativeUrl == null) {
            throw new IllegalArgumentException("Relative URL must not be null");
        }
        final Url url = resolveUrl(parseUrl(baseUrl.trim()), relativeUrl.trim());

        return url.toString();
    }

    /**
     * Resolves a given relative URL against a base URL. See
     * <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a>
     * Section 4 for more details.
     *
     * @param baseUrl     The base URL in which to resolve the specification.
     * @param relativeUrl The relative URL to resolve against the base URL.
     * @return the resolved specification.
     */
    public static String resolveUrl(final URL baseUrl, final String relativeUrl) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("Base URL must not be null");
        }
        return resolveUrl(baseUrl.toExternalForm(), relativeUrl);
    }

    /**
     * Parses a given specification using the algorithm depicted in
     * <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a>:
     *
     * Section 2.4: Parsing a URL
     *
     *   An accepted method for parsing URLs is useful to clarify the
     *   generic-RL syntax of Section 2.2 and to describe the algorithm for
     *   resolving relative URLs presented in Section 4. This section
     *   describes the parsing rules for breaking down a URL (relative or
     *   absolute) into the component parts described in Section 2.1.  The
     *   rules assume that the URL has already been separated from any
     *   surrounding text and copied to a "parse string". The rules are
     *   listed in the order in which they would be applied by the parser.
     *
     * @param spec The specification to parse.
     * @return the parsed specification.
     */
    private static Url parseUrl(final String spec) {
        final Url url = new Url();
        int startIndex = 0;
        int endIndex = spec.length();

        // Section 2.4.1: Parsing the Fragment Identifier
        //
        //   If the parse string contains a crosshatch "#" character, then the
        //   substring after the first (left-most) crosshatch "#" and up to the
        //   end of the parse string is the <fragment> identifier. If the
        //   crosshatch is the last character, or no crosshatch is present, then
        //   the fragment identifier is empty. The matched substring, including
        //   the crosshatch character, is removed from the parse string before
        //   continuing.
        //
        //   Note that the fragment identifier is not considered part of the URL.
        //   However, since it is often attached to the URL, parsers must be able
        //   to recognize and set aside fragment identifiers as part of the
        //   process.
        final int crosshatchIndex = StringUtils.indexOf(spec, '#', startIndex, endIndex);

        if (crosshatchIndex >= 0) {
            url.fragment_ = spec.substring(crosshatchIndex + 1, endIndex);
            endIndex = crosshatchIndex;
        }
        // Section 2.4.2: Parsing the Scheme
        //
        //   If the parse string contains a colon ":" after the first character
        //   and before any characters not allowed as part of a scheme name (i.e.,
        //   any not an alphanumeric, plus "+", period ".", or hyphen "-"), the
        //   <scheme> of the URL is the substring of characters up to but not
        //   including the first colon. These characters and the colon are then
        //   removed from the parse string before continuing.
        final int colonIndex = StringUtils.indexOf(spec, ':', startIndex, endIndex);

        if (colonIndex > 0) {
            final String scheme = spec.substring(startIndex, colonIndex);
            if (isValidScheme(scheme)) {
                url.scheme_ = scheme;
                startIndex = colonIndex + 1;
            }
        }
        // Section 2.4.3: Parsing the Network Location/Login
        //
        //   If the parse string begins with a double-slash "//", then the
        //   substring of characters after the double-slash and up to, but not
        //   including, the next slash "/" character is the network location/login
        //   (<net_loc>) of the URL. If no trailing slash "/" is present, the
        //   entire remaining parse string is assigned to <net_loc>. The double-
        //   slash and <net_loc> are removed from the parse string before
        //   continuing.
        //
        // Note: We also accept a question mark "?" or a semicolon ";" character as
        //       delimiters for the network location/login (<net_loc>) of the URL.
        final int locationStartIndex;
        int locationEndIndex;

        if (spec.startsWith("//", startIndex)) {
            locationStartIndex = startIndex + 2;
            locationEndIndex = StringUtils.indexOf(spec, '/', locationStartIndex, endIndex);
            if (locationEndIndex >= 0) {
                startIndex = locationEndIndex;
            }
        }
        else {
            locationStartIndex = -1;
            locationEndIndex = -1;
        }
        // Section 2.4.4: Parsing the Query Information
        //
        //   If the parse string contains a question mark "?" character, then the
        //   substring after the first (left-most) question mark "?" and up to the
        //   end of the parse string is the <query> information. If the question
        //   mark is the last character, or no question mark is present, then the
        //   query information is empty. The matched substring, including the
        //   question mark character, is removed from the parse string before
        //   continuing.
        final int questionMarkIndex = StringUtils.indexOf(spec, '?', startIndex, endIndex);

        if (questionMarkIndex >= 0) {
            if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
                // The substring of characters after the double-slash and up to, but not
                // including, the question mark "?" character is the network location/login
                // (<net_loc>) of the URL.
                locationEndIndex = questionMarkIndex;
                startIndex = questionMarkIndex;
            }
            url.query_ = spec.substring(questionMarkIndex + 1, endIndex);
            endIndex = questionMarkIndex;
        }
        // Section 2.4.5: Parsing the Parameters
        //
        //   If the parse string contains a semicolon ";" character, then the
        //   substring after the first (left-most) semicolon ";" and up to the end
        //   of the parse string is the parameters (<params>). If the semicolon
        //   is the last character, or no semicolon is present, then <params> is
        //   empty. The matched substring, including the semicolon character, is
        //   removed from the parse string before continuing.
        final int semicolonIndex = StringUtils.indexOf(spec, ';', startIndex, endIndex);

        if (semicolonIndex >= 0) {
            if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
                // The substring of characters after the double-slash and up to, but not
                // including, the semicolon ";" character is the network location/login
                // (<net_loc>) of the URL.
                locationEndIndex = semicolonIndex;
                startIndex = semicolonIndex;
            }
            url.parameters_ = spec.substring(semicolonIndex + 1, endIndex);
            endIndex = semicolonIndex;
        }
        // Section 2.4.6: Parsing the Path
        //
        //   After the above steps, all that is left of the parse string is the
        //   URL <path> and the slash "/" that may precede it. Even though the
        //   initial slash is not part of the URL path, the parser must remember
        //   whether or not it was present so that later processes can
        //   differentiate between relative and absolute paths. Often this is
        //   done by simply storing the preceding slash along with the path.
        if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
            // The entire remaining parse string is assigned to the network
            // location/login (<net_loc>) of the URL.
            locationEndIndex = endIndex;
        }
        else if (startIndex < endIndex) {
            url.path_ = spec.substring(startIndex, endIndex);
        }
        // Set the network location/login (<net_loc>) of the URL.
        if ((locationStartIndex >= 0) && (locationEndIndex >= 0)) {
            url.location_ = spec.substring(locationStartIndex, locationEndIndex);
        }
        return url;
    }

    /*
     * Returns true if specified string is a valid scheme name.
     */
    private static boolean isValidScheme(final String scheme) {
        final int length = scheme.length();
        if (length < 1) {
            return false;
        }
        char c = scheme.charAt(0);
        if (!Character.isLetter(c)) {
            return false;
        }
        for (int i = 1; i < length; i++) {
            c = scheme.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '.' && c != '+' && c != '-') {
                return false;
            }
        }
        return true;
    }

    /**
     * Resolves a given relative URL against a base URL using the algorithm
     * depicted in <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a>:
     *
     * Section 4: Resolving Relative URLs
     *
     *   This section describes an example algorithm for resolving URLs within
     *   a context in which the URLs may be relative, such that the result is
     *   always a URL in absolute form. Although this algorithm cannot
     *   guarantee that the resulting URL will equal that intended by the
     *   original author, it does guarantee that any valid URL (relative or
     *   absolute) can be consistently transformed to an absolute form given a
     *   valid base URL.
     *
     * @param baseUrl     The base URL in which to resolve the specification.
     * @param relativeUrl The relative URL to resolve against the base URL.
     * @return the resolved specification.
     */
    private static Url resolveUrl(final Url baseUrl, final String relativeUrl) {
        final Url url = parseUrl(relativeUrl);
        // Step 1: The base URL is established according to the rules of
        //         Section 3.  If the base URL is the empty string (unknown),
        //         the embedded URL is interpreted as an absolute URL and
        //         we are done.
        if (baseUrl == null) {
            return url;
        }
        // Step 2: Both the base and embedded URLs are parsed into their
        //         component parts as described in Section 2.4.
        //      a) If the embedded URL is entirely empty, it inherits the
        //         entire base URL (i.e., is set equal to the base URL)
        //         and we are done.
        if (relativeUrl.isEmpty()) {
            return new Url(baseUrl);
        }
        //      b) If the embedded URL starts with a scheme name, it is
        //         interpreted as an absolute URL and we are done.
        if (url.scheme_ != null) {
            return url;
        }
        //      c) Otherwise, the embedded URL inherits the scheme of
        //         the base URL.
        url.scheme_ = baseUrl.scheme_;
        // Step 3: If the embedded URL's <net_loc> is non-empty, we skip to
        //         Step 7.  Otherwise, the embedded URL inherits the <net_loc>
        //         (if any) of the base URL.
        if (url.location_ != null) {
            return url;
        }
        url.location_ = baseUrl.location_;
        // Step 4: If the embedded URL path is preceded by a slash "/", the
        //         path is not relative and we skip to Step 7.
        if (url.path_ != null && !url.path_.isEmpty() && url.path_.charAt(0) == '/') {
            url.path_ = removeLeadingSlashPoints(url.path_);
            return url;
        }
        // Step 5: If the embedded URL path is empty (and not preceded by a
        //         slash), then the embedded URL inherits the base URL path,
        //         and
        if (url.path_ == null) {
            url.path_ = baseUrl.path_;
            //  a) if the embedded URL's <params> is non-empty, we skip to
            //     step 7; otherwise, it inherits the <params> of the base
            //     URL (if any) and
            if (url.parameters_ != null) {
                return url;
            }
            url.parameters_ = baseUrl.parameters_;
            //  b) if the embedded URL's <query> is non-empty, we skip to
            //     step 7; otherwise, it inherits the <query> of the base
            //     URL (if any) and we skip to step 7.
            if (url.query_ != null) {
                return url;
            }
            url.query_ = baseUrl.query_;
            return url;
        }
        // Step 6: The last segment of the base URL's path (anything
        //         following the rightmost slash "/", or the entire path if no
        //         slash is present) is removed and the embedded URL's path is
        //         appended in its place.  The following operations are
        //         then applied, in order, to the new path:
        final String basePath = baseUrl.path_;
        String path = "";

        if (basePath == null) {
            path = "/";
        }
        else {
            final int lastSlashIndex = basePath.lastIndexOf('/');

            if (lastSlashIndex >= 0) {
                path = basePath.substring(0, lastSlashIndex + 1);
            }
        }

        path = path.concat(url.path_);
        //      a) All occurrences of "./", where "." is a complete path
        //         segment, are removed.
        int pathSegmentIndex;

        while ((pathSegmentIndex = path.indexOf("/./")) >= 0) {
            path = path.substring(0, pathSegmentIndex + 1).concat(path.substring(pathSegmentIndex + 3));
        }
        //      b) If the path ends with "." as a complete path segment,
        //         that "." is removed.
        if (path.endsWith("/.")) {
            path = path.substring(0, path.length() - 1);
        }
        //      c) All occurrences of "<segment>/../", where <segment> is a
        //         complete path segment not equal to "..", are removed.
        //         Removal of these path segments is performed iteratively,
        //         removing the leftmost matching pattern on each iteration,
        //         until no matching pattern remains.
        while ((pathSegmentIndex = path.indexOf("/../")) > 0) {
            final String pathSegment = path.substring(0, pathSegmentIndex);
            final int slashIndex = pathSegment.lastIndexOf('/');

            if (slashIndex >= 0) {
                if (!"..".equals(pathSegment.substring(slashIndex))) {
                    path = path.substring(0, slashIndex + 1).concat(path.substring(pathSegmentIndex + 4));
                }
            }
            else {
                path = path.substring(pathSegmentIndex + 4);
            }
        }
        //      d) If the path ends with "<segment>/..", where <segment> is a
        //         complete path segment not equal to "..", that
        //         "<segment>/.." is removed.
        if (path.endsWith("/..")) {
            final String pathSegment = path.substring(0, path.length() - 3);
            final int slashIndex = pathSegment.lastIndexOf('/');

            if (slashIndex >= 0) {
                path = path.substring(0, slashIndex + 1);
            }
        }

        path = removeLeadingSlashPoints(path);

        url.path_ = path;
        // Step 7: The resulting URL components, including any inherited from
        //         the base URL, are recombined to give the absolute form of
        //         the embedded URL.
        return url;
    }

    /**
     * "../" after the leading "/" should be removed as browsers do (not in RFC)
     */
    private static String removeLeadingSlashPoints(final String path) {
        int i = 1;
        while (path.startsWith("../", i)) {
            i = i + 3;
        }

        if (i > 1) {
            return "/" + path.substring(i);
        }

        return path;
    }

    /**
     * Class <tt>Url</tt> represents a Uniform Resource Locator.
     *
     * @author Martin Tamme
     */
    private static class Url {

        private String scheme_;
        private String location_;
        private String path_;
        private String parameters_;
        private String query_;
        private String fragment_;

        /**
         * Creates a <tt>Url</tt> object.
         */
        Url() {
        }

        /**
         * Creates a <tt>Url</tt> object from the specified
         * <tt>Url</tt> object.
         *
         * @param url a <tt>Url</tt> object.
         */
        Url(final Url url) {
            scheme_ = url.scheme_;
            location_ = url.location_;
            path_ = url.path_;
            parameters_ = url.parameters_;
            query_ = url.query_;
            fragment_ = url.fragment_;
        }

        /**
         * Returns a string representation of the <tt>Url</tt> object.
         *
         * @return a string representation of the <tt>Url</tt> object.
         */
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();

            if (scheme_ != null) {
                sb.append(scheme_);
                sb.append(':');
            }
            if (location_ != null) {
                sb.append("//");
                sb.append(location_);
            }
            if (path_ != null) {
                sb.append(path_);
            }
            if (parameters_ != null) {
                sb.append(';');
                sb.append(parameters_);
            }
            if (query_ != null) {
                sb.append('?');
                sb.append(query_);
            }
            if (fragment_ != null) {
                sb.append('#');
                sb.append(fragment_);
            }
            return sb.toString();
        }
    }

    static boolean isNormalUrlProtocol(final String protocol) {
        return "http".equals(protocol) || "https".equals(protocol) || "file".equals(protocol);
    }

    /**
     * More or less the same as sameFile(URL, URL) but without
     * resolving the host to an IP address for comparing.
     * Additionally we do some path normalization.
     *
     * @param u1 a URL object
     * @param u2 a URL object
     * @return true if u1 and u2 refer to the same file
     */
    public static boolean sameFile(final URL u1, final URL u2) {
        if (u1 == u2) {
            return true;
        }
        if (u1 == null || u2 == null) {
            return false;
        }

        // Compare the protocols.
        final String p1 = u1.getProtocol();
        final String p2 = u2.getProtocol();
        if (!(p1 == p2 || (p1 != null && p1.equalsIgnoreCase(p2)))) {
            return false;
        }

        // Compare the ports.
        final int port1 = (u1.getPort() == -1) ? u1.getDefaultPort() : u1.getPort();
        final int port2 = (u2.getPort() == -1) ? u2.getDefaultPort() : u2.getPort();
        if (port1 != port2) {
            return false;
        }

        // Compare the hosts.
        final String h1 = u1.getHost();
        final String h2 = u2.getHost();
        if (!(h1 == h2 || (h1 != null && h1.equalsIgnoreCase(h2)))) {
            return false;
        }

        // Compare the files.
        String f1 = u1.getFile();
        if (f1.isEmpty()) {
            f1 = "/";
        }
        String f2 = u2.getFile();
        if (f2.isEmpty()) {
            f2 = "/";
        }
        if (f1.indexOf('.') > 0 || f2.indexOf('.') > 0) {
            try {
                f1 = u1.toURI().normalize().toURL().getFile();
                f2 = u2.toURI().normalize().toURL().getFile();
            }
            catch (final Exception e) {
                // ignore
            }
        }

        return f1 == f2 || (f1 != null && f1.equals(f2));
    }

    /**
     * Helper that constructs a normalized url string
     * usable as cache key.
     *
     * @param url a URL object
     * @return the normalized string
     */
    public static String normalize(final URL url) {
        final StringBuilder result = new StringBuilder();
        result.append(url.getProtocol())
                .append("://")
                .append(url.getHost())
                .append(':')
                .append((url.getPort() == -1) ? url.getDefaultPort() : url.getPort());

        // Compare the files.
        String f = url.getFile();
        if (f.isEmpty()) {
            result.append('/');
        }
        else {
            if (f.indexOf('.') > 0) {
                try {
                    f = url.toURI().normalize().toURL().getFile();
                }
                catch (final Exception e) {
                    // ignore
                }
            }
            result.append(f);
        }

        return result.toString();
    }

    /**
     * Constructs a {@link URI} using the specified URL.
     *
     * @param url the URL
     * @param query the query
     *
     * @throws URISyntaxException
     *         If both a scheme and a path are given but the path is
     *         relative, if the URI string constructed from the given
     *         components violates RFC&nbsp;2396, or if the authority
     *         component of the string is present but cannot be parsed
     *         as a server-based authority
     * @return the URI
     */
    public static URI toURI(final URL url, final String query) throws URISyntaxException {
        final String scheme = url.getProtocol();
        final String host = url.getHost();
        final int port = url.getPort();
        final String path = url.getPath();
        final StringBuilder buffer = new StringBuilder();
        if (host != null) {
            if (scheme != null) {
                buffer.append(scheme);
                buffer.append("://");
            }
            buffer.append(host);
            if (port > 0) {
                buffer.append(':');
                buffer.append(port);
            }
        }
        if (path == null || path.isEmpty() || path.charAt(0) != '/') {
            buffer.append('/');
        }
        if (path != null) {
            buffer.append(path);
        }
        if (query != null) {
            buffer.append('?');
            buffer.append(query);
        }
        return new URI(buffer.toString());
    }
}
