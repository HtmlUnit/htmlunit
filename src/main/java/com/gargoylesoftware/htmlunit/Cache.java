/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * <p>Simple cache implementation which caches compiled JavaScript files and parsed CSS snippets. Caching
 * compiled JavaScript files avoids unnecessary web requests and additional compilation overhead, while
 * caching parsed CSS snippets avoids very expensive CSS parsing.</p>
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
public class Cache implements Serializable {

    /** The maximum size of the cache. */
    private int maxSize_ = 40;

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
        private final Object value_;
        private long lastAccess_;

        Entry(final String key, final Object value) {
            key_ = key;
            value_ = value;
            lastAccess_ = System.currentTimeMillis();
        }

        /**
         * {@inheritDoc}
         */
        public int compareTo(final Entry other) {
            return NumberUtils.compare(lastAccess_, other.lastAccess_);
        }

        /**
         * Updates the last access date.
         */
        public void touch() {
            lastAccess_ = System.currentTimeMillis();
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
     */
    public void cacheIfPossible(final WebRequest request, final WebResponse response, final Object toCache) {
        if (isCacheable(request, response)) {
            final String url = response.getWebRequest().getUrl().toString();
            final Entry entry = new Entry(url, toCache);
            entries_.put(entry.key_, entry);
            deleteOverflow();
        }
    }

    /**
     * Caches the parsed version of the specified CSS snippet. We key the cache based on CSS snippets (rather
     * than requests and responses as is done above) because a) this allows us to cache inline CSS, b) CSS is
     * extremely expensive to parse, so we want to avoid it as much as possible, c) CSS files aren't usually
     * nearly as large as JavaScript files, so memory bloat won't be too bad, and d) caching on requests and
     * responses requires checking dynamicity (see {@link #isDynamicContent(WebResponse)}), and headers often
     * aren't set up correctly, disallowing caching when in fact it should be allowed.
     *
     * @param css the CSS snippet from which <tt>styleSheet</tt> is derived
     * @param styleSheet the parsed version of <tt>css</tt>
     */
    public void cache(final String css, final CSSStyleSheet styleSheet) {
        final Entry entry = new Entry(css, styleSheet);
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
            }
        }
    }

    /**
     * Determines if the specified response can be cached.
     *
     * @param request the performed request
     * @param response the received response
     * @return <code>true</code> if the response can be cached
     */
    protected boolean isCacheable(final WebRequest request, final  WebResponse response) {
        return HttpMethod.GET == response.getWebRequest().getHttpMethod()
            && !isDynamicContent(response);
    }

    /**
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
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html">RFC 2616</a>
     * @param response the response to examine
     * @return <code>true</code> if the response should be considered as dynamic and therefore uncacheable
     */
    protected boolean isDynamicContent(final WebResponse response) {
        final Date lastModified = parseDateHeader(response, "Last-Modified");
        final Date expires = parseDateHeader(response, "Expires");

        final long delay = 10 * org.apache.commons.lang.time.DateUtils.MILLIS_PER_MINUTE;
        final long now = getCurrentTimestamp();

        final boolean cacheableContent = (expires != null && (expires.getTime() - now > delay)
                || (expires == null && lastModified != null && (now - lastModified.getTime() > delay)));

        return !cacheableContent;
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
     * returns <tt>null</tt> if the specified header cannot be found or cannot be parsed as
     * a date.
     *
     * @param response the response
     * @param headerName the header name
     * @return the specified date header of the specified response
     */
    protected Date parseDateHeader(final WebResponse response, final String headerName) {
        final String value = response.getResponseHeaderValue(headerName);
        if (value == null) {
            return null;
        }
        else if (value.matches("-?\\d+")) {
            return new Date();
        }
        try {
            return DateUtils.parseDate(value);
        }
        catch (final DateParseException e) {
            return null;
        }
    }

    /**
     * Returns the cached object corresponding to the specified request. If there is
     * no corresponding cached object, this method returns <tt>null</tt>.
     *
     * @param request the request whose corresponding cached compiled script is sought
     * @return the cached object corresponding to the specified request if any
     */
    public Object getCachedObject(final WebRequest request) {
        if (HttpMethod.GET != request.getHttpMethod()) {
            return null;
        }
        final Entry cachedEntry = entries_.get(request.getUrl().toString());
        if (cachedEntry == null) {
            return null;
        }
        synchronized (entries_) {
            cachedEntry.touch();
        }
        return cachedEntry.value_;
    }

    /**
     * Returns the cached parsed version of the specified CSS snippet. If there is no
     * corresponding cached stylesheet, this method returns <tt>null</tt>.
     *
     * @param css the CSS snippet whose cached stylesheet is sought
     * @return the cached stylesheet corresponding to the specified CSS snippet
     */
    public CSSStyleSheet getCachedStyleSheet(final String css) {
        final Entry cachedEntry = entries_.get(css);
        if (cachedEntry == null) {
            return null;
        }
        synchronized (entries_) {
            cachedEntry.touch();
        }
        return (CSSStyleSheet) cachedEntry.value_;
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
            entries_.clear();
        }
    }

}
