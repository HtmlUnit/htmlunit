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
import java.io.Serializable;
import java.net.URL;

/**
 * An abstract page that represents some content returned from a server.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 */
public interface Page extends Serializable {

    /**
     * Initialize this page.
     * This method gets called when a new page is loaded and you should probably never
     * need to call it directly.
     * @throws IOException if an IO problem occurs
     */
    void initialize() throws IOException;

    /**
     * Clean up this page.
     * This method gets called by the web client when an other page is loaded in the window
     * and you should probably never need to call it directly
     * @throws IOException if an IO problem occurs
     */
    void cleanUp() throws IOException;

    /**
     * Returns the web response that was originally used to create this page.
     * @return the web response
     */
    WebResponse getWebResponse();

    /**
     * Returns the window that this page is sitting inside.
     * @return the enclosing window
     */
    WebWindow getEnclosingWindow();

    /**
     * Returns the URL of this page.
     * @return the URL of this page
     */
    URL getUrl();
}
