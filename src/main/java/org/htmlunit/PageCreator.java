/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit;

import java.io.IOException;

import org.htmlunit.html.parser.HTMLParser;

/**
 * Something that knows how to create a page object. It is also the responsibility
 * of the page creator to establish the relationship between the <code>webWindow</code>
 * and the page, usually by calling {@link WebWindow#setEnclosedPage(Page)}. This should
 * be done as early as possible, e.g. to allow for re-loading of pages during page parsing.
 *
 * @author Mike Bowler
 * @author Christian Sell
 */
public interface PageCreator {

    /**
     * Create a Page object for the specified web response.
     *
     * @param webResponse the response from the server
     * @param webWindow the window that this page will be loaded into
     * @exception IOException If an io problem occurs
     * @return the new page
     */
    Page createPage(WebResponse webResponse, WebWindow webWindow) throws IOException;

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @return the HTMLParser in use
     */
    HTMLParser getHtmlParser();
}

