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

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * A representation of an XHTML page returned from a server.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class XHtmlPage extends HtmlPage {

    private static final long serialVersionUID = -8217258738281561778L;

    /**
     * Creates a new XHTML page instance. An XHTML page instance is normally retrieved
     * with {@link com.gargoylesoftware.htmlunit.WebClient#getPage(String)}.
     *
     * @param originatingUrl the URL that was used to load this page
     * @param webResponse the web response that was used to create this page
     * @param webWindow the window that this page is being loaded into
     */
    public XHtmlPage(final URL originatingUrl, final WebResponse webResponse, final WebWindow webWindow) {
        super(originatingUrl, webResponse, webWindow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasCaseSensitiveTagNames() {
        return true;
    }

}
