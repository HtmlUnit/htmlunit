/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.math.NumberUtils;
import org.mozilla.javascript.Context;

/**
 * A JavaScript object for a ComputedCSSStyleDeclaration.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class ComputedCSSStyleDeclaration extends CSSStyleDeclaration {

    private static final long serialVersionUID = -1883166331827717255L;

    /**
     * Local modifications maintained here rather than in the element. We use a sorted
     * map so that results are deterministic and thus easily testable.
     */
    private SortedMap<String, StyleElement> localModifications_ = new TreeMap<String, StyleElement>();

    private static final Map<String, String> DEFAULT_DISPLAY;

    static {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("A", "inline");
        map.put("CODE", "inline");
        map.put("LI", "list-item");
        map.put("SPAN", "inline");
        map.put("TABLE", "table");
        map.put("TBODY", "table-row-group");
        map.put("TD", "table-cell");
        map.put("TH", "table-cell");
        map.put("THEAD", "table-header-group");
        map.put("TR", "table-row");
        
        DEFAULT_DISPLAY = Collections.unmodifiableMap(map);
    }

    /**
     * Create an instance. JavaScript objects must have a default constructor.
     */
    public ComputedCSSStyleDeclaration() {
        // Empty.
    }

    /**
     * @param style the original Style.
     */
    ComputedCSSStyleDeclaration(final CSSStyleDeclaration style) {
        super(style.getHTMLElement());
        getHTMLElement().setDefaults(this);
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
     * @param name the name of the style attribute to set
     * @param newValue the value of the style attribute to set
     */
    void setLocalStyleAttribute(final String name, final String newValue) {
        final StyleElement element = new StyleElement(name, newValue, getCurrentElementIndex());
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
    void setDefaultLocalStyleAttribute(final String name, final String newValue) {
        final StyleElement element = new StyleElement(name, newValue);
        localModifications_.put(name, element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SortedMap<String, StyleElement> getStyleMap(final boolean camelCase) {
        final SortedMap<String, StyleElement> styleMap = super.getStyleMap(camelCase);
        if (localModifications_ != null) {
            for (final StyleElement e : localModifications_.values()) {
                String key = e.getName();
                if (camelCase) {
                    key = camelize(key);
                }
                final StyleElement existent = styleMap.get(key);
                final StyleElement element = new StyleElement(key, e.getValue(), e.getIndex());
                if (existent == null || !element.isDefault()) {
                    styleMap.put(key, element);
                }
            }
        }
        return styleMap;
    }

    private String getValue(final String currentValue, final String defaultValue) {
        if (currentValue.length() == 0) {
            return defaultValue;
        }
        else {
            return currentValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_backgroundAttachment() {
        return getValue(super.jsxGet_backgroundAttachment(), "scroll");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_backgroundColor() {
        return getValue(super.jsxGet_backgroundColor(), "transparent");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_backgroundImage() {
        return getValue(super.jsxGet_backgroundImage(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_backgroundRepeat() {
        return getValue(super.jsxGet_backgroundRepeat(), "repeat");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderBottomColor() {
        return getValue(super.jsxGet_borderBottomColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderBottomStyle() {
        return getValue(super.jsxGet_borderBottomStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderBottomWidth() {
        return getValue(super.jsxGet_borderBottomWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderCollapse() {
        return getValue(super.jsxGet_borderCollapse(), "separate");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderLeftColor() {
        return getValue(super.jsxGet_borderLeftColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderLeftStyle() {
        return getValue(super.jsxGet_borderLeftStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderLeftWidth() {
        return getValue(super.jsxGet_borderLeftWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderRightColor() {
        return getValue(super.jsxGet_borderRightColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderRightStyle() {
        return getValue(super.jsxGet_borderRightStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderRightWidth() {
        return getValue(super.jsxGet_borderRightWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderSpacing() {
        return getValue(super.jsxGet_borderSpacing(), "0px 0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderTopColor() {
        return getValue(super.jsxGet_borderTopColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderTopStyle() {
        return getValue(super.jsxGet_borderTopStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_borderTopWidth() {
        return getValue(super.jsxGet_borderTopWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_bottom() {
        return getValue(super.jsxGet_bottom(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_captionSide() {
        return getValue(super.jsxGet_captionSide(), "top");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_clear() {
        return getValue(super.jsxGet_clear(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_clip() {
        return getValue(super.jsxGet_clip(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_color() {
        return getValue(super.jsxGet_color(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_counterIncrement() {
        return getValue(super.jsxGet_counterIncrement(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_counterReset() {
        return getValue(super.jsxGet_counterReset(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_cssFloat() {
        return getValue(super.jsxGet_cssFloat(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_cursor() {
        return getValue(super.jsxGet_cursor(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_direction() {
        return getValue(super.jsxGet_direction(), "ltr");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_display() {
        return getValue(super.jsxGet_display(), getDefaultStyleDisplay());
    }

    private String getDefaultStyleDisplay() {
        final String defaultValue = DEFAULT_DISPLAY.get(getHTMLElement().jsxGet_tagName());
        if (defaultValue == null) {
            return "block";
        }
        else {
            return defaultValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_emptyCells() {
        return getValue(super.jsxGet_emptyCells(), "-moz-show-background");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontFamily() {
        return getValue(super.jsxGet_fontFamily(), "serif");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontSize() {
        return getValue(super.jsxGet_fontSize(), "16px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontSizeAdjust() {
        return getValue(super.jsxGet_fontSizeAdjust(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontStyle() {
        return getValue(super.jsxGet_fontStyle(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontVariant() {
        return getValue(super.jsxGet_fontVariant(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_fontWeight() {
        return getValue(super.jsxGet_fontWeight(), "400");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_height() {
        return getValue(super.jsxGet_height(), "363px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_left() {
        return getValue(super.jsxGet_left(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_letterSpacing() {
        return getValue(super.jsxGet_letterSpacing(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_lineHeight() {
        return getValue(super.jsxGet_lineHeight(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_listStyleImage() {
        return getValue(super.jsxGet_listStyleImage(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_listStylePosition() {
        return getValue(super.jsxGet_listStylePosition(), "outside");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_listStyleType() {
        return getValue(super.jsxGet_listStyleType(), "disc");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_marginBottom() {
        return getValue(super.jsxGet_marginBottom(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_marginLeft() {
        return getValue(super.jsxGet_marginLeft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_marginRight() {
        return getValue(super.jsxGet_marginRight(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_marginTop() {
        return getValue(super.jsxGet_marginTop(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_markerOffset() {
        return getValue(super.jsxGet_markerOffset(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_maxHeight() {
        return getValue(super.jsxGet_maxHeight(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_maxWidth() {
        return getValue(super.jsxGet_maxWidth(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_minHeight() {
        return getValue(super.jsxGet_minHeight(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_minWidth() {
        return getValue(super.jsxGet_minWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozAppearance() {
        return getValue(super.jsxGet_MozAppearance(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBackgroundClip() {
        return getValue(super.jsxGet_MozBackgroundClip(), "border");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBackgroundInlinePolicy() {
        return getValue(super.jsxGet_MozBackgroundInlinePolicy(), "continuous");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBackgroundOrigin() {
        return getValue(super.jsxGet_MozBackgroundOrigin(), "padding");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBinding() {
        return getValue(super.jsxGet_MozBinding(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderBottomColors() {
        return getValue(super.jsxGet_MozBorderBottomColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderLeftColors() {
        return getValue(super.jsxGet_MozBorderLeftColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderRadiusBottomleft() {
        return getValue(super.jsxGet_MozBorderRadiusBottomleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderRadiusBottomright() {
        return getValue(super.jsxGet_MozBorderRadiusBottomright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderRadiusTopleft() {
        return getValue(super.jsxGet_MozBorderRadiusTopleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderRadiusTopright() {
        return getValue(super.jsxGet_MozBorderRadiusTopright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderRightColors() {
        return getValue(super.jsxGet_MozBorderRightColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBorderTopColors() {
        return getValue(super.jsxGet_MozBorderTopColors(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxAlign() {
        return getValue(super.jsxGet_MozBoxAlign(), "stretch");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxDirection() {
        return getValue(super.jsxGet_MozBoxDirection(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxFlex() {
        return getValue(super.jsxGet_MozBoxFlex(), "0");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxOrdinalGroup() {
        return getValue(super.jsxGet_MozBoxOrdinalGroup(), "1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxOrient() {
        return getValue(super.jsxGet_MozBoxOrient(), "horizontal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxPack() {
        return getValue(super.jsxGet_MozBoxPack(), "start");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozBoxSizing() {
        return getValue(super.jsxGet_MozBoxSizing(), "content-box");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozColumnCount() {
        return getValue(super.jsxGet_MozColumnCount(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozColumnGap() {
        return getValue(super.jsxGet_MozColumnGap(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozColumnWidth() {
        return getValue(super.jsxGet_MozColumnWidth(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozFloatEdge() {
        return getValue(super.jsxGet_MozFloatEdge(), "content-box");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozImageRegion() {
        return getValue(super.jsxGet_MozImageRegion(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOpacity() {
        return getValue(super.jsxGet_MozOpacity(), "1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineColor() {
        return getValue(super.jsxGet_MozOutlineColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineOffset() {
        return getValue(super.jsxGet_MozOutlineOffset(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineRadiusBottomleft() {
        return getValue(super.jsxGet_MozOutlineRadiusBottomleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineRadiusBottomright() {
        return getValue(super.jsxGet_MozOutlineRadiusBottomright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineRadiusTopleft() {
        return getValue(super.jsxGet_MozOutlineRadiusTopleft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineRadiusTopright() {
        return getValue(super.jsxGet_MozOutlineRadiusTopright(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineStyle() {
        return getValue(super.jsxGet_MozOutlineStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozOutlineWidth() {
        return getValue(super.jsxGet_MozOutlineWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozUserFocus() {
        return getValue(super.jsxGet_MozUserFocus(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozUserInput() {
        return getValue(super.jsxGet_MozUserInput(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozUserModify() {
        return getValue(super.jsxGet_MozUserModify(), "read-only");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_MozUserSelect() {
        return getValue(super.jsxGet_MozUserSelect(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_opacity() {
        return getValue(super.jsxGet_opacity(), "1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_outlineColor() {
        return getValue(super.jsxGet_outlineColor(), "rgb(0, 0, 0)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_outlineOffset() {
        return getValue(super.jsxGet_outlineOffset(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_outlineStyle() {
        return getValue(super.jsxGet_outlineStyle(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_outlineWidth() {
        return getValue(super.jsxGet_outlineWidth(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_overflow() {
        return getValue(super.jsxGet_overflow(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_overflowX() {
        return getValue(super.jsxGet_overflowX(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_overflowY() {
        return getValue(super.jsxGet_overflowY(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_paddingBottom() {
        return getValue(super.jsxGet_paddingBottom(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_paddingLeft() {
        return getValue(super.jsxGet_paddingLeft(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_paddingRight() {
        return getValue(super.jsxGet_paddingRight(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_paddingTop() {
        return getValue(super.jsxGet_paddingTop(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_position() {
        return getValue(super.jsxGet_position(), "static");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_right() {
        return getValue(super.jsxGet_right(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_tableLayout() {
        return getValue(super.jsxGet_tableLayout(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_textAlign() {
        return getValue(super.jsxGet_textAlign(), "start");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_textDecoration() {
        return getValue(super.jsxGet_textDecoration(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_textIndent() {
        return getValue(super.jsxGet_textIndent(), "0px");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_textTransform() {
        return getValue(super.jsxGet_textTransform(), "none");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_top() {
        return getValue(super.jsxGet_top(), "auto");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_unicodeBidi() {
        return getValue(super.jsxGet_unicodeBidi(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_verticalAlign() {
        return getValue(super.jsxGet_verticalAlign(), "baseline");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_visibility() {
        return getValue(super.jsxGet_visibility(), "visible");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_whiteSpace() {
        return getValue(super.jsxGet_whiteSpace(), "normal");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_width() {
        final String defaultWidth;
        if (jsxGet_display().equals("none")) {
            defaultWidth = "auto";
        }
        else {
            defaultWidth = "1256px";
        }
        return getValue(super.jsxGet_width(), defaultWidth);
    }

    /**
     * Returns the element's width, possibly including its padding and border
     * @return the element's width, possibly including its padding and border
     */
    int getCalculatedWidth(final boolean includeBorder, final boolean includePadding) {
        int width = intValue(super.jsxGet_width());
        if (includeBorder) {
            final int borderLeft = intValue(jsxGet_borderLeftWidth());
            final int borderRight = intValue(jsxGet_borderRightWidth());
            width += borderLeft + borderRight;
        }
        if (includePadding) {
            final int paddingLeft = intValue(jsxGet_paddingLeft());
            final int paddingRight = intValue(jsxGet_paddingRight());
            width += paddingLeft + paddingRight;
        }
        return width;
    }

    /**
     * Returns the element's height, possibly including its padding and border
     * @return the element's height, possibly including its padding and border
     */
    int getCalculatedHeight(final boolean includeBorder, final boolean includePadding) {
        int height = intValue(super.jsxGet_height());
        if (includeBorder) {
            final int borderTop = intValue(jsxGet_borderTopWidth());
            final int borderBottom = intValue(jsxGet_borderBottomWidth());
            height += borderTop + borderBottom;
        }
        if (includePadding) {
            final int paddingTop = intValue(jsxGet_paddingTop());
            final int paddingBottom = intValue(jsxGet_paddingBottom());
            height += paddingTop + paddingBottom;
        }
        return height;
    }

    /**
     * Returns the computed top (Y coordinate), relative to the node's parent.
     * @return the computed top (Y coordinate), relative to the node's parent
     */
    int getTop(final boolean includeMargin, final boolean includeBorder, final boolean includePadding) {
        int top = intValue(jsxGet_top());
        if (includeMargin) {
            final int margin = intValue(jsxGet_marginTop());
            top += margin;
        }
        if (includeBorder) {
            final int border = intValue(jsxGet_borderTopWidth());
            top += border;
        }
        if (includePadding) {
            final int padding = intValue(jsxGet_paddingTop());
            top += padding;
        }
        return top;
    }

    /**
     * Returns the computed left (X coordinate), relative to the node's parent.
     * @return the computed left (X coordinate), relative to the node's parent
     */
    int getLeft(final boolean includeMargin, final boolean includeBorder, final boolean includePadding) {
        int left = intValue(jsxGet_left());
        if (includeMargin) {
            final int margin = intValue(jsxGet_marginLeft());
            left += margin;
        }
        if (includeBorder) {
            final int border = intValue(jsxGet_borderLeftWidth());
            left += border;
        }
        if (includePadding) {
            final int padding = intValue(jsxGet_paddingLeft());
            left += padding;
        }
        return left;
    }

    private int intValue(final String value) {
        return NumberUtils.toInt(value.replaceAll("(\\d+).*", "$1"), 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_wordSpacing() {
        return getValue(super.jsxGet_wordSpacing(), "normal");
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
}
