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
package org.htmlunit.javascript.host.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for the CSS transform lists used by the {@link DOMMatrixReadOnly} and {@link DOMMatrix}
 * constructors and by {@link DOMMatrix#setMatrixValue(String)}.
 *
 * <p>Supported are the empty string and {@code none} (the identity matrix) and a list of the
 * transform functions {@code matrix()}, {@code matrix3d()}, {@code translate()}, {@code translateX()},
 * {@code translateY()}, {@code translateZ()}, {@code translate3d()}, {@code scale()}, {@code scaleX()},
 * {@code scaleY()}, {@code scaleZ()}, {@code scale3d()}, {@code rotate()}, {@code rotateX()},
 * {@code rotateY()}, {@code rotateZ()}, {@code rotate3d()}, {@code skew()}, {@code skewX()},
 * {@code skewY()} and {@code perspective()}.</p>
 *
 * <p>Lengths can have the absolute units {@code px}, {@code cm}, {@code mm}, {@code q},
 * {@code in}, {@code pt} and {@code pc}; angles the units {@code deg}, {@code rad},
 * {@code grad} and {@code turn}. A length or angle without unit is only allowed for 0.
 * Relative units (e.g. {@code em} or percentages for lengths) can't be resolved without an element;
 * like in the browsers, they are a syntax error. Scale factors can be numbers or percentages.</p>
 *
 * <p>If one of the functions is a 3D function, the resulting matrix is 3D (even if the values
 * would allow a 2D matrix); like in the browsers, {@code rotateZ()} counts as 2D function.
 * Like in CSS, the end of the input closes a function that is still open.</p>
 *
 * @author Ronald Brill
 */
final class DOMMatrixTransformListParser {

    private static final Pattern NUMBER_PATTERN =
            Pattern.compile("([+-]?(?:\\d*\\.\\d+|\\d+)(?:[eE][+-]?\\d+)?)([a-zA-Z]*|%)");

    private enum ArgumentType {
        NUMBER, NUMBER_OR_PERCENTAGE, LENGTH, ANGLE
    }

    private DOMMatrixTransformListParser() {
        // util class
    }

    /**
     * Parses the given transform list and replaces the values of the given matrix with the result.
     *
     * @param transformList the transform list
     * @param matrix the matrix to set the result to
     * @throws IllegalArgumentException if the transform list is not valid
     */
    static void parse(final String transformList, final DOMMatrixReadOnly matrix) {
        final DOMMatrixReadOnly result = new DOMMatrixReadOnly();

        final String text = trimCssWhitespace(transformList);
        if (text.isEmpty() || "none".equalsIgnoreCase(text)) {
            matrix.setValues(result.getValues(), true);
            return;
        }

        boolean is3D = false;
        int pos = 0;
        while (pos < text.length()) {
            final int open = text.indexOf('(', pos);
            if (open < 0) {
                throw new IllegalArgumentException("transform function expected at position " + pos);
            }
            final String name = text.substring(pos, open).toLowerCase(Locale.ROOT);
            if (!name.matches("[a-z][a-z0-9]*")) {
                throw new IllegalArgumentException("invalid transform function '" + name + "'");
            }

            // like in CSS, the end of the input closes the last function
            int close = text.indexOf(')', open);
            if (close < 0) {
                close = text.length();
            }

            is3D |= apply(name, splitArguments(text.substring(open + 1, close)), result);

            pos = close + 1;
            while (pos < text.length() && isCssWhitespace(text.charAt(pos))) {
                pos++;
            }
        }

        matrix.setValues(result.getValues(), result.isIs2D() && !is3D);
    }

    /**
     * Applies the given transform function to the matrix.
     *
     * @param name the function name (lower case)
     * @param args the arguments
     * @param matrix the matrix
     * @return true if this is a 3D function
     */
    private static boolean apply(final String name, final List<String> args, final DOMMatrixReadOnly matrix) {
        switch (name) {
            case "matrix": {
                final double[] v = values(name, args, 6, 6, ArgumentType.NUMBER);
                final DOMMatrixReadOnly other = new DOMMatrixReadOnly();
                other.setValues2D(v[0], v[1], v[2], v[3], v[4], v[5]);
                matrix.multiplySelfInternal(other);
                return false;
            }
            case "matrix3d": {
                final double[] v = values(name, args, 16, 16, ArgumentType.NUMBER);
                final DOMMatrixReadOnly other = new DOMMatrixReadOnly();
                other.setValues(v, false);
                matrix.multiplySelfInternal(other);
                return true;
            }

            case "translate": {
                final double[] v = values(name, args, 1, 2, ArgumentType.LENGTH);
                matrix.translateSelfInternal(v[0], v.length > 1 ? v[1] : 0, 0);
                return false;
            }
            case "translatex":
                matrix.translateSelfInternal(values(name, args, 1, 1, ArgumentType.LENGTH)[0], 0, 0);
                return false;
            case "translatey":
                matrix.translateSelfInternal(0, values(name, args, 1, 1, ArgumentType.LENGTH)[0], 0);
                return false;
            case "translatez":
                matrix.translateSelfInternal(0, 0, values(name, args, 1, 1, ArgumentType.LENGTH)[0]);
                return true;
            case "translate3d": {
                final double[] v = values(name, args, 3, 3, ArgumentType.LENGTH);
                matrix.translateSelfInternal(v[0], v[1], v[2]);
                return true;
            }

            case "scale": {
                final double[] v = values(name, args, 1, 2, ArgumentType.NUMBER_OR_PERCENTAGE);
                matrix.scaleSelfInternal(v[0], v.length > 1 ? v[1] : v[0], 1, 0, 0, 0);
                return false;
            }
            case "scalex":
                matrix.scaleSelfInternal(values(name, args, 1, 1, ArgumentType.NUMBER_OR_PERCENTAGE)[0],
                        1, 1, 0, 0, 0);
                return false;
            case "scaley":
                matrix.scaleSelfInternal(1, values(name, args, 1, 1, ArgumentType.NUMBER_OR_PERCENTAGE)[0],
                        1, 0, 0, 0);
                return false;
            case "scalez":
                matrix.scaleSelfInternal(1, 1, values(name, args, 1, 1, ArgumentType.NUMBER_OR_PERCENTAGE)[0],
                        0, 0, 0);
                return true;
            case "scale3d": {
                final double[] v = values(name, args, 3, 3, ArgumentType.NUMBER_OR_PERCENTAGE);
                matrix.scaleSelfInternal(v[0], v[1], v[2], 0, 0, 0);
                return true;
            }

            case "rotate":
                matrix.rotateSelfInternal(0, 0, values(name, args, 1, 1, ArgumentType.ANGLE)[0]);
                return false;
            case "rotatez":
                // the browsers are treating this as 2D function
                matrix.rotateSelfInternal(0, 0, values(name, args, 1, 1, ArgumentType.ANGLE)[0]);
                return false;
            case "rotatex":
                matrix.rotateAxisAngleSelfInternal(1, 0, 0, values(name, args, 1, 1, ArgumentType.ANGLE)[0]);
                return true;
            case "rotatey":
                matrix.rotateAxisAngleSelfInternal(0, 1, 0, values(name, args, 1, 1, ArgumentType.ANGLE)[0]);
                return true;
            case "rotate3d": {
                if (args.size() != 4) {
                    throw new IllegalArgumentException("rotate3d() requires 4 arguments");
                }
                final double x = value(args.get(0), ArgumentType.NUMBER);
                final double y = value(args.get(1), ArgumentType.NUMBER);
                final double z = value(args.get(2), ArgumentType.NUMBER);
                final double angle = value(args.get(3), ArgumentType.ANGLE);
                matrix.rotateAxisAngleSelfInternal(x, y, z, angle);
                return true;
            }

            case "skew": {
                final double[] v = values(name, args, 1, 2, ArgumentType.ANGLE);
                final DOMMatrixReadOnly skew = new DOMMatrixReadOnly();
                skew.setValues2D(1, Math.tan(Math.toRadians(v.length > 1 ? v[1] : 0)),
                        Math.tan(Math.toRadians(v[0])), 1, 0, 0);
                matrix.multiplySelfInternal(skew);
                return false;
            }
            case "skewx":
                matrix.skewXSelfInternal(values(name, args, 1, 1, ArgumentType.ANGLE)[0]);
                return false;
            case "skewy":
                matrix.skewYSelfInternal(values(name, args, 1, 1, ArgumentType.ANGLE)[0]);
                return false;

            case "perspective": {
                if (args.size() != 1) {
                    throw new IllegalArgumentException("perspective() requires 1 argument");
                }
                if ("none".equalsIgnoreCase(args.get(0))) {
                    return true;
                }
                final double length = value(args.get(0), ArgumentType.LENGTH);
                if (length < 0) {
                    throw new IllegalArgumentException("perspective() requires a non-negative length");
                }
                final DOMMatrixReadOnly perspective = new DOMMatrixReadOnly();
                final double[] v = perspective.getValues();
                // values below 1px are treated as 1px
                v[11] = -1 / Math.max(1, length);
                perspective.setValues(v, false);
                matrix.multiplySelfInternal(perspective);
                return true;
            }

            default:
                throw new IllegalArgumentException("unknown transform function '" + name + "'");
        }
    }

    private static double[] values(final String name, final List<String> args, final int min, final int max,
            final ArgumentType type) {
        if (args.size() < min || args.size() > max) {
            throw new IllegalArgumentException(name + "() requires "
                    + (min == max ? Integer.toString(min) : min + " to " + max) + " arguments");
        }

        final double[] result = new double[args.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = value(args.get(i), type);
        }
        return result;
    }

    /**
     * Parses one argument.
     *
     * @param arg the argument
     * @param type the expected type
     * @return the value; lengths in px, angles in degrees, percentages as factor
     */
    private static double value(final String arg, final ArgumentType type) {
        final Matcher matcher = NUMBER_PATTERN.matcher(arg);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("invalid value '" + arg + "'");
        }

        final double number = Double.parseDouble(matcher.group(1));
        final String unit = matcher.group(2).toLowerCase(Locale.ROOT);

        switch (type) {
            case NUMBER:
                if (unit.isEmpty()) {
                    return number;
                }
                break;

            case NUMBER_OR_PERCENTAGE:
                if (unit.isEmpty()) {
                    return number;
                }
                if ("%".equals(unit)) {
                    return number / 100;
                }
                break;

            case LENGTH: {
                if (unit.isEmpty() && number == 0) {
                    return 0;
                }
                final double factor = lengthFactor(unit);
                if (factor > 0) {
                    return number * factor;
                }
                break;
            }

            case ANGLE:
                if (unit.isEmpty() && number == 0) {
                    return 0;
                }
                switch (unit) {
                    case "deg":
                        return number;
                    case "rad":
                        return Math.toDegrees(number);
                    case "grad":
                        return number * 0.9;
                    case "turn":
                        return number * 360;
                    default:
                        break;
                }
                break;

            default:
                break;
        }

        throw new IllegalArgumentException("invalid value '" + arg + "'");
    }

    private static double lengthFactor(final String unit) {
        return switch (unit) {
            case "px" -> 1;
            case "in" -> 96;
            case "cm" -> 96 / 2.54;
            case "mm" -> 96 / 25.4;
            case "q" -> 96 / 101.6;
            case "pt" -> 96.0 / 72;
            case "pc" -> 16;
            default -> -1;
        };
    }

    private static List<String> splitArguments(final String args) {
        final List<String> result = new ArrayList<>();
        for (final String arg : args.split(",", -1)) {
            final String trimmed = trimCssWhitespace(arg);
            if (trimmed.isEmpty()) {
                throw new IllegalArgumentException("empty argument");
            }
            result.add(trimmed);
        }
        return result;
    }

    private static String trimCssWhitespace(final String text) {
        int start = 0;
        int end = text.length();
        while (start < end && isCssWhitespace(text.charAt(start))) {
            start++;
        }
        while (end > start && isCssWhitespace(text.charAt(end - 1))) {
            end--;
        }
        return text.substring(start, end);
    }

    private static boolean isCssWhitespace(final char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f';
    }
}
