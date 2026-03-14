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
package org.htmlunit.javascript.host.html;

import java.net.MalformedURLException;
import java.net.URL;

import org.htmlunit.util.StringUtils;
import org.htmlunit.util.UrlUtils;

/**
 * Implementation of the {@code HTMLHyperlinkElementUtils} mixin.
 * Provides URL decomposition property logic
 * @see <a href="https://html.spec.whatwg.org/multipage/links.html#htmlhyperlinkelementutils">
 *      HTMLHyperlinkElementUtils</a>
 */
final class HTMLHyperlinkElementUtils {

    /**
     * Returns the {@code search} component of the URL.
     * @param url the URL
     * @return the query string prefixed with {@code ?}, or empty string if no query
     */
    static String getSearch(final URL url) {
        final String query = url.getQuery();
        if (query == null) {
            return "";
        }
        return "?" + query;
    }

    /**
     * Returns a new URL with the {@code search} component set.
     * @param url the current URL
     * @param search the new search value (with or without leading {@code ?})
     * @return the new URL
     * @throws MalformedURLException if an error occurs
     */
    static URL setSearch(final URL url, final String search) throws MalformedURLException {
        final String query;
        if (search == null
                || StringUtils.isEmptyString(search)
                || StringUtils.equalsChar('?', search)) {
            query = null;
        }
        else if (search.charAt(0) == '?') {
            query = search.substring(1);
        }
        else {
            query = search;
        }
        return UrlUtils.getUrlWithNewQuery(url, query);
    }

    /**
     * Returns the {@code hash} component of the URL.
     * @param url the URL
     * @return the fragment prefixed with {@code #}, or empty string if no fragment
     */
    static String getHash(final URL url) {
        final String hash = url.getRef();
        if (hash == null) {
            return "";
        }
        return "#" + hash;
    }

    /**
     * Returns a new URL with the {@code hash} component set.
     * @param url the current URL
     * @param hash the new hash value
     * @return the new URL
     * @throws MalformedURLException if an error occurs
     */
    static URL setHash(final URL url, final String hash) throws MalformedURLException {
        return UrlUtils.getUrlWithNewRef(url, hash);
    }

    /**
     * Returns the {@code hostname} component of the URL.
     * @param url the URL
     * @return the hostname
     */
    static String getHostname(final URL url) {
        return UrlUtils.encodeAnchor(url.getHost());
    }

    /**
     * Returns a new URL with the {@code host} component set.
     * Parses the host string for an optional {@code :port} suffix.
     * @param url the current URL
     * @param host the new host value (e.g. {@code "example.com:8080"})
     * @return the new URL
     * @throws MalformedURLException if an error occurs
     */
    static URL setHost(final URL url, final String host) throws MalformedURLException {
        final String hostname;
        final int port;
        final int index = host.indexOf(':');
        if (index != -1) {
            hostname = host.substring(0, index);
            port = Integer.parseInt(host.substring(index + 1));
        }
        else {
            hostname = host;
            port = -1;
        }
        return UrlUtils.getUrlWithNewHostAndPort(url, hostname, port);
    }

    /**
     * Returns a new URL with the {@code protocol} set, or {@code null}
     * if the protocol is invalid or should not be applied.
     * @param url the current URL
     * @param protocol the new protocol value (with or without trailing {@code :})
     * @return the new URL, or {@code null} if the protocol is invalid
     */
    static URL setProtocol(final URL url, final String protocol) {
        if (protocol.isEmpty()) {
            return null;
        }

        final String bareProtocol = StringUtils.substringBefore(protocol, ":").trim();
        if (!UrlUtils.isValidScheme(bareProtocol)) {
            return null;
        }
        if (!UrlUtils.isSpecialScheme(bareProtocol)) {
            return null;
        }

        try {
            URL result = UrlUtils.getUrlWithNewProtocol(url, bareProtocol);
            result = UrlUtils.removeRedundantPort(result);
            return result;
        }
        catch (final MalformedURLException ignored) {
            return null;
        }
    }

    /**
     * Returns a new URL with the {@code pathname} set.
     * @param url the current URL
     * @param pathname the new pathname value
     * @return the new URL
     * @throws MalformedURLException if an error occurs
     */
    static URL setPathname(final URL url, final String pathname) throws MalformedURLException {
        return UrlUtils.getUrlWithNewPath(url, pathname);
    }

    /**
     * Returns the {@code username} component of the URL.
     * @param url the URL
     * @return the username, or empty string if no user info
     */
    static String getUsername(final URL url) {
        final String userInfo = url.getUserInfo();
        if (userInfo == null) {
            return "";
        }
        return StringUtils.substringBefore(userInfo, ":");
    }

    /**
     * Returns the {@code password} component of the URL.
     * @param url the URL
     * @return the password, or empty string if no user info
     */
    static String getPassword(final URL url) {
        final String userInfo = url.getUserInfo();
        if (userInfo == null) {
            return "";
        }
        return StringUtils.substringAfter(userInfo, ":");
    }

    /**
     * Checks whether the given port is the default port for the protocol.
     * @param protocol the protocol (e.g. {@code "http"}, {@code "https"})
     * @param port the port number
     * @return {@code true} if the port is the default for the protocol
     */
    static boolean isDefaultPort(final String protocol, final int port) {
        return ("http".equals(protocol) && port == 80)
                || ("https".equals(protocol) && port == 443);
    }
}
