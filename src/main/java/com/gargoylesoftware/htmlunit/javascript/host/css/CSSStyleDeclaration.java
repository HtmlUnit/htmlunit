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

import java.awt.Color;
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

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement;
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
 * @author Ronald Brill
 */
public class CSSStyleDeclaration extends SimpleScriptable implements ScriptableWithFallbackGetter {
    /** Css important property constant. */
    protected static final String PRIORITY_IMPORTANT = "important";

    private static final String AZIMUTH = "azimuth";
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
    private static final String CAPTION_SIDE = "caption-side";
    private static final String CLEAR = "clear";
    private static final String CLIP = "clip";
    private static final String COLOR = "color";
    private static final String CONTENT = "content";
    private static final String COUNTER_INCREMENT = "counter-increment";
    private static final String COUNTER_RESET = "counter-reset";
    private static final String CUE = "cue";
    private static final String CUE_AFTER = "cue-after";
    private static final String CUE_BEFORE = "cue-before";
    private static final String CURSOR = "cursor";
    private static final String DIRECTION = "direction";
    private static final String DISPLAY = "display";
    private static final String ELEVATION = "elevation";
    private static final String EMPTY_CELLS = "empty-cells";
    private static final String FILTER = "filter";
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
    private static final String LINE_HEIGHT = "line-height";
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
    private static final String MOZ_APPEARANCE = "-moz-appearance";
    private static final String MOZ_BACKGROUND_CLIP = "-moz-background-clip";
    private static final String MOZ_BACKGROUND_INLINE_POLICY = "-moz-background-inline-policy";
    private static final String MOZ_BACKGROUND_ORIGIN = "-moz-background-origin";
    private static final String MOZ_BINDING = "-moz-binding";
    private static final String MOZ_BORDER_BOTTOM_COLORS = "-moz-border-bottom-colors";
    private static final String MOZ_BORDER_LEFT_COLORS = "-moz-border-left-colors";
    private static final String MOZ_BORDER_RADIUS = "-moz-border-radius";
    private static final String MOZ_BORDER_RADIUS_BOTTOMLEFT = "-moz-border-radius-bottomleft";
    private static final String MOZ_BORDER_RADIUS_BOTTOMRIGHT = "-moz-border-radius-bottomright";
    private static final String MOZ_BORDER_RADIUS_TOPLEFT = "-moz-border-radius-topleft";
    private static final String MOZ_BORDER_RADIUS_TOPRIGHT = "-moz-border-radius-topright";
    private static final String MOZ_BORDER_RIGHT_COLORS = "-moz-border-right-colors";
    private static final String MOZ_BORDER_TOP_COLORS = "-moz-border-top-colors";
    private static final String MOZ_BOX_ALIGN = "-moz-box-align";
    private static final String MOZ_BOX_DIRECTION = "-moz-box-direction";
    private static final String MOZ_BOX_FLEX = "-moz-box-flex";
    private static final String MOZ_BOX_ORDINAL_GROUP = "-moz-box-ordinal-group";
    private static final String MOZ_BOX_ORIENT = "-moz-box-orient";
    private static final String MOZ_BOX_PACK = "-moz-box-pack";
    private static final String MOZ_BOX_SIZING = "-moz-box-sizing";
    private static final String MOZ_COLUMN_COUNT = "-moz-column-count";
    private static final String MOZ_COLUMN_GAP = "-moz-column-gap";
    private static final String MOZ_COLUMN_WIDTH = "-moz-column-width";
    private static final String MOZ_FLOAT_EDGE = "-moz-float-edge";
    private static final String MOZ_FORCE_BROKEN_IMAGE_ICON = "-moz-force-broken-image-icon";
    private static final String MOZ_IMAGE_REGION = "-moz-image-region";
    private static final String MOZ_MARGIN_END = "-moz-margin-end";
    private static final String MOZ_MARGIN_START = "-moz-margin-start";
    private static final String MOZ_OPACITY = "-moz-opacity";
    private static final String MOZ_OUTLINE = "-moz-outline";
    private static final String MOZ_OUTLINE_COLOR = "-moz-outline-color";
    private static final String MOZ_OUTLINE_OFFSET = "-moz-outline-offset";
    private static final String MOZ_OUTLINE_RADIUS = "-mz-outline-radius";
    private static final String MOZ_OUTLINE_RADIUS_BOTTOMLEFT = "-moz-outline-radius-bottomleft";
    private static final String MOZ_OUTLINE_RADIUS_BOTTOMRIGHT = "-moz-outline-radius-bottomright";
    private static final String MOZ_OUTLINE_RADIUS_TOPLEFT = "-moz-outline-radius-topleft";
    private static final String MOZ_OUTLINE_RADIUS_TOPRIGHT = "-moz-outline-radius-topright";
    private static final String MOZ_OUTLINE_STYLE = "-moz-outline-style";
    private static final String MOZ_OUTLINE_WIDTH = "-moz-outline-width";
    private static final String MOZ_PADDING_END = "-moz-padding-end";
    private static final String MOZ_PADDING_START = "-moz-padding-start";
    private static final String MOZ_USER_FOCUS = "-moz-user-focus";
    private static final String MOZ_USER_INPUT = "-moz-user-input";
    private static final String MOZ_USER_MODIFY = "-moz-user-modify";
    private static final String MOZ_USER_SELECT = "-moz-user-select";
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
    private static final String PAUSE = "pause";
    private static final String PAUSE_AFTER = "pause-after";
    private static final String PAUSE_BEFORE = "pause-before";
    private static final String PITCH = "pitch";
    private static final String PITCH_RANGE = "pitch-range";
    private static final String POSITION = "position";
    private static final String QUOTES = "quotes";
    private static final String RICHNESS = "richness";
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
    private static final String SPEAK = "speak";
    private static final String SPEAK_HEADER = "speak-header";
    private static final String SPEAK_NUMERAL = "speak-numeral";
    private static final String SPEAK_PUNCTUATION = "speak-punctuation";
    private static final String SPEECH_RATE = "speech-rate";
    private static final String STRESS = "stress";
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
    private static final String UNICODE_BIDI = "unicode-bidi";
    private static final String VERTICAL_ALIGN = "vertical-align";
    private static final String VISIBILITY = "visibility";
    private static final String VOICE_FAMILY = "voice-family";
    private static final String VOLUME = "volume";
    private static final String WHITE_SPACE = "white-space";
    private static final String WIDOWS = "widows";
    private static final String WORD_BREAK = "word-break";
    private static final String WORD_SPACING = "word-spacing";
    private static final String WORD_WRAP = "word-wrap";
    private static final String WRITING_MODE = "writing-mode";
    private static final String Z_INDEX = "z-index";
    private static final String ZOOM = "zoom";

    /** The width style attribute. **/
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
    private static final Map<String, String> CamelizeCache_ = new HashMap<String, String>();
    private static final Map<String, Integer> pixelValuesCache_ = new HashMap<String, Integer>();

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
        if (element instanceof HTMLElement
            && getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_SUPPORTS_BEHAVIOR_PROPERTY)) {
            final HTMLElement htmlElement = (HTMLElement) element;
            for (final StyleElement styleElement : getStyleMap().values()) {
                if (BEHAVIOR.equals(styleElement.getName())) {
                    try {
                        final Object[] url = URL_FORMAT.parse(styleElement.getValue());
                        if (url.length > 0) {
                            htmlElement.jsxFunction_addBehavior((String) url[0]);
                            break;
                        }
                    }
                    catch (final ParseException e) {
                        LOG.warn("Invalid behavior: '" + styleElement.getValue() + "'.");
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
    public Object getWithFallback(final String name) {
        // TODO
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_STYLE_UNSUPPORTED_PROPERTY_GETTER)) {
            if (null != jsElement_) {
                final Map<String, StyleElement> style = getStyleMap();
                final StyleElement element = style.get(name);
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
     * @param styleMap if the style map was calculated before, you can provide it here
     * for performance
     * @return the named style attribute value, or an empty string if it is not found
     */
    protected String getStyleAttribute(final String name, final Map<String, StyleElement> styleMap) {
        if (styleDeclaration_ != null) {
            return styleDeclaration_.getPropertyValue(name);
        }
        Map<String, StyleElement> style = styleMap;
        if (null == style) {
            style = getStyleMap();
        }
        final StyleElement element = style.get(name);
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
            final Map<String, StyleElement> styleMap = getStyleMap();
            final StyleElement element1 = styleMap.get(name1);
            final StyleElement element2 = styleMap.get(name2);

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
    protected void setStyleAttribute(final String name, final String newValue) {
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
    protected Map<String, StyleElement> getStyleMap() {
        final Map<String, StyleElement> styleMap = new LinkedHashMap<String, StyleElement>();
        final String styleAttribute = jsElement_.getDomNodeOrDie().getAttribute("style");
        for (final String token : StringUtils.split(styleAttribute, ';')) {
            final int index = token.indexOf(":");
            if (index != -1) {
                final String key = token.substring(0, index).trim().toLowerCase();
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

        String result = CamelizeCache_.get(string);
        if (null != result) {
            return result;
        }

        // not found in CamelizeCache_; convert and store in cache
        final int pos = string.indexOf('-');
        if (pos == -1 || pos >= string.length() - 1) {
            // cache also this strings for performance
            CamelizeCache_.put(string, result);
            return string;
        }

        final StringBuilder buffer = new StringBuilder(string);
        buffer.deleteCharAt(pos);
        buffer.setCharAt(pos, Character.toUpperCase(buffer.charAt(pos)));

        for (int i = pos + 1; i < buffer.length() - 1; i++) {
            if (buffer.charAt(i) == '-') {
                buffer.deleteCharAt(i);
                buffer.setCharAt(i, Character.toUpperCase(buffer.charAt(i)));
            }
        }
        result = buffer.toString();
        CamelizeCache_.put(string, result);

        return result;
    }

    /**
     * Gets the "azimuth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_azimuth() {
        return getStyleAttribute(AZIMUTH, null);
    }

    /**
     * Sets the "azimuth" style attribute.
     * @param azimuth the new attribute
     */
    public void jsxSet_azimuth(final String azimuth) {
        setStyleAttribute(AZIMUTH, azimuth);
    }

    /**
     * Gets the "background" style attribute.
     * @return the style attribute
     */
    public String jsxGet_background() {
        return getStyleAttribute(BACKGROUND, null);
    }

    /**
     * Sets the "background" style attribute.
     * @param background the new attribute
     */
    public void jsxSet_background(final String background) {
        setStyleAttribute(BACKGROUND, background);
    }

    /**
     * Gets the "backgroundAttachment" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundAttachment() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BACKGROUND_ATTACHMENT, style);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND, style);
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
    public void jsxSet_backgroundAttachment(final String backgroundAttachment) {
        setStyleAttribute(BACKGROUND_ATTACHMENT, backgroundAttachment);
    }

    /**
     * Gets the "backgroundColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundColor() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BACKGROUND_COLOR, style);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND, style);
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
    public void jsxSet_backgroundColor(final String backgroundColor) {
        setStyleAttribute(BACKGROUND_COLOR, backgroundColor);
    }

    /**
     * Gets the "backgroundImage" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundImage() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BACKGROUND_IMAGE, style);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND, style);
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
    public void jsxSet_backgroundImage(final String backgroundImage) {
        setStyleAttribute(BACKGROUND_IMAGE, backgroundImage);
    }

    /**
     * Gets the "backgroundPosition" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundPosition() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BACKGROUND_POSITION, style);
        if (value == null) {
            return null;
        }
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND, style);
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
    public void jsxSet_backgroundPosition(final String backgroundPosition) {
        setStyleAttribute(BACKGROUND_POSITION, backgroundPosition);
    }

    /**
     * Gets the "backgroundPositionX" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundPositionX() {
        return getStyleAttribute(BACKGROUND_POSITION_X, null);
    }

    /**
     * Sets the "backgroundPositionX" style attribute.
     * @param backgroundPositionX the new attribute
     */
    public void jsxSet_backgroundPositionX(final String backgroundPositionX) {
        setStyleAttribute(BACKGROUND_POSITION_X, backgroundPositionX);
    }

    /**
     * Gets the "backgroundPositionY" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundPositionY() {
        return getStyleAttribute(BACKGROUND_POSITION_Y, null);
    }

    /**
     * Sets the "backgroundPositionY" style attribute.
     * @param backgroundPositionY the new attribute
     */
    public void jsxSet_backgroundPositionY(final String backgroundPositionY) {
        setStyleAttribute(BACKGROUND_POSITION_Y, backgroundPositionY);
    }

    /**
     * Gets the "backgroundRepeat" style attribute.
     * @return the style attribute
     */
    public String jsxGet_backgroundRepeat() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BACKGROUND_REPEAT, style);
        if (StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(BACKGROUND, style);
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
    public void jsxSet_backgroundRepeat(final String backgroundRepeat) {
        setStyleAttribute(BACKGROUND_REPEAT, backgroundRepeat);
    }

    /**
     * Gets the object's behavior (IE only).
     * @return the object's behavior
     */
    public String jsxGet_behavior() {
        return getStyleAttribute(BEHAVIOR, null);
    }

    /**
     * Sets the object's behavior (IE only).
     * @param behavior the new behavior
     */
    public void jsxSet_behavior(final String behavior) {
        setStyleAttribute(BEHAVIOR, behavior);

        // many methods/properties need to be moved from HTMLElement to Element
        // is it the case for behavior related methods? Assuming not in a first time...
        if (!(jsElement_ instanceof HTMLElement)) {
            throw new RuntimeException("Bug! behavior can be set for Element too!!!");
        }

        final HTMLElement htmlElement = (HTMLElement) jsElement_;
        htmlElement.jsxFunction_removeBehavior(HTMLElement.BEHAVIOR_ID_CLIENT_CAPS);
        htmlElement.jsxFunction_removeBehavior(HTMLElement.BEHAVIOR_ID_HOMEPAGE);
        htmlElement.jsxFunction_removeBehavior(HTMLElement.BEHAVIOR_ID_DOWNLOAD);
        if (behavior.length() != 0) {
            try {
                final Object[] url = URL_FORMAT.parse(behavior);
                if (url.length > 0) {
                    htmlElement.jsxFunction_addBehavior((String) url[0]);
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
        return getStyleAttribute(BORDER, null);
    }

    /**
     * Sets the "border" style attribute.
     * @param border the new attribute
     */
    public void jsxSet_border(final String border) {
        setStyleAttribute(BORDER, border);
    }

    /**
     * Gets the "borderBottom" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderBottom() {
        return getStyleAttribute(BORDER_BOTTOM, null);
    }

    /**
     * Sets the "borderBottom" style attribute.
     * @param borderBottom the new attribute
     */
    public void jsxSet_borderBottom(final String borderBottom) {
        setStyleAttribute(BORDER_BOTTOM, borderBottom);
    }

    /**
     * Gets the "borderBottomColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderBottomColor() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BORDER_BOTTOM_COLOR, style);
        if (value.length() == 0) {
            value = findColor(getStyleAttribute(BORDER_BOTTOM, style));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER, style));
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
        setStyleAttribute(BORDER_BOTTOM_COLOR, borderBottomColor);
    }

    /**
     * Gets the "borderBottomStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderBottomStyle() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BORDER_BOTTOM_STYLE, style);
        if (value.length() == 0) {
            value = findBorderStyle(getStyleAttribute(BORDER_BOTTOM, style));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER, style));
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
        setStyleAttribute(BORDER_BOTTOM_STYLE, borderBottomStyle);
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
        setStyleAttributePixel(BORDER_BOTTOM_WIDTH, borderBottomWidth);
    }

    /**
     * Gets the "borderCollapse" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderCollapse() {
        return getStyleAttribute(BORDER_COLLAPSE, null);
    }

    /**
     * Sets the "borderCollapse" style attribute.
     * @param borderCollapse the new attribute
     */
    public void jsxSet_borderCollapse(final String borderCollapse) {
        setStyleAttribute(BORDER_COLLAPSE, borderCollapse);
    }

    /**
     * Gets the "borderColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderColor() {
        return getStyleAttribute(BORDER_COLOR, null);
    }

    /**
     * Sets the "borderColor" style attribute.
     * @param borderColor the new attribute
     */
    public void jsxSet_borderColor(final String borderColor) {
        setStyleAttribute(BORDER_COLOR, borderColor);
    }

    /**
     * Gets the "borderLeft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderLeft() {
        return getStyleAttribute(BORDER_LEFT, null);
    }

    /**
     * Sets the "borderLeft" style attribute.
     * @param borderLeft the new attribute
     */
    public void jsxSet_borderLeft(final String borderLeft) {
        setStyleAttribute(BORDER_LEFT, borderLeft);
    }

    /**
     * Gets the "borderLeftColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderLeftColor() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BORDER_LEFT_COLOR, style);
        if (value.length() == 0) {
            value = findColor(getStyleAttribute(BORDER_LEFT, style));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER, style));
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
        setStyleAttribute(BORDER_LEFT_COLOR, borderLeftColor);
    }

    /**
     * Gets the "borderLeftStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderLeftStyle() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BORDER_LEFT_STYLE, style);
        if (value.length() == 0) {
            value = findBorderStyle(getStyleAttribute(BORDER_LEFT, style));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER, style));
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
        setStyleAttribute(BORDER_LEFT_STYLE, borderLeftStyle);
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
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BORDER + "-" + side + "-width", style);
        if (value.length() == 0) {
            value = findBorderWidth(getStyleAttribute(BORDER + "-" + side, style));
            if (value == null) {
                final String borderWidth = getStyleAttribute(BORDER_WIDTH, style);
                if (!StringUtils.isEmpty(borderWidth)) {
                    final String[] values = StringUtils.split(borderWidth);
                    if (values.length > side.ordinal()) {
                        value = values[side.ordinal()];
                    }
                }
            }
            if (value == null) {
                value = findBorderWidth(getStyleAttribute(BORDER, style));
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
        setStyleAttributePixel(BORDER_LEFT_WIDTH, borderLeftWidth);
    }

    /**
     * Gets the "borderRight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderRight() {
        return getStyleAttribute(BORDER_RIGHT, null);
    }

    /**
     * Sets the "borderRight" style attribute.
     * @param borderRight the new attribute
     */
    public void jsxSet_borderRight(final String borderRight) {
        setStyleAttribute(BORDER_RIGHT, borderRight);
    }

    /**
     * Gets the "borderRightColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderRightColor() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BORDER_RIGHT_COLOR, style);
        if (value.length() == 0) {
            value = findColor(getStyleAttribute(BORDER_RIGHT, style));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER, style));
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
        setStyleAttribute(BORDER_RIGHT_COLOR, borderRightColor);
    }

    /**
     * Gets the "borderRightStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderRightStyle() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BORDER_RIGHT_STYLE, style);
        if (value.length() == 0) {
            value = findBorderStyle(getStyleAttribute(BORDER_RIGHT, style));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER, style));
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
        setStyleAttribute(BORDER_RIGHT_STYLE, borderRightStyle);
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
        setStyleAttributePixel(BORDER_RIGHT_WIDTH, borderRightWidth);
    }

    /**
     * Gets the "borderSpacing" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderSpacing() {
        return getStyleAttribute(BORDER_SPACING, null);
    }

    /**
     * Sets the "borderSpacing" style attribute.
     * @param borderSpacing the new attribute
     */
    public void jsxSet_borderSpacing(final String borderSpacing) {
        setStyleAttribute(BORDER_SPACING, borderSpacing);
    }

    /**
     * Gets the "borderStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderStyle() {
        return getStyleAttribute(BORDER_STYLE, null);
    }

    /**
     * Sets the "borderStyle" style attribute.
     * @param borderStyle the new attribute
     */
    public void jsxSet_borderStyle(final String borderStyle) {
        setStyleAttribute(BORDER_STYLE, borderStyle);
    }

    /**
     * Gets the "borderTop" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderTop() {
        return getStyleAttribute(BORDER_TOP, null);
    }

    /**
     * Sets the "borderTop" style attribute.
     * @param borderTop the new attribute
     */
    public void jsxSet_borderTop(final String borderTop) {
        setStyleAttribute(BORDER_TOP, borderTop);
    }

    /**
     * Gets the "borderTopColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderTopColor() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BORDER_TOP_COLOR, style);
        if (value.length() == 0) {
            value = findColor(getStyleAttribute(BORDER_TOP, style));
            if (value == null) {
                value = findColor(getStyleAttribute(BORDER, style));
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
        setStyleAttribute(BORDER_TOP_COLOR, borderTopColor);
    }

    /**
     * Gets the "borderTopStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderTopStyle() {
        Map<String, StyleElement> style = null;
        if (styleDeclaration_ == null) {
            style = getStyleMap();
        }

        String value = getStyleAttribute(BORDER_TOP_STYLE, style);
        if (value.length() == 0) {
            value = findBorderStyle(getStyleAttribute(BORDER_TOP, style));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(BORDER, style));
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
        setStyleAttribute(BORDER_TOP_STYLE, borderTopStyle);
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
        setStyleAttributePixel(BORDER_TOP_WIDTH, borderTopWidth);
    }

    /**
     * Gets the "borderWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_borderWidth() {
        return getStyleAttribute(BORDER_WIDTH, null);
    }

    /**
     * Sets the "borderWidth" style attribute.
     * @param borderWidth the new attribute
     */
    public void jsxSet_borderWidth(final String borderWidth) {
        setStyleAttribute(BORDER_WIDTH, borderWidth);
    }

    /**
     * Gets the "bottom" style attribute.
     * @return the style attribute
     */
    public String jsxGet_bottom() {
        return getStyleAttribute(BOTTOM, null);
    }

    /**
     * Sets the "bottom" style attribute.
     * @param bottom the new attribute
     */
    public void jsxSet_bottom(final String bottom) {
        setStyleAttributePixel(BOTTOM, bottom);
    }

    /**
     * Gets the "captionSide" style attribute.
     * @return the style attribute
     */
    public String jsxGet_captionSide() {
        return getStyleAttribute(CAPTION_SIDE, null);
    }

    /**
     * Sets the "captionSide" style attribute.
     * @param captionSide the new attribute
     */
    public void jsxSet_captionSide(final String captionSide) {
        setStyleAttribute(CAPTION_SIDE, captionSide);
    }

    /**
     * Gets the "clear" style attribute.
     * @return the style attribute
     */
    public String jsxGet_clear() {
        return getStyleAttribute(CLEAR, null);
    }

    /**
     * Sets the "clear" style attribute.
     * @param clear the new attribute
     */
    public void jsxSet_clear(final String clear) {
        setStyleAttribute(CLEAR, clear);
    }

    /**
     * Gets the "clip" style attribute.
     * @return the style attribute
     */
    public String jsxGet_clip() {
        return getStyleAttribute(CLIP, null);
    }

    /**
     * Sets the "clip" style attribute.
     * @param clip the new attribute
     */
    public void jsxSet_clip(final String clip) {
        setStyleAttribute(CLIP, clip);
    }

    /**
     * Gets the "color" style attribute.
     * @return the style attribute
     */
    public String jsxGet_color() {
        return getStyleAttribute(COLOR, null);
    }

    /**
     * Sets the "color" style attribute.
     * @param color the new attribute
     */
    public void jsxSet_color(final String color) {
        setStyleAttribute(COLOR, color);
    }

    /**
     * Gets the "content" style attribute.
     * @return the style attribute
     */
    public String jsxGet_content() {
        return getStyleAttribute(CONTENT, null);
    }

    /**
     * Sets the "content" style attribute.
     * @param content the new attribute
     */
    public void jsxSet_content(final String content) {
        setStyleAttribute(CONTENT, content);
    }

    /**
     * Gets the "counterIncrement" style attribute.
     * @return the style attribute
     */
    public String jsxGet_counterIncrement() {
        return getStyleAttribute(COUNTER_INCREMENT, null);
    }

    /**
     * Sets the "counterIncrement" style attribute.
     * @param counterIncrement the new attribute
     */
    public void jsxSet_counterIncrement(final String counterIncrement) {
        setStyleAttribute(COUNTER_INCREMENT, counterIncrement);
    }

    /**
     * Gets the "counterReset" style attribute.
     * @return the style attribute
     */
    public String jsxGet_counterReset() {
        return getStyleAttribute(COUNTER_RESET, null);
    }

    /**
     * Sets the "counterReset" style attribute.
     * @param counterReset the new attribute
     */
    public void jsxSet_counterReset(final String counterReset) {
        setStyleAttribute(COUNTER_RESET, counterReset);
    }

    /**
     * Gets the "cssFloat" style attribute.
     * @return the style attribute
     */
    public String jsxGet_cssFloat() {
        return getStyleAttribute(FLOAT, null);
    }

    /**
     * Sets the "cssFloat" style attribute.
     * @param value the new attribute
     */
    public void jsxSet_cssFloat(final String value) {
        setStyleAttribute(FLOAT, value);
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
        return getStyleAttribute(CUE, null);
    }

    /**
     * Sets the "cue" style attribute.
     * @param cue the new attribute
     */
    public void jsxSet_cue(final String cue) {
        setStyleAttribute(CUE, cue);
    }

    /**
     * Gets the "cueAfter" style attribute.
     * @return the style attribute
     */
    public String jsxGet_cueAfter() {
        return getStyleAttribute(CUE_AFTER, null);
    }

    /**
     * Sets the "cueAfter" style attribute.
     * @param cueAfter the new attribute
     */
    public void jsxSet_cueAfter(final String cueAfter) {
        setStyleAttribute(CUE_AFTER, cueAfter);
    }

    /**
     * Gets the "cueBefore" style attribute.
     * @return the style attribute
     */
    public String jsxGet_cueBefore() {
        return getStyleAttribute(CUE_BEFORE, null);
    }

    /**
     * Sets the "cueBefore" style attribute.
     * @param cueBefore the new attribute
     */
    public void jsxSet_cueBefore(final String cueBefore) {
        setStyleAttribute(CUE_BEFORE, cueBefore);
    }

    /**
     * Gets the "cursor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_cursor() {
        return getStyleAttribute(CURSOR, null);
    }

    /**
     * Sets the "cursor" style attribute.
     * @param cursor the new attribute
     */
    public void jsxSet_cursor(final String cursor) {
        setStyleAttribute(CURSOR, cursor);
    }

    /**
     * Gets the "direction" style attribute.
     * @return the style attribute
     */
    public String jsxGet_direction() {
        return getStyleAttribute(DIRECTION, null);
    }

    /**
     * Sets the "direction" style attribute.
     * @param direction the new attribute
     */
    public void jsxSet_direction(final String direction) {
        setStyleAttribute(DIRECTION, direction);
    }

    /**
     * Gets the "display" style attribute.
     * @return the style attribute
     */
    public String jsxGet_display() {
        return getStyleAttribute(DISPLAY, null);
    }

    /**
     * Sets the "display" style attribute.
     * @param display the new attribute
     */
    public void jsxSet_display(final String display) {
        setStyleAttribute(DISPLAY, display);
    }

    /**
     * Gets the "elevation" style attribute.
     * @return the style attribute
     */
    public String jsxGet_elevation() {
        return getStyleAttribute(ELEVATION, null);
    }

    /**
     * Sets the "elevation" style attribute.
     * @param elevation the new attribute
     */
    public void jsxSet_elevation(final String elevation) {
        setStyleAttribute(ELEVATION, elevation);
    }

    /**
     * Gets the "emptyCells" style attribute.
     * @return the style attribute
     */
    public String jsxGet_emptyCells() {
        return getStyleAttribute(EMPTY_CELLS, null);
    }

    /**
     * Sets the "emptyCells" style attribute.
     * @param emptyCells the new attribute
     */
    public void jsxSet_emptyCells(final String emptyCells) {
        setStyleAttribute(EMPTY_CELLS, emptyCells);
    }

    /**
     * Gets the object's filter (IE only). See the <a
     * href="http://msdn2.microsoft.com/en-us/library/ms530752.aspx">MSDN documentation</a> for
     * more information.
     * @return the object's filter
     */
    public String jsxGet_filter() {
        return getStyleAttribute(FILTER, null);
    }

    /**
     * Sets the object's filter (IE only). See the <a
     * href="http://msdn2.microsoft.com/en-us/library/ms530752.aspx">MSDN documentation</a> for
     * more information.
     * @param filter the new filter
     */
    public void jsxSet_filter(final String filter) {
        setStyleAttribute(FILTER, filter);
    }

    /**
     * Gets the "font" style attribute.
     * @return the style attribute
     */
    public String jsxGet_font() {
        return getStyleAttribute(FONT, null);
    }

    /**
     * Sets the "font" style attribute.
     * @param font the new attribute
     */
    public void jsxSet_font(final String font) {
        setStyleAttribute(FONT, font);
    }

    /**
     * Gets the "fontFamily" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontFamily() {
        return getStyleAttribute(FONT_FAMILY, null);
    }

    /**
     * Sets the "fontFamily" style attribute.
     * @param fontFamily the new attribute
     */
    public void jsxSet_fontFamily(final String fontFamily) {
        setStyleAttribute(FONT_FAMILY, fontFamily);
    }

    /**
     * Gets the "fontSize" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontSize() {
        return getStyleAttribute(FONT_SIZE, null);
    }

    /**
     * Sets the "fontSize" style attribute.
     * @param fontSize the new attribute
     */
    public void jsxSet_fontSize(final String fontSize) {
        setStyleAttributePixel(FONT_SIZE, fontSize);
    }

    /**
     * Gets the "fontSizeAdjust" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontSizeAdjust() {
        return getStyleAttribute(FONT_SIZE_ADJUST, null);
    }

    /**
     * Sets the "fontSizeAdjust" style attribute.
     * @param fontSizeAdjust the new attribute
     */
    public void jsxSet_fontSizeAdjust(final String fontSizeAdjust) {
        setStyleAttribute(FONT_SIZE_ADJUST, fontSizeAdjust);
    }

    /**
     * Gets the "fontStretch" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontStretch() {
        return getStyleAttribute(FONT_STRETCH, null);
    }

    /**
     * Sets the "fontStretch" style attribute.
     * @param fontStretch the new attribute
     */
    public void jsxSet_fontStretch(final String fontStretch) {
        setStyleAttribute(FONT_STRETCH, fontStretch);
    }

    /**
     * Gets the "fontStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontStyle() {
        return getStyleAttribute(FONT_STYLE, null);
    }

    /**
     * Sets the "fontStyle" style attribute.
     * @param fontStyle the new attribute
     */
    public void jsxSet_fontStyle(final String fontStyle) {
        setStyleAttribute(FONT_STYLE, fontStyle);
    }

    /**
     * Gets the "fontVariant" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontVariant() {
        return getStyleAttribute(FONT_VARIANT, null);
    }

    /**
     * Sets the "fontVariant" style attribute.
     * @param fontVariant the new attribute
     */
    public void jsxSet_fontVariant(final String fontVariant) {
        setStyleAttribute(FONT_VARIANT, fontVariant);
    }

    /**
     * Gets the "fontWeight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_fontWeight() {
        return getStyleAttribute(FONT_WEIGHT, null);
    }

    /**
     * Sets the "fontWeight" style attribute.
     * @param fontWeight the new attribute
     */
    public void jsxSet_fontWeight(final String fontWeight) {
        setStyleAttribute(FONT_WEIGHT, fontWeight);
    }

    /**
     * Gets the "height" style attribute.
     * @return the style attribute
     */
    public String jsxGet_height() {
        return getStyleAttribute(HEIGHT, null);
    }

    /**
     * Sets the "height" style attribute.
     * @param height the new attribute
     */
    public void jsxSet_height(final String height) {
        setStyleAttributePixel(HEIGHT, height);
    }

    /**
     * Gets the "imeMode" style attribute.
     * @return the style attribute
     */
    public String jsxGet_imeMode() {
        return getStyleAttribute(IME_MODE, null);
    }

    /**
     * Sets the "imeMode" style attribute.
     * @param imeMode the new attribute
     */
    public void jsxSet_imeMode(final String imeMode) {
        setStyleAttribute(IME_MODE, imeMode);
    }

    /**
     * Gets the "layoutFlow" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutFlow() {
        return getStyleAttribute(LAYOUT_FLOW, null);
    }

    /**
     * Sets the "layoutFlow" style attribute.
     * @param layoutFlow the new attribute
     */
    public void jsxSet_layoutFlow(final String layoutFlow) {
        setStyleAttribute(LAYOUT_FLOW, layoutFlow);
    }

    /**
     * Gets the "layoutGrid" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutGrid() {
        return getStyleAttribute(LAYOUT_GRID, null);
    }

    /**
     * Sets the "layoutGrid" style attribute.
     * @param layoutGrid the new attribute
     */
    public void jsxSet_layoutGrid(final String layoutGrid) {
        setStyleAttribute(LAYOUT_GRID_CHAR, layoutGrid);
    }

    /**
     * Gets the "layoutGridChar" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutGridChar() {
        return getStyleAttribute(LAYOUT_GRID_CHAR, null);
    }

    /**
     * Sets the "layoutGridChar" style attribute.
     * @param layoutGridChar the new attribute
     */
    public void jsxSet_layoutGridChar(final String layoutGridChar) {
        setStyleAttribute(LAYOUT_GRID_CHAR, layoutGridChar);
    }

    /**
     * Gets the "layoutGridLine" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutGridLine() {
        return getStyleAttribute(LAYOUT_GRID_LINE, null);
    }

    /**
     * Sets the "layoutGridLine" style attribute.
     * @param layoutGridLine the new attribute
     */
    public void jsxSet_layoutGridLine(final String layoutGridLine) {
        setStyleAttribute(LAYOUT_GRID_LINE, layoutGridLine);
    }

    /**
     * Gets the "layoutGridMode" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutGridMode() {
        return getStyleAttribute(LAYOUT_GRID_MODE, null);
    }

    /**
     * Sets the "layoutGridMode" style attribute.
     * @param layoutGridMode the new attribute
     */
    public void jsxSet_layoutGridMode(final String layoutGridMode) {
        setStyleAttribute(LAYOUT_GRID_MODE, layoutGridMode);
    }

    /**
     * Gets the "layoutGridType" style attribute.
     * @return the style attribute
     */
    public String jsxGet_layoutGridType() {
        return getStyleAttribute(LAYOUT_GRID_TYPE, null);
    }

    /**
     * Sets the "layoutGridType" style attribute.
     * @param layoutGridType the new attribute
     */
    public void jsxSet_layoutGridType(final String layoutGridType) {
        setStyleAttribute(LAYOUT_GRID_TYPE, layoutGridType);
    }

    /**
     * Gets the "left" style attribute.
     * @return the style attribute
     */
    public String jsxGet_left() {
        return getStyleAttribute(LEFT, null);
    }

    /**
     * Sets the "left" style attribute.
     * @param left the new attribute
     */
    public void jsxSet_left(final String left) {
        setStyleAttributePixel(LEFT, left);
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
        return getStyleAttribute(LETTER_SPACING, null);
    }

    /**
     * Sets the "letterSpacing" style attribute.
     * @param letterSpacing the new attribute
     */
    public void jsxSet_letterSpacing(final String letterSpacing) {
        setStyleAttributePixel(LETTER_SPACING, letterSpacing);
    }

    /**
     * Gets the "lineBreak" style attribute.
     * @return the style attribute
     */
    public String jsxGet_lineBreak() {
        return getStyleAttribute(LINE_BREAK, null);
    }

    /**
     * Sets the "lineBreak" style attribute.
     * @param lineBreak the new attribute
     */
    public void jsxSet_lineBreak(final String lineBreak) {
        setStyleAttribute(LINE_BREAK, lineBreak);
    }

    /**
     * Gets the "lineHeight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_lineHeight() {
        return getStyleAttribute(LINE_HEIGHT, null);
    }

    /**
     * Sets the "lineHeight" style attribute.
     * @param lineHeight the new attribute
     */
    public void jsxSet_lineHeight(final String lineHeight) {
        setStyleAttribute(LINE_HEIGHT, lineHeight);
    }

    /**
     * Gets the "listStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_listStyle() {
        return getStyleAttribute(LIST_STYLE, null);
    }

    /**
     * Sets the "listStyle" style attribute.
     * @param listStyle the new attribute
     */
    public void jsxSet_listStyle(final String listStyle) {
        setStyleAttribute(LIST_STYLE, listStyle);
    }

    /**
     * Gets the "listStyleImage" style attribute.
     * @return the style attribute
     */
    public String jsxGet_listStyleImage() {
        return getStyleAttribute(LIST_STYLE_IMAGE, null);
    }

    /**
     * Sets the "listStyleImage" style attribute.
     * @param listStyleImage the new attribute
     */
    public void jsxSet_listStyleImage(final String listStyleImage) {
        setStyleAttribute(LIST_STYLE_IMAGE, listStyleImage);
    }

    /**
     * Gets the "listStylePosition" style attribute.
     * @return the style attribute
     */
    public String jsxGet_listStylePosition() {
        return getStyleAttribute(LIST_STYLE_POSITION, null);
    }

    /**
     * Sets the "listStylePosition" style attribute.
     * @param listStylePosition the new attribute
     */
    public void jsxSet_listStylePosition(final String listStylePosition) {
        setStyleAttribute(LIST_STYLE_POSITION, listStylePosition);
    }

    /**
     * Gets the "listStyleType" style attribute.
     * @return the style attribute
     */
    public String jsxGet_listStyleType() {
        return getStyleAttribute(LIST_STYLE_TYPE, null);
    }

    /**
     * Sets the "listStyleType" style attribute.
     * @param listStyleType the new attribute
     */
    public void jsxSet_listStyleType(final String listStyleType) {
        setStyleAttribute(LIST_STYLE_TYPE, listStyleType);
    }

    /**
     * Gets the "margin" style attribute.
     * @return the style attribute
     */
    public String jsxGet_margin() {
        return getStyleAttribute(MARGIN, null);
    }

    /**
     * Sets the "margin" style attribute.
     * @param margin the new attribute
     */
    public void jsxSet_margin(final String margin) {
        setStyleAttribute(MARGIN, margin);
    }

    /**
     * Gets the "marginBottom" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginBottom() {
        return getStyleAttribute(MARGIN_BOTTOM, MARGIN, Shorthand.BOTTOM);
    }

    /**
     * Sets the "marginBottom" style attribute.
     * @param marginBottom the new attribute
     */
    public void jsxSet_marginBottom(final String marginBottom) {
        setStyleAttributePixel(MARGIN_BOTTOM, marginBottom);
    }

    /**
     * Gets the "marginLeft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginLeft() {
        return getStyleAttribute(MARGIN_LEFT, MARGIN, Shorthand.LEFT);
    }

    /**
     * Sets the "marginLeft" style attribute.
     * @param marginLeft the new attribute
     */
    public void jsxSet_marginLeft(final String marginLeft) {
        setStyleAttributePixel(MARGIN_LEFT, marginLeft);
    }

    /**
     * Gets the "marginRight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginRight() {
        return getStyleAttribute(MARGIN_RIGHT, MARGIN, Shorthand.RIGHT);
    }

    /**
     * Sets the "marginRight" style attribute.
     * @param marginRight the new attribute
     */
    public void jsxSet_marginRight(final String marginRight) {
        setStyleAttributePixel(MARGIN_RIGHT, marginRight);
    }

    /**
     * Gets the "marginTop" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marginTop() {
        return getStyleAttribute(MARGIN_TOP, MARGIN, Shorthand.TOP);
    }

    /**
     * Sets the "marginTop" style attribute.
     * @param marginTop the new attribute
     */
    public void jsxSet_marginTop(final String marginTop) {
        setStyleAttributePixel(MARGIN_TOP, marginTop);
    }

    /**
     * Gets the "markerOffset" style attribute.
     * @return the style attribute
     */
    public String jsxGet_markerOffset() {
        return getStyleAttribute(MARKER_OFFSET, null);
    }

    /**
     * Sets the "markerOffset" style attribute.
     * @param markerOffset the new attribute
     */
    public void jsxSet_markerOffset(final String markerOffset) {
        setStyleAttribute(MARKER_OFFSET, markerOffset);
    }

    /**
     * Gets the "marks" style attribute.
     * @return the style attribute
     */
    public String jsxGet_marks() {
        return getStyleAttribute(MARKS, null);
    }

    /**
     * Sets the "marks" style attribute.
     * @param marks the new attribute
     */
    public void jsxSet_marks(final String marks) {
        setStyleAttribute(MARKS, marks);
    }

    /**
     * Gets the "maxHeight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_maxHeight() {
        return getStyleAttribute(MAX_HEIGHT, null);
    }

    /**
     * Sets the "maxHeight" style attribute.
     * @param maxHeight the new attribute
     */
    public void jsxSet_maxHeight(final String maxHeight) {
        setStyleAttributePixel(MAX_HEIGHT, maxHeight);
    }

    /**
     * Gets the "maxWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_maxWidth() {
        return getStyleAttribute(MAX_WIDTH, null);
    }

    /**
     * Sets the "maxWidth" style attribute.
     * @param maxWidth the new attribute
     */
    public void jsxSet_maxWidth(final String maxWidth) {
        setStyleAttributePixel(MAX_WIDTH, maxWidth);
    }

    /**
     * Gets the "minHeight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_minHeight() {
        return getStyleAttribute(MIN_HEIGHT, null);
    }

    /**
     * Sets the "minHeight" style attribute.
     * @param minHeight the new attribute
     */
    public void jsxSet_minHeight(final String minHeight) {
        setStyleAttributePixel(MIN_HEIGHT, minHeight);
    }

    /**
     * Gets the "minWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_minWidth() {
        return getStyleAttribute(MIN_WIDTH, null);
    }

    /**
     * Sets the "minWidth" style attribute.
     * @param minWidth the new attribute
     */
    public void jsxSet_minWidth(final String minWidth) {
        setStyleAttributePixel(MIN_WIDTH, minWidth);
    }

    /**
     * Gets the "MozAppearance" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozAppearance() {
        return getStyleAttribute(MOZ_APPEARANCE, null);
    }

    /**
     * Sets the "MozAppearance" style attribute.
     * @param mozAppearance the new attribute
     */
    public void jsxSet_MozAppearance(final String mozAppearance) {
        setStyleAttribute(MOZ_APPEARANCE, mozAppearance);
    }

    /**
     * Gets the "MozBackgroundClip" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBackgroundClip() {
        return getStyleAttribute(MOZ_BACKGROUND_CLIP, null);
    }

    /**
     * Sets the "MozBackgroundClip" style attribute.
     * @param mozBackgroundClip the new attribute
     */
    public void jsxSet_MozBackgroundClip(final String mozBackgroundClip) {
        setStyleAttribute(MOZ_BACKGROUND_CLIP, mozBackgroundClip);
    }

    /**
     * Gets the "MozBackgroundInlinePolicy" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBackgroundInlinePolicy() {
        return getStyleAttribute(MOZ_BACKGROUND_INLINE_POLICY, null);
    }

    /**
     * Sets the "MozBackgroundInlinePolicy" style attribute.
     * @param mozBackgroundInlinePolicy the new attribute
     */
    public void jsxSet_MozBackgroundInlinePolicy(final String mozBackgroundInlinePolicy) {
        setStyleAttribute(MOZ_BACKGROUND_INLINE_POLICY, mozBackgroundInlinePolicy);
    }

    /**
     * Gets the "MozBackgroundOrigin" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBackgroundOrigin() {
        return getStyleAttribute(MOZ_BACKGROUND_ORIGIN, null);
    }

    /**
     * Sets the "MozBackgroundOrigin" style attribute.
     * @param mozBackgroundOrigin the new attribute
     */
    public void jsxSet_MozBackgroundOrigin(final String mozBackgroundOrigin) {
        setStyleAttribute(MOZ_BACKGROUND_ORIGIN, mozBackgroundOrigin);
    }

    /**
     * Gets the "MozBinding" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBinding() {
        return getStyleAttribute(MOZ_BINDING, null);
    }

    /**
     * Sets the "MozBinding" style attribute.
     * @param mozBinding the new attribute
     */
    public void jsxSet_MozBinding(final String mozBinding) {
        setStyleAttribute(MOZ_BINDING, mozBinding);
    }

    /**
     * Gets the "MozBorderBottomColors" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderBottomColors() {
        return getStyleAttribute(MOZ_BORDER_BOTTOM_COLORS, null);
    }

    /**
     * Sets the "MozBorderBottomColors" style attribute.
     * @param mozBorderBottomColors the new attribute
     */
    public void jsxSet_MozBorderBottomColors(final String mozBorderBottomColors) {
        setStyleAttribute(MOZ_BORDER_BOTTOM_COLORS, mozBorderBottomColors);
    }

    /**
     * Gets the "MozBorderLeftColors" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderLeftColors() {
        return getStyleAttribute(MOZ_BORDER_LEFT_COLORS, null);
    }

    /**
     * Sets the "MozBorderLeftColors" style attribute.
     * @param mozBorderLeftColors the new attribute
     */
    public void jsxSet_MozBorderLeftColors(final String mozBorderLeftColors) {
        setStyleAttribute(MOZ_BORDER_LEFT_COLORS, mozBorderLeftColors);
    }

    /**
     * Gets the "MozBorderRadius" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRadius() {
        return getStyleAttribute(MOZ_BORDER_RADIUS, null);
    }

    /**
     * Sets the "MozBorderRadius" style attribute.
     * @param mozBorderRadius the new attribute
     */
    public void jsxSet_MozBorderRadius(final String mozBorderRadius) {
        setStyleAttribute(MOZ_BORDER_RADIUS, mozBorderRadius);
    }

    /**
     * Gets the "MozBorderRadiusBottomleft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRadiusBottomleft() {
        return getStyleAttribute(MOZ_BORDER_RADIUS_BOTTOMLEFT, null);
    }

    /**
     * Sets the "MozBorderRadiusBottomleft" style attribute.
     * @param mozBorderRadiusBottomleft the new attribute
     */
    public void jsxSet_MozBorderRadiusBottomleft(final String mozBorderRadiusBottomleft) {
        setStyleAttribute(MOZ_BORDER_RADIUS_BOTTOMLEFT, mozBorderRadiusBottomleft);
    }

    /**
     * Gets the "MozBorderRadiusBottomright" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRadiusBottomright() {
        return getStyleAttribute(MOZ_BORDER_RADIUS_BOTTOMRIGHT, null);
    }

    /**
     * Sets the "MozBorderRadiusBottomright" style attribute.
     * @param mozBorderRadiusBottomright the new attribute
     */
    public void jsxSet_MozBorderRadiusBottomright(final String mozBorderRadiusBottomright) {
        setStyleAttribute(MOZ_BORDER_RADIUS_BOTTOMRIGHT, mozBorderRadiusBottomright);
    }

    /**
     * Gets the "MozBorderRadiusTopleft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRadiusTopleft() {
        return getStyleAttribute(MOZ_BORDER_RADIUS_TOPLEFT, null);
    }

    /**
     * Sets the "MozBorderRadiusTopleft" style attribute.
     * @param mozBorderRadiusTopleft the new attribute
     */
    public void jsxSet_MozBorderRadiusTopleft(final String mozBorderRadiusTopleft) {
        setStyleAttribute(MOZ_BORDER_RADIUS_TOPLEFT, mozBorderRadiusTopleft);
    }

    /**
     * Gets the "MozBorderRadiusTopright" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRadiusTopright() {
        return getStyleAttribute(MOZ_BORDER_RADIUS_TOPRIGHT, null);
    }

    /**
     * Sets the "MozBorderRadiusTopright" style attribute.
     * @param mozBorderRadiusTopright the new attribute
     */
    public void jsxSet_MozBorderRadiusTopright(final String mozBorderRadiusTopright) {
        setStyleAttribute(MOZ_BORDER_RADIUS_TOPRIGHT, mozBorderRadiusTopright);
    }

    /**
     * Gets the "MozBorderRightColors" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderRightColors() {
        return getStyleAttribute(MOZ_BORDER_RIGHT_COLORS, null);
    }

    /**
     * Sets the "MozBorderRightColors" style attribute.
     * @param mozBorderRightColors the new attribute
     */
    public void jsxSet_MozBorderRightColors(final String mozBorderRightColors) {
        setStyleAttribute(MOZ_BORDER_RIGHT_COLORS, mozBorderRightColors);
    }

    /**
     * Gets the "MozBorderTopColors" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBorderTopColors() {
        return getStyleAttribute(MOZ_BORDER_TOP_COLORS, null);
    }

    /**
     * Sets the "MozBorderTopColors" style attribute.
     * @param mozBorderTopColors the new attribute
     */
    public void jsxSet_MozBorderTopColors(final String mozBorderTopColors) {
        setStyleAttribute(MOZ_BORDER_TOP_COLORS, mozBorderTopColors);
    }

    /**
     * Gets the "MozBoxAlign" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxAlign() {
        return getStyleAttribute(MOZ_BOX_ALIGN, null);
    }

    /**
     * Sets the "MozBoxAlign" style attribute.
     * @param mozBoxAlign the new attribute
     */
    public void jsxSet_MozBoxAlign(final String mozBoxAlign) {
        setStyleAttribute(MOZ_BOX_ALIGN, mozBoxAlign);
    }

    /**
     * Gets the "MozBoxDirection" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxDirection() {
        return getStyleAttribute(MOZ_BOX_DIRECTION, null);
    }

    /**
     * Sets the "MozBoxDirection" style attribute.
     * @param mozBoxDirection the new attribute
     */
    public void jsxSet_MozBoxDirection(final String mozBoxDirection) {
        setStyleAttribute(MOZ_BOX_DIRECTION, mozBoxDirection);
    }

    /**
     * Gets the "MozBoxFlex" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxFlex() {
        return getStyleAttribute(MOZ_BOX_FLEX, null);
    }

    /**
     * Sets the "MozBoxFlex" style attribute.
     * @param mozBoxFlex the new attribute
     */
    public void jsxSet_MozBoxFlex(final String mozBoxFlex) {
        setStyleAttribute(MOZ_BOX_FLEX, mozBoxFlex);
    }

    /**
     * Gets the "MozBoxOrdinalGroup" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxOrdinalGroup() {
        return getStyleAttribute(MOZ_BOX_ORDINAL_GROUP, null);
    }

    /**
     * Sets the "MozBoxOrdinalGroup" style attribute.
     * @param mozBoxOrdinalGroup the new attribute
     */
    public void jsxSet_MozBoxOrdinalGroup(final String mozBoxOrdinalGroup) {
        setStyleAttribute(MOZ_BOX_ORDINAL_GROUP, mozBoxOrdinalGroup);
    }

    /**
     * Gets the "MozBoxOrient" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxOrient() {
        return getStyleAttribute(MOZ_BOX_ORIENT, null);
    }

    /**
     * Sets the "MozBoxOrient" style attribute.
     * @param mozBoxOrient the new attribute
     */
    public void jsxSet_MozBoxOrient(final String mozBoxOrient) {
        setStyleAttribute(MOZ_BOX_ORIENT, mozBoxOrient);
    }

    /**
     * Gets the "MozBoxPack" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxPack() {
        return getStyleAttribute(MOZ_BOX_PACK, null);
    }

    /**
     * Sets the "MozBoxPack" style attribute.
     * @param mozBoxPack the new attribute
     */
    public void jsxSet_MozBoxPack(final String mozBoxPack) {
        setStyleAttribute(MOZ_BOX_PACK, mozBoxPack);
    }

    /**
     * Gets the "MozBoxSizing" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozBoxSizing() {
        return getStyleAttribute(MOZ_BOX_SIZING, null);
    }

    /**
     * Sets the "MozBoxSizing" style attribute.
     * @param mozBoxSizing the new attribute
     */
    public void jsxSet_MozBoxSizing(final String mozBoxSizing) {
        setStyleAttribute(MOZ_BOX_SIZING, mozBoxSizing);
    }

    /**
     * Gets the "MozColumnCount" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozColumnCount() {
        return getStyleAttribute(MOZ_COLUMN_COUNT, null);
    }

    /**
     * Sets the "MozColumnCount" style attribute.
     * @param mozColumnCount the new attribute
     */
    public void jsxSet_MozColumnCount(final String mozColumnCount) {
        setStyleAttribute(MOZ_COLUMN_COUNT, mozColumnCount);
    }

    /**
     * Gets the "MozColumnGap" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozColumnGap() {
        return getStyleAttribute(MOZ_COLUMN_GAP, null);
    }

    /**
     * Sets the "MozColumnGap" style attribute.
     * @param mozColumnGap the new attribute
     */
    public void jsxSet_MozColumnGap(final String mozColumnGap) {
        setStyleAttribute(MOZ_COLUMN_GAP, mozColumnGap);
    }

    /**
     * Gets the "MozColumnWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozColumnWidth() {
        return getStyleAttribute(MOZ_COLUMN_WIDTH, null);
    }

    /**
     * Sets the "MozColumnWidth" style attribute.
     * @param mozColumnWidth the new attribute
     */
    public void jsxSet_MozColumnWidth(final String mozColumnWidth) {
        setStyleAttribute(MOZ_COLUMN_WIDTH, mozColumnWidth);
    }

    /**
     * Gets the "MozFloatEdge" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozFloatEdge() {
        return getStyleAttribute(MOZ_FLOAT_EDGE, null);
    }

    /**
     * Sets the "MozFloatEdge" style attribute.
     * @param mozFloatEdge the new attribute
     */
    public void jsxSet_MozFloatEdge(final String mozFloatEdge) {
        setStyleAttribute(MOZ_FLOAT_EDGE, mozFloatEdge);
    }

    /**
     * Gets the "MozForceBrokenImageIcon" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozForceBrokenImageIcon() {
        return getStyleAttribute(MOZ_FORCE_BROKEN_IMAGE_ICON, null);
    }

    /**
     * Sets the "MozForceBrokenImageIcon" style attribute.
     * @param mozForceBrokenImageIcon the new attribute
     */
    public void jsxSet_MozForceBrokenImageIcon(final String mozForceBrokenImageIcon) {
        setStyleAttribute(MOZ_FORCE_BROKEN_IMAGE_ICON, mozForceBrokenImageIcon);
    }

    /**
     * Gets the "MozImageRegion" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozImageRegion() {
        return getStyleAttribute(MOZ_IMAGE_REGION, null);
    }

    /**
     * Sets the "MozImageRegion" style attribute.
     * @param mozImageRegion the new attribute
     */
    public void jsxSet_MozImageRegion(final String mozImageRegion) {
        setStyleAttribute(MOZ_IMAGE_REGION, mozImageRegion);
    }

    /**
     * Gets the "MozMarginEnd" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozMarginEnd() {
        return getStyleAttribute(MOZ_MARGIN_END, null);
    }

    /**
     * Sets the "MozMarginEnd" style attribute.
     * @param mozMarginEnd the new attribute
     */
    public void jsxSet_MozMarginEnd(final String mozMarginEnd) {
        setStyleAttribute(MOZ_MARGIN_END, mozMarginEnd);
    }

    /**
     * Gets the "MozMarginStart" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozMarginStart() {
        return getStyleAttribute(MOZ_MARGIN_START, null);
    }

    /**
     * Sets the "MozMarginStart" style attribute.
     * @param mozMarginStart the new attribute
     */
    public void jsxSet_MozMarginStart(final String mozMarginStart) {
        setStyleAttribute(MOZ_MARGIN_START, mozMarginStart);
    }

    /**
     * Gets the "MozOpacity" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOpacity() {
        return getStyleAttribute(MOZ_OPACITY, null);
    }

    /**
     * Sets the "MozOpacity" style attribute.
     * @param mozOpacity the new attribute
     */
    public void jsxSet_MozOpacity(final String mozOpacity) {
        setStyleAttribute(MOZ_OPACITY, mozOpacity);
    }

    /**
     * Gets the "MozOutline" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutline() {
        return getStyleAttribute(MOZ_OUTLINE, null);
    }

    /**
     * Sets the "MozOutline" style attribute.
     * @param mozOutline the new attribute
     */
    public void jsxSet_MozOutline(final String mozOutline) {
        setStyleAttribute(MOZ_OUTLINE, mozOutline);
    }

    /**
     * Gets the "MozOutlineColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineColor() {
        return getStyleAttribute(MOZ_OUTLINE_COLOR, null);
    }

    /**
     * Sets the "MozOutlineColor" style attribute.
     * @param mozOutlineColor the new attribute
     */
    public void jsxSet_MozOutlineColor(final String mozOutlineColor) {
        setStyleAttribute(MOZ_OUTLINE_COLOR, mozOutlineColor);
    }

    /**
     * Gets the "MozOutlineOffset" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineOffset() {
        return getStyleAttribute(MOZ_OUTLINE_OFFSET, null);
    }

    /**
     * Sets the "MozOutlineOffset" style attribute.
     * @param mozOutlineOffset the new attribute
     */
    public void jsxSet_MozOutlineOffset(final String mozOutlineOffset) {
        setStyleAttribute(MOZ_OUTLINE_OFFSET, mozOutlineOffset);
    }

    /**
     * Gets the "MozOutlineRadius" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineRadius() {
        return getStyleAttribute(MOZ_OUTLINE_RADIUS, null);
    }

    /**
     * Sets the "MozOutlineRadius" style attribute.
     * @param mozOutlineRadius the new attribute
     */
    public void jsxSet_MozOutlineRadius(final String mozOutlineRadius) {
        setStyleAttribute(MOZ_OUTLINE_RADIUS, mozOutlineRadius);
    }

    /**
     * Gets the "MozOutlineRadiusBottomleft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineRadiusBottomleft() {
        return getStyleAttribute(MOZ_OUTLINE_RADIUS_BOTTOMLEFT, null);
    }

    /**
     * Sets the "MozOutlineRadiusBottomleft" style attribute.
     * @param mozOutlineRadiusBottomleft the new attribute
     */
    public void jsxSet_MozOutlineRadiusBottomleft(final String mozOutlineRadiusBottomleft) {
        setStyleAttribute(MOZ_OUTLINE_RADIUS_BOTTOMLEFT, mozOutlineRadiusBottomleft);
    }

    /**
     * Gets the "MozOutlineRadiusBottomright" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineRadiusBottomright() {
        return getStyleAttribute(MOZ_OUTLINE_RADIUS_BOTTOMRIGHT, null);
    }

    /**
     * Sets the "MozOutlineRadiusBottomright" style attribute.
     * @param mozOutlineRadiusBottomright the new attribute
     */
    public void jsxSet_MozOutlineRadiusBottomright(final String mozOutlineRadiusBottomright) {
        setStyleAttribute(MOZ_OUTLINE_RADIUS_BOTTOMRIGHT, mozOutlineRadiusBottomright);
    }

    /**
     * Gets the "MozOutlineRadiusTopleft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineRadiusTopleft() {
        return getStyleAttribute(MOZ_OUTLINE_RADIUS_TOPLEFT, null);
    }

    /**
     * Sets the "MozOutlineRadiusTopleft" style attribute.
     * @param mozOutlineRadiusTopleft the new attribute
     */
    public void jsxSet_MozOutlineRadiusTopleft(final String mozOutlineRadiusTopleft) {
        setStyleAttribute(MOZ_OUTLINE_RADIUS_TOPLEFT, mozOutlineRadiusTopleft);
    }

    /**
     * Gets the "MozOutlineRadiusTopright" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineRadiusTopright() {
        return getStyleAttribute(MOZ_OUTLINE_RADIUS_TOPRIGHT, null);
    }

    /**
     * Sets the "MozOutlineRadiusTopright" style attribute.
     * @param mozOutlineRadiusTopright the new attribute
     */
    public void jsxSet_MozOutlineRadiusTopright(final String mozOutlineRadiusTopright) {
        setStyleAttribute(MOZ_OUTLINE_RADIUS_TOPRIGHT, mozOutlineRadiusTopright);
    }

    /**
     * Gets the "MozOutlineStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineStyle() {
        return getStyleAttribute(MOZ_OUTLINE_STYLE, null);
    }

    /**
     * Sets the "MozOutlineStyle" style attribute.
     * @param mozOutlineStyle the new attribute
     */
    public void jsxSet_MozOutlineStyle(final String mozOutlineStyle) {
        setStyleAttribute(MOZ_OUTLINE_STYLE, mozOutlineStyle);
    }

    /**
     * Gets the "MozOutlineWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozOutlineWidth() {
        return getStyleAttribute(MOZ_OUTLINE_WIDTH, null);
    }

    /**
     * Sets the "MozOutlineWidth" style attribute.
     * @param mozOutlineWidth the new attribute
     */
    public void jsxSet_MozOutlineWidth(final String mozOutlineWidth) {
        setStyleAttribute(MOZ_OUTLINE_WIDTH, mozOutlineWidth);
    }

    /**
     * Gets the "MozPaddingEnd" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozPaddingEnd() {
        return getStyleAttribute(MOZ_PADDING_END, null);
    }

    /**
     * Sets the "MozPaddingEnd" style attribute.
     * @param mozPaddingEnd the new attribute
     */
    public void jsxSet_MozPaddingEnd(final String mozPaddingEnd) {
        setStyleAttribute(MOZ_PADDING_END, mozPaddingEnd);
    }

    /**
     * Gets the "MozPaddingStart" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozPaddingStart() {
        return getStyleAttribute(MOZ_PADDING_START, null);
    }

    /**
     * Sets the "MozPaddingStart" style attribute.
     * @param mozPaddingStart the new attribute
     */
    public void jsxSet_MozPaddingStart(final String mozPaddingStart) {
        setStyleAttribute(MOZ_PADDING_START, mozPaddingStart);
    }

    /**
     * Gets the "MozUserFocus" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozUserFocus() {
        return getStyleAttribute(MOZ_USER_FOCUS, null);
    }

    /**
     * Sets the "MozUserFocus" style attribute.
     * @param mozUserFocus the new attribute
     */
    public void jsxSet_MozUserFocus(final String mozUserFocus) {
        setStyleAttribute(MOZ_USER_FOCUS, mozUserFocus);
    }

    /**
     * Gets the "MozUserInput" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozUserInput() {
        return getStyleAttribute(MOZ_USER_INPUT, null);
    }

    /**
     * Sets the "MozUserInput" style attribute.
     * @param mozUserInput the new attribute
     */
    public void jsxSet_MozUserInput(final String mozUserInput) {
        setStyleAttribute(MOZ_USER_INPUT, mozUserInput);
    }

    /**
     * Gets the "MozUserModify" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozUserModify() {
        return getStyleAttribute(MOZ_USER_MODIFY, null);
    }

    /**
     * Sets the "MozUserModify" style attribute.
     * @param mozUserModify the new attribute
     */
    public void jsxSet_MozUserModify(final String mozUserModify) {
        setStyleAttribute(MOZ_USER_MODIFY, mozUserModify);
    }

    /**
     * Gets the "MozUserSelect" style attribute.
     * @return the style attribute
     */
    public String jsxGet_MozUserSelect() {
        return getStyleAttribute(MOZ_USER_SELECT, null);
    }

    /**
     * Sets the "MozUserSelect" style attribute.
     * @param mozUserSelect the new attribute
     */
    public void jsxSet_MozUserSelect(final String mozUserSelect) {
        setStyleAttribute(MOZ_USER_SELECT, mozUserSelect);
    }

    /**
     * Gets the "msInterpolationMode" style attribute.
     * @return the style attribute
     */
    public String jsxGet_msInterpolationMode() {
        return getStyleAttribute(MS_INTERPOLATION_MODE, null);
    }

    /**
     * Sets the "msInterpolationMode" style attribute.
     * @param msInterpolationMode the new attribute
     */
    public void jsxSet_msInterpolationMode(final String msInterpolationMode) {
        setStyleAttribute(MS_INTERPOLATION_MODE, msInterpolationMode);
    }

    /**
     * Gets the "opacity" style attribute.
     * @return the style attribute
     */
    public String jsxGet_opacity() {
        final String opacity = getStyleAttribute(OPACITY, null);
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_OPACITY_ACCEPTS_ARBITRARY_VALUES)) {
            return opacity;
        }

        if (opacity == null || opacity.length() == 0) {
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
    public void jsxSet_opacity(final String opacity) {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_OPACITY_ACCEPTS_ARBITRARY_VALUES)) {
            setStyleAttribute(OPACITY, opacity);
            return;
        }

        if (opacity.length() == 0) {
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
    public String jsxGet_orphans() {
        return getStyleAttribute(ORPHANS, null);
    }

    /**
     * Sets the "orphans" style attribute.
     * @param orphans the new attribute
     */
    public void jsxSet_orphans(final String orphans) {
        setStyleAttribute(ORPHANS, orphans);
    }

    /**
     * Gets the "outline" style attribute.
     * @return the style attribute
     */
    public String jsxGet_outline() {
        return getStyleAttribute(OUTLINE, null);
    }

    /**
     * Sets the "outline" style attribute.
     * @param outline the new attribute
     */
    public void jsxSet_outline(final String outline) {
        setStyleAttribute(OUTLINE, outline);
    }

    /**
     * Gets the "outlineColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_outlineColor() {
        return getStyleAttribute(OUTLINE_COLOR, null);
    }

    /**
     * Sets the "outlineColor" style attribute.
     * @param outlineColor the new attribute
     */
    public void jsxSet_outlineColor(final String outlineColor) {
        setStyleAttribute(OUTLINE_COLOR, outlineColor);
    }

    /**
     * Gets the "outlineOffset" style attribute.
     * @return the style attribute
     */
    public String jsxGet_outlineOffset() {
        return getStyleAttribute(OUTLINE_OFFSET, null);
    }

    /**
     * Sets the "outlineOffset" style attribute.
     * @param outlineOffset the new attribute
     */
    public void jsxSet_outlineOffset(final String outlineOffset) {
        setStyleAttribute(OUTLINE_OFFSET, outlineOffset);
    }

    /**
     * Gets the "outlineStyle" style attribute.
     * @return the style attribute
     */
    public String jsxGet_outlineStyle() {
        return getStyleAttribute(OUTLINE_STYLE, null);
    }

    /**
     * Sets the "outlineStyle" style attribute.
     * @param outlineStyle the new attribute
     */
    public void jsxSet_outlineStyle(final String outlineStyle) {
        setStyleAttribute(OUTLINE_STYLE, outlineStyle);
    }

    /**
     * Gets the "outlineWidth" style attribute.
     * @return the style attribute
     */
    public String jsxGet_outlineWidth() {
        return getStyleAttribute(OUTLINE_WIDTH, null);
    }

    /**
     * Sets the "outlineWidth" style attribute.
     * @param outlineWidth the new attribute
     */
    public void jsxSet_outlineWidth(final String outlineWidth) {
        setStyleAttributePixel(OUTLINE_WIDTH, outlineWidth);
    }

    /**
     * Gets the "overflow" style attribute.
     * @return the style attribute
     */
    public String jsxGet_overflow() {
        return getStyleAttribute(OVERFLOW, null);
    }

    /**
     * Sets the "overflow" style attribute.
     * @param overflow the new attribute
     */
    public void jsxSet_overflow(final String overflow) {
        setStyleAttribute(OVERFLOW, overflow);
    }

    /**
     * Gets the "overflowX" style attribute.
     * @return the style attribute
     */
    public String jsxGet_overflowX() {
        return getStyleAttribute(OVERFLOW_X, null);
    }

    /**
     * Sets the "overflowX" style attribute.
     * @param overflowX the new attribute
     */
    public void jsxSet_overflowX(final String overflowX) {
        setStyleAttribute(OVERFLOW_X, overflowX);
    }

    /**
     * Gets the "overflowY" style attribute.
     * @return the style attribute
     */
    public String jsxGet_overflowY() {
        return getStyleAttribute(OVERFLOW_Y, null);
    }

    /**
     * Sets the "overflowY" style attribute.
     * @param overflowY the new attribute
     */
    public void jsxSet_overflowY(final String overflowY) {
        setStyleAttribute(OVERFLOW_Y, overflowY);
    }

    /**
     * Gets the "padding" style attribute.
     * @return the style attribute
     */
    public String jsxGet_padding() {
        return getStyleAttribute(PADDING, null);
    }

    /**
     * Sets the "padding" style attribute.
     * @param padding the new attribute
     */
    public void jsxSet_padding(final String padding) {
        setStyleAttribute(PADDING, padding);
    }

    /**
     * Gets the "paddingBottom" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingBottom() {
        return getStyleAttribute(PADDING_BOTTOM, PADDING, Shorthand.BOTTOM);
    }

    /**
     * Sets the "paddingBottom" style attribute.
     * @param paddingBottom the new attribute
     */
    public void jsxSet_paddingBottom(final String paddingBottom) {
        setStyleAttributePixel(PADDING_BOTTOM, paddingBottom);
    }

    /**
     * Gets the "paddingLeft" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingLeft() {
        return getStyleAttribute(PADDING_LEFT, PADDING, Shorthand.LEFT);
    }

    /**
     * Sets the "paddingLeft" style attribute.
     * @param paddingLeft the new attribute
     */
    public void jsxSet_paddingLeft(final String paddingLeft) {
        setStyleAttributePixel(PADDING_LEFT, paddingLeft);
    }

    /**
     * Gets the "paddingRight" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingRight() {
        return getStyleAttribute(PADDING_RIGHT, PADDING, Shorthand.RIGHT);
    }

    /**
     * Sets the "paddingRight" style attribute.
     * @param paddingRight the new attribute
     */
    public void jsxSet_paddingRight(final String paddingRight) {
        setStyleAttributePixel(PADDING_RIGHT, paddingRight);
    }

    /**
     * Gets the "paddingTop" style attribute.
     * @return the style attribute
     */
    public String jsxGet_paddingTop() {
        return getStyleAttribute(PADDING_TOP, PADDING, Shorthand.TOP);
    }

    /**
     * Sets the "paddingTop" style attribute.
     * @param paddingTop the new attribute
     */
    public void jsxSet_paddingTop(final String paddingTop) {
        setStyleAttributePixel(PADDING_TOP, paddingTop);
    }

    /**
     * Gets the "page" style attribute.
     * @return the style attribute
     */
    public String jsxGet_page() {
        return getStyleAttribute(PAGE, null);
    }

    /**
     * Sets the "page" style attribute.
     * @param page the new attribute
     */
    public void jsxSet_page(final String page) {
        setStyleAttribute(PAGE, page);
    }

    /**
     * Gets the "pageBreakAfter" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pageBreakAfter() {
        return getStyleAttribute(PAGE_BREAK_AFTER, null);
    }

    /**
     * Sets the "pageBreakAfter" style attribute.
     * @param pageBreakAfter the new attribute
     */
    public void jsxSet_pageBreakAfter(final String pageBreakAfter) {
        setStyleAttribute(PAGE_BREAK_AFTER, pageBreakAfter);
    }

    /**
     * Gets the "pageBreakBefore" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pageBreakBefore() {
        return getStyleAttribute(PAGE_BREAK_BEFORE, null);
    }

    /**
     * Sets the "pageBreakBefore" style attribute.
     * @param pageBreakBefore the new attribute
     */
    public void jsxSet_pageBreakBefore(final String pageBreakBefore) {
        setStyleAttribute(PAGE_BREAK_BEFORE, pageBreakBefore);
    }

    /**
     * Gets the "pageBreakInside" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pageBreakInside() {
        return getStyleAttribute(PAGE_BREAK_INSIDE, null);
    }

    /**
     * Sets the "pageBreakInside" style attribute.
     * @param pageBreakInside the new attribute
     */
    public void jsxSet_pageBreakInside(final String pageBreakInside) {
        setStyleAttribute(PAGE_BREAK_INSIDE, pageBreakInside);
    }

    /**
     * Gets the "pause" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pause() {
        return getStyleAttribute(PAUSE, null);
    }

    /**
     * Sets the "pause" style attribute.
     * @param pause the new attribute
     */
    public void jsxSet_pause(final String pause) {
        setStyleAttribute(PAUSE, pause);
    }

    /**
     * Gets the "pauseAfter" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pauseAfter() {
        return getStyleAttribute(PAUSE_AFTER, null);
    }

    /**
     * Sets the "pauseAfter" style attribute.
     * @param pauseAfter the new attribute
     */
    public void jsxSet_pauseAfter(final String pauseAfter) {
        setStyleAttribute(PAUSE_AFTER, pauseAfter);
    }

    /**
     * Gets the "pauseBefore" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pauseBefore() {
        return getStyleAttribute(PAUSE_BEFORE, null);
    }

    /**
     * Sets the "pauseBefore" style attribute.
     * @param pauseBefore the new attribute
     */
    public void jsxSet_pauseBefore(final String pauseBefore) {
        setStyleAttribute(PAUSE_BEFORE, pauseBefore);
    }

    /**
     * Gets the "pitch" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pitch() {
        return getStyleAttribute(PITCH, null);
    }

    /**
     * Sets the "pitch" style attribute.
     * @param pitch the new attribute
     */
    public void jsxSet_pitch(final String pitch) {
        setStyleAttribute(PITCH, pitch);
    }

    /**
     * Gets the "pitchRange" style attribute.
     * @return the style attribute
     */
    public String jsxGet_pitchRange() {
        return getStyleAttribute(PITCH_RANGE, null);
    }

    /**
     * Sets the "pitchRange" style attribute.
     * @param pitchRange the new attribute
     */
    public void jsxSet_pitchRange(final String pitchRange) {
        setStyleAttribute(PITCH_RANGE, pitchRange);
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
        return getStyleAttribute(POSITION, null);
    }

    /**
     * Sets the "position" style attribute.
     * @param position the new attribute
     */
    public void jsxSet_position(final String position) {
        setStyleAttribute(POSITION, position);
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
        return getStyleAttribute(QUOTES, null);
    }

    /**
     * Sets the "quotes" style attribute.
     * @param quotes the new attribute
     */
    public void jsxSet_quotes(final String quotes) {
        setStyleAttribute(QUOTES, quotes);
    }

    /**
     * Gets the "richness" style attribute.
     * @return the style attribute
     */
    public String jsxGet_richness() {
        return getStyleAttribute(RICHNESS, null);
    }

    /**
     * Sets the "richness" style attribute.
     * @param richness the new attribute
     */
    public void jsxSet_richness(final String richness) {
        setStyleAttribute(RICHNESS, richness);
    }

    /**
     * Gets the "right" style attribute.
     * @return the style attribute
     */
    public String jsxGet_right() {
        return getStyleAttribute(RIGHT, null);
    }

    /**
     * Sets the "right" style attribute.
     * @param right the new attribute
     */
    public void jsxSet_right(final String right) {
        setStyleAttributePixel(RIGHT, right);
    }

    /**
     * Gets the "rubyAlign" style attribute.
     * @return the style attribute
     */
    public String jsxGet_rubyAlign() {
        return getStyleAttribute(RUBY_ALIGN, null);
    }

    /**
     * Sets the "rubyAlign" style attribute.
     * @param rubyAlign the new attribute
     */
    public void jsxSet_rubyAlign(final String rubyAlign) {
        setStyleAttribute(RUBY_ALIGN, rubyAlign);
    }

    /**
     * Gets the "rubyOverhang" style attribute.
     * @return the style attribute
     */
    public String jsxGet_rubyOverhang() {
        return getStyleAttribute(RUBY_OVERHANG, null);
    }

    /**
     * Sets the "rubyOverhang" style attribute.
     * @param rubyOverhang the new attribute
     */
    public void jsxSet_rubyOverhang(final String rubyOverhang) {
        setStyleAttribute(RUBY_OVERHANG, rubyOverhang);
    }

    /**
     * Gets the "rubyPosition" style attribute.
     * @return the style attribute
     */
    public String jsxGet_rubyPosition() {
        return getStyleAttribute(RUBY_POSITION, null);
    }

    /**
     * Sets the "rubyPosition" style attribute.
     * @param rubyPosition the new attribute
     */
    public void jsxSet_rubyPosition(final String rubyPosition) {
        setStyleAttribute(RUBY_POSITION, rubyPosition);
    }

    /**
     * Gets the "scrollbar3dLightColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbar3dLightColor() {
        return getStyleAttribute(SCROLLBAR3D_LIGHT_COLOR, null);
    }

    /**
     * Sets the "scrollbar3dLightColor" style attribute.
     * @param scrollbar3dLightColor the new attribute
     */
    public void jsxSet_scrollbar3dLightColor(final String scrollbar3dLightColor) {
        setStyleAttribute(SCROLLBAR3D_LIGHT_COLOR, scrollbar3dLightColor);
    }

    /**
     * Gets the "scrollbarArrowColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarArrowColor() {
        return getStyleAttribute(SCROLLBAR_ARROW_COLOR, null);
    }

    /**
     * Sets the "scrollbarArrowColor" style attribute.
     * @param scrollbarArrowColor the new attribute
     */
    public void jsxSet_scrollbarArrowColor(final String scrollbarArrowColor) {
        setStyleAttribute(SCROLLBAR_ARROW_COLOR, scrollbarArrowColor);
    }

    /**
     * Gets the "scrollbarBaseColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarBaseColor() {
        return getStyleAttribute(SCROLLBAR_BASE_COLOR, null);
    }

    /**
     * Sets the "scrollbarBaseColor" style attribute.
     * @param scrollbarBaseColor the new attribute
     */
    public void jsxSet_scrollbarBaseColor(final String scrollbarBaseColor) {
        setStyleAttribute(SCROLLBAR_BASE_COLOR, scrollbarBaseColor);
    }

    /**
     * Gets the "scrollbarDarkShadowColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarDarkShadowColor() {
        return getStyleAttribute(SCROLLBAR_DARK_SHADOW_COLOR, null);
    }

    /**
     * Sets the "scrollbarDarkShadowColor" style attribute.
     * @param scrollbarDarkShadowColor the new attribute
     */
    public void jsxSet_scrollbarDarkShadowColor(final String scrollbarDarkShadowColor) {
        setStyleAttribute(SCROLLBAR_DARK_SHADOW_COLOR, scrollbarDarkShadowColor);
    }

    /**
     * Gets the "scrollbarFaceColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarFaceColor() {
        return getStyleAttribute(SCROLLBAR_FACE_COLOR, null);
    }

    /**
     * Sets the "scrollbarFaceColor" style attribute.
     * @param scrollbarFaceColor the new attribute
     */
    public void jsxSet_scrollbarFaceColor(final String scrollbarFaceColor) {
        setStyleAttribute(SCROLLBAR_FACE_COLOR, scrollbarFaceColor);
    }

    /**
     * Gets the "scrollbarHighlightColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarHighlightColor() {
        return getStyleAttribute(SCROLLBAR_HIGHLIGHT_COLOR, null);
    }

    /**
     * Sets the "scrollbarHighlightColor" style attribute.
     * @param scrollbarHighlightColor the new attribute
     */
    public void jsxSet_scrollbarHighlightColor(final String scrollbarHighlightColor) {
        setStyleAttribute(SCROLLBAR_HIGHLIGHT_COLOR, scrollbarHighlightColor);
    }

    /**
     * Gets the "scrollbarShadowColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarShadowColor() {
        return getStyleAttribute(SCROLLBAR_SHADOW_COLOR, null);
    }

    /**
     * Sets the "scrollbarShadowColor" style attribute.
     * @param scrollbarShadowColor the new attribute
     */
    public void jsxSet_scrollbarShadowColor(final String scrollbarShadowColor) {
        setStyleAttribute(SCROLLBAR_SHADOW_COLOR, scrollbarShadowColor);
    }

    /**
     * Gets the "scrollbarTrackColor" style attribute.
     * @return the style attribute
     */
    public String jsxGet_scrollbarTrackColor() {
        return getStyleAttribute(SCROLLBAR_TRACK_COLOR, null);
    }

    /**
     * Sets the "scrollbarTrackColor" style attribute.
     * @param scrollbarTrackColor the new attribute
     */
    public void jsxSet_scrollbarTrackColor(final String scrollbarTrackColor) {
        setStyleAttribute(SCROLLBAR_TRACK_COLOR, scrollbarTrackColor);
    }

    /**
     * Gets the "size" style attribute.
     * @return the style attribute
     */
    public String jsxGet_size() {
        return getStyleAttribute(SIZE, null);
    }

    /**
     * Sets the "size" style attribute.
     * @param size the new attribute
     */
    public void jsxSet_size(final String size) {
        setStyleAttribute(SIZE, size);
    }

    /**
     * Gets the "speak" style attribute.
     * @return the style attribute
     */
    public String jsxGet_speak() {
        return getStyleAttribute(SPEAK, null);
    }

    /**
     * Sets the "speak" style attribute.
     * @param speak the new attribute
     */
    public void jsxSet_speak(final String speak) {
        setStyleAttribute(SPEAK, speak);
    }

    /**
     * Gets the "speakHeader" style attribute.
     * @return the style attribute
     */
    public String jsxGet_speakHeader() {
        return getStyleAttribute(SPEAK_HEADER, null);
    }

    /**
     * Sets the "speakHeader" style attribute.
     * @param speakHeader the new attribute
     */
    public void jsxSet_speakHeader(final String speakHeader) {
        setStyleAttribute(SPEAK_HEADER, speakHeader);
    }

    /**
     * Gets the "speakNumeral" style attribute.
     * @return the style attribute
     */
    public String jsxGet_speakNumeral() {
        return getStyleAttribute(SPEAK_NUMERAL, null);
    }

    /**
     * Sets the "speakNumeral" style attribute.
     * @param speakNumeral the new attribute
     */
    public void jsxSet_speakNumeral(final String speakNumeral) {
        setStyleAttribute(SPEAK_NUMERAL, speakNumeral);
    }

    /**
     * Gets the "speakPunctuation" style attribute.
     * @return the style attribute
     */
    public String jsxGet_speakPunctuation() {
        return getStyleAttribute(SPEAK_PUNCTUATION, null);
    }

    /**
     * Sets the "speakPunctuation" style attribute.
     * @param speakPunctuation the new attribute
     */
    public void jsxSet_speakPunctuation(final String speakPunctuation) {
        setStyleAttribute(SPEAK_PUNCTUATION, speakPunctuation);
    }

    /**
     * Gets the "speechRate" style attribute.
     * @return the style attribute
     */
    public String jsxGet_speechRate() {
        return getStyleAttribute(SPEECH_RATE, null);
    }

    /**
     * Sets the "speechRate" style attribute.
     * @param speechRate the new attribute
     */
    public void jsxSet_speechRate(final String speechRate) {
        setStyleAttribute(SPEECH_RATE, speechRate);
    }

    /**
     * Gets the "stress" style attribute.
     * @return the style attribute
     */
    public String jsxGet_stress() {
        return getStyleAttribute(STRESS, null);
    }

    /**
     * Sets the "stress" style attribute.
     * @param stress the new attribute
     */
    public void jsxSet_stress(final String stress) {
        setStyleAttribute(STRESS, stress);
    }

    /**
     * Gets the "styleFloat" style attribute.
     * @return the style attribute
     */
    public String jsxGet_styleFloat() {
        return getStyleAttribute(FLOAT, null);
    }

    /**
     * Sets the "styleFloat" style attribute.
     * @param value the new attribute
     */
    public void jsxSet_styleFloat(final String value) {
        setStyleAttribute(FLOAT, value);
    }

    /**
     * Gets the "tableLayout" style attribute.
     * @return the style attribute
     */
    public String jsxGet_tableLayout() {
        return getStyleAttribute(TABLE_LAYOUT, null);
    }

    /**
     * Sets the "tableLayout" style attribute.
     * @param tableLayout the new attribute
     */
    public void jsxSet_tableLayout(final String tableLayout) {
        setStyleAttribute(TABLE_LAYOUT, tableLayout);
    }

    /**
     * Gets the "textAlign" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textAlign() {
        return getStyleAttribute(TEXT_ALIGN, null);
    }

    /**
     * Sets the "textAlign" style attribute.
     * @param textAlign the new attribute
     */
    public void jsxSet_textAlign(final String textAlign) {
        setStyleAttribute(TEXT_ALIGN, textAlign);
    }

    /**
     * Gets the "textAlignLast" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textAlignLast() {
        return getStyleAttribute(TEXT_ALIGN_LAST, null);
    }

    /**
     * Sets the "textAlignLast" style attribute.
     * @param textAlignLast the new attribute
     */
    public void jsxSet_textAlignLast(final String textAlignLast) {
        setStyleAttribute(TEXT_ALIGN_LAST, textAlignLast);
    }

    /**
     * Gets the "textAutospace" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textAutospace() {
        return getStyleAttribute(TEXT_AUTOSPACE, null);
    }

    /**
     * Sets the "textAutospace" style attribute.
     * @param textAutospace the new attribute
     */
    public void jsxSet_textAutospace(final String textAutospace) {
        setStyleAttribute(TEXT_AUTOSPACE, textAutospace);
    }

    /**
     * Gets the "textDecoration" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textDecoration() {
        return getStyleAttribute(TEXT_DECORATION, null);
    }

    /**
     * Sets the "textDecoration" style attribute.
     * @param textDecoration the new attribute
     */
    public void jsxSet_textDecoration(final String textDecoration) {
        setStyleAttribute(TEXT_DECORATION, textDecoration);
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
        return getStyleAttribute(TEXT_INDENT, null);
    }

    /**
     * Sets the "textIndent" style attribute.
     * @param textIndent the new attribute
     */
    public void jsxSet_textIndent(final String textIndent) {
        setStyleAttributePixel(TEXT_INDENT, textIndent);
    }

    /**
     * Gets the "textJustify" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textJustify() {
        return getStyleAttribute(TEXT_JUSTIFY, null);
    }

    /**
     * Sets the "textJustify" style attribute.
     * @param textJustify the new attribute
     */
    public void jsxSet_textJustify(final String textJustify) {
        setStyleAttribute(TEXT_JUSTIFY, textJustify);
    }

    /**
     * Gets the "textJustifyTrim" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textJustifyTrim() {
        return getStyleAttribute(TEXT_JUSTIFY_TRIM, null);
    }

    /**
     * Sets the "textJustifyTrim" style attribute.
     * @param textJustifyTrim the new attribute
     */
    public void jsxSet_textJustifyTrim(final String textJustifyTrim) {
        setStyleAttribute(TEXT_JUSTIFY_TRIM, textJustifyTrim);
    }

    /**
     * Gets the "textKashida" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textKashida() {
        return getStyleAttribute(TEXT_KASHIDA, null);
    }

    /**
     * Sets the "textKashida" style attribute.
     * @param textKashida the new attribute
     */
    public void jsxSet_textKashida(final String textKashida) {
        setStyleAttribute(TEXT_KASHIDA, textKashida);
    }

    /**
     * Gets the "textKashidaSpace" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textKashidaSpace() {
        return getStyleAttribute(TEXT_KASHIDA_SPACE, null);
    }

    /**
     * Sets the "textKashidaSpace" style attribute.
     * @param textKashidaSpace the new attribute
     */
    public void jsxSet_textKashidaSpace(final String textKashidaSpace) {
        setStyleAttribute(TEXT_KASHIDA_SPACE, textKashidaSpace);
    }

    /**
     * Gets the "textOverflow" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textOverflow() {
        return getStyleAttribute(TEXT_OVERFLOW, null);
    }

    /**
     * Sets the "textOverflow" style attribute.
     * @param textOverflow the new attribute
     */
    public void jsxSet_textOverflow(final String textOverflow) {
        setStyleAttribute(TEXT_OVERFLOW, textOverflow);
    }

    /**
     * Gets the "textShadow" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textShadow() {
        return getStyleAttribute(TEXT_SHADOW, null);
    }

    /**
     * Sets the "textShadow" style attribute.
     * @param textShadow the new attribute
     */
    public void jsxSet_textShadow(final String textShadow) {
        setStyleAttribute(TEXT_SHADOW, textShadow);
    }

    /**
     * Gets the "textTransform" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textTransform() {
        return getStyleAttribute(TEXT_TRANSFORM, null);
    }

    /**
     * Sets the "textTransform" style attribute.
     * @param textTransform the new attribute
     */
    public void jsxSet_textTransform(final String textTransform) {
        setStyleAttribute(TEXT_TRANSFORM, textTransform);
    }

    /**
     * Gets the "textUnderlinePosition" style attribute.
     * @return the style attribute
     */
    public String jsxGet_textUnderlinePosition() {
        return getStyleAttribute(TEXT_UNDERLINE_POSITION, null);
    }

    /**
     * Sets the "textUnderlinePosition" style attribute.
     * @param textUnderlinePosition the new attribute
     */
    public void jsxSet_textUnderlinePosition(final String textUnderlinePosition) {
        setStyleAttribute(TEXT_UNDERLINE_POSITION, textUnderlinePosition);
    }

    /**
     * Gets the "top" style attribute.
     * @return the style attribute
     */
    public String jsxGet_top() {
        return getStyleAttribute(TOP, null);
    }

    /**
     * Sets the "top" style attribute.
     * @param top the new attribute
     */
    public void jsxSet_top(final String top) {
        setStyleAttributePixel(TOP, top);
    }

    /**
     * Gets the "unicodeBidi" style attribute.
     * @return the style attribute
     */
    public String jsxGet_unicodeBidi() {
        return getStyleAttribute(UNICODE_BIDI, null);
    }

    /**
     * Sets the "unicodeBidi" style attribute.
     * @param unicodeBidi the new attribute
     */
    public void jsxSet_unicodeBidi(final String unicodeBidi) {
        setStyleAttribute(UNICODE_BIDI, unicodeBidi);
    }

    /**
     * Gets the "verticalAlign" style attribute.
     * @return the style attribute
     */
    public String jsxGet_verticalAlign() {
        return getStyleAttribute(VERTICAL_ALIGN, null);
    }

    /**
     * Sets the "verticalAlign" style attribute.
     * @param verticalAlign the new attribute
     */
    public void jsxSet_verticalAlign(final String verticalAlign) {
        setStyleAttributePixel(VERTICAL_ALIGN, verticalAlign);
    }

    /**
     * Gets the "visibility" style attribute.
     * @return the style attribute
     */
    public String jsxGet_visibility() {
        return getStyleAttribute(VISIBILITY, null);
    }

    /**
     * Sets the "visibility" style attribute.
     * @param visibility the new attribute
     */
    public void jsxSet_visibility(final String visibility) {
        setStyleAttribute(VISIBILITY, visibility);
    }

    /**
     * Gets the "voiceFamily" style attribute.
     * @return the style attribute
     */
    public String jsxGet_voiceFamily() {
        return getStyleAttribute(VOICE_FAMILY, null);
    }

    /**
     * Sets the "voiceFamily" style attribute.
     * @param voiceFamily the new attribute
     */
    public void jsxSet_voiceFamily(final String voiceFamily) {
        setStyleAttribute(VOICE_FAMILY, voiceFamily);
    }

    /**
     * Gets the "volume" style attribute.
     * @return the style attribute
     */
    public String jsxGet_volume() {
        return getStyleAttribute(VOLUME, null);
    }

    /**
     * Sets the "volume" style attribute.
     * @param volume the new attribute
     */
    public void jsxSet_volume(final String volume) {
        setStyleAttribute(VOLUME, volume);
    }

    /**
     * Gets the "whiteSpace" style attribute.
     * @return the style attribute
     */
    public String jsxGet_whiteSpace() {
        return getStyleAttribute(WHITE_SPACE, null);
    }

    /**
     * Sets the "whiteSpace" style attribute.
     * @param whiteSpace the new attribute
     */
    public void jsxSet_whiteSpace(final String whiteSpace) {
        setStyleAttribute(WHITE_SPACE, whiteSpace);
    }

    /**
     * Gets the "widows" style attribute.
     * @return the style attribute
     */
    public String jsxGet_widows() {
        return getStyleAttribute(WIDOWS, null);
    }

    /**
     * Sets the "widows" style attribute.
     * @param widows the new attribute
     */
    public void jsxSet_widows(final String widows) {
        setStyleAttribute(WIDOWS, widows);
    }

    /**
     * Gets the "width" style attribute.
     * @return the style attribute
     */
    public String jsxGet_width() {
        return getStyleAttribute(WIDTH, null);
    }

    /**
     * Sets the "width" style attribute.
     * @param width the new attribute
     */
    public void jsxSet_width(final String width) {
        setStyleAttributePixel(WIDTH, width);
    }

    /**
     * Gets the "wordBreak" style attribute.
     * @return the style attribute
     */
    public String jsxGet_wordBreak() {
        return getStyleAttribute(WORD_BREAK, null);
    }

    /**
     * Sets the "wordBreak" style attribute.
     * @param wordBreak the new attribute
     */
    public void jsxSet_wordBreak(final String wordBreak) {
        setStyleAttribute(WORD_BREAK, wordBreak);
    }

    /**
     * Gets the "wordSpacing" style attribute.
     * @return the style attribute
     */
    public String jsxGet_wordSpacing() {
        return getStyleAttribute(WORD_SPACING, null);
    }

    /**
     * Sets the "wordSpacing" style attribute.
     * @param wordSpacing the new attribute
     */
    public void jsxSet_wordSpacing(final String wordSpacing) {
        setStyleAttributePixel(WORD_SPACING, wordSpacing);
    }

    /**
     * Gets the "wordWrap" style attribute.
     * @return the style attribute
     */
    public String jsxGet_wordWrap() {
        return getStyleAttribute(WORD_WRAP, null);
    }

    /**
     * Sets the "wordWrap" style attribute.
     * @param wordWrap the new attribute
     */
    public void jsxSet_wordWrap(final String wordWrap) {
        setStyleAttribute(WORD_WRAP, wordWrap);
    }

    /**
     * Gets the "writingMode" style attribute.
     * @return the style attribute
     */
    public String jsxGet_writingMode() {
        return getStyleAttribute(WRITING_MODE, null);
    }

    /**
     * Sets the "writingMode" style attribute.
     * @param writingMode the new attribute
     */
    public void jsxSet_writingMode(final String writingMode) {
        setStyleAttribute(WRITING_MODE, writingMode);
    }

    /**
     * Gets the "zIndex" style attribute.
     * @return the style attribute
     */
    public Object jsxGet_zIndex() {
        final String value = getStyleAttribute(Z_INDEX, null);
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_ZINDEX_TYPE_NUMBER)) {
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
    public void jsxSet_zIndex(final Object zIndex) {
        if (zIndex == null
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_ZINDEX_UNDEFINED_OR_NULL_THROWS_ERROR)) {
            throw new EvaluatorException("Null is invalid for z-index.");
        }

        // empty
        if (zIndex == null || StringUtils.isEmpty(zIndex.toString())) {
            setStyleAttribute(Z_INDEX, "");
            return;
        }
        // undefined
        if (Context.getUndefinedValue().equals(zIndex)) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_ZINDEX_UNDEFINED_OR_NULL_THROWS_ERROR)) {
                throw new EvaluatorException("Undefind is invalid for z-index.");
            }
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_ZINDEX_UNDEFINED_FORCES_RESET)) {
                setStyleAttribute(Z_INDEX, "");
            }
            return;
        }

        // numeric (IE)
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_ZINDEX_TYPE_NUMBER)) {
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
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_ZINDEX_ROUNDED)) {
                setStyleAttribute(Z_INDEX, Integer.toString(Math.round(d.floatValue() - 0.00001f)));
            }
            else {
                setStyleAttribute(Z_INDEX, Integer.toString(d.intValue()));
            }
            return;
        }

        // string (FF)
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
    public String jsxGet_zoom() {
        return getStyleAttribute(ZOOM, null);
    }

    /**
     * Sets the "zoom" style attribute.
     * @param zoom the new attribute
     */
    public void jsxSet_zoom(final String zoom) {
        setStyleAttribute(ZOOM, zoom);
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
        return getStyleAttribute(name, null);
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
            final String uri = this.<DomNode>getDomNodeOrDie().getPage().getWebResponse().getWebRequest()
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
    public String jsxFunction_removeProperty(final String name) {
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
            return getStyleAttribute(name, null);
        }

        // Case-insensitive.
        final Map<String, StyleElement> map = getStyleMap();
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
            final Map<String, StyleElement> map = getStyleMap();
            for (final String key : map.keySet()) {
                if (key.equalsIgnoreCase(name)) {
                    setStyleAttribute(key, value);
                }
            }
        }
        else {
            // Case-sensitive.
            if (getStyleAttribute(name, null).length() > 0) {
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
            final Map<String, StyleElement> map = getStyleMap();
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
        final String s = getStyleAttribute(name, null);
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
                if (getBrowserVersion().
                        hasFeature(BrowserVersionFeatures.JS_GET_BACKGROUND_COLOR_FOR_COMPUTED_STYLE_RETURNS_RGB)) {
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
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_IMAGE_URL_QUOTED)) {
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
        if (s.endsWith("%") || (s.length() == 0 && element instanceof HTMLHtmlElement)) {
            final int i = NumberUtils.toInt(TO_INT_PATTERN.matcher(s).replaceAll("$1"), 100);
            final Element parent = element.getParentElement();
            final int absoluteValue = (parent == null) ? value.getWindowDefaultValue() : pixelValue(parent, value);
            return (int) ((i / 100D) * absoluteValue);
        }
        else if (s.length() == 0 && element instanceof HTMLCanvasElement) {
            return value.getWindowDefaultValue();
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
        final Integer result = pixelValuesCache_.get(value);
        if (null != result) {
            return result.intValue();
        }

        int i = NumberUtils.toInt(TO_INT_PATTERN.matcher(value).replaceAll("$1"), 0);
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

        pixelValuesCache_.put(value, Integer.valueOf(i));
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
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.CSS_PIXEL_VALUES_INT_ONLY)) {
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
