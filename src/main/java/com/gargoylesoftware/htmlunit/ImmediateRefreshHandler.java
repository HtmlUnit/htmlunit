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
import java.io.Serializable;
import java.net.URL;

/**
 * This refresh handler immediately refreshes the specified page,
 * using the specified URL and ignoring the wait time.
 *
 * If you want a refresh handler that does not ignore the wait time,
 * see {@link ThreadedRefreshHandler}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class ImmediateRefreshHandler implements RefreshHandler, Serializable {

    /**
     * Immediately refreshes the specified page using the specified URL.
     * @param page the page that is going to be refreshed
     * @param url the URL where the new page will be loaded
     * @param seconds the number of seconds to wait before reloading the page (ignored!)
     * @throws IOException if the refresh fails
     */
    public void handleRefresh(final Page page, final URL url, final int seconds) throws IOException {
        final WebWindow window = page.getEnclosingWindow();
        if (window == null) {
            return;
        }
        final WebClient client = window.getWebClient();
        if (page.getUrl().toExternalForm().equals(url.toExternalForm())
                && HttpMethod.GET == page.getWebResponse().getWebRequest().getHttpMethod()) {
            final String msg = "Refresh to " + url + " (" + seconds + "s) aborted by HtmlUnit: "
                + "Attempted to refresh a page using an ImmediateRefreshHandler "
                + "which could have caused an OutOfMemoryError "
                + "Please use WaitingRefreshHandler or ThreadedRefreshHandler instead.";
            throw new RuntimeException(msg);
        }
        client.getPage(window, new WebRequest(url));
    }

}
