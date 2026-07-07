/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import org.htmlunit.html.HtmlTime;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLTimeElement}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLTimeElement">MDN Documentation</a>
 */
@JsxClass(domClass = HtmlTime.class)
public class HTMLTimeElement extends HTMLElement {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the value of the {@code dateTime} property.
     * @return the value of the {@code dateTime} property
     */
    @JsxGetter
    public String getDateTime() {
        return getDomNodeOrDie().getAttribute("dateTime");
    }

    /**
     * Sets the value of the {@code dateTime} property.
     * @param dateTime the {@code dateTime} property value
     */
    @JsxSetter
    public void setDateTime(final String dateTime) {
        getDomNodeOrDie().setAttribute("dateTime", dateTime);
    }
}
