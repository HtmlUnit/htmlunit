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
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.utils.DateUtils;

import com.gargoylesoftware.css.dom.CSSStyleSheetImpl;
import com.gargoylesoftware.htmlunit.util.HeaderUtils;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * <p>Simple cache implementation which caches compiled JavaScript files and parsed CSS snippets. Caching
 * compiled JavaScript files avoids unnecessary web requests and additional compilation overhead, while
 * caching parsed CSS snippets avoids very expensive CSS parsing.</p>
 *
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Anton Demydenko
 * @author Ronald Brill
 */
public class Cache implements Serializable {

    /** The maximum size of the cache. */
    private int maxSize_ = 40;

    private static final Pattern DATE_HEADER_PATTERN = Pattern.compile("-?\\d+");
    static final long DELAY = 10 * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_MINUTE;

    /**
     * The map which holds the cached responses. Note that when keying on URLs, we key on the string version
     * of the URLs, rather than on the URLs themselves. This is done for performance, because a) the
     * {@link java.net.URL#hashCode()} method is synchronized, and b) the {@link java.net.URL#hashCode()}
     * method triggers DNS lookups of the URL hostnames' IPs. As of this writing, the HtmlUnit unit tests
     * run ~20% faster whey keying on strings rather than on {@link java.net.URL} instances.
     */
    private final Map<String, Entry> entries_ = Collections.synchronizedMap(new HashMap<String, Entry>(maxSize_));

    /**
     * A cache entry.
     */
    private static class Entry implements Comparable<Entry>, Serializable {
        private final String key_;
        private final WebResponse response_;
        private final Object value_;
        private long lastAccess_;
        private final long createdAt_;

        Entry(final String key, final WebResponse response, final Object value) {
            key_ = key;
            response_ = response;
            value_ = value;
            createdAt_ = System.currentTimeMillis();
            lastAccess_ = createdAt_;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(final Entry other) {
            if (lastAccess_ < other.lastAccess_) {
                return -1;
            }
            if (lastAccess_ == other.lastAccess_) {
                return 0;
            }
            return 1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object obj) {
            return obj instanceof Entry && lastAccess_ == ((Entry) obj).lastAccess_;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return ((Long) lastAccess_).hashCode();
        }

        /**
         * Updates the last access date.
         */
        public void touch() {
            lastAccess_ = System.currentTimeMillis();
        }

        /**
         * <p>Check freshness return value if
         * a) s-maxage specified
         * b) max-age specified
         * c) expired specified
         * otherwise return {@code null}</p>
         *
         * @see <a href="https://tools.ietf.org/html/rfc7234">RFC 7234</a>
         *
         * @param response
         * @param createdAt
         * @return freshnessLifetime
         */
        boolean isStillFresh(final long now) {
            long freshnessLifetime = 0;
            if (!HeaderUtils.containsPrivate(response_) && HeaderUtils.containsSMaxage(response_)) {
                // check s-maxage
                freshnessLifetime = HeaderUtils.sMaxage(response_);
            }
            else if (HeaderUtils.containsMaxAge(response_)) {
                // check max-age
                freshnessLifetime = HeaderUtils.maxAge(response_);
            }
            else if (response_.getResponseHeaderValue(HttpHeader.EXPIRES) == null) {
                return true;
            }
            else {
                final Date expires = parseDateHeader(response_, HttpHeader.EXPIRES);
                if (expires != null) {
                    // use the same logic as in isCacheableContent()
                    return expires.getTime() - now > DELAY;
                }
            }
            return now - createdAt_ < freshnessLifetime * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_SECOND;
        }
    }

    /**
     * Caches the specified object, if the corresponding request and response objects indicate
     * that it is cacheable.
     *
     * @param request the request corresponding to the specified compiled script
     * @param response the response corresponding to the specified compiled script
     * @param toCache the object that is to be cached, if possible (may be for instance a compiled script or
     * simply a WebResponse)
     * @return whether the response was cached or not
     */
    public boolean cacheIfPossible(final WebRequest request, final WebResponse response, final Object toCache) {
        if (isCacheable(request, response)) {
            final URL url = request.getUrl();
            if (url == null) {
                return false;
            }

            final Entry entry = new Entry(UrlUtils.normalize(url), response, toCache);
            entries_.put(entry.key_, entry);
            deleteOverflow();
            return true;
        }

        return false;
    }

    /**
     * Caches the parsed version of the specified CSS snippet. We key the cache based on CSS snippets (rather
     * than requests and responses as is done above) because a) this allows us to cache inline CSS, b) CSS is
     * extremely expensive to parse, so we want to avoid it as much as possible, c) CSS files aren't usually
     * nearly as large as JavaScript files, so memory bloat won't be too bad, and d) caching on requests and
     * responses requires checking dynamically (see {@link #isCacheableContent(WebResponse)}), and headers often
     * aren't set up correctly, disallowing caching when in fact it should be allowed.
     *
     * @param css the CSS snippet from which <tt>styleSheet</tt> is derived
     * @param styleSheet the parsed version of <tt>css</tt>
     */
    public void cache(final String css, final CSSStyleSheetImpl styleSheet) {
        final Entry entry = new Entry(css, null, styleSheet);
        entries_.put(entry.key_, entry);
        deleteOverflow();
    }

    /**
     * Truncates the cache to the maximal number of entries.
     */
    protected void deleteOverflow() {
        synchronized (entries_) {
            while (entries_.size() > maxSize_) {
                final Entry oldestEntry = Collections.min(entries_.values());
                entries_.remove(oldestEntry.key_);
                if (oldestEntry.response_ != null) {
                    oldestEntry.response_.cleanUp();
                }
            }
        }
    }

    /**
     * Determines if the specified response can be cached.
     *
     * @param request the performed request
     * @param response the received response
     * @return {@code true} if the response can be cached
     */
    protected boolean isCacheable(final WebRequest request, final WebResponse response) {
        return HttpMethod.GET == response.getWebRequest().getHttpMethod()
            && UrlUtils.URL_ABOUT_BLANK != request.getUrl()
            && isCacheableContent(response);
    }

    /**
     * <p>Perform prior validation for 'no-store' directive in Cache-Control header.</p>
     *
     * <p>Tries to guess if the content is dynamic or not.</p>
     *
     * <p>"Since origin servers do not always provide explicit expiration times, HTTP caches typically
     * assign heuristic expiration times, employing algorithms that use other header values (such as the
     * <tt>Last-Modified</tt> time) to estimate a plausible expiration time".</p>
     *
     * <p>The current implementation considers as dynamic content everything except responses with a
     * <tt>Last-Modified</tt> header with a date older than 10 minutes or with an <tt>Expires</tt> header
     * specifying expiration in more than 10 minutes.</p>
     *
     * @see <a href="https://tools.ietf.org/html/rfc7234">RFC 7234</a>
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html">RFC 2616</a>
     * @param response the response to examine
     * @return {@code true} if the response should be considered as cacheable
     */
    protected boolean isCacheableContent(final WebResponse response) {
        if (HeaderUtils.containsNoStore(response)) {
            return false;
        }

        final Date lastModified = parseDateHeader(response, HttpHeader.LAST_MODIFIED);

        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Expires
        // If there is a Cache-Control header with the max-age or s-maxage directive
        // in the response, the Expires header is ignored.
        Date expires = null;
        if (!HeaderUtils.containsMaxAgeOrSMaxage(response)) {
            expires = parseDateHeader(response, HttpHeader.EXPIRES);
        }

        final long now = getCurrentTimestamp();

        return expires != null && (expires.getTime() - now > DELAY)
                || (expires == null && lastModified != null && now - lastModified.getTime() > DELAY);
    }

    /**
     * Gets the current time stamp. As method to allow overriding it, when simulating an other time.
     * @return the current time stamp
     */
    protected long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * Parses and returns the specified date header of the specified response. This method
     * returns {@code null} if the specified header cannot be found or cannot be parsed as a date.
     *
     * @param response the response
     * @param headerName the header name
     * @return the specified date header of the specified response
     */
    protected static Date parseDateHeader(final WebResponse response, final String headerName) {
        final String value = response.getResponseHeaderValue(headerName);
        if (value == null) {
            return null;
        }
        final Matcher matcher = DATE_HEADER_PATTERN.matcher(value);
        if (matcher.matches()) {
            return new Date();
        }
        return DateUtils.parseDate(value);
    }

    /**
     * Returns the cached response corresponding to the specified request. If there is
     * no corresponding cached object, this method returns {@code null}.
     *
     * <p>Calculates and check if object still fresh(RFC 7234) otherwise returns {@code null}.</p>
     * @see <a href="https://tools.ietf.org/html/rfc7234">RFC 7234</a>
     *
     * @param request the request whose corresponding response is sought
     * @return the cached response corresponding to the specified request if any
     */
    public WebResponse getCachedResponse(final WebRequest request) {
        final Entry cachedEntry = getCacheEntry(request);
        if (cachedEntry == null) {
            return null;
        }
        return cachedEntry.response_;
    }

    /**
     * Returns the cached object corresponding to the specified request. If there is
     * no corresponding cached object, this method returns {@code null}.
     *
     * <p>Calculates and check if object still fresh(RFC 7234) otherwise returns {@code null}.</p>
     * @see <a href="https://tools.ietf.org/html/rfc7234">RFC 7234</a>
     *
     * @param request the request whose corresponding cached compiled script is sought
     * @return the cached object corresponding to the specified request if any
     */
    public Object getCachedObject(final WebRequest request) {
        final Entry cachedEntry = getCacheEntry(request);
        if (cachedEntry == null) {
            return null;
        }
        return cachedEntry.value_;
    }

    private Entry getCacheEntry(final WebRequest request) {
        if (HttpMethod.GET != request.getHttpMethod()) {
            return null;
        }

        final URL url = request.getUrl();
        if (url == null) {
            return null;
        }

        final String normalizedUrl = UrlUtils.normalize(url);
        final Entry cachedEntry = entries_.get(normalizedUrl);
        if (cachedEntry == null) {
            return null;
        }

        if (cachedEntry.isStillFresh(getCurrentTimestamp())) {
            synchronized (entries_) {
                cachedEntry.touch();
            }
            return cachedEntry;
        }
        entries_.remove(UrlUtils.normalize(url));
        return null;
    }

    /**
     * Returns the cached parsed version of the specified CSS snippet. If there is no
     * corresponding cached stylesheet, this method returns {@code null}.
     *
     * @param css the CSS snippet whose cached stylesheet is sought
     * @return the cached stylesheet corresponding to the specified CSS snippet
     */
    public CSSStyleSheetImpl getCachedStyleSheet(final String css) {
        final Entry cachedEntry = entries_.get(css);
        if (cachedEntry == null) {
            return null;
        }
        synchronized (entries_) {
            cachedEntry.touch();
        }
        return (CSSStyleSheetImpl) cachedEntry.value_;
    }

    /**
     * Returns the cache's maximum size. This is the maximum number of files that will
     * be cached. The default is <tt>25</tt>.
     *
     * @return the cache's maximum size
     */
    public int getMaxSize() {
        return maxSize_;
    }

    /**
     * Sets the cache's maximum size. This is the maximum number of files that will
     * be cached. The default is <tt>25</tt>.
     *
     * @param maxSize the cache's maximum size (must be &gt;= 0)
     */
    public void setMaxSize(final int maxSize) {
        if (maxSize < 0) {
            throw new IllegalArgumentException("Illegal value for maxSize: " + maxSize);
        }
        maxSize_ = maxSize;
        deleteOverflow();
    }

    /**
     * Returns the number of entries in the cache.
     *
     * @return the number of entries in the cache
     */
    public int getSize() {
        return entries_.size();
    }

    /**
     * Clears the cache.
     */
    public void clear() {
        synchronized (entries_) {
            for (final Entry entry : entries_.values()) {
                if (entry.response_ != null) {
                    entry.response_.cleanUp();
                }
            }
            entries_.clear();
        }
    }

    /**
     * Removes outdated entries from the cache.
     */
    public void clearOutdated() {
        synchronized (entries_) {
            final long now = getCurrentTimestamp();

            final Iterator<Map.Entry<String, Entry>> iter = entries_.entrySet().iterator();
            while (iter.hasNext()) {
                final Map.Entry<String, Entry> entry = iter.next();
                if (entry.getValue().response_ == null
                        || !entry.getValue().isStillFresh(now)) {
                    iter.remove();
                }
            }
        }
    }
}
