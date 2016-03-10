/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.performance;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for {@code PerformanceTiming}.
 * This impl is a simple mock for the moment.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class PerformanceTiming extends SimpleScriptable {

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
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public PerformanceTiming() {
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
     * @return a domainLookupStart
     */
    @JsxGetter
    public long getDomainLookupStart() {
        return domainLookupStart_;
    }

    /**
     * @return a domainLookupEnd
     */
    @JsxGetter
    public long getDomainLookupEnd() {
        return domainLookupEnd_;
    }

    /**
     * @return a connectStart
     */
    @JsxGetter
    public long getConnectStart() {
        return connectStart_;
    }

    /**
     * @return a connectEnd
     */
    @JsxGetter
    public long getConnectEnd() {
        return connectEnd_;
    }

    /**
     * @return a responseStart
     */
    @JsxGetter
    public long getResponseStart() {
        return responseStart_;
    }

    /**
     * @return a responseEnd
     */
    @JsxGetter
    public long getResponseEnd() {
        return responseEnd_;
    }

    /**
     * @return a secureConnectionStart
     */
    @JsxGetter(@WebBrowser(CHROME))
    public long getSecureConnectionStart() {
        return 0;
    }

    /**
     * @return an unloadEventStart
     */
    @JsxGetter
    public long getUnloadEventStart() {
        return 0;
    }

    /**
     * @return an unloadEventEnd
     */
    @JsxGetter
    public long getUnloadEventEnd() {
        return 0;
    }

    /**
     * @return an redirectStart
     */
    @JsxGetter
    public long getRedirectStart() {
        return 0;
    }

    /**
     * @return an redirectEnd
     */
    @JsxGetter
    public long getRedirectEnd() {
        return 0;
    }

    /**
     * @return a domContentLoadedEventStart
     */
    @JsxGetter
    public long getDomContentLoadedEventStart() {
        return domContentLoadedEventStart_;
    }

    /**
     * @return a domLoading
     */
    @JsxGetter
    public long getDomLoading() {
        return domLoading_;
    }

    /**
     * @return a domInteractive
     */
    @JsxGetter
    public long getDomInteractive() {
        return domInteractive_;
    }

    /**
     * @return a domContentLoadedEventEnd
     */
    @JsxGetter
    public long getDomContentLoadedEventEnd() {
        return domContentLoadedEventEnd_;
    }

    /**
     * @return a domComplete
     */
    @JsxGetter
    public long getDomComplete() {
        return domComplete_;
    }

    /**
     * @return a loadEventStart
     */
    @JsxGetter
    public long getLoadEventStart() {
        return loadEventStart_;
    }

    /**
     * @return a loadEventEnd
     */
    @JsxGetter
    public long getLoadEventEnd() {
        return loadEventEnd_;
    }

    /**
     * @return a navigationStart
     */
    @JsxGetter
    public long getNavigationStart() {
        return navigationStart_;
    }

    /**
     * @return a navigationStart
     */
    @JsxGetter
    public long getFetchStart() {
        return fetchStart_;
    }
}
