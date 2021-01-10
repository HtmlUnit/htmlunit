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

import java.io.Serializable;
import java.util.Optional;

import com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl;
import com.gargoylesoftware.css.parser.selector.SelectorSpecificity;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.html.DomElement;

/**
 * A non-JavaScript object for {@code CSSStyleDeclaration}.
 *
 */
public interface CssStyleDeclaration extends Serializable {

    StyleElement EMPTY_FINAl = new StyleElement("", "", "", SelectorSpecificity.FROM_STYLE_ATTRIBUTE);

    /**
     * Returns an unmodifiable empty instance
     */
    static CssStyleDeclaration getEmptyUnmodifiableInstance() {
        return CssStyleDeclarationStatic.getInstance();
    }

    /**
     * Creates an instance for element styles.
     * @param element the element to which this style is bound
     */
    static ElementCssStyleDeclaration build(DomElement element) {
        return new ElementCssStyleDeclaration(element);
    }

    /**
     * Creates an instance for computed element styles.
     * @param element the element to which this style is bound
     */
    static ComputedCssStyleDeclaration buildComputed(DomElement element) {
        return new ComputedCssStyleDeclaration(build(element));
    }

    /**
     * Creates an instance which wraps the specified style declaration.
     * @param styleDeclaration the style declaration to wrap
     * @param browserVersion the browser version
     */
    static CssStyleDeclarationWrapper wrapStylesheet(CSSStyleDeclarationImpl styleDeclaration, BrowserVersion browserVersion) {
        return new CssStyleDeclarationWrapper(styleDeclaration, browserVersion);
    }


    /**
     * Get the browser version
     * @return the browser version
     */
    BrowserVersion getBrowserVersion();

    /**
     * Determines the StyleElement for the given name.
     *
     * @param name the name of the requested StyleElement
     * @return the StyleElement or null if not found
     */
    StyleElement getStyleElement(String name);

    /**
     * Get the value for the style attribute.
     * @param name the name of the style
     * @return the value
     */
    default String getStyleAttribute(String name){
        return Optional.ofNullable(getStyleElement(name))
            .map(StyleElement::getValue)
            .orElse("");
    }

    /**
     * Get the value for the style attribute or default value.
     * @param style the definition
     * @return the value or default value
     */
    default String getStyleAttribute(Definition style){
        return getStyleAttribute(style, true);
    }

    /**
     * Get the value for the style attribute.
     * @param style the definition
     * @param getDefaultValueIfEmpty whether to get the default value if empty or not
     * @return the value
     */
    default String getStyleAttribute(Definition style, boolean getDefaultValueIfEmpty){
        StyleElement styleElement = getStyleElement(style.getAttributeName());
        if(styleElement == EMPTY_FINAl)
            return ""; //prevent getting default value
        String value = Optional.ofNullable(styleElement)
                .map(StyleElement::getValue)
                .orElse("");
        if (value.isEmpty() && getDefaultValueIfEmpty) {
            value = style.getDefaultComputedValue(getBrowserVersion());
        }
        return value;
    }

    /**
     * Returns the priority of the named style attribute, or an empty string if it is not found.
     *
     * @param name the name of the style attribute whose value is to be retrieved
     * @return the named style attribute value, or an empty string if it is not found
     */
    default String getStylePriority(String name){
        final StyleElement element = getStyleElement(name);
        if (element != null && element.getValue() != null) {
            return element.getPriority();
        }
        return "";
    }

    /**
     * Sets the value of the specified property.
     *
     * @param name the name of the attribute
     * @param newValue the value to assign to the attribute
     * @param important may be null
     */
    void setStyleAttribute(String name, String newValue, String important);

    /**
     * Removes the named property.
     * @param name the name of the property to remove
     * @return the value deleted
     */
    String removeStyleAttribute(String name);

    /**
     * Returns the actual text of the style.
     * @return the actual text of the style
     */
    String getCssText();

    /**
     * Sets the actual text of the style.
     * @param value the new text
     */
    void setCssText(String value);
}
