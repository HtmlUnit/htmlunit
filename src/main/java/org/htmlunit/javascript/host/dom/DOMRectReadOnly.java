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

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code DOMRectReadOnly}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/DOMRectReadOnly">MDN Documentation</a>
 */
@JsxClass
public class DOMRectReadOnly extends HtmlUnitScriptable {

    // private static final Log LOG = LogFactory.getLog(DOMRectReadOnly.class);

    private double xVal_;
    private double yVal_;
    private double width_;
    private double height_;

    /**
     * Creates an instance.
     */
    public DOMRectReadOnly() {
        // default ctor.
    }

    /**
     * Creates an instance, with the given coordinates.
     *
     * @param x the x coordinate of the rectangle surrounding the object content
     * @param y the y coordinate of the rectangle surrounding the object content
     * @param width the width coordinate of the rectangle surrounding the object content
     * @param height the height of the rectangle surrounding the object content
     */
    public DOMRectReadOnly(final double x, final double y, final double width, final double height) {
        xVal_ = x;
        yVal_ = y;
        width_ = width;
        height_ = height;
    }

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments to the DOMRectReadOnly constructor
     * @param ctorObj the function object
     * @param inNewExpr {@code true} if invoked with the {@code new} operator
     * @return the Java object that JavaScript can access
     */
    @JsxConstructor
    public static DOMRectReadOnly jsConstructor(final Context cx, final VarScope scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {

        final DOMRectReadOnly rect = new DOMRectReadOnly(0, 0, 0, 0);
        rect.init(args, scope, ctorObj);
        return rect;
    }

    protected void init(final Object[] args, final VarScope scope, final Function ctorObj) {
        setParentScope(scope);
        setPrototype(((FunctionObject) ctorObj).getClassPrototype());

        if (args.length == 0 || JavaScriptEngine.isUndefined(args[0])) {
            return;
        }

        if (args.length > 0) {
            xVal_ = JavaScriptEngine.toNumber(args[0]);
        }

        if (args.length > 1) {
            yVal_ = JavaScriptEngine.toNumber(args[1]);
        }

        if (args.length > 2) {
            width_ = JavaScriptEngine.toNumber(args[2]);
        }

        if (args.length > 3) {
            height_ = JavaScriptEngine.toNumber(args[3]);
        }
    }

    /**
     * Returns the {@code x} coordinate.
     * @return the {@code x} coordinate
     */
    @JsxGetter
    public double getX() {
        return xVal_;
    }

    /**
     * Sets the {@code x} coordinate.
     * @param x the new value
     */
    public void setX(final double x) {
        xVal_ = x;
    }

    /**
     * Returns the {@code y} coordinate.
     * @return the {@code y} coordinate
     */
    @JsxGetter
    public double getY() {
        return yVal_;
    }

    /**
     * Sets the {@code y} coordinate.
     * @param y the new value
     */
    public void setY(final double y) {
        yVal_ = y;
    }

    /**
     * Returns the {@code width}.
     * @return the {@code width}
     */
    @JsxGetter
    public double getWidth() {
        return width_;
    }

    /**
     * Sets the {@code width}.
     * @param width the new value
     */
    public void setWidth(final double width) {
        width_ = width;
    }

    /**
     * Returns the {@code height}.
     * @return the {@code height}
     */
    @JsxGetter
    public double getHeight() {
        return height_;
    }

    /**
     * Sets the {@code height}.
     * @param height the new value
     */
    public void setHeight(final double height) {
        height_ = height;
    }

    /**
     * Returns the {@code top} coordinate.
     * @return the {@code top} coordinate
     */
    @JsxGetter
    public double getTop() {
        return Math.min(getY(), getY() + getHeight());
    }

    /**
     * Returns the {@code right} coordinate.
     * @return the {@code right} coordinate
     */
    @JsxGetter
    public double getRight() {
        return Math.max(getX(), getX() + getWidth());
    }

    /**
     * Returns the {@code bottom} coordinate.
     * @return the {@code bottom} coordinate
     */
    @JsxGetter
    public double getBottom() {
        return Math.max(getY(), getY() + getHeight());
    }

    /**
     * Returns the {@code left} coordinate.
     * @return the {@code left} coordinate
     */
    @JsxGetter
    public double getLeft() {
        return Math.min(getX(), getX() + getWidth());
    }

    /**
     * Returns a JSON representation of the DOMRectReadOnly object.
     * @return a JSON representation of the DOMRectReadOnly object
     */
    @JsxFunction
    public Scriptable toJSON() {
        final Scriptable json = JavaScriptEngine.newObject(getParentScope());
        json.put("x", json, xVal_);
        json.put("y", json, yVal_);
        json.put("width", json, width_);
        json.put("height", json, height_);

        json.put("top", json, getTop());
        json.put("right", json, getRight());
        json.put("bottom", json, getBottom());
        json.put("left", json, getLeft());

        return json;
    }
}
