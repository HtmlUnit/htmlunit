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
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for {@code DOMPoint}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class DOMPoint extends DOMPointReadOnly {

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static DOMPoint jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {

        final DOMPoint point = new DOMPoint();
        point.init(args, ctorObj);
        return point;
    }

    /**
     * Creates an instance.
     */
    public DOMPoint() {
        super();
    }

    /**
     * Creates an instance with the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param w the w perspective value
     */
    public DOMPoint(final double x, final double y, final double z, final double w) {
        super(x, y, z, w);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getX() {
        return super.getX();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setX(final double x) {
        super.setX(x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getY() {
        return super.getY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setY(final double y) {
        super.setY(y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getZ() {
        return super.getZ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setZ(final double z) {
        super.setZ(z);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getW() {
        return super.getW();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setW(final double w) {
        super.setW(w);
    }
}
