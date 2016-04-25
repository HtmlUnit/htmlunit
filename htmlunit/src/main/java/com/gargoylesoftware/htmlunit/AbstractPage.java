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
package com.gargoylesoftware.htmlunit;

import java.net.URL;

/**
 * A basic {@link Page} implementation.
 *
 * @author Ahmed Ashour
 */
public class AbstractPage implements Page {

    private final WebResponse webResponse_;
    private WebWindow enclosingWindow_;

    /**
     * Creates an instance.
     *
     * @param webResponse the response from the server
     * @param enclosingWindow the window that holds the page
     */
    public AbstractPage(final WebResponse webResponse, final WebWindow enclosingWindow) {
        webResponse_ = webResponse;
        enclosingWindow_ = enclosingWindow;
    }

    /**
     * Initializes this page.
     */
    @Override
    public void initialize() {
        // nothing to do
    }

    /**
     * Cleans up this page.
     */
    @Override
    public void cleanUp() {
        if (getEnclosingWindow().getWebClient().getCache().getCachedResponse(webResponse_.getWebRequest()) == null) {
            webResponse_.cleanUp();
        }
    }

    /**
     * Returns the web response that was originally used to create this page.
     *
     * @return the web response that was originally used to create this page
     */
    @Override
    public WebResponse getWebResponse() {
        return webResponse_;
    }

    /**
     * Returns the window that this page is sitting inside.
     *
     * @return the enclosing frame or null if this page isn't inside a frame
     */
    @Override
    public WebWindow getEnclosingWindow() {
        return enclosingWindow_;
    }

    /**
     * Returns the URL of this page.
     * @return the URL of this page
     */
    @Override
    public URL getUrl() {
        return getWebResponse().getWebRequest().getUrl();
    }

    @Override
    public boolean isHtmlPage() {
        return false;
    }
}
