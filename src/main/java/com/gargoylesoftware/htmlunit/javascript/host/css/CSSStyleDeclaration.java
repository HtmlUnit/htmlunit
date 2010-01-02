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

import static com.gargoylesoftware.htmlunit.util.StringUtils.isFloat;

import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;

import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS21;

/**
 * A JavaScript object for a CSSStyleDeclaration.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Rodney Gitzel
 * @author Sudhan Moghe
 */
public class CSSStyleDeclaration extends SimpleScriptable {

    private static final long serialVersionUID = -1976370264911039311L;
    private static final Log LOG = LogFactory.getLog(CSSStyleDeclaration.class);
    private static Map<String, String> CSSColors_ = new HashMap<String, String>();

    /** The different types of shorthand values. */
    private enum Shorthand {
        TOP("Top"),
        RIGHT("Right"),
        BOTTOM("Bottom"),
        LEFT("Left");

        private final String string_;

        Shorthand(final String stringRepresentation) {
            string_ = stringRepresentation;
        }

        @Override
        public String toString() {
            return string_;
        }
    }

    /** Used to parse URLs. */
    private static final MessageFormat URL_FORMAT = new MessageFormat("url({0})");

    /** The element to which this style belongs. */
    private HTMLElement jsElement_;

    /** The wrapped CSSStyleDeclaration (if created from CSSStyleRule). */
    private org.w3c.dom.css.CSSStyleDeclaration styleDeclaration_;

    /** The current style element index. */
    private long currentElementIndex_;

    static {
        CSSColors_.put("aqua", "rgb(0, 255, 255)");
        CSSColors_.put("black", "rgb(0, 0, 0)");
        CSSColors_.put("blue", "rgb(0, 0, 255)");
        CSSColors_.put("fuchsia", "rgb(255, 0, 255)");
        CSSColors_.put("gray", "rgb(128, 128, 128)");
        CSSColors_.put("green", "rgb(0, 128, 0)");
        CSSColors_.put("lime", "rgb(0, 255, 0)");
        CSSColors_.put("maroon", "rgb(128, 0, 0)");
        CSSColors_.put("navy", "rgb(0, 0, 128)");
        CSSColors_.put("olive", "rgb(128, 128, 0)");
        CSSColors_.put("purple", "rgb(128, 0, 128)");
        CSSColors_.put("red", "rgb(255, 0, 0)");
        CSSColors_.put("silver", "rgb(192, 192, 192)");
        CSSColors_.put("teal", "rgb(0, 128, 128)");
        CSSColors_.put("white", "rgb(255, 255, 255)");
        CSSColors_.put("yellow", "rgb(255, 255, 0)");
    }

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public CSSStyleDeclaration() {
        // Empty.
    }

    /**
     * Creates an instance and sets its parent scope to the one of the provided element.
     * @param element the element to which this style is bound
     */
    public CSSStyleDeclaration(final HTMLElement element) {
        setParentScope(element.getParentScope());
        setPrototype(getPrototype(getClass()));
        initialize(element);
    }

    /**
     * Creates an instance which wraps the specified style declaration.
     * @param parentScope the parent scope to use
     * @param styleDeclaration the style declaration to wrap
     */
    CSSStyleDeclaration(final Scriptable parentScope, final org.w3c.dom.css.CSSStyleDeclaration styleDeclaration) {
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
        styleDeclaration_ = styleDeclaration;
    }

    /**
     * Initializes the object.
     * @param htmlElement the element that this style describes
     */
    void initialize(final HTMLElement htmlElement) {
        // Initialize.
        WebAssert.notNull("htmlElement", htmlElement);
        jsElement_ = htmlElement;
        setDomNode(htmlElement.getDomNodeOrNull(), false);
        // If an IE behavior was specified in the style, apply the behavior.
        if (getBrowserVersion().isIE()) {
            for (final StyleElement element : getStyleMap(true).values()) {
                if ("behavior".equals(element.getName())) {
                    try {
                        final Object[] url = URL_FORMAT.parse(element.getValue());
                        if (url.length > 0) {
                            jsElement_.jsxFunction_addBehavior((String) url[0]);
                            break;
                        }
                    }
                    catch (final ParseException e) {
                        LOG.warn("Invalid behavior: '" + element.getValue() + "'.");
                    }
                }
            }
        }
    }

    /**
     * Returns the element to which this style belongs.
     * @return the element to which this style belongs
     */
    protected HTMLElement getElement() {
        return jsElement_;
    }

    /**
     * Returns the value of the named style attribute, or an empty string if it is not found.
     *
     * @param name the name of the style attribute whose value is to be retrieved
     * @param camelCase whether or not the name is expected to be in camel case
     * @return the named style attribute value, or an empty string if it is not found
     */
    protected String getStyleAttribute(String name, final boolean camelCase) {
        if (styleDeclaration_ != null) {
            if (camelCase) {
                name = uncamelize(name);
            }
            return styleDeclaration_.getPropertyValue(name);
        }
        final StyleElement element = getStyleMap(camelCase).get(name);
        if (element != null && element.getValue() != null) {
            return element.getValue();
        }
        return "";
    }

    /**
     * <p>Returns the value of one of the two named style attributes. If both attributes exist,
     * the value of the attribute that was declared last is returned. If only one of the
     * attributes exists, its value is returned. If neither attribute exists, an empty string
     * is returned.</p>
     *
     * <p>The second named attribute may be shorthand for a the actual desired property.
     * The following formats are possible:</p>
     * <ol>
     *   <li><tt>top right bottom left</tt>: All values are explicit.</li>
     *   <li><tt>top right bottom</tt>: Left is implicitly the same as right.</li>
     *   <li><tt>top right</tt>: Left is implicitly the same as right, bottom is implicitly the same as top.</li>
     *   <li><tt>top</tt>: Left, bottom and right are implicitly the same as top.</li>
     * </ol>
     *
     * @param name1 the name of the first style attribute
     * @param name2 the name of the second style attribute
     * @param shorthand the type of shorthand value to return, if any
     * @param camelCase whether or not the names are expected to be in camel case
     * @return the value of one of the two named style attributes
     */
    private String getStyleAttribute(final String name1, final String name2, final Shorthand shorthand,
        final boolean camelCase) {

        final Map<String, StyleElement> styleMap = getStyleMap(camelCase);
        final StyleElement element1 = styleMap.get(name1);
        final StyleElement element2 = styleMap.get(name2);
        if (element1 == null && element2 == null) {
            return "";
        }

        final String value;
        final boolean mayBeShorthand;
        if (element1 != null && element2 == null) {
            value = element1.getValue();
            mayBeShorthand = false;
        }
        else if (element1 == null && element2 != null) {
            value = element2.getValue();
            mayBeShorthand = true;
        }
        else if (element1.getIndex() > element2.getIndex()) {
            value = element1.getValue();
            mayBeShorthand = false;
        }
        else {
            value = element2.getValue();
            mayBeShorthand = true;
        }

        if (!mayBeShorthand) {
            return value;
        }

        final String[] values = value.split("\\s+");
        switch (shorthand) {
            case TOP:
                return values[0];
            case RIGHT:
                if (values.length > 1) {
                    return values[1];
                }
                return values[0];
            case BOTTOM:
                if (values.length > 2) {
                    return values[2];
                }
                return values[0];
            case LEFT:
                if (values.length > 3) {
                    return values[3];
                }
                else if (values.length > 1) {
                    return values[1];
                }
                else {
                    return values[0];
                }
            default:
                throw new IllegalStateException("Unknown shorthand value: " + shorthand);
        }
    }

    /**
     * Sets the specified style attribute.
     * @param name the attribute name (camel-cased)
     * @param newValue the attribute value
     */
    protected void setStyleAttribute(String name, final String newValue) {
        name = uncamelize(name);
        if (styleDeclaration_ != null) {
            styleDeclaration_.setProperty(name, newValue, null);
        }
        else {
            replaceStyleAttribute(name, newValue);
        }
    }

    /**
     * Replaces the value of the named style attribute. If there is no style attribute with the
     * specified name, a new one is added. If the specified value is an empty (or all whitespace)
     * string, this method actually removes the named style attribute.
     * @param name the attribute name (delimiter-separated, not camel-cased)
     * @param value the attribute value
     */
    private void replaceStyleAttribute(final String name, final String value) {
        if (value.trim().length() == 0) {
            removeStyleAttribute(name);
        }
        else {
            final Map<String, StyleElement> styleMap = getStyleMap(false);
            final StyleElement old = styleMap.get(name);
            final Long index;
            if (old != null) {
                index = old.getIndex();
            }
            else {
                index = getCurrentElementIndex();
            }
            final StyleElement element = new StyleElement(name, value, index);
            styleMap.put(name, element);
            writeToElement(styleMap);
        }
    }

    /**
     * Removes the specified style attribute, returning the element index of the removed attribute.
     * @param name the attribute name (delimiter-separated, not camel-cased)
     * @return the style element index of the removed attribute, or <tt>null</tt> if no attribute was removed
     */
    private Long removeStyleAttribute(final String name) {
        final Map<String, StyleElement> styleMap = getStyleMap(false);
        if (!styleMap.containsKey(name)) {
            return null;
        }
        final StyleElement removed = styleMap.remove(name);
        writeToElement(styleMap);
        return removed.getIndex();
    }

    /**
     * Returns a sorted map containing style elements, keyed on style element name. We use a
     * {@link LinkedHashMap} map so that results are deterministic and are thus testable.
     *
     * @param camelCase if <tt>true</tt>, the keys are camel cased (i.e. <tt>fontSize</tt>),
     *        if <tt>false</tt>, the keys are delimiter-separated (i.e. <tt>font-size</tt>).
     * @return a sorted map containing style elements, keyed on style element name
     */
    protected Map<String, StyleElement> getStyleMap(final boolean camelCase) {
        final Map<String, StyleElement> styleMap = new LinkedHashMap<String, StyleElement>();
        final String styleAttribute = jsElement_.getDomNodeOrDie().getAttribute("style");
        for (final String token : styleAttribute.split(";")) {
            final int index = token.indexOf(":");
            if (index != -1) {
                String key = token.substring(0, index).trim().toLowerCase();
                if (camelCase) {
                    key = camelize(key);
                }
                final String value = token.substring(index + 1).trim();
                final StyleElement element = new StyleElement(key, value, getCurrentElementIndex());
                styleMap.put(key, element);
            }
        }
        return styleMap;
    }

    private void writeToElement(final Map<String, StyleElement> styleMap) {
        final StringBuilder buffer = new StringBuilder();
        final SortedSet<StyleElement> sortedValues = new TreeSet<StyleElement>(styleMap.values());
        for (final StyleElement e : sortedValues) {
            if (buffer.length() > 0) {
                buffer.append(" ");
            }
            buffer.append(e.getName());
            buffer.append(": ");
            buffer.append(e.getValue());
            buffer.append(";");
        }
        jsElement_.getDomNodeOrDie().setAttribute("style", buffer.toString());
    }

    /**
     * Returns the current style element index. An index is assigned to each style element so that
     * we can determine which style elements have precedence over others.
     *
     * This method also takes care of incrementing the index for the next use.
     *
     * @return the current style element index
     */
    protected long getCurrentElementIndex() {
        return currentElementIndex_++;
    }

    /**
     * Transforms the specified string from delimiter-separated (e.g. <tt>font-size</tt>)
     * to camel-cased (e.g. <tt>fontSize</tt>).
     * @param string the string to camelize
     * @return the transformed string
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
     * Transforms the specified string from camel-cased (e.g. <tt>fontSize</tt>) to
     * delimiter-separated (e.g. <tt>font-size</tt>)
     * @param string the string to uncamelize
     * @return the transformed string
     */
    protected static String uncamelize(final String string) {
        if (string == null) {
            return null;
        }
        return string.replaceAll("([A-Z])", "-$1").toLowerCase();
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
     * @param azimuth the new attribute
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
     * @param background the new attribute
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
     * @param backgroundAttachment the new attribute
     */
    public void jsxSet_backgroundAttachment(final String backgroundAttachment) {
        setStyleAttribute("backgroundAttachment", backgroundAttachment);
    }

    /**
     * Gets the "backgroundColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundColor() {
        String value = getStyleAttribute("backgroundColor", true);
        if (value.length() == 0) {
            value = findColor(getStyleAttribute("background", true));
            if (value == null) {
                value = "";
            }
        }

        return value;
    }

    /**
     * Sets the "backgroundColor" style attribute.
     * @param backgroundColor the new attribute
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
     * @param backgroundImage the new attribute
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
     * @param backgroundPosition the new attribute
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
     * @param backgroundPositionX the new attribute
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
     * @param backgroundPositionY the new attribute
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
     * @param backgroundRepeat the new attribute
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
                LOG.warn("Invalid behavior: '" + behavior + "'.");
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
     * @param border the new attribute
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
     * @param borderBottom the new attribute
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
     * @param borderBottomColor the new attribute
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
     * @param borderBottomStyle the new attribute
     */
    public void jsxSet_borderBottomStyle(final String borderBottomStyle) {
        setStyleAttribute("borderBottomStyle", borderBottomStyle);
    }

    /**
     * Gets the "borderBottomWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderBottomWidth() {
        return getBorderWidth(Shorthand.BOTTOM);
    }

    /**
     * Sets the "borderBottomWidth" style attribute.
     * @param borderBottomWidth the new attribute
     */
    public void jsxSet_borderBottomWidth(final String borderBottomWidth) {
        setStyleAttributePixelInt("borderBottomWidth", borderBottomWidth);
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
     * @param borderCollapse the new attribute
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
     * @param borderColor the new attribute
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
     * @param borderLeft the new attribute
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
     * @param borderLeftColor the new attribute
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
     * @param borderLeftStyle the new attribute
     */
    public void jsxSet_borderLeftStyle(final String borderLeftStyle) {
        setStyleAttribute("borderLeftStyle", borderLeftStyle);
    }

    /**
     * Gets the "borderLeftWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderLeftWidth() {
        return getBorderWidth(Shorthand.LEFT);
    }

    /**
     * Gets the border width for the specified side
     * @param side the side
     * @param side the side's position
     * @return the width, "" if not defined
     */
    private String getBorderWidth(final Shorthand side) {
        String value = getStyleAttribute("border" + side + "Width", true);
        if (value.length() == 0) {
            value = findBorderWidth(getStyleAttribute("border" + side, true));
            if (value == null) {
                final String borderWidth = getStyleAttribute("borderWidth", true);
                if (!StringUtils.isEmpty(borderWidth)) {
                    final String[] values = borderWidth.split("\\s");
                    if (values.length > side.ordinal()) {
                        value = values[side.ordinal()];
                    }
                }
            }
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
     * @param borderLeftWidth the new attribute
     */
    public void jsxSet_borderLeftWidth(final String borderLeftWidth) {
        setStyleAttributePixelInt("borderLeftWidth", borderLeftWidth);
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
     * @param borderRight the new attribute
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
     * @param borderRightColor the new attribute
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
     * @param borderRightStyle the new attribute
     */
    public void jsxSet_borderRightStyle(final String borderRightStyle) {
        setStyleAttribute("borderRightStyle", borderRightStyle);
    }

    /**
     * Gets the "borderRightWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderRightWidth() {
        return getBorderWidth(Shorthand.RIGHT);
    }

    /**
     * Sets the "borderRightWidth" style attribute.
     * @param borderRightWidth the new attribute
     */
    public void jsxSet_borderRightWidth(final String borderRightWidth) {
        setStyleAttributePixelInt("borderRightWidth", borderRightWidth);
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
     * @param borderSpacing the new attribute
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
     * @param borderStyle the new attribute
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
     * @param borderTop the new attribute
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
     * @param borderTopColor the new attribute
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
     * @param borderTopStyle the new attribute
     */
    public void jsxSet_borderTopStyle(final String borderTopStyle) {
        setStyleAttribute("borderTopStyle", borderTopStyle);
    }

    /**
     * Gets the "borderTopWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderTopWidth() {
        return getBorderWidth(Shorthand.TOP);
    }

    /**
     * Sets the "borderTopWidth" style attribute.
     * @param borderTopWidth the new attribute
     */
    public void jsxSet_borderTopWidth(final String borderTopWidth) {
        setStyleAttributePixelInt("borderTopWidth", borderTopWidth);
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
     * @param borderWidth the new attribute
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
     * @param bottom the new attribute
     */
    public void jsxSet_bottom(final String bottom) {
        setStyleAttributePixelInt("bottom", bottom);
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
     * @param captionSide the new attribute
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
     * @param clear the new attribute
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
     * @param clip the new attribute
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
     * @param color the new attribute
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
     * @param content the new attribute
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
     * @param counterIncrement the new attribute
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
     * @param counterReset the new attribute
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
     * @param value the new attribute
     */
    public void jsxSet_cssFloat(final String value) {
        setStyleAttribute("float", value);
    }

    /**
     * Returns the actual text of the style.
     * @return the actual text of the style
     */
    public String jsxGet_cssText() {
        return jsElement_.getDomNodeOrDie().getAttribute("style");
    }

    /**
     * Sets the actual text of the style.
     * @param value the new text
     */
    public void jsxSet_cssText(final String value) {
        jsElement_.getDomNodeOrDie().setAttribute("style", value);
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
     * @param cue the new attribute
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
     * @param cueAfter the new attribute
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
     * @param cueBefore the new attribute
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
     * @param cursor the new attribute
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
     * @param direction the new attribute
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
     * @param display the new attribute
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
     * @param elevation the new attribute
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
     * @param emptyCells the new attribute
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
     * @param font the new attribute
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
     * @param fontFamily the new attribute
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
     * @param fontSize the new attribute
     */
    public void jsxSet_fontSize(final String fontSize) {
        setStyleAttributePixelInt("fontSize", fontSize);
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
     * @param fontSizeAdjust the new attribute
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
     * @param fontStretch the new attribute
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
     * @param fontStyle the new attribute
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
     * @param fontVariant the new attribute
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
     * @param fontWeight the new attribute
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
     * @param height the new attribute
     */
    public void jsxSet_height(final String height) {
        setStyleAttributePixelInt("height", height);
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
     * @param imeMode the new attribute
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
     * @param layoutFlow the new attribute
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
     * @param layoutGrid the new attribute
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
     * @param layoutGridChar the new attribute
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
     * @param layoutGridLine the new attribute
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
     * @param layoutGridMode the new attribute
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
     * @param layoutGridType the new attribute
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
     * @param left the new attribute
     */
    public void jsxSet_left(final String left) {
        setStyleAttributePixelInt("left", left);
    }

    /**
     * Gets the "length", not yet implemented.
     * @return the length
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
     * @param letterSpacing the new attribute
     */
    public void jsxSet_letterSpacing(final String letterSpacing) {
        setStyleAttributePixelInt("letterSpacing", letterSpacing);
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
     * @param lineBreak the new attribute
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
     * @param lineHeight the new attribute
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
     * @param listStyle the new attribute
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
     * @param listStyleImage the new attribute
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
     * @param listStylePosition the new attribute
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
     * @param listStyleType the new attribute
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
     * @param margin the new attribute
     */
    public void jsxSet_margin(final String margin) {
        setStyleAttribute("margin", margin);
    }

    /**
     * Gets the "marginBottom" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginBottom() {
        return getStyleAttribute("marginBottom", "margin", Shorthand.BOTTOM, true);
    }

    /**
     * Sets the "marginBottom" style attribute.
     * @param marginBottom the new attribute
     */
    public void jsxSet_marginBottom(final String marginBottom) {
        setStyleAttributePixelInt("marginBottom", marginBottom);
    }

    /**
     * Gets the "marginLeft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginLeft() {
        return getStyleAttribute("marginLeft", "margin", Shorthand.LEFT, true);
    }

    /**
     * Sets the "marginLeft" style attribute.
     * @param marginLeft the new attribute
     */
    public void jsxSet_marginLeft(final String marginLeft) {
        setStyleAttributePixelInt("marginLeft", marginLeft);
    }

    /**
     * Gets the "marginRight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginRight() {
        return getStyleAttribute("marginRight", "margin", Shorthand.RIGHT, true);
    }

    /**
     * Sets the "marginRight" style attribute.
     * @param marginRight the new attribute
     */
    public void jsxSet_marginRight(final String marginRight) {
        setStyleAttributePixelInt("marginRight", marginRight);
    }

    /**
     * Gets the "marginTop" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginTop() {
        return getStyleAttribute("marginTop", "margin", Shorthand.TOP, true);
    }

    /**
     * Sets the "marginTop" style attribute.
     * @param marginTop the new attribute
     */
    public void jsxSet_marginTop(final String marginTop) {
        setStyleAttributePixelInt("marginTop", marginTop);
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
     * @param markerOffset the new attribute
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
     * @param marks the new attribute
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
     * @param maxHeight the new attribute
     */
    public void jsxSet_maxHeight(final String maxHeight) {
        setStyleAttributePixelInt("maxHeight", maxHeight);
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
     * @param maxWidth the new attribute
     */
    public void jsxSet_maxWidth(final String maxWidth) {
        setStyleAttributePixelInt("maxWidth", maxWidth);
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
     * @param minHeight the new attribute
     */
    public void jsxSet_minHeight(final String minHeight) {
        setStyleAttributePixelInt("minHeight", minHeight);
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
     * @param minWidth the new attribute
     */
    public void jsxSet_minWidth(final String minWidth) {
        setStyleAttributePixelInt("minWidth", minWidth);
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
     * @param mozAppearance the new attribute
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
     * @param mozBackgroundClip the new attribute
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
     * @param mozBackgroundInlinePolicy the new attribute
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
     * @param mozBackgroundOrigin the new attribute
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
     * @param mozBinding the new attribute
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
     * @param mozBorderBottomColors the new attribute
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
     * @param mozBorderLeftColors the new attribute
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
     * @param mozBorderRadius the new attribute
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
     * @param mozBorderRadiusBottomleft the new attribute
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
     * @param mozBorderRadiusBottomright the new attribute
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
     * @param mozBorderRadiusTopleft the new attribute
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
     * @param mozBorderRadiusTopright the new attribute
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
     * @param mozBorderRightColors the new attribute
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
     * @param mozBorderTopColors the new attribute
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
     * @param mozBoxAlign the new attribute
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
     * @param mozBoxDirection the new attribute
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
     * @param mozBoxFlex the new attribute
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
     * @param mozBoxOrdinalGroup the new attribute
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
     * @param mozBoxOrient the new attribute
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
     * @param mozBoxPack the new attribute
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
     * @param mozBoxSizing the new attribute
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
     * @param mozColumnCount the new attribute
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
     * @param mozColumnGap the new attribute
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
     * @param mozColumnWidth the new attribute
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
     * @param mozFloatEdge the new attribute
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
     * @param mozForceBrokenImageIcon the new attribute
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
     * @param mozImageRegion the new attribute
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
     * @param mozMarginEnd the new attribute
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
     * @param mozMarginStart the new attribute
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
     * @param mozOpacity the new attribute
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
     * @param mozOutline the new attribute
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
     * @param mozOutlineColor the new attribute
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
     * @param mozOutlineOffset the new attribute
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
     * @param mozOutlineRadius the new attribute
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
     * @param mozOutlineRadiusBottomleft the new attribute
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
     * @param mozOutlineRadiusBottomright the new attribute
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
     * @param mozOutlineRadiusTopleft the new attribute
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
     * @param mozOutlineRadiusTopright the new attribute
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
     * @param mozOutlineStyle the new attribute
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
     * @param mozOutlineWidth the new attribute
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
     * @param mozPaddingEnd the new attribute
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
     * @param mozPaddingStart the new attribute
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
     * @param mozUserFocus the new attribute
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
     * @param mozUserInput the new attribute
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
     * @param mozUserModify the new attribute
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
     * @param mozUserSelect the new attribute
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
     * @param msInterpolationMode the new attribute
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
     * @param opacity the new attribute
     */
    public void jsxSet_opacity(final String opacity) {
        if (getBrowserVersion().isIE()) {
            setStyleAttribute("opacity", opacity);
        }
        else if (isFloat(opacity, true) || opacity.length() == 0) {
            setStyleAttribute("opacity", opacity.trim());
        }
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
     * @param orphans the new attribute
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
     * @param outline the new attribute
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
     * @param outlineColor the new attribute
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
     * @param outlineOffset the new attribute
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
     * @param outlineStyle the new attribute
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
     * @param outlineWidth the new attribute
     */
    public void jsxSet_outlineWidth(final String outlineWidth) {
        setStyleAttributePixelInt("outlineWidth", outlineWidth);
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
     * @param overflow the new attribute
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
     * @param overflowX the new attribute
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
     * @param overflowY the new attribute
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
     * @param padding the new attribute
     */
    public void jsxSet_padding(final String padding) {
        setStyleAttribute("padding", padding);
    }

    /**
     * Gets the "paddingBottom" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingBottom() {
        return getStyleAttribute("paddingBottom", "padding", Shorthand.BOTTOM, true);
    }

    /**
     * Sets the "paddingBottom" style attribute.
     * @param paddingBottom the new attribute
     */
    public void jsxSet_paddingBottom(final String paddingBottom) {
        setStyleAttributePixelInt("paddingBottom", paddingBottom);
    }

    /**
     * Gets the "paddingLeft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingLeft() {
        return getStyleAttribute("paddingLeft", "padding", Shorthand.LEFT, true);
    }

    /**
     * Sets the "paddingLeft" style attribute.
     * @param paddingLeft the new attribute
     */
    public void jsxSet_paddingLeft(final String paddingLeft) {
        setStyleAttributePixelInt("paddingLeft", paddingLeft);
    }

    /**
     * Gets the "paddingRight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingRight() {
        return getStyleAttribute("paddingRight", "padding", Shorthand.RIGHT, true);
    }

    /**
     * Sets the "paddingRight" style attribute.
     * @param paddingRight the new attribute
     */
    public void jsxSet_paddingRight(final String paddingRight) {
        setStyleAttributePixelInt("paddingRight", paddingRight);
    }

    /**
     * Gets the "paddingTop" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingTop() {
        return getStyleAttribute("paddingTop", "padding", Shorthand.TOP, true);
    }

    /**
     * Sets the "paddingTop" style attribute.
     * @param paddingTop the new attribute
     */
    public void jsxSet_paddingTop(final String paddingTop) {
        setStyleAttributePixelInt("paddingTop", paddingTop);
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
     * @param page the new attribute
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
     * @param pageBreakAfter the new attribute
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
     * @param pageBreakBefore the new attribute
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
     * @param pageBreakInside the new attribute
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
     * @param pause the new attribute
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
     * @param pauseAfter the new attribute
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
     * @param pauseBefore the new attribute
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
     * @param pitch the new attribute
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
     * @param pitchRange the new attribute
     */
    public void jsxSet_pitchRange(final String pitchRange) {
        setStyleAttribute("pitchRange", pitchRange);
    }

    /**
     * Gets the "pixelBottom" style attribute.
     * @return the style attribute
     */
    public int jsxGet_pixelBottom() {
        return pixelValue(jsxGet_bottom());
    }

    /**
     * Sets the "pixelBottom" style attribute.
     * @param pixelBottom the new attribute
     */
    public void jsxSet_pixelBottom(final int pixelBottom) {
        jsxSet_bottom(pixelBottom + "px");
    }

    /**
     * Gets the "pixelLeft" style attribute.
     * @return the style attribute
     */
    public int jsxGet_pixelLeft() {
        return pixelValue(jsxGet_left());
    }

    /**
     * Sets the "pixelLeft" style attribute.
     * @param pixelLeft the new attribute
     */
    public void jsxSet_pixelLeft(final int pixelLeft) {
        jsxSet_left(pixelLeft + "px");
    }

    /**
     * Gets the "pixelRight" style attribute.
     * @return the style attribute
     */
    public int jsxGet_pixelRight() {
        return pixelValue(jsxGet_right());
    }

    /**
     * Sets the "pixelRight" style attribute.
     * @param pixelRight the new attribute
     */
    public void jsxSet_pixelRight(final int pixelRight) {
        jsxSet_right(pixelRight + "px");
    }

    /**
     * Gets the "pixelTop" style attribute.
     * @return the style attribute
     */
    public int jsxGet_pixelTop() {
        return pixelValue(jsxGet_top());
    }

    /**
     * Sets the "pixelTop" style attribute.
     * @param pixelTop the new attribute
     */
    public void jsxSet_pixelTop(final int pixelTop) {
        jsxSet_top(pixelTop + "px");
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
     * @param posBottom the new attribute
     */
    public void jsxSet_posBottom(final int posBottom) {
        // Empty.
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
     * @param posHeight the new attribute
     */
    public void jsxSet_posHeight(final int posHeight) {
        // Empty.
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
     * @param position the new attribute
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
     * @param posLeft the new attribute
     */
    public void jsxSet_posLeft(final int posLeft) {
        // Empty.
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
     * @param posRight the new attribute
     */
    public void jsxSet_posRight(final int posRight) {
        // Empty.
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
     * @param posTop the new attribute
     */
    public void jsxSet_posTop(final int posTop) {
        // Empty.
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
     * @param posWidth the new attribute
     */
    public void jsxSet_posWidth(final int posWidth) {
        // Empty.
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
     * @param quotes the new attribute
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
     * @param richness the new attribute
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
     * @param right the new attribute
     */
    public void jsxSet_right(final String right) {
        setStyleAttributePixelInt("right", right);
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
     * @param rubyAlign the new attribute
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
     * @param rubyOverhang the new attribute
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
     * @param rubyPosition the new attribute
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
     * @param scrollbar3dLightColor the new attribute
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
     * @param scrollbarArrowColor the new attribute
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
     * @param scrollbarBaseColor the new attribute
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
     * @param scrollbarDarkShadowColor the new attribute
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
     * @param scrollbarFaceColor the new attribute
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
     * @param scrollbarHighlightColor the new attribute
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
     * @param scrollbarShadowColor the new attribute
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
     * @param scrollbarTrackColor the new attribute
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
     * @param size the new attribute
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
     * @param speak the new attribute
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
     * @param speakHeader the new attribute
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
     * @param speakNumeral the new attribute
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
     * @param speakPunctuation the new attribute
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
     * @param speechRate the new attribute
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
     * @param stress the new attribute
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
     * @param value the new attribute
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
     * @param tableLayout the new attribute
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
     * @param textAlign the new attribute
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
     * @param textAlignLast the new attribute
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
     * @param textAutospace the new attribute
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
     * @param textDecoration the new attribute
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
     * @param textDecorationBlink the new attribute
     */
    public void jsxSet_textDecorationBlink(final boolean textDecorationBlink) {
        // Empty.
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
     * @param textDecorationLineThrough the new attribute
     */
    public void jsxSet_textDecorationLineThrough(final boolean textDecorationLineThrough) {
        // Empty.
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
     * @param textDecorationNone the new attribute
     */
    public void jsxSet_textDecorationNone(final boolean textDecorationNone) {
        // Empty.
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
     * @param textDecorationOverline the new attribute
     */
    public void jsxSet_textDecorationOverline(final boolean textDecorationOverline) {
        // Empty.
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
     * @param textDecorationUnderline the new attribute
     */
    public void jsxSet_textDecorationUnderline(final boolean textDecorationUnderline) {
        // Empty.
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
     * @param textIndent the new attribute
     */
    public void jsxSet_textIndent(final String textIndent) {
        setStyleAttributePixelInt("textIndent", textIndent);
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
     * @param textJustify the new attribute
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
     * @param textJustifyTrim the new attribute
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
     * @param textKashida the new attribute
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
     * @param textKashidaSpace the new attribute
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
     * @param textOverflow the new attribute
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
     * @param textShadow the new attribute
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
     * @param textTransform the new attribute
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
     * @param textUnderlinePosition the new attribute
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
     * @param top the new attribute
     */
    public void jsxSet_top(final String top) {
        setStyleAttributePixelInt("top", top);
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
     * @param unicodeBidi the new attribute
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
     * @param verticalAlign the new attribute
     */
    public void jsxSet_verticalAlign(final String verticalAlign) {
        setStyleAttributePixelInt("verticalAlign", verticalAlign);
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
     * @param visibility the new attribute
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
     * @param voiceFamily the new attribute
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
     * @param volume the new attribute
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
     * @param whiteSpace the new attribute
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
     * @param widows the new attribute
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
     * @param width the new attribute
     */
    public void jsxSet_width(final String width) {
        setStyleAttributePixelInt("width", width);
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
     * @param wordBreak the new attribute
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
     * @param wordSpacing the new attribute
     */
    public void jsxSet_wordSpacing(final String wordSpacing) {
        setStyleAttributePixelInt("wordSpacing", wordSpacing);
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
     * @param wordWrap the new attribute
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
     * @param writingMode the new attribute
     */
    public void jsxSet_writingMode(final String writingMode) {
        setStyleAttribute("writingMode", writingMode);
    }

    /**
     * Gets the "zIndex" style attribute.
     * @return the style attribute
     */
    public Object jsxGet_zIndex() {
        final String value = getStyleAttribute("zIndex", true);
        if (getBrowserVersion().isIE()) {
            if (value == null || value.length() == 0) {
                return 0;
            }
            return Integer.parseInt(value);
        }
        return value;
    }

    /**
     * Sets the specified style attribute, which is presumed to be a numeric, taking into consideration
     * its {@link Math#round(float)}ed value.
     * @param name the attribute name (camel-cased)
     * @param value the attribute value
     */
    protected void setRoundedStyleAttribute(final String name, final Object value) {
        if (value == null || value.toString().length() == 0) {
            setStyleAttribute(name, "0");
        }
        else {
            final Double d;
            if (value instanceof Double) {
                d = (Double) value;
            }
            else {
                d = Double.parseDouble(value.toString());
            }
            setStyleAttribute(name, ((Integer) Math.round(d.floatValue())).toString());
        }
    }

    /**
     * Sets the specified style attribute, if it's only an integer.
     * @param name the attribute name (camel-cased)
     * @param value the attribute value
     */
    protected void setIntegerStyleAttribute(final String name, final Object value) {
        if ((value == null) || value.toString().length() == 0) {
            setStyleAttribute(name, "0");
        }
        else {
            final String valueString = value.toString();
            if (value instanceof Number) {
                final Number number = (Number) value;
                if (number.doubleValue() % 1 == 0) {
                    setStyleAttribute(name, ((Integer) number.intValue()).toString());
                }
            }
            else {
                if (valueString.indexOf('.') == -1) {
                    setStyleAttribute(name, valueString);
                }
            }
        }
    }

    /**
     * Sets the "zIndex" style attribute.
     * @param zIndex the new attribute
     */
    public void jsxSet_zIndex(final Object zIndex) {
        if (getBrowserVersion().isIE()) {
            setRoundedStyleAttribute("zIndex", zIndex);
        }
        else {
            setIntegerStyleAttribute("zIndex", zIndex);
        }
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
     * @param zoom the new attribute
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
     * Gets the CSS property value.
     * @param name the name of the property to retrieve
     * @return the value
     */
    public CSSValue jsxFunction_getPropertyCSSValue(final String name) {
        LOG.info("getPropertyCSSValue(" + name + "): getPropertyCSSValue support is experimental");
        // following is a hack, just to have basic support for getPropertyCSSValue
        // TODO: rework the whole CSS processing here! we should *always* parse the style!
        if (styleDeclaration_ == null) {
            final String uri = this.<DomNode>getDomNodeOrDie().getPage().getWebResponse().getRequestSettings()
            .getUrl().toExternalForm();
            final String styleAttribute = jsElement_.getDomNodeOrDie().getAttribute("style");
            final InputSource source = new InputSource(new StringReader(styleAttribute));
            source.setURI(uri);
            final ErrorHandler errorHandler = getWindow().getWebWindow().getWebClient().getCssErrorHandler();
            final CSSOMParser parser = new CSSOMParser(new SACParserCSS21());
            parser.setErrorHandler(errorHandler);
            try {
                styleDeclaration_ = parser.parseStyleDeclaration(source);
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        org.w3c.dom.css.CSSValue cssValue = styleDeclaration_.getPropertyCSSValue(name);
        if (cssValue == null) {
            final CSSValueImpl newValue = new CSSValueImpl();
            newValue.setFloatValue(CSSPrimitiveValue.CSS_PX, 0);
            cssValue = newValue;
        }
        // FF has spaces next to ","
        if (cssValue.getCssText().startsWith("rgb(")) {
            cssValue.setCssText(cssValue.getCssText().replaceAll(",", ", "));
        }

        return new CSSPrimitiveValue(jsElement_, (org.w3c.dom.css.CSSPrimitiveValue) cssValue);
    }

    /**
     * Sets an expression for the specified Style.
     *
     * @param propertyName Specifies the name of the property to which expression is added
     * @param expression specifies any valid script statement without quotations or semicolons;
     *        this string can include references to other properties on the current page.
     *        Array references are not allowed on object properties included in this script.
     * @param language specified the language used
     */
    public void jsxFunction_setExpression(final String propertyName, final String expression, final String language) {
        // Empty.
    }

    /**
     * Removes the expression from the specified property.
     *
     * @param propertyName the name of the property from which to remove an expression
     * @return true if the expression was successfully removed
     */
    public boolean jsxFunction_removeExpression(final String propertyName) {
        return true;
    }

    /**
     * Returns the value of the specified attribute, or an empty string if it does not exist.
     * This method exists only in IE.
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536429.aspx">MSDN Documentation</a>
     * @param name the name of the attribute
     * @param flag 0 for case insensitive, 1 (default) for case sensitive
     * @return the value of the specified attribute
     */
    public Object jsxFunction_getAttribute(final String name, final int flag) {
        if (flag == 1) {
            // Case-sensitive.
            return getStyleAttribute(name, true);
        }

        // Case-insensitive.
        final Map<String, StyleElement> map = getStyleMap(true);
        for (final String key : map.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                return map.get(key).getValue();
            }
        }
        return "";
    }

    /**
     * Sets the value of the specified attribute. This method exists only in IE.
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536739.aspx">MSDN Documentation</a>
     * @param name the name of the attribute
     * @param value the value to assign to the attribute
     * @param flag 0 for case insensitive, 1 (default) for case sensitive
     */
    public void jsxFunction_setAttribute(final String name, final String value, final Object flag) {
        int flagInt;
        if (flag == Undefined.instance) {
            flagInt = 1;
        }
        else {
            flagInt = (int) Context.toNumber(flag);
        }
        if (flagInt == 0) {
            // Case-insensitive.
            final Map<String, StyleElement> map = getStyleMap(true);
            for (final String key : map.keySet()) {
                if (key.equalsIgnoreCase(name)) {
                    setStyleAttribute(key, value);
                }
            }
        }
        else {
            // Case-sensitive.
            if (getStyleAttribute(name, true).length() > 0) {
                setStyleAttribute(name, value);
            }
        }
    }

    /**
     * Removes the specified attribute. This method exists only in IE.
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536696.aspx">MSDN Documentation</a>
     * @param name the name of the attribute
     * @param flag 0 for case insensitive, 1 (default) for case sensitive
     * @return <tt>true</tt> if the attribute was successfully removed, <tt>false</tt> otherwise
     */
    public boolean jsxFunction_removeAttribute(final String name, final Object flag) {
        int flagInt;
        if (flag == Undefined.instance) {
            flagInt = 1;
        }
        else {
            flagInt = (int) Context.toNumber(flag);
        }
        if (flagInt == 0) {
            // Case-insensitive.
            String lastName = null;
            final Map<String, StyleElement> map = getStyleMap(true);
            for (final String key : map.keySet()) {
                if (key.equalsIgnoreCase(name)) {
                    lastName = key;
                }
            }
            if (lastName != null) {
                removeStyleAttribute(lastName);
                return true;
            }
            return false;
        }

        // Case-sensitive.
        final String s = getStyleAttribute(name, true);
        if (s.length() > 0) {
            removeStyleAttribute(name);
            return true;
        }
        return false;
    }

    /**
     * Searches for any color notation in the specified text.
     * @param text the string to search in
     * @return the string of the color if found, null otherwise
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
     * @param text the string to search in
     * @return the border style if found, null otherwise
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
     * @param text the string to search in
     * @return the border width if found, null otherwise
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
     * @param token the token to check
     * @return whether the token is a color in hexadecimal notation or not
     */
    private static boolean isColorHexadecimal(final String token) {
        return token.toLowerCase().matches("#([0-9a-f]{3}|[0-9a-f]{6})");
    }

    /**
     * Returns if the specified token is a reserved color keyword.
     * @param token the token to check
     * @return whether the token is a reserved color keyword or not
     */
    private static boolean isColorKeyword(final String token) {
        return CSSColors_.containsKey(token.toLowerCase());
    }

    /**
     * Gets the RGB equivalent of a CSS color if the provided color is recognized.
     * @param color the color
     * @return the provided color if this is not a recognized color keyword, the RGB value
     * in the form "rgb(x, y, z)" otherwise
     */
    public static String toRGBColor(final String color) {
        final String rgbValue = CSSColors_.get(color.toLowerCase());
        if (rgbValue != null) {
            return rgbValue;
        }
        return color;
    }

    /**
     * Returns if the specified token is a border style.
     * @param token the token to check
     * @return whether the token is a border style or not
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
     * @param token the token to check
     * @return whether the token is a border width or not
     */
    private static boolean isBorderWidth(final String token) {
        return token.equalsIgnoreCase("thin") || token.equalsIgnoreCase("medium")
            || token.equalsIgnoreCase("thick ") || isLength(token);
    }

    /**
     * Returns if the specified token is a length.
     * @param token the token to check
     * @return whether the token is a length or not
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
            catch (final NumberFormatException e) {
                // Ignore.
            }
        }
        return false;
    }

    /**
     * Converts the specified length CSS attribute value into an integer number of pixels. If the
     * specified CSS attribute value is a percentage, this method uses the specified value object
     * to recursively retrieve the base (parent) CSS attribute value.
     * @param element the element for which the CSS attribute value is to be retrieved
     * @param value the CSS attribute value which is to be retrieved
     * @return the integer number of pixels corresponding to the specified length CSS attribute value
     * @see #pixelValue(String)
     */
    protected static int pixelValue(final HTMLElement element, final CssValue value) {
        final String s = value.get(element);
        final int i = NumberUtils.toInt(s.replaceAll("(\\d+).*", "$1"), 0);
        if (s.endsWith("%")) {
            final HTMLElement parent = element.getParentHTMLElement();
            return (int) ((i / 100D) * pixelValue(parent, value));
        }
        else {
            return pixelValue(s);
        }
    }

    /**
     * Converts the specified length string value into an integer number of pixels. This method does
     * <b>NOT</b> handle percentages correctly; use {@link #pixelValue(HTMLElement, CssValue)} if you
     * need percentage support).
     * @param value the length string value to convert to an integer number of pixels
     * @return the integer number of pixels corresponding to the specified length string value
     * @see <a href="http://htmlhelp.com/reference/css/units.html">CSS Units</a>
     * @see #pixelValue(HTMLElement, CssValue)
     */
    protected static int pixelValue(final String value) {
        final int i = NumberUtils.toInt(value.replaceAll("(\\d+).*", "$1"), 0);
        if (value.endsWith("px")) {
            return i;
        }
        else if (value.endsWith("em")) {
            return i * 16;
        }
        else if (value.endsWith("ex")) {
            return i * 10;
        }
        else if (value.endsWith("in")) {
            return i * 150;
        }
        else if (value.endsWith("cm")) {
            return i * 50;
        }
        else if (value.endsWith("mm")) {
            return i * 5;
        }
        else if (value.endsWith("pt")) {
            return i * 2;
        }
        else if (value.endsWith("pc")) {
            return i * 24;
        }
        else {
            return i;
        }
    }

    /**
     * Encapsulates the retrieval of a style attribute, given a DOM element from which to retrieve it.
     */
    protected abstract static class CssValue {
        /**
         * Returns the CSS attribute value for the specified element.
         * @param element the element for which the CSS attribute value is to be retrieved
         * @return the CSS attribute value for the specified element
         */
        public final String get(final HTMLElement element) {
            final ComputedCSSStyleDeclaration style = element.jsxGet_currentStyle();
            final String value = get(style);
            return value;
        }
        /**
         * Returns the CSS attribute value from the specified computed style.
         * @param style the computed style from which to retrieve the CSS attribute value
         * @return the CSS attribute value from the specified computed style
         */
        public abstract String get(final ComputedCSSStyleDeclaration style);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (jsElement_ == null) {
            return "CSSStyleDeclaration for 'null'"; // for instance on prototype
        }
        final String style = jsElement_.getDomNodeOrDie().getAttribute("style");
        return "CSSStyleDeclaration for '" + style + "'";
    }

    /**
     * Contains information about a single style element, including its name, its value, and an index which
     * can be compared against other indices in order to determine precedence.
     */
    protected static class StyleElement implements Comparable<StyleElement> {
        private final String name_;
        private final String value_;
        private final String priority_;
        private final long index_;
        private final SelectorSpecificity specificity_;

        /**
         * Creates a new instance.
         * @param name the style element's name
         * @param value the style element's value
         * @param priority the style element's priority like "important"
         * @param specificity the specificity of the rule providing this style information
         * @param index the style element's index
         */
        protected StyleElement(final String name, final String value, final String priority,
                final SelectorSpecificity specificity, final long index) {
            this.name_ = name;
            this.value_ = value;
            this.priority_ = priority;
            this.index_ = index;
            this.specificity_ = specificity;
        }
        /**
         * Creates a new instance.
         * @param name the style element's name
         * @param value the style element's value
         * @param index the style element's index
         */
        protected StyleElement(final String name, final String value, final long index) {
            this(name, value, "", SelectorSpecificity.FROM_STYLE_ATTRIBUTE, index);
        }

        /**
         * Creates a new default instance.
         * @param name the style element's name
         * @param value the style element's value
         */
        protected StyleElement(final String name, final String value) {
            this(name, value, Long.MIN_VALUE);
        }

        /**
         * Returns the style element's name.
         * @return the style element's name
         */
        public String getName() {
            return name_;
        }
        /**
         * Returns the style element's value.
         * @return the style element's value
         */
        public String getValue() {
            return value_;
        }
        /**
         * Returns the style element's priority.
         * @return the style element's priority
         */
        public String getPriority() {
            return priority_;
        }
        /**
         * Returns the specificity of the rule specifying this element.
         * @return the specificity
         */
        public SelectorSpecificity getSpecificity() {
            return specificity_;
        }
        /**
         * Returns the style element's index.
         * @return the style element's index
         */
        public long getIndex() {
            return index_;
        }
        /**
         * Returns <tt>true</tt> if this style element contains a default value. This method isn't
         * currently used anywhere because default style elements are applied before non-default
         * style elements, so the natural ordering results in correct precedence rules being applied
         * (i.e. default style elements don't override non-default style elements) without the need
         * for special checks.
         * @return <tt>true</tt> if this style element contains a default value
         */
        public boolean isDefault() {
            return index_ == Long.MIN_VALUE;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "[" + index_ + "]" + name_  + "=" + value_;
        }
        /**
         * {@inheritDoc}
         */
        public int compareTo(final StyleElement e) {
            if (e != null) {
                return new Long(index_).compareTo(e.index_);
            }
            return 1;
        }
    }

    /**
     * Sets the style attribute which should be treated as an integer in pixels.
     * @param name the attribute name
     * @param value the attribute value
     */
    protected void setStyleAttributePixelInt(final String name, String value) {
        if (value.endsWith("px")) {
            value = value.substring(0, value.length() - 2);
        }
        try {
            final float floatValue = Float.parseFloat(value);
            if (getBrowserVersion().isIE()) {
                value = Integer.toString((int) floatValue) + "px";
            }
            else {
                if (floatValue % 1 == 0) {
                    value = Integer.toString((int) floatValue) + "px";
                }
                else {
                    value = Float.toString(floatValue) + "px";
                }
            }
        }
        catch (final Exception e) {
            //ignore
        }
        setStyleAttribute(name, value);
    }

}
