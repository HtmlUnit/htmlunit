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
package org.htmlunit.javascript.host.canvas;

import java.util.List;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.platform.canvas.rendering.RecordedPath;
import org.htmlunit.platform.canvas.rendering.SvgPathDataParser;

/**
 * A JavaScript object for {@code Path2D}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Path2D">MDN Documentation</a>
 * @see <a href="https://html.spec.whatwg.org/multipage/canvas.html#path2d-objects">HTML Living Standard</a>
 */
@JsxClass
public class Path2D extends HtmlUnitScriptable {

    private static final String TYPE = "Path2D";

    private RecordedPath path_ = new RecordedPath();

    /**
     * JavaScript constructor.
     * @param path an other {@code Path2D} to copy, a string with svg path data or undefined
     */
    @JsxConstructor
    public void jsConstructor(final Object path) {
        if (path instanceof Path2D other) {
            path_ = new RecordedPath(other.path_);
        }
        else if (!JavaScriptEngine.isUndefined(path)) {
            SvgPathDataParser.parse(JavaScriptEngine.toString(path), path_);
        }
    }

    /**
     * Returns the recorded path.
     * @return the recorded path
     */
    RecordedPath getRecordedPath() {
        return path_;
    }

    /**
     * Adds the given path to this path.
     * @param path the path to add
     * @param transform the transformation (a DOMMatrix2DInit) to be applied to the added path
     */
    @JsxFunction
    public void addPath(final Object path, final Object transform) {
        if (!(path instanceof Path2D other)) {
            throw JavaScriptEngine.typeError(
                    "Failed to execute 'addPath' on 'Path2D': parameter 1 is not of type 'Path2D'.");
        }

        double a = 1;
        double b = 0;
        double c = 0;
        double d = 1;
        double e = 0;
        double f = 0;
        if (transform instanceof Scriptable matrix) {
            a = matrixValue(matrix, "a", "m11", 1);
            b = matrixValue(matrix, "b", "m12", 0);
            c = matrixValue(matrix, "c", "m21", 0);
            d = matrixValue(matrix, "d", "m22", 1);
            e = matrixValue(matrix, "e", "m41", 0);
            f = matrixValue(matrix, "f", "m42", 0);
        }
        else if (transform != null && !JavaScriptEngine.isUndefined(transform)) {
            throw JavaScriptEngine.typeError("Failed to execute 'addPath' on 'Path2D': "
                    + "The provided value is not of type 'DOMMatrix2DInit'.");
        }

        if (!areFinite(a, b, c, d, e, f)) {
            return;
        }
        path_.addPath(other.path_, a, b, c, d, e, f);
    }

    /**
     * Reads one value of a DOMMatrix2DInit dictionary.
     * @param matrix the dictionary
     * @param name the name (a..f)
     * @param alias the name of the alias (m11..m42)
     * @param defaultValue the value if both are not defined
     * @return the value
     */
    private static double matrixValue(final Scriptable matrix, final String name, final String alias,
            final double defaultValue) {
        final Object value = ScriptableObject.getProperty(matrix, name);
        final Object aliasValue = ScriptableObject.getProperty(matrix, alias);
        final boolean hasValue = isDefined(value);
        final boolean hasAlias = isDefined(aliasValue);

        if (hasValue && hasAlias) {
            final double number = JavaScriptEngine.toNumber(value);
            final double aliasNumber = JavaScriptEngine.toNumber(aliasValue);
            if (number != aliasNumber && !(Double.isNaN(number) && Double.isNaN(aliasNumber))) {
                throw JavaScriptEngine.typeError("Failed to execute 'addPath' on 'Path2D': "
                        + "Property mismatch on matrix initialization.");
            }
            return number;
        }
        if (hasValue) {
            return JavaScriptEngine.toNumber(value);
        }
        if (hasAlias) {
            return JavaScriptEngine.toNumber(aliasValue);
        }
        return defaultValue;
    }

    private static boolean isDefined(final Object value) {
        return value != Scriptable.NOT_FOUND && !JavaScriptEngine.isUndefined(value);
    }

    /**
     * Closes the current sub path.
     */
    @JsxFunction
    public void closePath() {
        path_.closePath();
    }

    /**
     * Starts a new sub path at the given point.
     * @param x the x
     * @param y the y
     */
    @JsxFunction
    public void moveTo(final double x, final double y) {
        if (areFinite(x, y)) {
            path_.moveTo(x, y);
        }
    }

    /**
     * Connects the last point to the given point.
     * @param x the x
     * @param y the y
     */
    @JsxFunction
    public void lineTo(final double x, final double y) {
        if (areFinite(x, y)) {
            path_.lineTo(x, y);
        }
    }

    /**
     * Adds a cubic Bézier curve.
     * @param cp1x the cp1x
     * @param cp1y the cp1y
     * @param cp2x the cp2x
     * @param cp2y the cp2y
     * @param x the x
     * @param y the y
     */
    @JsxFunction
    public void bezierCurveTo(final double cp1x, final double cp1y, final double cp2x, final double cp2y,
            final double x, final double y) {
        if (areFinite(cp1x, cp1y, cp2x, cp2y, x, y)) {
            path_.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y);
        }
    }

    /**
     * Adds a quadratic Bézier curve.
     * @param cpx the cpx
     * @param cpy the cpy
     * @param x the x
     * @param y the y
     */
    @JsxFunction
    public void quadraticCurveTo(final double cpx, final double cpy, final double x, final double y) {
        if (areFinite(cpx, cpy, x, y)) {
            path_.quadraticCurveTo(cpx, cpy, x, y);
        }
    }

    /**
     * Adds an arc.
     * @param x the x
     * @param y the y
     * @param radius the radius
     * @param startAngle the start angle
     * @param endAngle the end angle
     * @param anticlockwise is anti-clockwise
     */
    @JsxFunction
    public void arc(final double x, final double y, final double radius, final double startAngle,
            final double endAngle, final boolean anticlockwise) {
        if (!areFinite(x, y, radius, startAngle, endAngle)) {
            return;
        }
        checkRadius(this, "arc", TYPE, radius);
        path_.ellipse(x, y, radius, radius, 0, startAngle, endAngle, anticlockwise);
    }

    /**
     * Adds an arc with the given control points and radius.
     * @param x1 the x1
     * @param y1 the y1
     * @param x2 the x2
     * @param y2 the y2
     * @param radius the radius
     */
    @JsxFunction
    public void arcTo(final double x1, final double y1, final double x2, final double y2, final double radius) {
        if (!areFinite(x1, y1, x2, y2, radius)) {
            return;
        }
        checkRadius(this, "arcTo", TYPE, radius);
        path_.arcTo(x1, y1, x2, y2, radius);
    }

    /**
     * Adds an elliptical arc.
     * @param x the x
     * @param y the y
     * @param radiusX the radiusX
     * @param radiusY the radiusY
     * @param rotation the rotation
     * @param startAngle the startAngle
     * @param endAngle the endAngle
     * @param anticlockwise is anti-clockwise
     */
    @JsxFunction
    public void ellipse(final double x, final double y, final double radiusX, final double radiusY,
            final double rotation, final double startAngle, final double endAngle, final boolean anticlockwise) {
        if (!areFinite(x, y, radiusX, radiusY, rotation, startAngle, endAngle)) {
            return;
        }
        checkRadius(this, "ellipse", TYPE, radiusX, "major-axis");
        checkRadius(this, "ellipse", TYPE, radiusY, "minor-axis");
        path_.ellipse(x, y, radiusX, radiusY, rotation, startAngle, endAngle, anticlockwise);
    }

    /**
     * Adds a rectangle.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    @JsxFunction
    public void rect(final double x, final double y, final double w, final double h) {
        if (areFinite(x, y, w, h)) {
            path_.rect(x, y, w, h);
        }
    }

    /**
     * Adds a rectangle with rounded corners.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     * @param radii a radius, a DOMPointInit or an array of up to four of them
     */
    @JsxFunction
    public void roundRect(final double x, final double y, final double w, final double h, final Object radii) {
        roundRect(path_, TYPE, x, y, w, h, radii);
    }

    /**
     * Implementation of {@code roundRect()} for the {@code Path2D} and the canvas.
     * @param path the path to add the round rect to
     * @param type the name of the type for error messages
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     * @param radii a radius, a DOMPointInit or an array of up to four of them
     */
    static void roundRect(final RecordedPath path, final String type,
            final double x, final double y, final double w, final double h, final Object radii) {
        if (!areFinite(x, y, w, h)) {
            return;
        }

        final Object[] radiiList;
        if (radii == null || JavaScriptEngine.isUndefined(radii)) {
            radiiList = new Object[] {0};
        }
        else if (radii instanceof List<?> list) {
            radiiList = list.toArray();
        }
        else {
            radiiList = new Object[] {radii};
        }

        if (radiiList.length < 1 || radiiList.length > 4) {
            throw JavaScriptEngine.rangeError("Failed to execute 'roundRect' on '" + type + "': "
                    + radiiList.length + " radii provided. Between one and four radii are necessary.");
        }

        // x and y radius for each given value
        final double[] values = new double[radiiList.length * 2];
        for (int i = 0; i < radiiList.length; i++) {
            final Object radius = radiiList[i];
            final double rx;
            final double ry;
            if (radius instanceof Scriptable point && !(radius instanceof List<?>)) {
                rx = toNumber(ScriptableObject.getProperty(point, "x"));
                ry = toNumber(ScriptableObject.getProperty(point, "y"));
            }
            else {
                rx = JavaScriptEngine.toNumber(radius);
                ry = rx;
            }

            if (!areFinite(rx, ry)) {
                return;
            }
            if (rx < 0 || ry < 0) {
                throw JavaScriptEngine.rangeError("Failed to execute 'roundRect' on '" + type + "': "
                        + "Radius value " + JavaScriptEngine.toString(rx < 0 ? rx : ry) + " is negative.");
            }
            values[i * 2] = rx;
            values[i * 2 + 1] = ry;
        }

        // the index (of the given values) for the upper left, upper right, lower right and lower left corner
        final int[] corners = switch (radiiList.length) {
            case 1 -> new int[] {0, 0, 0, 0};
            case 2 -> new int[] {0, 1, 0, 1};
            case 3 -> new int[] {0, 1, 2, 1};
            default -> new int[] {0, 1, 2, 3};
        };
        final double[] cornerRadii = new double[8];
        for (int i = 0; i < corners.length; i++) {
            cornerRadii[i * 2] = values[corners[i] * 2];
            cornerRadii[i * 2 + 1] = values[corners[i] * 2 + 1];
        }

        path.roundRect(x, y, w, h, cornerRadii);
    }

    private static double toNumber(final Object value) {
        if (value == Scriptable.NOT_FOUND || JavaScriptEngine.isUndefined(value)) {
            return 0;
        }
        return JavaScriptEngine.toNumber(value);
    }

    /**
     * Throws an IndexSizeError if the radius is negative.
     * @param scriptable the scriptable used to create the exception
     * @param method the name of the method for the error message
     * @param type the name of the type for the error message
     * @param radius the radius
     */
    static void checkRadius(final HtmlUnitScriptable scriptable, final String method, final String type,
            final double radius) {
        if (radius < 0) {
            throw JavaScriptEngine.asJavaScriptException(scriptable,
                    "Failed to execute '" + method + "' on '" + type + "': The radius provided ("
                            + JavaScriptEngine.toString(radius) + ") is negative.",
                    DOMException.INDEX_SIZE_ERR);
        }
    }

    /**
     * Throws an IndexSizeError if the radius of the ellipse is negative.
     * @param scriptable the scriptable used to create the exception
     * @param method the name of the method for the error message
     * @param type the name of the type for the error message
     * @param radius the radius
     * @param axis the name of the axis for the error message
     */
    static void checkRadius(final HtmlUnitScriptable scriptable, final String method, final String type,
            final double radius, final String axis) {
        if (radius < 0) {
            throw JavaScriptEngine.asJavaScriptException(scriptable,
                    "Failed to execute '" + method + "' on '" + type + "': The " + axis + " radius provided ("
                            + JavaScriptEngine.toString(radius) + ") is negative.",
                    DOMException.INDEX_SIZE_ERR);
        }
    }

    /**
     * Checks whether all values are finite.
     * @param values the values to check
     * @return true if all values are finite
     */
    static boolean areFinite(final double... values) {
        for (final double value : values) {
            if (!Double.isFinite(value)) {
                return false;
            }
        }
        return true;
    }
}
