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
package org.htmlunit.javascript.host;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.dom.DOMRectReadOnly;

/**
 * Specifies a rectangle that contains a line of text in either an element or a TextRange object.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/DOMRect">DOMRect</a>
 */
@JsxClass
public class DOMRect extends DOMRectReadOnly {

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
    public static DOMRect jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {

        final DOMRect rect = new DOMRect();
        rect.init(args, ctorObj);
        return rect;
    }

    /**
     * Creates an instance.
     */
    public DOMRect() {
        super();
    }

    /**
     * Creates an instance, with the given coordinates.
     *
     * @param x the x coordinate of the rectangle surrounding the object content
     * @param y the y coordinate of the rectangle surrounding the object content
     * @param width the width coordinate of the rectangle surrounding the object content
     * @param height the height of the rectangle surrounding the object content
     */
    public DOMRect(final int x, final int y, final int width, final int height) {
        super(x, y, width, height);
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
    public double getWidth() {
        return super.getWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setWidth(final double width) {
        super.setWidth(width);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public double getHeight() {
        return super.getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setHeight(final double height) {
        super.setHeight(height);
    }
}
