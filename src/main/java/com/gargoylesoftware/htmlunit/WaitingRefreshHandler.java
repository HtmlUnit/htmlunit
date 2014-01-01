/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This refresh handler waits the specified number of seconds (or a user defined maximum)
 * before refreshing the specified page, using the specified URL. Waiting happens
 * on the current thread
 *
 * If you want a refresh handler that ignores the wait time, see
 * {@link ImmediateRefreshHandler}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 */
public class WaitingRefreshHandler implements RefreshHandler {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(WaitingRefreshHandler.class);

    private final int maxwait_;

    /**
     * Creates a new refresh handler that will wait whatever time the server or content asks, unless
     * it it longer than <tt>maxwait</tt>. A value of <tt>maxwait</tt> that is less than <tt>1</tt>
     * will cause the refresh handler to always wait for whatever time the server or content requests.
     *
     * @param maxwait the maximum wait time before the refresh (in seconds)
     */
    public WaitingRefreshHandler(final int maxwait) {
        maxwait_ = maxwait;
    }

    /**
     * Creates a new refresh handler that will always wait whatever time the server or content asks.
     */
    public WaitingRefreshHandler() {
        maxwait_ = 0;
    }

    /**
     * Refreshes the specified page using the specified URL after the specified number of seconds.
     * @param page the page that is going to be refreshed
     * @param url the URL where the new page will be loaded
     * @param requestedWait the number of seconds to wait before reloading the page; if this is
     *        greater than <tt>maxwait</tt> then <tt>maxwait</tt> will be used instead
     * @throws IOException if the refresh fails
     */
    public void handleRefresh(final Page page, final URL url, final int requestedWait) throws IOException {
        int seconds = requestedWait;
        if (seconds > maxwait_ && maxwait_ > 0) {
            seconds = maxwait_;
        }
        try {
            Thread.sleep(seconds * 1000);
        }
        catch (final InterruptedException e) {
            /* This can happen when the refresh is happening from a navigation that started
             * from a setTimeout or setInterval. The navigation will cause all threads to get
             * interrupted, including the current thread in this case. It should be safe to
             * ignore it since this is the thread now doing the navigation. Eventually we should
             * refactor to force all navigation to happen back on the main thread.
             */
            if (LOG.isDebugEnabled()) {
                LOG.debug("Waiting thread was interrupted. Ignoring interruption to continue navigation.");
            }
        }
        final WebWindow window = page.getEnclosingWindow();
        if (window == null) {
            return;
        }
        final WebClient client = window.getWebClient();
        client.getPage(window, new WebRequest(url));
    }

}
