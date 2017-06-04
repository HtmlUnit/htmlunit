/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_LENGTH_INITIAL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_SET_NULL_THROWS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_SUPPORTS_BEHAVIOR_PROPERTY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_ZINDEX_TYPE_INTEGER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_SET_PROPERTY_IMPORTANT_IGNORES_CASE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BEHAVIOR;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_BOTTOM;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_BOTTOM_COLOR;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_BOTTOM_STYLE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_BOTTOM_WIDTH;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_LEFT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_LEFT_COLOR;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_LEFT_STYLE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_LEFT_WIDTH;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_RIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_RIGHT_COLOR;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_RIGHT_STYLE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_RIGHT_WIDTH;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_TOP;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_TOP_COLOR;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_TOP_STYLE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_TOP_WIDTH;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BORDER_WIDTH;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BOTTOM;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.COLOR;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.DISPLAY;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.FLOAT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.FONT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.FONT_FAMILY;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.FONT_SIZE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.HEIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.LEFT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.LINE_HEIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MARGIN;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MARGIN_BOTTOM;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MARGIN_LEFT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MARGIN_RIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MARGIN_TOP;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PADDING;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PADDING_BOTTOM;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PADDING_LEFT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PADDING_RIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PADDING_TOP;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.RIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.TOP;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.WIDTH;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.awt.Color;
import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.css.SelectorSpecificity;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.javascript.NashornJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.htmlunit.javascript.host.Element2;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * A JavaScript object for {@code CSSStyleDeclaration}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Rodney Gitzel
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 */
@ScriptClass
public class CSSStyleDeclaration2 extends SimpleScriptObject {

    private static final Log LOG = LogFactory.getLog(CSSStyleDeclaration2.class);

    /** CSS important property constant. */
    protected static final String PRIORITY_IMPORTANT = "important";

    private static final Pattern TO_INT_PATTERN = Pattern.compile("(\\d+).*");
    private static final Pattern URL_PATTERN =
        Pattern.compile("url\\(\\s*[\"']?(.*?)[\"']?\\s*\\)");
    private static final Pattern POSITION_PATTERN =
        Pattern.compile("(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex))\\s*"
                    + "(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|top|bottom|center)");
    private static final Pattern POSITION_PATTERN2 =
        Pattern.compile("(left|right|center)\\s*(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|top|bottom|center)");
    private static final Pattern POSITION_PATTERN3 =
        Pattern.compile("(top|bottom|center)\\s*(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|left|right|center)");

    private static final Map<String, String> CSSColors_ = new HashMap<>();

    private static final Map<String, String> CamelizeCache_
        = Collections.synchronizedMap(new HashMap<String, String>());

    /** Used to parse URLs. */
    private static final MessageFormat URL_FORMAT = new MessageFormat("url({0})");

    /** The element to which this style belongs. */
    private Element2 jsElement_;

    /** The wrapped CSSStyleDeclaration (if created from CSSStyleRule). */
    private org.w3c.dom.css.CSSStyleDeclaration styleDeclaration_;

    /** Cache for the styles. */
    private String styleString_ = new String();
    private Map<String, StyleElement> styleMap_;

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

    CSSStyleDeclaration2() {
        ScriptUtils.initialize(this);
    }

    /**
     * Creates an instance and sets its parent scope to the one of the provided element.
     * @param element the element to which this style is bound
     */
    public CSSStyleDeclaration2(final Element2 element) {
        ScriptUtils.initialize(this);
        initialize(element);
    }

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static CSSStyleDeclaration2 constructor(final boolean newObj, final Object self) {
        final CSSStyleDeclaration2 host = new CSSStyleDeclaration2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Initializes the object.
     * @param htmlElement the element that this style describes
     */
    void initialize(final Element2 element) {
        // Initialize.
        jsElement_ = element;
        setDomNode(element.getDomNodeOrNull(), false);

        // If an IE behavior was specified in the style, apply the behavior.
        if (getBrowserVersion().hasFeature(CSS_SUPPORTS_BEHAVIOR_PROPERTY)
            && element instanceof HTMLElement2) {
            final HTMLElement2 htmlElement = (HTMLElement2) element;
            final String behavior = getStyleAttribute(BEHAVIOR);
            if (StringUtils.isNotBlank(behavior)) {
                try {
                    final Object[] url = URL_FORMAT.parse(behavior);
                    if (url.length > 0) {
                        htmlElement.addBehavior((String) url[0]);
                    }
                }
                catch (final ParseException e) {
                    LOG.warn("Invalid behavior: '" + behavior + "'.");
                }
            }
        }
    }

    /**
     * Returns the element to which this style belongs.
     * @return the element to which this style belongs
     */
    protected Element2 getElement() {
        return jsElement_;
    }

    /**
     * Get the value for the style attribute.
     * @param definition the definition
     * @return the value
     */
    public final String getStyleAttribute(final Definition definition) {
        return getStyleAttribute(definition, true);
    }

    /**
     * Get the value for the style attribute.
     * @param definition the definition
     * @param getDefaultValueIfEmpty whether to get the default value if empty or not
     * @return the value
     */
    public String getStyleAttribute(final Definition definition, final boolean getDefaultValueIfEmpty) {
        return getStyleAttributeImpl(definition.getAttributeName());
    }

    private String getStyleAttributeImpl(final String string) {
        if (styleDeclaration_ != null) {
            return styleDeclaration_.getPropertyValue(string);
        }
        final StyleElement element = getStyleElement(string);
        if (element != null && element.getValue() != null) {
            final String value = element.getValue();
            if (!value.contains("url")
                    && getBrowserVersion().hasFeature(JS_STYLE_SET_PROPERTY_IMPORTANT_IGNORES_CASE)) {
                return value.toLowerCase(Locale.ROOT);
            }
            return value;
        }
        return "";
    }

    /**
     * Determines the StyleElement for the given name.
     *
     * @param name the name of the requested StyleElement
     * @return the StyleElement or null if not found
     */
    protected StyleElement getStyleElement(final String name) {
        final Map<String, StyleElement> map = getStyleMap();
        if (map != null) {
            return map.get(name);
        }
        return null;
    }

    /**
     * Determines the StyleElement for the given name.
     * This ignores the case of the name.
     *
     * @param name the name of the requested StyleElement
     * @return the StyleElement or null if not found
     */
    private StyleElement getStyleElementCaseInSensitive(final String name) {
        final Map<String, StyleElement> map = getStyleMap();
        for (final Map.Entry<String, StyleElement> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Returns a sorted map containing style elements, keyed on style element name. We use a
     * {@link LinkedHashMap} map so that results are deterministic and are thus testable.
     *
     * @return a sorted map containing style elements, keyed on style element name
     */
    private Map<String, StyleElement> getStyleMap() {
        if (jsElement_ == null) {
            return Collections.emptyMap();
        }
        final String styleAttribute = jsElement_.getDomNodeOrDie().getAttribute("style");
        if (styleString_ == styleAttribute) {
            return styleMap_;
        }

        final Map<String, StyleElement> styleMap = new LinkedHashMap<>();
        if (DomElement.ATTRIBUTE_NOT_DEFINED == styleAttribute || DomElement.ATTRIBUTE_VALUE_EMPTY == styleAttribute) {
            styleMap_ = styleMap;
            styleString_ = styleAttribute;
            return styleMap_;
        }

        for (final String token : StringUtils.split(styleAttribute, ';')) {
            final int index = token.indexOf(":");
            if (index != -1) {
                final String key = token.substring(0, index).trim().toLowerCase(Locale.ROOT);
                String value = token.substring(index + 1).trim();
                String priority = "";
                if (StringUtils.endsWithIgnoreCase(value, "!important")) {
                    priority = PRIORITY_IMPORTANT;
                    value = value.substring(0, value.length() - 10);
                    value = value.trim();
                }
                final StyleElement element = new StyleElement(key, value, priority,
                        SelectorSpecificity.FROM_STYLE_ATTRIBUTE, getCurrentElementIndex());
                styleMap.put(key, element);
            }
        }

        styleMap_ = styleMap;
        styleString_ = styleAttribute;
        return styleMap_;
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
     * Returns the actual text of the style.
     * @return the actual text of the style
     */
    @Getter
    public String getCssText() {
        return jsElement_.getDomNodeOrDie().getAttribute("style");
    }

    /**
     * Sets the actual text of the style.
     * @param value the new text
     */
    @Setter
    public void setCssText(final String value) {
        jsElement_.getDomNodeOrDie().setAttribute("style", value);
    }

    /**
     * Gets the {@code color} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getColor() {
        return getStyleAttribute(COLOR);
    }

    /**
     * Sets the {@code color} style attribute.
     * @param color the new attribute
     */
    @Setter
    public void setColor(final String color) {
        setStyleAttribute(COLOR.getAttributeName(), color);
    }

    /**
     * Gets the {@code display} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getDisplay() {
        return getStyleAttribute(DISPLAY);
    }

    /**
     * Sets the {@code display} style attribute.
     * @param display the new attribute
     */
    @Setter
    public void setDisplay(final String display) {
        setStyleAttribute(DISPLAY.getAttributeName(), display);
    }

    /**
     * Sets the specified style attribute.
     * @param name the attribute name (camel-cased)
     * @param newValue the attribute value
     */
    protected void setStyleAttribute(final String name, final String newValue) {
        setStyleAttribute(name, newValue, "");
    }

    /**
     * Sets the specified style attribute.
     * @param name the attribute name (camel-cased)
     * @param newValue the attribute value
     * @param important important value
     */
    protected void setStyleAttribute(final String name, String newValue, final String important) {
        if (null == newValue || "null".equals(newValue)) {
            if (getBrowserVersion().hasFeature(CSS_SET_NULL_THROWS)) {
                //Context.throwAsScriptRuntimeEx(new Exception("Invalid argument."));
            }
            newValue = "";
        }
        if (styleDeclaration_ != null) {
            styleDeclaration_.setProperty(name, newValue, important);
            return;
        }

        replaceStyleAttribute(name, newValue, important);
    }

    /**
     * Replaces the value of the named style attribute. If there is no style attribute with the
     * specified name, a new one is added. If the specified value is an empty (or all whitespace)
     * string, this method actually removes the named style attribute.
     * @param name the attribute name (delimiter-separated, not camel-cased)
     * @param value the attribute value
     * @param priority  the new priority of the property; <code>"important"</code>or the empty string if none.
     */
    private void replaceStyleAttribute(final String name, final String value, final String priority) {
        if (StringUtils.isBlank(value)) {
            removeStyleAttribute(name);
        }
        else {
            final Map<String, StyleElement> styleMap = getStyleMap();
            final StyleElement old = styleMap.get(name);
            final long index;
            if (old != null) {
                index = old.getIndex();
            }
            else {
                index = getCurrentElementIndex();
            }
            final StyleElement element = new StyleElement(name, value, priority,
                    SelectorSpecificity.FROM_STYLE_ATTRIBUTE, index);
            styleMap.put(name, element);
            writeToElement(styleMap);
        }
    }

    /**
     * Removes the specified style attribute, returning the value of the removed attribute.
     * @param name the attribute name (delimiter-separated, not camel-cased)
     */
    private String removeStyleAttribute(final String name) {
        if (null != styleDeclaration_) {
            return styleDeclaration_.removeProperty(name);
        }

        final Map<String, StyleElement> styleMap = getStyleMap();
        final StyleElement value = styleMap.get(name);
        if (value == null) {
            return "";
        }
        styleMap.remove(name);
        writeToElement(styleMap);
        return value.getValue();
    }

    private void writeToElement(final Map<String, StyleElement> styleMap) {
        final StringBuilder builder = new StringBuilder();
        final SortedSet<StyleElement> sortedValues = new TreeSet<>(styleMap.values());
        for (final StyleElement e : sortedValues) {
            if (builder.length() != 0) {
                builder.append(" ");
            }
            builder.append(e.getName());
            builder.append(": ");
            builder.append(e.getValue());

            final String prio = e.getPriority();
            if (StringUtils.isNotBlank(prio)) {
                builder.append(" !");
                builder.append(prio);
            }
            builder.append(";");
        }
        jsElement_.getDomNodeOrDie().setAttribute("style", builder.toString());
    }

    /**
     * Gets the RGB equivalent of a CSS color if the provided color is recognized.
     * @param color the color
     * @return the provided color if this is not a recognized color keyword, the RGB value
     * in the form "rgb(x, y, z)" otherwise
     */
    public static String toRGBColor(final String color) {
        final String rgbValue = CSSColors_.get(color.toLowerCase(Locale.ROOT));
        if (rgbValue != null) {
            return rgbValue;
        }
        return color;
    }

    /**
     * Converts the specified length string value into an integer number of pixels. This method does
     * <b>NOT</b> handle percentages correctly; use {@link #pixelValue(Element2, CssValue)} if you
     * need percentage support).
     * @param value the length string value to convert to an integer number of pixels
     * @return the integer number of pixels corresponding to the specified length string value
     * @see <a href="http://htmlhelp.com/reference/css/units.html">CSS Units</a>
     * @see #pixelValue(Element2, CssValue)
     */
    protected static int pixelValue(final String value) {
        int i = NumberUtils.toInt(TO_INT_PATTERN.matcher(value).replaceAll("$1"), 0);
        if (value.length() < 2) {
            return i;
        }

        if (value.endsWith("px")) {
            // nothing to do
        }
        else if (value.endsWith("em")) {
            i = i * 16;
        }
        else if (value.endsWith("%")) {
            i = i * 16 / 100;
        }
        else if (value.endsWith("ex")) {
            i = i * 10;
        }
        else if (value.endsWith("in")) {
            i = i * 150;
        }
        else if (value.endsWith("cm")) {
            i = i * 50;
        }
        else if (value.endsWith("mm")) {
            i = i * 5;
        }
        else if (value.endsWith("pt")) {
            i = i * 2;
        }
        else if (value.endsWith("pc")) {
            i = i * 24;
        }
        return i;
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
    protected static int pixelValue(final Element2 element, final CssValue value) {
        return pixelValue(element, value, false);
    }

    private static int pixelValue(final Element2 element, final CssValue value, final boolean percentMode) {
        final String s = value.get(element);
        if (s.endsWith("%") || (s.isEmpty() && element instanceof HTMLHtmlElement2)) {
            final int i = NumberUtils.toInt(TO_INT_PATTERN.matcher(s).replaceAll("$1"), 100);
            final Element2 parent = element.getParentElement();
            final int absoluteValue = (parent == null)
                            ? value.getWindowDefaultValue() : pixelValue(parent, value, true);
            return (int) ((i / 100D) * absoluteValue);
        }
        if ("auto".equals(s)) {
            return value.getDefaultValue();
        }
        if (s.isEmpty()) {
            if (element instanceof HTMLCanvasElement2) {
                return value.getWindowDefaultValue();
            }

            // if the call was originated from a percent value we have to go up until
            // we can provide some kind of base value for percent calculation
            if (percentMode) {
                final Element2 parent = element.getParentElement();
                if (parent == null || parent instanceof HTMLHtmlElement2) {
                    return value.getWindowDefaultValue();
                }
                return pixelValue(parent, value, true);
            }

            return 0;
        }
        return pixelValue(s);
    }

    /**
     * Gets the value of the specified property of the style.
     * @param name the style property name
     * @return empty string if nothing found
     */
    @Function
    public String getPropertyValue(final String name) {
//        if (name != null && name.contains("-")) {
//            final Object value = getProperty(this, camelize(name));
//            if (value instanceof String) {
//                return (String) value;
//            }
//        }
        return getStyleAttributeImpl(name);
    }

    /**
     * Transforms the specified string from delimiter-separated (e.g. <tt>font-size</tt>)
     * to camel-cased (e.g. <tt>fontSize</tt>).
     * @param string the string to camelize
     * @return the transformed string
     * @see com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap#decamelize(String)
     */
    protected static final String camelize(final String string) {
        if (string == null) {
            return null;
        }

        String result = CamelizeCache_.get(string);
        if (null != result) {
            return result;
        }

        // not found in CamelizeCache_; convert and store in cache
        final int pos = string.indexOf('-');
        if (pos == -1 || pos == string.length() - 1) {
            // cache also this strings for performance
            CamelizeCache_.put(string, string);
            return string;
        }

        final StringBuilder builder = new StringBuilder(string);
        builder.deleteCharAt(pos);
        builder.setCharAt(pos, Character.toUpperCase(builder.charAt(pos)));

        int i = pos + 1;
        while (i < builder.length() - 1) {
            if (builder.charAt(i) == '-') {
                builder.deleteCharAt(i);
                builder.setCharAt(i, Character.toUpperCase(builder.charAt(i)));
            }
            i++;
        }
        result = builder.toString();
        CamelizeCache_.put(string, result);

        return result;
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
     * @return the value of one of the two named style attributes
     */
    private String getStyleAttribute(final Definition name1, final Definition name2) {
        final String value;
        if (styleDeclaration_ != null) {
            final String value1 = styleDeclaration_.getPropertyValue(name1.getAttributeName());
            final String value2 = styleDeclaration_.getPropertyValue(name2.getAttributeName());

            if ("".equals(value1) && "".equals(value2)) {
                return "";
            }
            if (!"".equals(value1) && "".equals(value2)) {
                return value1;
            }
            value = value2;
        }
        else {
            final StyleElement element1 = getStyleElement(name1.getAttributeName());
            final StyleElement element2 = getStyleElement(name2.getAttributeName());

            if (element2 == null) {
                if (element1 == null) {
                    return "";
                }
                return element1.getValue();
            }
            if (element1 == null) {
                value = element2.getValue();
            }
            else if (element1.getIndex() > element2.getIndex()) {
                return element1.getValue();
            }
            else {
                value = element2.getValue();
            }
        }

        final String[] values = StringUtils.split(value);
        if (name1.name().contains("TOP")) {
            return values[0];
        }
        else if (name1.name().contains("RIGHT")) {
            if (values.length > 1) {
                return values[1];
            }
            return values[0];
        }
        else if (name1.name().contains("BOTTOM")) {
            if (values.length > 2) {
                return values[2];
            }
            return values[0];
        }
        else if (name1.name().contains("LEFT")) {
            if (values.length > 3) {
                return values[3];
            }
            else if (values.length > 1) {
                return values[1];
            }
            else {
                return values[0];
            }
        }
        else {
            throw new IllegalStateException("Unsupported definitino: " + name1);
        }
    }

    /**
     * Sets the style attribute which should be treated as an integer in pixels.
     * @param name the attribute name
     * @param value the attribute value
     * @param important important value
     * @param auto true if auto is supported
     * @param perecent true if percent is supported
     * @param thinMedThick thin, medium, thick are supported
     * @param unitRequired unit is required
     */
    private void setStyleLengthAttribute(final String name, String value, final String important,
                final boolean auto, final boolean percent, final boolean thinMedThick, final boolean unitRequired) {
        if (StringUtils.isEmpty(value)) {
            setStyleAttribute(name, value);
            return;
        }

        if ((auto && "auto".equals(value))
                || ("initial".equals(value) && getBrowserVersion().hasFeature(CSS_LENGTH_INITIAL))
                || "inherit".equals(value)) {
            setStyleAttribute(name, value);
            return;
        }

        if ((thinMedThick && "thin".equals(value))
                || "medium".equals(value)
                || "thick".equals(value)) {
            setStyleAttribute(name, value);
            return;
        }

        String unit = "px";
        if (percent && value.endsWith("%")) {
            unit = value.substring(value.length() - 1);
            value = value.substring(0, value.length() - 1);
        }
        else if (value.endsWith("px")
            || value.endsWith("em")
            || value.endsWith("ex")
            || value.endsWith("px")
            || value.endsWith("cm")
            || value.endsWith("mm")
            || value.endsWith("in")
            || value.endsWith("pc")
            || value.endsWith("pc")
            || value.endsWith("ch")
            || value.endsWith("vh")
            || value.endsWith("vw")) {
            unit = value.substring(value.length() - 2);
            value = value.substring(0, value.length() - 2);
        }
        else if (value.endsWith("rem")
            || value.endsWith("vmin")
            || value.endsWith("vmax")) {
            unit = value.substring(value.length() - 3);
            value = value.substring(0, value.length() - 3);
        }
        else if (unitRequired) {
            return;
        }

        try {
            final float floatValue = Float.parseFloat(value);
            if (floatValue % 1 == 0) {
                value = Integer.toString((int) floatValue) + unit;
            }
            else {
                value = Float.toString(floatValue) + unit;
            }

            setStyleAttribute(name, value, important);
        }
        catch (final Exception e) {
            //ignore
        }
    }

    /**
     * Gets the {@code width} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getWidth() {
        return getStyleAttribute(WIDTH);
    }

    /**
     * Sets the {@code width} style attribute.
     * @param width the new attribute
     */
    @Setter
    public void setWidth(final String width) {
        setStyleLengthAttribute(WIDTH.getAttributeName(), width, "", true, true, false, false);
    }

    /**
     * Gets the {@code cssFloat} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getCssFloat() {
        return getStyleAttribute(FLOAT);
    }

    /**
     * Sets the {@code cssFloat} style attribute.
     * @param value the new attribute
     */
    @Setter
    public void setCssFloat(final String value) {
        setStyleAttribute(FLOAT.getAttributeName(), value);
    }

    /**
     * Gets the {@code height} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getHeight() {
        return getStyleAttribute(HEIGHT);
    }

    /**
     * Sets the {@code height} style attribute.
     * @param height the new attribute
     */
    @Setter
    public void setHeight(final String height) {
        setStyleLengthAttribute(HEIGHT.getAttributeName(), height, "", true, true, false, false);
    }

    /**
     * Gets the {@code fontSize} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getFontSize() {
        return getStyleAttribute(FONT_SIZE);
    }

    /**
     * Sets the {@code fontSize} style attribute.
     * @param fontSize the new attribute
     */
    @Setter
    public void setFontSize(final String fontSize) {
        setStyleLengthAttribute(FONT_SIZE.getAttributeName(), fontSize, "", false, true, false, false);
        updateFont(getFont(), false);
    }

    /**
     * Gets the {@code font} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getFont() {
        return getStyleAttribute(FONT);
    }

    /**
     * Sets the {@code font} style attribute.
     * @param font the new attribute
     */
    @Setter
    public void setFont(final String font) {
        final String[] details = ComputedFont.getDetails(font, !getBrowserVersion().hasFeature(CSS_SET_NULL_THROWS));
        if (details != null) {
            setStyleAttribute(FONT_FAMILY.getAttributeName(), details[ComputedFont.FONT_FAMILY_INDEX]);
            final String fontSize = details[ComputedFont.FONT_SIZE_INDEX];
            if (details[ComputedFont.LINE_HEIGHT_INDEX] != null) {
                setStyleAttribute(LINE_HEIGHT.getAttributeName(), details[ComputedFont.LINE_HEIGHT_INDEX]);
            }
            setStyleAttribute(FONT_SIZE.getAttributeName(), fontSize);
            updateFont(font, true);
        }
    }

    private void updateFont(final String font, final boolean force) {
        final BrowserVersion browserVersion = getBrowserVersion();
        final String[] details = ComputedFont.getDetails(font, !browserVersion.hasFeature(CSS_SET_NULL_THROWS));
        if (details != null || force) {
            final StringBuilder newFont = new StringBuilder();
            newFont.append(getFontSize());
            String lineHeight = getLineHeight();
            final String defaultLineHeight = LINE_HEIGHT.getDefaultComputedValue(browserVersion);
            if (lineHeight.isEmpty()) {
                lineHeight = defaultLineHeight;
            }

            if (browserVersion.hasFeature(CSS_ZINDEX_TYPE_INTEGER) || !lineHeight.equals(defaultLineHeight)) {
                newFont.append('/');
                if (!lineHeight.equals(defaultLineHeight)) {
                    newFont.append(lineHeight);
                }
                else {
                    newFont.append(LINE_HEIGHT.getDefaultComputedValue(browserVersion));
                }
            }

            newFont.append(' ').append(getFontFamily());
            setStyleAttribute(FONT.getAttributeName(), newFont.toString());
        }
    }

    /**
     * Gets the {@code fontFamily} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getFontFamily() {
        return getStyleAttribute(FONT_FAMILY);
    }

    /**
     * Sets the {@code fontFamily} style attribute.
     * @param fontFamily the new attribute
     */
    @Setter
    public void setFontFamily(final String fontFamily) {
        setStyleAttribute(FONT_FAMILY.getAttributeName(), fontFamily);
        updateFont(getFont(), false);
    }

    /**
     * Gets the {@code lineHeight} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getLineHeight() {
        return getStyleAttribute(LINE_HEIGHT);
    }

    /**
     * Sets the {@code lineHeight} style attribute.
     * @param lineHeight the new attribute
     */
    @Setter
    public void setLineHeight(final String lineHeight) {
        setStyleAttribute(LINE_HEIGHT.getAttributeName(), lineHeight);
        updateFont(getFont(), false);
    }

    /**
     * Gets the {@code borderBottomColor} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderBottomColor() {
        String value = getStyleAttribute(BORDER_BOTTOM_COLOR, false);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(BORDER_BOTTOM, false));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the {@code borderBottomColor} style attribute.
     * @param borderBottomColor the new attribute
     */
    @Setter
    public void setBorderBottomColor(final String borderBottomColor) {
        setStyleAttribute(BORDER_BOTTOM_COLOR.getAttributeName(), borderBottomColor);
    }

    /**
     * Gets the {@code borderBottomStyle} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderBottomStyle() {
        String value = getStyleAttribute(BORDER_BOTTOM_STYLE, false);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(BORDER_BOTTOM, false));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the {@code borderBottomStyle} style attribute.
     * @param borderBottomStyle the new attribute
     */
    @Setter
    public void setBorderBottomStyle(final String borderBottomStyle) {
        setStyleAttribute(BORDER_BOTTOM_STYLE.getAttributeName(), borderBottomStyle);
    }

    /**
     * Gets the {@code borderBottomWidth} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderBottomWidth() {
        return getBorderWidth(BORDER_BOTTOM_WIDTH, BORDER_BOTTOM);
    }

    /**
     * Sets the {@code borderBottomWidth} style attribute.
     * @param borderBottomWidth the new attribute
     */
    @Setter
    public void setBorderBottomWidth(final String borderBottomWidth) {
        setStyleLengthAttribute(BORDER_BOTTOM_WIDTH.getAttributeName(), borderBottomWidth, "",
                false, false, false, false);
    }

    /**
     * Gets the {@code borderLeftColor} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderLeftColor() {
        String value = getStyleAttribute(BORDER_LEFT_COLOR, false);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(BORDER_LEFT, false));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the {@code borderLeftColor} style attribute.
     * @param borderLeftColor the new attribute
     */
    @Setter
    public void setBorderLeftColor(final String borderLeftColor) {
        setStyleAttribute(BORDER_LEFT_COLOR.getAttributeName(), borderLeftColor);
    }

    /**
     * Gets the {@code borderLeftStyle} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderLeftStyle() {
        String value = getStyleAttribute(BORDER_LEFT_STYLE, false);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(BORDER_LEFT, false));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the {@code borderLeftStyle} style attribute.
     * @param borderLeftStyle the new attribute
     */
    @Setter
    public void setBorderLeftStyle(final String borderLeftStyle) {
        setStyleAttribute(BORDER_LEFT_STYLE.getAttributeName(), borderLeftStyle);
    }

    /**
     * Gets the {@code borderLeftWidth} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderLeftWidth() {
        return getBorderWidth(BORDER_LEFT_WIDTH, BORDER_LEFT);
    }

    /**
     * Gets the border width for the specified side
     * @param borderSideWidth the border side width Definition
     * @param borderside the border side Definition
     * @return the width, "" if not defined
     */
    private String getBorderWidth(final Definition borderSideWidth, final Definition borderSide) {
        String value = getStyleAttribute(borderSideWidth, false);
        if (value.isEmpty()) {
            value = findBorderWidth(getStyleAttribute(borderSide, false));
            if (value == null) {
                final String borderWidth = getStyleAttribute(BORDER_WIDTH, false);
                if (!StringUtils.isEmpty(borderWidth)) {
                    final String[] values = StringUtils.split(borderWidth);
                    int index = values.length;
                    if (borderSideWidth.name().contains("TOP")) {
                        index = 0;
                    }
                    else if (borderSideWidth.name().contains("RIGHT")) {
                        index = 1;
                    }
                    else if (borderSideWidth.name().contains("BOTTOM")) {
                        index = 2;
                    }
                    else if (borderSideWidth.name().contains("LEFT")) {
                        index = 3;
                    }
                    if (index < values.length) {
                        value = values[index];
                    }
                }
            }
            if (value == null) {
                value = findBorderWidth(getStyleAttribute(BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the {@code borderLeftWidth} style attribute.
     * @param borderLeftWidth the new attribute
     */
    @Setter
    public void setBorderLeftWidth(final String borderLeftWidth) {
        setStyleLengthAttribute(BORDER_LEFT_WIDTH.getAttributeName(), borderLeftWidth, "", false, false, false, false);
    }

    /**
     * Gets the {@code borderRightColor} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderRightColor() {
        String value = getStyleAttribute(BORDER_RIGHT_COLOR, false);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(BORDER_RIGHT, false));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the {@code borderRightColor} style attribute.
     * @param borderRightColor the new attribute
     */
    @Setter
    public void setBorderRightColor(final String borderRightColor) {
        setStyleAttribute(BORDER_RIGHT_COLOR.getAttributeName(), borderRightColor);
    }

    /**
     * Gets the {@code borderRightStyle} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderRightStyle() {
        String value = getStyleAttribute(BORDER_RIGHT_STYLE, false);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(BORDER_RIGHT, false));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the {@code borderRightStyle} style attribute.
     * @param borderRightStyle the new attribute
     */
    @Setter
    public void setBorderRightStyle(final String borderRightStyle) {
        setStyleAttribute(BORDER_RIGHT_STYLE.getAttributeName(), borderRightStyle);
    }

    /**
     * Gets the {@code borderRightWidth} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderRightWidth() {
        return getBorderWidth(BORDER_RIGHT_WIDTH, BORDER_RIGHT);
    }

    /**
     * Sets the {@code borderRightWidth} style attribute.
     * @param borderRightWidth the new attribute
     */
    @Setter
    public void setBorderRightWidth(final String borderRightWidth) {
        setStyleLengthAttribute(BORDER_RIGHT_WIDTH.getAttributeName(), borderRightWidth, "",
                false, false, false, false);
    }

    /**
     * Sets the {@code borderTop} style attribute.
     * @param borderTop the new attribute
     */
    @Setter
    public void setBorderTop(final String borderTop) {
        setStyleAttribute(BORDER_TOP.getAttributeName(), borderTop);
    }

    /**
     * Gets the {@code borderTopColor} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderTopColor() {
        String value = getStyleAttribute(BORDER_TOP_COLOR, false);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(BORDER_TOP, false));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the {@code borderTopColor} style attribute.
     * @param borderTopColor the new attribute
     */
    @Setter
    public void setBorderTopColor(final String borderTopColor) {
        setStyleAttribute(BORDER_TOP_COLOR.getAttributeName(), borderTopColor);
    }

    /**
     * Gets the {@code borderTopStyle} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderTopStyle() {
        String value = getStyleAttribute(BORDER_TOP_STYLE, false);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(BORDER_TOP, false));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Sets the {@code borderTopStyle} style attribute.
     * @param borderTopStyle the new attribute
     */
    @Setter
    public void setBorderTopStyle(final String borderTopStyle) {
        setStyleAttribute(BORDER_TOP_STYLE.getAttributeName(), borderTopStyle);
    }

    /**
     * Gets the {@code borderTopWidth} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBorderTopWidth() {
        return getBorderWidth(BORDER_TOP_WIDTH, BORDER_TOP);
    }

    /**
     * Sets the {@code borderTopWidth} style attribute.
     * @param borderTopWidth the new attribute
     */
    @Setter
    public void setBorderTopWidth(final String borderTopWidth) {
        setStyleLengthAttribute(BORDER_TOP_WIDTH.getAttributeName(), borderTopWidth, "", false, false, false, false);
    }

    /**
     * Searches for any color notation in the specified text.
     * @param text the string to search in
     * @return the string of the color if found, null otherwise
     */
    private static String findColor(final String text) {
        Color tmpColor = com.gargoylesoftware.htmlunit.util.StringUtils.findColorRGB(text);
        if (tmpColor != null) {
            return com.gargoylesoftware.htmlunit.util.StringUtils.formatColor(tmpColor);
        }

        final String[] tokens = StringUtils.split(text, ' ');
        for (final String token : tokens) {
            if (isColorKeyword(token)) {
                return token;
            }

            tmpColor = com.gargoylesoftware.htmlunit.util.StringUtils.asColorHexadecimal(token);
            if (tmpColor != null) {
                return com.gargoylesoftware.htmlunit.util.StringUtils.formatColor(tmpColor);
            }
        }
        return null;
    }

    /**
     * Searches for any URL notation in the specified text.
     * @param text the string to search in
     * @return the string of the URL if found, null otherwise
     */
    private static String findImageUrl(final String text) {
        final Matcher m = URL_PATTERN.matcher(text);
        if (m.find()) {
            return "url(\"" + m.group(1) + "\")";
        }
        return null;
    }

    /**
     * Searches for any position notation in the specified text.
     * @param text the string to search in
     * @return the string of the position if found, null otherwise
     */
    private static String findPosition(final String text) {
        Matcher m = POSITION_PATTERN.matcher(text);
        if (m.find()) {
            return m.group(1) + " " + m.group(3);
        }
        m = POSITION_PATTERN2.matcher(text);
        if (m.find()) {
            return m.group(1) + " " + m.group(2);
        }
        m = POSITION_PATTERN3.matcher(text);
        if (m.find()) {
            return m.group(2) + " " + m.group(1);
        }
        return null;
    }

    /**
     * Searches for any repeat notation in the specified text.
     * @param text the string to search in
     * @return the string of the repeat if found, null otherwise
     */
    private static String findRepeat(final String text) {
        if (text.contains("repeat-x")) {
            return "repeat-x";
        }
        if (text.contains("repeat-y")) {
            return "repeat-y";
        }
        if (text.contains("no-repeat")) {
            return "no-repeat";
        }
        if (text.contains("repeat")) {
            return "repeat";
        }
        return null;
    }

    /**
     * Searches for any attachment notation in the specified text.
     * @param text the string to search in
     * @return the string of the attachment if found, null otherwise
     */
    private static String findAttachment(final String text) {
        if (text.contains("scroll")) {
            return "scroll";
        }
        if (text.contains("fixed")) {
            return "fixed";
        }
        return null;
    }

    /**
     * Searches for a border style in the specified text.
     * @param text the string to search in
     * @return the border style if found, null otherwise
     */
    private static String findBorderStyle(final String text) {
        for (final String token : StringUtils.split(text, ' ')) {
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
        for (final String token : StringUtils.split(text, ' ')) {
            if (isBorderWidth(token)) {
                return token;
            }
        }
        return null;
    }

    /**
     * Returns if the specified token is a reserved color keyword.
     * @param token the token to check
     * @return whether the token is a reserved color keyword or not
     */
    private static boolean isColorKeyword(final String token) {
        return CSSColors_.containsKey(token.toLowerCase(Locale.ROOT));
    }

    /**
     * Returns if the specified token is a border style.
     * @param token the token to check
     * @return whether the token is a border style or not
     */
    private static boolean isBorderStyle(final String token) {
        return "none".equalsIgnoreCase(token) || "hidden".equalsIgnoreCase(token)
            || "dotted".equalsIgnoreCase(token) || "dashed".equalsIgnoreCase(token)
            || "solid".equalsIgnoreCase(token) || "double".equalsIgnoreCase(token)
            || "groove".equalsIgnoreCase(token) || "ridge".equalsIgnoreCase(token)
            || "inset".equalsIgnoreCase(token) || "outset".equalsIgnoreCase(token);
    }

    /**
     * Returns if the specified token is a border width.
     * @param token the token to check
     * @return whether the token is a border width or not
     */
    private static boolean isBorderWidth(final String token) {
        return "thin".equalsIgnoreCase(token) || "medium".equalsIgnoreCase(token)
            || "thick".equalsIgnoreCase(token) || isLength(token);
    }

    /**
     * Returns if the specified token is a length.
     * @param token the token to check
     * @return whether the token is a length or not
     */
    static boolean isLength(String token) {
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
     * Gets the {@code margin} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getMargin() {
        return getStyleAttribute(MARGIN);
    }

    /**
     * Sets the {@code margin} style attribute.
     * @param margin the new attribute
     */
    @Setter
    public void setMargin(final String margin) {
        setStyleAttribute(MARGIN.getAttributeName(), margin);
    }

    /**
     * Gets the {@code marginBottom} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getMarginBottom() {
        return getStyleAttribute(MARGIN_BOTTOM, MARGIN);
    }

    /**
     * Sets the {@code marginBottom} style attribute.
     * @param marginBottom the new attribute
     */
    @Setter
    public void setMarginBottom(final String marginBottom) {
        setStyleLengthAttribute(MARGIN_BOTTOM.getAttributeName(), marginBottom, "", true, true, false, false);
    }

    /**
     * Gets the {@code marginLeft} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getMarginLeft() {
        return getStyleAttribute(MARGIN_LEFT, MARGIN);
    }

    /**
     * Sets the {@code marginLeft} style attribute.
     * @param marginLeft the new attribute
     */
    @Setter
    public void setMarginLeft(final String marginLeft) {
        setStyleLengthAttribute(MARGIN_LEFT.getAttributeName(), marginLeft, "", true, true, false, false);
    }

    /**
     * Gets the {@code marginRight} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getMarginRight() {
        return getStyleAttribute(MARGIN_RIGHT, MARGIN);
    }

    /**
     * Sets the {@code marginRight} style attribute.
     * @param marginRight the new attribute
     */
    @Setter
    public void setMarginRight(final String marginRight) {
        setStyleLengthAttribute(MARGIN_RIGHT.getAttributeName(), marginRight, "", true, true, false, false);
    }

    /**
     * Gets the {@code marginTop} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getMarginTop() {
        return getStyleAttribute(MARGIN_TOP, MARGIN);
    }

    /**
     * Sets the {@code marginTop} style attribute.
     * @param marginTop the new attribute
     */
    @Setter
    public void setMarginTop(final String marginTop) {
        setStyleLengthAttribute(MARGIN_TOP.getAttributeName(), marginTop, "", true, true, false, false);
    }

    /**
     * Gets the {@code padding} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getPadding() {
        return getStyleAttribute(PADDING);
    }

    /**
     * Sets the {@code padding} style attribute.
     * @param padding the new attribute
     */
    @Setter
    public void setPadding(final String padding) {
        setStyleAttribute(PADDING.getAttributeName(), padding);
    }

    /**
     * Gets the {@code paddingBottom} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getPaddingBottom() {
        return getStyleAttribute(PADDING_BOTTOM, PADDING);
    }

    /**
     * Sets the {@code paddingBottom} style attribute.
     * @param paddingBottom the new attribute
     */
    @Setter
    public void setPaddingBottom(final String paddingBottom) {
        setStyleLengthAttribute(PADDING_BOTTOM.getAttributeName(), paddingBottom, "", false, true, false, false);
    }

    /**
     * Gets the {@code paddingLeft} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getPaddingLeft() {
        return getStyleAttribute(PADDING_LEFT, PADDING);
    }

    /**
     * Sets the {@code paddingLeft} style attribute.
     * @param paddingLeft the new attribute
     */
    @Setter
    public void setPaddingLeft(final String paddingLeft) {
        setStyleLengthAttribute(PADDING_LEFT.getAttributeName(), paddingLeft, "", false, true, false, false);
    }

    /**
     * Gets the {@code paddingRight} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getPaddingRight() {
        return getStyleAttribute(PADDING_RIGHT, PADDING);
    }

    /**
     * Sets the {@code paddingRight} style attribute.
     * @param paddingRight the new attribute
     */
    @Setter
    public void setPaddingRight(final String paddingRight) {
        setStyleLengthAttribute(PADDING_RIGHT.getAttributeName(), paddingRight, "", false, true, false, false);
    }

    /**
     * Gets the {@code paddingTop} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getPaddingTop() {
        return getStyleAttribute(PADDING_TOP, PADDING);
    }

    /**
     * Sets the {@code paddingTop} style attribute.
     * @param paddingTop the new attribute
     */
    @Setter
    public void setPaddingTop(final String paddingTop) {
        setStyleLengthAttribute(PADDING_TOP.getAttributeName(), paddingTop, "", false, true, false, false);
    }

    /**
     * Gets the {@code right} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getRight() {
        return getStyleAttribute(RIGHT);
    }

    /**
     * Sets the {@code right} style attribute.
     * @param right the new attribute
     */
    @Setter
    public void setRight(final String right) {
        setStyleLengthAttribute(RIGHT.getAttributeName(), right, "", true, true, false, false);
    }

    /**
     * Gets the {@code left} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getLeft() {
        return getStyleAttribute(LEFT);
    }

    /**
     * Sets the {@code left} style attribute.
     * @param left the new attribute
     */
    @Setter
    public void setLeft(final String left) {
        setStyleLengthAttribute(LEFT.getAttributeName(), left, "", true, true, false, false);
    }

    /**
     * Gets the {@code top} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getTop() {
        return getStyleAttribute(TOP);
    }

    /**
     * Sets the {@code top} style attribute.
     * @param top the new attribute
     */
    @Setter
    public void setTop(final String top) {
        setStyleLengthAttribute(TOP.getAttributeName(), top, "", true, true, false, false);
    }

    /**
     * Gets the {@code bottom} style attribute.
     * @return the style attribute
     */
    @Getter
    public String getBottom() {
        return getStyleAttribute(BOTTOM);
    }

    /**
     * Sets the {@code bottom} style attribute.
     * @param bottom the new attribute
     */
    @Setter
    public void setBottom(final String bottom) {
        setStyleLengthAttribute(BOTTOM.getAttributeName(), bottom, "", true, true, false, false);
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(CSSStyleDeclaration2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("CSSStyleDeclaration",
                    staticHandle("constructor", CSSStyleDeclaration2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("CSSStyleDeclaration");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("CSSStyleDeclaration");
        }
    }

    /**
     * Contains information about a single style element, including its name, its value, and an index which
     * can be compared against other indices in order to determine precedence.
     */
    protected static class StyleElement implements Comparable<StyleElement>, Serializable {
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
            name_ = name;
            value_ = value;
            priority_ = priority;
            index_ = index;
            specificity_ = specificity;
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
         * Returns {@code true} if this style element contains a default value. This method isn't
         * currently used anywhere because default style elements are applied before non-default
         * style elements, so the natural ordering results in correct precedence rules being applied
         * (i.e. default style elements don't override non-default style elements) without the need
         * for special checks.
         * @return {@code true} if this style element contains a default value
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
        @Override
        public int compareTo(final StyleElement e) {
            if (e != null) {
                final long styleIndex = e.index_;
                // avoid conversion to long
                return (index_ < styleIndex) ? -1 : (index_ == styleIndex) ? 0 : 1;
            }
            return 1;
        }
    }

    /**
     * Encapsulates the retrieval of a style attribute, given a DOM element from which to retrieve it.
     */
    protected abstract static class CssValue {
        private final int defaultValue_;
        private final int windowDefaultValue_;

        /**
         * C'tor.
         * @param defaultValue the default value
         * @param windowDefaultValue the default value for the window
         */
        public CssValue(final int defaultValue, final int windowDefaultValue) {
            defaultValue_ = defaultValue;
            windowDefaultValue_ = windowDefaultValue;
        }

        /**
         * Gets the default value.
         * @return the default value
         */
        public int getDefaultValue() {
            return defaultValue_;
        }

        /**
         * Gets the default size for the window.
         * @return the default value for the window
         */
        public int getWindowDefaultValue() {
            return windowDefaultValue_;
        }

        /**
         * Returns the CSS attribute value for the specified element.
         * @param element the element for which the CSS attribute value is to be retrieved
         * @return the CSS attribute value for the specified element
         */
        public final String get(final Element2 element) {
            final Global global = NashornJavaScriptEngine.getGlobal(element.getWindow().getWebWindow());
            final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, element, null);
            final String value = get(style);
            return value;
        }

        /**
         * Returns the CSS attribute value from the specified computed style.
         * @param style the computed style from which to retrieve the CSS attribute value
         * @return the CSS attribute value from the specified computed style
         */
        public abstract String get(ComputedCSSStyleDeclaration2 style);
    }

}
