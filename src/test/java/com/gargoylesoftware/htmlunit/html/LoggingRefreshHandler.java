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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;
import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.RefreshHandler;

/**
 * A handler for page refreshes that logs the refreshes but doesn't actually
 * perform any refreshes.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 */
public class LoggingRefreshHandler implements RefreshHandler {
    private final List<Object> list_;

    /**
     * @param list the list to add data to
     */
    public LoggingRefreshHandler(final List<Object> list) {
        list_ = list;
    }

    /**
     * Logs the requested refresh, but does not actually refresh anything.
     * @param page the page that is going to be refreshed
     * @param url the URL where the new page will be loaded
     * @param seconds the number of seconds to wait before reloading the page
     */
    public void handleRefresh(final Page page, final URL url, final int seconds) {
        list_.add(((HtmlPage) page).getTitleText());
        list_.add(url);
        list_.add(Integer.valueOf(seconds));
    }
}
