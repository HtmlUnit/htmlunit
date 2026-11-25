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
package org.htmlunit.httpclient;

import static org.htmlunit.BrowserVersionFeatures.HTTP_COOKIE_HOST_PREFIX_ALLOWS_DOMAIN_OF_HOST;
import static org.htmlunit.BrowserVersionFeatures.HTTP_COOKIE_HOST_PREFIX_PATH_ATTRIBUTE_OPTIONAL;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookiePathComparator;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SM;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicCommentHandler;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.message.TokenParser;
import org.apache.http.util.CharArrayBuffer;
import org.htmlunit.BrowserVersion;
import org.htmlunit.util.StringUtils;

/**
 * Customized BrowserCompatSpec for HtmlUnit.
 * <p>
 * Workaround for <a href="https://issues.apache.org/jira/browse/HTTPCLIENT-1006">HttpClient bug 1006</a>:
 * quotes are wrongly removed in cookie's values.
 * </p>
 * Implementation is based on the HttpClient code.
 *
 * @author Mike Bowler
 * @author Noboru Sinohara
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Brad Clarke
 * @author Ahmed Ashour
 * @author Nicolas Belisle
 * @author Ronald Brill
 * @author John J Murdoch
 */
public class HtmlUnitBrowserCompatCookieSpec extends CookieSpecBase {

    /** The cookie name used for cookies with no name (HttpClient doesn't like empty names). */
    public static final String EMPTY_COOKIE_NAME = "HTMLUNIT_EMPTY_COOKIE";

    /** Workaround for domain of local files. */
    public static final String LOCAL_FILESYSTEM_DOMAIN = "LOCAL_FILESYSTEM";

    private static final String SECURE_PREFIX = "__Secure-";
    private static final String HOST_PREFIX = "__Host-";
    private static final String COOKIE_MESSAGE_PREFIX = "Cookie '";

    /**
     * Comparator for sending cookies in right order.
     * See specification:
     * - RFC2109 (#4.3.4) http://www.ietf.org/rfc/rfc2109.txt
     * - RFC2965 (#3.3.4) http://www.ietf.org/rfc/rfc2965.txt http://www.ietf.org/rfc/rfc2109.txt
     */
    private static final Comparator<Cookie> COOKIE_COMPARATOR = new CookiePathComparator();

    private static final NetscapeDraftHeaderParser DEFAULT_NETSCAPE_DRAFT_HEADER_PARSER
                            = new NetscapeDraftHeaderParser();

    static final Date DATE_1_1_1970;

    static {
        final Calendar calendar = Calendar.getInstance(Locale.ROOT);
        calendar.setTimeZone(DateUtils.GMT);
        calendar.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        DATE_1_1_1970 = calendar.getTime();
    }

    private final BrowserVersion browserVersion_;

    /**
     * Constructor.
     *
     * @param browserVersion the {@link BrowserVersion} to simulate
     */
    public HtmlUnitBrowserCompatCookieSpec(final BrowserVersion browserVersion) {
        super(new HtmlUnitVersionAttributeHandler(),
                new HtmlUnitDomainHandler(),
                new HtmlUnitPathHandler(),
                new HtmlUnitMaxAgeHandler(),
                new HtmlUnitSecureHandler(),
                new BasicCommentHandler(),
                new HtmlUnitExpiresHandler(browserVersion),
                new HtmlUnitHttpOnlyHandler(),
                new HtmlUnitSameSiteHandler());
        browserVersion_ = browserVersion;
    }

    /**
     * {@inheritDoc}
     * Additionally checks the cookie name prefixes '__Secure-' and '__Host-' (case-insensitive).
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/draft-ietf-httpbis-rfc6265bis#name-cookie-name-prefixes">
     *     RFC 6265bis - Cookie Name Prefixes</a>
     */
    @Override
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        super.validate(cookie, origin);

        final String name = cookie.getName();
        if (StringUtils.startsWithIgnoreCase(name, SECURE_PREFIX)) {
            if (!cookie.isSecure()) {
                throw new CookieRestrictionViolationException(
                        COOKIE_MESSAGE_PREFIX + name + "' requires the 'secure' attribute");
            }
        }
        else if (StringUtils.startsWithIgnoreCase(name, HOST_PREFIX)) {
            validateHostPrefix(cookie, origin);
        }
    }

    /**
     * A '__Host-' cookie has to be secure and host-only (no domain attribute) and has to use the path '/'.
     *
     * @param cookie the cookie to check
     * @param origin the origin of the cookie
     * @throws CookieRestrictionViolationException if the cookie violates the rules
     */
    private void validateHostPrefix(final Cookie cookie, final CookieOrigin origin)
            throws CookieRestrictionViolationException {
        final String name = cookie.getName();
        if (!cookie.isSecure()) {
            throw new CookieRestrictionViolationException(
                    COOKIE_MESSAGE_PREFIX + name + "' requires the 'secure' attribute");
        }

        final ClientCookie clientCookie = cookie instanceof ClientCookie cc ? cc : null;
        if (clientCookie != null && clientCookie.containsAttribute(ClientCookie.DOMAIN_ATTR)) {
            String domain = cookie.getDomain();
            if (domain != null && domain.startsWith(".")) {
                domain = domain.substring(1);
            }
            final boolean domainOfHost = domain != null && domain.equalsIgnoreCase(origin.getHost());
            if (!domainOfHost || !browserVersion_.hasFeature(HTTP_COOKIE_HOST_PREFIX_ALLOWS_DOMAIN_OF_HOST)) {
                throw new CookieRestrictionViolationException(
                        COOKIE_MESSAGE_PREFIX + name + "' does not allow the 'domain' attribute");
            }
        }

        if (!"/".equals(cookie.getPath())
                || (!browserVersion_.hasFeature(HTTP_COOKIE_HOST_PREFIX_PATH_ATTRIBUTE_OPTIONAL)
                        && (clientCookie == null || !clientCookie.containsAttribute(ClientCookie.PATH_ATTR)))) {
            throw new CookieRestrictionViolationException(
                    COOKIE_MESSAGE_PREFIX + name + "' requires the 'path' attribute '/'");
        }
    }

    /**
     * A hack to support cookies without a name; such cookies get the name {@link #EMPTY_COOKIE_NAME}.
     *
     * @param header the cookie header
     * @return the given header or a new header with the cookie name added
     */
    private static Header supportEmptyCookieName(final Header header) {
        final String text = header.getValue();
        int endPos = text.indexOf(';');
        if (endPos < 0) {
            endPos = text.indexOf('=');
        }
        else {
            final int pos = text.indexOf('=');
            if (pos > endPos) {
                endPos = -1;
            }
            else {
                endPos = pos;
            }
        }
        if (endPos < 0) {
            return new BasicHeader(header.getName(), EMPTY_COOKIE_NAME + "=" + header.getValue());
        }
        if (endPos == 0 || StringUtils.isBlank(text.substring(0, endPos))) {
            return new BasicHeader(header.getName(), EMPTY_COOKIE_NAME + header.getValue());
        }
        return header;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Cookie> parse(final Header rawHeader, final CookieOrigin origin) throws MalformedCookieException {
        final Header header = supportEmptyCookieName(rawHeader);

        final String headername = header.getName();
        if (!SM.SET_COOKIE.equalsIgnoreCase(headername)) {
            throw new MalformedCookieException("Unrecognized cookie header '" + header + "'");
        }
        final HeaderElement[] helems = header.getElements();
        boolean versioned = false;
        boolean netscape = false;
        for (final HeaderElement helem: helems) {
            if (helem.getParameterByName("version") != null) {
                versioned = true;
            }
            if (helem.getParameterByName("expires") != null) {
                netscape = true;
            }
        }

        final List<Cookie> cookies;
        if (netscape || !versioned) {
            // Need to parse the header again, because Netscape style cookies do not correctly
            // support multiple header elements (comma cannot be treated as an element separator)
            final CharArrayBuffer buffer;
            final ParserCursor cursor;
            if (header instanceof FormattedHeader formattedHeader) {
                buffer = formattedHeader.getBuffer();
                cursor = new ParserCursor(
                        formattedHeader.getValuePos(),
                        buffer.length());
            }
            else {
                final String s = header.getValue();
                if (s == null) {
                    throw new MalformedCookieException("Header value is null");
                }
                buffer = new CharArrayBuffer(s.length());
                buffer.append(s);
                cursor = new ParserCursor(0, buffer.length());
            }
            final HeaderElement elem = DEFAULT_NETSCAPE_DRAFT_HEADER_PARSER.parseHeader(buffer, cursor);
            final String name = elem.getName();
            if (name == null || name.isEmpty()) {
                throw new MalformedCookieException("Cookie name may not be empty");
            }
            final String value = elem.getValue();
            final BasicClientCookie cookie = new BasicClientCookie(name, value);
            // used for the SameSite handling (Lax-allowing-unsafe)
            cookie.setCreationDate(new Date());
            cookie.setPath(getDefaultPath(origin));
            cookie.setDomain(getDefaultDomain(origin));

            // cycle through the parameters
            final NameValuePair[] attribs = elem.getParameters();
            for (int j = attribs.length - 1; j >= 0; j--) {
                final NameValuePair attrib = attribs[j];
                final String s = attrib.getName().toLowerCase(Locale.ROOT);
                cookie.setAttribute(s, attrib.getValue());
                final CookieAttributeHandler handler = findAttribHandler(s);
                if (handler != null) {
                    handler.parse(cookie, attrib.getValue());
                }
            }
            // Override version for Netscape style cookies
            if (netscape) {
                cookie.setVersion(0);
            }
            cookies = Collections.singletonList(cookie);
        }
        else {
            cookies = parse(helems, origin);
        }

        for (final Cookie c : cookies) {
            // re-add quotes around value if parsing as incorrectly trimmed them
            if (header.getValue().contains(c.getName() + "=\"" + c.getValue())) {
                ((BasicClientCookie) c).setValue('"' + c.getValue() + '"');
            }
        }
        return cookies;
    }

    @Override
    public List<Header> formatCookies(final List<Cookie> cookies) {
        cookies.sort(COOKIE_COMPARATOR);

        final CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
        buffer.append(SM.COOKIE);
        buffer.append(": ");
        final int size = cookies.size();
        for (int i = 0; i < size; i++) {
            final Cookie cookie = cookies.get(i);
            if (i > 0) {
                buffer.append("; ");
            }
            final String cookieName = cookie.getName();
            final String cookieValue = cookie.getValue();
            if (cookie.getVersion() > 0 && !isQuoteEnclosed(cookieValue)) {
                HtmlUnitBrowserCompatCookieHeaderValueFormatter.INSTANCE.formatHeaderElement(
                        buffer,
                        new BasicHeaderElement(cookieName, cookieValue),
                        false);
            }
            else {
                // Netscape style cookies do not support quoted values
                buffer.append(cookieName);
                buffer.append("=");
                if (cookieValue != null) {
                    buffer.append(cookieValue);
                }
            }
        }
        final List<Header> headers = new ArrayList<>(1);
        headers.add(new BufferedHeader(buffer));
        return headers;
    }

    private static boolean isQuoteEnclosed(final String s) {
        return s != null
                && s.length() > 1
                && '\"' == s.charAt(0)
                && '\"' == s.charAt(s.length() - 1);
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public Header getVersionHeader() {
        return null;
    }

    @Override
    public String toString() {
        return "compatibility";
    }

    private static final class NetscapeDraftHeaderParser {

        private static final char PARAM_DELIMITER = ';';

        // IMPORTANT!
        // These private static variables must be treated as immutable and never exposed outside this class
        private static final BitSet TOKEN_DELIMS = TokenParser.INIT_BITSET('=', PARAM_DELIMITER);
        private static final BitSet VALUE_DELIMS = TokenParser.INIT_BITSET(PARAM_DELIMITER);

        private static final TokenParser TOKEN_PARSER = TokenParser.INSTANCE;

        HeaderElement parseHeader(final CharArrayBuffer buffer, final ParserCursor cursor) throws ParseException {
            final NameValuePair nvp = parseNameValuePair(buffer, cursor);
            final List<NameValuePair> params = new ArrayList<>();
            while (!cursor.atEnd()) {
                final NameValuePair param = parseNameValuePair(buffer, cursor);
                params.add(param);
            }

            return new BasicHeaderElement(nvp.getName(), nvp.getValue(),
                    params.toArray(new NameValuePair[0]));
        }

        private NameValuePair parseNameValuePair(final CharArrayBuffer buffer, final ParserCursor cursor) {
            final String name = TOKEN_PARSER.parseToken(buffer, cursor, TOKEN_DELIMS);
            if (cursor.atEnd()) {
                return new BasicNameValuePair(name, null);
            }

            final int delim = buffer.charAt(cursor.getPos());
            cursor.updatePos(cursor.getPos() + 1);
            if (delim != '=') {
                return new BasicNameValuePair(name, null);
            }

            final String value = TOKEN_PARSER.parseToken(buffer, cursor, VALUE_DELIMS);
            if (!cursor.atEnd()) {
                cursor.updatePos(cursor.getPos() + 1);
            }

            return new BasicNameValuePair(name, value);
        }
    }
}
