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

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.javascript.host.Element;

/**
 * A css StyleDeclaration.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Rodney Gitzel
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 * @author Dennis Duysak
 * @author cd alexndr
 */
public abstract class AbstractCssStyleDeclaration implements Serializable {

    /**
     * Returns the priority of the named style attribute, or an empty string if it is not found.
     *
     * @param name the name of the style attribute whose value is to be retrieved
     * @return the named style attribute value, or an empty string if it is not found
     */
    public abstract String getStylePriority(String name);

    /**
     * Returns the actual text of the style.
     * @return the actual text of the style
     */
    public abstract String getCssText();

    /**
     * Get the value for the style attribute.
     * @param name the name
     * @return the value
     */
    public abstract String getStyleAttribute(String name);

    /**
     * Sets the actual text of the style.
     * @param value the new text
     */
    public abstract void setCssText(String value);

    /**
     * Sets the specified style attribute.
     * @param name the attribute name (camel-cased)
     * @param newValue the attribute value
     * @param important important value
     */
    public abstract void setStyleAttribute(String name, String newValue, String important);

    /**
     * Removes the specified style attribute, returning the value of the removed attribute.
     * @param name the attribute name (delimiter-separated, not camel-cased)
     * @return the removed value
     */
    public abstract String removeStyleAttribute(String name);

    /**
     * Returns the {@code length} property.
     * @return the {@code length} property
     */
    public abstract int getLength();

    /**
     * Returns the item in the given index.
     * @param index the index
     * @return the item in the given index
     */
    public abstract Object item(int index);

    /**
     * Returns the CSSRule that is the parent of this style block or <code>null</code> if this CSSStyleDeclaration is
     * not attached to a CSSRule.
     * @return the CSSRule that is the parent of this style block or <code>null</code> if this CSSStyleDeclaration is
     *      not attached to a CSSRule
     */
    public abstract AbstractCSSRuleImpl getParentRule();

    /**
     * Determines the StyleElement for the given name.
     *
     * @param name the name of the requested StyleElement
     * @return the StyleElement or null if not found
     */
    public abstract StyleElement getStyleElement(String name);

    /**
     * Determines the StyleElement for the given name.
     * This ignores the case of the name.
     *
     * @param name the name of the requested StyleElement
     * @return the StyleElement or null if not found
     */
    public abstract StyleElement getStyleElementCaseInSensitive(String name);

    /**
     * Returns a sorted map containing style elements, keyed on style element name. We use a
     * {@link LinkedHashMap} map so that results are deterministic and are thus testable.
     *
     * @return a sorted map containing style elements, keyed on style element name
     */
    public abstract Map<String, StyleElement> getStyleMap();

    /**
     * @return the element to which this style belongs
     */
    public abstract Element getElementOrNull();

    protected String getStyleAttribute(final Definition name, final String value) {
        final String[] values = StringUtils.split(value);
        if (name.name().contains("TOP")) {
            if (values.length > 0) {
                return values[0];
            }
            return "";
        }
        else if (name.name().contains("RIGHT")) {
            if (values.length > 1) {
                return values[1];
            }
            else if (values.length > 0) {
                return values[0];
            }
            return "";
        }
        else if (name.name().contains("BOTTOM")) {
            if (values.length > 2) {
                return values[2];
            }
            else if (values.length > 0) {
                return values[0];
            }
            return "";
        }
        else if (name.name().contains("LEFT")) {
            if (values.length > 3) {
                return values[3];
            }
            else if (values.length > 1) {
                return values[1];
            }
            else if (values.length > 0) {
                return values[0];
            }
            else {
                return "";
            }
        }
        else {
            throw new IllegalStateException("Unsupported definition: " + name);
        }
    }
}
