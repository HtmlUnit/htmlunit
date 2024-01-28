/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import java.util.Collections;
import java.util.Map;

import org.htmlunit.BrowserVersion;
import org.htmlunit.BrowserVersionFeatures;
import org.htmlunit.css.StyleAttributes.Definition;
import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.cssparser.dom.CSSStyleDeclarationImpl;
import org.htmlunit.cssparser.parser.selector.SelectorSpecificity;

/**
 * A css StyleDeclaration backed by a {@link CSSStyleDeclarationImpl}.
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
public class WrappedCssStyleDeclaration extends AbstractCssStyleDeclaration {

    // private static final Log LOG = LogFactory.getLog(WrappedCssStyleDeclaration.class);

    /** The wrapped CSSStyleDeclaration. */
    private final CSSStyleDeclarationImpl cssStyleDeclarationImpl_;
    private final BrowserVersion browserVersion_;

    /**
     * Creates an instance which wraps the specified style declaration implementation.
     * @param cssStyleDeclarationImpl the style declaration to wrap
     * @param browserVersion the {@link BrowserVersion}
     */
    public WrappedCssStyleDeclaration(final CSSStyleDeclarationImpl cssStyleDeclarationImpl,
                final BrowserVersion browserVersion) {
        cssStyleDeclarationImpl_ = cssStyleDeclarationImpl;
        browserVersion_ = browserVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStylePriority(final String name) {
        return cssStyleDeclarationImpl_.getPropertyPriority(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssText() {
        final String text = cssStyleDeclarationImpl_.getCssText();
        if (cssStyleDeclarationImpl_.getLength() > 0) {
            return text + ";";
        }
        return text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleAttribute(final String name) {
        return cssStyleDeclarationImpl_.getPropertyValue(name);
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
        cssStyleDeclarationImpl_.setCssText(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleAttribute(final String name, final String newValue, final String important) {
        cssStyleDeclarationImpl_.setProperty(name, newValue, important);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeStyleAttribute(final String name) {
        return cssStyleDeclarationImpl_.removeProperty(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return cssStyleDeclarationImpl_.getProperties().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object item(final int index) {
        return cssStyleDeclarationImpl_.getProperties().get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractCSSRuleImpl getParentRule() {
        return cssStyleDeclarationImpl_.getParentRule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleElement getStyleElement(final String name) {
        final String value = cssStyleDeclarationImpl_.getPropertyValue(name);
        if (value.isEmpty()) {
            return null;
        }
        final String priority = cssStyleDeclarationImpl_.getPropertyPriority(name);
        return new StyleElement(name, value, priority, SelectorSpecificity.FROM_STYLE_ATTRIBUTE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleElement getStyleElementCaseInSensitive(final String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, StyleElement> getStyleMap() {
        return Collections.emptyMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFeature(final BrowserVersionFeatures property) {
        return browserVersion_.hasFeature(property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BrowserVersion getBrowserVersion() {
        return browserVersion_;
    }
}
