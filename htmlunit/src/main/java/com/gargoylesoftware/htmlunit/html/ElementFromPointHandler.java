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
package com.gargoylesoftware.htmlunit.html;

/**
 * An interface to allow the user to specify which element to return for the javascript
 * {@code document.elementFromPoint()}.
 *
 * This is needed since calculating the element location is outside the current scope of HtmlUnit.
 * @author Ahmed Ashour
 */
public interface ElementFromPointHandler {

    /**
     * Returns the element for the specified x coordinate and the specified y coordinate.
     * The current implementation always returns the &lt;body&gt; element.
     *
     * @param htmlPage the page
     * @param x the x offset, in pixels
     * @param y the y offset, in pixels
     * @return the element for the specified x coordinate and the specified y coordinate
     */
    HtmlElement getElementFromPoint(final HtmlPage htmlPage, final int x, final int y);
}
