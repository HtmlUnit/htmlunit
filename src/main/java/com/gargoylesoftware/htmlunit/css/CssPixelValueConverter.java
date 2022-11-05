/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.css;

import static com.gargoylesoftware.htmlunit.css.CssStyleSheet.AUTO;

import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlCanvas;
import com.gargoylesoftware.htmlunit.html.HtmlHtml;

/**
 * Utilities for css value handling.
 *
 * @author Ronald Brill
 */
public final class CssPixelValueConverter {

    private static final Pattern TO_FLOAT_PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?).*");

    /**
     * Disallow instantiation of this class.
     */
    private CssPixelValueConverter() {
        // Empty.
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
    public static int pixelValue(final DomElement element, final CssValue value) {
        return pixelValue(element, value, false);
    }

    /**
     * Returns the specified length CSS attribute value value as a pixel length value, as long as
     * we're not emulating IE. If the specified CSS attribute value is a percentage, this method
     * uses the specified value object to recursively retrieve the base (parent) CSS attribute value.
     * @param element the element for which the CSS attribute value is to be retrieved
     * @param value the CSS attribute value which is to be retrieved
     * @return the specified length CSS attribute value as a pixel length value
     * @see #pixelValue(DomElement, CssValue)
     */
    public static String pixelString(final DomElement element, final CssValue value) {
        final ComputedCssStyleDeclaration style =
                element.getPage().getEnclosingWindow().getComputedStyle(element, null);
        final String s = value.get(style);
        if (s.endsWith("px")) {
            return s;
        }
        return pixelValue(element, value) + "px";
    }

    /**
     * Converts the specified length string value into an integer number of pixels. This method does
     * <b>NOT</b> handle percentages correctly; use {@link #pixelString(DomElement, CssValue)} if you
     * need percentage support).
     * @param value the length string value to convert to an integer number of pixels
     * @return the integer number of pixels corresponding to the specified length string value
     * @see <a href="http://htmlhelp.com/reference/css/units.html">CSS Units</a>
     * @see #pixelString(DomElement, CssValue)
     */
    public static int pixelValue(final String value) {
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

    private static int pixelValue(final DomElement element, final CssValue value, final boolean percentMode) {
        final ComputedCssStyleDeclaration style =
                element.getPage().getEnclosingWindow().getComputedStyle(element, null);
        final String s = value.get(style);
        if (s.endsWith("%") || (s.isEmpty() && element instanceof HtmlHtml)) {
            final float i = NumberUtils.toFloat(TO_FLOAT_PATTERN.matcher(s).replaceAll("$1"), 100);

            final DomNode parent = element.getParentNode();
            final int absoluteValue = (parent instanceof DomElement)
                            ? pixelValue((DomElement) parent, value, true) : value.getWindowDefaultValue();
            return  Math.round((i / 100f) * absoluteValue);
        }
        if (AUTO.equals(s)) {
            return value.getDefaultValue();
        }
        if (s.isEmpty()) {
            if (element instanceof HtmlCanvas) {
                return value.getWindowDefaultValue();
            }

            // if the call was originated from a percent value we have to go up until
            // we can provide some kind of base value for percent calculation
            if (percentMode) {
                final DomNode parent = element.getParentNode();
                if (parent == null || parent instanceof HtmlHtml) {
                    return value.getWindowDefaultValue();
                }
                return pixelValue((DomElement) parent, value, true);
            }

            return 0;
        }
        return pixelValue(s);
    }

    /**
     * Encapsulates the retrieval of a style attribute, given a DOM element from which to retrieve it.
     */
    public abstract static class CssValue {
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
         * Returns the CSS attribute value from the specified computed style.
         * @param style the computed style from which to retrieve the CSS attribute value
         * @return the CSS attribute value from the specified computed style
         */
        public abstract String get(ComputedCssStyleDeclaration style);
    }
}
