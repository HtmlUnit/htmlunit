/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.TREATS_POSITION_FIXED_LIKE_POSITION_STATIC;
import static org.apache.commons.lang.StringUtils.defaultIfEmpty;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.commons.lang.StringUtils;
import org.w3c.css.sac.Selector;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHead;
import com.gargoylesoftware.htmlunit.javascript.host.Text;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for a ComputedCSSStyleDeclaration.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class ComputedCSSStyleDeclaration extends CSSStyleDeclaration {

    private static final long serialVersionUID = -1883166331827717255L;

    /** The number of (horizontal) pixels to assume that each character occupies. */
    private static final int PIXELS_PER_CHAR = 10;

    /** The default element width (in pixels) for elements taking up the full "screen" width. */
    private static final int DEFAULT_ELEMENT_WIDTH = 1256;

    /**
     * Local modifications maintained here rather than in the element. We use a sorted
     * map so that results are deterministic and thus easily testable.
     */
    private SortedMap<String, StyleElement> localModifications_ = new TreeMap<String, StyleElement>();

    /** Maps element types to custom display types (display types that are not "block". */
    private Map<String, String> defaultDisplays_;

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
    protected String getStyleAttribute(final String name, final boolean camelCase) {
        String s = super.getStyleAttribute(name, camelCase);
        if (s.length() == 0 && isInheritable(name, camelCase)) {
            final HTMLElement parent = getElement().getParentHTMLElement();
            if (parent != null) {
                s = getWindow().jsxFunction_getComputedStyle(parent, null).getStyleAttribute(name, camelCase);
            }
        }
        return s;
    }

    /**
     * Returns <tt>true</tt> if the specified CSS property is inheritable from parent elements.
     * @param name the name of the style attribute to check for inheritability
     * @param camelCase whether or not the name is expected to be in camel case
     * @return <tt>true</tt> if the specified CSS property is inheritable from parent elements
     * @see <a href="http://www.w3.org/TR/CSS21/propidx.html">CSS Property Table</a>
     */
    private boolean isInheritable(String name, final boolean camelCase) {
        if (!camelCase) {
            name = camelize(name);
        }
        return    "azimuth".equals(name)
               || "borderCollapse".equals(name)
               || "borderSpacing".equals(name)
               || "captionSide".equals(name)
               || "color".equals(name)
               || "cursor".equals(name)
               || "direction".equals(name)
               || "elevation".equals(name)
               || "emptyCells".equals(name)
               || "fontFamily".equals(name)
               || "fontSize".equals(name)
               || "fontStyle".equals(name)
               || "fontVariant".equals(name)
               || "fontWeight".equals(name)
               || "font".equals(name)
               || "letterSpacing".equals(name)
               || "lineHeight".equals(name)
               || "listStyleImage".equals(name)
               || "listStylePosition".equals(name)
               || "listStyleType".equals(name)
               || "listStyle".equals(name)
               || "orphans".equals(name)
               || "pitchRange".equals(name)
               || "pitch".equals(name)
               || "quotes".equals(name)
               || "richness".equals(name)
               || "speakHeader".equals(name)
               || "speakNumeral".equals(name)
               || "speakPunctuation".equals(name)
               || "speak".equals(name)
               || "speechRate".equals(name)
               || "stress".equals(name)
               || "textAlign".equals(name)
               || "textIndent".equals(name)
               || "textTransform".equals(name)
               || "visibility".equals(name)
               || "voiceFamily".equals(name)
               || "volume".equals(name)
               || "whiteSpace".equals(name)
               || "widows".equals(name)
               || "wordSpacing".equals(name);
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
        if (!"important".equals(priority)) {
            final StyleElement existingElement = localModifications_.get(name);
            if (existingElement != null) {
                if ("important".equals(existingElement.getPriority())) {
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
    protected Map<String, StyleElement> getStyleMap(final boolean camelCase) {
        final Map<String, StyleElement> styleMap = super.getStyleMap(camelCase);
        if (localModifications_ != null) {
            for (final StyleElement e : localModifications_.values()) {
                String key = e.getName();
                if (camelCase) {
                    key = camelize(key);
                }
                final StyleElement existent = styleMap.get(key);
                final StyleElement element = new StyleElement(key, e.getValue(), e.getIndex());
                if (existent == null) {
                    // Local modifications represent either default style elements or style elements
                    // defined in stylesheets; either way, they shouldn't overwrite any style
                    // elements derived directly from the HTML element's "style" attribute.
                    styleMap.put(key, element);
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
        else if (getBrowserVersion().isFirefox()) {
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
            if (!getBrowserVersion().isIE()) {
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
        if (value.length() == 0) {
            final HTMLElement parent = getElement().getParentHTMLElement();
            if (parent != null) {
                value = parent.jsxGet_currentStyle().jsxGet_fontSize();
            }
        }
        if (value.length() == 0) {
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
        return pixelString(defaultIfEmpty(super.jsxGet_height(), "363px"));
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
        return defaultIfEmpty(super.jsxGet_lineHeight(), "normal");
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
        return defaultIfEmpty(super.jsxGet_markerOffset(), "none");
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
        return defaultIfEmpty(super.jsxGet_MozColumnGap(), "0px");
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
        return defaultIfEmpty(super.jsxGet_unicodeBidi(), "normal");
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
        if (jsxGet_display().equals("none")) {
            return "auto";
        }
        final String defaultWidth;
        if (getBrowserVersion().isIE()) {
            defaultWidth = "auto";
        }
        else {
            defaultWidth = DEFAULT_ELEMENT_WIDTH + "px";
        }
        return pixelString(defaultIfEmpty(super.jsxGet_width(), defaultWidth));
    }

    /**
     * Returns the element's width in pixels, possibly including its padding and border.
     * @param includeBorder whether or not to include the border width in the returned value
     * @param includePadding whether or not to include the padding width in the returned value
     * @return the element's width in pixels, possibly including its padding and border
     */
    public int getCalculatedWidth(final boolean includeBorder, final boolean includePadding) {
        final String display = jsxGet_display();
        if ("none".equals(display)) {
            return 0;
        }
        int width;
        final String styleWidth = super.jsxGet_width();
        final DomNode parent = getElement().getDomNodeOrDie().getParentNode();
        if (StringUtils.isEmpty(styleWidth) && parent instanceof HtmlElement) {
            // Width not explicitly set.
            final String cssFloat = jsxGet_cssFloat();
            if ("right".equals(cssFloat) || "left".equals(cssFloat)) {
                // We're floating; simplistic approximation: text content * pixels per character.
                width = this.<DomNode>getDomNodeOrDie().getTextContent().length() * PIXELS_PER_CHAR;
            }
            else if ("block".equals(display)) {
                // Block elements take up 100% of the parent's width.
                final HTMLElement parentJS = (HTMLElement) parent.getScriptObject();
                final String parentWidth = getWindow().jsxFunction_getComputedStyle(parentJS, null).jsxGet_width();
                if (getBrowserVersion().isIE() && "auto".equals(parentWidth)) {
                    width = DEFAULT_ELEMENT_WIDTH;
                }
                else {
                    width = pixelValue(parentWidth);
                }
            }
            else {
                // Inline elements take up however much space is required by their children.
                width = 0;
                for (DomNode child : this.<DomNode>getDomNodeOrDie().getChildren()) {
                    if (child.getScriptObject() instanceof HTMLElement) {
                        final HTMLElement e = (HTMLElement) child.getScriptObject();
                        final int w = e.jsxGet_currentStyle().getCalculatedWidth(true, true);
                        width += w;
                    }
                    else if (child.getScriptObject() instanceof Text) {
                        width += child.getTextContent().length() * PIXELS_PER_CHAR;
                    }
                }
            }
        }
        else {
            // Width explicitly set in the style attribute, or there was no parent to provide guidance.
            width = pixelValue(styleWidth);
            if (includeBorder) {
                final int borderLeft = pixelValue(jsxGet_borderLeftWidth());
                final int borderRight = pixelValue(jsxGet_borderRightWidth());
                width += borderLeft + borderRight;
            }
            if (includePadding) {
                final int paddingLeft = getPaddingLeft();
                final int paddingRight = pixelValue(jsxGet_paddingRight());
                width += paddingLeft + paddingRight;
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
        if ("none".equals(jsxGet_display())) {
            return 0;
        }

        final boolean ie = getBrowserVersion().isIE();
        final int defaultHeight = (ie ? 15 : 20);

        final String h = super.jsxGet_height();
        int elementHeight = pixelValue(h);
        if (elementHeight == 0 || (ie && elementHeight < defaultHeight)) {
            elementHeight = defaultHeight;
        }

        Integer childrenHeight = null;
        for (DomNode child : this.<DomNode>getDomNodeOrDie().getChildren()) {
            if (child.getScriptObject() instanceof HTMLElement) {
                final HTMLElement e = (HTMLElement) child.getScriptObject();
                final int x = e.jsxGet_currentStyle().getCalculatedHeight(includeBorder, includePadding);
                childrenHeight = (childrenHeight != null ? childrenHeight + x : x);
            }
        }

        int height;
        if (childrenHeight != null && ((ie && childrenHeight > elementHeight) || (!ie && h.length() == 0))) {
            height = childrenHeight;
        }
        else {
            height = elementHeight;
        }

        if (includeBorder) {
            final int borderTop = pixelValue(jsxGet_borderTopWidth());
            final int borderBottom = pixelValue(jsxGet_borderBottomWidth());
            height += borderTop + borderBottom;
        }

        if (includePadding) {
            final int paddingTop = getPaddingTop();
            final int paddingBottom = pixelValue(jsxGet_paddingBottom());
            height += paddingTop + paddingBottom;
        }

        return height;
    }

    /**
     * Returns the computed top (Y coordinate), relative to the node's parent's top edge.
     * @param includeMargin whether or not to take the margin into account in the calculation
     * @param includeBorder whether or not to take the border into account in the calculation
     * @param includePadding whether or not to take the padding into account in the calculation
     * @return the computed top (Y coordinate), relative to the node's parent's top edge
     */
    public int getTop(final boolean includeMargin, final boolean includeBorder, final boolean includePadding) {
        final String p = getPositionWithInheritance();
        final String t = getTopWithInheritance();
        final String b = getBottomWithInheritance();

        int top;
        if ("absolute".equals(p) && !"auto".equals(t)) {
            // No need to calculate displacement caused by sibling nodes.
            top = pixelValue(t);
        }
        else if ("absolute".equals(p) && !"auto".equals(b)) {
            // Estimate the vertical displacement caused by *all* siblings.
            // This is very rough, and doesn't even take position or display types into account (hence the
            // need for the explicit check for HtmlHead elements, which are display:none in regular UAs).
            // It also doesn't take into account the fact that the parent's height may be hardcoded in CSS.
            top = 0;
            DomNode child = this.getElement().getDomNodeOrDie().getParentNode().getFirstChild();
            while (child != null) {
                if (child instanceof HtmlElement && !(child instanceof HtmlHead)) {
                    top += 20;
                }
                child = child.getPreviousSibling();
            }
            top -= pixelValue(b);
        }
        else {
            // Estimate the vertical displacement caused by *previous* siblings.
            // This is very rough, and doesn't even take position or display types into account (hence the
            // need for the explicit check for HtmlHead elements, which are display:none in regular UAs).
            top = 0;
            DomNode prev = this.getElement().getDomNodeOrDie().getPreviousSibling();
            while (prev != null) {
                if (prev instanceof HtmlElement && !(prev instanceof HtmlHead)) {
                    top += 20;
                }
                prev = prev.getPreviousSibling();
            }
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
            final HTMLElement parent = getElement().getParentHTMLElement();
            final int parentWidth = parent.jsxGet_currentStyle().getCalculatedWidth(false, false);
            left = parentWidth - pixelValue(r);
        }
        else if ("fixed".equals(p) && "auto".equals(l)) {
            // Fixed to the location at which the browser puts it via normal element flowing.
            final HTMLElement parent = getElement().getParentHTMLElement();
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
            if (getBrowserVersion().hasFeature(CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = getElement().getParentHTMLElement();
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
            if (getBrowserVersion().hasFeature(CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = getElement().getParentHTMLElement();
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
            if (getBrowserVersion().hasFeature(CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = getElement().getParentHTMLElement();
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
            if (getBrowserVersion().hasFeature(CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = getElement().getParentHTMLElement();
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
            if (getBrowserVersion().hasFeature(CAN_INHERIT_CSS_PROPERTY_VALUES)) {
                final HTMLElement parent = getElement().getParentHTMLElement();
                bottom = (parent != null ? parent.jsxGet_currentStyle().getBottomWithInheritance() : "auto");
            }
            else {
                bottom = "auto";
            }
        }
        return bottom;
    }

    /**
     * Gets the top padding of the element.
     * @return the value in pixels
     */
    public int getPaddingTop() {
        return pixelValue(jsxGet_paddingTop());
    }

    /**
     * Gets the top margin of the element.
     * @return the value in pixels
     */
    public int getMarginTop() {
        return pixelValue(jsxGet_marginTop());
    }

    /**
     * Gets the left padding of the element.
     * @return the value in pixels
     */
    public int getPaddingLeft() {
        return pixelValue(jsxGet_paddingLeft());
    }

    /**
     * Gets the left margin of the element.
     * @return the value in pixels
     */
    public int getMarginLeft() {
        return pixelValue(jsxGet_marginLeft());
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
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_wordSpacing() {
        return defaultIfEmpty(super.jsxGet_wordSpacing(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object jsxGet_zIndex() {
        final Object response = super.jsxGet_zIndex();
        if (response.toString().length() == 0) {
            return "auto";
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxFunction_getPropertyValue(final String name) {
        // need to invoke the getter to take care of the default value
        final String response = Context.toString(getProperty(this, camelize(name)));
        if (response == NOT_FOUND) {
            return super.jsxFunction_getPropertyValue(name);
        }
        return response;
    }

    /**
     * Returns the specified length value as a pixel length value, as long as we're not emulating IE.
     * @param value the length value to convert to a pixel length value
     * @return the specified length value as a pixel length value
     */
    protected String pixelString(final String value) {
        if (getBrowserVersion().isIE()) {
            return value;
        }
        if (value.endsWith("px")) {
            return value;
        }
        return pixelValue(value) + "px";
    }

}
