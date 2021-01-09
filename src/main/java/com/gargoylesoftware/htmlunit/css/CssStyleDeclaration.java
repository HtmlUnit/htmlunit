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

import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.html.DomElement;

/**
 * A non-JavaScript object for {@code CSSStyleDeclaration}.
 *
 */
public interface CssStyleDeclaration extends Serializable {

    /**
     * Returns an unmodifiable empty instance
     */
    static CssStyleDeclaration getEmptyUnmodifiableInstance() {
        return CssStyleDeclarationStatic.getInstance();
    }

    /**
     * Creates an instance and sets its parent scope to the one of the provided element.
     * @param parentScope the element to which this style is bound
     */
    static CssStyleDeclaration build(DomElement parentScope) {
        return new CssStyleDeclarationImpl(parentScope);
    }

    /**
     * Creates an instance which wraps the specified style declaration.
     * @param element the element to which this style is bound
     * @param styleDeclaration the style declaration to wrap
     */
    static CssStyleDeclaration build(DomElement element, com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl styleDeclaration) {
        return new CssStyleDeclarationImpl(element, styleDeclaration);
    }

    /**
     * Makes a local, "computed", modification to this CSS style that won't override other
     * style attributes of the same name. This method should be used to set default values
     * for style attributes.
     *
     * @param name the name of the style attribute to set
     * @param newValue the value of the style attribute to set
     */
    void setDefaultLocalStyleAttribute(String name, String newValue);

    /**
     * Determines the StyleElement for the given name.
     *
     * @param name the name of the requested StyleElement
     * @return the StyleElement or null if not found
     */
    StyleElement getStyleElement(String name);

    /**
     * Get the value for the style attribute.
     * @param definition the definition
     * @return the value
     */
    String getStyleAttribute(Definition definition);

    /**
     * Get the value for the style attribute.
     * @param name the name of the style
     * @return the value
     */
    String getStyleAttribute(String name);

    /**
     * Get the value for the style attribute.
     * @param style the definition
     * @param getDefaultValueIfEmpty whether to get the default value if empty or not
     * @return the value
     */
    String getStyleAttribute(Definition style, boolean getDefaultValueIfEmpty);

    /**
     * Returns the priority of the named style attribute, or an empty string if it is not found.
     *
     * @param name the name of the style attribute whose value is to be retrieved
     * @return the named style attribute value, or an empty string if it is not found
     */
    String getPropertyPriority(String name);

    /**
     * Sets the value of the specified property.
     *
     * @param name the name of the attribute
     * @param newValue the value to assign to the attribute
     * @param important may be null
     */
    void setProperty(String name, String newValue, String important);

    /**
     * Removes the named property.
     * @param name the name of the property to remove
     * @return the value deleted
     */
    String removeProperty(String name);

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
