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
        if (CssMathFunctionEvaluator.isMathFunction(value)) {
            // like for simple percentages, 100% is 16px here
            final Double result = CssMathFunctionEvaluator.evaluate(value, null, () -> 16);
            return result == null ? 0 : (int) Math.round(result);
        }

        final float i = StringUtils.toFloat(TO_FLOAT_PATTERN.matcher(value).replaceAll("$1"), 0);
        if (value.length() < 2) {
            return Math.round(i);
        }
        return (int) Math.round(toPixels(i, value));
    }

    /**
     * Converts the given number with the given unit into pixels.
     * @param number the number
     * @param unit the unit (e.g. {@code em}); percentages are based on 16px
     * @return the number of pixels
     */
    static double toPixels(final double number, final String unit) {
        if (unit.endsWith("px")) {
            return number;
        }
        if (unit.endsWith("em")) {
            return number * 16;
        }
        if (unit.endsWith("%")) {
            return number * 16 / 100;
        }
        if (unit.endsWith("ex")) {
            return number * 8;
        }
        if (unit.endsWith("cm")) {
            return number * 38;
        }
        if (unit.endsWith("mm")) {
            return number * 4;
        }
        if (unit.endsWith("pt")) {
            return number * 2;
        }
        if (unit.endsWith("pc")) {
            return number * 24;
        }
        if (unit.endsWith("ch")) {
            return number * 8;
        }
        if (unit.endsWith("vh")
                || unit.endsWith("vmin")) {
            // this matches also
            // "dvh" "dvmin" "lvh" "lvmin" "svh" "svmin"
            return number * 6;
        }
        if (unit.endsWith("vw")
                || unit.endsWith("vmax")) {
            // this matches also
            // "dvw" "dvmax" "lvw" "lvmax" "svw" "svmax"
            return number * 12;
        }
        // placed at the end to handle min before
        if (unit.endsWith("in")) {
            return number * 150;
        }
        return number;
    }

    private static int pixelValue(final DomElement element,
            final String styleValue,
            final Function<ComputedCssStyleDeclaration, String> styleGetter,
            final IntSupplier defaultValue,
            final IntSupplier windowDefaultValue,
            final boolean percentMode) {
        if (CssMathFunctionEvaluator.isMathFunction(styleValue)) {
            final Double result = CssMathFunctionEvaluator.evaluate(styleValue, element.getPage().getWebClient(),
                    () -> percentBase(element, styleGetter, defaultValue, windowDefaultValue));
            return result == null ? 0 : (int) Math.round(result);
        }

        if (styleValue.endsWith("%") || (styleValue.isEmpty() && element instanceof HtmlHtml)) {
            final float i = StringUtils.toFloat(TO_FLOAT_PATTERN.matcher(styleValue).replaceAll("$1"), 100);
            final int absoluteValue = percentBase(element, styleGetter, defaultValue, windowDefaultValue);
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

    /**
     * Returns the value (in pixels) a percentage of the given element is based on;
     * this is the value of the parent or the window default value for the root.
     */
    private static int percentBase(final DomElement element,
            final Function<ComputedCssStyleDeclaration, String> styleGetter,
            final IntSupplier defaultValue,
            final IntSupplier windowDefaultValue) {
        final DomNode parent = element.getParentNode();
        if (parent instanceof DomElement parentElem) {
            final ComputedCssStyleDeclaration style =
                    parentElem.getPage().getEnclosingWindow().getComputedStyle(parentElem, null);
            final String parentStyleValue = styleGetter.apply(style);
            return pixelValue(parentElem, parentStyleValue, styleGetter,
                                        defaultValue, windowDefaultValue, true);
        }
        return windowDefaultValue.getAsInt();
    }
}
