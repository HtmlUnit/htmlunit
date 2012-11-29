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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLBASEFONT_END_TAG_FORBIDDEN;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.HtmlBaseFont;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object "HTMLBaseFontElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(domClasses = HtmlBaseFont.class)
public class HTMLBaseFontElement extends HTMLElement {

    /**
     * Gets the "color" attribute.
     * @return the "color" attribute
     */
    @JsxGetter
    public String getColor() {
        return getDomNodeOrDie().getAttribute("color");
    }

    /**
     * Sets the "color" attribute.
     * @param color the "color" attribute
     */
    @JsxSetter
    public void setColor(final String color) {
        getDomNodeOrDie().setAttribute("color", color);
    }

    /**
     * Gets the typeface family.
     * @return the typeface family
     */
    @JsxGetter
    public String getFace() {
        return getDomNodeOrDie().getAttribute("face");
    }

    /**
     * Sets the typeface family.
     * @param face the typeface family
     */
    @JsxSetter
    public void setFace(final String face) {
        getDomNodeOrDie().setAttribute("face", face);
    }

    /**
     * Gets the "size" attribute.
     * @return the "size" attribute
     */
    @JsxGetter
    public int getSize() {
        return (int) Context.toNumber(getDomNodeOrDie().getAttribute("size"));
    }

    /**
     * Sets the "size" attribute.
     * @param size the "size" attribute
     */
    @JsxSetter
    public void setSize(final int size) {
        getDomNodeOrDie().setAttribute("size", Context.toString(Integer.valueOf(size)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return getBrowserVersion().hasFeature(HTMLBASEFONT_END_TAG_FORBIDDEN);
    }
}
