/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * An interactive SGML page, which is able to handle JavaScript events.
 *
 * @author Ahmed Ashour
 * @deprecated as of 2.25, please use {@link HtmlPage} instead
 */
@Deprecated
public abstract class InteractivePage extends SgmlPage {

    /**
     * Creates an instance of {@code InteractiveSgmlPage}.
     *
     * @param webResponse the web response that was used to create this page
     * @param webWindow the window that this page is being loaded into
     */
    public InteractivePage(final WebResponse webResponse, final WebWindow webWindow) {
        super(webResponse, webWindow);
    }

    /**
     * Returns the element with the focus or null if no element has the focus.
     * @return the element with focus or null
     * @see #setFocusedElement(DomElement)
     */
    public abstract DomElement getFocusedElement();

}