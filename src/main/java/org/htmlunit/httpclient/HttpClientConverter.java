/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;
import org.htmlunit.BrowserVersion;
import org.htmlunit.http.HttpStatus;
import org.htmlunit.http.HttpUtils;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.UrlUtils;

/**
 * Helper methods to convert from/to HttpClient.
 *
 * @author Ronald Brill
 */
public final class HttpClientConverter {

    private HttpClientConverter() {
        // util class
    }

    /**
     * Converts the specified name/value pairs into HttpClient name/value pairs.
     * @param pairs the name/value pairs to convert
     * @return the converted name/value pairs
     */
    public static List<org.apache.http.NameValuePair> nameValuePairsToHttpClient(final List<NameValuePair> pairs) {
        final List<org.apache.http.NameValuePair> resultingPairs = new ArrayList<>(pairs.size());
        for (final NameValuePair pair : pairs) {
            resultingPairs.add(new BasicNameValuePair(pair.getName(), pair.getValue()));
        }
        return resultingPairs;
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
    public static URL replaceForCookieIfNecessary(URL url) {
        final String protocol = url.getProtocol();
        final boolean file = "file".equals(protocol);
        if (file) {
            try {
                url = UrlUtils.getUrlWithNewHostAndPort(url,
                        HtmlUnitBrowserCompatCookieSpec.LOCAL_FILESYSTEM_DOMAIN, 0);
            }
            catch (final MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return url;
    }

    /**
     * @param cookieString the string to parse
     * @param pageUrl the page url as root
     * @param browserVersion the {@link BrowserVersion}
     * @return a list of {@link org.htmlunit.util.Cookie}'s
     * @throws MalformedCookieException in case the cookie does not conform to the spec
     */
    public static List<org.htmlunit.util.Cookie> parseCookie(final String cookieString, final URL pageUrl,
            final BrowserVersion browserVersion)
            throws MalformedCookieException {
        final CharArrayBuffer buffer = new CharArrayBuffer(cookieString.length() + 22);
        buffer.append("Set-Cookie: ");
        buffer.append(cookieString);

        final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(browserVersion);
        final List<Cookie> cookies = cookieSpec.parse(new BufferedHeader(buffer), buildCookieOrigin(pageUrl));

        final List<org.htmlunit.util.Cookie> htmlUnitCookies = new ArrayList<>(cookies.size());
        for (final Cookie cookie : cookies) {
            final org.htmlunit.util.Cookie htmlUnitCookie = new org.htmlunit.util.Cookie((ClientCookie) cookie);
            htmlUnitCookies.add(htmlUnitCookie);
        }
        return htmlUnitCookies;
    }

    /**
     * Converts the specified collection of cookies into a collection of HttpClient cookies.
     * @param cookies the cookies to be converted
     * @return the specified cookies, as HttpClient cookies
     */
    public static List<Cookie> toHttpClient(final Collection<org.htmlunit.util.Cookie> cookies) {
        final ArrayList<Cookie> array = new ArrayList<>(cookies.size());
        for (final org.htmlunit.util.Cookie cookie : cookies) {
            array.add(cookie.toHttpClient());
        }
        return array;
    }

    /**
     * Converts the specified array of HttpClient cookies into a list of cookies.
     * @param cookies the cookies to be converted
     * @return the specified HttpClient cookies, as cookies
     */
    public static List<org.htmlunit.util.Cookie> fromHttpClient(final List<Cookie> cookies) {
        final List<org.htmlunit.util.Cookie> list = new ArrayList<>(cookies.size());
        for (final Cookie c : cookies) {
            list.add(new org.htmlunit.util.Cookie((ClientCookie) c));
        }
        return list;
    }

    /**
     * Adds all matching cookies to the provided set.
     * @param cookies the cookies to select from
     * @param normalizedUrl the url to match against
     * @param browserVersion the {@link BrowserVersion}
     * @param matches the set to add
     */
    public static void addMatching(final Set<org.htmlunit.util.Cookie> cookies,
            final URL normalizedUrl, final BrowserVersion browserVersion,
            final Set<org.htmlunit.util.Cookie> matches) {
        if (!cookies.isEmpty()) {
            final CookieOrigin cookieOrigin = HttpClientConverter.buildCookieOrigin(normalizedUrl);
            final CookieSpec cookieSpec = new HtmlUnitBrowserCompatCookieSpec(browserVersion);
            for (final org.htmlunit.util.Cookie cookie : cookies) {
                if (cookieSpec.match(cookie.toHttpClient(), cookieOrigin)) {
                    matches.add(cookie);
                }
            }
        }
    }
}
