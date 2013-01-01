/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
 * This refresh handler spawns a new thread that waits the specified
 * number of seconds before refreshing the specified page, using the
 * specified URL.
 *
 * If you want a refresh handler that ignores the wait time, see
 * {@link ImmediateRefreshHandler}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 */
public class ThreadedRefreshHandler implements RefreshHandler {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(ThreadedRefreshHandler.class);

    /**
     * Refreshes the specified page using the specified URL after the specified number
     * of seconds.
     * @param page the page that is going to be refreshed
     * @param url the URL where the new page will be loaded
     * @param seconds the number of seconds to wait before reloading the page
     */
    public void handleRefresh(final Page page, final URL url, final int seconds) {
        final Thread thread = new Thread("ThreadedRefreshHandler Thread") {
            @Override
            public void run() {
                try {
                    new WaitingRefreshHandler().handleRefresh(page, url, seconds);
                }
                catch (final IOException e) {
                    LOG.error("Unable to refresh page!", e);
                    throw new RuntimeException("Unable to refresh page!", e);
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

}
