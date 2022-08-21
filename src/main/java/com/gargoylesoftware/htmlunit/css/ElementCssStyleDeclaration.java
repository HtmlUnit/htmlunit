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

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.html.DomElement;

/**
 * A css StyleDeclaration backed by a {@link DomElement}.
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
public class ElementCssStyleDeclaration extends CssStyleDeclaration {

    private static final Log LOG = LogFactory.getLog(ElementCssStyleDeclaration.class);

    /** The DomElement. */
    private DomElement domElement_;

    /**
     * Creates an instance which backed by the given dom element.
     * @param domElement the dom element this is based on
     */
    public ElementCssStyleDeclaration(final DomElement domElement) {
        domElement_ = domElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStylePriority(final String name) {
        final StyleElement element = domElement_.getStyleElement(name);
        if (element.getValue() != null) {
            return element.getPriority();
        }
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssText() {
        return domElement_.getAttributeDirect("style");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleAttribute(final String name) {
        final StyleElement element = domElement_.getStyleElement(name);
        if (element != null && element.getValue() != null) {
            final String value = element.getValue();
            if (!value.contains("url")) {
                return value.toLowerCase(Locale.ROOT);
            }
            return value;
        }
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCssText(final String value) {
        domElement_.setAttribute("style", value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleAttribute(final String name, final String newValue, final String important) {
        domElement_.replaceStyleAttribute(name, newValue, important);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeStyleAttribute(final String name) {
        return domElement_.removeStyleAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return domElement_.getStyleMap().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object item(final int index) {
        return domElement_.getStyleMap().get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractCSSRuleImpl getParentRule() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleElement getStyleElement(final String name) {
        return domElement_.getStyleElement(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleElement getStyleElementCaseInSensitive(final String name) {
        return domElement_.getStyleElementCaseInSensitive(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleAttribute(final Definition name1, final Definition name2) {
        final StyleElement element1 = getStyleElement(name1.getAttributeName());
        final StyleElement element2 = getStyleElement(name2.getAttributeName());

        if (element2 == null) {
            if (element1 == null) {
                return "";
            }
            return element1.getValue();
        }
        if (element1 != null) {
            if (element1.compareTo(element2) > 0) {
                return element1.getValue();
            }
        }
        return getStyleAttribute(name1, element2.getValue());
    }
}
