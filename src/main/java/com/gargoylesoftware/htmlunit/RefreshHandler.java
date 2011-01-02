/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
 * A handler for page refreshes. A refresh can be triggered by a response header or a meta tag.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 */
public interface RefreshHandler {

    /**
     * Refreshes the specified page using the specified URL after the specified number
     * of seconds.
     * @param page the page that is going to be refreshed
     * @param url the URL where the new page will be loaded
     * @param seconds the number of seconds to wait before reloading the page
     * @throws IOException if the refresh fails
     */
    void handleRefresh(final Page page, final URL url, final int seconds) throws IOException;

}
