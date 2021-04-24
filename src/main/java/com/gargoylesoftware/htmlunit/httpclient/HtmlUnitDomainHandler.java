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
package com.gargoylesoftware.htmlunit.httpclient;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTTP_COOKIE_REMOVE_DOT_FROM_ROOT_DOMAINS;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.cookie.BasicDomainHandler;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpHeader;

/**
 * Customized BasicDomainHandler for HtmlUnit.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
final class HtmlUnitDomainHandler extends BasicDomainHandler {
    private final BrowserVersion browserVersion_;

    HtmlUnitDomainHandler(final BrowserVersion browserVersion) {
        browserVersion_ = browserVersion;
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
        String domain = value;
        domain = domain.toLowerCase(Locale.ROOT);

        final int dotIndex = domain.indexOf('.');
        if (browserVersion_.hasFeature(HTTP_COOKIE_REMOVE_DOT_FROM_ROOT_DOMAINS)
                && dotIndex == 0 && domain.length() > 1 && domain.indexOf('.', 1) == -1) {
            domain = domain.toLowerCase(Locale.ROOT);
            domain = domain.substring(1);
        }
        if (dotIndex > 0) {
            domain = '.' + domain;
        }

        cookie.setDomain(domain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(final Cookie cookie, final CookieOrigin origin) {
        String domain = cookie.getDomain();
        if (domain == null) {
            return false;
        }

        final int dotIndex = domain.indexOf('.');
        if (dotIndex == 0 && domain.length() > 1 && domain.indexOf('.', 1) == -1) {
            final String host = origin.getHost();
            domain = domain.toLowerCase(Locale.ROOT);
            if (browserVersion_.hasFeature(HTTP_COOKIE_REMOVE_DOT_FROM_ROOT_DOMAINS)) {
                domain = domain.substring(1);
            }
            return host.equals(domain);
        }

        if (dotIndex == -1
                && !HtmlUnitBrowserCompatCookieSpec.LOCAL_FILESYSTEM_DOMAIN.equalsIgnoreCase(domain)) {
            try {
                InetAddress.getByName(domain);
            }
            catch (final UnknownHostException e) {
                return false;
            }
        }

        return super.match(cookie, origin);
    }
}
