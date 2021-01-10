/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.css;

import com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl;
import com.gargoylesoftware.css.parser.selector.SelectorSpecificity;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;

/**
 * A non-js CSSStyleDeclaration for wrapping a stylesheet
 *
 */
public class CssStyleDeclarationWrapper implements CssStyleDeclaration {

    /** The wrapped CSSStyleDeclaration (if created from CSSStyleRule). */
    private final CSSStyleDeclarationImpl styleDeclaration_;

    private final BrowserVersion browserVersion;

    /**
     * Creates an instance which wraps the specified style declaration.
     * @param styleDeclaration the style declaration to wrap
     * @param browserVersion the browser version
     */
    CssStyleDeclarationWrapper(CSSStyleDeclarationImpl styleDeclaration, BrowserVersion browserVersion) {
        this.browserVersion = browserVersion;
        this.styleDeclaration_ = styleDeclaration;
    }

    @Override
    public BrowserVersion getBrowserVersion() {
        return browserVersion;
    }

    @Override
    public StyleElement getStyleElement(String name) {
        String value = styleDeclaration_.getPropertyValue(name);
        if(value.isEmpty())
            return null;
        String priority = styleDeclaration_.getPropertyPriority(name);
        return new StyleElement(name, value, priority, SelectorSpecificity.FROM_STYLE_ATTRIBUTE);
    }

    @Override
    public String getStylePriority(String name) {
        return styleDeclaration_.getPropertyPriority(name);
    }

    @Override
    public void setStyleAttribute(String name, String newValue, String important) {
        if (null == newValue || "null".equals(newValue)) {
            newValue = "";
        }
        if (styleDeclaration_ != null) {
            styleDeclaration_.setProperty(name, newValue, important);
        }
    }

    @Override
    public String removeStyleAttribute(String name) {
        return styleDeclaration_.removeProperty(name);
    }

    @Override
    public String getCssText() {
        final String text = styleDeclaration_.getCssText();
        if (styleDeclaration_.getLength() > 0) {
            return text + ";";
        }
        return text;
    }

    @Override
    public void setCssText(String value) {
        styleDeclaration_.setCssText(value);
    }

    @Override
    public String toString() {
        return "CSSStyleDeclaration for 'null'"; // for instance on prototype
    }
}
