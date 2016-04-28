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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.html.HtmlFont;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * The JavaScript object {@code HTMLFontElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlFont.class)
public class HTMLFontElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public HTMLFontElement() {
    }

    /**
     * Gets the {@code color} attribute.
     * @return the {@code color} attribute
     */
    @JsxGetter
    public String getColor() {
        return getDomNodeOrDie().getAttribute("color");
    }

    /**
     * Sets the {@code color} attribute.
     * @param color the {@code color} attribute
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
     * Gets the {@code size} attribute.
     * @return the {@code size} attribute
     */
    @JsxGetter
    public int getSize() {
        return (int) Context.toNumber(getDomNodeOrDie().getAttribute("size"));
    }

    /**
     * Sets the {@code size} attribute.
     * @param size the {@code size} attribute
     */
    @JsxSetter
    public void setSize(final int size) {
        getDomNodeOrDie().setAttribute("size", Context.toString(size));
    }
}
