/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import static org.htmlunit.BrowserVersionFeatures.JS_PRE_WIDTH_STRING;
import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;
import static org.htmlunit.javascript.configuration.SupportedBrowser.IE;

import org.apache.commons.lang3.ArrayUtils;
import org.htmlunit.html.HtmlExample;
import org.htmlunit.html.HtmlListing;
import org.htmlunit.html.HtmlPreformattedText;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLPreElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlExample.class, value = {CHROME, EDGE, FF, FF_ESR})
@JsxClass(domClass = HtmlPreformattedText.class)
@JsxClass(domClass = HtmlListing.class, value = {CHROME, EDGE, FF, FF_ESR})
public class HTMLPreElement extends HTMLElement {

    /** Valid values for the {@link #getClear() clear} property. */
    private static final String[] VALID_CLEAR_VALUES = {"left", "right", "all", "none"};

    /**
     * Creates an instance.
     */
    public HTMLPreElement() {
    }

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the value of the {@code cite} property.
     * @return the value of the {@code cite} property
     */
    @JsxGetter(IE)
    public String getCite() {
        return getDomNodeOrDie().getAttributeDirect("cite");
    }

    /**
     * Returns the value of the {@code cite} property.
     * @param cite the value
     */
    @JsxSetter(IE)
    public void setCite(final String cite) {
        getDomNodeOrDie().setAttribute("cite", cite);
    }

    /**
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    @JsxGetter(propertyName = "width")
    public Object getWidth_js() {
        if (getBrowserVersion().hasFeature(JS_PRE_WIDTH_STRING)) {
            return getWidthOrHeight("width", Boolean.TRUE);
        }
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
    @JsxSetter(propertyName = "width")
    public void setWidth_js(final String width) {
        if (getBrowserVersion().hasFeature(JS_PRE_WIDTH_STRING)) {
            setWidthOrHeight("width", width, true);
        }
        else {
            getDomNodeOrDie().setAttribute("width", width);
        }
    }

    /**
     * Returns the value of the {@code clear} property.
     * @return the value of the {@code clear} property
     */
    @JsxGetter(IE)
    public String getClear() {
        final String clear = getDomNodeOrDie().getAttributeDirect("clear");
        if (!ArrayUtils.contains(VALID_CLEAR_VALUES, clear)) {
            return "";
        }
        return clear;
    }

    /**
     * Sets the value of the {@code clear} property.
     * @param clear the value of the {@code clear} property
     */
    @JsxSetter(IE)
    public void setClear(final String clear) {
        if (!ArrayUtils.contains(VALID_CLEAR_VALUES, clear)) {
            throw JavaScriptEngine.reportRuntimeError("Invalid clear property value: '" + clear + "'.");
        }
        getDomNodeOrDie().setAttribute("clear", clear);
    }
}
