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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.conn.util.PublicSuffixList;
import org.apache.http.conn.util.PublicSuffixListParser;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;
import org.htmlunit.BrowserVersion;
import org.htmlunit.util.UrlUtils;

/**
 * Helper methods to convert from/to HttpClient.
 *
 * @author Ronald Brill
 * @author Vidya m
 */
public final class HttpClientConverter {

    private static final Log LOG = LogFactory.getLog(HttpClientConverter.class);

    // like the browsers, all rules of the public suffix list (ICANN and PRIVATE section) are used;
    // the default matcher of HttpClient (PublicSuffixMatcherLoader.getDefault()) handles the private
    // suffixes (e.g. 'github.io') as registrable domains
    private static final PublicSuffixMatcher PUBLIC_SUFFIX_MATCHER = loadPublicSuffixMatcher();

    private HttpClientConverter() {
        // util class
    }

    /**
     * Returns the {@link PublicSuffixMatcher} used for cookies and the site computation;
     * the private suffixes (e.g. 'github.io') are handled as public suffixes like in the browsers.
     *
     * @return the public suffix matcher
     */
    static PublicSuffixMatcher getPublicSuffixMatcher() {
        return PUBLIC_SUFFIX_MATCHER;
    }

    private static PublicSuffixMatcher loadPublicSuffixMatcher() {
        // the list is part of the HttpClient jar
        final URL url = PublicSuffixMatcherLoader.class.getResource("/mozilla/public-suffix-list.txt");
        if (url != null) {
            try (Reader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
                // parse() ignores the sections; all rules are handled the same way
                final PublicSuffixList list = new PublicSuffixListParser().parse(reader);
                return new PublicSuffixMatcher(list.getRules(), list.getExceptions());
            }
            catch (final IOException e) {
                LOG.warn("Failure loading the public suffix list; using the default", e);
            }
        }
        return PublicSuffixMatcherLoader.getDefault();
    }

    /**
     * Returns the "registrable domain" (effective TLD+1) for the given host, per the
     * public suffix list (https://publicsuffix.org/). Backed by Apache HttpClient's own
     * {@link PublicSuffixMatcher}, already on the classpath as part of the httpclient
     * dependency this class already uses elsewhere for cookie handling - no new
     * dependency needed, and it avoids the wrong results a naive "last two labels"
     * heuristic would give for domains under multi-part public suffixes (e.g. a
     * {@code co.uk}-style suffix, or a private-section entry like {@code github.io}).
     * <p>
     * Note this is deliberately unrelated to {@link HtmlUnitDomainHandler}'s cookie
     * domain-matching: RFC 6265 cookie domain-match is a simple suffix/subdomain check
     * against a single cookie-supplied domain, not a registrable-domain computation
     * between two hosts, so there is nothing to share between the two.
     * </p>
     * @param host the host part of a URL
     * @return the registrable domain, or the host itself if it doesn't have one (e.g.
     *         a bare public suffix, a single-label host like {@code localhost}, or an
     *         IP address - which per the HTML "obtain a site" algorithm is always its
     *         own site, never run through the public suffix list at all)
     */
    public static String registrableDomain(final String host) {
        if (InetAddressUtils.isIPv4Address(host) || InetAddressUtils.isIPv6Address(host)) {
            return host;
        }
        // getDomainRoot() returns null for a bare public suffix (e.g. 'com' or 'co.uk')
        final String domainRoot = PUBLIC_SUFFIX_MATCHER.getDomainRoot(host);
        return domainRoot == null ? host : IDN.toASCII(domainRoot);
    }

    /**
     * Same-site comparison per the HTML "same site" algorithm: same scheme and same
     * registrable domain (eTLD+1), ignoring port and subdomain.
     *
     * @param a first URL
     * @param b second URL
     * @return whether both URLs are considered "same-site"
     */
    public static boolean isSameSite(final URL a, final URL b) {
        if (!a.getProtocol().equals(b.getProtocol())) {
            return false;
        }

        // both are normalized and in lower case
        return registrableDomain(a.getHost()).equals(registrableDomain(b.getHost()));
    }

    /**
     * Returns whether the origin is handled as secure for cookies: https and, like the browsers,
     * localhost and the loopback addresses (potentially trustworthy origins).
     *
     * @param origin the origin
     * @return true if the origin is handled as secure
     * @see <a href="https://w3c.github.io/webappsec-secure-contexts/#is-origin-trustworthy">
     *     Secure Contexts - Is origin potentially trustworthy?</a>
     */
    public static boolean isSecureCookieOrigin(final CookieOrigin origin) {
        if (origin.isSecure()) {
            return true;
        }

        final String host = origin.getHost().toLowerCase(Locale.ROOT);
        return "localhost".equals(host)
                || host.endsWith(".localhost")
                || (InetAddressUtils.isIPv4Address(host) && host.startsWith("127."))
                || "::1".equals(host)
                || "[::1]".equals(host);
    }

    /**
     * Returns whether a (non-secure) cookie from an insecure origin would overwrite or shadow
     * one of the given secure cookies; such a cookie is ignored (RFC 6265bis section 5.7, step 16):
     * a secure cookie with the same name, a domain that domain-matches the domain of the new cookie
     * (or the other way round) and a path that is path-matched by the path of the new cookie.
     *
     * @param cookie the new cookie
     * @param existingCookies the stored cookies
     * @return true if the cookie has to be ignored
     */
    public static boolean shadowsSecureCookie(final org.htmlunit.http.Cookie cookie,
            final Collection<org.htmlunit.http.Cookie> existingCookies) {
        for (final org.htmlunit.http.Cookie existing : existingCookies) {
            if (existing.isSecure()
                    && shadows(cookie.getName(), cookie.getDomain(), cookie.getPath(),
                            existing.getName(), existing.getDomain(), existing.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Same as {@link #shadowsSecureCookie(org.htmlunit.http.Cookie, Collection)} for HttpClient cookies.
     *
     * @param cookie the new cookie
     * @param existingCookies the stored cookies
     * @return true if the cookie has to be ignored
     */
    static boolean shadowsSecureCookie(final Cookie cookie, final Collection<Cookie> existingCookies) {
        for (final Cookie existing : existingCookies) {
            if (existing.isSecure()
                    && shadows(cookie.getName(), cookie.getDomain(), cookie.getPath(),
                            existing.getName(), existing.getDomain(), existing.getPath())) {
                return true;
            }
        }
        return false;
    }

    private static boolean shadows(final String name, final String domain, final String path,
            final String existingName, final String existingDomain, final String existingPath) {
        if (name == null || !name.equals(existingName)) {
            return false;
        }

        final String d = normalizeCookieDomain(domain);
        final String e = normalizeCookieDomain(existingDomain);
        if (!(d.equals(e) || d.endsWith("." + e) || e.endsWith("." + d))) {
            return false;
        }

        // the path of the new cookie path-matches the path of the existing one
        final String p = path == null || path.isEmpty() ? "/" : path;
        final String ep = existingPath == null || existingPath.isEmpty() ? "/" : existingPath;
        return p.equals(ep)
                || (p.startsWith(ep) && (ep.endsWith("/") || p.charAt(ep.length()) == '/'));
    }

    private static String normalizeCookieDomain(final String domain) {
        if (domain == null) {
            return "";
        }
        final String d = domain.toLowerCase(Locale.ROOT);
        return d.startsWith(".") ? d.substring(1) : d;
    }

    /**
     * Helper that builds a CookieOrigin.
     * @param url the url to be used
     * @return the new CookieOrigin
     */
    public static CookieOrigin buildCookieOrigin(final URL url) {
        final URL normalizedUrl = replaceForCookieIfNecessary(url);

        int port = normalizedUrl.getPort();
        if (port == -1) {
            port = normalizedUrl.getDefaultPort();
        }

        return new CookieOrigin(
                normalizedUrl.getHost(),
                port,
                normalizedUrl.getPath(),
                "https".equals(normalizedUrl.getProtocol()));
    }

    /**
     * {@link CookieOrigin} doesn't like empty hosts and negative ports,
     * but these things happen if we're dealing with a local file.
     * This method allows us to work around this limitation in HttpClient by feeding it a bogus host and port.
     *
     * @param url the URL to replace if necessary
     * @return the replacement URL, or the original URL if no replacement was necessary
     */
    public static URL replaceForCookieIfNecessary(final URL url) {
        if (!"file".equals(url.getProtocol())) {
            return url;
        }

        try {
            return UrlUtils.getUrlWithNewHostAndPort(url,
                    HtmlUnitBrowserCompatCookieSpec.LOCAL_FILESYSTEM_DOMAIN, 0);
        }
        catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses the specified cookie string into HtmlUnit cookie objects.
     *
     * @param cookieString the {@code Set-Cookie} header value to parse
     * @param pageUrl the URL of the page from which the cookie originates
     * @param browserVersion the browser version used to determine cookie parsing behavior
     * @return a list of parsed {@link org.htmlunit.http.Cookie} instances
     * @throws MalformedCookieException if the cookie string does not conform to the cookie specification
     */
    public static List<org.htmlunit.http.Cookie> parseCookie(final String cookieString, final URL pageUrl,
                                                             final BrowserVersion browserVersion)
            throws MalformedCookieException {
        final CharArrayBuffer buffer = new CharArrayBuffer(cookieString.length() + 22);
        buffer.append("Set-Cookie: ");
        buffer.append(cookieString);

        final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(browserVersion);
        final CookieOrigin cookieOrigin = buildCookieOrigin(pageUrl);
        final List<Cookie> cookies = cookieSpec.parse(new BufferedHeader(buffer), cookieOrigin);

        final List<org.htmlunit.http.Cookie> htmlUnitCookies = new ArrayList<>(cookies.size());
        for (final Cookie cookie : cookies) {
            // the same checks HttpClient does for Set-Cookie headers (e.g. the domain has to match the page)
            cookieSpec.validate(cookie, cookieOrigin);

            final org.htmlunit.http.Cookie htmlUnitCookie = new HttpClientCookie((ClientCookie) cookie);
            htmlUnitCookies.add(htmlUnitCookie);
        }
        return htmlUnitCookies;
    }

    /**
     * Converts the specified collection of cookies into a collection of HttpClient cookies.
     * @param cookies the cookies to be converted
     * @return the specified cookies, as HttpClient cookies
     */
    public static List<Cookie> toHttpClient(final Collection<org.htmlunit.http.Cookie> cookies) {
        final ArrayList<Cookie> array = new ArrayList<>(cookies.size());
        for (final org.htmlunit.http.Cookie cookie : cookies) {
            array.add(toHttpClient(cookie));
        }
        return array;
    }

    /**
     * Adds all matching cookies to the provided set.
     * @param cookies the cookies to select from
     * @param normalizedUrl the url to match against
     * @param browserVersion the {@link BrowserVersion}
     * @param matches the set to add
     */
    public static void addMatching(final Set<org.htmlunit.http.Cookie> cookies,
            final URL normalizedUrl, final BrowserVersion browserVersion,
            final Set<org.htmlunit.http.Cookie> matches) {
        if (!cookies.isEmpty()) {
            final CookieOrigin cookieOrigin = HttpClientConverter.buildCookieOrigin(normalizedUrl);
            final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(browserVersion);
            for (final org.htmlunit.http.Cookie cookie : cookies) {
                if (cookieSpec.match(toHttpClient(cookie), cookieOrigin)) {
                    matches.add(cookie);
                }
            }
        }
    }

    private static ClientCookie toHttpClient(final org.htmlunit.http.Cookie cookie) {
        if (cookie instanceof HttpClientCookie) {
            return ((HttpClientCookie) cookie).getHttpClientCookie();
        }

        final BasicClientCookie httpClientCookie = new BasicClientCookie(cookie.getName(),
                cookie.getValue() == null ? "" : cookie.getValue());

        httpClientCookie.setDomain(cookie.getDomain());
        // BasicDomainHandler.match(Cookie, CookieOrigin) checks the attribute also (see #333)
        httpClientCookie.setAttribute(ClientCookie.DOMAIN_ATTR, cookie.getDomain());

        httpClientCookie.setPath(cookie.getPath());
        httpClientCookie.setExpiryDate(cookie.getExpires());

        httpClientCookie.setSecure(cookie.isSecure());
        if (cookie.isHttpOnly()) {
            httpClientCookie.setAttribute("httponly", "true");
        }

        if (cookie.getSameSite() != null) {
            httpClientCookie.setAttribute("samesite", cookie.getSameSite());
        }

        return httpClientCookie;
    }
}
