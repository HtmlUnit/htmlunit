/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CAN_INHERIT_CSS_PROPERTY_VALUES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_DEFAULT_ELEMENT_HEIGHT_15;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_DEFAULT_ELEMENT_HEIGHT_MARKS_MIN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_DEFAULT_WIDTH_AUTO;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_FONT_STRECH_DEFAULT_NORMAL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_TEXT_SHADOW_DEFAULT_NONE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_GET_BACKGROUND_COLOR_FOR_COMPUTED_STYLE_AS_RGB;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_LENGTH_WITHOUT_PX;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.TREATS_POSITION_FIXED_LIKE_POSITION_STATIC;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.apache.commons.lang3.StringUtils;
import org.w3c.css.sac.Selector;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlResetInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Text;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for a ComputedCSSStyleDeclaration.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass
public class ComputedCSSStyleDeclaration extends CSSStyleDeclaration {

    /** The number of (horizontal) pixels to assume that each character occupies. */
    private static final int PIXELS_PER_CHAR = 10;

    /** The set of 'inheritable' attributes. */
    private static final Set<String> INHERITABLE_ATTRIBUTES = new HashSet<String>(Arrays.asList(
        "azimuth",
        "border-collapse",
        "border-spacing",
        "caption-side",
        "color",
        "cursor",
        "direction",
        "elevation",
        "empty-cells",
        "font-family",
        "font-size",
        "font-style",
        "font-variant",
        "font-weight",
        "font",
        "letter-spacing",
        "line-height",
        "list-style-image",
        "list-style-position",
        "list-style-type",
        "list-style",
        "orphans",
        "pitch-range",
        "pitch",
        "quotes",
        "richness",
        "speak-header",
        "speak-numeral",
        "speak-punctuation",
        "speak",
        "speech-rate",
        "stress",
        "text-align",
        "text-indent",
        "text-transform",
        "visibility",
        "voice-fFamily",
        "volume",
        "white-space",
        "widows",
        "word-spacing"));

    /**
     * Local modifications maintained here rather than in the element. We use a sorted
     * map so that results are deterministic and thus easily testable.
     */
    private final SortedMap<String, StyleElement> localModifications_ = new TreeMap<String, StyleElement>();

    /** The computed, cached width of the element to which this computed style belongs (no padding, borders, etc). */
    private Integer width_;

    /**
     * The computed, cached height of the element to which this computed style belongs (no padding, borders, etc),
     * taking child elements into account.
     */
    private Integer height_;

    /**
     * The computed, cached height of the element to which this computed style belongs (no padding, borders, etc),
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
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public ComputedCSSStyleDeclaration() {
        // Empty.
    }

    /**
     * @param style the original Style
     */
    public ComputedCSSStyleDeclaration(final CSSStyleDeclaration style) {
        super(style.getElement());
        getElement().setDefaults(this);
    }

    /**
     * {@inheritDoc}
     *
     * Overridden because some CSS properties are inherited from parent elements.
     */
    @Override
    protected String getStyleAttribute(final String name) {
        String s = super.getStyleAttribute(name);
        if (s.isEmpty() && isInheritable(name)) {
            final Element parent = getElement().getParentElement();
            if (parent != null) {
                s = getWindow().getComputedStyle(parent, null).getStyleAttribute(name);
            }
        }
        return s;
    }

    /**
     * Returns <tt>true</tt> if the specified CSS property is inheritable from parent elements.
     * @param name the name of the style attribute to check for inheritability
     * @return <tt>true</tt> if the specified CSS property is inheritable from parent elements
     * @see <a href="http://www.w3.org/TR/CSS21/propidx.html">CSS Property Table</a>
     */
    private boolean isInheritable(final String name) {
        return INHERITABLE_ATTRIBUTES.contains(name);
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
     * Makes a local, "computed", modification to this CSS style.
     *
     * @param declaration the style declaration
     * @param selector the selector determining that the style applies to this element
     */
    public void applyStyleFromSelector(final org.w3c.dom.css.CSSStyleDeclaration declaration, final Selector selector) {
        final SelectorSpecificity specificity = new SelectorSpecificity(selector);
        for (int k = 0; k < declaration.getLength(); k++) {
            final String name = declaration.item(k);
            final String value = declaration.getPropertyValue(name);
            final String priority = declaration.getPropertyPriority(name);
            applyLocalStyleAttribute(name, value, priority, specificity);
        }
    }

    private void applyLocalStyleAttribute(final String name, final String newValue, final String priority,
            final SelectorSpecificity specificity) {
        if (!PRIORITY_IMPORTANT.equals(priority)) {
            final StyleElement existingElement = localModifications_.get(name);
            if (existingElement != null) {
                if (PRIORITY_IMPORTANT.equals(existingElement.getPriority())) {
                    return; // can't override a !important rule by a normal rule. Ignore it!
                }
                else if (specificity.compareTo(existingElement.getSpecificity()) < 0) {
                    return; // can't override a rule with a rule having higher specificity
                }
            }
        }
        final StyleElement element = new StyleElement(name, newValue, priority, specificity, getCurrentElementIndex());
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
        final StyleElement element = new StyleElement(name, newValue);
        localModifications_.put(name, element);
    }

    @Override
    protected StyleElement getStyleElement(final String name) {
        final StyleElement existent = super.getStyleElement(name);

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
            if (PRIORITY_IMPORTANT.equals(localStyleMod.getPriority())) {
                if (PRIORITY_IMPORTANT.equals(existent.getPriority())) {
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
    public String getAccelerator() {
        return defaultIfEmpty(getStyleAttribute("accelerator"), "undefined");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundAttachment() {
        return defaultIfEmpty(super.getBackgroundAttachment(), "scroll");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundColor() {
        String value = super.getBackgroundColor();
        if (StringUtils.isEmpty(value)) {
            value = "transparent";
        }
        else if (getBrowserVersion().hasFeature(JS_GET_BACKGROUND_COLOR_FOR_COMPUTED_STYLE_AS_RGB)) {
            value = toRGBColor(value);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundImage() {
        return defaultIfEmpty(super.getBackgroundImage(), "none");
    }

    /**
     * Gets the "backgroundPosition" style attribute.
     * @return the style attribute
     */
    @Override
    public String getBackgroundPosition() {
        String bg = super.getBackgroundPosition();
        if (StringUtils.isNotBlank(bg)) {
            bg = StringUtils.replace(bg, "left", "0%");
            bg = StringUtils.replace(bg, "right", "100%");
            bg = StringUtils.replace(bg, "center", "50%");

            bg = StringUtils.replace(bg, "top", "0%");
            bg = StringUtils.replace(bg, "bottom", "100%");
        }

        return defaultIfEmpty(bg, "0% 0%");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackgroundRepeat() {
        return defaultIfEmpty(super.getBackgroundRepeat(), "repeat");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomColor() {
        return defaultIfEmpty(super.getBorderBottomColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomStyle() {
        return defaultIfEmpty(super.getBorderBottomStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderBottomWidth() {
        return pixelString(defaultIfEmpty(super.getBorderBottomWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderCollapse() {
        return defaultIfEmpty(super.getBorderCollapse(), "separate");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderLeftColor() {
        return defaultIfEmpty(super.getBorderLeftColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderLeftStyle() {
        return defaultIfEmpty(super.getBorderLeftStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderLeftWidth() {
        return pixelString(defaultIfEmpty(super.getBorderLeftWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderRightColor() {
        return defaultIfEmpty(super.getBorderRightColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderRightStyle() {
        return defaultIfEmpty(super.getBorderRightStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderRightWidth() {
        return pixelString(defaultIfEmpty(super.getBorderRightWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderSpacing() {
        return defaultIfEmpty(super.getBorderSpacing(), "0px 0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderTopColor() {
        return defaultIfEmpty(super.getBorderTopColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderTopStyle() {
        return defaultIfEmpty(super.getBorderTopStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBorderTopWidth() {
        return pixelString(defaultIfEmpty(super.getBorderTopWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBottom() {
        return defaultIfEmpty(super.getBottom(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCaptionSide() {
        return defaultIfEmpty(super.getCaptionSide(), "top");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClear() {
        return defaultIfEmpty(super.getClear(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClip() {
        return defaultIfEmpty(super.getClip(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContent() {
        return defaultIfEmpty(super.getContent(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColor() {
        String value = defaultIfEmpty(super.getColor(), "rgb(0, 0, 0)");
        if (getBrowserVersion().hasFeature(JS_GET_BACKGROUND_COLOR_FOR_COMPUTED_STYLE_AS_RGB)) {
            value = toRGBColor(value);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCounterIncrement() {
        return defaultIfEmpty(super.getCounterIncrement(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCounterReset() {
        return defaultIfEmpty(super.getCounterReset(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssFloat() {
        return defaultIfEmpty(super.getCssFloat(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCursor() {
        return defaultIfEmpty(super.getCursor(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDirection() {
        return defaultIfEmpty(super.getDirection(), "ltr");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplay() {
        // don't use defaultIfEmpty for performance
        // (no need to calculate the default if not empty)
        final String value = super.getDisplay();
        if (StringUtils.isEmpty(value)) {
            final Element elem = getElement();
            if (elem instanceof HTMLElement) {
                return ((HTMLElement) elem).getDefaultStyleDisplay();
            }
            return "";
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmptyCells() {
        return defaultIfEmpty(super.getEmptyCells(), "-moz-show-background");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontFamily() {
        return defaultIfEmpty(super.getFontFamily(), "serif");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontSize() {
        String value = super.getFontSize();
        if (value.isEmpty()) {
            final Element parent = getElement().getParentElement();
            if (parent != null) {
                final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                value = style.getFontSize();
            }
        }
        if (value.isEmpty()) {
            value = "16px";
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontSizeAdjust() {
        return defaultIfEmpty(super.getFontSizeAdjust(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontStretch() {
        String defaultStretch = "";
        if (getBrowserVersion().hasFeature(CSS_FONT_STRECH_DEFAULT_NORMAL)) {
            defaultStretch = "normal";
        }
        return defaultIfEmpty(super.getFontStretch(), defaultStretch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontStyle() {
        return defaultIfEmpty(super.getFontStyle(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontVariant() {
        return defaultIfEmpty(super.getFontVariant(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFontWeight() {
        return defaultIfEmpty(super.getFontWeight(), "400");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHeight() {
        final Element elem = getElement();
        if (!elem.getDomNodeOrDie().isDirectlyAttachedToPage()) {
            return "auto";
        }
        final int windowHeight = elem.getWindow().getWebWindow().getInnerHeight();
        return pixelString(elem, new CssValue(windowHeight) {
            @Override public String get(final ComputedCSSStyleDeclaration style) {
                return defaultIfEmpty(style.getStyleAttribute("height"), "362px");
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImeMode() {
        return defaultIfEmpty(super.getImeMode(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLeft() {
        return defaultIfEmpty(super.getLeft(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLetterSpacing() {
        return defaultIfEmpty(super.getLetterSpacing(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getListStyleImage() {
        return defaultIfEmpty(super.getListStyleImage(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getListStylePosition() {
        return defaultIfEmpty(super.getListStylePosition(), "outside");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getListStyleType() {
        return defaultIfEmpty(super.getListStyleType(), "disc");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginBottom() {
        return pixelString(defaultIfEmpty(super.getMarginBottom(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginLeft() {
        return pixelString(defaultIfEmpty(super.getMarginLeft(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginRight() {
        return pixelString(defaultIfEmpty(super.getMarginRight(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarginTop() {
        return pixelString(defaultIfEmpty(super.getMarginTop(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMarkerOffset() {
        return defaultIfEmpty(super.getMarkerOffset(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMaxHeight() {
        return defaultIfEmpty(super.getMaxHeight(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMaxWidth() {
        return defaultIfEmpty(super.getMaxWidth(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMinHeight() {
        return defaultIfEmpty(super.getMinHeight(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMinWidth() {
        return defaultIfEmpty(super.getMinWidth(), "0px");
    }

    @Override
    protected String getStyleAttributeValue(final Definition style) {
        // don't use defaultIfEmpty for performance
        // (no need to calculate the default if not empty)
        final String value = super.getStyleAttributeValue(style);
        if (StringUtils.isEmpty(value)) {
            return style.getDefaultComputedValue(getBrowserVersion());
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOpacity() {
        return defaultIfEmpty(super.getOpacity(), "1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOutlineColor() {
        return defaultIfEmpty(super.getOutlineColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOutlineOffset() {
        return defaultIfEmpty(super.getOutlineOffset(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOutlineStyle() {
        return defaultIfEmpty(super.getOutlineStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOutlineWidth() {
        return defaultIfEmpty(super.getOutlineWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOverflow() {
        return defaultIfEmpty(super.getOverflow(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOverflowX() {
        return defaultIfEmpty(super.getOverflowX(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOverflowY() {
        return defaultIfEmpty(super.getOverflowY(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPageBreakAfter() {
        return defaultIfEmpty(super.getPageBreakAfter(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPageBreakBefore() {
        return defaultIfEmpty(super.getPageBreakBefore(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingBottom() {
        return pixelString(defaultIfEmpty(super.getPaddingBottom(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingLeft() {
        return pixelString(defaultIfEmpty(super.getPaddingLeft(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingRight() {
        return pixelString(defaultIfEmpty(super.getPaddingRight(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPaddingTop() {
        return pixelString(defaultIfEmpty(super.getPaddingTop(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPointerEvents() {
        return defaultIfEmpty(super.getPointerEvents(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPosition() {
        return defaultIfEmpty(super.getPosition(), "static");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRight() {
        return defaultIfEmpty(super.getRight(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTableLayout() {
        return defaultIfEmpty(super.getTableLayout(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextAlign() {
        return defaultIfEmpty(super.getTextAlign(), "start");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextDecoration() {
        return defaultIfEmpty(super.getTextDecoration(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextIndent() {
        return defaultIfEmpty(super.getTextIndent(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextShadow() {
        String shadow = "";
        if (getBrowserVersion().hasFeature(CSS_TEXT_SHADOW_DEFAULT_NONE)) {
            shadow = "none";
        }
        return defaultIfEmpty(super.getTextShadow(), shadow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextTransform() {
        return defaultIfEmpty(super.getTextTransform(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTop() {
        return defaultIfEmpty(super.getTop(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVerticalAlign() {
        return defaultIfEmpty(super.getVerticalAlign(), "baseline");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVisibility() {
        return defaultIfEmpty(super.getVisibility(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWhiteSpace() {
        return defaultIfEmpty(super.getWhiteSpace(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWidth() {
        if ("none".equals(getDisplay())) {
            return "auto";
        }

        final Element elem = getElement();
        if (!elem.getDomNodeOrDie().isDirectlyAttachedToPage()) {
            return "auto";
        }

        final int windowWidth = elem.getWindow().getWebWindow().getInnerWidth();
        return pixelString(elem, new CssValue(windowWidth) {
            @Override
            public String get(final ComputedCSSStyleDeclaration style) {
                final String value = style.getStyleAttribute(WIDTH);
                if (StringUtils.isEmpty(value)) {
                    if (getBrowserVersion().hasFeature(CSS_DEFAULT_WIDTH_AUTO)) {
                        return "auto";
                    }

                    if ("absolute".equals(getStyleAttribute("position"))) {
                        final String content = getDomNodeOrDie().getTextContent();
                        // do this only for small content
                        // at least for empty div's this is more correct
                        if (null != content && (content.length() < 13)) {
                            return (content.length() * 7) + "px";
                        }
                    }

                    return getWindowDefaultValue() + "px";
                }
                return value;
            }
        });
    }

    /**
     * Returns the element's width in pixels, possibly including its padding and border.
     * @param includeBorder whether or not to include the border width in the returned value
     * @param includePadding whether or not to include the padding width in the returned value
     * @return the element's width in pixels, possibly including its padding and border
     */
    public int getCalculatedWidth(final boolean includeBorder, final boolean includePadding) {
        int width = getCalculatedWidth();
        if (includeBorder) {
            width += getBorderHorizontal();
        }
        if (includePadding) {
            width += getPaddingHorizontal();
        }
        return width;
    }

    private int getCalculatedWidth() {
        if (width_ != null) {
            return width_.intValue();
        }

        final DomNode node = getElement().getDomNodeOrDie();
        if (!node.mayBeDisplayed()) {
            width_ = Integer.valueOf(0);
            return 0;
        }

        final String display = getDisplay();
        if ("none".equals(display)) {
            width_ = Integer.valueOf(0);
            return 0;
        }

        final int windowWidth = getElement().getWindow().getWebWindow().getInnerWidth();

        int width;
        final String styleWidth = super.getWidth();
        final DomNode parent = node.getParentNode();
        if (StringUtils.isEmpty(styleWidth) && parent instanceof HtmlElement) {
            // hack: TODO find a way to specify default values for different tags
            if (getElement() instanceof HTMLCanvasElement) {
                return 300;
            }

            // Width not explicitly set.
            final String cssFloat = getCssFloat();
            if ("right".equals(cssFloat) || "left".equals(cssFloat)) {
                // We're floating; simplistic approximation: text content * pixels per character.
                width = node.getTextContent().length() * PIXELS_PER_CHAR;
            }
            else if ("block".equals(display)) {
                // Block elements take up 100% of the parent's width.
                final HTMLElement parentJS = (HTMLElement) parent.getScriptObject();
                final String parentWidth = getWindow().getComputedStyle(parentJS, null).getWidth();
                if (getBrowserVersion().hasFeature(CSS_DEFAULT_WIDTH_AUTO)
                        && "auto".equals(parentWidth)) {
                    width = windowWidth;
                }
                else {
                    width = pixelValue(parentJS, new CssValue(windowWidth) {
                        @Override public String get(final ComputedCSSStyleDeclaration style) {
                            return style.getWidth();
                        }
                    });
                }
                width -= (getBorderHorizontal() + getPaddingHorizontal());
            }
            else if (node instanceof HtmlSubmitInput || node instanceof HtmlResetInput
                        || node instanceof HtmlButtonInput || node instanceof HtmlButton) {
                final String text = node.asText();
                width = 10 + (text.length() * PIXELS_PER_CHAR);
            }
            else if (node instanceof HtmlTextInput || node instanceof HtmlPasswordInput) {
                width = 50; // wild guess
            }
            else if (node instanceof HtmlRadioButtonInput || node instanceof HtmlCheckBoxInput) {
                width = 20; // wild guess
            }
            else if (node instanceof HtmlTextInput || node instanceof HtmlPasswordInput) {
                width = 50; // wild guess
            }
            else if (node instanceof HtmlTextArea) {
                width = 100; // wild guess
            }
            else {
                // Inline elements take up however much space is required by their children.
                width = getContentWidth();
            }
        }
        else {
            // Width explicitly set in the style attribute, or there was no parent to provide guidance.
            width = pixelValue(getElement(), new CssValue(windowWidth) {
                @Override public String get(final ComputedCSSStyleDeclaration style) {
                    return style.getStyleAttribute(WIDTH);
                }
            });
        }

        width_ = Integer.valueOf(width);
        return width;
    }

    /**
     * Returns the total width of the element's children.
     * @return the total width of the element's children
     */
    public int getContentWidth() {
        int width = 0;
        final DomNode domNode = getDomNodeOrDie();
        Iterable<DomNode> childs = domNode.getChildren();
        if (domNode instanceof BaseFrameElement) {
            final Page enclosedPage = ((BaseFrameElement) domNode).getEnclosedPage();
            if (enclosedPage != null && enclosedPage.isHtmlPage()) {
                final HtmlPage htmlPage = (HtmlPage) enclosedPage;
                childs = htmlPage.getChildren();
            }
        }
        for (final DomNode child : childs) {
            if (child.getScriptObject() instanceof HTMLElement) {
                final HTMLElement e = (HTMLElement) child.getScriptObject();
                final ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
                final int w = style.getCalculatedWidth(true, true);
                width += w;
            }
            else if (child.getScriptObject() instanceof Text) {
                width += child.getTextContent().length() * PIXELS_PER_CHAR;
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
        int height = getCalculatedHeight();
        if (includeBorder) {
            height += getBorderVertical();
        }
        if (includePadding) {
            height += getPaddingVertical();
        }
        return height;
    }

    /**
     * Returns the element's calculated height, taking both relevant CSS and the element's children into account.
     * @return the element's calculated height, taking both relevant CSS and the element's children into account
     */
    private int getCalculatedHeight() {
        if (height_ != null) {
            return height_.intValue();
        }

        final int elementHeight = getEmptyHeight();
        if (elementHeight == 0) {
            height_ = Integer.valueOf(elementHeight);
            return elementHeight;
        }

        final int contentHeight = getContentHeight();
        final boolean useDefaultHeight = getBrowserVersion().hasFeature(CSS_DEFAULT_ELEMENT_HEIGHT_MARKS_MIN);
        final boolean explicitHeightSpecified = super.getHeight().length() > 0;

        int height;
        if (contentHeight > 0
                && ((useDefaultHeight && contentHeight > elementHeight)
                        || (!useDefaultHeight && !explicitHeightSpecified))) {
            height = contentHeight;
        }
        else {
            height = elementHeight;
        }

        height_ = Integer.valueOf(height);
        return height;
    }

    /**
     * Returns the element's calculated height taking relevant CSS into account, but <b>not</b> the element's child
     * elements.
     *
     * @return the element's calculated height taking relevant CSS into account, but <b>not</b> the element's child
     *         elements
     */
    private int getEmptyHeight() {
        if (height2_ != null) {
            return height2_.intValue();
        }

        final DomNode node = getElement().getDomNodeOrDie();
        if (!node.mayBeDisplayed()) {
            height2_ = Integer.valueOf(0);
            return 0;
        }

        if ("none".equals(getDisplay())) {
            height2_ = Integer.valueOf(0);
            return 0;
        }

        final int windowHeight = getElement().getWindow().getWebWindow().getInnerHeight();

        if (getElement() instanceof HTMLBodyElement) {
            height2_ = windowHeight;
            return windowHeight;
        }

        final boolean explicitHeightSpecified = super.getHeight().length() > 0;

        final int defaultHeight;
        if (getElement().getFirstChild() == null) {
            if (node instanceof HtmlButton
                    || (node instanceof HtmlInput && !(node instanceof HtmlHiddenInput))) {
                defaultHeight = 20;
            }
            else if (node instanceof HtmlSelect) {
                defaultHeight = 20;
            }
            else if (node instanceof HtmlTextArea) {
                defaultHeight = 49;
            }
            else if (node instanceof HtmlInlineFrame) {
                defaultHeight = 154;
            }
            else {
                defaultHeight = 0;
            }
        }
        else if (getBrowserVersion().hasFeature(CSS_DEFAULT_ELEMENT_HEIGHT_15)) {
            defaultHeight = 15;
        }
        else {
            defaultHeight = 20;
        }

        final int defaultValue = getElement() instanceof HTMLCanvasElement ? 150 : windowHeight;

        int height = pixelValue(getElement(), new CssValue(defaultValue) {
            @Override public String get(final ComputedCSSStyleDeclaration style) {
                return style.getStyleAttribute("height");
            }
        });

        final boolean useDefaultHeight = getBrowserVersion().hasFeature(CSS_DEFAULT_ELEMENT_HEIGHT_MARKS_MIN);
        if (height == 0 && !explicitHeightSpecified || (useDefaultHeight && height < defaultHeight)) {
            height = defaultHeight;
        }

        height2_ = Integer.valueOf(height);
        return height;
    }

    /**
     * Returns the total height of the element's children.
     * @return the total height of the element's children
     */
    public int getContentHeight() {
        // There are two different kinds of elements that might determine the content height:
        //  - elements with position:static or position:relative (elements that flow and build on each other)
        //  - elements with position:absolute (independent elements)

        final DomNode node = getElement().getDomNodeOrDie();
        if (!node.mayBeDisplayed()) {
            return 0;
        }

        ComputedCSSStyleDeclaration lastFlowing = null;
        final Set<ComputedCSSStyleDeclaration> styles = new HashSet<ComputedCSSStyleDeclaration>();
        for (final DomNode child : node.getChildren()) {
            if (child.mayBeDisplayed()) {
                final ScriptableObject scriptObj = child.getScriptObject();
                if (scriptObj instanceof HTMLElement) {
                    final HTMLElement e = (HTMLElement) scriptObj;
                    final ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
                    final String pos = style.getPositionWithInheritance();
                    if ("static".equals(pos) || "relative".equals(pos)) {
                        lastFlowing = style;
                    }
                    else if ("absolute".equals(pos)) {
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
     * Returns <tt>true</tt> if the element is scrollable along the specified axis.
     * @param horizontal if <tt>true</tt>, the caller is interested in scrollability along the x-axis;
     *        if <tt>false</tt>, the caller is interested in scrollability along the y-axis
     * @return <tt>true</tt> if the element is scrollable along the specified axis
     */
    public boolean isScrollable(final boolean horizontal) {
        final boolean scrollable;
        final Element node = getElement();
        final String overflow = getOverflow();
        if (horizontal) {
            // TODO: inherit, overflow-x
            scrollable = (node instanceof HTMLBodyElement || "scroll".equals(overflow) || "auto".equals(overflow))
                && getContentWidth() > getCalculatedWidth();
        }
        else {
            // TODO: inherit, overflow-y
            scrollable = (node instanceof HTMLBodyElement || "scroll".equals(overflow) || "auto".equals(overflow))
                && getContentHeight() > getEmptyHeight();
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
        int top = 0;
        if (null == top_) {
            final String p = getPositionWithInheritance();
            if ("absolute".equals(p)) {
                final String t = getTopWithInheritance();

                if (!"auto".equals(t)) {
                    // No need to calculate displacement caused by sibling nodes.
                    top = pixelValue(t);
                }
                else {
                    final String b = getBottomWithInheritance();

                    if (!"auto".equals(b)) {
                        // Estimate the vertical displacement caused by *all* siblings.
                        // This is very rough, and doesn't even take position or display types into account.
                        // It also doesn't take into account the fact that the parent's height may be hardcoded in CSS.
                        top = 0;
                        DomNode child = getElement().getDomNodeOrDie().getParentNode().getFirstChild();
                        while (child != null) {
                            if (child instanceof HtmlElement && child.mayBeDisplayed()) {
                                top += 20;
                            }
                            child = child.getNextSibling();
                        }
                        top -= pixelValue(b);
                    }
                }
            }
            else {
                // Calculate the vertical displacement caused by *previous* siblings.
                DomNode prev = getElement().getDomNodeOrDie().getPreviousSibling();
                while (prev != null && !(prev instanceof HtmlElement)) {
                    prev = prev.getPreviousSibling();
                }
                if (prev != null) {
                    final HTMLElement e = (HTMLElement) ((HtmlElement) prev).getScriptObject();
                    final ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
                    top = style.getTop(true, false, false) + style.getCalculatedHeight(true, true);
                }
                // If the position is relative, we also need to add the specified "top" displacement.
                if ("relative".equals(p)) {
                    final String t = getTopWithInheritance();
                    top += pixelValue(t);
                }
            }
            top_ = Integer.valueOf(top);
        }
        else {
            top = top_.intValue();
        }

        if (includeMargin) {
            final int margin = pixelValue(getMarginTop());
            top += margin;
        }

        if (includeBorder) {
            final int border = pixelValue(getBorderTopWidth());
            top += border;
        }

        if (includePadding) {
            final int padding = getPaddingTopValue();
            top += padding;
        }

        return top;
    }

    /**
     * Returns the computed left (X coordinate), relative to the node's parent's left edge.
     * @param includeMargin whether or not to take the margin into account in the calculation
     * @param includeBorder whether or not to take the border into account in the calculation
     * @param includePadding whether or not to take the padding into account in the calculation
     * @return the computed left (X coordinate), relative to the node's parent's left edge
     */
    public int getLeft(final boolean includeMargin, final boolean includeBorder, final boolean includePadding) {
        String p = getPositionWithInheritance();
        final String l = getLeftWithInheritance();
        final String r = getRightWithInheritance();

        if ("fixed".equals(p) && getBrowserVersion().hasFeature(TREATS_POSITION_FIXED_LIKE_POSITION_STATIC)) {
            p = "static";
        }

        int left;
        if ("absolute".equals(p) && !"auto".equals(l)) {
            // No need to calculate displacement caused by sibling nodes.
            left = pixelValue(l);
        }
        else if ("absolute".equals(p) && !"auto".equals(r)) {
            // Need to calculate the horizontal displacement caused by *all* siblings.
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
            final int parentWidth = style.getCalculatedWidth(false, false);
            left = parentWidth - pixelValue(r);
        }
        else if ("fixed".equals(p) && "auto".equals(l)) {
            // Fixed to the location at which the browser puts it via normal element flowing.
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
            left = pixelValue(style.getLeftWithInheritance());
        }
        else if ("static".equals(p)) {
            // We need to calculate the horizontal displacement caused by *previous* siblings.
            left = 0;
            for (DomNode n = getDomNodeOrDie(); n != null; n = n.getPreviousSibling()) {
                if (n.getScriptObject() instanceof HTMLElement) {
                    final HTMLElement e = (HTMLElement) n.getScriptObject();
                    final ComputedCSSStyleDeclaration style = e.getWindow().getComputedStyle(e, null);
                    final String d = style.getDisplay();
                    if ("block".equals(d)) {
                        break;
                    }
                    else if (!"none".equals(d)) {
                        left += style.getCalculatedWidth(true, true);
                    }
                }
                else if (n.getScriptObject() instanceof Text) {
                    left += n.getTextContent().length() * PIXELS_PER_CHAR;
                }
                if (n instanceof HtmlTableRow) {
                    break;
                }
            }
        }
        else {
            // Just use the CSS specified value.
            left = pixelValue(l);
        }

        if (includeMargin) {
            final int margin = getMarginLeftValue();
            left += margin;
        }

        if (includeBorder) {
            final int border = pixelValue(getBorderLeftWidth());
            left += border;
        }

        if (includePadding) {
            final int padding = getPaddingLeftValue();
            left += padding;
        }

        return left;
    }

    /**
     * Returns the CSS <tt>position</tt> attribute, replacing inherited values with the actual parent values.
     * @return the CSS <tt>position</tt> attribute, replacing inherited values with the actual parent values
     */
    public String getPositionWithInheritance() {
        String p = getPosition();
        if ("inherit".equals(p)) {
            if (getBrowserVersion().hasFeature(CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                if (parent == null) {
                    p = "static";
                }
                else {
                    final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                    p = style.getPositionWithInheritance();
                }
            }
            else {
                p = "static";
            }
        }
        return p;
    }

    /**
     * Returns the CSS <tt>left</tt> attribute, replacing inherited values with the actual parent values.
     * @return the CSS <tt>left</tt> attribute, replacing inherited values with the actual parent values
     */
    public String getLeftWithInheritance() {
        String left = getLeft();
        if ("inherit".equals(left)) {
            if (getBrowserVersion().hasFeature(CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                if (parent == null) {
                    left = "auto";
                }
                else {
                    final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                    left = style.getLeftWithInheritance();
                }
            }
            else {
                left = "auto";
            }
        }
        return left;
    }

    /**
     * Returns the CSS <tt>right</tt> attribute, replacing inherited values with the actual parent values.
     * @return the CSS <tt>right</tt> attribute, replacing inherited values with the actual parent values
     */
    public String getRightWithInheritance() {
        String right = getRight();
        if ("inherit".equals(right)) {
            if (getBrowserVersion().hasFeature(CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                if (parent == null) {
                    right = "auto";
                }
                else {
                    final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                    right = style.getRightWithInheritance();
                }
            }
            else {
                right = "auto";
            }
        }
        return right;
    }

    /**
     * Returns the CSS <tt>top</tt> attribute, replacing inherited values with the actual parent values.
     * @return the CSS <tt>top</tt> attribute, replacing inherited values with the actual parent values
     */
    public String getTopWithInheritance() {
        String top = getTop();
        if ("inherit".equals(top)) {
            if (getBrowserVersion().hasFeature(CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                if (parent == null) {
                    top = "auto";
                }
                else {
                    final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                    top = style.getTopWithInheritance();
                }
            }
            else {
                top = "auto";
            }
        }
        return top;
    }

    /**
     * Returns the CSS <tt>bottom</tt> attribute, replacing inherited values with the actual parent values.
     * @return the CSS <tt>bottom</tt> attribute, replacing inherited values with the actual parent values
     */
    public String getBottomWithInheritance() {
        String bottom = getBottom();
        if ("inherit".equals(bottom)) {
            if (getBrowserVersion().hasFeature(CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                if (parent == null) {
                    bottom = "auto";
                }
                else {
                    final ComputedCSSStyleDeclaration style = parent.getWindow().getComputedStyle(parent, null);
                    bottom = style.getBottomWithInheritance();
                }
            }
            else {
                bottom = "auto";
            }
        }
        return bottom;
    }

    /**
     * Gets the left margin of the element.
     * @return the value in pixels
     */
    public int getMarginLeftValue() {
        return pixelValue(getMarginLeft());
    }

    /**
     * Gets the right margin of the element.
     * @return the value in pixels
     */
    public int getMarginRightValue() {
        return pixelValue(getMarginRight());
    }

    /**
     * Gets the top margin of the element.
     * @return the value in pixels
     */
    public int getMarginTopValue() {
        return pixelValue(getMarginTop());
    }

    /**
     * Gets the bottom margin of the element.
     * @return the value in pixels
     */
    public int getMarginBottomValue() {
        return pixelValue(getMarginBottom());
    }

    /**
     * Gets the left padding of the element.
     * @return the value in pixels
     */
    public int getPaddingLeftValue() {
        return pixelValue(getPaddingLeft());
    }

    /**
     * Gets the right padding of the element.
     * @return the value in pixels
     */
    public int getPaddingRightValue() {
        return pixelValue(getPaddingRight());
    }

    /**
     * Gets the top padding of the element.
     * @return the value in pixels
     */
    public int getPaddingTopValue() {
        return pixelValue(getPaddingTop());
    }

    /**
     * Gets the bottom padding of the element.
     * @return the value in pixels
     */
    public int getPaddingBottomValue() {
        return pixelValue(getPaddingBottom());
    }

    private int getPaddingHorizontal() {
        if (paddingHorizontal_ == null) {
            paddingHorizontal_ =
                Integer.valueOf("none".equals(getDisplay()) ? 0 : getPaddingLeftValue() + getPaddingRightValue());
        }
        return paddingHorizontal_.intValue();
    }

    private int getPaddingVertical() {
        if (paddingVertical_ == null) {
            paddingVertical_ =
                Integer.valueOf("none".equals(getDisplay()) ? 0 : getPaddingTopValue() + getPaddingBottomValue());
        }
        return paddingVertical_.intValue();
    }

    /**
     * Gets the size of the left border of the element.
     * @return the value in pixels
     */
    public int getBorderLeftValue() {
        return pixelValue(getBorderLeftWidth());
    }

    /**
     * Gets the size of the right border of the element.
     * @return the value in pixels
     */
    public int getBorderRightValue() {
        return pixelValue(getBorderRightWidth());
    }

    /**
     * Gets the size of the top border of the element.
     * @return the value in pixels
     */
    public int getBorderTopValue() {
        return pixelValue(getBorderTopWidth());
    }

    /**
     * Gets the size of the bottom border of the element.
     * @return the value in pixels
     */
    public int getBorderBottomValue() {
        return pixelValue(getBorderBottomWidth());
    }

    private int getBorderHorizontal() {
        if (borderHorizontal_ == null) {
            borderHorizontal_ =
                Integer.valueOf("none".equals(getDisplay()) ? 0 : getBorderLeftValue() + getBorderRightValue());
        }
        return borderHorizontal_.intValue();
    }

    private int getBorderVertical() {
        if (borderVertical_ == null) {
            borderVertical_ =
                Integer.valueOf("none".equals(getDisplay()) ? 0 : getBorderTopValue() + getBorderBottomValue());
        }
        return borderVertical_.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWordSpacing() {
        return defaultIfEmpty(super.getWordSpacing(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWordWrap() {
        return defaultIfEmpty(super.getWordWrap(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getZIndex() {
        final Object response = super.getZIndex();
        if (response.toString().isEmpty()) {
            return "auto";
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyValue(final String name) {
        // need to invoke the getter to take care of the default value
        final Object property = getProperty(this, camelize(name));
        if (property == NOT_FOUND) {
            return super.getPropertyValue(name);
        }
        return Context.toString(property);
    }

    /**
     * Returns the specified length value as a pixel length value, as long as we're not emulating IE.
     * This method does <b>NOT</b> handle percentages correctly; use {@link #pixelValue(HTMLElement, CssValue)}
     * if you need percentage support).
     * @param value the length value to convert to a pixel length value
     * @return the specified length value as a pixel length value
     * @see #pixelString(HTMLElement, CSSStyleDeclaration.CssValue)
     */
    protected String pixelString(final String value) {
        if (getBrowserVersion().hasFeature(JS_LENGTH_WITHOUT_PX)) {
            return value;
        }
        if (value.endsWith("px")) {
            return value;
        }
        return pixelValue(value) + "px";
    }

    /**
     * Returns the specified length CSS attribute value value as a pixel length value, as long as
     * we're not emulating IE. If the specified CSS attribute value is a percentage, this method
     * uses the specified value object to recursively retrieve the base (parent) CSS attribute value.
     * @param element the element for which the CSS attribute value is to be retrieved
     * @param value the CSS attribute value which is to be retrieved
     * @return the specified length CSS attribute value as a pixel length value
     * @see #pixelString(String)
     */
    protected String pixelString(final Element element, final CssValue value) {
        final String s = value.get(element);
        if (getBrowserVersion().hasFeature(JS_LENGTH_WITHOUT_PX)) {
            return s;
        }
        if (s.endsWith("px")) {
            return s;
        }
        return pixelValue(element, value) + "px";
    }

}
