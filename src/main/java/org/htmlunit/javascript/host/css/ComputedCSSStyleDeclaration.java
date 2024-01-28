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
package org.htmlunit.javascript.host.css;

import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.htmlunit.corejs.javascript.ES6Iterator;
import org.htmlunit.css.ComputedCssStyleDeclaration;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.javascript.configuration.JsxSymbolConstant;
import org.htmlunit.javascript.host.Element;

/**
 * An object for a CSSStyleDeclaration, which is computed.
 *
 * @see org.htmlunit.javascript.host.Window#getComputedStyle(Object, String)
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 * @author Alex Gorbatovsky
 */
@JsxClass(value = {FF, FF_ESR}, className = "CSS2Properties")
public class ComputedCSSStyleDeclaration extends CSSStyleDeclaration {

    /** Symbol.toStringTag support. */
    @JsxSymbolConstant({FF, FF_ESR})
    public static final String TO_STRING_TAG = "CSS2Properties";

    /**
     * Creates an instance.
     */
    public ComputedCSSStyleDeclaration() {
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
    }

    /**
     * Creates an instance.
     *
     * @param element the element this belongs to
     * @param cssStyleDeclaration the {@link ComputedCssStyleDeclaration} this is base on
     */
    public ComputedCSSStyleDeclaration(final Element element, final ComputedCssStyleDeclaration cssStyleDeclaration) {
        super(element, cssStyleDeclaration);
    }

    @Override
    protected ComputedCssStyleDeclaration getCssStyleDeclaration() {
        return (ComputedCssStyleDeclaration) super.getCssStyleDeclaration();
    }

    /**
     * {@inheritDoc}
     *
     * This method does nothing as the object is read-only.
     */
    @Override
    @JsxSymbol(value = {FF, FF_ESR}, symbolName = "iterator")
    public ES6Iterator values() {
        return super.values();
    }

    /**
     * {@inheritDoc}
     *
     * This method does nothing as the object is read-only.
     */
    @Override
    protected void setStyleAttribute(final String name, final String newValue) {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAccelerator() {
        return getCssStyleDeclaration().getAccelerator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundAttachment() {
        return getCssStyleDeclaration().getBackgroundAttachment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundColor() {
        return getCssStyleDeclaration().getBackgroundColor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundImage() {
        return getCssStyleDeclaration().getBackgroundImage();
    }

    /**
     * Gets the {@code backgroundPosition} style attribute.
     * @return the style attribute
     */
    @Override
    public String getBackgroundPosition() {
        return getCssStyleDeclaration().getBackgroundPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundRepeat() {
        return getCssStyleDeclaration().getBackgroundRepeat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomColor() {
        return getCssStyleDeclaration().getBorderBottomColor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomStyle() {
        return getCssStyleDeclaration().getBorderBottomStyle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomWidth() {
        return getCssStyleDeclaration().getBorderBottomWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderLeftColor() {
        return getCssStyleDeclaration().getBorderLeftColor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderLeftStyle() {
        return getCssStyleDeclaration().getBorderLeftStyle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderLeftWidth() {
        return getCssStyleDeclaration().getBorderLeftWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderRightColor() {
        return getCssStyleDeclaration().getBorderRightColor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderRightStyle() {
        return getCssStyleDeclaration().getBorderRightStyle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderRightWidth() {
        return getCssStyleDeclaration().getBorderRightWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderTopColor() {
        return getCssStyleDeclaration().getBorderTopColor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderTopStyle() {
        return getCssStyleDeclaration().getBorderTopStyle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderTopWidth() {
        return getCssStyleDeclaration().getBorderTopWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBottom() {
        return getCssStyleDeclaration().getBottom();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColor() {
        return getCssStyleDeclaration().getColor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssFloat() {
        return getCssStyleDeclaration().getCssFloat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplay() {
        return getCssStyleDeclaration().getDisplay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFont() {
        return getCssStyleDeclaration().getFont();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontFamily() {
        return getCssStyleDeclaration().getFontFamily();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontSize() {
        return getCssStyleDeclaration().getFontSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLineHeight() {
        return getCssStyleDeclaration().getLineHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeight() {
        return getCssStyleDeclaration().getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLeft() {
        return getCssStyleDeclaration().getLeft();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLetterSpacing() {
        return getCssStyleDeclaration().getLetterSpacing();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMargin() {
        return getCssStyleDeclaration().getMargin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginBottom() {
        return getCssStyleDeclaration().getMarginBottom();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginLeft() {
        return getCssStyleDeclaration().getMarginLeft();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginRight() {
        return getCssStyleDeclaration().getMarginRight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginTop() {
        return getCssStyleDeclaration().getMarginTop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMaxHeight() {
        return getCssStyleDeclaration().getMaxHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMaxWidth() {
        return getCssStyleDeclaration().getMaxWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMinHeight() {
        return getCssStyleDeclaration().getMinHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMinWidth() {
        return getCssStyleDeclaration().getMinWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOpacity() {
        return getCssStyleDeclaration().getOpacity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOutlineWidth() {
        return getCssStyleDeclaration().getOutlineWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPadding() {
        return getCssStyleDeclaration().getPadding();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingBottom() {
        return getCssStyleDeclaration().getPaddingBottom();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingLeft() {
        return getCssStyleDeclaration().getPaddingLeft();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingRight() {
        return getCssStyleDeclaration().getPaddingRight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingTop() {
        return getCssStyleDeclaration().getPaddingTop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRight() {
        return getCssStyleDeclaration().getRight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextIndent() {
        return getCssStyleDeclaration().getTextIndent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTop() {
        return getCssStyleDeclaration().getTop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVerticalAlign() {
        return getCssStyleDeclaration().getVerticalAlign();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWidows() {
        return getCssStyleDeclaration().getWidows();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOrphans() {
        return getCssStyleDeclaration().getOrphans();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPosition() {
        return getCssStyleDeclaration().getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWordSpacing() {
        return getCssStyleDeclaration().getWordSpacing();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getZIndex() {
        return getCssStyleDeclaration().getZIndex();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyValue(final String name) {
        // need to invoke the getter to take care of the default value
        final Object property = getProperty(this, org.htmlunit.util.StringUtils.cssCamelize(name));
        if (property == NOT_FOUND) {
            return super.getPropertyValue(name);
        }
        return JavaScriptEngine.toString(property);
    }
}
