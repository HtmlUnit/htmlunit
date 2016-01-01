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

import java.io.IOException;
import java.io.InputStream;

/**
 * A generic page that is returned whenever an unexpected content type is returned by the server.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
public class UnexpectedPage extends AbstractPage {

    /**
     * Creates an instance.
     *
     * @param webResponse the response from the server that contains the data required to create this page
     * @param enclosingWindow the window that this page is being loaded into
     */
    public UnexpectedPage(final WebResponse webResponse, final WebWindow enclosingWindow) {
        super(webResponse, enclosingWindow);
    }

    /**
     * Returns an input stream representing all the content that was returned from the server.
     *
     * @return an input stream representing all the content that was returned from the server
     * @throws IOException in case of IO problems
     */
    public InputStream getInputStream() throws IOException {
        return getWebResponse().getContentAsStream();
    }
}
