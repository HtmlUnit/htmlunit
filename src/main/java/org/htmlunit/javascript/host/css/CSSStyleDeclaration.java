/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.css;

import static org.htmlunit.BrowserVersionFeatures.CSS_BACKGROUND_INITIAL;
import static org.htmlunit.BrowserVersionFeatures.JS_STYLE_LETTER_SPACING_ACCEPTS_PERCENT;
import static org.htmlunit.BrowserVersionFeatures.JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT;
import static org.htmlunit.css.CssStyleSheet.ABSOLUTE;
import static org.htmlunit.css.CssStyleSheet.AUTO;
import static org.htmlunit.css.CssStyleSheet.FIXED;
import static org.htmlunit.css.CssStyleSheet.INHERIT;
import static org.htmlunit.css.CssStyleSheet.INITIAL;
import static org.htmlunit.css.CssStyleSheet.RELATIVE;
import static org.htmlunit.css.CssStyleSheet.STATIC;
import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.css.AbstractCssStyleDeclaration;
import org.htmlunit.css.StyleAttributes;
import org.htmlunit.css.StyleAttributes.Definition;
import org.htmlunit.css.StyleElement;
import org.htmlunit.css.WrappedCssStyleDeclaration;
import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.cssparser.dom.DOMExceptionImpl;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.javascript.host.Element;
import org.htmlunit.util.StringUtils;

/**
 * A JavaScript object for {@code CSSStyleDeclaration}.
 *
 * @author Mike Bowler
 * @author Christian Sell
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Rodney Gitzel
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 * @author Dennis Duysak
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/CSSStyleDeclaration">MDN doc</a>
 */
@JsxClass
public class CSSStyleDeclaration extends HtmlUnitScriptable {

    private static final Set<String> LENGTH_PROPERTIES_FFFF = new HashSet<>(Arrays.asList(
            Definition.BORDER_TOP_WIDTH.getAttributeName(),
            Definition.BORDER_LEFT_WIDTH.getAttributeName(),
            Definition.BORDER_BOTTOM_WIDTH.getAttributeName(),
            Definition.BORDER_RIGHT_WIDTH.getAttributeName()));

    private static final Set<String> LENGTH_PROPERTIES_TTFF = new HashSet<>(Arrays.asList(
            Definition.HEIGHT.getAttributeName(),
            Definition.WIDTH.getAttributeName(),
            Definition.TOP.getAttributeName(),
            Definition.LEFT.getAttributeName(),
            Definition.BOTTOM.getAttributeName(),
            Definition.RIGHT.getAttributeName(),
            Definition.MARGIN_TOP.getAttributeName(),
            Definition.MARGIN_LEFT.getAttributeName(),
            Definition.MARGIN_BOTTOM.getAttributeName(),
            Definition.MARGIN_RIGHT.getAttributeName(),
            Definition.MIN_HEIGHT.getAttributeName(),
            Definition.MIN_WIDTH.getAttributeName()
            ));

    private static final Set<String> LENGTH_PROPERTIES_FTFF = new HashSet<>(Arrays.asList(
            Definition.FONT_SIZE.getAttributeName(),
            Definition.TEXT_INDENT.getAttributeName(),
            Definition.PADDING_TOP.getAttributeName(),
            Definition.PADDING_LEFT.getAttributeName(),
            Definition.PADDING_BOTTOM.getAttributeName(),
            Definition.PADDING_RIGHT.getAttributeName(),
            Definition.MAX_HEIGHT.getAttributeName(),
            Definition.MAX_WIDTH.getAttributeName()
            ));

    private static final String[] THIN_MED_THICK = {"thin", "medium", "thick"};
    private static final String[] ALIGN_KEYWORDS =
        {"baseline", "sub", "super", "text-top", "text-bottom", "middle", "top", "bottom",
         "inherit", "initial", "revert", "unset"};
    private static final String[] FONT_SIZES =
        {"xx-small", "x-small", "small", "medium", "large", "x-large", "xx-large", "xxx-large",
         "smaller", "larger"};

    // private static final Log LOG = LogFactory.getLog(CSSStyleDeclaration.class);

    /** The wrapped CSSStyleDeclaration */
    private AbstractCssStyleDeclaration styleDeclaration_;

    /**
     * Creates an instance.
     */
    public CSSStyleDeclaration() {
        super();
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @JsxConstructor
    public void jsConstructor(final String type, final ScriptableObject details) {
        throw JavaScriptEngine.typeError("CSSStyleDeclaration ctor is not available");
    }

    /**
     * Creates an instance and sets its parent scope to the one of the provided element.
     * @param element the element to which this style is bound
     * @param styleDeclaration the style declaration to be based on
     */
    public CSSStyleDeclaration(final Element element, final AbstractCssStyleDeclaration styleDeclaration) {
        super();
        setParentScope(element.getParentScope());
        setPrototype(getPrototype(getClass()));

        setDomNode(element.getDomNodeOrNull(), false);

        if (styleDeclaration == null) {
            throw new IllegalStateException("styleDeclaration can't be null");
        }
        styleDeclaration_ = styleDeclaration;
    }

    /**
     * Creates an instance which wraps the specified style declaration.
     * @param parentStyleSheet the parent {@link CSSStyleSheet} to use
     * @param styleDeclaration the style declaration to wrap
     */
    CSSStyleDeclaration(final CSSStyleSheet parentStyleSheet, final WrappedCssStyleDeclaration styleDeclaration) {
        super();
        setParentScope(parentStyleSheet);
        setPrototype(getPrototype(getClass()));

        if (styleDeclaration == null) {
            throw new IllegalStateException("styleDeclaration can't be null");
        }
        styleDeclaration_ = styleDeclaration;
    }

    protected AbstractCssStyleDeclaration getCssStyleDeclaration() {
        return styleDeclaration_;
    }

    /**
     * Returns the priority of the named style attribute, or an empty string if it is not found.
     *
     * @param name the name of the style attribute whose value is to be retrieved
     * @return the named style attribute value, or an empty string if it is not found
     */
    protected String getStylePriority(final String name) {
        return styleDeclaration_.getStylePriority(name);
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

        styleDeclaration_.setStyleAttribute(name, newValue, important);
    }

    /**
     * Removes the specified style attribute, returning the value of the removed attribute.
     * @param name the attribute name (delimiter-separated, not camel-cased)
     */
    private String removeStyleAttribute(final String name) {
        if (styleDeclaration_ == null) {
            return null;
        }
        return styleDeclaration_.removeStyleAttribute(name);
    }

    /**
     * Returns a sorted map containing style elements, keyed on style element name. We use a
     * {@link LinkedHashMap} map so that results are deterministic and are thus testable.
     *
     * @return a sorted map containing style elements, keyed on style element name
     */
    private Map<String, StyleElement> getStyleMap() {
        if (styleDeclaration_ == null) {
            return Collections.emptyMap();
        }
        return styleDeclaration_.getStyleMap();
    }

    /**
     * Gets the {@code backgroundAttachment} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundAttachment() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBackgroundAttachment();
    }

    /**
     * Sets the {@code backgroundAttachment} style attribute.
     * @param backgroundAttachment the new attribute
     */
    @JsxSetter
    public void setBackgroundAttachment(final String backgroundAttachment) {
        setStyleAttribute(Definition.BACKGROUND_ATTACHMENT.getAttributeName(), backgroundAttachment);
    }

    /**
     * Gets the {@code backgroundColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundColor() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBackgroundColor();
    }

    /**
     * Sets the {@code backgroundColor} style attribute.
     * @param backgroundColor the new attribute
     */
    @JsxSetter
    public void setBackgroundColor(final String backgroundColor) {
        setStyleAttribute(Definition.BACKGROUND_COLOR.getAttributeName(), backgroundColor);
    }

    /**
     * Gets the {@code backgroundImage} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundImage() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBackgroundImage();
    }

    /**
     * Sets the {@code backgroundImage} style attribute.
     * @param backgroundImage the new attribute
     */
    @JsxSetter
    public void setBackgroundImage(final String backgroundImage) {
        setStyleAttribute(Definition.BACKGROUND_IMAGE.getAttributeName(), backgroundImage);
    }

    /**
     * Gets the {@code backgroundPosition} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundPosition() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBackgroundPosition();
    }

    /**
     * Sets the {@code backgroundPosition} style attribute.
     * @param backgroundPosition the new attribute
     */
    @JsxSetter
    public void setBackgroundPosition(final String backgroundPosition) {
        setStyleAttribute(Definition.BACKGROUND_POSITION.getAttributeName(), backgroundPosition);
    }

    /**
     * Gets the {@code backgroundRepeat} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBackgroundRepeat() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBackgroundRepeat();
    }

    /**
     * Sets the {@code backgroundRepeat} style attribute.
     * @param backgroundRepeat the new attribute
     */
    @JsxSetter
    public void setBackgroundRepeat(final String backgroundRepeat) {
        setStyleAttribute(Definition.BACKGROUND_REPEAT.getAttributeName(), backgroundRepeat);
    }

    /**
     * Gets the {@code blockSize} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBlockSize() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBlockSize();
    }

    /**
     * Sets the {@code blockSize} style attribute.
     * @param blockSize the new attribute
     */
    @JsxSetter
    public void setBlockSize(final String blockSize) {
        setStyleAttribute(Definition.BLOCK_SIZE.getAttributeName(), blockSize);
    }

    /**
     * Gets the {@code borderBottomColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderBottomColor() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderBottomColor();
    }

    /**
     * Sets the {@code borderBottomColor} style attribute.
     * @param borderBottomColor the new attribute
     */
    @JsxSetter
    public void setBorderBottomColor(final String borderBottomColor) {
        setStyleAttribute(Definition.BORDER_BOTTOM_COLOR.getAttributeName(), borderBottomColor);
    }

    /**
     * Gets the {@code borderBottomStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderBottomStyle() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderBottomStyle();
    }

    /**
     * Sets the {@code borderBottomStyle} style attribute.
     * @param borderBottomStyle the new attribute
     */
    @JsxSetter
    public void setBorderBottomStyle(final String borderBottomStyle) {
        setStyleAttribute(Definition.BORDER_BOTTOM_STYLE.getAttributeName(), borderBottomStyle);
    }

    /**
     * Gets the {@code borderBottomWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderBottomWidth() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderBottomWidth();
    }

    /**
     * Sets the {@code borderBottomWidth} style attribute.
     * @param borderBottomWidth the new attribute
     */
    @JsxSetter
    public void setBorderBottomWidth(final Object borderBottomWidth) {
        setStyleLengthAttribute(Definition.BORDER_BOTTOM_WIDTH.getAttributeName(), borderBottomWidth, "",
                false, false, null);
    }

    /**
     * Gets the {@code borderLeftColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderLeftColor() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderLeftColor();
    }

    /**
     * Sets the {@code borderLeftColor} style attribute.
     * @param borderLeftColor the new attribute
     */
    @JsxSetter
    public void setBorderLeftColor(final String borderLeftColor) {
        setStyleAttribute(Definition.BORDER_LEFT_COLOR.getAttributeName(), borderLeftColor);
    }

    /**
     * Gets the {@code borderLeftStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderLeftStyle() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderLeftStyle();
    }

    /**
     * Sets the {@code borderLeftStyle} style attribute.
     * @param borderLeftStyle the new attribute
     */
    @JsxSetter
    public void setBorderLeftStyle(final String borderLeftStyle) {
        setStyleAttribute(Definition.BORDER_LEFT_STYLE.getAttributeName(), borderLeftStyle);
    }

    /**
     * Gets the {@code borderLeftWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderLeftWidth() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderLeftWidth();
    }

    /**
     * Sets the {@code borderLeftWidth} style attribute.
     * @param borderLeftWidth the new attribute
     */
    @JsxSetter
    public void setBorderLeftWidth(final Object borderLeftWidth) {
        setStyleLengthAttribute(Definition.BORDER_LEFT_WIDTH.getAttributeName(), borderLeftWidth, "",
                false, false, null);
    }

    /**
     * Gets the {@code borderRightColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderRightColor() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderRightColor();
    }

    /**
     * Sets the {@code borderRightColor} style attribute.
     * @param borderRightColor the new attribute
     */
    @JsxSetter
    public void setBorderRightColor(final String borderRightColor) {
        setStyleAttribute(Definition.BORDER_RIGHT_COLOR.getAttributeName(), borderRightColor);
    }

    /**
     * Gets the {@code borderRightStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderRightStyle() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderRightStyle();
    }

    /**
     * Sets the {@code borderRightStyle} style attribute.
     * @param borderRightStyle the new attribute
     */
    @JsxSetter
    public void setBorderRightStyle(final String borderRightStyle) {
        setStyleAttribute(Definition.BORDER_RIGHT_STYLE.getAttributeName(), borderRightStyle);
    }

    /**
     * Gets the {@code borderRightWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderRightWidth() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderRightWidth();
    }

    /**
     * Sets the {@code borderRightWidth} style attribute.
     * @param borderRightWidth the new attribute
     */
    @JsxSetter
    public void setBorderRightWidth(final Object borderRightWidth) {
        setStyleLengthAttribute(Definition.BORDER_RIGHT_WIDTH.getAttributeName(), borderRightWidth, "",
                false, false, null);
    }

    /**
     * Gets the {@code borderTop} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderTop() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderTop();
    }

    /**
     * Sets the {@code borderTop} style attribute.
     * @param borderTop the new attribute
     */
    @JsxSetter
    public void setBorderTop(final String borderTop) {
        setStyleAttribute(Definition.BORDER_TOP.getAttributeName(), borderTop);
    }

    /**
     * Gets the {@code borderTopColor} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderTopColor() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderTopColor();
    }

    /**
     * Sets the {@code borderTopColor} style attribute.
     * @param borderTopColor the new attribute
     */
    @JsxSetter
    public void setBorderTopColor(final String borderTopColor) {
        setStyleAttribute(Definition.BORDER_TOP_COLOR.getAttributeName(), borderTopColor);
    }

    /**
     * Gets the {@code borderTopStyle} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderTopStyle() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderTopStyle();
    }

    /**
     * Sets the {@code borderTopStyle} style attribute.
     * @param borderTopStyle the new attribute
     */
    @JsxSetter
    public void setBorderTopStyle(final String borderTopStyle) {
        setStyleAttribute(Definition.BORDER_TOP_STYLE.getAttributeName(), borderTopStyle);
    }

    /**
     * Gets the {@code borderTopWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBorderTopWidth() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBorderTopWidth();
    }

    /**
     * Sets the {@code borderTopWidth} style attribute.
     * @param borderTopWidth the new attribute
     */
    @JsxSetter
    public void setBorderTopWidth(final Object borderTopWidth) {
        setStyleLengthAttribute(Definition.BORDER_TOP_WIDTH.getAttributeName(), borderTopWidth, "",
                false, false, null);
    }

    /**
     * Gets the {@code bottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getBottom() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getBottom();
    }

    /**
     * Sets the {@code bottom} style attribute.
     * @param bottom the new attribute
     */
    @JsxSetter
    public void setBottom(final Object bottom) {
        setStyleLengthAttribute(Definition.BOTTOM.getAttributeName(), bottom, "", true, true, null);
    }

    /**
     * Gets the {@code color} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getColor() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getColor();
    }

    /**
     * Sets the {@code color} style attribute.
     * @param color the new attribute
     */
    @JsxSetter
    public void setColor(final String color) {
        setStyleAttribute(Definition.COLOR.getAttributeName(), color);
    }

    /**
     * Gets the {@code cssFloat} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getCssFloat() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getCssFloat();
    }

    /**
     * Sets the {@code cssFloat} style attribute.
     * @param value the new attribute
     */
    @JsxSetter
    public void setCssFloat(final String value) {
        setStyleAttribute(Definition.FLOAT.getAttributeName(), value);
    }

    /**
     * Returns the actual text of the style.
     * @return the actual text of the style
     */
    @JsxGetter
    public String getCssText() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getCssText();
    }

    /**
     * Sets the actual text of the style.
     * @param value the new text
     */
    @JsxSetter
    public void setCssText(final String value) {
        String fixedValue = value;
        if (fixedValue == null || "null".equals(fixedValue)) {
            fixedValue = "";
        }

        try {
            styleDeclaration_.setCssText(fixedValue);
        }
        catch (final DOMExceptionImpl e) {
            // parsing errors handled this way
            styleDeclaration_.setCssText("");
        }
    }

    /**
     * Gets the {@code display} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getDisplay() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getDisplay();
    }

    /**
     * Sets the {@code display} style attribute.
     * @param display the new attribute
     */
    @JsxSetter
    public void setDisplay(final String display) {
        setStyleAttribute(Definition.DISPLAY.getAttributeName(), display);
    }

    /**
     * Gets the {@code fontSize} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFontSize() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getFontSize();
    }

    /**
     * Sets the {@code fontSize} style attribute.
     * @param fontSize the new attribute
     */
    @JsxSetter
    public void setFontSize(final Object fontSize) {
        setStyleLengthAttribute(Definition.FONT_SIZE.getAttributeName(), fontSize, "", false, true, FONT_SIZES);
        updateFont(getFont(), false);
    }

    /**
     * Gets the {@code lineHeight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getLineHeight() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getLineHeight();
    }

    /**
     * Sets the {@code lineHeight} style attribute.
     * @param lineHeight the new attribute
     */
    @JsxSetter
    public void setLineHeight(final String lineHeight) {
        setStyleAttribute(Definition.LINE_HEIGHT.getAttributeName(), lineHeight);
        updateFont(getFont(), false);
    }

    /**
     * Gets the {@code fontFamily} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFontFamily() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getFontFamily();
    }

    /**
     * Sets the {@code fontFamily} style attribute.
     * @param fontFamily the new attribute
     */
    @JsxSetter
    public void setFontFamily(final String fontFamily) {
        setStyleAttribute(Definition.FONT_FAMILY.getAttributeName(), fontFamily);
        updateFont(getFont(), false);
    }

    private void updateFont(final String font, final boolean force) {
        final BrowserVersion browserVersion = getBrowserVersion();
        final String[] details = ComputedFont.getDetails(font);
        if (details != null || force) {
            final StringBuilder newFont = new StringBuilder();
            newFont.append(getFontSize());
            String lineHeight = getLineHeight();
            final String defaultLineHeight = Definition.LINE_HEIGHT.getDefaultComputedValue(browserVersion);
            if (lineHeight.isEmpty()) {
                lineHeight = defaultLineHeight;
            }

            if (!lineHeight.equals(defaultLineHeight)) {
                newFont.append('/').append(lineHeight);
            }

            newFont.append(' ').append(getFontFamily());
            setStyleAttribute(Definition.FONT.getAttributeName(), newFont.toString());
        }
    }

    /**
     * Gets the {@code font} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getFont() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getFont();
    }

    /**
     * Sets the {@code font} style attribute.
     * @param font the new attribute
     */
    @JsxSetter
    public void setFont(final String font) {
        final String[] details = ComputedFont.getDetails(font);
        if (details != null) {
            setStyleAttribute(Definition.FONT_FAMILY.getAttributeName(), details[ComputedFont.FONT_FAMILY_INDEX]);
            final String fontSize = details[ComputedFont.FONT_SIZE_INDEX];
            if (details[ComputedFont.LINE_HEIGHT_INDEX] != null) {
                setStyleAttribute(Definition.LINE_HEIGHT.getAttributeName(), details[ComputedFont.LINE_HEIGHT_INDEX]);
            }
            setStyleAttribute(Definition.FONT_SIZE.getAttributeName(), fontSize);
            updateFont(font, true);
        }
    }

    /**
     * Gets the {@code height} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getHeight() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getHeight();
    }

    /**
     * Sets the {@code height} style attribute.
     * @param height the new attribute
     */
    @JsxSetter
    public void setHeight(final Object height) {
        setStyleLengthAttribute(Definition.HEIGHT.getAttributeName(), height, "", true, true, null);
    }

    /**
     * Gets the {@code left} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getLeft() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getLeft();
    }

    /**
     * Sets the {@code left} style attribute.
     * @param left the new attribute
     */
    @JsxSetter
    public void setLeft(final Object left) {
        setStyleLengthAttribute(Definition.LEFT.getAttributeName(), left, "", true, true, null);
    }

    /**
     * Returns the {@code length} property.
     * @return the {@code length} property
     */
    @JsxGetter
    public int getLength() {
        if (styleDeclaration_ == null) {
            return 0; // prototype
        }
        return styleDeclaration_.getLength();
    }

    /**
     * @param index the index
     * @return a CSS property name from a CSSStyleDeclaration by index.
     */
    @JsxFunction
    public String item(final int index) {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }

        return styleDeclaration_.item(index);
    }

    /**
     * Returns an Iterator allowing to go through all keys contained in this object.
     * @return a NativeArrayIterator
     */
    @JsxSymbol(symbolName = "iterator")
    public Scriptable values() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }

    /**
     * Gets the {@code letterSpacing} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getLetterSpacing() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getLetterSpacing();
    }

    /**
     * Sets the {@code letterSpacing} style attribute.
     * @param letterSpacing the new attribute
     */
    @JsxSetter
    public void setLetterSpacing(final Object letterSpacing) {
        setStyleLengthAttribute(Definition.LETTER_SPACING.getAttributeName(), letterSpacing, "",
                false, getBrowserVersion().hasFeature(JS_STYLE_LETTER_SPACING_ACCEPTS_PERCENT), null);
    }

    /**
     * Gets the {@code margin} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMargin() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getMargin();
    }

    /**
     * Sets the {@code margin} style attribute.
     * @param margin the new attribute
     */
    @JsxSetter
    public void setMargin(final String margin) {
        setStyleAttribute(Definition.MARGIN.getAttributeName(), margin);
    }

    /**
     * Gets the {@code marginBottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginBottom() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getMarginBottom();
    }

    /**
     * Sets the {@code marginBottom} style attribute.
     * @param marginBottom the new attribute
     */
    @JsxSetter
    public void setMarginBottom(final Object marginBottom) {
        setStyleLengthAttribute(Definition.MARGIN_BOTTOM.getAttributeName(), marginBottom, "", true, true, null);
    }

    /**
     * Gets the {@code marginLeft} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginLeft() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getMarginLeft();
    }

    /**
     * Sets the {@code marginLeft} style attribute.
     * @param marginLeft the new attribute
     */
    @JsxSetter
    public void setMarginLeft(final Object marginLeft) {
        setStyleLengthAttribute(Definition.MARGIN_LEFT.getAttributeName(), marginLeft, "", true, true, null);
    }

    /**
     * Gets the {@code marginRight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginRight() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getMarginRight();
    }

    /**
     * Sets the {@code marginRight} style attribute.
     * @param marginRight the new attribute
     */
    @JsxSetter
    public void setMarginRight(final Object marginRight) {
        setStyleLengthAttribute(Definition.MARGIN_RIGHT.getAttributeName(), marginRight, "", true, true, null);
    }

    /**
     * Gets the {@code marginTop} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMarginTop() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getMarginTop();
    }

    /**
     * Sets the {@code marginTop} style attribute.
     * @param marginTop the new attribute
     */
    @JsxSetter
    public void setMarginTop(final Object marginTop) {
        setStyleLengthAttribute(Definition.MARGIN_TOP.getAttributeName(), marginTop, "", true, true, null);
    }

    /**
     * Gets the {@code maxHeight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMaxHeight() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getMaxHeight();
    }

    /**
     * Sets the {@code maxHeight} style attribute.
     * @param maxHeight the new attribute
     */
    @JsxSetter
    public void setMaxHeight(final Object maxHeight) {
        setStyleLengthAttribute(Definition.MAX_HEIGHT.getAttributeName(), maxHeight, "", false, true, null);
    }

    /**
     * Gets the {@code maxWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMaxWidth() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getMaxWidth();
    }

    /**
     * Sets the {@code maxWidth} style attribute.
     * @param maxWidth the new attribute
     */
    @JsxSetter
    public void setMaxWidth(final Object maxWidth) {
        setStyleLengthAttribute(Definition.MAX_WIDTH.getAttributeName(), maxWidth, "", false, true, null);
    }

    /**
     * Gets the {@code minHeight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMinHeight() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getMinHeight();
    }

    /**
     * Sets the {@code minHeight} style attribute.
     * @param minHeight the new attribute
     */
    @JsxSetter
    public void setMinHeight(final Object minHeight) {
        setStyleLengthAttribute(Definition.MIN_HEIGHT.getAttributeName(), minHeight, "", true, true, null);
    }

    /**
     * Gets the {@code minWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getMinWidth() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getMinWidth();
    }

    /**
     * Sets the {@code minWidth} style attribute.
     * @param minWidth the new attribute
     */
    @JsxSetter
    public void setMinWidth(final Object minWidth) {
        setStyleLengthAttribute(Definition.MIN_WIDTH.getAttributeName(), minWidth, "", true, true, null);
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
            Object value = prototype.get(name, start);
            if (value != Scriptable.NOT_FOUND) {
                return value;
            }

            final String camel = StringUtils.cssCamelize(name);
            if (!name.equals(camel)) {
                value = prototype.get(camel, start);
                if (value != Scriptable.NOT_FOUND) {
                    return value;
                }
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
            return JavaScriptEngine.UNDEFINED;
        }

        final Map<String, StyleElement> style = getStyleMap();
        final int size = style.size();
        if (index >= size) {
            return JavaScriptEngine.UNDEFINED;
        }
        return style.keySet().toArray(new String[0])[index];
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
     * This impl ignores the default getDefaultValueIfEmpty flag, but there is an overload
     * in {@link ComputedCSSStyleDeclaration}.
     * @param definition the definition
     * @param getDefaultValueIfEmpty whether to get the default value if empty or not
     * @return the value
     */
    public String getStyleAttribute(final Definition definition, final boolean getDefaultValueIfEmpty) {
        if (styleDeclaration_ == null) {
            return ""; // prototype
        }
        return styleDeclaration_.getStyleAttribute(definition, getDefaultValueIfEmpty);
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
            final String camel = StringUtils.cssCamelize(name);
            if (!name.equals(camel) && prototype.get(camel, start) != Scriptable.NOT_FOUND) {
                prototype.put(camel, start, value);
                return;
            }
        }

        if (getDomNodeOrNull() != null) { // check if prototype or not
            final Definition style = StyleAttributes.getDefinition(name, getBrowserVersion());
            if (style != null) {
                final String stringValue = JavaScriptEngine.toString(value);
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

        final BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion != null) {
            final Definition style = StyleAttributes.getDefinition(name, getBrowserVersion());
            if (style != null) {
                return true;
            }
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
     * Gets the {@code opacity} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getOpacity() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getOpacity();
    }

    /**
     * Sets the {@code opacity} style attribute.
     * @param opacity the new attribute
     */
    @JsxSetter
    public void setOpacity(final Object opacity) {
        if (JavaScriptEngine.isNaN(opacity)) {
            return;
        }

        final double doubleValue;
        if (opacity instanceof Number) {
            doubleValue = ((Number) opacity).doubleValue();
        }
        else {
            String valueString = JavaScriptEngine.toString(opacity);

            if (valueString.isEmpty()) {
                setStyleAttribute(Definition.OPACITY.getAttributeName(), valueString);
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
        setStyleAttribute(Definition.OPACITY.getAttributeName(), Double.toString(doubleValue));
    }

    /**
     * Gets the {@code outline} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getOutline() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getOutline();
    }

    /**
     * Sets the {@code outline} style attribute.
     * @param outline the new attribute
     */
    @JsxSetter
    public void setOutline(final String outline) {
        setStyleAttribute(Definition.OUTLINE.getAttributeName(), outline);
    }

    /**
     * Gets the {@code outlineWidth} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getOutlineWidth() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getOutlineWidth();
    }

    /**
     * Sets the {@code outlineWidth} style attribute.
     * @param outlineWidth the new attribute
     */
    @JsxSetter
    public void setOutlineWidth(final Object outlineWidth) {
        setStyleLengthAttribute(Definition.OUTLINE_WIDTH.getAttributeName(), outlineWidth, "",
                false, false, THIN_MED_THICK);
    }

    /**
     * Gets the {@code padding} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPadding() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getPadding();
    }

    /**
     * Sets the {@code padding} style attribute.
     * @param padding the new attribute
     */
    @JsxSetter
    public void setPadding(final String padding) {
        setStyleAttribute(Definition.PADDING.getAttributeName(), padding);
    }

    /**
     * Gets the {@code paddingBottom} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingBottom() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getPaddingBottom();
    }

    /**
     * Sets the {@code paddingBottom} style attribute.
     * @param paddingBottom the new attribute
     */
    @JsxSetter
    public void setPaddingBottom(final Object paddingBottom) {
        setStyleLengthAttribute(Definition.PADDING_BOTTOM.getAttributeName(),
                paddingBottom, "", false, true, null);
    }

    /**
     * Gets the {@code paddingLeft} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingLeft() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getPaddingLeft();
    }

    /**
     * Sets the {@code paddingLeft} style attribute.
     * @param paddingLeft the new attribute
     */
    @JsxSetter
    public void setPaddingLeft(final Object paddingLeft) {
        setStyleLengthAttribute(Definition.PADDING_LEFT.getAttributeName(), paddingLeft, "", false, true, null);
    }

    /**
     * Gets the {@code paddingRight} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingRight() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getPaddingRight();
    }

    /**
     * Sets the {@code paddingRight} style attribute.
     * @param paddingRight the new attribute
     */
    @JsxSetter
    public void setPaddingRight(final Object paddingRight) {
        setStyleLengthAttribute(Definition.PADDING_RIGHT.getAttributeName(),
                paddingRight, "", false, true, null);
    }

    /**
     * Gets the {@code paddingTop} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPaddingTop() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getPaddingTop();
    }

    /**
     * Sets the {@code paddingTop} style attribute.
     * @param paddingTop the new attribute
     */
    @JsxSetter
    public void setPaddingTop(final Object paddingTop) {
        setStyleLengthAttribute(Definition.PADDING_TOP.getAttributeName(), paddingTop, "", false, true, null);
    }

    /**
     * Returns the CSSRule that is the parent of this style block or <code>null</code> if this CSSStyleDeclaration is
     * not attached to a CSSRule.
     * @return the CSSRule that is the parent of this style block or <code>null</code> if this CSSStyleDeclaration is
     *      not attached to a CSSRule
     */
    @JsxGetter
    public CSSRule getParentRule() {
        final AbstractCSSRuleImpl parentRule = styleDeclaration_.getParentRule();
        if (parentRule != null) {
            return CSSRule.create((CSSStyleSheet) getParentScope(), parentRule);
        }
        return null;
    }

    /**
     * Nothing.
     * @param parentRule ignored
     */
    @JsxSetter
    public void setParentRule(final CSSRule parentRule) {
        // nothing to do
    }

    /**
     * Gets the {@code right} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getRight() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getRight();
    }

    /**
     * Sets the {@code right} style attribute.
     * @param right the new attribute
     */
    @JsxSetter
    public void setRight(final Object right) {
        setStyleLengthAttribute(Definition.RIGHT.getAttributeName(), right, "", true, true, null);
    }

    /**
     * Gets the {@code rubyAlign} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getRubyAlign() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getRubyAlign();
    }

    /**
     * Sets the {@code rubyAlign} style attribute.
     * @param rubyAlign the new attribute
     */
    @JsxSetter
    public void setRubyAlign(final String rubyAlign) {
        setStyleAttribute(Definition.RUBY_ALIGN.getAttributeName(), rubyAlign);
    }

    /**
     * Gets the {@code size} style attribute.
     * @return the style attribute
     */
    @JsxGetter({CHROME, EDGE})
    public String getSize() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getSize();
    }

    /**
     * Sets the {@code size} style attribute.
     * @param size the new attribute
     */
    @JsxSetter({CHROME, EDGE})
    public void setSize(final String size) {
        setStyleAttribute(Definition.SIZE.getAttributeName(), size);
    }

    /**
     * Gets the {@code textIndent} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getTextIndent() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getTextIndent();
    }

    /**
     * Sets the {@code textIndent} style attribute.
     * @param textIndent the new attribute
     */
    @JsxSetter
    public void setTextIndent(final Object textIndent) {
        setStyleLengthAttribute(Definition.TEXT_INDENT.getAttributeName(), textIndent, "", false, true, null);
    }

    /**
     * Gets the {@code top} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getTop() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getTop();
    }

    /**
     * Sets the {@code top} style attribute.
     * @param top the new attribute
     */
    @JsxSetter
    public void setTop(final Object top) {
        setStyleLengthAttribute(Definition.TOP.getAttributeName(), top, "", true, true, null);
    }

    /**
     * Gets the {@code verticalAlign} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getVerticalAlign() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getVerticalAlign();
    }

    /**
     * Sets the {@code verticalAlign} style attribute.
     * @param verticalAlign the new attribute
     */
    @JsxSetter
    public void setVerticalAlign(final Object verticalAlign) {
        setStyleLengthAttribute(Definition.VERTICAL_ALIGN.getAttributeName(),
                verticalAlign, "", false, true, ALIGN_KEYWORDS);
    }

    /**
     * Gets the {@code width} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getWidth() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getWidth();
    }

    /**
     * Sets the {@code width} style attribute.
     * @param width the new attribute
     */
    @JsxSetter
    public void setWidth(final Object width) {
        setStyleLengthAttribute(Definition.WIDTH.getAttributeName(), width, "", true, true, null);
    }

    /**
     * Gets the {@code widows} style attribute.
     * @return the style attribute
     */
    @JsxGetter({CHROME, EDGE})
    public String getWidows() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getWidows();
    }

    /**
     * Sets the {@code widows} style attribute.
     * @param widows the new attribute
     */
    @JsxSetter({CHROME, EDGE})
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
        setStyleAttribute(Definition.WIDOWS.getAttributeName(), widows);
    }

    /**
     * Gets the {@code orphans} style attribute.
     * @return the style attribute
     */
    @JsxGetter({CHROME, EDGE})
    public String getOrphans() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getOrphans();
    }

    /**
     * Sets the {@code orphans} style attribute.
     * @param orphans the new attribute
     */
    @JsxSetter({CHROME, EDGE})
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
        setStyleAttribute(Definition.ORPHANS.getAttributeName(), orphans);
    }

    /**
     * Gets the {@code position} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getPosition() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getPosition();
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
            setStyleAttribute(Definition.POSITION.getAttributeName(), position.toLowerCase(Locale.ROOT));
        }
    }

    /**
     * Gets the {@code wordSpacing} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getWordSpacing() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getWordSpacing();
    }

    /**
     * Sets the {@code wordSpacing} style attribute.
     * @param wordSpacing the new attribute
     */
    @JsxSetter
    public void setWordSpacing(final Object wordSpacing) {
        setStyleLengthAttribute(Definition.WORD_SPACING.getAttributeName(), wordSpacing, "",
                false, getBrowserVersion().hasFeature(JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT), null);
    }

    /**
     * Gets the {@code zIndex} style attribute.
     * @return the style attribute
     */
    @JsxGetter
    public String getZIndex() {
        if (styleDeclaration_ == null) {
            return null; // prototype
        }
        return styleDeclaration_.getZIndex();
    }

    /**
     * Sets the {@code zIndex} style attribute.
     * @param zIndex the new attribute
     */
    @JsxSetter
    public void setZIndex(final Object zIndex) {
        // empty
        if (zIndex == null || StringUtils.isEmptyOrNull(zIndex.toString())) {
            setStyleAttribute(Definition.Z_INDEX_.getAttributeName(), "");
            return;
        }
        // undefined
        if (JavaScriptEngine.isUndefined(zIndex)) {
            return;
        }

        // string
        if (zIndex instanceof Number) {
            final Number number = (Number) zIndex;
            if (number.doubleValue() % 1 == 0) {
                setStyleAttribute(Definition.Z_INDEX_.getAttributeName(), Integer.toString(number.intValue()));
            }
            return;
        }
        try {
            final int i = Integer.parseInt(zIndex.toString());
            setStyleAttribute(Definition.Z_INDEX_.getAttributeName(), Integer.toString(i));
        }
        catch (final NumberFormatException ignored) {
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
            final Object value = getProperty(this, StringUtils.cssCamelize(name));
            if (value instanceof String) {
                return (String) value;
            }
        }

        return styleDeclaration_.getStyleAttribute(name);
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
        if (!StringUtils.isEmptyOrNull(important) && !"null".equals(important)) {
            if (!StyleElement.PRIORITY_IMPORTANT.equalsIgnoreCase(important)) {
                return;
            }
            imp = StyleElement.PRIORITY_IMPORTANT;
        }

        if (LENGTH_PROPERTIES_FFFF.contains(name)) {
            setStyleLengthAttribute(name, value, imp, false, false, null);
        }
        else if (LENGTH_PROPERTIES_TTFF.contains(name)) {
            setStyleLengthAttribute(name, value, imp, true, true, null);
        }
        else if (LENGTH_PROPERTIES_FTFF.contains(name)) {
            setStyleLengthAttribute(name, value, imp, false, true, null);
        }
        else if (Definition.OUTLINE_WIDTH.getAttributeName().equals(name)) {
            setStyleLengthAttribute(Definition.OUTLINE_WIDTH.getAttributeName(),
                    value, imp, false, false, THIN_MED_THICK);
        }
        else if (Definition.LETTER_SPACING.getAttributeName().equals(name)) {
            setStyleLengthAttribute(Definition.LETTER_SPACING.getAttributeName(), value, imp,
                    false, getBrowserVersion().hasFeature(JS_STYLE_LETTER_SPACING_ACCEPTS_PERCENT), null);
        }
        else if (Definition.WORD_SPACING.getAttributeName().equals(name)) {
            setStyleLengthAttribute(Definition.WORD_SPACING.getAttributeName(), value, imp,
                    false, getBrowserVersion().hasFeature(JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT), null);
        }
        else if (Definition.VERTICAL_ALIGN.getAttributeName().equals(name)) {
            setStyleLengthAttribute(Definition.VERTICAL_ALIGN.getAttributeName(), value, imp, false, true, null);
        }
        else {
            setStyleAttribute(name, JavaScriptEngine.toString(value), imp);
        }
    }

    /**
     * Removes the named property.
     * @param name the name of the property to remove
     * @return the value deleted
     */
    @JsxFunction
    public String removeProperty(final Object name) {
        return removeStyleAttribute(JavaScriptEngine.toString(name));
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
            catch (final NumberFormatException ignored) {
                // ignore
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (styleDeclaration_ == null) {
            return "CSSStyleDeclaration for 'null'"; // for instance on prototype
        }

        return "CSSStyleDeclaration for '" + styleDeclaration_ + "'";
    }

    /**
     * Sets the style attribute which should be treated as an integer in pixels.
     * @param name the attribute name
     * @param value the attribute value
     * @param important important value
     * @param auto true if auto is supported
     * @param percent true if percent is supported
     * @param validValues valid values
     */
    private void setStyleLengthAttribute(final String name, final Object value, final String important,
                final boolean auto, final boolean percent, final String[] validValues) {
        if (JavaScriptEngine.isNaN(value)) {
            return;
        }

        if (value instanceof Number) {
            return;
        }

        String valueString = JavaScriptEngine.toString(value);
        if (null == value) {
            valueString = "";
        }

        if (StringUtils.isEmptyOrNull(valueString)) {
            setStyleAttribute(name, valueString, important);
            return;
        }

        if ((auto && AUTO.equals(valueString))
                || INITIAL.equals(valueString)
                || INHERIT.equals(valueString)) {
            setStyleAttribute(name, valueString, important);
            return;
        }

        if (validValues != null && ArrayUtils.contains(validValues, valueString)) {
            setStyleAttribute(name, valueString, important);
            return;
        }

        String unit = "px";
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
        else {
            return;
        }

        if (!valueString.equals(valueString.trim())) {
            // we have a unit but surrounding blanks
            return;
        }
        final double doubleValue = JavaScriptEngine.toNumber(valueString);

        try {
            if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                return;
            }

            final String valueStr;
            if (doubleValue % 1 == 0) {
                valueStr = (int) doubleValue + unit;
            }
            else {
                valueStr = doubleValue + unit;
            }

            setStyleAttribute(name, valueStr, important);
        }
        catch (final Exception ignored) {
            // ignore
        }
    }
}
