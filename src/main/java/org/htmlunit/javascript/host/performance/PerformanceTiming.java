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
package org.htmlunit.javascript.host.performance;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * JavaScript host object for {@code PerformanceTiming}.
 * This implementation is a simple mock for the moment.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/PerformanceTiming">MDN Documentation</a>
 */
@JsxClass
public class PerformanceTiming extends HtmlUnitScriptable {

    private final long domainLookupStart_;
    private final long domainLookupEnd_;
    private final long connectStart_;
    private final long connectEnd_;
    private final long responseStart_;
    private final long responseEnd_;

    private final long domContentLoadedEventStart_;
    private final long domContentLoadedEventEnd_;
    private final long domLoading_;
    private final long domInteractive_;
    private final long domComplete_;

    private final long loadEventStart_;
    private final long loadEventEnd_;
    private final long navigationStart_;
    private final long fetchStart_;

    /**
     * Creates an instance.
     */
    public PerformanceTiming() {
        super();
        final long now = System.currentTimeMillis();

        // simulate the fastest browser on earth
        domainLookupStart_ = now;
        domainLookupEnd_ = domainLookupStart_ + 1L;

        connectStart_ = domainLookupEnd_;
        connectEnd_ = connectStart_ + 1L;

        responseStart_ = connectEnd_;
        responseEnd_ = responseStart_ + 1L;

        loadEventStart_ = responseEnd_;
        loadEventEnd_ = loadEventStart_ + 1L;
        domLoading_ = responseEnd_;
        domInteractive_ = responseEnd_;
        domContentLoadedEventStart_ = responseEnd_;
        domContentLoadedEventEnd_ = domContentLoadedEventStart_ + 1L;
        domComplete_ = domContentLoadedEventEnd_;

        navigationStart_ = now;
        fetchStart_ = now;
    }

    /**
     * Creates an instance of this object.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Returns the value of the {@code domainLookupStart} property.
     *
     * @return the timestamp immediately before the browser starts the domain name lookup
     */
    @JsxGetter
    public long getDomainLookupStart() {
        return domainLookupStart_;
    }

    /**
     * Returns the value of the {@code domainLookupEnd} property.
     *
     * @return the timestamp immediately after the browser finishes the domain name lookup
     */
    @JsxGetter
    public long getDomainLookupEnd() {
        return domainLookupEnd_;
    }

    /**
     * Returns the value of the {@code connectStart} property.
     *
     * @return the timestamp immediately before the browser starts to establish the connection to the server
     */
    @JsxGetter
    public long getConnectStart() {
        return connectStart_;
    }

    /**
     * Returns the value of the {@code connectEnd} property.
     *
     * @return the timestamp immediately after the browser finishes establishing the connection to the server
     */
    @JsxGetter
    public long getConnectEnd() {
        return connectEnd_;
    }

    /**
     * Returns the value of the {@code responseStart} property.
     *
     * @return the timestamp immediately after the browser receives the first byte of the response
     */
    @JsxGetter
    public long getResponseStart() {
        return responseStart_;
    }

    /**
     * Returns the value of the {@code responseEnd} property.
     *
     * @return the timestamp immediately after the browser receives the last byte of the response
     */
    @JsxGetter
    public long getResponseEnd() {
        return responseEnd_;
    }

    /**
     * Returns the value of the {@code secureConnectionStart} property.
     *
     * @return the timestamp immediately before the browser starts the handshake process to secure the current connection
     */
    @JsxGetter
    public long getSecureConnectionStart() {
        return 0;
    }

    /**
     * Returns the value of the {@code unloadEventStart} property.
     *
     * @return the timestamp immediately before the unload event is fired
     */
    @JsxGetter
    public long getUnloadEventStart() {
        return 0;
    }

    /**
     * Returns the value of the {@code unloadEventEnd} property.
     *
     * @return the timestamp immediately after the unload event completes
     */
    @JsxGetter
    public long getUnloadEventEnd() {
        return 0;
    }

    /**
     * Returns the value of the {@code redirectStart} property.
     *
     * @return the timestamp of the start of the first HTTP redirect
     */
    @JsxGetter
    public long getRedirectStart() {
        return 0;
    }

    /**
     * Returns the value of the {@code redirectEnd} property.
     *
     * @return the timestamp immediately after the last redirect response is received
     */
    @JsxGetter
    public long getRedirectEnd() {
        return 0;
    }

    /**
     * Returns the value of the {@code domContentLoadedEventStart} property.
     *
     * @return the timestamp immediately before the {@code DOMContentLoaded} event is fired
     */
    @JsxGetter
    public long getDomContentLoadedEventStart() {
        return domContentLoadedEventStart_;
    }

    /**
     * Returns the value of the {@code domLoading} property.
     *
     * @return the timestamp immediately before the browser sets the document readyState to {@code loading}
     */
    @JsxGetter
    public long getDomLoading() {
        return domLoading_;
    }

    /**
     * Returns the value of the {@code domInteractive} property.
     *
     * @return the timestamp immediately before the browser sets the document readyState to {@code interactive}
     */
    @JsxGetter
    public long getDomInteractive() {
        return domInteractive_;
    }

    /**
     * Returns the value of the {@code domContentLoadedEventEnd} property.
     *
     * @return the timestamp immediately after the {@code DOMContentLoaded} event completes
     */
    @JsxGetter
    public long getDomContentLoadedEventEnd() {
        return domContentLoadedEventEnd_;
    }

    /**
     * Returns the value of the {@code domComplete} property.
     *
     * @return the timestamp immediately before the browser sets the document readyState to {@code complete}
     */
    @JsxGetter
    public long getDomComplete() {
        return domComplete_;
    }

    /**
     * Returns the value of the {@code loadEventStart} property.
     *
     * @return the timestamp immediately before the load event is fired
     */
    @JsxGetter
    public long getLoadEventStart() {
        return loadEventStart_;
    }

    /**
     * Returns the value of the {@code loadEventEnd} property.
     *
     * @return the timestamp immediately after the load event completes
     */
    @JsxGetter
    public long getLoadEventEnd() {
        return loadEventEnd_;
    }

    /**
     * Returns the value of the {@code navigationStart} property.
     *
     * @return the timestamp immediately after the previous document's unload event completes
     */
    @JsxGetter
    public long getNavigationStart() {
        return navigationStart_;
    }

    /**
     * Returns the value of the {@code fetchStart} property.
     *
     * @return the timestamp immediately before the browser starts to fetch the resource
     */
    @JsxGetter
    public long getFetchStart() {
        return fetchStart_;
    }
}
