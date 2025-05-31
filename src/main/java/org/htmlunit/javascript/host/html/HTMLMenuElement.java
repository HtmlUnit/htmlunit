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

import static org.htmlunit.BrowserVersionFeatures.JS_MENU_TYPE_PASS;

import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlMenu;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLMenuElement}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlMenu.class)
public class HTMLMenuElement extends HTMLElement {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the value of the {@code type} property.
     * @return the value of the {@code type} property
     */
    protected String getType() {
        final String type = getDomNodeOrDie().getAttributeDirect("type");
        if (getBrowserVersion().hasFeature(JS_MENU_TYPE_PASS)) {
            return type;
        }

        if ("context".equalsIgnoreCase(type)) {
            return "context";
        }
        if ("toolbar".equalsIgnoreCase(type)) {
            return "toolbar";
        }

        return "list";
    }

    /**
     * Sets the value of the {@code type} property.
     * @param type the value of the {@code type} property
     */
    protected void setType(final String type) {
        if (getBrowserVersion().hasFeature(JS_MENU_TYPE_PASS)) {
            getDomNodeOrDie().setAttribute(DomElement.TYPE_ATTRIBUTE, type);
            return;
        }

        if ("context".equalsIgnoreCase(type)) {
            getDomNodeOrDie().setAttribute(DomElement.TYPE_ATTRIBUTE, "context");
            return;
        }
        if ("toolbar".equalsIgnoreCase(type)) {
            getDomNodeOrDie().setAttribute(DomElement.TYPE_ATTRIBUTE, "toolbar");
            return;
        }

        getDomNodeOrDie().setAttribute(DomElement.TYPE_ATTRIBUTE, "list");
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
