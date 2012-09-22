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
    public String get_backgroundAttachment() {
        return defaultIfEmpty(super.get_backgroundAttachment(), "scroll");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_backgroundColor() {
        String value = super.get_backgroundColor();
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
    public String get_backgroundImage() {
        return defaultIfEmpty(super.get_backgroundImage(), "none");
    }

    /**
     * Gets the "backgroundPosition" style attribute.
     * @return the style attribute
     */
    @Override
    public String get_backgroundPosition() {
        String bg = super.get_backgroundPosition();
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
    public String get_backgroundRepeat() {
        return defaultIfEmpty(super.get_backgroundRepeat(), "repeat");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderBottomColor() {
        return defaultIfEmpty(super.get_borderBottomColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderBottomStyle() {
        return defaultIfEmpty(super.get_borderBottomStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderBottomWidth() {
        return pixelString(defaultIfEmpty(super.get_borderBottomWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderCollapse() {
        return defaultIfEmpty(super.get_borderCollapse(), "separate");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderLeftColor() {
        return defaultIfEmpty(super.get_borderLeftColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderLeftStyle() {
        return defaultIfEmpty(super.get_borderLeftStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderLeftWidth() {
        return pixelString(defaultIfEmpty(super.get_borderLeftWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderRightColor() {
        return defaultIfEmpty(super.get_borderRightColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderRightStyle() {
        return defaultIfEmpty(super.get_borderRightStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderRightWidth() {
        return pixelString(defaultIfEmpty(super.get_borderRightWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderSpacing() {
        return defaultIfEmpty(super.get_borderSpacing(), "0px 0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderTopColor() {
        return defaultIfEmpty(super.get_borderTopColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderTopStyle() {
        return defaultIfEmpty(super.get_borderTopStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_borderTopWidth() {
        return pixelString(defaultIfEmpty(super.get_borderTopWidth(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_bottom() {
        return defaultIfEmpty(super.get_bottom(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_captionSide() {
        return defaultIfEmpty(super.get_captionSide(), "top");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_clear() {
        return defaultIfEmpty(super.get_clear(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_clip() {
        return defaultIfEmpty(super.get_clip(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_content() {
        return defaultIfEmpty(super.get_content(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_color() {
        return defaultIfEmpty(super.get_color(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_counterIncrement() {
        return defaultIfEmpty(super.get_counterIncrement(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_counterReset() {
        return defaultIfEmpty(super.get_counterReset(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_cssFloat() {
        return defaultIfEmpty(super.get_cssFloat(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_cursor() {
        return defaultIfEmpty(super.get_cursor(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_direction() {
        return defaultIfEmpty(super.get_direction(), "ltr");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_display() {
        return defaultIfEmpty(super.get_display(), getDefaultStyleDisplay());
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
        final String defaultValue = defaultDisplays_.get(getElement().get_tagName());
        if (defaultValue == null) {
            return "block";
        }
        return defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_emptyCells() {
        return defaultIfEmpty(super.get_emptyCells(), "-moz-show-background");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_fontFamily() {
        return defaultIfEmpty(super.get_fontFamily(), "serif");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_fontSize() {
        String value = super.get_fontSize();
        if (value.isEmpty()) {
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            if (parent != null) {
                value = parent.get_currentStyle().get_fontSize();
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
    public String get_fontSizeAdjust() {
        return defaultIfEmpty(super.get_fontSizeAdjust(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_fontStretch() {
        String defaultStretch = "";
        if (getBrowserVersion().hasFeature(
                BrowserVersionFeatures.CSS_FONT_STRECH_DEFAULT_NORMAL)) {
            defaultStretch = "normal";
        }
        return defaultIfEmpty(super.get_fontStretch(), defaultStretch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_fontStyle() {
        return defaultIfEmpty(super.get_fontStyle(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_fontVariant() {
        return defaultIfEmpty(super.get_fontVariant(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_fontWeight() {
        return defaultIfEmpty(super.get_fontWeight(), "400");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_height() {
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
    public String get_imeMode() {
        return defaultIfEmpty(super.get_imeMode(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_left() {
        return defaultIfEmpty(super.get_left(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_letterSpacing() {
        return defaultIfEmpty(super.get_letterSpacing(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_lineHeight() {
        return defaultIfEmpty(super.get_lineHeight(), "20px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_listStyleImage() {
        return defaultIfEmpty(super.get_listStyleImage(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_listStylePosition() {
        return defaultIfEmpty(super.get_listStylePosition(), "outside");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_listStyleType() {
        return defaultIfEmpty(super.get_listStyleType(), "disc");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_marginBottom() {
        return pixelString(defaultIfEmpty(super.get_marginBottom(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_marginLeft() {
        return pixelString(defaultIfEmpty(super.get_marginLeft(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_marginRight() {
        return pixelString(defaultIfEmpty(super.get_marginRight(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_marginTop() {
        return pixelString(defaultIfEmpty(super.get_marginTop(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_markerOffset() {
        return defaultIfEmpty(super.get_markerOffset(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_maxHeight() {
        return defaultIfEmpty(super.get_maxHeight(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_maxWidth() {
        return defaultIfEmpty(super.get_maxWidth(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_minHeight() {
        return defaultIfEmpty(super.get_minHeight(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_minWidth() {
        return defaultIfEmpty(super.get_minWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozAppearance() {
        return defaultIfEmpty(super.get_MozAppearance(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBackgroundClip() {
        return defaultIfEmpty(super.get_MozBackgroundClip(), "border");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBackgroundInlinePolicy() {
        return defaultIfEmpty(super.get_MozBackgroundInlinePolicy(), "continuous");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBackgroundOrigin() {
        return defaultIfEmpty(super.get_MozBackgroundOrigin(), "padding");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBackgroundSize() {
        return defaultIfEmpty(super.get_MozBackgroundSize(), "auto auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBinding() {
        return defaultIfEmpty(super.get_MozBinding(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBorderBottomColors() {
        return defaultIfEmpty(super.get_MozBorderBottomColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBorderImage() {
        return defaultIfEmpty(super.get_MozBorderImage(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBorderLeftColors() {
        return defaultIfEmpty(super.get_MozBorderLeftColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBorderRadiusBottomleft() {
        return defaultIfEmpty(super.get_MozBorderRadiusBottomleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBorderRadiusBottomright() {
        return defaultIfEmpty(super.get_MozBorderRadiusBottomright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBorderRadiusTopleft() {
        return defaultIfEmpty(super.get_MozBorderRadiusTopleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBorderRadiusTopright() {
        return defaultIfEmpty(super.get_MozBorderRadiusTopright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBorderRightColors() {
        return defaultIfEmpty(super.get_MozBorderRightColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBorderTopColors() {
        return defaultIfEmpty(super.get_MozBorderTopColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBoxAlign() {
        return defaultIfEmpty(super.get_MozBoxAlign(), "stretch");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBoxDirection() {
        return defaultIfEmpty(super.get_MozBoxDirection(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBoxFlex() {
        return defaultIfEmpty(super.get_MozBoxFlex(), "0");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBoxOrdinalGroup() {
        return defaultIfEmpty(super.get_MozBoxOrdinalGroup(), "1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBoxOrient() {
        return defaultIfEmpty(super.get_MozBoxOrient(), "horizontal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBoxShadow() {
        return defaultIfEmpty(super.get_MozBoxShadow(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBoxPack() {
        return defaultIfEmpty(super.get_MozBoxPack(), "start");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozBoxSizing() {
        return defaultIfEmpty(super.get_MozBoxSizing(), "content-box");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozColumnCount() {
        return defaultIfEmpty(super.get_MozColumnCount(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozColumnGap() {
        return defaultIfEmpty(super.get_MozColumnGap(), "16px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozColumnRuleColor() {
        return defaultIfEmpty(super.get_MozColumnRuleColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozColumnRuleStyle() {
        return defaultIfEmpty(super.get_MozColumnRuleStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozColumnRuleWidth() {
        return defaultIfEmpty(super.get_MozColumnRuleWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozColumnWidth() {
        return defaultIfEmpty(super.get_MozColumnWidth(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozFloatEdge() {
        return defaultIfEmpty(super.get_MozFloatEdge(), "content-box");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozForceBrokenImageIcon() {
        return defaultIfEmpty(super.get_MozForceBrokenImageIcon(), "0");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozImageRegion() {
        return defaultIfEmpty(super.get_MozImageRegion(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozOpacity() {
        return defaultIfEmpty(super.get_MozOpacity(), "1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozOutlineColor() {
        return defaultIfEmpty(super.get_MozOutlineColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozOutlineOffset() {
        return defaultIfEmpty(super.get_MozOutlineOffset(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozOutlineRadiusBottomleft() {
        return defaultIfEmpty(super.get_MozOutlineRadiusBottomleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozOutlineRadiusBottomright() {
        return defaultIfEmpty(super.get_MozOutlineRadiusBottomright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozOutlineRadiusTopleft() {
        return defaultIfEmpty(super.get_MozOutlineRadiusTopleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozOutlineRadiusTopright() {
        return defaultIfEmpty(super.get_MozOutlineRadiusTopright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozOutlineStyle() {
        return defaultIfEmpty(super.get_MozOutlineStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozOutlineWidth() {
        return defaultIfEmpty(super.get_MozOutlineWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozStackSizing() {
        return defaultIfEmpty(super.get_MozStackSizing(), "stretch-to-fit");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozTransform() {
        return defaultIfEmpty(super.get_MozTransform(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozTransformOrigin() {
        return defaultIfEmpty(super.get_MozTransformOrigin(), "50% 50%");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozUserFocus() {
        return defaultIfEmpty(super.get_MozUserFocus(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozUserInput() {
        return defaultIfEmpty(super.get_MozUserInput(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozUserModify() {
        return defaultIfEmpty(super.get_MozUserModify(), "read-only");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozUserSelect() {
        return defaultIfEmpty(super.get_MozUserSelect(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_MozWindowShadow() {
        return defaultIfEmpty(super.get_MozWindowShadow(), "default");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_opacity() {
        return defaultIfEmpty(super.get_opacity(), "1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_outlineColor() {
        return defaultIfEmpty(super.get_outlineColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_outlineOffset() {
        return defaultIfEmpty(super.get_outlineOffset(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_outlineStyle() {
        return defaultIfEmpty(super.get_outlineStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_outlineWidth() {
        return defaultIfEmpty(super.get_outlineWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_overflow() {
        return defaultIfEmpty(super.get_overflow(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_overflowX() {
        return defaultIfEmpty(super.get_overflowX(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_overflowY() {
        return defaultIfEmpty(super.get_overflowY(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_pageBreakAfter() {
        return defaultIfEmpty(super.get_pageBreakAfter(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_pageBreakBefore() {
        return defaultIfEmpty(super.get_pageBreakBefore(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_paddingBottom() {
        return pixelString(defaultIfEmpty(super.get_paddingBottom(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_paddingLeft() {
        return pixelString(defaultIfEmpty(super.get_paddingLeft(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_paddingRight() {
        return pixelString(defaultIfEmpty(super.get_paddingRight(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_paddingTop() {
        return pixelString(defaultIfEmpty(super.get_paddingTop(), "0px"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_pointerEvents() {
        return defaultIfEmpty(super.get_pointerEvents(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_position() {
        return defaultIfEmpty(super.get_position(), "static");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_right() {
        return defaultIfEmpty(super.get_right(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_tableLayout() {
        return defaultIfEmpty(super.get_tableLayout(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_textAlign() {
        return defaultIfEmpty(super.get_textAlign(), "start");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_textDecoration() {
        return defaultIfEmpty(super.get_textDecoration(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_textIndent() {
        return defaultIfEmpty(super.get_textIndent(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_textShadow() {
        String shadow = "";
        if (getBrowserVersion().hasFeature(
                BrowserVersionFeatures.CSS_TEXT_SHADOW_DEFAULT_NONE)) {
            shadow = "none";
        }
        return defaultIfEmpty(super.get_textShadow(), shadow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_textTransform() {
        return defaultIfEmpty(super.get_textTransform(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_top() {
        return defaultIfEmpty(super.get_top(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_unicodeBidi() {
        String unicodeBidi = "embed";
        if (getBrowserVersion().hasFeature(
                BrowserVersionFeatures.CSS_UNICODE_BIDI_DEFAULT_NORMAL)) {
            unicodeBidi = "normal";
        }

        return defaultIfEmpty(super.get_unicodeBidi(), unicodeBidi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_verticalAlign() {
        return defaultIfEmpty(super.get_verticalAlign(), "baseline");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_visibility() {
        return defaultIfEmpty(super.get_visibility(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_whiteSpace() {
        return defaultIfEmpty(super.get_whiteSpace(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_width() {
        if ("none".equals(get_display())) {
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

        final String display = get_display();
        if ("none".equals(display)) {
            width_ = Integer.valueOf(0);
            return 0;
        }

        int width;
        final String styleWidth = super.get_width();
        final DomNode parent = node.getParentNode();
        if (StringUtils.isEmpty(styleWidth) && parent instanceof HtmlElement) {
            // hack: TODO find a way to specify default values for different tags
            if (getElement() instanceof HTMLCanvasElement) {
                return 300;
            }

            // Width not explicitly set.
            final String cssFloat = get_cssFloat();
            if ("right".equals(cssFloat) || "left".equals(cssFloat)) {
                // We're floating; simplistic approximation: text content * pixels per character.
                width = getDomNodeOrDie().getTextContent().length() * PIXELS_PER_CHAR;
            }
            else if ("block".equals(display)) {
                // Block elements take up 100% of the parent's width.
                final HTMLElement parentJS = (HTMLElement) parent.getScriptObject();
                final String parentWidth = getWindow().getComputedStyle(parentJS, null).get_width();
                if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_DEFAULT_WIDTH_AUTO)
                        && "auto".equals(parentWidth)) {
                    width = Window.WINDOW_WIDTH;
                }
                else {
                    width = pixelValue(parentJS, new CssValue(Window.WINDOW_WIDTH) {
                        @Override public String get(final ComputedCSSStyleDeclaration style) {
                            return style.get_width();
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
                final int w = e.get_currentStyle().getCalculatedWidth(true, true);
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
        final boolean explicitHeightSpecified = super.get_height().length() > 0;

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

        if ("none".equals(get_display())) {
            height2_ = Integer.valueOf(0);
            return 0;
        }

        if (getElement() instanceof HTMLBodyElement) {
            height2_ = Integer.valueOf(Window.WINDOW_HEIGHT);
            return Window.WINDOW_HEIGHT;
        }

        final boolean explicitHeightSpecified = super.get_height().length() > 0;

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
                final ComputedCSSStyleDeclaration style = e.get_currentStyle();
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
            final ComputedCSSStyleDeclaration style = e.get_currentStyle();
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
        final String overflow = get_overflow();
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
                    final ComputedCSSStyleDeclaration style = e.get_currentStyle();
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
            final int margin = pixelValue(get_marginTop());
            top += margin;
        }

        if (includeBorder) {
            final int border = pixelValue(get_borderTopWidth());
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
            final int parentWidth = parent.get_currentStyle().getCalculatedWidth(false, false);
            left = parentWidth - pixelValue(r);
        }
        else if ("fixed".equals(p) && "auto".equals(l)) {
            // Fixed to the location at which the browser puts it via normal element flowing.
            final HTMLElement parent = (HTMLElement) getElement().getParentElement();
            left = pixelValue(parent.get_currentStyle().getLeftWithInheritance());
        }
        else if ("static".equals(p)) {
            // We need to calculate the horizontal displacement caused by *previous* siblings.
            left = 0;
            for (DomNode n = getDomNodeOrDie(); n != null; n = n.getPreviousSibling()) {
                if (n.getScriptObject() instanceof HTMLElement) {
                    final HTMLElement e = (HTMLElement) n.getScriptObject();
                    final String d = e.get_currentStyle().get_display();
                    if ("block".equals(d)) {
                        break;
                    }
                    else if (!"none".equals(d)) {
                        left += e.get_currentStyle().getCalculatedWidth(true, true);
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
            final int border = pixelValue(get_borderLeftWidth());
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
        String p = get_position();
        if ("inherit".equals(p)) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                p = (parent != null ? parent.get_currentStyle().getPositionWithInheritance() : "static");
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
        String left = get_left();
        if ("inherit".equals(left)) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                left = (parent != null ? parent.get_currentStyle().getLeftWithInheritance() : "auto");
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
        String right = get_right();
        if ("inherit".equals(right)) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                right = (parent != null ? parent.get_currentStyle().getRightWithInheritance() : "auto");
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
        String top = get_top();
        if ("inherit".equals(top)) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                top = (parent != null ? parent.get_currentStyle().getTopWithInheritance() : "auto");
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
        String bottom = get_bottom();
        if ("inherit".equals(bottom)) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = (HTMLElement) getElement().getParentElement();
                bottom = (parent != null ? parent.get_currentStyle().getBottomWithInheritance() : "auto");
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
        return pixelValue(get_marginLeft());
    }

    /**
     * Gets the right margin of the element.
     * @return the value in pixels
     */
    public int getMarginRight() {
        return pixelValue(get_marginRight());
    }

    /**
     * Gets the top margin of the element.
     * @return the value in pixels
     */
    public int getMarginTop() {
        return pixelValue(get_marginTop());
    }

    /**
     * Gets the bottom margin of the element.
     * @return the value in pixels
     */
    public int getMarginBottom() {
        return pixelValue(get_marginBottom());
    }

    /**
     * Gets the left padding of the element.
     * @return the value in pixels
     */
    public int getPaddingLeft() {
        return pixelValue(get_paddingLeft());
    }

    /**
     * Gets the right padding of the element.
     * @return the value in pixels
     */
    public int getPaddingRight() {
        return pixelValue(get_paddingRight());
    }

    /**
     * Gets the top padding of the element.
     * @return the value in pixels
     */
    public int getPaddingTop() {
        return pixelValue(get_paddingTop());
    }

    /**
     * Gets the bottom padding of the element.
     * @return the value in pixels
     */
    public int getPaddingBottom() {
        return pixelValue(get_paddingBottom());
    }

    private int getPaddingHorizontal() {
        if (paddingHorizontal_ == null) {
            paddingHorizontal_ =
                Integer.valueOf("none".equals(get_display()) ? 0 : getPaddingLeft() + getPaddingRight());
        }
        return paddingHorizontal_.intValue();
    }

    private int getPaddingVertical() {
        if (paddingVertical_ == null) {
            paddingVertical_ =
                Integer.valueOf("none".equals(get_display()) ? 0 : getPaddingTop() + getPaddingBottom());
        }
        return paddingVertical_.intValue();
    }

    /**
     * Gets the size of the left border of the element.
     * @return the value in pixels
     */
    public int getBorderLeft() {
        return pixelValue(get_borderLeftWidth());
    }

    /**
     * Gets the size of the right border of the element.
     * @return the value in pixels
     */
    public int getBorderRight() {
        return pixelValue(get_borderRightWidth());
    }

    /**
     * Gets the size of the top border of the element.
     * @return the value in pixels
     */
    public int getBorderTop() {
        return pixelValue(get_borderTopWidth());
    }

    /**
     * Gets the size of the bottom border of the element.
     * @return the value in pixels
     */
    public int getBorderBottom() {
        return pixelValue(get_borderBottomWidth());
    }

    private int getBorderHorizontal() {
        if (borderHorizontal_ == null) {
            borderHorizontal_ =
                Integer.valueOf("none".equals(get_display()) ? 0 : getBorderLeft() + getBorderRight());
        }
        return borderHorizontal_.intValue();
    }

    private int getBorderVertical() {
        if (borderVertical_ == null) {
            borderVertical_ =
                Integer.valueOf("none".equals(get_display()) ? 0 : getBorderTop() + getBorderBottom());
        }
        return borderVertical_.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_wordSpacing() {
        String wordSpacing = "0px";
        if (getBrowserVersion().hasFeature(
                BrowserVersionFeatures.CSS_WORD_SPACING_DEFAULT_NORMAL)) {
            wordSpacing = "normal";
        }

        return defaultIfEmpty(super.get_wordSpacing(), wordSpacing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get_wordWrap() {
        return defaultIfEmpty(super.get_wordWrap(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get_zIndex() {
        final Object response = super.get_zIndex();
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
