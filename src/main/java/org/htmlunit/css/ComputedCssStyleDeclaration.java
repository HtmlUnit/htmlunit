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

import static org.htmlunit.BrowserVersionFeatures.CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY;
import static org.htmlunit.BrowserVersionFeatures.CSS_STYLE_PROP_FONT_DISCONNECTED_IS_EMPTY;
import static org.htmlunit.BrowserVersionFeatures.JS_CLIENTHEIGHT_INPUT_17;
import static org.htmlunit.BrowserVersionFeatures.JS_CLIENTHEIGHT_INPUT_18;
import static org.htmlunit.BrowserVersionFeatures.JS_CLIENTHEIGHT_RADIO_CHECKBOX_10;
import static org.htmlunit.BrowserVersionFeatures.JS_CLIENTWIDTH_INPUT_TEXT_143;
import static org.htmlunit.BrowserVersionFeatures.JS_CLIENTWIDTH_INPUT_TEXT_173;
import static org.htmlunit.BrowserVersionFeatures.JS_CLIENTWIDTH_RADIO_CHECKBOX_10;
import static org.htmlunit.css.CssStyleSheet.ABSOLUTE;
import static org.htmlunit.css.CssStyleSheet.AUTO;
import static org.htmlunit.css.CssStyleSheet.BLOCK;
import static org.htmlunit.css.CssStyleSheet.FIXED;
import static org.htmlunit.css.CssStyleSheet.INHERIT;
import static org.htmlunit.css.CssStyleSheet.INLINE;
import static org.htmlunit.css.CssStyleSheet.NONE;
import static org.htmlunit.css.CssStyleSheet.RELATIVE;
import static org.htmlunit.css.CssStyleSheet.SCROLL;
import static org.htmlunit.css.CssStyleSheet.STATIC;
import static org.htmlunit.css.StyleAttributes.Definition.ACCELERATOR;
import static org.htmlunit.css.StyleAttributes.Definition.AZIMUTH;
import static org.htmlunit.css.StyleAttributes.Definition.BORDER_COLLAPSE;
import static org.htmlunit.css.StyleAttributes.Definition.BORDER_SPACING;
import static org.htmlunit.css.StyleAttributes.Definition.BOTTOM;
import static org.htmlunit.css.StyleAttributes.Definition.CAPTION_SIDE;
import static org.htmlunit.css.StyleAttributes.Definition.COLOR;
import static org.htmlunit.css.StyleAttributes.Definition.CURSOR;
import static org.htmlunit.css.StyleAttributes.Definition.DIRECTION;
import static org.htmlunit.css.StyleAttributes.Definition.DISPLAY;
import static org.htmlunit.css.StyleAttributes.Definition.ELEVATION;
import static org.htmlunit.css.StyleAttributes.Definition.EMPTY_CELLS;
import static org.htmlunit.css.StyleAttributes.Definition.FONT;
import static org.htmlunit.css.StyleAttributes.Definition.FONT_FAMILY;
import static org.htmlunit.css.StyleAttributes.Definition.FONT_SIZE;
import static org.htmlunit.css.StyleAttributes.Definition.FONT_STYLE;
import static org.htmlunit.css.StyleAttributes.Definition.FONT_VARIANT;
import static org.htmlunit.css.StyleAttributes.Definition.FONT_WEIGHT;
import static org.htmlunit.css.StyleAttributes.Definition.LETTER_SPACING;
import static org.htmlunit.css.StyleAttributes.Definition.LINE_HEIGHT;
import static org.htmlunit.css.StyleAttributes.Definition.LIST_STYLE;
import static org.htmlunit.css.StyleAttributes.Definition.LIST_STYLE_IMAGE;
import static org.htmlunit.css.StyleAttributes.Definition.LIST_STYLE_POSITION;
import static org.htmlunit.css.StyleAttributes.Definition.LIST_STYLE_TYPE;
import static org.htmlunit.css.StyleAttributes.Definition.ORPHANS;
import static org.htmlunit.css.StyleAttributes.Definition.PITCH;
import static org.htmlunit.css.StyleAttributes.Definition.PITCH_RANGE;
import static org.htmlunit.css.StyleAttributes.Definition.POSITION;
import static org.htmlunit.css.StyleAttributes.Definition.QUOTES;
import static org.htmlunit.css.StyleAttributes.Definition.RICHNESS;
import static org.htmlunit.css.StyleAttributes.Definition.SPEAK;
import static org.htmlunit.css.StyleAttributes.Definition.SPEAK_HEADER;
import static org.htmlunit.css.StyleAttributes.Definition.SPEAK_NUMERAL;
import static org.htmlunit.css.StyleAttributes.Definition.SPEAK_PUNCTUATION;
import static org.htmlunit.css.StyleAttributes.Definition.SPEECH_RATE;
import static org.htmlunit.css.StyleAttributes.Definition.STRESS;
import static org.htmlunit.css.StyleAttributes.Definition.TEXT_ALIGN;
import static org.htmlunit.css.StyleAttributes.Definition.TEXT_INDENT;
import static org.htmlunit.css.StyleAttributes.Definition.TEXT_TRANSFORM;
import static org.htmlunit.css.StyleAttributes.Definition.VISIBILITY;
import static org.htmlunit.css.StyleAttributes.Definition.VOICE_FAMILY;
import static org.htmlunit.css.StyleAttributes.Definition.VOLUME;
import static org.htmlunit.css.StyleAttributes.Definition.WHITE_SPACE;
import static org.htmlunit.css.StyleAttributes.Definition.WIDOWS;
import static org.htmlunit.css.StyleAttributes.Definition.WIDTH;
import static org.htmlunit.css.StyleAttributes.Definition.WORD_SPACING;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.BrowserVersionFeatures;
import org.htmlunit.Page;
import org.htmlunit.WebWindow;
import org.htmlunit.css.StyleAttributes.Definition;
import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.cssparser.dom.CSSStyleDeclarationImpl;
import org.htmlunit.cssparser.dom.Property;
import org.htmlunit.cssparser.parser.selector.Selector;
import org.htmlunit.cssparser.parser.selector.SelectorSpecificity;
import org.htmlunit.html.BaseFrameElement;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomText;
import org.htmlunit.html.HtmlAbbreviated;
import org.htmlunit.html.HtmlAcronym;
import org.htmlunit.html.HtmlAddress;
import org.htmlunit.html.HtmlArticle;
import org.htmlunit.html.HtmlAside;
import org.htmlunit.html.HtmlBaseFont;
import org.htmlunit.html.HtmlBidirectionalIsolation;
import org.htmlunit.html.HtmlBidirectionalOverride;
import org.htmlunit.html.HtmlBig;
import org.htmlunit.html.HtmlBlink;
import org.htmlunit.html.HtmlBody;
import org.htmlunit.html.HtmlBold;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlButtonInput;
import org.htmlunit.html.HtmlCanvas;
import org.htmlunit.html.HtmlCenter;
import org.htmlunit.html.HtmlCheckBoxInput;
import org.htmlunit.html.HtmlCitation;
import org.htmlunit.html.HtmlCode;
import org.htmlunit.html.HtmlData;
import org.htmlunit.html.HtmlDefinition;
import org.htmlunit.html.HtmlDefinitionDescription;
import org.htmlunit.html.HtmlDefinitionTerm;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlEmphasis;
import org.htmlunit.html.HtmlFigure;
import org.htmlunit.html.HtmlFigureCaption;
import org.htmlunit.html.HtmlFileInput;
import org.htmlunit.html.HtmlFooter;
import org.htmlunit.html.HtmlHeader;
import org.htmlunit.html.HtmlHiddenInput;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlInlineFrame;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlItalic;
import org.htmlunit.html.HtmlKeyboard;
import org.htmlunit.html.HtmlLayer;
import org.htmlunit.html.HtmlLegend;
import org.htmlunit.html.HtmlMain;
import org.htmlunit.html.HtmlMark;
import org.htmlunit.html.HtmlNav;
import org.htmlunit.html.HtmlNoBreak;
import org.htmlunit.html.HtmlNoEmbed;
import org.htmlunit.html.HtmlNoFrames;
import org.htmlunit.html.HtmlNoLayer;
import org.htmlunit.html.HtmlNoScript;
import org.htmlunit.html.HtmlOutput;
import org.htmlunit.html.HtmlPasswordInput;
import org.htmlunit.html.HtmlPlainText;
import org.htmlunit.html.HtmlRadioButtonInput;
import org.htmlunit.html.HtmlRb;
import org.htmlunit.html.HtmlResetInput;
import org.htmlunit.html.HtmlRp;
import org.htmlunit.html.HtmlRt;
import org.htmlunit.html.HtmlRtc;
import org.htmlunit.html.HtmlRuby;
import org.htmlunit.html.HtmlS;
import org.htmlunit.html.HtmlSample;
import org.htmlunit.html.HtmlSection;
import org.htmlunit.html.HtmlSelect;
import org.htmlunit.html.HtmlSlot;
import org.htmlunit.html.HtmlSmall;
import org.htmlunit.html.HtmlSpan;
import org.htmlunit.html.HtmlStrike;
import org.htmlunit.html.HtmlStrong;
import org.htmlunit.html.HtmlSubmitInput;
import org.htmlunit.html.HtmlSubscript;
import org.htmlunit.html.HtmlSummary;
import org.htmlunit.html.HtmlSuperscript;
import org.htmlunit.html.HtmlTableCell;
import org.htmlunit.html.HtmlTableRow;
import org.htmlunit.html.HtmlTeletype;
import org.htmlunit.html.HtmlTextArea;
import org.htmlunit.html.HtmlTextInput;
import org.htmlunit.html.HtmlTime;
import org.htmlunit.html.HtmlUnderlined;
import org.htmlunit.html.HtmlUnknownElement;
import org.htmlunit.html.HtmlVariable;
import org.htmlunit.html.HtmlWordBreak;
import org.htmlunit.platform.Platform;

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
 * @author cd alexndr
 */
public class ComputedCssStyleDeclaration extends AbstractCssStyleDeclaration {

    /** The set of 'inheritable' definitions. */
    private static final Set<Definition> INHERITABLE_DEFINITIONS = EnumSet.of(
        AZIMUTH,
        BORDER_COLLAPSE,
        BORDER_SPACING,
        CAPTION_SIDE,
        COLOR,
        CURSOR,
        DIRECTION,
        ELEVATION,
        EMPTY_CELLS,
        FONT_FAMILY,
        FONT_SIZE,
        FONT_STYLE,
        FONT_VARIANT,
        FONT_WEIGHT,
        FONT,
        LETTER_SPACING,
        LINE_HEIGHT,
        LIST_STYLE_IMAGE,
        LIST_STYLE_POSITION,
        LIST_STYLE_TYPE,
        LIST_STYLE,
        ORPHANS,
        PITCH_RANGE,
        PITCH,
        QUOTES,
        RICHNESS,
        SPEAK_HEADER,
        SPEAK_NUMERAL,
        SPEAK_PUNCTUATION,
        SPEAK,
        SPEECH_RATE,
        STRESS,
        TEXT_ALIGN,
        TEXT_INDENT,
        TEXT_TRANSFORM,
        VISIBILITY,
        VOICE_FAMILY,
        VOLUME,
        WHITE_SPACE,
        WIDOWS,
        WORD_SPACING);

    /** Denotes a value which should be returned as is. */
    public static final String EMPTY_FINAL = new String("");

    /** The computed, cached width of the element to which this computed style belongs (no padding, borders, etc.). */
    private Integer width_;

    /**
     * The computed, cached height of the element to which this computed style belongs (no padding, borders, etc.),
     * taking child elements into account.
     */
    private Integer height_;

    /**
     * The computed, cached height of the element to which this computed style belongs (no padding, borders, etc.),
     * <b>not</b> taking child elements into account.
     */
    private Integer height2_;

    /** The computed, cached horizontal padding (left + right) of the element to which this computed style belongs. */
    private Integer paddingHorizontal_;

    /** The computed, cached vertical padding (top + bottom) of the element to which this computed style belongs. */
    private Integer paddingVertical_;

    /** The computed, cached horizontal border (left + right) of the element to which this computed style belongs. */
    private Integer borderHorizontal_;

    /** The computed, cached vertical border (top + bottom) of the element to which this computed style belongs. */
    private Integer borderVertical_;

    /** The computed, cached top of the element to which this computed style belongs. */
    private Integer top_;

    /**
     * Local modifications maintained here rather than in the element. We use a sorted
     * map so that results are deterministic and thus easily testable.
     */
    private final SortedMap<String, StyleElement> localModifications_ = new TreeMap<>();

    /** The wrapped CSSStyleDeclaration */
    private ElementCssStyleDeclaration elementStyleDeclaration_;

    public ComputedCssStyleDeclaration(final ElementCssStyleDeclaration styleDeclaration) {
        elementStyleDeclaration_ = styleDeclaration;
        getDomElement().setDefaults(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStylePriority(final String name) {
        return elementStyleDeclaration_.getStylePriority(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssText() {
        return elementStyleDeclaration_.getCssText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleAttribute(final String name) {
        final StyleElement element = getStyleElement(name);
        if (element != null && element.getValue() != null) {
            final String value = element.getValue();
            if (!"content".equals(name)
                    && !value.contains("url")) {
                return org.htmlunit.util.StringUtils.toRootLowerCase(value);
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
        final BrowserVersion browserVersion = getDomElement().getPage().getWebClient().getBrowserVersion();
        final boolean feature = hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY);
        final boolean isDefInheritable = INHERITABLE_DEFINITIONS.contains(definition);

        // to make the fuzzer happy the recursion was removed
        final ComputedCssStyleDeclaration[] queue = new ComputedCssStyleDeclaration[] {this};
        String value = null;
        while (queue[0] != null) {
            value = getStyleAttributeWorker(definition, getDefaultValueIfEmpty,
                        browserVersion, feature, isDefInheritable, queue);
        }

        return value;
    }

    private static String getStyleAttributeWorker(final Definition definition,
                final boolean getDefaultValueIfEmpty, final BrowserVersion browserVersion,
                final boolean feature, final boolean isDefInheritable,
                final ComputedCssStyleDeclaration[] queue) {
        final ComputedCssStyleDeclaration decl = queue[0];
        queue[0] = null;

        final DomElement domElem = decl.getDomElement();
        if (!domElem.isAttachedToPage() && feature) {
            return EMPTY_FINAL;
        }

        String value = decl.getStyleAttribute(definition.getAttributeName());
        if (value.isEmpty()) {
            final DomNode parent = domElem.getParentNode();
            if (isDefInheritable && parent instanceof DomElement) {
                final WebWindow window = domElem.getPage().getEnclosingWindow();

                queue[0] = window.getComputedStyle((DomElement) parent, null);
            }
            else if (getDefaultValueIfEmpty) {
                value = definition.getDefaultComputedValue(browserVersion);
            }
        }

        return value;
    }

    /**
     * @param toReturnIfEmptyOrDefault the value to return if empty or equals the {@code defaultValue}
     * @param defaultValue the default value of the string
     * @return the string, or {@code toReturnIfEmptyOrDefault}
     */
    private String getStyleAttribute(final Definition definition, final String toReturnIfEmptyOrDefault,
            final String defaultValue) {
        final DomElement domElement = getDomElement();

        if (!domElement.isAttachedToPage()
                && hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
            return ComputedCssStyleDeclaration.EMPTY_FINAL;
        }

        final boolean feature = hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY);
        final boolean isDefInheritable = INHERITABLE_DEFINITIONS.contains(definition);

        // to make the fuzzer happy the recursion was removed
        final BrowserVersion browserVersion = domElement.getPage().getWebClient().getBrowserVersion();
        final ComputedCssStyleDeclaration[] queue = new ComputedCssStyleDeclaration[] {this};
        String value = null;
        while (queue[0] != null) {
            value = getStyleAttributeWorker(definition, false,
                    browserVersion, feature, isDefInheritable, queue);
        }

        if (value == null || value.isEmpty() || value.equals(defaultValue)) {
            return toReturnIfEmptyOrDefault;
        }

        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCssText(final String value) {
        // read only
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleAttribute(final String name, final String newValue, final String important) {
        // read only
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeStyleAttribute(final String name) {
        // read only
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return elementStyleDeclaration_.getLength();
    }

    /**
     * @return the width
     */
    @Override
    public String getWidth() {
        if (NONE.equals(getDisplay())) {
            return AUTO;
        }

        final DomElement domElem = getDomElement();
        if (!domElem.isAttachedToPage()) {
            if (hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
                return "";
            }
            if (getStyleAttribute(WIDTH, true).isEmpty()) {
                return AUTO;
            }
        }

        final int windowWidth = domElem.getPage().getEnclosingWindow().getInnerWidth();
        return CssPixelValueConverter.pixelString(domElem, new CssPixelValueConverter.CssValue(0, windowWidth) {
            @Override
            public String get(final ComputedCssStyleDeclaration style) {
                final String value = style.getStyleAttribute(WIDTH, true);
                if (StringUtils.isEmpty(value)) {
                    final String position = getStyleAttribute(POSITION, true);
                    if (ABSOLUTE.equals(position) || FIXED.equals(position)) {
                        final String content = domElem.getVisibleText();
                        // do this only for small content
                        // at least for empty div's this is more correct
                        if (null != content && content.length() < 13) {
                            return (content.length() * 7) + "px";
                        }
                    }

                    int windowDefaultValue = getWindowDefaultValue();
                    if (domElem instanceof HtmlBody) {
                        windowDefaultValue -= 16;
                    }
                    return windowDefaultValue + "px";
                }
                else if (AUTO.equals(value)) {
                    int windowDefaultValue = getWindowDefaultValue();
                    if (domElem instanceof HtmlBody) {
                        windowDefaultValue -= 16;
                    }
                    return windowDefaultValue + "px";
                }

                return value;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object item(final int index) {
        return elementStyleDeclaration_.item(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractCSSRuleImpl getParentRule() {
        return elementStyleDeclaration_.getParentRule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleElement getStyleElement(final String name) {
        final StyleElement existent = elementStyleDeclaration_.getStyleElement(name);

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
                if (existent.isImportant()) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleElement getStyleElementCaseInSensitive(final String name) {
        return elementStyleDeclaration_.getStyleElementCaseInSensitive(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, StyleElement> getStyleMap() {
        return elementStyleDeclaration_.getStyleMap();
    }

    public DomElement getDomElement() {
        return elementStyleDeclaration_.getDomElement();
    }

    /**
     * @return the accelerator setting
     */
    @Override
    public String getAccelerator() {
        return getStyleAttribute(ACCELERATOR, true);
    }

    /**
     * @return the bottom setting
     */
    @Override
    public String getBottom() {
        return getStyleAttribute(BOTTOM, AUTO, null);
    }

    /**
     * @return the color setting
     */
    @Override
    public String getColor() {
        final String value = getStyleAttribute(COLOR, "rgb(0, 0, 0)", null);
        return CssColors.toRGBColor(value);
    }

    /**
     * @return the display setting
     */
    @Override
    public String getDisplay() {
        final DomElement domElem = getDomElement();
        if (!domElem.isAttachedToPage()) {
            if (hasFeature(CSS_STYLE_PROP_DISCONNECTED_IS_EMPTY)) {
                return "";
            }
        }

        // don't use defaultIfEmpty for performance
        // (no need to calculate the default if not empty)
        final String value = getStyleAttribute(DISPLAY.getAttributeName());
        if (StringUtils.isEmpty(value)) {
            if (domElem instanceof HtmlElement) {
                return ((HtmlElement) domElem).getDefaultStyleDisplay().value();
            }
            return "";
        }
        return value;
    }

    /**
     * @return the font setting
     */
    @Override
    public String getFont() {
        final DomElement domElem = getDomElement();
        if (domElem.isAttachedToPage()) {
            if (hasFeature(CSS_STYLE_PROP_FONT_DISCONNECTED_IS_EMPTY)) {
                return getStyleAttribute(FONT, true);
            }
        }
        return "";
    }

    /**
     * @return the font family setting
     */
    @Override
    public String getFontFamily() {
        return getStyleAttribute(FONT_FAMILY, true);
    }

    /**
     * @return the font size setting
     */
    @Override
    public String getFontSize() {
        String value = getStyleAttribute(FONT_SIZE, true);
        if (!value.isEmpty()) {
            value = CssPixelValueConverter.pixelValue(value) + "px";
        }
        return value;
    }

    /**
     * Returns the computed top (Y coordinate), relative to the node's parent's top edge.
     * @param includeMargin whether or not to take the margin into account in the calculation
     * @param includeBorder whether or not to take the border into account in the calculation
     * @param includePadding whether or not to take the padding into account in the calculation
     * @return the computed top (Y coordinate), relative to the node's parent's top edge
     */
    public int getTop(final boolean includeMargin, final boolean includeBorder, final boolean includePadding) {
        Integer cachedTop = getCachedTop();

        int top = 0;
        if (null == cachedTop) {
            final String position = getPositionWithInheritance();
            if (ABSOLUTE.equals(position) || FIXED.equals(position)) {
                top = getTopForAbsolutePositionWithInheritance();
            }
            else if (getDomElement() instanceof HtmlTableCell) {
                top = 0;
            }
            else {
                // Calculate the vertical displacement caused by *previous* siblings.
                DomNode prev = getDomElement().getPreviousSibling();
                boolean prevHadComputedTop = false;
                while (prev != null && !prevHadComputedTop) {
                    if (prev instanceof HtmlElement) {
                        final ComputedCssStyleDeclaration style =
                                prev.getPage().getEnclosingWindow().getComputedStyle((DomElement) prev, null);

                        // only previous block elements are counting
                        final String display = style.getDisplay();
                        if (isBlock(display)) {
                            int prevTop = 0;
                            final Integer eCachedTop = style.getCachedTop();
                            if (eCachedTop == null) {
                                final String prevPosition = style.getPositionWithInheritance();
                                if (ABSOLUTE.equals(prevPosition) || FIXED.equals(prevPosition)) {
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
                if (RELATIVE.equals(position)) {
                    final String t = getTopWithInheritance();
                    top += CssPixelValueConverter.pixelValue(t);
                }
            }
            cachedTop = Integer.valueOf(top);
            setCachedTop(cachedTop);
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

    /**
     * Returns the CSS {@code top} attribute, replacing inherited values with the actual parent values.
     * @return the CSS {@code top} attribute, replacing inherited values with the actual parent values
     */
    public String getTopWithInheritance() {
        String top = getTop();
        if (INHERIT.equals(top)) {
            final HtmlElement parent = (HtmlElement) getDomElement().getParentNode();
            if (parent == null) {
                top = AUTO;
            }
            else {
                final ComputedCssStyleDeclaration style =
                        parent.getPage().getEnclosingWindow().getComputedStyle(parent, null);
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
            final DomNode parent = getDomElement().getParentNode();
            if (parent == null) {
                bottom = AUTO;
            }
            else {
                final ComputedCssStyleDeclaration style =
                        parent.getPage().getEnclosingWindow().getComputedStyle((DomElement) parent, null);
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
        if ((ABSOLUTE.equals(p) || FIXED.equals(p)) && !AUTO.equals(l)) {
            // No need to calculate displacement caused by sibling nodes.
            left = CssPixelValueConverter.pixelValue(l);
        }
        else if ((ABSOLUTE.equals(p) || FIXED.equals(p)) && !AUTO.equals(r)) {
            // Need to calculate the horizontal displacement caused by *all* siblings.
            final DomNode parent = getDomElement().getParentNode();
            final int parentWidth;
            if (parent == null) {
                parentWidth = getDomElement().getPage().getEnclosingWindow().getInnerWidth();
            }
            else {
                final ComputedCssStyleDeclaration parentStyle =
                        parent.getPage().getEnclosingWindow().getComputedStyle((DomElement) parent, null);
                parentWidth = parentStyle.getCalculatedWidth(false, false);
            }
            left = parentWidth - CssPixelValueConverter.pixelValue(r);
        }
        else if (FIXED.equals(p) && !AUTO.equals(r)) {
            final DomElement e = getDomElement();
            final WebWindow win = e.getPage().getEnclosingWindow();
            final ComputedCssStyleDeclaration style = win.getComputedStyle(e, null);

            final DomNode parent = e.getParentNode();
            final int parentWidth;
            if (parent == null) {
                parentWidth = win.getInnerWidth();
            }
            else {
                final ComputedCssStyleDeclaration parentStyle = win.getComputedStyle((DomElement) parent, null);
                parentWidth = CssPixelValueConverter.pixelValue(parentStyle.getWidth())
                                - CssPixelValueConverter.pixelValue(style.getWidth());
            }
            left = parentWidth - CssPixelValueConverter.pixelValue(r);
        }
        else if (FIXED.equals(p) && AUTO.equals(l)) {
            // Fixed to the location at which the browser puts it via normal element flowing.
            final DomNode parent = getDomElement().getParentNode();
            if (parent == null) {
                left = 0;
            }
            else {
                final ComputedCssStyleDeclaration style =
                        parent.getPage().getEnclosingWindow().getComputedStyle((DomElement) parent, null);
                left = CssPixelValueConverter.pixelValue(style.getLeftWithInheritance());
            }
        }
        else if (STATIC.equals(p)) {
            // We need to calculate the horizontal displacement caused by *previous* siblings.
            left = 0;
            DomNode prev = getDomElement().getPreviousSibling();
            while (prev != null) {
                if (prev instanceof HtmlElement) {
                    final ComputedCssStyleDeclaration style =
                            prev.getPage().getEnclosingWindow().getComputedStyle((DomElement) prev, null);
                    final String d = style.getDisplay();
                    if (isBlock(d)) {
                        break;
                    }
                    else if (!NONE.equals(d)) {
                        left += style.getCalculatedWidth(true, true);
                    }
                }
                else if (prev instanceof DomText) {
                    final String content = prev.getVisibleText();
                    if (content != null) {
                        left += content.trim().length()
                                * getDomElement().getPage().getWebClient().getBrowserVersion().getPixesPerChar();
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
        String p = getStyleAttribute(Definition.POSITION, true);
        if (INHERIT.equals(p)) {
            final DomNode parent = getDomElement().getParentNode();
            if (parent == null) {
                p = STATIC;
            }
            else {
                final ComputedCssStyleDeclaration style =
                        parent.getPage().getEnclosingWindow().getComputedStyle((DomElement) parent, null);
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
            final DomNode parent = getDomElement().getParentNode();
            if (parent == null) {
                left = AUTO;
            }
            else {
                final ComputedCssStyleDeclaration style =
                        parent.getPage().getEnclosingWindow().getComputedStyle((DomElement) parent, null);
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
            final DomNode parent = getDomElement().getParentNode();
            if (parent == null) {
                right = AUTO;
            }
            else {
                final ComputedCssStyleDeclaration style =
                        parent.getPage().getEnclosingWindow().getComputedStyle((DomElement) parent, null);
                right = style.getRightWithInheritance();
            }
        }
        return right;
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
        if (!"border-box".equals(getStyleAttribute(Definition.BOX_SIZING, true))) {
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
        final Integer cachedHeight = getCachedHeight();
        if (cachedHeight != null) {
            return cachedHeight.intValue();
        }

        final DomElement element = getDomElement();

        if (element instanceof HtmlImage) {
            return setCachedHeight(((HtmlImage) element).getHeightOrDefault());
        }

        final boolean isInline = INLINE.equals(getDisplay()) && !(element instanceof HtmlInlineFrame);
        // height is ignored for inline elements
        if (isInline || super.getHeight().isEmpty()) {
            final int contentHeight = getContentHeight();
            if (contentHeight > 0) {
                return setCachedHeight(contentHeight);
            }
        }

        return setCachedHeight(getEmptyHeight());
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
        if (!"border-box".equals(getStyleAttribute(Definition.BOX_SIZING, true))) {
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
        final Integer cachedWidth = getCachedWidth();
        if (cachedWidth != null) {
            return cachedWidth.intValue();
        }

        final DomElement element = getDomElement();
        if (!element.mayBeDisplayed()) {
            return setCachedWidth(0);
        }

        final String display = getDisplay();
        if (NONE.equals(display)) {
            return setCachedWidth(0);
        }

        final int width;
        final String styleWidth = getStyleAttribute(Definition.WIDTH, true);
        final DomNode parent = element.getParentNode();

        // width is ignored for inline elements
        if ((INLINE.equals(display) || StringUtils.isEmpty(styleWidth)) && parent instanceof HtmlElement) {
            // hack: TODO find a way to specify default values for different tags
            if (element instanceof HtmlCanvas) {
                return 300;
            }

            // Width not explicitly set.
            final String cssFloat = getCssFloat();
            final String position = getStyleAttribute(Definition.POSITION, true);
            if ("right".equals(cssFloat) || "left".equals(cssFloat)
                    || ABSOLUTE.equals(position) || FIXED.equals(position)) {
                final BrowserVersion browserVersion = getDomElement().getPage().getWebClient().getBrowserVersion();
                // We're floating; simplistic approximation: text content * pixels per character.
                width = element.getVisibleText().length() * browserVersion.getPixesPerChar();
            }
            else if (BLOCK.equals(display)) {
                final int windowWidth = element.getPage().getEnclosingWindow().getInnerWidth();
                if (element instanceof HtmlBody) {
                    width = windowWidth - 16;
                }
                else {
                    // Block elements take up 100% of the parent's width.
                    width = CssPixelValueConverter.pixelValue((DomElement) parent,
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
                final BrowserVersion browserVersion = getDomElement().getPage().getWebClient().getBrowserVersion();
                // default font for buttons is a bit smaller than the body font size
                width = 10 + (int) (text.length() * browserVersion.getPixesPerChar() * 0.9);
            }
            else if (element instanceof HtmlTextInput || element instanceof HtmlPasswordInput) {
                final BrowserVersion browserVersion = getDomElement().getPage().getWebClient().getBrowserVersion();
                if (browserVersion.hasFeature(JS_CLIENTWIDTH_INPUT_TEXT_143)) {
                    return 143;
                }
                if (browserVersion.hasFeature(JS_CLIENTWIDTH_INPUT_TEXT_173)) {
                    return 173;
                }
                width = 154; // FF
            }
            else if (element instanceof HtmlRadioButtonInput || element instanceof HtmlCheckBoxInput) {
                final BrowserVersion browserVersion = getDomElement().getPage().getWebClient().getBrowserVersion();
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
                        return style.getStyleAttribute(Definition.WIDTH, true);
                    }
                });
        }

        return setCachedWidth(width);
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
        final WebWindow webWindow = element.getPage().getEnclosingWindow();
        for (final DomNode child : children) {
            if (child instanceof HtmlElement) {
                final HtmlElement e = (HtmlElement) child;
                final ComputedCssStyleDeclaration style = webWindow.getComputedStyle(e, null);
                final int w = style.getCalculatedWidth(true, true);
                width += w;
            }
            else if (child instanceof DomText) {
                final BrowserVersion browserVersion = getDomElement().getPage().getWebClient().getBrowserVersion();

                final DomNode parent = child.getParentNode();
                if (parent instanceof HtmlElement) {
                    final ComputedCssStyleDeclaration style = webWindow.getComputedStyle((DomElement) parent, null);
                    final int height = browserVersion.getFontHeight(style.getFontSize());
                    width += child.getVisibleText().length() * (int) (height / 1.8f);
                }
                else {
                    width += child.getVisibleText().length() * browserVersion.getPixesPerChar();
                }
            }
        }
        return width;
    }

    /**
     * Returns the element's calculated height taking relevant CSS into account, but <b>not</b> the element's child
     * elements.
     *
     * @return the element's calculated height taking relevant CSS into account, but <b>not</b> the element's child
     *         elements
     */
    private int getEmptyHeight() {
        final Integer cachedHeight2 = getCachedHeight2();
        if (cachedHeight2 != null) {
            return cachedHeight2.intValue();
        }

        final DomElement element = getDomElement();
        if (!element.mayBeDisplayed()) {
            return setCachedHeight2(0);
        }

        final String display = getDisplay();
        if (NONE.equals(display)) {
            return setCachedHeight2(0);
        }

        final WebWindow webWindow = element.getPage().getEnclosingWindow();
        final int windowHeight = webWindow.getInnerHeight();

        if (element instanceof HtmlBody) {
            return setCachedHeight2(windowHeight);
        }

        final boolean isInline = INLINE.equals(display) && !(element instanceof HtmlInlineFrame);
        // height is ignored for inline elements
        final boolean explicitHeightSpecified = !isInline && !super.getHeight().isEmpty();

        int defaultHeight;
        if ((element instanceof HtmlAbbreviated
                || element instanceof HtmlAcronym
                || element instanceof HtmlAddress
                || element instanceof HtmlArticle
                || element instanceof HtmlAside
                || element instanceof HtmlBaseFont
                || element instanceof HtmlBidirectionalIsolation
                || element instanceof HtmlBidirectionalOverride
                || element instanceof HtmlBig
                || element instanceof HtmlBlink
                || element instanceof HtmlBold
                || element instanceof HtmlCenter
                || element instanceof HtmlCitation
                || element instanceof HtmlCode
                || element instanceof HtmlDefinition
                || element instanceof HtmlDefinitionDescription
                || element instanceof HtmlDefinitionTerm
                || element instanceof HtmlEmphasis
                || element instanceof HtmlFigure
                || element instanceof HtmlFigureCaption
                || element instanceof HtmlFooter
                || element instanceof HtmlHeader
                || element instanceof HtmlItalic
                || element instanceof HtmlKeyboard
                || element instanceof HtmlLayer
                || element instanceof HtmlMark
                || element instanceof HtmlNav
                || element instanceof HtmlNoBreak
                || element instanceof HtmlNoEmbed
                || element instanceof HtmlNoFrames
                || element instanceof HtmlNoLayer
                || element instanceof HtmlNoScript
                || element instanceof HtmlPlainText
                || element instanceof HtmlRuby
                || element instanceof HtmlRb
                || element instanceof HtmlRp
                || element instanceof HtmlRt
                || element instanceof HtmlRtc
                || element instanceof HtmlS
                || element instanceof HtmlSample
                || element instanceof HtmlSection
                || element instanceof HtmlSmall
                || element instanceof HtmlStrike
                || element instanceof HtmlStrong
                || element instanceof HtmlSubscript
                || element instanceof HtmlSummary
                || element instanceof HtmlSuperscript
                || element instanceof HtmlTeletype
                || element instanceof HtmlUnderlined
                || element instanceof HtmlUnknownElement
                || element instanceof HtmlWordBreak
                || element instanceof HtmlMain
                || element instanceof HtmlVariable

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
                String width = getStyleAttribute(Definition.WIDTH, false);

                // maybe we are enclosed something that forces a width
                DomNode parent = getDomElement().getParentNode();
                final WebWindow win = parent.getPage().getEnclosingWindow();
                while (width.isEmpty() && parent != null) {
                    if (parent instanceof DomElement) {
                        final ComputedCssStyleDeclaration computedCss = win.getComputedStyle((DomElement) parent, null);
                        width = computedCss.getStyleAttribute(Definition.WIDTH, false);
                    }
                    parent = parent.getParentNode();
                }
                final int pixelWidth = CssPixelValueConverter.pixelValue(width);
                final String content = element.getVisibleText();

                if (pixelWidth > 0
                        && !width.isEmpty()
                        && StringUtils.isNotBlank(content)) {
                    final int lineCount = Platform.getFontUtil().countLines(content, pixelWidth, fontSize);
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
                    final DomElement elem = style.getDomElement();
                    if (elem instanceof HtmlBody) {
                        return String.valueOf(elem.getPage().getEnclosingWindow().getInnerHeight());
                    }
                    // height is ignored for inline elements
                    if (isInline) {
                        return "";
                    }
                    return style.getStyleAttribute(Definition.HEIGHT, true);
                }
            });

        if (height == 0 && !explicitHeightSpecified) {
            height = defaultHeight;
        }

        return setCachedHeight2(height);
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

        ComputedCssStyleDeclaration lastFlowing = null;
        final Set<ComputedCssStyleDeclaration> styles = new HashSet<>();

        if (node instanceof HtmlTableRow) {
            final HtmlTableRow row = (HtmlTableRow) node;
            for (final HtmlTableCell cell : row.getCellIterator()) {
                if (cell.mayBeDisplayed()) {
                    final ComputedCssStyleDeclaration style =
                            cell.getPage().getEnclosingWindow().getComputedStyle(cell, null);
                    styles.add(style);
                }
            }
        }
        else {
            for (final DomNode child : node.getChildren()) {
                if (child.mayBeDisplayed()) {
                    if (child instanceof HtmlElement) {
                        final HtmlElement e = (HtmlElement) child;
                        final ComputedCssStyleDeclaration style =
                                e.getPage().getEnclosingWindow().getComputedStyle(e, null);
                        final String position = style.getPositionWithInheritance();
                        if (STATIC.equals(position) || RELATIVE.equals(position)) {
                            lastFlowing = style;
                        }
                        else if (ABSOLUTE.equals(position) || FIXED.equals(position)) {
                            styles.add(style);
                        }
                    }
                }
            }

            if (lastFlowing != null) {
                styles.add(lastFlowing);
            }
        }

        int max = 0;
        for (final ComputedCssStyleDeclaration style : styles) {
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
        final String overflow = getStyleAttribute(Definition.OVERFLOW, true);
        if (horizontal) {
            // TODO: inherit, overflow-x
            scrollable = (element instanceof HtmlBody || SCROLL.equals(overflow) || AUTO.equals(overflow))
                && (ignoreSize || getContentWidth() > getCalculatedWidth());
        }
        else {
            // TODO: inherit, overflow-y
            scrollable = (element instanceof HtmlBody || SCROLL.equals(overflow) || AUTO.equals(overflow))
                && (ignoreSize || getContentHeight() > getEmptyHeight());
        }
        return scrollable;
    }

    private int getBorderHorizontal() {
        final Integer borderHorizontal = getCachedBorderHorizontal();
        if (borderHorizontal != null) {
            return borderHorizontal.intValue();
        }

        final int border = NONE.equals(getDisplay()) ? 0 : getBorderLeftValue() + getBorderRightValue();
        return setCachedBorderHorizontal(border);
    }

    private int getBorderVertical() {
        final Integer borderVertical = getCachedBorderVertical();
        if (borderVertical != null) {
            return borderVertical.intValue();
        }

        final int border = NONE.equals(getDisplay()) ? 0 : getBorderTopValue() + getBorderBottomValue();
        return setCachedBorderVertical(border);
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

    private int getPaddingHorizontal() {
        final Integer paddingHorizontal = getCachedPaddingHorizontal();
        if (paddingHorizontal != null) {
            return paddingHorizontal.intValue();
        }

        final int padding = NONE.equals(getDisplay()) ? 0 : getPaddingLeftValue() + getPaddingRightValue();
        return setCachedPaddingHorizontal(padding);
    }

    private int getPaddingVertical() {
        final Integer paddingVertical = getCachedPaddingVertical();
        if (paddingVertical != null) {
            return paddingVertical.intValue();
        }

        final int padding = NONE.equals(getDisplay()) ? 0 : getPaddingTopValue() + getPaddingBottomValue();
        return setCachedPaddingVertical(padding);
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

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @return the cached width
     */
    public Integer getCachedWidth() {
        return width_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @param width the new value
     * @return the param width
     */
    public int setCachedWidth(final int width) {
        width_ = Integer.valueOf(width);
        return width;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @return the cached height
     */
    public Integer getCachedHeight() {
        return height_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @param height the new value
     * @return the param height
     */
    public int setCachedHeight(final int height) {
        height_ = Integer.valueOf(height);
        return height;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @return the cached height2
     */
    public Integer getCachedHeight2() {
        return height2_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @param height the new value
     * @return the param height2
     */
    public int setCachedHeight2(final int height) {
        height2_ = Integer.valueOf(height);
        return height;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @return the cached top
     */
    public Integer getCachedTop() {
        return top_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @param top the new value
     */
    public void setCachedTop(final Integer top) {
        top_ = top;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @return the cached padding horizontal
     */
    public Integer getCachedPaddingHorizontal() {
        return paddingHorizontal_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @param paddingHorizontal the new value
     * @return the param paddingHorizontal
     */
    public int setCachedPaddingHorizontal(final int paddingHorizontal) {
        paddingHorizontal_ = Integer.valueOf(paddingHorizontal);
        return paddingHorizontal;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @return the cached padding vertical
     */
    public Integer getCachedPaddingVertical() {
        return paddingVertical_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @param paddingVertical the new value
     * @return the param paddingVertical
     */
    public int setCachedPaddingVertical(final int paddingVertical) {
        paddingVertical_ = Integer.valueOf(paddingVertical);
        return paddingVertical;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @return the cached border horizontal
     */
    public Integer getCachedBorderHorizontal() {
        return borderHorizontal_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @param borderHorizontal the new value
     * @return the param borderHorizontal
     */
    public int setCachedBorderHorizontal(final int borderHorizontal) {
        borderHorizontal_ = Integer.valueOf(borderHorizontal);
        return borderHorizontal;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @return the cached border vertical
     */
    public Integer getCachedBorderVertical() {
        return borderVertical_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * @param borderVertical the new value
     * @return the param borderVertical
     */
    public int setCachedBorderVertical(final int borderVertical) {
        borderVertical_ = Integer.valueOf(borderVertical);
        return borderVertical;
    }

    /**
     * Makes a local, "computed", modification to this CSS style.
     *
     * @param declaration the style declaration
     * @param selector the selector determining that the style applies to this element
     */
    public void applyStyleFromSelector(final CSSStyleDeclarationImpl declaration, final Selector selector) {
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
                if (existingElement.isImportant()) {
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
     * Makes a local, "computed", modification to this CSS style that won't override other
     * style attributes of the same name. This method should be used to set default values
     * for style attributes.
     *
     * @param name the name of the style attribute to set
     * @param newValue the value of the style attribute to set
     */
    public void setDefaultLocalStyleAttribute(final String name, final String newValue) {
        final StyleElement element = new StyleElement(name, newValue, "", SelectorSpecificity.DEFAULT_STYLE_ATTRIBUTE);
        localModifications_.put(name, element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFeature(final BrowserVersionFeatures property) {
        return getDomElement().hasFeature(property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isComputed() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ComputedCssStyleDeclaration for '" + getDomElement() + "'";
    }
}
