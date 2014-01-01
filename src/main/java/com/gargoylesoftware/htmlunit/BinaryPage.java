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
import java.io.InputStream;
import java.net.URL;

/**
 * A page for binary content. You must use {@link #getInputStream()} to get the content.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class BinaryPage implements Page {

    private final WebResponse webResponse_;
    private WebWindow enclosingWindow_;

    /**
     * Creates an instance.
     *
     * @param webResponse the response from the server that contains the data required to create this page
     * @param enclosingWindow the window that this page is being loaded into
     */
    public BinaryPage(final WebResponse webResponse, final WebWindow enclosingWindow) {
        webResponse_ = webResponse;
        enclosingWindow_ = enclosingWindow;
    }

    /**
     * {@inheritDoc}
     */
    public void initialize() {
        // nothing to do here
    }

    /**
     * {@inheritDoc}
     */
    public void cleanUp() {
        webResponse_.cleanUp();
    }

    /**
     * Returns an input stream representing all the content that was returned from the server.
     *
     * @return an input stream representing all the content that was returned from the server
     * @throws IOException in case of IO problems
     */
    public InputStream getInputStream() throws IOException {
        return webResponse_.getContentAsStream();
    }

    /**
     * {@inheritDoc}
     */
    public WebResponse getWebResponse() {
        return webResponse_;
    }

    /**
     * {@inheritDoc}
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

    @Override
    public boolean isHtmlPage() {
        return false;
    }
}
