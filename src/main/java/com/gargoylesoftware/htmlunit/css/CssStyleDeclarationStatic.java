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

import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;

class CssStyleDeclarationStatic implements CssStyleDeclaration {
    private static final CssStyleDeclarationStatic INSTANCE = new CssStyleDeclarationStatic();

    static CssStyleDeclarationStatic getInstance() {
        return INSTANCE;
    }

    @Override
    public BrowserVersion getBrowserVersion() {
        return null;
    }

    @Override
    public StyleElement getStyleElement(String name) {
        return null;
    }

    @Override
    public String getStyleAttribute(Definition definition) {
        return "";
    }

    @Override
    public String getStyleAttribute(String name) {
        return "";
    }

    @Override
    public String getStyleAttribute(Definition style, boolean getDefaultValueIfEmpty) {
        return "";
    }

    @Override
    public String getStylePriority(String name) {
        return "";
    }

    @Override
    public void setStyleAttribute(String name, String newValue, String important) {
    }

    @Override
    public String removeStyleAttribute(String name) {
        return "";
    }

    @Override
    public String getCssText() {
        return "";
    }

    @Override
    public void setCssText(String value) {
    }

    @Override
    public List<Property> getProperties() {
        return Collections.emptyList();
    }

    @Override
    public AbstractCSSRuleImpl getParentRule() {
        return null;
    }
}
