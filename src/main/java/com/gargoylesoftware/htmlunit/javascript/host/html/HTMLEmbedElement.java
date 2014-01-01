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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.html.HtmlEmbed;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for {@link HtmlEmbed}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlEmbed.class)
public class HTMLEmbedElement extends HTMLElement {

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    @JsxGetter(@WebBrowser(value = FF, minVersion = 17))
    public String getAlign() {
        return getAlign(true);
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    @JsxSetter(@WebBrowser(value = FF, minVersion = 17))
    public void setAlign(final String align) {
        setAlign(align, false);
    }

    /**
     * Returns the value of the "height" property.
     * @return the value of the "height" property
     */
    @JsxGetter(propertyName = "height")
    public String getHeightString() {
        return getDomNodeOrDie().getAttribute("height");
    }

    /**
     * Sets the value of the "height" property.
     * @param height the value of the "height" property
     */
    @JsxSetter(propertyName = "height")
    public void setHeightString(final String height) {
        getDomNodeOrDie().setAttribute("height", height);
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    @JsxGetter(propertyName = "width")
    public String getWidthString() {
        return getDomNodeOrDie().getAttribute("width");
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    @JsxSetter(propertyName = "width")
    public void setWidthString(final String width) {
        getDomNodeOrDie().setAttribute("width", width);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return true;
    }
}
