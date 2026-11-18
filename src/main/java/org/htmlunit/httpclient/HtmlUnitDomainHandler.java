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

import java.util.Locale;

import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicDomainHandler;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;
import org.htmlunit.HttpHeader;

/**
 * Customized BasicDomainHandler for HtmlUnit.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author Vidya m
 */
final class HtmlUnitDomainHandler extends BasicDomainHandler {
    private static final PublicSuffixMatcher PUBLIC_SUFFIX_MATCHER = PublicSuffixMatcherLoader.getDefault();

    HtmlUnitDomainHandler() {
        super();
    }

    @Override
    public void parse(final SetCookie cookie, final String value)
            throws MalformedCookieException {
        Args.notNull(cookie, HttpHeader.COOKIE);
        if (TextUtils.isBlank(value)) {
            throw new MalformedCookieException("Blank or null value for domain attribute");
        }
        // Ignore domain attributes ending with '.' per RFC 6265, 4.1.2.3
        if (value.endsWith(".")) {
            return;
        }
        String domain = value.toLowerCase(Locale.ROOT);

        final int dotIndex = domain.indexOf('.');
        if (dotIndex == 0 && domain.length() > 1 && domain.indexOf('.', 1) == -1) {
            // like the browsers, a single label domain is stored without the leading dot
            // ('.intranet' is handled as 'intranet')
            domain = domain.substring(1);
        }
        if (dotIndex > 0) {
            domain = '.' + domain;
        }

        cookie.setDomain(domain);
    }

    /**
     * {@inheritDoc}
     * Like the browsers, cookies for a public suffix (e.g. 'co.uk')
     * are rejected unless the domain is the host itself (RFC 6265 section 5.3, step 5).
     */
    @Override
    public void validate(final Cookie cookie, final CookieOrigin origin) throws MalformedCookieException {
        // like the browsers, a domain attribute with the ip address of the host is accepted;
        // the cookie is a host-only cookie (no other host can match an ip address)
        final String host = origin.getHost();
        if (isIpAddress(host) && cookie instanceof BasicClientCookie clientCookie
                && clientCookie.containsAttribute(ClientCookie.DOMAIN_ATTR)) {
            final String cookieDomain = cookie.getDomain();
            if (cookieDomain != null
                    && host.equalsIgnoreCase(cookieDomain.startsWith(".") ? cookieDomain.substring(1) : cookieDomain)) {
                clientCookie.setDomain(host);
                clientCookie.removeAttribute(ClientCookie.DOMAIN_ATTR);
            }
        }

        super.validate(cookie, origin);

        final String domain = cookie.getDomain();
        final String normalizedDomain = domain.startsWith(".") ? domain.substring(1) : domain;
        // top level domains (e.g. 'com') are public suffixes also; single label names
        // not on the list (e.g. 'localhost', 'intranet') are no public suffixes
        if (!normalizedDomain.equalsIgnoreCase(origin.getHost())
                && PUBLIC_SUFFIX_MATCHER.matches(normalizedDomain)) {
            throw new CookieRestrictionViolationException("Illegal 'domain' attribute \"" + domain
                    + "\"; cookies for a public suffix are not allowed");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        final String domain = cookie.getDomain();
        if (domain == null) {
            return false;
        }

        final int dotIndex = domain.indexOf('.');
        if (dotIndex == 0 && domain.length() > 1 && domain.indexOf('.', 1) == -1) {
            // single label domain with leading dot (e.g. '.intranet', only possible for cookies
            // added by the api); like the browsers the cookie matches the host itself
            return origin.getHost().equalsIgnoreCase(domain.substring(1));
        }

        // cookies for a public suffix (e.g. 'com') are rejected by validate() already; this check
        // is for cookies added by the api. Single label names that are no public suffix
        // (e.g. 'localhost', 'intranet') match like any other domain - without name resolution.
        final String normalizedDomain = dotIndex == 0 ? domain.substring(1) : domain;
        if (!normalizedDomain.equalsIgnoreCase(origin.getHost())
                && !HtmlUnitBrowserCompatCookieSpec.LOCAL_FILESYSTEM_DOMAIN.equalsIgnoreCase(normalizedDomain)
                && PUBLIC_SUFFIX_MATCHER.matches(normalizedDomain)) {
            return false;
        }

        return super.match(cookie, origin);
    }

    private static boolean isIpAddress(final String host) {
        String h = host;
        if (h.startsWith("[") && h.endsWith("]")) {
            h = h.substring(1, h.length() - 1);
        }
        return InetAddressUtils.isIPv4Address(h) || InetAddressUtils.isIPv6Address(h);
    }
}
