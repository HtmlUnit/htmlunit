/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.lang.StringUtils;

/**
 * Manages cookies for a {@link WebClient}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class CookieManager implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 4145377365165079425L;

    /**
     * HtmlUnit's cookie policy is to be browser-compatible. Code which requires access to
     * HtmlUnit's cookie policy should use this constant, rather than making assumptions and using
     * one of the HttpClient {@link CookiePolicy} constants directly.
     */
    public static final String HTMLUNIT_COOKIE_POLICY = CookiePolicy.BROWSER_COMPATIBILITY;

    /** Whether or not cookies are enabled. */
    private boolean cookiesEnabled_;

    /** The cookies added to this cookie manager. */
    private final Set<Cookie> cookies_;

    /** The HTTP state last updated in {@link #updateState(HttpState)} (for optimization purposes). */
    private transient WeakReference<HttpState> lastStateUpdated_;

    /**
     * Whether or not the cookie set has been modified since the last {@link #updateState(HttpState)}
     * call (for optimization purposes).
     */
    private transient boolean cookiesModified_;

    /**
     * Creates a new instance.
     */
    public CookieManager() {
        cookiesEnabled_ = true;
        cookies_ = new LinkedHashSet<Cookie>();
        lastStateUpdated_ = null;
        cookiesModified_ = false;
    }

    /**
     * Enables/disables cookie support. Cookies are enabled by default.
     * @param enabled <tt>true</tt> to enable cookie support, <tt>false</tt> otherwise
     */
    public void setCookiesEnabled(final boolean enabled) {
        cookiesEnabled_ = enabled;
        if (!cookiesEnabled_) {
            lastStateUpdated_ = null;
        }
    }

    /**
     * Returns <tt>true</tt> if cookies are enabled. Cookies are enabled by default.
     * @return <tt>true</tt> if cookies are enabled, <tt>false</tt> otherwise
     */
    public boolean isCookiesEnabled() {
        return cookiesEnabled_;
    }

    /**
     * Returns the currently configured cookies, in an unmodifiable set.
     * @return the currently configured cookies, in an unmodifiable set
     */
    public Set<Cookie> getCookies() {
        return Collections.unmodifiableSet(cookies_);
    }

    /**
     * Returns the currently configured cookies for the specified domain, in an unmodifiable set.
     * @param domain the domain on which to filter the returned cookies
     * @return the currently configured cookies for the specified domain, in an unmodifiable set
     */
    public Set<Cookie> getCookies(final String domain) {
        final Set<Cookie> cookies = new LinkedHashSet<Cookie>();
        for (Cookie cookie : cookies_) {
            if (StringUtils.equals(cookie.getDomain(), domain)) {
                cookies.add(cookie);
            }
        }
        return Collections.unmodifiableSet(cookies);
    }

    /**
     * Returns the currently configured cookie with the specified name, or <tt>null</tt> if one does not exist.
     * @param name the name of the cookie to return
     * @return the currently configured cookie with the specified name, or <tt>null</tt> if one does not exist
     */
    public Cookie getCookie(final String name) {
        for (Cookie cookie : cookies_) {
            if (StringUtils.equals(cookie.getName(), name)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * Adds the specified cookie.
     * @param cookie the cookie to add
     */
    public void addCookie(final Cookie cookie) {
        cookies_.add(cookie);
        cookiesModified_ = true;
    }

    /**
     * Removes the specified cookie.
     * @param cookie the cookie to remove
     */
    public void removeCookie(final Cookie cookie) {
        cookies_.remove(cookie);
        cookiesModified_ = true;
    }

    /**
     * Removes all cookies.
     */
    public void clearCookies() {
        cookies_.clear();
        cookiesModified_ = true;
    }

    /**
     * Updates the specified HTTP state's cookie configuration according to the current cookie settings.
     * @param state the HTTP state to update
     * @see #updateFromState(HttpState)
     */
    protected void updateState(final HttpState state) {
        // Optimization: if cookies aren't enabled, we can exit immediately.
        if (!cookiesEnabled_) {
            return;
        }

        // Optimization: if we're being asked to update the same state as we updated last time,
        // and the cookie set hasn't changed in the interim, there's nothing for us to do.
        if (lastStateUpdated_ != null) {
            final HttpState lastStateUpdated = lastStateUpdated_.get();
            if (state == lastStateUpdated && !cookiesModified_) {
                return;
            }
        }

        // The HttpState API doesn't allow us to remove cookies one at a time, so we have to go with a
        // shotgun approach: clear all the old cookies, then add the cookies which need to be used.
        // This is fairly expensive given how often this method will be called, hence the optimizations
        // above.
        state.clearCookies();
        for (Cookie cookie : cookies_) {
            state.addCookie(cookie);
        }

        // Keep track of the last updated HTTP state and cookie modifications, so we can optimize next time through.
        lastStateUpdated_ = new WeakReference<HttpState>(state);
        cookiesModified_ = false;
    }

    /**
     * Updates the current cookie settings from the specified HTTP state's cookie configuration.
     * @param state the HTTP state to update from
     * @see #updateState(HttpState)
     */
    protected void updateFromState(final HttpState state) {
        // Add missing cookies.
        final List<Cookie> stateCookies = Arrays.asList(state.getCookies());
        for (final Cookie stateCookie : stateCookies) {
            if (!cookies_.contains(stateCookie)) {
                cookies_.add(stateCookie);
                cookiesModified_ = true;
            }
        }
        // Remove extraneous cookies.
        for (final Iterator<Cookie> i = cookies_.iterator(); i.hasNext();) {
            final Cookie cookie = i.next();
            if (!stateCookies.contains(cookie)) {
                i.remove();
                cookiesModified_ = true;
            }
        }
    }

    /**
     * Returns whether or not the cookie set has been modified since the last {@link #updateState(HttpState)} call.
     * This method exists for testing purposes only.
     * @return whether or not the cookie set has been modified since the last {@link #updateState(HttpState)} call
     */
    boolean isCookiesModified() {
        return cookiesModified_;
    }

    /**
     * Returns the HTTP state last updated in {@link #updateState(HttpState)}.
     * This method exists for testing purposes only.
     * @return the HTTP state last updated in {@link #updateState(HttpState)}
     */
    HttpState getLastStateUpdated() {
        return lastStateUpdated_.get();
    }

}
