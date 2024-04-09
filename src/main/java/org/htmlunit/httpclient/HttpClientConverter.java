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
package org.htmlunit.httpclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.http.NoHttpResponseException;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;
import org.htmlunit.BrowserVersion;

/**
 * Helper methods to convert from/to HttpClient.
 *
 * @author Ronald Brill
 */
public final class HttpClientConverter {

    private HttpClientConverter() {
    }

    /**
     * @param e the exception to check
     * @return true if the provided Exception is na {@link NoHttpResponseException}
     */
    public static boolean isNoHttpResponseException(final Exception e) {
        return e instanceof NoHttpResponseException;
    }

    /**
     * Helper that builds a CookieOrigin.
     * @param url the url to be used
     * @return the new CookieOrigin
     */
    private static CookieOrigin buildCookieOrigin(final URL url) {
        int port = url.getPort();
        if (port == -1) {
            port = url.getDefaultPort();
        }

        return new CookieOrigin(
                url.getHost(),
                port,
                url.getPath(),
                "https".equals(url.getProtocol()));
    }

    public static List<org.htmlunit.http.Cookie> parseCookie(final String cookieString, final URL pageUrl,
            final BrowserVersion browserVersion)
            throws MalformedCookieException {
        final String host = pageUrl.getHost();
        // URLs like "about:blank" don't have cookies and we need to catch these
        // cases here before HttpClient complains
        if (host.isEmpty()) {
            return new ArrayList<>(0);
        }

        final String protocol = pageUrl.getProtocol();
        if (protocol == null || !protocol.toLowerCase(Locale.ROOT).startsWith("http")) {
            return new ArrayList<>(0);
        }

        final CharArrayBuffer buffer = new CharArrayBuffer(cookieString.length() + 22);
        buffer.append("Set-Cookie: ");
        buffer.append(cookieString);

        final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(browserVersion);
        final List<Cookie> cookies = cookieSpec.parse(new BufferedHeader(buffer), buildCookieOrigin(pageUrl));

        final List<org.htmlunit.http.Cookie> htmlUnitCookies = new ArrayList<>(cookies.size());
        for (final Cookie cookie : cookies) {
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

    public static void addMatching(final Set<org.htmlunit.http.Cookie> cookies,
            final URL url, final BrowserVersion browserVersion,
            final Set<org.htmlunit.http.Cookie> matches) {
        final String host = url.getHost();
        // URLs like "about:blank" don't have cookies and we need to catch these
        // cases here before HttpClient complains
        if (host.isEmpty()) {
            return;
        }

        final String protocol = url.getProtocol();
        if (protocol == null || !protocol.toLowerCase(Locale.ROOT).startsWith("http")) {
            return;
        }

        if (cookies.size() > 0) {
            final CookieOrigin cookieOrigin = HttpClientConverter.buildCookieOrigin(url);
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
        // BasicDomainHandler.match(Cookie, CookieOrigin) checks the attib also (see #333)
        httpClientCookie.setAttribute(ClientCookie.DOMAIN_ATTR, httpClientCookie.getDomain());

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
