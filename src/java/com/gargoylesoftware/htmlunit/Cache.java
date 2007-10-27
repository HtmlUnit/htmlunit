/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 * Simple cache implementation.<br/>
 * Current implementation's main purpose is to provide the ability to cache <tt>.js</tt> files.
 * @version $Revision$
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
public class Cache implements Serializable {

    private static final long serialVersionUID = -3864114727885057419L;

    private int nbMaxEntries_ = 20;
    private final Map entries_ = Collections.synchronizedMap(new HashMap(nbMaxEntries_));

    /**
     * A cache entry.
     */
    private class Entry implements Comparable {
        private final WebResponse response_;
        private long lastAccess_;
        
        Entry(final WebResponse response) {
            response_ = response;
            lastAccess_ = System.currentTimeMillis();
        }
        
        public int compareTo(final Object other) {
            return NumberUtils.compare(lastAccess_, ((Entry) other).lastAccess_);
        }

        /**
         * Updates the last access date
         */
        public void touch() {
            lastAccess_ = System.currentTimeMillis();
        }
    }

    /**
     * Cache the response if needed. Current implementation only caches JavaScript files
     * @param request the request
     * @param response the response
     */
    public void cacheIfNeeded(final WebRequestSettings request, final WebResponse response) {
        if (isCacheable(request, response)) {
            entries_.put(response.getUrl(), new Entry(response));
            deleteOverflow();
        }
    }

    /**
     * Truncates the cache to the maximal number of entries
     */
    protected void deleteOverflow() {
        synchronized (entries_) {
            while (entries_.size() > nbMaxEntries_) {
                final Entry oldestEntry = (Entry) Collections.min(entries_.values());
                entries_.remove(oldestEntry.response_.getUrl());
            }
        }
    }

    /**
     * Determines if the response should be cached
     * @param request the performed request
     * @param response the received response
     * @return <code>true</code> if the response should be cached
     */
    protected boolean isCacheable(final WebRequestSettings request, final  WebResponse response) {
        return SubmitMethod.GET.equals(response.getRequestMethod())
            && isJavaScript(response) && !isDynamicContent(response);
    }

    /**
     * Tries to guess if the content is dynamic or not.<br/>
     * "Since origin servers do not always provide explicit expiration times, HTTP caches typically assign heuristic
     * expiration times, employing algorithms that use other header values (such as the Last-Modified time) to
     * estimate a plausible expiration time". <br/>
     * Current implementation consider as dynamic content everything except responses with a Last-Modified header with
     * a date older than 10 minutes or with an Expires header specifying expiration in more than 10 minutes.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html">
     * @param response the response to examine
     * @return <code>true</code> if the response should be considered as dynamic and therefore uncachable
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
     * Parses the specified date header of the response
     * @param response the response
     * @param headerName the header name
     * @return <code>null</code> if no header found or if it can't be parsed as a date, otherwise the date
     */
    protected Date parseDateHeader(final WebResponse response, final String headerName) {
        final String value = response.getResponseHeaderValue(headerName);
        Date date = null;
        if (value != null) {
            try {
                date = DateUtil.parseDate(value);
            }
            catch (final DateParseException e) {
                date = null;
            }
        }
        return date;
    }

    /**
     * Indicates if the provided response is javascript content
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
     * Gets the cached content (if any)
     * @param request the request being performed
     * @return <code>null</code> if no cached content is available
     */
    public WebResponse getCachedContent(final WebRequestSettings request) {
        if (!SubmitMethod.GET.equals(request.getSubmitMethod())) {
            return null;
        }
        final Entry cachedEntry = (Entry) entries_.get(request.getURL());
        if (cachedEntry == null) {
            return null;
        }
        else {
            synchronized (entries_) {
                cachedEntry.touch();
            }
            return cachedEntry.response_;
        }
    }

    /**
     * Gets the maximal number of entries in cache (doesn't depend on their size)
     * @return the number of entries. Default is 20.
     */
    public int getNbMaxEntries() {
        return nbMaxEntries_;
    }

    /**
     * Sets the maximal number of entries in cache
     * @param nbMaxEntries the new value (must be &gt;= 0)
     */
    public void setNbMaxEntries(final int nbMaxEntries) {
        if (nbMaxEntries < 0) {
            throw new IllegalArgumentException("Illegal value for nbMaxEntries: " + nbMaxEntries);
        }
        nbMaxEntries_ = nbMaxEntries;
        deleteOverflow();
    }

    /**
     * Returns the number of entries in the cache.
     * @return the number of entries in the cache
     */
    public int getSize() {
        return entries_.size();
    }

}
