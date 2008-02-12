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

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a CSSStyleDeclaration.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class CSSStyleDeclaration extends SimpleScriptable implements Cloneable {

    private static final long serialVersionUID = -1976370264911039311L;

    /** Used to parse URLs. */
    private static final MessageFormat URL_FORMAT = new MessageFormat("url({0})");

    /** The element to which this style belongs. */
    private HTMLElement jsElement_;

    /**
     * Create an instance. Javascript objects must have a default constructor.
     */
    public CSSStyleDeclaration() {
        // Empty.
    }

    /**
     * Create an instance and set its parent scope to the one of the provided element.
     * @param htmlElement the element to which this style is bound.
     */
    CSSStyleDeclaration(final HTMLElement htmlElement) {
        setParentScope(htmlElement.getParentScope());
        setPrototype(getPrototype(getClass()));
        initialize(htmlElement);
    }

    HTMLElement getHTMLElement() {
        return jsElement_;
    }

    /**
     * Initializes the object.
     *
     * @param htmlElement The element that this style describes.
     */
    void initialize(final HTMLElement htmlElement) {
        // Initialize.
        WebAssert.notNull("htmlElement", htmlElement);
        jsElement_ = htmlElement;
        setDomNode(htmlElement.getDomNodeOrNull(), false);

        if (htmlElement.getDomNodeOrDie().getPage().getEnclosingWindow().getWebClient()
                .getBrowserVersion().isIE()) {
            // If a behavior was specified in the style, apply the behavior.
            for (final Map.Entry<String, String> entry : getStyleMap(true).entrySet()) {
                final String key = entry.getKey();
                if ("behavior".equals(key)) {
                    final String value = entry.getValue();
                    try {
                        final Object[] url = URL_FORMAT.parse(value);
                        if (url.length > 0) {
                            jsElement_.jsxFunction_addBehavior((String) url[0]);
                            break;
                        }
                    }
                    catch (final ParseException e) {
                        getLog().warn("Invalid behavior: '" + value + "'.");
                    }
                }
            }
        }
    }

    /**
     * Creates a clone of this style.
     * @return a clone of this style.
     */
    CSSStyleDeclaration createClone() {
        try {
            return (CSSStyleDeclaration) clone();
        }
        catch (final CloneNotSupportedException e) {
            // Should never happen.
            getLog().error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Returns the named style attribute value.
     * @param name the style attribute name
     * @param camelCase whether or not the name is expected to be in camel case
     * @return empty string if noting found
     */
    protected String getStyleAttribute(final String name, final boolean camelCase) {
        final String value = getStyleMap(camelCase).get(name);
        if (value == null) {
            return "";
        }
        else {
            return value;
        }
    }

    /**
     * Sets the specified style attribute.
     * @param name the attribute name (camel-cased)
     * @param newValue the attribute value
     */
    protected void setStyleAttribute(String name, final String newValue) {
        name = name.replaceAll("([A-Z])", "-$1").toLowerCase();
        removeStyleAttribute(name);
        final Map<String, String> styleMap = getStyleMap(true);
        if (newValue.trim().length() != 0) {
            styleMap.put(name, newValue);

            final StringBuilder buffer = new StringBuilder();
            for (final Map.Entry<String, String> entry : styleMap.entrySet()) {
                buffer.append(" ");
                buffer.append(entry.getKey());
                buffer.append(": ");
                buffer.append(entry.getValue());
                buffer.append(";");
            }
            buffer.deleteCharAt(0);

            jsElement_.getHtmlElementOrDie().setAttributeValue("style", buffer.toString());
        }
    }

    /**
     * Returns a sorted map containing style elements, keyed on style element name. We use a sorted
     * map so that results are deterministic and are thus testable.
     *
     * @param camelCase if <tt>true</tt>, the keys are camel cased (i.e. <tt>fontSize</tt>),
     *        if <tt>false</tt>, the keys are delimiter-separated (i.e. <tt>font-size</tt>).
     * @return a sorted map containing style elements, keyed on style element name
     */
    protected SortedMap<String, String> getStyleMap(final boolean camelCase) {
        final SortedMap<String, String> styleMap = new TreeMap<String, String>();
        final String styleAttribute = jsElement_.getHtmlElementOrDie().getAttributeValue("style");
        for (final String token : styleAttribute.split(";")) {
            final int index = token.indexOf(":");
            if (index != -1) {
                String key = token.substring(0, index).trim();
                if (camelCase) {
                    key = camelize(key);
                }
                final String value = token.substring(index + 1).trim();
                styleMap.put(key, value);
            }
        }
        return styleMap;
    }

    /**
     * Removes the specified style attribute.
     * @param name The attribute name (delimiter-separated, not camel-cased).
     */
    protected void removeStyleAttribute(final String name) {
        final SortedMap<String, String> styleMap = getStyleMap(false);
        styleMap.remove(name);
        final StringBuilder buffer = new StringBuilder();
        for (final String style : styleMap.keySet()) {
            buffer.append(style).append(':').append(styleMap.get(style)).append(';');
        }
        if (buffer.length() != 0) {
            buffer.setLength(buffer.length() - 1);
        }
        jsElement_.getHtmlElementOrDie().setAttributeValue("style", buffer.toString());
    }

    /**
     * Transform the given string from delimiter-separated (e.g. <tt>font-size</tt>)
     * to camel cased (e.g. <tt>fontSize</tt>).
     * @param string the string to camelize.
     * @return the transformed string.
     */
    protected static String camelize(final String string) {
        if (string == null) {
            return null;
        }
        final StringBuilder buffer = new StringBuilder(string);
        for (int i = 0; i < buffer.length() - 1; i++) {
            if (buffer.charAt(i) == '-') {
                buffer.deleteCharAt(i);
                buffer.setCharAt(i, Character.toUpperCase(buffer.charAt(i)));
            }
        }
        return buffer.toString();
    }

    /**
     * Gets the "azimuth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_azimuth() {
        return getStyleAttribute("azimuth", true);
    }

    /**
     * Sets the "azimuth" style attribute.
     * @param azimuth the new attribute.
     */
    public void jsxSet_azimuth(final String azimuth) {
        setStyleAttribute("azimuth", azimuth);
    }

    /**
     * Gets the "background" style attribute.
     * @return the style attribute
     */
    public String jsxGet_background() {
        return getStyleAttribute("background", true);
    }

    /**
     * Sets the "background" style attribute.
     * @param background the new attribute.
     */
    public void jsxSet_background(final String background) {
        setStyleAttribute("background", background);
    }

    /**
     * Gets the "backgroundAttachment" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundAttachment() {
        return getStyleAttribute("backgroundAttachment", true);
    }

    /**
     * Sets the "backgroundAttachment" style attribute.
     * @param backgroundAttachment the new attribute.
     */
    public void jsxSet_backgroundAttachment(final String backgroundAttachment) {
        setStyleAttribute("backgroundAttachment", backgroundAttachment);
    }

    /**
     * Gets the "backgroundColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundColor() {
        return getStyleAttribute("backgroundColor", true);
    }

    /**
     * Sets the "backgroundColor" style attribute.
     * @param backgroundColor the new attribute.
     */
    public void jsxSet_backgroundColor(final String backgroundColor) {
        setStyleAttribute("backgroundColor", backgroundColor);
    }

    /**
     * Gets the "backgroundImage" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundImage() {
        return getStyleAttribute("backgroundImage", true);
    }

    /**
     * Sets the "backgroundImage" style attribute.
     * @param backgroundImage the new attribute.
     */
    public void jsxSet_backgroundImage(final String backgroundImage) {
        setStyleAttribute("backgroundImage", backgroundImage);
    }

    /**
     * Gets the "backgroundPosition" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundPosition() {
        return getStyleAttribute("backgroundPosition", true);
    }

    /**
     * Sets the "backgroundPosition" style attribute.
     * @param backgroundPosition the new attribute.
     */
    public void jsxSet_backgroundPosition(final String backgroundPosition) {
        setStyleAttribute("backgroundPosition", backgroundPosition);
    }

    /**
     * Gets the "backgroundPositionX" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundPositionX() {
        return getStyleAttribute("backgroundPositionX", true);
    }

    /**
     * Sets the "backgroundPositionX" style attribute.
     * @param backgroundPositionX the new attribute.
     */
    public void jsxSet_backgroundPositionX(final String backgroundPositionX) {
        setStyleAttribute("backgroundPositionX", backgroundPositionX);
    }

    /**
     * Gets the "backgroundPositionY" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundPositionY() {
        return getStyleAttribute("backgroundPositionY", true);
    }

    /**
     * Sets the "backgroundPositionY" style attribute.
     * @param backgroundPositionY the new attribute.
     */
    public void jsxSet_backgroundPositionY(final String backgroundPositionY) {
        setStyleAttribute("backgroundPositionY", backgroundPositionY);
    }

    /**
     * Gets the "backgroundRepeat" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundRepeat() {
        return getStyleAttribute("backgroundRepeat", true);
    }

    /**
     * Sets the "backgroundRepeat" style attribute.
     * @param backgroundRepeat the new attribute.
     */
    public void jsxSet_backgroundRepeat(final String backgroundRepeat) {
        setStyleAttribute("backgroundRepeat", backgroundRepeat);
    }

    /**
     * Gets the object's behavior (IE only).
     * @return the object's behavior
     */
    public String jsxGet_behavior() {
        return getStyleAttribute("behavior", true);
    }

    /**
     * Sets the object's behavior (IE only).
     * @param behavior the new behavior
     */
    public void jsxSet_behavior(final String behavior) {
        setStyleAttribute("behavior", behavior);
        jsElement_.jsxFunction_removeBehavior(HTMLElement.BEHAVIOR_ID_CLIENT_CAPS);
        jsElement_.jsxFunction_removeBehavior(HTMLElement.BEHAVIOR_ID_HOMEPAGE);
        jsElement_.jsxFunction_removeBehavior(HTMLElement.BEHAVIOR_ID_DOWNLOAD);
        if (behavior.length() != 0) {
            try {
                final Object[] url = URL_FORMAT.parse(behavior);
                if (url.length > 0) {
                    jsElement_.jsxFunction_addBehavior((String) url[0]);
                }
            }
            catch (final ParseException e) {
                getLog().warn("Invalid behavior: '" + behavior + "'.");
            }
        }
    }

    /**
     * Gets the "border" style attribute.
     * @return the style attribute
     */
    public String jsxGet_border() {
        return getStyleAttribute("border", true);
    }

    /**
     * Sets the "border" style attribute.
     * @param border the new attribute.
     */
    public void jsxSet_border(final String border) {
        setStyleAttribute("border", border);
    }

    /**
     * Gets the "borderBottom" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderBottom() {
        return getStyleAttribute("borderBottom", true);
    }

    /**
     * Sets the "borderBottom" style attribute.
     * @param borderBottom the new attribute.
     */
    public void jsxSet_borderBottom(final String borderBottom) {
        setStyleAttribute("borderBottom", borderBottom);
    }

    /**
     * Gets the "borderBottomColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderBottomColor() {
        String value = getStyleAttribute("borderBottomColor", true);
        if (value.length() == 0) {
            value = findColor(getStyleAttribute("borderBottom", true));
            if (value == null) {
                value = findColor(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderBottomColor" style attribute.
     * @param borderBottomColor the new attribute.
     */
    public void jsxSet_borderBottomColor(final String borderBottomColor) {
        setStyleAttribute("borderBottomColor", borderBottomColor);
    }

    /**
     * Gets the "borderBottomStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderBottomStyle() {
        String value = getStyleAttribute("borderBottomStyle", true);
        if (value.length() == 0) {
            value = findBorderStyle(getStyleAttribute("borderBottom", true));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderBottomStyle" style attribute.
     * @param borderBottomStyle the new attribute.
     */
    public void jsxSet_borderBottomStyle(final String borderBottomStyle) {
        setStyleAttribute("borderBottomStyle", borderBottomStyle);
    }

    /**
     * Gets the "borderBottomWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderBottomWidth() {
        String value = getStyleAttribute("borderBottomWidth", true);
        if (value.length() == 0) {
            value = findBorderWidth(getStyleAttribute("borderBottom", true));
            if (value == null) {
                value = findBorderWidth(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderBottomWidth" style attribute.
     * @param borderBottomWidth the new attribute.
     */
    public void jsxSet_borderBottomWidth(final String borderBottomWidth) {
        setStyleAttribute("borderBottomWidth", borderBottomWidth);
    }

    /**
     * Gets the "borderCollapse" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderCollapse() {
        return getStyleAttribute("borderCollapse", true);
    }

    /**
     * Sets the "borderCollapse" style attribute.
     * @param borderCollapse the new attribute.
     */
    public void jsxSet_borderCollapse(final String borderCollapse) {
        setStyleAttribute("borderCollapse", borderCollapse);
    }

    /**
     * Gets the "borderColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderColor() {
        return getStyleAttribute("borderColor", true);
    }

    /**
     * Sets the "borderColor" style attribute.
     * @param borderColor the new attribute.
     */
    public void jsxSet_borderColor(final String borderColor) {
        setStyleAttribute("borderColor", borderColor);
    }

    /**
     * Gets the "borderLeft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderLeft() {
        return getStyleAttribute("borderLeft", true);
    }

    /**
     * Sets the "borderLeft" style attribute.
     * @param borderLeft the new attribute.
     */
    public void jsxSet_borderLeft(final String borderLeft) {
        setStyleAttribute("borderLeft", borderLeft);
    }

    /**
     * Gets the "borderLeftColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderLeftColor() {
        String value = getStyleAttribute("borderLeftColor", true);
        if (value.length() == 0) {
            value = findColor(getStyleAttribute("borderLeft", true));
            if (value == null) {
                value = findColor(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderLeftColor" style attribute.
     * @param borderLeftColor the new attribute.
     */
    public void jsxSet_borderLeftColor(final String borderLeftColor) {
        setStyleAttribute("borderLeftColor", borderLeftColor);
    }

    /**
     * Gets the "borderLeftStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderLeftStyle() {
        String value = getStyleAttribute("borderLeftStyle", true);
        if (value.length() == 0) {
            value = findBorderStyle(getStyleAttribute("borderLeft", true));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderLeftStyle" style attribute.
     * @param borderLeftStyle the new attribute.
     */
    public void jsxSet_borderLeftStyle(final String borderLeftStyle) {
        setStyleAttribute("borderLeftStyle", borderLeftStyle);
    }

    /**
     * Gets the "borderLeftWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderLeftWidth() {
        String value = getStyleAttribute("borderLeftWidth", true);
        if (value.length() == 0) {
            value = findBorderWidth(getStyleAttribute("borderLeft", true));
            if (value == null) {
                value = findBorderWidth(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderLeftWidth" style attribute.
     * @param borderLeftWidth the new attribute.
     */
    public void jsxSet_borderLeftWidth(final String borderLeftWidth) {
        setStyleAttribute("borderLeftWidth", borderLeftWidth);
    }

    /**
     * Gets the "borderRight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderRight() {
        return getStyleAttribute("borderRight", true);
    }

    /**
     * Sets the "borderRight" style attribute.
     * @param borderRight the new attribute.
     */
    public void jsxSet_borderRight(final String borderRight) {
        setStyleAttribute("borderRight", borderRight);
    }

    /**
     * Gets the "borderRightColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderRightColor() {
        String value = getStyleAttribute("borderRightColor", true);
        if (value.length() == 0) {
            value = findColor(getStyleAttribute("borderRight", true));
            if (value == null) {
                value = findColor(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderRightColor" style attribute.
     * @param borderRightColor the new attribute.
     */
    public void jsxSet_borderRightColor(final String borderRightColor) {
        setStyleAttribute("borderRightColor", borderRightColor);
    }

    /**
     * Gets the "borderRightStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderRightStyle() {
        String value = getStyleAttribute("borderRightStyle", true);
        if (value.length() == 0) {
            value = findBorderStyle(getStyleAttribute("borderRight", true));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderRightStyle" style attribute.
     * @param borderRightStyle the new attribute.
     */
    public void jsxSet_borderRightStyle(final String borderRightStyle) {
        setStyleAttribute("borderRightStyle", borderRightStyle);
    }

    /**
     * Gets the "borderRightWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderRightWidth() {
        String value = getStyleAttribute("borderRightWidth", true);
        if (value.length() == 0) {
            value = findBorderWidth(getStyleAttribute("borderRight", true));
            if (value == null) {
                value = findBorderWidth(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderRightWidth" style attribute.
     * @param borderRightWidth the new attribute.
     */
    public void jsxSet_borderRightWidth(final String borderRightWidth) {
        setStyleAttribute("borderRightWidth", borderRightWidth);
    }

    /**
     * Gets the "borderSpacing" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderSpacing() {
        return getStyleAttribute("borderSpacing", true);
    }

    /**
     * Sets the "borderSpacing" style attribute.
     * @param borderSpacing the new attribute.
     */
    public void jsxSet_borderSpacing(final String borderSpacing) {
        setStyleAttribute("borderSpacing", borderSpacing);
    }

    /**
     * Gets the "borderStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderStyle() {
        return getStyleAttribute("borderStyle", true);
    }

    /**
     * Sets the "borderStyle" style attribute.
     * @param borderStyle the new attribute.
     */
    public void jsxSet_borderStyle(final String borderStyle) {
        setStyleAttribute("borderStyle", borderStyle);
    }

    /**
     * Gets the "borderTop" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderTop() {
        return getStyleAttribute("borderTop", true);
    }

    /**
     * Sets the "borderTop" style attribute.
     * @param borderTop the new attribute.
     */
    public void jsxSet_borderTop(final String borderTop) {
        setStyleAttribute("borderTop", borderTop);
    }

    /**
     * Gets the "borderTopColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderTopColor() {
        String value = getStyleAttribute("borderTopColor", true);
        if (value.length() == 0) {
            value = findColor(getStyleAttribute("borderTop", true));
            if (value == null) {
                value = findColor(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderTopColor" style attribute.
     * @param borderTopColor the new attribute.
     */
    public void jsxSet_borderTopColor(final String borderTopColor) {
        setStyleAttribute("borderTopColor", borderTopColor);
    }

    /**
     * Gets the "borderTopStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderTopStyle() {
        String value = getStyleAttribute("borderTopStyle", true);
        if (value.length() == 0) {
            value = findBorderStyle(getStyleAttribute("borderTop", true));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderTopStyle" style attribute.
     * @param borderTopStyle the new attribute.
     */
    public void jsxSet_borderTopStyle(final String borderTopStyle) {
        setStyleAttribute("borderTopStyle", borderTopStyle);
    }

    /**
     * Gets the "borderTopWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderTopWidth() {
        String value = getStyleAttribute("borderTopWidth", true);
        if (value.length() == 0) {
            value = findBorderWidth(getStyleAttribute("borderTop", true));
            if (value == null) {
                value = findBorderWidth(getStyleAttribute("border", true));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the "borderTopWidth" style attribute.
     * @param borderTopWidth the new attribute.
     */
    public void jsxSet_borderTopWidth(final String borderTopWidth) {
        setStyleAttribute("borderTopWidth", borderTopWidth);
    }

    /**
     * Gets the "borderWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderWidth() {
        return getStyleAttribute("borderWidth", true);
    }

    /**
     * Sets the "borderWidth" style attribute.
     * @param borderWidth the new attribute.
     */
    public void jsxSet_borderWidth(final String borderWidth) {
        setStyleAttribute("borderWidth", borderWidth);
    }

    /**
     * Gets the "bottom" style attribute.
     * @return the style attribute
     */
    public String jsxGet_bottom() {
        return getStyleAttribute("bottom", true);
    }

    /**
     * Sets the "bottom" style attribute.
     * @param bottom the new attribute.
     */
    public void jsxSet_bottom(final String bottom) {
        setStyleAttribute("bottom", bottom);
    }

    /**
     * Gets the "captionSide" style attribute.
     * @return the style attribute
     */
    public String jsxGet_captionSide() {
        return getStyleAttribute("captionSide", true);
    }

    /**
     * Sets the "captionSide" style attribute.
     * @param captionSide the new attribute.
     */
    public void jsxSet_captionSide(final String captionSide) {
        setStyleAttribute("captionSide", captionSide);
    }

    /**
     * Gets the "clear" style attribute.
     * @return the style attribute
     */
    public String jsxGet_clear() {
        return getStyleAttribute("clear", true);
    }

    /**
     * Sets the "clear" style attribute.
     * @param clear the new attribute.
     */
    public void jsxSet_clear(final String clear) {
        setStyleAttribute("clear", clear);
    }

    /**
     * Gets the "clip" style attribute.
     * @return the style attribute
     */
    public String jsxGet_clip() {
        return getStyleAttribute("clip", true);
    }

    /**
     * Sets the "clip" style attribute.
     * @param clip the new attribute.
     */
    public void jsxSet_clip(final String clip) {
        setStyleAttribute("clip", clip);
    }

    /**
     * Gets the "color" style attribute.
     * @return the style attribute
     */
    public String jsxGet_color() {
        return getStyleAttribute("color", true);
    }

    /**
     * Sets the "color" style attribute.
     * @param color the new attribute.
     */
    public void jsxSet_color(final String color) {
        setStyleAttribute("color", color);
    }

    /**
     * Gets the "content" style attribute.
     * @return the style attribute
     */
    public String jsxGet_content() {
        return getStyleAttribute("content", true);
    }

    /**
     * Sets the "content" style attribute.
     * @param content the new attribute.
     */
    public void jsxSet_content(final String content) {
        setStyleAttribute("content", content);
    }

    /**
     * Gets the "counterIncrement" style attribute.
     * @return the style attribute
     */
    public String jsxGet_counterIncrement() {
        return getStyleAttribute("counterIncrement", true);
    }

    /**
     * Sets the "counterIncrement" style attribute.
     * @param counterIncrement the new attribute.
     */
    public void jsxSet_counterIncrement(final String counterIncrement) {
        setStyleAttribute("counterIncrement", counterIncrement);
    }

    /**
     * Gets the "counterReset" style attribute.
     * @return the style attribute
     */
    public String jsxGet_counterReset() {
        return getStyleAttribute("counterReset", true);
    }

    /**
     * Sets the "counterReset" style attribute.
     * @param counterReset the new attribute.
     */
    public void jsxSet_counterReset(final String counterReset) {
        setStyleAttribute("counterReset", counterReset);
    }

    /**
     * Gets the "cssFloat" style attribute.
     * @return the style attribute
     */
    public String jsxGet_cssFloat() {
        return getStyleAttribute("float", true);
    }

    /**
     * Sets the "cssFloat" style attribute.
     * @param value the new attribute.
     */
    public void jsxSet_cssFloat(final String value) {
        setStyleAttribute("float", value);
    }

    /**
     * Returns the actual text of the style.
     * @return the actual text of the style.
     */
    public String jsxGet_cssText() {
        return jsElement_.getHtmlElementOrDie().getAttribute("style");
    }

    /**
     * Sets the actual text of the style.
     * @param value the new text.
     */
    public void jsxSet_cssText(final String value) {
        jsElement_.getHtmlElementOrDie().setAttribute("style", value);
    }

    /**
     * Gets the "cue" style attribute.
     * @return the style attribute
     */
    public String jsxGet_cue() {
        return getStyleAttribute("cue", true);
    }

    /**
     * Sets the "cue" style attribute.
     * @param cue the new attribute.
     */
    public void jsxSet_cue(final String cue) {
        setStyleAttribute("cue", cue);
    }

    /**
     * Gets the "cueAfter" style attribute.
     * @return the style attribute
     */
    public String jsxGet_cueAfter() {
        return getStyleAttribute("cueAfter", true);
    }

    /**
     * Sets the "cueAfter" style attribute.
     * @param cueAfter the new attribute.
     */
    public void jsxSet_cueAfter(final String cueAfter) {
        setStyleAttribute("cueAfter", cueAfter);
    }

    /**
     * Gets the "cueBefore" style attribute.
     * @return the style attribute
     */
    public String jsxGet_cueBefore() {
        return getStyleAttribute("cueBefore", true);
    }

    /**
     * Sets the "cueBefore" style attribute.
     * @param cueBefore the new attribute.
     */
    public void jsxSet_cueBefore(final String cueBefore) {
        setStyleAttribute("cueBefore", cueBefore);
    }

    /**
     * Gets the "cursor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_cursor() {
        return getStyleAttribute("cursor", true);
    }

    /**
     * Sets the "cursor" style attribute.
     * @param cursor the new attribute.
     */
    public void jsxSet_cursor(final String cursor) {
        setStyleAttribute("cursor", cursor);
    }

    /**
     * Gets the "direction" style attribute.
     * @return the style attribute
     */
    public String jsxGet_direction() {
        return getStyleAttribute("direction", true);
    }

    /**
     * Sets the "direction" style attribute.
     * @param direction the new attribute.
     */
    public void jsxSet_direction(final String direction) {
        setStyleAttribute("direction", direction);
    }

    /**
     * Gets the "display" style attribute.
     * @return the style attribute
     */
    public String jsxGet_display() {
        return getStyleAttribute("display", true);
    }

    /**
     * Sets the "display" style attribute.
     * @param display the new attribute.
     */
    public void jsxSet_display(final String display) {
        setStyleAttribute("display", display);
    }

    /**
     * Gets the "elevation" style attribute.
     * @return the style attribute
     */
    public String jsxGet_elevation() {
        return getStyleAttribute("elevation", true);
    }

    /**
     * Sets the "elevation" style attribute.
     * @param elevation the new attribute.
     */
    public void jsxSet_elevation(final String elevation) {
        setStyleAttribute("elevation", elevation);
    }

    /**
     * Gets the "emptyCells" style attribute.
     * @return the style attribute
     */
    public String jsxGet_emptyCells() {
        return getStyleAttribute("emptyCells", true);
    }

    /**
     * Sets the "emptyCells" style attribute.
     * @param emptyCells the new attribute.
     */
    public void jsxSet_emptyCells(final String emptyCells) {
        setStyleAttribute("emptyCells", emptyCells);
    }

    /**
     * Gets the object's filter (IE only). See the <a
     * href="http://msdn2.microsoft.com/en-us/library/ms530752.aspx">MSDN documentation</a> for
     * more information.
     * @return the object's filter
     */
    public String jsxGet_filter() {
        return getStyleAttribute("filter", true);
    }

    /**
     * Sets the object's filter (IE only). See the <a
     * href="http://msdn2.microsoft.com/en-us/library/ms530752.aspx">MSDN documentation</a> for
     * more information.
     * @param filter the new filter
     */
    public void jsxSet_filter(final String filter) {
        setStyleAttribute("filter", filter);
    }

    /**
     * Gets the "font" style attribute.
     * @return the style attribute
     */
    public String jsxGet_font() {
        return getStyleAttribute("font", true);
    }

    /**
     * Sets the "font" style attribute.
     * @param font the new attribute.
     */
    public void jsxSet_font(final String font) {
        setStyleAttribute("font", font);
    }

    /**
     * Gets the "fontFamily" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontFamily() {
        return getStyleAttribute("fontFamily", true);
    }

    /**
     * Sets the "fontFamily" style attribute.
     * @param fontFamily the new attribute.
     */
    public void jsxSet_fontFamily(final String fontFamily) {
        setStyleAttribute("fontFamily", fontFamily);
    }

    /**
     * Gets the "fontSize" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontSize() {
        return getStyleAttribute("fontSize", true);
    }

    /**
     * Sets the "fontSize" style attribute.
     * @param fontSize the new attribute.
     */
    public void jsxSet_fontSize(final String fontSize) {
        setStyleAttribute("fontSize", fontSize);
    }

    /**
     * Gets the "fontSizeAdjust" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontSizeAdjust() {
        return getStyleAttribute("fontSizeAdjust", true);
    }

    /**
     * Sets the "fontSizeAdjust" style attribute.
     * @param fontSizeAdjust the new attribute.
     */
    public void jsxSet_fontSizeAdjust(final String fontSizeAdjust) {
        setStyleAttribute("fontSizeAdjust", fontSizeAdjust);
    }

    /**
     * Gets the "fontStretch" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontStretch() {
        return getStyleAttribute("fontStretch", true);
    }

    /**
     * Sets the "fontStretch" style attribute.
     * @param fontStretch the new attribute.
     */
    public void jsxSet_fontStretch(final String fontStretch) {
        setStyleAttribute("fontStretch", fontStretch);
    }

    /**
     * Gets the "fontStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontStyle() {
        return getStyleAttribute("fontStyle", true);
    }

    /**
     * Sets the "fontStyle" style attribute.
     * @param fontStyle the new attribute.
     */
    public void jsxSet_fontStyle(final String fontStyle) {
        setStyleAttribute("fontStyle", fontStyle);
    }

    /**
     * Gets the "fontVariant" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontVariant() {
        return getStyleAttribute("fontVariant", true);
    }

    /**
     * Sets the "fontVariant" style attribute.
     * @param fontVariant the new attribute.
     */
    public void jsxSet_fontVariant(final String fontVariant) {
        setStyleAttribute("fontVariant", fontVariant);
    }

    /**
     * Gets the "fontWeight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontWeight() {
        return getStyleAttribute("fontWeight", true);
    }

    /**
     * Sets the "fontWeight" style attribute.
     * @param fontWeight the new attribute.
     */
    public void jsxSet_fontWeight(final String fontWeight) {
        setStyleAttribute("fontWeight", fontWeight);
    }

    /**
     * Gets the "height" style attribute.
     * @return the style attribute
     */
    public String jsxGet_height() {
        return getStyleAttribute("height", true);
    }

    /**
     * Sets the "height" style attribute.
     * @param height the new attribute.
     */
    public void jsxSet_height(final String height) {
        setStyleAttribute("height", height);
    }

    /**
     * Gets the "imeMode" style attribute.
     * @return the style attribute
     */
    public String jsxGet_imeMode() {
        return getStyleAttribute("imeMode", true);
    }

    /**
     * Sets the "imeMode" style attribute.
     * @param imeMode the new attribute.
     */
    public void jsxSet_imeMode(final String imeMode) {
        setStyleAttribute("imeMode", imeMode);
    }

    /**
     * Gets the "layoutFlow" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutFlow() {
        return getStyleAttribute("layoutFlow", true);
    }

    /**
     * Sets the "layoutFlow" style attribute.
     * @param layoutFlow the new attribute.
     */
    public void jsxSet_layoutFlow(final String layoutFlow) {
        setStyleAttribute("layoutFlow", layoutFlow);
    }

    /**
     * Gets the "layoutGrid" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutGrid() {
        return getStyleAttribute("layoutGrid", true);
    }

    /**
     * Sets the "layoutGrid" style attribute.
     * @param layoutGrid the new attribute.
     */
    public void jsxSet_layoutGrid(final String layoutGrid) {
        setStyleAttribute("layoutGrid", layoutGrid);
    }

    /**
     * Gets the "layoutGridChar" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutGridChar() {
        return getStyleAttribute("layoutGridChar", true);
    }

    /**
     * Sets the "layoutGridChar" style attribute.
     * @param layoutGridChar the new attribute.
     */
    public void jsxSet_layoutGridChar(final String layoutGridChar) {
        setStyleAttribute("layoutGridChar", layoutGridChar);
    }

    /**
     * Gets the "layoutGridLine" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutGridLine() {
        return getStyleAttribute("layoutGridLine", true);
    }

    /**
     * Sets the "layoutGridLine" style attribute.
     * @param layoutGridLine the new attribute.
     */
    public void jsxSet_layoutGridLine(final String layoutGridLine) {
        setStyleAttribute("layoutGridLine", layoutGridLine);
    }

    /**
     * Gets the "layoutGridMode" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutGridMode() {
        return getStyleAttribute("layoutGridMode", true);
    }

    /**
     * Sets the "layoutGridMode" style attribute.
     * @param layoutGridMode the new attribute.
     */
    public void jsxSet_layoutGridMode(final String layoutGridMode) {
        setStyleAttribute("layoutGridMode", layoutGridMode);
    }

    /**
     * Gets the "layoutGridType" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutGridType() {
        return getStyleAttribute("layoutGridType", true);
    }

    /**
     * Sets the "layoutGridType" style attribute.
     * @param layoutGridType the new attribute.
     */
    public void jsxSet_layoutGridType(final String layoutGridType) {
        setStyleAttribute("layoutGridType", layoutGridType);
    }

    /**
     * Gets the "left" style attribute.
     * @return the style attribute
     */
    public String jsxGet_left() {
        return getStyleAttribute("left", true);
    }

    /**
     * Sets the "left" style attribute.
     * @param left the new attribute.
     */
    public void jsxSet_left(final String left) {
        setStyleAttribute("left", left);
    }

    /**
     * Gets the "length", not yet implemented.
     * @return the length.
     */
    public int jsxGet_length() {
        return 0;
    }

    /**
     * Gets the "letterSpacing" style attribute.
     * @return the style attribute
     */
    public String jsxGet_letterSpacing() {
        return getStyleAttribute("letterSpacing", true);
    }

    /**
     * Sets the "letterSpacing" style attribute.
     * @param letterSpacing the new attribute.
     */
    public void jsxSet_letterSpacing(final String letterSpacing) {
        setStyleAttribute("letterSpacing", letterSpacing);
    }

    /**
     * Gets the "lineBreak" style attribute.
     * @return the style attribute
     */
    public String jsxGet_lineBreak() {
        return getStyleAttribute("lineBreak", true);
    }

    /**
     * Sets the "lineBreak" style attribute.
     * @param lineBreak the new attribute.
     */
    public void jsxSet_lineBreak(final String lineBreak) {
        setStyleAttribute("lineBreak", lineBreak);
    }

    /**
     * Gets the "lineHeight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_lineHeight() {
        return getStyleAttribute("lineHeight", true);
    }

    /**
     * Sets the "lineHeight" style attribute.
     * @param lineHeight the new attribute.
     */
    public void jsxSet_lineHeight(final String lineHeight) {
        setStyleAttribute("lineHeight", lineHeight);
    }

    /**
     * Gets the "listStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_listStyle() {
        return getStyleAttribute("listStyle", true);
    }

    /**
     * Sets the "listStyle" style attribute.
     * @param listStyle the new attribute.
     */
    public void jsxSet_listStyle(final String listStyle) {
        setStyleAttribute("listStyle", listStyle);
    }

    /**
     * Gets the "listStyleImage" style attribute.
     * @return the style attribute
     */
    public String jsxGet_listStyleImage() {
        return getStyleAttribute("listStyleImage", true);
    }

    /**
     * Sets the "listStyleImage" style attribute.
     * @param listStyleImage the new attribute.
     */
    public void jsxSet_listStyleImage(final String listStyleImage) {
        setStyleAttribute("listStyleImage", listStyleImage);
    }

    /**
     * Gets the "listStylePosition" style attribute.
     * @return the style attribute
     */
    public String jsxGet_listStylePosition() {
        return getStyleAttribute("listStylePosition", true);
    }

    /**
     * Sets the "listStylePosition" style attribute.
     * @param listStylePosition the new attribute.
     */
    public void jsxSet_listStylePosition(final String listStylePosition) {
        setStyleAttribute("listStylePosition", listStylePosition);
    }

    /**
     * Gets the "listStyleType" style attribute.
     * @return the style attribute
     */
    public String jsxGet_listStyleType() {
        return getStyleAttribute("listStyleType", true);
    }

    /**
     * Sets the "listStyleType" style attribute.
     * @param listStyleType the new attribute.
     */
    public void jsxSet_listStyleType(final String listStyleType) {
        setStyleAttribute("listStyleType", listStyleType);
    }

    /**
     * Gets the "margin" style attribute.
     * @return the style attribute
     */
    public String jsxGet_margin() {
        return getStyleAttribute("margin", true);
    }

    /**
     * Sets the "margin" style attribute.
     * @param margin the new attribute.
     */
    public void jsxSet_margin(final String margin) {
        setStyleAttribute("margin", margin);
    }

    /**
     * Gets the "marginBottom" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginBottom() {
        return getStyleAttribute("marginBottom", true);
    }

    /**
     * Sets the "marginBottom" style attribute.
     * @param marginBottom the new attribute.
     */
    public void jsxSet_marginBottom(final String marginBottom) {
        setStyleAttribute("marginBottom", marginBottom);
    }

    /**
     * Gets the "marginLeft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginLeft() {
        return getStyleAttribute("marginLeft", true);
    }

    /**
     * Sets the "marginLeft" style attribute.
     * @param marginLeft the new attribute.
     */
    public void jsxSet_marginLeft(final String marginLeft) {
        setStyleAttribute("marginLeft", marginLeft);
    }

    /**
     * Gets the "marginRight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginRight() {
        return getStyleAttribute("marginRight", true);
    }

    /**
     * Sets the "marginRight" style attribute.
     * @param marginRight the new attribute.
     */
    public void jsxSet_marginRight(final String marginRight) {
        setStyleAttribute("marginRight", marginRight);
    }

    /**
     * Gets the "marginTop" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginTop() {
        return getStyleAttribute("marginTop", true);
    }

    /**
     * Sets the "marginTop" style attribute.
     * @param marginTop the new attribute.
     */
    public void jsxSet_marginTop(final String marginTop) {
        setStyleAttribute("marginTop", marginTop);
    }

    /**
     * Gets the "markerOffset" style attribute.
     * @return the style attribute
     */
    public String jsxGet_markerOffset() {
        return getStyleAttribute("markerOffset", true);
    }

    /**
     * Sets the "markerOffset" style attribute.
     * @param markerOffset the new attribute.
     */
    public void jsxSet_markerOffset(final String markerOffset) {
        setStyleAttribute("markerOffset", markerOffset);
    }

    /**
     * Gets the "marks" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marks() {
        return getStyleAttribute("marks", true);
    }

    /**
     * Sets the "marks" style attribute.
     * @param marks the new attribute.
     */
    public void jsxSet_marks(final String marks) {
        setStyleAttribute("marks", marks);
    }

    /**
     * Gets the "maxHeight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_maxHeight() {
        return getStyleAttribute("maxHeight", true);
    }

    /**
     * Sets the "maxHeight" style attribute.
     * @param maxHeight the new attribute.
     */
    public void jsxSet_maxHeight(final String maxHeight) {
        setStyleAttribute("maxHeight", maxHeight);
    }

    /**
     * Gets the "maxWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_maxWidth() {
        return getStyleAttribute("maxWidth", true);
    }

    /**
     * Sets the "maxWidth" style attribute.
     * @param maxWidth the new attribute.
     */
    public void jsxSet_maxWidth(final String maxWidth) {
        setStyleAttribute("maxWidth", maxWidth);
    }

    /**
     * Gets the "minHeight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_minHeight() {
        return getStyleAttribute("minHeight", true);
    }

    /**
     * Sets the "minHeight" style attribute.
     * @param minHeight the new attribute.
     */
    public void jsxSet_minHeight(final String minHeight) {
        setStyleAttribute("minHeight", minHeight);
    }

    /**
     * Gets the "minWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_minWidth() {
        return getStyleAttribute("minWidth", true);
    }

    /**
     * Sets the "minWidth" style attribute.
     * @param minWidth the new attribute.
     */
    public void jsxSet_minWidth(final String minWidth) {
        setStyleAttribute("minWidth", minWidth);
    }

    /**
     * Gets the "MozAppearance" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozAppearance() {
        return getStyleAttribute("MozAppearance", true);
    }

    /**
     * Sets the "MozAppearance" style attribute.
     * @param mozAppearance the new attribute.
     */
    public void jsxSet_MozAppearance(final String mozAppearance) {
        setStyleAttribute("MozAppearance", mozAppearance);
    }

    /**
     * Gets the "MozBackgroundClip" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBackgroundClip() {
        return getStyleAttribute("MozBackgroundClip", true);
    }

    /**
     * Sets the "MozBackgroundClip" style attribute.
     * @param mozBackgroundClip the new attribute.
     */
    public void jsxSet_MozBackgroundClip(final String mozBackgroundClip) {
        setStyleAttribute("MozBackgroundClip", mozBackgroundClip);
    }

    /**
     * Gets the "MozBackgroundInlinePolicy" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBackgroundInlinePolicy() {
        return getStyleAttribute("MozBackgroundInlinePolicy", true);
    }

    /**
     * Sets the "MozBackgroundInlinePolicy" style attribute.
     * @param mozBackgroundInlinePolicy the new attribute.
     */
    public void jsxSet_MozBackgroundInlinePolicy(final String mozBackgroundInlinePolicy) {
        setStyleAttribute("MozBackgroundInlinePolicy", mozBackgroundInlinePolicy);
    }

    /**
     * Gets the "MozBackgroundOrigin" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBackgroundOrigin() {
        return getStyleAttribute("MozBackgroundOrigin", true);
    }

    /**
     * Sets the "MozBackgroundOrigin" style attribute.
     * @param mozBackgroundOrigin the new attribute.
     */
    public void jsxSet_MozBackgroundOrigin(final String mozBackgroundOrigin) {
        setStyleAttribute("MozBackgroundOrigin", mozBackgroundOrigin);
    }

    /**
     * Gets the "MozBinding" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBinding() {
        return getStyleAttribute("MozBinding", true);
    }

    /**
     * Sets the "MozBinding" style attribute.
     * @param mozBinding the new attribute.
     */
    public void jsxSet_MozBinding(final String mozBinding) {
        setStyleAttribute("MozBinding", mozBinding);
    }

    /**
     * Gets the "MozBorderBottomColors" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderBottomColors() {
        return getStyleAttribute("MozBorderBottomColors", true);
    }

    /**
     * Sets the "MozBorderBottomColors" style attribute.
     * @param mozBorderBottomColors the new attribute.
     */
    public void jsxSet_MozBorderBottomColors(final String mozBorderBottomColors) {
        setStyleAttribute("MozBorderBottomColors", mozBorderBottomColors);
    }

    /**
     * Gets the "MozBorderLeftColors" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderLeftColors() {
        return getStyleAttribute("MozBorderLeftColors", true);
    }

    /**
     * Sets the "MozBorderLeftColors" style attribute.
     * @param mozBorderLeftColors the new attribute.
     */
    public void jsxSet_MozBorderLeftColors(final String mozBorderLeftColors) {
        setStyleAttribute("MozBorderLeftColors", mozBorderLeftColors);
    }

    /**
     * Gets the "MozBorderRadius" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRadius() {
        return getStyleAttribute("MozBorderRadius", true);
    }

    /**
     * Sets the "MozBorderRadius" style attribute.
     * @param mozBorderRadius the new attribute.
     */
    public void jsxSet_MozBorderRadius(final String mozBorderRadius) {
        setStyleAttribute("MozBorderRadius", mozBorderRadius);
    }

    /**
     * Gets the "MozBorderRadiusBottomleft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRadiusBottomleft() {
        return getStyleAttribute("MozBorderRadiusBottomleft", true);
    }

    /**
     * Sets the "MozBorderRadiusBottomleft" style attribute.
     * @param mozBorderRadiusBottomleft the new attribute.
     */
    public void jsxSet_MozBorderRadiusBottomleft(final String mozBorderRadiusBottomleft) {
        setStyleAttribute("MozBorderRadiusBottomleft", mozBorderRadiusBottomleft);
    }

    /**
     * Gets the "MozBorderRadiusBottomright" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRadiusBottomright() {
        return getStyleAttribute("MozBorderRadiusBottomright", true);
    }

    /**
     * Sets the "MozBorderRadiusBottomright" style attribute.
     * @param mozBorderRadiusBottomright the new attribute.
     */
    public void jsxSet_MozBorderRadiusBottomright(final String mozBorderRadiusBottomright) {
        setStyleAttribute("MozBorderRadiusBottomright", mozBorderRadiusBottomright);
    }

    /**
     * Gets the "MozBorderRadiusTopleft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRadiusTopleft() {
        return getStyleAttribute("MozBorderRadiusTopleft", true);
    }

    /**
     * Sets the "MozBorderRadiusTopleft" style attribute.
     * @param mozBorderRadiusTopleft the new attribute.
     */
    public void jsxSet_MozBorderRadiusTopleft(final String mozBorderRadiusTopleft) {
        setStyleAttribute("MozBorderRadiusTopleft", mozBorderRadiusTopleft);
    }

    /**
     * Gets the "MozBorderRadiusTopright" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRadiusTopright() {
        return getStyleAttribute("MozBorderRadiusTopright", true);
    }

    /**
     * Sets the "MozBorderRadiusTopright" style attribute.
     * @param mozBorderRadiusTopright the new attribute.
     */
    public void jsxSet_MozBorderRadiusTopright(final String mozBorderRadiusTopright) {
        setStyleAttribute("MozBorderRadiusTopright", mozBorderRadiusTopright);
    }

    /**
     * Gets the "MozBorderRightColors" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRightColors() {
        return getStyleAttribute("MozBorderRightColors", true);
    }

    /**
     * Sets the "MozBorderRightColors" style attribute.
     * @param mozBorderRightColors the new attribute.
     */
    public void jsxSet_MozBorderRightColors(final String mozBorderRightColors) {
        setStyleAttribute("MozBorderRightColors", mozBorderRightColors);
    }

    /**
     * Gets the "MozBorderTopColors" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderTopColors() {
        return getStyleAttribute("MozBorderTopColors", true);
    }

    /**
     * Sets the "MozBorderTopColors" style attribute.
     * @param mozBorderTopColors the new attribute.
     */
    public void jsxSet_MozBorderTopColors(final String mozBorderTopColors) {
        setStyleAttribute("MozBorderTopColors", mozBorderTopColors);
    }

    /**
     * Gets the "MozBoxAlign" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxAlign() {
        return getStyleAttribute("MozBoxAlign", true);
    }

    /**
     * Sets the "MozBoxAlign" style attribute.
     * @param mozBoxAlign the new attribute.
     */
    public void jsxSet_MozBoxAlign(final String mozBoxAlign) {
        setStyleAttribute("MozBoxAlign", mozBoxAlign);
    }

    /**
     * Gets the "MozBoxDirection" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxDirection() {
        return getStyleAttribute("MozBoxDirection", true);
    }

    /**
     * Sets the "MozBoxDirection" style attribute.
     * @param mozBoxDirection the new attribute.
     */
    public void jsxSet_MozBoxDirection(final String mozBoxDirection) {
        setStyleAttribute("MozBoxDirection", mozBoxDirection);
    }

    /**
     * Gets the "MozBoxFlex" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxFlex() {
        return getStyleAttribute("MozBoxFlex", true);
    }

    /**
     * Sets the "MozBoxFlex" style attribute.
     * @param mozBoxFlex the new attribute.
     */
    public void jsxSet_MozBoxFlex(final String mozBoxFlex) {
        setStyleAttribute("MozBoxFlex", mozBoxFlex);
    }

    /**
     * Gets the "MozBoxOrdinalGroup" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxOrdinalGroup() {
        return getStyleAttribute("MozBoxOrdinalGroup", true);
    }

    /**
     * Sets the "MozBoxOrdinalGroup" style attribute.
     * @param mozBoxOrdinalGroup the new attribute.
     */
    public void jsxSet_MozBoxOrdinalGroup(final String mozBoxOrdinalGroup) {
        setStyleAttribute("MozBoxOrdinalGroup", mozBoxOrdinalGroup);
    }

    /**
     * Gets the "MozBoxOrient" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxOrient() {
        return getStyleAttribute("MozBoxOrient", true);
    }

    /**
     * Sets the "MozBoxOrient" style attribute.
     * @param mozBoxOrient the new attribute.
     */
    public void jsxSet_MozBoxOrient(final String mozBoxOrient) {
        setStyleAttribute("MozBoxOrient", mozBoxOrient);
    }

    /**
     * Gets the "MozBoxPack" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxPack() {
        return getStyleAttribute("MozBoxPack", true);
    }

    /**
     * Sets the "MozBoxPack" style attribute.
     * @param mozBoxPack the new attribute.
     */
    public void jsxSet_MozBoxPack(final String mozBoxPack) {
        setStyleAttribute("MozBoxPack", mozBoxPack);
    }

    /**
     * Gets the "MozBoxSizing" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxSizing() {
        return getStyleAttribute("MozBoxSizing", true);
    }

    /**
     * Sets the "MozBoxSizing" style attribute.
     * @param mozBoxSizing the new attribute.
     */
    public void jsxSet_MozBoxSizing(final String mozBoxSizing) {
        setStyleAttribute("MozBoxSizing", mozBoxSizing);
    }

    /**
     * Gets the "MozColumnCount" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozColumnCount() {
        return getStyleAttribute("MozColumnCount", true);
    }

    /**
     * Sets the "MozColumnCount" style attribute.
     * @param mozColumnCount the new attribute.
     */
    public void jsxSet_MozColumnCount(final String mozColumnCount) {
        setStyleAttribute("MozColumnCount", mozColumnCount);
    }

    /**
     * Gets the "MozColumnGap" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozColumnGap() {
        return getStyleAttribute("MozColumnGap", true);
    }

    /**
     * Sets the "MozColumnGap" style attribute.
     * @param mozColumnGap the new attribute.
     */
    public void jsxSet_MozColumnGap(final String mozColumnGap) {
        setStyleAttribute("MozColumnGap", mozColumnGap);
    }

    /**
     * Gets the "MozColumnWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozColumnWidth() {
        return getStyleAttribute("MozColumnWidth", true);
    }

    /**
     * Sets the "MozColumnWidth" style attribute.
     * @param mozColumnWidth the new attribute.
     */
    public void jsxSet_MozColumnWidth(final String mozColumnWidth) {
        setStyleAttribute("MozColumnWidth", mozColumnWidth);
    }

    /**
     * Gets the "MozFloatEdge" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozFloatEdge() {
        return getStyleAttribute("MozFloatEdge", true);
    }

    /**
     * Sets the "MozFloatEdge" style attribute.
     * @param mozFloatEdge the new attribute.
     */
    public void jsxSet_MozFloatEdge(final String mozFloatEdge) {
        setStyleAttribute("MozFloatEdge", mozFloatEdge);
    }

    /**
     * Gets the "MozForceBrokenImageIcon" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozForceBrokenImageIcon() {
        return getStyleAttribute("MozForceBrokenImageIcon", true);
    }

    /**
     * Sets the "MozForceBrokenImageIcon" style attribute.
     * @param mozForceBrokenImageIcon the new attribute.
     */
    public void jsxSet_MozForceBrokenImageIcon(final String mozForceBrokenImageIcon) {
        setStyleAttribute("MozForceBrokenImageIcon", mozForceBrokenImageIcon);
    }

    /**
     * Gets the "MozImageRegion" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozImageRegion() {
        return getStyleAttribute("MozImageRegion", true);
    }

    /**
     * Sets the "MozImageRegion" style attribute.
     * @param mozImageRegion the new attribute.
     */
    public void jsxSet_MozImageRegion(final String mozImageRegion) {
        setStyleAttribute("MozImageRegion", mozImageRegion);
    }

    /**
     * Gets the "MozMarginEnd" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozMarginEnd() {
        return getStyleAttribute("MozMarginEnd", true);
    }

    /**
     * Sets the "MozMarginEnd" style attribute.
     * @param mozMarginEnd the new attribute.
     */
    public void jsxSet_MozMarginEnd(final String mozMarginEnd) {
        setStyleAttribute("MozMarginEnd", mozMarginEnd);
    }

    /**
     * Gets the "MozMarginStart" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozMarginStart() {
        return getStyleAttribute("MozMarginStart", true);
    }

    /**
     * Sets the "MozMarginStart" style attribute.
     * @param mozMarginStart the new attribute.
     */
    public void jsxSet_MozMarginStart(final String mozMarginStart) {
        setStyleAttribute("MozMarginStart", mozMarginStart);
    }

    /**
     * Gets the "MozOpacity" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOpacity() {
        return getStyleAttribute("MozOpacity", true);
    }

    /**
     * Sets the "MozOpacity" style attribute.
     * @param mozOpacity the new attribute.
     */
    public void jsxSet_MozOpacity(final String mozOpacity) {
        setStyleAttribute("MozOpacity", mozOpacity);
    }

    /**
     * Gets the "MozOutline" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutline() {
        return getStyleAttribute("MozOutline", true);
    }

    /**
     * Sets the "MozOutline" style attribute.
     * @param mozOutline the new attribute.
     */
    public void jsxSet_MozOutline(final String mozOutline) {
        setStyleAttribute("MozOutline", mozOutline);
    }

    /**
     * Gets the "MozOutlineColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineColor() {
        return getStyleAttribute("MozOutlineColor", true);
    }

    /**
     * Sets the "MozOutlineColor" style attribute.
     * @param mozOutlineColor the new attribute.
     */
    public void jsxSet_MozOutlineColor(final String mozOutlineColor) {
        setStyleAttribute("MozOutlineColor", mozOutlineColor);
    }

    /**
     * Gets the "MozOutlineOffset" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineOffset() {
        return getStyleAttribute("MozOutlineOffset", true);
    }

    /**
     * Sets the "MozOutlineOffset" style attribute.
     * @param mozOutlineOffset the new attribute.
     */
    public void jsxSet_MozOutlineOffset(final String mozOutlineOffset) {
        setStyleAttribute("MozOutlineOffset", mozOutlineOffset);
    }

    /**
     * Gets the "MozOutlineRadius" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineRadius() {
        return getStyleAttribute("MozOutlineRadius", true);
    }

    /**
     * Sets the "MozOutlineRadius" style attribute.
     * @param mozOutlineRadius the new attribute.
     */
    public void jsxSet_MozOutlineRadius(final String mozOutlineRadius) {
        setStyleAttribute("MozOutlineRadius", mozOutlineRadius);
    }

    /**
     * Gets the "MozOutlineRadiusBottomleft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineRadiusBottomleft() {
        return getStyleAttribute("MozOutlineRadiusBottomleft", true);
    }

    /**
     * Sets the "MozOutlineRadiusBottomleft" style attribute.
     * @param mozOutlineRadiusBottomleft the new attribute.
     */
    public void jsxSet_MozOutlineRadiusBottomleft(final String mozOutlineRadiusBottomleft) {
        setStyleAttribute("MozOutlineRadiusBottomleft", mozOutlineRadiusBottomleft);
    }

    /**
     * Gets the "MozOutlineRadiusBottomright" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineRadiusBottomright() {
        return getStyleAttribute("MozOutlineRadiusBottomright", true);
    }

    /**
     * Sets the "MozOutlineRadiusBottomright" style attribute.
     * @param mozOutlineRadiusBottomright the new attribute.
     */
    public void jsxSet_MozOutlineRadiusBottomright(final String mozOutlineRadiusBottomright) {
        setStyleAttribute("MozOutlineRadiusBottomright", mozOutlineRadiusBottomright);
    }

    /**
     * Gets the "MozOutlineRadiusTopleft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineRadiusTopleft() {
        return getStyleAttribute("MozOutlineRadiusTopleft", true);
    }

    /**
     * Sets the "MozOutlineRadiusTopleft" style attribute.
     * @param mozOutlineRadiusTopleft the new attribute.
     */
    public void jsxSet_MozOutlineRadiusTopleft(final String mozOutlineRadiusTopleft) {
        setStyleAttribute("MozOutlineRadiusTopleft", mozOutlineRadiusTopleft);
    }

    /**
     * Gets the "MozOutlineRadiusTopright" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineRadiusTopright() {
        return getStyleAttribute("MozOutlineRadiusTopright", true);
    }

    /**
     * Sets the "MozOutlineRadiusTopright" style attribute.
     * @param mozOutlineRadiusTopright the new attribute.
     */
    public void jsxSet_MozOutlineRadiusTopright(final String mozOutlineRadiusTopright) {
        setStyleAttribute("MozOutlineRadiusTopright", mozOutlineRadiusTopright);
    }

    /**
     * Gets the "MozOutlineStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineStyle() {
        return getStyleAttribute("MozOutlineStyle", true);
    }

    /**
     * Sets the "MozOutlineStyle" style attribute.
     * @param mozOutlineStyle the new attribute.
     */
    public void jsxSet_MozOutlineStyle(final String mozOutlineStyle) {
        setStyleAttribute("MozOutlineStyle", mozOutlineStyle);
    }

    /**
     * Gets the "MozOutlineWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineWidth() {
        return getStyleAttribute("MozOutlineWidth", true);
    }

    /**
     * Sets the "MozOutlineWidth" style attribute.
     * @param mozOutlineWidth the new attribute.
     */
    public void jsxSet_MozOutlineWidth(final String mozOutlineWidth) {
        setStyleAttribute("MozOutlineWidth", mozOutlineWidth);
    }

    /**
     * Gets the "MozPaddingEnd" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozPaddingEnd() {
        return getStyleAttribute("MozPaddingEnd", true);
    }

    /**
     * Sets the "MozPaddingEnd" style attribute.
     * @param mozPaddingEnd the new attribute.
     */
    public void jsxSet_MozPaddingEnd(final String mozPaddingEnd) {
        setStyleAttribute("MozPaddingEnd", mozPaddingEnd);
    }

    /**
     * Gets the "MozPaddingStart" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozPaddingStart() {
        return getStyleAttribute("MozPaddingStart", true);
    }

    /**
     * Sets the "MozPaddingStart" style attribute.
     * @param mozPaddingStart the new attribute.
     */
    public void jsxSet_MozPaddingStart(final String mozPaddingStart) {
        setStyleAttribute("MozPaddingStart", mozPaddingStart);
    }

    /**
     * Gets the "MozUserFocus" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozUserFocus() {
        return getStyleAttribute("MozUserFocus", true);
    }

    /**
     * Sets the "MozUserFocus" style attribute.
     * @param mozUserFocus the new attribute.
     */
    public void jsxSet_MozUserFocus(final String mozUserFocus) {
        setStyleAttribute("MozUserFocus", mozUserFocus);
    }

    /**
     * Gets the "MozUserInput" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozUserInput() {
        return getStyleAttribute("MozUserInput", true);
    }

    /**
     * Sets the "MozUserInput" style attribute.
     * @param mozUserInput the new attribute.
     */
    public void jsxSet_MozUserInput(final String mozUserInput) {
        setStyleAttribute("MozUserInput", mozUserInput);
    }

    /**
     * Gets the "MozUserModify" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozUserModify() {
        return getStyleAttribute("MozUserModify", true);
    }

    /**
     * Sets the "MozUserModify" style attribute.
     * @param mozUserModify the new attribute.
     */
    public void jsxSet_MozUserModify(final String mozUserModify) {
        setStyleAttribute("MozUserModify", mozUserModify);
    }

    /**
     * Gets the "MozUserSelect" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozUserSelect() {
        return getStyleAttribute("MozUserSelect", true);
    }

    /**
     * Sets the "MozUserSelect" style attribute.
     * @param mozUserSelect the new attribute.
     */
    public void jsxSet_MozUserSelect(final String mozUserSelect) {
        setStyleAttribute("MozUserSelect", mozUserSelect);
    }

    /**
     * Gets the "msInterpolationMode" style attribute.
     * @return the style attribute
     */
    public String jsxGet_msInterpolationMode() {
        return getStyleAttribute("msInterpolationMode", true);
    }

    /**
     * Sets the "msInterpolationMode" style attribute.
     * @param msInterpolationMode the new attribute.
     */
    public void jsxSet_msInterpolationMode(final String msInterpolationMode) {
        setStyleAttribute("msInterpolationMode", msInterpolationMode);
    }

    /**
     * Gets the "opacity" style attribute.
     * @return the style attribute
     */
    public String jsxGet_opacity() {
        return getStyleAttribute("opacity", true);
    }

    /**
     * Sets the "opacity" style attribute.
     * @param opacity the new attribute.
     */
    public void jsxSet_opacity(final String opacity) {
        setStyleAttribute("opacity", opacity);
    }

    /**
     * Gets the "orphans" style attribute.
     * @return the style attribute
     */
    public String jsxGet_orphans() {
        return getStyleAttribute("orphans", true);
    }

    /**
     * Sets the "orphans" style attribute.
     * @param orphans the new attribute.
     */
    public void jsxSet_orphans(final String orphans) {
        setStyleAttribute("orphans", orphans);
    }

    /**
     * Gets the "outline" style attribute.
     * @return the style attribute
     */
    public String jsxGet_outline() {
        return getStyleAttribute("outline", true);
    }

    /**
     * Sets the "outline" style attribute.
     * @param outline the new attribute.
     */
    public void jsxSet_outline(final String outline) {
        setStyleAttribute("outline", outline);
    }

    /**
     * Gets the "outlineColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_outlineColor() {
        return getStyleAttribute("outlineColor", true);
    }

    /**
     * Sets the "outlineColor" style attribute.
     * @param outlineColor the new attribute.
     */
    public void jsxSet_outlineColor(final String outlineColor) {
        setStyleAttribute("outlineColor", outlineColor);
    }

    /**
     * Gets the "outlineOffset" style attribute.
     * @return the style attribute
     */
    public String jsxGet_outlineOffset() {
        return getStyleAttribute("outlineOffset", true);
    }

    /**
     * Sets the "outlineOffset" style attribute.
     * @param outlineOffset the new attribute.
     */
    public void jsxSet_outlineOffset(final String outlineOffset) {
        setStyleAttribute("outlineOffset", outlineOffset);
    }

    /**
     * Gets the "outlineStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_outlineStyle() {
        return getStyleAttribute("outlineStyle", true);
    }

    /**
     * Sets the "outlineStyle" style attribute.
     * @param outlineStyle the new attribute.
     */
    public void jsxSet_outlineStyle(final String outlineStyle) {
        setStyleAttribute("outlineStyle", outlineStyle);
    }

    /**
     * Gets the "outlineWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_outlineWidth() {
        return getStyleAttribute("outlineWidth", true);
    }

    /**
     * Sets the "outlineWidth" style attribute.
     * @param outlineWidth the new attribute.
     */
    public void jsxSet_outlineWidth(final String outlineWidth) {
        setStyleAttribute("outlineWidth", outlineWidth);
    }

    /**
     * Gets the "overflow" style attribute.
     * @return the style attribute
     */
    public String jsxGet_overflow() {
        return getStyleAttribute("overflow", true);
    }

    /**
     * Sets the "overflow" style attribute.
     * @param overflow the new attribute.
     */
    public void jsxSet_overflow(final String overflow) {
        setStyleAttribute("overflow", overflow);
    }

    /**
     * Gets the "overflowX" style attribute.
     * @return the style attribute
     */
    public String jsxGet_overflowX() {
        return getStyleAttribute("overflowX", true);
    }

    /**
     * Sets the "overflowX" style attribute.
     * @param overflowX the new attribute.
     */
    public void jsxSet_overflowX(final String overflowX) {
        setStyleAttribute("overflowX", overflowX);
    }

    /**
     * Gets the "overflowY" style attribute.
     * @return the style attribute
     */
    public String jsxGet_overflowY() {
        return getStyleAttribute("overflowY", true);
    }

    /**
     * Sets the "overflowY" style attribute.
     * @param overflowY the new attribute.
     */
    public void jsxSet_overflowY(final String overflowY) {
        setStyleAttribute("overflowY", overflowY);
    }

    /**
     * Gets the "padding" style attribute.
     * @return the style attribute
     */
    public String jsxGet_padding() {
        return getStyleAttribute("padding", true);
    }

    /**
     * Sets the "padding" style attribute.
     * @param padding the new attribute.
     */
    public void jsxSet_padding(final String padding) {
        setStyleAttribute("padding", padding);
    }

    /**
     * Gets the "paddingBottom" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingBottom() {
        return getStyleAttribute("paddingBottom", true);
    }

    /**
     * Sets the "paddingBottom" style attribute.
     * @param paddingBottom the new attribute.
     */
    public void jsxSet_paddingBottom(final String paddingBottom) {
        setStyleAttribute("paddingBottom", paddingBottom);
    }

    /**
     * Gets the "paddingLeft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingLeft() {
        return getStyleAttribute("paddingLeft", true);
    }

    /**
     * Sets the "paddingLeft" style attribute.
     * @param paddingLeft the new attribute.
     */
    public void jsxSet_paddingLeft(final String paddingLeft) {
        setStyleAttribute("paddingLeft", paddingLeft);
    }

    /**
     * Gets the "paddingRight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingRight() {
        return getStyleAttribute("paddingRight", true);
    }

    /**
     * Sets the "paddingRight" style attribute.
     * @param paddingRight the new attribute.
     */
    public void jsxSet_paddingRight(final String paddingRight) {
        setStyleAttribute("paddingRight", paddingRight);
    }

    /**
     * Gets the "paddingTop" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingTop() {
        return getStyleAttribute("paddingTop", true);
    }

    /**
     * Sets the "paddingTop" style attribute.
     * @param paddingTop the new attribute.
     */
    public void jsxSet_paddingTop(final String paddingTop) {
        setStyleAttribute("paddingTop", paddingTop);
    }

    /**
     * Gets the "page" style attribute.
     * @return the style attribute
     */
    public String jsxGet_page() {
        return getStyleAttribute("page", true);
    }

    /**
     * Sets the "page" style attribute.
     * @param page the new attribute.
     */
    public void jsxSet_page(final String page) {
        setStyleAttribute("page", page);
    }

    /**
     * Gets the "pageBreakAfter" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pageBreakAfter() {
        return getStyleAttribute("pageBreakAfter", true);
    }

    /**
     * Sets the "pageBreakAfter" style attribute.
     * @param pageBreakAfter the new attribute.
     */
    public void jsxSet_pageBreakAfter(final String pageBreakAfter) {
        setStyleAttribute("pageBreakAfter", pageBreakAfter);
    }

    /**
     * Gets the "pageBreakBefore" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pageBreakBefore() {
        return getStyleAttribute("pageBreakBefore", true);
    }

    /**
     * Sets the "pageBreakBefore" style attribute.
     * @param pageBreakBefore the new attribute.
     */
    public void jsxSet_pageBreakBefore(final String pageBreakBefore) {
        setStyleAttribute("pageBreakBefore", pageBreakBefore);
    }

    /**
     * Gets the "pageBreakInside" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pageBreakInside() {
        return getStyleAttribute("pageBreakInside", true);
    }

    /**
     * Sets the "pageBreakInside" style attribute.
     * @param pageBreakInside the new attribute.
     */
    public void jsxSet_pageBreakInside(final String pageBreakInside) {
        setStyleAttribute("pageBreakInside", pageBreakInside);
    }

    /**
     * Gets the "pause" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pause() {
        return getStyleAttribute("pause", true);
    }

    /**
     * Sets the "pause" style attribute.
     * @param pause the new attribute.
     */
    public void jsxSet_pause(final String pause) {
        setStyleAttribute("pause", pause);
    }

    /**
     * Gets the "pauseAfter" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pauseAfter() {
        return getStyleAttribute("pauseAfter", true);
    }

    /**
     * Sets the "pauseAfter" style attribute.
     * @param pauseAfter the new attribute.
     */
    public void jsxSet_pauseAfter(final String pauseAfter) {
        setStyleAttribute("pauseAfter", pauseAfter);
    }

    /**
     * Gets the "pauseBefore" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pauseBefore() {
        return getStyleAttribute("pauseBefore", true);
    }

    /**
     * Sets the "pauseBefore" style attribute.
     * @param pauseBefore the new attribute.
     */
    public void jsxSet_pauseBefore(final String pauseBefore) {
        setStyleAttribute("pauseBefore", pauseBefore);
    }

    /**
     * Gets the "pitch" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pitch() {
        return getStyleAttribute("pitch", true);
    }

    /**
     * Sets the "pitch" style attribute.
     * @param pitch the new attribute.
     */
    public void jsxSet_pitch(final String pitch) {
        setStyleAttribute("pitch", pitch);
    }

    /**
     * Gets the "pitchRange" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pitchRange() {
        return getStyleAttribute("pitchRange", true);
    }

    /**
     * Sets the "pitchRange" style attribute.
     * @param pitchRange the new attribute.
     */
    public void jsxSet_pitchRange(final String pitchRange) {
        setStyleAttribute("pitchRange", pitchRange);
    }

    /**
     * Gets the "posBottom" style attribute.
     * @return the style attribute
     */
    public int jsxGet_posBottom() {
        return 0;
    }

    /**
     * Sets the "posBottom" style attribute.
     * @param posBottom the new attribute.
     */
    public void jsxSet_posBottom(final int posBottom) {
        //empty
    }

    /**
     * Gets the "posHeight" style attribute.
     * @return the style attribute
     */
    public int jsxGet_posHeight() {
        return 0;
    }

    /**
     * Sets the "posHeight" style attribute.
     * @param posHeight the new attribute.
     */
    public void jsxSet_posHeight(final int posHeight) {
        //empty
    }

    /**
     * Gets the "position" style attribute.
     * @return the style attribute
     */
    public String jsxGet_position() {
        return getStyleAttribute("position", true);
    }

    /**
     * Sets the "position" style attribute.
     * @param position the new attribute.
     */
    public void jsxSet_position(final String position) {
        setStyleAttribute("position", position);
    }

    /**
     * Gets the "posLeft" style attribute.
     * @return the style attribute
     */
    public int jsxGet_posLeft() {
        return 0;
    }

    /**
     * Sets the "posLeft" style attribute.
     * @param posLeft the new attribute.
     */
    public void jsxSet_posLeft(final int posLeft) {
        //empty
    }

    /**
     * Gets the "posRight" style attribute.
     * @return the style attribute
     */
    public int jsxGet_posRight() {
        return 0;
    }

    /**
     * Sets the "posRight" style attribute.
     * @param posRight the new attribute.
     */
    public void jsxSet_posRight(final int posRight) {
        //empty
    }

    /**
     * Gets the "posTop" style attribute.
     * @return the style attribute
     */
    public int jsxGet_posTop() {
        return 0;
    }

    /**
     * Sets the "posTop" style attribute.
     * @param posTop the new attribute.
     */
    public void jsxSet_posTop(final int posTop) {
        //empty
    }

    /**
     * Gets the "posWidth" style attribute.
     * @return the style attribute
     */
    public int jsxGet_posWidth() {
        return 0;
    }

    /**
     * Sets the "posWidth" style attribute.
     * @param posWidth the new attribute.
     */
    public void jsxSet_posWidth(final int posWidth) {
        //empty
    }

    /**
     * Gets the "quotes" style attribute.
     * @return the style attribute
     */
    public String jsxGet_quotes() {
        return getStyleAttribute("quotes", true);
    }

    /**
     * Sets the "quotes" style attribute.
     * @param quotes the new attribute.
     */
    public void jsxSet_quotes(final String quotes) {
        setStyleAttribute("quotes", quotes);
    }

    /**
     * Gets the "richness" style attribute.
     * @return the style attribute
     */
    public String jsxGet_richness() {
        return getStyleAttribute("richness", true);
    }

    /**
     * Sets the "richness" style attribute.
     * @param richness the new attribute.
     */
    public void jsxSet_richness(final String richness) {
        setStyleAttribute("richness", richness);
    }

    /**
     * Gets the "right" style attribute.
     * @return the style attribute
     */
    public String jsxGet_right() {
        return getStyleAttribute("right", true);
    }

    /**
     * Sets the "right" style attribute.
     * @param right the new attribute.
     */
    public void jsxSet_right(final String right) {
        setStyleAttribute("right", right);
    }

    /**
     * Gets the "rubyAlign" style attribute.
     * @return the style attribute
     */
    public String jsxGet_rubyAlign() {
        return getStyleAttribute("rubyAlign", true);
    }

    /**
     * Sets the "rubyAlign" style attribute.
     * @param rubyAlign the new attribute.
     */
    public void jsxSet_rubyAlign(final String rubyAlign) {
        setStyleAttribute("rubyAlign", rubyAlign);
    }

    /**
     * Gets the "rubyOverhang" style attribute.
     * @return the style attribute
     */
    public String jsxGet_rubyOverhang() {
        return getStyleAttribute("rubyOverhang", true);
    }

    /**
     * Sets the "rubyOverhang" style attribute.
     * @param rubyOverhang the new attribute.
     */
    public void jsxSet_rubyOverhang(final String rubyOverhang) {
        setStyleAttribute("rubyOverhang", rubyOverhang);
    }

    /**
     * Gets the "rubyPosition" style attribute.
     * @return the style attribute
     */
    public String jsxGet_rubyPosition() {
        return getStyleAttribute("rubyPosition", true);
    }

    /**
     * Sets the "rubyPosition" style attribute.
     * @param rubyPosition the new attribute.
     */
    public void jsxSet_rubyPosition(final String rubyPosition) {
        setStyleAttribute("rubyPosition", rubyPosition);
    }

    /**
     * Gets the "scrollbar3dLightColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbar3dLightColor() {
        return getStyleAttribute("scrollbar3dLightColor", true);
    }

    /**
     * Sets the "scrollbar3dLightColor" style attribute.
     * @param scrollbar3dLightColor the new attribute.
     */
    public void jsxSet_scrollbar3dLightColor(final String scrollbar3dLightColor) {
        setStyleAttribute("scrollbar3dLightColor", scrollbar3dLightColor);
    }

    /**
     * Gets the "scrollbarArrowColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarArrowColor() {
        return getStyleAttribute("scrollbarArrowColor", true);
    }

    /**
     * Sets the "scrollbarArrowColor" style attribute.
     * @param scrollbarArrowColor the new attribute.
     */
    public void jsxSet_scrollbarArrowColor(final String scrollbarArrowColor) {
        setStyleAttribute("scrollbarArrowColor", scrollbarArrowColor);
    }

    /**
     * Gets the "scrollbarBaseColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarBaseColor() {
        return getStyleAttribute("scrollbarBaseColor", true);
    }

    /**
     * Sets the "scrollbarBaseColor" style attribute.
     * @param scrollbarBaseColor the new attribute.
     */
    public void jsxSet_scrollbarBaseColor(final String scrollbarBaseColor) {
        setStyleAttribute("scrollbarBaseColor", scrollbarBaseColor);
    }

    /**
     * Gets the "scrollbarDarkShadowColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarDarkShadowColor() {
        return getStyleAttribute("scrollbarDarkShadowColor", true);
    }

    /**
     * Sets the "scrollbarDarkShadowColor" style attribute.
     * @param scrollbarDarkShadowColor the new attribute.
     */
    public void jsxSet_scrollbarDarkShadowColor(final String scrollbarDarkShadowColor) {
        setStyleAttribute("scrollbarDarkShadowColor", scrollbarDarkShadowColor);
    }

    /**
     * Gets the "scrollbarFaceColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarFaceColor() {
        return getStyleAttribute("scrollbarFaceColor", true);
    }

    /**
     * Sets the "scrollbarFaceColor" style attribute.
     * @param scrollbarFaceColor the new attribute.
     */
    public void jsxSet_scrollbarFaceColor(final String scrollbarFaceColor) {
        setStyleAttribute("scrollbarFaceColor", scrollbarFaceColor);
    }

    /**
     * Gets the "scrollbarHighlightColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarHighlightColor() {
        return getStyleAttribute("scrollbarHighlightColor", true);
    }

    /**
     * Sets the "scrollbarHighlightColor" style attribute.
     * @param scrollbarHighlightColor the new attribute.
     */
    public void jsxSet_scrollbarHighlightColor(final String scrollbarHighlightColor) {
        setStyleAttribute("scrollbarHighlightColor", scrollbarHighlightColor);
    }

    /**
     * Gets the "scrollbarShadowColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarShadowColor() {
        return getStyleAttribute("scrollbarShadowColor", true);
    }

    /**
     * Sets the "scrollbarShadowColor" style attribute.
     * @param scrollbarShadowColor the new attribute.
     */
    public void jsxSet_scrollbarShadowColor(final String scrollbarShadowColor) {
        setStyleAttribute("scrollbarShadowColor", scrollbarShadowColor);
    }

    /**
     * Gets the "scrollbarTrackColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarTrackColor() {
        return getStyleAttribute("scrollbarTrackColor", true);
    }

    /**
     * Sets the "scrollbarTrackColor" style attribute.
     * @param scrollbarTrackColor the new attribute.
     */
    public void jsxSet_scrollbarTrackColor(final String scrollbarTrackColor) {
        setStyleAttribute("scrollbarTrackColor", scrollbarTrackColor);
    }

    /**
     * Gets the "size" style attribute.
     * @return the style attribute
     */
    public String jsxGet_size() {
        return getStyleAttribute("size", true);
    }

    /**
     * Sets the "size" style attribute.
     * @param size the new attribute.
     */
    public void jsxSet_size(final String size) {
        setStyleAttribute("size", size);
    }

    /**
     * Gets the "speak" style attribute.
     * @return the style attribute
     */
    public String jsxGet_speak() {
        return getStyleAttribute("speak", true);
    }

    /**
     * Sets the "speak" style attribute.
     * @param speak the new attribute.
     */
    public void jsxSet_speak(final String speak) {
        setStyleAttribute("speak", speak);
    }

    /**
     * Gets the "speakHeader" style attribute.
     * @return the style attribute
     */
    public String jsxGet_speakHeader() {
        return getStyleAttribute("speakHeader", true);
    }

    /**
     * Sets the "speakHeader" style attribute.
     * @param speakHeader the new attribute.
     */
    public void jsxSet_speakHeader(final String speakHeader) {
        setStyleAttribute("speakHeader", speakHeader);
    }

    /**
     * Gets the "speakNumeral" style attribute.
     * @return the style attribute
     */
    public String jsxGet_speakNumeral() {
        return getStyleAttribute("speakNumeral", true);
    }

    /**
     * Sets the "speakNumeral" style attribute.
     * @param speakNumeral the new attribute.
     */
    public void jsxSet_speakNumeral(final String speakNumeral) {
        setStyleAttribute("speakNumeral", speakNumeral);
    }

    /**
     * Gets the "speakPunctuation" style attribute.
     * @return the style attribute
     */
    public String jsxGet_speakPunctuation() {
        return getStyleAttribute("speakPunctuation", true);
    }

    /**
     * Sets the "speakPunctuation" style attribute.
     * @param speakPunctuation the new attribute.
     */
    public void jsxSet_speakPunctuation(final String speakPunctuation) {
        setStyleAttribute("speakPunctuation", speakPunctuation);
    }

    /**
     * Gets the "speechRate" style attribute.
     * @return the style attribute
     */
    public String jsxGet_speechRate() {
        return getStyleAttribute("speechRate", true);
    }

    /**
     * Sets the "speechRate" style attribute.
     * @param speechRate the new attribute.
     */
    public void jsxSet_speechRate(final String speechRate) {
        setStyleAttribute("speechRate", speechRate);
    }

    /**
     * Gets the "stress" style attribute.
     * @return the style attribute
     */
    public String jsxGet_stress() {
        return getStyleAttribute("stress", true);
    }

    /**
     * Sets the "stress" style attribute.
     * @param stress the new attribute.
     */
    public void jsxSet_stress(final String stress) {
        setStyleAttribute("stress", stress);
    }

    /**
     * Gets the "styleFloat" style attribute.
     * @return the style attribute
     */
    public String jsxGet_styleFloat() {
        return getStyleAttribute("float", true);
    }

    /**
     * Sets the "styleFloat" style attribute.
     * @param value the new attribute.
     */
    public void jsxSet_styleFloat(final String value) {
        setStyleAttribute("float", value);
    }

    /**
     * Gets the "tableLayout" style attribute.
     * @return the style attribute
     */
    public String jsxGet_tableLayout() {
        return getStyleAttribute("tableLayout", true);
    }

    /**
     * Sets the "tableLayout" style attribute.
     * @param tableLayout the new attribute.
     */
    public void jsxSet_tableLayout(final String tableLayout) {
        setStyleAttribute("tableLayout", tableLayout);
    }

    /**
     * Gets the "textAlign" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textAlign() {
        return getStyleAttribute("textAlign", true);
    }

    /**
     * Sets the "textAlign" style attribute.
     * @param textAlign the new attribute.
     */
    public void jsxSet_textAlign(final String textAlign) {
        setStyleAttribute("textAlign", textAlign);
    }

    /**
     * Gets the "textAlignLast" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textAlignLast() {
        return getStyleAttribute("textAlignLast", true);
    }

    /**
     * Sets the "textAlignLast" style attribute.
     * @param textAlignLast the new attribute.
     */
    public void jsxSet_textAlignLast(final String textAlignLast) {
        setStyleAttribute("textAlignLast", textAlignLast);
    }

    /**
     * Gets the "textAutospace" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textAutospace() {
        return getStyleAttribute("textAutospace", true);
    }

    /**
     * Sets the "textAutospace" style attribute.
     * @param textAutospace the new attribute.
     */
    public void jsxSet_textAutospace(final String textAutospace) {
        setStyleAttribute("textAutospace", textAutospace);
    }

    /**
     * Gets the "textDecoration" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textDecoration() {
        return getStyleAttribute("textDecoration", true);
    }

    /**
     * Sets the "textDecoration" style attribute.
     * @param textDecoration the new attribute.
     */
    public void jsxSet_textDecoration(final String textDecoration) {
        setStyleAttribute("textDecoration", textDecoration);
    }

    /**
     * Gets the "textDecorationBlink" style attribute.
     * @return the style attribute
     */
    public boolean jsxGet_textDecorationBlink() {
        return false;
    }

    /**
     * Sets the "textDecorationBlink" style attribute.
     * @param textDecorationBlink the new attribute.
     */
    public void jsxSet_textDecorationBlink(final boolean textDecorationBlink) {
        //empty
    }

    /**
     * Gets the "textDecorationLineThrough" style attribute.
     * @return the style attribute
     */
    public boolean jsxGet_textDecorationLineThrough() {
        return false;
    }

    /**
     * Sets the "textDecorationLineThrough" style attribute.
     * @param textDecorationLineThrough the new attribute.
     */
    public void jsxSet_textDecorationLineThrough(final boolean textDecorationLineThrough) {
        //empty
    }

    /**
     * Gets the "textDecorationNone" style attribute.
     * @return the style attribute
     */
    public boolean jsxGet_textDecorationNone() {
        return false;
    }

    /**
     * Sets the "textDecorationNone" style attribute.
     * @param textDecorationNone the new attribute.
     */
    public void jsxSet_textDecorationNone(final boolean textDecorationNone) {
        //empty
    }

    /**
     * Gets the "textDecorationOverline" style attribute.
     * @return the style attribute
     */
    public boolean jsxGet_textDecorationOverline() {
        return false;
    }

    /**
     * Sets the "textDecorationOverline" style attribute.
     * @param textDecorationOverline the new attribute.
     */
    public void jsxSet_textDecorationOverline(final boolean textDecorationOverline) {
        //empty
    }

    /**
     * Gets the "textDecorationUnderline" style attribute.
     * @return the style attribute
     */
    public boolean jsxGet_textDecorationUnderline() {
        return false;
    }

    /**
     * Sets the "textDecorationUnderline" style attribute.
     * @param textDecorationUnderline the new attribute.
     */
    public void jsxSet_textDecorationUnderline(final boolean textDecorationUnderline) {
        //empty
    }

    /**
     * Gets the "textIndent" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textIndent() {
        return getStyleAttribute("textIndent", true);
    }

    /**
     * Sets the "textIndent" style attribute.
     * @param textIndent the new attribute.
     */
    public void jsxSet_textIndent(final String textIndent) {
        setStyleAttribute("textIndent", textIndent);
    }

    /**
     * Gets the "textJustify" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textJustify() {
        return getStyleAttribute("textJustify", true);
    }

    /**
     * Sets the "textJustify" style attribute.
     * @param textJustify the new attribute.
     */
    public void jsxSet_textJustify(final String textJustify) {
        setStyleAttribute("textJustify", textJustify);
    }

    /**
     * Gets the "textJustifyTrim" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textJustifyTrim() {
        return getStyleAttribute("textJustifyTrim", true);
    }

    /**
     * Sets the "textJustifyTrim" style attribute.
     * @param textJustifyTrim the new attribute.
     */
    public void jsxSet_textJustifyTrim(final String textJustifyTrim) {
        setStyleAttribute("textJustifyTrim", textJustifyTrim);
    }

    /**
     * Gets the "textKashida" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textKashida() {
        return getStyleAttribute("textKashida", true);
    }

    /**
     * Sets the "textKashida" style attribute.
     * @param textKashida the new attribute.
     */
    public void jsxSet_textKashida(final String textKashida) {
        setStyleAttribute("textKashida", textKashida);
    }

    /**
     * Gets the "textKashidaSpace" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textKashidaSpace() {
        return getStyleAttribute("textKashidaSpace", true);
    }

    /**
     * Sets the "textKashidaSpace" style attribute.
     * @param textKashidaSpace the new attribute.
     */
    public void jsxSet_textKashidaSpace(final String textKashidaSpace) {
        setStyleAttribute("textKashidaSpace", textKashidaSpace);
    }

    /**
     * Gets the "textOverflow" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textOverflow() {
        return getStyleAttribute("textOverflow", true);
    }

    /**
     * Sets the "textOverflow" style attribute.
     * @param textOverflow the new attribute.
     */
    public void jsxSet_textOverflow(final String textOverflow) {
        setStyleAttribute("textOverflow", textOverflow);
    }

    /**
     * Gets the "textShadow" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textShadow() {
        return getStyleAttribute("textShadow", true);
    }

    /**
     * Sets the "textShadow" style attribute.
     * @param textShadow the new attribute.
     */
    public void jsxSet_textShadow(final String textShadow) {
        setStyleAttribute("textShadow", textShadow);
    }

    /**
     * Gets the "textTransform" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textTransform() {
        return getStyleAttribute("textTransform", true);
    }

    /**
     * Sets the "textTransform" style attribute.
     * @param textTransform the new attribute.
     */
    public void jsxSet_textTransform(final String textTransform) {
        setStyleAttribute("textTransform", textTransform);
    }

    /**
     * Gets the "textUnderlinePosition" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textUnderlinePosition() {
        return getStyleAttribute("textUnderlinePosition", true);
    }

    /**
     * Sets the "textUnderlinePosition" style attribute.
     * @param textUnderlinePosition the new attribute.
     */
    public void jsxSet_textUnderlinePosition(final String textUnderlinePosition) {
        setStyleAttribute("textUnderlinePosition", textUnderlinePosition);
    }

    /**
     * Gets the "top" style attribute.
     * @return the style attribute
     */
    public String jsxGet_top() {
        return getStyleAttribute("top", true);
    }

    /**
     * Sets the "top" style attribute.
     * @param top the new attribute.
     */
    public void jsxSet_top(final String top) {
        setStyleAttribute("top", top);
    }

    /**
     * Gets the "unicodeBidi" style attribute.
     * @return the style attribute
     */
    public String jsxGet_unicodeBidi() {
        return getStyleAttribute("unicodeBidi", true);
    }

    /**
     * Sets the "unicodeBidi" style attribute.
     * @param unicodeBidi the new attribute.
     */
    public void jsxSet_unicodeBidi(final String unicodeBidi) {
        setStyleAttribute("unicodeBidi", unicodeBidi);
    }

    /**
     * Gets the "verticalAlign" style attribute.
     * @return the style attribute
     */
    public String jsxGet_verticalAlign() {
        return getStyleAttribute("verticalAlign", true);
    }

    /**
     * Sets the "verticalAlign" style attribute.
     * @param verticalAlign the new attribute.
     */
    public void jsxSet_verticalAlign(final String verticalAlign) {
        setStyleAttribute("verticalAlign", verticalAlign);
    }

    /**
     * Gets the "visibility" style attribute.
     * @return the style attribute
     */
    public String jsxGet_visibility() {
        return getStyleAttribute("visibility", true);
    }

    /**
     * Sets the "visibility" style attribute.
     * @param visibility the new attribute.
     */
    public void jsxSet_visibility(final String visibility) {
        setStyleAttribute("visibility", visibility);
    }

    /**
     * Gets the "voiceFamily" style attribute.
     * @return the style attribute
     */
    public String jsxGet_voiceFamily() {
        return getStyleAttribute("voiceFamily", true);
    }

    /**
     * Sets the "voiceFamily" style attribute.
     * @param voiceFamily the new attribute.
     */
    public void jsxSet_voiceFamily(final String voiceFamily) {
        setStyleAttribute("voiceFamily", voiceFamily);
    }

    /**
     * Gets the "volume" style attribute.
     * @return the style attribute
     */
    public String jsxGet_volume() {
        return getStyleAttribute("volume", true);
    }

    /**
     * Sets the "volume" style attribute.
     * @param volume the new attribute.
     */
    public void jsxSet_volume(final String volume) {
        setStyleAttribute("volume", volume);
    }

    /**
     * Gets the "whiteSpace" style attribute.
     * @return the style attribute
     */
    public String jsxGet_whiteSpace() {
        return getStyleAttribute("whiteSpace", true);
    }

    /**
     * Sets the "whiteSpace" style attribute.
     * @param whiteSpace the new attribute.
     */
    public void jsxSet_whiteSpace(final String whiteSpace) {
        setStyleAttribute("whiteSpace", whiteSpace);
    }

    /**
     * Gets the "widows" style attribute.
     * @return the style attribute
     */
    public String jsxGet_widows() {
        return getStyleAttribute("widows", true);
    }

    /**
     * Sets the "widows" style attribute.
     * @param widows the new attribute.
     */
    public void jsxSet_widows(final String widows) {
        setStyleAttribute("widows", widows);
    }

    /**
     * Gets the "width" style attribute.
     * @return the style attribute
     */
    public String jsxGet_width() {
        return getStyleAttribute("width", true);
    }

    /**
     * Sets the "width" style attribute.
     * @param width the new attribute.
     */
    public void jsxSet_width(final String width) {
        setStyleAttribute("width", width);
    }

    /**
     * Gets the "wordBreak" style attribute.
     * @return the style attribute
     */
    public String jsxGet_wordBreak() {
        return getStyleAttribute("wordBreak", true);
    }

    /**
     * Sets the "wordBreak" style attribute.
     * @param wordBreak the new attribute.
     */
    public void jsxSet_wordBreak(final String wordBreak) {
        setStyleAttribute("wordBreak", wordBreak);
    }

    /**
     * Gets the "wordSpacing" style attribute.
     * @return the style attribute
     */
    public String jsxGet_wordSpacing() {
        return getStyleAttribute("wordSpacing", true);
    }

    /**
     * Sets the "wordSpacing" style attribute.
     * @param wordSpacing the new attribute.
     */
    public void jsxSet_wordSpacing(final String wordSpacing) {
        setStyleAttribute("wordSpacing", wordSpacing);
    }

    /**
     * Gets the "wordWrap" style attribute.
     * @return the style attribute
     */
    public String jsxGet_wordWrap() {
        return getStyleAttribute("wordWrap", true);
    }

    /**
     * Sets the "wordWrap" style attribute.
     * @param wordWrap the new attribute.
     */
    public void jsxSet_wordWrap(final String wordWrap) {
        setStyleAttribute("wordWrap", wordWrap);
    }

    /**
     * Gets the "writingMode" style attribute.
     * @return the style attribute
     */
    public String jsxGet_writingMode() {
        return getStyleAttribute("writingMode", true);
    }

    /**
     * Sets the "writingMode" style attribute.
     * @param writingMode the new attribute.
     */
    public void jsxSet_writingMode(final String writingMode) {
        setStyleAttribute("writingMode", writingMode);
    }

    /**
     * Gets the "zIndex" style attribute.
     * @return the style attribute
     */
    public Object jsxGet_zIndex() {
        if (getWindow().getWebWindow().getWebClient().getBrowserVersion().isIE()) {
            return 0;
        }
        else {
            return "";
        }
    }

    /**
     * Sets the "zIndex" style attribute.
     * @param zIndex the new attribute
     */
    public void jsxSet_zIndex(final Object zIndex) {
        //empty
    }

    /**
     * Gets the "zoom" style attribute.
     * @return the style attribute
     */
    public String jsxGet_zoom() {
        return getStyleAttribute("zoom", true);
    }

    /**
     * Sets the "zoom" style attribute.
     * @param zoom the new attribute.
     */
    public void jsxSet_zoom(final String zoom) {
        setStyleAttribute("zoom", zoom);
    }

    /**
     * Gets the value of the specified property of the style.
     * @param name the style property name
     * @return empty string if nothing found
     */
    public String jsxFunction_getPropertyValue(final String name) {
        if (name != null && name.contains("-")) {
            final Object value = getProperty(this, camelize(name));
            if (value instanceof String) {
                return (String) value;
            }
        }
        return getStyleAttribute(name, false);
    }

    /**
     * Sets an expression for the specified Style.
     *
     * @param propertyName Specifies the name of the property to which expression is added.
     * @param expression specifies any valid script statement without quotations or semicolons.
     *        This string can include references to other properties on the current page.
     *        Array references are not allowed on object properties included in this script.
     * @param language specified the language used.
     */
    public void jsxFunction_setExpression(final String propertyName, final String expression, final String language) {
        // empty implementation
    }
    
    /**
     * Removes the expression from the specified property.
     *
     * @param propertyName Specifies the name of the property from which to remove an expression.
     * @return true if the expression was successfully removed.
     */
    public boolean jsxFunction_removeExpression(final String propertyName) {
        return true;
    }

    /**
     * Searches for any color notation in the specified text.
     * @param text the string to search in.
     * @return the string of the color if found, null otherwise.
     */
    private static String findColor(final String text) {
        final Pattern p = Pattern.compile("(rgb.*?\\(.*?\\d{1,3}.*?,.*?\\d{1,3}.*?,.*?\\d{1,3}.*?\\))");
        final Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        final String[] tokens = text.split(" ");
        for (final String token : tokens) {
            if (isColorKeyword(token)) {
                return token;
            }
            else if (isColorHexadecimal(token)) {
                return token;
            }
        }
        return null;
    }

    /**
     * Searches for a border style in the specified text.
     * @param text the string to search in.
     * @return the border style if found, null otherwise.
     */
    private static String findBorderStyle(final String text) {
        for (final String token : text.split(" ")) {
            if (isBorderStyle(token)) {
                return token;
            }
        }
        return null;
    }

    /**
     * Searches for a border width in the specified text.
     * @param text the string to search in.
     * @return the border width if found, null otherwise.
     */
    private static String findBorderWidth(final String text) {
        for (final String token : text.split(" ")) {
            if (isBorderWidth(token)) {
                return token;
            }
        }
        return null;
    }

    /**
     * Returns if the specified token is an RGB in hexadecimal notation.
     * @param token the token to check.
     * @return whether the token is a color in hexadecimal notation or not.
     */
    private static boolean isColorHexadecimal(final String token) {
        return token.toLowerCase().matches("#([0-9a-f]{3}|[0-9a-f]{6})");
    }

    /**
     * Returns if the specified token is a reserved color keyword.
     * @param token the token to check.
     * @return whether the token is a reserved color keyword or not.
     */
    private static boolean isColorKeyword(final String token) {
        return token.equalsIgnoreCase("aqua") || token.equalsIgnoreCase("black")
            || token.equalsIgnoreCase("blue") || token.equalsIgnoreCase("fuchsia")
            || token.equalsIgnoreCase("gray") || token.equalsIgnoreCase("green")
            || token.equalsIgnoreCase("lime") || token.equalsIgnoreCase("maroon")
            || token.equalsIgnoreCase("navy") || token.equalsIgnoreCase("olive")
            || token.equalsIgnoreCase("purple") || token.equalsIgnoreCase("red")
            || token.equalsIgnoreCase("silver") || token.equalsIgnoreCase("teal")
            || token.equalsIgnoreCase("white") || token.equalsIgnoreCase("yellow");
    }
    
    /**
     * Returns if the specified token is a border style.
     * @param token the token to check.
     * @return whether the token is a border style or not.
     */
    private static boolean isBorderStyle(final String token) {
        return token.equalsIgnoreCase("none") || token.equalsIgnoreCase("hidden")
            || token.equalsIgnoreCase("dotted") || token.equalsIgnoreCase("dashed")
            || token.equalsIgnoreCase("solid") || token.equalsIgnoreCase("double")
            || token.equalsIgnoreCase("groove") || token.equalsIgnoreCase("ridge")
            || token.equalsIgnoreCase("inset") || token.equalsIgnoreCase("outset");
    }

    /**
     * Returns if the specified token is a border width.
     * @param token the token to check.
     * @return whether the token is a border width or not.
     */
    private static boolean isBorderWidth(final String token) {
        return token.equalsIgnoreCase("thin") || token.equalsIgnoreCase("medium")
            || token.equalsIgnoreCase("thick ") || isLength(token);
    }

    /**
     * Returns if the specified token is a length.
     * @param token the token to check.
     * @return whether the token is a length or not.
     */
    private static boolean isLength(String token) {
        if (token.endsWith("em") || token.endsWith("ex") || token.endsWith("px") || token.endsWith("in")
            || token.endsWith("cm") || token.endsWith("mm") || token.endsWith("pt") || token.endsWith("pc")
            || token.endsWith("%")) {
            
            if (token.endsWith("%")) {
                token = token.substring(0, token.length() - 1);
            }
            else {
                token = token.substring(0, token.length() - 2);
            }
            try {
                Float.parseFloat(token);
                return true;
            }
            catch (final Exception e) {
                //ignore
            }
        }
        return false;
    }
}
