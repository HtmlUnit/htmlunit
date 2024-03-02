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

import org.htmlunit.html.HtmlHtml;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLHtmlElement}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlHtml.class)
public class HTMLHtmlElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    public HTMLHtmlElement() {
    }

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /** {@inheritDoc} */
    @Override
    public Object getParentNode() {
        return getWindow().getDocument_js();
    }

    /** {@inheritDoc} */
    @Override
    public int getClientWidth() {
        return getWindow().getInnerWidth();
    }

    /** {@inheritDoc} */
    @Override
    public int getClientHeight() {
        return getWindow().getInnerHeight();
    }

    /**
     * Overwritten to throw an exception.
     * @param value the new value for replacing this node
     */
    @Override
    public void setOuterHTML(final Object value) {
        throw JavaScriptEngine.reportRuntimeError("outerHTML is read-only for tag 'html'");
    }

    /**
     * Returns {@code version} property.
     * @return the {@code version} property
     */
    @JsxGetter
    public String getVersion() {
        return getDomNodeOrDie().getAttributeDirect("version");
    }

    /**
     * Sets {@code version} property.
     * @param version the {@code version} property
     */
    @JsxSetter
    public void setVersion(final String version) {
        getDomNodeOrDie().setAttribute("version", version);
    }

}
