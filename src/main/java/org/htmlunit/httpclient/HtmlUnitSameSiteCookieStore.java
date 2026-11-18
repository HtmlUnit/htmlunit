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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * A {@link CookieStore} used for one request; returns only the cookies that are allowed
 * by their SameSite attribute (all other operations are delegated).
 * <p>Like the browsers, for a cross-site request</p>
 * <ul>
 *   <li>'None' cookies are always sent (they are secure, see {@link HtmlUnitSameSiteHandler})</li>
 *   <li>'Strict' cookies are never sent</li>
 *   <li>'Lax' cookies are only sent for top-level navigations with a safe method (e.g. GET)</li>
 *   <li>cookies without (or with an invalid) SameSite attribute are handled as 'Lax'; additionally
 *       they are sent for top-level navigations with an unsafe method (e.g. POST) within two minutes
 *       after they were created (Lax-allowing-unsafe)</li>
 * </ul>
 *
 * @author Ronald Brill
 * @see <a href="https://datatracker.ietf.org/doc/html/draft-ietf-httpbis-rfc6265bis#name-samesite-cookies">
 *     RFC 6265bis - SameSite Cookies</a>
 */
public final class HtmlUnitSameSiteCookieStore implements CookieStore {

    /** Cookies without SameSite attribute are sent for unsafe top-level navigations for this time. */
    private static final long LAX_ALLOWING_UNSAFE_MILLIS = 2 * 60 * 1000;

    private final CookieStore delegate_;
    private final boolean topLevelNavigation_;
    private final boolean safeMethod_;

    /**
     * Ctor.
     *
     * @param delegate the cookie store providing all cookies
     * @param topLevelNavigation whether the (cross-site) request is a top-level navigation
     * @param safeMethod whether the request uses a safe method (GET, HEAD, OPTIONS, TRACE)
     */
    public HtmlUnitSameSiteCookieStore(final CookieStore delegate,
            final boolean topLevelNavigation, final boolean safeMethod) {
        delegate_ = delegate;
        topLevelNavigation_ = topLevelNavigation;
        safeMethod_ = safeMethod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCookie(final Cookie cookie) {
        delegate_.addCookie(cookie);
    }

    /**
     * Returns the cookies that are allowed to be sent with this cross-site request.
     * {@inheritDoc}
     */
    @Override
    public List<Cookie> getCookies() {
        final long now = System.currentTimeMillis();
        final List<Cookie> result = new ArrayList<>();
        for (final Cookie cookie : delegate_.getCookies()) {
            if (isAllowed(cookie, now)) {
                result.add(cookie);
            }
        }
        return result;
    }

    private boolean isAllowed(final Cookie cookie, final long now) {
        String sameSite = null;
        if (cookie instanceof ClientCookie clientCookie) {
            sameSite = clientCookie.getAttribute(HtmlUnitSameSiteHandler.SAMESITE_ATTR);
        }
        if (sameSite != null) {
            sameSite = sameSite.toLowerCase(Locale.ROOT);
        }

        if ("none".equals(sameSite)) {
            return true;
        }
        if ("strict".equals(sameSite)) {
            return false;
        }
        if (!topLevelNavigation_) {
            return false;
        }
        if (safeMethod_) {
            return true;
        }
        if ("lax".equals(sameSite)) {
            return false;
        }

        // no (or an invalid) SameSite attribute: Lax-allowing-unsafe
        if (cookie instanceof BasicClientCookie basicClientCookie) {
            final Date creationDate = basicClientCookie.getCreationDate();
            return creationDate != null && now - creationDate.getTime() < LAX_ALLOWING_UNSAFE_MILLIS;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clearExpired(final Date date) {
        return delegate_.clearExpired(date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        delegate_.clear();
    }
}
