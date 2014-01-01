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

/**
 * This refresh handler performs an immediate refresh if the refresh delay is
 * less or equal to the configured time and otherwise ignores totally the refresh instruction.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class NiceRefreshHandler extends ImmediateRefreshHandler {
    private final int maxDelay_;

    /**
     * Creates a new refresh handler that will immediately refresh if the refresh delay is no
     * longer than <tt>maxDelay</tt>. No refresh will be perform at all for refresh values
     * larger than <tt>maxDelay</tt>.
     *
     * @param maxDelay the maximum refreshValue (in seconds) that should cause a refresh
     */
    public NiceRefreshHandler(final int maxDelay) {
        if (maxDelay <= 0) {
            throw new IllegalArgumentException("Invalid maxDelay: " + maxDelay);
        }
        maxDelay_ = maxDelay;
    }

    /**
     * Refreshes the specified page using the specified URL immediately if the <tt>requestedWait</tt>
     * not larget that the <tt>maxDelay</tt>. Does nothing otherwise.
     * @param page the page that is going to be refreshed
     * @param url the URL where the new page will be loaded
     * @param requestedWait the number of seconds to wait before reloading the page
     * @throws IOException if the refresh fails
     */
    @Override
    public void handleRefresh(final Page page, final URL url, final int requestedWait) throws IOException {
        if (requestedWait > maxDelay_) {
            return;
        }

        super.handleRefresh(page, url, requestedWait);
    }

}
