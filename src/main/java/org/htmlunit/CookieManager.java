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
package org.htmlunit;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.htmlunit.http.Cookie;

/**
 * Manages cookies for a {@link WebClient}.
 *
 * <p>This class is thread-safe: all mutator and accessor operations are
 * protected via a {@link ReentrantReadWriteLock}.
 * </p>
 *
 * <p>Cookie support can be turned off via {@link #setCookiesEnabled(boolean)}.
 * While disabled, this manager ignores all cookie operations: additions and
 * removals become no-ops, and accessors behave as if the cookie store were
 * empty. Subclasses that override these methods must preserve this contract.
 * </p>
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Nicolas Belisle
 * @author Ronald Brill
 */
public class CookieManager implements Serializable {

    private final ReentrantReadWriteLock lock_ = new ReentrantReadWriteLock();

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
     * Enables or disables cookie support. Cookies are enabled by default.
     * Disabling does not clear existing cookies; it only suppresses all
     * cookie operations until re-enabled.
     *
     * @param enabled {@code true} to enable cookie support, {@code false} to disable it
     */
    public void setCookiesEnabled(final boolean enabled) {
        lock_.writeLock().lock();
        try {
            cookiesEnabled_ = enabled;
        }
        finally {
            lock_.writeLock().unlock();
        }
    }

    /**
     * Returns {@code true} if cookies are enabled. Cookies are enabled by default.
     *
     * @return {@code true} if cookies are enabled, {@code false} otherwise
     */
    public boolean isCookiesEnabled() {
        lock_.readLock().lock();
        try {
            return cookiesEnabled_;
        }
        finally {
            lock_.readLock().unlock();
        }
    }

    /**
     * Returns a snapshot of the currently configured cookies as an
     * unmodifiable set. The returned set is a copy and will not reflect
     * later changes to this manager's cookie store.
     *
     * @return the currently configured cookies, in an unmodifiable set;
     *         empty if cookie support is disabled
     */
    public Set<Cookie> getCookies() {
        lock_.readLock().lock();
        try {
            if (!cookiesEnabled_) {
                return Collections.emptySet();
            }

            final Set<Cookie> copy = new LinkedHashSet<>(cookies_);
            return Collections.unmodifiableSet(copy);
        }
        finally {
            lock_.readLock().unlock();
        }
    }

    /**
     * Removes all cookies whose expiration date is strictly before the
     * given date. A cookie expiring at exactly {@code date} is not
     * considered expired by this comparison (consistent with RFC 6265's
     * treatment of cookie expiry).
     *
     * @param date the date to compare against; if {@code null}, this method
     *             does nothing and returns {@code false}
     * @return {@code true} if one or more cookies were found expired and removed;
     *         {@code false} otherwise, or if cookie support is disabled
     */
    public boolean clearExpired(final Date date) {
        lock_.writeLock().lock();
        try {
            if (!cookiesEnabled_ || date == null) {
                return false;
            }

            return cookies_.removeIf(cookie ->
                cookie.getExpires() != null && date.after(cookie.getExpires()));
        }
        finally {
            lock_.writeLock().unlock();
        }
    }

    /**
     * Returns the currently configured cookie with the specified name, or
     * {@code null} if none exists. If multiple stored cookies happen to
     * share a name (e.g., differing by domain or path), the first match
     * encountered in iteration order is returned.
     *
     * @param name the cookie name to look up; may be {@code null}
     * @return the matching cookie, or {@code null} if none exists or cookie
     *         support is disabled
     */
    public Cookie getCookie(final String name) {
        lock_.readLock().lock();
        try {
            if (!cookiesEnabled_) {
                return null;
            }

            for (final Cookie cookie : cookies_) {
                if (Objects.equals(cookie.getName(), name)) {
                    return cookie;
                }
            }
            return null;
        }
        finally {
            lock_.readLock().unlock();
        }
    }

    /**
     * Adds the specified cookie, replacing any existing cookie considered
     * equal to it. If the cookie is already expired relative to the current
     * time, it replaces the old entry (if any) but is not itself re-added —
     * this is the mechanism by which a cookie can be deleted by supplying a
     * past expiration date, per RFC 6265.
     *
     * @param cookie the cookie to add
     */
    public void addCookie(final Cookie cookie) {
        lock_.writeLock().lock();
        try {
            if (!cookiesEnabled_) {
                return;
            }

            cookies_.remove(cookie);

            // don't add expired cookie
            if (cookie.getExpires() == null || cookie.getExpires().after(new Date())) {
                cookies_.add(cookie);
            }
        }
        finally {
            lock_.writeLock().unlock();
        }
    }

    /**
     * Removes the specified cookie, if present. Does nothing if cookie support
     * is disabled.
     *
     * @param cookie the cookie to remove; may be {@code null}, in which case
     *               this method does nothing
     */
    public void removeCookie(final Cookie cookie) {
        lock_.writeLock().lock();
        try {
            if (!cookiesEnabled_) {
                return;
            }

            cookies_.remove(cookie);
        }
        finally {
            lock_.writeLock().unlock();
        }
    }

    /**
     * Removes all cookies from this manager. Does nothing if cookie support
     * is disabled.
     */
    public void clearCookies() {
        lock_.writeLock().lock();
        try {
            if (!cookiesEnabled_) {
                return;
            }

            cookies_.clear();
        }
        finally {
            lock_.writeLock().unlock();
        }
    }
}
