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
package org.htmlunit.platform.canvas.rendering;

/**
 * Parser for the SVG path data syntax (the {@code d} attribute of the svg path element)
 * as used by the {@code Path2D(String)} constructor.
 * Like in the browsers, the path is built up to the first error in the data.
 *
 * @see <a href="https://www.w3.org/TR/SVG2/paths.html#PathDataBNF">SVG path data grammar</a>
 *
 * @author Ronald Brill
 */
public final class SvgPathDataParser {

    private final String data_;
    private final RecordedPath path_;
    private int pos_;

    // the current point, the start of the current sub path and the last control point
    private double currentX_;
    private double currentY_;
    private double startX_;
    private double startY_;
    private double controlX_;
    private double controlY_;

    private SvgPathDataParser(final String data, final RecordedPath path) {
        data_ = data;
        path_ = path;
    }

    /**
     * Parses the path data and adds the segments to the given path.
     * @param data the path data
     * @param path the path to add the segments to
     */
    public static void parse(final String data, final RecordedPath path) {
        new SvgPathDataParser(data, path).parse();
    }

    private void parse() {
        skipWhitespace();
        char command = 0;
        boolean first = true;
        while (pos_ < data_.length()) {
            final char c = data_.charAt(pos_);
            if (isCommand(c)) {
                command = c;
                pos_++;
                skipWhitespace();
            }
            else if (command == 0 || command == 'Z' || command == 'z' || !isNumberStart(c)) {
                // a number without a command or after a closepath
                return;
            }
            // else: the command is repeated implicitly

            if (first && command != 'M' && command != 'm') {
                return;
            }
            first = false;

            if (!parseSegment(command)) {
                return;
            }

            // a repeated moveto is a lineto
            if (command == 'M') {
                command = 'L';
            }
            else if (command == 'm') {
                command = 'l';
            }
            skipWhitespace();
        }
    }

    /**
     * Parses the arguments of one command and adds the segment.
     * @param command the command
     * @return false in case of an error
     */
    private boolean parseSegment(final char command) {
        final boolean relative = Character.isLowerCase(command);
        final double baseX = relative ? currentX_ : 0;
        final double baseY = relative ? currentY_ : 0;

        switch (Character.toUpperCase(command)) {
            case 'Z':
                path_.closePath();
                setCurrent(startX_, startY_);
                controlX_ = currentX_;
                controlY_ = currentY_;
                return true;

            case 'M': {
                final double[] args = numbers(2);
                if (args == null) {
                    return false;
                }
                setCurrent(baseX + args[0], baseY + args[1]);
                startX_ = currentX_;
                startY_ = currentY_;
                path_.moveTo(currentX_, currentY_);
                break;
            }

            case 'L': {
                final double[] args = numbers(2);
                if (args == null) {
                    return false;
                }
                setCurrent(baseX + args[0], baseY + args[1]);
                path_.lineTo(currentX_, currentY_);
                break;
            }

            case 'H': {
                final double[] args = numbers(1);
                if (args == null) {
                    return false;
                }
                setCurrent(baseX + args[0], currentY_);
                path_.lineTo(currentX_, currentY_);
                break;
            }

            case 'V': {
                final double[] args = numbers(1);
                if (args == null) {
                    return false;
                }
                setCurrent(currentX_, baseY + args[0]);
                path_.lineTo(currentX_, currentY_);
                break;
            }

            case 'C': {
                final double[] args = numbers(6);
                if (args == null) {
                    return false;
                }
                cubic(baseX + args[0], baseY + args[1], baseX + args[2], baseY + args[3],
                        baseX + args[4], baseY + args[5]);
                return true;
            }

            case 'S': {
                final double[] args = numbers(4);
                if (args == null) {
                    return false;
                }
                // the first control point is the reflection of the last one
                cubic(2 * currentX_ - controlX_, 2 * currentY_ - controlY_,
                        baseX + args[0], baseY + args[1], baseX + args[2], baseY + args[3]);
                return true;
            }

            case 'Q': {
                final double[] args = numbers(4);
                if (args == null) {
                    return false;
                }
                quadratic(baseX + args[0], baseY + args[1], baseX + args[2], baseY + args[3]);
                return true;
            }

            case 'T': {
                final double[] args = numbers(2);
                if (args == null) {
                    return false;
                }
                quadratic(2 * currentX_ - controlX_, 2 * currentY_ - controlY_,
                        baseX + args[0], baseY + args[1]);
                return true;
            }

            case 'A':
                return parseArc(baseX, baseY);

            default:
                return false;
        }

        // no curve; the next smooth curve uses the current point as control point
        controlX_ = currentX_;
        controlY_ = currentY_;
        return true;
    }

    private void cubic(final double cp1x, final double cp1y, final double cp2x, final double cp2y,
            final double x, final double y) {
        path_.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
        controlX_ = cp2x;
        controlY_ = cp2y;
        setCurrent(x, y);
    }

    private void quadratic(final double cpx, final double cpy, final double x, final double y) {
        path_.quadraticCurveTo(cpx, cpy, x, y);
        controlX_ = cpx;
        controlY_ = cpy;
        setCurrent(x, y);
    }

    private boolean parseArc(final double baseX, final double baseY) {
        final double[] radii = numbers(3);
        if (radii == null) {
            return false;
        }
        final int largeArc = flag();
        if (largeArc < 0) {
            return false;
        }
        final int sweep = flag();
        if (sweep < 0) {
            return false;
        }
        final double[] end = numbers(2);
        if (end == null) {
            return false;
        }

        arc(Math.abs(radii[0]), Math.abs(radii[1]), Math.toRadians(radii[2]), largeArc == 1, sweep == 1,
                baseX + end[0], baseY + end[1]);
        controlX_ = currentX_;
        controlY_ = currentY_;
        return true;
    }

    /**
     * Converts the endpoint parameterization of an svg arc into the center parameterization
     * used by the canvas ellipse.
     * @see <a href="https://www.w3.org/TR/SVG2/implnote.html#ArcConversionEndpointToCenter">
     *      SVG implementation notes</a>
     */
    private void arc(final double radiusX, final double radiusY, final double angle,
            final boolean largeArc, final boolean sweep, final double x2, final double y2) {
        final double x1 = currentX_;
        final double y1 = currentY_;
        setCurrent(x2, y2);

        if (x1 == x2 && y1 == y2) {
            return;
        }
        if (radiusX == 0 || radiusY == 0) {
            path_.lineTo(x2, y2);
            return;
        }

        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        final double dx = (x1 - x2) / 2;
        final double dy = (y1 - y2) / 2;
        final double x1p = cos * dx + sin * dy;
        final double y1p = -sin * dx + cos * dy;

        // scale up the radii if they are too small
        double rx = radiusX;
        double ry = radiusY;
        final double lambda = x1p * x1p / (rx * rx) + y1p * y1p / (ry * ry);
        if (lambda > 1) {
            rx *= Math.sqrt(lambda);
            ry *= Math.sqrt(lambda);
        }

        final double numerator = rx * rx * ry * ry - rx * rx * y1p * y1p - ry * ry * x1p * x1p;
        final double denominator = rx * rx * y1p * y1p + ry * ry * x1p * x1p;
        double coefficient = Math.sqrt(Math.max(0, numerator / denominator));
        if (largeArc == sweep) {
            coefficient = -coefficient;
        }
        final double cxp = coefficient * rx * y1p / ry;
        final double cyp = -coefficient * ry * x1p / rx;

        final double cx = cos * cxp - sin * cyp + (x1 + x2) / 2;
        final double cy = sin * cxp + cos * cyp + (y1 + y2) / 2;

        final double startAngle = Math.atan2((y1p - cyp) / ry, (x1p - cxp) / rx);
        final double endAngle = Math.atan2((-y1p - cyp) / ry, (-x1p - cxp) / rx);
        double delta = endAngle - startAngle;
        if (sweep && delta < 0) {
            delta += 2 * Math.PI;
        }
        else if (!sweep && delta > 0) {
            delta -= 2 * Math.PI;
        }

        // a positive sweep is clockwise on the screen (the y axis points down)
        path_.ellipse(cx, cy, rx, ry, angle, startAngle, startAngle + delta, !sweep);
    }

    private void setCurrent(final double x, final double y) {
        currentX_ = x;
        currentY_ = y;
    }

    /**
     * Reads the given number of numbers.
     * @param count the number of numbers
     * @return the numbers or null in case of an error
     */
    @SuppressWarnings("PMD.ReturnEmptyCollectionRatherThanNull") // null signals a syntax error
    private double[] numbers(final int count) {
        final double[] result = new double[count];
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                skipCommaWhitespace();
            }
            final double number = number();
            if (Double.isNaN(number)) {
                return null;
            }
            result[i] = number;
        }
        skipCommaWhitespace();
        return result;
    }

    /**
     * Reads a flag of the arc command; flags need no separator.
     * @return 0 or 1 or -1 in case of an error
     */
    private int flag() {
        if (pos_ >= data_.length()) {
            return -1;
        }
        final char c = data_.charAt(pos_);
        if (c != '0' && c != '1') {
            return -1;
        }
        pos_++;
        skipCommaWhitespace();
        return c - '0';
    }

    /**
     * Reads a number.
     * @return the number or NaN in case of an error
     */
    private double number() {
        final int start = pos_;
        int i = pos_;
        final int length = data_.length();

        if (i < length && (data_.charAt(i) == '+' || data_.charAt(i) == '-')) {
            i++;
        }
        final int digitsStart = i;
        while (i < length && isDigit(data_.charAt(i))) {
            i++;
        }
        boolean hasDigits = i > digitsStart;
        if (i < length && data_.charAt(i) == '.') {
            i++;
            final int fractionStart = i;
            while (i < length && isDigit(data_.charAt(i))) {
                i++;
            }
            hasDigits |= i > fractionStart;
        }
        if (!hasDigits) {
            return Double.NaN;
        }

        if (i < length && (data_.charAt(i) == 'e' || data_.charAt(i) == 'E')) {
            int exponent = i + 1;
            if (exponent < length && (data_.charAt(exponent) == '+' || data_.charAt(exponent) == '-')) {
                exponent++;
            }
            if (exponent < length && isDigit(data_.charAt(exponent))) {
                i = exponent;
                while (i < length && isDigit(data_.charAt(i))) {
                    i++;
                }
            }
        }

        pos_ = i;
        final double number = Double.parseDouble(data_.substring(start, i));
        if (Double.isInfinite(number)) {
            return Double.NaN;
        }
        return number;
    }

    private void skipWhitespace() {
        while (pos_ < data_.length() && isWhitespace(data_.charAt(pos_))) {
            pos_++;
        }
    }

    private void skipCommaWhitespace() {
        skipWhitespace();
        if (pos_ < data_.length() && data_.charAt(pos_) == ',') {
            pos_++;
            skipWhitespace();
        }
    }

    private static boolean isWhitespace(final char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f';
    }

    private static boolean isDigit(final char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isNumberStart(final char c) {
        return isDigit(c) || c == '+' || c == '-' || c == '.';
    }

    private static boolean isCommand(final char c) {
        return "MmZzLlHhVvCcSsQqTtAa".indexOf(c) > -1;
    }
}
