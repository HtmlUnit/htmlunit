/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.HtmlFrameSet;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * Wrapper for the HTML element "frameset".
 *
 * @version $Revision$
 * @author Bruce Chapman
 * @author Ahmed Ashour
 */
@JsxClass(domClasses = HtmlFrameSet.class)
public class HTMLFrameSetElement extends HTMLElement {

    /**
     * Creates a new frameset instance.
     */
    public HTMLFrameSetElement() {
        // Empty.
    }

    /**
     * Sets the rows property.
     *
     * @param rows the rows attribute value
     */
    @JsxSetter
    public void setRows(final String rows) {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        if (htmlFrameSet != null) {
            htmlFrameSet.setAttribute("rows", rows);
        }
    }

    /**
     * Gets the rows property.
     *
     * @return the rows attribute value
     */

    @JsxGetter
    public String getRows() {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        return htmlFrameSet.getRowsAttribute();
    }

    /**
     * Sets the cols property.
     *
     * @param cols the cols attribute value
     */
    @JsxSetter
    public void setCols(final String cols) {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        if (htmlFrameSet != null) {
            htmlFrameSet.setAttribute("cols", cols);
        }
    }

    /**
     * Gets the cols property.
     *
     * @return the cols attribute value
     */
    @JsxGetter
    public String getCols() {
        final HtmlFrameSet htmlFrameSet = (HtmlFrameSet) getDomNodeOrNull();
        return htmlFrameSet.getColsAttribute();
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBorder() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }
}
