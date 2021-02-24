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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.CookieOrigin;

import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitBrowserCompatCookieSpec;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Manages cookies for a {@link WebClient}. This class is thread-safe.
 * You can disable Cookies by calling setCookiesEnabled(false). The
 * CookieManager itself takes care of this and ignores all cookie request if
 * disabled. If you override this your methods have to do the same.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Nicolas Belisle
 * @author Ronald Brill
 */
public class CookieManager implements Serializable {

    /** Whether or not cookies are enabled. */
    private boolean cookiesEnabled_;

    /** The cookies added to this cookie manager. */
    private final Set<Cookie> cookies_ = new LinkedHashSet<>();

    /**
     * Creates a new instance.
     */
    public CookieManager() {
        cookiesEnabled_ = true;
    }

    /**
     * Enables/disables cookie support. Cookies are enabled by default.
     * @param enabled {@code true} to enable cookie support, {@code false} otherwise
     */
    public synchronized void setCookiesEnabled(final boolean enabled) {
        cookiesEnabled_ = enabled;
    }

    /**
     * Returns {@code true} if cookies are enabled. Cookies are enabled by default.
     * @return {@code true} if cookies are enabled, {@code false} otherwise
     */
    public synchronized boolean isCookiesEnabled() {
        return cookiesEnabled_;
    }

    /**
     * Returns the currently configured cookies, in an unmodifiable set.
     * If disabled, this returns an empty set.
     * @return the currently configured cookies, in an unmodifiable set
     */
    public synchronized Set<Cookie> getCookies() {
        if (!isCookiesEnabled()) {
            return Collections.<Cookie>emptySet();
        }

        final Set<Cookie> copy = new LinkedHashSet<>();
        copy.addAll(cookies_);
        return Collections.unmodifiableSet(copy);
    }

    /**
     * Helper that builds a CookieOrigin.
     * @param url the url to be used
     * @return the new CookieOrigin
     */
    public CookieOrigin buildCookieOrigin(final URL url) {
        final URL normalizedUrl = replaceForCookieIfNecessary(url);

        return new CookieOrigin(
                normalizedUrl.getHost(),
                getPort(normalizedUrl),
                normalizedUrl.getPath(),
                "https".equals(normalizedUrl.getProtocol()));
    }

    /**
     * Clears all cookies that have expired before supplied date.
     * If disabled, this returns false.
     * @param date the date to use for comparison when clearing expired cookies
     * @return whether any cookies were found expired, and were cleared
     */
    public synchronized boolean clearExpired(final Date date) {
        if (!isCookiesEnabled()) {
            return false;
        }

        if (date == null) {
            return false;
        }

        boolean foundExpired = false;
        for (final Iterator<Cookie> iter = cookies_.iterator(); iter.hasNext();) {
            final Cookie cookie = iter.next();
            if (cookie.getExpires() != null && date.after(cookie.getExpires())) {
                iter.remove();
                foundExpired = true;
            }
        }
        return foundExpired;
    }

    /**
     * Gets the port of the URL.
     * This functionality is implemented here as protected method to allow subclass to change it
     * as workaround to <a href="http://code.google.com/p/googleappengine/issues/detail?id=4784">
     * Google App Engine bug 4784</a>.
     * @param url the URL
     * @return the port use to connect the server
     */
    protected int getPort(final URL url) {
        if (url.getPort() != -1) {
            return url.getPort();
        }
        return url.getDefaultPort();
    }

    /**
     * {@link CookieOrigin} doesn't like empty hosts and negative ports,
     * but these things happen if we're dealing with a local file.
     * This method allows us to work around this limitation in HttpClient by feeding it a bogus host and port.
     *
     * @param url the URL to replace if necessary
     * @return the replacement URL, or the original URL if no replacement was necessary
     */
    public URL replaceForCookieIfNecessary(URL url) {
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
     * Returns the currently configured cookie with the specified name, or {@code null} if one does not exist.
     * If disabled, this returns null.
     * @param name the name of the cookie to return
     * @return the currently configured cookie with the specified name, or {@code null} if one does not exist
     */
    public synchronized Cookie getCookie(final String name) {
        if (!isCookiesEnabled()) {
            return null;
        }

        for (final Cookie cookie : cookies_) {
            if (StringUtils.equals(cookie.getName(), name)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * Adds the specified cookie.
     * If disabled, this does nothing.
     * @param cookie the cookie to add
     */
    public synchronized void addCookie(final Cookie cookie) {
        if (!isCookiesEnabled()) {
            return;
        }

        cookies_.remove(cookie);

        // don't add expired cookie
        if (cookie.getExpires() == null || cookie.getExpires().after(new Date())) {
            cookies_.add(cookie);
        }
    }

    /**
     * Removes the specified cookie.
     * If disabled, this does nothing.
     * @param cookie the cookie to remove
     */
    public synchronized void removeCookie(final Cookie cookie) {
        if (!isCookiesEnabled()) {
            return;
        }

        cookies_.remove(cookie);
    }

    /**
     * Removes all cookies.
     * If disabled, this does nothing.
     */
    public synchronized void clearCookies() {
        if (!isCookiesEnabled()) {
            return;
        }

        cookies_.clear();
    }
}
