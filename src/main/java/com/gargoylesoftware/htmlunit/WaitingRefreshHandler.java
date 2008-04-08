/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
    private static final Log LOG = LogFactory.getLog(ThreadManager.class);

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
     * Create a WaitingRefreshHandler that will always wait whatever time the server or
     * content asks.
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
            LOG.debug("Waiting thread was interrupted. Ignoring interruption to continue navigation.");
        }
        final WebWindow window = page.getEnclosingWindow();
        if (window == null) {
            return;
        }
        final WebClient client = window.getWebClient();
        client.getPage(window, new WebRequestSettings(url));
    }

}
