/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.commons.lang3.StringUtils;
import org.w3c.css.sac.Selector;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Text;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
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

    /** Maps element types to custom display types (display types that are not "block". */
    private Map<String, String> defaultDisplays_;

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
    protected String getStyleAttribute(final String name, final Map<String, StyleElement> styleMap) {
        String s = super.getStyleAttribute(name, null);
        if (s.isEmpty() && isInheritable(name)) {
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            if (parent != null) {
                s = getWindow().getComputedStyle(parent, null).getStyleAttribute(name, null);
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected Map<String, StyleElement> getStyleMap() {
        final Map<String, StyleElement> styleMap = super.getStyleMap();
        if (localModifications_ != null) {
            for (final StyleElement localStyleMod : localModifications_.values()) {
                final String key = localStyleMod.getName();
                final StyleElement existent = styleMap.get(key);
                if (existent == null) {
                    // Local modifications represent either default style elements or style elements
                    // defined in stylesheets; either way, they shouldn't overwrite any style
                    // elements derived directly from the HTML element's "style" attribute.
                    final StyleElement element =
                            new StyleElement(key, localStyleMod.getValue(), localStyleMod.getIndex());
                    styleMap.put(key, element);
                }
                else {
                    // replace if !IMPORTANT
                    if (PRIORITY_IMPORTANT.equals(localStyleMod.getPriority())) {
                        if (PRIORITY_IMPORTANT.equals(existent.getPriority())) {
                            if (existent.getSpecificity().compareTo(localStyleMod.getSpecificity()) < 0) {
                                final StyleElement element =
                                        new StyleElement(key, localStyleMod.getValue(), localStyleMod.getIndex());
                                styleMap.put(key, element);
                            }
                        }
                        else {
                            final StyleElement element =
                                    new StyleElement(key, localStyleMod.getValue(), localStyleMod.getIndex());
                            styleMap.put(key, element);
                        }
                    }
                }
            }
        }
        return styleMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_backgroundAttachment() {
        return defaultIfEmpty(super.jsxGet_backgroundAttachment(), "scroll");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_backgroundColor() {
        String value = super.jsxGet_backgroundColor();
        if (StringUtils.isEmpty(value)) {
            value = "transparent";
        }
        else if (getBrowserVersion()
                .hasFeature(BrowserVersionFeatures.JS_GET_BACKGROUND_COLOR_FOR_COMPUTED_STYLE_RETURNS_RGB)) {
            value = toRGBColor(value);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_backgroundImage() {
        return defaultIfEmpty(super.jsxGet_backgroundImage(), "none");
    }

    /**
     * Gets the "backgroundPosition" style attribute.
     * @return the style attribute
     */
    @Override
    public String jsxGet_backgroundPosition() {
        String bg = super.jsxGet_backgroundPosition();
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
    public String jsxGet_backgroundRepeat() {
        return defaultIfEmpty(super.jsxGet_backgroundRepeat(), "repeat");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderBottomColor() {
        return defaultIfEmpty(super.jsxGet_borderBottomColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderBottomStyle() {
        return defaultIfEmpty(super.jsxGet_borderBottomStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderBottomWidth() {
        return pixelString(defaultIfEmpty(super.jsxGet_borderBottomWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderCollapse() {
        return defaultIfEmpty(super.jsxGet_borderCollapse(), "separate");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderLeftColor() {
        return defaultIfEmpty(super.jsxGet_borderLeftColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderLeftStyle() {
        return defaultIfEmpty(super.jsxGet_borderLeftStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderLeftWidth() {
        return pixelString(defaultIfEmpty(super.jsxGet_borderLeftWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderRightColor() {
        return defaultIfEmpty(super.jsxGet_borderRightColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderRightStyle() {
        return defaultIfEmpty(super.jsxGet_borderRightStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderRightWidth() {
        return pixelString(defaultIfEmpty(super.jsxGet_borderRightWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderSpacing() {
        return defaultIfEmpty(super.jsxGet_borderSpacing(), "0px 0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderTopColor() {
        return defaultIfEmpty(super.jsxGet_borderTopColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderTopStyle() {
        return defaultIfEmpty(super.jsxGet_borderTopStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderTopWidth() {
        return pixelString(defaultIfEmpty(super.jsxGet_borderTopWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_bottom() {
        return defaultIfEmpty(super.jsxGet_bottom(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_captionSide() {
        return defaultIfEmpty(super.jsxGet_captionSide(), "top");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_clear() {
        return defaultIfEmpty(super.jsxGet_clear(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_clip() {
        return defaultIfEmpty(super.jsxGet_clip(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_content() {
        return defaultIfEmpty(super.jsxGet_content(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_color() {
        return defaultIfEmpty(super.jsxGet_color(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_counterIncrement() {
        return defaultIfEmpty(super.jsxGet_counterIncrement(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_counterReset() {
        return defaultIfEmpty(super.jsxGet_counterReset(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_cssFloat() {
        return defaultIfEmpty(super.jsxGet_cssFloat(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_cursor() {
        return defaultIfEmpty(super.jsxGet_cursor(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_direction() {
        return defaultIfEmpty(super.jsxGet_direction(), "ltr");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_display() {
        return defaultIfEmpty(super.jsxGet_display(), getDefaultStyleDisplay());
    }

    private String getDefaultStyleDisplay() {
        if (defaultDisplays_ == null) {
            final Map<String, String> map = new HashMap<String, String>();
            map.put("A", "inline");
            map.put("CODE", "inline");
            map.put("SPAN", "inline");
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_DISPLAY_DEFAULT)) {
                map.put("LI", "list-item");
                map.put("TABLE", "table");
                map.put("TBODY", "table-row-group");
                map.put("TD", "table-cell");
                map.put("TH", "table-cell");
                map.put("THEAD", "table-header-group");
                map.put("TR", "table-row");
            }
            defaultDisplays_ = Collections.unmodifiableMap(map);
        }
        final String defaultValue = defaultDisplays_.get(getElement().jsxGet_tagName());
        if (defaultValue == null) {
            return "block";
        }
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_emptyCells() {
        return defaultIfEmpty(super.jsxGet_emptyCells(), "-moz-show-background");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontFamily() {
        return defaultIfEmpty(super.jsxGet_fontFamily(), "serif");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontSize() {
        String value = super.jsxGet_fontSize();
        if (value.isEmpty()) {
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            if (parent != null) {
                value = parent.jsxGet_currentStyle().jsxGet_fontSize();
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
    public String jsxGet_fontSizeAdjust() {
        return defaultIfEmpty(super.jsxGet_fontSizeAdjust(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontStretch() {
        String defaultStretch = "";
        if (getBrowserVersion().hasFeature(
                BrowserVersionFeatures.CSS_FONT_STRECH_DEFAULT_NORMAL)) {
            defaultStretch = "normal";
        }
        return defaultIfEmpty(super.jsxGet_fontStretch(), defaultStretch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontStyle() {
        return defaultIfEmpty(super.jsxGet_fontStyle(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontVariant() {
        return defaultIfEmpty(super.jsxGet_fontVariant(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontWeight() {
        return defaultIfEmpty(super.jsxGet_fontWeight(), "400");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_height() {
        return pixelString(getElement(), new CssValue(Window.WINDOW_HEIGHT) {
            @Override public String get(final ComputedCSSStyleDeclaration style) {
                return defaultIfEmpty(style.getStyleAttribute("height", null), "362px");
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_imeMode() {
        return defaultIfEmpty(super.jsxGet_imeMode(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_left() {
        return defaultIfEmpty(super.jsxGet_left(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_letterSpacing() {
        return defaultIfEmpty(super.jsxGet_letterSpacing(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_lineHeight() {
        return defaultIfEmpty(super.jsxGet_lineHeight(), "20px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_listStyleImage() {
        return defaultIfEmpty(super.jsxGet_listStyleImage(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_listStylePosition() {
        return defaultIfEmpty(super.jsxGet_listStylePosition(), "outside");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_listStyleType() {
        return defaultIfEmpty(super.jsxGet_listStyleType(), "disc");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_marginBottom() {
        return pixelString(defaultIfEmpty(super.jsxGet_marginBottom(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_marginLeft() {
        return pixelString(defaultIfEmpty(super.jsxGet_marginLeft(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_marginRight() {
        return pixelString(defaultIfEmpty(super.jsxGet_marginRight(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_marginTop() {
        return pixelString(defaultIfEmpty(super.jsxGet_marginTop(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_markerOffset() {
        return defaultIfEmpty(super.jsxGet_markerOffset(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_maxHeight() {
        return defaultIfEmpty(super.jsxGet_maxHeight(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_maxWidth() {
        return defaultIfEmpty(super.jsxGet_maxWidth(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_minHeight() {
        return defaultIfEmpty(super.jsxGet_minHeight(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_minWidth() {
        return defaultIfEmpty(super.jsxGet_minWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozAppearance() {
        return defaultIfEmpty(super.jsxGet_MozAppearance(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBackgroundClip() {
        return defaultIfEmpty(super.jsxGet_MozBackgroundClip(), "border");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBackgroundInlinePolicy() {
        return defaultIfEmpty(super.jsxGet_MozBackgroundInlinePolicy(), "continuous");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBackgroundOrigin() {
        return defaultIfEmpty(super.jsxGet_MozBackgroundOrigin(), "padding");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBackgroundSize() {
        return defaultIfEmpty(super.jsxGet_MozBackgroundSize(), "auto auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBinding() {
        return defaultIfEmpty(super.jsxGet_MozBinding(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderBottomColors() {
        return defaultIfEmpty(super.jsxGet_MozBorderBottomColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderImage() {
        return defaultIfEmpty(super.jsxGet_MozBorderImage(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderLeftColors() {
        return defaultIfEmpty(super.jsxGet_MozBorderLeftColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderRadiusBottomleft() {
        return defaultIfEmpty(super.jsxGet_MozBorderRadiusBottomleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderRadiusBottomright() {
        return defaultIfEmpty(super.jsxGet_MozBorderRadiusBottomright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderRadiusTopleft() {
        return defaultIfEmpty(super.jsxGet_MozBorderRadiusTopleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderRadiusTopright() {
        return defaultIfEmpty(super.jsxGet_MozBorderRadiusTopright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderRightColors() {
        return defaultIfEmpty(super.jsxGet_MozBorderRightColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderTopColors() {
        return defaultIfEmpty(super.jsxGet_MozBorderTopColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxAlign() {
        return defaultIfEmpty(super.jsxGet_MozBoxAlign(), "stretch");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxDirection() {
        return defaultIfEmpty(super.jsxGet_MozBoxDirection(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxFlex() {
        return defaultIfEmpty(super.jsxGet_MozBoxFlex(), "0");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxOrdinalGroup() {
        return defaultIfEmpty(super.jsxGet_MozBoxOrdinalGroup(), "1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxOrient() {
        return defaultIfEmpty(super.jsxGet_MozBoxOrient(), "horizontal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxShadow() {
        return defaultIfEmpty(super.jsxGet_MozBoxShadow(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxPack() {
        return defaultIfEmpty(super.jsxGet_MozBoxPack(), "start");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxSizing() {
        return defaultIfEmpty(super.jsxGet_MozBoxSizing(), "content-box");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozColumnCount() {
        return defaultIfEmpty(super.jsxGet_MozColumnCount(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozColumnGap() {
        return defaultIfEmpty(super.jsxGet_MozColumnGap(), "16px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozColumnRuleColor() {
        return defaultIfEmpty(super.jsxGet_MozColumnRuleColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozColumnRuleStyle() {
        return defaultIfEmpty(super.jsxGet_MozColumnRuleStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozColumnRuleWidth() {
        return defaultIfEmpty(super.jsxGet_MozColumnRuleWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozColumnWidth() {
        return defaultIfEmpty(super.jsxGet_MozColumnWidth(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozFloatEdge() {
        return defaultIfEmpty(super.jsxGet_MozFloatEdge(), "content-box");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozForceBrokenImageIcon() {
        return defaultIfEmpty(super.jsxGet_MozForceBrokenImageIcon(), "0");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozImageRegion() {
        return defaultIfEmpty(super.jsxGet_MozImageRegion(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOpacity() {
        return defaultIfEmpty(super.jsxGet_MozOpacity(), "1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineColor() {
        return defaultIfEmpty(super.jsxGet_MozOutlineColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineOffset() {
        return defaultIfEmpty(super.jsxGet_MozOutlineOffset(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineRadiusBottomleft() {
        return defaultIfEmpty(super.jsxGet_MozOutlineRadiusBottomleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineRadiusBottomright() {
        return defaultIfEmpty(super.jsxGet_MozOutlineRadiusBottomright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineRadiusTopleft() {
        return defaultIfEmpty(super.jsxGet_MozOutlineRadiusTopleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineRadiusTopright() {
        return defaultIfEmpty(super.jsxGet_MozOutlineRadiusTopright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineStyle() {
        return defaultIfEmpty(super.jsxGet_MozOutlineStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineWidth() {
        return defaultIfEmpty(super.jsxGet_MozOutlineWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozStackSizing() {
        return defaultIfEmpty(super.jsxGet_MozStackSizing(), "stretch-to-fit");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozTransform() {
        return defaultIfEmpty(super.jsxGet_MozTransform(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozTransformOrigin() {
        return defaultIfEmpty(super.jsxGet_MozTransformOrigin(), "50% 50%");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozUserFocus() {
        return defaultIfEmpty(super.jsxGet_MozUserFocus(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozUserInput() {
        return defaultIfEmpty(super.jsxGet_MozUserInput(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozUserModify() {
        return defaultIfEmpty(super.jsxGet_MozUserModify(), "read-only");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozUserSelect() {
        return defaultIfEmpty(super.jsxGet_MozUserSelect(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozWindowShadow() {
        return defaultIfEmpty(super.jsxGet_MozWindowShadow(), "default");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_opacity() {
        return defaultIfEmpty(super.jsxGet_opacity(), "1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_outlineColor() {
        return defaultIfEmpty(super.jsxGet_outlineColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_outlineOffset() {
        return defaultIfEmpty(super.jsxGet_outlineOffset(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_outlineStyle() {
        return defaultIfEmpty(super.jsxGet_outlineStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_outlineWidth() {
        return defaultIfEmpty(super.jsxGet_outlineWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_overflow() {
        return defaultIfEmpty(super.jsxGet_overflow(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_overflowX() {
        return defaultIfEmpty(super.jsxGet_overflowX(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_overflowY() {
        return defaultIfEmpty(super.jsxGet_overflowY(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_pageBreakAfter() {
        return defaultIfEmpty(super.jsxGet_pageBreakAfter(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_pageBreakBefore() {
        return defaultIfEmpty(super.jsxGet_pageBreakBefore(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_paddingBottom() {
        return pixelString(defaultIfEmpty(super.jsxGet_paddingBottom(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_paddingLeft() {
        return pixelString(defaultIfEmpty(super.jsxGet_paddingLeft(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_paddingRight() {
        return pixelString(defaultIfEmpty(super.jsxGet_paddingRight(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_paddingTop() {
        return pixelString(defaultIfEmpty(super.jsxGet_paddingTop(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_pointerEvents() {
        return defaultIfEmpty(super.jsxGet_pointerEvents(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_position() {
        return defaultIfEmpty(super.jsxGet_position(), "static");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_right() {
        return defaultIfEmpty(super.jsxGet_right(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_tableLayout() {
        return defaultIfEmpty(super.jsxGet_tableLayout(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_textAlign() {
        return defaultIfEmpty(super.jsxGet_textAlign(), "start");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_textDecoration() {
        return defaultIfEmpty(super.jsxGet_textDecoration(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_textIndent() {
        return defaultIfEmpty(super.jsxGet_textIndent(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_textShadow() {
        String shadow = "";
        if (getBrowserVersion().hasFeature(
                BrowserVersionFeatures.CSS_TEXT_SHADOW_DEFAULT_NONE)) {
            shadow = "none";
        }
        return defaultIfEmpty(super.jsxGet_textShadow(), shadow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_textTransform() {
        return defaultIfEmpty(super.jsxGet_textTransform(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_top() {
        return defaultIfEmpty(super.jsxGet_top(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_unicodeBidi() {
        String unicodeBidi = "embed";
        if (getBrowserVersion().hasFeature(
                BrowserVersionFeatures.CSS_UNICODE_BIDI_DEFAULT_NORMAL)) {
            unicodeBidi = "normal";
        }

        return defaultIfEmpty(super.jsxGet_unicodeBidi(), unicodeBidi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_verticalAlign() {
        return defaultIfEmpty(super.jsxGet_verticalAlign(), "baseline");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_visibility() {
        return defaultIfEmpty(super.jsxGet_visibility(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_whiteSpace() {
        return defaultIfEmpty(super.jsxGet_whiteSpace(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_width() {
        if ("none".equals(jsxGet_display())) {
            return "auto";
        }
        final String defaultWidth;
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_DEFAULT_WIDTH_AUTO)) {
            defaultWidth = "auto";
        }
        else {
            defaultWidth = Window.WINDOW_WIDTH + "px";
        }
        return pixelString(getElement(), new CssValue(Window.WINDOW_WIDTH) {
            @Override public String get(final ComputedCSSStyleDeclaration style) {
                final String value = style.getStyleAttribute(WIDTH, null);
                if (StringUtils.isEmpty(value)) {
                    if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_DEFAULT_WIDTH_AUTO)
                            && "absolute".equals(getStyleAttribute("position", null))) {
                        final DomNode domNode = getDomNodeOrDie();
                        final String content = domNode.getTextContent();
                        // do this only for small content
                        // at least for empty div's this is more correct
                        if (null != content && (content.length() < 13)) {
                            return (content.length() * 7) + "px";
                        }
                    }
                    return defaultWidth;
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

        final String display = jsxGet_display();
        if ("none".equals(display)) {
            width_ = Integer.valueOf(0);
            return 0;
        }

        int width;
        final String styleWidth = super.jsxGet_width();
        final DomNode parent = node.getParentNode();
        if (StringUtils.isEmpty(styleWidth) && parent instanceof HtmlElement) {
            // hack: TODO find a way to specify default values for different tags
            if (getElement() instanceof HTMLCanvasElement) {
                return 300;
            }

            // Width not explicitly set.
            final String cssFloat = jsxGet_cssFloat();
            if ("right".equals(cssFloat) || "left".equals(cssFloat)) {
                // We're floating; simplistic approximation: text content * pixels per character.
                width = getDomNodeOrDie().getTextContent().length() * PIXELS_PER_CHAR;
            }
            else if ("block".equals(display)) {
                // Block elements take up 100% of the parent's width.
                final HTMLElement parentJS = (HTMLElement) parent.getScriptObject();
                final String parentWidth = getWindow().getComputedStyle(parentJS, null).jsxGet_width();
                if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_DEFAULT_WIDTH_AUTO)
                        && "auto".equals(parentWidth)) {
                    width = Window.WINDOW_WIDTH;
                }
                else {
                    width = pixelValue(parentJS, new CssValue(Window.WINDOW_WIDTH) {
                        @Override public String get(final ComputedCSSStyleDeclaration style) {
                            return style.jsxGet_width();
                        }
                    });
                }
                width -= (getBorderHorizontal() + getPaddingHorizontal());
            }
            else {
                // Inline elements take up however much space is required by their children.
                width = getContentWidth();
            }
        }
        else {
            // Width explicitly set in the style attribute, or there was no parent to provide guidance.
            width = pixelValue(getElement(), new CssValue(Window.WINDOW_WIDTH) {
                @Override public String get(final ComputedCSSStyleDeclaration style) {
                    return style.getStyleAttribute(WIDTH, null);
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
        for (final DomNode child : getDomNodeOrDie().getChildren()) {
            if (child.getScriptObject() instanceof HTMLElement) {
                final HTMLElement e = (HTMLElement) child.getScriptObject();
                final int w = e.jsxGet_currentStyle().getCalculatedWidth(true, true);
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
        final boolean useDefaultHeight = getBrowserVersion().hasFeature(
                BrowserVersionFeatures.CSS_DEFAULT_ELMENT_HEIGHT_MARKS_MIN);
        final boolean explicitHeightSpecified = super.jsxGet_height().length() > 0;

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

        if ("none".equals(jsxGet_display())) {
            height2_ = Integer.valueOf(0);
            return 0;
        }

        if (getElement() instanceof HTMLBodyElement) {
            height2_ = Integer.valueOf(Window.WINDOW_HEIGHT);
            return Window.WINDOW_HEIGHT;
        }

        final boolean explicitHeightSpecified = super.jsxGet_height().length() > 0;

        int defaultHeight = 20;
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_DEFAULT_ELMENT_HEIGHT_15)) {
            defaultHeight = 15;
        }

        final int defaultValue = getElement() instanceof HTMLCanvasElement ? 150 : Window.WINDOW_HEIGHT;

        int height = pixelValue(getElement(), new CssValue(defaultValue) {
            @Override public String get(final ComputedCSSStyleDeclaration style) {
                return style.getStyleAttribute("height", null);
            }
        });

        final boolean useDefaultHeight = getBrowserVersion().hasFeature(
                BrowserVersionFeatures.CSS_DEFAULT_ELMENT_HEIGHT_MARKS_MIN);
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

        HTMLElement lastFlowing = null;
        final Set<HTMLElement> independent = new HashSet<HTMLElement>();
        for (final DomNode child : node.getChildren()) {
            if (child.mayBeDisplayed() && child.getScriptObject() instanceof HTMLElement) {
                final HTMLElement e = (HTMLElement) child.getScriptObject();
                final ComputedCSSStyleDeclaration style = e.jsxGet_currentStyle();
                final String pos = style.getPositionWithInheritance();
                if ("static".equals(pos) || "relative".equals(pos)) {
                    lastFlowing = e;
                }
                else if ("absolute".equals(pos)) {
                    independent.add(e);
                }
            }
        }

        final Set<HTMLElement> relevant = new HashSet<HTMLElement>();
        relevant.addAll(independent);
        if (lastFlowing != null) {
            relevant.add(lastFlowing);
        }

        int max = 0;
        for (final HTMLElement e : relevant) {
            final ComputedCSSStyleDeclaration style = e.jsxGet_currentStyle();
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
        final String overflow = jsxGet_overflow();
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
        int top;
        if (null == top_) {
            final String p = getPositionWithInheritance();
            final String t = getTopWithInheritance();
            final String b = getBottomWithInheritance();

            if ("absolute".equals(p) && !"auto".equals(t)) {
                // No need to calculate displacement caused by sibling nodes.
                top = pixelValue(t);
            }
            else if ("absolute".equals(p) && !"auto".equals(b)) {
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
            else {
                // Calculate the vertical displacement caused by *previous* siblings.
                top = 0;
                DomNode prev = getElement().getDomNodeOrDie().getPreviousSibling();
                while (prev != null && !(prev instanceof HtmlElement)) {
                    prev = prev.getPreviousSibling();
                }
                if (prev != null) {
                    final HTMLElement e = (HTMLElement) ((HtmlElement) prev).getScriptObject();
                    final ComputedCSSStyleDeclaration style = e.jsxGet_currentStyle();
                    top = style.getTop(true, false, false) + style.getCalculatedHeight(true, true);
                }
                // If the position is relative, we also need to add the specified "top" displacement.
                if ("relative".equals(p)) {
                    top += pixelValue(t);
                }
            }
            top_ = Integer.valueOf(top);
        }
        else {
            top = top_.intValue();
        }

        if (includeMargin) {
            final int margin = pixelValue(jsxGet_marginTop());
            top += margin;
        }

        if (includeBorder) {
            final int border = pixelValue(jsxGet_borderTopWidth());
            top += border;
        }

        if (includePadding) {
            final int padding = getPaddingTop();
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

        if ("fixed".equals(p) && getBrowserVersion().hasFeature(
                BrowserVersionFeatures.TREATS_POSITION_FIXED_LIKE_POSITION_STATIC)) {
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
            final int parentWidth = parent.jsxGet_currentStyle().getCalculatedWidth(false, false);
            left = parentWidth - pixelValue(r);
        }
        else if ("fixed".equals(p) && "auto".equals(l)) {
            // Fixed to the location at which the browser puts it via normal element flowing.
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            left = pixelValue(parent.jsxGet_currentStyle().getLeftWithInheritance());
        }
        else if ("static".equals(p)) {
            // We need to calculate the horizontal displacement caused by *previous* siblings.
            left = 0;
            for (DomNode n = getDomNodeOrDie(); n != null; n = n.getPreviousSibling()) {
                if (n.getScriptObject() instanceof HTMLElement) {
                    final HTMLElement e = (HTMLElement) n.getScriptObject();
                    final String d = e.jsxGet_currentStyle().jsxGet_display();
                    if ("block".equals(d)) {
                        break;
                    }
                    else if (!"none".equals(d)) {
                        left += e.jsxGet_currentStyle().getCalculatedWidth(true, true);
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
            final int margin = getMarginLeft();
            left += margin;
        }

        if (includeBorder) {
            final int border = pixelValue(jsxGet_borderLeftWidth());
            left += border;
        }

        if (includePadding) {
            final int padding = getPaddingLeft();
            left += padding;
        }

        return left;
    }

    /**
     * Returns the CSS <tt>position</tt> attribute, replacing inherited values with the actual parent values.
     * @return the CSS <tt>position</tt> attribute, replacing inherited values with the actual parent values
     */
    public String getPositionWithInheritance() {
        String p = jsxGet_position();
        if ("inherit".equals(p)) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                p = (parent != null ? parent.jsxGet_currentStyle().getPositionWithInheritance() : "static");
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
        String left = jsxGet_left();
        if ("inherit".equals(left)) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                left = (parent != null ? parent.jsxGet_currentStyle().getLeftWithInheritance() : "auto");
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
        String right = jsxGet_right();
        if ("inherit".equals(right)) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                right = (parent != null ? parent.jsxGet_currentStyle().getRightWithInheritance() : "auto");
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
        String top = jsxGet_top();
        if ("inherit".equals(top)) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                top = (parent != null ? parent.jsxGet_currentStyle().getTopWithInheritance() : "auto");
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
        String bottom = jsxGet_bottom();
        if ("inherit".equals(bottom)) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                bottom = (parent != null ? parent.jsxGet_currentStyle().getBottomWithInheritance() : "auto");
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
    public int getMarginLeft() {
        return pixelValue(jsxGet_marginLeft());
    }

    /**
     * Gets the right margin of the element.
     * @return the value in pixels
     */
    public int getMarginRight() {
        return pixelValue(jsxGet_marginRight());
    }

    /**
     * Gets the top margin of the element.
     * @return the value in pixels
     */
    public int getMarginTop() {
        return pixelValue(jsxGet_marginTop());
    }

    /**
     * Gets the bottom margin of the element.
     * @return the value in pixels
     */
    public int getMarginBottom() {
        return pixelValue(jsxGet_marginBottom());
    }

    /**
     * Gets the left padding of the element.
     * @return the value in pixels
     */
    public int getPaddingLeft() {
        return pixelValue(jsxGet_paddingLeft());
    }

    /**
     * Gets the right padding of the element.
     * @return the value in pixels
     */
    public int getPaddingRight() {
        return pixelValue(jsxGet_paddingRight());
    }

    /**
     * Gets the top padding of the element.
     * @return the value in pixels
     */
    public int getPaddingTop() {
        return pixelValue(jsxGet_paddingTop());
    }

    /**
     * Gets the bottom padding of the element.
     * @return the value in pixels
     */
    public int getPaddingBottom() {
        return pixelValue(jsxGet_paddingBottom());
    }

    private int getPaddingHorizontal() {
        if (paddingHorizontal_ == null) {
            paddingHorizontal_ =
                Integer.valueOf("none".equals(jsxGet_display()) ? 0 : getPaddingLeft() + getPaddingRight());
        }
        return paddingHorizontal_.intValue();
    }

    private int getPaddingVertical() {
        if (paddingVertical_ == null) {
            paddingVertical_ =
                Integer.valueOf("none".equals(jsxGet_display()) ? 0 : getPaddingTop() + getPaddingBottom());
        }
        return paddingVertical_.intValue();
    }

    /**
     * Gets the size of the left border of the element.
     * @return the value in pixels
     */
    public int getBorderLeft() {
        return pixelValue(jsxGet_borderLeftWidth());
    }

    /**
     * Gets the size of the right border of the element.
     * @return the value in pixels
     */
    public int getBorderRight() {
        return pixelValue(jsxGet_borderRightWidth());
    }

    /**
     * Gets the size of the top border of the element.
     * @return the value in pixels
     */
    public int getBorderTop() {
        return pixelValue(jsxGet_borderTopWidth());
    }

    /**
     * Gets the size of the bottom border of the element.
     * @return the value in pixels
     */
    public int getBorderBottom() {
        return pixelValue(jsxGet_borderBottomWidth());
    }

    private int getBorderHorizontal() {
        if (borderHorizontal_ == null) {
            borderHorizontal_ =
                Integer.valueOf("none".equals(jsxGet_display()) ? 0 : getBorderLeft() + getBorderRight());
        }
        return borderHorizontal_.intValue();
    }

    private int getBorderVertical() {
        if (borderVertical_ == null) {
            borderVertical_ =
                Integer.valueOf("none".equals(jsxGet_display()) ? 0 : getBorderTop() + getBorderBottom());
        }
        return borderVertical_.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_wordSpacing() {
        String wordSpacing = "0px";
        if (getBrowserVersion().hasFeature(
                BrowserVersionFeatures.CSS_WORD_SPACING_DEFAULT_NORMAL)) {
            wordSpacing = "normal";
        }

        return defaultIfEmpty(super.jsxGet_wordSpacing(), wordSpacing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_wordWrap() {
        return defaultIfEmpty(super.jsxGet_wordWrap(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object jsxGet_zIndex() {
        final Object response = super.jsxGet_zIndex();
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
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_LENGTH_WITHOUT_PX)) {
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
    protected String pixelString(final HTMLElement element, final CssValue value) {
        final String s = value.get(element);
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_LENGTH_WITHOUT_PX)) {
            return s;
        }
        if (s.endsWith("px")) {
            return s;
        }
        return pixelValue(element, value) + "px";
    }

}
