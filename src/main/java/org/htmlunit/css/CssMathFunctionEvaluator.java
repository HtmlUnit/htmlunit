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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.DoubleSupplier;

import org.htmlunit.SilentCssErrorHandler;
import org.htmlunit.WebClient;
import org.htmlunit.WebClient.PooledCSS3Parser;
import org.htmlunit.cssparser.dom.CSSValueImpl;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.LexicalUnit;
import org.htmlunit.cssparser.parser.LexicalUnit.LexicalUnitType;

/**
 * Evaluates the CSS math functions {@code calc()}, {@code min()}, {@code max()} and
 * {@code clamp()} for length values.
 * <p>
 * The value is parsed by the CSS parser; the resulting tree of lexical units (numbers,
 * lengths, percentages, the operators {@code +}, {@code -}, {@code *} and {@code /},
 * parentheses and nested math functions) is evaluated here. Lengths are converted to
 * pixels using {@link CssPixelValueConverter#toPixels(double, String)}; percentages are
 * resolved against the given base. Units are not type checked.
 * </p>
 *
 * @author Ronald Brill
 */
final class CssMathFunctionEvaluator {

    private static final int MAX_CACHE_SIZE = 500;

    /** The parsed values; the value is {@code null} if the value could not be parsed. */
    private static final Map<String, LexicalUnit> PARSED = Collections.synchronizedMap(
            new LinkedHashMap<>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(final Map.Entry<String, LexicalUnit> eldest) {
                    return size() > MAX_CACHE_SIZE;
                }
            });

    private final DoubleSupplier percentBase_;

    private CssMathFunctionEvaluator(final DoubleSupplier percentBase) {
        percentBase_ = percentBase;
    }

    /**
     * Returns whether the given value starts with a math function supported by this class.
     *
     * @param value the CSS value
     * @return {@code true} if the value starts with {@code calc(}, {@code min(},
     *         {@code max(} or {@code clamp(}
     */
    static boolean isMathFunction(final String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        final String lower = value.trim().toLowerCase(Locale.ROOT);
        return lower.startsWith("calc(")
                || lower.startsWith("min(")
                || lower.startsWith("max(")
                || lower.startsWith("clamp(");
    }

    /**
     * Evaluates the given math function.
     *
     * @param value the CSS value (e.g. {@code calc(100% - 20px)})
     * @param webClient the web client providing the css parser; may be {@code null}
     * @param percentBase supplier for the value (in pixels) that corresponds to 100%;
     *        only called if the expression contains a percentage
     * @return the value in pixels or {@code null} if the value is not supported
     */
    static Double evaluate(final String value, final WebClient webClient, final DoubleSupplier percentBase) {
        final LexicalUnit unit = parse(value, webClient);
        if (unit == null || unit.getNextLexicalUnit() != null) {
            return null;
        }

        try {
            final double result = new CssMathFunctionEvaluator(percentBase).evaluateFunction(unit);
            if (Double.isNaN(result) || Double.isInfinite(result)) {
                return null;
            }
            return result;
        }
        catch (final IllegalArgumentException e) {
            return null;
        }
    }

    private static LexicalUnit parse(final String value, final WebClient webClient) {
        LexicalUnit unit = PARSED.get(value);
        if (unit == null && !PARSED.containsKey(value)) {
            unit = parseValue(value, webClient);
            // also cache unsupported values (null)
            PARSED.put(value, unit);
        }
        return unit;
    }

    private static LexicalUnit parseValue(final String value, final WebClient webClient) {
        try {
            final CSSValueImpl cssValue;
            if (webClient == null) {
                final CSSOMParser parser = new CSSOMParser();
                parser.setErrorHandler(new SilentCssErrorHandler());
                cssValue = parser.parsePropertyValue(value);
            }
            else {
                // use a pooled parser to avoid the expensive creation
                try (PooledCSS3Parser pooledParser = webClient.getCSS3Parser()) {
                    final CSSOMParser parser = new CSSOMParser(pooledParser);
                    parser.setErrorHandler(new SilentCssErrorHandler());
                    cssValue = parser.parsePropertyValue(value);
                }
            }

            if (cssValue != null && cssValue.getValue() instanceof LexicalUnit unit) {
                return unit;
            }
        }
        catch (final Exception e) {
            // not supported
        }
        return null;
    }

    private double evaluateFunction(final LexicalUnit function) {
        final LexicalUnitType type = function.getLexicalUnitType();
        final List<Double> args = evaluateArguments(function.getParameters());

        if (type == LexicalUnitType.FUNCTION_CALC
                || (type == LexicalUnitType.FUNCTION && function.getFunctionName().isEmpty())) {
            // calc() or a parenthesized block
            if (args.size() == 1) {
                return args.get(0);
            }
        }
        else if (type == LexicalUnitType.FUNCTION_MIN) {
            return args.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
        }
        else if (type == LexicalUnitType.FUNCTION_MAX) {
            return args.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
        }
        else if (type == LexicalUnitType.FUNCTION_CLAMP) {
            if (args.size() == 3) {
                return Math.max(args.get(0), Math.min(args.get(1), args.get(2)));
            }
        }
        throw new IllegalArgumentException("Unsupported function '" + function + "'");
    }

    /**
     * Evaluates the comma separated arguments of a function.
     */
    private List<Double> evaluateArguments(final LexicalUnit params) {
        if (params == null) {
            throw new IllegalArgumentException("Arguments expected");
        }

        final List<Double> args = new ArrayList<>();
        final Cursor cursor = new Cursor(params);
        args.add(evaluateSum(cursor));
        while (cursor.is(LexicalUnitType.OPERATOR_COMMA)) {
            cursor.next();
            args.add(evaluateSum(cursor));
        }
        if (cursor.current_ != null) {
            throw new IllegalArgumentException("Unexpected '" + cursor.current_ + "'");
        }
        return args;
    }

    private double evaluateSum(final Cursor cursor) {
        double result = evaluateProduct(cursor);
        while (true) {
            if (cursor.is(LexicalUnitType.OPERATOR_PLUS)) {
                cursor.next();
                result += evaluateProduct(cursor);
            }
            else if (cursor.is(LexicalUnitType.OPERATOR_MINUS)) {
                cursor.next();
                result -= evaluateProduct(cursor);
            }
            else {
                return result;
            }
        }
    }

    private double evaluateProduct(final Cursor cursor) {
        double result = evaluateValue(cursor);
        while (true) {
            if (cursor.is(LexicalUnitType.OPERATOR_MULTIPLY)) {
                cursor.next();
                result *= evaluateValue(cursor);
            }
            else if (cursor.is(LexicalUnitType.OPERATOR_SLASH)) {
                cursor.next();
                final double divisor = evaluateValue(cursor);
                if (divisor == 0) {
                    throw new IllegalArgumentException("Division by zero");
                }
                result /= divisor;
            }
            else {
                return result;
            }
        }
    }

    private double evaluateValue(final Cursor cursor) {
        final LexicalUnit unit = cursor.current_;
        if (unit == null) {
            throw new IllegalArgumentException("Value expected");
        }
        cursor.next();

        switch (unit.getLexicalUnitType()) {
            case INTEGER:
                return unit.getIntegerValue();

            case REAL:
                return unit.getDoubleValue();

            case PERCENTAGE:
                return unit.getDoubleValue() * percentBase_.getAsDouble() / 100;

            case PIXEL:
            case EM:
            case REM:
            case EX:
            case CH:
            case VW:
            case VH:
            case VMIN:
            case VMAX:
            case DVW:
            case DVH:
            case DVMIN:
            case DVMAX:
            case LVW:
            case LVH:
            case LVMIN:
            case LVMAX:
            case SVW:
            case SVH:
            case SVMIN:
            case SVMAX:
            case INCH:
            case CENTIMETER:
            case MILLIMETER:
            case POINT:
            case PICA:
                return CssPixelValueConverter.toPixels(unit.getDoubleValue(), unit.getDimensionUnitText());

            case FUNCTION:
            case FUNCTION_CALC:
            case FUNCTION_MIN:
            case FUNCTION_MAX:
            case FUNCTION_CLAMP:
                return evaluateFunction(unit);

            default:
                // e.g. var() or an angle
                throw new IllegalArgumentException("Unsupported value '" + unit + "'");
        }
    }

    /**
     * Iterates over a chain of lexical units.
     */
    private static final class Cursor {
        private LexicalUnit current_;

        Cursor(final LexicalUnit first) {
            current_ = first;
        }

        boolean is(final LexicalUnitType type) {
            return current_ != null && current_.getLexicalUnitType() == type;
        }

        void next() {
            current_ = current_.getNextLexicalUnit();
        }
    }
}
