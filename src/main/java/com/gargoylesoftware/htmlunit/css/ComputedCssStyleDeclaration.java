/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.css;

import java.util.Map;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.javascript.host.Element;

/**
 * An object for a CSSStyleDeclaration, which is computed.
 *
 * @see com.gargoylesoftware.htmlunit.javascript.host.Window#getComputedStyle(Object, String)
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 * @author Alex Gorbatovsky
 * @author cd alexndr
 */
public class ComputedCssStyleDeclaration extends AbstractCssStyleDeclaration {

    /** The wrapped CSSStyleDeclaration */
    private ElementCssStyleDeclaration elementStyleDeclaration_;

    public ComputedCssStyleDeclaration(final ElementCssStyleDeclaration styleDeclaration) {
        elementStyleDeclaration_ = styleDeclaration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStylePriority(final String name) {
        return elementStyleDeclaration_.getStylePriority(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssText() {
        return elementStyleDeclaration_.getCssText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleAttribute(final String name) {
        return elementStyleDeclaration_.getStyleAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCssText(final String value) {
        // read only
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleAttribute(final String name, final String newValue, final String important) {
        // read only
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeStyleAttribute(final String name) {
        // read only
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return elementStyleDeclaration_.getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object item(final int index) {
        return elementStyleDeclaration_.item(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractCSSRuleImpl getParentRule() {
        return elementStyleDeclaration_.getParentRule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleElement getStyleElement(final String name) {
        return elementStyleDeclaration_.getStyleElement(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleElement getStyleElementCaseInSensitive(final String name) {
        return elementStyleDeclaration_.getStyleElementCaseInSensitive(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, StyleElement> getStyleMap() {
        return elementStyleDeclaration_.getStyleMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element getElementOrNull() {
        return elementStyleDeclaration_.getElementOrNull();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement getDomElementOrNull() {
        return elementStyleDeclaration_.getDomElementOrNull();
    }
}
