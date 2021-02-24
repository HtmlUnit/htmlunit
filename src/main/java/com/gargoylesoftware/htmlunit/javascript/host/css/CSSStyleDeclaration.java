/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_BACKGROUND_INITIAL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_BACKGROUND_RGBA;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_LENGTH_INITIAL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_OUTLINE_WIDTH_UNIT_NOT_REQUIRED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_SET_NULL_THROWS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_SUPPORTS_BEHAVIOR_PROPERTY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_VERTICAL_ALIGN_SUPPORTS_AUTO;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_ZINDEX_TYPE_INTEGER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_UNSUPPORTED_PROPERTY_GETTER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_WRONG_INDEX_RETURNS_UNDEFINED;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.ACCELERATOR;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BACKGROUND;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BACKGROUND_ATTACHMENT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BACKGROUND_COLOR;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BACKGROUND_IMAGE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BACKGROUND_POSITION;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.BACKGROUND_REPEAT;
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
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.LETTER_SPACING;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.LINE_HEIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MARGIN;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MARGIN_BOTTOM;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MARGIN_LEFT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MARGIN_RIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MARGIN_TOP;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MAX_HEIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MAX_WIDTH;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MIN_HEIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.MIN_WIDTH;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.OPACITY;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.ORPHANS;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.OUTLINE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.OUTLINE_WIDTH;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PADDING;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PADDING_BOTTOM;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PADDING_LEFT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PADDING_RIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.PADDING_TOP;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.POSITION;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.RIGHT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.RUBY_ALIGN;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.SIZE;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.TEXT_INDENT;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.TOP;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.VERTICAL_ALIGN;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.WIDOWS;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.WIDTH;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.WORD_SPACING;
import static com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition.Z_INDEX_;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

import java.awt.Color;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.css.StyleElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
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
 * @author Dennis Duysak
 */
@JsxClass
public class CSSStyleDeclaration extends SimpleScriptable {
    private static final Pattern TO_FLOAT_PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?).*");
    private static final Pattern URL_PATTERN =
        Pattern.compile("url\\(\\s*[\"']?(.*?)[\"']?\\s*\\)");
    private static final Pattern POSITION_PATTERN =
        Pattern.compile("(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex))\\s*"
                + "(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|top|bottom|center)");
    private static final Pattern POSITION_PATTERN2 =
        Pattern.compile("(left|right|center)\\s*(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|top|bottom|center)");
    private static final Pattern POSITION_PATTERN3 =
        Pattern.compile("(top|bottom|center)\\s*(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|left|right|center)");

    private static final Set<String> LENGTH_PROPERTIES_FFFF = new HashSet<>(Arrays.asList(
            BORDER_TOP_WIDTH.getAttributeName(),
            BORDER_LEFT_WIDTH.getAttributeName(),
            BORDER_BOTTOM_WIDTH.getAttributeName(),
            BORDER_RIGHT_WIDTH.getAttributeName(),
            LETTER_SPACING.getAttributeName()));

    private static final Set<String> LENGTH_PROPERTIES_TTFF = new HashSet<>(Arrays.asList(
            HEIGHT.getAttributeName(),
            WIDTH.getAttributeName(),
            TOP.getAttributeName(),
            LEFT.getAttributeName(),
            BOTTOM.getAttributeName(),
            RIGHT.getAttributeName(),
            MARGIN_TOP.getAttributeName(),
            MARGIN_LEFT.getAttributeName(),
            MARGIN_BOTTOM.getAttributeName(),
            MARGIN_RIGHT.getAttributeName(),
            MIN_HEIGHT.getAttributeName(),
            MIN_WIDTH.getAttributeName()
            ));

    private static final Set<String> LENGTH_PROPERTIES_FTFF = new HashSet<>(Arrays.asList(
            FONT_SIZE.getAttributeName(),
            TEXT_INDENT.getAttributeName(),
            PADDING_TOP.getAttributeName(),
            PADDING_LEFT.getAttributeName(),
            PADDING_BOTTOM.getAttributeName(),
            PADDING_RIGHT.getAttributeName(),
            MAX_HEIGHT.getAttributeName(),
            MAX_WIDTH.getAttributeName()
            ));

    static final String NONE = "none";
    static final String AUTO = "auto";
    static final String STATIC = "static";
    static final String INHERIT = "inherit";
    private static final String INITIAL = "initial";
    static final String RELATIVE = "relative";
    static final String FIXED = "fixed";
    static final String ABSOLUTE = "absolute";
    private static final String REPEAT = "repeat";
    static final String BLOCK = "block";
    static final String INLINE = "inline";

    private static final Log LOG = LogFactory.getLog(CSSStyleDeclaration.class);
    private static final Map<String, String> CSSColors_ = new HashMap<>();

    private static final Map<String, String> CamelizeCache_
            = Collections.synchronizedMap(new HashMap<String, String>());

    /** Used to parse URLs. */
    private static final MessageFormat URL_FORMAT = new MessageFormat("url({0})");

    /** The element to which this style belongs. */
    private Element jsElement_;

    /** The wrapped CSSStyleDeclaration (if created from CSSStyleRule). */
    private CSSStyleDeclarationImpl styleDeclaration_;

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
    @JsxConstructor({CHROME, EDGE, FF, FF78})
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
    CSSStyleDeclaration(final Scriptable parentScope, final CSSStyleDeclarationImpl styleDeclaration) {
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
        styleDeclaration_ = styleDeclaration;
    }

    /**
     * Initializes the object.
     * @param htmlElement the element that this style describes
     */
    private void initialize(final Element element) {
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
                    final Object[] url;
                    synchronized (URL_FORMAT) {
                        url = URL_FORMAT.parse(behavior);
                    }
                    if (url.length > 0) {
                        htmlElement.addBehavior((String) url[0]);
                    }
                }
                catch (final ParseException e) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Invalid behavior: '" + behavior + "'.");
                    }
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
    protected Object getWithPreemption(final String name) {
        if (getBrowserVersion().hasFeature(JS_STYLE_UNSUPPORTED_PROPERTY_GETTER) && null != jsElement_) {
            final StyleElement element = getStyleElement(name);
            if (element != null && element.getValue() != null) {
                return element.getValue();
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
        if (jsElement_ == null) {
            return null;
        }
        return jsElement_.getDomNodeOrDie().getStyleElement(name);
    }

    /**
     * Determines the StyleElement for the given name.
     * This ignores the case of the name.
     *
     * @param name the name of the requested StyleElement
     * @return the StyleElement or null if not found
     */
    private StyleElement getStyleElementCaseInSensitive(final String name) {
        if (jsElement_ == null) {
            return null;
        }
        return jsElement_.getDomNodeOrDie().getStyleElementCaseInSensitive(name);
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
        if (styleDeclaration_ == null) {
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
            else {
                if (element1.compareTo(element2) > 0) {
                    return element1.getValue();
                }
                value = element2.getValue();
            }
        }
        else {
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

        final String[] values = StringUtils.split(value);
        if (name1.name().contains("TOP")) {
            if (values.length > 0) {
                return values[0];
            }
            return "";
        }
        else if (name1.name().contains("RIGHT")) {
            if (values.length > 1) {
                return values[1];
            }
            else if (values.length > 0) {
                return values[0];
            }
            return "";
        }
        else if (name1.name().contains("BOTTOM")) {
            if (values.length > 2) {
                return values[2];
            }
            else if (values.length > 0) {
                return values[0];
            }
            return "";
        }
        else if (name1.name().contains("LEFT")) {
            if (values.length > 3) {
                return values[3];
            }
            else if (values.length > 1) {
                return values[1];
            }
            else if (values.length > 0) {
                return values[0];
            }
            else {
                return "";
            }
        }
        else {
            throw new IllegalStateException("Unsupported definition: " + name1);
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
        if (null == newValue || "null".equals(newValue)) {
            newValue = "";
        }
        if (styleDeclaration_ != null) {
            styleDeclaration_.setProperty(name, newValue, important);
            return;
        }

        jsElement_.getDomNodeOrDie().replaceStyleAttribute(name, newValue, important);
    }

    /**
     * Removes the specified style attribute, returning the value of the removed attribute.
     * @param name the attribute name (delimiter-separated, not camel-cased)
     */
    private String removeStyleAttribute(final String name) {
        if (null != styleDeclaration_) {
            return styleDeclaration_.removeProperty(name);
        }

        return jsElement_.getDomNodeOrDie().removeStyleAttribute(name);
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
        return jsElement_.getDomNodeOrDie().getStyleMap();
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
     * Gets the {@code accelerator} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public String getAccelerator() {
        return defaultIfEmpty(getStyleAttribute(ACCELERATOR), "false");
    }

    /**
     * Sets the {@code accelerator} style attribute.
     * @param accelerator the new attribute
     */
    @JsxSetter(IE)
    public void setAccelerator(final String accelerator) {
        setStyleAttribute(ACCELERATOR.getAttributeName(), accelerator);
    }

    /**
     * Gets the {@code backgroundAttachment} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundAttachment() {
        String value = getStyleAttribute(BACKGROUND_ATTACHMENT, false);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND);
            if (StringUtils.isNotBlank(bg)) {
                value = findAttachment(bg);
                if (value == null) {
                    if (getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL)
                            && getClass() == CSSStyleDeclaration.class) {
                        return INITIAL;
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
        setStyleAttribute(BACKGROUND_ATTACHMENT.getAttributeName(), backgroundAttachment);
    }

    /**
     * Gets the {@code backgroundColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundColor() {
        String value = getStyleAttribute(BACKGROUND_COLOR, false);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND, false);
            if (StringUtils.isBlank(bg)) {
                return "";
            }
            value = findColor(bg);
            if (value == null) {
                if (getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL)) {
                    if (getClass() == CSSStyleDeclaration.class) {
                        return INITIAL;
                    }
                    return "rgba(0, 0, 0, 0)";
                }
                if (getBrowserVersion().hasFeature(CSS_BACKGROUND_RGBA)) {
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
        setStyleAttribute(BACKGROUND_COLOR.getAttributeName(), backgroundColor);
    }

    /**
     * Gets the {@code backgroundImage} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundImage() {
        String value = getStyleAttribute(BACKGROUND_IMAGE, false);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND, false);
            if (StringUtils.isNotBlank(bg)) {
                value = findImageUrl(bg);
                final boolean isComputed = getClass() != CSSStyleDeclaration.class;
                final boolean backgroundInitial = getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL);
                if (value == null) {
                    return backgroundInitial && !isComputed ? INITIAL : NONE;
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
        setStyleAttribute(BACKGROUND_IMAGE.getAttributeName(), backgroundImage);
    }

    /**
     * Gets the {@code backgroundPosition} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundPosition() {
        String value = getStyleAttribute(BACKGROUND_POSITION, false);
        if (value == null) {
            return null;
        }
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND, false);
            if (bg == null) {
                return null;
            }
            if (StringUtils.isNotBlank(bg)) {
                value = findPosition(bg);
                final boolean isInitial = getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL);
                final boolean isComputed = getClass() != CSSStyleDeclaration.class;
                if (value == null) {
                    if (isInitial) {
                        return isComputed ? "" : INITIAL;
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
                if (isComputed) {
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
        setStyleAttribute(BACKGROUND_POSITION.getAttributeName(), backgroundPosition);
    }

    /**
     * Gets the {@code backgroundRepeat} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundRepeat() {
        String value = getStyleAttribute(BACKGROUND_REPEAT, false);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND, false);
            if (StringUtils.isNotBlank(bg)) {
                value = findRepeat(bg);
                if (value == null) {
                    if (getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL)
                            && getClass() == CSSStyleDeclaration.class) {
                        return INITIAL;
                    }
                    return REPEAT; // default if shorthand is used
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
        setStyleAttribute(BACKGROUND_REPEAT.getAttributeName(), backgroundRepeat);
    }

    /**
     * Gets the {@code borderBottomColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
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
    @JsxSetter
    public void setBorderBottomColor(final String borderBottomColor) {
        setStyleAttribute(BORDER_BOTTOM_COLOR.getAttributeName(), borderBottomColor);
    }

    /**
     * Gets the {@code borderBottomStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
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
    @JsxSetter
    public void setBorderBottomStyle(final String borderBottomStyle) {
        setStyleAttribute(BORDER_BOTTOM_STYLE.getAttributeName(), borderBottomStyle);
    }

    /**
     * Gets the {@code borderBottomWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderBottomWidth() {
        return getBorderWidth(BORDER_BOTTOM_WIDTH, BORDER_BOTTOM);
    }

    /**
     * Sets the {@code borderBottomWidth} style attribute.
     * @param borderBottomWidth the new attribute
     */
    @JsxSetter
    public void setBorderBottomWidth(final Object borderBottomWidth) {
        setStyleLengthAttribute(BORDER_BOTTOM_WIDTH.getAttributeName(), borderBottomWidth, "",
                false, false, false, false);
    }

    /**
     * Gets the {@code borderLeftColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
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
    @JsxSetter
    public void setBorderLeftColor(final String borderLeftColor) {
        setStyleAttribute(BORDER_LEFT_COLOR.getAttributeName(), borderLeftColor);
    }

    /**
     * Gets the {@code borderLeftStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
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
    @JsxSetter
    public void setBorderLeftStyle(final String borderLeftStyle) {
        setStyleAttribute(BORDER_LEFT_STYLE.getAttributeName(), borderLeftStyle);
    }

    /**
     * Gets the {@code borderLeftWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
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
    @JsxSetter
    public void setBorderLeftWidth(final Object borderLeftWidth) {
        setStyleLengthAttribute(BORDER_LEFT_WIDTH.getAttributeName(), borderLeftWidth, "",
                false, false, false, false);
    }

    /**
     * Gets the {@code borderRightColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
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
    @JsxSetter
    public void setBorderRightColor(final String borderRightColor) {
        setStyleAttribute(BORDER_RIGHT_COLOR.getAttributeName(), borderRightColor);
    }

    /**
     * Gets the {@code borderRightStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
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
    @JsxSetter
    public void setBorderRightStyle(final String borderRightStyle) {
        setStyleAttribute(BORDER_RIGHT_STYLE.getAttributeName(), borderRightStyle);
    }

    /**
     * Gets the {@code borderRightWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderRightWidth() {
        return getBorderWidth(BORDER_RIGHT_WIDTH, BORDER_RIGHT);
    }

    /**
     * Sets the {@code borderRightWidth} style attribute.
     * @param borderRightWidth the new attribute
     */
    @JsxSetter
    public void setBorderRightWidth(final Object borderRightWidth) {
        setStyleLengthAttribute(BORDER_RIGHT_WIDTH.getAttributeName(), borderRightWidth, "",
                false, false, false, false);
    }

    /**
     * Sets the {@code borderTop} style attribute.
     * @param borderTop the new attribute
     */
    @JsxSetter
    public void setBorderTop(final String borderTop) {
        setStyleAttribute(BORDER_TOP.getAttributeName(), borderTop);
    }

    /**
     * Gets the {@code borderTopColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
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
    @JsxSetter
    public void setBorderTopColor(final String borderTopColor) {
        setStyleAttribute(BORDER_TOP_COLOR.getAttributeName(), borderTopColor);
    }

    /**
     * Gets the {@code borderTopStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
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
    @JsxSetter
    public void setBorderTopStyle(final String borderTopStyle) {
        setStyleAttribute(BORDER_TOP_STYLE.getAttributeName(), borderTopStyle);
    }

    /**
     * Gets the {@code borderTopWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderTopWidth() {
        return getBorderWidth(BORDER_TOP_WIDTH, BORDER_TOP);
    }

    /**
     * Sets the {@code borderTopWidth} style attribute.
     * @param borderTopWidth the new attribute
     */
    @JsxSetter
    public void setBorderTopWidth(final Object borderTopWidth) {
        setStyleLengthAttribute(BORDER_TOP_WIDTH.getAttributeName(), borderTopWidth, "",
                false, false, false, false);
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
    public void setBottom(final Object bottom) {
        setStyleLengthAttribute(BOTTOM.getAttributeName(), bottom, "", true, true, false, false);
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
        setStyleAttribute(COLOR.getAttributeName(), color);
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
        if (styleDeclaration_ != null) {
            final String text = styleDeclaration_.getCssText();
            if (styleDeclaration_.getLength() > 0) {
                return text + ";";
            }
            return text;
        }
        return jsElement_.getDomNodeOrDie().getAttributeDirect("style");
    }

    /**
     * Sets the actual text of the style.
     * @param value the new text
     */
    @JsxSetter
    public void setCssText(final String value) {
        if (styleDeclaration_ != null) {
            styleDeclaration_.setCssText(value);
            return;
        }
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
        setStyleAttribute(DISPLAY.getAttributeName(), display);
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
    public void setFontSize(final Object fontSize) {
        setStyleLengthAttribute(FONT_SIZE.getAttributeName(), fontSize, "", false, true, false, false);
        updateFont(getFont(), false);
    }

    /**
     * Gets the {@code lineHeight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getLineHeight() {
        return getStyleAttribute(LINE_HEIGHT);
    }

    /**
     * Sets the {@code lineHeight} style attribute.
     * @param lineHeight the new attribute
     */
    @JsxSetter
    public void setLineHeight(final String lineHeight) {
        setStyleAttribute(LINE_HEIGHT.getAttributeName(), lineHeight);
        updateFont(getFont(), false);
    }

    /**
     * Gets the {@code fontFamily} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFontFamily() {
        return getStyleAttribute(FONT_FAMILY);
    }

    /**
     * Sets the {@code fontFamily} style attribute.
     * @param fontFamily the new attribute
     */
    @JsxSetter
    public void setFontFamily(final String fontFamily) {
        setStyleAttribute(FONT_FAMILY.getAttributeName(), fontFamily);
        updateFont(getFont(), false);
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
                if (lineHeight.equals(defaultLineHeight)) {
                    newFont.append(LINE_HEIGHT.getDefaultComputedValue(browserVersion));
                }
                else {
                    newFont.append(lineHeight);
                }
            }

            newFont.append(' ').append(getFontFamily());
            setStyleAttribute(FONT.getAttributeName(), newFont.toString());
        }
    }

    /**
     * Gets the {@code font} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFont() {
        return getStyleAttribute(FONT);
    }

    /**
     * Sets the {@code font} style attribute.
     * @param font the new attribute
     */
    @JsxSetter
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
    public void setHeight(final Object height) {
        setStyleLengthAttribute(HEIGHT.getAttributeName(), height, "", true, true, false, false);
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
    public void setLeft(final Object left) {
        setStyleLengthAttribute(LEFT.getAttributeName(), left, "", true, true, false, false);
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
    public void setLetterSpacing(final Object letterSpacing) {
        setStyleLengthAttribute(LETTER_SPACING.getAttributeName(), letterSpacing, "",
                false, false, false, false);
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
        setStyleAttribute(MARGIN.getAttributeName(), margin);
    }

    /**
     * Gets the {@code marginBottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginBottom() {
        return getStyleAttribute(MARGIN_BOTTOM, MARGIN);
    }

    /**
     * Sets the {@code marginBottom} style attribute.
     * @param marginBottom the new attribute
     */
    @JsxSetter
    public void setMarginBottom(final Object marginBottom) {
        setStyleLengthAttribute(MARGIN_BOTTOM.getAttributeName(), marginBottom, "", true, true, false, false);
    }

    /**
     * Gets the {@code marginLeft} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginLeft() {
        return getStyleAttribute(MARGIN_LEFT, MARGIN);
    }

    /**
     * Sets the {@code marginLeft} style attribute.
     * @param marginLeft the new attribute
     */
    @JsxSetter
    public void setMarginLeft(final Object marginLeft) {
        setStyleLengthAttribute(MARGIN_LEFT.getAttributeName(), marginLeft, "", true, true, false, false);
    }

    /**
     * Gets the {@code marginRight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginRight() {
        return getStyleAttribute(MARGIN_RIGHT, MARGIN);
    }

    /**
     * Sets the {@code marginRight} style attribute.
     * @param marginRight the new attribute
     */
    @JsxSetter
    public void setMarginRight(final Object marginRight) {
        setStyleLengthAttribute(MARGIN_RIGHT.getAttributeName(), marginRight, "", true, true, false, false);
    }

    /**
     * Gets the {@code marginTop} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginTop() {
        return getStyleAttribute(MARGIN_TOP, MARGIN);
    }

    /**
     * Sets the {@code marginTop} style attribute.
     * @param marginTop the new attribute
     */
    @JsxSetter
    public void setMarginTop(final Object marginTop) {
        setStyleLengthAttribute(MARGIN_TOP.getAttributeName(), marginTop, "", true, true, false, false);
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
    public void setMaxHeight(final Object maxHeight) {
        setStyleLengthAttribute(MAX_HEIGHT.getAttributeName(), maxHeight, "", false, true, false, false);
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
    public void setMaxWidth(final Object maxWidth) {
        setStyleLengthAttribute(MAX_WIDTH.getAttributeName(), maxWidth, "", false, true, false, false);
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
    public void setMinHeight(final Object minHeight) {
        setStyleLengthAttribute(MIN_HEIGHT.getAttributeName(), minHeight, "", true, true, false, false);
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
    public void setMinWidth(final Object minWidth) {
        setStyleLengthAttribute(MIN_WIDTH.getAttributeName(), minWidth, "", true, true, false, false);
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

        final Map<String, StyleElement> style = getStyleMap();
        final int size = style.size();
        if (index >= size) {
            if (getBrowserVersion().hasFeature(JS_STYLE_WRONG_INDEX_RETURNS_UNDEFINED)) {
                return Undefined.instance;
            }
            return "";
        }
        return style.keySet().toArray(new String[size])[index];
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
            if (!value.contains("url")) {
                return value.toLowerCase(Locale.ROOT);
            }
            return value;
        }
        return "";
    }

    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (this != start) {
            super.put(name, start, value);
            return;
        }

        final Scriptable prototype = getPrototype();
        if (prototype != null && !"constructor".equals(name) && prototype.get(name, start) != Scriptable.NOT_FOUND) {
            prototype.put(name, start, value);
            return;
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
    @JsxSetter(IE)
    public void setMsImeAlign(final String msImeAlign) {
        setStyleAttribute(Definition.MS_IME_ALIGN.getAttributeName(), msImeAlign);
    }

    /**
     * Gets the {@code opacity} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getOpacity() {
        final String opacity = getStyleAttribute(OPACITY, false);
        if (opacity == null || opacity.isEmpty()) {
            return "";
        }

        final String trimedOpacity = opacity.trim();
        try {
            final double value = Double.parseDouble(trimedOpacity);
            if (value % 1 == 0) {
                return Integer.toString((int) value);
            }
            return Double.toString(value);
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
    public void setOpacity(final Object opacity) {
        if (ScriptRuntime.isNaN(opacity)) {
            return;
        }

        final double doubleValue;
        if (opacity instanceof Number) {
            doubleValue = ((Number) opacity).doubleValue();
        }
        else {
            String valueString = Context.toString(opacity);

            if (valueString.isEmpty()) {
                setStyleAttribute(OPACITY.getAttributeName(), valueString);
                return;
            }

            valueString = valueString.trim();
            try {
                doubleValue = Double.parseDouble(valueString);
            }
            catch (final NumberFormatException e) {
                // ignore wrong value
                return;
            }
        }

        if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
            return;
        }
        setStyleAttribute(OPACITY.getAttributeName(), Double.toString(doubleValue));
    }

    /**
     * Gets the {@code outline} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getOutline() {
        return getStyleAttribute(OUTLINE);
    }

    /**
     * Sets the {@code outline} style attribute.
     * @param outline the new attribute
     */
    @JsxSetter
    public void setOutline(final String outline) {
        setStyleAttribute(OUTLINE.getAttributeName(), outline);
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
    public void setOutlineWidth(final Object outlineWidth) {
        final boolean requiresUnit = !getBrowserVersion().hasFeature(CSS_OUTLINE_WIDTH_UNIT_NOT_REQUIRED);
        setStyleLengthAttribute(OUTLINE_WIDTH.getAttributeName(), outlineWidth, "",
                false, false, true, requiresUnit);
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
        setStyleAttribute(PADDING.getAttributeName(), padding);
    }

    /**
     * Gets the {@code paddingBottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingBottom() {
        return getStyleAttribute(PADDING_BOTTOM, PADDING);
    }

    /**
     * Sets the {@code paddingBottom} style attribute.
     * @param paddingBottom the new attribute
     */
    @JsxSetter
    public void setPaddingBottom(final Object paddingBottom) {
        setStyleLengthAttribute(PADDING_BOTTOM.getAttributeName(), paddingBottom, "", false, true, false, false);
    }

    /**
     * Gets the {@code paddingLeft} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingLeft() {
        return getStyleAttribute(PADDING_LEFT, PADDING);
    }

    /**
     * Sets the {@code paddingLeft} style attribute.
     * @param paddingLeft the new attribute
     */
    @JsxSetter
    public void setPaddingLeft(final Object paddingLeft) {
        setStyleLengthAttribute(PADDING_LEFT.getAttributeName(), paddingLeft, "", false, true, false, false);
    }

    /**
     * Gets the {@code paddingRight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingRight() {
        return getStyleAttribute(PADDING_RIGHT, PADDING);
    }

    /**
     * Sets the {@code paddingRight} style attribute.
     * @param paddingRight the new attribute
     */
    @JsxSetter
    public void setPaddingRight(final Object paddingRight) {
        setStyleLengthAttribute(PADDING_RIGHT.getAttributeName(), paddingRight, "", false, true, false, false);
    }

    /**
     * Gets the {@code paddingTop} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingTop() {
        return getStyleAttribute(PADDING_TOP, PADDING);
    }

    /**
     * Sets the {@code paddingTop} style attribute.
     * @param paddingTop the new attribute
     */
    @JsxSetter
    public void setPaddingTop(final Object paddingTop) {
        setStyleLengthAttribute(PADDING_TOP.getAttributeName(), paddingTop, "", false, true, false, false);
    }

    /**
     * Gets the {@code pixelBottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPixelBottom() {
        return pixelValue(getBottom());
    }

    /**
     * Sets the {@code pixelBottom} style attribute.
     * @param pixelBottom the new attribute
     */
    @JsxSetter(IE)
    public void setPixelBottom(final int pixelBottom) {
        setBottom(pixelBottom + "px");
    }

    /**
     * Gets the {@code pixelHeight} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPixelHeight() {
        return pixelValue(getHeight());
    }

    /**
     * Sets the {@code pixelHeight} style attribute.
     * @param pixelHeight the new attribute
     */
    @JsxSetter(IE)
    public void setPixelHeight(final int pixelHeight) {
        setHeight(pixelHeight + "px");
    }

    /**
     * Gets the {@code pixelLeft} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPixelLeft() {
        return pixelValue(getLeft());
    }

    /**
     * Sets the {@code pixelLeft} style attribute.
     * @param pixelLeft the new attribute
     */
    @JsxSetter(IE)
    public void setPixelLeft(final int pixelLeft) {
        setLeft(pixelLeft + "px");
    }

    /**
     * Gets the {@code pixelRight} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPixelRight() {
        return pixelValue(getRight());
    }

    /**
     * Sets the {@code pixelRight} style attribute.
     * @param pixelRight the new attribute
     */
    @JsxSetter(IE)
    public void setPixelRight(final int pixelRight) {
        setRight(pixelRight + "px");
    }

    /**
     * Gets the {@code pixelTop} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPixelTop() {
        return pixelValue(getTop());
    }

    /**
     * Sets the {@code pixelTop} style attribute.
     * @param pixelTop the new attribute
     */
    @JsxSetter(IE)
    public void setPixelTop(final int pixelTop) {
        setTop(pixelTop + "px");
    }

    /**
     * Gets the {@code pixelWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPixelWidth() {
        return pixelValue(getWidth());
    }

    /**
     * Sets the {@code pixelWidth} style attribute.
     * @param pixelWidth the new attribute
     */
    @JsxSetter(IE)
    public void setPixelWidth(final int pixelWidth) {
        setWidth(pixelWidth + "px");
    }

    /**
     * Gets the {@code posBottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPosBottom() {
        return 0;
    }

    /**
     * Sets the {@code posBottom} style attribute.
     * @param posBottom the new attribute
     */
    @JsxSetter(IE)
    public void setPosBottom(final int posBottom) {
        // Empty.
    }

    /**
     * Gets the {@code posHeight} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPosHeight() {
        return 0;
    }

    /**
     * Sets the {@code posHeight} style attribute.
     * @param posHeight the new attribute
     */
    @JsxSetter(IE)
    public void setPosHeight(final int posHeight) {
        // Empty.
    }

    /**
     * Gets the {@code posLeft} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPosLeft() {
        return 0;
    }

    /**
     * Sets the {@code posLeft} style attribute.
     * @param posLeft the new attribute
     */
    @JsxSetter(IE)
    public void setPosLeft(final int posLeft) {
        // Empty.
    }

    /**
     * Gets the {@code posRight} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPosRight() {
        return 0;
    }

    /**
     * Sets the {@code posRight} style attribute.
     * @param posRight the new attribute
     */
    @JsxSetter(IE)
    public void setPosRight(final int posRight) {
        // Empty.
    }

    /**
     * Gets the {@code posTop} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPosTop() {
        return 0;
    }

    /**
     * Sets the {@code posTop} style attribute.
     * @param posTop the new attribute
     */
    @JsxSetter(IE)
    public void setPosTop(final int posTop) {
        // Empty.
    }

    /**
     * Gets the {@code posWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public int getPosWidth() {
        return 0;
    }

    /**
     * Sets the {@code posWidth} style attribute.
     * @param posWidth the new attribute
     */
    @JsxSetter(IE)
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
    public void setRight(final Object right) {
        setStyleLengthAttribute(RIGHT.getAttributeName(), right, "", true, true, false, false);
    }

    /**
     * Gets the {@code rubyAlign} style attribute.
     * @return the style attribute
     */
    @JsxGetter({IE, FF, FF78})
    public String getRubyAlign() {
        return getStyleAttribute(RUBY_ALIGN);
    }

    /**
     * Sets the {@code rubyAlign} style attribute.
     * @param rubyAlign the new attribute
     */
    @JsxSetter({IE, FF, FF78})
    public void setRubyAlign(final String rubyAlign) {
        setStyleAttribute(RUBY_ALIGN.getAttributeName(), rubyAlign);
    }

    /**
     * Gets the {@code size} style attribute.
     * @return the style attribute
     */
    @JsxGetter({CHROME, EDGE})
    public String getSize() {
        return getStyleAttribute(SIZE);
    }

    /**
     * Sets the {@code size} style attribute.
     * @param size the new attribute
     */
    @JsxSetter({CHROME, EDGE})
    public void setSize(final String size) {
        setStyleAttribute(SIZE.getAttributeName(), size);
    }

    /**
     * Gets the {@code textDecorationBlink} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public boolean isTextDecorationBlink() {
        return false;
    }

    /**
     * Sets the {@code textDecorationBlink} style attribute.
     * @param textDecorationBlink the new attribute
     */
    @JsxSetter(IE)
    public void setTextDecorationBlink(final boolean textDecorationBlink) {
        // Empty.
    }

    /**
     * Gets the {@code textDecorationLineThrough} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public boolean isTextDecorationLineThrough() {
        return false;
    }

    /**
     * Sets the {@code textDecorationLineThrough} style attribute.
     * @param textDecorationLineThrough the new attribute
     */
    @JsxSetter(IE)
    public void setTextDecorationLineThrough(final boolean textDecorationLineThrough) {
        // Empty.
    }

    /**
     * Gets the {@code textDecorationNone} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public boolean isTextDecorationNone() {
        return false;
    }

    /**
     * Sets the {@code textDecorationNone} style attribute.
     * @param textDecorationNone the new attribute
     */
    @JsxSetter(IE)
    public void setTextDecorationNone(final boolean textDecorationNone) {
        // Empty.
    }

    /**
     * Gets the {@code textDecorationOverline} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public boolean isTextDecorationOverline() {
        return false;
    }

    /**
     * Sets the {@code textDecorationOverline} style attribute.
     * @param textDecorationOverline the new attribute
     */
    @JsxSetter(IE)
    public void setTextDecorationOverline(final boolean textDecorationOverline) {
        // Empty.
    }

    /**
     * Gets the {@code textDecorationUnderline} style attribute.
     * @return the style attribute
     */
    @JsxGetter(IE)
    public boolean getTextDecorationUnderline() {
        return false;
    }

    /**
     * Sets the {@code textDecorationUnderline} style attribute.
     * @param textDecorationUnderline the new attribute
     */
    @JsxSetter(IE)
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
    public void setTextIndent(final Object textIndent) {
        setStyleLengthAttribute(TEXT_INDENT.getAttributeName(), textIndent, "", false, true, false, false);
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
    public void setTop(final Object top) {
        setStyleLengthAttribute(TOP.getAttributeName(), top, "", true, true, false, false);
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
    public void setVerticalAlign(final Object verticalAlign) {
        final boolean auto = getBrowserVersion().hasFeature(CSS_VERTICAL_ALIGN_SUPPORTS_AUTO);
        setStyleLengthAttribute(VERTICAL_ALIGN.getAttributeName(), verticalAlign, "", auto, true, false, false);
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
    public void setWidth(final Object width) {
        setStyleLengthAttribute(WIDTH.getAttributeName(), width, "", true, true, false, false);
    }

    /**
     * Gets the {@code widows} style attribute.
     * @return the style attribute
     */
    @JsxGetter({CHROME, EDGE, IE})
    public String getWidows() {
        return getStyleAttribute(WIDOWS);
    }

    /**
     * Sets the {@code widows} style attribute.
     * @param widows the new attribute
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setWidows(final String widows) {
        if (getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL)) {
            try {
                if (Integer.parseInt(widows) <= 0) {
                    return;
                }
            }
            catch (final NumberFormatException e) {
                return;
            }
        }
        setStyleAttribute(WIDOWS.getAttributeName(), widows);
    }

    /**
     * Gets the {@code orphans} style attribute.
     * @return the style attribute
     */
    @JsxGetter({CHROME, EDGE, IE})
    public String getOrphans() {
        return getStyleAttribute(ORPHANS);
    }

    /**
     * Sets the {@code orphans} style attribute.
     * @param orphans the new attribute
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setOrphans(final String orphans) {
        if (getBrowserVersion().hasFeature(CSS_BACKGROUND_INITIAL)) {
            try {
                if (Integer.parseInt(orphans) <= 0) {
                    return;
                }
            }
            catch (final NumberFormatException e) {
                return;
            }
        }
        setStyleAttribute(ORPHANS.getAttributeName(), orphans);
    }

    /**
     * Gets the {@code position} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPosition() {
        return getStyleAttribute(POSITION);
    }

    /**
     * Sets the {@code position} style attribute.
     * @param position the new attribute
     */
    @JsxSetter
    public void setPosition(final String position) {
        if (position.isEmpty() || STATIC.equalsIgnoreCase(position) || ABSOLUTE.equalsIgnoreCase(position)
                || FIXED.equalsIgnoreCase(position) || RELATIVE.equalsIgnoreCase(position)
                || INITIAL.equalsIgnoreCase(position) || INHERIT.equalsIgnoreCase(position)) {
            setStyleAttribute(POSITION.getAttributeName(), position.toLowerCase(Locale.ROOT));
        }
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
    public void setWordSpacing(final Object wordSpacing) {
        setStyleLengthAttribute(WORD_SPACING.getAttributeName(), wordSpacing, "",
                false, getBrowserVersion().hasFeature(JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT), false, false);
    }

    /**
     * Gets the {@code zIndex} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public Object getZIndex() {
        final String value = getStyleAttribute(Z_INDEX_);
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
            setStyleAttribute(Z_INDEX_.getAttributeName(), "");
            return;
        }
        // undefined
        if (Undefined.isUndefined(zIndex)) {
            return;
        }

        // string
        if (zIndex instanceof Number) {
            final Number number = (Number) zIndex;
            if (number.doubleValue() % 1 == 0) {
                setStyleAttribute(Z_INDEX_.getAttributeName(), Integer.toString(number.intValue()));
            }
            return;
        }
        try {
            final int i = Integer.parseInt(zIndex.toString());
            setStyleAttribute(Z_INDEX_.getAttributeName(), Integer.toString(i));
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
        return getStyleAttributeImpl(name);
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
    public void setProperty(final String name, final Object value, final String important) {
        String imp = "";
        if (!StringUtils.isEmpty(important) && !"null".equals(important)) {
            if (!StyleElement.PRIORITY_IMPORTANT.equalsIgnoreCase(important)) {
                return;
            }
            imp = StyleElement.PRIORITY_IMPORTANT;
        }

        if (LENGTH_PROPERTIES_FFFF.contains(name)) {
            setStyleLengthAttribute(name, value, imp, false, false, false, false);
        }
        else if (LENGTH_PROPERTIES_TTFF.contains(name)) {
            setStyleLengthAttribute(name, value, imp, true, true, false, false);
        }
        else if (LENGTH_PROPERTIES_FTFF.contains(name)) {
            setStyleLengthAttribute(name, value, imp, false, true, false, false);
        }
        else if (OUTLINE_WIDTH.getAttributeName().equals(name)) {
            final boolean requiresUnit = !getBrowserVersion().hasFeature(CSS_OUTLINE_WIDTH_UNIT_NOT_REQUIRED);
            setStyleLengthAttribute(OUTLINE_WIDTH.getAttributeName(), value, imp, false, false, true, requiresUnit);
        }
        else if (WORD_SPACING.getAttributeName().equals(name)) {
            setStyleLengthAttribute(WORD_SPACING.getAttributeName(), value, imp,
                    false, getBrowserVersion().hasFeature(JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT), false, false);
        }
        else if (VERTICAL_ALIGN.getAttributeName().equals(name)) {
            final boolean auto = getBrowserVersion().hasFeature(CSS_VERTICAL_ALIGN_SUPPORTS_AUTO);
            setStyleLengthAttribute(VERTICAL_ALIGN.getAttributeName(), value, imp, auto, true, false, false);
        }
        else {
            setStyleAttribute(name, Context.toString(value), imp);
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
    @JsxFunction(IE)
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
    @JsxFunction(IE)
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
    @JsxFunction(IE)
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
        if (text.contains(REPEAT)) {
            return REPEAT;
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
        if (text.contains(FIXED)) {
            return FIXED;
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
        return NONE.equalsIgnoreCase(token) || "hidden".equalsIgnoreCase(token)
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
                Double.parseDouble(token);
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
            final float i = NumberUtils.toFloat(TO_FLOAT_PATTERN.matcher(s).replaceAll("$1"), 100);

            final Element parent = element.getParentElement();
            final int absoluteValue = (parent == null)
                            ? value.getWindowDefaultValue() : pixelValue(parent, value, true);
            return  Math.round((i / 100f) * absoluteValue);
        }
        if (AUTO.equals(s)) {
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
        float i = NumberUtils.toFloat(TO_FLOAT_PATTERN.matcher(value).replaceAll("$1"), 0);
        if (value.length() < 2) {
            return Math.round(i);
        }
        if (value.endsWith("px")) {
            return Math.round(i);
        }

        if (value.endsWith("em")) {
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
        return Math.round(i);
    }

    @Override
    protected boolean isReadOnlySettable(final String name, final Object value) {
        if ("length".equals(name)) {
            return false; //ignore
        }
        return super.isReadOnlySettable(name, value);
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
            return get(style);
        }

        /**
         * Returns the CSS attribute value from the specified computed style.
         * @param style the computed style from which to retrieve the CSS attribute value
         * @return the CSS attribute value from the specified computed style
         */
        public abstract String get(ComputedCSSStyleDeclaration style);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (jsElement_ == null) {
            return "CSSStyleDeclaration for 'null'"; // for instance on prototype
        }
        final String style = jsElement_.getDomNodeOrDie().getAttributeDirect("style");
        return "CSSStyleDeclaration for '" + style + "'";
    }

    /**
     * Sets the style attribute which should be treated as an integer in pixels.
     * @param name the attribute name
     * @param value the attribute value
     * @param important important value
     * @param auto true if auto is supported
     * @param thinMedThick thin, medium, thick are supported
     * @param unitRequired unit is required
     * @param perecent true if percent is supported
     */
    private void setStyleLengthAttribute(final String name, final Object value, final String important,
                final boolean auto, final boolean percent, final boolean thinMedThick, final boolean unitRequired) {

        if (ScriptRuntime.isNaN(value)) {
            return;
        }

        final double doubleValue;
        String unit = "px";
        if (value instanceof Number) {
            if (unitRequired) {
                return;
            }
            doubleValue = ((Number) value).doubleValue();
        }
        else {
            String valueString = Context.toString(value);
            if (null == value) {
                valueString = "";
            }

            if (StringUtils.isEmpty(valueString)) {
                setStyleAttribute(name, valueString, important);
                return;
            }

            if ((auto && AUTO.equals(valueString))
                    || INITIAL.equals(valueString) && getBrowserVersion().hasFeature(CSS_LENGTH_INITIAL)
                    || INHERIT.equals(valueString)) {
                setStyleAttribute(name, valueString, important);
                return;
            }

            if (thinMedThick && "thin".equals(valueString)
                    || "medium".equals(valueString)
                    || "thick".equals(valueString)) {
                setStyleAttribute(name, valueString, important);
                return;
            }

            if (percent && valueString.endsWith("%")) {
                unit = valueString.substring(valueString.length() - 1);
                valueString = valueString.substring(0, valueString.length() - 1);
            }
            else if (valueString.endsWith("px")
                || valueString.endsWith("em")
                || valueString.endsWith("ex")
                || valueString.endsWith("pt")
                || valueString.endsWith("cm")
                || valueString.endsWith("mm")
                || valueString.endsWith("in")
                || valueString.endsWith("pc")
                || valueString.endsWith("ch")
                || valueString.endsWith("vh")
                || valueString.endsWith("vw")) {
                unit = valueString.substring(valueString.length() - 2);
                valueString = valueString.substring(0, valueString.length() - 2);
            }
            else if (valueString.endsWith("rem")
                || valueString.endsWith("vmin")
                || valueString.endsWith("vmax")) {
                unit = valueString.substring(valueString.length() - 3);
                valueString = valueString.substring(0, valueString.length() - 3);
            }
            else if (unitRequired) {
                return;
            }

            doubleValue = Context.toNumber(valueString);
        }

        try {
            if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                return;
            }

            final String valueString;
            if (doubleValue % 1 == 0) {
                valueString = Integer.toString((int) doubleValue) + unit;
            }
            else {
                valueString = Double.toString(doubleValue) + unit;
            }

            setStyleAttribute(name, valueString, important);
        }
        catch (final Exception e) {
            //ignore
        }
    }
}
