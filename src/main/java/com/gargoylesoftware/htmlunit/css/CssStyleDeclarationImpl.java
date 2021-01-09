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

import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.css.parser.selector.Selector;
import com.gargoylesoftware.css.parser.selector.SelectorSpecificity;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.html.DomElement;

class CssStyleDeclarationImpl implements CssStyleDeclaration {
    private DomElement element;

    /** The wrapped CSSStyleDeclaration (if created from CSSStyleRule). */
    protected com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl styleDeclaration_;

    /**
     * Local modifications maintained here rather than in the element. We use a sorted
     * map so that results are deterministic and thus easily testable.
     */
    private final SortedMap<String, StyleElement> localModifications_ = new TreeMap<>();

    /**
     * Creates an instance and sets its parent scope to the one of the provided element.
     * @param parentScope the element to which this style is bound
     */
    CssStyleDeclarationImpl(DomElement parentScope){
        this.element = parentScope;
    }

    /**
     * Creates an instance which wraps the specified style declaration.
     * @param element the element to which this style is bound
     * @param styleDeclaration the style declaration to wrap
     */
    CssStyleDeclarationImpl(DomElement element, com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl styleDeclaration) {
        this.element = element;
        this.styleDeclaration_ = styleDeclaration;
    }

    protected DomElement getDomElement() {
        return element;
    }

    /**
     * Gets the browser version currently used.
     * @return the browser version
     */
    private BrowserVersion getBrowserVersion() {
        return element.getPage().getWebClient().getBrowserVersion();
    }

    @Override
    public void setDefaultLocalStyleAttribute(final String name, final String newValue) {
        //prevent overwrite of non-default styles
        if (!localModifications_.containsKey(name)
            || localModifications_.get(name).getSpecificity().equals(SelectorSpecificity.DEFAULT_STYLE_ATTRIBUTE)) {
            final StyleElement element = new StyleElement(name, newValue, "",
                SelectorSpecificity.DEFAULT_STYLE_ATTRIBUTE);
            localModifications_.put(name, element);
        }
    }

    @Override
    public StyleElement getStyleElement(final String name) {
        final StyleElement existent;
        if (styleDeclaration_ != null) {
            String value = styleDeclaration_.getPropertyValue(name);
            if (value.isEmpty()) {
                existent = null;
            } else {
                String priority = styleDeclaration_.getPropertyPriority(name);
                existent = new StyleElement(name, value, priority, SelectorSpecificity.FROM_STYLE_ATTRIBUTE);
            }
        } else {
            existent = element == null ? null : element.getStyleElement(name);
        }

        if (localModifications_ != null) {
            final StyleElement localStyleMod = localModifications_.get(name);
            if (localStyleMod == null) {
                return existent;
            }

            if (existent == null) {
                // Local modifications represent either default style elements or style elements
                // defined in stylesheets; either way, they shouldn't overwrite any style
                // elements derived directly from the HTML element's "style" attribute.
                return localStyleMod;
            }

            // replace if !IMPORTANT
            if (StyleElement.PRIORITY_IMPORTANT.equals(localStyleMod.getPriority())) {
                if (StyleElement.PRIORITY_IMPORTANT.equals(existent.getPriority())) {
                    if (existent.getSpecificity().compareTo(localStyleMod.getSpecificity()) < 0) {
                        return localStyleMod;
                    }
                }
                else {
                    return localStyleMod;
                }
            }
        }
        return existent;
    }

    @Override
    public final String getStyleAttribute(final Definition definition) {
        return getStyleAttribute(definition, true);
    }

    @Override
    public final String getStyleAttribute(final String name) {
        return getStyleAttributeImpl(name);
    }

    @Override
    public String getStyleAttribute(final Definition style, final boolean getDefaultValueIfEmpty) {
        String value = getStyleAttributeImpl(style.getAttributeName());
        if (value.isEmpty() && getDefaultValueIfEmpty) {
            value = style.getDefaultComputedValue(getBrowserVersion());
        }
        return value;
    }

    private String getStyleAttributeImpl(final String string) {
//        if (styleDeclaration_ != null) {
//            return styleDeclaration_.getPropertyValue(string);
//        }
        final StyleElement element = getStyleElement(string);
        if (element != null && element.getValue() != null) {
            final String value = element.getValue();
            if (!value.contains("url")) {
                return value.toLowerCase(Locale.ROOT);
            }
            return value;
        }
        return "";
    }


    @Override
    public String getPropertyPriority(String name) {
//        if (styleDeclaration_ != null) {
//            return styleDeclaration_.getPropertyPriority(name);
//        }
        final StyleElement element = getStyleElement(name);
        if (element != null && element.getValue() != null) {
            return element.getPriority();
        }
        return "";
    }

    @Override
    public void setProperty(String name, String newValue, String important) {
        if (styleDeclaration_ != null) {
            styleDeclaration_.setProperty(name, newValue, important);
        }
        else {
            element.replaceStyleAttribute(name, newValue, important);
        }
    }

    @Override
    public String removeProperty(String name) {
        if (styleDeclaration_ != null) {
            return styleDeclaration_.removeProperty(name);
        }
        return element.removeStyleAttribute(name);
    }

    @Override
    public String getCssText() {
        if (styleDeclaration_ != null) {
            final String text = styleDeclaration_.getCssText();
            if (styleDeclaration_.getLength() > 0) {
                return text + ";";
            }
            return text;
        }
        return element.getAttributeDirect("style");
    }

    @Override
    public void setCssText(String value) {
        if (styleDeclaration_ != null) {
            styleDeclaration_.setCssText(value);
        }else {
            element.setAttribute("style", value);
        }
    }

    /**
     * Makes a local, "computed", modification to this CSS style.
     *
     * @param declaration the style declaration
     * @param selector the selector determining that the style applies to this element
     */
    void applyStyleFromSelector(final com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl declaration, final Selector selector) {
        final SelectorSpecificity specificity = selector.getSelectorSpecificity();
        for (final Property prop : declaration.getProperties()) {
            final String name = prop.getName();
            final String value = declaration.getPropertyValue(name);
            final String priority = declaration.getPropertyPriority(name);
            applyLocalStyleAttribute(name, value, priority, specificity);
        }
    }

    private void applyLocalStyleAttribute(final String name, final String newValue, final String priority,
        final SelectorSpecificity specificity) {
        if (!StyleElement.PRIORITY_IMPORTANT.equals(priority)) {
            final StyleElement existingElement = localModifications_.get(name);
            if (existingElement != null) {
                if (StyleElement.PRIORITY_IMPORTANT.equals(existingElement.getPriority())) {
                    return; // can't override a !important rule by a normal rule. Ignore it!
                }
                else if (specificity.compareTo(existingElement.getSpecificity()) < 0) {
                    return; // can't override a rule with a rule having higher specificity
                }
            }
        }
        final StyleElement element = new StyleElement(name, newValue, priority, specificity);
        localModifications_.put(name, element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (element == null) {
            return "CSSStyleDeclaration for 'null'"; // for instance on prototype
        }
        final String style = element.getAttributeDirect("style");
        return "CSSStyleDeclaration for '" + style + "'";
    }
}
