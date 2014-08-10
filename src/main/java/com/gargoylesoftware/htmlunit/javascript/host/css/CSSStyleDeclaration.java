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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_IMAGE_URL_QUOTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_PIXEL_VALUES_INT_ONLY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_SET_NULL_THROWS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_SUPPORTS_BEHAVIOR_PROPERTY;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_ZINDEX_TYPE_INTEGER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_ZINDEX_TYPE_NUMBER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_ZINDEX_UNDEFINED_FORCES_RESET;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_ZINDEX_UNDEFINED_OR_NULL_THROWS_ERROR;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_GET_BACKGROUND_COLOR_FOR_COMPUTED_STYLE_AS_RGB;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OPACITY_ACCEPTS_ARBITRARY_VALUES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_GET_ATTRIBUTE_SUPPORTS_FLAGS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_REMOVE_ATTRIBUTE_SUPPORTS_FLAGS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_SET_ATTRIBUTE_SUPPORTS_FLAGS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_STYLE_UNSUPPORTED_PROPERTY_GETTER;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.WrappedException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;

import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
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
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass
public class CSSStyleDeclaration extends SimpleScriptable implements ScriptableWithFallbackGetter {
    /** Css important property constant. */
    protected static final String PRIORITY_IMPORTANT = "important";

    private static final String ACCELERATOR = "accelerator";
    private static final String BACKGROUND = "background";
    private static final String BACKGROUND_ATTACHMENT = "background-attachment";
    private static final String BACKGROUND_COLOR = "background-color";
    private static final String BACKGROUND_IMAGE = "background-image";
    private static final String BACKGROUND_POSITION = "background-position";
    private static final String BACKGROUND_POSITION_X = "background-position-x";
    private static final String BACKGROUND_POSITION_Y = "background-position-y";
    private static final String BACKGROUND_REPEAT = "background-repeat";
    private static final String BEHAVIOR = "behavior";
    private static final String BORDER = "border";
    private static final String BORDER_BOTTOM = "border-bottom";
    private static final String BORDER_BOTTOM_COLOR = "border-bottom-color";
    private static final String BORDER_BOTTOM_STYLE = "border-bottom-style";
    private static final String BORDER_BOTTOM_WIDTH = "border-bottom-width";
    private static final String BORDER_COLLAPSE = "border-collapse";
    private static final String BORDER_COLOR = "border-color";
    private static final String BORDER_LEFT = "border-left";
    private static final String BORDER_LEFT_COLOR = "border-left-color";
    private static final String BORDER_LEFT_STYLE = "border-left-style";
    private static final String BORDER_WIDTH = "border-width";
    private static final String BORDER_LEFT_WIDTH = "border-left-width";
    private static final String BORDER_RIGHT = "border-right";
    private static final String BORDER_RIGHT_COLOR = "border-right-color";
    private static final String BORDER_RIGHT_STYLE = "border-right-style";
    private static final String BORDER_RIGHT_WIDTH = "border-right-width";
    private static final String BORDER_SPACING = "border-spacing";
    private static final String BORDER_STYLE = "border-style";
    private static final String BORDER_TOP = "border-top";
    private static final String BORDER_TOP_COLOR = "border-top-color";
    private static final String BORDER_TOP_STYLE = "border-top-style";
    private static final String BORDER_TOP_WIDTH = "border-top-width";
    private static final String BOTTOM = "bottom";
    private static final String BOX_SIZING = "box-sizing";
    private static final String CAPTION_SIDE = "caption-side";
    private static final String CLEAR = "clear";
    private static final String CLIP = "clip";
    private static final String COLOR = "color";
    private static final String CONTENT = "content";
    private static final String COUNTER_INCREMENT = "counter-increment";
    private static final String COUNTER_RESET = "counter-reset";
    private static final String CURSOR = "cursor";
    private static final String DIRECTION = "direction";
    private static final String DISPLAY = "display";
    private static final String EMPTY_CELLS = "empty-cells";
    private static final String FONT = "font";
    private static final String FONT_FAMILY = "font-family";
    private static final String FONT_SIZE = "font-size";
    private static final String FONT_SIZE_ADJUST = "font-size-adjust";
    private static final String FONT_STRETCH = "font-stretch";
    private static final String FONT_STYLE = "font-style";
    private static final String FONT_VARIANT = "font-variant";
    private static final String FONT_WEIGHT = "font-weight";
    private static final String HEIGHT = "height";
    private static final String IME_MODE = "ime-mode";
    private static final String LAYOUT_FLOW = "layout-flow";
    private static final String LAYOUT_GRID = "layout-grid";
    private static final String LAYOUT_GRID_CHAR = "layout-grid-char";
    private static final String LAYOUT_GRID_LINE = "layout-grid-line";
    private static final String LAYOUT_GRID_MODE = "layout-grid-mode";
    private static final String LAYOUT_GRID_TYPE = "layout-grid-type";
    private static final String LEFT = "left";
    private static final String LETTER_SPACING = "letter-spacing";
    private static final String LINE_BREAK = "line-break";
    private static final String LIST_STYLE = "list-style";
    private static final String LIST_STYLE_IMAGE = "list-style-image";
    private static final String LIST_STYLE_POSITION = "list-style-position";
    private static final String LIST_STYLE_TYPE = "list-style-type";
    private static final String MARGIN_BOTTOM = "margin-bottom";
    private static final String MARGIN_LEFT = "margin-left";
    private static final String MARGIN_RIGHT = "margin-right";
    private static final String MARGIN = "margin";
    private static final String MARGIN_TOP = "margin-top";
    private static final String MARKER_OFFSET = "marker-offset";
    private static final String MARKS = "marks";
    private static final String MAX_HEIGHT = "max-height";
    private static final String MAX_WIDTH = "max-width";
    private static final String MIN_HEIGHT = "min-height";
    private static final String MIN_WIDTH = "min-width";
    private static final String MS_BLOCK_PROGRESSION = "ms-block-progression";
    private static final String MS_INTERPOLATION_MODE = "ms-interpolation-mode";
    private static final String OPACITY = "opacity";
    private static final String ORPHANS = "orphans";
    private static final String OUTLINE = "outline";
    private static final String OUTLINE_COLOR = "outline-color";
    private static final String OUTLINE_OFFSET = "outline-offset";
    private static final String OUTLINE_STYLE = "outline-style";
    private static final String OUTLINE_WIDTH = "outline-width";
    private static final String OVERFLOW = "overflow";
    private static final String OVERFLOW_X = "overflow-x";
    private static final String OVERFLOW_Y = "overflow-y";
    private static final String PADDING_BOTTOM = "padding-bottom";
    private static final String PADDING_LEFT = "padding-left";
    private static final String PADDING_RIGHT = "padding-right";
    private static final String PADDING = "padding";
    private static final String PADDING_TOP = "padding-top";
    private static final String PAGE = "page";
    private static final String PAGE_BREAK_AFTER = "page-break-after";
    private static final String PAGE_BREAK_BEFORE = "page-break-before";
    private static final String PAGE_BREAK_INSIDE = "page-break-inside";
    private static final String POINTER_EVENTS = "pointer-events";
    private static final String POSITION = "position";
    private static final String RIGHT = "right";
    private static final String RUBY_ALIGN = "ruby-align";
    private static final String RUBY_OVERHANG = "ruby-overhang";
    private static final String RUBY_POSITION = "ruby-position";
    private static final String SCROLLBAR3D_LIGHT_COLOR = "scrollbar3d-light-color";
    private static final String SCROLLBAR_ARROW_COLOR = "scrollbar-arrow-color";
    private static final String SCROLLBAR_BASE_COLOR = "scrollbar-base-color";
    private static final String SCROLLBAR_DARK_SHADOW_COLOR = "scrollbar-dark-shadow-color";
    private static final String SCROLLBAR_FACE_COLOR = "scrollbar-face-color";
    private static final String SCROLLBAR_HIGHLIGHT_COLOR = "scrollbar-highlight-color";
    private static final String SCROLLBAR_SHADOW_COLOR = "scrollbar-shadow-color";
    private static final String SCROLLBAR_TRACK_COLOR = "scrollbar-track-color";
    private static final String SIZE = "size";
    private static final String FLOAT = "float";
    private static final String TABLE_LAYOUT = "table-layout";
    private static final String TEXT_ALIGN = "text-align";
    private static final String TEXT_ALIGN_LAST = "text-align-last";
    private static final String TEXT_AUTOSPACE = "text-autospace";
    private static final String TEXT_DECORATION = "text-decoration";
    private static final String TEXT_INDENT = "text-indent";
    private static final String TEXT_JUSTIFY = "text-justify";
    private static final String TEXT_JUSTIFY_TRIM = "text-justify-trim";
    private static final String TEXT_KASHIDA = "text-kashida";
    private static final String TEXT_KASHIDA_SPACE = "text-kashida-space";
    private static final String TEXT_OVERFLOW = "text-overflow";
    private static final String TEXT_SHADOW = "text-shadow";
    private static final String TEXT_TRANSFORM = "text-transform";
    private static final String TEXT_UNDERLINE_POSITION = "text-underline-position";
    private static final String TOP = "top";
    private static final String VERTICAL_ALIGN = "vertical-align";
    private static final String VISIBILITY = "visibility";
    private static final String WHITE_SPACE = "white-space";
    private static final String WIDOWS = "widows";
    private static final String WORD_SPACING = "word-spacing";
    private static final String WORD_WRAP = "word-wrap";
    private static final String WRITING_MODE = "writing-mode";
    private static final String Z_INDEX = "z-index";
    private static final String ZOOM = "zoom";

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
    private static final Map<String, String> CSSColors_ = new HashMap<String, String>();

    // use plain old hashtable because this is synchronized and does not introduce one more
    // indirection layer (hope this is a bit faster)
    // we only need the get/set api so there is no difference at all
    private static final Hashtable<String, String> CamelizeCache_ = new Hashtable<String, String>();

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
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public CSSStyleDeclaration() {
        // Empty.
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
            return element.getValue();
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
        return getStyleMap().get(name);
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

            if (element1 == null && element2 == null) {
                return "";
            }
            if (element1 != null && element2 == null) {
                return element1.getValue();
            }
            if (element1 == null && element2 != null) {
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
    protected void setStyleAttribute(final String name, String newValue) {
        if ("null".equals(newValue)) {
            if (getBrowserVersion().hasFeature(CSS_SET_NULL_THROWS)) {
                //Context.throwAsScriptRuntimeEx(new Exception("Invalid argument."));
            }
            newValue = "";
        }
        if (styleDeclaration_ != null) {
            styleDeclaration_.setProperty(name, newValue, null);
            return;
        }

        replaceStyleAttribute(name, newValue);
    }

    /**
     * Replaces the value of the named style attribute. If there is no style attribute with the
     * specified name, a new one is added. If the specified value is an empty (or all whitespace)
     * string, this method actually removes the named style attribute.
     * @param name the attribute name (delimiter-separated, not camel-cased)
     * @param value the attribute value
     */
    private void replaceStyleAttribute(final String name, final String value) {
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
            final StyleElement element = new StyleElement(name, value, index);
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
        final String styleAttribute = jsElement_.getDomNodeOrDie().getAttribute("style");
        if (styleString_ == styleAttribute) {
            return styleMap_;
        }

        final Map<String, StyleElement> styleMap = new LinkedHashMap<String, StyleElement>();
        if (DomElement.ATTRIBUTE_NOT_DEFINED == styleAttribute || DomElement.ATTRIBUTE_VALUE_EMPTY == styleAttribute) {
            styleMap_ = styleMap;
            styleString_ = styleAttribute;
            return styleMap_;
        }

        for (final String token : StringUtils.split(styleAttribute, ';')) {
            final int index = token.indexOf(":");
            if (index != -1) {
                final String key = token.substring(0, index).trim().toLowerCase(Locale.ENGLISH);
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
     * Gets the "accelerator" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(value = IE, minVersion = 11))
    public String getAccelerator() {
        return defaultIfEmpty(getStyleAttribute(ACCELERATOR), "false");
    }

    /**
     * Sets the "accelerator" style attribute.
     * @param accelerator the new attribute
     */
    @JsxSetter(@WebBrowser(value = IE, minVersion = 11))
    public void setAccelerator(final String accelerator) {
        setStyleAttributePixel(ACCELERATOR, accelerator);
    }

    /**
     * Gets the "background" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackground() {
        return getStyleAttribute(BACKGROUND);
    }

    /**
     * Sets the "background" style attribute.
     * @param background the new attribute
     */
    @JsxSetter
    public void setBackground(final String background) {
        setStyleAttribute(BACKGROUND, background);
    }

    /**
     * Gets the "backgroundAttachment" style attribute.
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
                    return "scroll"; // default if shorthand is used
                }
                return value;
            }
            return "";
        }

        return value;
    }

    /**
     * Sets the "backgroundAttachment" style attribute.
     * @param backgroundAttachment the new attribute
     */
    @JsxSetter
    public void setBackgroundAttachment(final String backgroundAttachment) {
        setStyleAttribute(BACKGROUND_ATTACHMENT, backgroundAttachment);
    }

    /**
     * Gets the "backgroundColor" style attribute.
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
     * Sets the "backgroundColor" style attribute.
     * @param backgroundColor the new attribute
     */
    @JsxSetter
    public void setBackgroundColor(final String backgroundColor) {
        setStyleAttribute(BACKGROUND_COLOR, backgroundColor);
    }

    /**
     * Gets the "backgroundImage" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundImage() {
        String value = getStyleAttribute(BACKGROUND_IMAGE);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND);
            if (StringUtils.isNotBlank(bg)) {
                value = findImageUrl(bg);
                if (value == null) {
                    return "none"; // default if shorthand is used
                }
                return value;
            }
            return "";
        }

        return value;
    }

    /**
     * Sets the "backgroundImage" style attribute.
     * @param backgroundImage the new attribute
     */
    @JsxSetter
    public void setBackgroundImage(final String backgroundImage) {
        setStyleAttribute(BACKGROUND_IMAGE, backgroundImage);
    }

    /**
     * Gets the "backgroundPosition" style attribute.
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
                if (value == null) {
                    return "0% 0%"; // default if shorthand is used
                }
                return value;
            }
            return "";
        }

        return value;
    }

    /**
     * Sets the "backgroundPosition" style attribute.
     * @param backgroundPosition the new attribute
     */
    @JsxSetter
    public void setBackgroundPosition(final String backgroundPosition) {
        setStyleAttribute(BACKGROUND_POSITION, backgroundPosition);
    }

    /**
     * Gets the "backgroundPositionX" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBackgroundPositionX() {
        return getStyleAttribute(BACKGROUND_POSITION_X);
    }

    /**
     * Sets the "backgroundPositionX" style attribute.
     * @param backgroundPositionX the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBackgroundPositionX(final String backgroundPositionX) {
        setStyleAttribute(BACKGROUND_POSITION_X, backgroundPositionX);
    }

    /**
     * Gets the "backgroundPositionY" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBackgroundPositionY() {
        return getStyleAttribute(BACKGROUND_POSITION_Y);
    }

    /**
     * Sets the "backgroundPositionY" style attribute.
     * @param backgroundPositionY the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBackgroundPositionY(final String backgroundPositionY) {
        setStyleAttribute(BACKGROUND_POSITION_Y, backgroundPositionY);
    }

    /**
     * Gets the "backgroundRepeat" style attribute.
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
                    return "repeat"; // default if shorthand is used
                }
                return value;
            }
            return "";
        }

        return value;
    }

    /**
     * Sets the "backgroundRepeat" style attribute.
     * @param backgroundRepeat the new attribute
     */
    @JsxSetter
    public void setBackgroundRepeat(final String backgroundRepeat) {
        setStyleAttribute(BACKGROUND_REPEAT, backgroundRepeat);
    }

    /**
     * Gets the object's behavior (IE only).
     * @return the object's behavior
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getBehavior() {
        return getStyleAttribute(BEHAVIOR);
    }

    /**
     * Sets the object's behavior (IE only).
     * @param behavior the new behavior
     */
    @JsxSetter(@WebBrowser(value = IE, maxVersion = 8))
    public void setBehavior(final String behavior) {
        setStyleAttribute(BEHAVIOR, behavior);

        // many methods/properties need to be moved from HTMLElement to Element
        // is it the case for behavior related methods? Assuming not in a first time...
        if (!(jsElement_ instanceof HTMLElement)) {
            throw new RuntimeException("Bug! behavior can be set for Element too!!!");
        }

        final HTMLElement htmlElement = (HTMLElement) jsElement_;
        htmlElement.removeBehavior(HTMLElement.BEHAVIOR_ID_CLIENT_CAPS);
        htmlElement.removeBehavior(HTMLElement.BEHAVIOR_ID_HOMEPAGE);
        htmlElement.removeBehavior(HTMLElement.BEHAVIOR_ID_DOWNLOAD);
        if (!behavior.isEmpty()) {
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

    /**
     * Gets the "border" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorder() {
        return getStyleAttribute(BORDER);
    }

    /**
     * Sets the "border" style attribute.
     * @param border the new attribute
     */
    @JsxSetter
    public void setBorder(final String border) {
        setStyleAttribute(BORDER, border);
    }

    /**
     * Gets the "borderBottom" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderBottom() {
        return getStyleAttribute(BORDER_BOTTOM);
    }

    /**
     * Sets the "borderBottom" style attribute.
     * @param borderBottom the new attribute
     */
    @JsxSetter
    public void setBorderBottom(final String borderBottom) {
        setStyleAttribute(BORDER_BOTTOM, borderBottom);
    }

    /**
     * Gets the "borderBottomColor" style attribute.
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
     * Sets the "borderBottomColor" style attribute.
     * @param borderBottomColor the new attribute
     */
    @JsxSetter
    public void setBorderBottomColor(final String borderBottomColor) {
        setStyleAttribute(BORDER_BOTTOM_COLOR, borderBottomColor);
    }

    /**
     * Gets the "borderBottomStyle" style attribute.
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
     * Sets the "borderBottomStyle" style attribute.
     * @param borderBottomStyle the new attribute
     */
    @JsxSetter
    public void setBorderBottomStyle(final String borderBottomStyle) {
        setStyleAttribute(BORDER_BOTTOM_STYLE, borderBottomStyle);
    }

    /**
     * Gets the "borderBottomWidth" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderBottomWidth() {
        return getBorderWidth(Shorthand.BOTTOM);
    }

    /**
     * Sets the "borderBottomWidth" style attribute.
     * @param borderBottomWidth the new attribute
     */
    @JsxSetter
    public void setBorderBottomWidth(final String borderBottomWidth) {
        setStyleAttributePixel(BORDER_BOTTOM_WIDTH, borderBottomWidth);
    }

    /**
     * Gets the "borderCollapse" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderCollapse() {
        return getStyleAttribute(BORDER_COLLAPSE);
    }

    /**
     * Sets the "borderCollapse" style attribute.
     * @param borderCollapse the new attribute
     */
    @JsxSetter
    public void setBorderCollapse(final String borderCollapse) {
        setStyleAttribute(BORDER_COLLAPSE, borderCollapse);
    }

    /**
     * Gets the "borderColor" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderColor() {
        return getStyleAttribute(BORDER_COLOR);
    }

    /**
     * Sets the "borderColor" style attribute.
     * @param borderColor the new attribute
     */
    @JsxSetter
    public void setBorderColor(final String borderColor) {
        setStyleAttribute(BORDER_COLOR, borderColor);
    }

    /**
     * Gets the "borderLeft" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderLeft() {
        return getStyleAttribute(BORDER_LEFT);
    }

    /**
     * Sets the "borderLeft" style attribute.
     * @param borderLeft the new attribute
     */
    @JsxSetter
    public void setBorderLeft(final String borderLeft) {
        setStyleAttribute(BORDER_LEFT, borderLeft);
    }

    /**
     * Gets the "borderLeftColor" style attribute.
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
     * Sets the "borderLeftColor" style attribute.
     * @param borderLeftColor the new attribute
     */
    @JsxSetter
    public void setBorderLeftColor(final String borderLeftColor) {
        setStyleAttribute(BORDER_LEFT_COLOR, borderLeftColor);
    }

    /**
     * Gets the "borderLeftStyle" style attribute.
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
     * Sets the "borderLeftStyle" style attribute.
     * @param borderLeftStyle the new attribute
     */
    @JsxSetter
    public void setBorderLeftStyle(final String borderLeftStyle) {
        setStyleAttribute(BORDER_LEFT_STYLE, borderLeftStyle);
    }

    /**
     * Gets the "borderLeftWidth" style attribute.
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
     * Sets the "borderLeftWidth" style attribute.
     * @param borderLeftWidth the new attribute
     */
    @JsxSetter
    public void setBorderLeftWidth(final String borderLeftWidth) {
        setStyleAttributePixel(BORDER_LEFT_WIDTH, borderLeftWidth);
    }

    /**
     * Gets the "borderRight" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderRight() {
        return getStyleAttribute(BORDER_RIGHT);
    }

    /**
     * Sets the "borderRight" style attribute.
     * @param borderRight the new attribute
     */
    @JsxSetter
    public void setBorderRight(final String borderRight) {
        setStyleAttribute(BORDER_RIGHT, borderRight);
    }

    /**
     * Gets the "borderRightColor" style attribute.
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
     * Sets the "borderRightColor" style attribute.
     * @param borderRightColor the new attribute
     */
    @JsxSetter
    public void setBorderRightColor(final String borderRightColor) {
        setStyleAttribute(BORDER_RIGHT_COLOR, borderRightColor);
    }

    /**
     * Gets the "borderRightStyle" style attribute.
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
     * Sets the "borderRightStyle" style attribute.
     * @param borderRightStyle the new attribute
     */
    @JsxSetter
    public void setBorderRightStyle(final String borderRightStyle) {
        setStyleAttribute(BORDER_RIGHT_STYLE, borderRightStyle);
    }

    /**
     * Gets the "borderRightWidth" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderRightWidth() {
        return getBorderWidth(Shorthand.RIGHT);
    }

    /**
     * Sets the "borderRightWidth" style attribute.
     * @param borderRightWidth the new attribute
     */
    @JsxSetter
    public void setBorderRightWidth(final String borderRightWidth) {
        setStyleAttributePixel(BORDER_RIGHT_WIDTH, borderRightWidth);
    }

    /**
     * Gets the "borderSpacing" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getBorderSpacing() {
        return getStyleAttribute(BORDER_SPACING);
    }

    /**
     * Sets the "borderSpacing" style attribute.
     * @param borderSpacing the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setBorderSpacing(final String borderSpacing) {
        setStyleAttribute(BORDER_SPACING, borderSpacing);
    }

    /**
     * Gets the "borderStyle" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderStyle() {
        return getStyleAttribute(BORDER_STYLE);
    }

    /**
     * Sets the "borderStyle" style attribute.
     * @param borderStyle the new attribute
     */
    @JsxSetter
    public void setBorderStyle(final String borderStyle) {
        setStyleAttribute(BORDER_STYLE, borderStyle);
    }

    /**
     * Gets the "borderTop" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderTop() {
        return getStyleAttribute(BORDER_TOP);
    }

    /**
     * Sets the "borderTop" style attribute.
     * @param borderTop the new attribute
     */
    @JsxSetter
    public void setBorderTop(final String borderTop) {
        setStyleAttribute(BORDER_TOP, borderTop);
    }

    /**
     * Gets the "borderTopColor" style attribute.
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
     * Sets the "borderTopColor" style attribute.
     * @param borderTopColor the new attribute
     */
    @JsxSetter
    public void setBorderTopColor(final String borderTopColor) {
        setStyleAttribute(BORDER_TOP_COLOR, borderTopColor);
    }

    /**
     * Gets the "borderTopStyle" style attribute.
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
     * Sets the "borderTopStyle" style attribute.
     * @param borderTopStyle the new attribute
     */
    @JsxSetter
    public void setBorderTopStyle(final String borderTopStyle) {
        setStyleAttribute(BORDER_TOP_STYLE, borderTopStyle);
    }

    /**
     * Gets the "borderTopWidth" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderTopWidth() {
        return getBorderWidth(Shorthand.TOP);
    }

    /**
     * Sets the "borderTopWidth" style attribute.
     * @param borderTopWidth the new attribute
     */
    @JsxSetter
    public void setBorderTopWidth(final String borderTopWidth) {
        setStyleAttributePixel(BORDER_TOP_WIDTH, borderTopWidth);
    }

    /**
     * Gets the "borderWidth" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderWidth() {
        return getStyleAttribute(BORDER_WIDTH);
    }

    /**
     * Sets the "borderWidth" style attribute.
     * @param borderWidth the new attribute
     */
    @JsxSetter
    public void setBorderWidth(final String borderWidth) {
        setStyleAttribute(BORDER_WIDTH, borderWidth);
    }

    /**
     * Gets the "bottom" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBottom() {
        return getStyleAttribute(BOTTOM);
    }

    /**
     * Sets the "bottom" style attribute.
     * @param bottom the new attribute
     */
    @JsxSetter
    public void setBottom(final String bottom) {
        setStyleAttributePixel(BOTTOM, bottom);
    }

    /**
     * Gets the "boxSizing" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBoxSizing() {
        return getStyleAttribute(BOX_SIZING);
    }

    /**
     * Sets the "boxSizing" style attribute.
     * @param boxSizing the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBoxSizing(final String boxSizing) {
        setStyleAttribute(BOX_SIZING, boxSizing);
    }

    /**
     * Gets the "captionSide" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getCaptionSide() {
        return getStyleAttribute(CAPTION_SIDE);
    }

    /**
     * Sets the "captionSide" style attribute.
     * @param captionSide the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setCaptionSide(final String captionSide) {
        setStyleAttribute(CAPTION_SIDE, captionSide);
    }

    /**
     * Gets the "clear" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getClear() {
        return getStyleAttribute(CLEAR);
    }

    /**
     * Sets the "clear" style attribute.
     * @param clear the new attribute
     */
    @JsxSetter
    public void setClear(final String clear) {
        setStyleAttribute(CLEAR, clear);
    }

    /**
     * Gets the "clip" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getClip() {
        return getStyleAttribute(CLIP);
    }

    /**
     * Sets the "clip" style attribute.
     * @param clip the new attribute
     */
    @JsxSetter
    public void setClip(final String clip) {
        setStyleAttribute(CLIP, clip);
    }

    /**
     * Gets the "color" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getColor() {
        return getStyleAttribute(COLOR);
    }

    /**
     * Sets the "color" style attribute.
     * @param color the new attribute
     */
    @JsxSetter
    public void setColor(final String color) {
        setStyleAttribute(COLOR, color);
    }

    /**
     * Gets the "content" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getContent() {
        return getStyleAttribute(CONTENT);
    }

    /**
     * Sets the "content" style attribute.
     * @param content the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setContent(final String content) {
        setStyleAttribute(CONTENT, content);
    }

    /**
     * Gets the "counterIncrement" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getCounterIncrement() {
        return getStyleAttribute(COUNTER_INCREMENT);
    }

    /**
     * Sets the "counterIncrement" style attribute.
     * @param counterIncrement the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setCounterIncrement(final String counterIncrement) {
        setStyleAttribute(COUNTER_INCREMENT, counterIncrement);
    }

    /**
     * Gets the "counterReset" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getCounterReset() {
        return getStyleAttribute(COUNTER_RESET);
    }

    /**
     * Sets the "counterReset" style attribute.
     * @param counterReset the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setCounterReset(final String counterReset) {
        setStyleAttribute(COUNTER_RESET, counterReset);
    }

    /**
     * Gets the "cssFloat" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public String getCssFloat() {
        return getStyleAttribute(FLOAT);
    }

    /**
     * Sets the "cssFloat" style attribute.
     * @param value the new attribute
     */
    @JsxSetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public void setCssFloat(final String value) {
        setStyleAttribute(FLOAT, value);
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
     * Gets the "cursor" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getCursor() {
        return getStyleAttribute(CURSOR);
    }

    /**
     * Sets the "cursor" style attribute.
     * @param cursor the new attribute
     */
    @JsxSetter
    public void setCursor(final String cursor) {
        setStyleAttribute(CURSOR, cursor);
    }

    /**
     * Gets the "direction" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getDirection() {
        return getStyleAttribute(DIRECTION);
    }

    /**
     * Sets the "direction" style attribute.
     * @param direction the new attribute
     */
    @JsxSetter
    public void setDirection(final String direction) {
        setStyleAttribute(DIRECTION, direction);
    }

    /**
     * Gets the "display" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getDisplay() {
        return getStyleAttribute(DISPLAY);
    }

    /**
     * Sets the "display" style attribute.
     * @param display the new attribute
     */
    @JsxSetter
    public void setDisplay(final String display) {
        setStyleAttribute(DISPLAY, display);
    }

    /**
     * Gets the "emptyCells" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getEmptyCells() {
        return getStyleAttribute(EMPTY_CELLS);
    }

    /**
     * Sets the "emptyCells" style attribute.
     * @param emptyCells the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setEmptyCells(final String emptyCells) {
        setStyleAttribute(EMPTY_CELLS, emptyCells);
    }

    /**
     * Gets the "font" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFont() {
        return getStyleAttribute(FONT);
    }

    /**
     * Sets the "font" style attribute.
     * @param font the new attribute
     */
    @JsxSetter
    public void setFont(final String font) {
        setStyleAttribute(FONT, font);
    }

    /**
     * Gets the "fontFamily" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFontFamily() {
        return getStyleAttribute(FONT_FAMILY);
    }

    /**
     * Sets the "fontFamily" style attribute.
     * @param fontFamily the new attribute
     */
    @JsxSetter
    public void setFontFamily(final String fontFamily) {
        setStyleAttribute(FONT_FAMILY, fontFamily);
    }

    /**
     * Gets the "fontSize" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFontSize() {
        return getStyleAttribute(FONT_SIZE);
    }

    /**
     * Sets the "fontSize" style attribute.
     * @param fontSize the new attribute
     */
    @JsxSetter
    public void setFontSize(final String fontSize) {
        setStyleAttributePixel(FONT_SIZE, fontSize);
    }

    /**
     * Gets the "fontSizeAdjust" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public String getFontSizeAdjust() {
        return getStyleAttribute(FONT_SIZE_ADJUST);
    }

    /**
     * Sets the "fontSizeAdjust" style attribute.
     * @param fontSizeAdjust the new attribute
     */
    @JsxSetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public void setFontSizeAdjust(final String fontSizeAdjust) {
        setStyleAttribute(FONT_SIZE_ADJUST, fontSizeAdjust);
    }

    /**
     * Gets the "fontStretch" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public String getFontStretch() {
        return getStyleAttribute(FONT_STRETCH);
    }

    /**
     * Sets the "fontStretch" style attribute.
     * @param fontStretch the new attribute
     */
    @JsxSetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public void setFontStretch(final String fontStretch) {
        setStyleAttribute(FONT_STRETCH, fontStretch);
    }

    /**
     * Gets the "fontStyle" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFontStyle() {
        return getStyleAttribute(FONT_STYLE);
    }

    /**
     * Sets the "fontStyle" style attribute.
     * @param fontStyle the new attribute
     */
    @JsxSetter
    public void setFontStyle(final String fontStyle) {
        setStyleAttribute(FONT_STYLE, fontStyle);
    }

    /**
     * Gets the "fontVariant" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFontVariant() {
        return getStyleAttribute(FONT_VARIANT);
    }

    /**
     * Sets the "fontVariant" style attribute.
     * @param fontVariant the new attribute
     */
    @JsxSetter
    public void setFontVariant(final String fontVariant) {
        setStyleAttribute(FONT_VARIANT, fontVariant);
    }

    /**
     * Gets the "fontWeight" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFontWeight() {
        return getStyleAttribute(FONT_WEIGHT);
    }

    /**
     * Sets the "fontWeight" style attribute.
     * @param fontWeight the new attribute
     */
    @JsxSetter
    public void setFontWeight(final String fontWeight) {
        setStyleAttribute(FONT_WEIGHT, fontWeight);
    }

    /**
     * Gets the "height" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getHeight() {
        return getStyleAttribute(HEIGHT);
    }

    /**
     * Sets the "height" style attribute.
     * @param height the new attribute
     */
    @JsxSetter
    public void setHeight(final String height) {
        setStyleAttributePixel(HEIGHT, height);
    }

    /**
     * Gets the "imeMode" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getImeMode() {
        return getStyleAttribute(IME_MODE);
    }

    /**
     * Sets the "imeMode" style attribute.
     * @param imeMode the new attribute
     */
    @JsxSetter
    public void setImeMode(final String imeMode) {
        setStyleAttribute(IME_MODE, imeMode);
    }

    /**
     * Gets the "layoutFlow" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getLayoutFlow() {
        return getStyleAttribute(LAYOUT_FLOW);
    }

    /**
     * Sets the "layoutFlow" style attribute.
     * @param layoutFlow the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLayoutFlow(final String layoutFlow) {
        setStyleAttribute(LAYOUT_FLOW, layoutFlow);
    }

    /**
     * Gets the "layoutGrid" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getLayoutGrid() {
        return getStyleAttribute(LAYOUT_GRID);
    }

    /**
     * Sets the "layoutGrid" style attribute.
     * @param layoutGrid the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLayoutGrid(final String layoutGrid) {
        setStyleAttribute(LAYOUT_GRID_CHAR, layoutGrid);
    }

    /**
     * Gets the "layoutGridChar" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getLayoutGridChar() {
        return getStyleAttribute(LAYOUT_GRID_CHAR);
    }

    /**
     * Sets the "layoutGridChar" style attribute.
     * @param layoutGridChar the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLayoutGridChar(final String layoutGridChar) {
        setStyleAttribute(LAYOUT_GRID_CHAR, layoutGridChar);
    }

    /**
     * Gets the "layoutGridLine" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getLayoutGridLine() {
        return getStyleAttribute(LAYOUT_GRID_LINE);
    }

    /**
     * Sets the "layoutGridLine" style attribute.
     * @param layoutGridLine the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLayoutGridLine(final String layoutGridLine) {
        setStyleAttribute(LAYOUT_GRID_LINE, layoutGridLine);
    }

    /**
     * Gets the "layoutGridMode" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getLayoutGridMode() {
        return getStyleAttribute(LAYOUT_GRID_MODE);
    }

    /**
     * Sets the "layoutGridMode" style attribute.
     * @param layoutGridMode the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLayoutGridMode(final String layoutGridMode) {
        setStyleAttribute(LAYOUT_GRID_MODE, layoutGridMode);
    }

    /**
     * Gets the "layoutGridType" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getLayoutGridType() {
        return getStyleAttribute(LAYOUT_GRID_TYPE);
    }

    /**
     * Sets the "layoutGridType" style attribute.
     * @param layoutGridType the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLayoutGridType(final String layoutGridType) {
        setStyleAttribute(LAYOUT_GRID_TYPE, layoutGridType);
    }

    /**
     * Gets the "left" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getLeft() {
        return getStyleAttribute(LEFT);
    }

    /**
     * Sets the "left" style attribute.
     * @param left the new attribute
     */
    @JsxSetter
    public void setLeft(final String left) {
        setStyleAttributePixel(LEFT, left);
    }

    /**
     * Gets the "length", not yet implemented.
     * @return the length
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public int getLength() {
        return getStyleMap().size();
    }

    /**
     * Gets the "letterSpacing" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getLetterSpacing() {
        return getStyleAttribute(LETTER_SPACING);
    }

    /**
     * Sets the "letterSpacing" style attribute.
     * @param letterSpacing the new attribute
     */
    @JsxSetter
    public void setLetterSpacing(final String letterSpacing) {
        setStyleAttributePixel(LETTER_SPACING, letterSpacing);
    }

    /**
     * Gets the "lineBreak" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getLineBreak() {
        return getStyleAttribute(LINE_BREAK);
    }

    /**
     * Sets the "lineBreak" style attribute.
     * @param lineBreak the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLineBreak(final String lineBreak) {
        setStyleAttribute(LINE_BREAK, lineBreak);
    }

    /**
     * Gets the "listStyle" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getListStyle() {
        return getStyleAttribute(LIST_STYLE);
    }

    /**
     * Sets the "listStyle" style attribute.
     * @param listStyle the new attribute
     */
    @JsxSetter
    public void setListStyle(final String listStyle) {
        setStyleAttribute(LIST_STYLE, listStyle);
    }

    /**
     * Gets the "listStyleImage" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getListStyleImage() {
        return getStyleAttribute(LIST_STYLE_IMAGE);
    }

    /**
     * Sets the "listStyleImage" style attribute.
     * @param listStyleImage the new attribute
     */
    @JsxSetter
    public void setListStyleImage(final String listStyleImage) {
        setStyleAttribute(LIST_STYLE_IMAGE, listStyleImage);
    }

    /**
     * Gets the "listStylePosition" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getListStylePosition() {
        return getStyleAttribute(LIST_STYLE_POSITION);
    }

    /**
     * Sets the "listStylePosition" style attribute.
     * @param listStylePosition the new attribute
     */
    @JsxSetter
    public void setListStylePosition(final String listStylePosition) {
        setStyleAttribute(LIST_STYLE_POSITION, listStylePosition);
    }

    /**
     * Gets the "listStyleType" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getListStyleType() {
        return getStyleAttribute(LIST_STYLE_TYPE);
    }

    /**
     * Sets the "listStyleType" style attribute.
     * @param listStyleType the new attribute
     */
    @JsxSetter
    public void setListStyleType(final String listStyleType) {
        setStyleAttribute(LIST_STYLE_TYPE, listStyleType);
    }

    /**
     * Gets the "margin" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMargin() {
        return getStyleAttribute(MARGIN);
    }

    /**
     * Sets the "margin" style attribute.
     * @param margin the new attribute
     */
    @JsxSetter
    public void setMargin(final String margin) {
        setStyleAttribute(MARGIN, margin);
    }

    /**
     * Gets the "marginBottom" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginBottom() {
        return getStyleAttribute(MARGIN_BOTTOM, MARGIN, Shorthand.BOTTOM);
    }

    /**
     * Sets the "marginBottom" style attribute.
     * @param marginBottom the new attribute
     */
    @JsxSetter
    public void setMarginBottom(final String marginBottom) {
        setStyleAttributePixel(MARGIN_BOTTOM, marginBottom);
    }

    /**
     * Gets the "marginLeft" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginLeft() {
        return getStyleAttribute(MARGIN_LEFT, MARGIN, Shorthand.LEFT);
    }

    /**
     * Sets the "marginLeft" style attribute.
     * @param marginLeft the new attribute
     */
    @JsxSetter
    public void setMarginLeft(final String marginLeft) {
        setStyleAttributePixel(MARGIN_LEFT, marginLeft);
    }

    /**
     * Gets the "marginRight" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginRight() {
        return getStyleAttribute(MARGIN_RIGHT, MARGIN, Shorthand.RIGHT);
    }

    /**
     * Sets the "marginRight" style attribute.
     * @param marginRight the new attribute
     */
    @JsxSetter
    public void setMarginRight(final String marginRight) {
        setStyleAttributePixel(MARGIN_RIGHT, marginRight);
    }

    /**
     * Gets the "marginTop" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginTop() {
        return getStyleAttribute(MARGIN_TOP, MARGIN, Shorthand.TOP);
    }

    /**
     * Sets the "marginTop" style attribute.
     * @param marginTop the new attribute
     */
    @JsxSetter
    public void setMarginTop(final String marginTop) {
        setStyleAttributePixel(MARGIN_TOP, marginTop);
    }

    /**
     * Gets the "markerOffset" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(FF))
    public String getMarkerOffset() {
        return getStyleAttribute(MARKER_OFFSET);
    }

    /**
     * Sets the "markerOffset" style attribute.
     * @param markerOffset the new attribute
     */
    @JsxSetter(@WebBrowser(FF))
    public void setMarkerOffset(final String markerOffset) {
        setStyleAttribute(MARKER_OFFSET, markerOffset);
    }

    /**
     * Gets the "marks" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(FF))
    public String getMarks() {
        return getStyleAttribute(MARKS);
    }

    /**
     * Sets the "marks" style attribute.
     * @param marks the new attribute
     */
    @JsxSetter(@WebBrowser(FF))
    public void setMarks(final String marks) {
        setStyleAttribute(MARKS, marks);
    }

    /**
     * Gets the "maxHeight" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMaxHeight() {
        return getStyleAttribute(MAX_HEIGHT);
    }

    /**
     * Sets the "maxHeight" style attribute.
     * @param maxHeight the new attribute
     */
    @JsxSetter
    public void setMaxHeight(final String maxHeight) {
        setStyleAttributePixel(MAX_HEIGHT, maxHeight);
    }

    /**
     * Gets the "maxWidth" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMaxWidth() {
        return getStyleAttribute(MAX_WIDTH);
    }

    /**
     * Sets the "maxWidth" style attribute.
     * @param maxWidth the new attribute
     */
    @JsxSetter
    public void setMaxWidth(final String maxWidth) {
        setStyleAttributePixel(MAX_WIDTH, maxWidth);
    }

    /**
     * Gets the "minHeight" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMinHeight() {
        return getStyleAttribute(MIN_HEIGHT);
    }

    /**
     * Sets the "minHeight" style attribute.
     * @param minHeight the new attribute
     */
    @JsxSetter
    public void setMinHeight(final String minHeight) {
        setStyleAttributePixel(MIN_HEIGHT, minHeight);
    }

    /**
     * Gets the "minWidth" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMinWidth() {
        return getStyleAttribute(MIN_WIDTH);
    }

    /**
     * Sets the "minWidth" style attribute.
     * @param minWidth the new attribute
     */
    @JsxSetter
    public void setMinWidth(final String minWidth) {
        setStyleAttributePixel(MIN_WIDTH, minWidth);
    }

    @Override
    public Object get(final String name, final Scriptable start) {
        if (this != start) {
            return super.get(name, start);
        }

        final Definition style = StyleAttributes.getDefinition(name, getBrowserVersion());
        if (style != null) {
            return getStyleAttributeValue(style);
        }

        return super.get(name, start);
    }

    @Override
    public Object get(final int index, final Scriptable start) {
        final int size = getStyleMap().size();
        if (index >= size) {
            return "";
        }
        return getStyleMap().keySet().toArray(new String[size])[index];
    }

    /**
     * Get the value for the style attribute.
     * @param style the style
     * @return the value
     */
    protected String getStyleAttributeValue(final Definition style) {
        return getStyleAttribute(style.getAttributeName());
    }

    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (this != start) {
            super.put(name, start, value);
            return;
        }

        if (getDomNodeOrNull() != null) { // check if prototype or not
            final Definition style = StyleAttributes.getDefinition(name, getBrowserVersion());
            if (style != null) {
                final String stringValue = Context.toString(value);
                setStyleAttribute(style.getPropertyName(), stringValue);
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
        final List<Object> ids = new ArrayList<Object>();
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
     * Gets the "msBlockProgression" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getMsBlockProgression() {
        return getStyleAttribute(MS_BLOCK_PROGRESSION);
    }

    /**
     * Sets the "msBlockProgression" style attribute.
     * @param msBlockProgression the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setMsBlockProgression(final String msBlockProgression) {
        setStyleAttribute(MS_BLOCK_PROGRESSION, msBlockProgression);
    }

    /**
     * Gets the "msInterpolationMode" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getMsInterpolationMode() {
        return getStyleAttribute(MS_INTERPOLATION_MODE);
    }

    /**
     * Sets the "msInterpolationMode" style attribute.
     * @param msInterpolationMode the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setMsInterpolationMode(final String msInterpolationMode) {
        setStyleAttribute(MS_INTERPOLATION_MODE, msInterpolationMode);
    }

    /**
     * Gets the "opacity" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public String getOpacity() {
        final String opacity = getStyleAttribute(OPACITY);
        if (getBrowserVersion().hasFeature(JS_OPACITY_ACCEPTS_ARBITRARY_VALUES)) {
            return opacity;
        }

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
     * Sets the "opacity" style attribute.
     * @param opacity the new attribute
     */
    @JsxSetter({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public void setOpacity(final String opacity) {
        if (getBrowserVersion().hasFeature(JS_OPACITY_ACCEPTS_ARBITRARY_VALUES)) {
            setStyleAttribute(OPACITY, opacity);
            return;
        }

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
     * Gets the "orphans" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getOrphans() {
        return getStyleAttribute(ORPHANS);
    }

    /**
     * Sets the "orphans" style attribute.
     * @param orphans the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setOrphans(final String orphans) {
        setStyleAttribute(ORPHANS, orphans);
    }

    /**
     * Gets the "outline" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getOutline() {
        return getStyleAttribute(OUTLINE);
    }

    /**
     * Sets the "outline" style attribute.
     * @param outline the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setOutline(final String outline) {
        setStyleAttribute(OUTLINE, outline);
    }

    /**
     * Gets the "outlineColor" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getOutlineColor() {
        return getStyleAttribute(OUTLINE_COLOR);
    }

    /**
     * Sets the "outlineColor" style attribute.
     * @param outlineColor the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setOutlineColor(final String outlineColor) {
        setStyleAttribute(OUTLINE_COLOR, outlineColor);
    }

    /**
     * Gets the "outlineOffset" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(FF))
    public String getOutlineOffset() {
        return getStyleAttribute(OUTLINE_OFFSET);
    }

    /**
     * Sets the "outlineOffset" style attribute.
     * @param outlineOffset the new attribute
     */
    @JsxSetter(@WebBrowser(FF))
    public void setOutlineOffset(final String outlineOffset) {
        setStyleAttribute(OUTLINE_OFFSET, outlineOffset);
    }

    /**
     * Gets the "outlineStyle" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getOutlineStyle() {
        return getStyleAttribute(OUTLINE_STYLE);
    }

    /**
     * Sets the "outlineStyle" style attribute.
     * @param outlineStyle the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setOutlineStyle(final String outlineStyle) {
        setStyleAttribute(OUTLINE_STYLE, outlineStyle);
    }

    /**
     * Gets the "outlineWidth" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getOutlineWidth() {
        return getStyleAttribute(OUTLINE_WIDTH);
    }

    /**
     * Sets the "outlineWidth" style attribute.
     * @param outlineWidth the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setOutlineWidth(final String outlineWidth) {
        setStyleAttributePixel(OUTLINE_WIDTH, outlineWidth);
    }

    /**
     * Gets the "overflow" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getOverflow() {
        return getStyleAttribute(OVERFLOW);
    }

    /**
     * Sets the "overflow" style attribute.
     * @param overflow the new attribute
     */
    @JsxSetter
    public void setOverflow(final String overflow) {
        setStyleAttribute(OVERFLOW, overflow);
    }

    /**
     * Gets the "overflowX" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getOverflowX() {
        return getStyleAttribute(OVERFLOW_X);
    }

    /**
     * Sets the "overflowX" style attribute.
     * @param overflowX the new attribute
     */
    @JsxSetter
    public void setOverflowX(final String overflowX) {
        setStyleAttribute(OVERFLOW_X, overflowX);
    }

    /**
     * Gets the "overflowY" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getOverflowY() {
        return getStyleAttribute(OVERFLOW_Y);
    }

    /**
     * Sets the "overflowY" style attribute.
     * @param overflowY the new attribute
     */
    @JsxSetter
    public void setOverflowY(final String overflowY) {
        setStyleAttribute(OVERFLOW_Y, overflowY);
    }

    /**
     * Gets the "padding" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPadding() {
        return getStyleAttribute(PADDING);
    }

    /**
     * Sets the "padding" style attribute.
     * @param padding the new attribute
     */
    @JsxSetter
    public void setPadding(final String padding) {
        setStyleAttribute(PADDING, padding);
    }

    /**
     * Gets the "paddingBottom" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingBottom() {
        return getStyleAttribute(PADDING_BOTTOM, PADDING, Shorthand.BOTTOM);
    }

    /**
     * Sets the "paddingBottom" style attribute.
     * @param paddingBottom the new attribute
     */
    @JsxSetter
    public void setPaddingBottom(final String paddingBottom) {
        setStyleAttributePixel(PADDING_BOTTOM, paddingBottom);
    }

    /**
     * Gets the "paddingLeft" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingLeft() {
        return getStyleAttribute(PADDING_LEFT, PADDING, Shorthand.LEFT);
    }

    /**
     * Sets the "paddingLeft" style attribute.
     * @param paddingLeft the new attribute
     */
    @JsxSetter
    public void setPaddingLeft(final String paddingLeft) {
        setStyleAttributePixel(PADDING_LEFT, paddingLeft);
    }

    /**
     * Gets the "paddingRight" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingRight() {
        return getStyleAttribute(PADDING_RIGHT, PADDING, Shorthand.RIGHT);
    }

    /**
     * Sets the "paddingRight" style attribute.
     * @param paddingRight the new attribute
     */
    @JsxSetter
    public void setPaddingRight(final String paddingRight) {
        setStyleAttributePixel(PADDING_RIGHT, paddingRight);
    }

    /**
     * Gets the "paddingTop" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingTop() {
        return getStyleAttribute(PADDING_TOP, PADDING, Shorthand.TOP);
    }

    /**
     * Sets the "paddingTop" style attribute.
     * @param paddingTop the new attribute
     */
    @JsxSetter
    public void setPaddingTop(final String paddingTop) {
        setStyleAttributePixel(PADDING_TOP, paddingTop);
    }

    /**
     * Gets the "page" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(FF))
    public String getPage() {
        return getStyleAttribute(PAGE);
    }

    /**
     * Sets the "page" style attribute.
     * @param page the new attribute
     */
    @JsxSetter(@WebBrowser(FF))
    public void setPage(final String page) {
        setStyleAttribute(PAGE, page);
    }

    /**
     * Gets the "pageBreakAfter" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPageBreakAfter() {
        return getStyleAttribute(PAGE_BREAK_AFTER);
    }

    /**
     * Sets the "pageBreakAfter" style attribute.
     * @param pageBreakAfter the new attribute
     */
    @JsxSetter
    public void setPageBreakAfter(final String pageBreakAfter) {
        setStyleAttribute(PAGE_BREAK_AFTER, pageBreakAfter);
    }

    /**
     * Gets the "pageBreakBefore" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPageBreakBefore() {
        return getStyleAttribute(PAGE_BREAK_BEFORE);
    }

    /**
     * Sets the "pageBreakBefore" style attribute.
     * @param pageBreakBefore the new attribute
     */
    @JsxSetter
    public void setPageBreakBefore(final String pageBreakBefore) {
        setStyleAttribute(PAGE_BREAK_BEFORE, pageBreakBefore);
    }

    /**
     * Gets the "pageBreakInside" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getPageBreakInside() {
        return getStyleAttribute(PAGE_BREAK_INSIDE);
    }

    /**
     * Sets the "pageBreakInside" style attribute.
     * @param pageBreakInside the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setPageBreakInside(final String pageBreakInside) {
        setStyleAttribute(PAGE_BREAK_INSIDE, pageBreakInside);
    }

    /**
     * Gets the "pointerEvents" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public String getPointerEvents() {
        return getStyleAttribute(POINTER_EVENTS);
    }

    /**
     * Sets the "pointerEvents" style attribute.
     * @param pointerEvents the new attribute
     */
    @JsxSetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public void setPointerEvents(final String pointerEvents) {
        setStyleAttribute(POINTER_EVENTS, pointerEvents);
    }

    /**
     * Gets the "pixelBottom" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPixelBottom() {
        return pixelValue(getBottom());
    }

    /**
     * Sets the "pixelBottom" style attribute.
     * @param pixelBottom the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPixelBottom(final int pixelBottom) {
        setBottom(pixelBottom + "px");
    }

    /**
     * Gets the "pixelHeight" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(value = IE, minVersion = 11))
    public int getPixelHeight() {
        return pixelValue(getHeight());
    }

    /**
     * Sets the "pixelHeight" style attribute.
     * @param pixelHeight the new attribute
     */
    @JsxSetter(@WebBrowser(value = IE, minVersion = 11))
    public void setPixelHeight(final int pixelHeight) {
        setHeight(pixelHeight + "px");
    }

    /**
     * Gets the "pixelLeft" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPixelLeft() {
        return pixelValue(getLeft());
    }

    /**
     * Sets the "pixelLeft" style attribute.
     * @param pixelLeft the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPixelLeft(final int pixelLeft) {
        setLeft(pixelLeft + "px");
    }

    /**
     * Gets the "pixelRight" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPixelRight() {
        return pixelValue(getRight());
    }

    /**
     * Sets the "pixelRight" style attribute.
     * @param pixelRight the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPixelRight(final int pixelRight) {
        setRight(pixelRight + "px");
    }

    /**
     * Gets the "pixelTop" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPixelTop() {
        return pixelValue(getTop());
    }

    /**
     * Sets the "pixelTop" style attribute.
     * @param pixelTop the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPixelTop(final int pixelTop) {
        setTop(pixelTop + "px");
    }

    /**
     * Gets the "pixelWidth" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(value = IE, minVersion = 11))
    public int getPixelWidth() {
        return pixelValue(getWidth());
    }

    /**
     * Sets the "pixelWidth" style attribute.
     * @param pixelWidth the new attribute
     */
    @JsxSetter(@WebBrowser(value = IE, minVersion = 11))
    public void setPixelWidth(final int pixelWidth) {
        setWidth(pixelWidth + "px");
    }

    /**
     * Gets the "posBottom" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosBottom() {
        return 0;
    }

    /**
     * Sets the "posBottom" style attribute.
     * @param posBottom the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosBottom(final int posBottom) {
        // Empty.
    }

    /**
     * Gets the "posHeight" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosHeight() {
        return 0;
    }

    /**
     * Sets the "posHeight" style attribute.
     * @param posHeight the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosHeight(final int posHeight) {
        // Empty.
    }

    /**
     * Gets the "position" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPosition() {
        return getStyleAttribute(POSITION);
    }

    /**
     * Sets the "position" style attribute.
     * @param position the new attribute
     */
    @JsxSetter
    public void setPosition(final String position) {
        setStyleAttribute(POSITION, position);
    }

    /**
     * Gets the "posLeft" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosLeft() {
        return 0;
    }

    /**
     * Sets the "posLeft" style attribute.
     * @param posLeft the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosLeft(final int posLeft) {
        // Empty.
    }

    /**
     * Gets the "posRight" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosRight() {
        return 0;
    }

    /**
     * Sets the "posRight" style attribute.
     * @param posRight the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosRight(final int posRight) {
        // Empty.
    }

    /**
     * Gets the "posTop" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosTop() {
        return 0;
    }

    /**
     * Sets the "posTop" style attribute.
     * @param posTop the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosTop(final int posTop) {
        // Empty.
    }

    /**
     * Gets the "posWidth" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public int getPosWidth() {
        return 0;
    }

    /**
     * Sets the "posWidth" style attribute.
     * @param posWidth the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setPosWidth(final int posWidth) {
        // Empty.
    }

    /**
     * Gets the "right" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getRight() {
        return getStyleAttribute(RIGHT);
    }

    /**
     * Sets the "right" style attribute.
     * @param right the new attribute
     */
    @JsxSetter
    public void setRight(final String right) {
        setStyleAttributePixel(RIGHT, right);
    }

    /**
     * Gets the "rubyAlign" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getRubyAlign() {
        return getStyleAttribute(RUBY_ALIGN);
    }

    /**
     * Sets the "rubyAlign" style attribute.
     * @param rubyAlign the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setRubyAlign(final String rubyAlign) {
        setStyleAttribute(RUBY_ALIGN, rubyAlign);
    }

    /**
     * Gets the "rubyOverhang" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getRubyOverhang() {
        return getStyleAttribute(RUBY_OVERHANG);
    }

    /**
     * Sets the "rubyOverhang" style attribute.
     * @param rubyOverhang the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setRubyOverhang(final String rubyOverhang) {
        setStyleAttribute(RUBY_OVERHANG, rubyOverhang);
    }

    /**
     * Gets the "rubyPosition" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getRubyPosition() {
        return getStyleAttribute(RUBY_POSITION);
    }

    /**
     * Sets the "rubyPosition" style attribute.
     * @param rubyPosition the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setRubyPosition(final String rubyPosition) {
        setStyleAttribute(RUBY_POSITION, rubyPosition);
    }

    /**
     * Gets the "scrollbar3dLightColor" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getScrollbar3dLightColor() {
        return getStyleAttribute(SCROLLBAR3D_LIGHT_COLOR);
    }

    /**
     * Sets the "scrollbar3dLightColor" style attribute.
     * @param scrollbar3dLightColor the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setScrollbar3dLightColor(final String scrollbar3dLightColor) {
        setStyleAttribute(SCROLLBAR3D_LIGHT_COLOR, scrollbar3dLightColor);
    }

    /**
     * Gets the "scrollbarArrowColor" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getScrollbarArrowColor() {
        return getStyleAttribute(SCROLLBAR_ARROW_COLOR);
    }

    /**
     * Sets the "scrollbarArrowColor" style attribute.
     * @param scrollbarArrowColor the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setScrollbarArrowColor(final String scrollbarArrowColor) {
        setStyleAttribute(SCROLLBAR_ARROW_COLOR, scrollbarArrowColor);
    }

    /**
     * Gets the "scrollbarBaseColor" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getScrollbarBaseColor() {
        return getStyleAttribute(SCROLLBAR_BASE_COLOR);
    }

    /**
     * Sets the "scrollbarBaseColor" style attribute.
     * @param scrollbarBaseColor the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setScrollbarBaseColor(final String scrollbarBaseColor) {
        setStyleAttribute(SCROLLBAR_BASE_COLOR, scrollbarBaseColor);
    }

    /**
     * Gets the "scrollbarDarkShadowColor" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getScrollbarDarkShadowColor() {
        return getStyleAttribute(SCROLLBAR_DARK_SHADOW_COLOR);
    }

    /**
     * Sets the "scrollbarDarkShadowColor" style attribute.
     * @param scrollbarDarkShadowColor the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setScrollbarDarkShadowColor(final String scrollbarDarkShadowColor) {
        setStyleAttribute(SCROLLBAR_DARK_SHADOW_COLOR, scrollbarDarkShadowColor);
    }

    /**
     * Gets the "scrollbarFaceColor" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getScrollbarFaceColor() {
        return getStyleAttribute(SCROLLBAR_FACE_COLOR);
    }

    /**
     * Sets the "scrollbarFaceColor" style attribute.
     * @param scrollbarFaceColor the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setScrollbarFaceColor(final String scrollbarFaceColor) {
        setStyleAttribute(SCROLLBAR_FACE_COLOR, scrollbarFaceColor);
    }

    /**
     * Gets the "scrollbarHighlightColor" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getScrollbarHighlightColor() {
        return getStyleAttribute(SCROLLBAR_HIGHLIGHT_COLOR);
    }

    /**
     * Sets the "scrollbarHighlightColor" style attribute.
     * @param scrollbarHighlightColor the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setScrollbarHighlightColor(final String scrollbarHighlightColor) {
        setStyleAttribute(SCROLLBAR_HIGHLIGHT_COLOR, scrollbarHighlightColor);
    }

    /**
     * Gets the "scrollbarShadowColor" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getScrollbarShadowColor() {
        return getStyleAttribute(SCROLLBAR_SHADOW_COLOR);
    }

    /**
     * Sets the "scrollbarShadowColor" style attribute.
     * @param scrollbarShadowColor the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setScrollbarShadowColor(final String scrollbarShadowColor) {
        setStyleAttribute(SCROLLBAR_SHADOW_COLOR, scrollbarShadowColor);
    }

    /**
     * Gets the "scrollbarTrackColor" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getScrollbarTrackColor() {
        return getStyleAttribute(SCROLLBAR_TRACK_COLOR);
    }

    /**
     * Sets the "scrollbarTrackColor" style attribute.
     * @param scrollbarTrackColor the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setScrollbarTrackColor(final String scrollbarTrackColor) {
        setStyleAttribute(SCROLLBAR_TRACK_COLOR, scrollbarTrackColor);
    }

    /**
     * Gets the "size" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(FF))
    public String getSize() {
        return getStyleAttribute(SIZE);
    }

    /**
     * Sets the "size" style attribute.
     * @param size the new attribute
     */
    @JsxSetter(@WebBrowser(FF))
    public void setSize(final String size) {
        setStyleAttribute(SIZE, size);
    }

    /**
     * Gets the "styleFloat" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getStyleFloat() {
        return getStyleAttribute(FLOAT);
    }

    /**
     * Sets the "styleFloat" style attribute.
     * @param value the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setStyleFloat(final String value) {
        setStyleAttribute(FLOAT, value);
    }

    /**
     * Gets the "tableLayout" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getTableLayout() {
        return getStyleAttribute(TABLE_LAYOUT);
    }

    /**
     * Sets the "tableLayout" style attribute.
     * @param tableLayout the new attribute
     */
    @JsxSetter
    public void setTableLayout(final String tableLayout) {
        setStyleAttribute(TABLE_LAYOUT, tableLayout);
    }

    /**
     * Gets the "textAlign" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getTextAlign() {
        return getStyleAttribute(TEXT_ALIGN);
    }

    /**
     * Sets the "textAlign" style attribute.
     * @param textAlign the new attribute
     */
    @JsxSetter
    public void setTextAlign(final String textAlign) {
        setStyleAttribute(TEXT_ALIGN, textAlign);
    }

    /**
     * Gets the "textAlignLast" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getTextAlignLast() {
        return getStyleAttribute(TEXT_ALIGN_LAST);
    }

    /**
     * Sets the "textAlignLast" style attribute.
     * @param textAlignLast the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextAlignLast(final String textAlignLast) {
        setStyleAttribute(TEXT_ALIGN_LAST, textAlignLast);
    }

    /**
     * Gets the "textAutospace" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getTextAutospace() {
        return getStyleAttribute(TEXT_AUTOSPACE);
    }

    /**
     * Sets the "textAutospace" style attribute.
     * @param textAutospace the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextAutospace(final String textAutospace) {
        setStyleAttribute(TEXT_AUTOSPACE, textAutospace);
    }

    /**
     * Gets the "textDecoration" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getTextDecoration() {
        return getStyleAttribute(TEXT_DECORATION);
    }

    /**
     * Sets the "textDecoration" style attribute.
     * @param textDecoration the new attribute
     */
    @JsxSetter
    public void setTextDecoration(final String textDecoration) {
        setStyleAttribute(TEXT_DECORATION, textDecoration);
    }

    /**
     * Gets the "textDecorationBlink" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getTextDecorationBlink() {
        return false;
    }

    /**
     * Sets the "textDecorationBlink" style attribute.
     * @param textDecorationBlink the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextDecorationBlink(final boolean textDecorationBlink) {
        // Empty.
    }

    /**
     * Gets the "textDecorationLineThrough" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getTextDecorationLineThrough() {
        return false;
    }

    /**
     * Sets the "textDecorationLineThrough" style attribute.
     * @param textDecorationLineThrough the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextDecorationLineThrough(final boolean textDecorationLineThrough) {
        // Empty.
    }

    /**
     * Gets the "textDecorationNone" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getTextDecorationNone() {
        return false;
    }

    /**
     * Sets the "textDecorationNone" style attribute.
     * @param textDecorationNone the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextDecorationNone(final boolean textDecorationNone) {
        // Empty.
    }

    /**
     * Gets the "textDecorationOverline" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getTextDecorationOverline() {
        return false;
    }

    /**
     * Sets the "textDecorationOverline" style attribute.
     * @param textDecorationOverline the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextDecorationOverline(final boolean textDecorationOverline) {
        // Empty.
    }

    /**
     * Gets the "textDecorationUnderline" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getTextDecorationUnderline() {
        return false;
    }

    /**
     * Sets the "textDecorationUnderline" style attribute.
     * @param textDecorationUnderline the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextDecorationUnderline(final boolean textDecorationUnderline) {
        // Empty.
    }

    /**
     * Gets the "textIndent" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getTextIndent() {
        return getStyleAttribute(TEXT_INDENT);
    }

    /**
     * Sets the "textIndent" style attribute.
     * @param textIndent the new attribute
     */
    @JsxSetter
    public void setTextIndent(final String textIndent) {
        setStyleAttributePixel(TEXT_INDENT, textIndent);
    }

    /**
     * Gets the "textJustify" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getTextJustify() {
        return getStyleAttribute(TEXT_JUSTIFY);
    }

    /**
     * Sets the "textJustify" style attribute.
     * @param textJustify the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextJustify(final String textJustify) {
        setStyleAttribute(TEXT_JUSTIFY, textJustify);
    }

    /**
     * Gets the "textJustifyTrim" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getTextJustifyTrim() {
        return getStyleAttribute(TEXT_JUSTIFY_TRIM);
    }

    /**
     * Sets the "textJustifyTrim" style attribute.
     * @param textJustifyTrim the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextJustifyTrim(final String textJustifyTrim) {
        setStyleAttribute(TEXT_JUSTIFY_TRIM, textJustifyTrim);
    }

    /**
     * Gets the "textKashida" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getTextKashida() {
        return getStyleAttribute(TEXT_KASHIDA);
    }

    /**
     * Sets the "textKashida" style attribute.
     * @param textKashida the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextKashida(final String textKashida) {
        setStyleAttribute(TEXT_KASHIDA, textKashida);
    }

    /**
     * Gets the "textKashidaSpace" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getTextKashidaSpace() {
        return getStyleAttribute(TEXT_KASHIDA_SPACE);
    }

    /**
     * Sets the "textKashidaSpace" style attribute.
     * @param textKashidaSpace the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextKashidaSpace(final String textKashidaSpace) {
        setStyleAttribute(TEXT_KASHIDA_SPACE, textKashidaSpace);
    }

    /**
     * Gets the "textOverflow" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getTextOverflow() {
        return getStyleAttribute(TEXT_OVERFLOW);
    }

    /**
     * Sets the "textOverflow" style attribute.
     * @param textOverflow the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextOverflow(final String textOverflow) {
        setStyleAttribute(TEXT_OVERFLOW, textOverflow);
    }

    /**
     * Gets the "textShadow" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public String getTextShadow() {
        return getStyleAttribute(TEXT_SHADOW);
    }

    /**
     * Sets the "textShadow" style attribute.
     * @param textShadow the new attribute
     */
    @JsxSetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public void setTextShadow(final String textShadow) {
        setStyleAttribute(TEXT_SHADOW, textShadow);
    }

    /**
     * Gets the "textTransform" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getTextTransform() {
        return getStyleAttribute(TEXT_TRANSFORM);
    }

    /**
     * Sets the "textTransform" style attribute.
     * @param textTransform the new attribute
     */
    @JsxSetter
    public void setTextTransform(final String textTransform) {
        setStyleAttribute(TEXT_TRANSFORM, textTransform);
    }

    /**
     * Gets the "textUnderlinePosition" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getTextUnderlinePosition() {
        return getStyleAttribute(TEXT_UNDERLINE_POSITION);
    }

    /**
     * Sets the "textUnderlinePosition" style attribute.
     * @param textUnderlinePosition the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTextUnderlinePosition(final String textUnderlinePosition) {
        setStyleAttribute(TEXT_UNDERLINE_POSITION, textUnderlinePosition);
    }

    /**
     * Gets the "top" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getTop() {
        return getStyleAttribute(TOP);
    }

    /**
     * Sets the "top" style attribute.
     * @param top the new attribute
     */
    @JsxSetter
    public void setTop(final String top) {
        setStyleAttributePixel(TOP, top);
    }

    /**
     * Gets the "verticalAlign" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getVerticalAlign() {
        return getStyleAttribute(VERTICAL_ALIGN);
    }

    /**
     * Sets the "verticalAlign" style attribute.
     * @param verticalAlign the new attribute
     */
    @JsxSetter
    public void setVerticalAlign(final String verticalAlign) {
        setStyleAttributePixel(VERTICAL_ALIGN, verticalAlign);
    }

    /**
     * Gets the "visibility" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getVisibility() {
        return getStyleAttribute(VISIBILITY);
    }

    /**
     * Sets the "visibility" style attribute.
     * @param visibility the new attribute
     */
    @JsxSetter
    public void setVisibility(final String visibility) {
        setStyleAttribute(VISIBILITY, visibility);
    }

    /**
     * Gets the "whiteSpace" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getWhiteSpace() {
        return getStyleAttribute(WHITE_SPACE);
    }

    /**
     * Sets the "whiteSpace" style attribute.
     * @param whiteSpace the new attribute
     */
    @JsxSetter
    public void setWhiteSpace(final String whiteSpace) {
        setStyleAttribute(WHITE_SPACE, whiteSpace);
    }

    /**
     * Gets the "widows" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getWidows() {
        return getStyleAttribute(WIDOWS);
    }

    /**
     * Sets the "widows" style attribute.
     * @param widows the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setWidows(final String widows) {
        setStyleAttribute(WIDOWS, widows);
    }

    /**
     * Gets the "width" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getWidth() {
        return getStyleAttribute(WIDTH);
    }

    /**
     * Sets the "width" style attribute.
     * @param width the new attribute
     */
    @JsxSetter
    public void setWidth(final String width) {
        setStyleAttributePixel(WIDTH, width);
    }

    /**
     * Gets the "wordSpacing" style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getWordSpacing() {
        return getStyleAttribute(WORD_SPACING);
    }

    /**
     * Sets the "wordSpacing" style attribute.
     * @param wordSpacing the new attribute
     */
    @JsxSetter
    public void setWordSpacing(final String wordSpacing) {
        setStyleAttributePixel(WORD_SPACING, wordSpacing);
    }

    /**
     * Gets the "wordWrap" style attribute.
     * @return the style attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public String getWordWrap() {
        return getStyleAttribute(WORD_WRAP);
    }

    /**
     * Sets the "wordWrap" style attribute.
     * @param wordWrap the new attribute
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public void setWordWrap(final String wordWrap) {
        setStyleAttribute(WORD_WRAP, wordWrap);
    }

    /**
     * Gets the "writingMode" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getWritingMode() {
        return getStyleAttribute(WRITING_MODE);
    }

    /**
     * Sets the "writingMode" style attribute.
     * @param writingMode the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setWritingMode(final String writingMode) {
        setStyleAttribute(WRITING_MODE, writingMode);
    }

    /**
     * Gets the "zIndex" style attribute.
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

        if (getBrowserVersion().hasFeature(CSS_ZINDEX_TYPE_NUMBER)) {
            if (value == null
                    || Context.getUndefinedValue().equals(value)
                    || StringUtils.isEmpty(value.toString())) {
                return Integer.valueOf(0);
            }
            try {
                final Double numericValue = Double.valueOf(value);
                return Integer.valueOf(numericValue.intValue());
            }
            catch (final NumberFormatException e) {
                return Integer.valueOf(0);
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
     * Sets the "zIndex" style attribute.
     * @param zIndex the new attribute
     */
    @JsxSetter
    public void setZIndex(final Object zIndex) {
        if (zIndex == null
                && getBrowserVersion().hasFeature(CSS_ZINDEX_UNDEFINED_OR_NULL_THROWS_ERROR)) {
            throw new EvaluatorException("Null is invalid for z-index.");
        }

        // empty
        if (zIndex == null || StringUtils.isEmpty(zIndex.toString())) {
            setStyleAttribute(Z_INDEX, "");
            return;
        }
        // undefined
        if (Context.getUndefinedValue().equals(zIndex)) {
            if (getBrowserVersion().hasFeature(CSS_ZINDEX_UNDEFINED_OR_NULL_THROWS_ERROR)) {
                throw new EvaluatorException("Undefind is invalid for z-index.");
            }
            if (getBrowserVersion().hasFeature(CSS_ZINDEX_UNDEFINED_FORCES_RESET)) {
                setStyleAttribute(Z_INDEX, "");
            }
            return;
        }

        // number
        if (getBrowserVersion().hasFeature(CSS_ZINDEX_TYPE_NUMBER)) {
            final Double d;
            if (zIndex instanceof Double) {
                d = (Double) zIndex;
            }
            else {
                try {
                    d = Double.valueOf(zIndex.toString());
                }
                catch (final NumberFormatException e) {
                    throw new WrappedException(e);
                }
            }
            setStyleAttribute(Z_INDEX, Integer.toString(d.intValue()));
            return;
        }

        // string
        if (zIndex instanceof Number) {
            final Number number = (Number) zIndex;
            if (number.doubleValue() % 1 == 0) {
                setStyleAttribute(Z_INDEX, (Integer.toString(number.intValue())));
            }
            return;
        }
        try {
            final int i = Integer.parseInt(zIndex.toString());
            setStyleAttribute(Z_INDEX, (Integer.toString(i)));
        }
        catch (final NumberFormatException e) {
            // ignore
        }
    }

    /**
     * Gets the "zoom" style attribute.
     * @return the style attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getZoom() {
        return getStyleAttribute(ZOOM);
    }

    /**
     * Sets the "zoom" style attribute.
     * @param zoom the new attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setZoom(final String zoom) {
        setStyleAttribute(ZOOM, zoom);
    }

    /**
     * Gets the value of the specified property of the style.
     * @param name the style property name
     * @return empty string if nothing found
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
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
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
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
     * Removes the named property.
     * @param name the name of the property to remove
     * @return the value deleted
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public String removeProperty(final String name) {
        return removeStyleAttribute(name);
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
    @JsxFunction(@WebBrowser(value = IE, maxVersion = 8))
    public void setExpression(final String propertyName, final String expression, final String language) {
        // Empty.
    }

    /**
     * Removes the expression from the specified property.
     *
     * @param propertyName the name of the property from which to remove an expression
     * @return true if the expression was successfully removed
     */
    @JsxFunction(@WebBrowser(value = IE, maxVersion = 8))
    public boolean removeExpression(final String propertyName) {
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
    @JsxFunction(@WebBrowser(IE))
    public Object getAttribute(final String name, final int flag) {
        if (getBrowserVersion().hasFeature(JS_STYLE_GET_ATTRIBUTE_SUPPORTS_FLAGS) && flag == 1) {
            // Case-sensitive.
            return getStyleAttribute(name);
        }

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
        int flagInt = 0;
        if (getBrowserVersion().hasFeature(JS_STYLE_SET_ATTRIBUTE_SUPPORTS_FLAGS)) {
            if (flag == Undefined.instance) {
                flagInt = 1;
            }
            else {
                flagInt = (int) Context.toNumber(flag);
            }
        }
        if (flagInt == 0) {
            // Case-insensitive.
            final StyleElement style = getStyleElementCaseInSensitive(name);
            if (null != style) {
                setStyleAttribute(style.getName(), value);
            }
        }
        else {
            // Case-sensitive.
            if (getStyleAttribute(name).length() > 0) {
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
    @JsxFunction(@WebBrowser(IE))
    public boolean removeAttribute(final String name, final Object flag) {
        int flagInt = 0;
        if (getBrowserVersion().hasFeature(JS_STYLE_REMOVE_ATTRIBUTE_SUPPORTS_FLAGS)) {
            if (flag == Undefined.instance) {
                flagInt = 1;
            }
            else {
                flagInt = (int) Context.toNumber(flag);
            }
        }
        if (flagInt == 0) {
            // Case-insensitive.
            final StyleElement style = getStyleElementCaseInSensitive(name);
            if (style != null) {
                removeStyleAttribute(style.getName());
                return true;
            }
            return false;
        }

        // Case-sensitive.
        final String s = getStyleAttribute(name);
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
    private String findColor(final String text) {
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
                if (getBrowserVersion().hasFeature(JS_GET_BACKGROUND_COLOR_FOR_COMPUTED_STYLE_AS_RGB)) {
                    return com.gargoylesoftware.htmlunit.util.StringUtils.formatColor(tmpColor);
                }
                return token;
            }
        }
        return null;
    }

    /**
     * Searches for any url notation in the specified text.
     * @param text the string to search in
     * @return the string of the url if found, null otherwise
     */
    private String findImageUrl(final String text) {
        final Matcher m = URL_PATTERN.matcher(text);
        if (m.find()) {
            if (getBrowserVersion().hasFeature(CSS_IMAGE_URL_QUOTED)) {
                return "url(\"" + m.group(1) + "\")";
            }
            return "url(" + m.group(1) + ")";
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
        return CSSColors_.containsKey(token.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Gets the RGB equivalent of a CSS color if the provided color is recognized.
     * @param color the color
     * @return the provided color if this is not a recognized color keyword, the RGB value
     * in the form "rgb(x, y, z)" otherwise
     */
    public static String toRGBColor(final String color) {
        final String rgbValue = CSSColors_.get(color.toLowerCase(Locale.ENGLISH));
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
        final String s = value.get(element);
        if (s.endsWith("%") || (s.isEmpty() && element instanceof HTMLHtmlElement)) {
            final int i = NumberUtils.toInt(TO_INT_PATTERN.matcher(s).replaceAll("$1"), 100);
            final Element parent = element.getParentElement();
            final int absoluteValue = (parent == null) ? value.getWindowDefaultValue() : pixelValue(parent, value);
            return (int) ((i / 100D) * absoluteValue);
        }
        if (s.isEmpty() && element instanceof HTMLCanvasElement) {
            return value.getWindowDefaultValue();
        }
        return pixelValue(s);
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
        private final int windowDefaultValue_;

        /**
         * C'tor.
         * @param windowDefaultValue the default value for the window
         */
        public CssValue(final int windowDefaultValue) {
            windowDefaultValue_ = windowDefaultValue;
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
            if (getBrowserVersion().hasFeature(CSS_PIXEL_VALUES_INT_ONLY)) {
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
