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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for {@code DOMPointReadOnly}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class DOMPointReadOnly extends HtmlUnitScriptable {

    private double xVal_;
    private double yVal_;
    private double zVal_;
    private double wVal_;

    /**
     * Creates an instance.
     */
    public DOMPointReadOnly() {
        // default ctor.
    }

    /**
     * Creates an instance with the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param w the w perspective value
     */
    public DOMPointReadOnly(final double x, final double y, final double z, final double w) {
        xVal_ = x;
        yVal_ = y;
        zVal_ = z;
        wVal_ = w;
    }

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments to the constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static DOMPointReadOnly jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {

        final DOMPointReadOnly point = new DOMPointReadOnly(0, 0, 0, 1);
        point.init(args, ctorObj);
        return point;
    }

    protected void init(final Object[] args, final Function ctorObj) {
        final Window window = getWindow(ctorObj);
        setParentScope(window);
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
            zVal_ = JavaScriptEngine.toNumber(args[2]);
        }

        if (args.length > 3) {
            wVal_ = JavaScriptEngine.toNumber(args[3]);
        }
    }

    /**
     * @return x coordinate
     */
    @JsxGetter
    public double getX() {
        return xVal_;
    }

    /**
     * @param x the new value
     */
    public void setX(final double x) {
        xVal_ = x;
    }

    /**
     * @return y coordinate
     */
    @JsxGetter
    public double getY() {
        return yVal_;
    }

    /**
     * @param y the new value
     */
    public void setY(final double y) {
        yVal_ = y;
    }

    /**
     * @return z coordinate
     */
    @JsxGetter
    public double getZ() {
        return zVal_;
    }

    /**
     * @param z the new value
     */
    public void setZ(final double z) {
        zVal_ = z;
    }

    /**
     * @return w perspective value
     */
    @JsxGetter
    public double getW() {
        return wVal_;
    }

    /**
     * @param w the new value
     */
    public void setW(final double w) {
        wVal_ = w;
    }

    /**
     * @return a JSON representation of the DOMPointReadOnly object.
     */
    @JsxFunction
    public Scriptable toJSON() {
        final Scriptable json = JavaScriptEngine.newObject(getParentScope());
        json.put("x", json, xVal_);
        json.put("y", json, yVal_);
        json.put("z", json, zVal_);
        json.put("w", json, wVal_);
        return json;
    }
}
