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

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.javascript.annotations.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxSetter;

/**
 * The JavaScript object "HTMLBaseFontElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLBaseFontElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    public HTMLBaseFontElement() {
        // Empty.
    }

    /**
     * Gets the "color" attribute.
     * @return the "color" attribute
     */
    @JsxGetter
    public String jsxGet_color() {
        return getDomNodeOrDie().getAttribute("color");
    }

    /**
     * Sets the "color" attribute.
     * @param color the "color" attribute
     */
    @JsxSetter
    public void jsxSet_color(final String color) {
        getDomNodeOrDie().setAttribute("color", color);
    }

    /**
     * Gets the typeface family.
     * @return the typeface family
     */
    @JsxGetter
    public String jsxGet_face() {
        return getDomNodeOrDie().getAttribute("face");
    }

    /**
     * Sets the typeface family.
     * @param face the typeface family
     */
    @JsxSetter
    public void jsxSet_face(final String face) {
        getDomNodeOrDie().setAttribute("face", face);
    }

    /**
     * Gets the "size" attribute.
     * @return the "size" attribute
     */
    @JsxGetter
    public int jsxGet_size() {
        return (int) Context.toNumber(getDomNodeOrDie().getAttribute("size"));
    }

    /**
     * Sets the "size" attribute.
     * @param size the "size" attribute
     */
    @JsxSetter
    public void jsxSet_size(final int size) {
        getDomNodeOrDie().setAttribute("size", Context.toString(Integer.valueOf(size)));
    }
}
