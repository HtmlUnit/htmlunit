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

import static org.htmlunit.BrowserVersionFeatures.CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY;
import static org.htmlunit.css.CssStyleSheet.AUTO;
import static org.htmlunit.css.CssStyleSheet.NONE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.corejs.javascript.ES6Iterator;
import org.htmlunit.css.ComputedCssStyleDeclaration;
import org.htmlunit.css.CssColors;
import org.htmlunit.css.CssPixelValueConverter;
import org.htmlunit.css.CssPixelValueConverter.CssValue;
import org.htmlunit.css.StyleAttributes;
import org.htmlunit.css.StyleAttributes.Definition;
import org.htmlunit.html.DomElement;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.javascript.configuration.JsxSymbolConstant;
import org.htmlunit.javascript.host.Element;
import org.htmlunit.javascript.host.html.HTMLElement;

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
     * @return the dom element to which this style belongs
     */
    private DomElement getDomElement() {
        return getCssStyleDeclaration().getDomElement();
    }

    private String defaultIfEmpty(final String str, final StyleAttributes.Definition definition) {
        return defaultIfEmpty(str, definition, false);
    }

    private String defaultIfEmpty(final String str, final StyleAttributes.Definition definition,
            final boolean isPixel) {
        if (!getDomElement().isAttachedToPage()
                && getBrowserVersion().hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
            return ComputedCssStyleDeclaration.EMPTY_FINAL;
        }
        if (str == null || str.isEmpty()) {
            return definition.getDefaultComputedValue(getBrowserVersion());
        }
        if (isPixel) {
            return pixelString(str);
        }
        return str;
    }

    /**
     * @param toReturnIfEmptyOrDefault the value to return if empty or equals the {@code defaultValue}
     * @param defaultValue the default value of the string
     * @return the string, or {@code toReturnIfEmptyOrDefault}
     */
    private String defaultIfEmpty(final String str, final String toReturnIfEmptyOrDefault, final String defaultValue) {
        if (!getDomElement().isAttachedToPage()
                && getBrowserVersion().hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
            return ComputedCssStyleDeclaration.EMPTY_FINAL;
        }
        if (str == null || str.isEmpty() || str.equals(defaultValue)) {
            return toReturnIfEmptyOrDefault;
        }
        return str;
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
        return defaultIfEmpty(super.getBackgroundAttachment(), Definition.BACKGROUND_ATTACHMENT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundColor() {
        final String value = super.getBackgroundColor();
        if (StringUtils.isEmpty(value)) {
            return Definition.BACKGROUND_COLOR.getDefaultComputedValue(getBrowserVersion());
        }
        return CssColors.toRGBColor(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundImage() {
        return defaultIfEmpty(super.getBackgroundImage(), Definition.BACKGROUND_IMAGE);
    }

    /**
     * Gets the {@code backgroundPosition} style attribute.
     * @return the style attribute
     */
    @Override
    public String getBackgroundPosition() {
        return defaultIfEmpty(super.getBackgroundPosition(), Definition.BACKGROUND_POSITION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundRepeat() {
        return defaultIfEmpty(super.getBackgroundRepeat(), Definition.BACKGROUND_REPEAT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomColor() {
        return defaultIfEmpty(super.getBorderBottomColor(), Definition.BORDER_BOTTOM_COLOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomStyle() {
        return defaultIfEmpty(super.getBorderBottomStyle(), Definition.BORDER_BOTTOM_STYLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomWidth() {
        return pixelString(defaultIfEmpty(super.getBorderBottomWidth(), Definition.BORDER_BOTTOM_WIDTH));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderLeftColor() {
        return defaultIfEmpty(super.getBorderLeftColor(), Definition.BORDER_LEFT_COLOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderLeftStyle() {
        return defaultIfEmpty(super.getBorderLeftStyle(), Definition.BORDER_LEFT_STYLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderLeftWidth() {
        return pixelString(defaultIfEmpty(super.getBorderLeftWidth(), "0px", null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderRightColor() {
        return defaultIfEmpty(super.getBorderRightColor(), "rgb(0, 0, 0)", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderRightStyle() {
        return defaultIfEmpty(super.getBorderRightStyle(), NONE, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderRightWidth() {
        return pixelString(defaultIfEmpty(super.getBorderRightWidth(), "0px", null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderTopColor() {
        return defaultIfEmpty(super.getBorderTopColor(), "rgb(0, 0, 0)", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderTopStyle() {
        return defaultIfEmpty(super.getBorderTopStyle(), NONE, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderTopWidth() {
        return pixelString(defaultIfEmpty(super.getBorderTopWidth(), "0px", null));
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
        return defaultIfEmpty(super.getCssFloat(), Definition.CSS_FLOAT);
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
        return defaultIfEmpty(super.getLineHeight(), Definition.LINE_HEIGHT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeight() {
        if (NONE.equals(getDisplay())) {
            return AUTO;
        }

        final Element elem = getElement();
        if (!elem.getDomNodeOrDie().isAttachedToPage()) {
            if (getBrowserVersion().hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
                return "";
            }
            if (getStyleAttribute(Definition.HEIGHT, true).isEmpty()) {
                return AUTO;
            }
        }
        final int windowHeight = elem.getWindow().getWebWindow().getInnerHeight();
        return CssPixelValueConverter
                .pixelString(elem.getDomNodeOrDie(), new CssPixelValueConverter.CssValue(0, windowHeight) {
                    @Override
                    public String get(final ComputedCssStyleDeclaration style) {
                        final String offsetHeight = ((HTMLElement) elem).getOffsetHeight() + "px";
                        return defaultIfEmpty(style.getStyleAttribute(Definition.HEIGHT, true), offsetHeight, AUTO);
                    }
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLeft() {
        final String superLeft = super.getLeft();
        if (!superLeft.endsWith("%")) {
            return defaultIfEmpty(superLeft, AUTO, null);
        }

        final DomElement element = getDomElement();
        return CssPixelValueConverter.pixelString(element, new CssPixelValueConverter.CssValue(0, 0) {
            @Override
            public String get(final ComputedCssStyleDeclaration style) {
                if (style.getDomElement() == element) {
                    return style.getStyleAttribute(Definition.LEFT, true);
                }
                return style.getStyleAttribute(Definition.WIDTH, true);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLetterSpacing() {
        return defaultIfEmpty(super.getLetterSpacing(), "normal", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMargin() {
        return defaultIfEmpty(super.getMargin(), Definition.MARGIN, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginBottom() {
        return pixelString(defaultIfEmpty(super.getMarginBottom(), "0px", null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginLeft() {
        return getMarginX(super.getMarginLeft(), Definition.MARGIN_LEFT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginRight() {
        return getMarginX(super.getMarginRight(), Definition.MARGIN_RIGHT);
    }

    private String getMarginX(final String superMarginX, final Definition definition) {
        if (!superMarginX.endsWith("%")) {
            return pixelString(defaultIfEmpty(superMarginX, "0px", null));
        }
        final DomElement element = getDomElement();
        if (!element.isAttachedToPage()
                && getBrowserVersion().hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
            return "";
        }

        final int windowWidth = element.getPage().getEnclosingWindow().getInnerWidth();
        return CssPixelValueConverter
                .pixelString(element, new CssPixelValueConverter.CssValue(0, windowWidth) {
                    @Override
                    public String get(final ComputedCssStyleDeclaration style) {
                        if (style.getDomElement() == element) {
                            return style.getStyleAttribute(definition, true);
                        }
                        return style.getStyleAttribute(Definition.WIDTH, true);
                    }
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginTop() {
        return pixelString(defaultIfEmpty(super.getMarginTop(), "0px", null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMaxHeight() {
        return defaultIfEmpty(super.getMaxHeight(), NONE, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMaxWidth() {
        return defaultIfEmpty(super.getMaxWidth(), NONE, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMinHeight() {
        return defaultIfEmpty(super.getMinHeight(), "0px", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMinWidth() {
        return defaultIfEmpty(super.getMinWidth(), "0px", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOpacity() {
        return defaultIfEmpty(super.getOpacity(), "1", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOutlineWidth() {
        return defaultIfEmpty(super.getOutlineWidth(), "0px", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPadding() {
        return defaultIfEmpty(super.getPadding(), Definition.PADDING, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingBottom() {
        return pixelString(defaultIfEmpty(super.getPaddingBottom(), "0px", null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingLeft() {
        return pixelString(defaultIfEmpty(super.getPaddingLeft(), "0px", null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingRight() {
        return pixelString(defaultIfEmpty(super.getPaddingRight(), "0px", null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingTop() {
        return pixelString(defaultIfEmpty(super.getPaddingTop(), "0px", null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRight() {
        return defaultIfEmpty(super.getRight(), AUTO, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextIndent() {
        return defaultIfEmpty(super.getTextIndent(), "0px", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTop() {
        final DomElement element = getDomElement();
        if (!element.isAttachedToPage()
                && getBrowserVersion().hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
            return "";
        }
        final String superTop = super.getTop();
        if (!superTop.endsWith("%")) {
            return defaultIfEmpty(superTop, Definition.TOP);
        }

        return CssPixelValueConverter.pixelString(element, new CssPixelValueConverter.CssValue(0, 0) {
            @Override
            public String get(final ComputedCssStyleDeclaration style) {
                if (style.getDomElement() == element) {
                    return style.getStyleAttribute(Definition.TOP, true);
                }
                return style.getStyleAttribute(Definition.HEIGHT, true);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVerticalAlign() {
        return defaultIfEmpty(super.getVerticalAlign(), "baseline", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWidows() {
        return defaultIfEmpty(super.getWidows(), Definition.WIDOWS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOrphans() {
        return defaultIfEmpty(super.getOrphans(), Definition.ORPHANS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPosition() {
        return defaultIfEmpty(super.getPosition(), Definition.POSITION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWordSpacing() {
        return defaultIfEmpty(super.getWordSpacing(), Definition.WORD_SPACING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getZIndex() {
        final Object response = super.getZIndex();
        if (response.toString().isEmpty()) {
            return AUTO;
        }
        return response;
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

    /**
     * Returns the specified length value as a pixel length value, as long as we're not emulating IE.
     * This method does <b>NOT</b> handle percentages correctly; use {@link #pixelValue(Element, CssValue)}
     * if you need percentage support).
     * @param value the length value to convert to a pixel length value
     * @return the specified length value as a pixel length value
     * @see #pixelString(Element, CSSStyleDeclaration.CssValue)
     */
    private static String pixelString(final String value) {
        if (ComputedCssStyleDeclaration.EMPTY_FINAL == value || value.endsWith("px")) {
            return value;
        }
        return CssPixelValueConverter.pixelValue(value) + "px";
    }

    private Element getElement() {
        return getCssStyleDeclaration().getDomElement().getScriptableObject();
    }

    private Element getParentElement() {
        return getElement().getParentElement();
    }
}
