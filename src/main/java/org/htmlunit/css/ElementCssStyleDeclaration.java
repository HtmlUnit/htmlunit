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
package org.htmlunit.css;

import java.util.Iterator;
import java.util.Map;

import org.htmlunit.BrowserVersion;
import org.htmlunit.BrowserVersionFeatures;
import org.htmlunit.css.StyleAttributes.Definition;
import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.html.DomElement;
import org.htmlunit.util.StringUtils;

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
 * @author Lai Quang Duong
 */
public class ElementCssStyleDeclaration extends AbstractCssStyleDeclaration {

    // private static final Log LOG = LogFactory.getLog(ElementCssStyleDeclaration.class);

    /** The DomElement. */
    private final DomElement domElement_;

    /**
     * Creates an instance which backed by the given dom element.
     * @param domElement the dom element this is based on
     */
    public ElementCssStyleDeclaration(final DomElement domElement) {
        super();
        if (domElement == null) {
            throw new IllegalStateException("domElement can't be null");
        }
        domElement_ = domElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStylePriority(final String name) {
        final StyleElement element = domElement_.getStyleElement(name);
        if (element != null && element.getValue() != null) {
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
        final StyleElement element = getStyleElement(name);
        if (element != null && element.getValue() != null) {
            final String value = element.getValue();
            if (!value.contains("url")) {
                return StringUtils.toRootLowerCase(value);
            }
            return value;
        }
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleAttribute(final Definition definition, final boolean getDefaultValueIfEmpty) {
        return getStyleAttribute(definition.getAttributeName());
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
    public String item(final int index) {
        if (index < 0) {
            return "";
        }

        int i = 0;
        final Iterator<StyleElement> values = domElement_.getStyleMap().values().iterator();
        while (values.hasNext()) {
            if (index == i) {
                return values.next().getName();
            }
            values.next();
            i++;
        }
        return "";
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
    public Map<String, StyleElement> getStyleMap() {
        return domElement_.getStyleMap();
    }

    /**
     * @return the {@link DomElement} associated with this
     */
    public DomElement getDomElement() {
        return domElement_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFeature(final BrowserVersionFeatures property) {
        return domElement_.hasFeature(property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BrowserVersion getBrowserVersion() {
        return domElement_.getPage().getWebClient().getBrowserVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ElementCssStyleDeclaration for '" + getDomElement() + "'";
    }
}
