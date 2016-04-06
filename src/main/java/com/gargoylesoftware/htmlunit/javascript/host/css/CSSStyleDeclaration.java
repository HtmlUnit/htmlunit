/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_BACKGROUND_INITIAL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_SET_NULL_THROWS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_SUPPORTS_BEHAVIOR_PROPERTY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_ZINDEX_TYPE_INTEGER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_SET_PROPERTY_IMPORTANT_IGNORES_CASE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_UNSUPPORTED_PROPERTY_GETTER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_WRONG_INDEX_RETURNS_UNDEFINED;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.FLOAT;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;

import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

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
@JsxClass
public class CSSStyleDeclaration extends SimpleScriptable implements ScriptableWithFallbackGetter {
    /** CSS important property constant. */
    protected static final String PRIORITY_IMPORTANT = "important";

    private static final String BACKGROUND = "background";
    private static final String BACKGROUND_ATTACHMENT = "background-attachment";
    private static final String BACKGROUND_COLOR = "background-color";
    private static final String BACKGROUND_IMAGE = "background-image";
    private static final String BACKGROUND_POSITION = "background-position";
    private static final String BACKGROUND_REPEAT = "background-repeat";
    private static final String BEHAVIOR = "behavior";
    private static final String BORDER = "border";
    private static final String BORDER_BOTTOM = "border-bottom";
    private static final String BORDER_BOTTOM_COLOR = "border-bottom-color";
    private static final String BORDER_BOTTOM_STYLE = "border-bottom-style";
    private static final String BORDER_BOTTOM_WIDTH = "border-bottom-width";
    private static final String BORDER_LEFT = "border-left";
    private static final String BORDER_LEFT_COLOR = "border-left-color";
    private static final String BORDER_LEFT_STYLE = "border-left-style";
    private static final String BORDER_WIDTH = "border-width";
    private static final String BORDER_LEFT_WIDTH = "border-left-width";
    private static final String BORDER_RIGHT = "border-right";
    private static final String BORDER_RIGHT_COLOR = "border-right-color";
    private static final String BORDER_RIGHT_STYLE = "border-right-style";
    private static final String BORDER_RIGHT_WIDTH = "border-right-width";
    private static final String BORDER_TOP = "border-top";
    private static final String BORDER_TOP_COLOR = "border-top-color";
    private static final String BORDER_TOP_STYLE = "border-top-style";
    private static final String BORDER_TOP_WIDTH = "border-top-width";
    private static final String BOTTOM = "bottom";
    private static final String COLOR = "color";
    private static final String DISPLAY = "display";
    private static final String FONT_SIZE = "font-size";
    private static final String HEIGHT = "height";
    private static final String LEFT = "left";
    private static final String LETTER_SPACING = "letter-spacing";
    private static final String MARGIN_BOTTOM = "margin-bottom";
    private static final String MARGIN_LEFT = "margin-left";
    private static final String MARGIN_RIGHT = "margin-right";
    private static final String MARGIN = "margin";
    private static final String MARGIN_TOP = "margin-top";
    private static final String MARKS = "marks";
    private static final String MAX_HEIGHT = "max-height";
    private static final String MAX_WIDTH = "max-width";
    private static final String MIN_HEIGHT = "min-height";
    private static final String MIN_WIDTH = "min-width";
    private static final String OPACITY = "opacity";
    private static final String OUTLINE = "outline";
    private static final String OUTLINE_WIDTH = "outline-width";
    private static final String PADDING_BOTTOM = "padding-bottom";
    private static final String PADDING_LEFT = "padding-left";
    private static final String PADDING_RIGHT = "padding-right";
    private static final String PADDING = "padding";
    private static final String PADDING_TOP = "padding-top";
    private static final String PAGE = "page";
    private static final String RIGHT = "right";
    private static final String RUBY_ALIGN = "ruby-align";
    private static final String SIZE = "size";
    private static final String TEXT_INDENT = "text-indent";
    private static final String TOP = "top";
    private static final String VERTICAL_ALIGN = "vertical-align";
    private static final String WORD_SPACING = "word-spacing";
    private static final String Z_INDEX = "z-index";

    /** The width style attribute. */
    protected static final String WIDTH = "width";

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

    private static final Log LOG = LogFactory.getLog(CSSStyleDeclaration.class);
    private static final Map<String, String> CSSColors_ = new HashMap<>();

    private static final Map<String, String> CamelizeCache_
            = Collections.synchronizedMap(new HashMap<String, String>());

    /** The different types of shorthand values. */
    private enum Shorthand {
        TOP("top"),
        RIGHT("right"),
        BOTTOM("bottom"),
        LEFT("left");

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
    private Element jsElement_;

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

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public CSSStyleDeclaration() {
    }

    /**
     * Creates an instance and sets its parent scope to the one of the provided element.
     * @param element the element to which this style is bound
     */
    public CSSStyleDeclaration(final Element element) {
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
    void initialize(final Element element) {
        // Initialize.
        WebAssert.notNull("htmlElement", element);
        jsElement_ = element;
        setDomNode(element.getDomNodeOrNull(), false);

        // If an IE behavior was specified in the style, apply the behavior.
        if (getBrowserVersion().hasFeature(CSS_SUPPORTS_BEHAVIOR_PROPERTY)
            && element instanceof HTMLElement) {
            final HTMLElement htmlElement = (HTMLElement) element;
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
     * IE makes unknown style properties accessible.
     * @param name the name of the requested property
     * @return the object value, {@link #NOT_FOUND} if nothing is found
     */
    @Override
    public Object getWithFallback(final String name) {
        // TODO
        if (getBrowserVersion().hasFeature(JS_STYLE_UNSUPPORTED_PROPERTY_GETTER)) {
            if (null != jsElement_) {
                final StyleElement element = getStyleElement(name);
                if (element != null && element.getValue() != null) {
                    return element.getValue();
                }
            }
        }

        return NOT_FOUND;
    }

    /**
     * Returns the element to which this style belongs.
     * @return the element to which this style belongs
     */
    protected Element getElement() {
        return jsElement_;
    }

    /**
     * Returns the value of the named style attribute, or an empty string if it is not found.
     *
     * @param name the name of the style attribute whose value is to be retrieved
     * @return the named style attribute value, or an empty string if it is not found
     */
    protected String getStyleAttribute(final String name) {
        if (styleDeclaration_ != null) {
            return styleDeclaration_.getPropertyValue(name);
        }
        final StyleElement element = getStyleElement(name);
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
     * Returns the priority of the named style attribute, or an empty string if it is not found.
     *
     * @param name the name of the style attribute whose value is to be retrieved
     * @return the named style attribute value, or an empty string if it is not found
     */
    protected String getStylePriority(final String name) {
        if (styleDeclaration_ != null) {
            return styleDeclaration_.getPropertyPriority(name);
        }
        final StyleElement element = getStyleElement(name);
        if (element != null && element.getValue() != null) {
            return element.getPriority();
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
     * @return the value of one of the two named style attributes
     */
    private String getStyleAttribute(final String name1, final String name2, final Shorthand shorthand) {
        final String value;
        if (styleDeclaration_ != null) {
            final String value1 = styleDeclaration_.getPropertyValue(name1);
            final String value2 = styleDeclaration_.getPropertyValue(name2);

            if ("".equals(value1) && "".equals(value2)) {
                return "";
            }
            if (!"".equals(value1) && "".equals(value2)) {
                return value1;
            }
            value = value2;
        }
        else {
            final StyleElement element1 = getStyleElement(name1);
            final StyleElement element2 = getStyleElement(name2);

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
        if ("null".equals(newValue)) {
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

    private void writeToElement(final Map<String, StyleElement> styleMap) {
        final StringBuilder buffer = new StringBuilder();
        final SortedSet<StyleElement> sortedValues = new TreeSet<>(styleMap.values());
        for (final StyleElement e : sortedValues) {
            if (buffer.length() != 0) {
                buffer.append(" ");
            }
            buffer.append(e.getName());
            buffer.append(": ");
            buffer.append(e.getValue());

            final String prio = e.getPriority();
            if (StringUtils.isNotBlank(prio)) {
                buffer.append(" !");
                buffer.append(prio);
            }
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
     * @see com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap#decamelize(String)
     */
    protected static String camelize(final String string) {
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

        final StringBuilder buffer = new StringBuilder(string);
        buffer.deleteCharAt(pos);
        buffer.setCharAt(pos, Character.toUpperCase(buffer.charAt(pos)));

        int i = pos + 1;
        while (i < buffer.length() - 1) {
            if (buffer.charAt(i) == '-') {
                buffer.deleteCharAt(i);
                buffer.setCharAt(i, Character.toUpperCase(buffer.charAt(i)));
            }
            i++;
        }
        result = buffer.toString();
        CamelizeCache_.put(string, result);

        return result;
    }

    /**
     * Gets the {@code accelerator} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getAccelerator() {
        return defaultIfEmpty(getStyleAttribute(Definition.ACCELERATOR.getAttributeName()), "false");
    }

    /**
     * Sets the {@code accelerator} style attribute.
     * @param accelerator the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setAccelerator(final String accelerator) {
        setStyleAttributePixel(Definition.ACCELERATOR.getAttributeName(), accelerator);
    }

    /**
     * Gets the {@code backgroundAttachment} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundAttachment() {
        String value = getStyleAttribute(BACKGROUND_ATTACHMENT);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND);
            if (StringUtils.isNotBlank(bg)) {
                value = findAttachment(bg);
                if (value == null) {
                    if (getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL)
                            && getClass() == CSSStyleDeclaration.class) {
                        return "initial";
                    }
                    return "scroll"; // default if shorthand is used
                }
                return value;
            }
            return "";
        }

        return value;
    }

    /**
     * Sets the {@code backgroundAttachment} style attribute.
     * @param backgroundAttachment the new attribute
     */
    @JsxSetter
    public void setBackgroundAttachment(final String backgroundAttachment) {
        setStyleAttribute(BACKGROUND_ATTACHMENT, backgroundAttachment);
    }

    /**
     * Gets the {@code backgroundColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundColor() {
        String value = getStyleAttribute(BACKGROUND_COLOR);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND);
            if (StringUtils.isBlank(bg)) {
                return "";
            }
            value = findColor(bg);
            if (value == null) {
                if (getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL)) {
                    if (getClass() == CSSStyleDeclaration.class) {
                        return "initial";
                    }
                    return "rgba(0, 0, 0, 0)";
                }
                return "transparent"; // default if shorthand is used
            }
            return value;
        }
        if (StringUtils.isBlank(value)) {
            return "";
        }
        return value;
    }

    /**
     * Sets the {@code backgroundColor} style attribute.
     * @param backgroundColor the new attribute
     */
    @JsxSetter
    public void setBackgroundColor(final String backgroundColor) {
        setStyleAttribute(BACKGROUND_COLOR, backgroundColor);
    }

    /**
     * Gets the {@code backgroundImage} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundImage() {
        String value = getStyleAttribute(BACKGROUND_IMAGE);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND);
            if (StringUtils.isNotBlank(bg)) {
                value = findImageUrl(bg);
                final boolean isComputed = getClass() != CSSStyleDeclaration.class;
                final boolean backgroundInitial = getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL);
                if (value == null) {
                    return backgroundInitial && !isComputed ? "initial" : "none";
                }
                if (isComputed) {
                    try {
                        value = value.substring(5, value.length() - 2);
                        return "url(\"" + ((HtmlElement) jsElement_.getDomNodeOrDie()).getHtmlPageOrNull()
                            .getFullyQualifiedUrl(value) + "\")";
                    }
                    catch (final Exception e) {
                        // ignore
                    }
                }
                return value;
            }
            return "";
        }

        return value;
    }

    /**
     * Sets the {@code backgroundImage} style attribute.
     * @param backgroundImage the new attribute
     */
    @JsxSetter
    public void setBackgroundImage(final String backgroundImage) {
        setStyleAttribute(BACKGROUND_IMAGE, backgroundImage);
    }

    /**
     * Gets the {@code backgroundPosition} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundPosition() {
        String value = getStyleAttribute(BACKGROUND_POSITION);
        if (value == null) {
            return null;
        }
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND);
            if (bg == null) {
                return null;
            }
            if (StringUtils.isNotBlank(bg)) {
                value = findPosition(bg);
                final boolean isInitial = getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL);
                final boolean isComputed = getClass() != CSSStyleDeclaration.class;
                if (value == null) {
                    if (isInitial) {
                        return isComputed ? "" : "initial";
                    }
                    return "0% 0%";
                }
                if (getBrowserVersion().hasFeature(CSS_ZINDEX_TYPE_INTEGER)) {
                    final String[] values = value.split(" ");
                    if ("center".equals(values[0])) {
                        values[0] = "";
                    }
                    if ("center".equals(values[1])) {
                        values[1] = "";
                    }
                    if (!isComputed || value.contains("top")) {
                        return (values[0] + ' ' + values[1]).trim();
                    }
                }
                if (isInitial || isComputed) {
                    final String[] values = value.split(" ");
                    switch (values[0]) {
                        case "left":
                            values[0] = "0%";
                            break;

                        case "center":
                            values[0] = "50%";
                            break;

                        case "right":
                            values[0] = "100%";
                            break;

                        default:
                    }
                    switch (values[1]) {
                        case "top":
                            values[1] = "0%";
                            break;

                        case "center":
                            values[1] = "50%";
                            break;

                        case "bottom":
                            values[1] = "100%";
                            break;

                        default:
                    }
                    value = values[0] + ' ' + values[1];
                }
                return value;
            }
            return "";
        }

        return value;
    }

    /**
     * Sets the {@code backgroundPosition} style attribute.
     * @param backgroundPosition the new attribute
     */
    @JsxSetter
    public void setBackgroundPosition(final String backgroundPosition) {
        setStyleAttribute(BACKGROUND_POSITION, backgroundPosition);
    }

    /**
     * Gets the {@code backgroundRepeat} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundRepeat() {
        String value = getStyleAttribute(BACKGROUND_REPEAT);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND);
            if (StringUtils.isNotBlank(bg)) {
                value = findRepeat(bg);
                if (value == null) {
                    if (getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL)
                            && getClass() == CSSStyleDeclaration.class) {
                        return "initial";
                    }
                    return "repeat"; // default if shorthand is used
                }
                return value;
            }
            return "";
        }

        return value;
    }

    /**
     * Sets the {@code backgroundRepeat} style attribute.
     * @param backgroundRepeat the new attribute
     */
    @JsxSetter
    public void setBackgroundRepeat(final String backgroundRepeat) {
        setStyleAttribute(BACKGROUND_REPEAT, backgroundRepeat);
    }

    /**
     * Gets the {@code border} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorder() {
        return getStyleAttribute(BORDER);
    }

    /**
     * Sets the {@code border} style attribute.
     * @param border the new attribute
     */
    @JsxSetter
    public void setBorder(final String border) {
        setStyleAttribute(BORDER, border);
    }

    /**
     * Gets the {@code borderBottomColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderBottomColor() {
        String value = getStyleAttribute(BORDER_BOTTOM_COLOR);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(BORDER_BOTTOM));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER));
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
    @JsxSetter
    public void setBorderBottomColor(final String borderBottomColor) {
        setStyleAttribute(BORDER_BOTTOM_COLOR, borderBottomColor);
    }

    /**
     * Gets the {@code borderBottomStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderBottomStyle() {
        String value = getStyleAttribute(BORDER_BOTTOM_STYLE);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(BORDER_BOTTOM));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER));
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
    @JsxSetter
    public void setBorderBottomStyle(final String borderBottomStyle) {
        setStyleAttribute(BORDER_BOTTOM_STYLE, borderBottomStyle);
    }

    /**
     * Gets the {@code borderBottomWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderBottomWidth() {
        return getBorderWidth(Shorthand.BOTTOM);
    }

    /**
     * Sets the {@code borderBottomWidth} style attribute.
     * @param borderBottomWidth the new attribute
     */
    @JsxSetter
    public void setBorderBottomWidth(final String borderBottomWidth) {
        if (!borderBottomWidth.endsWith("%")) {
            setStyleAttributePixel(BORDER_BOTTOM_WIDTH, borderBottomWidth);
        }
    }

    /**
     * Gets the {@code borderLeftColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderLeftColor() {
        String value = getStyleAttribute(BORDER_LEFT_COLOR);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(BORDER_LEFT));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER));
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
    @JsxSetter
    public void setBorderLeftColor(final String borderLeftColor) {
        setStyleAttribute(BORDER_LEFT_COLOR, borderLeftColor);
    }

    /**
     * Gets the {@code borderLeftStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderLeftStyle() {
        String value = getStyleAttribute(BORDER_LEFT_STYLE);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(BORDER_LEFT));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER));
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
    @JsxSetter
    public void setBorderLeftStyle(final String borderLeftStyle) {
        setStyleAttribute(BORDER_LEFT_STYLE, borderLeftStyle);
    }

    /**
     * Gets the {@code borderLeftWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderLeftWidth() {
        return getBorderWidth(Shorthand.LEFT);
    }

    /**
     * Gets the border width for the specified side
     * @param side the side
     * @param side the side's position
     * @return the width, "" if not defined
     */
    private String getBorderWidth(final Shorthand side) {
        String value = getStyleAttribute(BORDER + "-" + side + "-width");
        if (value.isEmpty()) {
            value = findBorderWidth(getStyleAttribute(BORDER + "-" + side));
            if (value == null) {
                final String borderWidth = getStyleAttribute(BORDER_WIDTH);
                if (!StringUtils.isEmpty(borderWidth)) {
                    final String[] values = StringUtils.split(borderWidth);
                    if (values.length > side.ordinal()) {
                        value = values[side.ordinal()];
                    }
                }
            }
            if (value == null) {
                value = findBorderWidth(getStyleAttribute(BORDER));
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
    @JsxSetter
    public void setBorderLeftWidth(final String borderLeftWidth) {
        if (!borderLeftWidth.endsWith("%")) {
            setStyleAttributePixel(BORDER_LEFT_WIDTH, borderLeftWidth);
        }
    }

    /**
     * Gets the {@code borderRightColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderRightColor() {
        String value = getStyleAttribute(BORDER_RIGHT_COLOR);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(BORDER_RIGHT));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER));
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
    @JsxSetter
    public void setBorderRightColor(final String borderRightColor) {
        setStyleAttribute(BORDER_RIGHT_COLOR, borderRightColor);
    }

    /**
     * Gets the {@code borderRightStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderRightStyle() {
        String value = getStyleAttribute(BORDER_RIGHT_STYLE);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(BORDER_RIGHT));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER));
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
    @JsxSetter
    public void setBorderRightStyle(final String borderRightStyle) {
        setStyleAttribute(BORDER_RIGHT_STYLE, borderRightStyle);
    }

    /**
     * Gets the {@code borderRightWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderRightWidth() {
        return getBorderWidth(Shorthand.RIGHT);
    }

    /**
     * Sets the {@code borderRightWidth} style attribute.
     * @param borderRightWidth the new attribute
     */
    @JsxSetter
    public void setBorderRightWidth(final String borderRightWidth) {
        if (!borderRightWidth.endsWith("%")) {
            setStyleAttributePixel(BORDER_RIGHT_WIDTH, borderRightWidth);
        }
    }

    /**
     * Sets the {@code borderTop} style attribute.
     * @param borderTop the new attribute
     */
    @JsxSetter
    public void setBorderTop(final String borderTop) {
        setStyleAttribute(BORDER_TOP, borderTop);
    }

    /**
     * Gets the {@code borderTopColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderTopColor() {
        String value = getStyleAttribute(BORDER_TOP_COLOR);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(BORDER_TOP));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER));
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
    @JsxSetter
    public void setBorderTopColor(final String borderTopColor) {
        setStyleAttribute(BORDER_TOP_COLOR, borderTopColor);
    }

    /**
     * Gets the {@code borderTopStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderTopStyle() {
        String value = getStyleAttribute(BORDER_TOP_STYLE);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(BORDER_TOP));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER));
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
    @JsxSetter
    public void setBorderTopStyle(final String borderTopStyle) {
        setStyleAttribute(BORDER_TOP_STYLE, borderTopStyle);
    }

    /**
     * Gets the {@code borderTopWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderTopWidth() {
        return getBorderWidth(Shorthand.TOP);
    }

    /**
     * Sets the {@code borderTopWidth} style attribute.
     * @param borderTopWidth the new attribute
     */
    @JsxSetter
    public void setBorderTopWidth(final String borderTopWidth) {
        if (!borderTopWidth.endsWith("%")) {
            setStyleAttributePixel(BORDER_TOP_WIDTH, borderTopWidth);
        }
    }

    /**
     * Gets the {@code bottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBottom() {
        return getStyleAttribute(BOTTOM);
    }

    /**
     * Sets the {@code bottom} style attribute.
     * @param bottom the new attribute
     */
    @JsxSetter
    public void setBottom(final String bottom) {
        setStyleAttributePixel(BOTTOM, bottom);
    }

    /**
     * Gets the {@code color} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getColor() {
        return getStyleAttribute(COLOR);
    }

    /**
     * Sets the {@code color} style attribute.
     * @param color the new attribute
     */
    @JsxSetter
    public void setColor(final String color) {
        setStyleAttribute(COLOR, color);
    }

    /**
     * Gets the {@code cssFloat} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getCssFloat() {
        return getStyleAttribute(FLOAT);
    }

    /**
     * Sets the {@code cssFloat} style attribute.
     * @param value the new attribute
     */
    @JsxSetter
    public void setCssFloat(final String value) {
        setStyleAttribute(FLOAT.getAttributeName(), value);
    }

    /**
     * Returns the actual text of the style.
     * @return the actual text of the style
     */
    @JsxGetter
    public String getCssText() {
        return jsElement_.getDomNodeOrDie().getAttribute("style");
    }

    /**
     * Sets the actual text of the style.
     * @param value the new text
     */
    @JsxSetter
    public void setCssText(final String value) {
        jsElement_.getDomNodeOrDie().setAttribute("style", value);
    }

    /**
     * Gets the {@code display} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getDisplay() {
        return getStyleAttribute(DISPLAY);
    }

    /**
     * Sets the {@code display} style attribute.
     * @param display the new attribute
     */
    @JsxSetter
    public void setDisplay(final String display) {
        setStyleAttribute(DISPLAY, display);
    }

    /**
     * Gets the {@code fontSize} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFontSize() {
        return getStyleAttribute(FONT_SIZE);
    }

    /**
     * Sets the {@code fontSize} style attribute.
     * @param fontSize the new attribute
     */
    @JsxSetter
    public void setFontSize(final String fontSize) {
        setStyleAttributePixel(FONT_SIZE, fontSize);
    }

    /**
     * Gets the {@code height} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getHeight() {
        return getStyleAttribute(HEIGHT);
    }

    /**
     * Sets the {@code height} style attribute.
     * @param height the new attribute
     */
    @JsxSetter
    public void setHeight(final String height) {
        setStyleAttributePixel(HEIGHT, height);
    }

    /**
     * Gets the {@code left} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getLeft() {
        return getStyleAttribute(LEFT);
    }

    /**
     * Sets the {@code left} style attribute.
     * @param left the new attribute
     */
    @JsxSetter
    public void setLeft(final String left) {
        setStyleAttributePixel(LEFT, left);
    }

    /**
     * Returns the {@code length} property.
     * @return the {@code length} property
     */
    @JsxGetter
    public int getLength() {
        return getStyleMap().size();
    }

    /**
     * Gets the {@code letterSpacing} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getLetterSpacing() {
        return getStyleAttribute(LETTER_SPACING);
    }

    /**
     * Sets the {@code letterSpacing} style attribute.
     * @param letterSpacing the new attribute
     */
    @JsxSetter
    public void setLetterSpacing(final String letterSpacing) {
        if (!letterSpacing.endsWith("%")) {
            setStyleAttributePixel(LETTER_SPACING, letterSpacing);
        }
    }

    /**
     * Gets the {@code margin} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMargin() {
        return getStyleAttribute(MARGIN);
    }

    /**
     * Sets the {@code margin} style attribute.
     * @param margin the new attribute
     */
    @JsxSetter
    public void setMargin(final String margin) {
        setStyleAttribute(MARGIN, margin);
    }

    /**
     * Gets the {@code marginBottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginBottom() {
        return getStyleAttribute(MARGIN_BOTTOM, MARGIN, Shorthand.BOTTOM);
    }

    /**
     * Sets the {@code marginBottom} style attribute.
     * @param marginBottom the new attribute
     */
    @JsxSetter
    public void setMarginBottom(final String marginBottom) {
        setStyleAttributePixel(MARGIN_BOTTOM, marginBottom);
    }

    /**
     * Gets the {@code marginLeft} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginLeft() {
        return getStyleAttribute(MARGIN_LEFT, MARGIN, Shorthand.LEFT);
    }

    /**
     * Sets the {@code marginLeft} style attribute.
     * @param marginLeft the new attribute
     */
    @JsxSetter
    public void setMarginLeft(final String marginLeft) {
        setStyleAttributePixel(MARGIN_LEFT, marginLeft);
    }

    /**
     * Gets the {@code marginRight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginRight() {
        return getStyleAttribute(MARGIN_RIGHT, MARGIN, Shorthand.RIGHT);
    }

    /**
     * Sets the {@code marginRight} style attribute.
     * @param marginRight the new attribute
     */
    @JsxSetter
    public void setMarginRight(final String marginRight) {
        setStyleAttributePixel(MARGIN_RIGHT, marginRight);
    }

    /**
     * Gets the {@code marginTop} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginTop() {
        return getStyleAttribute(MARGIN_TOP, MARGIN, Shorthand.TOP);
    }

    /**
     * Sets the {@code marginTop} style attribute.
     * @param marginTop the new attribute
     */
    @JsxSetter
    public void setMarginTop(final String marginTop) {
        setStyleAttributePixel(MARGIN_TOP, marginTop);
    }

    /**
     * Gets the {@code marks} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(value = FF, maxVersion = 38))
    public String getMarks() {
        return getStyleAttribute(MARKS);
    }

    /**
     * Sets the {@code marks} style attribute.
     * @param marks the new attribute
     */
    @JsxSetter(@WebBrowser(value = FF, maxVersion = 38))
    public void setMarks(final String marks) {
        setStyleAttribute(MARKS, marks);
    }

    /**
     * Gets the {@code maxHeight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMaxHeight() {
        return getStyleAttribute(MAX_HEIGHT);
    }

    /**
     * Sets the {@code maxHeight} style attribute.
     * @param maxHeight the new attribute
     */
    @JsxSetter
    public void setMaxHeight(final String maxHeight) {
        setStyleAttributePixel(MAX_HEIGHT, maxHeight);
    }

    /**
     * Gets the {@code maxWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMaxWidth() {
        return getStyleAttribute(MAX_WIDTH);
    }

    /**
     * Sets the {@code maxWidth} style attribute.
     * @param maxWidth the new attribute
     */
    @JsxSetter
    public void setMaxWidth(final String maxWidth) {
        setStyleAttributePixel(MAX_WIDTH, maxWidth);
    }

    /**
     * Gets the {@code minHeight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMinHeight() {
        return getStyleAttribute(MIN_HEIGHT);
    }

    /**
     * Sets the {@code minHeight} style attribute.
     * @param minHeight the new attribute
     */
    @JsxSetter
    public void setMinHeight(final String minHeight) {
        setStyleAttributePixel(MIN_HEIGHT, minHeight);
    }

    /**
     * Gets the {@code minWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMinWidth() {
        return getStyleAttribute(MIN_WIDTH);
    }

    /**
     * Sets the {@code minWidth} style attribute.
     * @param minWidth the new attribute
     */
    @JsxSetter
    public void setMinWidth(final String minWidth) {
        setStyleAttributePixel(MIN_WIDTH, minWidth);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if (this != start) {
            return super.get(name, start);
        }

        Scriptable prototype = getPrototype();
        while (prototype != null) {
            final Object value = prototype.get(name, start);
            if (value != Scriptable.NOT_FOUND) {
                return value;
            }
            prototype = prototype.getPrototype();
        }

        final Definition style = StyleAttributes.getDefinition(name, getBrowserVersion());
        if (style != null) {
            return getStyleAttribute(style);
        }

        return super.get(name, start);
    }

    @Override
    public Object get(final int index, final Scriptable start) {
        if (index < 0) {
            return Undefined.instance;
        }

        final int size = getStyleMap().size();
        if (index >= size) {
            if (getBrowserVersion().hasFeature(JS_STYLE_WRONG_INDEX_RETURNS_UNDEFINED)) {
                return Undefined.instance;
            }
            return "";
        }
        return getStyleMap().keySet().toArray(new String[size])[index];
    }

    /**
     * Get the value for the style attribute.
     * @param style the style
     * @return the value
     */
    public String getStyleAttribute(final Definition style) {
        return getStyleAttribute(style.getAttributeName());
    }

    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (this != start) {
            super.put(name, start, value);
            return;
        }

        final Scriptable prototype = getPrototype();
        if (prototype != null && !"constructor".equals(name)) {
            if (prototype.get(name, start) != Scriptable.NOT_FOUND) {
                prototype.put(name, start, value);
                return;
            }
        }

        if (getDomNodeOrNull() != null) { // check if prototype or not
            final Definition style = StyleAttributes.getDefinition(name, getBrowserVersion());
            if (style != null) {
                final String stringValue = Context.toString(value);
                setStyleAttribute(style.getAttributeName(), stringValue);
                return;
            }
        }

        super.put(name, start, value);
    }

    @Override
    public boolean has(final String name, final Scriptable start) {
        if (this != start) {
            return super.has(name, start);
        }

        final Definition style = StyleAttributes.getDefinition(name, getBrowserVersion());
        if (style != null) {
            return true;
        }

        return super.has(name, start);
    }

    @Override
    public Object[] getIds() {
        final List<Object> ids = new ArrayList<>();
        for (final Definition styleAttribute : StyleAttributes.getDefinitions(getBrowserVersion())) {
            ids.add(styleAttribute.getPropertyName());
        }
        final Object[] normalIds = super.getIds();
        for (final Object o : normalIds) {
            if (!ids.contains(o)) {
                ids.add(o);
            }
        }
        return ids.toArray();
    }

    /**
     * Sets the {@code msImeAlign} style attribute.
     * @param msImeAlign the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setMsImeAlign(final String msImeAlign) {
        setStyleAttribute(Definition.MS_IME_ALIGN.getAttributeName(), msImeAlign);
    }

    /**
     * Gets the {@code opacity} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getOpacity() {
        final String opacity = getStyleAttribute(OPACITY);
        if (opacity == null || opacity.isEmpty()) {
            return "";
        }

        final String trimedOpacity = opacity.trim();
        try {
            final float value = Float.parseFloat(trimedOpacity);
            if (value % 1 == 0) {
                return Integer.toString((int) value);
            }
            return Float.toString(value);
        }
        catch (final NumberFormatException e) {
            // ignore wrong value
        }
        return "";
    }

    /**
     * Sets the {@code opacity} style attribute.
     * @param opacity the new attribute
     */
    @JsxSetter
    public void setOpacity(final String opacity) {
        if (opacity.isEmpty()) {
            setStyleAttribute(OPACITY, opacity);
        }

        final String trimedOpacity = opacity.trim();
        try {
            Float.parseFloat(trimedOpacity);
            setStyleAttribute(OPACITY, trimedOpacity);
        }
        catch (final NumberFormatException e) {
            // ignore wrong value
        }
    }

    /**
     * Gets the {@code outline} style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getOutline() {
        return getStyleAttribute(OUTLINE);
    }

    /**
     * Sets the {@code outline} style attribute.
     * @param outline the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setOutline(final String outline) {
        setStyleAttribute(OUTLINE, outline);
    }

    /**
     * Gets the {@code outlineWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getOutlineWidth() {
        return getStyleAttribute(OUTLINE_WIDTH);
    }

    /**
     * Sets the {@code outlineWidth} style attribute.
     * @param outlineWidth the new attribute
     */
    @JsxSetter
    public void setOutlineWidth(final String outlineWidth) {
        if (getBrowserVersion().hasFeature(CSS_SUPPORTS_BEHAVIOR_PROPERTY)) {
            if (!outlineWidth.endsWith("%")) {
                setStyleAttributePixel(OUTLINE_WIDTH, outlineWidth);
            }
        }
        else if (outlineWidth.endsWith("px") || outlineWidth.endsWith("em") || outlineWidth.endsWith("mm")) {
            setStyleAttributePixel(OUTLINE_WIDTH, outlineWidth);
        }
    }

    /**
     * Gets the {@code padding} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPadding() {
        return getStyleAttribute(PADDING);
    }

    /**
     * Sets the {@code padding} style attribute.
     * @param padding the new attribute
     */
    @JsxSetter
    public void setPadding(final String padding) {
        setStyleAttribute(PADDING, padding);
    }

    /**
     * Gets the {@code paddingBottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingBottom() {
        return getStyleAttribute(PADDING_BOTTOM, PADDING, Shorthand.BOTTOM);
    }

    /**
     * Sets the {@code paddingBottom} style attribute.
     * @param paddingBottom the new attribute
     */
    @JsxSetter
    public void setPaddingBottom(final String paddingBottom) {
        setStyleAttributePixel(PADDING_BOTTOM, paddingBottom);
    }

    /**
     * Gets the {@code paddingLeft} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingLeft() {
        return getStyleAttribute(PADDING_LEFT, PADDING, Shorthand.LEFT);
    }

    /**
     * Sets the {@code paddingLeft} style attribute.
     * @param paddingLeft the new attribute
     */
    @JsxSetter
    public void setPaddingLeft(final String paddingLeft) {
        setStyleAttributePixel(PADDING_LEFT, paddingLeft);
    }

    /**
     * Gets the {@code paddingRight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingRight() {
        return getStyleAttribute(PADDING_RIGHT, PADDING, Shorthand.RIGHT);
    }

    /**
     * Sets the {@code paddingRight} style attribute.
     * @param paddingRight the new attribute
     */
    @JsxSetter
    public void setPaddingRight(final String paddingRight) {
        setStyleAttributePixel(PADDING_RIGHT, paddingRight);
    }

    /**
     * Gets the {@code paddingTop} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingTop() {
        return getStyleAttribute(PADDING_TOP, PADDING, Shorthand.TOP);
    }

    /**
     * Sets the {@code paddingTop} style attribute.
     * @param paddingTop the new attribute
     */
    @JsxSetter
    public void setPaddingTop(final String paddingTop) {
        setStyleAttributePixel(PADDING_TOP, paddingTop);
    }

    /**
     * Gets the {@code page} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(value = FF, maxVersion = 38))
    public String getPage() {
        return getStyleAttribute(PAGE);
    }

    /**
     * Sets the {@code page} style attribute.
     * @param page the new attribute
     */
    @JsxSetter(@WebBrowser(value = FF, maxVersion = 38))
    public void setPage(final String page) {
        setStyleAttribute(PAGE, page);
    }

    /**
     * Gets the {@code pixelBottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPixelBottom() {
        return pixelValue(getBottom());
    }

    /**
     * Sets the {@code pixelBottom} style attribute.
     * @param pixelBottom the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPixelBottom(final int pixelBottom) {
        setBottom(pixelBottom + "px");
    }

    /**
     * Gets the {@code pixelHeight} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPixelHeight() {
        return pixelValue(getHeight());
    }

    /**
     * Sets the {@code pixelHeight} style attribute.
     * @param pixelHeight the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPixelHeight(final int pixelHeight) {
        setHeight(pixelHeight + "px");
    }

    /**
     * Gets the {@code pixelLeft} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPixelLeft() {
        return pixelValue(getLeft());
    }

    /**
     * Sets the {@code pixelLeft} style attribute.
     * @param pixelLeft the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPixelLeft(final int pixelLeft) {
        setLeft(pixelLeft + "px");
    }

    /**
     * Gets the {@code pixelRight} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPixelRight() {
        return pixelValue(getRight());
    }

    /**
     * Sets the {@code pixelRight} style attribute.
     * @param pixelRight the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPixelRight(final int pixelRight) {
        setRight(pixelRight + "px");
    }

    /**
     * Gets the {@code pixelTop} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPixelTop() {
        return pixelValue(getTop());
    }

    /**
     * Sets the {@code pixelTop} style attribute.
     * @param pixelTop the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPixelTop(final int pixelTop) {
        setTop(pixelTop + "px");
    }

    /**
     * Gets the {@code pixelWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPixelWidth() {
        return pixelValue(getWidth());
    }

    /**
     * Sets the {@code pixelWidth} style attribute.
     * @param pixelWidth the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPixelWidth(final int pixelWidth) {
        setWidth(pixelWidth + "px");
    }

    /**
     * Gets the {@code posBottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosBottom() {
        return 0;
    }

    /**
     * Sets the {@code posBottom} style attribute.
     * @param posBottom the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosBottom(final int posBottom) {
        // Empty.
    }

    /**
     * Gets the {@code posHeight} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosHeight() {
        return 0;
    }

    /**
     * Sets the {@code posHeight} style attribute.
     * @param posHeight the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosHeight(final int posHeight) {
        // Empty.
    }

    /**
     * Gets the {@code posLeft} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosLeft() {
        return 0;
    }

    /**
     * Sets the {@code posLeft} style attribute.
     * @param posLeft the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosLeft(final int posLeft) {
        // Empty.
    }

    /**
     * Gets the {@code posRight} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosRight() {
        return 0;
    }

    /**
     * Sets the {@code posRight} style attribute.
     * @param posRight the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosRight(final int posRight) {
        // Empty.
    }

    /**
     * Gets the {@code posTop} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosTop() {
        return 0;
    }

    /**
     * Sets the {@code posTop} style attribute.
     * @param posTop the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosTop(final int posTop) {
        // Empty.
    }

    /**
     * Gets the {@code posWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosWidth() {
        return 0;
    }

    /**
     * Sets the {@code posWidth} style attribute.
     * @param posWidth the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosWidth(final int posWidth) {
        // Empty.
    }

    /**
     * Gets the {@code right} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getRight() {
        return getStyleAttribute(RIGHT);
    }

    /**
     * Sets the {@code right} style attribute.
     * @param right the new attribute
     */
    @JsxSetter
    public void setRight(final String right) {
        setStyleAttributePixel(RIGHT, right);
    }

    /**
     * Gets the {@code rubyAlign} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getRubyAlign() {
        return getStyleAttribute(RUBY_ALIGN);
    }

    /**
     * Sets the {@code rubyAlign} style attribute.
     * @param rubyAlign the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setRubyAlign(final String rubyAlign) {
        setStyleAttribute(RUBY_ALIGN, rubyAlign);
    }

    /**
     * Gets the {@code size} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(value = FF, maxVersion = 38))
    public String getSize() {
        return getStyleAttribute(SIZE);
    }

    /**
     * Sets the {@code size} style attribute.
     * @param size the new attribute
     */
    @JsxSetter(@WebBrowser(value = FF, maxVersion = 38))
    public void setSize(final String size) {
        setStyleAttribute(SIZE, size);
    }

    /**
     * Gets the {@code textDecorationBlink} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getTextDecorationBlink() {
        return false;
    }

    /**
     * Sets the {@code textDecorationBlink} style attribute.
     * @param textDecorationBlink the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextDecorationBlink(final boolean textDecorationBlink) {
        // Empty.
    }

    /**
     * Gets the {@code textDecorationLineThrough} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getTextDecorationLineThrough() {
        return false;
    }

    /**
     * Sets the {@code textDecorationLineThrough} style attribute.
     * @param textDecorationLineThrough the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextDecorationLineThrough(final boolean textDecorationLineThrough) {
        // Empty.
    }

    /**
     * Gets the {@code textDecorationNone} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getTextDecorationNone() {
        return false;
    }

    /**
     * Sets the {@code textDecorationNone} style attribute.
     * @param textDecorationNone the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextDecorationNone(final boolean textDecorationNone) {
        // Empty.
    }

    /**
     * Gets the {@code textDecorationOverline} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getTextDecorationOverline() {
        return false;
    }

    /**
     * Sets the {@code textDecorationOverline} style attribute.
     * @param textDecorationOverline the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextDecorationOverline(final boolean textDecorationOverline) {
        // Empty.
    }

    /**
     * Gets the {@code textDecorationUnderline} style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getTextDecorationUnderline() {
        return false;
    }

    /**
     * Sets the {@code textDecorationUnderline} style attribute.
     * @param textDecorationUnderline the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextDecorationUnderline(final boolean textDecorationUnderline) {
        // Empty.
    }

    /**
     * Gets the {@code textIndent} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getTextIndent() {
        return getStyleAttribute(TEXT_INDENT);
    }

    /**
     * Sets the {@code textIndent} style attribute.
     * @param textIndent the new attribute
     */
    @JsxSetter
    public void setTextIndent(final String textIndent) {
        setStyleAttributePixel(TEXT_INDENT, textIndent);
    }

    /**
     * Gets the {@code top} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getTop() {
        return getStyleAttribute(TOP);
    }

    /**
     * Sets the {@code top} style attribute.
     * @param top the new attribute
     */
    @JsxSetter
    public void setTop(final String top) {
        setStyleAttributePixel(TOP, top);
    }

    /**
     * Gets the {@code verticalAlign} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getVerticalAlign() {
        return getStyleAttribute(VERTICAL_ALIGN);
    }

    /**
     * Sets the {@code verticalAlign} style attribute.
     * @param verticalAlign the new attribute
     */
    @JsxSetter
    public void setVerticalAlign(final String verticalAlign) {
        setStyleAttributePixel(VERTICAL_ALIGN, verticalAlign);
    }

    /**
     * Gets the {@code width} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getWidth() {
        return getStyleAttribute(WIDTH);
    }

    /**
     * Sets the {@code width} style attribute.
     * @param width the new attribute
     */
    @JsxSetter
    public void setWidth(final String width) {
        setStyleAttributePixel(WIDTH, width);
    }

    /**
     * Gets the {@code wordSpacing} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getWordSpacing() {
        return getStyleAttribute(WORD_SPACING);
    }

    /**
     * Sets the {@code wordSpacing} style attribute.
     * @param wordSpacing the new attribute
     */
    @JsxSetter
    public void setWordSpacing(final String wordSpacing) {
        if (!wordSpacing.endsWith("%") || getBrowserVersion().hasFeature(JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT)) {
            setStyleAttributePixel(WORD_SPACING, wordSpacing);
        }
    }

    /**
     * Gets the {@code zIndex} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public Object getZIndex() {
        final String value = getStyleAttribute(Z_INDEX);
        if (getBrowserVersion().hasFeature(CSS_ZINDEX_TYPE_INTEGER)) {
            try {
                return Integer.valueOf(value);
            }
            catch (final NumberFormatException e) {
                return "";
            }
        }

        // zIndex is string
        try {
            Integer.parseInt(value);
            return value;
        }
        catch (final NumberFormatException e) {
            return "";
        }
    }

    /**
     * Sets the {@code zIndex} style attribute.
     * @param zIndex the new attribute
     */
    @JsxSetter
    public void setZIndex(final Object zIndex) {
        // empty
        if (zIndex == null || StringUtils.isEmpty(zIndex.toString())) {
            setStyleAttribute(Z_INDEX, "");
            return;
        }
        // undefined
        if (Context.getUndefinedValue().equals(zIndex)) {
            return;
        }

        // string
        if (zIndex instanceof Number) {
            final Number number = (Number) zIndex;
            if (number.doubleValue() % 1 == 0) {
                setStyleAttribute(Z_INDEX, Integer.toString(number.intValue()));
            }
            return;
        }
        try {
            final int i = Integer.parseInt(zIndex.toString());
            setStyleAttribute(Z_INDEX, Integer.toString(i));
        }
        catch (final NumberFormatException e) {
            // ignore
        }
    }

    /**
     * Gets the value of the specified property of the style.
     * @param name the style property name
     * @return empty string if nothing found
     */
    @JsxFunction
    public String getPropertyValue(final String name) {
        if (name != null && name.contains("-")) {
            final Object value = getProperty(this, camelize(name));
            if (value instanceof String) {
                return (String) value;
            }
        }
        return getStyleAttribute(name);
    }

    /**
     * Gets the CSS property value.
     * @param name the name of the property to retrieve
     * @return the value
     */
    @JsxFunction(@WebBrowser(FF))
    public CSSValue getPropertyCSSValue(final String name) {
        LOG.info("getPropertyCSSValue(" + name + "): getPropertyCSSValue support is experimental");
        // following is a hack, just to have basic support for getPropertyCSSValue
        // TODO: rework the whole CSS processing here! we should *always* parse the style!
        if (styleDeclaration_ == null) {
            final String uri = getDomNodeOrDie().getPage().getWebResponse().getWebRequest()
                    .getUrl().toExternalForm();
            final String styleAttribute = jsElement_.getDomNodeOrDie().getAttribute("style");
            final InputSource source = new InputSource(new StringReader(styleAttribute));
            source.setURI(uri);
            final ErrorHandler errorHandler = getWindow().getWebWindow().getWebClient().getCssErrorHandler();
            final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
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
        final String cssText = cssValue.getCssText();
        if (cssText.startsWith("rgb(")) {
            final String formatedCssText = StringUtils.replace(cssText, ",", ", ");
            cssValue.setCssText(formatedCssText);
        }

        return new CSSPrimitiveValue(jsElement_, (org.w3c.dom.css.CSSPrimitiveValue) cssValue);
    }

    /**
     * Gets the value of the specified property of the style.
     * @param name the style property name
     * @return empty string if nothing found
     */
    @JsxFunction
    public String getPropertyPriority(final String name) {
        return getStylePriority(name);
    }

    /**
     * Sets the value of the specified property.
     *
     * @param name the name of the attribute
     * @param value the value to assign to the attribute
     * @param important may be null
     */
    @JsxFunction
    public void setProperty(final String name, final String value, final String important) {
        if (StringUtils.isEmpty(important) || "null".equals(important)) {
            setStyleAttribute(name, value, "");
        }
        if (getBrowserVersion().hasFeature(JS_STYLE_SET_PROPERTY_IMPORTANT_IGNORES_CASE)) {
            if (PRIORITY_IMPORTANT.equalsIgnoreCase(important)) {
                setStyleAttribute(name, value, PRIORITY_IMPORTANT);
            }
        }
        else {
            if (PRIORITY_IMPORTANT.equals(important)) {
                setStyleAttribute(name, value, PRIORITY_IMPORTANT);
            }
        }
    }

    /**
     * Removes the named property.
     * @param name the name of the property to remove
     * @return the value deleted
     */
    @JsxFunction
    public String removeProperty(final Object name) {
        return removeStyleAttribute(Context.toString(name));
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
    @JsxFunction(@WebBrowser(IE))
    public Object getAttribute(final String name, final int flag) {
        // Case-insensitive.
        final StyleElement style = getStyleElementCaseInSensitive(name);
        if (null == style) {
            return "";
        }
        return style.getValue();
    }

    /**
     * Sets the value of the specified attribute. This method exists only in IE.
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536739.aspx">MSDN Documentation</a>
     * @param name the name of the attribute
     * @param value the value to assign to the attribute
     * @param flag 0 for case insensitive, 1 (default) for case sensitive
     */
    @JsxFunction(@WebBrowser(IE))
    public void setAttribute(final String name, final String value, final Object flag) {
        // Case-insensitive.
        final StyleElement style = getStyleElementCaseInSensitive(name);
        if (null != style) {
            setStyleAttribute(style.getName(), value);
        }
    }

    /**
     * Removes the specified attribute. This method exists only in IE.
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536696.aspx">MSDN Documentation</a>
     * @param name the name of the attribute
     * @param flag 0 for case insensitive, 1 (default) for case sensitive
     * @return {@code true} if the attribute was successfully removed, {@code false} otherwise
     */
    @JsxFunction(@WebBrowser(IE))
    public boolean removeAttribute(final String name, final Object flag) {
        // Case-insensitive.
        final StyleElement style = getStyleElementCaseInSensitive(name);
        if (style != null) {
            removeStyleAttribute(style.getName());
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
    protected static int pixelValue(final Element element, final CssValue value) {
        return pixelValue(element, value, false);
    }

    private static int pixelValue(final Element element, final CssValue value, final boolean percentMode) {
        final String s = value.get(element);
        if (s.endsWith("%") || (s.isEmpty() && element instanceof HTMLHtmlElement)) {
            final int i = NumberUtils.toInt(TO_INT_PATTERN.matcher(s).replaceAll("$1"), 100);
            final Element parent = element.getParentElement();
            final int absoluteValue = (parent == null)
                            ? value.getWindowDefaultValue() : pixelValue(parent, value, true);
            return (int) ((i / 100D) * absoluteValue);
        }
        if ("auto".equals(s)) {
            return value.getDefaultValue();
        }
        if (s.isEmpty()) {
            if (element instanceof HTMLCanvasElement) {
                return value.getWindowDefaultValue();
            }

            // if the call was originated from a percent value we have to go up until
            // we can provide some kind of base value for percent calculation
            if (percentMode) {
                final Element parent = element.getParentElement();
                if (parent == null || parent instanceof HTMLHtmlElement) {
                    return value.getWindowDefaultValue();
                }
                return pixelValue(parent, value, true);
            }

            return 0;
        }
        return pixelValue(s);
    }

    /**
     * Converts the specified length string value into an integer number of pixels. This method does
     * <b>NOT</b> handle percentages correctly; use {@link #pixelValue(Element, CssValue)} if you
     * need percentage support).
     * @param value the length string value to convert to an integer number of pixels
     * @return the integer number of pixels corresponding to the specified length string value
     * @see <a href="http://htmlhelp.com/reference/css/units.html">CSS Units</a>
     * @see #pixelValue(Element, CssValue)
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
        public final String get(final Element element) {
            final ComputedCSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
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
    public static class StyleElement implements Comparable<StyleElement>, Serializable {
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
     * Sets the style attribute which should be treated as an integer in pixels.
     * @param name the attribute name
     * @param value the attribute value
     */
    protected void setStyleAttributePixel(final String name, String value) {
        if (value.endsWith("px")) {
            value = value.substring(0, value.length() - 2);
        }
        try {
            final float floatValue = Float.parseFloat(value);
            if (floatValue % 1 == 0) {
                value = Integer.toString((int) floatValue) + "px";
            }
            else {
                value = Float.toString(floatValue) + "px";
            }
        }
        catch (final Exception e) {
            //ignore
        }
        setStyleAttribute(name, value);
    }
}
