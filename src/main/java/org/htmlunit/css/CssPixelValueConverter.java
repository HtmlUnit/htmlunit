/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static org.htmlunit.css.CssStyleSheet.AUTO;

import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;

import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlCanvas;
import org.htmlunit.html.HtmlHtml;
import org.htmlunit.util.StringUtils;

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
     * specified CSS attribute value is a percentage, this method uses the specified value getters
     * to recursively retrieve the base (parent) CSS attribute value.
     * @param element the element for which the CSS attribute value is to be retrieved
     * @param styleGetter getter function to retrieve the CSS attribute value from ComputedCssStyleDeclaration
     * @param defaultValue supplier for the default value
     * @param windowDefaultValue supplier for the default value for the window
     * @return the integer number of pixels corresponding to the specified length CSS attribute value
     * @see #pixelValue(String)
     */
    public static int pixelValue(
            final DomElement element,
            final Function<ComputedCssStyleDeclaration, String> styleGetter,
            final IntSupplier defaultValue,
            final IntSupplier windowDefaultValue) {
        final ComputedCssStyleDeclaration style =
                element.getPage().getEnclosingWindow().getComputedStyle(element, null);
        final String s = styleGetter.apply(style);
        return pixelValue(element, s, styleGetter, defaultValue, windowDefaultValue, false);
    }

    /**
     * Returns the specified length CSS attribute value as a pixel length value.
     * If the specified CSS attribute value is a percentage, this method
     * uses the specified value getters to recursively retrieve the base (parent) CSS attribute value.
     * @param element the element for which the CSS attribute value is to be retrieved
     * @param styleGetter getter function to retrieve the CSS attribute value from ComputedCssStyleDeclaration
     * @param defaultValue supplier for the default value
     * @param windowDefaultValue supplier for the default value for the window
     * @return the specified length CSS attribute value as a pixel length value
     * @see #pixelValue(DomElement, Function, IntSupplier, IntSupplier)
     */
    public static String pixelString(
            final DomElement element,
            final Function<ComputedCssStyleDeclaration, String> styleGetter,
            final IntSupplier defaultValue,
            final IntSupplier windowDefaultValue) {
        final ComputedCssStyleDeclaration style =
                element.getPage().getEnclosingWindow().getComputedStyle(element, null);
        final String styleValue = styleGetter.apply(style);
        if (styleValue.endsWith("px")) {
            return styleValue;
        }
        return pixelValue(element, styleValue, styleGetter, defaultValue, windowDefaultValue, false) + "px";
    }

    /**
     * Converts the specified length string value into an integer number of pixels. This method does
     * <b>NOT</b> handle percentages correctly; use {@link #pixelString(DomElement, Function, IntSupplier, IntSupplier)}
     * if you need percentage support.
     * @param value the length string value to convert to an integer number of pixels
     * @return the integer number of pixels corresponding to the specified length string value
     * @see <a href="http://htmlhelp.com/reference/css/units.html">CSS Units</a>
     * @see #pixelString(DomElement, Function, IntSupplier, IntSupplier)
     */
    public static int pixelValue(final String value) {
        float i = StringUtils.toFloat(TO_FLOAT_PATTERN.matcher(value).replaceAll("$1"), 0);
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
            i = i * 8;
        }
        else if (value.endsWith("cm")) {
            i = i * 38;
        }
        else if (value.endsWith("mm")) {
            i = i * 4;
        }
        else if (value.endsWith("pt")) {
            i = i * 2;
        }
        else if (value.endsWith("pc")) {
            i = i * 24;
        }
        else if (value.endsWith("ch")) {
            i = i * 8;
        }
        else if (value.endsWith("vh")
                || value.endsWith("vmin")) {
            // this matches also
            // "dvh" "dvmin" "lvh" "lvmin" "svh" "svmin"
            i = i * 6;
        }
        else if (value.endsWith("vw")
                || value.endsWith("vmax")) {
            // this matches also
            // "dvw" "dvmax" "lvw" "lvmax" "svw" "svmax"
            i = i * 12;
        }
        // placed at the end to handle min before
        else if (value.endsWith("in")) {
            i = i * 150;
        }
        return Math.round(i);
    }

    private static int pixelValue(final DomElement element,
            final String styleValue,
            final Function<ComputedCssStyleDeclaration, String> styleGetter,
            final IntSupplier defaultValue,
            final IntSupplier windowDefaultValue,
            final boolean percentMode) {
        if (styleValue.endsWith("%") || (styleValue.isEmpty() && element instanceof HtmlHtml)) {
            final float i = StringUtils.toFloat(TO_FLOAT_PATTERN.matcher(styleValue).replaceAll("$1"), 100);

            final DomNode parent = element.getParentNode();
            final int absoluteValue;
            if (parent instanceof DomElement parentElem) {
                final ComputedCssStyleDeclaration style =
                        parentElem.getPage().getEnclosingWindow().getComputedStyle(parentElem, null);
                final String parentStyleValue = styleGetter.apply(style);
                absoluteValue = pixelValue(parentElem, parentStyleValue, styleGetter,
                                            defaultValue, windowDefaultValue, true);
            }
            else {
                absoluteValue = windowDefaultValue.getAsInt();
            }
            return Math.round((i / 100f) * absoluteValue);
        }
        if (AUTO.equals(styleValue)) {
            return defaultValue.getAsInt();
        }
        if (styleValue.isEmpty()) {
            if (element instanceof HtmlCanvas) {
                return windowDefaultValue.getAsInt();
            }

            // if the call was originated from a percent value we have to go up until
            // we can provide some kind of base value for percent calculation
            if (percentMode) {
                final DomNode parent = element.getParentNode();
                if (parent == null || parent instanceof HtmlHtml) {
                    return windowDefaultValue.getAsInt();
                }
                final DomElement parentElem = (DomElement) parent;
                final ComputedCssStyleDeclaration style =
                        parentElem.getPage().getEnclosingWindow().getComputedStyle(parentElem, null);
                final String parentStyleValue = styleGetter.apply(style);
                return pixelValue(parentElem, parentStyleValue, styleGetter, defaultValue, windowDefaultValue, true);
            }

            return 0;
        }
        return pixelValue(styleValue);
    }
}
