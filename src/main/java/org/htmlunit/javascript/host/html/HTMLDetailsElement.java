/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;

import org.htmlunit.html.HtmlDetails;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLDetailsElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlDetails.class)
public class HTMLDetailsElement extends HTMLElement {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the {@code open} property.
     * @return the {@code open} property
     */
    @JsxGetter
    public boolean isOpen() {
        return ((HtmlDetails) getDomNodeOrDie()).isOpen();
    }

    /**
     * Sets the open attribute.
     * @param newValue the new value to set
     */
    @JsxSetter
    public void setOpen(final Object newValue) {
        final boolean bool = JavaScriptEngine.toBoolean(newValue);

        ((HtmlDetails) getDomNodeOrDie()).setOpen(bool);
    }

    /**
     * Returns the {@code name} property.
     * @return the {@code name} property
     */
    @JsxGetter({CHROME, EDGE})
    @Override
    public String getName() {
        return super.getName();
    }

    /**
     * Sets the name attribute.
     * @param newValue the new value to set
     */
    @JsxSetter
    @Override
    public void setName(final String newValue) {
        super.setName(newValue);
    }
}
