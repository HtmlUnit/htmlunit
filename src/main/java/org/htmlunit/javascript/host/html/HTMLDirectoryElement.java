/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import org.htmlunit.html.HtmlDirectory;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLDirectoryElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlDirectory.class)
public class HTMLDirectoryElement extends HTMLElement {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the value of the {@code compact} attribute.
     * @return the value of the {@code compact} attribute
     */
    @JsxGetter
    public boolean isCompact() {
        return getDomNodeOrDie().hasAttribute("compact");
    }

    /**
     * Sets the value of the {@code compact} attribute.
     * @param compact the value of the {@code compact} attribute
     */
    @JsxSetter
    public void setCompact(final Object compact) {
        if (JavaScriptEngine.toBoolean(compact)) {
            getDomNodeOrDie().setAttribute("compact", "");
        }
        else {
            getDomNodeOrDie().removeAttribute("compact");
        }
    }
}
