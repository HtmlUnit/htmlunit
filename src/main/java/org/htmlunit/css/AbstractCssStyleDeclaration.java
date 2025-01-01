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
package org.htmlunit.css;

import static org.htmlunit.BrowserVersionFeatures.CSS_BACKGROUND_INITIAL;
import static org.htmlunit.BrowserVersionFeatures.CSS_BACKGROUND_RGBA;
import static org.htmlunit.css.CssStyleSheet.FIXED;
import static org.htmlunit.css.CssStyleSheet.INITIAL;
import static org.htmlunit.css.CssStyleSheet.NONE;
import static org.htmlunit.css.CssStyleSheet.REPEAT;
import static org.htmlunit.css.CssStyleSheet.SCROLL;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlunit.BrowserVersion;
import org.htmlunit.BrowserVersionFeatures;
import org.htmlunit.css.StyleAttributes.Definition;
import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.impl.Color;
import org.htmlunit.util.StringUtils;

/**
 * A css StyleDeclaration.
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
 * @author cd alexndr
 */
public abstract class AbstractCssStyleDeclaration implements Serializable {

    private static final Pattern URL_PATTERN =
            Pattern.compile("url\\(\\s*[\"']?(.*?)[\"']?\\s*\\)");

    private static final Pattern POSITION_PATTERN =
            Pattern.compile("(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex))\\s*"
                    + "(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|top|bottom|center)");
    private static final Pattern POSITION_PATTERN2 =
            Pattern.compile("(left|right|center)\\s*(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|top|bottom|center)");
    private static final Pattern POSITION_PATTERN3 =
            Pattern.compile("(top|bottom|center)\\s*(\\d+\\s*(%|px|cm|mm|in|pt|pc|em|ex)|left|right|center)");

    /**
     * Returns the priority of the named style attribute, or an empty string if it is not found.
     *
     * @param name the name of the style attribute whose value is to be retrieved
     * @return the named style attribute value, or an empty string if it is not found
     */
    public abstract String getStylePriority(String name);

    /**
     * Returns the actual text of the style.
     * @return the actual text of the style
     */
    public abstract String getCssText();

    /**
     * Get the value for the style attribute.
     * @param name the name
     * @return the value
     */
    public abstract String getStyleAttribute(String name);

    /**
     * Get the value for the style attribute.
     * This impl ignores the default getDefaultValueIfEmpty flag, but there is a overload
     * in {@link ComputedCssStyleDeclaration}.
     * @param definition the definition
     * @param getDefaultValueIfEmpty whether to get the default value if empty or not
     * @return the value
     */
    public abstract String getStyleAttribute(Definition definition, boolean getDefaultValueIfEmpty);

    /**
     * Indicates if the browser this is associated with has the feature.
     * @param property the property name
     * @return {@code false} if this browser doesn't have this feature
     */
    public abstract boolean hasFeature(BrowserVersionFeatures property);

    /**
     * @return the {@link BrowserVersion}
     */
    public abstract BrowserVersion getBrowserVersion();

    /**
     * <p>Returns the value of one of the two named style attributes. If both attributes exist,
     * the value of the attribute that was declared last is returned. If only one of the
     * attributes exists, its value is returned. If neither attribute exists, an empty string
     * is returned.</p>
     *
     * <p>The second named attribute may be shorthand for a the actual desired property.
     * The following formats are possible:</p>
     * <ol>
     *   <li><code>top right bottom left</code>: All values are explicit.</li>
     *   <li><code>top right bottom</code>: Left is implicitly the same as right.</li>
     *   <li><code>top right</code>: Left is implicitly the same as right, bottom is implicitly the same as top.</li>
     *   <li><code>top</code>: Left, bottom and right are implicitly the same as top.</li>
     * </ol>
     *
     * @param definition1 the name of the first style attribute
     * @param definition2 the name of the second style attribute
     * @return the value of one of the two named style attributes
     */
    public String getStyleAttribute(final Definition definition1, final Definition definition2) {
        final StyleElement element1 = getStyleElement(definition1.getAttributeName());
        final StyleElement element2 = getStyleElement(definition2.getAttributeName());

        if (element2 == null) {
            if (element1 == null) {
                return "";
            }
            return element1.getValue();
        }
        if (element1 != null) {
            if (element1.compareTo(element2) > 0) {
                return element1.getValue();
            }
        }

        final String[] values = StringUtils.splitAtJavaWhitespace(element2.getValue());
        if (definition1.name().contains("TOP")) {
            if (values.length > 0) {
                return values[0];
            }
            return "";
        }
        else if (definition1.name().contains("RIGHT")) {
            if (values.length > 1) {
                return values[1];
            }
            else if (values.length > 0) {
                return values[0];
            }
            return "";
        }
        else if (definition1.name().contains("BOTTOM")) {
            if (values.length > 2) {
                return values[2];
            }
            else if (values.length > 0) {
                return values[0];
            }
            return "";
        }
        else if (definition1.name().contains("LEFT")) {
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
            throw new IllegalStateException("Unsupported definition: " + definition1);
        }
    }

    /**
     * Sets the actual text of the style.
     * @param value the new text
     */
    public abstract void setCssText(String value);

    /**
     * Sets the specified style attribute.
     * @param name the attribute name (camel-cased)
     * @param newValue the attribute value
     * @param important important value
     */
    public abstract void setStyleAttribute(String name, String newValue, String important);

    /**
     * Removes the specified style attribute, returning the value of the removed attribute.
     * @param name the attribute name (delimiter-separated, not camel-cased)
     * @return the removed value
     */
    public abstract String removeStyleAttribute(String name);

    /**
     * Returns the {@code length} property.
     * @return the {@code length} property
     */
    public abstract int getLength();

    /**
     * @param index the index
     * @return the name of the CSS property at the specified index
     */
    public abstract String item(int index);

    /**
     * Returns the CSSRule that is the parent of this style block or <code>null</code> if this CSSStyleDeclaration is
     * not attached to a CSSRule.
     * @return the CSSRule that is the parent of this style block or <code>null</code> if this CSSStyleDeclaration is
     *      not attached to a CSSRule
     */
    public abstract AbstractCSSRuleImpl getParentRule();

    /**
     * Determines the StyleElement for the given name.
     *
     * @param name the name of the requested StyleElement
     * @return the StyleElement or null if not found
     */
    public abstract StyleElement getStyleElement(String name);

    /**
     * Determines the StyleElement for the given name.
     * This ignores the case of the name.
     *
     * @param name the name of the requested StyleElement
     * @return the StyleElement or null if not found
     */
    public abstract StyleElement getStyleElementCaseInSensitive(String name);

    /**
     * Returns a sorted map containing style elements, keyed on style element name. We use a
     * {@link LinkedHashMap} map so that results are deterministic and are thus testable.
     *
     * @return a sorted map containing style elements, keyed on style element name
     */
    public abstract Map<String, StyleElement> getStyleMap();

    /**
     * @return true if this is a computed style declaration
     */
    public boolean isComputed() {
        return false;
    }

    /**
     * Gets the {@code backgroundAttachment} style attribute.
     * @return the style attribute
     */
    public String getBackgroundAttachment() {
        String value = getStyleAttribute(Definition.BACKGROUND_ATTACHMENT, false);
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(Definition.BACKGROUND, true);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(bg)) {
                value = findAttachment(bg);
                if (value == null) {
                    if (hasFeature(CSS_BACKGROUND_INITIAL) && !isComputed()) {
                        return INITIAL;
                    }
                    return SCROLL; // default if shorthand is used
                }
                return value;
            }
            return "";
        }

        return value;
    }

    /**
     * Gets the {@code backgroundColor} style attribute.
     * @return the style attribute
     */
    public String getBackgroundColor() {
        String value = getStyleAttribute(Definition.BACKGROUND_COLOR, false);
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(Definition.BACKGROUND, false);
            if (org.apache.commons.lang3.StringUtils.isBlank(bg)) {
                return "";
            }
            value = findColor(bg);
            if (value == null) {
                if (hasFeature(CSS_BACKGROUND_INITIAL)) {
                    if (!isComputed()) {
                        return INITIAL;
                    }
                    return "rgba(0, 0, 0, 0)";
                }
                if (hasFeature(CSS_BACKGROUND_RGBA)) {
                    return "rgba(0, 0, 0, 0)";
                }
                return "transparent"; // default if shorthand is used
            }
            return value;
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            return "";
        }
        return value;
    }

    /**
     * Gets the {@code backgroundImage} style attribute.
     * @return the style attribute
     */
    public String getBackgroundImage() {
        String value = getStyleAttribute(Definition.BACKGROUND_IMAGE, false);
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(Definition.BACKGROUND, false);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(bg)) {
                value = findImageUrl(bg);
                final boolean backgroundInitial = hasFeature(CSS_BACKGROUND_INITIAL);
                if (value == null) {
                    return backgroundInitial && !isComputed() ? INITIAL : NONE;
                }
                if (isComputed()) {
                    try {
                        value = value.substring(5, value.length() - 2);
                        final DomElement domElement = ((ComputedCssStyleDeclaration) this).getDomElement();
                        return "url(\"" + domElement.getHtmlPageOrNull()
                            .getFullyQualifiedUrl(value) + "\")";
                    }
                    catch (final Exception ignored) {
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
     * Gets the {@code backgroundPosition} style attribute.
     * @return the style attribute
     */
    public String getBackgroundPosition() {
        String value = getStyleAttribute(Definition.BACKGROUND_POSITION, false);
        if (value == null) {
            return null;
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(Definition.BACKGROUND, false);
            if (bg == null) {
                return null;
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(bg)) {
                value = findPosition(bg);
                final boolean isInitial = hasFeature(CSS_BACKGROUND_INITIAL);
                if (value == null) {
                    if (isInitial) {
                        return isComputed() ? "" : INITIAL;
                    }
                    return "0% 0%";
                }
                if (isComputed()) {
                    final String[] values = StringUtils.splitAtBlank(value);
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
     * Gets the {@code backgroundRepeat} style attribute.
     * @return the style attribute
     */
    public String getBackgroundRepeat() {
        String value = getStyleAttribute(Definition.BACKGROUND_REPEAT, false);
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            final String bg = getStyleAttribute(Definition.BACKGROUND, false);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(bg)) {
                value = findRepeat(bg);
                if (value == null) {
                    if (hasFeature(CSS_BACKGROUND_INITIAL) && !isComputed()) {
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
     * Gets the {@code borderBottomColor} style attribute.
     * @return the style attribute
     */
    public String getBorderBottomColor() {
        String value = getStyleAttribute(Definition.BORDER_BOTTOM_COLOR, false);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(Definition.BORDER_BOTTOM, false));
            if (value == null) {
                value = findColor(getStyleAttribute(Definition.BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Gets the {@code borderBottomStyle} style attribute.
     * @return the style attribute
     */
    public String getBorderBottomStyle() {
        String value = getStyleAttribute(Definition.BORDER_BOTTOM_STYLE, false);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(Definition.BORDER_BOTTOM, false));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(Definition.BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Gets the {@code borderBottomWidth} style attribute.
     * @return the style attribute
     */
    public String getBorderBottomWidth() {
        return getBorderWidth(Definition.BORDER_BOTTOM_WIDTH, Definition.BORDER_BOTTOM);
    }

    /**
     * Gets the {@code borderLeftColor} style attribute.
     * @return the style attribute
     */
    public String getBorderLeftColor() {
        String value = getStyleAttribute(Definition.BORDER_LEFT_COLOR, false);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(Definition.BORDER_LEFT, false));
            if (value == null) {
                value = findColor(getStyleAttribute(Definition.BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Gets the {@code borderLeftStyle} style attribute.
     * @return the style attribute
     */
    public String getBorderLeftStyle() {
        String value = getStyleAttribute(Definition.BORDER_LEFT_STYLE, false);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(Definition.BORDER_LEFT, false));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(Definition.BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Gets the {@code borderLeftWidth} style attribute.
     * @return the style attribute
     */
    public String getBorderLeftWidth() {
        return getBorderWidth(Definition.BORDER_LEFT_WIDTH, Definition.BORDER_LEFT);
    }

    /**
     * Gets the border width for the specified side
     * @param borderSideWidth the border side width Definition
     * @param borderSide the border side Definition
     * @return the width, "" if not defined
     */
    private String getBorderWidth(final Definition borderSideWidth, final Definition borderSide) {
        String value = getStyleAttribute(borderSideWidth, false);
        if (value.isEmpty()) {
            value = findBorderWidth(getStyleAttribute(borderSide, false));
            if (value == null) {
                final String borderWidth = getStyleAttribute(Definition.BORDER_WIDTH, false);
                if (!org.apache.commons.lang3.StringUtils.isEmpty(borderWidth)) {
                    final String[] values = StringUtils.splitAtJavaWhitespace(borderWidth);
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
                value = findBorderWidth(getStyleAttribute(Definition.BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Gets the {@code borderRightColor} style attribute.
     * @return the style attribute
     */
    public String getBorderRightColor() {
        String value = getStyleAttribute(Definition.BORDER_RIGHT_COLOR, false);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(Definition.BORDER_RIGHT, false));
            if (value == null) {
                value = findColor(getStyleAttribute(Definition.BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Gets the {@code borderRightStyle} style attribute.
     * @return the style attribute
     */
    public String getBorderRightStyle() {
        String value = getStyleAttribute(Definition.BORDER_RIGHT_STYLE, false);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(Definition.BORDER_RIGHT, false));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(Definition.BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Gets the {@code borderRightWidth} style attribute.
     * @return the style attribute
     */
    public String getBorderRightWidth() {
        return getBorderWidth(Definition.BORDER_RIGHT_WIDTH, Definition.BORDER_RIGHT);
    }

    /**
     * Gets the {@code borderTop} style attribute.
     * @return the style attribute
     */
    public String getBorderTop() {
        return getStyleAttribute(Definition.BORDER_TOP, true);
    }

    /**
     * Gets the {@code borderTopColor} style attribute.
     * @return the style attribute
     */
    public String getBorderTopColor() {
        String value = getStyleAttribute(Definition.BORDER_TOP_COLOR, false);
        if (value.isEmpty()) {
            value = findColor(getStyleAttribute(Definition.BORDER_TOP, false));
            if (value == null) {
                value = findColor(getStyleAttribute(Definition.BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Gets the {@code borderTopStyle} style attribute.
     * @return the style attribute
     */
    public String getBorderTopStyle() {
        String value = getStyleAttribute(Definition.BORDER_TOP_STYLE, false);
        if (value.isEmpty()) {
            value = findBorderStyle(getStyleAttribute(Definition.BORDER_TOP, false));
            if (value == null) {
                value = findBorderStyle(getStyleAttribute(Definition.BORDER, false));
            }
            if (value == null) {
                value = "";
            }
        }
        return value;
    }

    /**
     * Gets the {@code borderTopWidth} style attribute.
     * @return the style attribute
     */
    public String getBorderTopWidth() {
        return getBorderWidth(Definition.BORDER_TOP_WIDTH, Definition.BORDER_TOP);
    }

    /**
     * Gets the {@code bottom} style attribute.
     * @return the style attribute
     */
    public String getBottom() {
        return getStyleAttribute(Definition.BOTTOM, true);
    }

    /**
     * Gets the {@code color} style attribute.
     * @return the style attribute
     */
    public String getColor() {
        return getStyleAttribute(Definition.COLOR, true);
    }

    /**
     * Gets the {@code cssFloat} style attribute.
     * @return the style attribute
     */
    public String getCssFloat() {
        return getStyleAttribute(Definition.FLOAT, true);
    }

    /**
     * Gets the {@code display} style attribute.
     * @return the style attribute
     */
    public String getDisplay() {
        return getStyleAttribute(Definition.DISPLAY, true);
    }

    /**
     * Gets the {@code font} style attribute.
     * @return the style attribute
     */
    public String getFont() {
        return getStyleAttribute(Definition.FONT, true);
    }

    /**
     * Gets the {@code fontFamily} style attribute.
     * @return the style attribute
     */
    public String getFontFamily() {
        return getStyleAttribute(Definition.FONT_FAMILY, true);
    }

    /**
     * Gets the {@code fontSize} style attribute.
     * @return the style attribute
     */
    public String getFontSize() {
        return getStyleAttribute(Definition.FONT_SIZE, true);
    }

    /**
     * Gets the {@code height} style attribute.
     * @return the style attribute
     */
    public String getHeight() {
        return getStyleAttribute(Definition.HEIGHT, true);
    }

    /**
     * @return the style attribute {@code left}
     */
    public String getLeft() {
        return getStyleAttribute(Definition.LEFT, true);
    }

    /**
     * @return the style attribute {@code letterSpacing}
     */
    public String getLetterSpacing() {
        return getStyleAttribute(Definition.LETTER_SPACING, true);
    }

    /**
     * @return the style attribute {@code lineHeight}
     */
    public String getLineHeight() {
        return getStyleAttribute(Definition.LINE_HEIGHT, true);
    }

    /**
     * @return the style attribute {@code margin}
     */
    public String getMargin() {
        return getStyleAttribute(Definition.MARGIN, true);
    }

    /**
     * Gets the {@code marginBottom} style attribute.
     * @return the style attribute
     */
    public String getMarginBottom() {
        return getStyleAttribute(Definition.MARGIN_BOTTOM, Definition.MARGIN);
    }

    /**
     * Gets the {@code marginLeft} style attribute.
     * @return the style attribute
     */
    public String getMarginLeft() {
        return getStyleAttribute(Definition.MARGIN_LEFT, Definition.MARGIN);
    }

    /**
     * Gets the {@code marginRight} style attribute.
     * @return the style attribute
     */
    public String getMarginRight() {
        return getStyleAttribute(Definition.MARGIN_RIGHT, Definition.MARGIN);
    }

    /**
     * Gets the {@code marginTop} style attribute.
     * @return the style attribute
     */
    public String getMarginTop() {
        return getStyleAttribute(Definition.MARGIN_TOP, Definition.MARGIN);
    }

    /**
     * @return the style attribute {@code maxHeight}
     */
    public String getMaxHeight() {
        return getStyleAttribute(Definition.MAX_HEIGHT, true);
    }

    /**
     * @return the style attribute {@code maxWidth}
     */
    public String getMaxWidth() {
        return getStyleAttribute(Definition.MAX_WIDTH, true);
    }

    /**
     * @return the style attribute {@code minHeight}
     */
    public String getMinHeight() {
        return getStyleAttribute(Definition.MIN_HEIGHT, true);
    }

    /**
     * @return the style attribute {@code minWidth}
     */
    public String getMinWidth() {
        return getStyleAttribute(Definition.MIN_WIDTH, true);
    }

    /**
     * Gets the {@code opacity} style attribute.
     * @return the style attribute
     */
    public String getOpacity() {
        final String opacity = getStyleAttribute(Definition.OPACITY, false);
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
        catch (final NumberFormatException ignored) {
            // ignore wrong values
        }
        return "";
    }

    /**
     * @return the style attribute {@code orphans}
     */
    public String getOrphans() {
        return getStyleAttribute(Definition.ORPHANS, true);
    }

    /**
     * @return the style attribute {@code outline}
     */
    public String getOutline() {
        return getStyleAttribute(Definition.OUTLINE, true);
    }

    /**
     * @return the style attribute {@code outlineWidth}
     */
    public String getOutlineWidth() {
        return getStyleAttribute(Definition.OUTLINE_WIDTH, true);
    }

    /**
     * @return the style attribute {@code padding}
     */
    public String getPadding() {
        return getStyleAttribute(Definition.PADDING, true);
    }

    /**
     * @return the style attribute {@code paddingBottom}
     */
    public String getPaddingBottom() {
        return getStyleAttribute(Definition.PADDING_BOTTOM, Definition.PADDING);
    }

    /**
     * @return the style attribute {@code paddingLeft}
     */
    public String getPaddingLeft() {
        return getStyleAttribute(Definition.PADDING_LEFT, Definition.PADDING);
    }

    /**
     * @return the style attribute {@code paddingRight}
     */
    public String getPaddingRight() {
        return getStyleAttribute(Definition.PADDING_RIGHT, Definition.PADDING);
    }

    /**
     * @return the style attribute {@code paddingTop}
     */
    public String getPaddingTop() {
        return getStyleAttribute(Definition.PADDING_TOP, Definition.PADDING);
    }

    /**
     * @return the style attribute {@code position}
     */
    public String getPosition() {
        return getStyleAttribute(Definition.POSITION, true);
    }

    /**
     * @return the style attribute {@code right}
     */
    public String getRight() {
        return getStyleAttribute(Definition.RIGHT, true);
    }

    /**
     * @return the style attribute {@code rubyAlign}
     */
    public String getRubyAlign() {
        return getStyleAttribute(Definition.RUBY_ALIGN, true);
    }

    /**
     * @return the style attribute {@code size}
     */
    public String getSize() {
        return getStyleAttribute(Definition.SIZE, true);
    }

    /**
     * @return the style attribute {@code textIndent}
     */
    public String getTextIndent() {
        return getStyleAttribute(Definition.TEXT_INDENT, true);
    }

    /**
     * @return the style attribute {@code top}
     */
    public String getTop() {
        return getStyleAttribute(Definition.TOP, true);
    }

    /**
     * @return the style attribute {@code verticalAlign}
     */
    public String getVerticalAlign() {
        return getStyleAttribute(Definition.VERTICAL_ALIGN, true);
    }

    /**
     * @return the style attribute {@code widows}
     */
    public String getWidows() {
        return getStyleAttribute(Definition.WIDOWS, true);
    }

    /**
     * @return the style attribute {@code width}
     */
    public String getWidth() {
        return getStyleAttribute(Definition.WIDTH, true);
    }

    /**
     * @return the style attribute {@code wordSpacing}
     */
    public String getWordSpacing() {
        return getStyleAttribute(Definition.WORD_SPACING, true);
    }

    /**
     * Gets the {@code zIndex} style attribute.
     * @return the style attribute
     */
    public String getZIndex() {
        final String value = getStyleAttribute(Definition.Z_INDEX_, true);
        try {
            Integer.parseInt(value);
            return value;
        }
        catch (final NumberFormatException e) {
            return "";
        }
    }

    /**
     * Searches for any attachment notation in the specified text.
     * @param text the string to search in
     * @return the string of the attachment if found, null otherwise
     */
    private static String findAttachment(final String text) {
        if (text.contains(SCROLL)) {
            return SCROLL;
        }
        if (text.contains(FIXED)) {
            return FIXED;
        }
        return null;
    }

    /**
     * Searches for any color notation in the specified text.
     * @param text the string to search in
     * @return the string of the color if found, null otherwise
     */
    private static String findColor(final String text) {
        Color tmpColor = StringUtils.findColorRGB(text);
        if (tmpColor != null) {
            return StringUtils.formatColor(tmpColor);
        }

        final String[] tokens = StringUtils.splitAtBlank(text);
        for (final String token : tokens) {
            if (CssColors.isColorKeyword(token)) {
                return token;
            }

            tmpColor = StringUtils.asColorHexadecimal(token);
            if (tmpColor != null) {
                return StringUtils.formatColor(tmpColor);
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
     * Searches for a border style in the specified text.
     * @param text the string to search in
     * @return the border style if found, null otherwise
     */
    private static String findBorderStyle(final String text) {
        for (final String token : StringUtils.splitAtBlank(text)) {
            if (isBorderStyle(token)) {
                return token;
            }
        }
        return null;
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
     * Searches for a border width in the specified text.
     * @param text the string to search in
     * @return the border width if found, null otherwise
     */
    private static String findBorderWidth(final String text) {
        for (final String token : StringUtils.splitAtBlank(text)) {
            if (isBorderWidth(token)) {
                return token;
            }
        }
        return null;
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
    static boolean isLength(final String token) {
        if (token.endsWith("%")) {
            try {
                Double.parseDouble(token.substring(0, token.length() - 1));
                return true;
            }
            catch (final NumberFormatException ignored) {
                // ignore wrong values
            }
            return false;
        }

        if (token.endsWith("em") || token.endsWith("ex") || token.endsWith("px") || token.endsWith("in")
            || token.endsWith("cm") || token.endsWith("mm") || token.endsWith("pt") || token.endsWith("pc")) {

            try {
                Double.parseDouble(token.substring(0, token.length() - 2));
                return true;
            }
            catch (final NumberFormatException ignored) {
                // ignore wrong values
            }
            return false;
        }

        return false;
    }
}
