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

import com.gargoylesoftware.htmlunit.html.HtmlData;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLDataElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlData.class, value = {CHROME, EDGE, FF, FF78})
public class HTMLDataElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public HTMLDataElement() {
    }

    /**
     * Sets the value of the attribute {@code value}.
     * @param newValue the new value to set
     */
    @JsxSetter
    public void setValue(final String newValue) {
        getDomNodeOrDie().setAttribute("value", newValue);
    }

    /**
     * Returns the {@code value} property.
     * @return the {@code value} property
     */
    @JsxGetter
    @Override
    public String getValue() {
        return getDomNodeOrDie().getAttributeDirect("value");
    }
}
