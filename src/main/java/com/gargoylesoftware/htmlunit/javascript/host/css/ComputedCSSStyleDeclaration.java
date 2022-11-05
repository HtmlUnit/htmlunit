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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_STYLE_PROP_FONT_DISCONNECTED_IS_EMPTY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CLIENTHEIGHT_INPUT_17;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CLIENTHEIGHT_INPUT_18;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CLIENTHEIGHT_RADIO_CHECKBOX_10;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CLIENTWIDTH_INPUT_TEXT_143;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CLIENTWIDTH_INPUT_TEXT_173;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CLIENTWIDTH_RADIO_CHECKBOX_10;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.ABSOLUTE;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.AUTO;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.BLOCK;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.FIXED;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.INHERIT;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.INLINE;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.NONE;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.RELATIVE;
import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.STATIC;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.ACCELERATOR;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BACKGROUND_ATTACHMENT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BACKGROUND_COLOR;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BACKGROUND_IMAGE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BACKGROUND_POSITION;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BACKGROUND_REPEAT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BORDER_BOTTOM_COLOR;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BORDER_BOTTOM_STYLE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BORDER_BOTTOM_WIDTH;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BORDER_LEFT_COLOR;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BORDER_LEFT_STYLE;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.BOX_SIZING;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.CSS_FLOAT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.FONT_FAMILY;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.HEIGHT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.LEFT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.LINE_HEIGHT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.MARGIN;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.MARGIN_LEFT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.MARGIN_RIGHT;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.ORPHANS;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.OVERFLOW;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.PADDING;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.POSITION;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.TOP;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.WIDOWS;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.WIDTH;
import static com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition.WORD_SPACING;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.css.ComputedCssStyleDeclaration;
import com.gargoylesoftware.htmlunit.css.CssPixelValueConverter;
import com.gargoylesoftware.htmlunit.css.CssPixelValueConverter.CssValue;
import com.gargoylesoftware.htmlunit.css.StyleAttributes;
import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCanvas;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlData;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLegend;
import com.gargoylesoftware.htmlunit.html.HtmlOutput;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlResetInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSlot;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlTime;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Text;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

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
 */
@JsxClass(value = {FF, FF_ESR}, className = "CSS2Properties")
public class ComputedCSSStyleDeclaration extends CSSStyleDeclaration {

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public ComputedCSSStyleDeclaration() {
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
    // todo reduce visibility
    public ComputedCssStyleDeclaration getCssStyleDeclaration() {
        return (ComputedCssStyleDeclaration) super.getCssStyleDeclaration();
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
        return defaultIfEmpty(getStyleAttribute(ACCELERATOR, false), ACCELERATOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundAttachment() {
        return defaultIfEmpty(super.getBackgroundAttachment(), BACKGROUND_ATTACHMENT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundColor() {
        final String value = super.getBackgroundColor();
        if (StringUtils.isEmpty(value)) {
            return BACKGROUND_COLOR.getDefaultComputedValue(getBrowserVersion());
        }
        return toRGBColor(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundImage() {
        return defaultIfEmpty(super.getBackgroundImage(), BACKGROUND_IMAGE);
    }

    /**
     * Gets the {@code backgroundPosition} style attribute.
     * @return the style attribute
     */
    @Override
    public String getBackgroundPosition() {
        return defaultIfEmpty(super.getBackgroundPosition(), BACKGROUND_POSITION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundRepeat() {
        return defaultIfEmpty(super.getBackgroundRepeat(), BACKGROUND_REPEAT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomColor() {
        return defaultIfEmpty(super.getBorderBottomColor(), BORDER_BOTTOM_COLOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomStyle() {
        return defaultIfEmpty(super.getBorderBottomStyle(), BORDER_BOTTOM_STYLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomWidth() {
        return pixelString(defaultIfEmpty(super.getBorderBottomWidth(), BORDER_BOTTOM_WIDTH));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderLeftColor() {
        return defaultIfEmpty(super.getBorderLeftColor(), BORDER_LEFT_COLOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderLeftStyle() {
        return defaultIfEmpty(super.getBorderLeftStyle(), BORDER_LEFT_STYLE);
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
        return defaultIfEmpty(super.getBottom(), AUTO, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColor() {
        final String value = defaultIfEmpty(super.getColor(), "rgb(0, 0, 0)", null);
        return toRGBColor(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssFloat() {
        return defaultIfEmpty(super.getCssFloat(), CSS_FLOAT);
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
        if (getBrowserVersion().hasFeature(CSS_STYLE_PROP_FONT_DISCONNECTED_IS_EMPTY)
                && getDomElement().isAttachedToPage()) {
            return super.getFont();
        }
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontSize() {
        String value = super.getFontSize();
        if (!value.isEmpty()) {
            value = CssPixelValueConverter.pixelValue(value) + "px";
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLineHeight() {
        return defaultIfEmpty(super.getLineHeight(), LINE_HEIGHT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontFamily() {
        return defaultIfEmpty(super.getFontFamily(), FONT_FAMILY);
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
            if (getStyleAttribute(HEIGHT, true).isEmpty()) {
                return AUTO;
            }
        }
        final int windowHeight = elem.getWindow().getWebWindow().getInnerHeight();
        return CssPixelValueConverter
                .pixelString(elem.getDomNodeOrDie(), new CssPixelValueConverter.CssValue(0, windowHeight) {
                    @Override
                    public String get(final ComputedCssStyleDeclaration style) {
                        final String offsetHeight = ((HTMLElement) elem).getOffsetHeight() + "px";
                        return defaultIfEmpty(style.getStyleAttribute(HEIGHT, true), offsetHeight, AUTO);
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
                if (style.getDomElementOrNull() == element) {
                    return style.getStyleAttribute(LEFT, true);
                }
                return style.getStyleAttribute(WIDTH, true);
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
        return defaultIfEmpty(super.getMargin(), MARGIN, true);
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
        return getMarginX(super.getMarginLeft(), MARGIN_LEFT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginRight() {
        return getMarginX(super.getMarginRight(), MARGIN_RIGHT);
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
                        if (style.getDomElementOrNull() == element) {
                            return style.getStyleAttribute(definition, true);
                        }
                        return style.getStyleAttribute(WIDTH, true);
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
        return defaultIfEmpty(super.getPadding(), PADDING, true);
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
            return defaultIfEmpty(superTop, TOP);
        }

        return CssPixelValueConverter.pixelString(element, new CssPixelValueConverter.CssValue(0, 0) {
            @Override
            public String get(final ComputedCssStyleDeclaration style) {
                if (style.getDomElementOrNull() == element) {
                    return style.getStyleAttribute(TOP, true);
                }
                return style.getStyleAttribute(HEIGHT, true);
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
        return defaultIfEmpty(super.getWidows(), WIDOWS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOrphans() {
        return defaultIfEmpty(super.getOrphans(), ORPHANS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPosition() {
        return defaultIfEmpty(super.getPosition(), POSITION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWidth() {
        return getCssStyleDeclaration().getWidth();
    }

    /**
     * Returns the element's width in pixels, possibly including its padding and border.
     * @param includeBorder whether or not to include the border width in the returned value
     * @param includePadding whether or not to include the padding width in the returned value
     * @return the element's width in pixels, possibly including its padding and border
     */
    public int getCalculatedWidth(final boolean includeBorder, final boolean includePadding) {
        final DomElement element = getDomElement();

        if (!element.isAttachedToPage()) {
            return 0;
        }
        int width = getCalculatedWidth();
        if (!"border-box".equals(getStyleAttribute(BOX_SIZING))) {
            if (includeBorder) {
                width += getBorderHorizontal();
            }
            else if (isScrollable(true, true) && !(element instanceof HtmlBody)) {
                width -= 17;
            }

            if (includePadding) {
                width += getPaddingHorizontal();
            }
        }
        return width;
    }

    private int getCalculatedWidth() {
        final Integer cachedWidth = getCssStyleDeclaration().getCachedWidth();
        if (cachedWidth != null) {
            return cachedWidth.intValue();
        }

        final DomElement element = getDomElement();
        if (!element.mayBeDisplayed()) {
            return getCssStyleDeclaration().setCachedWidth(0);
        }

        final String display = getDisplay();
        if (NONE.equals(display)) {
            return getCssStyleDeclaration().setCachedWidth(0);
        }

        final int width;
        final String styleWidth = super.getWidth();
        final DomNode parent = element.getParentNode();

        // width is ignored for inline elements
        if ((INLINE.equals(display) || StringUtils.isEmpty(styleWidth)) && parent instanceof HtmlElement) {
            // hack: TODO find a way to specify default values for different tags
            if (element instanceof HtmlCanvas) {
                return 300;
            }

            // Width not explicitly set.
            final String cssFloat = getCssFloat();
            if ("right".equals(cssFloat) || "left".equals(cssFloat)
                    || ABSOLUTE.equals(getStyleAttribute(POSITION, true))) {
                // We're floating; simplistic approximation: text content * pixels per character.
                width = element.getVisibleText().length() * getBrowserVersion().getPixesPerChar();
            }
            else if (BLOCK.equals(display)) {
                final int windowWidth = element.getPage().getEnclosingWindow().getInnerWidth();
                if (element instanceof HtmlBody) {
                    width = windowWidth - 16;
                }
                else {
                    // Block elements take up 100% of the parent's width.
                    final HTMLElement parentJS = parent.getScriptableObject();
                    width = CssPixelValueConverter.pixelValue(parentJS.getDomNodeOrDie(),
                                        new CssPixelValueConverter.CssValue(0, windowWidth) {
                            @Override public String get(final ComputedCssStyleDeclaration style) {
                                return style.getWidth();
                            }
                        }) - (getBorderHorizontal() + getPaddingHorizontal());
                }
            }
            else if (element instanceof HtmlSubmitInput
                        || element instanceof HtmlResetInput
                        || element instanceof HtmlButtonInput
                        || element instanceof HtmlButton
                        || element instanceof HtmlFileInput) {
                // use asNormalizedText() here because getVisibleText() returns an empty string
                // for submit and reset buttons
                final String text = element.asNormalizedText();
                // default font for buttons is a bit smaller than the body font size
                width = 10 + (int) (text.length() * getBrowserVersion().getPixesPerChar() * 0.9);
            }
            else if (element instanceof HtmlTextInput || element instanceof HtmlPasswordInput) {
                final BrowserVersion browserVersion = getBrowserVersion();
                if (browserVersion.hasFeature(JS_CLIENTWIDTH_INPUT_TEXT_143)) {
                    return 143;
                }
                if (browserVersion.hasFeature(JS_CLIENTWIDTH_INPUT_TEXT_173)) {
                    return 173;
                }
                width = 145; // FF
            }
            else if (element instanceof HtmlRadioButtonInput || element instanceof HtmlCheckBoxInput) {
                final BrowserVersion browserVersion = getBrowserVersion();
                if (browserVersion.hasFeature(JS_CLIENTWIDTH_RADIO_CHECKBOX_10)) {
                    width = 10;
                }
                else {
                    width = 13;
                }
            }
            else if (element instanceof HtmlTextArea) {
                width = 100; // wild guess
            }
            else if (element instanceof HtmlImage) {
                width = ((HtmlImage) element).getWidthOrDefault();
            }
            else {
                // Inline elements take up however much space is required by their children.
                width = getContentWidth();
            }
        }
        else if (AUTO.equals(styleWidth)) {
            width = element.getPage().getEnclosingWindow().getInnerWidth();
        }
        else {
            // Width explicitly set in the style attribute, or there was no parent to provide guidance.
            width = CssPixelValueConverter.pixelValue(element,
                    new CssPixelValueConverter.CssValue(0, element.getPage().getEnclosingWindow().getInnerWidth()) {
                    @Override public String get(final ComputedCssStyleDeclaration style) {
                        return style.getStyleAttribute(WIDTH, true);
                    }
                });
        }

        return getCssStyleDeclaration().setCachedWidth(width);
    }

    /**
     * Returns the total width of the element's children.
     * @return the total width of the element's children
     */
    public int getContentWidth() {
        int width = 0;
        final DomElement element = getDomElement();
        Iterable<DomNode> children = element.getChildren();
        if (element instanceof BaseFrameElement) {
            final Page enclosedPage = ((BaseFrameElement) element).getEnclosedPage();
            if (enclosedPage != null && enclosedPage.isHtmlPage()) {
                children = ((DomNode) enclosedPage).getChildren();
            }
        }
        for (final DomNode child : children) {
            if (child.getScriptableObject() instanceof HTMLElement) {
                final HTMLElement e = child.getScriptableObject();
                final ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
                final int w = style.getCalculatedWidth(true, true);
                width += w;
            }
            else if (child.getScriptableObject() instanceof Text) {
                final DomNode parent = child.getParentNode();
                if (parent instanceof HtmlElement) {
                    final HTMLElement e = child.getParentNode().getScriptableObject();
                    final ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
                    final int height = getBrowserVersion().getFontHeight(style.getFontSize());
                    width += child.getVisibleText().length() * (int) (height / 1.8f);
                }
                else {
                    width += child.getVisibleText().length() * getBrowserVersion().getPixesPerChar();
                }
            }
        }
        return width;
    }

    /**
     * Returns the element's height, possibly including its padding and border.
     * @param includeBorder whether or not to include the border height in the returned value
     * @param includePadding whether or not to include the padding height in the returned value
     * @return the element's height, possibly including its padding and border
     */
    public int getCalculatedHeight(final boolean includeBorder, final boolean includePadding) {
        final DomElement element = getDomElement();

        if (!element.isAttachedToPage()) {
            return 0;
        }
        int height = getCalculatedHeight();
        if (!"border-box".equals(getStyleAttribute(BOX_SIZING))) {
            if (includeBorder) {
                height += getBorderVertical();
            }
            else if (isScrollable(false, true) && !(element instanceof HtmlBody)) {
                height -= 17;
            }

            if (includePadding) {
                height += getPaddingVertical();
            }
        }
        return height;
    }

    /**
     * Returns the element's calculated height, taking both relevant CSS and the element's children into account.
     * @return the element's calculated height, taking both relevant CSS and the element's children into account
     */
    private int getCalculatedHeight() {
        final Integer cachedHeight = getCssStyleDeclaration().getCachedHeight();
        if (cachedHeight != null) {
            return cachedHeight.intValue();
        }

        final DomElement element = getDomElement();

        if (element instanceof HtmlImage) {
            return getCssStyleDeclaration()
                    .setCachedHeight(((HtmlImage) element).getHeightOrDefault());
        }

        final boolean isInline = INLINE.equals(getDisplay()) && !(element instanceof HtmlInlineFrame);
        // height is ignored for inline elements
        if (isInline || super.getHeight().isEmpty()) {
            final int contentHeight = getContentHeight();
            if (contentHeight > 0) {
                return getCssStyleDeclaration().setCachedHeight(contentHeight);
            }
        }

        return getCssStyleDeclaration().setCachedHeight(getEmptyHeight());
    }

    /**
     * Returns the element's calculated height taking relevant CSS into account, but <b>not</b> the element's child
     * elements.
     *
     * @return the element's calculated height taking relevant CSS into account, but <b>not</b> the element's child
     *         elements
     */
    private int getEmptyHeight() {
        final Integer cachedHeight2 = getCssStyleDeclaration().getCachedHeight2();
        if (cachedHeight2 != null) {
            return cachedHeight2.intValue();
        }

        final DomElement element = getDomElement();
        if (!element.mayBeDisplayed()) {
            return getCssStyleDeclaration().setCachedHeight2(0);
        }

        final String display = getDisplay();
        if (NONE.equals(display)) {
            return getCssStyleDeclaration().setCachedHeight2(0);
        }

        final WebWindow webWindow = element.getPage().getEnclosingWindow();
        final int windowHeight = webWindow.getInnerHeight();

        if (element instanceof HtmlBody) {
            return getCssStyleDeclaration().setCachedHeight2(windowHeight);
        }

        final boolean isInline = INLINE.equals(display) && !(element instanceof HtmlInlineFrame);
        // height is ignored for inline elements
        final boolean explicitHeightSpecified = !isInline && !super.getHeight().isEmpty();

        int defaultHeight;
        if ((element.getScriptableObject().getClass() == HTMLElement.class
                || element instanceof HtmlDivision
                || element instanceof HtmlUnknownElement
                || element instanceof HtmlData
                || element instanceof HtmlTime
                || element instanceof HtmlOutput
                || element instanceof HtmlSlot
                || element instanceof HtmlLegend)
                && StringUtils.isBlank(element.getTextContent())) {
            defaultHeight = 0;
        }
        else if (element.getFirstChild() == null) {
            if (element instanceof HtmlRadioButtonInput || element instanceof HtmlCheckBoxInput) {
                if (webWindow.getWebClient().getBrowserVersion().hasFeature(JS_CLIENTHEIGHT_RADIO_CHECKBOX_10)) {
                    defaultHeight = 10;
                }
                else {
                    defaultHeight = 13;
                }
            }
            else if (element instanceof HtmlButton) {
                defaultHeight = 20;
            }
            else if (element instanceof HtmlInput && !(element instanceof HtmlHiddenInput)) {
                final BrowserVersion browser = webWindow.getWebClient().getBrowserVersion();
                if (browser.hasFeature(JS_CLIENTHEIGHT_INPUT_17)) {
                    defaultHeight = 17;
                }
                else if (browser.hasFeature(JS_CLIENTHEIGHT_INPUT_18)) {
                    defaultHeight = 18;
                }
                else {
                    defaultHeight = 20;
                }
            }
            else if (element instanceof HtmlSelect) {
                defaultHeight = 20;
            }
            else if (element instanceof HtmlTextArea) {
                defaultHeight = 49;
            }
            else if (element instanceof HtmlInlineFrame) {
                defaultHeight = 154;
            }
            else {
                defaultHeight = 0;
            }
        }
        else {
            final String fontSize = getFontSize();
            defaultHeight = webWindow.getWebClient().getBrowserVersion().getFontHeight(fontSize);

            if (element instanceof HtmlDivision
                    || element instanceof HtmlSpan) {
                String width = getStyleAttribute(WIDTH, false);

                // maybe we are enclosed something that forces a width
                Element parent = getElement().getParentElement();
                while (width.isEmpty() && parent != null) {
                    width = getWindow().getComputedStyle(parent, null).getStyleAttribute(WIDTH, false);
                    parent = parent.getParentElement();
                }
                final int pixelWidth = CssPixelValueConverter.pixelValue(width);
                final String content = element.getVisibleText();

                if (pixelWidth > 0
                        && !width.isEmpty()
                        && StringUtils.isNotBlank(content)) {
                    final String[] lines = StringUtils.split(content, '\n');
                    int lineCount = 0;
                    final int fontSizeInt = Integer.parseInt(fontSize.substring(0, fontSize.length() - 2));
                    final FontRenderContext fontRenderCtx = new FontRenderContext(null, false, true);
                    for (final String line : lines) {
                        if (StringUtils.isBlank(line)) {
                            lineCount++;
                        }
                        else {
                            // width is specified, we have to to some line breaking
                            final AttributedString attributedString = new AttributedString(line);
                            attributedString.addAttribute(TextAttribute.SIZE, fontSizeInt / 1.1);
                            final LineBreakMeasurer lineBreakMeasurer =
                                    new LineBreakMeasurer(attributedString.getIterator(), fontRenderCtx);
                            lineBreakMeasurer.nextLayout(pixelWidth);
                            lineCount++;
                            while (lineBreakMeasurer.getPosition() < line.length() && lineCount < 1000) {
                                lineBreakMeasurer.nextLayout(pixelWidth);
                                lineCount++;
                            }
                        }
                    }
                    defaultHeight *= lineCount;
                }
                else {
                    if (element instanceof HtmlSpan && StringUtils.isEmpty(content)) {
                        defaultHeight = 0;
                    }
                    else {
                        defaultHeight *= StringUtils.countMatches(content, '\n') + 1;
                    }
                }
            }
        }

        final int defaultWindowHeight = element instanceof HtmlCanvas ? 150 : windowHeight;

        int height = CssPixelValueConverter.pixelValue(element,
                new CssPixelValueConverter.CssValue(defaultHeight, defaultWindowHeight) {
                @Override public String get(final ComputedCssStyleDeclaration style) {
                    final Element element = style.getElementOrNull();
                    if (element instanceof HTMLBodyElement) {
                        return String.valueOf(element.getWindow().getWebWindow().getInnerHeight());
                    }
                    // height is ignored for inline elements
                    if (isInline) {
                        return "";
                    }
                    return style.getStyleAttribute(HEIGHT, true);
                }
            });

        if (height == 0 && !explicitHeightSpecified) {
            height = defaultHeight;
        }

        return getCssStyleDeclaration().setCachedHeight2(height);
    }

    /**
     * Returns the total height of the element's children.
     * @return the total height of the element's children
     */
    public int getContentHeight() {
        // There are two different kinds of elements that might determine the content height:
        //  - elements with position:static or position:relative (elements that flow and build on each other)
        //  - elements with position:absolute (independent elements)

        final DomNode node = getDomElement();
        if (!node.mayBeDisplayed()) {
            return 0;
        }

        ComputedCSSStyleDeclaration lastFlowing = null;
        final Set<ComputedCSSStyleDeclaration> styles = new HashSet<>();
        for (final DomNode child : node.getChildren()) {
            if (child.mayBeDisplayed()) {
                final Object scriptObj = child.getScriptableObject();
                if (scriptObj instanceof HTMLElement) {
                    final HTMLElement e = (HTMLElement) scriptObj;
                    final ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
                    final String pos = style.getPositionWithInheritance();
                    if (STATIC.equals(pos) || RELATIVE.equals(pos)) {
                        lastFlowing = style;
                    }
                    else if (ABSOLUTE.equals(pos)) {
                        styles.add(style);
                    }
                }
            }
        }

        if (lastFlowing != null) {
            styles.add(lastFlowing);
        }

        int max = 0;
        for (final ComputedCSSStyleDeclaration style : styles) {
            final int h = style.getTop(true, false, false) + style.getCalculatedHeight(true, true);
            if (h > max) {
                max = h;
            }
        }
        return max;
    }

    /**
     * Returns {@code true} if the element is scrollable along the specified axis.
     * @param horizontal if {@code true}, the caller is interested in scrollability along the x-axis;
     *        if {@code false}, the caller is interested in scrollability along the y-axis
     * @return {@code true} if the element is scrollable along the specified axis
     */
    public boolean isScrollable(final boolean horizontal) {
        return isScrollable(horizontal, false);
    }

    /**
     * @param ignoreSize whether to consider the content/calculated width/height
     */
    private boolean isScrollable(final boolean horizontal, final boolean ignoreSize) {
        final boolean scrollable;
        final DomElement element = getDomElement();
        final String overflow = getStyleAttribute(OVERFLOW, true);
        if (horizontal) {
            // TODO: inherit, overflow-x
            scrollable = (element instanceof HtmlBody || "scroll".equals(overflow) || AUTO.equals(overflow))
                && (ignoreSize || getContentWidth() > getCalculatedWidth());
        }
        else {
            // TODO: inherit, overflow-y
            scrollable = (element instanceof HtmlBody || "scroll".equals(overflow) || AUTO.equals(overflow))
                && (ignoreSize || getContentHeight() > getEmptyHeight());
        }
        return scrollable;
    }

    /**
     * Returns the computed top (Y coordinate), relative to the node's parent's top edge.
     * @param includeMargin whether or not to take the margin into account in the calculation
     * @param includeBorder whether or not to take the border into account in the calculation
     * @param includePadding whether or not to take the padding into account in the calculation
     * @return the computed top (Y coordinate), relative to the node's parent's top edge
     */
    public int getTop(final boolean includeMargin, final boolean includeBorder, final boolean includePadding) {
        Integer cachedTop = getCssStyleDeclaration().getCachedTop();

        int top = 0;
        if (null == cachedTop) {
            final String p = getPositionWithInheritance();
            if (ABSOLUTE.equals(p)) {
                top = getTopForAbsolutePositionWithInheritance();
            }
            else {
                // Calculate the vertical displacement caused by *previous* siblings.
                DomNode prev = getDomElement().getPreviousSibling();
                boolean prevHadComputedTop = false;
                while (prev != null && !prevHadComputedTop) {
                    if (prev instanceof HtmlElement) {
                        final Element e = prev.getScriptableObject();
                        final ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);

                        // only previous block elements are counting
                        final String display = style.getDisplay();
                        if (isBlock(display)) {
                            int prevTop = 0;
                            final Integer eCachedTop = style.getCssStyleDeclaration().getCachedTop();
                            if (eCachedTop == null) {
                                final String prevPosition = style.getPositionWithInheritance();
                                if (ABSOLUTE.equals(prevPosition)) {
                                    prevTop += style.getTopForAbsolutePositionWithInheritance();
                                }
                                else {
                                    if (RELATIVE.equals(prevPosition)) {
                                        final String t = style.getTopWithInheritance();
                                        prevTop += CssPixelValueConverter.pixelValue(t);
                                    }
                                }
                            }
                            else {
                                prevHadComputedTop = true;
                                prevTop += eCachedTop.intValue();
                            }
                            prevTop += style.getCalculatedHeight(true, true);
                            final int margin = CssPixelValueConverter.pixelValue(style.getMarginTop());
                            prevTop += margin;
                            top += prevTop;
                        }
                    }
                    prev = prev.getPreviousSibling();
                }
                // If the position is relative, we also need to add the specified "top" displacement.
                if (RELATIVE.equals(p)) {
                    final String t = getTopWithInheritance();
                    top += CssPixelValueConverter.pixelValue(t);
                }
            }
            cachedTop = Integer.valueOf(top);
            getCssStyleDeclaration().setCachedTop(cachedTop);
        }
        else {
            top = cachedTop.intValue();
        }

        if (includeMargin) {
            final int margin = CssPixelValueConverter.pixelValue(getMarginTop());
            top += margin;
        }

        if (includeBorder) {
            final int border = CssPixelValueConverter.pixelValue(getBorderTopWidth());
            top += border;
        }

        if (includePadding) {
            final int padding = getPaddingTopValue();
            top += padding;
        }

        return top;
    }

    private static boolean isBlock(final String display) {
        return display != null
                && !INLINE.equals(display)
                && !NONE.equals(display);
    }

    private int getTopForAbsolutePositionWithInheritance() {
        final String t = getTopWithInheritance();

        if (!AUTO.equals(t)) {
            // No need to calculate displacement caused by sibling nodes.
            return CssPixelValueConverter.pixelValue(t);
        }

        final String b = getBottomWithInheritance();
        if (!AUTO.equals(b)) {
            // Estimate the vertical displacement caused by *all* siblings.
            // This is very rough, and doesn't even take position or display types into account.
            // It also doesn't take into account the fact that the parent's height may be hardcoded in CSS.
            int top = 0;
            DomNode child = getDomElement().getParentNode().getFirstChild();
            while (child != null) {
                if (child instanceof HtmlElement && child.mayBeDisplayed()) {
                    top += 20;
                }
                child = child.getNextSibling();
            }
            top -= CssPixelValueConverter.pixelValue(b);
            return top;
        }

        return 0;
    }

    /**
     * Returns the computed left (X coordinate), relative to the node's parent's left edge.
     * @param includeMargin whether or not to take the margin into account in the calculation
     * @param includeBorder whether or not to take the border into account in the calculation
     * @param includePadding whether or not to take the padding into account in the calculation
     * @return the computed left (X coordinate), relative to the node's parent's left edge
     */
    public int getLeft(final boolean includeMargin, final boolean includeBorder, final boolean includePadding) {
        final String p = getPositionWithInheritance();
        final String l = getLeftWithInheritance();
        final String r = getRightWithInheritance();

        int left;
        if (ABSOLUTE.equals(p) && !AUTO.equals(l)) {
            // No need to calculate displacement caused by sibling nodes.
            left = CssPixelValueConverter.pixelValue(l);
        }
        else if (ABSOLUTE.equals(p) && !AUTO.equals(r)) {
            // Need to calculate the horizontal displacement caused by *all* siblings.
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            final int parentWidth;
            if (parent == null) {
                parentWidth = getWindow().getInnerWidth();
            }
            else {
                final ComputedCSSStyleDeclaration parentStyle = parent.getWindow().getComputedStyle(parent, null);
                parentWidth = parentStyle.getCalculatedWidth(false, false);
            }
            left = parentWidth - CssPixelValueConverter.pixelValue(r);
        }
        else if (FIXED.equals(p) && !AUTO.equals(r)) {
            final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(getElement(), null);

            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            final int parentWidth;
            if (parent == null) {
                parentWidth = getWindow().getInnerWidth();
            }
            else {
                final ComputedCSSStyleDeclaration parentStyle = parent.getWindow().getComputedStyle(parent, null);
                parentWidth = CssPixelValueConverter.pixelValue(parentStyle.getWidth())
                                - CssPixelValueConverter.pixelValue(style.getWidth());
            }
            left = parentWidth - CssPixelValueConverter.pixelValue(r);
        }
        else if (FIXED.equals(p) && AUTO.equals(l)) {
            // Fixed to the location at which the browser puts it via normal element flowing.
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            if (parent == null) {
                left = 0;
            }
            else {
                final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                left = CssPixelValueConverter.pixelValue(style.getLeftWithInheritance());
            }
        }
        else if (STATIC.equals(p)) {
            // We need to calculate the horizontal displacement caused by *previous* siblings.
            left = 0;
            DomNode prev = getDomElement().getPreviousSibling();
            while (prev != null) {
                final Scriptable prevScriptable = prev.getScriptableObject();
                if (prevScriptable instanceof HTMLElement) {
                    final HTMLElement e = (HTMLElement) prevScriptable;
                    final ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
                    final String d = style.getDisplay();
                    if (isBlock(d)) {
                        break;
                    }
                    else if (!NONE.equals(d)) {
                        left += style.getCalculatedWidth(true, true);
                    }
                }
                else if (prevScriptable instanceof Text) {
                    final String content = prev.getVisibleText();
                    if (content != null) {
                        left += content.trim().length() * getBrowserVersion().getPixesPerChar();
                    }
                }
                prev = prev.getPreviousSibling();
            }
        }
        else {
            // Just use the CSS specified value.
            left = CssPixelValueConverter.pixelValue(l);
        }

        if (includeMargin) {
            final int margin = getMarginLeftValue();
            left += margin;
        }

        if (includeBorder) {
            final int border = CssPixelValueConverter.pixelValue(getBorderLeftWidth());
            left += border;
        }

        if (includePadding) {
            final int padding = getPaddingLeftValue();
            left += padding;
        }

        return left;
    }

    /**
     * Returns the CSS {@code position} attribute, replacing inherited values with the actual parent values.
     * @return the CSS {@code position} attribute, replacing inherited values with the actual parent values
     */
    public String getPositionWithInheritance() {
        String p = getStyleAttribute(POSITION, true);
        if (INHERIT.equals(p)) {
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            if (parent == null) {
                p = STATIC;
            }
            else {
                final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                p = style.getPositionWithInheritance();
            }
        }
        return p;
    }

    /**
     * Returns the CSS {@code left} attribute, replacing inherited values with the actual parent values.
     * @return the CSS {@code left} attribute, replacing inherited values with the actual parent values
     */
    public String getLeftWithInheritance() {
        String left = getLeft();
        if (INHERIT.equals(left)) {
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            if (parent == null) {
                left = AUTO;
            }
            else {
                final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                left = style.getLeftWithInheritance();
            }
        }
        return left;
    }

    /**
     * Returns the CSS {@code right} attribute, replacing inherited values with the actual parent values.
     * @return the CSS {@code right} attribute, replacing inherited values with the actual parent values
     */
    public String getRightWithInheritance() {
        String right = getRight();
        if (INHERIT.equals(right)) {
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            if (parent == null) {
                right = AUTO;
            }
            else {
                final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                right = style.getRightWithInheritance();
            }
        }
        return right;
    }

    /**
     * Returns the CSS {@code top} attribute, replacing inherited values with the actual parent values.
     * @return the CSS {@code top} attribute, replacing inherited values with the actual parent values
     */
    public String getTopWithInheritance() {
        String top = getTop();
        if (INHERIT.equals(top)) {
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            if (parent == null) {
                top = AUTO;
            }
            else {
                final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                top = style.getTopWithInheritance();
            }
        }
        return top;
    }

    /**
     * Returns the CSS {@code bottom} attribute, replacing inherited values with the actual parent values.
     * @return the CSS {@code bottom} attribute, replacing inherited values with the actual parent values
     */
    public String getBottomWithInheritance() {
        String bottom = getBottom();
        if (INHERIT.equals(bottom)) {
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            if (parent == null) {
                bottom = AUTO;
            }
            else {
                final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                bottom = style.getBottomWithInheritance();
            }
        }
        return bottom;
    }

    /**
     * Gets the left margin of the element.
     * @return the value in pixels
     */
    public int getMarginLeftValue() {
        return CssPixelValueConverter.pixelValue(getMarginLeft());
    }

    /**
     * Gets the right margin of the element.
     * @return the value in pixels
     */
    public int getMarginRightValue() {
        return CssPixelValueConverter.pixelValue(getMarginRight());
    }

    /**
     * Gets the top margin of the element.
     * @return the value in pixels
     */
    public int getMarginTopValue() {
        return CssPixelValueConverter.pixelValue(getMarginTop());
    }

    /**
     * Gets the bottom margin of the element.
     * @return the value in pixels
     */
    public int getMarginBottomValue() {
        return CssPixelValueConverter.pixelValue(getMarginBottom());
    }

    /**
     * Gets the left padding of the element.
     * @return the value in pixels
     */
    public int getPaddingLeftValue() {
        return CssPixelValueConverter.pixelValue(getPaddingLeft());
    }

    /**
     * Gets the right padding of the element.
     * @return the value in pixels
     */
    public int getPaddingRightValue() {
        return CssPixelValueConverter.pixelValue(getPaddingRight());
    }

    /**
     * Gets the top padding of the element.
     * @return the value in pixels
     */
    public int getPaddingTopValue() {
        return CssPixelValueConverter.pixelValue(getPaddingTop());
    }

    /**
     * Gets the bottom padding of the element.
     * @return the value in pixels
     */
    public int getPaddingBottomValue() {
        return CssPixelValueConverter.pixelValue(getPaddingBottom());
    }

    private int getPaddingHorizontal() {
        final Integer paddingHorizontal = getCssStyleDeclaration().getCachedPaddingHorizontal();
        if (paddingHorizontal != null) {
            return paddingHorizontal.intValue();
        }

        final int padding = NONE.equals(getDisplay()) ? 0 : getPaddingLeftValue() + getPaddingRightValue();
        return getCssStyleDeclaration().setCachedPaddingHorizontal(padding);
    }

    private int getPaddingVertical() {
        final Integer paddingVertical = getCssStyleDeclaration().getCachedPaddingVertical();
        if (paddingVertical != null) {
            return paddingVertical.intValue();
        }

        final int padding = NONE.equals(getDisplay()) ? 0 : getPaddingTopValue() + getPaddingBottomValue();
        return getCssStyleDeclaration().setCachedPaddingVertical(padding);
    }

    /**
     * Gets the size of the left border of the element.
     * @return the value in pixels
     */
    public int getBorderLeftValue() {
        return CssPixelValueConverter.pixelValue(getBorderLeftWidth());
    }

    /**
     * Gets the size of the right border of the element.
     * @return the value in pixels
     */
    public int getBorderRightValue() {
        return CssPixelValueConverter.pixelValue(getBorderRightWidth());
    }

    /**
     * Gets the size of the top border of the element.
     * @return the value in pixels
     */
    public int getBorderTopValue() {
        return CssPixelValueConverter.pixelValue(getBorderTopWidth());
    }

    /**
     * Gets the size of the bottom border of the element.
     * @return the value in pixels
     */
    public int getBorderBottomValue() {
        return CssPixelValueConverter.pixelValue(getBorderBottomWidth());
    }

    private int getBorderHorizontal() {
        final Integer borderHorizontal = getCssStyleDeclaration().getCachedBorderHorizontal();
        if (borderHorizontal != null) {
            return borderHorizontal.intValue();
        }

        final int border = NONE.equals(getDisplay()) ? 0 : getBorderLeftValue() + getBorderRightValue();
        return getCssStyleDeclaration().setCachedBorderHorizontal(border);
    }

    private int getBorderVertical() {
        final Integer borderVertical = getCssStyleDeclaration().getCachedBorderVertical();
        if (borderVertical != null) {
            return borderVertical.intValue();
        }

        final int border = NONE.equals(getDisplay()) ? 0 : getBorderTopValue() + getBorderBottomValue();
        return getCssStyleDeclaration().setCachedBorderVertical(border);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWordSpacing() {
        return defaultIfEmpty(super.getWordSpacing(), WORD_SPACING);
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
        final Object property = getProperty(this, com.gargoylesoftware.htmlunit.util.StringUtils.cssCamelize(name));
        if (property == NOT_FOUND) {
            return super.getPropertyValue(name);
        }
        return Context.toString(property);
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
}
