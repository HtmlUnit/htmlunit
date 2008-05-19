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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * <p>Simple cache implementation.</p>
 *
 * <p>The current implementation's main purpose is to provide the ability to cache <tt>.js</tt> files.</p>
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
public class Cache implements Serializable {

    private static final long serialVersionUID = -3864114727885057419L;

    /** The maximum size of the cache. */
    private int maxSize_ = 20;

    /**
     * The map which holds the cached responses. Note that we key on the string version of URLs, rather than
     * on the URLs themselves. This is done for performance, because a) the {@link java.net.URL#hashCode()}
     * method is synchronized, and b) the {@link java.net.URL#hashCode()} method triggers DNS lookups of the
     * URL hostnames' IPs. As of this writing, the HtmlUnit unit tests run ~20% faster whey keying on strings
     * rather than on {@link java.net.URL} instances.
     */
    private final Map<String, Entry> entries_ = Collections.synchronizedMap(new HashMap<String, Entry>(maxSize_));

    /**
     * A cache entry.
     */
    private class Entry implements Comparable<Entry>, Serializable {

        private static final long serialVersionUID = 588400350259242484L;
        private final WebResponse response_;
        private long lastAccess_;
        
        Entry(final WebResponse response) {
            response_ = response;
            lastAccess_ = System.currentTimeMillis();
        }
        
        public int compareTo(final Entry other) {
            return NumberUtils.compare(lastAccess_, other.lastAccess_);
        }

        /**
         * Updates the last access date
         */
        public void touch() {
            lastAccess_ = System.currentTimeMillis();
        }
    }

    /**
     * Cache the response if needed. The current implementation only caches JavaScript files.
     *
     * @param request the request
     * @param response the response
     */
    public void cacheIfNeeded(final WebRequestSettings request, final WebResponse response) {
        if (isCacheable(request, response)) {
            entries_.put(response.getUrl().toString(), new Entry(response));
            deleteOverflow();
        }
    }

    /**
     * Truncates the cache to the maximal number of entries.
     */
    protected void deleteOverflow() {
        synchronized (entries_) {
            while (entries_.size() > maxSize_) {
                final Entry oldestEntry = Collections.min(entries_.values());
                entries_.remove(oldestEntry.response_.getUrl().toString());
            }
        }
    }

    /**
     * Determines if the response should be cached.
     *
     * @param request the performed request
     * @param response the received response
     * @return <code>true</code> if the response should be cached
     */
    protected boolean isCacheable(final WebRequestSettings request, final  WebResponse response) {
        return HttpMethod.GET == response.getRequestMethod() && isJavaScript(response) && !isDynamicContent(response);
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
        
        final long delay = 10 * DateUtils.MILLIS_PER_MINUTE;
        final long now = System.currentTimeMillis();

        final boolean cacheableContent = (expires != null && (expires.getTime() - now > delay)
                || (expires == null && lastModified != null && (now - lastModified.getTime() > delay)));

        return !cacheableContent;
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
        Date date = null;
        if (value != null) {
            try {
                date = DateUtil.parseDate(value);
            }
            catch (final DateParseException e) {
                //empty
            }
        }
        return date;
    }

    /**
     * Indicates if the provided response is JavaScript content.
     *
     * @param webResponse the response to analyze
     * @return <code>true</code> if it can be considered as JavaScript
     */
    protected boolean isJavaScript(final WebResponse webResponse) {
        final String contentType = webResponse.getContentType().toLowerCase();

        // many web applications are badly configured and have wrong headers, look at file extension too
        return "text/javascript".equals(contentType)
                || "application/x-javascript".equals(contentType)
                || webResponse.getUrl().getPath().endsWith(".js");
    }

    /**
     * Returns the cached content corresponding to the specified request. If there is
     * no corresponding cached content, this method returns <tt>null</tt>.
     *
     * @param request the request whose cached content is sought
     * @return the cached content corresponding to the specified request
     */
    public WebResponse getCachedContent(final WebRequestSettings request) {
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
        return cachedEntry.response_;
    }

    /**
     * Returns the cache's maximum size. This is the maximum number of files that will
     * be cached. The default is <tt>20</tt>.
     *
     * @return the cache's maximum size
     */
    public int getMaxSize() {
        return maxSize_;
    }

    /**
     * Sets the cache's maximum size. This is the maximum number of files that will
     * be cached. The default is <tt>20</tt>.
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
