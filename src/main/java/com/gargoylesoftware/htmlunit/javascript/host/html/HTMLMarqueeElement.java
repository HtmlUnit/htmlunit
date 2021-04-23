/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.html.HtmlMarquee;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLMarqueeElement}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@JsxClass(domClass = HtmlMarquee.class, value = {CHROME, EDGE, FF, FF78, IE})
public class HTMLMarqueeElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLMarqueeElement() {
    }

    /**
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    @Override
    @JsxGetter({CHROME, EDGE, IE})
    public int getWidth() {
        final String value = getDomNodeOrDie().getAttributeDirect("width");
        final Integer intValue = HTMLCanvasElement.getValue(value);
        if (intValue != null) {
            return intValue;
        }
        return 0;
    }

    /**
     * Sets the {@code width} property.
     * @param width the {@code width} property
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setWidth(final int width) {
        getDomNodeOrDie().setAttribute("width", Integer.toString(width));
    }

    /**
     * Returns the {@code height} property.
     * @return the {@code height} property
     */
    @Override
    @JsxGetter({CHROME, EDGE, IE})
    public int getHeight() {
        final String value = getDomNodeOrDie().getAttributeDirect("height");
        final Integer intValue = HTMLCanvasElement.getValue(value);
        if (intValue != null) {
            return intValue;
        }
        return 0;
    }

    /**
     * Sets the {@code height} property.
     * @param height the {@code height} property
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setHeight(final int height) {
        getDomNodeOrDie().setAttribute("height", Integer.toString(height));
    }

    /**
     * Returns the value of the {@code bgColor} property.
     * @return the value of the {@code bgColor} property
     */
    @JsxGetter({CHROME, EDGE, IE})
    public String getBgColor() {
        return getDomNodeOrDie().getAttribute("bgColor");
    }

    /**
     * Sets the value of the {@code bgColor} property.
     * @param bgColor the value of the {@code bgColor} property
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setBgColor(final String bgColor) {
        setColorAttribute("bgColor", bgColor);
    }

}
