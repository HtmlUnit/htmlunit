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

import java.net.URL;

/**
 * A generic page that will be returned for JavaScript content.
 * Specifically any content types of "text/javascript"
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 */
public class JavaScriptPage implements Page {

    private final WebResponse webResponse_;
    private final String content_;
    private WebWindow enclosingWindow_;

    /**
     * Creates an instance.
     *
     * @param webResponse the response from the server
     * @param enclosingWindow the window that holds the page
     */
    public JavaScriptPage(final WebResponse webResponse, final WebWindow enclosingWindow) {
        webResponse_ = webResponse;
        content_ = webResponse.getContentAsString();
        enclosingWindow_ = enclosingWindow;
    }

    /**
     * Initializes this page.
     */
    public void initialize() {
    }

    /**
     * Cleans up this page.
     */
    public void cleanUp() {
        webResponse_.cleanUp();
    }

    /**
     * Returns the content of the page.
     *
     * @return the content of the page
     */
    public String getContent() {
        return content_;
    }

    /**
     * Returns the web response that was originally used to create this page.
     *
     * @return the web response that was originally used to create this page
     */
    public WebResponse getWebResponse() {
        return webResponse_;
    }

    /**
     * Returns the window that this page is sitting inside.
     *
     * @return the enclosing frame or null if this page isn't inside a frame
     */
    public WebWindow getEnclosingWindow() {
        return enclosingWindow_;
    }

    /**
     * Returns the URL of this page.
     * @return the URL of this page
     */
    public URL getUrl() {
        return getWebResponse().getWebRequest().getUrl();
    }
}
